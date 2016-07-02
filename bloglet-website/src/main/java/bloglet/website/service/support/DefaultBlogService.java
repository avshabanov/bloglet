package bloglet.website.service.support;

import bloglet.model.Bloglet;
import bloglet.model.BlogletDb;
import bloglet.website.dao.BlogEntryDao;
import bloglet.website.dao.TagEntryDao;
import bloglet.website.service.BlogService;
import com.truward.tupl.support.AbstractTuplDaoSupport;
import com.truward.tupl.support.exception.InternalErrorDaoException;
import com.truward.tupl.support.exception.KeyNotFoundException;
import com.truward.tupl.support.id.Key;
import com.truward.tupl.support.map.PersistentMapDao;
import com.truward.tupl.support.transaction.TuplTransactionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.annotation.Nonnull;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public final class DefaultBlogService extends AbstractTuplDaoSupport implements BlogService {
  private final Logger log = LoggerFactory.getLogger(getClass());

  private final BlogEntryDao blogEntryDao;
  private final TagEntryDao tagEntryDao;
  private final PersistentMapDao<String> blogEntryTimeIndexDao;

  public DefaultBlogService(@Nonnull TuplTransactionManager txManager,
                            @Nonnull BlogEntryDao blogEntryDao,
                            @Nonnull TagEntryDao tagEntryDao) {
    super(txManager);
    this.blogEntryDao = Objects.requireNonNull(blogEntryDao, "blogEntryDao");
    this.tagEntryDao = Objects.requireNonNull(tagEntryDao, "tagEntryDao");

    // internal DAO, for storing time indexes for blog entries to support fast queries of blog entries,
    // uniformly ordered by creation date.
    // this can be moved to DI, but this would probably be useless as this is implementation detail for this service
    this.blogEntryTimeIndexDao = PersistentMapDao.newStringMap(txManager, "BlogEntryTime");
  }

  @Nonnull
  @Override
  public List<Bloglet.BlogPost> getBlogPosts(int offset, int limit) {
    final int actualLimit = checkOffsetAndLimit(offset, limit);
    return withTransaction(tx -> {
      // use time index to query blog posts ordered by date in descending order
      final List<Map.Entry<Key, BlogletDb.BlogEntry>> blogPostEntries = blogEntryTimeIndexDao
          .getEntries(offset, actualLimit, false)
          .stream()
          .map(entry -> {
            final Key blogEntryKey = Key.fromBase64(entry.getValue());
            final BlogletDb.BlogEntry blogEntry = blogEntryDao.get(blogEntryKey);
            if (blogEntry == null) {
              throw new InternalErrorDaoException("BlogEntryTime index is broken, unknown key=" + blogEntryKey);
            }
            return new AbstractMap.SimpleImmutableEntry<>(blogEntryKey, blogEntry);
          })
          .collect(Collectors.toList());

      return getPostByEntries(blogPostEntries);
    });
  }

  @Nonnull
  @Override
  public List<Bloglet.Tag> getTags() {
    final List<Map.Entry<Key, BlogletDb.TagEntry>> entries = new ArrayList<>(20);
    for (Key offset = null;;) {
      final List<Map.Entry<Key, BlogletDb.TagEntry>> tmpEntries = tagEntryDao.getEntries(offset, MAX_LIMIT);
      entries.addAll(tmpEntries);
      if (tmpEntries.size() < MAX_LIMIT) {
        break;
      }
      offset = tmpEntries.get(tmpEntries.size() - 1).getKey();
    }

    return entries
        .stream()
        .map(entry -> Bloglet.Tag.newBuilder()
            .setId(entry.getKey().toBase64())
            .setName(entry.getValue().getName())
            .build())
        .collect(Collectors.toList());
  }

  @Nonnull
  @Override
  public List<Bloglet.BlogPost> getBlogPostsForTag(@Nonnull String tagId, int offset, int limit) {
    final int actualLimit = checkOffsetAndLimit(offset, limit);
    return withTransaction(tx -> {
      final BlogletDb.TagEntry tagEntry = tagEntryDao.get(Key.fromBase64(tagId));
      if (tagEntry == null) {
        throw new KeyNotFoundException("Unknown tag with id=" + tagId);
      }

      final List<Map.Entry<Key, BlogletDb.BlogEntry>> entries = tagEntry.getBlogEntryIdsList()
          .stream()
          .map(id -> new AbstractMap.SimpleImmutableEntry<>(Key.fromBase64(id), blogEntryDao.get(Key.fromBase64(id))))
          .sorted((lhs, rhs) -> Long.compare(rhs.getValue().getDateCreated(), lhs.getValue().getDateCreated()))
          .skip(offset)
          .limit(actualLimit)
          .collect(Collectors.toList());

      return getPostByEntries(entries);
    });
  }

  @Nonnull
  @Override
  public Bloglet.BlogPost getBlogPost(@Nonnull String blogPostId) {
    final BlogletDb.BlogEntry blogEntry = blogEntryDao.get(Key.fromBase64(blogPostId));
    if (blogEntry == null) {
      throw new KeyNotFoundException("Unknown blogPostId=" + blogPostId);
    }
    return getPostFromEntry(Key.fromBase64(blogPostId), blogEntry);
  }

  @Nonnull
  @Override
  public Bloglet.Tag getTag(@Nonnull String tagId) {
    final BlogletDb.TagEntry tagEntry = tagEntryDao.get(Key.fromBase64(tagId));
    if (tagEntry == null) {
      throw new KeyNotFoundException("Unknown tagId=" + tagId);
    }
    return Bloglet.Tag.newBuilder().setId(tagId).setName(tagEntry.getName()).build();
  }

  @Nonnull
  @Override
  public String addTag(@Nonnull String tagName) {
    if (!StringUtils.hasLength(tagName)) {
      throw new IllegalArgumentException("Invalid tagName={}" + tagName);
    }

    return withTransaction(tx -> {
      final Key id = Key.random();
      tagEntryDao.put(id, BlogletDb.TagEntry.newBuilder().setName(tagName).build());
      return id.toBase64();
    });
  }

  @Nonnull
  @Override
  public String addBlogEntry(@Nonnull BlogletDb.BlogEntry blogEntry) {
    return withTransaction(tx -> {
      final Key id = Key.random();

      // Consistency check: tags in this blog entry should exist
      // Also: maintaining n-m relationship between blog posts and tags
      for (final String tagId : blogEntry.getTagIdsList()) {
        final Key tagIdKey = Key.fromBase64(tagId);
        final BlogletDb.TagEntry tagEntry = tagEntryDao.get(tagIdKey);
        if (tagEntry == null) {
          throw new KeyNotFoundException("Unknown tag with id=" + tagId);
        }

        // update tag->blog entry relationship
        tagEntryDao.put(tagIdKey, BlogletDb.TagEntry.newBuilder(tagEntry)
            .addBlogEntryIds(id.toBase64())
            .build());
      }

      // Persist given entry to DB
      blogEntryDao.put(id, blogEntry);

      final String idStr = id.toBase64();
      blogEntryTimeIndexDao.put(getBlogEntryTimeKey(id, blogEntry), idStr);
      return idStr;
    });
  }

  //
  // Private
  //

  @Nonnull
  private static Key getBlogEntryTimeKey(@Nonnull Key id, @Nonnull BlogletDb.BlogEntry blogEntry) {
    try (final ByteArrayOutputStream baos = new ByteArrayOutputStream(17 + id.getByteSize())) {
      try (final DataOutputStream dos = new DataOutputStream(baos)) {
        dos.writeLong(blogEntry.getDateCreated());
        dos.writeByte('-');
        dos.write(id.getBytesNoCopy());
      }

      return Key.inplace(baos.toByteArray());
    } catch (IOException e) {
      throw new IllegalStateException(e); // should never happen
    }
  }

  @Nonnull
  private List<Bloglet.BlogPost> getPostByEntries(@Nonnull List<Map.Entry<Key, BlogletDb.BlogEntry>> entries) {
    return entries.stream().map(entry -> getPostFromEntry(entry.getKey(), entry.getValue()))
        .collect(Collectors.toList());
  }

  @Nonnull
  private Bloglet.BlogPost getPostFromEntry(@Nonnull Key id, @Nonnull BlogletDb.BlogEntry blogEntry) {
    return Bloglet.BlogPost.newBuilder()
        .setId(id.toBase64())
        .setContents(blogEntry)
        .addAllTags(toTags(blogEntry.getTagIdsList()))
        .build();
  }

  private int checkOffsetAndLimit(int offset, int limit) {
    if (offset < 0) {
      throw new IllegalArgumentException("offset < 0");
    }

    if (limit < 0) {
      throw new IllegalArgumentException("limit < 0");
    }

    final int actualLimit;
    if (limit > MAX_LIMIT) {
      actualLimit = MAX_LIMIT;
      log.debug("Too big limit={}", limit);
    } else {
      actualLimit = limit;
    }

    return actualLimit;
  }

  @Nonnull
  private List<Bloglet.Tag> toTags(@Nonnull List<String> tagIds) {
    return tagIds
        .stream()
        .map(tagId -> {
          final BlogletDb.TagEntry tagEntry = tagEntryDao.get(Key.fromBase64(tagId));
          if (tagEntry == null) {
            throw new KeyNotFoundException("Unknown tag with id=" + tagId);
          }
          return Bloglet.Tag.newBuilder().setId(tagId).setName(tagEntry.getName()).build();
        })
        .collect(Collectors.toList());
  }
}

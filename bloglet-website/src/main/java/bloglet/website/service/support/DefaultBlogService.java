package bloglet.website.service.support;

import bloglet.model.Bloglet;
import bloglet.model.BlogletDb;
import bloglet.website.dao.BlogEntryDao;
import bloglet.website.dao.TagEntryDao;
import bloglet.website.service.BlogService;
import com.truward.tupl.support.AbstractTuplDaoSupport;
import com.truward.tupl.support.exception.KeyNotFoundException;
import com.truward.tupl.support.id.IdSupport;
import com.truward.tupl.support.transaction.TuplTransactionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public final class DefaultBlogService extends AbstractTuplDaoSupport implements BlogService, IdSupport {
  private final Logger log = LoggerFactory.getLogger(getClass());

  private final BlogEntryDao blogEntryDao;
  private final TagEntryDao tagEntryDao;

  public DefaultBlogService(@Nonnull TuplTransactionManager txManager,
                            @Nonnull BlogEntryDao blogEntryDao,
                            @Nonnull TagEntryDao tagEntryDao) {
    super(txManager);
    this.blogEntryDao = Objects.requireNonNull(blogEntryDao, "blogEntryDao");
    this.tagEntryDao = Objects.requireNonNull(tagEntryDao, "tagEntryDao");
  }

  @Nonnull
  @Override
  public List<Bloglet.BlogPost> getBlogPosts(int offset, int limit) {
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

    return withTransaction(tx -> {
      final List<Map.Entry<String, BlogletDb.BlogEntry>> entries = blogEntryDao.getEntries(offset, actualLimit);
      return entries
          .stream()
          .map(entry -> Bloglet.BlogPost.newBuilder()
              .setId(entry.getKey())
              .setContents(entry.getValue())
              .addAllTags(toTags(entry.getValue().getTagIdsList()))
              .build())
          .collect(Collectors.toList());
    });
  }

  @Nonnull
  @Override
  public String addTag(@Nonnull String tagName) {
    if (!StringUtils.hasLength(tagName)) {
      throw new IllegalArgumentException("Invalid tagName={}" + tagName);
    }

    return withTransaction(tx -> {
      final String id = generateId();
      tagEntryDao.put(id, BlogletDb.TagEntry.newBuilder().setName(tagName).build());
      return id;
    });
  }

  @Nonnull
  @Override
  public String addBlogEntry(@Nonnull BlogletDb.BlogEntry blogEntry) {
    return withTransaction(tx -> {
      final String id = generateId();

      // Consistency check: tags in this blog entry should exist
      for (final String tagId : blogEntry.getTagIdsList()) {
        if (tagEntryDao.get(tagId) == null) {
          throw new KeyNotFoundException("Unknown tag with id=" + tagId);
        }
      }

      // Persist given entry to DB
      blogEntryDao.put(id, blogEntry);
      return id;
    });
  }

  //
  // Private
  //

  @Nonnull
  private List<Bloglet.Tag> toTags(@Nonnull List<String> tagIds) {
    return tagIds
        .stream()
        .map(tagId -> {
          final BlogletDb.TagEntry tagEntry = tagEntryDao.get(tagId);
          if (tagEntry == null) {
            throw new KeyNotFoundException("Unknown tag with id=" + tagId);
          }
          return Bloglet.Tag.newBuilder().setId(tagId).setName(tagEntry.getName()).build();
        })
        .collect(Collectors.toList());
  }
}

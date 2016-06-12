package bloglet.website.service;

import bloglet.model.Bloglet;
import bloglet.model.BlogletDb;
import bloglet.website.dao.support.DefaultBlogEntryDao;
import bloglet.website.dao.support.DefaultTagEntryDao;
import bloglet.website.service.support.DefaultBlogService;
import com.truward.tupl.support.transaction.TuplTransactionManager;
import com.truward.tupl.support.transaction.support.StandardTuplTransactionManager;
import org.cojen.tupl.Database;
import org.cojen.tupl.DatabaseConfig;
import org.cojen.tupl.DurabilityMode;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link BlogService}.
 *
 * @author Alexander Shabanov
 */
public final class BlogServiceTest {
  private BlogService blogService;

  @Before
  public void init() throws IOException {
    final TuplTransactionManager txManager = new StandardTuplTransactionManager(Database.open(new DatabaseConfig()
        .baseFilePath(File.createTempFile("Test-", "-db").getPath())
        .maxCacheSize(100_000L)
        .durabilityMode(DurabilityMode.NO_SYNC)));

    blogService = new DefaultBlogService(txManager,
        new DefaultBlogEntryDao(txManager), new DefaultTagEntryDao(txManager));
  }

  @Test
  public void shouldGetBlogPosts() {
    // Given:
    final String tagId = blogService.addTag("untagged");
    final String blogEntryId = blogService.addBlogEntry(BlogletDb.BlogEntry.newBuilder()
        .setDateCreated(1L)
        .setDateUpdated(2L)
        .setTitle("First Blog Post")
        .setShortContents("Lorem ipsum")
        .setContents("Lorem ipsum dolorem sic amet")
        .addTagIds(tagId)
        .build());

    // When:
    final List<Bloglet.BlogPost> posts = blogService.getBlogPosts(0, 10);

    // Then:
    assertEquals(Collections.singletonList(blogEntryId),
        posts.stream().map(Bloglet.BlogPost::getId).collect(Collectors.toList()));
  }
}

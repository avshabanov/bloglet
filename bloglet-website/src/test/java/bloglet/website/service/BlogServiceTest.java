package bloglet.website.service;

import bloglet.model.Bloglet;
import bloglet.model.BlogletDb;
import bloglet.website.dao.support.DefaultBlogEntryDao;
import bloglet.website.dao.support.DefaultTagEntryDao;
import bloglet.website.init.DemoInitializer;
import bloglet.website.service.support.DefaultBlogService;
import com.truward.tupl.support.transaction.TuplTransactionManager;
import com.truward.tupl.support.transaction.support.StandardTuplTransactionManager;
import com.truward.tupl.test.TuplTestUtil;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link BlogService}.
 *
 * @author Alexander Shabanov
 */
public final class BlogServiceTest {
  private BlogService blogService;

  @Before
  public void init() throws IOException {
    final TuplTransactionManager txManager = new StandardTuplTransactionManager(TuplTestUtil.createTempDatabase());
    blogService = new DefaultBlogService(txManager,
        new DefaultBlogEntryDao(txManager), new DefaultTagEntryDao(txManager));
  }

  @Test
  public void shouldGetNoPostsForEmptyDb() {
    assertEquals(Collections.emptyList(), blogService.getBlogPosts(0, 10));
  }

  @Test
  public void shouldGetNoPostsByTag() {
    final String tagId = blogService.addTag("untagged");
    assertEquals(Collections.emptyList(), blogService.getBlogPostsForTag(tagId, 0, 10));
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
    assertEquals(posts, blogService.getBlogPostsForTag(tagId, 0, 10));
  }

  @Test
  public void shouldGetPaginatedBlogPosts() {
    // Given:
    final int limit = 4;
    final DemoInitializer demoInitializer = new DemoInitializer(blogService);
    demoInitializer.init();

    // When:
    final List<Bloglet.Tag> tags = blogService.getTags();

    // Then:
    assertEquals(4, tags.size());

    final List<Bloglet.BlogPost> posts = new ArrayList<>();
    for (;;) {
      final List<Bloglet.BlogPost> tmpPosts = blogService.getBlogPosts(posts.size(), limit);
      posts.addAll(tmpPosts);
      if (tmpPosts.size() < limit) {
        break;
      }
      assertEquals(limit, tmpPosts.size());
    }

    // check all the returned posts are sorted by DateCreated
    assertEquals(14, posts.size());
    assertSortedByCreateDate(posts);

    // check post retrieval
    for (final Bloglet.BlogPost post : posts) {
      assertEquals(post, blogService.getBlogPost(post.getId()));
    }

    // retrieve all the posts associated with software tag
    final String softwareTagId = tags.stream().filter(t -> t.getName().equals("software")).findFirst().get().getId();
    final List<Bloglet.BlogPost> softwarePosts = blogService.getBlogPostsForTag(softwareTagId, 0, limit);
    assertEquals(limit, softwarePosts.size());
    final List<Bloglet.BlogPost> softwarePosts2 = blogService.getBlogPostsForTag(softwareTagId, limit, limit);
    assertEquals(2, softwarePosts2.size());

    // check posts associated with tag are sorted by DateCreated
    final List<Bloglet.BlogPost> allSoftwarePosts = new ArrayList<>();
    allSoftwarePosts.addAll(softwarePosts);
    allSoftwarePosts.addAll(softwarePosts2);
    assertSortedByCreateDate(allSoftwarePosts);
  }

  //
  // Private
  //

  private static void assertSortedByCreateDate(@Nonnull List<Bloglet.BlogPost> blogPosts) {
    for (int i = 0; i < blogPosts.size(); ++i) {
      if (i > 0) {
        assertTrue("DateCreated of post #" + i + " is not smaller than of previous post",
            blogPosts.get(i - 1).getContents().getDateCreated() >= blogPosts.get(i).getContents().getDateCreated());
      }
    }
  }
}

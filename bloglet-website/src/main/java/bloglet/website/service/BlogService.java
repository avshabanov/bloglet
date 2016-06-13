package bloglet.website.service;

import bloglet.model.Bloglet;
import bloglet.model.BlogletDb;

import javax.annotation.Nonnull;
import java.util.List;

public interface BlogService {
  int MAX_LIMIT = 4;

  /**
   * Queries all blog entries.
   *
   * @param offset Offset in the resultant set
   * @param limit Size limit of the returned list
   * @return Blog posts, sorted by creation date
   */
  @Nonnull
  List<Bloglet.BlogPost> getBlogPosts(int offset, int limit);

  /**
   * Queries blog entries, associated with a given tag.
   *
   * @param tagId Unique ID of tag, associated with a given post
   * @param offset Offset in the resultant set
   * @param limit Size limit of the returned list
   * @return Blog posts, sorted by creation date
   */
  @Nonnull
  List<Bloglet.BlogPost> getBlogPostsForTag(@Nonnull String tagId, int offset, int limit);

  /**
   * @param blogPostId Unique ID, associated with a blog post (must exist)
   * @return Corresponding blog post
   */
  @Nonnull
  Bloglet.BlogPost getBlogPost(@Nonnull String blogPostId);

  /**
   * @return All the tags
   */
  @Nonnull
  List<Bloglet.Tag> getTags();

  @Nonnull
  Bloglet.Tag getTag(@Nonnull String tagId);

  @Nonnull
  String addTag(@Nonnull String tagName);

  @Nonnull
  String addBlogEntry(@Nonnull BlogletDb.BlogEntry blogEntry);
}

package bloglet.website.service;

import bloglet.model.Bloglet;
import bloglet.model.BlogletDb;

import javax.annotation.Nonnull;
import java.util.List;

public interface BlogService {
  int MAX_LIMIT = 4;

  @Nonnull
  List<Bloglet.BlogPost> getBlogPosts(int offset, int limit);

  @Nonnull
  String addTag(@Nonnull String tagName);

  @Nonnull
  String addBlogEntry(@Nonnull BlogletDb.BlogEntry blogEntry);
}

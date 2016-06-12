package bloglet.website.controller;

import bloglet.model.Bloglet;
import bloglet.website.service.BlogService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/g/part")
public final class PagePartController {

  /**
   * Default value for limit parameter.
   * NOTE: Can't use Integer.toString here as it makes value non-constant.
   */
  private static final String DEFAULT_LIMIT_VAL = "" + BlogService.MAX_LIMIT;

  @Resource BlogService blogService;

  @RequestMapping("/posts")
  public ModelAndView getPosts(@RequestParam("offset") int offset,
                               @RequestParam(value = "limit", defaultValue = DEFAULT_LIMIT_VAL) int limit) {
    final List<Bloglet.BlogPost> posts = blogService.getBlogPosts(offset, limit);
    return new ModelAndView("part/posts", "posts", posts);
  }
}

package bloglet.website.controller;

import bloglet.model.Bloglet;
import bloglet.website.service.BlogService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/g")
public final class PublicPageController {
  @Resource BlogService blogService;

  @RequestMapping("/login")
  public String login() {
    return "page/login";
  }

  @RequestMapping("/index")
  public ModelAndView index() {
    final List<Bloglet.BlogPost> posts = blogService.getBlogPosts(0, BlogService.MAX_LIMIT);
    return new ModelAndView("page/index", "posts", posts);
  }

  @RequestMapping("/tag")
  public ModelAndView tag(@RequestParam("id") String tagId) {
    final Map<String, Object> model = new HashMap<>();
    model.put("tag", blogService.getTag(tagId));
    model.put("posts", blogService.getBlogPostsForTag(tagId, 0, BlogService.MAX_LIMIT));

    return new ModelAndView("page/tag", model);
  }

  @RequestMapping("/post")
  public ModelAndView blogPost(@RequestParam("id") String postId) {
    return new ModelAndView("page/post", "post", blogService.getBlogPost(postId));
  }

  @RequestMapping("/about")
  public String about() {
    return "page/about";
  }
}

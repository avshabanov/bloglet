package bloglet.website.controller;

import bloglet.model.Bloglet;
import bloglet.website.service.BlogService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

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

  @RequestMapping("/about")
  public String about() {
    return "page/about";
  }
}

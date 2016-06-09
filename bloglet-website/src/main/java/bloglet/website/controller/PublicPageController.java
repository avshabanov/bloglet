package bloglet.website.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import bloglet.website.service.BookService;

import javax.annotation.Resource;

@Controller
@RequestMapping("/g")
public final class PublicPageController {
  @Resource BookService bookService;

  @RequestMapping("/login")
  public String login() {
    return "page/login";
  }

  @RequestMapping("/index")
  public ModelAndView index() {
    return new ModelAndView("page/index",
        "books", bookService.getBooks(0, BookService.DEFAULT_OFFSET));
  }

  @RequestMapping("/about")
  public String about() {
    return "page/about";
  }
}

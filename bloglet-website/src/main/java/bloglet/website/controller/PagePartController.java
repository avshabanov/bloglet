package bloglet.website.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import bloglet.website.model.Book;
import bloglet.website.service.BookService;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/g/part")
public final class PagePartController {

  /**
   * Default value for limit parameter.
   * NOTE: Can't use Integer.toString here as it makes value non-constant.
   */
  private static final String DEFAULT_LIMIT_VAL = "" + BookService.DEFAULT_OFFSET;

  @Resource BookService bookService;

  @RequestMapping("/books")
  public ModelAndView getBooks(@RequestParam("offset") int offset,
                               @RequestParam(value = "limit", defaultValue = DEFAULT_LIMIT_VAL) int limit) {
    final List<Book> books = bookService.getBooks(offset, limit);
    return new ModelAndView("part/books", "books", books);
  }
}

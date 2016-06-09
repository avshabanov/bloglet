package bloglet.website.service;

import bloglet.website.model.Book;

import java.util.List;

public interface BookService {
  int DEFAULT_OFFSET = 4;

  List<Book> getBooks(int offset, int limit);
}

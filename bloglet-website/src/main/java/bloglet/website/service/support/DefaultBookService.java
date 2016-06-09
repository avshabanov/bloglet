package bloglet.website.service.support;

import bloglet.website.model.Book;
import bloglet.website.service.BookService;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public final class DefaultBookService implements BookService {

  private final List<Book> books = new ArrayList<>();

  public DefaultBookService() {
    books.add(new Book(100, "Book 1", asList("genre1", "genre2"), asList("author1", "author2")));
    books.add(new Book(101, "Book 2", singletonList("genre1"), singletonList("author1")));
    books.add(new Book(102, "Book 3", asList("genre1", "genre2"), asList("author1", "author2")));
    books.add(new Book(103, "Book 4", asList("genre1", "genre2"), asList("author1", "author2")));
    books.add(new Book(104, "Book 5", asList("genre1", "genre2"), asList("author1", "author2")));
    books.add(new Book(105, "Book 6", asList("genre1", "genre2"), asList("author1", "author2")));
    books.add(new Book(106, "Book 7", asList("genre1", "genre2"), asList("author1", "author2")));
    books.add(new Book(107, "Book 8", asList("genre1", "genre2"), asList("author1", "author2")));
    books.add(new Book(108, "Book 9", asList("genre1", "genre2"), asList("author1", "author2")));
    books.add(new Book(109, "Book 10", asList("genre1", "genre2"), asList("author1", "author2")));
    books.add(new Book(110, "Book 11", asList("genre1", "genre2"), asList("author1", "author2")));
    books.add(new Book(111, "Book 12", asList("genre1", "genre2"), asList("author1", "author2")));
    books.add(new Book(112, "Book 13", asList("genre1", "genre2"), asList("author1", "author2")));
    books.add(new Book(113, "Book 14", asList("genre1", "genre2"), asList("author1", "author2")));
    books.add(new Book(114, "Book 15", asList("genre1", "genre2"), asList("author1", "author2")));
    books.add(new Book(115, "Book 16", asList("genre1", "genre2"), asList("author1", "author2")));
    books.add(new Book(116, "Book 17", asList("genre1", "genre2"), asList("author1", "author2")));
    books.add(new Book(117, "Book 18", asList("genre1", "genre2"), asList("author1", "author2")));
    books.add(new Book(118, "Book 19", asList("genre1", "genre2"), asList("author1", "author2")));
    books.add(new Book(119, "Book 20", asList("genre1", "genre2"), asList("author1", "author2")));
    books.add(new Book(121, "Book 21", asList("genre1", "genre2"), asList("author1", "author2")));
  }

  @Override
  public List<Book> getBooks(int offset, int limit) {
    if (offset < 0) {
      throw new IllegalArgumentException("offset");
    }
    if (limit < 0) {
      throw new IllegalArgumentException("limit");
    }
    if (offset >= books.size()) {
      return emptyList();
    }

    return books.subList(offset, Math.min(books.size(), offset + limit));
  }
}

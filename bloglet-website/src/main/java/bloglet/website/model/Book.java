package bloglet.website.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a book entry in book catalog.
 *
 * @author Alexander Shabanov
 */
public final class Book {
  private final int id;
  private final String name;
  private final List<String> genres;
  private final List<String> authors;

  public Book(int id, String name, List<String> genres, List<String> authors) {
    this.id = id;
    this.name = name;
    this.genres = Collections.unmodifiableList(new ArrayList<>(genres));
    this.authors = Collections.unmodifiableList(new ArrayList<>(authors));
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public List<String> getGenres() {
    return genres;
  }

  public List<String> getAuthors() {
    return authors;
  }

  @Override
  public String toString() {
    return "Book{" +
        "id=" + getId() +
        ", name='" + getName() + '\'' +
        ", genres=" + getGenres() +
        ", authors=" + getAuthors() +
        '}';
  }
}

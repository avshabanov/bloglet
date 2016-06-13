package bloglet.website.init;

import bloglet.model.BlogletDb;
import bloglet.website.service.BlogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

/**
 * Initializer, that adds demo data to the database.
 *
 * @author Alexander Shabanov
 */
public final class DemoInitializer {

  private final Logger log = LoggerFactory.getLogger(getClass());
  private final BlogService blogService;
  private final Random timeRandom;

  public DemoInitializer(BlogService blogService) {
    this.blogService = Objects.requireNonNull(blogService, "blogService");
    timeRandom = new Random(164567243167435231L);
  }

  @PostConstruct
  public void init() {
    log.info("Adding demo data to DB");

    final String[] tags = {
        blogService.addTag("software"),
        blogService.addTag("humour"),
        blogService.addTag("science"),
        blogService.addTag("hardware"),
    };

    final String[] blogEntryIds = {
        // Random titles composed using Wordnik
        blogService.addBlogEntry(createBlogEntry("Kaikomako Algarroba Weirdnet", tags[0], tags[1])),
        blogService.addBlogEntry(createBlogEntry("Upas Sassafras Dryad Actor", tags[2])),
        blogService.addBlogEntry(createBlogEntry("Hickory", tags[0], tags[2])),
        blogService.addBlogEntry(createBlogEntry("Light Yew Acacia", tags[2], tags[3])),
        blogService.addBlogEntry(createBlogEntry("Bergamot Shoot Timber", tags[0], tags[2])),
        blogService.addBlogEntry(createBlogEntry("Squshed Obon", tags[1])),
        blogService.addBlogEntry(createBlogEntry("Wasotec Vulgarized Treasures", tags[3])),
        blogService.addBlogEntry(createBlogEntry("Sabertoothed Stretch", tags[2], tags[3])),
        blogService.addBlogEntry(createBlogEntry("Colophon Humpty Dumpty", tags[1], tags[2])),
        blogService.addBlogEntry(createBlogEntry("Phospherous Carbon", tags[0], tags[1], tags[2], tags[3])),
        blogService.addBlogEntry(createBlogEntry("Track Scrub Lime", tags[1], tags[3])),
        blogService.addBlogEntry(createBlogEntry("Pine Pollard Guide Steer", tags[0], tags[3])),
        blogService.addBlogEntry(createBlogEntry("Aspen Body Manuever", tags[0], tags[2])),
        blogService.addBlogEntry(createBlogEntry("Ivory Aul", tags[2])),
    };

    log.info("Added blog entries={}", Arrays.asList(blogEntryIds));
  }

  private BlogletDb.BlogEntry createBlogEntry(String title, String... tagIds) {
    final Random random = new Random(title.hashCode());
    final StringBuilder contentBuilder = new StringBuilder(8192);
    final int contentSize = random.nextInt(3096) + 1000;
    for (int w = 0, wlen = 0, s = 0, slen = 0; contentBuilder.length() < contentSize; ++w, ++s) {
      if (wlen == 0 || w == wlen) {
        w = 0;
        wlen = 1 + random.nextInt(10);
        if (contentBuilder.length() > 0) {
          contentBuilder.append(' ');
        }
      }

      if (s == slen || slen == 0) {
        slen = 30 + random.nextInt(100);
        if (contentBuilder.length() > 0) {
          contentBuilder.append('.').append('\n');
        }
      }

      contentBuilder.append((char) ('a' + random.nextInt(26)));
    }

    final String contents = contentBuilder.toString();
    final String shortContents = contentBuilder.substring(0, 120);

    final long dateCreated = 1380628471120L + (timeRandom.nextLong() % 102158471120L);
    return BlogletDb.BlogEntry.newBuilder()
        .setTitle(title)
        .setDateCreated(dateCreated)
        .setDateUpdated(dateCreated)
        .setContents(toHtml(contents))
        .setShortContents(toHtml(shortContents))
        .addAllTagIds(Arrays.asList(tagIds))
        .build();
  }

  private static String toHtml(String text) {
    return "<div>\n<p>" + text.replaceAll("\\n", "</p>\n<p>") + "</p>\n</div>\n";
  }
}

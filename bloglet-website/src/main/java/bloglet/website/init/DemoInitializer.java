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

  public DemoInitializer(BlogService blogService) {
    this.blogService = Objects.requireNonNull(blogService, "blogService");
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

    log.info("Added blog entries={}", blogEntryIds);
  }

  private BlogletDb.BlogEntry createBlogEntry(String title, String... tagIds) {
    final Random random = new Random(title.hashCode());
    final StringBuilder contentBuilder = new StringBuilder(4096);
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

    final long now = System.currentTimeMillis();
    return BlogletDb.BlogEntry.newBuilder()
        .setTitle(title)
        .setDateCreated(now)
        .setDateUpdated(now)
        .setContents(contentBuilder.toString())
        .setShortContents(contentBuilder.substring(0, 120))
        .addAllTagIds(Arrays.asList(tagIds))
        .build();
  }
}

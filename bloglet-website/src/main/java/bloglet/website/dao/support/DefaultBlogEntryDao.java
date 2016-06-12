package bloglet.website.dao.support;

import bloglet.model.BlogletDb;
import bloglet.website.dao.BlogEntryDao;
import com.truward.tupl.support.transaction.TuplTransactionManager;

import javax.annotation.Nonnull;

/**
 * @author Alexander Shabanov
 */
public final class DefaultBlogEntryDao extends ProtobufModelPersistentMapDao<BlogletDb.BlogEntry> implements BlogEntryDao {
  public static final String INDEX_NAME = "BlogEntry";

  public DefaultBlogEntryDao(@Nonnull TuplTransactionManager txManager) {
    super(txManager, INDEX_NAME, (id, contents) -> BlogletDb.BlogEntry.parseFrom(contents));
  }
}

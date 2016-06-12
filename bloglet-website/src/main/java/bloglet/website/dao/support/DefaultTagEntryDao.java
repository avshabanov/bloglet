package bloglet.website.dao.support;

import bloglet.model.BlogletDb;
import bloglet.website.dao.TagEntryDao;
import com.truward.tupl.support.transaction.TuplTransactionManager;

import javax.annotation.Nonnull;

/**
 * @author Alexander Shabanov
 */
public final class DefaultTagEntryDao extends ProtobufModelPersistentMapDao<BlogletDb.TagEntry> implements TagEntryDao {
  public static final String INDEX_NAME = "Tag";

  public DefaultTagEntryDao(@Nonnull TuplTransactionManager txManager) {
    super(txManager, INDEX_NAME, (id, contents) -> BlogletDb.TagEntry.parseFrom(contents));
  }
}

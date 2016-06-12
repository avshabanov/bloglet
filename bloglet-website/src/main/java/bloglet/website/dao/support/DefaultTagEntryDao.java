package bloglet.website.dao.support;

import bloglet.model.BlogletDb;
import bloglet.website.dao.TagEntryDao;
import com.truward.tupl.protobuf.ProtobufModelPersistentMapDao;
import com.truward.tupl.support.transaction.TuplTransactionManager;

import javax.annotation.Nonnull;
import java.io.IOException;

/**
 * @author Alexander Shabanov
 */
public final class DefaultTagEntryDao extends ProtobufModelPersistentMapDao<BlogletDb.TagEntry> implements TagEntryDao {
  public static final String INDEX_NAME = "Tag";

  public DefaultTagEntryDao(@Nonnull TuplTransactionManager txManager) {
    super(txManager, INDEX_NAME);
  }

  @Nonnull
  @Override
  protected BlogletDb.TagEntry toValue(@Nonnull String id, @Nonnull byte[] contents) throws IOException {
    return BlogletDb.TagEntry.parseFrom(contents);
  }
}

package bloglet.website.dao.support;

import bloglet.model.BlogletDb;
import bloglet.website.dao.BlogEntryDao;
import com.truward.tupl.protobuf.ProtobufModelPersistentMapDao;
import com.truward.tupl.support.transaction.TuplTransactionManager;

import javax.annotation.Nonnull;
import java.io.IOException;

/**
 * @author Alexander Shabanov
 */
public final class DefaultBlogEntryDao extends ProtobufModelPersistentMapDao<BlogletDb.BlogEntry> implements BlogEntryDao {
  public static final String INDEX_NAME = "BlogEntry";

  public DefaultBlogEntryDao(@Nonnull TuplTransactionManager txManager) {
    super(txManager, INDEX_NAME);
  }

  @Nonnull
  @Override
  protected BlogletDb.BlogEntry toValue(@Nonnull String id, @Nonnull byte[] contents) throws IOException {
    return BlogletDb.BlogEntry.parseFrom(contents);
  }
}

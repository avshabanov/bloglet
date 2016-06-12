package bloglet.website.dao.support;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.truward.tupl.support.exception.InternalErrorDaoException;
import com.truward.tupl.support.map.support.StandardPersistentMapDao;
import com.truward.tupl.support.transaction.TuplTransactionManager;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * Base class for Tupl DAO, working with protobuf objects.
 *
 * @author Alexander Shabanov
 */
public abstract class ProtobufModelPersistentMapDao<TValue extends Message> extends StandardPersistentMapDao<TValue> {

  private final ParserCallback<TValue> parserCallback;

  protected ProtobufModelPersistentMapDao(@Nonnull TuplTransactionManager txManager,
                                          @Nonnull String indexName,
                                          @Nonnull ParserCallback<TValue> parserCallback) {
    super(txManager, indexName);
    this.parserCallback = Objects.requireNonNull(parserCallback);
  }

  @Nonnull
  @Override
  protected final byte[] toBytes(@Nonnull TValue value) {
    return value.toByteArray();
  }

  @Nonnull
  @Override
  protected final TValue toValue(@Nonnull String id, @Nonnull byte[] contents) {
    try {
      return parserCallback.parseFrom(id, contents);
    } catch (InvalidProtocolBufferException e) {
      throw new InternalErrorDaoException(e);
    }
  }

  protected interface ParserCallback<TValue> {

    @Nonnull
    TValue parseFrom(@Nonnull String id, @Nonnull byte[] contents) throws InvalidProtocolBufferException;
  }
}

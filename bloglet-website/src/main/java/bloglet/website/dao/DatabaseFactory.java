package bloglet.website.dao;

import org.cojen.tupl.Database;
import org.cojen.tupl.DatabaseConfig;
import org.cojen.tupl.DurabilityMode;
import org.springframework.beans.factory.FactoryBean;

import java.io.File;
import java.util.Objects;

/**
 * Database factory class.
 *
 * @author Alexander Shabanov
 */
public final class DatabaseFactory implements FactoryBean<Database> {
  private static final String TEMP = "temp";

  private final String databasePath;

  public DatabaseFactory(String databasePath) {
    this.databasePath = Objects.requireNonNull(databasePath, "databasePath");
  }

  @Override
  public Database getObject() throws Exception {
    final String path;
    if (TEMP.equals(databasePath)) {
      final File tmpFile = File.createTempFile("TempTupl-", "-db");
      path = tmpFile.getPath();
    } else {
      path = databasePath;
    }
    return Database.open(new DatabaseConfig()
        .baseFilePath(path)
        .maxCacheSize(1024 * 1024)
        .durabilityMode(DurabilityMode.NO_SYNC));
  }

  @Override
  public Class<?> getObjectType() {
    return Database.class;
  }

  @Override
  public boolean isSingleton() {
    return true;
  }
}

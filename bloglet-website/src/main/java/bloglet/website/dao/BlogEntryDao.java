package bloglet.website.dao;

import bloglet.model.BlogletDb;
import com.truward.tupl.support.map.PersistentMapDao;

/**
 * @author Alexander Shabanov
 */
public interface BlogEntryDao extends PersistentMapDao<BlogletDb.BlogEntry> {
}

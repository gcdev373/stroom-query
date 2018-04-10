package stroom.query.common.v2;

public interface SearchResponseCreatorManager {

    /**
     * Get a {@link SearchResponseCreator} from the cache or create one if it doesn't exist
     */
    SearchResponseCreator get(SearchResponseCreatorCache.Key key);

    /**
     * Remove an entry from the cache, this will also terminate any running search for that entry
     */
    void remove(SearchResponseCreatorCache.Key key);

    /**
     * Evicts any expired entries from the underlying cache
     */
    void evictExpiredElements();
}

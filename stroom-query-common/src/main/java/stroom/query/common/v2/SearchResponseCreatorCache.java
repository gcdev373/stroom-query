package stroom.query.common.v2;

import stroom.query.api.v2.QueryKey;
import stroom.query.api.v2.SearchRequest;
import stroom.util.shared.HasTerminate;

/**
 * A self populating cache of {@link SearchResponseCreator} instances
 */
public interface SearchResponseCreatorCache {

    /**
     * @param key The key to read/create
     * @return Get a {@link SearchResponseCreator} from the cache or create one and add it to the cache
     * if it doesn't exist.
     */
    SearchResponseCreator get(final Key key);

    /**
     * Remove an entry from the cache for {@link Key} key
     * @param key The key to remove
     */
    void remove(final Key key);

    void evictExpiredElements();


    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    class Key {
        private final QueryKey queryKey;
        private final SearchRequest searchRequest;
        private final HasTerminate hasTerminate;

        public Key(final SearchRequest searchRequest, final HasTerminate hasTerminate) {
            this.queryKey = searchRequest.getKey();
            this.searchRequest = searchRequest;
            this.hasTerminate = hasTerminate;
        }

        public Key(final QueryKey queryKey, final HasTerminate hasTerminate) {
            this.queryKey = queryKey;
            this.searchRequest = null;
            this.hasTerminate = hasTerminate;
        }

        public QueryKey getQueryKey() {
            return queryKey;
        }

        public SearchRequest getSearchRequest() {
            return searchRequest;
        }

        public HasTerminate getHasTerminate() {
            return hasTerminate;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            final Key key = (Key) o;

            return queryKey.equals(key.queryKey);
        }

        @Override
        public int hashCode() {
            return queryKey.hashCode();
        }

        @Override
        public String toString() {
            return "Key{" +
                    "queryKey=" + queryKey +
                    '}';
        }
    }
}

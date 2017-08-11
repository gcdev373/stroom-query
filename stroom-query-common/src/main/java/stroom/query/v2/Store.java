/*
 * Copyright 2017 Crown Copyright
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package stroom.query.v2;

import java.util.List;

public interface Store {
    /**
     * Stop searching and destroy any stored data.
     */
    void destroy();

    /**
     * Find out if the search has completed. There will be no new results once
     * complete so we can stop requesting updates.
     *
     * @return True if the search has completed.
     */
    boolean isComplete();

    /**
     * Get the current data that is available for the specified component.
     *
     * @param componentId The id of the component that results are being populated for.
     * @return A store of current search results for the specified component.
     */
    Data getData(String componentId);

    /**
     * Gets a list of strings containing all errors that have occurred so far during the
     * current search.
     *
     * @return A string containing all errors that have occurred so far during
     * the current search.
     */
    List<String> getErrors();

    /**
     * Get any search query highlights that can be extracted from the query.
     *
     * @return A set of strings found in the query that could be highlighted in
     * the UI to show where the query terms have been found.
     */
    List<String> getHighlights();
}

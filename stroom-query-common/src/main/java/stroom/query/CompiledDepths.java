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

package stroom.query;

import stroom.query.api.v1.Field;

import java.util.List;

public class CompiledDepths {
    private final int maxGroupDepth;
    private final int maxDepth;
    private final int[] depths;
    private final boolean hasGroupBy;

    public CompiledDepths(final List<Field> fields, boolean showDetail) {
        int maxGroupDepth = -1;

        if (fields == null) {
            depths = new int[0];
        } else {
            depths = new int[fields.size()];

            int i = 0;
            for (final Field field : fields) {
                // Create compiled field.
                int groupDepth = -1;
                if (field.getGroup() != null) {
                    groupDepth = field.getGroup();
                }
                maxGroupDepth = Math.max(maxGroupDepth, groupDepth);
                depths[i++] = groupDepth;
            }
        }

        this.maxGroupDepth = maxGroupDepth;

        // If we want the table to show details below the group level then we
        // need to set max depth to be 1 greater than the max group depth.
        if (showDetail) {
            maxDepth = this.maxGroupDepth + 1;
        } else {
            maxDepth = this.maxGroupDepth;
        }

        hasGroupBy = maxGroupDepth >= 0;
    }

    /**
     * This is the maximum depth of grouping, 0 based
     *
     * @return The maximum depth for all groups.
     */
    public int getMaxGroupDepth() {
        return maxGroupDepth;
    }

    /**
     * @return The maximum depth (can be equal to max group depth or max group depth + 1 if showing detail).
     */
    public int getMaxDepth() {
        return maxDepth;
    }

    /**
     * An array of all field depths.
     *
     * @return Get an array of depths for all fields.
     */
    public int[] getDepths() {
        return depths;
    }

    /**
     * True if one of more fields are grouped.
     *
     * @return True if one of more fields are grouped.
     */
    public boolean hasGroupBy() {
        return hasGroupBy;
    }
}

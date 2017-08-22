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

package stroom.query.common.v2;

import stroom.mapreduce.v2.OutputCollector;
import stroom.mapreduce.v2.Reducer;
import stroom.mapreduce.v2.SimplePartitioner;

public class ItemPartitioner extends SimplePartitioner<Key, Item, Key, Item> {
    private final ItemReducer itemReducer;
    private OutputCollector<Key, Item> outputCollector;

    public ItemPartitioner(final int[] depths, final int maxDepth) {
        // Create a reusable reducer as it doesn't hold state.
        itemReducer = new ItemReducer(depths, maxDepth);
    }

    @Override
    protected Reducer<Key, Item, Key, Item> createReducer() {
        // Reuse the same reducer as there is no state.
        return itemReducer;
    }

    @Override
    protected void collect(final Key key, final Item value) {
        // The standard collect method is overridden so that items with a null
        // key are passed straight to the output collector and will not undergo
        // partitioning and reduction as we don't want to group items with null
        // keys.
        if (key != null && key.getValues() != null) {
            super.collect(key, value);
        } else {
            outputCollector.collect(key, value);
        }
    }

    @Override
    public void setOutputCollector(final OutputCollector<Key, Item> outputCollector) {
        super.setOutputCollector(outputCollector);
        this.outputCollector = outputCollector;
    }
}

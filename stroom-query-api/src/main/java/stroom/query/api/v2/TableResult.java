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

package stroom.query.api.v2;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@JsonPropertyOrder({"componentId", "rows", "resultRange", "totalResults", "error"})
@ApiModel(
        description = "Object for describing a set of results in a table form that supports grouped data",
        parent = Result.class)
public final class TableResult extends Result {
    private static final long serialVersionUID = -2964122512841756795L;

    @ApiModelProperty(
            required = true)
    private List<Row> rows;

    @ApiModelProperty(
            required = true)
    private OffsetRange resultRange;

    @ApiModelProperty(
            value = "The total number of results in this result set",
            required = false)
    private Integer totalResults;

    TableResult() {
    }

    public TableResult(final String componentId,
                       final List<Row> rows,
                       final OffsetRange resultRange,
                       final Integer totalResults,
                       final String error) {
        super(componentId, error);
        this.rows = rows;
        this.resultRange = resultRange;
        this.totalResults = totalResults;
    }

    public TableResult(final String componentId,
                       final List<Row> rows,
                       final OffsetRange resultRange,
                       final String error) {
        super(componentId, error);
        this.rows = rows;
        this.resultRange = resultRange;
        this.totalResults = rows.size();
    }

    public List<Row> getRows() {
        return rows;
    }

    public OffsetRange getResultRange() {
        return resultRange;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        final TableResult that = (TableResult) o;
        return Objects.equals(rows, that.rows) &&
                Objects.equals(resultRange, that.resultRange) &&
                Objects.equals(totalResults, that.totalResults);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), rows, resultRange, totalResults);
    }

    @Override
    public String toString() {
        if (rows == null) {
            return "0 rows";
        }

        return rows.size() + " rows";
    }

    /**
     * Builder for constructing a {@link TableResult tableResult}
     */
    public static class Builder
            extends Result.Builder<TableResult, Builder> {
        private final List<Row> rows = new ArrayList<>();
        private OffsetRange resultRange;

        /**
         * @param values add rows of data to our table
         *
         * @return The {@link Builder}, enabling method chaining
         */
        public Builder addRows(final Row...values) {
            this.rows.addAll(Arrays.asList(values));
            return this;
        }

        /**
         * @param value The offset range used to generate the results
         *
         * @return The {@link Builder}, enabling method chaining
         */
        public Builder resultRange(final OffsetRange value) {
            this.resultRange = value;
            return this;
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public TableResult build() {
            return new TableResult(getComponentId(), rows, resultRange, getError());
        }
    }
}
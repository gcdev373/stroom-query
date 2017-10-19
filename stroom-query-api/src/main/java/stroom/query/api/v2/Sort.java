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
import stroom.util.shared.HasDisplayValue;
import stroom.util.shared.PojoBuilder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@JsonPropertyOrder({"order", "direction"})
@XmlType(name = "Sort", propOrder = {"order", "direction"})
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(description = "Describes the sorting applied to a field")
public final class Sort implements Serializable {
    private static final long serialVersionUID = 4530846367973824427L;

    @XmlElement
    @ApiModelProperty(
            value = "Where multiple fields are sorted this value describes the sort order, with 0 being the first " +
                    "field to sort on",
            example = "0",
            required = true)
    private Integer order;

    @XmlElement
    @ApiModelProperty(
            value = "The direction to sort in, ASCENDING or DESCENDING",
            example = "ASCENDING",
            required = true)
    private SortDirection direction;

    private Sort() {
    }

    public Sort(final Integer order, final SortDirection direction) {
        this.order = order;
        this.direction = direction;
    }

    public Integer getOrder() {
        return order;
    }

    public SortDirection getDirection() {
        return direction;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Sort sort = (Sort) o;

        if (order != null ? !order.equals(sort.order) : sort.order != null) return false;
        return direction == sort.direction;
    }

    @Override
    public int hashCode() {
        int result = order != null ? order.hashCode() : 0;
        result = 31 * result + (direction != null ? direction.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Sort{" +
                "order=" + order +
                ", direction=" + direction +
                '}';
    }

    public enum SortDirection implements HasDisplayValue {
        ASCENDING("Ascending"), DESCENDING("Descending");

        private final String displayValue;

        SortDirection(final String displayValue) {
            this.displayValue = displayValue;
        }

        @Override
        public String getDisplayValue() {
            return displayValue;
        }
    }

    /**
     * Builder for constructing a {@link Sort sort}
     */
    public static class Builder<ParentBuilder extends PojoBuilder>
            extends PojoBuilder<ParentBuilder, Sort, Builder<ParentBuilder>> {
        private Integer order;

        private SortDirection direction;

        public Builder<ParentBuilder> order(final Integer value) {
            this.order = value;
            return self();
        }

        public Builder<ParentBuilder> direction(final SortDirection value) {
            this.direction = value;
            return self();
        }

        protected Sort pojoBuild() {
            return new Sort(order, direction);
        }

        @Override
        public Builder<ParentBuilder> self() {
            return this;
        }
    }
}

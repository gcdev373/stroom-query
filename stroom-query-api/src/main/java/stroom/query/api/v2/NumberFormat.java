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
import stroom.util.shared.OwnedBuilder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@JsonPropertyOrder({"decimalPlaces", "useSeparator"})
@XmlType(name = "NumberFormat", propOrder = {"decimalPlaces", "useSeparator"})
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(description = "The definition of a format to apply to numeric data")
public final class NumberFormat implements Serializable {
    private static final long serialVersionUID = 9145624653060319801L;

    @XmlElement
    @ApiModelProperty(
            value = "The number of decimal places",
            example = "2",
            required = true)
    private Integer decimalPlaces;

    @XmlElement
    @ApiModelProperty(
            value = "Whether to use a thousands separator or not. Defaults to false",
            example = "true",
            required = false)
    private Boolean useSeparator;

    private NumberFormat() {
    }

    public NumberFormat(Integer decimalPlaces, Boolean useSeparator) {
        this.decimalPlaces = decimalPlaces;
        this.useSeparator = useSeparator;
    }

    public Integer getDecimalPlaces() {
        return decimalPlaces;
    }

    public Boolean getUseSeparator() {
        return useSeparator;
    }

    public boolean useSeparator() {
        return useSeparator != null && useSeparator;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof NumberFormat)) return false;

        final NumberFormat that = (NumberFormat) o;

        if (decimalPlaces != null ? !decimalPlaces.equals(that.decimalPlaces) : that.decimalPlaces != null)
            return false;
        return useSeparator != null ? useSeparator.equals(that.useSeparator) : that.useSeparator == null;
    }

    @Override
    public int hashCode() {
        int result = decimalPlaces != null ? decimalPlaces.hashCode() : 0;
        result = 31 * result + (useSeparator != null ? useSeparator.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "NumberFormat{" +
                "decimalPlaces=" + decimalPlaces +
                ", useSeparator=" + useSeparator +
                '}';
    }

    /**
     * Builder for constructing a {@link NumberFormat}
     *
     * @param <OwningBuilder> The class of the popToWhenComplete builder, allows nested building
     */
    public static class Builder<OwningBuilder extends OwnedBuilder>
            extends OwnedBuilder<OwningBuilder, NumberFormat, Builder<OwningBuilder>> {
        private Integer decimalPlaces;
        private Boolean useSeparator;

        /**
         * @param value Number of decimal places to apply to the number format
         *
         * @return The {@link Builder}, enabling method chaining
         */
        public Builder<OwningBuilder> decimalPlaces(final Integer value) {
            this.decimalPlaces = value;
            return self();
        }

        /**
         * @param value Whether to use a thousands separator or not.
         *
         * @return The {@link Builder}, enabling method chaining
         */
        public Builder<OwningBuilder> useSeparator(final Boolean value) {
            this.useSeparator = value;
            return self();
        }

        protected NumberFormat pojoBuild() {
            return new NumberFormat(decimalPlaces, useSeparator);
        }

        @Override
        public Builder<OwningBuilder> self() {
            return this;
        }
    }

}

/*
 * Copyright 2017 Crown Copyright
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package stroom.query.api.v2;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import stroom.util.shared.OwnedBuilder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ExpressionOperator.class, name = "addOperator"),
        @JsonSubTypes.Type(value = ExpressionTerm.class, name = "term")
})
@XmlType(name = "ExpressionItem", propOrder = {"enabled"})
@XmlSeeAlso({ExpressionOperator.class, ExpressionTerm.class})
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        description = "Base type for an item in an expression tree",
        subTypes = {ExpressionOperator.class, ExpressionTerm.class})
public abstract class ExpressionItem implements Serializable {
    private static final long serialVersionUID = -8483817637655853635L;

    @XmlElement
    @ApiModelProperty(
            value = "Whether this item in the expression tree is enabled or not",
            example = "true",
            required = true)
    private Boolean enabled;

    ExpressionItem() {
    }

    public ExpressionItem(final Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public boolean enabled() {
        return enabled == null || enabled;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof ExpressionItem)) return false;

        final ExpressionItem that = (ExpressionItem) o;

        return enabled != null ? enabled.equals(that.enabled) : that.enabled == null;
    }

    @Override
    public int hashCode() {
        return enabled != null ? enabled.hashCode() : 0;
    }

    abstract void append(final StringBuilder sb, final String pad, final boolean singleLine);

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        append(sb, "", true);
        return sb.toString();
    }

    public String toMultiLineString() {
        final StringBuilder sb = new StringBuilder();
        append(sb, "", false);
        return sb.toString();
    }

    /**
     * Builder for constructing a {@link ExpressionItem}. This is an abstract type, each subclass
     * of ExpressionItem should provide a builder that extends this one.
     *
     * @param <OwningBuilder> The class of the popToWhenComplete builder, allows nested building
     * @param <T> The subclass of {@link ExpressionItem}
     * @param <CHILD_CLASS> The class of the specific ExpressionItem builder.
     */
    public static abstract class Builder<
                OwningBuilder extends OwnedBuilder,
                T extends ExpressionItem,
                CHILD_CLASS extends Builder<OwningBuilder, T, ?>>
            extends OwnedBuilder<OwningBuilder, T, CHILD_CLASS> {

        private Boolean enabled;

        /**
         * @param value Sets the terms state to enabled if true or null, disabled if false
         *
         * @return The CHILD_CLASS Builder, enabling method chaining
         */
        public CHILD_CLASS enabled(final Boolean value) {
            this.enabled = value;
            return self();
        }

        /**
         * Accessible to child classes when buildPojo() is called.
         *
         * @return Whether the expression is enabled or not
         */
        protected Boolean getEnabled() {
            return enabled;
        }
    }
}
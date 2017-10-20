/*
 * Copyright 2016 Crown Copyright
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

import stroom.query.api.v2.Format.Type;

/**
 * A builder class for constructing a {@link Field}
 */
public final class FieldBuilder {

    private String name;
    private String expression;
    private Sort sort;
    private Filter filter;
    private Format format;
    private Integer group;

    /**
     * @param value XXXXXXXXXXXXXXXX
     *
     * @return The {@link Builder}, enabling method chaining
     */
    public FieldBuilder name(final String value) {
        this.name = value;
        return this;
    }

    /**
     * @param value XXXXXXXXXXXXXXXX
     *
     * @return The {@link Builder}, enabling method chaining
     */
    public FieldBuilder expression(final String value) {
        this.expression = value;
        return this;
    }

    /**
     * @param value XXXXXXXXXXXXXXXX
     *
     * @return The {@link Builder}, enabling method chaining
     */
    public FieldBuilder sort(final Sort value) {
        this.sort = value;
        return this;
    }

    /**
     * @param value XXXXXXXXXXXXXXXX
     *
     * @return The {@link Builder}, enabling method chaining
     */
    public FieldBuilder filter(final Filter value) {
        this.filter = value;
        return this;
    }

    /**
     * @param value XXXXXXXXXXXXXXXX
     *
     * @return The {@link Builder}, enabling method chaining
     */
    public FieldBuilder format(final Format value) {
        this.format = value;
        return this;
    }

    /**
     * @param value XXXXXXXXXXXXXXXX
     *
     * @return The {@link Builder}, enabling method chaining
     */
    public FieldBuilder format(final Type value) {
        this.format = new Format(value);
        return this;
    }

    /**
     * @param value XXXXXXXXXXXXXXXX
     *
     * @return The {@link Builder}, enabling method chaining
     */
    public FieldBuilder group(final Integer value) {
        this.group = value;
        return this;
    }

    public Field build() {
        return new Field(name, expression, sort, filter, format, group);
    }
}

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

package stroom.query.api.v1;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@JsonPropertyOrder({"name", "expression", "sort", "filter", "format", "group"})
@XmlType(name = "Field", propOrder = {"name", "expression", "sort", "filter", "format", "group"})
@XmlAccessorType(XmlAccessType.FIELD)
public final class Field implements Serializable {
    private static final long serialVersionUID = 7327802315955158337L;

    @XmlElement
    private String name;
    @XmlElement
    private String expression;
    @XmlElement
    private Sort sort;
    @XmlElement
    private Filter filter;
    @XmlElement
    private Format format;
    @XmlElement
    private Integer group;

    public Field() {
    }

    public Field(String name, String expression, Sort sort, Filter filter, Format format, Integer group) {
        this.name = name;
        this.expression = expression;
        this.sort = sort;
        this.filter = filter;
        this.format = format;
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public String getExpression() {
        return expression;
    }

    public Sort getSort() {
        return sort;
    }

    public Filter getFilter() {
        return filter;
    }

    public Format getFormat() {
        return format;
    }

    public Integer getGroup() {
        return group;
    }

    public Field copy() {
        final Field field = new Field();
        field.name = name;
        field.expression = expression;
        field.sort = sort;
        field.filter = filter;
        field.format = format;
        field.group = group;

        return field;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Field)) return false;

        final Field field = (Field) o;

        if (name != null ? !name.equals(field.name) : field.name != null) return false;
        if (expression != null ? !expression.equals(field.expression) : field.expression != null) return false;
        if (sort != null ? !sort.equals(field.sort) : field.sort != null) return false;
        if (filter != null ? !filter.equals(field.filter) : field.filter != null) return false;
        if (format != null ? !format.equals(field.format) : field.format != null) return false;
        return group != null ? group.equals(field.group) : field.group == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (expression != null ? expression.hashCode() : 0);
        result = 31 * result + (sort != null ? sort.hashCode() : 0);
        result = 31 * result + (filter != null ? filter.hashCode() : 0);
        result = 31 * result + (format != null ? format.hashCode() : 0);
        result = 31 * result + (group != null ? group.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Field{" +
                "name='" + name + '\'' +
                ", expression='" + expression + '\'' +
                ", sort=" + sort +
                ", filter=" + filter +
                ", format=" + format +
                ", group=" + group +
                '}';
    }
}
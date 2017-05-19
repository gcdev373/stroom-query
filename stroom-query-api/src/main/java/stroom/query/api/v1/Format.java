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
import stroom.util.shared.HasDisplayValue;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@JsonPropertyOrder({"type", "numberFormat", "dateTimeFormat"})
@XmlType(name = "Format", propOrder = {"type", "numberFormat", "dateTimeFormat"})
@XmlAccessorType(XmlAccessType.FIELD)
public final class Format implements Serializable {
    private static final long serialVersionUID = -5380825645719299089L;

    @XmlElement
    private Type type;
    @XmlElement
    private NumberFormat numberFormat;
    @XmlElement
    private DateTimeFormat dateTimeFormat;

    private Format() {
    }

    public Format(final Type type) {
        this.type = type;
    }

    public Format(final NumberFormat numberFormat) {
        this.type = Type.NUMBER;
        this.numberFormat = numberFormat;
    }

    public Format(final DateTimeFormat dateTimeFormat) {
        this.type = Type.DATE_TIME;
        this.dateTimeFormat = dateTimeFormat;
    }

    public Format(final Type type, final NumberFormat numberFormat, final DateTimeFormat dateTimeFormat) {
        this.type = type;
        this.numberFormat = numberFormat;
        this.dateTimeFormat = dateTimeFormat;
    }

    public Type getType() {
        return type;
    }

    public NumberFormat getNumberFormat() {
        return numberFormat;
    }

    public DateTimeFormat getDateTimeFormat() {
        return dateTimeFormat;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Format format = (Format) o;

        if (type != format.type) return false;
        if (numberFormat != null ? !numberFormat.equals(format.numberFormat) : format.numberFormat != null)
            return false;
        return dateTimeFormat != null ? dateTimeFormat.equals(format.dateTimeFormat) : format.dateTimeFormat == null;
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (numberFormat != null ? numberFormat.hashCode() : 0);
        result = 31 * result + (dateTimeFormat != null ? dateTimeFormat.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Format{" +
                "type=" + type +
                ", numberFormat=" + numberFormat +
                ", dateTimeFormat=" + dateTimeFormat +
                '}';
    }

    public enum Type implements HasDisplayValue {
        GENERAL("General"), NUMBER("Number"), DATE_TIME("Date Time"), TEXT("Text");

        private final String displayValue;

        Type(final String displayValue) {
            this.displayValue = displayValue;
        }

        @Override
        public String getDisplayValue() {
            return displayValue;
        }
    }
}
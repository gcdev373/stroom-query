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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@JsonPropertyOrder({"pattern", "timeZone"})
@XmlType(name = "DateTimeFormat", propOrder = {"pattern", "timeZone"})
@XmlAccessorType(XmlAccessType.FIELD)
public final class DateTimeFormat implements Serializable {
    private static final long serialVersionUID = 9145624653060319801L;

    @XmlElement
    private String pattern;
    @XmlElement
    private TimeZone timeZone;

    private DateTimeFormat() {
    }

    public DateTimeFormat(final String pattern, final TimeZone timeZone) {
        this.pattern = pattern;
        this.timeZone = timeZone;
    }

    public String getPattern() {
        return pattern;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof DateTimeFormat)) return false;

        final DateTimeFormat that = (DateTimeFormat) o;

        if (pattern != null ? !pattern.equals(that.pattern) : that.pattern != null) return false;
        return timeZone != null ? timeZone.equals(that.timeZone) : that.timeZone == null;
    }

    @Override
    public int hashCode() {
        int result = pattern != null ? pattern.hashCode() : 0;
        result = 31 * result + (timeZone != null ? timeZone.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DateTimeFormat{" +
                "pattern='" + pattern + '\'' +
                ", timeZone=" + timeZone +
                '}';
    }
}
/*
 * Copyright (C) 2016-2018, Stichting Mapcode Foundation (http://www.mapcode.com)
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

package com.mapcode.stats.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mapcode.stats.api.ApiConstants;
import com.tomtom.speedtools.apivalidation.ApiDTO;
import com.tomtom.speedtools.json.DateTimeSerializer.FromStringDeserializer;
import com.tomtom.speedtools.json.DateTimeSerializer.ToStringSerializer;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.DateTime;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings({"NonFinalFieldReferenceInEquals", "NonFinalFieldReferencedInHashCode", "NullableProblems", "EqualsWhichDoesntCheckParameterClass"})
@JsonInclude(Include.NON_EMPTY)
@XmlRootElement(name = "status")
@XmlAccessorType(XmlAccessType.FIELD)
public final class StatusDTO extends ApiDTO {

    @XmlElement(name = "cachedEvents")
    private int cachedEvents;

    @XmlElement(name = "totalEvents")
    private int totalEvents;

    @XmlElement(name = "oldestEvent")
    @Nonnull
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = FromStringDeserializer.class)
    private DateTime oldestEvent;

    @XmlElement(name = "newestEvent")
    @Nonnull
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = FromStringDeserializer.class)
    private DateTime newestEvent;

    @Override
    public void validate() {
        validator().start();
        validator().checkInteger(true, "cachedEvents", cachedEvents, 0, Integer.MAX_VALUE);
        validator().checkInteger(true, "totalEvents", totalEvents, 0, Integer.MAX_VALUE);
        validator().checkDate(true, "oldestEvent", oldestEvent, ApiConstants.API_DATE_MIN, ApiConstants.API_DATE_MAX);
        validator().checkDate(true, "newestEvent", newestEvent, ApiConstants.API_DATE_MIN, ApiConstants.API_DATE_MAX);
        validator().done();
    }

    public StatusDTO(
            final int cachedEvents,
            final int totalEvents,
            @Nonnull final DateTime oldestEvent,
            @Nonnull final DateTime newestEvent) {
        this.cachedEvents = cachedEvents;
        this.totalEvents = totalEvents;
        this.oldestEvent = oldestEvent;
        this.newestEvent = newestEvent;
    }

    @SuppressWarnings("UnusedDeclaration")
    @Deprecated
    private StatusDTO() {
        // Default constructor required by JAX-B.
        super();
    }

    public int getCachedEvents() {
        beforeGet();
        return cachedEvents;
    }

    public void setCachedEvents(final int cachedEvents) {
        beforeSet();
        this.cachedEvents = cachedEvents;
    }

    public int getTotalEvents() {
        beforeGet();
        return totalEvents;
    }

    public void setTotalEvents(final int totalEvents) {
        beforeSet();
        this.totalEvents = totalEvents;
    }

    @Nonnull
    public DateTime getOldestEvent() {
        beforeGet();
        return oldestEvent;
    }

    public void setOldestEvent(@Nonnull  final DateTime oldestEvent) {
        beforeSet();
        this.oldestEvent = oldestEvent;
    }

    @Nonnull
    public DateTime getNewestEvent() {
        beforeGet();
        return newestEvent;
    }

    public void setNewestEvent(@Nonnull  final DateTime newestEvent) {
        beforeSet();
        this.newestEvent = newestEvent;
    }

    @Override
    @Nonnull
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public boolean equals(@Nullable final Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj, false);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, false);
    }
}

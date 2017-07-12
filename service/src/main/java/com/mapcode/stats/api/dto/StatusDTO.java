/*
 * Copyright (C) 2016-2017, Stichting Mapcode Foundation (http://www.mapcode.com)
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
import com.tomtom.speedtools.apivalidation.ApiDTO;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

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

    @XmlElement(name = "nrOfRequestsActive")
    @Nonnull
    private int nrOfRequestsActive;

    @XmlElement(name = "nrOfRequestsTotal")
    @Nonnull
    private int nrOfRequestsTotal;

    @XmlElement(name = "nrOfEventsInCache")
    @Nonnull
    private int nrOfEventsInCache;

    @XmlElement(name = "nrOfEventsTotal")
    @Nonnull
    private int nrOfEventsTotal;

    @Override
    public void validate() {
        validator().start();
        validator().checkInteger(true, "nrOfRequestsActive", nrOfRequestsActive, 0, Integer.MAX_VALUE);
        validator().checkInteger(true, "nrOfRequestsTotal", nrOfRequestsTotal, 0, Integer.MAX_VALUE);
        validator().checkInteger(true, "nrOfEventsInCache", nrOfEventsInCache, 0, Integer.MAX_VALUE);
        validator().checkInteger(true, "nrOfEventsTotal", nrOfEventsTotal, 0, Integer.MAX_VALUE);
        validator().done();
    }

    public StatusDTO(
            final int nrOfRequestsActive,
            final int nrOfRequestsTotal,
            final int nrOfEventsInCache,
            final int nrOfEventsTotal) {
        this.nrOfRequestsActive = nrOfRequestsActive;
        this.nrOfRequestsTotal = nrOfRequestsTotal;
        this.nrOfEventsInCache = nrOfEventsInCache;
        this.nrOfEventsTotal = nrOfEventsTotal;
    }

    @SuppressWarnings("UnusedDeclaration")
    @Deprecated
    private StatusDTO() {
        // Default constructor required by JAX-B.
        super();
    }

    public int getNrOfRequestsActive() {
        beforeGet();
        return nrOfRequestsActive;
    }

    public void setNrOfRequestsActive(final int nrOfRequestsActive) {
        beforeSet();
        this.nrOfRequestsActive = nrOfRequestsActive;
    }

    public int getNrOfRequestsTotal() {
        beforeGet();
        return nrOfRequestsTotal;
    }

    public void setNrOfRequestsTotal(final int nrOfRequestsTotal) {
        beforeSet();
        this.nrOfRequestsTotal = nrOfRequestsTotal;
    }

    public int getNrOfEventsInCache() {
        beforeGet();
        return nrOfEventsInCache;
    }

    public void setNrOfEventsInCache(final int nrOfEventsInCache) {
        beforeSet();
        this.nrOfEventsInCache = nrOfEventsInCache;
    }

    public int getNrOfEventsTotal() {
        beforeGet();
        return nrOfEventsTotal;
    }

    public void setNrOfEventsTotal(final int nrOfEventsTotal) {
        beforeSet();
        this.nrOfEventsTotal = nrOfEventsTotal;
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

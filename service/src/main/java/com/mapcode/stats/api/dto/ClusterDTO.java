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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings({"NonFinalFieldReferenceInEquals", "NonFinalFieldReferencedInHashCode", "NullableProblems", "EqualsWhichDoesntCheckParameterClass"})
@JsonInclude(Include.NON_EMPTY)
@XmlRootElement(name = "mapcode")
@XmlAccessorType(XmlAccessType.FIELD)
public final class ClusterDTO extends ApiDTO {

    @XmlElement(name = "count")
    @Nonnull
    private int count;

    @XmlElement(name = "southWest")
    @Nullable
    private PointDTO southWest;

    @XmlElement(name = "northEast")
    @Nullable
    private PointDTO northEast;

    @Override
    public void validate() {
        validator().start();
        validator().checkInteger(true, "nrEvents", count, 0, Integer.MAX_VALUE);
        validator().done();
    }

    public ClusterDTO(
            @Nonnull final int count,
            @Nullable final PointDTO southWest,
            @Nullable final PointDTO northEast) {
        this.count = count;
        this.southWest = southWest;
        this.northEast = northEast;
    }

    @SuppressWarnings("UnusedDeclaration")
    @Deprecated
    private ClusterDTO() {
        // Default constructor required by JAX-B.
        super();
    }

    public int getCount() {
        beforeGet();
        return count;
    }

    public void setCount(final int count) {
        beforeSet();
        this.count = count;
    }

    @Nullable
    public PointDTO getSouthWest() {
        beforeGet();
        return southWest;
    }

    public void setSouthWest(@Nullable final PointDTO southWest) {
        beforeSet();
        this.southWest = southWest;
    }

    @Nullable
    public PointDTO getNorthEast() {
        beforeGet();
        return northEast;
    }

    public void setNorthEast(@Nullable final PointDTO northEast) {
        beforeSet();
        this.northEast = northEast;
    }
}


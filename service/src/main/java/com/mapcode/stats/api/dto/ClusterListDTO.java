/*
 * Copyright (C) 2016-2021, Stichting Mapcode Foundation (http://www.mapcode.com)
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
import com.tomtom.speedtools.apivalidation.ApiListDTO;

import javax.annotation.Nonnull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@SuppressWarnings({"NonFinalFieldReferenceInEquals", "NonFinalFieldReferencedInHashCode", "NullableProblems", "EqualsWhichDoesntCheckParameterClass"})
@JsonInclude(Include.NON_EMPTY)
@XmlRootElement(name = "mapcodes")
@XmlAccessorType(XmlAccessType.FIELD)
public final class ClusterListDTO extends ApiListDTO<ClusterDTO> {

    @Override
    public void validateOne(@Nonnull final ClusterDTO elm) {
        validator().checkNotNullAndValidate(true, "mapcode", elm);
    }

    public ClusterListDTO(@Nonnull final List<ClusterDTO> mapcodes) {
        super(mapcodes);
    }

    @SuppressWarnings("UnusedDeclaration")
    @Deprecated
    private ClusterListDTO() {
        // Default constructor required by JAX-B.
        super();
    }
}

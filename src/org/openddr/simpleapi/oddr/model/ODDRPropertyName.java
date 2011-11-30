/**
 * Copyright 2011 OpenDDR LLC
 * This software is distributed under the terms of the GNU Lesser General Public License.
 *
 *
 * This file is part of OpenDDR Simple APIs.
 * OpenDDR Simple APIs is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 *
 * OpenDDR Simple APIs is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Simple APIs.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.openddr.simpleapi.oddr.model;

import org.w3c.ddr.simple.PropertyName;

public class ODDRPropertyName implements PropertyName {

    private String localPropertyName;
    private String namespace;

    public ODDRPropertyName(String localPropertyName, String namespace) {
        this.localPropertyName = localPropertyName;
        this.namespace = namespace;
    }

    public String getLocalPropertyName() {
        return this.localPropertyName;
    }

    public String getNamespace() {
        return this.namespace;
    }
}

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
import org.w3c.ddr.simple.PropertyRef;

public class ODDRPropertyRef implements PropertyRef {

    private final PropertyName pn;
    private final String aspectName;

    public ODDRPropertyRef(PropertyName pn, String string) {
        this.pn = pn;
        this.aspectName = string;
    }

    public String getLocalPropertyName() {
        return pn.getLocalPropertyName();
    }

    public String getAspectName() {
        return aspectName;
    }

    public String getNamespace() {
        return pn.getNamespace();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof ODDRPropertyRef)) {
            return false;
        }
        ODDRPropertyRef oddr = (ODDRPropertyRef) o;
        try {
            return (oddr.getAspectName().equals(this.getAspectName()) && oddr.getLocalPropertyName().equals(this.getLocalPropertyName()) && oddr.getNamespace().equals(this.getNamespace()));

        } catch (NullPointerException x) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 73 * hash + (this.pn != null ? this.pn.hashCode() : 0);
        hash = 73 * hash + (this.aspectName != null ? this.aspectName.hashCode() : 0);
        return hash;
    }
}

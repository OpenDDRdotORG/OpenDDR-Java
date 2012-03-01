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

import java.util.ArrayList;
import java.util.List;
import org.w3c.ddr.simple.PropertyRef;
import org.w3c.ddr.simple.PropertyValue;
import org.w3c.ddr.simple.PropertyValues;
import org.w3c.ddr.simple.exception.NameException;

public class ODDRPropertyValues implements PropertyValues {

    List<PropertyValue> properties;

    public ODDRPropertyValues() {
        this.properties = new ArrayList<PropertyValue>();
    }

    public void addProperty(PropertyValue v) {
        properties.add(v);
    }

    public PropertyValue[] getAll() {
        try {
            return properties.toArray(new PropertyValue[properties.size()]);

        } catch (NullPointerException x) {
            return new PropertyValue[0];
        }
    }

    public PropertyValue getValue(PropertyRef pr) throws NameException {
        for (PropertyValue propertyValue : properties) {
            if (propertyValue.getPropertyRef().equals(pr)) {
                return propertyValue;
            }
        }
        return null;
        //throw new NameException(NameException.PROPERTY_NOT_RECOGNIZED, new IllegalArgumentException());
    }
}

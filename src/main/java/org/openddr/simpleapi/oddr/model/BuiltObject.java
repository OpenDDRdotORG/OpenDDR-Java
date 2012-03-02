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

import java.util.HashMap;
import java.util.Map;

public class BuiltObject {

    protected int confidence;
    protected final Map<String, String> properties;

    public BuiltObject() {
        this.properties = new HashMap<String, String>();
        this.confidence = 0;
    }

    public BuiltObject(int confidence, Map<String, String> properties) {
        this.confidence = confidence;
        this.properties = properties;
    }

    public BuiltObject(Map<String, String> properties) {
        this.confidence = 0;
        this.properties = properties;
    }

    public int getConfidence() {
        return confidence;
    }

    public void setConfidence(int confidence) {
        this.confidence = confidence;
    }

    public String get(String property) {
        if (properties.containsKey(property)) {
            return properties.get(property);
        }
        return null;
    }

    public void putProperty(String name, String value) {
        this.properties.put(name, value);
    }

    public void putPropertiesMap(Map<String, String> properties) {
        this.properties.putAll(properties);
    }

    public Map<String, String> getPropertiesMap() {
        return properties;
    }
}

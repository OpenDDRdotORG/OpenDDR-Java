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
import org.w3c.ddr.simple.Evidence;

public class ODDRHTTPEvidence implements Evidence {

    Map<String, String> headers;

    public ODDRHTTPEvidence() {
        headers = new HashMap<String, String>();
    }

    public ODDRHTTPEvidence(Map<String, String> map) {
        headers = new HashMap<String, String>();
        headers.putAll(map);
    }

    public boolean exists(String string) {
        if (string == null) {
            return false;
        }
        return headers.containsKey(string.toLowerCase());
    }

    public String get(String key) {
        return headers.get(key.toLowerCase());
    }

    /**
     *
     * @param key case insensitive
     * @param value case sensitive
     */
    public void put(String key, String value) {
        headers.put(key.toLowerCase(), value);
    }
}

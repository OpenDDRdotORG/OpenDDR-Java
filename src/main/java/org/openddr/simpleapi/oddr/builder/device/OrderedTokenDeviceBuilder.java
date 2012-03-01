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
package org.openddr.simpleapi.oddr.builder.device;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.openddr.simpleapi.oddr.model.device.Device;

public abstract class OrderedTokenDeviceBuilder implements DeviceBuilder {

    protected LinkedHashMap<String, Object> orderedRules;

    public OrderedTokenDeviceBuilder() {
        orderedRules = new LinkedHashMap<String, Object>();
    }

    abstract protected void afterOderingCompleteInit(Map<String, Device> devices);

    public final void completeInit(Map<String, Device> devices) {
        LinkedHashMap<String, Object> tmp = new LinkedHashMap<String, Object>();
        ArrayList<String> keys = new ArrayList<String>(orderedRules.keySet());
        Collections.sort(keys, new Comparator<String>() {

            public int compare(String o1, String o2) {
                return o2.length() - o1.length();
            }
        });
        for (String string : keys) {
            tmp.put(string, orderedRules.get(string));
        }
        ArrayList<String> keysOrdered = new ArrayList<String>();

        orderedRules = new LinkedHashMap();

        while (keys.size() > 0) {
            boolean found = false;
            for (String k1 : keys) {
                for (String k2 : keys) {
                    if ((!k1.equals(k2)) && k2.matches(".*" + k1 + ".*")) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    keysOrdered.add(k1);
                    keys.remove(k1);
                    break;
                }
            }
            if (!found) {
                continue;
            }
            int max = 0;
            int idx = -1;
            for (int i = 0; i < keys.size(); i++) {
                String string = keys.get(i);
                if (string.length() > max) {
                    max = string.length();
                    idx = i;
                }
            }
            if (idx >= 0) {
                keysOrdered.add(keys.get(idx));
                keys.remove(idx);
            }
        }
        for (String key : keysOrdered) {
            orderedRules.put(key, tmp.get(key));
            tmp.remove(key);
        }

        afterOderingCompleteInit(devices);
    }
}

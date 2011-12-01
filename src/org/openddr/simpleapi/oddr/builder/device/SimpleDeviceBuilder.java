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

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.openddr.simpleapi.oddr.model.BuiltObject;
import org.openddr.simpleapi.oddr.model.UserAgent;
import org.openddr.simpleapi.oddr.model.device.Device;

public class SimpleDeviceBuilder implements DeviceBuilder {

    private LinkedHashMap<String, String> simpleTokenMap;
    private Map<String, Device> devices;

    public SimpleDeviceBuilder() {
        simpleTokenMap = new LinkedHashMap<String, String>();
    }

    public void putDevice(String deviceId, List<String> initProperties) {

        for (String token : initProperties) {
            simpleTokenMap.put(token, deviceId);
        }
    }

    public void completeInit(Map<String, Device> devices) {
        this.devices = devices;

        for (String deviceID : simpleTokenMap.values()) {
            if (!devices.containsKey(deviceID)) {
                throw new IllegalStateException("unable to find device with id: " + deviceID + "in devices");
            }
        }
    }

    public boolean canBuild(UserAgent userAgent) {
        for (String token : simpleTokenMap.keySet()) {
            if (userAgent.getCompleteUserAgent().matches("(?i).*" + Pattern.quote(token) + ".*")) {
                return true;
            }
        }
        return false;
    }

    public BuiltObject build(UserAgent userAgent, int confidenceTreshold) {
        Iterator it = simpleTokenMap.keySet().iterator();
        while (it.hasNext()) {
            String token = (String) it.next();
            if (userAgent.getCompleteUserAgent().matches("(?i).*" + Pattern.quote(token) + ".*")) {
                String desktopDeviceId = simpleTokenMap.get(token);
                if (desktopDeviceId != null) {
                    Device device = devices.get(desktopDeviceId);
                    return device;
                }
            }
        }
        return null;
    }
}

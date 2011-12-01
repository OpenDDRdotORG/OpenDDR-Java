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
import org.openddr.simpleapi.oddr.model.device.Device;
import org.openddr.simpleapi.oddr.model.UserAgent;

public class IOSDeviceBuilder implements DeviceBuilder {

    private LinkedHashMap<String, String> iOSDevices;
    private Map<String, Device> devices;

    public IOSDeviceBuilder() {
        iOSDevices = new LinkedHashMap<String, String>();
    }

    public boolean canBuild(UserAgent userAgent) {
        if (userAgent.containsIOSDevices() && (!userAgent.containsAndroid()) && (!userAgent.containsWindowsPhone())) {
            return true;

        } else {
            return false;
        }
    }

    public Device build(UserAgent userAgent, int confidenceTreshold) {
        Iterator it = iOSDevices.keySet().iterator();
        while (it.hasNext()) {
            String token = (String) it.next();
            if (userAgent.getCompleteUserAgent().matches(".*" + token + ".*")) {
                String iosDeviceID = iOSDevices.get(token);
                if (iosDeviceID != null) {
                    Device retDevice = (Device) devices.get(iosDeviceID).clone();
                    retDevice.setConfidence(90);
                    return retDevice;
                }
            }
        }
        return null;
    }

    public void putDevice(String device, List<String> initProperties) {
        iOSDevices.put(initProperties.get(0), device);
    }

    public void completeInit(Map<String, Device> devices) {
        String global = "iPhone";
        if (iOSDevices.containsKey(global)) {
            String iphone = iOSDevices.get(global);
            iOSDevices.remove(global);
            iOSDevices.put(global, iphone);
        }

        this.devices = devices;

        for (String deviceID : iOSDevices.values()) {
            if (!devices.containsKey(deviceID)) {
                throw new IllegalStateException("unable to find device with id: " + deviceID + "in devices");
            }
        }
    }
}

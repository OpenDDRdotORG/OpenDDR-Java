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
package org.openddr.simpleapi.oddr.identificator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.openddr.simpleapi.oddr.builder.device.DeviceBuilder;
import org.openddr.simpleapi.oddr.model.device.Device;
import org.openddr.simpleapi.oddr.model.UserAgent;
import org.openddr.simpleapi.oddr.model.UserAgentFactory;
import org.w3c.ddr.simple.Evidence;

public class DeviceIdentificator implements Identificator {

    private DeviceBuilder[] builders;
    private Map<String, Device> devices;

    public DeviceIdentificator(DeviceBuilder[] builders, Map<String, Device> devices) {
        this.builders = builders;
        this.devices = devices;
    }

    public Device get(String userAgent, int confidenceTreshold) {
        return get(UserAgentFactory.newUserAgent(userAgent), confidenceTreshold);
    }

    //XXX to be refined, this should NOT be the main entry point, we should use a set of evidence derivation
    public Device get(Evidence evdnc, int threshold) {
        UserAgent ua = UserAgentFactory.newDeviceUserAgent(evdnc);
        if (ua != null) {
            return get(ua, threshold);
        }
        return null;
    }

    public Device get(UserAgent userAgent, int confidenceTreshold) {
        List<Device> foundDevices = new ArrayList<Device>();
        Device foundDevice = null;
        for (DeviceBuilder deviceBuilder : builders) {
            if (deviceBuilder.canBuild(userAgent)) {
                Device device = (Device) deviceBuilder.build(userAgent, confidenceTreshold);
                if (device != null) {
                    String parentId = device.getParentId();
                    Device parentDevice = null;
                    Set propertiesSet = null;
                    Iterator it = null;
                    while (!"root".equals(parentId)) {
                        parentDevice = (Device) devices.get(parentId);
                        propertiesSet = parentDevice.getPropertiesMap().entrySet();
                        it = propertiesSet.iterator();
                        while (it.hasNext()) {
                            Map.Entry entry = (Map.Entry) it.next();
                            if (!device.containsProperty((String) entry.getKey())) {
                                device.putProperty((String) entry.getKey(), (String) entry.getValue());
                            }
                        }
                        parentId = parentDevice.getParentId();
                    }
                    foundDevices.add(device);
                    if (device.getConfidence() >= confidenceTreshold) {
                        foundDevice = device;
                        break;
                    }
                }
            }
        }

        if (foundDevice != null) {
            return foundDevice;

        } else {
            if (foundDevices.isEmpty()) {
                return null;
            }

            Collections.sort(foundDevices, Collections.reverseOrder());
            return foundDevices.get(0);
        }
    }

    public void completeInit() {
        for (DeviceBuilder deviceBuilder : builders) {
            deviceBuilder.completeInit(devices);
        }
    }
}

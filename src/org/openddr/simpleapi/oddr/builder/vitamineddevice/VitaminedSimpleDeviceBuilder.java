/**
 * Copyright 2012 Fundación CTIC
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
 * @author José Quiroga Álvarez
 * @author Diego Martínez Ballesteros
 *
 */
package org.openddr.simpleapi.oddr.builder.vitamineddevice;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.openddr.simpleapi.oddr.builder.device.DeviceBuilder;
import org.openddr.simpleapi.oddr.model.BuiltObject;
import org.openddr.simpleapi.oddr.model.UserAgent;
import org.openddr.simpleapi.oddr.model.device.Device;

public class VitaminedSimpleDeviceBuilder extends VitaminedDeviceBuilder {

    private LinkedHashMap<String, String> simpleTokenMap;
    private Map<String, Device> devices;

    public VitaminedSimpleDeviceBuilder() {
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
            else
            	tokensCache.put(deviceID, new Token(deviceID));
        }
    }

    public boolean canBuild(UserAgent userAgent) {
        for (String token : simpleTokenMap.keySet()) {
        	Token tokenInstance = tokensCache.get(token);
            if (tokenInstance.getQuotePattern().matcher(userAgent.getCompleteUserAgent()).matches()) {
                return true;
            }
        }
        return false;
    }

    public BuiltObject build(UserAgent userAgent, int confidenceTreshold) {
        Iterator it = simpleTokenMap.keySet().iterator();
        while (it.hasNext()) {
        	String tokenString = (String) it.next();
            Token tokenInstance = tokensCache.get(tokenString); //get the token by key
            if (tokenInstance.getQuotePattern().matcher(userAgent.getCompleteUserAgent()).matches()) {
                String desktopDeviceId = simpleTokenMap.get(tokenString);
                if (desktopDeviceId != null) {
                    Device device = devices.get(desktopDeviceId);
                    return device;
                }
            }
        }
        return null;
    }
}

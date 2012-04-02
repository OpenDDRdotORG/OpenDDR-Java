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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openddr.simpleapi.oddr.model.device.Device;
import org.openddr.simpleapi.oddr.model.UserAgent;

public class AndroidDeviceBuilder extends OrderedTokenDeviceBuilder {

    private static final String BUILD_HASH_REGEXP = ".*Build/([^ \\)\\(]*).*";
    private Pattern buildHashPattern = Pattern.compile(BUILD_HASH_REGEXP);
    private Map<String, Device> devices;

    public AndroidDeviceBuilder() {
        super();
    }

    public boolean canBuild(UserAgent userAgent) {
        if (userAgent.containsAndroid()) {
            return true;

        } else {
            return false;
        }
    }

    public Device build(UserAgent userAgent, int confidenceTreshold) {
        ArrayList<Device> foundDevices = new ArrayList<Device>();
        Iterator it = orderedRules.keySet().iterator();
        while (it.hasNext()) {
            String token = (String) it.next();
            Device d = elaborateAndroidDeviceWithToken(userAgent, token);
            if (d != null) {
                if (d.getConfidence() > confidenceTreshold) {
                    return d;

                } else {
                    if (d.getConfidence() > 0) {
                        foundDevices.add(d);
                    }
                }
            }
        }
        if (foundDevices.size() > 0) {
            Collections.sort(foundDevices, Collections.reverseOrder());
            return foundDevices.get(0);
        }
        return null;
    }

    public void putDevice(String deviceID, List<String> initProperties) {
        orderedRules.put(initProperties.get(0), deviceID);
    }

    private Device elaborateAndroidDeviceWithToken(UserAgent userAgent, String token) {
        if (userAgent.hasMozillaPattern() || userAgent.hasOperaPattern()) {
            int subtract = 0;
            String currentToken = token;

            String looseToken = token.replaceAll("[ _/-]", ".?");

            Pattern loosePattern = Pattern.compile("(?i).*" + looseToken + ".*");

            if (!loosePattern.matcher(userAgent.getCompleteUserAgent().replaceAll("Android", "")).matches()) {
                return null;
            }

            String patternElementInsideClean = cleanPatternElementInside(userAgent.getPatternElementsInside());

            Pattern currentPattern = null;

            for (int i = 0; i <= 1; i++) {
                if (i == 1) {
                    currentToken = looseToken;
                }

                currentPattern = Pattern.compile("(?i).*" + currentToken + ".?Build/.*");
                if (patternElementInsideClean != null && currentPattern.matcher(patternElementInsideClean).matches()) {//&& userAgent.getPatternElementsInside().matches(".*" + currentToken + ".?Build/.*")) {
                    String deviceId = (String) orderedRules.get(token);

                    try {
                        Device retDevice = (Device) devices.get(deviceId).clone();
                        retDevice.setConfidence(100 - subtract);
                        return retDevice;

                    } catch (NullPointerException x) {
                    }
                }

                currentPattern = Pattern.compile("(?i).*" + currentToken);
                if (userAgent.getPatternElementsPre() != null && currentPattern.matcher(userAgent.getPatternElementsPre()).matches()) {//userAgent.getPatternElementsPre().matches(".*" + currentToken)) {
                    String deviceId = (String) orderedRules.get(token);

                    try {
                        Device retDevice = (Device) devices.get(deviceId).clone();
                        retDevice.setConfidence(95 - subtract);
                        return retDevice;

                    } catch (NullPointerException x) {
                    }
                }

                if (patternElementInsideClean != null && currentPattern.matcher(patternElementInsideClean).matches()) {//userAgent.getPatternElementsInside().matches(".*" + currentToken)) {
                    String deviceId = (String) orderedRules.get(token);

                    try {
                        Device retDevice = (Device) devices.get(deviceId).clone();
                        retDevice.setConfidence(90 - subtract);
                        return retDevice;

                    } catch (NullPointerException x) {
                    }
                }

                currentPattern = Pattern.compile("(?i).*" + currentToken + ".?;.*");
                if (patternElementInsideClean != null && currentPattern.matcher(patternElementInsideClean).matches()) {//userAgent.getPatternElementsInside().matches(".*" + currentToken + ".?;.*")) {
                    String deviceId = (String) orderedRules.get(token);

                    try {
                        Device retDevice = (Device) devices.get(deviceId).clone();
                        retDevice.setConfidence(90 - subtract);
                        return retDevice;

                    } catch (NullPointerException x) {
                    }
                }

                if (i == 1) {
                    currentPattern = loosePattern;

                } else {
                    currentPattern = Pattern.compile("(?i).*" + currentToken + ".*");
                }
                if (patternElementInsideClean != null && currentPattern.matcher(patternElementInsideClean).matches()) {//userAgent.getPatternElementsInside().matches(".*" + currentToken + ".*")) {
                    String deviceId = (String) orderedRules.get(token);

                    try {
                        Device retDevice = (Device) devices.get(deviceId).clone();
                        retDevice.setConfidence(80 - subtract);
                        return retDevice;

                    } catch (NullPointerException x) {
                    }
                }
                if (userAgent.getPatternElementsPre() != null && currentPattern.matcher(userAgent.getPatternElementsPre()).matches()) {//userAgent.getPatternElementsPre().matches(".*" + currentToken + ".*")) {
                    String deviceId = (String) orderedRules.get(token);

                    try {
                        Device retDevice = (Device) devices.get(deviceId).clone();
                        retDevice.setConfidence(80 - subtract);
                        return retDevice;

                    } catch (NullPointerException x) {
                    }
                }
                if (userAgent.getPatternElementsPost() != null && currentPattern.matcher(userAgent.getPatternElementsPost()).matches()) {//userAgent.getPatternElementsPost().matches(".*" + currentToken + ".*")) {
                    String deviceId = (String) orderedRules.get(token);
                    
                    try {
                        Device retDevice = (Device) devices.get(deviceId).clone();
                        retDevice.setConfidence(60 - subtract);
                        return retDevice;

                    } catch (NullPointerException x) {
                    }
                }
                if (i == 1) {
                    if (userAgent.getPatternElementsInside() != null && currentPattern.matcher(userAgent.getPatternElementsInside()).matches()) {//userAgent.getPatternElementsInside().matches(".*" + currentToken + ".*")) {
                        String deviceId = (String) orderedRules.get(token);

                        try {
                            Device retDevice = (Device) devices.get(deviceId).clone();
                            retDevice.setConfidence(40);
                            return retDevice;

                        } catch (NullPointerException x) {
                        }
                    }
                }
                subtract += 20;
            }
        }

        return null;
    }

    @Override
    protected void afterOderingCompleteInit(Map<String, Device> devices) {
        this.devices = devices;
        for (Object devIdObj : orderedRules.values()) {
            String devId = (String) devIdObj;
            if (!devices.containsKey(devId)) {
                throw new IllegalStateException("unable to find device with id: " + devId + "in devices");
            }
        }
    }

    private String cleanPatternElementInside(String patternElementsInside) {
        String patternElementInsideClean = patternElementsInside;

        Matcher buildHashMatcher = buildHashPattern.matcher(patternElementInsideClean);

        if (buildHashMatcher.find()) {
            String build = buildHashMatcher.group(1);
            patternElementInsideClean = patternElementInsideClean.replaceAll("Build/" + build, "Build/");
        }
        patternElementInsideClean = patternElementInsideClean.replaceAll("Android", "");
        
        return patternElementInsideClean;
    }
}

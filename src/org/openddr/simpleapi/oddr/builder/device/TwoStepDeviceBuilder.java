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
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openddr.simpleapi.oddr.model.device.Device;
import org.openddr.simpleapi.oddr.model.UserAgent;

public class TwoStepDeviceBuilder extends OrderedTokenDeviceBuilder {

    private Map<String, Device> devices;

    public TwoStepDeviceBuilder() {
        super();
    }

    public boolean canBuild(UserAgent userAgent) {
        for (String step1Token : orderedRules.keySet()) {
            if (userAgent.getCompleteUserAgent().matches("(?i).*" + step1Token + ".*")) {
                return true;
            }
        }
        return false;
    }

    public Device build(UserAgent userAgent, int confidenceTreshold) {
        ArrayList<Device> foundDevices = new ArrayList<Device>();
        for (String step1Token : orderedRules.keySet()) {
            if (userAgent.getCompleteUserAgent().matches("(?i).*" + step1Token + ".*")) {
                Map<String, String> step1Compliant = (Map<String, String>) orderedRules.get(step1Token);
                for (String step2token : step1Compliant.keySet()) {
                    Device d = elaborateTwoStepDeviceWithToken(userAgent, step1Token, step2token);
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
            }
        }
        if (foundDevices.size() > 0) {
            Collections.sort(foundDevices, Collections.reverseOrder());
            return foundDevices.get(0);
        }

        return null;
    }

    public void putDevice(String device, List<String> initProperties) {
        String step1TokenString = initProperties.get(0);
        String step2TokenString = initProperties.get(1);

        if (step2TokenString.matches(".*" + step1TokenString + ".*")) {
            step2TokenString = step2TokenString.replaceAll(step1TokenString + "[^\\p{Alnum}]?", "");
        }

        Map<String, String> step1Token = (Map<String, String>) orderedRules.get(step1TokenString);
        if (step1Token == null) {
            step1Token = new LinkedHashMap<String, String>();
            orderedRules.put(initProperties.get(0), step1Token);
        }
        step1Token.put(step2TokenString, device);
    }

    private Device elaborateTwoStepDeviceWithToken(UserAgent userAgent, String step1Token, String step2Token) {
        int maxLittleTokensDistance = 4;
        int maxBigTokensDistance = 8;
        int confidence;

        String originalToken = step2Token;
        String looseToken = step2Token.replaceAll("[ _/-]", ".?");

        if (userAgent.getCompleteUserAgent().matches("(?i).*" + step2Token + ".*")) {
            confidence = 100;

        } else if (userAgent.getCompleteUserAgent().matches("(?i).*" + looseToken + ".*")) {
            step2Token = looseToken;
            confidence = 90;

        } else {
            return null;
        }

        Matcher result = Pattern.compile("(?i)(?:(?:.*" + step1Token + "(.*?)" + step2Token + ".*)|(?:.*" + step2Token + "(.*?)" + step1Token + ".*))").matcher(userAgent.getCompleteUserAgent());
        if (result.matches()) {
            //test for case sensitive match
            Matcher bestResult = Pattern.compile("(?:(?:.*" + step1Token + "(.*?)" + step2Token + ".*)|(?:.*" + step2Token + "(.*?)" + step1Token + ".*))").matcher(userAgent.getCompleteUserAgent());
            if (bestResult.matches()) {
                result = bestResult;

            } else {
                confidence -= 20;
            }

            String betweenTokens = result.group(1);
            String s2 = result.group(2);
            if (s2 != null && (betweenTokens == null || betweenTokens.length() > s2.length())) {
                betweenTokens = s2;
            }
            int betweenTokensLength = betweenTokens.length();

            if (step2Token.length() > 3) {
                if (betweenTokensLength > maxBigTokensDistance) {
                    confidence -= 10;
                }

                String deviceId = ((Map<String, String>) orderedRules.get(step1Token)).get(originalToken);

                try {
                    Device retDevice = (Device) devices.get(deviceId).clone();
                    retDevice.setConfidence(confidence);
                    return retDevice;

                } catch (NullPointerException x) {
                }
            }

            if ((betweenTokensLength < maxLittleTokensDistance) || (betweenTokensLength < maxBigTokensDistance && (step2Token.length() < 6 || !step2Token.matches(".*[a-zA-Z].*")))) {
                if (betweenTokensLength <= 1) {
                    if (!betweenTokens.matches(".*[ _/-].*")) {
                        confidence -= 20;
                    }

                } else {
                    confidence -= 40;
                }

                confidence -= (betweenTokensLength * 4);

                String deviceId = ((Map<String, String>) orderedRules.get(step1Token)).get(originalToken);

                try {
                    Device retDevice = (Device) devices.get(deviceId).clone();
                    retDevice.setConfidence(confidence);
                    return retDevice;

                } catch (NullPointerException x) {
                }
            }
        }

        return null;
    }

    @Override
    protected void afterOderingCompleteInit(Map<String, Device> devices) {
        for (String step1Token : orderedRules.keySet()) {
            sortElement(step1Token);
        }

        this.devices = devices;
        for (Object devIdsMapObj : orderedRules.values()) {
            Map<String, String> devIdsMap = (Map<String, String>) devIdsMapObj;
            for (Object devIdObj : devIdsMap.values()) {
                String devId = (String) devIdObj;
                if (!devices.containsKey(devId)) {
                    throw new IllegalStateException("unable to find device with id: " + devId + "in devices");
                }
            }
        }
    }

    private void sortElement(String step1Token) {
        Map<String, String> currentStep1Map = (Map<String, String>) orderedRules.get(step1Token);

        LinkedHashMap<String, String> tmp = new LinkedHashMap<String, String>();
        ArrayList<String> keys = new ArrayList<String>(currentStep1Map.keySet());
        Collections.sort(keys, new Comparator<String>() {

            public int compare(String o1, String o2) {
                return o2.length() - o1.length();
            }
        });
        for (String string : keys) {
            tmp.put(string, currentStep1Map.get(string));
        }
        ArrayList<String> keysOrdered = new ArrayList<String>();
        currentStep1Map = new LinkedHashMap();
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
            currentStep1Map.put(key, tmp.get(key));
            tmp.remove(key);
        }

        orderedRules.put(step1Token, currentStep1Map);
    }
}

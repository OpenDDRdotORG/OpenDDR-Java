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

import java.util.Map;
import org.openddr.simpleapi.oddr.builder.Builder;
import org.openddr.simpleapi.oddr.model.UserAgent;
import org.openddr.simpleapi.oddr.model.UserAgentFactory;
import org.openddr.simpleapi.oddr.model.os.OperatingSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.ddr.simple.Evidence;

public class OSIdentificator implements Identificator {

    private Builder[] builders;
    private Map<String, OperatingSystem> operatingSystemCapabilities;
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    public OSIdentificator(Builder[] builders, Map<String, OperatingSystem> operatingSystemCapabilities) {
        this.builders = builders;
        this.operatingSystemCapabilities = operatingSystemCapabilities;
    }

    public OperatingSystem get(String userAgent, int confidenceTreshold) {
        return get(UserAgentFactory.newUserAgent(userAgent), confidenceTreshold);
    }

    //XXX to be refined, this should NOT be the main entry point, we should use a set of evidence derivation
    public OperatingSystem get(Evidence evdnc, int threshold) {
        UserAgent ua = UserAgentFactory.newDeviceUserAgent(evdnc);
        if (ua != null) {
            return get(ua, threshold);
        }
        return null;
    }

    public OperatingSystem get(UserAgent userAgent, int confidenceTreshold) {
        for (Builder builder : builders) {
            if (builder.canBuild(userAgent)) {
                OperatingSystem os = (OperatingSystem) builder.build(userAgent, confidenceTreshold);
                if (os != null) {
                    if (operatingSystemCapabilities != null) {
                        String bestID = getClosestKnownBrowserID(os.getId());
                        if (bestID != null) {
                            os.putPropertiesMap(operatingSystemCapabilities.get(bestID).getPropertiesMap());
                            if (!bestID.equals(os.getId())) {
                                os.setConfidence(os.getConfidence() - 15);
                            }
                        }
                    }
                    return os;
                }
            }
        }
        return null;
    }

    private String getClosestKnownBrowserID(String actualOperatingSystemID) {
        if (actualOperatingSystemID == null) {
            return null;
        }

        int idx = actualOperatingSystemID.indexOf(".");

        if (idx < 0) {
            logger.error("SHOULD NOT BE HERE, PLEASE CHECK BROWSER DOCUMENT(1)");
            logger.debug(actualOperatingSystemID);
            return null;

        } else {
            idx++;
        }
        idx = actualOperatingSystemID.indexOf(".", idx);

        if (idx < 0) {
            logger.error("SHOULD NOT BE HERE, PLEASE CHECK BROWSER DOCUMENT(2)" + idx);
            logger.debug(actualOperatingSystemID);
            return null;

        } else {
            idx++;
        }

        String bestID = null;
        for (String listOperatingSystemID : operatingSystemCapabilities.keySet()) {
            if (listOperatingSystemID.equals(actualOperatingSystemID)) {
                return actualOperatingSystemID;
            }

            if (listOperatingSystemID.length() > idx && listOperatingSystemID.substring(0, idx).equals(actualOperatingSystemID.substring(0, idx))) {
                if (listOperatingSystemID.compareTo(actualOperatingSystemID) <= 0) {
                    bestID = listOperatingSystemID;
                }
            }
        }

        return bestID;
    }

    public void completeInit() {
        //does nothing
    }
}

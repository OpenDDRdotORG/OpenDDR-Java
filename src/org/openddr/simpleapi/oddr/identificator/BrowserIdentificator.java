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
import org.openddr.simpleapi.oddr.model.browser.Browser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.ddr.simple.Evidence;

public class BrowserIdentificator implements Identificator {

    private Builder[] builders;
    private Map<String, Browser> browserCapabilities;
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    public BrowserIdentificator(Builder[] builders, Map<String, Browser> browserCapabilities) {
        this.builders = builders;
        this.browserCapabilities = browserCapabilities;
    }

    public Browser get(String userAgent, int confidenceTreshold) {
        return get(UserAgentFactory.newUserAgent(userAgent), confidenceTreshold);
    }

    //XXX to be refined, this should NOT be the main entry point, we should use a set of evidence derivation
    public Browser get(Evidence evdnc, int threshold) {
        UserAgent ua = UserAgentFactory.newBrowserUserAgent(evdnc);

        if (ua != null) {
            return get(ua, threshold);
        }

        return null;
    }

    public Browser get(UserAgent userAgent, int confidenceTreshold) {
        for (Builder builder : builders) {
            if (builder.canBuild(userAgent)) {
                Browser browser = (Browser) builder.build(userAgent, confidenceTreshold);
                if (browser != null) {
                    if (browserCapabilities != null) {
                        String bestID = getClosestKnownBrowserID(browser.getId());
                        if (bestID != null) {
                            browser.putPropertiesMap(browserCapabilities.get(bestID).getPropertiesMap());
                            if (!bestID.equals(browser.getId())) {
                                browser.setConfidence(browser.getConfidence() - 15);
                            }
                        }
                    }
                    return browser;
                }
            }
        }

        return null;
    }

    private String getClosestKnownBrowserID(String actualBrowserID) {
        if (actualBrowserID == null) {
            return null;
        }

        int idx = actualBrowserID.indexOf(".");

        if (idx < 0) {
            logger.error("SHOULD NOT BE HERE, PLEASE CHECK BROWSER DOCUMENT(1)");
            logger.debug(actualBrowserID);
            return null;

        } else {
            idx++;
        }
        idx = actualBrowserID.indexOf(".", idx);

        if (idx < 0) {
            logger.error("SHOULD NOT BE HERE, PLEASE CHECK BROWSER DOCUMENT(2)" + idx);
            logger.debug(actualBrowserID);
            return null;

        } else {
            idx++;
        }

        String bestID = null;
        for (String listBrowserID : browserCapabilities.keySet()) {
            if (listBrowserID.equals(actualBrowserID)) {
                return actualBrowserID;
            }

            if (listBrowserID.length() > idx && listBrowserID.substring(0, idx).equals(actualBrowserID.substring(0, idx))) {
                if (listBrowserID.compareTo(actualBrowserID) <= 0) {
                    bestID = listBrowserID;
                }
            }
        }

        return bestID;
    }

    public void completeInit() {
        //does nothing
    }
}

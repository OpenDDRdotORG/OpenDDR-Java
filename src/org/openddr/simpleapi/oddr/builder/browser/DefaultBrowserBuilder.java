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
package org.openddr.simpleapi.oddr.builder.browser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.openddr.simpleapi.oddr.builder.Builder;
import org.openddr.simpleapi.oddr.model.UserAgent;
import org.openddr.simpleapi.oddr.model.browser.Browser;

public class DefaultBrowserBuilder implements Builder {

    private Builder[] builders;
    private static DefaultBrowserBuilder instance;

    public static synchronized DefaultBrowserBuilder getInstance() {
        if (instance == null) {
            instance = new DefaultBrowserBuilder();
        }
        return instance;
    }

    private DefaultBrowserBuilder() {

        builders = new Builder[]{
                    new OperaMiniBrowserBuilder(),
                    new SafariMobileBrowserBuilder(),
                    new AndroidMobileBrowserBuilder(),
                    new NetFrontBrowserBuilder(),
                    new UPBrowserBuilder(),
                    new OpenWaveBrowserBuilder(),
                    new SEMCBrowserBuilder(),
                    new DolfinBrowserBuilder(),
                    new JasmineBrowserBuilder(),
                    new PolarisBrowserBuilder(),
                    new ObigoBrowserBuilder(),
                    new OperaBrowserBuilder(),
                    new IEMobileBrowserBuilder(),
                    new NokiaBrowserBuilder(),
                    new BlackBerryBrowserBuilder(),
                    new FennecBrowserBuilder(),
                    new InternetExplorerBrowserBuilder(),
                    new ChromeBrowserBuilder(),
                    new FirefoxBrowserBuilder(),
                    new SafariBrowserBuilder(),
                    new KonquerorBrowserBuilder(),};
    }

    public boolean canBuild(UserAgent userAgent) {
        for (Builder browserBuilder : builders) {
            if (browserBuilder.canBuild(userAgent)) {
                return true;
            }
        }
        return false;
    }

    public Browser build(UserAgent userAgent, int confidenceTreshold) {
        List<Browser> founds = new ArrayList<Browser>();
        Browser found = null;
        for (Builder builder : builders) {
            if (builder.canBuild(userAgent)) {
                Browser builded = (Browser) builder.build(userAgent, confidenceTreshold);
                if (builded != null) {
                    founds.add(builded);
                    if (builded.getConfidence() >= confidenceTreshold) {
                        found = builded;
                        break;
                    }
                }
            }
        }

        if (found != null) {
            return found;

        } else {
            if (founds.isEmpty()) {
                return null;
            }

            Collections.sort(founds, Collections.reverseOrder());
            return founds.get(0);
        }
    }
}

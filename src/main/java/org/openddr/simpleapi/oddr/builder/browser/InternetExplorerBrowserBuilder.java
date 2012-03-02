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

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openddr.simpleapi.oddr.model.UserAgent;
import org.openddr.simpleapi.oddr.model.browser.Browser;

public class InternetExplorerBrowserBuilder extends LayoutEngineBrowserBuilder {

    private static final String MSIE_VERSION_REGEXP = ".*MSIE.([0-9\\.b]+).*";
    private static final String DOT_NET_CLR_REGEXP = ".*\\.NET.CLR.*";
    private Pattern msieVersionPattern = Pattern.compile(MSIE_VERSION_REGEXP);
    private Pattern dotNetClrPattern = Pattern.compile(DOT_NET_CLR_REGEXP);

    public boolean canBuild(UserAgent userAgent) {
        return (userAgent.containsMSIE());
    }

    @Override
    protected Browser buildBrowser(UserAgent userAgent, String layoutEngine, String layoutEngineVersion, int hintedWidth, int hintedHeight) {
        if (!userAgent.containsMSIE() || !(userAgent.hasMozillaPattern()) || userAgent.getCompleteUserAgent().matches(".*Windows.?(?:(?:CE)|(?:Phone)).*")) {
            return null;
        }

        int confidence = 60;
        Browser identified = new Browser();

        identified.setVendor("Microsoft");
        identified.setModel("Internet Explorer");

        Matcher msieMatcher = msieVersionPattern.matcher(userAgent.getCompleteUserAgent());
        if (msieMatcher.matches()) {
            if (msieMatcher.group(1) != null) {
                identified.setVersion(msieMatcher.group(1));
                String version[] = msieMatcher.group(1).split("\\.");

                if (version.length > 0) {
                    identified.setMajorRevision(version[0]);
                    if (identified.getMajorRevision().length() == 0) {
                        identified.setMajorRevision("1");
                    }
                }

                if (version.length > 1) {
                    identified.setMinorRevision(version[1]);
                    confidence += 10;
                }
            }
            
        } else {
            //fallback version
            identified.setVersion("1.0");
            identified.setMajorRevision("1");
        }

        if (layoutEngine != null) {
            identified.setLayoutEngine(layoutEngine);
            identified.setLayoutEngineVersion(layoutEngineVersion);
            if (layoutEngine.equals(LayoutEngineBrowserBuilder.TRIDENT)) {
                confidence += 10;
            }
        }

        Matcher dotNetClrMatcher = dotNetClrPattern.matcher(userAgent.getCompleteUserAgent());
        if (dotNetClrMatcher.matches()) {
            confidence += 10;
        }

        identified.setDisplayWidth(hintedWidth);
        identified.setDisplayHeight(hintedHeight);
        identified.setConfidence(confidence);

        return identified;
    }
}

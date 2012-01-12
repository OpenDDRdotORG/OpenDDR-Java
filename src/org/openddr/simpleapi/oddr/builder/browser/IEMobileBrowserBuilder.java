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

public class IEMobileBrowserBuilder extends LayoutEngineBrowserBuilder {

    private static final String VERSION_REGEXP = ".*[^MS]IEMobile.([0-9\\.]+).*?";
    private static final String MSIE_VERSION_REGEXP = ".*MSIE.([0-9\\.]+).*";
    private static final String MSIEMOBILE_VERSION_REGEXP = ".*MSIEMobile.([0-9\\.]+).*";
    private Pattern versionPattern = Pattern.compile(VERSION_REGEXP);
    private Pattern msieVersionPattern = Pattern.compile(MSIE_VERSION_REGEXP);
    private Pattern msieMobileVersionPattern = Pattern.compile(MSIEMOBILE_VERSION_REGEXP);

    public boolean canBuild(UserAgent userAgent) {
        return (userAgent.containsWindowsPhone());
    }

    @Override
    protected Browser buildBrowser(UserAgent userAgent, String layoutEngine, String layoutEngineVersion, int hintedWidth, int hintedHeight) {
        if (!userAgent.containsWindowsPhone() || !(userAgent.getCompleteUserAgent().matches(".*Windows.?(?:(?:CE)|(?:Phone)).*"))) {
            return null;
        }

        int confidence = 40;
        Browser identified = new Browser();

        identified.setVendor("Microsoft");
        identified.setModel("IEMobile");

        if (userAgent.getCompleteUserAgent().contains("MSIEMobile")) {
            confidence += 10;
        }

        if (userAgent.hasMozillaPattern()) {
            confidence += 10;
        }

        Matcher versionMatcher = versionPattern.matcher(userAgent.getCompleteUserAgent());
        if (versionMatcher.matches()) {
            if (versionMatcher.group(1) != null) {
                identified.setVersion(versionMatcher.group(1));
                String version[] = versionMatcher.group(1).split("\\.");

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

                if (version.length > 2) {
                    identified.setMicroRevision(version[2]);
                }

                if (version.length > 3) {
                    identified.setNanoRevision(version[3]);
                }
            }

        } else {
            //fallback version
            identified.setVersion("1.0");
            identified.setMajorRevision("1");
        }

        Matcher msieMatcher = msieVersionPattern.matcher(userAgent.getCompleteUserAgent());
        if (msieMatcher.matches()) {
            if (msieMatcher.group(1) != null) {
                identified.setReferenceBrowser("MSIE");
                identified.setReferenceBrowserVersion(msieMatcher.group(1));
                confidence += 10;
            }
        }

        Matcher msieMobileMatcher = msieMobileVersionPattern.matcher(userAgent.getCompleteUserAgent());
        if (msieMobileMatcher.matches()) {
            if (msieMobileMatcher.group(1) != null) {
                identified.setLayoutEngine("MSIEMobile");
                identified.setLayoutEngineVersion(msieMobileMatcher.group(1));
                confidence += 10;
            }
        }

        if (layoutEngine != null) {
            identified.setLayoutEngine(layoutEngine);
            identified.setLayoutEngineVersion(layoutEngineVersion);
            if (layoutEngine.equals(LayoutEngineBrowserBuilder.TRIDENT)) {
                confidence += 10;
            }
        }

        identified.setDisplayWidth(hintedWidth);
        identified.setDisplayHeight(hintedHeight);
        identified.setConfidence(confidence);

        return identified;
    }
}

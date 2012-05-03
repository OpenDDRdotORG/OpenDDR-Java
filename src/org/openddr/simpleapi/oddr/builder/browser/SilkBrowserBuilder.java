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

public class SilkBrowserBuilder extends LayoutEngineBrowserBuilder {

    private static final String VERSION_REGEXP = ".*Version/([0-9\\.]+).*?";
    private static final String SILK_VERSION_REGEXP = ".*Silk/([0-9a-z\\.\\-]+)";
    private Pattern versionPattern = Pattern.compile(VERSION_REGEXP);
    private Pattern silkVersionPattern = Pattern.compile(SILK_VERSION_REGEXP);

    public boolean canBuild(UserAgent userAgent) {
        return (userAgent.getCompleteUserAgent().contains("Silk-Accelerated"));
    }

    @Override
    protected Browser buildBrowser(UserAgent userAgent, String layoutEngine, String layoutEngineVersion, int hintedWidth, int hintedHeight) {
        if (!(userAgent.hasMozillaPattern())) {
            return null;
        }

        int confidence = 60;
        Browser identified = new Browser();

        identified.setVendor("Amazon");
        identified.setModel("Silk");
        identified.setVersion("-");
        identified.setMajorRevision("-");

        Matcher silkMatcher = silkVersionPattern.matcher(userAgent.getPatternElementsInside());
        if (silkMatcher.matches()) {
            if (silkMatcher.group(1) != null) {
                identified.setVersion(silkMatcher.group(1));
                String version[] = silkMatcher.group(1).split("\\.");

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

                if (version[2] != null) {
                    String subVersion[] = version[2].split("-");
                    if (subVersion.length > 0) {
                        identified.setMicroRevision(subVersion[0]);
                    }

                    if (subVersion.length > 1) {
                        identified.setNanoRevision(subVersion[1]);
                    }
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
            if (layoutEngine.equals(LayoutEngineBrowserBuilder.APPLEWEBKIT)) {
                confidence += 10;
            }
        }


        if (userAgent.containsAndroid()) {
            identified.setReferenceBrowser("Android Browser");
            Matcher androidMatcher = versionPattern.matcher(userAgent.getCompleteUserAgent());
            if (androidMatcher.matches()) {
                if (androidMatcher.group(1) != null) {
                    identified.setReferenceBrowserVersion(androidMatcher.group(1));
                    confidence += 5;
                }
            }
            confidence += 5;
        } else if (userAgent.getCompleteUserAgent().contains("Safari") && !userAgent.getCompleteUserAgent().contains("Mobile")) {
            identified.setReferenceBrowser("Safari");
            Matcher safariMatcher = versionPattern.matcher(userAgent.getCompleteUserAgent());
            if (safariMatcher.matches()) {
                if (safariMatcher.group(1) != null) {
                    identified.setReferenceBrowserVersion(safariMatcher.group(1));
                    confidence += 5;
                }
            }
            confidence += 5;
        }


        identified.setDisplayWidth(hintedWidth);
        identified.setDisplayHeight(hintedHeight);
        identified.setConfidence(confidence);

        return identified;
    }
}

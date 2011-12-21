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

public class FennecBrowserBuilder extends LayoutEngineBrowserBuilder {

    private static final String FENNEC_VERSION_REGEXP = ".*Fennec/([0-9a-z\\.\\-]+)";
    private static final String FIREFOX_VERSION_REGEXP = ".*Firefox.([0-9a-z\\.b]+).*";
    private Pattern fennecVersionPattern = Pattern.compile(FENNEC_VERSION_REGEXP);
    private Pattern firefoxVersionPattern = Pattern.compile(FIREFOX_VERSION_REGEXP);

    public boolean canBuild(UserAgent userAgent) {
        return (userAgent.getCompleteUserAgent().contains("Fennec"));
    }

    @Override
    protected Browser buildBrowser(UserAgent userAgent, String layoutEngine, String layoutEngineVersion, int hintedWidth, int hintedHeight) {
        if (!(userAgent.hasMozillaPattern())) {
            return null;
        }

        int confidence = 60;
        Browser identified = new Browser();

        identified.setVendor("Mozilla");
        identified.setModel("Firefox Mobile");
        identified.setVersion("-");
        identified.setMajorRevision("-");

        Matcher fennecMatcher = fennecVersionPattern.matcher(userAgent.getCompleteUserAgent());
        if (fennecMatcher.matches()) {
            if (fennecMatcher.group(1) != null) {
                identified.setVersion(fennecMatcher.group(1));
                String version[] = fennecMatcher.group(1).split("\\.");

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

        if (layoutEngine != null) {
            identified.setLayoutEngine(layoutEngine);
            identified.setLayoutEngineVersion(layoutEngineVersion);
            if (layoutEngine.equals(LayoutEngineBrowserBuilder.GECKO)) {
                confidence += 10;
            }
        }

        Matcher firefoxMatcher = firefoxVersionPattern.matcher(userAgent.getCompleteUserAgent());
        if (firefoxMatcher.matches()) {
            if (firefoxMatcher.group(1) != null) {
                identified.setReferenceBrowser("Firefox");
                identified.setReferenceBrowserVersion(firefoxMatcher.group(1));
                confidence += 10;
            }
        }

        identified.setDisplayWidth(hintedWidth);
        identified.setDisplayHeight(hintedHeight);
        identified.setConfidence(confidence);

        return identified;
    }
}

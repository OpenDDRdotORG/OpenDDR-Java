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

public class ObigoBrowserBuilder extends LayoutEngineBrowserBuilder {

    private static final String VERSION_REGEXP = ".*?(?:(?:ObigoInternetBrowser/)|(?:Obigo Browser )|(?:[Oo]bigo[- ][Bb]rowser/))([0-9A-Z\\.]+).*?";
    private static final String VERSION_REGEXP2 = ".*?(?:(?:Browser/Obigo)|(?:OBIGO[/_-])|(?:Obigo[-/ ]))([0-9A-Z\\.]+).*?";
    private static final String VERSION_REGEXP3 = ".*?(?:(?:Obigo[Il]nternetBrowser/)|(?:Obigo Browser )|(?:[Oo]bigo[- ][Bb]rowser/))([0-9A-Zacqv\\.]+).*?";
    private static final String VERSION_REGEXP4 = ".*?(?:(?:[Bb]rowser/[Oo]bigo)|(?:OBIGO[/_-])|(?:Obigo[-/ ]))([0-9A-Zacqv\\.]+).*?";
    private static final String VERSION_REGEXP5 = ".*?(?:(?:[Tt]eleca Q))([0-9A-Zacqv\\.]+).*?";
    private Pattern versionPattern = Pattern.compile(VERSION_REGEXP);
    private Pattern versionPattern2 = Pattern.compile(VERSION_REGEXP2);
    private Pattern versionPattern3 = Pattern.compile(VERSION_REGEXP3);
    private Pattern versionPattern4 = Pattern.compile(VERSION_REGEXP4);
    private Pattern versionPattern5 = Pattern.compile(VERSION_REGEXP5);

    public boolean canBuild(UserAgent userAgent) {
        if (userAgent.getCompleteUserAgent().matches("((?i).*obigo.*)|((?i).*teleca.*)")) {
            return true;
        }
        return false;
    }

    @Override
    protected Browser buildBrowser(UserAgent userAgent, String layoutEngine, String layoutEngineVersion, int hintedWidth, int hintedHeight) {
        String version = null;

        int confidence = 60;
        Browser identified = new Browser();
        identified.setVendor("Obigo");
        identified.setModel("Obigo Browser");

        Matcher versionMatcher = versionPattern.matcher(userAgent.getCompleteUserAgent());
        if (!versionMatcher.matches()) {
            version = null;

        } else {
            if (versionMatcher.group(1) != null) {
                version = versionMatcher.group(1);
            }
        }

        if (version == null) {
            Matcher versionMatcher2 = versionPattern2.matcher(userAgent.getCompleteUserAgent());
            if (!versionMatcher2.matches()) {
                version = null;

            } else {
                if (versionMatcher2.group(1) != null) {
                    version = versionMatcher2.group(1);
                }
            }
        }

        if (version == null) {
            Matcher versionMatcher3 = versionPattern3.matcher(userAgent.getCompleteUserAgent());
            if (!versionMatcher3.matches()) {
                version = null;

            } else {
                if (versionMatcher3.group(1) != null) {
                    version = versionMatcher3.group(1);
                }
            }
        }

        if (version == null) {
            Matcher versionMatcher4 = versionPattern4.matcher(userAgent.getCompleteUserAgent());
            if (!versionMatcher4.matches()) {
                version = null;

            } else {
                if (versionMatcher4.group(1) != null) {
                    version = versionMatcher4.group(1);
                }
            }
        }

        if (version == null) {
            Matcher versionMatcher5 = versionPattern5.matcher(userAgent.getCompleteUserAgent());
            if (!versionMatcher5.matches()) {
                version = null;

            } else {
                if (versionMatcher5.group(1) != null) {
                    version = versionMatcher5.group(1);
                    identified.setModel("Teleca-Obigo");
                }
            }
        }

        if (version == null) {
            return null;
        }

        identified.setVersion(version);

        if (layoutEngine != null) {
            identified.setLayoutEngine(layoutEngine);
            identified.setLayoutEngineVersion(layoutEngineVersion);
        }

        identified.setDisplayWidth(hintedWidth);
        identified.setDisplayHeight(hintedHeight);
        identified.setConfidence(confidence);

        return identified;
    }
}

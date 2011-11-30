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

public abstract class LayoutEngineBrowserBuilder extends HintedResolutionBrowserBuilder {

    static final String APPLEWEBKIT = "AppleWebKit";
    static final String PRESTO = "Presto";
    static final String GECKO = "Gecko";
    static final String TRIDENT = "Trident";
    static final String KHML = "KHTML";
    private final String WEBKIT_VERSION_REGEXP = ".*AppleWebKit/([0-9\\.]+).*?";
    private final String PRESTO_VERSION_REGEXP = ".*Presto/([0-9\\.]+).*?";
    private final String GECKO_VERSION_REGEXP = ".*Gecko/([0-9\\.]+).*?";
    private final String TRIDENT_VERSION_REGEXP = ".*Trident/([0-9\\.]+).*?";
    private final String KHTML_VERSION_REGEXP = ".*KHTML/([0-9\\.]+).*?";
    private Pattern webkitVersionPattern = Pattern.compile(WEBKIT_VERSION_REGEXP);
    private Pattern prestoVersionPattern = Pattern.compile(PRESTO_VERSION_REGEXP);
    private Pattern geckoVersionPattern = Pattern.compile(GECKO_VERSION_REGEXP);
    private Pattern tridentVersionPattern = Pattern.compile(TRIDENT_VERSION_REGEXP);
    private Pattern khtmlVersionPattern = Pattern.compile(KHTML_VERSION_REGEXP);

    @Override
    protected Browser buildBrowser(UserAgent userAgent, int hintedWidth, int hintedHeight) {
        String layoutEngine = null;
        String layoutEngineVersion = null;
        Matcher result = webkitVersionPattern.matcher(userAgent.getCompleteUserAgent());

        if (result.matches()) {
            layoutEngine = APPLEWEBKIT;
            layoutEngineVersion = result.group(1);

        } else {
            result = prestoVersionPattern.matcher(userAgent.getCompleteUserAgent());
            if (result.matches()) {
                layoutEngine = "Presto";
                layoutEngineVersion = result.group(1);

            } else {
                result = geckoVersionPattern.matcher(userAgent.getCompleteUserAgent());
                if (result.matches()) {
                    layoutEngine = "Gecko";
                    layoutEngineVersion = result.group(1);

                } else {
                    result = tridentVersionPattern.matcher(userAgent.getCompleteUserAgent());
                    if (result.matches()) {
                        layoutEngine = "Trident";
                        layoutEngineVersion = result.group(1);

                    } else {
                        result = khtmlVersionPattern.matcher(userAgent.getCompleteUserAgent());
                        if (result.matches()) {
                            layoutEngine = "KHTML";
                            layoutEngineVersion = result.group(1);
                        }
                    }
                }
            }
        }
        return buildBrowser(userAgent, layoutEngine, layoutEngineVersion, hintedWidth, hintedHeight);
    }

    protected abstract Browser buildBrowser(UserAgent userAgent, String layoutEngine, String layoutEngineVersion, int hintedWidth, int hintedHeight);
}

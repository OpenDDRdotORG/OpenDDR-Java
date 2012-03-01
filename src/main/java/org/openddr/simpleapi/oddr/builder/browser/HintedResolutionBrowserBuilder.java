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
import org.openddr.simpleapi.oddr.builder.Builder;
import org.openddr.simpleapi.oddr.model.UserAgent;
import org.openddr.simpleapi.oddr.model.browser.Browser;

public abstract class HintedResolutionBrowserBuilder implements Builder {

    private final String RESOLUTION_HINT_WXH_REGEXP = ".*([0-9][0-9][0-9]+)[*Xx]([0-9][0-9][0-9]+).*";
    private final String RESOLUTION_HINT_FWVGA_REGEXP = ".*FWVGA.*";
    private final String RESOLUTION_HINT_WVGA_REGEXP = ".*WVGA.*";
    private final String RESOLUTION_HINT_WXGA_REGEXP = ".*WXGA.*";
    private final String RESOLUTION_HINT_WQVGA_REGEXP = ".*WQVGA.*";
    private Pattern resolutionHintWxHPattern = Pattern.compile(RESOLUTION_HINT_WXH_REGEXP);
    private Pattern resolutionHintFWVGAPattern = Pattern.compile(RESOLUTION_HINT_FWVGA_REGEXP);
    private Pattern resolutionHintWVGAPattern = Pattern.compile(RESOLUTION_HINT_WVGA_REGEXP);
    private Pattern resolutionHintWXGAPattern = Pattern.compile(RESOLUTION_HINT_WXGA_REGEXP);
    private Pattern resolutionHintWQVGAPattern = Pattern.compile(RESOLUTION_HINT_WQVGA_REGEXP);

    public Browser build(UserAgent userAgent, int confidenceTreshold) {
        int hintedWidth = -1;
        int hintedHeight = -1;

        Matcher result = resolutionHintWxHPattern.matcher(userAgent.getCompleteUserAgent());

        if (result.matches()) {
            try {
                hintedWidth = Integer.parseInt(result.group(1));
                hintedHeight = Integer.parseInt(result.group(2));

            } catch (NumberFormatException x) {
                hintedWidth = -1;
                hintedHeight = -1;
            }

        } else if (userAgent.getCompleteUserAgent().contains("VGA") || userAgent.getCompleteUserAgent().contains("WXGA")) {
            result = resolutionHintFWVGAPattern.matcher(userAgent.getCompleteUserAgent());
            if (result.matches()) {
                hintedWidth = 480;
                hintedHeight = 854;

            } else {
                result = resolutionHintWVGAPattern.matcher(userAgent.getCompleteUserAgent());
                if (result.matches()) {
                    hintedWidth = 480;
                    hintedHeight = 800;

                } else {
                    result = resolutionHintWXGAPattern.matcher(userAgent.getCompleteUserAgent());
                    if (result.matches()) {
                        hintedWidth = 768;
                        hintedHeight = 1280;

                    } else {
                        result = resolutionHintWQVGAPattern.matcher(userAgent.getCompleteUserAgent());
                        if (result.matches()) {
                            hintedWidth = 240;
                            hintedHeight = 400;
                        }
                    }
                }
            }
        }
        return buildBrowser(userAgent, hintedWidth, hintedHeight);
    }

    protected abstract Browser buildBrowser(UserAgent userAgent, int hintedWidth, int hintedHeight);
}

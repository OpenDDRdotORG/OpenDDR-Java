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

public class OperaBrowserBuilder extends LayoutEngineBrowserBuilder {

    private static final String OPERAMINI_VERSION_REGEXP = "Opera Mobi/(.*)";
    private Pattern operaMiniVersionPattern = Pattern.compile(OPERAMINI_VERSION_REGEXP);

    public boolean canBuild(UserAgent userAgent) {
        return (userAgent.hasOperaPattern() && (!userAgent.getCompleteUserAgent().contains("Opera Mini")));
    }

    @Override
    protected Browser buildBrowser(UserAgent userAgent, String layoutEngine, String layoutEngineVersion, int hintedWidth, int hintedHeight) {
        if (!userAgent.hasOperaPattern() || userAgent.getOperaVersion() == null || userAgent.getOperaVersion().length() == 0) {
            return null;
        }

        int confidence = 60;
        Browser identified = new Browser();

        identified.setVendor("Opera");
        if (userAgent.getCompleteUserAgent().contains("Mobi")) {
            identified.setModel("Opera Mobile");
            confidence += 10;

        } else if (userAgent.getCompleteUserAgent().contains("Tablet")) {
            identified.setModel("Opera Tablet");

        } else {
            identified.setModel("Opera");
        }

        identified.setVersion(userAgent.getOperaVersion());
        String version[] = userAgent.getOperaVersion().split("\\.");

        if (version.length > 0) {
            identified.setMajorRevision(version[0]);
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

        if (layoutEngine != null) {
            identified.setLayoutEngine(layoutEngine);
            identified.setLayoutEngineVersion(layoutEngineVersion);
            if (layoutEngine.equals(LayoutEngineBrowserBuilder.PRESTO)) {
                confidence += 10;
            }
        }

        String inside[] = userAgent.getPatternElementsInside().split(";");
        for (String token : inside) {
            String element = token.trim();
            Matcher miniMatcher = operaMiniVersionPattern.matcher(element);
            if (miniMatcher.matches()) {
                if (miniMatcher.group(1) != null) {
                    identified.setReferenceBrowser("Opera Mobi");
                    identified.setReferenceBrowserVersion(miniMatcher.group(1));
                    confidence += 10;
                    break;
                }
            }
        }

        identified.setDisplayWidth(hintedWidth);
        identified.setDisplayHeight(hintedHeight);
        identified.setConfidence(confidence);

        return identified;
    }
}

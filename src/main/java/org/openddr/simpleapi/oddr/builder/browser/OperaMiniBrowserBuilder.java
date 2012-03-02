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

public class OperaMiniBrowserBuilder extends LayoutEngineBrowserBuilder {

    private static final String VERSION_REGEXP = ".*?Opera Mini/(?:att/)?v?((\\d+)\\.(\\d+)(?:\\.(\\d+))?(?:\\.(\\d+))?).*?";
    private static final String BUILD_REGEXP = ".*?Opera Mini/(?:att/)?v?.*?/(.*?);.*";
    private Pattern versionPattern = Pattern.compile(VERSION_REGEXP);
    private Pattern buildPattern = Pattern.compile(BUILD_REGEXP);

    public boolean canBuild(UserAgent userAgent) {
        if (userAgent.getCompleteUserAgent().contains("Opera Mini")) {
            return true;
        }
        return false;
    }

    @Override
    protected Browser buildBrowser(UserAgent userAgent, String layoutEngine, String layoutEngineVersion, int hintedWidth, int hintedHeight) {
        Matcher versionMatcher = versionPattern.matcher(userAgent.getCompleteUserAgent());
        if (!versionMatcher.matches()) {
            return null;
        }

        int confidence = 60;
        Browser identified = new Browser();

        identified.setVendor("Opera");
        identified.setModel("Opera Mini");

        if (versionMatcher.group(1) != null) {
            identified.setVersion(versionMatcher.group(1));
        }
        if (versionMatcher.group(2) != null) {
            identified.setMajorRevision(versionMatcher.group(2));
        }
        if (versionMatcher.group(3) != null) {
            identified.setMinorRevision(versionMatcher.group(3));
        }
        if (versionMatcher.group(4) != null) {
            identified.setMicroRevision(versionMatcher.group(4));
        }
        if (versionMatcher.group(5) != null) {
            identified.setNanoRevision(versionMatcher.group(5));
        }

        if (userAgent.hasOperaPattern() && userAgent.getOperaVersion() != null) {
            identified.setReferenceBrowser("Opera");
            identified.setReferenceBrowserVersion(userAgent.getOperaVersion());
            confidence += 20;
        }

        Matcher buildMatcher = buildPattern.matcher(userAgent.getCompleteUserAgent());
        if (buildMatcher.matches()) {
            if (buildMatcher.group(1) != null) {
                identified.setBuild(buildMatcher.group(1));
                confidence += 10;
            }
        }

        if (layoutEngine != null) {
            identified.setLayoutEngine(layoutEngine);
            identified.setLayoutEngineVersion(layoutEngineVersion);
            if (layoutEngine.equals(LayoutEngineBrowserBuilder.PRESTO)) {
                confidence += 10;
            }
        }

        identified.setDisplayWidth(hintedWidth);
        identified.setDisplayHeight(hintedHeight);
        identified.setConfidence(confidence);

        return identified;
    }
}

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

public class OpenWaveBrowserBuilder extends LayoutEngineBrowserBuilder {

    private static final String VERSION_REGEXP = "(?i).*openwave[/ ]?([0-9\\.]+).*?";
    private Pattern versionPattern = Pattern.compile(VERSION_REGEXP);

    public boolean canBuild(UserAgent userAgent) {
        if (userAgent.getCompleteUserAgent().matches("(?i).*openwave.*")) {
            return true;
        }
        return false;
    }

    @Override
    protected Browser buildBrowser(UserAgent userAgent, String layoutEngine, String layoutEngineVersion, int hintedWidth, int hintedHeight) {
        String version = null;

        Matcher versionMatcher = versionPattern.matcher(userAgent.getCompleteUserAgent());
        if (!versionMatcher.matches()) {
            return null;

        } else {
            if (versionMatcher.group(1) != null) {
                version = versionMatcher.group(1);
            }
        }

        int confidence = 60;
        Browser identified = new Browser();

        identified.setVendor("Openwave");
        identified.setModel("OpenWave");
        identified.setVersion(version);
        String[] versionEl = version.split("\\.");

        if (versionEl.length > 0) {
            identified.setMajorRevision(versionEl[0]);
        }

        if (versionEl.length > 1) {
            identified.setMinorRevision(versionEl[1]);
            confidence += 10;
        }

        if (versionEl.length > 2) {
            identified.setMicroRevision(versionEl[2]);
        }

        if (versionEl.length > 3) {
            identified.setNanoRevision(versionEl[3]);
        }

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

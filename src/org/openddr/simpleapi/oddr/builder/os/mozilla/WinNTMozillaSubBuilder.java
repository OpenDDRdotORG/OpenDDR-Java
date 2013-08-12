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
package org.openddr.simpleapi.oddr.builder.os.mozilla;

import org.openddr.simpleapi.oddr.builder.Builder;
import org.openddr.simpleapi.oddr.model.UserAgent;
import org.openddr.simpleapi.oddr.model.os.OperatingSystem;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WinNTMozillaSubBuilder implements Builder {

    private static final String VERSION_REGEXP = ".*Windows NT ((\\d+)\\.(\\d+)).*";
    private static final String TRIDENT_REGEXP = ".*Trident/[0-9.].*";
    private static final String NET_CLR_REGEXP = ".*\\.NET CLR[ /][0-9.]+.*";
    private Pattern versionPattern = Pattern.compile(VERSION_REGEXP);
    private Pattern tridentPattern = Pattern.compile(TRIDENT_REGEXP);
    private Pattern clrPattern = Pattern.compile(NET_CLR_REGEXP);

    public boolean canBuild(UserAgent userAgent) {
        return userAgent.containsWindowsPhone();
    }

    public OperatingSystem build(UserAgent userAgent, int confidenceTreshold) {
        final OperatingSystem os = new OperatingSystem();
        os.setVendor("Microsoft");
        os.setModel("Windows");
        os.setConfidence(40);

        final String[] splittedTokens = userAgent.getPatternElementsInside().split(";");
        for (String tokenElement : splittedTokens) {
            final Matcher versionMatcher = versionPattern.matcher(tokenElement);
            if (versionMatcher.find()) {
                os.setConfidence(85);
                if (versionMatcher.group(1) != null) {
                    os.setVersion(versionMatcher.group(1));
                }
                if (versionMatcher.group(2) != null) {
                    os.setMajorRevision(versionMatcher.group(2));
                }
                if (versionMatcher.group(3) != null) {
                    os.setMinorRevision(versionMatcher.group(3));
                }
            } else if (tridentPattern.matcher(tokenElement).find() || clrPattern.matcher(tokenElement).find()) {
                if (os.getConfidence() > 80) {
                    os.setConfidence(100);
                } else {
                    os.setConfidence(85);
                }
            }
        }

        if (os.getVersion() != null) {
            os.setDescription("Windows " + os.getVersion());
        } else {
            os.setDescription("Windows");
        }

        return os;
    }
}
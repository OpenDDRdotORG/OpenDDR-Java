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

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openddr.simpleapi.oddr.builder.Builder;
import org.openddr.simpleapi.oddr.model.UserAgent;
import org.openddr.simpleapi.oddr.model.os.OperatingSystem;

public class IOSMozillaSubBuilder implements Builder {

    private static final String VERSION_REGEXP = ".*(?:iPhone)?.?OS.?((\\d+)_(\\d+)(?:_(\\d+))?).*";
    private Pattern versionPattern = Pattern.compile(VERSION_REGEXP);

    public boolean canBuild(UserAgent userAgent) {
        if (userAgent.containsIOSDevices()) {
            return true;
        }
        return false;
    }

    public OperatingSystem build(UserAgent userAgent, int confidenceTreshold) {
        OperatingSystem model = new OperatingSystem();
        model.setMajorRevision("1");
        model.setVendor("Apple");
        model.setModel("iOS");
        model.setConfidence(40);

        String[] splittedTokens = userAgent.getPatternElementsInside().split(";");
        for (String tokenElement : splittedTokens) {
            Matcher versionMatcher = versionPattern.matcher(tokenElement);
            if (versionMatcher.find()) {
                model.setConfidence(90);

                if (versionMatcher.group(1) != null) {
                    model.setVersion(versionMatcher.group(1));
                }
                if (versionMatcher.group(2) != null) {
                    model.setMajorRevision(versionMatcher.group(2));
                }
                if (versionMatcher.group(3) != null) {
                    model.setMinorRevision(versionMatcher.group(3));
                }
                if (versionMatcher.group(4) != null) {
                    model.setMicroRevision(versionMatcher.group(4));
                }
            }
        }
        return model;
    }
}

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

public class SymbianMozillaSubBuilder implements Builder {

    private static final String VERSION_REGEXP = ".*Series.?60/(\\d+)(?:[\\.\\- ](\\d+))?(?:[\\.\\- ](\\d+))?.*";
    private static final String VERSION_EXTRA = ".*Symbian(?:OS)?/(.*)";
    private Pattern versionPattern = Pattern.compile(VERSION_REGEXP);
    private Pattern versionExtraPattern = Pattern.compile(VERSION_EXTRA);

    public boolean canBuild(UserAgent userAgent) {
        if (userAgent.containsSymbian()) {
            return true;
        }
        return false;
    }

    public OperatingSystem build(UserAgent userAgent, int confidenceTreshold) {
        OperatingSystem model = new OperatingSystem();
        model.setMajorRevision("1");
        model.setVendor("Nokia");
        model.setModel("Symbian OS");
        model.setConfidence(40);

        String[] splittedTokens = userAgent.getPatternElementsInside().split(";");
        for (String tokenElement : splittedTokens) {
            Matcher versionMatcher = versionPattern.matcher(tokenElement);
            if (versionMatcher.find()) {
                model.setDescription("Series60");
                if (model.getConfidence() > 40) {
                    model.setConfidence(100);

                } else {
                    model.setConfidence(90);
                }

                if (versionMatcher.group(1) != null) {
                    model.setMajorRevision(versionMatcher.group(1));
                }
                if (versionMatcher.group(2) != null) {
                    model.setMinorRevision(versionMatcher.group(2));
                }
                if (versionMatcher.group(3) != null) {
                    model.setMicroRevision(versionMatcher.group(3));
                }
            }

            Matcher versionExtraMatcher = versionExtraPattern.matcher(tokenElement);
            if (versionExtraMatcher.find()) {
                if (model.getConfidence() > 40) {
                    model.setConfidence(100);

                } else {
                    model.setConfidence(85);
                }

                if (versionExtraMatcher.group(1) != null) {
                    model.setVersion(versionExtraMatcher.group(1).trim());
                }
            }
            //TODO: inference VERSION_EXTRA/VERSION_REGEXP and vice-versa
        }
        return model;
    }
}

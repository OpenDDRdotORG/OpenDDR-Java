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

public class WinPhoneMozillaSubBuilder implements Builder {

    private static final String VERSION_REGEXP = ".*Windows.?Phone.?(?:OS)?.?((\\d+)(?:\\.(\\d+))?(?:\\.(\\d+))?(?:\\.(\\d+))?).*";
    private Pattern versionPattern = Pattern.compile(VERSION_REGEXP);

    public boolean canBuild(UserAgent userAgent) {
        if (userAgent.containsWindowsPhone()) {
            if ((userAgent.getPatternElementsInside() + userAgent.getPatternElementsPost()).matches(".*Windows.?Phone.*")) {
                return true;
            }
        }
        return false;
    }

    public OperatingSystem build(UserAgent userAgent, int confidenceTreshold) {
        OperatingSystem model = new OperatingSystem();
        model.setVendor("Microsoft");
        model.setModel("Windows Phone");
        model.setConfidence(40);
        boolean isInPostMoz = false;

        String toSplit = userAgent.getPatternElementsInside();
        if (!toSplit.matches(VERSION_REGEXP)) {
            toSplit = userAgent.getPatternElementsPost();
            isInPostMoz = true;
        }

        String[] splittedTokens = toSplit.split(";");
        for (String tokenElement : splittedTokens) {
            Matcher versionMatcher = versionPattern.matcher(tokenElement);
            if (versionMatcher.find()) {
                if (isInPostMoz) {
                    model.setConfidence(85);

                } else {
                    model.setConfidence(90);
                }

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

                if (versionMatcher.group(5) != null) {
                    model.setNanoRevision(versionMatcher.group(5));
                }
            }

            if (model.getMajorRevision().equals("0") && model.getMinorRevision().equals("0")) {
                model.setMajorRevision("6");
                model.setMinorRevision("5");
            }
            model.setDescription("Windows Phone " + model.getMajorRevision() + "." + model.getMinorRevision() + "." + model.getMicroRevision() + "." + model.getNanoRevision());
        }
        return model;
    }
}

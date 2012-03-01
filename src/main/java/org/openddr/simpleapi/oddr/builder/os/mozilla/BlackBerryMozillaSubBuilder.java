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

public class BlackBerryMozillaSubBuilder implements Builder {

    private static final String VERSION_REGEXP = "(?:.*?Version.?((\\d+)\\.(\\d+)(?:\\.(\\d+))?(?:\\.(\\d+))?).*)|(?:.*?[Bb]lack.?[Bb]erry(?:\\d+)/((\\d+)\\.(\\d+)(?:\\.(\\d+))?(?:\\.(\\d+))?).*)|(?:.*?RIM.?Tablet.?OS.?((\\d+)\\.(\\d+)(?:\\.(\\d+))?(?:\\.(\\d+))?).*)";
    private Pattern versionPattern = Pattern.compile(VERSION_REGEXP);

    public boolean canBuild(UserAgent userAgent) {
        if (userAgent.containsBlackBerryOrRim()) {
            return true;
        }
        return false;
    }

    public OperatingSystem build(UserAgent userAgent, int confidenceTreshold) {
        OperatingSystem model = new OperatingSystem();

        String rebuilded = userAgent.getPatternElementsInside() + ";" + userAgent.getPatternElementsPost();

        String[] splittedTokens = rebuilded.split(";");
        for (String tokenElement : splittedTokens) {
            Matcher versionMatcher = versionPattern.matcher(tokenElement);
            if (versionMatcher.find()) {
                if (versionMatcher.group(11) != null) {
                    model.setVendor("Research In Motion");
                    model.setModel("RIM Tablet OS");
                    model.setMajorRevision("1");
                    model.setConfidence(50);

                    if (versionMatcher.group(11) != null) {
                        model.setVersion(versionMatcher.group(11));

                    }

                    if (versionMatcher.group(12) != null) {
                        model.setMajorRevision(versionMatcher.group(12));
                        model.setConfidence(60);

                    }

                    if (versionMatcher.group(13) != null) {
                        model.setMinorRevision(versionMatcher.group(13));
                        model.setConfidence(70);

                    }

                    if (versionMatcher.group(14) != null) {
                        model.setMicroRevision(versionMatcher.group(14));
                        model.setConfidence(80);

                    }

                    if (versionMatcher.group(15) != null) {
                        model.setNanoRevision(versionMatcher.group(5));
                        model.setConfidence(90);

                    }
                    return model;

                } else if (versionMatcher.group(1) != null || versionMatcher.group(6) != null) {
                    model.setVendor("Research In Motion");
                    model.setModel("Black Berry OS");
                    model.setMajorRevision("1");
                    model.setConfidence(40);

                    if (versionMatcher.group(1) != null) {
                        if (versionMatcher.group(6) != null) {
                            model.setConfidence(100);

                        } else {
                            model.setConfidence(80);
                        }

                    } else if (versionMatcher.group(6) != null) {
                        model.setConfidence(90);
                    }

                    if (versionMatcher.group(1) != null) {
                        model.setVersion(versionMatcher.group(1));

                    } else if (versionMatcher.group(6) != null) {
                        model.setVersion(versionMatcher.group(6));
                    }

                    if (versionMatcher.group(2) != null) {
                        model.setMajorRevision(versionMatcher.group(2));

                    } else if (versionMatcher.group(7) != null) {
                        model.setMajorRevision(versionMatcher.group(7));
                    }

                    if (versionMatcher.group(3) != null) {
                        model.setMinorRevision(versionMatcher.group(3));

                    } else if (versionMatcher.group(8) != null) {
                        model.setMinorRevision(versionMatcher.group(8));
                    }

                    if (versionMatcher.group(4) != null) {
                        model.setMicroRevision(versionMatcher.group(4));

                    } else if (versionMatcher.group(9) != null) {
                        model.setMicroRevision(versionMatcher.group(9));
                    }

                    if (versionMatcher.group(5) != null) {
                        model.setNanoRevision(versionMatcher.group(5));

                    } else if (versionMatcher.group(10) != null) {
                        model.setNanoRevision(versionMatcher.group(10));
                    }
                    return model;

                }
            }
        }
        return model;
    }
}

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
import org.openddr.simpleapi.oddr.model.BuiltObject;
import org.openddr.simpleapi.oddr.model.UserAgent;
import org.openddr.simpleapi.oddr.model.os.OperatingSystem;

public class LinuxMozillaSubBuilder implements Builder {

    private static final String DESCRIPTION_REGEXP = ".*(X11;)?.*?Linux[^;]?([^;]*)?;.*";
    private Pattern descriptionPattern = Pattern.compile(DESCRIPTION_REGEXP);

    public boolean canBuild(UserAgent userAgent) {
        return userAgent.getCompleteUserAgent().contains("Linux") && !userAgent.getCompleteUserAgent().contains("Android");
    }

    public BuiltObject build(UserAgent userAgent, int confidenceTreshold) {
        OperatingSystem model = new OperatingSystem();
        model.setMajorRevision("-");
        model.setVendor("-");
        model.setModel("Linux");

        int confidence = 60;

        Matcher descriptionMatcher = descriptionPattern.matcher(userAgent.getPatternElementsInside());
        if (descriptionMatcher.find()) {
            if (descriptionMatcher.group(1) != null) {
                confidence += 10;
            }
            if (descriptionMatcher.group(2) != null) {
                model.setDescription(descriptionMatcher.group(2));
                confidence += 10;
            }
        }

        model.setConfidence(confidence);

        return model;
    }
}

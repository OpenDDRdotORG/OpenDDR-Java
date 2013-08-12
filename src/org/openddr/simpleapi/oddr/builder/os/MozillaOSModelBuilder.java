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
package org.openddr.simpleapi.oddr.builder.os;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.openddr.simpleapi.oddr.builder.Builder;
import org.openddr.simpleapi.oddr.builder.os.mozilla.*;
import org.openddr.simpleapi.oddr.model.UserAgent;
import org.openddr.simpleapi.oddr.model.os.OperatingSystem;

public class MozillaOSModelBuilder implements Builder {

    private Builder[] builders = {
        new IOSMozillaSubBuilder(),
        new AndroidMozillaSubBuilder(),
        new WinPhoneMozillaSubBuilder(),
        new BlackBerryMozillaSubBuilder(),
        new SymbianMozillaSubBuilder(),
        new WinCEMozillaSubBuilder(),
        new BadaMozillaSubBuilder(),
        new BrewMozillaSubBuilder(),
        new WebOSMozillaSubBuilder(),
        new LinuxMozillaSubBuilder(),
        new MacOSXMozillaSubBuilder(),
        new WinNTMozillaSubBuilder()
    };

    public boolean canBuild(UserAgent userAgent) {
        if (userAgent.hasMozillaPattern()) {
            return true;
        }
        return false;
    }

    public OperatingSystem build(UserAgent userAgent, int confidenceTreshold) {
        List<OperatingSystem> founds = new ArrayList<OperatingSystem>();
        OperatingSystem found = null;
        for (Builder builder : builders) {
            if (builder.canBuild(userAgent)) {
                OperatingSystem builded = (OperatingSystem) builder.build(userAgent, confidenceTreshold);
                if (builded != null) {
                    founds.add(builded);
                    if (builded.getConfidence() >= confidenceTreshold) {
                        found = builded;
                        break;
                    }
                }
            }
        }

        if (found != null) {
            return found;

        } else {
            if (founds.isEmpty()) {
                return null;
            }

            Collections.sort(founds, Collections.reverseOrder());
            return founds.get(0);
        }
    }
}

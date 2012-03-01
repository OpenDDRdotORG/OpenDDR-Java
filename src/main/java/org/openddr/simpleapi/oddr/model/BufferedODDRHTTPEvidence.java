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
package org.openddr.simpleapi.oddr.model;

import org.openddr.simpleapi.oddr.model.browser.Browser;
import org.openddr.simpleapi.oddr.model.device.Device;
import org.openddr.simpleapi.oddr.model.os.OperatingSystem;

public class BufferedODDRHTTPEvidence extends ODDRHTTPEvidence {

    private Browser browserFound = null;
    private Device deviceFound = null;
    private OperatingSystem osFound = null;

    public synchronized Browser getBrowserFound() {
        return browserFound;
    }

    public synchronized void setBrowserFound(Browser browserFound) {
        this.browserFound = browserFound;
    }

    public synchronized Device getDeviceFound() {
        return deviceFound;
    }

    public synchronized void setDeviceFound(Device deviceFound) {
        this.deviceFound = deviceFound;
    }

    public synchronized OperatingSystem getOsFound() {
        return osFound;
    }

    public synchronized void setOsFound(OperatingSystem osFound) {
        this.osFound = osFound;
    }

    @Override
    public synchronized void put(String key, String value) {
        setOsFound(null);
        setBrowserFound(null);
        setDeviceFound(null);
        super.put(key, value);
    }
}

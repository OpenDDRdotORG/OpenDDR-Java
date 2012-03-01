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
package org.openddr.simpleapi.oddr.model.os;

import java.util.Map;
import org.openddr.simpleapi.oddr.model.BuiltObject;

public class OperatingSystem extends BuiltObject implements Comparable {

    private String majorRevision = "0";
    private String minorRevision = "0";
    private String microRevision = "0";
    private String nanoRevision = "0";

    public OperatingSystem() {
        super();
    }

    public OperatingSystem(Map<String, String> properties) {
        super(properties);
    }

    //version id getter and setters
    public String getMajorRevision() {
        return majorRevision;
    }

    public void setMajorRevision(String majorRevision) {
        this.majorRevision = majorRevision;
    }

    public String getMicroRevision() {
        return microRevision;
    }

    public void setMicroRevision(String microRevision) {
        this.microRevision = microRevision;
    }

    public String getMinorRevision() {
        return minorRevision;
    }

    public void setMinorRevision(String minorRevision) {
        this.minorRevision = minorRevision;
    }

    public String getNanoRevision() {
        return nanoRevision;
    }

    public void setNanoRevision(String nanoRevision) {
        this.nanoRevision = nanoRevision;
    }

    public String getId() {
        if (getModel() == null || getVendor() == null) {
            return null;
        }
        String id = getVendor() + "." + getModel() + "." + getMajorRevision() + "." + getMinorRevision() + "." + getMicroRevision() + "." + getNanoRevision();
        return id;
    }

    //GETTERS
    //utility getter for significant oddr OS properties
    public String getModel() {
        return get("model");
    }

    public String getVendor() {
        return get("vendor");
    }

    public String getVersion() {
        return get("version");
    }

    public String getBuild() {
        return get("build");
    }

    public String getDescription() {
        return get("description");
    }

    //SETTERS
    //utility setter for significant oddr OS properties
    public void setModel(String model) {
        putProperty("model", model);
    }

    public void setVendor(String vendor) {
        putProperty("vendor", vendor);
    }

    public void setVersion(String version) {
        putProperty("version", version);
    }

    public void setBuild(String build) {
        putProperty("build", build);
    }

    public void setDescription(String description) {
        putProperty("description", description);
    }

    //Comparable
    public int compareTo(Object o) {
        if (o == null || !(o instanceof OperatingSystem)) {
            return Integer.MAX_VALUE;
        }

        OperatingSystem bd = (OperatingSystem) o;
        return this.getConfidence() - bd.getConfidence();
    }

    // Cloneable
    @Override
    public Object clone() {
        OperatingSystem os = new OperatingSystem();
        os.setMajorRevision(getMajorRevision());
        os.setMinorRevision(getMinorRevision());
        os.setMicroRevision(getMicroRevision());
        os.setNanoRevision(getNanoRevision());
        os.setConfidence(getConfidence());
        os.putPropertiesMap(getPropertiesMap());
        return os;
    }

    //Utility
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getVendor());
        sb.append(" ");
        sb.append(getModel());

        if (getDescription() != null && getDescription().length() > 0) {
            sb.append("(");
            sb.append(getDescription());
            sb.append(getVersion()).append(")");
        }

        sb.append(" [").append(getMajorRevision()).append(".").append(getMinorRevision()).append(".").append(getMicroRevision()).append(".").append(getNanoRevision()).append("]");
        if (getBuild() != null) {
            sb.append(" - ").append(getBuild());
        }
        sb.append(" ").append(getConfidence()).append("%");
        return new String(sb);
    }
}

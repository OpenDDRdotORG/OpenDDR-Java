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
package org.openddr.simpleapi.oddr;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created By: rob @ VirtualTourist.com 2012-03-02 10:43AM
 */
public class FileLocator {

    /**
     * This method tries to support the original way of loading a resource from the
     * oddr.properites files (full file system path), and also a more web application /
     * build process friendly way of loading the files as a resource.
     *
     * If the path starts with a "/" it is assumed to be a full path on the filesystem
     * if not, it's loaded as a resource.
     *
     * (Someone who cares about windows, and needs full path support, can update the
     * method for that OS)
     *
     * @param path
     * @return
     * @throws java.io.IOException
     */
    public InputStream openODDRResource(String path) throws IOException {
        if(path.startsWith("/")) {
            return new FileInputStream(new File(path));
        } else {
            return this.getClass().getClassLoader().getResource(path).openStream();
        }
    }

}

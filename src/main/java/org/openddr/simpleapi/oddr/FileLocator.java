package org.openddr.simpleapi.oddr;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Copyright 2012 VirtualTourist.com
 * Date: 3/2/12 10:43 AM
 * Created By: rob
 */
public class FileLocator {

    /**
     * This method tries to support the original way of loading a resource from the
     * oddr.properites files (full file system path), and also a more web application
     * friendly way of loading as a resource.
     *
     * If the path starts with a "/" it is assumed to be a full path on the filesystem
     * if not, it's loaded as a resource.
     *
     * (Someone who cares about windows, and needs full path support,  can update the
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

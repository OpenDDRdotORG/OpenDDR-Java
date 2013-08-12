package com.openddr.simpleapi.oddr.builder.os.mozilla;

import org.junit.Test;
import org.openddr.simpleapi.oddr.builder.os.mozilla.MacOSXMozillaSubBuilder;
import org.openddr.simpleapi.oddr.model.UserAgent;
import org.openddr.simpleapi.oddr.model.UserAgentFactory;
import org.openddr.simpleapi.oddr.model.os.OperatingSystem;
import org.w3c.ddr.simple.exception.InitializationException;
import org.w3c.ddr.simple.exception.NameException;

import java.io.IOException;

import static org.junit.Assert.*;

public class MacOSXMozillaSubBuilderTest {

    private final MacOSXMozillaSubBuilder macOSXMozillaSubBuilder = new MacOSXMozillaSubBuilder();

    @Test
    public void testCanBuild() throws InitializationException, IOException, NameException {

        final UserAgent wrongUserAgent = UserAgentFactory.newUserAgent("wrong user agent");
        assertFalse(macOSXMozillaSubBuilder.canBuild(wrongUserAgent));

        final UserAgent realUserAgent = UserAgentFactory.newUserAgent(
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.5; rv:11.0) Gecko/20100101 Firefox/11.0");
        assertTrue(macOSXMozillaSubBuilder.canBuild(realUserAgent));
    }

    @Test
    public void testBuild() {

        final UserAgent userAgent = UserAgentFactory.newUserAgent(
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.5; rv:11.0) Gecko/20100101 Firefox/11.0");
        final OperatingSystem os = (OperatingSystem) macOSXMozillaSubBuilder.build(userAgent, 0);

        assertEquals("Apple", os.getVendor());
        assertEquals("Mac OS X", os.getModel());
        assertEquals(80, os.getConfidence());
        assertEquals("10.5", os.getVersion());
        assertEquals("10", os.getMajorRevision());
        assertEquals("5", os.getMinorRevision());
        assertEquals("0", os.getMicroRevision());
        assertEquals("0", os.getNanoRevision());
        assertNull(os.getDescription());

    }
}

package org.openddr.simpleapi.oddr.builder.browser;

import org.junit.Test;
import org.openddr.simpleapi.oddr.model.UserAgent;
import org.openddr.simpleapi.oddr.model.UserAgentFactory;
import org.openddr.simpleapi.oddr.model.browser.Browser;
import org.w3c.ddr.simple.exception.InitializationException;
import org.w3c.ddr.simple.exception.NameException;

import java.io.IOException;

import static org.junit.Assert.*;

public class KonquerorMozillaSubBuilderTest {

    private final KonquerorBrowserBuilder konquerorBrowserBuilder = new KonquerorBrowserBuilder();

    @Test
    public void testCanBuild() throws InitializationException, IOException, NameException {

        final UserAgent wrongUserAgent = UserAgentFactory.newUserAgent("wrong user agent");
        assertFalse(konquerorBrowserBuilder.canBuild(wrongUserAgent));

        final UserAgent realUserAgent = UserAgentFactory.newUserAgent(
                "Mozilla/5.0 (compatible; Konqueror/3.5; Linux) KHTML/3.5.5 (like Gecko) (Exabot-Thumbnails)");
        assertTrue(konquerorBrowserBuilder.canBuild(realUserAgent));

        final UserAgent realUserAgentLowerCase = UserAgentFactory.newUserAgent(
                "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/534.34 (KHTML, like Gecko) konqueror/4.7.2 Safari/534.34");
        assertTrue(konquerorBrowserBuilder.canBuild(realUserAgentLowerCase));
    }

    @Test
    public void testBuild() {

        UserAgent userAgent = UserAgentFactory.newUserAgent(
                "Mozilla/5.0 (compatible; Konqueror/3.5; Linux) KHTML/3.5.5 (like Gecko) (Exabot-Thumbnails)");
        Browser browser = konquerorBrowserBuilder.build(userAgent, 0);

        assertEquals("KDE", browser.getVendor());
        assertEquals("Konqueror", browser.getModel());
        assertEquals(80, browser.getConfidence());
        assertEquals("3.5", browser.getVersion());
        assertEquals("3", browser.getMajorRevision());
        assertEquals("5", browser.getMinorRevision());
        assertEquals("0", browser.getMicroRevision());
        assertEquals("0", browser.getNanoRevision());


        userAgent = UserAgentFactory.newUserAgent(
                "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/534.34 (KHTML, like Gecko) konqueror/4.7.2 Safari/534.34");
        browser = konquerorBrowserBuilder.build(userAgent, 0);

        assertEquals("KDE", browser.getVendor());
        assertEquals("Konqueror", browser.getModel());
        assertEquals(70, browser.getConfidence());
        assertEquals("4.7.2", browser.getVersion());
        assertEquals("4", browser.getMajorRevision());
        assertEquals("7", browser.getMinorRevision());
        assertEquals("2", browser.getMicroRevision());
        assertEquals("0", browser.getNanoRevision());

    }
}

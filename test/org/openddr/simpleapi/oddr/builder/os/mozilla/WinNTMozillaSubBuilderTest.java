package org.openddr.simpleapi.oddr.builder.os.mozilla;

import org.junit.Test;
import org.openddr.simpleapi.oddr.model.UserAgent;
import org.openddr.simpleapi.oddr.model.UserAgentFactory;
import org.openddr.simpleapi.oddr.model.os.OperatingSystem;
import org.w3c.ddr.simple.exception.InitializationException;
import org.w3c.ddr.simple.exception.NameException;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class WinNTMozillaSubBuilderTest {

    private final WinNTMozillaSubBuilder winNTMozillaSubBuilder = new WinNTMozillaSubBuilder();

    @Test
    public void testCanBuild() throws InitializationException, IOException, NameException {

        final UserAgent wrongUserAgent = UserAgentFactory.newUserAgent("wrong user agent");
        assertEquals(false, winNTMozillaSubBuilder.canBuild(wrongUserAgent));

        final UserAgent realUserAgent = UserAgentFactory.newUserAgent(
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.72 Safari/537.36");
        assertEquals(true, winNTMozillaSubBuilder.canBuild(realUserAgent));
    }

    @Test
    public void testBuild() {

        UserAgent userAgent = UserAgentFactory.newUserAgent(
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.72 Safari/537.36");
        OperatingSystem os = winNTMozillaSubBuilder.build(userAgent, 0);

        assertEquals("Microsoft", os.getVendor());
        assertEquals("Windows", os.getModel());
        assertEquals(85, os.getConfidence());
        assertEquals("Windows 6.1", os.getDescription());
        assertEquals("6.1", os.getVersion());
        assertEquals("6", os.getMajorRevision());
        assertEquals("1", os.getMinorRevision());
        assertEquals("0", os.getMicroRevision());
        assertEquals("0", os.getNanoRevision());


        userAgent = UserAgentFactory.newUserAgent("Mozilla/5.0 (Windows NT)");
        os = winNTMozillaSubBuilder.build(userAgent, 0);

        assertEquals("Microsoft", os.getVendor());
        assertEquals("Windows", os.getModel());
        assertEquals(40, os.getConfidence());
        assertEquals("Windows", os.getDescription());


        userAgent = UserAgentFactory.newUserAgent("Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0)");
        os = winNTMozillaSubBuilder.build(userAgent, 0);

        assertEquals("Microsoft", os.getVendor());
        assertEquals("Windows", os.getModel());
        assertEquals(100, os.getConfidence());
        assertEquals("Windows 6.1", os.getDescription());
        assertEquals("6.1", os.getVersion());
        assertEquals("6", os.getMajorRevision());
        assertEquals("1", os.getMinorRevision());
        assertEquals("0", os.getMicroRevision());
        assertEquals("0", os.getNanoRevision());


        userAgent = UserAgentFactory.newUserAgent("Mozilla/5.0 (compatible; MSIE 10.0; WOW64; Trident/6.0)");
        os = winNTMozillaSubBuilder.build(userAgent, 0);

        assertEquals("Microsoft", os.getVendor());
        assertEquals("Windows", os.getModel());
        assertEquals(85, os.getConfidence());
        assertEquals("Windows", os.getDescription());
        assertNull(os.getVersion());
        assertEquals("0", os.getMajorRevision());
        assertEquals("0", os.getMinorRevision());
        assertEquals("0", os.getMicroRevision());
        assertEquals("0", os.getNanoRevision());


        userAgent = UserAgentFactory.newUserAgent
                ("Mozilla/4.0 (compatible; MSIE 8.0; Windows NT; WOW64; GTB7.5; SLCC1; .NET CLR 2.0.50727)");
        os = winNTMozillaSubBuilder.build(userAgent, 0);

        assertEquals("Microsoft", os.getVendor());
        assertEquals("Windows", os.getModel());
        assertEquals(85, os.getConfidence());
        assertEquals("Windows", os.getDescription());
        assertNull(os.getVersion());
        assertEquals("0", os.getMajorRevision());
        assertEquals("0", os.getMinorRevision());
        assertEquals("0", os.getMicroRevision());
        assertEquals("0", os.getNanoRevision());


        userAgent = UserAgentFactory.newUserAgent(
                "Mozilla/5.0 (IE 11.0; Windows NT 6.3; Trident/7.0; .NET4.0E; .NET4.0C; rv:11.0) like Gecko");
        os = winNTMozillaSubBuilder.build(userAgent, 0);

        assertEquals("Microsoft", os.getVendor());
        assertEquals("Windows", os.getModel());
        assertEquals(100, os.getConfidence());
        assertEquals("Windows 6.3", os.getDescription());
        assertEquals("6.3", os.getVersion());
        assertEquals("6", os.getMajorRevision());
        assertEquals("3", os.getMinorRevision());
        assertEquals("0", os.getMicroRevision());
        assertEquals("0", os.getNanoRevision());


        userAgent = UserAgentFactory.newUserAgent("Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.2; Trident/6.0)");
        os = winNTMozillaSubBuilder.build(userAgent, 0);

        assertEquals("Microsoft", os.getVendor());
        assertEquals("Windows", os.getModel());
        assertEquals(100, os.getConfidence());
        assertEquals("Windows 6.2", os.getDescription());
        assertEquals("6.2", os.getVersion());
        assertEquals("6", os.getMajorRevision());
        assertEquals("2", os.getMinorRevision());
        assertEquals("0", os.getMicroRevision());
        assertEquals("0", os.getNanoRevision());


        userAgent = UserAgentFactory.newUserAgent(
                "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; Trident/6.0; SLCC2; .NET CLR 3.0.30729; OfficeLiveConnector.1.5; OfficeLivePatch.1.3; BRI/2; .NET4.0E; yie9)");
        os = winNTMozillaSubBuilder.build(userAgent, 0);

        assertEquals("Microsoft", os.getVendor());
        assertEquals("Windows", os.getModel());
        assertEquals(100, os.getConfidence());
        assertEquals("Windows 6.1", os.getDescription());
        assertEquals("6.1", os.getVersion());
        assertEquals("6", os.getMajorRevision());
        assertEquals("1", os.getMinorRevision());
        assertEquals("0", os.getMicroRevision());
        assertEquals("0", os.getNanoRevision());


        userAgent = UserAgentFactory.newUserAgent(
                "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.0; Trident/5.0; chromeframe/11.0.696.57)");
        os = winNTMozillaSubBuilder.build(userAgent, 0);

        assertEquals("Microsoft", os.getVendor());
        assertEquals("Windows", os.getModel());
        assertEquals(100, os.getConfidence());
        assertEquals("Windows 6.0", os.getDescription());
        assertEquals("6.0", os.getVersion());
        assertEquals("6", os.getMajorRevision());
        assertEquals("0", os.getMinorRevision());
        assertEquals("0", os.getMicroRevision());
        assertEquals("0", os.getNanoRevision());


        userAgent = UserAgentFactory.newUserAgent(
                "Mozilla/5.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; Media Center PC 4.0; SLCC1; .NET CLR 3.0.04320)");
        os = winNTMozillaSubBuilder.build(userAgent, 0);

        assertEquals("Microsoft", os.getVendor());
        assertEquals("Windows", os.getModel());
        assertEquals(100, os.getConfidence());
        assertEquals("Windows 5.2", os.getDescription());
        assertEquals("5.2", os.getVersion());
        assertEquals("5", os.getMajorRevision());
        assertEquals("2", os.getMinorRevision());
        assertEquals("0", os.getMicroRevision());
        assertEquals("0", os.getNanoRevision());


        userAgent = UserAgentFactory.newUserAgent("Mozilla/5.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; SLCC1; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729; .NET CLR 1.1.4322)");
        os = winNTMozillaSubBuilder.build(userAgent, 0);

        assertEquals("Microsoft", os.getVendor());
        assertEquals("Windows", os.getModel());
        assertEquals(100, os.getConfidence());
        assertEquals("Windows 5.1", os.getDescription());
        assertEquals("5.1", os.getVersion());
        assertEquals("5", os.getMajorRevision());
        assertEquals("1", os.getMinorRevision());
        assertEquals("0", os.getMicroRevision());
        assertEquals("0", os.getNanoRevision());


        userAgent = UserAgentFactory.newUserAgent("Mozilla/4.0 (compatible; MSIE 5.0; Windows NT 5.01; YComp 5.0.2.6; Hotbar 3.0)");
        os = winNTMozillaSubBuilder.build(userAgent, 0);

        assertEquals("Microsoft", os.getVendor());
        assertEquals("Windows", os.getModel());
        assertEquals(85, os.getConfidence());
        assertEquals("Windows 5.01", os.getDescription());
        assertEquals("5.01", os.getVersion());
        assertEquals("5", os.getMajorRevision());
        assertEquals("01", os.getMinorRevision());
        assertEquals("0", os.getMicroRevision());
        assertEquals("0", os.getNanoRevision());


        userAgent = UserAgentFactory.newUserAgent("Mozilla/4.0 (compatible; MSIE 6.0b; Windows NT 5.0; YComp 5.0.0.0)");
        os = winNTMozillaSubBuilder.build(userAgent, 0);

        assertEquals("Microsoft", os.getVendor());
        assertEquals("Windows", os.getModel());
        assertEquals(85, os.getConfidence());
        assertEquals("Windows 5.0", os.getDescription());
        assertEquals("5.0", os.getVersion());
        assertEquals("5", os.getMajorRevision());
        assertEquals("0", os.getMinorRevision());
        assertEquals("0", os.getMicroRevision());
        assertEquals("0", os.getNanoRevision());


        userAgent = UserAgentFactory.newUserAgent("Mozilla/4.0 (compatible; MSIE 6.0b; Windows NT 4.0)");
        os = winNTMozillaSubBuilder.build(userAgent, 0);

        assertEquals("Microsoft", os.getVendor());
        assertEquals("Windows", os.getModel());
        assertEquals(85, os.getConfidence());
        assertEquals("Windows 4.0", os.getDescription());
        assertEquals("4.0", os.getVersion());
        assertEquals("4", os.getMajorRevision());
        assertEquals("0", os.getMinorRevision());
        assertEquals("0", os.getMicroRevision());
        assertEquals("0", os.getNanoRevision());
    }
}

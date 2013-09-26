package org.openddr.simpleapi.oddr.builder.os.mozilla;

import org.junit.Test;
import org.openddr.simpleapi.oddr.model.UserAgent;
import org.openddr.simpleapi.oddr.model.UserAgentFactory;
import org.openddr.simpleapi.oddr.model.os.OperatingSystem;

import static org.junit.Assert.*;

public class BlackBerryMozillaSubBuilderTest {

    private static final String OS_NAME = "BlackBerry OS";

    private static final BlackBerryMozillaSubBuilder BLACKBERRY_BUILDER = new BlackBerryMozillaSubBuilder();
    private static final UserAgent PHONE_USER_AGENT = UserAgentFactory.newUserAgent("Mozilla/5.0 (BlackBerry; U; BlackBerry 9860; en-US) AppleWebKit/534.11+ (KHTML, like Gecko) Version/7.0.0.254 Mobile Safari/534.11+");
    private static final UserAgent TABLET_USER_AGENT = UserAgentFactory.newUserAgent("Mozilla/5.0 (PlayBook; U; RIM Tablet OS 1.0.0; en-US) AppleWebKit/534.11 (KHTML, like Gecko) Version/7.1.0.7 Safari/534.11");


    @Test
    public void testBlackBerryPhone() {
        assertTrue(BLACKBERRY_BUILDER.canBuild(PHONE_USER_AGENT));
        final OperatingSystem phoneOs = BLACKBERRY_BUILDER.build(PHONE_USER_AGENT, 0);
        assertEquals(OS_NAME, phoneOs.getModel());
    }

    @Test
    public void testBlackBerryTablet() {
        assertTrue(BLACKBERRY_BUILDER.canBuild(TABLET_USER_AGENT));
        final OperatingSystem tabletOs = BLACKBERRY_BUILDER.build(TABLET_USER_AGENT, 0);
        assertEquals(OS_NAME, tabletOs.getModel());
    }

}

package org.openddr.simpleapi.oddr.builder.browser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openddr.simpleapi.oddr.model.UserAgent;
import org.openddr.simpleapi.oddr.model.browser.Browser;

public class WebOsBrowserBuilder extends LayoutEngineBrowserBuilder {

    private static final String VERSION_REGEXP = ".*webOSBrowser/([0-9\\.]+).*?";
    private Pattern versionPattern = Pattern.compile(VERSION_REGEXP);

    public boolean canBuild(UserAgent userAgent) {
        if (userAgent.getCompleteUserAgent().contains("webOSBrowser")) {
            return true;
        }
        return false;
    }

    @Override
    protected Browser buildBrowser(UserAgent userAgent, String layoutEngine, String layoutEngineVersion, int hintedWidth, int hintedHeight) {
        String version = null;

        Matcher versionMatcher = versionPattern.matcher(userAgent.getCompleteUserAgent());
        if (!versionMatcher.matches()) {
            return null;

        } else {
            if (versionMatcher.group(1) != null) {
                version = versionMatcher.group(1);
            }
        }

        int confidence = 60;
        Browser identified = new Browser();

        identified.setVendor("HP");
        identified.setModel("Web OS Browser");
        identified.setVersion(version);
        String[] versionEl = version.split("\\.");

        if (versionEl.length > 0) {
            identified.setMajorRevision(versionEl[0]);
        }

        if (versionEl.length > 1) {
            identified.setMinorRevision(versionEl[1]);
            confidence += 10;
        }

        if (layoutEngine != null) {
            identified.setLayoutEngine(layoutEngine);
            identified.setLayoutEngineVersion(layoutEngineVersion);
        }

        identified.setDisplayWidth(hintedWidth);
        identified.setDisplayHeight(hintedHeight);
        identified.setConfidence(confidence);

        return identified;
    }
}

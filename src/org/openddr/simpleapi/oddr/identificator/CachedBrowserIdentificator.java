/**
 * Copyright 2012 Fundación CTIC
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
 * @author José Quiroga Álvarez
 * @author Diego Martínez Ballesteros
 *
 */
package org.openddr.simpleapi.oddr.identificator;

import java.util.Map;

import org.apache.commons.collections.map.LRUMap;
import org.openddr.simpleapi.oddr.builder.Builder;
import org.openddr.simpleapi.oddr.model.UserAgent;
import org.openddr.simpleapi.oddr.model.UserAgentFactory;
import org.openddr.simpleapi.oddr.model.browser.Browser;
import org.w3c.ddr.simple.Evidence;

public class CachedBrowserIdentificator extends BrowserIdentificator implements
		CachedIdentificator {

	// caches
	private LRUMap browserCache;
	private LRUMap notFoundUA;

	public CachedBrowserIdentificator(Builder[] builders,
			Map<String, Browser> browserCapabilities, Integer maxCacheSize, Integer maxNotFoundUASize) {
		super(builders, browserCapabilities);
		browserCache = new LRUMap(maxCacheSize);
		notFoundUA = new LRUMap(maxNotFoundUASize);
	}

	public Browser get(String userAgent, int confidenceTreshold) {
		return get(UserAgentFactory.newUserAgent(userAgent), confidenceTreshold);
	}

	// XXX to be refined, this should NOT be the main entry point, we should use
	// a set of evidence derivation
	public Browser get(Evidence evdnc, int threshold) {
		UserAgent ua = UserAgentFactory.newBrowserUserAgent(evdnc);

		if (ua != null) {
			return get(ua, threshold);
		}

		return null;
	}

	@Override
	public Browser get(UserAgent userAgent, int confidenceTreshold) {
		// check if the device is in the cache
		Browser foundBrowser = (Browser) getFromCache(userAgent);

		if (foundBrowser == null) {
			if (isUaNotFound(userAgent)) {
				return null;
			}
			for (Builder builder : builders) {
				if (builder.canBuild(userAgent)) {
					Browser browser = (Browser) builder.build(userAgent,
							confidenceTreshold);
					if (browser != null) {
						if (browserCapabilities != null) {
							String bestID = getClosestKnownBrowserID(browser
									.getId());
							if (bestID != null) {
								browser.putPropertiesMap(browserCapabilities
										.get(bestID).getPropertiesMap());
								if (!bestID.equals(browser.getId())) {
									browser.setConfidence(browser
											.getConfidence() - 15);
								}
							}
						}
						// add browser to the cache
						addToCache(userAgent, browser);
						return browser;
					}
				}
			}
		} else {
			return foundBrowser;
		}
		addNotFoundUa(userAgent);
		return null;
	}

	public Boolean isUaNotFound(UserAgent userAgent) {
		if (notFoundUA.get(userAgent.getCompleteUserAgent()) != null) {
			return true;
		}
		return false;
	}

	@Override
	public void addNotFoundUa(UserAgent userAgent) {
		notFoundUA.put(userAgent.getCompleteUserAgent(), false);
	}

	@Override
	public Object getFromCache(UserAgent userAgent) {
		Browser cachedBrowser = (Browser) browserCache.get(userAgent
				.getCompleteUserAgent());
		if (cachedBrowser == null) {
			return null;
		}
		return cachedBrowser;
	}

	@Override
	public void addToCache(UserAgent userAgent, Object browser) {
		browserCache.put(userAgent.getCompleteUserAgent(), browser);

	}
}

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
import org.openddr.simpleapi.oddr.model.os.OperatingSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CachedOSIdentificator extends OSIdentificator implements
		CachedIdentificator {

	// caches
	private LRUMap osCache;
	private LRUMap notFoundUA;

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	public CachedOSIdentificator(Builder[] builders,
			Map<String, OperatingSystem> operatingSystemCapabilities, Integer maxCacheSize, Integer maxNotFoundUASize) {
		super(builders, operatingSystemCapabilities);
		osCache = new LRUMap(maxCacheSize);
		notFoundUA = new LRUMap(maxNotFoundUASize);
	}

	@Override
	public OperatingSystem get(UserAgent userAgent, int confidenceTreshold) {
		// check if the device is in the cache
		OperatingSystem foundOs = (OperatingSystem) getFromCache(userAgent);

		if (foundOs == null) {
			if (isUaNotFound(userAgent)) {
				return null;
			}
			for (Builder builder : builders) {
				if (builder.canBuild(userAgent)) {
					OperatingSystem os = (OperatingSystem) builder.build(
							userAgent, confidenceTreshold);
					if (os != null) {
						if (operatingSystemCapabilities != null) {
							String bestID = getClosestKnownBrowserID(os.getId());
							if (bestID != null) {
								os.putPropertiesMap(operatingSystemCapabilities
										.get(bestID).getPropertiesMap());
								if (!bestID.equals(os.getId())) {
									os.setConfidence(os.getConfidence() - 15);
								}
							}
						}
						// add browser to the cache
						addToCache(userAgent, os);
						return os;
					}
				}
			}
		} else {
			return foundOs;
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
		OperatingSystem cachedOs = (OperatingSystem) osCache.get(userAgent
				.getCompleteUserAgent());
		if (cachedOs == null) {
			return null;
		}
		return cachedOs;
	}

	@Override
	public void addToCache(UserAgent userAgent, Object os) {
		osCache.put(userAgent.getCompleteUserAgent(), os);

	}
}

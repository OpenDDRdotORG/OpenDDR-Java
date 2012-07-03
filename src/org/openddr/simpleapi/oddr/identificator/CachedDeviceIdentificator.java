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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.map.LRUMap;
import org.openddr.simpleapi.oddr.builder.device.DeviceBuilder;
import org.openddr.simpleapi.oddr.model.UserAgent;
import org.openddr.simpleapi.oddr.model.device.Device;

public class CachedDeviceIdentificator extends DeviceIdentificator implements
		CachedIdentificator {

	// caches
	private LRUMap deviceCache;
	private LRUMap notFoundUA;

	public CachedDeviceIdentificator(DeviceBuilder[] builders,
			Map<String, Device> devices, Integer maxCacheSize, Integer maxNotFoundUASize) {
		super(builders, devices);
		deviceCache = new LRUMap(maxCacheSize);
		notFoundUA = new LRUMap(maxNotFoundUASize);
	}

	@Override
	public Device get(UserAgent userAgent, int confidenceTreshold) {
		List<Device> foundDevices = new ArrayList<Device>();
		// check if the device is in the cache
		Device foundDevice = (Device) getFromCache(userAgent);

		// if not, build the device
		if (foundDevice == null) {
			if (isUaNotFound(userAgent)) {
				return null;
			}
			for (DeviceBuilder deviceBuilder : builders) {
				if (deviceBuilder.canBuild(userAgent)) {
					Device device = (Device) deviceBuilder.build(userAgent,
							confidenceTreshold);
					if (device != null) {
						String parentId = device.getParentId();
						Device parentDevice = null;
						Set propertiesSet = null;
						Iterator it = null;
						while (!"root".equals(parentId)) {
							parentDevice = (Device) devices.get(parentId);
							propertiesSet = parentDevice.getPropertiesMap()
									.entrySet();
							it = propertiesSet.iterator();
							while (it.hasNext()) {
								Map.Entry entry = (Map.Entry) it.next();
								if (!device.containsProperty((String) entry
										.getKey())) {
									device.putProperty((String) entry.getKey(),
											(String) entry.getValue());
								}
							}
							parentId = parentDevice.getParentId();
						}
						foundDevices.add(device);
						if (device.getConfidence() >= confidenceTreshold) {
							foundDevice = device;
							break;
						}
					}
				}
			}

			if (foundDevice != null) {
				// add the device to the cache
				addToCache(userAgent, foundDevice);
			} else {
				if (foundDevices.isEmpty()) {
					addNotFoundUa(userAgent);
					return null;
				}

				Collections.sort(foundDevices, Collections.reverseOrder());
				foundDevice = foundDevices.get(0);
				// add the device to the cache
				addToCache(userAgent, foundDevice);
			}
		}
		return foundDevice;
	}

	@Override
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
		Device cachedDevice = (Device) deviceCache.get(userAgent
				.getCompleteUserAgent());
		if (cachedDevice == null) {
			return null;
		}
		return cachedDevice;
	}

	@Override
	public void addToCache(UserAgent userAgent, Object device) {
		deviceCache.put(userAgent.getCompleteUserAgent(), device);
	}
}

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

import org.openddr.simpleapi.oddr.model.UserAgent;

public interface CachedIdentificator {

	/**
	 * Check if the request come from an unknown device.
	 * 
	 * @param userAgent
	 *            UserAgent of the device
	 * @return <code>true</code> if the UA is in the cache of not found devices
	 *         or <code>false</code> in other case
	 */
	public Boolean isUaNotFound(UserAgent userAgent);

	/**
	 * Add a device to the cache of not found devices
	 * 
	 * @param userAgent
	 *            UserAgent of the device
	 */
	public void addNotFoundUa(UserAgent userAgent);

	/**
	 * Get from the cache
	 * 
	 * @param userAgent
	 *            UserAgent of the device
	 * @return a <code>Object</code> if the UA is in the cache or
	 *         <code>null</code> in other case
	 */
	public Object getFromCache(UserAgent userAgent);

	/**
	 * Add to the cache
	 * 
	 * @param userAgent
	 *            UserAgent of the device
	 * @param object
	 * 
	 */
	public void addToCache(UserAgent userAgent, Object object);
}

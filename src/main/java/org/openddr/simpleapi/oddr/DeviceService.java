/**
 * Copyright 2011 OpenDDR LLC
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
 */
package org.openddr.simpleapi.oddr;

import org.w3c.ddr.simple.*;
import org.w3c.ddr.simple.exception.InitializationException;
import org.w3c.ddr.simple.exception.NameException;

import java.util.Map;
import java.util.Properties;

/**
 * Created By: VirtualTourist.com 2012-03-01 3:44 PM
 * My Kingdom for delegation in java... c'mon it's 2012...
 */
public class DeviceService {

    private ODDRService service;

    public DeviceService(String defaultVocabularyIRI, Properties prprts) throws NameException, InitializationException {
        service = new ODDRService();
        service.initialize(defaultVocabularyIRI, prprts);
    }

    public String getAPIVersion() {
        return service.getAPIVersion();
    }

    public String getDataVersion() {
        return service.getDataVersion();
    }

    public PropertyRef[] listPropertyRefs() {
        return service.listPropertyRefs();
    }

    public PropertyValue getPropertyValue(Evidence evdnc, PropertyRef pr) throws NameException {
        return service.getPropertyValue(evdnc, pr);
    }

    public PropertyValue getPropertyValue(Evidence evdnc, String localPropertyName, String localAspectName, String vocabularyIRI) throws NameException {
        return service.getPropertyValue(evdnc, localPropertyName, localAspectName, vocabularyIRI);
    }

    public PropertyValue getPropertyValue(Evidence evdnc, PropertyName pn) throws NameException {
        return service.getPropertyValue(evdnc, pn);
    }

    public PropertyValue getPropertyValue(Evidence evdnc, String localPropertyName) throws NameException {
        return service.getPropertyValue(evdnc, localPropertyName);
    }

    public PropertyValues getPropertyValues(Evidence evdnc) {
        return service.getPropertyValues(evdnc);
    }

    public PropertyValues getPropertyValues(Evidence evdnc, PropertyRef[] prs) throws NameException {
        return service.getPropertyValues(evdnc, prs);
    }

    public PropertyValues getPropertyValues(Evidence evdnc, String localAspectName) throws NameException {
        return service.getPropertyValues(evdnc, localAspectName);
    }

    public PropertyValues getPropertyValues(Evidence evdnc, String localAspectName, String vocabularyIRI) throws NameException {
        return service.getPropertyValues(evdnc, localAspectName, vocabularyIRI);
    }

    public PropertyName newPropertyName(String localPropertyName, String vocabularyIRI) throws NameException {
        return service.newPropertyName(localPropertyName, vocabularyIRI);
    }

    public PropertyName newPropertyName(String localPropertyName) throws NameException {
        return service.newPropertyName(localPropertyName);
    }

    public PropertyRef newPropertyRef(PropertyName pn, String localAspectName) throws NameException {
        return service.newPropertyRef(pn, localAspectName);
    }

    public PropertyRef newPropertyRef(PropertyName pn) throws NameException {
        return service.newPropertyRef(pn);
    }

    public PropertyRef newPropertyRef(String localPropertyName) throws NameException {
        return service.newPropertyRef(localPropertyName);
    }

    public Evidence newHTTPEvidence() {
        return service.newHTTPEvidence();
    }

    public Evidence newHTTPEvidence(Map<String, String> map) {
        return service.newHTTPEvidence(map);
    }
}

package org.openddr.simpleapi.oddr;

import org.w3c.ddr.simple.*;
import org.w3c.ddr.simple.exception.InitializationException;
import org.w3c.ddr.simple.exception.NameException;

import java.util.Map;
import java.util.Properties;

/**
 * Copyright 2012 VirtualTourist.com
 * Date: 3/1/12 3:44 PM
 * Created By: rob
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

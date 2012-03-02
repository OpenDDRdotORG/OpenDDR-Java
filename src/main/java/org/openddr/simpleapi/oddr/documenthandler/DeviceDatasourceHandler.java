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
package org.openddr.simpleapi.oddr.documenthandler;

import java.util.HashMap;
import java.util.Map;
import org.openddr.simpleapi.oddr.ODDRService;
import org.openddr.simpleapi.oddr.ODDRVocabularyService;
import org.openddr.simpleapi.oddr.model.device.Device;
import org.openddr.simpleapi.oddr.vocabulary.VocabularyHolder;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class DeviceDatasourceHandler extends DefaultHandler {

    private static final String PROPERTY_ID = "id";
    private static final String ELEMENT_DEVICE = "device";
    private static final String ELEMENT_PROPERTY = "property";
    private static final String ATTRIBUTE_DEVICE_ID = "id";
    private static final String ATTRIBUTE_DEVICE_PARENT_ID = "parentId";
    private static final String ATTRIBUTE_PROPERTY_NAME = "name";
    private static final String ATTRIBUTE_PROPERTY_VALUE = "value";
    private String propertyName = null;
    private String propertyValue = null;
    private Device device = null;
    private Map properties = null;
    private Map<String, Device> devices = null;
    private boolean patching = false;
    private VocabularyHolder vocabularyHolder = null;

    public DeviceDatasourceHandler() {
        this.devices = new HashMap<String, Device>();
    }

    public DeviceDatasourceHandler(Map devices) {
        this.devices = devices;
    }

    public DeviceDatasourceHandler(Map devices, VocabularyHolder vocabularyHolder) {
        this.devices = devices;
        try {
            vocabularyHolder.existVocabulary(ODDRVocabularyService.ODDR_LIMITED_VOCABULARY_IRI);
            this.vocabularyHolder = vocabularyHolder;

        } catch (Exception ex) {
            vocabularyHolder = null;
        }
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
    }

    @Override
    public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
        if (ELEMENT_DEVICE.equals(name)) {
            startDeviceElement(attributes);

        } else if (ELEMENT_PROPERTY.equals(name)) {
            startPropertyElement(attributes);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
    }

    @Override
    public void endElement(String uri, String localName, String name) throws SAXException {
        if (ELEMENT_DEVICE.equals(name)) {
            endDeviceElement();

        } else if (ELEMENT_PROPERTY.equals(name)) {
            endPropertyElement();
        }
    }

    @Override
    public void endDocument() throws SAXException {
    }

    private void startDeviceElement(Attributes attributes) {
        device = new Device();
        device.setId(attributes.getValue(ATTRIBUTE_DEVICE_ID));
        if (attributes.getValue(ATTRIBUTE_DEVICE_PARENT_ID) != null) {
            device.setParentId(attributes.getValue(ATTRIBUTE_DEVICE_PARENT_ID));
        }
        properties = new HashMap();
    }

    private void startPropertyElement(Attributes attributes) {
        propertyName = attributes.getValue(ATTRIBUTE_PROPERTY_NAME);
        propertyValue = attributes.getValue(ATTRIBUTE_PROPERTY_VALUE);

        if (vocabularyHolder != null) {
            try {
                vocabularyHolder.existProperty(propertyName, ODDRService.ASPECT_DEVICE, ODDRVocabularyService.ODDR_LIMITED_VOCABULARY_IRI);
                properties.put(propertyName.intern(), propertyValue);

            } catch (Exception ex) {
                //property non loaded
            }

        } else {
            properties.put(propertyName.intern(), propertyValue);
        }
    }

    private void endDeviceElement() {
        if (devices.containsKey(device.getId())) {
            if (patching) {
                devices.get(device.getId()).getPropertiesMap().putAll(properties);
                return;

            } else {
                //TODO: WARNING already present
            }
        }
        properties.put(PROPERTY_ID, device.getId());
        device.putPropertiesMap(properties);
        devices.put(device.getId(), device);
        device = null;
        properties = null;
    }

    private void endPropertyElement() {
    }

    public Map<String, Device> getDevices() {
        return devices;
    }

    public void setPatching(boolean patching) {
        this.patching = patching;
    }
}

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

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import org.openddr.simpleapi.oddr.ODDRService;
import org.openddr.simpleapi.oddr.ODDRVocabularyService;
import org.openddr.simpleapi.oddr.model.os.OperatingSystem;
import org.openddr.simpleapi.oddr.vocabulary.VocabularyHolder;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class OperatingSystemDatasourceHandler extends DefaultHandler {

    private static final String ELEMENT_OPERATING_SYSTEM_DESCRIPTION = "operatingSystem";
    private static final String ELEMENT_PROPERTY = "property";
    private static final String ATTRIBUTE_BROWSER_ID = "id";
    private static final String ATTRIBUTE_PROPERTY_NAME = "name";
    private static final String ATTRIBUTE_PROPERTY_VALUE = "value";
    private String propertyName = null;
    private String propertyValue = null;
    private OperatingSystem operatingSystem = null;
    private String operatingSystemId = null;
    private Map properties = null;
    private Map<String, OperatingSystem> operatingSystems = null;
    private VocabularyHolder vocabularyHolder = null;

    public OperatingSystemDatasourceHandler() {
        this.operatingSystems = new TreeMap<String, OperatingSystem>(new Comparator<String>() {

            public int compare(String keya, String keyb) {
                return keya.compareTo(keyb);
            }
        });
    }

    public OperatingSystemDatasourceHandler(VocabularyHolder vocabularyHolder) {
        this.operatingSystems = new TreeMap<String, OperatingSystem>(new Comparator<String>() {

            public int compare(String keya, String keyb) {
                return keya.compareTo(keyb);
            }
        });
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
        if (ELEMENT_OPERATING_SYSTEM_DESCRIPTION.equals(name)) {
            startOperatingSystemDescription(attributes);

        } else if (ELEMENT_PROPERTY.equals(name)) {
            startProperty(attributes);
        }
    }

    @Override
    public void endElement(String uri, String localName, String name) throws SAXException {
        if (ELEMENT_OPERATING_SYSTEM_DESCRIPTION.equals(name)) {
            endOperatingSystemDescription();

        } else if (ELEMENT_PROPERTY.equals(name)) {
            endProperty();
        }
    }

    private void startOperatingSystemDescription(Attributes attributes) {
        properties = new HashMap();
        operatingSystemId = attributes.getValue(ATTRIBUTE_BROWSER_ID);
        operatingSystem = new OperatingSystem(properties);
    }

    private void startProperty(Attributes attributes) {
        propertyName = attributes.getValue(ATTRIBUTE_PROPERTY_NAME);
        propertyValue = attributes.getValue(ATTRIBUTE_PROPERTY_VALUE);

        if (vocabularyHolder != null) {
            try {
                vocabularyHolder.existProperty(propertyName, ODDRService.ASPECT_OPERATIVE_SYSTEM, ODDRVocabularyService.ODDR_LIMITED_VOCABULARY_IRI);
                properties.put(propertyName.intern(), propertyValue);

            } catch (Exception ex) {
                //property non loaded
            }

        } else {
            properties.put(propertyName.intern(), propertyValue);
        }
    }

    private void endProperty() {
    }

    private void endOperatingSystemDescription() {
        operatingSystems.put(operatingSystemId, operatingSystem);
        properties = null;
    }

    @Override
    public void endDocument() throws SAXException {
    }

    public Map<String, OperatingSystem> getOperatingSystems() {
        return operatingSystems;
    }
}

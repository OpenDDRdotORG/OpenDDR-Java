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
import org.openddr.simpleapi.oddr.model.browser.Browser;
import org.openddr.simpleapi.oddr.vocabulary.VocabularyHolder;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class BrowserDatasourceHandler extends DefaultHandler {

    private static final String ELEMENT_BROWSER_DESCRIPTION = "browser";
    private static final String ELEMENT_PROPERTY = "property";
    private static final String ATTRIBUTE_BROWSER_ID = "id";
    private static final String ATTRIBUTE_PROPERTY_NAME = "name";
    private static final String ATTRIBUTE_PROPERTY_VALUE = "value";
    private String propertyName = null;
    private String propertyValue = null;
    private Browser browser = null;
    private String browserId = null;
    private Map properties = null;
    private Map<String, Browser> browsers = null;
    private VocabularyHolder vocabularyHolder = null;

    public BrowserDatasourceHandler() {
        this.browsers = new TreeMap<String, Browser>(new Comparator<String>() {

            public int compare(String keya, String keyb) {
                return keya.compareTo(keyb);
            }
        });
    }

    public BrowserDatasourceHandler(VocabularyHolder vocabularyHolder) {
        this.browsers = new TreeMap<String, Browser>(new Comparator<String>() {

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
        if (ELEMENT_BROWSER_DESCRIPTION.equals(name)) {
            startBrowserDescription(attributes);

        } else if (ELEMENT_PROPERTY.equals(name)) {
            startProperty(attributes);
        }
    }

    @Override
    public void endElement(String uri, String localName, String name) throws SAXException {
        if (ELEMENT_BROWSER_DESCRIPTION.equals(name)) {
            endBrowserDescription();

        } else if (ELEMENT_PROPERTY.equals(name)) {
            endProperty();
        }
    }

    private void startBrowserDescription(Attributes attributes) {
        properties = new HashMap();
        browserId = attributes.getValue(ATTRIBUTE_BROWSER_ID);
        browser = new Browser(properties);
    }

    private void startProperty(Attributes attributes) {
        propertyName = attributes.getValue(ATTRIBUTE_PROPERTY_NAME);
        propertyValue = attributes.getValue(ATTRIBUTE_PROPERTY_VALUE);

        if (vocabularyHolder != null) {
            try {
                vocabularyHolder.existProperty(propertyName, ODDRService.ASPECT_WEB_BROWSER, ODDRVocabularyService.ODDR_LIMITED_VOCABULARY_IRI);
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

    private void endBrowserDescription() {
        browsers.put(browserId, browser);
        properties = null;
    }

    @Override
    public void endDocument() throws SAXException {
    }

    public Map<String, Browser> getBrowsers() {
        return browsers;
    }
}

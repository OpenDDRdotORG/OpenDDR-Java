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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.openddr.simpleapi.oddr.documenthandler.VocabularyHandler;
import org.openddr.simpleapi.oddr.model.vocabulary.Vocabulary;
import org.openddr.simpleapi.oddr.vocabulary.VocabularyHolder;
import org.w3c.ddr.simple.exception.InitializationException;
import org.xml.sax.SAXException;

public class ODDRVocabularyService {

    public static final String DDR_CORE_VOCABULARY_PATH_PROP = "ddr.vocabulary.core.path";
    public static final String ODDR_VOCABULARY_PATH_PROP = "oddr.vocabulary.path";
    public static final String ODDR_LIMITED_VOCABULARY_PATH_PROP = "oddr.limited.vocabulary.path";
    public static final String ODDR_LIMITED_VOCABULARY_IRI = "limitedVocabulary";
    private VocabularyHolder vocabularyHolder = null;

    public void initialize(Properties props) throws InitializationException {
        Map<String, Vocabulary> vocabularies = new HashMap<String, Vocabulary>();

        String ddrCoreVocabularyPath = props.getProperty(DDR_CORE_VOCABULARY_PATH_PROP);
        String oddrVocabularyPath = props.getProperty(ODDR_VOCABULARY_PATH_PROP);

        if (ddrCoreVocabularyPath == null || ddrCoreVocabularyPath.trim().length() == 0) {
            throw new InitializationException(InitializationException.INITIALIZATION_ERROR, new IllegalArgumentException("Can not find property " + DDR_CORE_VOCABULARY_PATH_PROP));
        }

        if (oddrVocabularyPath == null || oddrVocabularyPath.trim().length() == 0) {
            throw new InitializationException(InitializationException.INITIALIZATION_ERROR, new IllegalArgumentException("Can not find property " + ODDR_VOCABULARY_PATH_PROP));
        }

        String[] oddrVocabularyPaths = oddrVocabularyPath.split(",");
        for (int i = 0; i < oddrVocabularyPaths.length; i++) {
            oddrVocabularyPaths[i] = oddrVocabularyPaths[i].trim();
        }

        VocabularyHandler vocabularyHandler = new VocabularyHandler();

        parseVocabulary(vocabularyHandler, DDR_CORE_VOCABULARY_PATH_PROP, ddrCoreVocabularyPath);
        Vocabulary vocabulary = vocabularyHandler.getVocabulary();
        vocabularies.put(vocabulary.getVocabularyIRI(), vocabulary);

        for (String oddVocabularyString : oddrVocabularyPaths) {
            vocabularyHandler = new VocabularyHandler();
            parseVocabulary(vocabularyHandler, ODDR_VOCABULARY_PATH_PROP, oddVocabularyString);
            vocabulary = vocabularyHandler.getVocabulary();
            vocabularies.put(vocabulary.getVocabularyIRI(), vocabulary);
        }

        String oddrLimitedVocabularyPath = props.getProperty(ODDR_LIMITED_VOCABULARY_PATH_PROP);

        if (oddrLimitedVocabularyPath != null && oddrLimitedVocabularyPath.trim().length() != 0) {
            vocabularyHandler = new VocabularyHandler();
            parseVocabulary(vocabularyHandler, ODDR_LIMITED_VOCABULARY_PATH_PROP, oddrLimitedVocabularyPath);
            vocabulary = vocabularyHandler.getVocabulary();
            vocabularies.put(ODDR_LIMITED_VOCABULARY_IRI, vocabulary);
        }

        vocabularyHolder = new VocabularyHolder(vocabularies);

        vocabularyHandler = null;
        vocabularies = null;

    }

    private void parseVocabulary(VocabularyHandler vocabularyHandler, String prop, String path) throws InitializationException {
        InputStream stream = null;
        SAXParser parser = null;

        try {
            stream = new FileInputStream(new File(path));

        } catch (IOException ex) {
            throw new InitializationException(InitializationException.INITIALIZATION_ERROR, new IllegalArgumentException("Can not open " + prop + " : " + path));
        }

        try {
            parser = SAXParserFactory.newInstance().newSAXParser();

        } catch (ParserConfigurationException ex) {
            throw new InitializationException(InitializationException.INITIALIZATION_ERROR, new IllegalStateException("Can not instantiate SAXParserFactory.newInstance().newSAXParser()"));

        } catch (SAXException ex) {
            throw new InitializationException(InitializationException.INITIALIZATION_ERROR, new IllegalStateException("Can not instantiate SAXParserFactory.newInstance().newSAXParser()"));
        }

        try {
            parser.parse(stream, vocabularyHandler);

        } catch (SAXException ex) {
            throw new InitializationException(InitializationException.INITIALIZATION_ERROR, new RuntimeException("Can not parse document: " + path));

        } catch (IOException ex) {
            throw new InitializationException(InitializationException.INITIALIZATION_ERROR, new RuntimeException("Can not open " + prop + " : " + path));
        }

        try {
            stream.close();

        } catch (IOException ex) {
            Logger.getLogger(ODDRService.class.getName()).log(Level.WARNING, null, ex);
        }

        parser = null;
    }

    public VocabularyHolder getVocabularyHolder() {
        return vocabularyHolder;
    }
}

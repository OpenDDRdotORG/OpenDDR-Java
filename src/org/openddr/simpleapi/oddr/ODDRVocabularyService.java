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
    public static final String DDR_CORE_VOCABULARY_STREAM_PROP = "ddr.vocabulary.core.stream";
    public static final String ODDR_VOCABULARY_STREAM_PROP = "oddr.vocabulary.stream";
    public static final String ODDR_LIMITED_VOCABULARY_STREAM_PROP = "oddr.limited.vocabulary.stream";
    public static final String ODDR_LIMITED_VOCABULARY_IRI = "limitedVocabulary";
    private VocabularyHolder vocabularyHolder = null;

    public void initialize(Properties props) throws InitializationException {
        Map<String, Vocabulary> vocabularies = new HashMap<String, Vocabulary>();

        String ddrCoreVocabularyPath = props.getProperty(DDR_CORE_VOCABULARY_PATH_PROP);
        String oddrVocabularyPath = props.getProperty(ODDR_VOCABULARY_PATH_PROP);

        InputStream ddrCoreVocabulayStream = null;
        InputStream[] oddrVocabularyStream = null;
        try {
            ddrCoreVocabulayStream = (InputStream) props.get(DDR_CORE_VOCABULARY_STREAM_PROP);
        } catch (Exception ex) {
            ddrCoreVocabulayStream = null;
        }
        try {
            oddrVocabularyStream = (InputStream[]) props.get(ODDR_VOCABULARY_STREAM_PROP);
        } catch (Exception ex) {
            oddrVocabularyStream = null;
        }

        if ((ddrCoreVocabularyPath == null || ddrCoreVocabularyPath.trim().length() == 0) && ddrCoreVocabulayStream == null) {
            throw new InitializationException(InitializationException.INITIALIZATION_ERROR, new IllegalArgumentException("Can not find property " + DDR_CORE_VOCABULARY_PATH_PROP));
        }

        if ((oddrVocabularyPath == null || oddrVocabularyPath.trim().length() == 0) && oddrVocabularyStream == null) {
            throw new InitializationException(InitializationException.INITIALIZATION_ERROR, new IllegalArgumentException("Can not find property " + ODDR_VOCABULARY_PATH_PROP));
        }

        VocabularyHandler vocabularyHandler = new VocabularyHandler();
        Vocabulary vocabulary = null;

        if (ddrCoreVocabulayStream != null) {
            parseVocabularyFromStream(vocabularyHandler, DDR_CORE_VOCABULARY_STREAM_PROP, ddrCoreVocabulayStream);
        } else {
            parseVocabularyFromPath(vocabularyHandler, DDR_CORE_VOCABULARY_PATH_PROP, ddrCoreVocabularyPath);
        }
        vocabulary = vocabularyHandler.getVocabulary();
        vocabularies.put(vocabulary.getVocabularyIRI(), vocabulary);

        if (oddrVocabularyStream != null) {
            for (InputStream stream : oddrVocabularyStream) {
                vocabularyHandler = new VocabularyHandler();
                parseVocabularyFromStream(vocabularyHandler, ODDR_VOCABULARY_STREAM_PROP, stream);
                vocabulary = vocabularyHandler.getVocabulary();
                vocabularies.put(vocabulary.getVocabularyIRI(), vocabulary);
            }
        } else {
            String[] oddrVocabularyPaths = oddrVocabularyPath.split(",");
            for (int i = 0; i < oddrVocabularyPaths.length; i++) {
                oddrVocabularyPaths[i] = oddrVocabularyPaths[i].trim();
            }
            for (String oddVocabularyString : oddrVocabularyPaths) {
                vocabularyHandler = new VocabularyHandler();
                parseVocabularyFromPath(vocabularyHandler, ODDR_VOCABULARY_PATH_PROP, oddVocabularyString);
                vocabulary = vocabularyHandler.getVocabulary();
                vocabularies.put(vocabulary.getVocabularyIRI(), vocabulary);
            }
        }

        String oddrLimitedVocabularyPath = props.getProperty(ODDR_LIMITED_VOCABULARY_PATH_PROP);
        InputStream oddrLimitedVocabularyStream = (InputStream)props.get(ODDR_LIMITED_VOCABULARY_STREAM_PROP);

        if (oddrLimitedVocabularyStream != null) {
            parseVocabularyFromStream(vocabularyHandler, ODDR_LIMITED_VOCABULARY_STREAM_PROP, oddrLimitedVocabularyStream);

        } else {
           if (oddrLimitedVocabularyPath != null && oddrLimitedVocabularyPath.trim().length() != 0) {
                vocabularyHandler = new VocabularyHandler();
                parseVocabularyFromPath(vocabularyHandler, ODDR_LIMITED_VOCABULARY_PATH_PROP, oddrLimitedVocabularyPath);
            }
        }
        vocabulary = vocabularyHandler.getVocabulary();
        vocabularies.put(ODDR_LIMITED_VOCABULARY_IRI, vocabulary);

        vocabularyHolder = new VocabularyHolder(vocabularies);

        vocabularyHandler = null;
        vocabularies = null;

    }

    private void parseVocabularyFromPath(VocabularyHandler vocabularyHandler, String prop, String path) throws InitializationException {
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

    private void parseVocabularyFromStream(VocabularyHandler vocabularyHandler, String prop, InputStream inputStream) throws InitializationException {
        InputStream stream = null;
        SAXParser parser = null;
        stream = inputStream;

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
            throw new InitializationException(InitializationException.INITIALIZATION_ERROR, new RuntimeException("Can not parse document in property: " + prop));

        } catch (IOException ex) {
            throw new InitializationException(InitializationException.INITIALIZATION_ERROR, new RuntimeException("Can not open " + prop));
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

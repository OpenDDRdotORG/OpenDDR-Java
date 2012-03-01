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
package org.openddr.simpleapi.oddr.model.vocabulary;

import java.util.Map;

public class Vocabulary {

    private String[] aspects = null;
    private Map<String, VocabularyProperty> properties = null;
    private Map<String, VocabularyVariable> vocabularyVariables = null;
    private String vocabularyIRI = null;

    public String[] getAspects() {
        return aspects;
    }

    public void setAspects(String[] aspects) {
        this.aspects = aspects;
    }

    public Map<String, VocabularyProperty> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, VocabularyProperty> properties) {
        this.properties = properties;
    }

    public Map<String, VocabularyVariable> getVocabularyVariables() {
        return vocabularyVariables;
    }

    public void setVocabularyVariables(Map<String, VocabularyVariable> vocabularyVariables) {
        this.vocabularyVariables = vocabularyVariables;
    }

    public String getVocabularyIRI() {
        return vocabularyIRI;
    }

    public void setVocabularyIRI(String vocabularyIRI) {
        this.vocabularyIRI = vocabularyIRI;
    }
}

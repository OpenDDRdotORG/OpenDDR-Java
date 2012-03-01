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
package org.openddr.simpleapi.oddr.vocabulary;

import java.util.Map;
import org.apache.commons.lang.ArrayUtils;
import org.openddr.simpleapi.oddr.cache.Cache;
import org.openddr.simpleapi.oddr.cache.CacheImpl;
import org.openddr.simpleapi.oddr.model.vocabulary.Vocabulary;
import org.openddr.simpleapi.oddr.model.vocabulary.VocabularyProperty;
import org.w3c.ddr.simple.exception.NameException;

public class VocabularyHolder {

    private Map<String, Vocabulary> vocabularies = null;
    private Cache vocabularyPropertyCache = new CacheImpl();

    public VocabularyHolder(Map<String, Vocabulary> vocabularies) {
        this.vocabularies = vocabularies;
    }

    public void existVocabulary(String vocabularyIRI) throws NameException {
        if (vocabularies.get(vocabularyIRI) == null) {
            throw new NameException(NameException.VOCABULARY_NOT_RECOGNIZED, "unknow \"" + vocabularyIRI + "\" vacabulary");
        }
    }

    public VocabularyProperty existProperty(String propertyName, String aspect, String vocabularyIRI) throws NameException {
        String realAspect = aspect;
        VocabularyProperty vocabularyProperty = (VocabularyProperty) vocabularyPropertyCache.getCachedElement(propertyName + aspect + vocabularyIRI);

        if (vocabularyProperty == null) {
            if (vocabularies.get(vocabularyIRI) != null) {
                Map<String, VocabularyProperty> propertyMap = vocabularies.get(vocabularyIRI).getProperties();
                vocabularyProperty = propertyMap.get(propertyName);

                if (vocabularyProperty != null) {
                    if (realAspect != null && realAspect.trim().length() > 0) {
                        if (ArrayUtils.contains(vocabularyProperty.getAspects(), realAspect)) {
                            vocabularyPropertyCache.setCachedElement(propertyName + aspect + vocabularyIRI, vocabularyProperty);
                            return vocabularyProperty;

                        } else {
                            throw new NameException(NameException.ASPECT_NOT_RECOGNIZED, "unknow \"" + realAspect + "\" aspect");
                        }

                    } else {
                        return vocabularyProperty;
                    }

                } else {
                    throw new NameException(NameException.PROPERTY_NOT_RECOGNIZED, "unknow \"" + propertyName + "\" property");
                }

            } else {
                throw new NameException(NameException.VOCABULARY_NOT_RECOGNIZED, "unknow \"" + vocabularyIRI + "\" vacabulary");
            }

        } else {
            return vocabularyProperty;
        }
    }

    public Map<String, Vocabulary> getVocabularies() {
        return vocabularies;
    }
}

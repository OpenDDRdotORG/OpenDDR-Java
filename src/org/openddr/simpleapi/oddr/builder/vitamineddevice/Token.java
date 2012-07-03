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
package org.openddr.simpleapi.oddr.builder.vitamineddevice;

import java.util.regex.Pattern;

//same approach as token strings with commndly used regex patterns 
public class Token {

	private String id; //token string
	private Pattern midPattern; //case-insensitive '.*id.*' regex
	private Pattern tailPattern; //case-insensitive '.*id' regex
	private Pattern semicolonPattern; //case-insensitive '.*id.?;.*' regex
	private Pattern quotePattern; //case-insensitive '.* + Pattern.quote(token) + .*'
	private String looseId; //loose token
	private Pattern looseMidPattern; //case-insensitive '.*looseID.*' regex
	private Pattern looseTailPattern; //case-insensitive '.*looseID' regex
	private Pattern looseSemicolonPattern; //case-insensitive '.*looseID.?;.*' regex
	
	private Pattern caseSensitiveMidPattern; //case-sensitive '.*id.*' regex
	private Pattern caseSensitiveTailPattern; //case-sensitive '.*id' regex
	private Pattern caseSensitiveSemicolonPattern; //case-sensitive '.*id.?;.*' regex
	private Pattern caseSensitiveQuotePattern; //case-sensitive '.* + Pattern.quote(token) + .*'
	private Pattern caseSensitiveLooseMidPattern; //case-sensitive '.*looseID.*' regex
	private Pattern caseSensitiveLooseTailPattern; //case-sensitive '.*looseID' regex
	private Pattern caseSensitiveLooseSemicolonPattern; //case-sensitive '.*looseID.?;.*' regex
	
	public Token(String id){
		this.id = id;
		this.midPattern = Pattern.compile("(?i).*" + this.id + ".*");
		this.tailPattern = Pattern.compile("(?i).*" + this.id);
		this.semicolonPattern = Pattern.compile("(?i).*" + this.id + ".?;.*");
		this.quotePattern = Pattern.compile("(?i).*" + Pattern.quote(this.id) + ".*");
		this.looseId = this.id.replaceAll("[ _/-]", ".?");
		this.looseMidPattern = Pattern.compile("(?i).*" + this.looseId + ".*");
		this.looseTailPattern = Pattern.compile("(?i).*" + this.looseId);
		this.looseSemicolonPattern = Pattern.compile("(?i).*" + this.looseId + ".?;.*");
		
		this.caseSensitiveMidPattern = Pattern.compile(".*" + this.id + ".*");
		this.caseSensitiveTailPattern = Pattern.compile(".*" + this.id);
		this.caseSensitiveSemicolonPattern = Pattern.compile(".*" + this.id + ".?;.*");
		this.caseSensitiveQuotePattern = Pattern.compile(".*" + Pattern.quote(this.id) + ".*");
		this.caseSensitiveLooseMidPattern = Pattern.compile(".*" + this.looseId + ".*");
		this.caseSensitiveLooseTailPattern = Pattern.compile(".*" + this.looseId);
		this.caseSensitiveLooseSemicolonPattern = Pattern.compile(".*" + this.looseId + ".?;.*");
	}

	public String getId() {
		return id;
	}

	public String getLooseId() {
		return looseId;
	}

	public Pattern getMidPattern() {
		return midPattern;
	}

	public Pattern getTailPattern() {
		return tailPattern;
	}

	public Pattern getSemicolonPattern() {
		return semicolonPattern;
	}

	public Pattern getLooseMidPattern() {
		return looseMidPattern;
	}

	public Pattern getLooseTailPattern() {
		return looseTailPattern;
	}

	public Pattern getLooseSemicolonPattern() {
		return looseSemicolonPattern;
	}

	public Pattern getQuotePattern() {
		return quotePattern;
	}

	public Pattern getCaseSensitiveMidPattern() {
		return caseSensitiveMidPattern;
	}

	public Pattern getCaseSensitiveTailPattern() {
		return caseSensitiveTailPattern;
	}

	public Pattern getCaseSensitiveSemicolonPattern() {
		return caseSensitiveSemicolonPattern;
	}

	public Pattern getCaseSensitiveQuotePattern() {
		return caseSensitiveQuotePattern;
	}

	public Pattern getCaseSensitiveLooseMidPattern() {
		return caseSensitiveLooseMidPattern;
	}

	public Pattern getCaseSensitiveLooseTailPattern() {
		return caseSensitiveLooseTailPattern;
	}

	public Pattern getCaseSensitiveLooseSemicolonPattern() {
		return caseSensitiveLooseSemicolonPattern;
	}
}

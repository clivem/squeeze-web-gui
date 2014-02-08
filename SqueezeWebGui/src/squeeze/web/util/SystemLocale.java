/**
 *  Web Configuration and Control Interface (Java version) for a 
 *  Wandboard Squeeze Player
 *  
 *  Copyright (C) 2013-2014 Clive Messer <clive.m.messer@gmail.com>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package squeeze.web.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Clive Messer <clive.m.messer@gmail.com>
 *
 */
public class SystemLocale {

	private final static Pattern pattern = Pattern.compile("([^ ]+) +([^ ]+) +([^ ]+) +(.*)");
	
	private String locale;
	private String encoding;
	private String font;
	private String description;
	
	/**
	 * @param locale
	 * @param encoding
	 * @param font
	 * @param description
	 */
	public SystemLocale(String locale, String encoding, String font,
			String description) {
		
		super();
		
		this.locale = locale;
		this.encoding = encoding;
		this.font = font;
		this.description = description;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		
		return description;
	}

	/**
	 * @return the locale
	 */
	public String getLocale() {
		return locale;
	}

	/**
	 * @return the encoding
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * @return the font
	 */
	public String getFont() {
		return font;
	}

	/**
	 * @param locale
	 * @return
	 */
	public final static SystemLocale getLocale(String locale) {

		synchronized (pattern) {
			Matcher m = pattern.matcher(locale);
			if (m.matches()) {
				return new SystemLocale(m.group(1), m.group(2), m.group(3), m.group(4));
			}
		}
		
		return null;
	}
	
	/**
	 * @param args
	 */
	public final static void main(String[] args) {
		SystemLocale l = SystemLocale.getLocale("en_GB.UTF-8 utf8 latarcyrheb-sun16 English (Great Britain)");
		if (l != null) {
			System.out.println(l.getDescription());
		}
	}
}

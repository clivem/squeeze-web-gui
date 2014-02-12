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

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

/**
 * @author Clive Messer <clive.m.messer@gmail.com>
 *
 */
public class SystemLocale {

	private final static Pattern pattern = Pattern.compile("([^ ]+) +([^ ]+) +([^ ]+) +(.*)");
	
	private final static String LOCALE_FILENAME = "/etc/locale.conf";
	private final static String LOCALE_LIST_FILENAME = "/usr/share/system-config-language/locale-list";

	private final static Logger LOGGER = Logger.getLogger(SystemLocale.class);
	
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
	private final static SystemLocale createSystemLocale(String locale) {

		synchronized (pattern) {
			Matcher m = pattern.matcher(locale);
			if (m.matches()) {
				return new SystemLocale(m.group(1), m.group(2), m.group(3), m.group(4));
			} else {
				LOGGER.warn("createSystemLocale(locale=" + locale + "): Invalid locale!");
			}
		}
		
		return null;
	}
	
	/**
	 * @return
	 */
	public final static List<SystemLocale> getSystemLocaleList() {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("getSystemLocaleList()");
		}
		
		ArrayList<SystemLocale> localeList = new ArrayList<SystemLocale>();
		
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(LOCALE_LIST_FILENAME));
			String line = null;
			while ((line = reader.readLine()) != null) {
				SystemLocale l = SystemLocale.createSystemLocale(line.trim());
				if (l != null) {
					localeList.add(l);
				}
			}
		} catch (Exception e) {
			LOGGER.warn("getSystemLocaleList()", e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (Exception e) {}
			}
		}
		
		return localeList;
	}
	
	/**
	 * @return
	 */
	public final static String getSystemLocale() {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("getSystemLocale()");
		}
		
		String line = Util.getFirstLineFromFile(LOCALE_FILENAME);
		if (line != null) {
			line.trim();
			if (line.startsWith("LANG=")) {
				return Util.trimQuotes(line.substring(5).trim());
			}
		}
		
		return null;
	}	

	/**
	 * @param args
	 */
	public final static void main(String[] args) {
		SystemLocale l = SystemLocale.createSystemLocale("en_GB.UTF-8 utf8 latarcyrheb-sun16 English (Great Britain)");
		System.out.println(l);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SystemLocale[locale=" + locale + ", encoding=" + encoding
				+ ", font=" + font + ", description=" + description + "]";
	}
}

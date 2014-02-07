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
package squeeze.web;

import java.util.List;

import org.apache.log4j.Logger;

import squeeze.web.util.TimeZone;
import squeeze.web.util.Util;

import com.opensymphony.xwork2.ActionSupport;

/**
 * @author Clive Messer <clive.m.messer@gmail.com>
 *
 */
public class ConfigurationAction extends ActionSupport {

	private static final long serialVersionUID = -6661521121373283855L;

	private final static Logger LOGGER = Logger.getLogger(ConfigurationAction.class);
	
	private final static List<String> ZONE_LIST = TimeZone.getTimeZoneList();
	private final static List<String> LOCALE_LIST = Util.getLocaleList();
	
	public final static String HOSTNAME_FILENAME = "/etc/hostname";
	public final static String FEDORA_VERSION_FILENAME = "/etc/fedora-release";
	public final static String CSOS_VERSION_FILENAME = "/etc/csos-release";
	public final static String LOCALE_FILENAME = "/etc/locale.conf";
	
	private String timeZone = null;
	private List<String> timeZoneList = null;

	private String hostName = null;
	private String fedoraVersion = null;
	private String csosVersion = null;
	
	private String locale_ = null;
	private List<String> localeList = null;
	
	/**
	 * 
	 */
	public ConfigurationAction() {
		
		super();
		timeZoneList = ZONE_LIST;
		localeList = LOCALE_LIST;
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ConfigurationAction()");
		}
	}
	
	/* (non-Javadoc)
	 * @see com.opensymphony.xwork2.ActionSupport#execute()
	 */
	public String execute() throws Exception {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("execute()");
		}
		
		String result = populate();
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("execute() returns " + result);
		}
		
		return result;
	}
	
	/**
	 * @return
	 * @throws Exception
	 */
	public String populate() throws Exception {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("populate()");
		}
		
		hostName = Util.getVersion(HOSTNAME_FILENAME);
		
		fedoraVersion = Util.getVersion(FEDORA_VERSION_FILENAME);
		csosVersion = Util.getVersion(CSOS_VERSION_FILENAME);
		
		locale_ = Util.getLocale(LOCALE_FILENAME);
		if (locale_ != null && !localeList.contains(locale_)) {
			localeList.add(0, locale_);
		}
		
		timeZone = TimeZone.getCurrentTimeZone();
		if (timeZone != null && !timeZoneList.contains(timeZone)) {
			timeZoneList.add(0, timeZone);
		}
		
		String result = "populate";
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("populate() returns " + result);
		}
		
		return result;
	}

	/**
	 * @return
	 * @throws Exception
	 */
	public String timeZone() throws Exception {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("timeZone()");
		}
		
		try {
			if (timeZone != null && timeZone.trim().length() > 0) {
				Util.setTimeZone(timeZone);
			}
		} catch (Exception e) {
			LOGGER.warn("Caught exception setting timezone!", e);
			throw e;
		}
		
		try {
			if (locale_ != null && locale_.trim().length() > 0) {
				Util.setLocale(LOCALE_FILENAME, locale_);
			}
		} catch (Exception e) {
			LOGGER.warn("Caught exception setting locale!", e);
			throw e;
		}
		
		String result = SUCCESS;
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("timeZone() returns " + result);
		}
		
		return result;
	}

	/**
	 * @return the timeZoneList
	 */
	public List<String> getTimeZoneList() {
		return timeZoneList;
	}

	/**
	 * @return the timeZone
	 */
	public String getTimeZone() {
		return timeZone;
	}

	/**
	 * @param timeZone the timeZone to set
	 */
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	/**
	 * @return the csosVersion
	 */
	public String getCsosVersion() {
		return csosVersion;
	}

	/**
	 * @param csosVersion the csosVersion to set
	 */
	public void setCsosVersion(String csosVersion) {
		this.csosVersion = csosVersion;
	}

	/**
	 * @return the fedoraVersion
	 */
	public String getFedoraVersion() {
		return fedoraVersion;
	}

	/**
	 * @param fedoraVersion the fedoraVersion to set
	 */
	public void setFedoraVersion(String fedoraVersion) {
		this.fedoraVersion = fedoraVersion;
	}

	/**
	 * @return the hostName
	 */
	public String getHostName() {
		return hostName;
	}

	/**
	 * @param hostName the hostName to set
	 */
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	/**
	 * @return the locale_
	 */
	public String getLocale_() {
		return locale_;
	}

	/**
	 * @param locale_ the locale_ to set
	 */
	public void setLocale_(String locale_) {
		this.locale_ = locale_;
	}

	/**
	 * @return the localeList
	 */
	public List<String> getLocaleList() {
		return localeList;
	}

	/**
	 * @param localeList the localeList to set
	 */
	public void setLocaleList(List<String> localeList) {
		this.localeList = localeList;
	}
}

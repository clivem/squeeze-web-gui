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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import squeeze.web.util.Commands;
import squeeze.web.util.ExecuteProcess;
import squeeze.web.util.SystemLocale;
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
	private final static List<SystemLocale> LOCALE_LIST = SystemLocale.getSystemLocaleList();
	
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
	private List<SystemLocale> localeList = null;
	
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
		
		hostName = Util.getFirstLineFromFile(HOSTNAME_FILENAME);
		
		fedoraVersion = Util.getFirstLineFromFile(FEDORA_VERSION_FILENAME);
		csosVersion = Util.getFirstLineFromFile(CSOS_VERSION_FILENAME);		
		locale_ = getSystemLocale();
		
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
	public String save() throws Exception {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("save()");
		}
		
		try {
			if (timeZone != null && timeZone.trim().length() > 0) {
				Util.setTimeZone(timeZone);
			}
		} catch (Exception e) {
			LOGGER.warn("Caught exception setting timezone!", e);
			throw e;
		}
		
		if (hostName != null && hostName.trim().length() > 0) {
			File file = null;
			try {
				file = writeTempConfig("hostname", hostName.trim());
				replaceConfig(file, Commands.SCRIPT_HOSTNAME_UPDATE);
			} catch (Exception e) {
				LOGGER.error("Caught exception saving hostname!", e);
				throw e;
			} finally {
				if (file != null) {
					try {
						file.delete();
					} catch (Exception e) {}
				}
			}
		}
		
		if (locale_ != null && locale_.trim().length() > 0) {
			File file = null;
			try {
				file = writeTempConfig("locale", "LANG=\"" + locale_.trim() + "\"");
				replaceConfig(file, Commands.SCRIPT_LOCALE_UPDATE);
			} catch (Exception e) {
				LOGGER.error("Caught exception saving locale!", e);
				throw e;
			} finally {
				if (file != null) {
					try {
						file.delete();
					} catch (Exception e) {}
				}
			}
		}
		
		String result = SUCCESS;
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("save() returns " + result);
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
	 * @return the fedoraVersion
	 */
	public String getFedoraVersion() {
		return fedoraVersion;
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
	public List<SystemLocale> getLocaleList() {
		return localeList;
	}
	
	/**
	 * @param configName
	 * @param locale_
	 * @return
	 * @throws IOException
	 */
	private File writeTempConfig(String configName, String config) 
			throws IOException {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("writeTempConfig(configName=" + 
							configName + ", config=" + config + ")");
		}

		BufferedWriter bw = null;
		try {
			File tempFile = Util.createTempFile(configName + "_config_", ".txt");
			bw = new BufferedWriter(new FileWriter(tempFile));
			bw.write(config + Util.LINE_SEP);
			return tempFile;
		} finally {
			if (bw != null) {
				try {
					bw.flush();
				} catch (Exception e) {}
				
				try {
					bw.close();
				} catch (Exception e) {}
 			}
		}
	}
	
	/**
	 * @param tmpFile
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private int replaceConfig(File tmpFile, String script)
			throws IOException, InterruptedException {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("replaceConfig(tmpFile=" + tmpFile + ", script=" + script + ")");
		}

		String[] cmdLineArgs = new String[] {
				Commands.CMD_SUDO, script, 
				tmpFile.getAbsolutePath()
		};
		
		return ExecuteProcess.executeCommand(cmdLineArgs);
	}
	
	/**
	 * @return
	 */
	private final static String getSystemLocale() {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("getSystemLocale()");
		}
		
		String line = Util.getFirstLineFromFile(LOCALE_FILENAME);
		if (line != null) {
			line.trim();
			if (line.startsWith("LANG=")) {
				line = line.substring(5).trim();
				if (line.startsWith("\"")) {
					line = line.substring(1);
				}
				if (line.endsWith("\"")) {
					line = line.substring(0, line.length() - 1);
				}
				return line;
			}
		}
		
		return null;
	}	
}

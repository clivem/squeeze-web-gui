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

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import squeeze.web.util.Commands;
import squeeze.web.util.ExecuteProcess;
import squeeze.web.util.SambaConfig;
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
	
	private String timeZone = null;
	private List<String> timeZoneList = null;

	private String hostName = null;
	private String fedoraVersion = null;
	private String csosVersion = null;
	
	private String systemLocale = null;
	private List<SystemLocale> systemLocaleList = null;
	
	private String sambaNetbiosName = null;
	private String sambaWorkgroup = null;
	
	/**
	 * 
	 */
	public ConfigurationAction() {
		
		super();
		timeZoneList = ZONE_LIST;
		systemLocaleList = LOCALE_LIST;
		
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
		
		fedoraVersion = Util.getFedoraVersion();
		csosVersion = Util.getCsosVersion();
		hostName = Util.getHostName();
		
		SambaConfig sambaConfig = SambaConfig.getSambaConfig();
		sambaNetbiosName = sambaConfig.getNetbiosName();
		sambaWorkgroup = sambaConfig.getWorkgroup();
		
		systemLocale = SystemLocale.getSystemLocale();
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
				file = Util.writeTempConfig("hostname", hostName.trim());
				Util.replaceConfig(file, Commands.SCRIPT_HOSTNAME_UPDATE);
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
		
		if (systemLocale != null && systemLocale.trim().length() > 0) {
			File file = null;
			try {
				file = Util.writeTempConfig("locale", "LANG=\"" + systemLocale.trim() + "\"");
				Util.replaceConfig(file, Commands.SCRIPT_LOCALE_UPDATE);
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
		
		SambaConfig oldSambaConfig = SambaConfig.getSambaConfig();
		boolean sambaConfigChanged = false;
		if (sambaNetbiosName != null && sambaNetbiosName.trim().length() > 0) {
			sambaNetbiosName = sambaNetbiosName.trim();
			if (!sambaNetbiosName.equals(oldSambaConfig.getNetbiosName())) {
				// update config
				Util.updateConfig(sambaNetbiosName, Commands.SCRIPT_SAMBA_NETBIOS_NAME_UPDATE);
				sambaConfigChanged = true;
			}
		}
		if (sambaWorkgroup != null && sambaWorkgroup.trim().length() > 0) {
			sambaWorkgroup = sambaWorkgroup.trim();
			if (!sambaWorkgroup.equals(oldSambaConfig.getWorkgroup())) {
				// update config
				Util.updateConfig(sambaWorkgroup, Commands.SCRIPT_SAMBA_WORGROUP_UPDATE);
				sambaConfigChanged = true;
			}
		}
		if (sambaConfigChanged) {
			// restart samba
			condRestartSamba();
		}
		
		String result = SUCCESS;
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("save() returns " + result);
		}
		
		return result;
	}
	
	/**
	 * @return
	 * @throws InterruptedException
	 * @throws IOException
	 */
	private int condRestartSamba() 
			throws InterruptedException, IOException {
		
		return ExecuteProcess.executeCommand(
				Util.getSystemctlCondRestartCmdLine("smb.service"));
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
	 * @return the systemLocale
	 */
	public String getSystemLocale() {
		return systemLocale;
	}

	/**
	 * @param systemLocale the systemLocale to set
	 */
	public void setSystemLocale(String systemLocale) {
		this.systemLocale = systemLocale;
	}

	/**
	 * @return the systemLocaleList
	 */
	public List<SystemLocale> getSystemLocaleList() {
		return systemLocaleList;
	}

	/**
	 * @param timeZoneList the timeZoneList to set
	 */
	public void setTimeZoneList(List<String> timeZoneList) {
		this.timeZoneList = timeZoneList;
	}

	/**
	 * @param systemLocaleList the systemLocaleList to set
	 */
	public void setSystemLocaleList(List<SystemLocale> systemLocaleList) {
		this.systemLocaleList = systemLocaleList;
	}

	/**
	 * @return the sambaNetbiosName
	 */
	public String getSambaNetbiosName() {
		return sambaNetbiosName;
	}

	/**
	 * @param sambaNetbiosName the sambaNetbiosName to set
	 */
	public void setSambaNetbiosName(String sambaNetbiosName) {
		this.sambaNetbiosName = sambaNetbiosName;
	}

	/**
	 * @return the sambaWorkgroup
	 */
	public String getSambaWorkgroup() {
		return sambaWorkgroup;
	}
	
	/**
	 * @param sambaWorkgroup the sambaWorkgroup to set
	 */
	public void setSambaWorkgroup(String sambaWorkgroup) {
		this.sambaWorkgroup = sambaWorkgroup;
	}
}

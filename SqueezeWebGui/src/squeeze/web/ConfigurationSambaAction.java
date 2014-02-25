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

import java.io.IOException;

import org.apache.log4j.Logger;

import squeeze.web.util.Commands;
import squeeze.web.util.ExecuteProcess;
import squeeze.web.util.SambaConfig;
import squeeze.web.util.Util;

/**
 * @author Clive Messer <clive.m.messer@gmail.com>
 *
 */
public class ConfigurationSambaAction extends ConfigurationAction {

	private static final long serialVersionUID = -6661521121373283855L;

	private final static Logger LOGGER = Logger.getLogger(ConfigurationSambaAction.class);
	
	/**
	 * 
	 */
	public ConfigurationSambaAction() {
		
		super();

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ConfigurationSambaAction()");
		}
	}
	
	/* (non-Javadoc)
	 * @see com.opensymphony.xwork2.ActionSupport#validate()
	 */
	@Override
	public void validate() {
	}

	/**
	 * @return
	 * @throws Exception
	 */
	public String save() throws Exception {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("save()");
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
}

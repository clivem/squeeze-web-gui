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

import org.apache.log4j.Logger;

import squeeze.web.util.Commands;
import squeeze.web.util.Util;
import squeeze.web.util.Validate;

/**
 * @author Clive Messer <clive.m.messer@gmail.com>
 *
 */
public class ConfigurationHostnameAction extends ConfigurationAction {

	private static final long serialVersionUID = -6661521121373283855L;

	private final static Logger LOGGER = Logger.getLogger(ConfigurationHostnameAction.class);
	
	/**
	 * 
	 */
	public ConfigurationHostnameAction() {
		
		super();

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ConfigurationHostnameAction()");
		}
	}
	
	/* (non-Javadoc)
	 * @see com.opensymphony.xwork2.ActionSupport#validate()
	 */
	@Override
	public void validate() {
		
		if (hostName == null || hostName.trim().length() < 1 || 
				!hostName.trim().matches(Validate.REGEX_HOST_ADDRESS)) {
			addActionError(getText("configuration.validation.hostName.fail"));
		}
		
		if (hasActionErrors()) {
			try {
				populate();
			} catch (Exception e) {}
		}
	}

	/**
	 * @return
	 * @throws Exception
	 */
	public String save() throws Exception {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("save()");
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
		
		String result = SUCCESS;
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("save() returns " + result);
		}
		
		return result;
	}	
}

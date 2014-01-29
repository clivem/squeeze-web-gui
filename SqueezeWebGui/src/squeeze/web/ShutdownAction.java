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

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

/**
 * @author Clive Messer <clive.m.messer@gmail.com>
 *
 */
public class ShutdownAction extends ActionSupport {

	private static final long serialVersionUID = -4872499992773664480L;
	
	private final static Logger LOGGER = Logger.getLogger(ShutdownAction.class);
	
	protected boolean cbForceReboot = false;

	/**
	 * 
	 */
	public ShutdownAction() {
		
		super();
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ShutdownAction()");
		}
	}

	/* (non-Javadoc)
	 * @see com.opensymphony.xwork2.ActionSupport#execute()
	 */
	public String execute() throws Exception {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("execute()");
		}
		
		String result = SUCCESS;
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("execute() returns " + result);
		}
		
		return result;
	}
	
	/**
	 * @return the cbForceReboot
	 */
	public boolean isCbForceReboot() {
		return cbForceReboot;
	}

	/**
	 * @param cbForceReboot the cbForceReboot to set
	 */
	public void setCbForceReboot(boolean cbForceReboot) {
		this.cbForceReboot = cbForceReboot;
	}
}

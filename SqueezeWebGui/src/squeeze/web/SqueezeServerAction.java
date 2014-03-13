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

import squeeze.web.util.Util;

/**
 * @author Clive Messer <clive.m.messer@gmail.com>
 *
 */
public class SqueezeServerAction extends SystemctlAction {

	private static final long serialVersionUID = -6022146345888210034L;
	
	private final static Logger LOGGER = Logger.getLogger(SqueezeServerAction.class);
	
	private final static String NAME = "squeezeboxserver";
	private final static String SERVICE_NAME = NAME + ".service";
	private final static String LOG_FILE = "/var/log/squeezeboxserver/server.log";
	
	protected String log = null;
		
	/**
	 * 
	 */
	public SqueezeServerAction() {
		
		super();
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("SqueezeServerAction()");
		}
	}
	
	/**
	 * @return
	 * @throws Exception
	 */
	public String populate() throws Exception {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("populate()");
		}

		try {
			populateServiceStatus();
			populateLog();
		} catch (Exception e) {
			LOGGER.error("Caught exception while populating " + getServiceName() + "!", e);
			throw e;
		}
 		
		String result = "populate";
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("populate() returns " + result);
		}
		
		return result;
	}	
	
	/**
	 * 
	 */
	protected void populateLog() {
		
		try {
			log = Util.tail(new File(LOG_FILE), 10);
		} catch (Exception e) {
			LOGGER.warn("Caught exception trying to read the log file!", e);
		}
	}
		
	/* (non-Javadoc)
	 * @see squeeze.web.SystemctlAction#getServiceName()
	 */
	@Override
	public String getServiceName() {
		
		return SERVICE_NAME;
	}
	
	/**
	 * @return the log
	 */
	public String getLog() {
		return log;
	}
}

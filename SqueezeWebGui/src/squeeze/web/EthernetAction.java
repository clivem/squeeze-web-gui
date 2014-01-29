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

import squeeze.web.util.WebConfig;

/**
 * @author Clive Messer <clive.m.messer@gmail.com>
 *
 */
public class EthernetAction extends InterfaceAction {
	
	private static final long serialVersionUID = -6566605197191164096L;

	private final static Logger LOGGER = Logger.getLogger(EthernetAction.class);
	
	//public final static String INTERFACE_NAME = "p20p1";
	//public final static String INTERFACE_NAME = "p4p1";
	//public final static String INTERFACE_NAME = "eth0";

	/**
	 * 
	 */
	public EthernetAction() {
		
		super();
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("EthernetAction()");
		}
	}
	
	/* (non-Javadoc)
	 * @see squeeze.web.InterfaceAction#getInterfaceName()
	 */
	public String getInterfaceName() {
		
		//return INTERFACE_NAME;
		return WebConfig.getWiredInterfaceName();
	}
}

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

import squeeze.web.util.NameFlag;
import squeeze.web.util.WebConfig;

/**
 * @author Clive Messer <clive.m.messer@gmail.com>
 *
 */
public class WirelessAction extends InterfaceAction {

	private static final long serialVersionUID = -3670704165326089152L;
	
	private final static Logger LOGGER = Logger.getLogger(WirelessAction.class);

	//private final static String INTERFACE_NAME = "wlan0";
	
	/**
	 * 
	 */
	public WirelessAction() {
		
		super();
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("WirelessAction()");
		}
	}

	/* (non-Javadoc)
	 * @see squeeze.web.InterfaceAction#getInterfaceName()
	 */
	@Override
	public String getInterfaceName() {
		
		//return INTERFACE_NAME;
		return WebConfig.getWirelessInterfaceName();
	}
	
	/**
	 * @return
	 */
	public List<NameFlag> getWirelessRegDomainList() {
		
		return NameFlag.WIRELESS_REG_DOMAIN_LIST;
	}
}

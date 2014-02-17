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

import java.util.Iterator;

import org.apache.log4j.Logger;

import squeeze.web.util.StorageMount;
import squeeze.web.util.Util;


/**
 * @author Clive Messer <clive.m.messer@gmail.com>
 *
 */
public class StorageUnmountAction extends StorageAction {

	private static final long serialVersionUID = -2979284633849195589L;
	
	private final static Logger LOGGER = Logger.getLogger(StorageUnmountAction.class);

	/**
	 * 
	 */
	public StorageUnmountAction() {
		
		super();
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("StorageUnmountAction()");
		}
	}

	/**
	 * @return
	 * @throws Exception
	 */
	public String unmount() throws Exception {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("unmount()");
		}
		
		if (userMountList != null) {
			Iterator<StorageMount> it = userMountList.iterator();
			while (it.hasNext()) {
				StorageMount mount = it.next();
				if (StorageMount.ACTION_UNMOUNT.equals(mount.getAction())) {
					if (LOG.isDebugEnabled()) {
						LOG.debug("Unmount: " + mount);
					}
					Util.umount(mount.getMountPoint());
				} else if (StorageMount.ACTION_REMOUNT.equals(mount.getAction())) {
					if (LOG.isDebugEnabled()) {
						LOG.warn("Remount: " + mount);
					}
				}
			}
		}
				
		String result = populate();
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("unmount() returns " + result);
		}
		
		return result;
	}
}

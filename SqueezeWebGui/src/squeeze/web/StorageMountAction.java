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
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import squeeze.web.util.StorageMount;
import squeeze.web.util.Util;


/**
 * @author Clive Messer <clive.m.messer@gmail.com>
 *
 */
public class StorageMountAction extends StorageAction {

	private static final long serialVersionUID = -2979284633849195589L;
	
	private final static Logger LOGGER = Logger.getLogger(StorageMountAction.class);

	/**
	 * 
	 */
	public StorageMountAction() {
		
		super();
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("StorageMountAction()");
		}
	}
	
	private void action_(List<StorageMount> mountList) 
			throws InterruptedException, IOException {
		
		if (mountList != null) {
			Iterator<StorageMount> it = mountList.iterator();
			while (it.hasNext()) {
				StorageMount mount = it.next();
				if (StorageMount.ACTION_MOUNT.equals(mount.getAction())) {
					if (LOG.isDebugEnabled()) {
						LOG.debug("Mount: " + mount);
					}
					// Make sure it isn't already mounted.
					String[] cmdLineArgs = Util.createUmountCommand(mount);
					int result = Util.umount(cmdLineArgs);
					// Mount it.
					cmdLineArgs = Util.createMountCommand(mount);
					result = Util.mount(cmdLineArgs);
					if (result != 0) {
						addActionError("Mount '" + Util.arrayToString(cmdLineArgs) + "' returned: " + result +
								". (If successful, return code should be 0.)");
					}					
				} else if (StorageMount.ACTION_UNMOUNT.equals(mount.getAction())) {
					if (LOG.isDebugEnabled()) {
						LOG.debug("Unmount: " + mount);
					}
					String[] cmdLineArgs = Util.createUmountCommand(mount);
					int result = Util.umount(cmdLineArgs);
					if (result != 0) {
						addActionError("Unmount '" + Util.arrayToString(cmdLineArgs) + "' returned: " + result +
								". (If successful, return code should be 0.)");
					}
				} else if (StorageMount.ACTION_REMOUNT.equals(mount.getAction())) {
					if (LOG.isDebugEnabled()) {
						LOG.warn("Remount: " + mount);
						String[] cmdLineArgs = Util.createReMountCommand(mount);
						int result = Util.remount(cmdLineArgs);
						if (result != 0) {
							addActionError("Remount '" + Util.arrayToString(cmdLineArgs) + "' returned: " + result +
									". (If successful, return code should be 0.)");
						}
					}
				}
			}
		}		
	}

	/**
	 * @return
	 * @throws Exception
	 */
	public String action() 
			throws Exception {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("action()");
		}
		
		action_(userMountList);
		action_(fstabUserUnmountedList);
				
		String result = populate();
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("action() returns " + result);
		}
		
		return result;
	}
}

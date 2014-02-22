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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import squeeze.web.util.CifsCredentials;
import squeeze.web.util.FsType;
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
	
	/* (non-Javadoc)
	 * @see com.opensymphony.xwork2.ActionSupport#validate()
	 */
	@Override
	public void validate() {
	
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("validate()");
		}
		
		validate_(userMountList);
		validate_(fstabUserMountList);
		
		if (hasActionErrors()) {
			try {
				populateMounts();
			} catch (Exception e) {}
		}
	}
	
	/**
	 * @param mountList
	 */
	private void validate_(List<StorageMount> mountList) {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("validate_(mountList=" + mountList + ")");
		}
		
		if (mountList != null) {
			Iterator<StorageMount> it = mountList.iterator();
			while (it.hasNext()) {
				StorageMount mount = it.next();
				
				String action = mount.getAction();
				if (StorageMount.ACTION_PERSIST.equals(action) || 
						StorageMount.ACTION_UPDATE.equals(action) ||
						StorageMount.ACTION_MOUNT.equals(action) ||
						StorageMount.ACTION_REMOUNT.equals(action)) {
					
					validateMount(mount, action);
				}
			}
		}		
	}

	/**
	 * @param mount
	 * @param action
	 */
	private void validateMount(StorageMount mount, String action) {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("validateMount(mount=" + mount + ", action=" + action + ")");
		}
		
		if (FsType.CIFS.equals(mount.getFsType())) {
			CifsCredentials credentials = mount.getCifsCredentials();
			if (credentials == null) {
				addActionError(action + ": " + mount.getSpec() + ": " + 
						getText("storage.validation.remoteFsUser.fail"));
			} else {
				String username = credentials.getUsername();
				if (username == null || username.trim().length() < 1) {
					addActionError(action + ": " + mount.getSpec() + ": " + 
							getText("storage.validation.remoteFsUser.fail"));
				}
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see squeeze.web.StorageAction#execute()
	 */
	public String execute() 
			throws Exception {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("execute()");
		}
		
		execute_(userMountList);
		execute_(fstabUserMountList);
				
		String result = populate();
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("execute() returns " + result);
		}
		
		return result;
	}

	/**
	 * @param mountList
	 * @throws InterruptedException
	 * @throws IOException
	 */
	private void execute_(List<StorageMount> mountList) 
			throws FileNotFoundException, InterruptedException, IOException {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("execute_(mountList=" + mountList + ")");
		}
		
		if (mountList != null) {
			Iterator<StorageMount> it = mountList.iterator();
			while (it.hasNext()) {
				StorageMount mount = it.next();
				if (StorageMount.ACTION_DELETE.equals(mount.getAction())) {
					
					if (LOG.isDebugEnabled()) {
						LOG.debug("Delete: " + mount);
					}
					
					persist(mount, true);
				} else if (StorageMount.ACTION_PERSIST.equals(mount.getAction()) || 
						StorageMount.ACTION_UPDATE.equals(mount.getAction())) {
					
					if (LOG.isDebugEnabled()) {
						LOG.debug("Persist: " + mount);
					}
					
					/*
					 * Deal with cifs credentials
					 */
					if (FsType.CIFS.equals(mount.getFsType())) {
						mount.createOrUpdateCifsCredentials(null, null, null);
					}
					
					persist(mount, false);
				} else if (StorageMount.ACTION_MOUNT.equals(mount.getAction())) {
					
					if (LOG.isDebugEnabled()) {
						LOG.debug("Mount: " + mount);
					}
					
					// Make sure it isn't already mounted.
					String[] cmdLineArgs = Util.createUmountCommand(mount);
					int result = Util.umount(cmdLineArgs);

					if (FsType.CIFS.equals(mount.getFsType())) {
						mount.createOrUpdateCifsCredentials(null, null, null);
					}
					
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
					
					if (FsType.CIFS.equals(mount.getFsType())) {
						mount.createOrUpdateCifsCredentials(null, null, null);
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
					}

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

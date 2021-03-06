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
 */
package squeeze.web;

import org.apache.log4j.Logger;

import squeeze.web.util.CifsCredentials;
import squeeze.web.util.FsType;
import squeeze.web.util.StorageMount;
import squeeze.web.util.Util;

/**
 * @author Clive Messer <clive.m.messer@gmail.com>
 *
 */
public class StorageMountRemoteAction extends StorageAction {

	private static final long serialVersionUID = 7836226420868215254L;
	
	private final static Logger LOGGER = Logger.getLogger(StorageMountRemoteAction.class);

	/**
	 * 
	 */
	public StorageMountRemoteAction() {
		
		super();
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("StorageMountRemoteAction()");
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
		
		if (remoteFsPartition == null || remoteFsPartition.trim().length() < 1) {
			addActionError(getText("storage.validation.remoteSpec.fail"));
		}

		if (remoteFsMountPoint == null || remoteFsMountPoint.trim().length() < 1) {
			addActionError(getText("storage.validation.file.fail"));
		}

		if (remoteFsType == null || remoteFsType.trim().length() < 1) {
			addActionError(getText("storage.validation.vfsType.fail"));
		}
		
		if (FsType.CIFS.equals(remoteFsType) && 
				(remoteFsUser == null || remoteFsUser.trim().length() < 1)) {
			addActionError(getText("storage.validation.remoteFsUser.fail"));
		}
		
		if (hasActionErrors()) {
			try {
				populateMounts();
			} catch (Exception e) {}
		}
	}
	
	/* (non-Javadoc)
	 * @see squeeze.web.StorageAction#execute()
	 */
	@Override
	public String execute() throws Exception {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("execute()");
		}
		
		String result = mountRemoteFs();
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("execute() returns " + result);
		}
		
		return result;
	}

	/**
	 * @return
	 * @throws Exception
	 */
	public String mountRemoteFs() throws Exception {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("mountRemoteFs()");
		}

		StorageMount mount = null;
		int mountResult = -1;
		
		if (remoteFsPartition != null && remoteFsPartition.trim().length() > 0 && 
				remoteFsMountPoint != null && remoteFsMountPoint.trim().length() > 0 &&
				remoteFsType != null && remoteFsType.trim().length() > 0) { 
		
			remoteFsPartition = remoteFsPartition.trim();
			remoteFsMountPoint = remoteFsMountPoint.trim();
			remoteFsType = remoteFsType.trim();

			if (remoteFsMountOptions != null || remoteFsMountOptions.trim().length() > 0) {
				remoteFsMountOptions = remoteFsMountOptions.trim();
			} else {
				remoteFsMountOptions = REMOTE_FS_DEFAULT_MOUNT_OPTIONS;
			}
			
			String mountOptions = remoteFsMountOptions;
			
			mount = new StorageMount(remoteFsPartition, remoteFsMountPoint, remoteFsType, 
					mountOptions, false, null);

			/*
			 * Make sure if we are mounting a cifs share, that we use credentials.
			 */
			if (FsType.CIFS.equals(remoteFsType)) {

				/*
				 * CIFS USERNAME
				 */
				String username = null;
				if (remoteFsUser != null && remoteFsUser.trim().length() > 0) {
					username = remoteFsUser.trim();
				} else {
					username = CifsCredentials.GUEST;
				}	

				/*
				 * CIFS PASSWORD
				 */
				String password = null;
				if (remoteFsPassword != null && remoteFsPassword.trim().length() > 0) {
					password = remoteFsPassword.trim();
				} else {
					password = Util.BLANK_STRING;
				}

				/*
				 * CIFS DOMAIN
				 */
				String domain = null;
				if (remoteFsDomain != null && remoteFsDomain.trim().length() > 0) {
					domain = remoteFsDomain.trim();
				} else {
					domain = Util.BLANK_STRING;
				}
				
				mount.createOrUpdateCifsCredentials(username, password, domain);
			}
						
			mountResult = mount(mount, false);
		}
		
		String result = "populate";
		if(mountResult == 0) {
			// successful mount.
			if (remoteFsPersist) {
				// persist to fstab
				if (mount != null) {
					persist(mount, false);
				}
			}
			// clear the local and remote fs fields
			result = populate();
		} else {
			// failed mount
			// just re-populate the mount lists
			populateMounts();
		}
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("mountRemoteFs() returns " + result);
		}
		
		return result;
	}
}

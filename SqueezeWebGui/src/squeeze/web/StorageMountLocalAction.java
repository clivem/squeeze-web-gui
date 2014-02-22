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

import squeeze.web.util.FsType;
import squeeze.web.util.StorageMount;

/**
 * @author Clive Messer <clive.m.messer@gmail.com>
 *
 */
public class StorageMountLocalAction extends StorageAction {

	private static final long serialVersionUID = 4840496090298371665L;

	private final static Logger LOGGER = Logger.getLogger(StorageMountLocalAction.class);
	
	/**
	 * 
	 */
	public StorageMountLocalAction() {
		
		super();
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("StorageMountLocalAction()");
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
		
		if (localFsPartition == null || localFsPartition.trim().length() < 1) {
			addActionError(getText("storage.validation.spec.fail"));
		}

		if (localFsMountPoint == null || localFsMountPoint.trim().length() < 1) {
			addActionError(getText("storage.validation.file.fail"));
		}

		if (localFsType == null || localFsType.trim().length() < 1) {
			addActionError(getText("storage.validation.vfsType.fail"));
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
		
		String result = mountLocalFs();
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("execute() returns " + result);
		}
		
		return result;
	}

	/**
	 * @return
	 * @throws Exception
	 */
	public String mountLocalFs() throws Exception {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("mountLocalFs()");
		}

		StorageMount mount = null;
		int mountResult = -1;
		
		if (localFsPartition != null && localFsPartition.trim().length() > 0 && 
				localFsMountPoint != null && localFsMountPoint.trim().length() > 0 &&
				localFsType != null && localFsType.trim().length() > 0) { 
		
			localFsPartition = localFsPartition.trim();
			localFsMountPoint = localFsMountPoint.trim();
			
			// map fat -> vfat, ntfs -> ntfs-3g
			localFsType = localFsType.trim();
			if (FsType.FAT.equals(localFsType)) {
				localFsType = FsType.VFAT;
			} else if (FsType.NTFS.equals(localFsType)) {
				localFsType = FsType.NTFS3G;
			}

			if (localFsMountOptions != null || localFsMountOptions.trim().length() > 0) {
				localFsMountOptions = localFsMountOptions.trim();
			} else {
				localFsMountOptions = LOCAL_FS_DEFAULT_MOUNT_OPTIONS;
			}
						
			mount = new StorageMount(localFsPartition, localFsMountPoint, localFsType, localFsMountOptions, false, null);
			mountResult = mountFs(mount);
		}

		String result = "populate";
		if(mountResult == 0) {
			// successful mount. 
			if (localFsPersist) {
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
			LOGGER.debug("mountLocalFs() returns " + result);
		}
		
		return result;
	}
}

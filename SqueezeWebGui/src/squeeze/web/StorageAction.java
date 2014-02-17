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
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import squeeze.web.util.FsType;
import squeeze.web.util.StorageMount;
import squeeze.web.util.Util;

import com.opensymphony.xwork2.ActionSupport;

/**
 * @author Clive Messer <clive.m.messer@gmail.com>
 *
 */
public class StorageAction extends ActionSupport {

	private final static Logger LOGGER = Logger.getLogger(StorageAction.class);
	
	private static final long serialVersionUID = 6170755172059220738L;

	protected List<StorageMount> mountList = null;
	protected List<StorageMount> userMountList = null;
	
	protected final static List<String> MOUNT_POINTS;
	protected final static List<String> LOCAL_FS_TYPES;
	protected final static List<String> REMOTE_FS_TYPES;
	
	public final static String FS_STATUS_REGEX = "^.*(type\\s(" + FsType.FAT + "|" + FsType.VFAT + "|" + FsType.NTFS + 
			"|" + FsType.NTFS3G + "|" + FsType.EXT2 + "|" + FsType.EXT3 + "|" + FsType.EXT4 + "|" + FsType.CIFS + 
			"|" + FsType.NFS + "|" + FsType.NFS4 + ")\\s){1}.*$";
	
	protected final static List<String> STORAGE_MOUNT_ACTION_LIST = StorageMount.generateActionList();
	
	protected List<String> localFsPartitionList = null;
	
	protected String localFsPartition = null;
	protected String localFsMountPoint = null;
	protected String localFsType = null;
	protected String localFsMountOptions = null;
	
	protected String remoteFsPartition = null;
	protected String remoteFsMountPoint = null;
	protected String remoteFsType = null;
	protected String remoteFsMountOptions = null;
	
	protected final static String LOCAL_FS_DEFAULT_MOUNT_OPTIONS = "defaults";
	protected final static String REMOTE_FS_DEFAULT_MOUNT_OPTIONS = "defaults,_netdev";
	
	static {
		MOUNT_POINTS = new ArrayList<String>();
		MOUNT_POINTS.add("/storage");
		
		LOCAL_FS_TYPES = new ArrayList<String>();
		LOCAL_FS_TYPES.add(Util.BLANK_STRING);
		LOCAL_FS_TYPES.add(FsType.AUTO);
		LOCAL_FS_TYPES.add(FsType.FAT);
		LOCAL_FS_TYPES.add(FsType.NTFS);
		LOCAL_FS_TYPES.add(FsType.EXT2);
		LOCAL_FS_TYPES.add(FsType.EXT3);
		LOCAL_FS_TYPES.add(FsType.EXT4);

		REMOTE_FS_TYPES = new ArrayList<String>();
		REMOTE_FS_TYPES.add(Util.BLANK_STRING);
		REMOTE_FS_TYPES.add(FsType.CIFS);
		REMOTE_FS_TYPES.add(FsType.NFS);
		REMOTE_FS_TYPES.add(FsType.NFS4);
	}
	
	/**
	 * 
	 */
	public StorageAction() {
		
		super();
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("StorageAction()");
		}
	}

	/* (non-Javadoc)
	 * @see com.opensymphony.xwork2.ActionSupport#execute()
	 */
	@Override
	public String execute() throws Exception {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("execute()");
		}

		String result = populate();
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("execute() returns " + result);
		}
		
		return result;
	}
	
	/**
	 * @param partition
	 * @param mountPoint
	 * @param type
	 * @param options
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	protected int mountFs(String partition, String mountPoint, String type, String options) 
			throws IOException, InterruptedException {
		
		// Make sure it isn't already mounted
		int result = Util.umount(mountPoint);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Util.umount(" + mountPoint + "): returns " + result);
		}

		//if (result != 0) {
		//	addActionError("Umount '" + mountPoint + "' returned: " + result);
		//}
		
		// Try to mount it
		StorageMount mount = new StorageMount(partition, mountPoint, type, options);
		String[] cmdLineArgs = Util.createMountCommand(mount);
		result = Util.mount(cmdLineArgs);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Util.mount(" + Util.arrayToString(cmdLineArgs) + "): returns " + result);
		}

		if (result != 0) {
			addActionError("Mount '" + Util.arrayToString(cmdLineArgs) + "' returned: " + result +
					". (If successful, return code should be 0.)");
		}

		return result;
	}

	/**
	 * @return
	 * @throws Exception
	 */
	public String populate() throws Exception {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("populate()");
		}

		localFsPartition = null;
		localFsMountPoint = null;
		localFsType = null;
		localFsMountOptions = LOCAL_FS_DEFAULT_MOUNT_OPTIONS;
		
		remoteFsPartition = null;
		remoteFsMountPoint = null;
		remoteFsType = null;
		remoteFsMountOptions = REMOTE_FS_DEFAULT_MOUNT_OPTIONS;
		
		populateMounts();
		
		String result = "populate";
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("populate() returns " + result);
		}
		
		return result;
	}
	
	/**
	 * @throws IOException
	 * @throws InterruptedException
	 */
	protected void populateMounts() 
			throws IOException, InterruptedException {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("populateMounts()");
		}
		
		mountList = new ArrayList<StorageMount>();
		userMountList = new ArrayList<StorageMount>();
		localFsPartitionList = Util.getPartitions();

		List<String> list = Util.getMountList(FS_STATUS_REGEX);
		for (int i = 0; i < list.size(); i++) {
			String mountSpec = list.get(i);
			StorageMount mount = StorageMount.createStorageMount(mountSpec);
			if (mount != null) {
				boolean userMount = false;
				String[] mountPoints = MOUNT_POINTS.toArray(new String[0]);
				for (int j = 0; j < mountPoints.length; j++) {
					if (mountPoints[j].equals(mount.getMountPoint())) {
						// add to the user controllable list
						userMountList.add(mount);
						userMount = true;
						break;
					}					
				}
				
				if (!userMount) {
					// add to the mount list
					mountList.add(mount);
				}
			}
			
		}
	}

	/**
	 * @return the userMountList
	 */
	public List<StorageMount> getUserMountList() {
		return userMountList;
	}

	/**
	 * @param userMountList the userMountList to set
	 */
	public void setUserMountList(List<StorageMount> userMountList) {
		this.userMountList = userMountList;
	}

	/**
	 * @return
	 */
	public List<String> getMountActionList() {
		return STORAGE_MOUNT_ACTION_LIST;
	}
	
	/**
	 * @return
	 */
	public List<String> getLocalFsTypes() {
		return LOCAL_FS_TYPES;
	}

	/**
	 * @return
	 */
	public List<String> getRemoteFsTypes() {
		return REMOTE_FS_TYPES;
	}

	/**
	 * @return
	 */
	public List<String> getMountPoints() {
		return MOUNT_POINTS;
	}
	
	/**
	 * @return
	 */
	public List<String> getAvailablePartitions() {
		return new ArrayList<String>();
	}
	
	/**
	 * @return the localFsPartition
	 */
	public String getLocalFsPartition() {
		return localFsPartition;
	}

	/**
	 * @param localFsPartition the localFsPartition to set
	 */
	public void setLocalFsPartition(String localFsPartition) {
		this.localFsPartition = localFsPartition;
	}

	/**
	 * @return the localFsMountPoint
	 */
	public String getLocalFsMountPoint() {
		return localFsMountPoint;
	}

	/**
	 * @param localFsMountPoint the localFsMountPoint to set
	 */
	public void setLocalFsMountPoint(String localFsMountPoint) {
		this.localFsMountPoint = localFsMountPoint;
	}

	/**
	 * @return the localFsType
	 */
	public String getLocalFsType() {
		return localFsType;
	}

	/**
	 * @param localFsType the localFsType to set
	 */
	public void setLocalFsType(String localFsType) {
		this.localFsType = localFsType;
	}

	/**
	 * @return the remoteFsPartition
	 */
	public String getRemoteFsPartition() {
		return remoteFsPartition;
	}

	/**
	 * @param remoteFsPartition the remoteFsPartition to set
	 */
	public void setRemoteFsPartition(String remoteFsPartition) {
		this.remoteFsPartition = remoteFsPartition;
	}

	/**
	 * @return the remoteFsMountPoint
	 */
	public String getRemoteFsMountPoint() {
		return remoteFsMountPoint;
	}

	/**
	 * @param remoteFsMountPoint the remoteFsMountPoint to set
	 */
	public void setRemoteFsMountPoint(String remoteFsMountPoint) {
		this.remoteFsMountPoint = remoteFsMountPoint;
	}

	/**
	 * @return the remoteFsType
	 */
	public String getRemoteFsType() {
		return remoteFsType;
	}

	/**
	 * @param remoteFsType the remoteFsType to set
	 */
	public void setRemoteFsType(String remoteFsType) {
		this.remoteFsType = remoteFsType;
	}

	/**
	 * @return the localFsMountOptions
	 */
	public String getLocalFsMountOptions() {
		return localFsMountOptions;
	}

	/**
	 * @param localFsMountOptions the localFsMountOptions to set
	 */
	public void setLocalFsMountOptions(String localFsMountOptions) {
		this.localFsMountOptions = localFsMountOptions;
	}

	/**
	 * @return the remoteFsMountOptions
	 */
	public String getRemoteFsMountOptions() {
		return remoteFsMountOptions;
	}

	/**
	 * @param remoteFsMountOptions the remoteFsMountOptions to set
	 */
	public void setRemoteFsMountOptions(String remoteFsMountOptions) {
		this.remoteFsMountOptions = remoteFsMountOptions;
	}

	/**
	 * @return the mountList
	 */
	public List<StorageMount> getMountList() {
		return mountList;
	}

	/**
	 * @return the localFsPartitionList
	 */
	public List<String> getLocalFsPartitionList() {
		return localFsPartitionList;
	}

	/**
	 * @param localFsPartitionList the localFsPartitionList to set
	 */
	public void setLocalFsPartitionList(List<String> localFsPartitionList) {
		this.localFsPartitionList = localFsPartitionList;
	}
}

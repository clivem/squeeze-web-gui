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
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

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

	protected String fsStatus = null;
	
	protected List<StorageMount> mountList = null;
	
	protected final static List<String> MOUNT_POINTS;
	protected final static List<String> LOCAL_FS_TYPES;
	protected final static List<String> REMOTE_FS_TYPES;
	
	//protected final static String[] MOUNT_POINTS = {"/storage"};
	//protected final static String[] LOCAL_FS_TYPES = {"", "fat", "ntfs", "ext2", "ext3", "ext4"};
	//protected final static String[] REMOTE_FS_TYPES = {"", "cifs", "nfs", "nfs4"};

	public final static String FS_STATUS_REGEX = "^.*(type fat|ntfs|ntfs-3g|ext2|ext3|ext4|cifs|nfs|nfs4 on){1}.*$";
	
	protected final static List<String> STORAGE_MOUNT_ACTION_LIST = StorageMount.generateActionList();
	
	private String localFsPartition = null;
	private String localFsMountPoint = null;
	private String localFsType = null;
	private String localFsMountOptions = null;
	
	private String remoteFsPartition = null;
	private String remoteFsMountPoint = null;
	private String remoteFsType = null;
	private String remoteFsMountOptions = null;
	
	static {
		MOUNT_POINTS = new ArrayList<String>();
		MOUNT_POINTS.add("/storage");
		
		LOCAL_FS_TYPES = new ArrayList<String>();
		LOCAL_FS_TYPES.add("");
		LOCAL_FS_TYPES.add("fat");
		LOCAL_FS_TYPES.add("ntfs");
		LOCAL_FS_TYPES.add("ext2");
		LOCAL_FS_TYPES.add("ext3");
		LOCAL_FS_TYPES.add("ext4");

		REMOTE_FS_TYPES = new ArrayList<String>();
		REMOTE_FS_TYPES.add("");
		REMOTE_FS_TYPES.add("cifs");
		REMOTE_FS_TYPES.add("nfs");
		REMOTE_FS_TYPES.add("nfs4");
	}
	
	/**
	 * 
	 */
	public StorageAction() {
		
		super();
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("FstabAction()");
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
	 * @return
	 * @throws Exception
	 */
	public String mountLocalFs() throws Exception {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("mountLocalFs()");
		}

		String result = populate();
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("mountLocalFs() returns " + result);
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

		String result = populate();
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("mountRemoteFs() returns " + result);
		}
		
		return result;
	}

	/**
	 * @return
	 * @throws Exception
	 */
	public String unmount() throws Exception {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("unmount()");
		}
		
		if (mountList != null) {
			Iterator<StorageMount> it = mountList.iterator();
			while (it.hasNext()) {
				StorageMount mount = it.next();
				if (StorageMount.ACTION_UNMOUNT.equals(mount.getAction())) {
					LOG.warn("Unmount: " + mount);
				} else if (StorageMount.ACTION_REMOUNT.equals(mount.getAction())) {
					LOG.warn("Remount: " + mount);
				}
			}
		}
				
		String result = populate();
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("unmount() returns " + result);
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
		
		populateMounts();
		
		localFsMountOptions = "defaults";
		remoteFsMountOptions = "defaults,_netdev";
		
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
		
		fsStatus = "";
		mountList = new ArrayList<StorageMount>();

		String[] list = Util.getMountList(FS_STATUS_REGEX);
		for (int i = 0; i < list.length; i++) {
			// add entry to the status window
			fsStatus += list[i];
			if (i + 1 < list.length) {
				fsStatus += Util.LINE_SEP;
			}
			// add to the user controllable list
			StorageMount mount = StorageMount.createStorageMount(list[i]);
			String[] mountPoints = MOUNT_POINTS.toArray(new String[0]);
			for (int j = 0; j < mountPoints.length; j++) {
				if (mountPoints[j].equals(mount.getMountPoint())) {
					mountList.add(mount);
					break;
				}
			}
			
		}
	}

	/**
	 * @return the fsStatus
	 */
	public String getFsStatus() {
		return fsStatus;
	}

	/**
	 * @param fsStatus the fsStatus to set
	 */
	public void setFsStatus(String fsStatus) {
		this.fsStatus = fsStatus;
	}

	/**
	 * @return the mountList
	 */
	public List<StorageMount> getMountList() {
		return mountList;
	}

	/**
	 * @param mountList the mountList to set
	 */
	public void setMountList(List<StorageMount> mountList) {
		this.mountList = mountList;
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
}

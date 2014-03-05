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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import squeeze.web.util.BlkId;
import squeeze.web.util.CifsCredentials;
import squeeze.web.util.Commands;
import squeeze.web.util.ExecuteProcess;
import squeeze.web.util.FsType;
import squeeze.web.util.StorageMount;
import squeeze.web.util.StorageMountComparator;
import squeeze.web.util.Util;
import squeeze.web.util.WebConfig;

import com.opensymphony.xwork2.ActionSupport;

/**
 * @author Clive Messer <clive.m.messer@gmail.com>
 *
 */
public class StorageAction extends ActionSupport {

	private final static Logger LOGGER = Logger.getLogger(StorageAction.class);
	
	private static final long serialVersionUID = 6170755172059220738L;

	protected final static List<String> MOUNT_POINTS;
	protected final static List<String> LOCAL_FS_TYPES;
	protected final static List<String> REMOTE_FS_TYPES;
	
	public final static String FS_STATUS_REGEX = "^.*(type\\s(" + 
			FsType.FAT + "|" + FsType.VFAT + "|" + 
			FsType.NTFS + "|" + FsType.NTFS3G + "|" + FsType.FUSEBLK + "|" +
			FsType.EXT2 + "|" + FsType.EXT3 + "|" + FsType.EXT4 + "|" + 
			FsType.CIFS + "|" + FsType.NFS + "|" + FsType.NFS4 + ")\\s){1}.*$";
		
	protected final static String LOCAL_FS_DEFAULT_MOUNT_OPTIONS = "defaults,nofail";
	protected final static String REMOTE_FS_DEFAULT_MOUNT_OPTIONS = "defaults,_netdev";
	
	protected final static String[] STORAGE_DIR_LIST = new String[] {
		// "/media", 
		// "/media/audio", 
		// "/media/playlists",
		"/movies",
		"/music",
		"/music/flac",
		"/music/m4a",
		"/music/mp3",
		"/music/playlist",
		"/pictures"
	};
	
	protected List<StorageMount> fstabUserMountList = null;
	protected List<StorageMount> systemMountList = null;
	protected List<StorageMount> userMountList = null;
	
	protected List<String> localFsPartitionList = null;
	
	protected String localFsPartition = null;
	protected String localFsMountPoint = null;
	protected String localFsType = null;
	protected String localFsMountOptions = null;
	protected boolean localFsPersist = false;
	
	protected String remoteFsPartition = null;
	protected String remoteFsMountPoint = null;
	protected String remoteFsType = null;
	protected String remoteFsMountOptions = null;
	protected boolean remoteFsPersist = false;
	
	protected String remoteFsUser = null;
	protected String remoteFsPassword = null;
	protected String remoteFsDomain = null;
	
	protected String storageDirectory = null;
	
	static {
		MOUNT_POINTS = new ArrayList<String>();
		String[] storageDirs = WebConfig.getStorageDirs();
		for (int i = 0; i < storageDirs.length; i++) {
			MOUNT_POINTS.add(storageDirs[i]);
		}
		
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
		localFsPersist = false;
		
		remoteFsPartition = null;
		remoteFsMountPoint = null;
		remoteFsType = null;
		remoteFsMountOptions = REMOTE_FS_DEFAULT_MOUNT_OPTIONS;
		remoteFsPersist = false;
		
		remoteFsUser = null;
		remoteFsPassword = null;
		remoteFsDomain = null;
		
		storageDirectory = null;
		
		populateMounts();
		
		String result = "populate";
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("populate() returns " + result);
		}
		
		return result;
	}
	
	/**
	 * @return
	 * @throws Exception
	 */
	public String createStorageLayout() throws Exception {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("createStorageLayout()");
		}

		String[] cmdLineArgs = new String[] {Commands.CMD_SUDO, Commands.SCRIPT_CREATE_STORAGE_LAYOUT, 
				storageDirectory};
		
		int cmdResult = ExecuteProcess.executeCommand(cmdLineArgs);
		if (cmdResult != 0) {
			addActionError("'" + Util.arrayToString(cmdLineArgs) + "' returned: " + cmdResult +
					". (If successful, return code should be 0.)");
		}

		String result = "populate";

		populateMounts();
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("createStorageLayout() returns " + result);
		}
		
		return result;
	}
	
	/**
	 * @param mount
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 *
	protected int mountFs(StorageMount mount) 
			throws IOException, InterruptedException {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("mountFs(mount=" + mount + ")");
		}
		
		// Make sure it isn't already mounted
		String[] cmdLineArgs = Util.createUmountCommand(mount);
		int result = Util.umount(cmdLineArgs);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Util.umount(" + Util.arrayToString(cmdLineArgs) + "): returns " + result);
		}

		//if (result != 0) {
		//	addActionError("Umount '" + mountPoint + "' returned: " + result);
		//}
		
		// Try to mount it
		cmdLineArgs = Util.createMountCommand(mount);
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
	*/
	
	/**
	 * @param mount
	 * @param createOrUpdateCifsCredentials
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	protected int mount(StorageMount mount, boolean createOrUpdateCifsCredentials) 
			throws IOException, InterruptedException {
		
		if (LOG.isDebugEnabled()) {
			LOG.debug("mount(mount=" + mount + ", createOrUpdateCifsCredentials=" + createOrUpdateCifsCredentials + ")");
		}
		
		// Make sure it isn't already mounted.
		String[] cmdLineArgs = Util.createUmountCommand(mount);
		int result = Util.umount(cmdLineArgs);

		if (createOrUpdateCifsCredentials) {
			mount.createOrUpdateCifsCredentials(null, null, null);
		}
		
		// Mount it.
		cmdLineArgs = Util.createMountCommand(mount);
		result = Util.mount(cmdLineArgs);
		if (result != 0) {
			addActionError("Mount '" + Util.arrayToString(cmdLineArgs) + "' returned: " + result +
					". (If successful, return code should be 0.)");
		}		
		
		return result;
	}
	
	/**
	 * @param mount
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	protected int umount(StorageMount mount) 
			throws IOException, InterruptedException {
		
		if (LOG.isDebugEnabled()) {
			LOG.debug("umount(mount=" + mount + ")");
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
		
		return result;
	}
	
	/**
	 * @param mount
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	protected int remount(StorageMount mount) throws IOException, InterruptedException {
		
		if (LOG.isDebugEnabled()) {
			LOG.warn("Remount: " + mount);
		}

		/*
		 * ntfs-3g doesn't support remount.
		 */
		if (FsType.CIFS.equals(mount.getFsType())) {
			return mount(mount, true);
		}
		
		String[] cmdLineArgs = Util.createReMountCommand(mount);
		int result = Util.remount(cmdLineArgs);
		if (result != 0) {
			addActionError("Remount '" + Util.arrayToString(cmdLineArgs) + "' returned: " + result +
					". (If successful, return code should be 0.)");
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

		String[] mountPoints = MOUNT_POINTS.toArray(new String[0]);
		
		List<StorageMount> fstabList = StorageMount.parseFstab();

		fstabUserMountList = new ArrayList<StorageMount>();
		Iterator<StorageMount> it = fstabList.iterator();
		while (it.hasNext()) {
			StorageMount mount = it.next();
			for (int j = 0; j < mountPoints.length; j++) {
				if (mountPoints[j].equals(mount.getMountPoint())) {
					// add to the user controllable list
					fstabUserMountList.add(mount);
					break;
				}		
			}
		}
		
		systemMountList = new ArrayList<StorageMount>();
		userMountList = new ArrayList<StorageMount>();
		localFsPartitionList = Util.getPartitions();

		List<String> mountList = Util.getMountList(FS_STATUS_REGEX);
		for (int i = 0; i < mountList.size(); i++) {
			StorageMount mount = StorageMount.createStorageMount(mountList.get(i));
			if (mount != null) {
				// BUG: Fedora 19 mount appends '/' to spec for nfs mounts.
				if (mount.getFsType().startsWith("nfs")) {
					String spec = mount.getSpec();
					if (spec.endsWith("/")) {
						mount.setSpec(spec.substring(0, spec.length() - 1));
					}
				}

				boolean userMount = false;
				for (int j = 0; j < mountPoints.length; j++) {
					if (mountPoints[j].equals(mount.getMountPoint())) {
						// add to the user controllable list
						userMountList.add(mount);
						// Make sure it isn't in the unmounted list.
						it = fstabUserMountList.iterator();
						while (it.hasNext()) {
							StorageMount fstabMount = it.next();
							if (mount.getMountPoint().equals(fstabMount.getMountPoint())) {
								if (mount.getSpec().equals(fstabMount.getSpec())) {
									processMount(it, mount, fstabMount);
								} else if (fstabMount.getSpec().startsWith(BlkId.LABEL) || 
										fstabMount.getSpec().startsWith(BlkId.UUID)) {
									BlkId blkid = BlkId.getBlkId(mount.getSpec());
									if (blkid != null) {
										if (blkid.getDevice().equals(mount.getSpec())) {
											processMount(it, mount, fstabMount);
											mount.setSpec(fstabMount.getSpec());
										}
									}
								}
							}
						}
						
						if (!mount.isFstabEntry() && FsType.CIFS.equals(mount.getFsType())) {
							mount.createOrUpdateCifsCredentials();
						}
						
						// don't add it to the system mount list
						userMount = true;
						break;
					}					
				}
				
				if (!userMount) {
					// add to the system mount list
					systemMountList.add(mount);
				}
			}	
		}
		
		synchronized (StorageMountComparator.COMPARATOR) {
			Collections.sort(userMountList, StorageMountComparator.COMPARATOR);			
			Collections.sort(systemMountList, StorageMountComparator.COMPARATOR);			
			Collections.sort(fstabUserMountList, StorageMountComparator.COMPARATOR);			
		}
	}
	
	/**
	 * @param it
	 * @param mount
	 * @param fstabMount
	 */
	private void processMount(Iterator<StorageMount> it, StorageMount mount, StorageMount fstabMount) {
		
		// remove it from what will be the un-mounted list
		it.remove();
		// mark the mounted entry as being in fstab
		mount.setFstabEntry(true);
		mount.setLineNo(fstabMount.getLineNo());
		mount.setOptions(fstabMount.getOptions());
		if (FsType.CIFS.equals(mount.getFsType())) {
			mount.setCifsCredentials(fstabMount.getCifsCredentials());									
		} else if (FsType.FUSEBLK.equals(mount.getFsType()) && 
				FsType.NTFS3G.equals(fstabMount.getFsType())) {
			mount.setFsType(fstabMount.getFsType());
		}		
	}
	
	/**
	 * @param mount
	 * @throws Exception
	 */
	protected void persist(StorageMount mount, boolean delete) 
			throws FileNotFoundException, InterruptedException, IOException {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("persist(mount=" + mount + ", delete=" + delete + ")");
		}
		
		HashMap<Integer, StorageMount> map = new HashMap<Integer, StorageMount>();
		
		List<String> fstabRawList = Util.getFstab();
		List<StorageMount> fstabMountList = StorageMount.parseFstab(fstabRawList);
		Iterator<StorageMount> it = fstabMountList.iterator();
		while (it.hasNext()) {
			StorageMount entry = it.next();
			if (entry.getMountPoint().equals(mount.getMountPoint())) {
				map.put(new Integer(entry.getLineNo()), entry);
			}
		}
		
		String date = Util.getModifiedFullDate();

		List<String> fstabWriteList = null;
		if (map.isEmpty()) {
			fstabWriteList = fstabRawList;
		} else {
			fstabWriteList = new ArrayList<String>();
			for (int i = 0; i < fstabRawList.size(); i++) {
				if (map.containsKey(new Integer(i + 1))) {
					// Comment existing entry for mount point.
					fstabWriteList.add("# START: Commented by squeeze-web-gui (Java) on " + date);
					fstabWriteList.add("# " + fstabRawList.get(i));
					fstabWriteList.add("# END: Commented by squeeze-web-gui (Java) on " + date);
				} else {
					fstabWriteList.add(fstabRawList.get(i));
				}
			}
		}
		
		if (!delete) {
			if (FsType.CIFS.equals(mount.getFsType())) {
				CifsCredentials credentials = mount.getCifsCredentials();
				if (credentials != null) {
					credentials.save();
				}
			}
			// Add new entry for mount point.
			fstabWriteList.add("# START: Addition by squeeze-web-gui (Java) on " + date);
			fstabWriteList.add(mount.getSpec() + "\t" + mount.getMountPoint() + "\t" + mount.getFsType() +
					"\t" + mount.getOptions() + "\t0" + "\t0");
			fstabWriteList.add("# END: Addition by squeeze-web-gui (Java) on " + date);
		}
		
		File file = null;
		try {
			file = writeTempFstab(fstabWriteList);
			Util.replaceConfig(file, Commands.SCRIPT_FSTAB_UPDATE);
		} catch (Exception e) {
			LOGGER.error("Caught exception saving fstab!", e);
			throw e;
		} finally {
			if (file != null) {
				try {
					file.delete();
				} catch (Exception e) {}
			}
		}		
	}
	
	/**
	 * @param list
	 * @return
	 * @throws IOException
	 */
	private File writeTempFstab(List<String> list) 
			throws IOException {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("writeTempFstab(list=" + list + ")");
		}

		BufferedWriter bw = null;
		try {
			File tempFile = Util.createTempFile("fstab_", ".txt");
			bw = new BufferedWriter(new FileWriter(tempFile));
			Iterator<String> it = list.iterator();
			while (it.hasNext()) {
				bw.write(it.next() + Util.LINE_SEP);
			}
			return tempFile;
		} finally {
			if (bw != null) {
				try {
					bw.flush();
				} catch (Exception e) {}
				
				try {
					bw.close();
				} catch (Exception e) {}
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
	 * @return the systemMountList
	 */
	public List<StorageMount> getSystemMountList() {
		
		return systemMountList;
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
	
	/**
	 * @return
	 */
	public String[] getStorageLayoutList() {
		
		return STORAGE_DIR_LIST;
	}
	
	/**
	 * @return the storageDirectory
	 */
	public String getStorageDirectory() {
		
		return storageDirectory;
	}
	
	/**
	 * @param storageDirectory the storageDirectory to set
	 */
	public void setStorageDirectory(String storageDirectory) {
		
		this.storageDirectory = storageDirectory;
	}

	/**
	 * @return the remoteFsUser
	 */
	public String getRemoteFsUser() {
		
		return remoteFsUser;
	}

	/**
	 * @param remoteFsUser the remoteFsUser to set
	 */
	public void setRemoteFsUser(String remoteFsUser) {
		
		this.remoteFsUser = remoteFsUser;
	}

	/**
	 * @return the remoteFsPassword
	 */
	public String getRemoteFsPassword() {
		
		return remoteFsPassword;
	}

	/**
	 * @param remoteFsPassword the remoteFsPassword to set
	 */
	public void setRemoteFsPassword(String remoteFsPassword) {
		
		this.remoteFsPassword = remoteFsPassword;
	}

	/**
	 * @return the remoteFsDomain
	 */
	public String getRemoteFsDomain() {
		
		return remoteFsDomain;
	}

	/**
	 * @param remoteFsDomain the remoteFsDomain to set
	 */
	public void setRemoteFsDomain(String remoteFsDomain) {
		
		this.remoteFsDomain = remoteFsDomain;
	}

	/**
	 * @return the localFsPersist
	 */
	public boolean isLocalFsPersist() {
		
		return localFsPersist;
	}

	/**
	 * @param localFsPersist the localFsPersist to set
	 */
	public void setLocalFsPersist(boolean localFsPersist) {
		
		this.localFsPersist = localFsPersist;
	}

	/**
	 * @return the remoteFsPersist
	 */
	public boolean isRemoteFsPersist() {
		
		return remoteFsPersist;
	}

	/**
	 * @param remoteFsPersist the remoteFsPersist to set
	 */
	public void setRemoteFsPersist(boolean remoteFsPersist) {
		
		this.remoteFsPersist = remoteFsPersist;
	}

	/**
	 * @return the fstabUserMountList
	 */
	public List<StorageMount> getFstabUserMountList() {
		
		return fstabUserMountList;
	}

	/**
	 * @param fstabUserMountList the fstabUserMountList to set
	 */
	public void setFstabUserMountList(List<StorageMount> fstabUserMountList) {
		
		this.fstabUserMountList = fstabUserMountList;
	}
}

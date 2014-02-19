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
package squeeze.web.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

/**
 * @author Clive Messer <clive.m.messer@gmail.com>
 *
 */
public class StorageMount {
	
	private final static Logger LOGGER = Logger.getLogger(StorageMount.class);

	// output from mount: spec on /mountPoint type fsType (options,options,options)
	private final static Pattern MOUNT_LIST_PATTERN = 
			Pattern.compile("([^ ]+) on ([^ ]+) type ([^ ]+) \\((.*)\\)$");

	// parse /etc/fstab
	private final static Pattern FSTAB_PATTERN = 
			Pattern.compile("^([^\\s]+)\\s+([^\\s]+)\\s+([^\\s]+)\\s+([^\\s]+)\\s+([^\\s]+)\\s+([^\\s]+).*$");
	
	// ACTIONS
	public final static String ACTION_MOUNT = "mount";
	public final static String ACTION_UNMOUNT = "unmount";
	public final static String ACTION_REMOUNT = "remount";
	
	private String spec = null;
	private String mountPoint = null;
	private String fsType = null;
	private String options = null;
	private int freq = 0;
	private int passNo = 0;

	private int lineNo = -1;

	private String action = null;
	
	private boolean persist = false;

	private boolean fstabEntry = false;
	
	private boolean mounted = false;
	
	/**
	 * 
	 */
	public StorageMount() {

		super();
	}

	/**
	 * 
	 */
	public StorageMount(String spec, String mountPoint, String fsType, String options, boolean mounted) {
		
		this(spec, mountPoint, fsType, options, 0, 0, -1, null, false, false, mounted);
	}

	/**
	 * @param spec
	 * @param mountPoint
	 * @param fsType
	 * @param options
	 * @param freq
	 * @param passNo
	 * @param lineNo
	 * @param action
	 * @param persist
	 * @param fstabEntry
	 * @param mounted
	 */
	public StorageMount(String spec, String mountPoint, String fsType,
			String options, int freq, int passNo, int lineNo, String action,
			boolean persist, boolean fstabEntry, boolean mounted) {
		
		super();
		
		this.spec = spec;
		this.mountPoint = mountPoint;
		this.fsType = fsType;
		this.options = options;
		this.freq = freq;
		this.passNo = passNo;
		this.lineNo = lineNo;
		this.action = action;
		this.persist = persist;
		this.fstabEntry = fstabEntry;
		this.mounted = mounted;
	}

	/**
	 * @return the spec
	 */
	public String getSpec() {
		return spec;
	}

	/**
	 * @return the mountPoint
	 */
	public String getMountPoint() {
		return mountPoint;
	}

	/**
	 * @return the fsType
	 */
	public String getFsType() {
		return fsType;
	}

	/**
	 * @return the options
	 */
	public String getOptions() {
		return options;
	}
	
	/**
	 * @param spec the spec to set
	 */
	public void setSpec(String spec) {
		this.spec = spec;
	}

	/**
	 * @param mountPoint the mountPoint to set
	 */
	public void setMountPoint(String mountPoint) {
		this.mountPoint = mountPoint;
	}

	/**
	 * @param fsType the fsType to set
	 */
	public void setFsType(String fsType) {
		this.fsType = fsType;
	}

	/**
	 * @param options the options to set
	 */
	public void setOptions(String options) {
		this.options = options;
	}

	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * @return the persist
	 */
	public boolean isPersist() {
		return persist;
	}

	/**
	 * @param persist the persist to set
	 */
	public void setPersist(boolean persist) {
		this.persist = persist;
	}

	/**
	 * @return the freq
	 */
	public int getFreq() {
		return freq;
	}

	/**
	 * @param freq the freq to set
	 */
	public void setFreq(int freq) {
		this.freq = freq;
	}

	/**
	 * @return the passNo
	 */
	public int getPassNo() {
		return passNo;
	}

	/**
	 * @param passNo the passNo to set
	 */
	public void setPassNo(int passNo) {
		this.passNo = passNo;
	}

	/**
	 * @return the lineNo
	 */
	public int getLineNo() {
		return lineNo;
	}

	/**
	 * @param lineNo the lineNo to set
	 */
	public void setLineNo(int lineNo) {
		this.lineNo = lineNo;
	}

	/**
	 * @return the fstabEntry
	 */
	public boolean isFstabEntry() {
		return fstabEntry;
	}

	/**
	 * @param fstabEntry the fstabEntry to set
	 */
	public void setFstabEntry(boolean fstabEntry) {
		this.fstabEntry = fstabEntry;
	}

	/**
	 * @return the mounted
	 */
	public boolean isMounted() {
		return mounted;
	}

	/**
	 * @param mounted the mounted to set
	 */
	public void setMounted(boolean mounted) {
		this.mounted = mounted;
	}

	/**
	 * @param lineOfMountOutput
	 * @return
	 */
	public final static StorageMount createStorageMount(String lineOfMountOutput) {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("createStorageMount(lineOfMountOutput=" + lineOfMountOutput + ")");
		}

		Matcher matcher = null;
		synchronized (MOUNT_LIST_PATTERN) {
			matcher = MOUNT_LIST_PATTERN.matcher(lineOfMountOutput);
		}
		
		if (matcher.matches()) {
			return new StorageMount(matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4), true);
		} else {
			LOGGER.warn("createStorageMount(line=" + lineOfMountOutput + "): Invalid!");
		}
		
		return null;
	}
	
	/**
	 * @return
	 */
	public List<String> getActionList() {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("getActionList()");
		}
		
		ArrayList<String> list = new ArrayList<String>();
		list.add(Util.BLANK_STRING);
		if (mounted) {
			list.add(ACTION_UNMOUNT);
			list.add(ACTION_REMOUNT);
		} else {
			list.add(ACTION_MOUNT);
		}
		
		return list;
	}
	
	/**
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public final static List<StorageMount> parseFstab() 
			throws FileNotFoundException, IOException {
				
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("parseFstab()");
		}

		return parseFstab(Util.getFstab());
	}
	
	/**
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public final static List<StorageMount> parseFstab(List<String> fstabList) 
			throws FileNotFoundException, IOException {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("parseFstab(fstabList=" + fstabList + ")");
		}

		ArrayList<StorageMount> list = new ArrayList<StorageMount>();

		int lineNo = 0;
		String line = null;
		Iterator<String> it = fstabList.iterator();
		while (it.hasNext()) {
			line = it.next();
			lineNo++;
			
			if (line.trim().length() > 0 && !line.startsWith(Util.HASH)) {
				
				Matcher matcher = null;
				
				synchronized (FSTAB_PATTERN) {
					matcher = FSTAB_PATTERN.matcher(line);
				}
				
				if (matcher.matches() && matcher.groupCount() == 6) {
					if (!FsType.SWAP.equals(matcher.group(3))) {
						int freq = 0;
						try {
							freq = Integer.parseInt(matcher.group(5));
						} catch (NumberFormatException nfe) {}
						
						int passNo = 0;
						try {
							passNo = Integer.parseInt(matcher.group(6));
						} catch (NumberFormatException nfe) {}
						
						StorageMount entry = new StorageMount(matcher.group(1), matcher.group(2), 
								matcher.group(3), matcher.group(4), freq, passNo, lineNo, null, 
								false, true, false);
						
						list.add(entry);
					}
				}
			}
		}
		
		return list;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		
		return "StorageMount [spec=" + spec + ", mountPoint=" + mountPoint
				+ ", fsType=" + fsType + ", options=" + options + ", freq="
				+ freq + ", passNo=" + passNo + ", lineNo=" + lineNo
				+ ", action=" + action + ", persist=" + persist + "]";
	}
}

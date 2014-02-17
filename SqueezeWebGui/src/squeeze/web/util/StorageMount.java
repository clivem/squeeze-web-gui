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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

/**
 * @author Clive Messer <clive.m.messer@gmail.com>
 *
 */
public class StorageMount {
	
	// spec on /mountPoint type fsType (options,options,options)
	private final static Pattern MOUNT_LIST_PATTERN = Pattern.compile("([^ ]+) on ([^ ]+) type ([^ ]+) \\((.*)\\)$");

	public final static String ACTION_MOUNT = "mount";
	public final static String ACTION_UNMOUNT = "unmount";
	public final static String ACTION_REMOUNT = "remount";
	
	private final static Logger LOGGER = Logger.getLogger(StorageMount.class);

	private String spec = null;
	private String mountPoint = null;
	private String fsType = null;
	private String options = null;

	private String action = null;
	
	/**
	 * 
	 */
	public StorageMount() {
	
		super();
	}
	
	/**
	 * 
	 */
	public StorageMount(String spec, String mountPoint, String fsType, String options) {
		
		this();
		
		this.spec = spec;
		this.mountPoint = mountPoint;
		this.fsType = fsType;
		this.options = options;
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
	 * @param locale
	 * @return
	 */
	public final static StorageMount createStorageMount(String line) {

		synchronized (MOUNT_LIST_PATTERN) {
			Matcher m = MOUNT_LIST_PATTERN.matcher(line);
			if (m.matches()) {
				return new StorageMount(m.group(1), m.group(2), m.group(3), m.group(4));
			} else {
				LOGGER.warn("createStorageMount(line=" + line + "): Invalid!");
			}
		}
		
		return null;
	}
	
	/**
	 * @return
	 */
	public final static List<String> generateActionList() {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("generateActionList()");
		}
		
		ArrayList<String> list = new ArrayList<String>();
		list.add(Util.BLANK_STRING);
		list.add(ACTION_UNMOUNT);
		//list.add(ACTION_REMOUNT);
		return list;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "StorageMount[spec=" + spec + ", mountPoint=" + mountPoint
				+ ", fsType=" + fsType + ", options=" + options + ", action=" + action + "]";
	}	
}

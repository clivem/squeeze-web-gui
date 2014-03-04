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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;
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
	public final static String ACTION_PERSIST = "persist";
	public final static String ACTION_UPDATE = "update";
	public final static String ACTION_DELETE = "delete";
	
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
	
	private CifsCredentials cifsCredentials = null;
	
	/**
	 * 
	 */
	public StorageMount() {

		super();
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("StorageMount()");
		}
	}

	/**
	 * 
	 */
	public StorageMount(String spec, String mountPoint, String fsType, String options, boolean mounted, CifsCredentials cifsCredentials) {
		
		this(spec, mountPoint, fsType, options, 0, 0, -1, null, false, false, mounted, cifsCredentials);

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("StorageMount(spec=" + spec + ", mountPoint=" + mountPoint + 
					", fsType=" + fsType + ", options=" + options + ", mounted=" + mounted + 
					", cifsCredentials=" + cifsCredentials + ")");
		}
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
			boolean persist, boolean fstabEntry, boolean mounted, CifsCredentials cifsCredentials) {
		
		this();
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("StorageMount(spec=" + spec + ", mountPoint=" + mountPoint + 
					", fsType=" + fsType + ", options=" + options + ", freq=" + freq + 
					", passNo=" + passNo + ", lineNo=" + lineNo + ", action=" + action +
					", persist=" + persist + ", fstabEntry=" + fstabEntry + 
					", mounted=" + mounted + ", cifsCredentials=" + cifsCredentials + ")");
		}
		
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
		this.cifsCredentials = cifsCredentials;
		
		/*
		 * check for and parse cifs credentials
		 */
		if (cifsCredentials == null) {
			parseOptions();
		}
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
	 * @return the cifsCredentials
	 */
	public CifsCredentials getCifsCredentials() {
		
		return cifsCredentials;
	}
	
	/**
	 * @param cifsCredentials the cifsCredentials to set
	 */
	public void setCifsCredentials(CifsCredentials cifsCredentials) {
		
		this.cifsCredentials = cifsCredentials;
	}
	
	/**
	 * @return
	 */
	public String getStatus() {
		
		return "Status: " + (mounted ? "Mounted" : "Not mounted") + 
				(fstabEntry ? (". Fstab line: " + lineNo) : ". Not in fstab.");
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
			return new StorageMount(matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4), true, null);
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
			if (FsType.CIFS.equals(fsType) || FsType.FUSEBLK.equals(fsType)) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("getActionList(): Not adding remount option to action list!");
				}
			} else {
				list.add(ACTION_REMOUNT);
			}
		} else {
			list.add(ACTION_MOUNT);
		}
		
		if (fstabEntry) {
			list.add(ACTION_UPDATE);
			list.add(ACTION_DELETE);
		} else {
			if (FsType.CIFS.equals(fsType) || FsType.FUSEBLK.equals(fsType)) {
				LOGGER.debug("getActionList(): Not adding persist option to action list!");
			} else {
				list.add(ACTION_PERSIST);
			}
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
						
						StorageMount mount = new StorageMount(matcher.group(1), matcher.group(2), 
								matcher.group(3), matcher.group(4), freq, passNo, lineNo, null, 
								false, true, false, null);
												
						list.add(mount);
					}
				}
			}
		}
		
		return list;
	}
	
	/**
	 * @param options
	 * @return
	 */
	private String getCredentialsFileNameFromMountOptions() {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("getCredentialsFileNameFromMountOptions()");
		}
		
		if (options != null) {
			String cred = CifsCredentials.CREDENTIALS + Util.EQUALS;
			int index = options.indexOf(cred);
			if (index > -1) {
				StringTokenizer tok = new StringTokenizer(
						options.substring(index + cred.length()), ",");
				if (tok.hasMoreTokens()) {
					return tok.nextToken();
				}
			}
		}
		
		return null;
	}
	
	/**
	 * 
	 */
	private void parseOptions() {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("parseOptions()");
		}
		
		/*
		 * Deal with cifs credentials
		 */
		if (fsType != null && FsType.CIFS.equals(fsType)) {
			if (options != null) {
				String fileName = getCredentialsFileNameFromMountOptions();
				if (fileName != null) {
					cifsCredentials = new CifsCredentials(fileName);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		
		return "StorageMount [spec=" + spec + ", mountPoint=" + mountPoint
				+ ", fsType=" + fsType + ", options=" + options + ", freq="
				+ freq + ", passNo=" + passNo + ", lineNo=" + lineNo
				+ ", action=" + action + ", persist=" + persist 
				+ ", cifsCredentials=" + cifsCredentials + "]";
	}
	
	/**
	 * @param value
	 * @return
	 */
	public final static String sanitiseCredentialsFileName(String value) {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("sanitiseCredentialsFileName(value" + value + ")");
		}

		if (!value.startsWith(CifsCredentials.CREDENTIALS_DIR)) {
			File f = new File(value);
			String value_ = CifsCredentials.CREDENTIALS_DIR + File.separator + f.getName();
			LOGGER.debug("sanitiseCredentialsFileName(value=" + value + 
					"): Changing credentials value from '" + value + "' to '" + value_ + "'");
			return value_;
		}
		
		return value;
	}
	
	/**
	 * @param swallowUserPassDomain
	 */
	private void sanitiseMountOptions(boolean swallowUserPassDomain) {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("sanitiseMountOptions(swallowUserPassDomain=" + swallowUserPassDomain + ")");
		}
		
		if (options == null) {
			options = Util.BLANK_STRING;
		}
		
		/*
		 * If we don't have a credentials param, set it here
		 */
		if (!options.contains(CifsCredentials.CREDENTIALS + Util.EQUALS)) {
			options += ((options.length() > 0) ? Util.COMMA : Util.BLANK_STRING) + 
					CifsCredentials.CREDENTIALS + Util.EQUALS +
					CifsCredentials.getUniqueCredentialsFileName();
		}
		
		Map<String, String> map = new HashMap<String, String>();
		
		StringTokenizer tok = new StringTokenizer(options, Util.COMMA);
		while (tok.hasMoreTokens()) {
			String config = tok.nextToken();
			StringTokenizer tok1 = new StringTokenizer(config, Util.EQUALS);
			if (tok1.countTokens() == 1) {
				map.put(tok1.nextToken(), null);
			} else if (tok1.countTokens() == 2) {
				String param = tok1.nextToken();
				String value = tok1.nextToken();
				
				if (CifsCredentials.CREDENTIALS.equals(param)) {
					value = sanitiseCredentialsFileName(value);
					cifsCredentials.setCredentials(value);
				} else if (CifsCredentials.USER.equals(param) || CifsCredentials.USERNAME.equals(param) ||
						CifsCredentials.PASS.equals(param) || CifsCredentials.PASSWORD.equals(param) ||
						CifsCredentials.DOMAIN.equals(param)) {
					LOGGER.debug("sanitiseMountOptions(options=" + options + "): Removing '" +
						param + Util.EQUALS + value + "'!");
					
					if (!swallowUserPassDomain) {
						if (CifsCredentials.USER.equals(param) || CifsCredentials.USERNAME.equals(param)) {
							cifsCredentials.setUsername(value);
						} else if (CifsCredentials.PASS.equals(param) || CifsCredentials.PASSWORD.equals(param)) {
							cifsCredentials.setPassword(value);
						} else if (CifsCredentials.DOMAIN.equals(param)) {
							cifsCredentials.setDomain(value);
						}
					}
					
					continue;
				}
				
				map.put(param, value);
			}
		}
		
		/*
		 * Build the mount options string from the map entries.
		 */
		String result = Util.BLANK_STRING;
		
		Set<Entry<String, String>> set = map.entrySet();
		Iterator<Entry<String, String>> it = set.iterator();
		while (it.hasNext()) {
			Entry<String, String> entry = it.next();
			
			String value = entry.getValue();
			if (value != null) {
				result += entry.getKey() + Util.EQUALS + value;
			} else {
				result += entry.getKey();
			}
			
			if (it.hasNext()) {
				result += Util.COMMA;
			}
		}
		
		options = result;
	}
	
	/**
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void createOrUpdateCifsCredentials()
			throws IOException, InterruptedException {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("createOrUpdateCifsCredentials()");
		}
		
		createOrUpdateCifsCredentials(null, null, null, false);
	}
	
	/**
	 * @param username
	 * @param password
	 * @param domain
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void createOrUpdateCifsCredentials(String username, String password, String domain) 
			throws IOException, InterruptedException {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("createOrUpdateCifsCredentials(username=" + username + 
					", password=" + password + ", domain=" + domain + ")");
		}

		createOrUpdateCifsCredentials(username, password, domain, true);
	}
	
	/**
	 * @param username
	 * @param password
	 * @param domain
	 */
	public void createOrUpdateCifsCredentials(String username, String password, String domain, 
			boolean swallowUserPassDomain) throws IOException, InterruptedException {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("createOrUpdateCifsCredentials(username=" + username + 
					", password=" + password + ", domain=" + domain + 
					", swallowUserPassDomain=" + swallowUserPassDomain + ")");
		}
		
		if (cifsCredentials == null) {
			cifsCredentials = new CifsCredentials();
		}
		
		/*
		 * make that we are using the system credentials path to store a
		 * credentials file and remove user/pass/domain from the mount options.
		 */
		sanitiseMountOptions(swallowUserPassDomain);
		
		String credentialsFile = getCredentialsFileNameFromMountOptions();
		cifsCredentials.setCredentials(credentialsFile);

		if (username != null) {
			cifsCredentials.setUsername(username);
		}
		
		if (password != null) {
			cifsCredentials.setPassword(password);
		}
		
		if (domain != null) {
			cifsCredentials.setDomain(domain);
		}
		
		cifsCredentials.save();
	}
}

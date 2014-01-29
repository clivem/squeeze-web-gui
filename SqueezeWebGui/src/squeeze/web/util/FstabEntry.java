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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author Clive Messer <clive.m.messer@gmail.com>
 *
 */
public class FstabEntry {
	
	private static final Logger LOGGER = Logger.getLogger(FstabEntry.class);
	
	private String spec = null;
	private String file = null;
	private String vfsType = null;
	private String mntOps = null;
	private int freq = 0;
	private int passNo = 0;
	
	private boolean delete = false;
	private String action = null;
	
	public final static String ACTION_MOUNT = "Mount";
	public final static String ACTION_UMOUNT = "Umount";
	public final static String ACTION_REMOUNT = "Remount";
	
	/**
	 * @param spec
	 * @param file
	 * @param vfsType
	 * @param mntOps
	 * @param freq
	 * @param passNo
	 */
	public FstabEntry(String spec, String file, String vfsType, String mntOps, int freq, int passNo) {
		
		super();
		
		this.spec = spec;
		this.file = file;
		this.vfsType = vfsType;
		this.mntOps = mntOps;
		this.freq = freq;
		this.passNo = passNo;
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("FstabEntry(spec=" + spec + ", file=" + file + ", vfsType=" + vfsType + 
					", mntOps=" + mntOps + ", freq=" + freq + ", passNo=" + passNo + ")");
		}
	}

	/**
	 * @return the delete
	 */
	public boolean isDelete() {
		return delete;
	}

	/**
	 * @param delete the delete to set
	 */
	public void setDelete(boolean delete) {
		this.delete = delete;
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
	 * @return the spec
	 */
	public String getSpec() {
		return spec;
	}

	/**
	 * @param spec the spec to set
	 */
	public void setSpec(String spec) {
		this.spec = spec;
	}

	/**
	 * @return the file
	 */
	public String getFile() {
		return file;
	}

	/**
	 * @param file the file to set
	 */
	public void setFile(String file) {
		this.file = file;
	}

	/**
	 * @return the vfsType
	 */
	public String getVfsType() {
		return vfsType;
	}

	/**
	 * @param vfsType the vfsType to set
	 */
	public void setVfsType(String vfsType) {
		this.vfsType = vfsType;
	}

	/**
	 * @return the mntOps
	 */
	public String getMntOps() {
		return mntOps;
	}

	/**
	 * @param mntOps the mntOps to set
	 */
	public void setMntOps(String mntOps) {
		this.mntOps = mntOps;
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
	 * @param line
	 * @return
	 */
	public final static FstabEntry parseEntry(String line) {

		String spec = null;
		String file = null;
		String vfsType = null;
		String mntOps = null;
		int freq = 0;
		int passNo = 0;
		
		int word = 0;
		int index = 0;
		String tmp = "";
		while (index < line.length()) {
			char ch = line.charAt(index);
			if (ch != ' ' && ch != '\t') {
				tmp += ch;
				index++;
			} else {
				if (tmp.length() > 0) {
					switch (word) {
						case 0:
							spec = tmp;
							break;
						case 1:
							file = tmp;
							break;
						case 2:
							vfsType = tmp;
							break;
						case 3:
							mntOps = tmp;
							break;
						case 4:
							freq = Integer.parseInt(tmp);
							break;
						case 5:
							passNo = Integer.parseInt(tmp);
							break;
						default:
							LOGGER.warn("Fstab entry contains more than 6 values: '" + line + "'");
							break;
					}
					tmp = "";
					word++;
				} else {
					index++;
				}
			}
		}
		
		if (word == 6) {
			return new FstabEntry(spec, file, vfsType, mntOps, freq, passNo);
		}
		
		return null;
	}
	
	/**
	 * @return
	 */
	public final static List<FstabEntry> parseFstab() 
			throws FileNotFoundException, IOException {
		
		File file = new File("/etc/fstab");
		BufferedReader br = null;
		try {
			ArrayList<FstabEntry> list = new ArrayList<FstabEntry>();
			
			br = new BufferedReader(new FileReader(file));
			String line = null;
			while ((line = br.readLine()) != null) {
				if (line.trim().length() > 0 && 
						!line.startsWith("#") && 
						!line.startsWith(" ") && 
						!line.startsWith("\t")) {
					FstabEntry entry = parseEntry(line.trim() + " ");
					if (entry != null && 
							!entry.getVfsType().equals("swap") /*&& 
							!entry.getFile().equals("/") &&
							!entry.getFile().equals("/boot") &&
							!entry.getFile().equals("/home")*/) {
						list.add(entry);
					}
				}
			}
			return list;
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (Exception e) {}
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("FstabEntry[");
		buffer.append("spec=");
		buffer.append(spec);
		buffer.append(", file=");
		buffer.append(file);
		buffer.append(", vfsType=");
		buffer.append(vfsType);
		buffer.append(", mntOps=");
		buffer.append(mntOps);
		buffer.append(", freq=");
		buffer.append(freq);
		buffer.append(", passNo=");
		buffer.append(passNo);
		buffer.append("]");
		
		return buffer.toString();
	}
	
	/**
	 * @param args
	 */
	public final static void main(String[] args) {
		try {
			List<FstabEntry> list = parseFstab();
			Iterator<FstabEntry> it = list.iterator();
			while (it.hasNext()) {
				LOGGER.debug(it.next());
			}
		} catch (Exception e) {
			LOGGER.warn("Caught exception processing /etc/fstab!", e);
		}
	}
}

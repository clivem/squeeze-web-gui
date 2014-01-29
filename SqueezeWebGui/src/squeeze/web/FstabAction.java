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
import squeeze.web.util.FstabEntry;
import squeeze.web.util.Util;

import com.opensymphony.xwork2.ActionSupport;

/**
 * @author Clive Messer <clive.m.messer@gmail.com>
 *
 */
public class FstabAction extends ActionSupport {

	private final static Logger LOGGER = Logger.getLogger(FstabAction.class);
	
	private static final long serialVersionUID = 6170755172059220738L;

	protected final static List<String> FSTAB_ENTRY_ACTION_LIST = generateFstabEntryActionList();
	
	protected boolean cbExt4 = true;
	protected boolean cbExt3 = true;
	protected boolean cbExt2 = true;
	protected boolean cbNfs = true;
	protected boolean cbCifs = true;
	protected boolean cbAll = false;

	protected String status = null;
	
	protected List<FstabEntry> fstabList = null;
	
	protected List<String> fstabEntryActionList = FSTAB_ENTRY_ACTION_LIST;
	
	/**
	 * 
	 */
	public FstabAction() {
		
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
	public String populate() throws Exception {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("populate()");
		}
		
		populateMounts();
		fstabList = FstabEntry.parseFstab();
		
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

		String regex = "";
		if (cbAll) {
			regex = ".*";
		} else if (cbExt4 || cbExt3 || cbExt2 || cbCifs || cbNfs) {
			ArrayList<String> fsTypeList = new ArrayList<String>();
			if (cbExt4) {
				fsTypeList.add(FsType.EXT4);
			}
			if (cbExt3) {
				fsTypeList.add(FsType.EXT3);
			}
			if (cbExt2) {
				fsTypeList.add(FsType.EXT2);
			}
			if (cbCifs) {
				fsTypeList.add(FsType.CIFS);
			}
			if (cbNfs) {
				fsTypeList.add(FsType.NFS);
			}
			
			regex = "^.*(";
			for (int i = 0; i < fsTypeList.size(); i++) {
				regex += "type " + fsTypeList.get(i);
				if (i + 1 < fsTypeList.size()) {
					regex += "|";
				}
			}
			
			regex += "){1}.*$";
		} else {
			status = "";
			return;
		}
		
		status = Util.getMounts(regex);
	}

	/**
	 * @return the fstabEntryActionList
	 */
	public List<String> getFstabEntryActionList() {
		return fstabEntryActionList;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		
		this.status = status;
	}

	/**
	 * @return the cbExt4
	 */
	public boolean isCbExt4() {
		
		return cbExt4;
	}

	/**
	 * @param cbExt4 the cbExt4 to set
	 */
	public void setCbExt4(boolean cbExt4) {
		
		this.cbExt4 = cbExt4;
	}
	
	/**
	 * @return the cbExt3
	 */
	public boolean isCbExt3() {
		return cbExt3;
	}

	/**
	 * @param cbExt3 the cbExt3 to set
	 */
	public void setCbExt3(boolean cbExt3) {
		this.cbExt3 = cbExt3;
	}

	/**
	 * @return the cbExt2
	 */
	public boolean isCbExt2() {
		return cbExt2;
	}

	/**
	 * @param cbExt2 the cbExt2 to set
	 */
	public void setCbExt2(boolean cbExt2) {
		this.cbExt2 = cbExt2;
	}

	/**
	 * @return the cbNfs
	 */
	public boolean isCbNfs() {
		
		return cbNfs;
	}

	/**
	 * @param cbNfs the cbNfs to set
	 */
	public void setCbNfs(boolean cbNfs) {
		
		this.cbNfs = cbNfs;
	}

	/**
	 * @return the cbCifs
	 */
	public boolean isCbCifs() {
		
		return cbCifs;
	}

	/**
	 * @param cbCifs the cbCifs to set
	 */
	public void setCbCifs(boolean cbCifs) {
		
		this.cbCifs = cbCifs;
	}

	/**
	 * @return the cbAll
	 */
	public boolean isCbAll() {
		
		return cbAll;
	}

	/**
	 * @param cbAll the cbAll to set
	 */
	public void setCbAll(boolean cbAll) {
		
		this.cbAll = cbAll;
	}

	/**
	 * @return the fstabList
	 */
	public List<FstabEntry> getFstabList() {
		return fstabList;
	}

	/**
	 * @param fstabList the fstabList to set
	 */
	public void setFstabList(List<FstabEntry> fstabList) {
		this.fstabList = fstabList;
	}
	
	/**
	 * @return
	 */
	public final static List<String> generateFstabEntryActionList() {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("generateFstabEntryActionList()");
		}
		
		ArrayList<String> list = new ArrayList<String>();
		list.add(Util.BLANK_STRING);
		list.add(FstabEntry.ACTION_MOUNT);
		list.add(FstabEntry.ACTION_UMOUNT);
		list.add(FstabEntry.ACTION_REMOUNT);
		return list;
	}
}

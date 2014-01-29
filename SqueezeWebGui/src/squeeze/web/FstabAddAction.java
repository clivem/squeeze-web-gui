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

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

/**
 * @author Clive Messer <clive.m.messer@gmail.com>
 *
 */
public class FstabAddAction extends ActionSupport {

	private static final long serialVersionUID = 4985449297356355910L;

	private final static Logger LOGGER = Logger.getLogger(FstabAddAction.class);

	protected String spec = null;
	protected String file = null;
	protected String vfsType = null;
	protected String mntOps = "defaults";
	protected int freq = 0;
	protected int passNo = 0;

	/**
	 * 
	 */
	public FstabAddAction() {
		
		super();
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("FstabAddAction()");
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

		String result = SUCCESS;
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("execute() returns " + result);
		}
		
		return result;
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
}

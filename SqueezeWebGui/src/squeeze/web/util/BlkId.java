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
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

/**
 * @author Clive Messer <clive.m.messer@gmail.com>
 *
 */
public class BlkId {

	private final static Logger LOGGER = Logger.getLogger(BlkId.class);
	
	public final static String LABEL = "LABEL";
	public final static String UUID = "UUID";
	public final static String TYPE = "TYPE";
	
	//private HashMap<String, String> properties = new HashMap<String, String>();
	
	private String device = null;
	private String label = null;
	private String uuid = null;
	private String type = null;
	
	/**
	 * @param blkid
	 */
	public BlkId(String blkid) {
		
		super();
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("BlkId(blkid=" + blkid + ")");
		}
		
		parseBlkId(blkid);
	}
	
	/**
	 * @return the device
	 */
	public String getDevice() {
		return device;
	}

	/**
	 * @param device the device to set
	 */
	public void setDevice(String device) {
		this.device = device;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return the uuid
	 */
	public String getUuid() {
		return uuid;
	}

	/**
	 * @param uuid the uuid to set
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @param blkid
	 */
	private void parseBlkId(String blkid) {
		
		StringTokenizer tok = new StringTokenizer(blkid, Util.SPACE);
		if (tok.hasMoreTokens()) {
			device = tok.nextToken();
			if (device.endsWith(":")) {
				device = device.substring(0, device.length() - 1);
			}
			
			while (tok.hasMoreTokens()) {
				String temp = tok.nextToken();
				if (temp.contains(Util.EQUALS)) {
					StringTokenizer tok1 = new StringTokenizer(temp, Util.EQUALS);
					if (tok1.countTokens() == 2) {
						String name = tok1.nextToken();
						String value = Util.trimQuotes(tok1.nextToken());
						if (LABEL.equals(name)) {
							label = value;
						} else if (UUID.equals(name)) {
							uuid = value;
						} else if (TYPE.equals(name)) {
							type = value;
						}
					}
				}
			}
		}
	}
	
	/**
	 * @param device
	 * @return
	 */
	public final static BlkId getBlkId(String device) {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("getBlkId(device=" + device + ")");
		}
		
		File tmpFile = null;
		BufferedReader reader = null;
		try {
			tmpFile = Util.createTempFile("blkid_", ".txt");
			Writer writer = new FileWriter(tmpFile);

			String[] cmdLineArgs = new String[] {
					Commands.CMD_SUDO, Commands.CMD_BLKID, device
			};
			
			ExecuteProcess.executeCommand(cmdLineArgs, writer, null);
			reader = new BufferedReader(new FileReader(tmpFile));
			String line = reader.readLine();
			if (line != null) {
				return new BlkId(line);
			}
		} catch (Exception e) {
			LOGGER.warn("getBlkId(device=" + device + ")", e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (Exception e) {}
			}
			
			if (tmpFile != null) {
				try {
					tmpFile.delete();
				} catch (Exception e) {}
			}
		}
		
		return null;
	}
}

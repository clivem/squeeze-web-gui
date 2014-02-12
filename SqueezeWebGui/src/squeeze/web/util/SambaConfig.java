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
import java.io.FileReader;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

/**
 * @author Clive Messer <clive.m.messer@gmail.com>
 *
 */
public class SambaConfig {

	private static final Logger LOGGER = Logger.getLogger(SambaConfig.class); 
	
	//public final static String REGEX_NETBIOS_NAME = "^\\s*netbios name\\s*=\\s*(.*)$";
	
	private final static String SAMBA_CONFIG_FILENAME = "/etc/samba/smb.conf";
	
	public String netbiosName = null;
	public String workgroup = null;
	
	/**
	 * 
	 */
	private SambaConfig(String netbiosName, String workgroup) {
		
		super();
		
		this.netbiosName = netbiosName;
		this.workgroup = workgroup;
	}
	
	/**
	 * @return the netbiosName
	 */
	public String getNetbiosName() {
		return netbiosName;
	}

	/**
	 * @return the workgroup
	 */
	public String getWorkgroup() {
		return workgroup;
	}

	/**
	 * @return
	 */
	public final static SambaConfig getSambaConfig() {
		
		String netbiosName = null;
		String workgroup = null;
		
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(SAMBA_CONFIG_FILENAME));
			String line = null;
			while ((line = reader.readLine()) != null) {
				/*
				Matcher m = Pattern.compile(REGEX_NETBIOS_NAME).matcher(line);
				if (m.matches()) {
					LOGGER.warn(m.group(1));
				}
				*/
				line = line.trim();
				if (line.startsWith("netbios name")) {
					StringTokenizer tok = new StringTokenizer(line, "=");
					if (tok.countTokens() == 2) {
						tok.nextToken();
						netbiosName = Util.trimQuotes(tok.nextToken().trim());
					}
				} else if (line.startsWith("workgroup")) {
					StringTokenizer tok = new StringTokenizer(line, "=");
					if (tok.countTokens() == 2) {
						tok.nextToken();
						workgroup = Util.trimQuotes(tok.nextToken().trim());
					}
				}
			}
		} catch (Exception e) {
			LOGGER.warn("getSambaConfig()", e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (Exception e) {}
			}
		}
		
		return new SambaConfig(netbiosName, workgroup);
	}	
}

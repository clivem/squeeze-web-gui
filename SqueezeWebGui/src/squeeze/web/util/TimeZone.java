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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Clive Messer <clive.m.messer@gmail.com>
 *
 */
public class TimeZone {

	private final static String ZONE_DIR = "/usr/share/zoneinfo";
	private final static String CURRENT_ZONE = "/etc/localtime";

	/**
	 * 
	 */
	private TimeZone() {}
	
	/**
	 * @return
	 */
	public final static String getCurrentTimeZone() {
		
		File file = new File(CURRENT_ZONE);
		try {
			if (isSymlink(file)) {
				String canon = file.getCanonicalPath();
				if (canon != null && canon.startsWith(ZONE_DIR)) {
					return canon.substring(ZONE_DIR.length() + 1);
				}
			}
		} catch (IOException ioe) {}
		
		return null;
	}
	
	/**
	 * @return
	 */
	public final static List<String> getTimeZoneList() {
		
		File file = new File(ZONE_DIR);
		List<String> list = new ArrayList<String>();
		list.add(Util.BLANK_STRING);
		
		walkZoneFiles(file, list);
		
		return list;
	}
	
	/**
	 * @param file
	 * @param list
	 */
	private final static void walkZoneFiles(File file, List<String> list) {
		
		if (file.isDirectory()) {
			String dirName = file.getName();
			if (!dirName.equals("posix") && !dirName.equals("right")) {
				File[] fileList = file.listFiles();
				if (fileList != null && fileList.length > 0) {
					Arrays.sort(fileList); 
					for (int i = 0; i < fileList.length; i++) {
						walkZoneFiles(fileList[i], list);
					}
				}
			}
		} else {
			String name = file.getName();
			if (!name.endsWith(".tab") && 
					!name.equals("posixrules")) {
				list.add(file.getAbsolutePath().substring(ZONE_DIR.length() + 1));
			}
		}		
	}
	
	/**
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public final static boolean isSymlink(File file) throws IOException {
		
		if (file == null) {
			throw new NullPointerException("File must not be null");
		}
		
		File canon;
		if (file.getParent() == null) {
			canon = file;
		} else {
			File canonDir = file.getParentFile().getCanonicalFile();
			canon = new File(canonDir, file.getName());
		}
		
		return !canon.getCanonicalFile().equals(canon.getAbsoluteFile());
	}
}

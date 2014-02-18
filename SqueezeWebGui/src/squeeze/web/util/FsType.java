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

/**
 * @author Clive Messer <clive.m.messer@gmail.com>
 *
 */
public class FsType {

	public final static String AUTO = "auto";
	public final static String EXT4 = "ext4";
	public final static String EXT3 = "ext3";
	public final static String EXT2 = "ext2";
	public final static String NFS = "nfs";
	public final static String NFS4 = "nfs4";
	public final static String CIFS = "cifs";
	public final static String FAT = "fat";
	public final static String VFAT = "vfat";
	public final static String NTFS = "ntfs";
	public final static String NTFS3G = "ntfs-3g";
	public final static String SWAP = "swap";
	
	/**
	 * 
	 */
	private FsType() {
		
		super();
	}
}

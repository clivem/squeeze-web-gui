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
public class Commands {
	
	public final static String SCRIPT_INTERFACE_UPDATE = "sp-ifcfgUpdate";
	public final static String SCRIPT_KEYS_READ = "sp-keysRead";
	public final static String SCRIPT_KEYS_UPDATE = "sp-keysUpdate";
	public final static String SCRIPT_SQUEEZELITE_CONFIG_UPDATE = "sp-squeezeliteConfigUpdate";
	public final static String SCRIPT_REBOOT = "sp-reboot";
	public final static String SCRIPT_HALT = "sp-halt";
	public final static String SCRIPT_TIMEZONE = "sp-timeZone";
	public final static String SCRIPT_LOCALE_UPDATE = "sp-localeUpdate";
	public final static String SCRIPT_HOSTNAME_UPDATE = "sp-hostnameUpdate";
	public final static String SCRIPT_SAMBA_NETBIOS_NAME_UPDATE = "sp-sambaConfigNetbiosName";
	public final static String SCRIPT_SAMBA_WORGROUP_UPDATE = "sp-sambaConfigWorkgroup";
	
	public final static String CMD_SUDO = "sudo";
	public final static String CMD_NOHUP = "nohup";
	public final static String CMD_REBOOT = "reboot";
	public final static String CMD_IFCONFIG = "ifconfig";
	public final static String CMD_IFUP = "ifup";
	public final static String CMD_IFDOWN = "ifdown";
	public final static String CMD_MOUNT = "mount";
	public final static String CMD_WALL = "wall";
	public final static String CMD_CAT = "cat";
	public final static String CMD_UMOUNT = "umount";
	
	public final static String SHUTDOWN_FORCE = "-f";
	public final static String UMOUNT_FORCE = "-f";
	
	public final static String CMD_APLAY = "aplay";
	public final static String APLAY_LIST = "-L";
	
	public final static String CMD_WPA_CLI = "wpa_cli"; 	
	public final static String WPA_CLI_SCAN = "scan"; 	
	public final static String WPA_CLI_SCAN_OK = "OK";
	public final static String WPA_CLI_SCAN_RESULTS = "scan_results"; 	
	public final static String WPA_CLI_INTERFACE_FLAG = "-i"; 	
	
	public final static String CMD_SYSTEMCTL = "systemctl"; 	
	public final static String SYSTEMCTL_STATUS = "status"; 
	public final static String SYSTEMCTL_START = "start"; 
	public final static String SYSTEMCTL_STOP = "stop"; 
	public final static String SYSTEMCTL_ENABLE = "enable"; 
	public final static String SYSTEMCTL_DISABLE = "disable"; 
	public final static String SYSTEMCTL_RESTART = "restart"; 
	public final static String SYSTEMCTL_COND_RESTART = "try-restart"; 
	
	/**
	 * 
	 */
	private Commands() {
		super();
	}
}

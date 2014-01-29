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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import squeeze.web.util.Commands;
import squeeze.web.util.ExecuteProcess;
import squeeze.web.util.StringIgnoreCaseComparator;
import squeeze.web.util.Util;
import squeeze.web.util.Validate;

import com.opensymphony.xwork2.ActionSupport;

/**
 * @author Clive Messer <clive.m.messer@gmail.com>
 *
 */
public abstract class InterfaceAction extends ActionSupport {
	
	private static final long serialVersionUID = -398961030369733246L;
	
	private final static Logger LOGGER = Logger.getLogger(InterfaceAction.class);
	
	private final static String[][] NETMASK_PREFIX_LIST = {
		{"255.255.255.0", "24"},
		{"255.255.255.128", "25"},
		{"255.255.255.192", "26"},
		{"255.255.255.224", "27"},
		{"255.255.255.240", "28"},
		{"255.255.255.248", "29"},
		{"255.255.255.252", "30"},
		{"255.255.255.254", "31"}
	};
	
	private final static String NETWORK_CONFIG_PATH = "/etc/sysconfig/network-scripts/";
	private final static String IFCFG_PREFIX = "ifcfg-";
	private final static String KEYS_PREFIX = "keys-";

	private final static String INTERFACE_PATH_PREFIX = NETWORK_CONFIG_PATH + IFCFG_PREFIX;
	
	/*
	 * Ethernet
	 */
	public final static String CFG_NAME = "NAME";
	public final static String CFG_TYPE = "TYPE";
	public final static String CFG_UUID = "UUID";
	public final static String CFG_HWADDR = "HWADDR";
	public final static String CFG_IPADDR0 = "IPADDR0";
	public final static String CFG_PREFIX0 = "PREFIX0";
	public final static String CFG_GATEWAY0 = "GATEWAY0";
	public final static String CFG_NETMASK0 = "NETMASK0";
	public final static String CFG_DNS1 = "DNS1";
	public final static String CFG_DNS2 = "DNS2";
	public final static String CFG_DNS3 = "DNS3";
	public final static String CFG_DOMAIN = "DOMAIN";
	public final static String CFG_ONBOOT = "ONBOOT";
	public final static String CFG_ZONE = "ZONE";
	public final static String CFG_BOOTPROTO = "BOOTPROTO";
	
	public final static String CFG_TYPE_WIRELESS = "Wireless";
	public final static String CFG_BOOTPROTO_NONE = "none";
	public final static String CFG_BOOTPROTO_DHCP = "dhcp";

	/*
	 * Wireless ifcfg-<interface>
	 */
	public final static String CFG_ESSID = "ESSID";
	public final static String CFG_MODE = "MODE";
	public final static String CFG_KEY_MGMT = "KEY_MGMT";
	public final static String CFG_SCAN_SSID = "SCAN_SSID";
	
	/*
	 * Wireless keys-<interface>
	 */
	public final static String CFG_WPA_PSK = "WPA_PSK";
	
	public final static String DUMMY_ESSID = "YOUR_ESSID_HERE";
	public final static String DUMMY_WPA_PSK = "YOUR_PSK_HERE";
	public final static String ESSID_SELECT_OTHER = "User Specified (below):";
	
	public final static String ONBOOT_TRUE = "yes";
	public final static String ONBOOT_FALSE = "no";
	
	protected SortedMap<String, String> interfaceProperties = 
			new TreeMap<String, String>(StringIgnoreCaseComparator.COMPARATOR);
	protected SortedMap<String, String> keysProperties = 
			new TreeMap<String, String>(StringIgnoreCaseComparator.COMPARATOR);
	
	protected String name;
	protected String type;
	protected String uuid;
	protected String hwAddr;
	
	protected String ipAddr0;
	protected String netmask0;
	protected String gateway0;
	
	protected String dns1;
	protected String dns2;
	protected String dns3;
	protected String domain;

	//protected String onBoot;
	protected boolean onBoot = true;
	protected String zone;
	protected String bootProto;

	protected String wirelessEssid;
	protected String wirelessEssidOther;
	protected String wirelessMode;
	protected String wirelessKeyMgmt;
	protected String wirelessWpaPsk;
	
	protected List<String> networkList;
	
	protected String status;
	
	protected boolean cbForceReboot = false;

	/**
	 * 
	 */
	public InterfaceAction() {
		
		super();
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("InterfaceAction()");
		}
	}
	
	/**
	 * @return
	 */
	public abstract String getInterfaceName();

	/**
	 * @return the networkList
	 */
	public List<String> getNetworkList() {
		
		return networkList;
	}
	
	/**
	 * @param networkList
	 */
	public void setNetworkList(List<String> networkList) {
		this.networkList = networkList;
	}

	/**
	 * @return the wirelessWpaPsk
	 */
	public String getWirelessWpaPsk() {
		
		return wirelessWpaPsk;
	}

	/**
	 * @param wirelessWpaPsk the wirelessWpaPsk to set
	 */
	public void setWirelessWpaPsk(String wirelessWpaPsk) {
		
		this.wirelessWpaPsk = wirelessWpaPsk;
	}

	/**
	 * @return the wirelessEssid
	 */
	public String getWirelessEssid() {
		
		return wirelessEssid;
	}

	/**
	 * @param wirelessEssid the wirelessEssid to set
	 */
	public void setWirelessEssid(String wirelessEssid) {
		
		this.wirelessEssid = wirelessEssid;
	}

	/**
	 * @return the wirelessEssidOther
	 */
	public String getWirelessEssidOther() {
		
		return wirelessEssidOther;
	}

	/**
	 * @param wirelessEssidOther the wirelessEssidOther to set
	 */
	public void setWirelessEssidOther(String wirelessEssidOther) {
		
		this.wirelessEssidOther = wirelessEssidOther;
	}

	/**
	 * @return the wirelessMode
	 */
	public String getWirelessMode() {
		
		return wirelessMode;
	}

	/**
	 * @param wirelessMode the wirelessMode to set
	 */
	public void setWirelessMode(String wirelessMode) {
		
		this.wirelessMode = wirelessMode;
	}

	/**
	 * @return the wirelessKeyMgmt
	 */
	public String getWirelessKeyMgmt() {
		
		return wirelessKeyMgmt;
	}

	/**
	 * @param wirelessKeyMgmt the wirelessKeyMgmt to set
	 */
	public void setWirelessKeyMgmt(String wirelessKeyMgmt) {
		
		this.wirelessKeyMgmt = wirelessKeyMgmt;
	}
	
	/**
	 * @return
	 */
	public String getName() {
		
		return name;
	}

	/**
	 * @param name
	 */
	public void setName(String name) {
		
		this.name = name;
	}

	/**
	 * @return
	 */
	public String getType() {
		
		return type;
	}

	/**
	 * @param type
	 */
	public void setType(String type) {
		
		this.type = type;
	}

	/**
	 * @return
	 */
	public String getUuid() {
		
		return uuid;
	}

	/**
	 * @param uuid
	 */
	public void setUuid(String uuid) {
		
		this.uuid = uuid;
	}

	/**
	 * @return
	 */
	public String getHwAddr() {
		
		return hwAddr;
	}

	/**
	 * @param hwAddr
	 */
	public void setHwAddr(String hwAddr) {
		
		this.hwAddr = hwAddr;
	}

	/**
	 * @return
	 */
	public String getIpAddr0() {
		
		return ipAddr0;
	}

	/**
	 * @param ipAddr0
	 */
	public void setIpAddr0(String ipAddr0) {
		
		this.ipAddr0 = ipAddr0;
	}

	/**
	 * @return
	 */
	public String getNetmask0() {
		
		return netmask0;
	}
	
	/**
	 * @param netmask0
	 */
	public void setNetmask0(String netmask0) {
		
		this.netmask0 = netmask0;
	}
	
	/**
	 * @return
	 */
	public String getGateway0() {
		
		return gateway0;
	}

	/**
	 * @param gateway0
	 */
	public void setGateway0(String gateway0) {
		
		this.gateway0 = gateway0;
	}

	/**
	 * @return
	 */
	public String getDns1() {
		
		return dns1;
	}

	/**
	 * @param dns1
	 */
	public void setDns1(String dns1) {
		
		this.dns1 = dns1;
	}

	/**
	 * @return
	 */
	public String getDns2() {
		
		return dns2;
	}

	/**
	 * @param dns2
	 */
	public void setDns2(String dns2) {
		
		this.dns2 = dns2;
	}

	/**
	 * @return
	 */
	public String getDns3() {
		
		return dns3;
	}

	/**
	 * @param dns3
	 */
	public void setDns3(String dns3) {
		
		this.dns3 = dns3;
	}

	/**
	 * @return
	 */
	public String getDomain() {
		
		return domain;
	}

	/**
	 * @param domain
	 */
	public void setDomain(String domain) {
		
		this.domain = domain;
	}

	/**
	 * @return the onBoot
	 */
	public boolean isOnBoot() {
		return onBoot;
	}

	/**
	 * @param onBoot the onBoot to set
	 */
	public void setOnBoot(boolean onBoot) {
		this.onBoot = onBoot;
	}

	/**
	 * @return
	 */
	public String getZone() {
		
		return zone;
	}

	/**
	 * @param zone
	 */
	public void setZone(String zone) {
		
		this.zone = zone;
	}

	/**
	 * @return
	 */
	public String getBootProto() {
		
		return bootProto;
	}

	/**
	 * @param bootProto
	 */
	public void setBootProto(String bootProto) {
		
		this.bootProto = bootProto;
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
	 * @return the cbForceReboot
	 */
	public boolean isCbForceReboot() {
		return cbForceReboot;
	}

	/**
	 * @param cbForceReboot the cbForceReboot to set
	 */
	public void setCbForceReboot(boolean cbForceReboot) {
		this.cbForceReboot = cbForceReboot;
	}

	/**
	 * @return
	 */
	public SortedMap<String, String> getInterfaceProperties() {
		
		return interfaceProperties;
	}
	
	/**
	 * @return
	 */
	public SortedMap<String, String> getKeysProperties() {
		
		return keysProperties;
	}

	/**
	 * 
	 */
	protected void validate_() {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("validate_()");
		}
		
		if (ipAddr0 != null && ipAddr0.trim().length() > 0) {
			if (!ipAddr0.trim().matches(Validate.REGEX_IP_ADDRESS)) {
				addActionError(getText("interface.validation.ipAddress.fail"));
			}
		}
		
		if (netmask0 != null && netmask0.trim().length() > 0) {
			if (!netmask0.trim().matches(Validate.REGEX_IP_ADDRESS)) {
				addActionError(getText("interface.validation.netmask.fail"));
			}
		}

		if (gateway0 != null && gateway0.trim().length() > 0) {
			if (!gateway0.trim().matches(Validate.REGEX_IP_ADDRESS)) {
				addActionError(getText("interface.validation.gateway.fail"));
			}
		}
		
		if (dns1 != null && dns1.trim().length() > 0) {
			if (!dns1.trim().matches(Validate.REGEX_IP_ADDRESS)) {
				addActionError(getText("interface.validation.dns1.fail"));
			}
		}
		
		if (dns2 != null && dns2.trim().length() > 0) {
			if (!dns2.trim().matches(Validate.REGEX_IP_ADDRESS)) {
				addActionError(getText("interface.validation.dns2.fail"));
			}
		}

		if (dns3 != null && dns3.trim().length() > 0) {
			if (!dns3.trim().matches(Validate.REGEX_IP_ADDRESS)) {
				addActionError(getText("interface.validation.dns3.fail"));
			}
		}
		
		if (domain != null && domain.trim().length() > 0) {
			if (!domain.trim().matches(Validate.REGEX_HOST_ADDRESS)) {
				addActionError(getText("interface.validation.domain.fail"));
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.opensymphony.xwork2.ActionSupport#execute()
	 */
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

		try {
			populatePropertiesFromConfigFile();
			populateInterfaceStatus();
		} catch (Exception e) {
			LOGGER.warn("Caught exception populating " + getInterfaceName() + "!", e);
			throw e;
		}
		
		String result = "populate";
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("populate() returns " + result);
		}
		
		return result;
	}
	
	/**
	 * @return
	 * @throws Exception
	 */
	protected String save_() throws Exception {		
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("save_()");
		}

		File tmpInterfaceFile = null;
		File tmpKeysFile = null;
		try {
			/*
			 * populate the property map
			 */
			readInterfaceConfigProperties();
			/*
			 * overwrite with the properties set in the web-gui
			 */
			saveProperties();
			/*
			 * write the properties to a temp file
			 */
			tmpInterfaceFile = writeTempInterfaceProperties();
			/*
			 * replace the real config file with the temp file
			 */
			replaceInterfaceConfig(tmpInterfaceFile);
			
			/*
			 * write the wireless keys file
			 */
			if (type != null && type.equals(CFG_TYPE_WIRELESS)) {
				tmpKeysFile = writeTempKeysProperties();
				replaceKeysConfig(tmpKeysFile);
			}
		} catch (Exception e) {
			LOGGER.error("Caught exception saving " + getInterfaceName() + "!", e);
			throw e;
		} finally {
			if (tmpInterfaceFile != null) {
				try {
					tmpInterfaceFile.delete();
				} catch (Exception e) {}
			}
			if (tmpKeysFile != null) {
				try {
					tmpKeysFile.delete();
				} catch (Exception e) {}
			}
		}
		
		String result = SUCCESS;
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("save_() returns " + result);
		}
		
		return result;
	}
	
	/**
	 * @return
	 * @throws Exception
	 */
	public String up() throws Exception {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("up()");
		}

		try {
			interfaceUp();
		} catch (Exception e) {
			LOGGER.warn("Caught exception bringing interface " + getInterfaceName() + " up!", e);
			throw e;
		}
		
		String result = SUCCESS;
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("up() returns " + result);
		}
		
		return result;
	}
	
	/**
	 * @return
	 * @throws Exception
	 */
	public String down() throws Exception {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("down()");
		}

		try {
			interfaceDown();
		} catch (Exception e) {
			LOGGER.warn("Caught exception taking interface " + getInterfaceName() + " down!", e);
			throw e;
		}
		
		String result = SUCCESS;
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("down() returns " + result);
		}
		
		return result;
	}
	
	/**
	 * @return
	 * @throws Exception
	 */
	public String downUp() throws Exception {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("downUp()");
		}

		try {
			interfaceDownUp();
		} catch (Exception e) {
			LOGGER.warn("Caught exception taking interface " + getInterfaceName() + 
							" down and back up again!", e);
			throw e;
		}
		
		String result = SUCCESS;
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("downUp() returns " + result);
		}
		
		return result;
	}
	
	/**
	 * 
	 */
	protected void saveProperties() {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("saveProperties()");
		}

		/*
		 * Are we a wireless interface?
		 */
		if (type != null && type.equalsIgnoreCase(CFG_TYPE_WIRELESS)) {
			/*
			 * If ESSID is user defined, set SCAN_SSID=1 
			 * to allow connecting to a hidden network. 
			 * Otherwise, remove SCAN_SSID from the properties.
			 */
			if (wirelessEssid != null && wirelessEssid.trim().length() > 0) {
				if (wirelessEssid.trim().equals(ESSID_SELECT_OTHER)) {
					if (wirelessEssidOther != null && wirelessEssidOther.trim().length() > 0) {
						interfaceProperties.put(CFG_ESSID, "\"" + wirelessEssidOther.trim() + "\"");
						interfaceProperties.put(CFG_SCAN_SSID, "1");
					} else {
						interfaceProperties.put(CFG_ESSID, "\"" + DUMMY_ESSID + "\"");
						interfaceProperties.remove(CFG_SCAN_SSID);
					}
				} else {
					interfaceProperties.put(CFG_ESSID, "\"" + wirelessEssid.trim() + "\"");
					interfaceProperties.remove(CFG_SCAN_SSID);
				}
			} else {
				interfaceProperties.put(CFG_ESSID, "\"" + DUMMY_ESSID + "\"");
				interfaceProperties.remove(CFG_SCAN_SSID);
			}
			
			if (wirelessWpaPsk != null && wirelessWpaPsk.trim().length() > 0) {
				keysProperties.put(CFG_WPA_PSK, "\'" + wirelessWpaPsk.trim() + "\'");
			} else {
				keysProperties.put(CFG_WPA_PSK, "\'" + DUMMY_WPA_PSK + "\'");
			}
		}
		
		/*
		 * static IP
		 */
		if (ipAddr0 != null && ipAddr0.trim().length() > 0 &&
				netmask0 != null && netmask0.trim().length() > 0 &&
				gateway0 != null && gateway0.trim().length() > 0) {
			interfaceProperties.put(CFG_IPADDR0, ipAddr0.trim());
			interfaceProperties.put(CFG_NETMASK0, netmask0.trim());
			interfaceProperties.put(CFG_GATEWAY0, gateway0.trim());
			interfaceProperties.put(CFG_BOOTPROTO, CFG_BOOTPROTO_NONE);
		/*
		 * dynamic IP / dhcp
		 */
		} else {
			interfaceProperties.remove(CFG_IPADDR0);
			interfaceProperties.remove(CFG_NETMASK0);
			interfaceProperties.remove(CFG_GATEWAY0);
			interfaceProperties.put(CFG_BOOTPROTO, CFG_BOOTPROTO_DHCP);
		}
			
		if (dns1 != null && dns1.trim().length() > 0) {
			interfaceProperties.put(CFG_DNS1, dns1.trim());
		} else {
			interfaceProperties.remove(CFG_DNS1);
		}
	
		if (dns2 != null && dns2.trim().length() > 0) {
			interfaceProperties.put(CFG_DNS2, dns2.trim());
		} else {
			interfaceProperties.remove(CFG_DNS2);
		}
		
		if (dns3 != null && dns3.trim().length() > 0) {
			interfaceProperties.put(CFG_DNS3, dns3.trim());
		} else {
			interfaceProperties.remove(CFG_DNS3);
		}
		
		if (domain != null && domain.trim().length() > 0) {
			interfaceProperties.put(CFG_DOMAIN, domain.trim());
		} else {
			interfaceProperties.remove(CFG_DOMAIN);
		}
		
		interfaceProperties.put(CFG_ONBOOT, (onBoot) ? ONBOOT_TRUE : ONBOOT_FALSE);
	}

	/**
	 * @throws IOException
	 * @throws InterruptedException
	 */
	protected void populateInterfaceStatus() 
			throws IOException, InterruptedException {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("populateInterfaceStatus()");
		}

		File outFile = null;
		try {
			String interfaceName = getInterfaceName();
			outFile = Util.createTempFile(interfaceName + "_status_", ".txt");
			status = Util.getInterfaceStatus(interfaceName, outFile);
		} finally {
			if (outFile != null) {
				try {
					outFile.delete();
				} catch (Exception e) {}
			}
		}
	}
	
	/**
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	protected void populatePropertiesFromConfigFile() 
			throws IOException, FileNotFoundException, InterruptedException {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("populatePropertiesFromConfigFile()");
		}

		readInterfaceConfigProperties();
		
		name = interfaceProperties.get(CFG_NAME);
		type = interfaceProperties.get(CFG_TYPE);
		uuid = interfaceProperties.get(CFG_UUID);
		hwAddr = interfaceProperties.get(CFG_HWADDR);
		ipAddr0 = interfaceProperties.get(CFG_IPADDR0);
		netmask0 = interfaceProperties.get(CFG_NETMASK0);
		gateway0 = interfaceProperties.get(CFG_GATEWAY0);
		dns1 = interfaceProperties.get(CFG_DNS1);
		dns2 = interfaceProperties.get(CFG_DNS2);
		dns3 = interfaceProperties.get(CFG_DNS3);
		domain = interfaceProperties.get(CFG_DOMAIN);
		String tmp = interfaceProperties.get(CFG_ONBOOT);
		if (tmp != null && tmp.equals("no")) {
			onBoot = false;
		} else {
			onBoot = true;
		}
		zone = interfaceProperties.get(CFG_ZONE);
		bootProto = interfaceProperties.get(CFG_BOOTPROTO);

		/*
		 * Are we a wireless interface?
		 */
		if (type != null && type.equalsIgnoreCase(CFG_TYPE_WIRELESS)) {
			wirelessEssid = interfaceProperties.get(CFG_ESSID);
			wirelessMode = interfaceProperties.get(CFG_MODE);
			wirelessKeyMgmt = interfaceProperties.get(CFG_KEY_MGMT);
			
			try {
				readKeysConfigProperties();
			} catch (FileNotFoundException fnfe) {
				/*
				 * It's OK to ignore this. keys-<interface> might not exist.
				 * We'll create it, when we save()
				 */
			}
			
			wirelessWpaPsk = keysProperties.get(CFG_WPA_PSK);
			
			/*
			 * get the available networks
			 */
			networkList = Util.getAvailableNetworks(getInterfaceName());
			networkList.add(ESSID_SELECT_OTHER);

			if (wirelessEssid != null && wirelessEssid.length() > 0) {
				if (interfaceProperties.containsKey(CFG_SCAN_SSID)) {
					/*
					 * This ESSID was user defined and might be hidden
					 */
					wirelessEssidOther = wirelessEssid;
					wirelessEssid = ESSID_SELECT_OTHER;
				} else if (!networkList.contains(wirelessEssid)) {				
					/*
					 * Make sure the network list contains the currently configured 
					 * wireless network. It might be temporaily unavailable, or the 
					 * scan may have failed to pick it up.
					 */
					networkList.add(0, wirelessEssid);
				}
			}
		}
	}	
	
	/**
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	protected void readInterfaceConfigProperties() 
			throws FileNotFoundException, IOException {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("readInterfaceConfigProperties()");
		}
		
		Util.readConfigProperties(
				new FileReader(INTERFACE_PATH_PREFIX + getInterfaceName()), 
				interfaceProperties);
		
		/*
		 * Use netmask, not prefix
		 */
		if (interfaceProperties.containsKey(CFG_PREFIX0)) {
			boolean found = false;
			String prefix = interfaceProperties.remove(CFG_PREFIX0);
			for (int i = 0; i < NETMASK_PREFIX_LIST.length; i++) {
				if (prefix.equals(NETMASK_PREFIX_LIST[i][1])) {
					interfaceProperties.put(CFG_NETMASK0, 
							NETMASK_PREFIX_LIST[i][0]);
					found = true;
					break;
				}
			}
			
			if (!found) {
				LOGGER.warn("Unable to map prefix: '" + prefix + "'!");
			}
		}
	}
	
	/**
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	protected void readKeysConfigProperties() 
			throws FileNotFoundException, IOException, InterruptedException {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("readKeysConfigProperties()");
		}

		File tmpFile = null;
		
		try {
			String interfaceName = getInterfaceName();
			tmpFile = Util.createTempFile(KEYS_PREFIX + interfaceName + "_", ".txt");
			Writer writer = new FileWriter(tmpFile);

			String[] cmdLineArgs = new String[] {
					Commands.CMD_SUDO, Commands.SCRIPT_KEYS_READ, interfaceName
			};
			
			ExecuteProcess.executeCommand(cmdLineArgs, writer, null);

			Util.readConfigProperties(new FileReader(tmpFile), keysProperties);
		} finally {
			if (tmpFile != null) {
				try {
					tmpFile.delete();
				} catch (Exception e) {}
			}
		}
		
	}
	
	/**
	 * @return
	 * @throws IOException
	 */
	protected File writeTempKeysProperties() 
			throws IOException {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("writeTempKeysProperties()");
		}

		BufferedWriter writer = null;
		try {
			String interfaceName = getInterfaceName();
			File file = Util.createTempFile(KEYS_PREFIX + interfaceName + "_", ".txt");
			writer = new BufferedWriter(new FileWriter(file));
			writer.write(Util.getModifiedComment());
			Set<Entry<String, String>> set = keysProperties.entrySet();
			Iterator<Entry<String, String>> it = set.iterator();
			while (it.hasNext()) {
				Entry<String, String> entry = it.next();
				writer.write(entry.getKey() + "=" + entry.getValue() + Util.LINE_SEP);
			}
			return file;
		} finally {
			if (writer != null) {
				try {
					writer.flush();
				} catch (Exception e) {}
				
				try {
					writer.close();
				} catch (Exception e) {}
			}
		}
	}
	
	/**
	 * @return
	 * @throws IOException
	 */
	protected File writeTempInterfaceProperties() 
			throws IOException {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("writeTempInterfaceProperties()");
		}

		BufferedWriter writer = null;
		try {
			File file = Util.createTempFile(IFCFG_PREFIX + getInterfaceName() + "_", ".txt");
			writer = new BufferedWriter(new FileWriter(file));
			writer.write(Util.getModifiedComment());
			/*
			 * If SCAN_SSID make sure it is first parameter written.
			 * Forum post by Pascal Hibon: it needs to be before ESSID.  
			 */
			boolean wroteScanSSID = false;
			if (interfaceProperties.containsKey(CFG_SCAN_SSID)) {
				wroteScanSSID = true;
				writer.write(CFG_SCAN_SSID + "=" + 
						interfaceProperties.get(CFG_SCAN_SSID) + Util.LINE_SEP);
			}
			Set<Entry<String, String>> set = interfaceProperties.entrySet();
			Iterator<Entry<String, String>> it = set.iterator();
			while (it.hasNext()) {
				Entry<String, String> entry = it.next();
				String key = entry.getKey();
				if (wroteScanSSID && key.equals(CFG_SCAN_SSID)) {
					/*
					 * Do nothing! 
					 * We already wrote SCAN_SSID.
					 */
				} else {
					writer.write(key + "=" + entry.getValue() + Util.LINE_SEP);
				}
			}
			return file;
		} finally {
			if (writer != null) {
				try {
					writer.flush();
				} catch (Exception e) {}
				
				try {
					writer.close();
				} catch (Exception e) {}
			}
		}
	}
	
	/**
	 * @param tmpFile
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	protected int replaceInterfaceConfig(File tmpFile)
			throws IOException, InterruptedException {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("replaceInterfaceConfig(tmpFile=" + tmpFile + ")");
		}

		String[] cmdLineArgs = new String[] {
				Commands.CMD_SUDO, Commands.SCRIPT_INTERFACE_UPDATE, 
				tmpFile.getAbsolutePath(), getInterfaceName()
		};
		
		return ExecuteProcess.executeCommand(cmdLineArgs);
	}
	
	/**
	 * @param tmpFile
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	protected int replaceKeysConfig(File tmpFile)
			throws IOException, InterruptedException {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("replaceKeysConfig(tmpFile=" + tmpFile + ")");
		}

		String[] cmdLineArgs = new String[] {
				Commands.CMD_SUDO, Commands.SCRIPT_KEYS_UPDATE, 
				tmpFile.getAbsolutePath(), getInterfaceName()
		};
		
		return ExecuteProcess.executeCommand(cmdLineArgs);
	}
	
	/**
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private int[] interfaceDownUp() 
			throws IOException, InterruptedException {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("interfaceDownUp()");
		}

		return new int[] {
				interfaceDown(), 
				interfaceUp()
		};
	}
	
	/**
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private int interfaceUp() 
			throws IOException, InterruptedException {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("interfaceUp()");
		}

		String[] cmdLineArgs = new String[] {
				Commands.CMD_SUDO, Commands.CMD_IFUP, getInterfaceName()
		};
		
		return ExecuteProcess.executeCommand(cmdLineArgs);
	}

	/**
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private int interfaceDown() 
			throws IOException, InterruptedException {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("interfaceDown()");
		}

		String[] cmdLineArgs = new String[] {
				Commands.CMD_SUDO, Commands.CMD_IFDOWN, getInterfaceName()
		};
		
		return ExecuteProcess.executeCommand(cmdLineArgs);
	}	
}

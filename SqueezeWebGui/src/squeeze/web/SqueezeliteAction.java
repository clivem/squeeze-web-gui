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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import squeeze.web.util.Commands;
import squeeze.web.util.ExecuteProcess;
import squeeze.web.util.Util;
import squeeze.web.util.Validate;
import squeeze.web.util.WebConfig;

/**
 * @author Clive Messer <clive.m.messer@gmail.com>
 *
 */
public class SqueezeliteAction extends SystemctlAction {

	private static final long serialVersionUID = -6984779965819629682L;
	
	private final static Logger LOGGER = Logger.getLogger(SqueezeliteAction.class);

	private final static String NAME = "squeezelite";
	private final static String SERVICE_NAME = NAME + ".service";
	private final static String SQUEEZELITE_CONFIG_PATH = "/etc/sysconfig/";
	private final static String SQUEEZELITE_CONFIG_FILE_NAME = NAME;
	
	private final static String SQUEEZELITE_SERVICE_DEFAULT_LOG_FILE = 
			"/var/log/squeezelite/squeezelite.log";
	
	private final static String SQUEEZELITE_SERVICE_DEFAULT_NAME = 
			"SqueezeliteWAND";

	private final static int SQUEEZELITE_MAX_RT_PRIORITY = 46;

	/*
	 * FSL kernel 3.0.35
	 */
	private final static String WANDBOARD_DEFAULT_AUDIO_DEVICE_3_0_35 = 
			"sgtl5000audio";
	// Force 16 bit playback. 24 bit results in distortion.
	private final static String WANDBOARD_DEFAULT_AUDIO_DEVICE_ALSA_PARAMS_3_0_35 = 
			"::16:";
	private final static String WANDBOARD_HDMI_AUDIO_DEVICE_3_0_35 = 
			"imxhdmisoc";
	// Disable MMAP on hdmi audio output.
	private final static String WANDBOARD_HDMI_AUDIO_DEVICE_ALSA_PARAMS_3_0_35 = 
			":::0";

	/*
	 * Kernel >= 3.12.6
	 */
	//private final static String WANDBOARD_DEFAULT_AUDIO_DEVICE1_3_12_6 = 
	//		"imx6wandboardsg";
	//private final static String WANDBOARD_HDMI_AUDIO_DEVICE_3_12_6 = 
	//		"";
	
	private final static String CFG_NAME = "NAME";
	private final static String CFG_NAME_OPTION = "-n ";
	private final static String CFG_MAC = "MAC";
	private final static String CFG_MAC_OPTION = "-m ";
	private final static String CFG_MAX_RATE = "MAX_RATE";
	private final static String CFG_MAX_RATE_OPTION = "-r ";
	private final static String CFG_AUDIO_DEV = "AUDIO_DEV";
	private final static String CFG_AUDIO_DEV_OPTION = "-o ";
	private final static String CFG_LOG_FILE = "LOG_FILE";
	private final static String CFG_LOG_FILE_OPTION = "-f ";
	private final static String CFG_LOG_LEVEL = "LOG_LEVEL";
	private final static String CFG_LOG_LEVEL_OPTION = "-d ";
	private final static String CFG_PRIORITY = "PRIORITY";
	private final static String CFG_PRIORITY_OPTION = "-p ";
	private final static String CFG_BUFFER = "BUFFER";
	private final static String CFG_BUFFER_OPTION = "-b ";
	private final static String CFG_CODEC = "CODEC";
	private final static String CFG_CODEC_OPTION = "-c ";
	private final static String CFG_ALSA_PARAMS = "ALSA_PARAMS";
	private final static String CFG_ALSA_PARAMS_OPTION = "-a ";
	private final static String CFG_SERVER_IP = "SERVER_IP";
	private final static String CFG_SERVER_IP_OPTION = "-s ";
	private final static String CFG_UPSAMPLE = "UPSAMPLE";
	private final static String CFG_UPSAMPLE_OPTION = "-u ";
	private final static String CFG_DOP = "DOP";
	private final static String CFG_DOP_OPTION = "-D ";
	private final static String CFG_OPTIONS = "OPTIONS";
	private final static String CFG_VISULIZER = "VISULIZER";
	private final static String CFG_VISULIZER_OPTION = "-v ";
	
	/*
	private final static String CFG_UPSAMPLE_QUALITY_QUICK = "q";
	private final static String CFG_UPSAMPLE_QUALITY_LOW = "l";
	private final static String CFG_UPSAMPLE_QUALITY_MEDIUM = "m";
	private final static String CFG_UPSAMPLE_QUALITY_HIGH = "h";
	private final static String CFG_UPSAMPLE_QUALITY_VERY_HIGH = "v";
	
	private final static String CFG_UPSAMPLE_PHASE_LINEAR = "L";
	private final static String CFG_UPSAMPLE_PHASE_MINIMUM = "M";
	private final static String CFG_UPSAMPLE_PHASE_INTERMEDIATE = "I";
	
	private final static String CFG_UPSAMPLE_FILTER_STEEP = "s";
	*/
	
	private final static List<String> PRIORITY_LIST = 
			Util.generatePriorityList(SQUEEZELITE_MAX_RT_PRIORITY);
	
	/*
	 * Store the non commented <name>="<value>" config params in a map
	 */
	protected HashMap<String, String> properties = new HashMap<String, String>();

	protected String name;
	protected String mac;
	protected String maxRate;
	protected String audioDev;
	protected String logFile;
	protected String logLevel;
	protected String priority;
	protected String buffer;
	protected String codec;
	protected String alsaParams;
	protected String serverIp;
	
	protected List<String> priorityList = PRIORITY_LIST;
	protected List<String> audioDevList;
	
	protected boolean defaultMac = false;
	protected boolean upsample = false;
	protected String upsampleOptions;
	
	protected boolean dop = false;
	protected String dopOptions;
	
	protected String options;
	
	protected boolean visulizer = false;

	/**
	 * 
	 */
	public SqueezeliteAction() {
		
		super();
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("SqueezeliteAction()");
		}
	}

	/* (non-Javadoc)
	 * @see squeeze.web.SystemctlAction#getServiceName()
	 */
	@Override
	public String getServiceName() {
		
		return SERVICE_NAME;
	}
	
	/**
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private void populatePropertiesFromConfigFile() 
			throws IOException, FileNotFoundException {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("populatePropertiesFromConfigFile()");
		}

		readSqueezeliteConfigProperties(SQUEEZELITE_CONFIG_FILE_NAME);
		
		name = properties.get(CFG_NAME);
		mac = properties.get(CFG_MAC);
		maxRate = properties.get(CFG_MAX_RATE);
		audioDev = properties.get(CFG_AUDIO_DEV);
		/*
		 * If AUDIO_DEV is set in config file, make sure it is in the list. 
		 * Might not be if USB DAC is being used but currently disconnected.
		 */
		if (audioDev != null && audioDev.length() > 0 && 
				!audioDevList.contains(audioDev)) {
			
			audioDevList.add(0, audioDev);
		}
		logFile = properties.get(CFG_LOG_FILE);
		logLevel = properties.get(CFG_LOG_LEVEL);
		priority = properties.get(CFG_PRIORITY);
		buffer = properties.get(CFG_BUFFER);
		codec = properties.get(CFG_CODEC);
		alsaParams = properties.get(CFG_ALSA_PARAMS);
		serverIp = properties.get(CFG_SERVER_IP);
		upsampleOptions = properties.get(CFG_UPSAMPLE);
		if (upsampleOptions != null) {
			upsample = true;
		}
		dopOptions = properties.get(CFG_DOP);
		if (dopOptions != null) {
			dop = true;
		}
		options = properties.get(CFG_OPTIONS);
		visulizer = (properties.get(CFG_VISULIZER) != null);
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
			/*
			 * get the list of audio devices
			 */
			audioDevList = Util.getAudioDevList();
			/*
			 * populate the editable properties
			 */
			populatePropertiesFromConfigFile();
			/*
			 * populate the service status
			 */
			populateServiceStatus();
		} catch (Exception e) {
			LOGGER.error("Caught exception populating " + getServiceName() + "!", e);
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

		ArrayList<String> list = new ArrayList<String>();
		
		/*
		 * -n <name>
		 * Set the player name
		 */
		if (name != null && name.trim().length() > 0) {
			list.add(CFG_NAME + "=\"" + CFG_NAME_OPTION + name.trim() + "\"");
		} else {
			list.add(CFG_NAME + "=\"" + CFG_NAME_OPTION + 
						SQUEEZELITE_SERVICE_DEFAULT_NAME + "\"");
		}

		/*
		 * If mac has not populated by the user and default mac cb is populated, 
		 * get the mac of the default wired network interface
		 */
		if ((mac == null || mac.trim().length() < Validate.MAC_STRING_LENGTH) && defaultMac) {
			String tmpMac = Util.getMacAddress(WebConfig.getWiredInterfaceName());
			if (tmpMac != null && tmpMac.matches(Validate.REGEX_MAC_ADDRESS)) {
				mac = tmpMac;
			}
		} 
		
		/*
		 * -m <mac addr>
		 * Set mac address, format: ab:cd:ef:12:34:56
		 */
		if (mac != null && mac.trim().length() == Validate.MAC_STRING_LENGTH) {
			list.add(CFG_MAC + "=\"" + CFG_MAC_OPTION + mac.trim() + "\"");
		}
		
		/*
		 * -r <rate>
		 * Max sample rate for output device, enables output device to be off 
		 * when squeezelite is started
		 */
		if (maxRate != null && maxRate.trim().length() > 0) {
			list.add(CFG_MAX_RATE + "=\"" + CFG_MAX_RATE_OPTION + maxRate.trim() + "\"");
		}
		
		String tmpAlsaParams = null;
		/*
		 * -o <output device>
		 * Specify output device, default "default" 
		 */
		if (audioDev != null && audioDev.trim().length() > 0) {
			list.add(CFG_AUDIO_DEV + "=\"" + CFG_AUDIO_DEV_OPTION + audioDev.trim() + "\"");
			/*
			 * If the user chooses sgtl5000audio or imxhdmisoc device, but doesn't set alsaParams, 
			 * use the defaults to works around issues....
			 */
			if (audioDev.contains(WANDBOARD_DEFAULT_AUDIO_DEVICE_3_0_35)) {
				tmpAlsaParams = WANDBOARD_DEFAULT_AUDIO_DEVICE_ALSA_PARAMS_3_0_35;
			} else if (audioDev.contains(WANDBOARD_HDMI_AUDIO_DEVICE_3_0_35)) {
				tmpAlsaParams = WANDBOARD_HDMI_AUDIO_DEVICE_ALSA_PARAMS_3_0_35;
			}
		}
		
		/*
		 * -f <logfile>
		 * Write debug to logfile
		 */
		if (logFile != null && logFile.trim().length() > 0) {
			list.add(CFG_LOG_FILE + "=\"" + CFG_LOG_FILE_OPTION + logFile.trim() + "\"");
		} else {
			list.add(CFG_LOG_FILE + "=\"" + CFG_LOG_FILE_OPTION + 
						SQUEEZELITE_SERVICE_DEFAULT_LOG_FILE + "\"");
		}
		
		/*
		 * -d <log>=<level>	
		 * Set logging level, 
		 * logs: all|slimproto|stream|decode|output, 
		 * level: info|debug|sdebug
		 */
		if (logLevel != null && logLevel.trim().length() > 0) {
			/*
			 * might be multiple args, separated by spaces
			 */
			String[] levelList = logLevel.trim().split(" ");
			if (levelList.length > 0) {
				String temp = CFG_LOG_LEVEL + "=\"";
				for (int i = 0; i < levelList.length; i++) {
					temp += CFG_LOG_LEVEL_OPTION + levelList[i].trim();
					if (i + 1 < levelList.length) {
						temp += " ";
					}
				}
				temp += "\"";
				list.add(temp);
			}
		}
		
		/*
		 * -c <codec1>,<codec2>
		 * Restrict codecs those specified, otherwise loads all available codecs; 
		 * known codecs: flac,pcm,mp3,ogg,aac (mad,mpg for specific mp3 codec)
		 */
		if (codec != null && codec.trim().length() > 0) {
			list.add(CFG_CODEC + "=\"" + CFG_CODEC_OPTION + codec.trim() + "\"");
		}
		
		/*
		 * -p <priority>
		 * Set real time priority of output thread (1-99)
		 */
		if (priority != null && priority.trim().length() > 0) {
			list.add(CFG_PRIORITY + "=\"" + CFG_PRIORITY_OPTION + priority.trim() + "\"");
		}
		
		/*
		 * -b <stream>:<output>
		 * Specify internal Stream and Output buffer sizes in Kbytes
		 */
		if (buffer != null && buffer.trim().length() > 0) {
			list.add(CFG_BUFFER + "=\"" + CFG_BUFFER_OPTION + buffer.trim() + "\"");
		}
		
		/*
		 * -a <b>:<c>:<f>:<m>
		 * Specify ALSA params to open output device, 
		 * b = buffer time in ms, 
		 * c = period count, 
		 * f sample format (16|24|24_3|32), 
		 * m = use mmap (0|1)
		 */
		if (alsaParams != null && alsaParams.trim().length() > 0) {
			list.add(CFG_ALSA_PARAMS + "=\"" + CFG_ALSA_PARAMS_OPTION + alsaParams.trim() + "\"");
		} else if (tmpAlsaParams != null && tmpAlsaParams.trim().length() > 0) {
			list.add(CFG_ALSA_PARAMS + "=\"" + CFG_ALSA_PARAMS_OPTION + tmpAlsaParams.trim() + "\"");
		}
		
		if (serverIp != null && serverIp.trim().length() > 0) {
			list.add(CFG_SERVER_IP + "=\"" + CFG_SERVER_IP_OPTION + serverIp.trim() + "\"");
		}
		
		if (upsample) {
			list.add(CFG_UPSAMPLE + "=\"" + 
						((upsampleOptions != null && upsampleOptions.trim().length() > 0) ? 
								(CFG_UPSAMPLE_OPTION + upsampleOptions.trim()) : CFG_UPSAMPLE_OPTION.trim()) + 
						"\"");
		}
		
		if (dop) {
			list.add(CFG_DOP + "=\"" +  
						((dopOptions != null && dopOptions.trim().length() > 0) ? 
								(CFG_DOP_OPTION + dopOptions.trim()) : CFG_DOP_OPTION.trim()) + 
						"\"");
		}
		
		if (options != null && options.trim().length() > 0) {
			list.add(CFG_OPTIONS + "=\"" + options.trim() + "\"");
		}
		
		if (visulizer) {
			list.add(CFG_VISULIZER + "=\"" + CFG_VISULIZER_OPTION.trim() + "\"");
		}
		
		File file = null;
		try {
			file = writeTempSqueezeliteProperties(list);
			replaceSqueezeliteConfig(file);
		} catch (Exception e) {
			LOGGER.error("Caught exception saving " + getServiceName() + "!", e);
			throw e;
		} finally {
			if (file != null) {
				try {
					file.delete();
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
	public String getMac() {
		
		return mac;
	}
	
	/**
	 * @param mac
	 */
	public void setMac(String mac) {
		
		this.mac = mac;
	}
	
	/**
	 * @return
	 */
	public String getMaxRate() {
		
		return maxRate;
	}
	
	/**
	 * @param maxRate
	 */
	public void setMaxRate(String maxRate) {
		
		this.maxRate = maxRate;
	}
	
	/**
	 * @return
	 */
	public String getAudioDev() {
		
		return audioDev;
	}
	
	/**
	 * @param audioDev
	 */
	public void setAudioDev(String audioDev) {
		
		this.audioDev = audioDev;
	}
	
	/**
	 * @return
	 */
	public String getLogFile() {
		
		return logFile;
	}
	
	/**
	 * @param logFile
	 */
	public void setLogFile(String logFile) {
		
		this.logFile = logFile;
	}
	
	/**
	 * @return
	 */
	public String getLogLevel() {
		
		return logLevel;
	}
	
	/**
	 * @param logLevel
	 */
	public void setLogLevel(String logLevel) {
		
		this.logLevel = logLevel;
	}
	
	/**
	 * @return
	 */
	public String getPriority() {
		
		return priority;
	}
	
	/**
	 * @param priority
	 */
	public void setPriority(String priority) {
		
		this.priority = priority;
	}
	
	/**
	 * @return
	 */
	public String getBuffer() {
		
		return buffer;
	}
	
	/**
	 * @param buffer
	 */
	public void setBuffer(String buffer) {
		
		this.buffer = buffer;
	}
	
	/**
	 * @return
	 */
	public String getCodec() {
		
		return codec;
	}
	
	/**
	 * @param codec
	 */
	public void setCodec(String codec) {
		
		this.codec = codec;
	}
	
	/**
	 * @return
	 */
	public String getAlsaParams() {
		
		return alsaParams;
	}
	
	/**
	 * @param alsaParams
	 */
	public void setAlsaParams(String alsaParams) {
		
		this.alsaParams = alsaParams;
	}
	
	/**
	 * @return
	 */
	public String getServerIp() {
		
		return serverIp;
	}
	
	/**
	 * @param serverIp
	 */
	public void setServerIp(String serverIp) {
		
		this.serverIp = serverIp;
	}

	/**
	 * @return
	 */
	public List<String> getPriorityList() {
		
		return priorityList;
	}

	/**
	 * @return
	 */
	public List<String> getAudioDevList() {
		
		return audioDevList;
	}

	/**
	 * @param audioDevList
	 */
	public void setAudioDevList(List<String> audioDevList) {
		
		this.audioDevList = audioDevList;
	}
	
	/**
	 * @return the defaultMac
	 */
	public boolean isDefaultMac() {
		return defaultMac;
	}

	/**
	 * @param defaultMac the defaultMac to set
	 */
	public void setDefaultMac(boolean defaultMac) {
		this.defaultMac = defaultMac;
	}

	/**
	 * @return the upsample
	 */
	public boolean isUpsample() {
		return upsample;
	}

	/**
	 * @param upsample the upsample to set
	 */
	public void setUpsample(boolean upsample) {
		this.upsample = upsample;
	}

	/**
	 * @return the upsampleOptions
	 */
	public String getUpsampleOptions() {
		return upsampleOptions;
	}

	/**
	 * @param upsampleOptions the upsampleOptions to set
	 */
	public void setUpsampleOptions(String upsampleOptions) {
		this.upsampleOptions = upsampleOptions;
	}

	/**
	 * @return the dop
	 */
	public boolean isDop() {
		return dop;
	}

	/**
	 * @param dop the dop to set
	 */
	public void setDop(boolean dop) {
		this.dop = dop;
	}

	/**
	 * @return the dopOptions
	 */
	public String getDopOptions() {
		return dopOptions;
	}

	/**
	 * @param dopOptions the dopOptions to set
	 */
	public void setDopOptions(String dopOptions) {
		this.dopOptions = dopOptions;
	}

	/**
	 * @return the options
	 */
	public String getOptions() {
		return options;
	}

	/**
	 * @param options the options to set
	 */
	public void setOptions(String options) {
		this.options = options;
	}

	/**
	 * @return the visulizer
	 */
	public boolean isVisulizer() {
		return visulizer;
	}

	/**
	 * @param visulizer the visulizer to set
	 */
	public void setVisulizer(boolean visulizer) {
		this.visulizer = visulizer;
	}

	/**
	 * @param configName
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void readSqueezeliteConfigProperties(String configName) 
			throws FileNotFoundException, IOException {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("readSqueezeliteConfigProperties(configName=" + 
							configName + ")");
		}

		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(SQUEEZELITE_CONFIG_PATH + configName));
			String line = null;
			while ((line = br.readLine()) != null) {
				// Remove any leading or trailing white space
				line = line.trim();
				if (!line.startsWith("#")) {
					if (LOGGER.isTraceEnabled()) {
						LOGGER.trace(line);
					}
					int index = line.indexOf('=');
					if (index > -1) {
						/*
						 * split at the first occurance of '='
						 */
						String name = line.substring(0, index);
						/*
						 * remove the '='
						 */
						String value = line.substring(index + 1);

						/*
						 * remove the '"' at the beginning of value string
						 */
						if (value.startsWith("\"")) {
							value = value.substring(1);
						}
						/*
						 * remove the '"' at end of value string
						 */
						if (value.endsWith("\"")) {
							value = value.substring(0, value.length() - 1);
						}
						
						/*
						 * we don't use an arg flag for the serverIp
						 */
						if ((name.equals(CFG_SERVER_IP) && !value.startsWith(CFG_SERVER_IP_OPTION)) || 
								name.equals(CFG_OPTIONS)) {
							properties.put(name, value);
							if (LOGGER.isTraceEnabled()) {
								LOGGER.trace("Name='" + name + "', Value='" + value + "'");
							}
						} else {
							/*
							 * Remove the arg flag
							 */
							String[] splitOption = value.split(" ");
							if (splitOption != null && splitOption.length == 2) {
								String temp = splitOption[1].trim();
								properties.put(name, temp);
								if (LOGGER.isTraceEnabled()) {
									LOGGER.trace("Name='" + name + "', Value='" + temp + "'");
								}
							} else if (name.equals(CFG_VISULIZER)) {
								if (splitOption.length == 1 && splitOption[0].equals(CFG_VISULIZER_OPTION.trim())) {
									properties.put(name, "");
									if (LOGGER.isTraceEnabled()) {
										LOGGER.trace("Name='" + name + "', Value='" + "" + "'");
									}									
								}
							} else if (name.equals(CFG_UPSAMPLE)) {
								if (splitOption.length == 1 && splitOption[0].equals(CFG_UPSAMPLE_OPTION.trim())) {
									properties.put(name, "");
									if (LOGGER.isTraceEnabled()) {
										LOGGER.trace("Name='" + name + "', Value='" + "" + "'");
									}									
								}
							} else if (name.equals(CFG_DOP)) {
								if (splitOption.length == 1 && splitOption[0].equals(CFG_DOP_OPTION.trim())) {
									properties.put(name, "");
									if (LOGGER.isTraceEnabled()) {
										LOGGER.trace("Name='" + name + "', Value='" + "" + "'");
									}									
								}
							} else if (name.equals(CFG_LOG_LEVEL)) {
								String tmp = "";
								int optionCount = splitOption.length / 2;
								for (int i = 0; i < optionCount; i++) {
									tmp += splitOption[(i * 2) + 1].trim();
									if (i + 1 < optionCount) {
										tmp += " ";
									}
								}
								properties.put(name, tmp);
								if (LOGGER.isTraceEnabled()) {
									LOGGER.trace("Name='" + name + "', Value='" + tmp + "'");
								}									
							}
						}
					} else {
						if (LOGGER.isTraceEnabled()) {
							LOGGER.warn("Ignoring line that does not contain 'name=value': " + line);
						}
					}
				} else {
					if (LOGGER.isTraceEnabled()) {
						LOGGER.trace("Ignoring commented line: " + line);
					}
				}
			}
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (Exception e) {}
			}
		}
	}
	
	/**
	 * @param configName
	 * @param argList
	 * @return
	 * @throws IOException
	 */
	private File writeTempSqueezeliteProperties(ArrayList<String> argList) 
			throws IOException {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("writeTempSqueezeliteProperties(argList=" + argList + ")");
		}

		BufferedWriter bw = null;
		try {
			File tempFile = Util.createTempFile(NAME + "_config_", ".txt");
			bw = new BufferedWriter(new FileWriter(tempFile));
			bw.write(Util.getModifiedComment());
			Iterator<String> it = argList.iterator();
			while (it.hasNext()) {
				bw.write(it.next() + Util.LINE_SEP);
			}
			return tempFile;
		} finally {
			if (bw != null) {
				try {
					bw.flush();
				} catch (Exception e) {}
				
				try {
					bw.close();
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
	private int replaceSqueezeliteConfig(File tmpFile)
			throws IOException, InterruptedException {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("replaceSqueezeliteConfig(tmpFile=" + tmpFile + ")");
		}

		String[] cmdLineArgs = new String[] {
				Commands.CMD_SUDO, Commands.SCRIPT_SQUEEZELITE_CONFIG_UPDATE, 
				tmpFile.getAbsolutePath()
		};
		
		return ExecuteProcess.executeCommand(cmdLineArgs);
	}
}

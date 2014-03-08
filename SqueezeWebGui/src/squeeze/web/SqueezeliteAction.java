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
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import squeeze.web.util.Commands;
import squeeze.web.util.NameFlag;
import squeeze.web.util.SoxResample;
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
	private final static String CFG_RESAMPLE = "UPSAMPLE";
	private final static String CFG_RESAMPLE_OPTION = "-u ";
	private final static String CFG_DOP = "DOP";
	private final static String CFG_DOP_OPTION = "-D ";
	private final static String CFG_OPTIONS = "OPTIONS";
	private final static String CFG_VISULIZER = "VISULIZER";
	private final static String CFG_VISULIZER_OPTION = "-v ";
	
	private final static List<String> PRIORITY_LIST = 
			Util.generatePriorityList(SQUEEZELITE_MAX_RT_PRIORITY);
	
	/*
	 * Store the non commented <name>="<value>" config params in a map
	 */
	protected HashMap<String, String> properties = new HashMap<String, String>();

	protected String name = null;
	protected String mac = null;
	protected String maxRate = null;
	protected String audioDev = null;
	protected String logFile = null;
	protected String logLevel = null;
	protected String priority = null;
	
	protected String bufferStream = null;
	protected String bufferOutput = null;
	
	protected String codec = null;
	
	protected String alsaParamsBuffer = null;
	protected String alsaParamsPeriod = null;
	protected String alsaParamsFormat = null;
	protected String alsaParamsMmap = null;
	
	protected String serverIp = null;
	
	protected List<String> priorityList = PRIORITY_LIST;
	protected List<String> audioDevList;
	
	protected boolean defaultMac = false;
	
	protected boolean resample = false;
	
	protected String resampleRecipeFilter = null;
	protected String resampleRecipeQuality = null;
	protected boolean resampleRecipeSteep = false;
	protected boolean resampleRecipeException = false;
	protected boolean resampleRecipeAsync = false;
	protected String resampleFlags = null;
	protected String resampleAttenuation = null;
	protected String resamplePrecision = null;
	protected String resamplePassbandEnd = null;
	protected String resampleStopbandStart = null;
	protected String resamplePhaseResponse = null;
	
	protected boolean dop = false;
	protected String dopOptions = null;
	
	protected String options = null;
	
	protected boolean visulizer = false;
	
	protected boolean showAdvancedOptions = false;
	
	protected String log = null;

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
		
		String buffer = properties.get(CFG_BUFFER);
		parseBuffer(buffer);
		
		codec = properties.get(CFG_CODEC);
		
		String alsaParams = properties.get(CFG_ALSA_PARAMS);
		if (alsaParams != null) {
			parseAlsaParams(alsaParams);
		}
		
		serverIp = properties.get(CFG_SERVER_IP);
		String resampleOptions = properties.get(CFG_RESAMPLE);
		if (resampleOptions != null) {
			resample = true;
			parseResampleOptions(resampleOptions);
		}
		
		dopOptions = properties.get(CFG_DOP);
		if (dopOptions != null) {
			dop = true;
		}
		
		options = properties.get(CFG_OPTIONS);
		
		visulizer = (properties.get(CFG_VISULIZER) != null);
	}
	
	/**
	 * @param buffer
	 */
	private void parseBuffer(String buffer) {
		
		if (buffer != null && buffer.trim().length() > 0) {

			int count = 0;

			StringTokenizer tok = new StringTokenizer(buffer, Util.COLON, true);
			while (tok.hasMoreTokens()) {
				String option = tok.nextToken().trim();
				if (option.charAt(0) == Util.COLON.charAt(0)) {
					count++;
				} else {
					switch (count) {
						// stream
						case 0:
							bufferStream = option;
							break;
						// output
						case 1:
							bufferOutput = option;
							break;
						default:
							break;
					}
				}
			}
		}
	}
	
	/**
	 * @param alsaParams
	 */
	private void parseAlsaParams(String alsaParams) {
		
		if (alsaParams != null && alsaParams.trim().length() > 0) {

			int count = 0;

			StringTokenizer tok = new StringTokenizer(alsaParams, Util.COLON, true);
			while (tok.hasMoreTokens()) {
				String option = tok.nextToken().trim();
				if (option.charAt(0) == Util.COLON.charAt(0)) {
					count++;
				} else {
					switch (count) {
						// buffer
						case 0:
							alsaParamsBuffer = option;
							break;
						// period
						case 1:
							alsaParamsPeriod = option;
							break;
						// format
						case 2:
							alsaParamsFormat = option;
							break;
						// mmap
						case 3:
							if (Util.ONE.equals(option)) {
								alsaParamsMmap = Util.ONE;
							} else if (Util.ZERO.equals(option)) {
								alsaParamsMmap = Util.ZERO;
							}
							break;
						default:
							break;
					}
				}
			}
		}
	}
	
	/**
	 * @param resampleOptions
	 */
	private void parseResampleOptions(String resampleOptions) {
		
		if (resampleOptions != null && resampleOptions.trim().length() > 0) {
		
			int count = 0;
	
			StringTokenizer tok = new StringTokenizer(resampleOptions, Util.COLON, true);
			while (tok.hasMoreTokens()) {
				String option = tok.nextToken().trim();
				if (option.charAt(0) == Util.COLON.charAt(0)) {
					count++;
				} else {
					switch (count) {
						// recipe
						case 0:
							// quality
							if (option.indexOf(SoxResample.SOXR_QQ_FLAG) > -1) {
								resampleRecipeQuality = "" + SoxResample.SOXR_QQ_FLAG;
							} else if (option.indexOf(SoxResample.SOXR_LQ_FLAG) > -1) {
								resampleRecipeQuality = "" + SoxResample.SOXR_LQ_FLAG;
							} else if (option.indexOf(SoxResample.SOXR_MQ_FLAG) > -1) {
								resampleRecipeQuality = "" + SoxResample.SOXR_MQ_FLAG;
							} else if (option.indexOf(SoxResample.SOXR_HQ_FLAG) > -1) {
								resampleRecipeQuality = "" + SoxResample.SOXR_HQ_FLAG;
							} else if (option.indexOf(SoxResample.SOXR_VHQ_FLAG) > -1) {
								resampleRecipeQuality = "" + SoxResample.SOXR_VHQ_FLAG;
							}
							// filter
							if (option.indexOf(SoxResample.SOXR_LINEAR_PHASE_FLAG) > -1) {
								resampleRecipeFilter = "" + SoxResample.SOXR_LINEAR_PHASE_FLAG;
							} else if (option.indexOf(SoxResample.SOXR_INTERMEDIATE_PHASE_FLAG) > -1) {
								resampleRecipeFilter = "" + SoxResample.SOXR_INTERMEDIATE_PHASE_FLAG;
							} else if (option.indexOf(SoxResample.SOXR_MINIMUM_PHASE_FLAG) > -1) {
								resampleRecipeFilter = "" + SoxResample.SOXR_MINIMUM_PHASE_FLAG;
							}
							// steep
							if (option.indexOf(SoxResample.SOXR_STEEP_FILTER_FLAG) > -1) {
								resampleRecipeSteep = true;
							}
							// exception
							if (option.indexOf(SoxResample.EXCEPTION_RATE_FLAG) > -1) {
								resampleRecipeException = true;
							}
							// async
							if (option.indexOf(SoxResample.ASYNC_RATE_FLAG) > -1) {
								resampleRecipeAsync = true;
							}
							break;
						// flags
						case 1:
							resampleFlags = option;
							break;
						// attenuation
						case 2:
							resampleAttenuation = option;
							break;
						// precision
						case 3:
							resamplePrecision = option;
							break;
						// passband end
						case 4:
							resamplePassbandEnd = option;
							break;
						// stopband start
						case 5:
							resampleStopbandStart = option;
							break;
						// phase response
						case 6:
							resamplePhaseResponse = option;
							break;
						default:
							break;
					}
				}
			}
		}
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
			/*
			 * tail the log file
			 */
			populateLog();
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
	 * 
	 */
	protected void populateLog() {
		
		if (logFile != null && logFile.trim().length() > 0) {
			try {
			log = Util.tail(new File(logFile));
			} catch (Exception e) {
				LOGGER.warn("Caught exception trying to read the log file!", e);
			}
		}		
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
		String buffer = "";
		if (bufferStream != null && bufferStream.trim().length() > 0) {
			buffer += bufferStream.trim();
		}
		
		if (bufferOutput != null && bufferOutput.trim().length() > 0) {
			buffer += Util.COLON + bufferOutput.trim();
		}
				
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
		String alsaParams = ((alsaParamsBuffer != null) ? alsaParamsBuffer.trim() : "") + Util.COLON +
				((alsaParamsPeriod != null) ? alsaParamsPeriod.trim() : "") + Util.COLON + 
				((alsaParamsFormat != null) ? alsaParamsFormat.trim() : "") + Util.COLON +
				((alsaParamsMmap != null) ? alsaParamsMmap.trim() : "");
		
		// trim trailing colons
		alsaParams = Util.trimTrailing(alsaParams, Util.COLON);

		if (alsaParams != null && alsaParams.trim().length() > 0) {
			list.add(CFG_ALSA_PARAMS + "=\"" + CFG_ALSA_PARAMS_OPTION + alsaParams.trim() + "\"");
		} else if (tmpAlsaParams != null && tmpAlsaParams.trim().length() > 0) {
			list.add(CFG_ALSA_PARAMS + "=\"" + CFG_ALSA_PARAMS_OPTION + tmpAlsaParams.trim() + "\"");
		}
		
		if (serverIp != null && serverIp.trim().length() > 0) {
			list.add(CFG_SERVER_IP + "=\"" + CFG_SERVER_IP_OPTION + serverIp.trim() + "\"");
		}
		
		if (resample) {
			String resampleRecipe = ((resampleRecipeQuality != null) ? resampleRecipeQuality : "") +
					((resampleRecipeFilter != null) ? resampleRecipeFilter : "") +
					((resampleRecipeSteep) ? SoxResample.SOXR_STEEP_FILTER_FLAG : "") +
					((resampleRecipeException) ? SoxResample.EXCEPTION_RATE_FLAG : "") +
					((resampleRecipeAsync) ? SoxResample.ASYNC_RATE_FLAG : "");
			
			String resampleOptions = resampleRecipe + Util.COLON +
					(resampleFlags != null ? resampleFlags.trim() : Util.BLANK_STRING) + Util.COLON +
					(resampleAttenuation != null ? resampleAttenuation.trim() : Util.BLANK_STRING) + Util.COLON +
					(resamplePrecision != null ? resamplePrecision.trim() : Util.BLANK_STRING) + Util.COLON +
					(resamplePassbandEnd != null ? resamplePassbandEnd.trim() : Util.BLANK_STRING) + Util.COLON +
					(resampleStopbandStart != null ? resampleStopbandStart.trim() : Util.BLANK_STRING) + Util.COLON +
					(resamplePhaseResponse != null ? resamplePhaseResponse.trim() : Util.BLANK_STRING);
			
			// trim trailing colons
			resampleOptions = Util.trimTrailing(resampleOptions, Util.COLON);

			list.add(CFG_RESAMPLE + "=\"" + 
					((resampleOptions.length() > 0) ? 
							(CFG_RESAMPLE_OPTION + resampleOptions) : CFG_RESAMPLE_OPTION.trim()) + 
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
			file = writeTempSqueezeliteProperties(NAME, list);
			Util.replaceConfig(file, Commands.SCRIPT_SQUEEZELITE_CONFIG_UPDATE);
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
						 * trim Quotes from beginning and end of string
						 */
						value = Util.trimQuotes(value);
						
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
							} else if (name.equals(CFG_RESAMPLE)) {
								if (splitOption.length == 1 && splitOption[0].equals(CFG_RESAMPLE_OPTION.trim())) {
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
	private File writeTempSqueezeliteProperties(String configName, List<String> argList) 
			throws IOException {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("writeTempSqueezeliteProperties(argList=" + argList + ")");
		}

		BufferedWriter bw = null;
		try {
			File tempFile = Util.createTempFile(configName + "_config_", ".txt");
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
	 * @param priorityList the priorityList to set
	 */
	public void setPriorityList(List<String> priorityList) {
		
		this.priorityList = priorityList;
	}

	/**
	 * @return
	 */
	public List<String> getAudioDevList() {
		
		return audioDevList;
	}

	/**
	 * @param audioDevList the audioDevList to set
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
	 * @return the resample
	 */
	public boolean isResample() {
		
		return resample;
	}

	/**
	 * @param resample the resample to set
	 */
	public void setResample(boolean resample) {
		
		this.resample = resample;
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
	 * @return the showAdvancedOptions
	 */
	public boolean isShowAdvancedOptions() {
		
		return showAdvancedOptions;
	}

	/**
	 * @param showAdvancedOptions the showAdvancedOptions to set
	 */
	public void setShowAdvancedOptions(boolean showAdvancedOptions) {
		
		this.showAdvancedOptions = showAdvancedOptions;
	}

	/**
	 * @return the resampleFlags
	 */
	public String getResampleFlags() {
		
		return resampleFlags;
	}

	/**
	 * @param resampleFlags the resampleFlags to set
	 */
	public void setResampleFlags(String resampleFlags) {
		
		this.resampleFlags = resampleFlags;
	}

	/**
	 * @return the resampleAttenuation
	 */
	public String getResampleAttenuation() {
		
		return resampleAttenuation;
	}

	/**
	 * @param resampleAttenuation the resampleAttenuation to set
	 */
	public void setResampleAttenuation(String resampleAttenuation) {
		
		this.resampleAttenuation = resampleAttenuation;
	}

	/**
	 * @return the resamplePrecision
	 */
	public String getResamplePrecision() {
		
		return resamplePrecision;
	}

	/**
	 * @param resamplePrecision the resamplePrecision to set
	 */
	public void setResamplePrecision(String resamplePrecision) {
		
		this.resamplePrecision = resamplePrecision;
	}

	/**
	 * @return the resamplePassbandEnd
	 */
	public String getResamplePassbandEnd() {
		
		return resamplePassbandEnd;
	}

	/**
	 * @param resamplePassbandEnd the resamplePassbandEnd to set
	 */
	public void setResamplePassbandEnd(String resamplePassbandEnd) {
		
		this.resamplePassbandEnd = resamplePassbandEnd;
	}

	/**
	 * @return the resampleStopbandStart
	 */
	public String getResampleStopbandStart() {
		
		return resampleStopbandStart;
	}

	/**
	 * @param resampleStopbandStart the resampleStopbandStart to set
	 */
	public void setResampleStopbandStart(String resampleStopbandStart) {
		
		this.resampleStopbandStart = resampleStopbandStart;
	}

	/**
	 * @return the resamplePhaseResponse
	 */
	public String getResamplePhaseResponse() {
		
		return resamplePhaseResponse;
	}

	/**
	 * @param resamplePhaseResponse the resamplePhaseResponse to set
	 */
	public void setResamplePhaseResponse(String resamplePhaseResponse) {
		
		this.resamplePhaseResponse = resamplePhaseResponse;
	}
	
	/**
	 * @return the resampleRecipeFilter
	 */
	public String getResampleRecipeFilter() {
		
		return resampleRecipeFilter;
	}

	/**
	 * @param resampleRecipeFilter the resampleRecipeFilter to set
	 */
	public void setResampleRecipeFilter(String resampleRecipeFilter) {
		
		this.resampleRecipeFilter = resampleRecipeFilter;
	}

	/**
	 * @return the resampleRecipeQuality
	 */
	public String getResampleRecipeQuality() {
		
		return resampleRecipeQuality;
	}

	/**
	 * @param resampleRecipeQuality the resampleRecipeQuality to set
	 */
	public void setResampleRecipeQuality(String resampleRecipeQuality) {
		
		this.resampleRecipeQuality = resampleRecipeQuality;
	}

	/**
	 * @return the resampleRecipeSteep
	 */
	public boolean isResampleRecipeSteep() {
		
		return resampleRecipeSteep;
	}

	/**
	 * @param resampleRecipeSteep the resampleRecipeSteep to set
	 */
	public void setResampleRecipeSteep(boolean resampleRecipeSteep) {
		
		this.resampleRecipeSteep = resampleRecipeSteep;
	}

	/**
	 * @return the resampleRecipeException
	 */
	public boolean isResampleRecipeException() {
		
		return resampleRecipeException;
	}

	/**
	 * @param resampleRecipeException the resampleRecipeException to set
	 */
	public void setResampleRecipeException(boolean resampleRecipeException) {
		
		this.resampleRecipeException = resampleRecipeException;
	}

	/**
	 * @return the resampleRecipeAsync
	 */
	public boolean isResampleRecipeAsync() {
		
		return resampleRecipeAsync;
	}

	/**
	 * @param resampleRecipeAsync the resampleRecipeAsync to set
	 */
	public void setResampleRecipeAsync(boolean resampleRecipeAsync) {
		
		this.resampleRecipeAsync = resampleRecipeAsync;
	}

	/**
	 * @return the alsaParamsBuffer
	 */
	public String getAlsaParamsBuffer() {
		
		return alsaParamsBuffer;
	}

	/**
	 * @param alsaParamsBuffer the alsaParamsBuffer to set
	 */
	public void setAlsaParamsBuffer(String alsaParamsBuffer) {
		
		this.alsaParamsBuffer = alsaParamsBuffer;
	}

	/**
	 * @return the alsaParamsPeriod
	 */
	public String getAlsaParamsPeriod() {
		
		return alsaParamsPeriod;
	}

	/**
	 * @param alsaParamsPeriod the alsaParamsPeriod to set
	 */
	public void setAlsaParamsPeriod(String alsaParamsPeriod) {
		
		this.alsaParamsPeriod = alsaParamsPeriod;
	}

	/**
	 * @return the alsaParamsFormat
	 */
	public String getAlsaParamsFormat() {
		
		return alsaParamsFormat;
	}

	/**
	 * @param alsaParamsFormat the alsaParamsFormat to set
	 */
	public void setAlsaParamsFormat(String alsaParamsFormat) {
		
		this.alsaParamsFormat = alsaParamsFormat;
	}

	/**
	 * @return the alsaParamsMmap
	 */
	public String getAlsaParamsMmap() {
		
		return alsaParamsMmap;
	}

	/**
	 * @param alsaParamsMmap the alsaParamsMmap to set
	 */
	public void setAlsaParamsMmap(String alsaParamsMmap) {
		
		this.alsaParamsMmap = alsaParamsMmap;
	}

	/**
	 * @return the bufferStream
	 */
	public String getBufferStream() {
		
		return bufferStream;
	}

	/**
	 * @param bufferStream the bufferStream to set
	 */
	public void setBufferStream(String bufferStream) {
		
		this.bufferStream = bufferStream;
	}

	/**
	 * @return the bufferOutput
	 */
	public String getBufferOutput() {
		
		return bufferOutput;
	}

	/**
	 * @param bufferOutput the bufferOutput to set
	 */
	public void setBufferOutput(String bufferOutput) {
		
		this.bufferOutput = bufferOutput;
	}

	/**
	 * @return the resampleQualityList
	 */
	public List<NameFlag> getResampleQualityList() {
		
		return NameFlag.getSoxResampleQualityList();
	}

	/**
	 * @return the resampleFilterList
	 */
	public List<NameFlag> getResampleFilterList() {
		
		return NameFlag.getSoxResampleFilterList();
	}
	
	/**
	 * @return the alsaParamsFormatList
	 */
	public List<NameFlag> getAlsaParamsFormatList() {
		
		return NameFlag.getAlsaParamsFormatList();
	}	

	/**
	 * @return the alsaParamsMmapList
	 */
	public List<NameFlag> getAlsaParamsMmapList() {
		
		return NameFlag.getAlsaParamsMmapList();
	}
	
	/**
	 * @return the log
	 */
	public String getLog() {
		return log;
	}
}

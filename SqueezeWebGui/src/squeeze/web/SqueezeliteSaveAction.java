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

import squeeze.web.util.Util;
import squeeze.web.util.Validate;

/**
 * @author Clive Messer <clive.m.messer@gmail.com>
 *
 */
public class SqueezeliteSaveAction extends SqueezeliteAction {

	private static final long serialVersionUID = -6556726358112205788L;
	
	private final static Logger LOGGER = Logger.getLogger(SqueezeliteSaveAction.class);
	
	/**
	 * 
	 */
	public SqueezeliteSaveAction() {
		
		super();
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("SqueezeliteSaveAction()");
		}
	}
	
	/* (non-Javadoc)
	 * @see com.opensymphony.xwork2.ActionSupport#validate()
	 */
	public void validate() {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("validate()");
		}
		
		if (name != null && name.trim().length() > 0) {
			if (!name.trim().matches(Validate.REGEX_ALPHA_NUMERIC_UNDERSCORE_DASH)) {
				addActionError(getText("squeezelite.validation.name.fail"));
			}
		}
		
		/*
		if (mac != null && mac.trim().length() > 0) {
			if (!mac.trim().matches(Validate.REGEX_MAC_ADDRESS)) {
				addActionError(getText("squeezelite.validation.mac.fail"));
			}
		}
		*/

		if((mac1 == null || mac1.trim().length() < 1) && 
				(mac2 == null || mac2.trim().length() < 1) &&
				(mac3 == null || mac3.trim().length() < 1) &&
				(mac4 == null || mac4.trim().length() < 1) &&
				(mac5 == null || mac5.trim().length() < 1) &&
				(mac6 == null || mac6.trim().length() < 1)) {
			/*
			 * Don't set mac.
			 */
		} else {
			/* 
			 * Set mac.
			 */
			if (mac1 == null || mac1.trim().length() < 2 || mac2 == null || mac2.trim().length() < 2 || 
					mac3 == null || mac3.trim().length() < 2 || mac4 == null || mac4.trim().length() < 2 || 
					mac5 == null || mac5.trim().length() < 2 || mac6 == null || mac6.trim().length() < 2) {
				addActionError(getText("squeezelite.validation.mac.fail"));
			} else {
				String mac = mac1.trim() + Util.COLON + mac2.trim() + Util.COLON + mac3.trim() + Util.COLON + 
						mac4.trim() + Util.COLON + mac5.trim() + Util.COLON + mac6.trim();
				if (!mac.matches(Validate.REGEX_MAC_ADDRESS)) {
					addActionError(getText("squeezelite.validation.mac.fail"));
				}
			}
		}
				
		if(maxRate != null && maxRate.trim().length() > 0) {
			if (maxRate.trim().matches(Validate.SQUEEZELITE_REGEX_MAX_RATE) || 
					maxRate.trim().matches(Validate.SQUEEZELITE_REGEX_MAX_RATE_RANGE) ||
					maxRate.trim().matches(Validate.SQUEEZELITE_REGEX_MAX_RATE_TIME_BTWN_CHANGE)) {
				// Validates OK
			} else {
				addActionError(getText("squeezelite.validation.maxRate.fail"));
			}
		}
		
		/*
		if (logLevel != null && logLevel.trim().length() > 0) {
			
			String [] tempList = logLevel.trim().split(" ");
			for (int i = 0; i < tempList.length; i++) {
				
				String[] logLevelList = tempList[i].split("=");
				if (logLevelList.length != 2) {
					addActionError(getText("squeezelite.validation.logLevel.fail"));
					break;
				} else {
					String type = logLevelList[0];
					String level = logLevelList[1];
					if (!validateLogName(type)) {
						addActionError(
								getText("squeezelite.validation.logLevel.fail.invalid") + 
								" '" + type + "=" + level + "'! " +
								getText("squeezelite.validation.logLevel.fail.validLogs") + 
								": " + Util.arrayToString(Validate.SQUEEZELITE_LOG_NAMES));
						break;
					}
					if (!validateLogLevel(level)) {
						addActionError(
								getText("squeezelite.validation.logLevel.fail.invalid") + 
								" '" + type + "=" + level + "'! " + 
								getText("squeezelite.validation.logLevel.fail.validLevels") + 
								": " + Util.arrayToString(Validate.SQUEEZELITE_LOG_LEVELS));
						break;
					}
				}
			}
		}
		*/
		
		/*
		if (buffer != null && buffer.trim().length() > 0) {
			if (!buffer.trim().matches(Validate.SQUEEZELITE_REGEX_ALSA_BUFFER)) {
				addActionError(getText("squeezelite.validation.buffer.fail"));		
			}
		}
		*/
		
		if (bufferStream != null && bufferStream.trim().length() > 0) {
			try {
				Integer.parseInt(bufferStream.trim());
			} catch (NumberFormatException nfe) {
				addActionError(getText("squeezelite.validation.buffer.stream.fail"));		
			}
		}

		if (bufferOutput != null && bufferOutput.trim().length() > 0) {
			try {
				Integer.parseInt(bufferOutput.trim());
			} catch (NumberFormatException nfe) {
				addActionError(getText("squeezelite.validation.buffer.output.fail"));		
			}
		}

		/*
		if (buffer != null && buffer.trim().length() > 0) {
			String[] tempList = buffer.trim().split(":");
			if (tempList.length != 2) {
				addActionError(getText("squeezelite.validation.buffer.fail"));
			} else {
				try {
					Integer.parseInt(tempList[0]);
					Integer.parseInt(tempList[1]);
				} catch (NumberFormatException nfe) {
					addActionError(getText("squeezelite.validation.buffer.fail"));
				}
			}
		}
		*/
		
		/*
		if (codec != null && codec.trim().length() > 0) {
			String[] tempList = codec.trim().split(",");
			for (int i = 0; i < tempList.length; i++) {
				if (!validateCodec(tempList[i])) {
					addActionError(
							getText("squeezelite.validation.codec.fail.invalid") + 
							" '" + tempList[i] + "'! " + 
							getText("squeezelite.validation.codec.fail.validCodecs") + 
							": " + Util.arrayToString(Validate.SQUEEZELITE_CODECS));
					break;
				}
			}
		}
		*/
		
		/*
		if (alsaParams != null && alsaParams.trim().length() > 0) {
			if (!alsaParams.trim().matches(Validate.SQUEEZELITE_REGEX_ALSA_PARAMS)) {
				addActionError(getText("squeezelite.validation.alsaParams.fail"));		
			}
		}
		*/
		if (alsaParamsBuffer != null && alsaParamsBuffer.trim().length() > 0) {
			try {
				Integer.parseInt(alsaParamsBuffer.trim());
			} catch (NumberFormatException nfe) {
				addActionError(getText("squeezelite.validation.alsaParams.buffer.fail"));		
			}
		}
		
		if (alsaParamsPeriod != null && alsaParamsPeriod.trim().length() > 0) {
			try {
				Integer.parseInt(alsaParamsPeriod.trim());
			} catch (NumberFormatException nfe) {
				addActionError(getText("squeezelite.validation.alsaParams.period.fail"));		
			}
		}
		
		/*
		 * Don't validate! We need to allow host names as well as IP's.
		 * 
		if (serverIp != null && serverIp.trim().length() > 0) {
			if (!serverIp.trim().matches(Validate.REGEX_IP_ADDRESS)) {
				addActionError(getText("squeezelite.validation.serverIp.fail"));
			}
		}
		*/
		
		if (dop && dopOptions != null && dopOptions.trim().length() > 0) {
			try {
				Integer.parseInt(dopOptions.trim());
			} catch (NumberFormatException nfe) { 
				addActionError(getText("squeezelite.validation.dopOptions.fail"));
			}
		}
		
		if (audioDevIdle != null && audioDevIdle.trim().length() > 0) {
			try {
				Integer.parseInt(audioDevIdle.trim());
			} catch (NumberFormatException nfe) { 
				addActionError(getText("squeezelite.validation.audioDevIdle.fail"));
			}
		}
		
		if (alsaUnmuteControl != null && alsaUnmuteControl.trim().length() > 0 &&
				alsaVolumeControl != null && alsaVolumeControl.trim().length() > 0) {
			addActionError(getText("squeezelite.validation.alsaControl.fail"));
		}
		
		// validate the resample options
		if (resample) {
			
			if (resampleFlags != null && resampleFlags.trim().length() > 0) {
				try {
					Integer.parseInt(resampleFlags.trim());
				} catch (NumberFormatException nfe) {
					addActionError(getText("squeezelite.validation.resample.flags.fail"));
				}
			}
			
			double attenuation = -1.0;
			if (resampleAttenuation != null && resampleAttenuation.trim().length() > 0) {
				try {
					attenuation = Double.parseDouble(resampleAttenuation.trim());
					if (attenuation < 0.0) {
						addActionError(getText("squeezelite.validation.resample.attenuation.fail"));
					}
				} catch (NumberFormatException nfe) {
					addActionError(getText("squeezelite.validation.resample.attenuation.fail"));
				}
			}
			
			int precision = -1;
			if (resamplePrecision != null && resamplePrecision.trim().length() > 0) {
				try {
					precision = Integer.parseInt(resamplePrecision.trim());
					if (precision < 1) {
						addActionError(getText("squeezelite.validation.resample.precision.fail"));
					}
				} catch (NumberFormatException nfe) {
					addActionError(getText("squeezelite.validation.resample.precision.fail"));
				}
			}
			
			double passbandEnd = -1.0;
			if (resamplePassbandEnd != null && resamplePassbandEnd.trim().length() > 0) {
				try {
					passbandEnd = Double.parseDouble(resamplePassbandEnd.trim());
					if (passbandEnd < 0.0 || passbandEnd > 100.0) {
						addActionError(getText("squeezelite.validation.resample.passbandEnd.fail"));
					}
				} catch (NumberFormatException nfe) {
					addActionError(getText("squeezelite.validation.resample.passbandEnd.fail"));
				}
			}

			double stopbandStart = -1.0;
			if (resampleStopbandStart != null && resampleStopbandStart.trim().length() > 0) {
				try {
					stopbandStart = Double.parseDouble(resampleStopbandStart.trim());
					if (stopbandStart < 0.0 || stopbandStart > 100.0) {
						addActionError(getText("squeezelite.validation.resample.stopbandStart.fail"));
					}
					// make sure stopbandStart is greater than passbandEnd, if passbandEnd has been set.
					if (passbandEnd > -1.0) {
						if (stopbandStart < passbandEnd) {
							addActionError(getText("squeezelite.validation.resample.stopbandStart.fail"));
						}
					}
				} catch (NumberFormatException nfe) {
					addActionError(getText("squeezelite.validation.resample.stopbandStart.fail"));
				}
			}

			double phaseResponse = -1.0;
			if (resamplePhaseResponse != null && resamplePhaseResponse.trim().length() > 0) {
				try {
					phaseResponse = Double.parseDouble(resamplePhaseResponse.trim());
					if (phaseResponse < 0.0 || phaseResponse > 100.0) {
						addActionError(getText("squeezelite.validation.resample.phaseResponse.fail"));
					}
				} catch (NumberFormatException nfe) {
					addActionError(getText("squeezelite.validation.resample.phaseResponse.fail"));
				}
			}
		}
		
		if (hasActionErrors()) {
			try {
				populateServiceStatus();
				populateLog();
			} catch(Exception e) {}
		}
	}
	
	/**
	 * @return
	 * @throws Exception
	 */
	public String saveAndRestart() throws Exception {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("saveAndRestart()");
		}
		
		String result = save();
		result = condRestartService();
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("saveAndRestart() returns " + result);
		}
		
		return result;
	}
	
	/**
	 * @return
	 * @throws Exception
	 */
	public String save() throws Exception {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("save()");
		}
		
		String result = save_();
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("save() returns " + result);
		}
		
		return result;
	}
	
	/* (non-Javadoc)
	 * @see squeeze.web.SystemctlAction#execute()
	 */
	public String execute() throws Exception {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("execute()");
		}
		
		String result = save();
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("execute() returns " + result);
		}
		
		return result;
	}
	
	/**
	 * @param name
	 * @return
	 *
	private final static boolean validateLogName(final String name) {
		
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("validateLogName(name=" + name + ")");
		}
		
		return Util.contains(Validate.SQUEEZELITE_LOG_NAMES, name);
	}
	*/

	/**
	 * @param level
	 * @return
	 *
	private final static boolean validateLogLevel(final String level) {
		
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("validateLogLevel(level=" + level + ")");
		}
		
		return Util.contains(Validate.SQUEEZELITE_LOG_LEVELS, level);
	}
	*/
	
	/**
	 * @param codec
	 * @return
	 *
	private final static boolean validateCodec(final String codec) {
		
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("validateCodec(codec=" + codec + ")");
		}
		
		return Util.contains(Validate.SQUEEZELITE_CODECS, codec);
	}
	*/
}

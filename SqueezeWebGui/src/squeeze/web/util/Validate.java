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
public class Validate {

	public final static String REGEX_IP_ADDRESS = 
		"^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$";
	
	public final static String REGEX_HOST_ADDRESS = 
		"^(([a-zA-Z0-9]|[a-zA-Z0-9][a-zA-Z0-9\\-]*[a-zA-Z0-9])\\.)*([A-Za-z0-9]|[A-Za-z0-9][A-Za-z0-9\\-]*[A-Za-z0-9])$";
	
	public final static String REGEX_MAC_ADDRESS = 
		"^[0-9A-Fa-f]{2}:[0-9A-Fa-f]{2}:[0-9A-Fa-f]{2}:[0-9A-Fa-f]{2}:[0-9A-Fa-f]{2}:[0-9A-Fa-f]{2}$";
	
	public final static String REGEX_MAC_ADDRESS_IN_LINE = 
			".*[0-9A-Fa-f]{2}:[0-9A-Fa-f]{2}:[0-9A-Fa-f]{2}:[0-9A-Fa-f]{2}:[0-9A-Fa-f]{2}:[0-9A-Fa-f]{2}.*";

	public final static int MAC_STRING_LENGTH = 17;
	
	public final static String REGEX_ALPHA_NUMERIC = "^[a-zA-Z0-9]*$";
	
	//public final static String REGEX_ALPHA_NUMERIC_UNDERSCORE = "^[a-zA-Z0-9_]+$";

	public final static String REGEX_ALPHA_NUMERIC_UNDERSCORE_DASH = "^[a-zA-Z0-9_\\-]+$";

	// public final static String SQUEEZELITE_REGEX_ALSA_PARAMS = "^([0-9]*):([0-9]*):(16|24|24_3|32)?:(0|1)?$";
	// Add optional 5th param, double close / open.
	//public final static String SQUEEZELITE_REGEX_ALSA_PARAMS = "^([0-9]*):([0-9]*):(16|24|24_3|32)?:(0|1)?(:(0|1)?)?$";

	//public final static String SQUEEZELITE_REGEX_ALSA_BUFFER = "^([0-9]+):([0-9]+)$";

	// single rate or comma separated list
	public final static String SQUEEZELITE_REGEX_MAX_RATE = "^([0-9]+)(,([0-9]+))*(:([0-9]+))?$";
	// range. eg. 44100-192000
	public final static String SQUEEZELITE_REGEX_MAX_RATE_RANGE = "^([0-9]+)-([0-9]+)(:([0-9]+))?$";
	// :100 - time between sample rate changes
	public final static String SQUEEZELITE_REGEX_MAX_RATE_TIME_BTWN_CHANGE = "^(:([0-9]+)){1}$";

	//public final static int SQUEEZELITE_MAX_SAMPLE_RATE = 384000;

	/*
	public final static String[] SQUEEZELITE_LOG_NAMES = {
		SqueezeliteAction.LOG_NAME_ALL, 
		SqueezeliteAction.LOG_NAME_SLIMPROTO, 
		SqueezeliteAction.LOG_NAME_STREAM, 
		SqueezeliteAction.LOG_NAME_DECODE, 
		SqueezeliteAction.LOG_NAME_OUTPUT
	};
	*/
	
	/*
	public final static String[] SQUEEZELITE_LOG_LEVELS = {
		SqueezeliteAction.LOG_LEVEL_INFO, 
		SqueezeliteAction.LOG_LEVEL_DEBUG, 
		SqueezeliteAction.LOG_LEVEL_SDEBUG
	};
	*/
	
	/*
	public final static String[] SQUEEZELITE_CODECS = {
		SqueezeliteAction.CODEC_MP3,
		SqueezeliteAction.CODEC_MP3_MAD,
		SqueezeliteAction.CODEC_MP3_MPG,
		SqueezeliteAction.CODEC_FLAC,
		SqueezeliteAction.CODEC_PCM,
		SqueezeliteAction.CODEC_OGG,
		SqueezeliteAction.CODEC_AAC,
		SqueezeliteAction.CODEC_ALAC,
		SqueezeliteAction.CODEC_WMA,
		SqueezeliteAction.CODEC_DSD
	};
	*/
		
	/**
	 * 
	 */
	private Validate() {
		
		super();
	}
}

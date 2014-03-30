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

import java.util.ArrayList;
import java.util.List;

import squeeze.web.SqueezeliteAction;

/**
 * @author Clive Messer <clive.m.messer@gmail.com>
 *
 */
public class NameFlag {

	public final static List<NameFlag> SOXR_QUALITY_LIST;
	public final static List<NameFlag> SOXR_FILTER_LIST;
	
	public final static List<NameFlag> ALSA_PARAMS_FORMAT_LIST;
	public final static List<NameFlag> ALSA_PARAMS_MMAP_LIST;
	
	public final static List<NameFlag> LOG_LEVEL_LIST;

	public final static List<NameFlag> MP3_LIST;
	
	public final static List<NameFlag> WIRELESS_REG_DOMAIN_LIST;

	private String name;
	private String flag;
	
	static {
		
		SOXR_QUALITY_LIST = new ArrayList<NameFlag>();	
		SOXR_QUALITY_LIST.add(new NameFlag("Quick (cubic interpolation)", 
				String.valueOf(SoxResample.SOXR_QQ_FLAG)));
		SOXR_QUALITY_LIST.add(new NameFlag("Low (16 bit with larger rolloff)", 
				String.valueOf(SoxResample.SOXR_LQ_FLAG)));
		SOXR_QUALITY_LIST.add(new NameFlag("Medium (16 bit with medium rolloff)", 
				String.valueOf(SoxResample.SOXR_MQ_FLAG)));
		SOXR_QUALITY_LIST.add(new NameFlag("High (20 bit)", 
				String.valueOf(SoxResample.SOXR_HQ_FLAG)));
		SOXR_QUALITY_LIST.add(new NameFlag("Very High (28 bit)", 
				String.valueOf(SoxResample.SOXR_VHQ_FLAG)));
	
		SOXR_FILTER_LIST = new ArrayList<NameFlag>();
		SOXR_FILTER_LIST.add(new NameFlag("Linear Phase", 
				String.valueOf(SoxResample.SOXR_LINEAR_PHASE_FLAG)));
		SOXR_FILTER_LIST.add(new NameFlag("Intermediate Phase",  
				String.valueOf(SoxResample.SOXR_INTERMEDIATE_PHASE_FLAG)));
		SOXR_FILTER_LIST.add(new NameFlag("Minimum Phase", 
				String.valueOf(SoxResample.SOXR_MINIMUM_PHASE_FLAG)));
		
		ALSA_PARAMS_FORMAT_LIST = new ArrayList<NameFlag>();
		ALSA_PARAMS_FORMAT_LIST.add(new NameFlag("16 bit (16)", 
				SqueezeliteAction.ALSA_PARAMS_FORMAT_16));
		ALSA_PARAMS_FORMAT_LIST.add(new NameFlag("24 bit (3 bytes - 24_3)", 
				SqueezeliteAction.ALSA_PARAMS_FORMAT_24_3));
		ALSA_PARAMS_FORMAT_LIST.add(new NameFlag("24 bit (4 bytes - 24)", 
				SqueezeliteAction.ALSA_PARAMS_FORMAT_24));
		ALSA_PARAMS_FORMAT_LIST.add(new NameFlag("32 bit (32)", 
				SqueezeliteAction.ALSA_PARAMS_FORMAT_32));

		ALSA_PARAMS_MMAP_LIST = new ArrayList<NameFlag>();
		ALSA_PARAMS_MMAP_LIST.add(new NameFlag("Disable", Util.ZERO));
		ALSA_PARAMS_MMAP_LIST.add(new NameFlag("Enable", Util.ONE));
		
		LOG_LEVEL_LIST = new ArrayList<NameFlag>();
		LOG_LEVEL_LIST.add(new NameFlag("Info", SqueezeliteAction.LOG_LEVEL_INFO));
		LOG_LEVEL_LIST.add(new NameFlag("Debug", SqueezeliteAction.LOG_LEVEL_DEBUG));
		LOG_LEVEL_LIST.add(new NameFlag("Trace", SqueezeliteAction.LOG_LEVEL_SDEBUG));
		
		MP3_LIST = new ArrayList<NameFlag>();
		MP3_LIST.add(new NameFlag("Enable", SqueezeliteAction.CODEC_MP3));
		MP3_LIST.add(new NameFlag("Use libmad to decode mp3", SqueezeliteAction.CODEC_MP3_MAD));
		MP3_LIST.add(new NameFlag("Use libmpg123 to decode mp3", SqueezeliteAction.CODEC_MP3_MPG));
		
		WIRELESS_REG_DOMAIN_LIST = new ArrayList<NameFlag>();
		WIRELESS_REG_DOMAIN_LIST.add(new NameFlag("Roaming", "00"));
		WIRELESS_REG_DOMAIN_LIST.add(new NameFlag("Austria", "AT"));
		WIRELESS_REG_DOMAIN_LIST.add(new NameFlag("Australia", "AU"));
		WIRELESS_REG_DOMAIN_LIST.add(new NameFlag("Belgium", "BE"));
		WIRELESS_REG_DOMAIN_LIST.add(new NameFlag("Canada", "CA"));
		WIRELESS_REG_DOMAIN_LIST.add(new NameFlag("Switzerland", "CH"));
		WIRELESS_REG_DOMAIN_LIST.add(new NameFlag("China", "CN"));
		WIRELESS_REG_DOMAIN_LIST.add(new NameFlag("Germany", "DE"));
		WIRELESS_REG_DOMAIN_LIST.add(new NameFlag("Denmark", "DK"));
		WIRELESS_REG_DOMAIN_LIST.add(new NameFlag("Spain", "ES"));
		WIRELESS_REG_DOMAIN_LIST.add(new NameFlag("Finland", "FI"));
		WIRELESS_REG_DOMAIN_LIST.add(new NameFlag("France", "FR"));
		WIRELESS_REG_DOMAIN_LIST.add(new NameFlag("Great Britain", "GB"));
		WIRELESS_REG_DOMAIN_LIST.add(new NameFlag("Hong Kong", "HK"));
		WIRELESS_REG_DOMAIN_LIST.add(new NameFlag("Hungary", "HU"));
		WIRELESS_REG_DOMAIN_LIST.add(new NameFlag("Japan", "JP"));
		WIRELESS_REG_DOMAIN_LIST.add(new NameFlag("Ireland", "IE"));
		WIRELESS_REG_DOMAIN_LIST.add(new NameFlag("Israel", "IL"));
		WIRELESS_REG_DOMAIN_LIST.add(new NameFlag("India", "IN"));
		WIRELESS_REG_DOMAIN_LIST.add(new NameFlag("Italy", "IT"));
		WIRELESS_REG_DOMAIN_LIST.add(new NameFlag("Netherlands", "NL"));
		WIRELESS_REG_DOMAIN_LIST.add(new NameFlag("Norway", "NO"));
		WIRELESS_REG_DOMAIN_LIST.add(new NameFlag("New Zealand", "NZ"));
		WIRELESS_REG_DOMAIN_LIST.add(new NameFlag("Poland", "PL"));
		WIRELESS_REG_DOMAIN_LIST.add(new NameFlag("Portugal", "PT"));
		WIRELESS_REG_DOMAIN_LIST.add(new NameFlag("Serbia", "RS"));
		WIRELESS_REG_DOMAIN_LIST.add(new NameFlag("Russian Federation", "RU"));
		WIRELESS_REG_DOMAIN_LIST.add(new NameFlag("Sweden", "SE"));
		WIRELESS_REG_DOMAIN_LIST.add(new NameFlag("USA", "US"));
		WIRELESS_REG_DOMAIN_LIST.add(new NameFlag("South Africa", "ZA"));
	}
	
	/**
	 * 
	 */
	public NameFlag() {
		
		super();
	}

	/**
	 * @param name
	 * @param flag
	 */
	public NameFlag(String name, String flag) {
		
		super();
		
		this.name = name;
		this.flag = flag;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		
		this.name = name;
	}

	/**
	 * @return the flag
	 */
	public String getFlag() {
		
		return flag;
	}

	/**
	 * @param flag the flag to set
	 */
	public void setFlag(String flag) {
		
		this.flag = flag;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "NameFlag[name=" + name + ", flag=" + flag + "]";
	}
}

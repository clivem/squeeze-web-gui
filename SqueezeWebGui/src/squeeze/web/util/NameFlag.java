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

/**
 * @author Clive Messer <clive.m.messer@gmail.com>
 *
 */
public class NameFlag {

	private final static List<NameFlag> SOXR_QUALITY_LIST;
	private final static List<NameFlag> SOXR_FILTER_LIST;
	
	private final static List<NameFlag> ALSA_PARAMS_FORMAT_LIST;
	private final static List<NameFlag> ALSA_PARAMS_MMAP_LIST;
	
	private String name;
	private String flag;
	
	static {
		
		SOXR_QUALITY_LIST = new ArrayList<NameFlag>();	
		SOXR_QUALITY_LIST.add(new NameFlag("Cubic", "" + SoxResample.SOXR_QQ_FLAG));
		SOXR_QUALITY_LIST.add(new NameFlag("Low", "" + SoxResample.SOXR_LQ_FLAG));
		SOXR_QUALITY_LIST.add(new NameFlag("Medium", "" + SoxResample.SOXR_MQ_FLAG));
		SOXR_QUALITY_LIST.add(new NameFlag("High", "" + SoxResample.SOXR_HQ_FLAG));
		SOXR_QUALITY_LIST.add(new NameFlag("Very High", "" + SoxResample.SOXR_VHQ_FLAG));
	
		SOXR_FILTER_LIST = new ArrayList<NameFlag>();
		SOXR_FILTER_LIST.add(new NameFlag("Linear", "" + SoxResample.SOXR_LINEAR_PHASE_FLAG));
		SOXR_FILTER_LIST.add(new NameFlag("Intermediate", "" + SoxResample.SOXR_INTERMEDIATE_PHASE_FLAG));
		SOXR_FILTER_LIST.add(new NameFlag("Minimum Phase", "" + SoxResample.SOXR_MINIMUM_PHASE_FLAG));
		
		ALSA_PARAMS_FORMAT_LIST = new ArrayList<NameFlag>();
		ALSA_PARAMS_FORMAT_LIST.add(new NameFlag("16 bit", "16"));
		ALSA_PARAMS_FORMAT_LIST.add(new NameFlag("24 bit (3 bytes)", "24_3"));
		ALSA_PARAMS_FORMAT_LIST.add(new NameFlag("24 bit (4 bytes)", "24"));
		ALSA_PARAMS_FORMAT_LIST.add(new NameFlag("32 bit", "32"));

		ALSA_PARAMS_MMAP_LIST = new ArrayList<NameFlag>();
		ALSA_PARAMS_MMAP_LIST.add(new NameFlag("Disable", Util.ZERO));
		ALSA_PARAMS_MMAP_LIST.add(new NameFlag("Enable", Util.ONE));
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

	/**
	 * @return the soxResampleQualityList
	 */
	public final static List<NameFlag> getSoxResampleQualityList() {
		
		return SOXR_QUALITY_LIST;
	}

	/**
	 * @return the soxResampleFilterList
	 */
	public final static List<NameFlag> getSoxResampleFilterList() {
		
		return SOXR_FILTER_LIST;
	}

	/**
	 * @return the alsaParamsFormatList
	 */
	public final static List<NameFlag> getAlsaParamsFormatList() {
		
		return ALSA_PARAMS_FORMAT_LIST;
	}

	/**
	 * @return the alsaParamsMmapList
	 */
	public final static List<NameFlag> getAlsaParamsMmapList() {
		
		return ALSA_PARAMS_MMAP_LIST;
	}
}

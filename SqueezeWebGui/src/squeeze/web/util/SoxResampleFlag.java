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
public class SoxResampleFlag {

	private final static List<SoxResampleFlag> QUALITY_LIST;

	private final static List<SoxResampleFlag> FILTER_LIST;
	
	private String name;
	private String flag;
	
	static {
		
		QUALITY_LIST = new ArrayList<SoxResampleFlag>();	
		//QUALITY_LIST.add(new SoxResampleFlag("", null));
		QUALITY_LIST.add(new SoxResampleFlag("Cubic", "" + SoxResample.SOXR_QQ_FLAG));
		QUALITY_LIST.add(new SoxResampleFlag("Low", "" + SoxResample.SOXR_LQ_FLAG));
		QUALITY_LIST.add(new SoxResampleFlag("Medium", "" + SoxResample.SOXR_MQ_FLAG));
		QUALITY_LIST.add(new SoxResampleFlag("High", "" + SoxResample.SOXR_HQ_FLAG));
		QUALITY_LIST.add(new SoxResampleFlag("Very High", "" + SoxResample.SOXR_VHQ_FLAG));
	
		FILTER_LIST = new ArrayList<SoxResampleFlag>();
		//FILTER_LIST.add(new SoxResampleFlag("", null));
		FILTER_LIST.add(new SoxResampleFlag("Linear", "" + SoxResample.SOXR_LINEAR_PHASE_FLAG));
		FILTER_LIST.add(new SoxResampleFlag("Intermediate", "" + SoxResample.SOXR_INTERMEDIATE_PHASE_FLAG));
		FILTER_LIST.add(new SoxResampleFlag("Minimum Phase", "" + SoxResample.SOXR_MINIMUM_PHASE_FLAG));
	}
	
	/**
	 * 
	 */
	public SoxResampleFlag() {
		
		super();
	}

	/**
	 * @param name
	 * @param flag
	 */
	public SoxResampleFlag(String name, String flag) {
		
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
	 * @return
	 */
	public final static List<SoxResampleFlag> getQualityList() {
		
		return QUALITY_LIST;
	}

	/**
	 * @return
	 */
	public final static List<SoxResampleFlag> getFilterList() {
		
		return FILTER_LIST;
	}
}

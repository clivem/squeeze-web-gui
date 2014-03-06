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
public class SoxResample {

	// 'Quick' cubic interpolation.
	public final static int SOXR_QQ = 0;
	public final static char SOXR_QQ_FLAG = 'q';

	// 'Low' 16-bit with larger roll-off.
	public final static int SOXR_LQ = 1;
	public final static char SOXR_LQ_FLAG = 'l';
	
	// 'Medium' 16-bit with medium roll-off.
	public final static int SOXR_MQ = 2;
	public final static char SOXR_MQ_FLAG = 'm';
	
	public final static int SOXR_16_BITQ = 3;
	
	// 'High quality'.
	public final static int SOXR_20_BITQ = 4;
	public final static int SOXR_HQ = SOXR_20_BITQ;
	public final static char SOXR_HQ_FLAG = 'h';
	
	public final static int SOXR_24_BITQ = 5;
	
	// 'Very high quality'.
	public final static int SOXR_28_BITQ = 6;
	public final static int SOXR_VHQ = SOXR_28_BITQ;
	public final static char SOXR_VHQ_FLAG = 'v';
	
	public final static int SOXR_32_BITQ = 7;

	// linear filter
	public final static int SOXR_LINEAR_PHASE = 0x00;
	public final static char SOXR_LINEAR_PHASE_FLAG = 'L';
	
	// intermediate filter
	public final static int SOXR_INTERMEDIATE_PHASE = 0x10;
	public final static char SOXR_INTERMEDIATE_PHASE_FLAG = 'I';
	
	// minimum phase filter
	public final static int SOXR_MINIMUM_PHASE = 0x30;
	public final static char SOXR_MINIMUM_PHASE_FLAG = 'M';
	
	// steep filter
	public final static int SOXR_STEEP_FILTER = 0x40;
	public final static char SOXR_STEEP_FILTER_FLAG = 's';
	
	// async upsampling to maximum rate
	public final static char ASYNC_RATE_FLAG = 'X';

	// exception resampling
	public final static char EXCEPTION_RATE_FLAG = 'E';
	
	public final static String VALID_RECIPE_FLAGS = new String("" +
			SOXR_QQ_FLAG +
			SOXR_LQ_FLAG +
			SOXR_MQ_FLAG +
			SOXR_HQ_FLAG +
			SOXR_VHQ_FLAG +
			SOXR_LINEAR_PHASE_FLAG +
			SOXR_INTERMEDIATE_PHASE_FLAG +
			SOXR_MINIMUM_PHASE_FLAG +
			SOXR_STEEP_FILTER_FLAG +
			ASYNC_RATE_FLAG +
			EXCEPTION_RATE_FLAG
	);
	
	/**
	 * 
	 */
	private SoxResample() {
		
		super();
	}
	
	/**
	 * @param recipe
	 * @return
	 */
	public final static String validateRecipe(String recipe) {

		String invalidFlags = "";
		
		char[] recipeFlags = new char[recipe.length()]; 
		recipe.getChars(0, recipe.length(), recipeFlags, 0);
		
		for (int i = 0; i < recipeFlags.length; i++) {
			if (VALID_RECIPE_FLAGS.indexOf(recipeFlags[i]) < 0) {
				invalidFlags += recipeFlags[i];
			}
		}
		
		return invalidFlags;
	}
}

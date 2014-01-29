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

import org.apache.log4j.Logger;

/**
 * @author Clive Messer <clive.m.messer@gmail.com>
 *
 */
public abstract class ShouldRunnable implements Runnable {
	
	private final static Logger LOGGER = Logger.getLogger(ShouldRunnable.class);
	
	protected String context = null;
	protected boolean shouldRun = true;
	protected Thread t;
	
	public ShouldRunnable() {
		
		super();
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ShouldRunnable()");
		}
	}
	
	/**
	 * @return
	 */
	public synchronized Thread execute() {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("execute()");
		}
		
		return execute(false, null, Thread.NORM_PRIORITY);
	}
	
	/**
	 * @param priority
	 * @return
	 */
	public synchronized Thread execute(int priority) {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("execute(priority=" + priority + ")");
		}
		
		return execute(false, null, priority);
	}
	
	/**
	 * @param daemon
	 * @return
	 */
	public synchronized Thread execute(boolean daemon) {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("execute(daemon=" + daemon + ")");
		}

		return execute(daemon, null, Thread.NORM_PRIORITY);
	}
	
	/**
	 * @param daemon
	 * @param priority
	 * @return
	 */
	public synchronized Thread execute(boolean daemon, int priority) {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("execute(daemon=" + daemon + 
					", priority=" + priority + ")");
		}

		return execute(daemon, null, priority);
	}
	
	/**
	 * @param daemon
	 * @param context
	 * @return
	 */
	public synchronized Thread execute(boolean daemon, String context) {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("execute(daemon=" + daemon + 
					", context=" + context + ")");
		}

		return execute(daemon, context, Thread.NORM_PRIORITY);
	}
	
	/**
	 * @param daemon
	 * @param context
	 * @param priority
	 * @return
	 */
	public synchronized Thread execute(boolean daemon, String context, int priority) {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("execute(daemon=" + daemon + 
					", context=" + context + 
					", priority=" + priority + ")");
		}

		if (t == null) {
			this.context = context;
			t = new Thread(this);
			t.setDaemon(daemon);
			t.setName(getNextThreadName());
			t.setPriority(priority);
			t.start();
		}

		return t;
	}
	
	/**
	 * @return
	 */
	public synchronized Thread end() {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("end()");
		}

		shouldRun = false;
		if (t != null  && t.isAlive()) {
			t.interrupt();
		}
		return t;
	}	
	
	/**
	 * @param prefix
	 * @return
	 */
	protected abstract String getNextThreadName(String prefix);
	
	/**
	 * @return
	 */
	protected abstract String getNextThreadName();
}
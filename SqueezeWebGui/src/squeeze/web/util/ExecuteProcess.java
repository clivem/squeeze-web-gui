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

import java.io.IOException;
import java.io.Writer;

import org.apache.log4j.Logger;

/**
 * @author Clive Messer <clive.m.messer@gmail.com>
 *
 */
public class ExecuteProcess {
	
	private final static Logger LOGGER = Logger.getLogger(ExecuteProcess.class);

	/**
	 * 
	 */
	private ExecuteProcess() {
		
		super();
	}
	
	/**
	 * @param cmdLineArgs
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public final static int executeCommand(final String[] cmdLineArgs) 
				throws IOException, InterruptedException {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("executeCommand(cmdLineArgs=" + 
					Util.arrayToString(cmdLineArgs) + ")");
		}

		return executeCommand(cmdLineArgs, null, null);
	}
	
	/**
	 * @param cmdLineArgs
	 * @param stdOutWriter
	 * @param stdErrWriter
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public final static int executeCommand(final String[] cmdLineArgs, 
		Writer stdOutWriter, Writer stdErrWriter) 
			throws IOException, InterruptedException {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("executeCommand(cmdLineArgs=" + 
					Util.arrayToString(cmdLineArgs) + 
					", stdOutWriter=" + stdOutWriter + 
					", stdErrWriter=" + stdErrWriter + ")");
		}

		Process process = null;
		StdReaderLogger stdOutLogger = null;
		StdReaderLogger stdErrLogger = null;
		try {
			String cmdLine = Util.arrayToString(cmdLineArgs);
			LOGGER.info("Executing process: '" + cmdLine + "'");
			process = Runtime.getRuntime().exec(cmdLineArgs);
			
			stdOutLogger = new StdReaderLogger(process.getInputStream(), 
					"'" + cmdLine + "' STDOUT", stdOutWriter);
			Thread t1 = stdOutLogger.execute(true);
	
			stdErrLogger = new StdReaderLogger(process.getErrorStream(), 
					"'" + cmdLine + "' STDERR", stdErrWriter);
			Thread t2 = stdErrLogger.execute(true);
			
			process.waitFor();
			t1.join();
			t2.join();
			int exitValue = process.exitValue();
			LOGGER.info("Finished: '" + cmdLine + "'. Exit Value: " + exitValue);
			return exitValue;
		} finally {
			if (process != null) {
				Util.closeProcessStreams(process);
			}
			/*
			if (stdOutLogger != null) {
				try {
					stdOutLogger.end();
				} catch (Exception e) {}
			}
			if (stdErrLogger != null) {
				try {
					stdErrLogger.end();
				} catch (Exception e) {}
			}
			*/
		}				
	}
}

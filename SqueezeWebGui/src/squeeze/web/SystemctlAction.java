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

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

import squeeze.web.util.ExecuteProcess;
import squeeze.web.util.Util;

import com.opensymphony.xwork2.ActionSupport;

/**
 * @author Clive Messer <clive.m.messer@gmail.com>
 *
 */
public abstract class SystemctlAction extends ActionSupport {

	private static final long serialVersionUID = 6135611417140890571L;
	
	private final static Logger LOGGER = Logger.getLogger(SystemctlAction.class);
	
	protected String status;
	
	/**
	 * 
	 */
	public SystemctlAction() {
		
		super();
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("SystemctlAction()");
		}
	}
	
	/* (non-Javadoc)
	 * @see com.opensymphony.xwork2.ActionSupport#execute()
	 */
	public String execute() throws Exception {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("execute()");
		}

		String result = populate();
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("execute() returns " + result);
		}
		
		return result;
	}
		
	/**
	 * @return
	 */
	public abstract String getServiceName();
	
	/**
	 * @return
	 * @throws Exception
	 */
	public String populate() throws Exception {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("populate()");
		}

		try {
			populateServiceStatus();
		} catch (Exception e) {
			LOGGER.error("Caught exception while populating " + getServiceName() + "!", e);
			throw e;
		}
 		
		String result = "populate";
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("populate() returns " + result);
		}
		
		return result;
	}	

	/**
	 * @throws IOException
	 * @throws InterruptedException
	 */
	protected void populateServiceStatus() 
			throws IOException, InterruptedException {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("populateServiceStatus()");
		}

		File outFile = null;
		try {
			String serviceName = getServiceName();
			outFile = Util.createTempFile(serviceName + "_status_", ".txt");
			status = Util.getServiceStatus(serviceName, outFile);
		} finally {
			if (outFile != null) {
				try {
					outFile.delete();
				} catch (Exception e) {}
			}
		}		
		
	}
	
	/**
	 * @return
	 * @throws Exception
	 */
	public String enableAndStartService() throws Exception {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("enableAndStartService()");
		}

		String result = enableService();
		result = startService();
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("enableAndStartService() returns " + result);
		}
		
		return result;		
	}
	
	/**
	 * @return
	 * @throws Exception
	 */
	public String disableAndStopService() throws Exception {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("disableAndStopService()");
		}

		String result = disableService();
		result = stopService();
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("disableAndStopService() returns " + result);
		}
		
		return result;		
	}
	
	/**
	 * @return
	 * @throws Exception
	 */
	public String stopService() throws Exception {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("stopService()");
		}

		try {
			stopService(getServiceName());
		} catch (Exception e) {
			LOGGER.error("Caught exception stopping " + getServiceName() + "!", e);
			throw e;
		}
		
		String result = SUCCESS;
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("stopService() returns " + result);
		}
		
		return result;
	}
	
	/**
	 * @return
	 * @throws Exception
	 */
	public String restartService() throws Exception {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("restartService()");
		}

		try {
			restartService(getServiceName());
		} catch (Exception e) {
			LOGGER.error("Caught exception restarting " + getServiceName() + "!", e);
			throw e;
		}
		
		String result = SUCCESS;
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("restartService() returns " + result);
		}
		
		return result;
	}

	/**
	 * @return
	 * @throws Exception
	 */
	public String condRestartService() throws Exception {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("condRestartService()");
		}

		try {
			condRestartService(getServiceName());
		} catch (Exception e) {
			LOGGER.error("Caught exception conditionally restarting " + 
							getServiceName() + "!", e);
			throw e;
		}
		
		String result = SUCCESS;
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("condRestartService() returns " + result);
		}
		
		return result;
	}

	/**
	 * @return
	 * @throws Exception
	 */
	public String startService() throws Exception {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("startService()");
		}

		try {
			startService(getServiceName());
		} catch (Exception e) {
			LOGGER.error("Caught exception starting " + getServiceName() + "!", e);
			throw e;
		}
		
		String result = SUCCESS;
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("startService() returns " + result);
		}
		
		return result;
	}

	/**
	 * @return
	 * @throws Exception
	 */
	public String enableService() throws Exception {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("enableService()");
		}

		try {
			enableService(getServiceName());
		} catch (Exception e) {
			LOGGER.error("Caught exception enabling " + getServiceName() + "!", e);
			throw e;
		}
		
		String result = SUCCESS;
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("enableService() returns " + result);
		}
		
		return result;
	}

	/**
	 * @return
	 * @throws Exception
	 */
	public String disableService() throws Exception {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("disableService()");
		}

		try {
			disableService(getServiceName());
		} catch (Exception e) {
			LOGGER.error("Caught exception disabling " + getServiceName() + "!", e);
			throw e;
		}
		
		String result = SUCCESS;
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("disableService() returns " + result);
		}
		
		return result;
	}

	/**
	 * @param serviceName
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private int condRestartService(String serviceName) 
				throws IOException, InterruptedException{
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("condRestartService(serviceName=" + serviceName + ")");
		}

		return ExecuteProcess.executeCommand(
				Util.getSystemctlCondRestartCmdLine(serviceName));
	}

	/**
	 * @param serviceName
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private int restartService(String serviceName) 
				throws IOException, InterruptedException{
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("restartService(serviceName=" + serviceName + ")");
		}

		return ExecuteProcess.executeCommand(
				Util.getSystemctlRestartCmdLine(serviceName));
	}

	/**
	 * @param serviceName
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private int startService(String serviceName) 
			throws IOException, InterruptedException{
	
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("startService(serviceName=" + serviceName + ")");
		}

		return ExecuteProcess.executeCommand(
				Util.getSystemctlStartCmdLine(serviceName));
	}

	/**
	 * @param serviceName
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private int stopService(String serviceName) 
			throws IOException, InterruptedException{
	
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("stopService(serviceName=" + serviceName + ")");
		}

		return ExecuteProcess.executeCommand(
				Util.getSystemctlStopCmdLine(serviceName));
	}

	/**
	 * @param serviceName
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private int enableService(String serviceName) 
			throws IOException, InterruptedException{
	
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("enableService(serviceName=" + serviceName + ")");
		}

		return ExecuteProcess.executeCommand(
				Util.getSystemctlEnableCmdLine(serviceName));
	}

	/**
	 * @param serviceName
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private int disableService(String serviceName) 
			throws IOException, InterruptedException{
	
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("disableService(serviceName=" + serviceName + ")");
		}

		return ExecuteProcess.executeCommand(
				Util.getSystemctlDisableCmdLine(serviceName));
	}	
	
	/**
	 * @return
	 */
	public String getStatus() {
		
		return status;
	}

	/**
	 * @param status
	 */
	public void setStatus(String status) {
		
		this.status = status;
	}
}

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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * @author Clive Messer <clive.m.messer@gmail.com>
 *
 */
public class CifsCredentials {

	private final static Logger LOGGER = Logger.getLogger(CifsCredentials.class);
	
	private final static String USERNAME = "username";
	private final static String USER = "user";
	private final static String PASS = "pass";
	private final static String PASSWORD = "password";
	private final static String DOMAIN = "domain";
	
	private final static String CREDENTIALS = "credentials";
	private final static String CREDENTIALS_DIR = "/etc/credentials";
	
	private String credentialsFile = null;
	private Map<String, String> credentialsMap = new HashMap<String, String>();
	
	/**
	 * @param username
	 * @param password
	 * @param domain
	 */
	public CifsCredentials(String username, String password, String domain) {
		
		super();

		setUsername(username);
		setPassword(password);
		setDomain(domain);
	}

	/**
	 * 
	 */
	public CifsCredentials() {
		
		super();
	}
	
	/**
	 * @param credentialsFile
	 */
	public CifsCredentials(String credentialsFile) {
		
		super();

		setCredentialsFile(credentialsFile);
	}
	
	private void populateCredentials() {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("populateCredentials()");
		}
		
		try {
			File file = new File(credentialsFile);
			if (file.exists() && file.isFile()) {
				if (CREDENTIALS_DIR.equals(file.getCanonicalFile().getParent())) { 
					File tmpFile = null;		
					try {
						tmpFile = Util.createTempFile(CREDENTIALS + "_" + file.getName() + "_", ".txt");
						Writer writer = new FileWriter(tmpFile);
			
						String[] cmdLineArgs = new String[] {
								Commands.CMD_SUDO, Commands.SCRIPT_CREDENTIALS_READ, file.getName()
						};
						
						ExecuteProcess.executeCommand(cmdLineArgs, writer, null);
			
						Util.readConfigProperties(new FileReader(tmpFile), credentialsMap);
					} catch (Exception e) {
						LOGGER.warn("Caught exception processing credentials file!", e);
					} finally {
						if (tmpFile != null) {
							try {
								tmpFile.delete();
							} catch (Exception e) {}
						}
					}
				} else {
					LOGGER.warn("populateCredentials(): " + file.getCanonicalPath() + 
							" not in " + CREDENTIALS_DIR + "!");
				} 
			} else {
				LOGGER.warn("populateCredentials(): " + file.getCanonicalPath() + " does not exist!");			
			}
		} catch (IOException ioe) {
			LOGGER.warn("Caught exception processing credentials file!", ioe);			
		}
	}
	
	/**
	 * @return
	 * @throws IOException
	 */
	private File writeTempCredentialsProperties(String name) 
			throws IOException {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("writeTempCredentialsProperties(name=" + name + ")");
		}

		BufferedWriter writer = null;
		try {
			File tmpFile = Util.createTempFile(CREDENTIALS + "_" + name + "_", ".txt");
			writer = new BufferedWriter(new FileWriter(tmpFile));
			
			writer.write(Util.getModifiedComment());

			String username = getUsername();
			writer.write(USERNAME + Util.EQUALS + (username != null ? username : "guest") + Util.LINE_SEP );
			
			String password = getPassword();
			writer.write(PASSWORD + Util.EQUALS + (password != null ? password : "") + Util.LINE_SEP );
			
			String domain = getDomain();
			writer.write(DOMAIN + Util.EQUALS + (domain != null ? domain : "") + Util.LINE_SEP );
			
			return tmpFile;
		} finally {
			if (writer != null) {
				try {
					writer.flush();
				} catch (Exception e) {}
				
				try {
					writer.close();
				} catch (Exception e) {}
			}
		}			
	}
	
	/**
	 * @param tmpFile
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private int createOrReplaceCredentials(File tmpFile, String name)
			throws IOException, InterruptedException {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("createOrReplaceCredentials(tmpFile=" + tmpFile + ", name=" + name + ")");
		}

		String[] cmdLineArgs = new String[] {
				Commands.CMD_SUDO, Commands.SCRIPT_CREDENTIALS_UPDATE, 
				tmpFile.getAbsolutePath(), name
		};
		
		return ExecuteProcess.executeCommand(cmdLineArgs);
	}
		
	/**
	 * @return the credentialsFile
	 */
	public String getCredentialsFile() {
		return credentialsFile;
	}

	/**
	 * @param credentialsFile the credentialsFile to set
	 */
	public void setCredentialsFile(String credentialsFile) {
		this.credentialsFile = credentialsFile;
		populateCredentials();
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		String username = credentialsMap.get(USERNAME);
		if (username == null) {
			return credentialsMap.get(USER);
		}
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		credentialsMap.put(USERNAME, username);
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		String password = credentialsMap.get(PASSWORD);
		if (password == null) {
			return credentialsMap.get(PASS);
		}
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		credentialsMap.put(PASSWORD, password);
	}

	/**
	 * @return the domain
	 */
	public String getDomain() {
		return credentialsMap.get(DOMAIN);
	}

	/**
	 * @param domain the domain to set
	 */
	public void setDomain(String domain) {
		credentialsMap.put(DOMAIN, domain);
	}
	
	/**
	 * @param args
	 */
	public final static void main(String[] args) {
		CifsCredentials credentials = new CifsCredentials("/etc/credentials/test");
		LOGGER.warn("username: " + credentials.getUsername() + ", password: " + 
					credentials.getPassword() + ", domain: " + credentials.getDomain());
		
		credentials.setUsername("TEST");
		credentials.setPassword("TEST");
		credentials.setDomain("TEST");
		
		File tmpFile = null;
		try {
			File file = new File(credentials.getCredentialsFile());
			if (CREDENTIALS_DIR.equals(file.getCanonicalFile().getParent())) {
				tmpFile = credentials.writeTempCredentialsProperties(file.getName());
				credentials.createOrReplaceCredentials(tmpFile, file.getName());
			} else {
				LOGGER.warn("populateCredentials(): " + file.getCanonicalPath() + 
						" not in " + CREDENTIALS_DIR + "!");
			}
		} catch (Exception e) {
			LOGGER.warn("", e);
		} finally {
			if (tmpFile != null) {
				try {
					tmpFile.delete();
				} catch (Exception e) {}
			}
		}
	}
}

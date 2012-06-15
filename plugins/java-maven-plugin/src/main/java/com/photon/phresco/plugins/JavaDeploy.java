/*
 * ###
 * java-maven-plugin Maven Mojo
 * 
 * Copyright (C) 1999 - 2012 Photon Infotech Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ###
 */
package com.photon.phresco.plugins;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.StringUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.photon.phresco.commons.FrameworkConstants;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.framework.PhrescoFrameworkFactory;
import com.photon.phresco.framework.api.ProjectAdministrator;
import com.photon.phresco.model.SettingsInfo;
import com.photon.phresco.util.ArchiveUtil;
import com.photon.phresco.util.ArchiveUtil.ArchiveType;
import com.photon.phresco.util.Constants;
import com.photon.phresco.util.PluginConstants;
import com.photon.phresco.util.PluginUtils;

/**
 * Goal which deploys the Java WebApp to a server
 * 
 * @goal deploy
 * 
 */
public class JavaDeploy extends AbstractMojo implements PluginConstants {

	/**
	 * The Maven project.
	 * 
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 */
	protected MavenProject project;

	/**
	 * @parameter expression="${project.basedir}" required="true"
	 * @readonly
	 */
	protected File baseDir;

	/**
	 * Build file name to deploy
	 * 
	 * @parameter expression="${buildName}" required="true"
	 */
	protected String buildName;
		
	/**
	 * @parameter expression="${environmentName}" required="true"
	 */
	protected String environmentName;
	
	/**
	 * @parameter expression="${importSql}" required="true"
	 */
	protected boolean importSql;

	private File buildFile;
	private File tempDir;
	private File buildDir;
	private String context;
	private static final String finalName = "finalName";
	public void execute() throws MojoExecutionException {
		init();
		updateFinalName();
		createDb();
		extractBuild();
		deployToServer();
		cleanUp();
	}

	private void init() throws MojoExecutionException {
		try {
			if (StringUtils.isEmpty(buildName) || StringUtils.isEmpty(environmentName)) {
				callUsage();
			}
			buildDir = new File(baseDir.getPath() + PluginConstants.BUILD_DIRECTORY);// build dir
			buildFile = new File(buildDir.getPath() + File.separator + buildName);// filename
			tempDir = new File(buildDir.getPath() + TEMP_DIR);// temp dir
			tempDir.mkdirs();
		} catch (Exception e) {
			getLog().error(e);
			throw new MojoExecutionException(e.getMessage(), e);
		}
	}

	private void callUsage() throws MojoExecutionException {
		getLog().error("Invalid usage.");
		getLog().info("Usage of Deploy Goal");
		getLog().info(
				"mvn java:deploy -DbuildName=\"Name of the build\""
						+ " -DenvironmentName=\"Multivalued evnironment names\""
						+ " -DimportSql=\"Does the deployment needs to import sql(TRUE/FALSE?)\"");
		throw new MojoExecutionException("Invalid Usage. Please see the Usage of Deploy Goal");
	}

	private void updateFinalName() throws MojoExecutionException {
		try {
			ProjectAdministrator projAdmin = PhrescoFrameworkFactory.getProjectAdministrator();
			String envName = environmentName;
			if(envName.equals(projAdmin.getDefaultEnvName(baseDir.getName()))) {
				return;
			}
			List<SettingsInfo> settingsInfos = projAdmin.getSettingsInfos(Constants.SETTINGS_TEMPLATE_SERVER, baseDir.getName(), envName);
			for (SettingsInfo settingsInfo : settingsInfos) {
				context = settingsInfo.getPropertyInfo(Constants.SERVER_CONTEXT).getValue();
				break;
			}
			File pom = project.getFile();
			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(pom);
			Element projectNode = doc.getRootElement();
			Element buildNode = projectNode.getChild(JAVA_POM_BUILD_NAME, projectNode.getNamespace());
			Element finalNameElement = buildNode.getChild(JAVA_POM_FINAL_NAME, buildNode.getNamespace());
			if (finalNameElement == null) {
				finalNameElement = new Element(finalName);
				finalNameElement.setText(context);
				buildNode.addContent(finalNameElement);
			} else {
				finalNameElement.setText(context);
			}
			savePOMFile(doc, pom);
		} catch (JDOMException e) {
			throw new MojoExecutionException(e.getMessage(), e);
		} catch (IOException e) {
			throw new MojoExecutionException(e.getMessage(), e);
		} catch (PhrescoException e) {
			throw new MojoExecutionException(e.getMessage(), e);
		} catch (Exception e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
	}

	private void savePOMFile(Document document, File xmlFile) throws IOException {
		FileWriter writer = null;
		try {
			writer = new FileWriter(FrameworkConstants.POM_FILE);
			if (xmlFile.exists()) {
				XMLOutputter xmlOutput = new XMLOutputter();
				xmlOutput.setFormat(Format.getPrettyFormat());
				xmlOutput.output(document, writer);
			}
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}
	
	private void createDb() throws MojoExecutionException {
		PluginUtils util = new PluginUtils();
		try {
			if (importSql) {
				List<SettingsInfo> settingsInfos = getSettingsInfo(Constants.SETTINGS_TEMPLATE_DB);
				for (SettingsInfo databaseDetails : settingsInfos) {
					util.executeSql(databaseDetails, getProjectRoot(baseDir), JAVA_SQL_DIR, JAVA_SQL_FILE);
				}
			}
		} catch (PhrescoException e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
	}

	private void extractBuild() throws MojoExecutionException {
		try {
			ArchiveUtil.extractArchive(buildFile.getPath(), tempDir.getPath(), ArchiveType.ZIP);
		} catch (PhrescoException e) {
			throw new MojoExecutionException(e.getErrorMessage(), e);
		}
	}

	private void deployToServer() throws MojoExecutionException {
		try {
			List<SettingsInfo> settingsInfos  = getSettingsInfo(Constants.SETTINGS_TEMPLATE_SERVER);
			for (SettingsInfo serverDetails : settingsInfos) {
				deploy(serverDetails);
			}			
		} catch (PhrescoException e) {
			throw new MojoExecutionException(e.getErrorMessage(), e);
		}
	}
	
	private void deploy(SettingsInfo info) throws MojoExecutionException {
		if (info == null) {
			return;
		}
		String serverhost = info.getPropertyInfo(Constants.SERVER_HOST).getValue();
		String serverport = info.getPropertyInfo(Constants.SERVER_PORT).getValue();
		String serverusername = info.getPropertyInfo(Constants.SERVER_ADMIN_USERNAME).getValue();
		String serverpassword = info.getPropertyInfo(Constants.SERVER_ADMIN_PASSWORD).getValue();
		String version = info.getPropertyInfo(Constants.SERVER_VERSION).getValue();
		String servertype = info.getPropertyInfo(Constants.SERVER_TYPE).getValue();
		String mavenHome = System.getProperty(MVN_HOME);

		// no remote deployment
		if (serverusername.isEmpty() && serverpassword.isEmpty()) {
			deploy();
			return;
		}

		// remote deployment
		if (servertype.contains(TYPE_TOMCAT)
				&& ((version.equals("7.0.x")) || (version.equals("7.1.x")) || (version.equals("6.0.x")))) {
			deployToTomcatServer(serverhost, serverport, serverusername, serverpassword, mavenHome);
		} else if (servertype.contains(TYPE_JBOSS) && (version.equals("7.0.x"))) {
			deployToJbossServer(serverhost, serverusername, serverpassword, mavenHome);
		} else if (servertype.contains(TYPE_WEBLOGIC) && (version.equals("12c(12.1.1)"))) {
			deployToWeblogicServer(serverhost, serverport, serverusername, serverpassword, mavenHome);
		} else {
			// for other servers
			deploy();
		}
	}

	private void deployToTomcatServer(String serverhost, String serverport, String serverusername,
			String serverpassword, String mavenHome) throws MojoExecutionException {
		try {
			getLog().info("project is deploying ......");
			ProcessBuilder pb = new ProcessBuilder(mavenHome + MVN_EXE_PATH);

			pb.redirectErrorStream(true);
			List<String> commands = pb.command();
			commands.add(TOMCAT_GOAL);
			commands.add(SERVER_HOST + serverhost);
			commands.add(SERVER_PORT + serverport);
			commands.add(SERVER_USERNAME + serverusername);
			commands.add(SERVER_PASSWORD + serverpassword);
			pb.directory(baseDir);
			Process process = pb.start();
		} catch (IOException e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
	}

	private void deployToJbossServer(String serverhost, String serverusername, String serverpassword, String mavenHome)
			throws MojoExecutionException {
		try {
			getLog().info("project is deploying ......");
			ProcessBuilder pb = new ProcessBuilder(mavenHome + MVN_EXE_PATH);
			pb.redirectErrorStream(true);
			List<String> commands = pb.command();
			commands.add(JBOSS_GOAL);
			commands.add(SERVER_HOST + serverhost);
			commands.add(SERVER_USERNAME + serverusername);
			commands.add(SERVER_PASSWORD + serverpassword);
			pb.directory(baseDir);
			Process process = pb.start();
		} catch (IOException e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
	}

	private void deployToWeblogicServer(String serverhost, String serverport, String serverusername,
			String serverpassword, String mavenHome) throws MojoExecutionException {
		try {
			getLog().info("project is deploying ......");
			ProcessBuilder pb = new ProcessBuilder(mavenHome + MVN_EXE_PATH);
			pb.redirectErrorStream(true);
			List<String> commands = pb.command();
			commands.add(WEBLOGIC_GOAL);
			commands.add(SERVER_HOST + serverhost);
			commands.add(SERVER_PORT + serverport);
			commands.add(SERVER_USERNAME + serverusername);
			commands.add(SERVER_PASSWORD + serverpassword);
			pb.directory(baseDir);
			Process process = pb.start();
		} catch (IOException e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
	}

	private File getProjectRoot(File childDir) {
		File[] listFiles = childDir.listFiles(new PhrescoDirFilter());
		if (listFiles != null && listFiles.length > 0) {
			return childDir;
		}
		if (childDir.getParentFile() != null) {
			return getProjectRoot(childDir.getParentFile()); 
		}
		return null;
	}
	
	public class PhrescoDirFilter implements FilenameFilter {

        public boolean accept(File dir, String name) {
            return name.equals(DOT_PHRESCO_FOLDER);
        }
    }
	
	private void deploy() throws MojoExecutionException {
		String deployLocation = "";
		try {
			List<SettingsInfo> settingsInfos = getSettingsInfo(Constants.SETTINGS_TEMPLATE_SERVER);
			for (SettingsInfo serverDetails : settingsInfos) {
				deployLocation = serverDetails.getPropertyInfo(Constants.SERVER_DEPLOY_DIR).getValue();
				break;
			}		
			File deployDir = new File(deployLocation);
			FileUtils.mkdir(deployDir.getPath().trim());	
			getLog().info("Project is deploying.........");
			FileUtils.copyDirectoryStructure(tempDir.getAbsoluteFile(), deployDir);
			getLog().info("Project is deployed successfully");
		} catch (Exception e) {
			getLog().error(e);
			throw new MojoExecutionException(e.getMessage(), e);
		}
	}
	
	private List<SettingsInfo> getSettingsInfo(String configType) throws PhrescoException {
		ProjectAdministrator projAdmin = PhrescoFrameworkFactory.getProjectAdministrator();
		return projAdmin.getSettingsInfos(configType, getProjectRoot(baseDir).getName(), environmentName);
	}
	
	private void cleanUp() throws MojoExecutionException {
		try {
			FileUtils.deleteDirectory(tempDir);
		} catch (IOException e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
	}
}
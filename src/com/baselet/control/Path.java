package com.baselet.control;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import com.baselet.control.Constants.Program;
import com.baselet.control.Constants.RuntimeType;

public class Path {


	private static String tempDir;
	private static String homeProgramDir;

	public static String config() {
		return Path.homeProgram() + Program.CONFIG_NAME;
	}

	public static String customElements() {
		return homeProgram() + "custom_elements/";
	}

	public static String temp() {
		if (tempDir == null) {
			String tmp = System.getProperty("java.io.tmpdir");
			if (!(tmp.endsWith(File.separator))) tmp = tmp + File.separator;
			tempDir = tmp;
		}
		return tempDir;
	}

	/**
	 * STANDALONE NOJAR: <programpath>
	 * STANDALONE JAR: <programpath>
	 * ECLIPSE NOJAR: <programpath>
	 * ECLIPSE JAR: <eclipsepath>/<configuration>/<dirToStoreCustomStuff>
	 */
	public static String homeProgram() {
		if (homeProgramDir == null) {
			if (Program.RUNTIME_TYPE == RuntimeType.ECLIPSE_PLUGIN) {
				String path = null;
//				try {
//					URL homeURL = MainPlugin.getURL();
					path ="";// FileLocator.toFileURL(homeURL).toString().substring(new String("file:/").length());
					if (File.separator.equals("/")) path = "/" + path;
//				} catch (IOException e) {
//					log.error("Cannot find location of Eclipse Plugin jar", e);
//				}
				homeProgramDir = path;
			}
			else {
				String tempPath, realPath;
				tempPath = Path.executable();
				tempPath = tempPath.substring(0, tempPath.length() - 1);
				tempPath = tempPath.substring(0, tempPath.lastIndexOf('/') + 1);
				tempPath = tempPath.substring(0, tempPath.length() - 1);
				tempPath = tempPath.substring(0, tempPath.lastIndexOf('/') + 1);
				realPath = new File(tempPath).getAbsolutePath() + "/";
				homeProgramDir = realPath;
			}
		}
		return homeProgramDir;
	}

	/**
	 * STANDALONE NOJAR: <programpath>/bin/
	 * STANDALONE JAR: <programpath>/<progname>.jar
	 * ECLIPSE NOJAR: <programpath>
	 * ECLIPSE JAR: <eclipsepath>/<pluginname>.jar
	 */
	public static String executable() {
		String path = null;
		URL codeSourceUrl = Main.class.getProtectionDomain().getCodeSource().getLocation();
		try { //Convert URL to URI to avoid HTML problems with special characters like space,ä,ö,ü,...
			path = codeSourceUrl.toURI().getPath();
		} catch (URISyntaxException e) {/*path stays null*/}

		if (path == null) { // URI2URL Conversion failed, because URI.getPath() returned null OR because of an URISyntaxException
			// In this case use the URL and replace special characters manually (for now only space)
			path = codeSourceUrl.getPath().replace("%20", " ");
		}

		return path;
	}

}

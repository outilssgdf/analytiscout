package org.leplan73.analytiscout.gui.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.Properties;

import javax.swing.JLabel;

import com.jcabi.manifests.Manifests;

public class VersionCheck {
	
	private static final String VERSION_MODE = "version.mode";
	private static final String VERSION_SUBMINOR = "version.subminor";
	private static final String VERSION_MINOR = "version.minor";
	private static final String VERSION_MAJOR = "version.major";

	public static void check(JLabel lblVersionStatus)
	{
		Version version = VersionCheck.checkGithub();
		if (version != null)
		{
			try
			{
				Version v = Version.parse(Manifests.read("version"));
				if (v.compare(version) == true)
				{
					lblVersionStatus.setText("Mise à jour \""+version.toString()+"\" disponible");
					return;
				}
			}
			catch (java.lang.IllegalArgumentException e) {
			}
		}
		lblVersionStatus.setText("");
	}
	
	private static Version checkGithub()
	{
		Reader reader = null;
		try {
			URL url = new URL("https://outilssgdf.github.io/outilssgdf.version.properties");
			InputStream in = url.openStream();
			reader = new InputStreamReader(in, "UTF-8");
			 
			Properties prop = new Properties();
		    prop.load(reader);
		    
		    Version version = new Version();
		    version.update(prop.getProperty(VERSION_MAJOR), prop.getProperty(VERSION_MINOR),prop.getProperty(VERSION_SUBMINOR),prop.getProperty(VERSION_MODE));
		    return version;
		} catch (IOException e) {
		} finally {
		    try {
				if (reader != null) reader.close();
			} catch (IOException e) {
			}
		}
		return null;
	}
}

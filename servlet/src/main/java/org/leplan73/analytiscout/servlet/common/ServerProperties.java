package org.leplan73.analytiscout.servlet.common;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

public class ServerProperties 
{
	// Static class, cannot be instantiated
	private ServerProperties() {}
	
	public static URL getUrl() throws MalformedURLException {
		try {
			return new URL("http://"+InetAddress.getLocalHost().getHostName()+":"+8080+"/analytiscout/api");
		} catch (MalformedURLException | UnknownHostException e) {
			return new URL("http://localhost:"+8080+"/outilsgdf/api");
		}
	}
}

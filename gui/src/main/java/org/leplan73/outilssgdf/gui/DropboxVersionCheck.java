package org.leplan73.outilssgdf.gui;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.leplan73.outilssgdf.gui.utils.Version;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.users.FullAccount;

public class DropboxVersionCheck {

	private static final String ACCESS_TOKEN = "ORrK8d640iAAAAAAAAAD_-oqIWmuL2ufgmCcxHwjjGWH8d4Vrg2BsdEx0Tr1nk0o";
	
	public static String check(String versions) {
		ByteArrayOutputStream outputStream = null;
		try {
	        DbxRequestConfig config = new DbxRequestConfig("dropbox/outilssgdf", "en_US");
	        DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);

	        // Get current account info
	        FullAccount account;
			account = client.users().getCurrentAccount();
			
	        System.out.println(account.getName().getDisplayName());
	        
	        DbxDownloader<FileMetadata> downloader = client.files().download("/version.properties");
	        
	        outputStream = new ByteArrayOutputStream();
	        downloader.download(outputStream);
	        
	        String dpVersions = new String(outputStream.toByteArray());
	        Version dbVersion = Version.parse(dpVersions);
	        Version version = Version.parse(versions);
	        if (dbVersion.getMajor() > version.getMajor())
	        {
	        	return "Nouvelle version "+dbVersion.toString();
	        }
	        else if (dbVersion.getMajor() == version.getMajor())
	        {
	        	if (dbVersion.getMinor() > version.getMinor())
		        {
		        	return "Nouvelle version "+dbVersion.toString();
		        }
		        else if (dbVersion.getMinor() == version.getMinor())
		        {
		        	if (dbVersion.getSubMinor() > version.getSubMinor())
			        {
			        	return "Nouvelle version "+dbVersion.toString();
			        }
		        }
	        }
		} catch (DbxApiException e1) {
			e1.printStackTrace();
		} catch (DbxException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
        	try {
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
		return "";
	}
}

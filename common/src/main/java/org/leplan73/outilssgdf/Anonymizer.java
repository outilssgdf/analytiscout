package org.leplan73.outilssgdf;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Anonymizer {
	
	private List<String> noms_ = new ArrayList<String>();
	private List<String> prenoms_ = new ArrayList<String>();

	public final static String ROOT_FOLDER = "org/leplan73/outilssgdf";
	private final static Random randomNoms_ = new Random();
	private final static Random randomPrenoms_ = new Random();
	
	private List<String> lire(String nom) throws IOException
	{
		List<String> strings = new ArrayList<String>();
		URL url = Anonymizer.class.getClassLoader().getResource(ROOT_FOLDER + nom);
		BufferedInputStream bis = new BufferedInputStream(url.openStream());
		BufferedReader dis = new BufferedReader(new InputStreamReader(bis));
		String temp = null;
		try
		{
	        while((temp = dis.readLine()) != null){
	        	strings.add(temp);
	        }
		}
		catch(EOFException e)
		{
			bis.close();
		}
		return strings;
	}
	
	public void init()
	{
		try {
			noms_ = lire("/noms.txt");
			prenoms_ = lire("/prenoms.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String prochainNom()
	{
		int index = randomNoms_.nextInt(noms_.size());
		return noms_.get(index);
	}
	
	public String prochainPrenom()
	{
		int index = randomPrenoms_.nextInt(prenoms_.size());
		return prenoms_.get(index);
	}
}

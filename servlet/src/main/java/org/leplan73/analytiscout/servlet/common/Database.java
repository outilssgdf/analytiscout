package org.leplan73.analytiscout.servlet.common;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.activation.DataSource;
import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.leplan73.analytiscout.ParamSortie;
import org.leplan73.analytiscout.engine.EngineAnalyseurEnLigne;
import org.leplan73.analytiscout.engine.EngineDetection;
import org.leplan73.analytiscout.engine.EngineException;
import org.leplan73.analytiscout.engine.EngineDetection.Utilisateur;
import org.leplan73.analytiscout.intranet.LoginEngineException;
import org.leplan73.analytiscout.outils.CryptoException;
import org.leplan73.analytiscout.outils.PasswdCrypt;
import org.leplan73.analytiscout.outils.ResetableFileInputStream;
import org.leplan73.analytiscout.servlet.war.Logger;
import org.leplan73.analytiscout.servlet.war.WebProgress;

public class Database {
	private Connection conn_ = null;
	
	public class Requete
	{
		public int id;
		public String identifiant;
		public String motdepasse;
		public int code_structure;
		public boolean age = false;
		public boolean recursif = true;
		public boolean anonymiser = false;
		
		public boolean go(boolean responsables, boolean pargroupe) throws FileNotFoundException {
			
			InputStream fBatch = null;
			InputStream fModele = null;
			if (responsables)
			{
				fBatch = new ResetableFileInputStream(new FileInputStream(new File(Manager.getConf(),"conf/batch_responsables.txt")));
				fModele = new ResetableFileInputStream(new FileInputStream(new File(Manager.getConf(),"conf/modele_responsables.xlsx")));
			}
			else
			{
				fBatch = new ResetableFileInputStream(new FileInputStream(new File(Manager.getConf(),"conf/batch_jeunes.txt")));
				fModele = new ResetableFileInputStream(new FileInputStream(new File(Manager.getConf(),"conf/modele_jeunes.xlsx")));
			}
			
			WebProgress progress = new WebProgress();
			EngineAnalyseurEnLigne en = new EngineAnalyseurEnLigne(progress, Logger.get());
			try {
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

				ParamSortie psortie = new ParamSortie(outputStream);
				if (responsables)
				{
					en.go(identifiant, motdepasse, fBatch, fModele, code_structure, age, "tout_responsables", recursif, psortie, anonymiser, false, pargroupe);
				}
				else
				{
 					en.go(identifiant, motdepasse, fBatch, fModele, code_structure, age, "tout_jeunes", recursif, psortie, anonymiser, false, pargroupe);
				}
				
				byte[] fichier = outputStream.toByteArray();
				
				EngineDetection engineUtilisateur = new EngineDetection(progress, Logger.get());
				Utilisateur utilisateur = engineUtilisateur.go(identifiant, motdepasse);
				
				MultiPartEmail email = new MultiPartEmail();
				email.setHostName(Preferences.lit(Preferences.MAIL_SERVEUR_HOST, "", false));
				email.setSmtpPort(Preferences.liti(Preferences.MAIL_SERVEUR_PORT, 25));
				email.setAuthenticator(new DefaultAuthenticator(Preferences.lit(Preferences.MAIL_SERVEUR_UTILISATEUR, "", false), Preferences.lit(Preferences.MAIL_SERVEUR_MOTDEPASSE, "", true)));
				email.setSSLOnConnect(true);
				email.setFrom(Preferences.lit(Preferences.MAIL_SERVEUR_UTILISATEUR, "", false));
				if (responsables)
				{
					email.setSubject("Analyse des responsables de la structure "+code_structure);
					email.setMsg("Voici l'analyse des responsables de la structure "+code_structure);
				}
				else
				{
					email.setSubject("Analyse des jeunes de la structure "+code_structure);
					email.setMsg("Voici l'analyse des jeunes de la structure "+code_structure);
				}
				
				String dest = Preferences.lit(Preferences.MAIL_SERVEUR_ADRESSE_DEST, "", false);
				if (dest.isEmpty())
					email.addTo(utilisateur.email);
				else
					email.addTo(dest);
				
				DataSource source = new ByteArrayDataSource(fichier, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");  
				email.attach(source, "analyse.xlsx", "Analyse");
				  
				email.send();
				
				return true;
				
			} catch (EngineException | EmailException e) {
				Logger.get().error("Erreur mail",e);
			} catch (LoginEngineException e) {
				Logger.get().error("Erreur mail",e);
			}
			return false;
		} 
	}
	
	private void retirerRequete(String nom, int id)
	{
		Statement stmt = null;
		try {
			stmt = conn_.createStatement();
			stmt.executeUpdate("DELETE from "+nom+" where id="+id);
			stmt.close();
		} catch (SQLException e) {
			Logger.get().error("Erreur H2 " + e.getLocalizedMessage());
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				Logger.get().error("Erreur H2 " + e.getLocalizedMessage());
			}
		}
	}
	
	private Requete lireRequete(String nom) throws CryptoException
	{
		Requete requete = null;
		Statement stmt = null;
		try {
			stmt = conn_.createStatement();
			String sql = "select * from "+nom+" limit 1";
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next())
			{
				requete = new Requete();
				requete.id = rs.getInt(1);
				requete.identifiant = PasswdCrypt.decrypt(rs.getString(2));
				requete.motdepasse = PasswdCrypt.decrypt(rs.getString(3));
				requete.code_structure = rs.getInt(4);
				rs.close();
			}
		} catch (SQLException e) {
			Logger.get().error("Erreur H2 " + e.getLocalizedMessage());
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				Logger.get().error("Erreur H2 " + e.getLocalizedMessage());
			}
		}
		return requete;
	}

	private int addRequete(String nom, String identifiant, String motdepasse, int code_structure) throws CryptoException {
		int nb = 0;
		Statement stmt = null;
		try {
			stmt = conn_.createStatement();
			String sql = "INSERT INTO "+nom+" VALUES (NULL,'"+PasswdCrypt.encrypt(identifiant)+"','"+PasswdCrypt.encrypt(motdepasse)+"',"+code_structure+")";
			stmt.executeUpdate(sql);
			stmt.close();
			
			stmt = conn_.createStatement();
			sql = "select count(*) from REQUETES_RESPONSABLES";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				nb = rs.getInt(1);
			}
			rs.close();
			
		} catch (SQLException e) {
			Logger.get().error("Erreur H2 " + e.getLocalizedMessage());
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				Logger.get().error("Erreur H2 " + e.getLocalizedMessage());
			}
		}
		return nb;
	}
	
	public Requete lireRequeteResponsables() throws CryptoException
	{
		Requete requete = lireRequete("REQUETES_RESPONSABLES");
		return requete;
	}
	
	public Requete lireRequeteJeunes() throws CryptoException
	{
		Requete requete = lireRequete("REQUETES_JEUNES");
		return requete;
	}

	public int addRequeteResponsables(String identifiant, String motdepasse, int code_structure) throws CryptoException {
		int nb = addRequete("REQUETES_RESPONSABLES",identifiant,motdepasse,code_structure);
		return nb;
	}

	public int addRequeteJeunes(String identifiant, String motdepasse, int code_structure) throws CryptoException {
		int nb = addRequete("REQUETES_JEUNES",identifiant,motdepasse,code_structure);
		return nb;
	}

	public void init(File path) {
		Statement stmt = null;
		try {
			Class.forName("org.h2.Driver");
			File fichierh2 = new File(path, "données/db.h2");
			conn_ = DriverManager.getConnection("jdbc:h2:" + fichierh2.getPath(), "analytiscout", "analytiscout");
			stmt = conn_.createStatement();
			stmt.executeUpdate(
					"CREATE TABLE IF NOT EXISTS requetes_responsables (id INTEGER not NULL AUTO_INCREMENT, identifiant VARCHAR(255) not NULL, motdepasse VARCHAR(255) not NULL, code_structure INTEGER not NULL, PRIMARY KEY ( id ))");
			stmt.close();
			stmt = conn_.createStatement();
			stmt.executeUpdate(
					"CREATE TABLE IF NOT EXISTS requetes_jeunes (id INTEGER not NULL AUTO_INCREMENT, identifiant VARCHAR(255) not NULL, motdepasse VARCHAR(255) not NULL, code_structure INTEGER not NULL, PRIMARY KEY ( id ))");
			stmt.close();
		} catch (ClassNotFoundException | SQLException e) {
			Logger.get().error("Erreur H2 " + e.getLocalizedMessage());
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				Logger.get().error("Erreur H2 " + e.getLocalizedMessage());
			}
		}
	}

	public void uninit() {
		try {
			if (conn_ != null)
				conn_.close();
		} catch (SQLException e) {
			Logger.get().error("Erreur H2 " + e.getLocalizedMessage());
		}
	}
	
	public void supprimerRequetesJeunes(int id) {
		retirerRequete("REQUETES_JEUNES", id);
	}

	public void supprimerRequetesResponsables(int id) {
		retirerRequete("REQUETES_RESPONSABLES", id);
	}

	public List<Requete> listRequetesJeunes(boolean decrypt) {
		List<Requete> requetes = listRequete("REQUETES_JEUNES", decrypt);
		return requetes;
	}

	public List<Requete> listRequetesResponsables(boolean decrypt) {
		List<Requete> requetes = listRequete("REQUETES_RESPONSABLES", decrypt);
		return requetes;
	}
	
	private List<Requete> listRequete(String nom, boolean decrypt)
	{
		List<Requete> requetes = new ArrayList<Requete>();
		Statement stmt = null;
		try {
			stmt = conn_.createStatement();
			String sql = "select * from "+nom;
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				Requete requete = new Requete();
				requete.id = rs.getInt(1);
				requete.identifiant = decrypt ? PasswdCrypt.decrypt(rs.getString(2)) : "***";
				requete.motdepasse = decrypt ? PasswdCrypt.decrypt(rs.getString(3)) : "***";
				requete.code_structure = rs.getInt(4);
				requetes.add(requete);
			}
			rs.close();

		} catch (SQLException | CryptoException e) {
			Logger.get().error("Erreur H2 " + e.getLocalizedMessage());
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				Logger.get().error("Erreur H2 " + e.getLocalizedMessage());
			}
		}
		return requetes;
	}
}

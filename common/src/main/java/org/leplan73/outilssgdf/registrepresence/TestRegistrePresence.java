package org.leplan73.outilssgdf.registrepresence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;

import net.sf.jett.transform.ExcelTransformer;

public class TestRegistrePresence {

	public static void go(File modele, Map<String, Object> beans, File sortie) throws InvalidFormatException, IOException
	{
		FileOutputStream outputStream = new FileOutputStream(sortie);
		InputStream inModele = new FileInputStream(modele);
		ExcelTransformer trans = new ExcelTransformer();
		Workbook workbook = trans.transform(inModele, beans);
		workbook.write(outputStream);
		inModele.close();
		outputStream.flush();
		outputStream.close();
	}
	
	public static void main(String[] args) throws InvalidFormatException, IOException {
		System.setProperty("line.separator","\n");
		ExtracteurRegistrePresence ex = new ExtracteurRegistrePresence();
		final int anneeDebut = ex.charge(new FileInputStream(new File("C:\\dev\\outilssgdf_data\\registrepresence_2018_2019_f.csv")))+1;
//		final int anneeDebut = ex.charge(new FileInputStream(new File("C:\\dev\\outilssgdf_data\\2018\\registrepresence-1.csv")))+1;
//		ex.charge(new FileInputStream(new File("C:\\dev\\outilssgdf_data\\2018\\registrepresence-2.csv")));
//		ex.exportInfluxDb(new File("C:\\dev\\outilssgdf_data\\export.txt"));
		
		List<RegistrePresenceActiviteHeure> activites = new ArrayList<RegistrePresenceActiviteHeure>();
		List<RegistrePresenceActiviteHeure> activitesForfaitaire = new ArrayList<RegistrePresenceActiviteHeure>();
		List<RegistrePresenceActiviteHeure> activites_jeunes = new ArrayList<RegistrePresenceActiviteHeure>();
		List<RegistrePresenceActiviteHeure> activites_chefs = new ArrayList<RegistrePresenceActiviteHeure>();
		List<RegistrePresenceActiviteHeure> activites_heures_cec = new ArrayList<RegistrePresenceActiviteHeure>();
		List<RegistrePresenceActiviteHeure> activites_cec = new ArrayList<RegistrePresenceActiviteHeure>();
		ex.getActivites(activites, activitesForfaitaire, activites_jeunes, activites_chefs);
		ex.getActivitesCec(anneeDebut, activites_heures_cec, activites_cec);
		
		List<RegistrePresenceActivite> activites_total = new ArrayList<RegistrePresenceActivite>();
		ex.construitActivites(activites_total);
		
		Collection<RegistrePresenceUnite> unites = ex.getUnites();
		unites.forEach(unite ->
		{
			Set<String> chefs = unite.getChefs();
			chefs.forEach(chef ->
			{
				List<RegistrePresenceActiviteHeure> activites_cec_chef = new ArrayList<RegistrePresenceActiviteHeure>();
				ex.getActivitesCecChef(chef, unite, anneeDebut, activites_cec_chef);
				
				Map<String, Object> beans = new HashMap<String, Object>();
				beans.put("annee", anneeDebut);
				beans.put("activites_cec", activites_cec_chef);
				try {
					go(new File("C:\\dev\\outilssgdf\\fichiers\\conf\\modele_cec.xlsx"), beans, new File("C:\\dev\\outilssgdf_data\\CEC-"+anneeDebut+"-"+chef+".xlsx"));
				} catch (InvalidFormatException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		});
		
		
		Map<String, Object> beans = new HashMap<String, Object>();
		beans.put("unites", ex.getUnites());
		beans.put("activites", activites_total);
		beans.put("activites_heures", activites);
		beans.put("activites_forfaitaires", activitesForfaitaire);
		beans.put("activites_heures_cec", activites_heures_cec);
		beans.put("activites_cec", activites_cec);
		beans.put("activites_heures_jeunes", activites_jeunes);
		beans.put("activites_heures_chefs", activites_chefs);
		go(new File("C:\\dev\\outilssgdf\\fichiers\\conf\\modele_registrepresence.xlsx"), beans, new File("C:\\dev\\outilssgdf_data\\registrepresence.xlsx"));
	}
}

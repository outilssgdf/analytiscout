package org.leplan73.outilssgdf.registrepresence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		ex.charge(new FileInputStream(new File("C:\\dev\\outilssgdf_data\\registrepresence.csv")));
//		ex.exportInfluxDb();
		
		List<RegistrePresenceActiviteHeure> activites = new ArrayList<RegistrePresenceActiviteHeure>();
		List<RegistrePresenceActiviteHeure> activites_jeunes = new ArrayList<RegistrePresenceActiviteHeure>();
		List<RegistrePresenceActiviteHeure> activites_chefs = new ArrayList<RegistrePresenceActiviteHeure>();
		ex.getActivites(activites, activites_jeunes, activites_chefs);
		
		List<RegistrePresenceActivite> activites_total = new ArrayList<RegistrePresenceActivite>();
		ex.construitActivites(activites_total);
		
		Map<String, Object> beans = new HashMap<String, Object>();
		beans.put("unites", ex.getUnites());
		beans.put("activites", activites_total);
		beans.put("activites_heures", activites);
		beans.put("activites_heures_jeunes", activites_jeunes);
		beans.put("activites_heures_chefs", activites_chefs);
		
		go(new File("C:\\dev\\outilssgdf\\fichiers\\conf\\modele_registrepresence.xlsx"), beans, new File("C:\\dev\\outilssgdf_data\\registrepresence.xlsx"));
	}
}

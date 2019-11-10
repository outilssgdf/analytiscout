package org.leplan73.outilssgdf.formatage;

import java.io.IOException;

import org.apache.commons.csv.CSVPrinter;
import org.leplan73.outilssgdf.calcul.Unites;
import org.leplan73.outilssgdf.extraction.Adherent;
import org.leplan73.outilssgdf.extraction.Adherents;
import org.leplan73.outilssgdf.extraction.ColonnesAdherents;

public class CsvMySqlFormatteur {
	
	public void genereEmail(int id, String nom,Unites unites, Adherents adherents, ColonnesAdherents colonnes, CSVPrinter out, boolean init, String groupe) throws IOException {
		adherents.forEach((key,adherent) -> {
			try {
				liste(id,nom, colonnes, adherent,out, groupe);
			} catch (IOException e) {
			}
		});
	}

	private void liste(int id, String nom, ColonnesAdherents colonnes, Adherent adherent, CSVPrinter out, String groupe) throws IOException {
		adherent.liste(id,nom, out, groupe);
	}
}

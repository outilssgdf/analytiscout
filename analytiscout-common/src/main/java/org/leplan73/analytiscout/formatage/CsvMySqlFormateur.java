package org.leplan73.analytiscout.formatage;

import java.io.IOException;

import org.apache.commons.csv.CSVPrinter;
import org.leplan73.analytiscout.calcul.Unites;
import org.leplan73.analytiscout.extraction.Adherent;
import org.leplan73.analytiscout.extraction.Adherents;
import org.leplan73.analytiscout.extraction.ColonnesAdherents;

public class CsvMySqlFormateur {
	
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

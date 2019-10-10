package org.leplan73.outilssgdf.servlet.war;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.jdom2.JDOMException;
import org.leplan73.outilssgdf.Consts;
import org.leplan73.outilssgdf.ExtracteurExtraHtml;
import org.leplan73.outilssgdf.ExtracteurIndividusHtml;
import org.leplan73.outilssgdf.ExtractionException;
import org.leplan73.outilssgdf.Transformeur;
import org.leplan73.outilssgdf.TransformeurException;
import org.leplan73.outilssgdf.calcul.General;
import org.leplan73.outilssgdf.calcul.Global;
import org.leplan73.outilssgdf.extraction.AdherentForme.ExtraKey;
import org.leplan73.outilssgdf.extraction.AdherentFormes;
import org.leplan73.outilssgdf.intranet.ExtractionAdherents;
import org.leplan73.outilssgdf.intranet.ExtractionIntranet;

import com.jcabi.manifests.Manifests;

import io.swagger.annotations.Api;

@Path("/v1")
@Api(value = "Server")
public class Server {
	
	@POST
    @Path("/analyseenligne")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response analyseenligne(@FormDataParam(value = "identifiant") String identifiant, @FormDataParam(value = "password") String motdepasse, @FormDataParam(value = "code_structure") String code_structure) throws ExtractionException, IOException, JDOMException, InvalidFormatException, TransformeurException {

		Properties pbatch = new Properties();

		File fModele = new File("conf/modele_responsables.xlsx");
		File fBatch = new File("./conf/batch_responsables.txt");
		pbatch.load(new FileInputStream(fBatch));
		
		ExtractionAdherents app = new ExtractionAdherents();
		app.init();
		if (app.login(identifiant, new String(motdepasse)) == false) {
			throw new IOException("erreur de connexion");
		}
		
		int structure = Integer.parseInt(code_structure);

			Map<ExtraKey, ExtracteurExtraHtml> extraMap = new TreeMap<ExtraKey, ExtracteurExtraHtml>();
			
			String donneesAdherents=null;
			int index = 1;
			for (;;) {
				// generateur.x
				// format.x
				// categorie.x
				// specialite.x
				// fonction.x
				// diplome.x
				// qualif.x
				// formation.x
				// nom.x
				// type.x
				String generateur = pbatch.getProperty("generateur." + index);
				if (generateur == null) {
					break;
				}
				int diplome = pbatch.getProperty("diplome." + index, "").isEmpty()
						? ExtractionIntranet.DIPLOME_TOUT
						: Integer.parseInt(pbatch.getProperty("diplome." + index));
				int qualif = pbatch.getProperty("qualif." + index, "").isEmpty()
						? ExtractionIntranet.QUALIFICATION_TOUT
						: Integer.parseInt(pbatch.getProperty("qualif." + index));
				int formation = pbatch.getProperty("formation." + index, "").isEmpty()
						? ExtractionIntranet.FORMATION_TOUT
						: Integer.parseInt(pbatch.getProperty("formation." + index));
				int format = pbatch.getProperty("format." + index, "").isEmpty()
						? ExtractionIntranet.FORMAT_INDIVIDU
						: Integer.parseInt(pbatch.getProperty("format." + index));
				int categorie = pbatch.getProperty("categorie." + index, "").isEmpty()
						? ExtractionIntranet.CATEGORIE_TOUT
						: Integer.parseInt(pbatch.getProperty("categorie." + index));
				int type = pbatch.getProperty("type." + index, "").isEmpty() ? ExtractionIntranet.TYPE_TOUT
						: Integer.parseInt(pbatch.getProperty("type." + index));
				int specialite = pbatch.getProperty("specialite." + index, "").isEmpty()
						? ExtractionIntranet.SPECIALITE_SANS_IMPORTANCE
						: Integer.parseInt(pbatch.getProperty("specialite." + index));
				boolean adherentsseuls = pbatch.getProperty("adherents." + index, "").isEmpty() ? false
						: Boolean.parseBoolean(pbatch.getProperty("adherents." + index));
				String nom = pbatch.getProperty("nom." + index, "");
				String fonction = pbatch.getProperty("fonction." + index);

				ExtraKey extra = new ExtraKey(pbatch.getProperty("fichier." + index, nom), nom, pbatch.getProperty("batchtype." + index, "tout_responsables"));
				
				String donnees = app.extract(structure,true,type,adherentsseuls,fonction,specialite,categorie, diplome,qualif,formation,format, false);
				
				if (extra.ifTout()) {
					donneesAdherents = donnees;
				} else {
					InputStream in = new ByteArrayInputStream(donnees.getBytes(Consts.ENCODING_UTF8));
					extraMap.put(extra, new ExtracteurExtraHtml(in, true));
				}
				index++;
			}
			
			InputStream in = new ByteArrayInputStream(donneesAdherents.getBytes(Consts.ENCODING_UTF8));
			ExtracteurIndividusHtml adherents = new ExtracteurIndividusHtml(in, extraMap,true,false);
	 
			AdherentFormes compas = new AdherentFormes();
			compas.charge(adherents,extraMap);
			
			String version = "";
			try
			{
				version = Manifests.read("version");
			}
			catch(java.lang.IllegalArgumentException e) {
			}
			General general = new General(version);
			Global global = new Global(adherents.getGroupe(), adherents.getMarins());
			adherents.calculGlobal(global);
	
			Map<String, Object> beans = new HashMap<String, Object>();
			beans.put("chefs", adherents.getChefsList());
			beans.put("compas", adherents.getCompasList());
			beans.put("unites", adherents.getUnitesList());
			beans.put("general", general);
			beans.put("global", global);

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			Transformeur.go(fModele, beans, outputStream);
			app.close();
		return Response.ok(outputStream).type(MediaType.TEXT_PLAIN).header("Content-Disposition","attachment; filename=\"file.zip\"").build();
	}
}

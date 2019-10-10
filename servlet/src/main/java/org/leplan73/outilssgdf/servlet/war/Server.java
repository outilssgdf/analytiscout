package org.leplan73.outilssgdf.servlet.war;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.jdom2.JDOMException;
import org.leplan73.outilssgdf.ExtractionException;
import org.leplan73.outilssgdf.TransformeurException;
import org.leplan73.outilssgdf.engine.EngineAnalyseurEnLigne;
import org.leplan73.outilssgdf.engine.EngineException;

import io.swagger.annotations.Api;

@Path("/v1")
@Api(value = "Server")
public class Server {
	
	static protected int[] construitStructures(String txfCodeStructure)
	{
		String stStructures[] = txfCodeStructure.split(",");
		int structures[] = new int[stStructures.length];
		int index = 0;
		for (String stStructure : stStructures)
		{
			structures[index++] = Integer.parseInt(stStructure);
		}
		return structures;
	}
	
	@POST
    @Path("/analyseenligne")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response analyseenligne(@FormDataParam(value = "identifiant") String identifiant, @FormDataParam(value = "password") String motdepasse, @FormDataParam(value = "code_structure") String code_structure, @FormDataParam(value = "age") boolean age, @FormDataParam(value = "recursif") boolean recursif, @FormDataParam(value = "anonymiser") boolean anonymiser) throws ExtractionException, IOException, JDOMException, InvalidFormatException, TransformeurException {

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream fModele = classLoader.getResourceAsStream("conf/modele_responsables.xlsx");
		InputStream fBatch = classLoader.getResourceAsStream("conf/batch_responsables.txt");
		
		int[] structures = construitStructures(code_structure);
		
		WebProgress progress = new WebProgress();
		EngineAnalyseurEnLigne en = new EngineAnalyseurEnLigne(progress, Logger.get());
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			en.go(identifiant, motdepasse, fBatch, fModele, null, structures[0], age, "tout_responsables", recursif, "responsables_", outputStream, anonymiser);
			return Response.ok(outputStream).type(MediaType.APPLICATION_OCTET_STREAM).header("Content-Disposition","attachment; filename=\"analyse.xlsx\"").build();
		} catch (EngineException e1) {
			return Response.serverError().build();
		}
	}
}

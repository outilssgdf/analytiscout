package org.leplan73.outilssgdf.servlet.war;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.jdom2.JDOMException;
import org.leplan73.outilssgdf.ExtractionException;
import org.leplan73.outilssgdf.ParamSortie;
import org.leplan73.outilssgdf.TransformeurException;
import org.leplan73.outilssgdf.engine.EngineAnalyseurEnLigne;
import org.leplan73.outilssgdf.engine.EngineException;
import org.leplan73.outilssgdf.engine.LoginEngineException;
import org.leplan73.outilssgdf.outils.CryptoException;
import org.leplan73.outilssgdf.outils.ResetableFileInputStream;
import org.leplan73.outilssgdf.servlet.common.Manager;

import io.swagger.annotations.Api;

@Path("/v1")
@Api(value = "Server")
public class Server {
	
	static private int[] construitStructures(String txfCodeStructure)
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
    @Path("/analyseenligne/jeunes/email")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response analyseenligne_jeunes_email(@FormDataParam(value = "identifiant") String identifiant, @FormDataParam(value = "password") String motdepasse, @FormDataParam(value = "code_structure") String code_structure, @DefaultValue("true") @FormDataParam(value = "age") boolean age, @DefaultValue("true") @FormDataParam(value = "recursif") boolean recursif, @DefaultValue("false") @FormDataParam(value = "anonymiser") boolean anonymiser) throws ExtractionException, IOException, JDOMException, InvalidFormatException, TransformeurException {

		int[] structures = construitStructures(code_structure);
		int nb;
		try {
			nb = Manager.getDb().addRequeteJeunes(identifiant, motdepasse, structures[0]);
			return Response.ok(""+nb).build();
		} catch (CryptoException e) {
			throw new WebApplicationException(e);
		}
	}
	
	@POST
    @Path("/analyseenligne/jeunes")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response analyseenligne_jeunes(@FormDataParam(value = "identifiant") String identifiant, @FormDataParam(value = "password") String motdepasse, @FormDataParam(value = "code_structure") String code_structure, @DefaultValue("true") @FormDataParam(value = "age") boolean age, @DefaultValue("true") @FormDataParam(value = "recursif") boolean recursif, @DefaultValue("false") @FormDataParam(value = "anonymiser") boolean anonymiser) throws ExtractionException, IOException, JDOMException, InvalidFormatException, TransformeurException {

		int[] structures = construitStructures(code_structure);
		
		InputStream fBatch = new ResetableFileInputStream(new FileInputStream(new File(Manager.getConf(),"conf/batch_jeunes.txt")));
		InputStream fModele = new ResetableFileInputStream(new FileInputStream(new File(Manager.getConf(),"conf/modele_jeunes.xlsx")));
		
		WebProgress progress = new WebProgress();
		EngineAnalyseurEnLigne en = new EngineAnalyseurEnLigne(progress, Logger.get());
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

			ParamSortie psortie = new ParamSortie(outputStream);
			en.go(identifiant, motdepasse, fBatch, fModele, structures[0], age, "tout_jeunes", recursif, psortie, anonymiser, false);
			return Response.ok(outputStream.toByteArray()).type(MediaType.TEXT_PLAIN_TYPE).header("Content-Disposition","attachment; filename=\"analyse_jeunes.xlsx\"").build();
		} catch (EngineException e) {
			throw new WebApplicationException(e);
		} catch (LoginEngineException e) {
			throw new WebApplicationException(e);
		}
	}
	
	@POST
    @Path("/analyseenligne/responsables/email")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response analyseenligne_responsables_email(@FormDataParam(value = "identifiant") String identifiant, @FormDataParam(value = "password") String motdepasse, @FormDataParam(value = "code_structure") String code_structure, @DefaultValue("true") @FormDataParam(value = "age") boolean age, @DefaultValue("true") @FormDataParam(value = "recursif") boolean recursif, @DefaultValue("false") @FormDataParam(value = "anonymiser") boolean anonymiser) throws ExtractionException, IOException, JDOMException, InvalidFormatException, TransformeurException {

		int[] structures = construitStructures(code_structure);
		int nb;
		try {
			nb = Manager.getDb().addRequeteResponsables(identifiant, motdepasse, structures[0]);
			return Response.ok(""+nb).build();
		} catch (CryptoException e) {
			throw new WebApplicationException(e);
		}
	}
	
	@POST
    @Path("/analyseenligne/responsables")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response analyseenligne_responsables(@FormDataParam(value = "identifiant") String identifiant, @FormDataParam(value = "password") String motdepasse, @FormDataParam(value = "code_structure") String code_structure, @DefaultValue("true") @FormDataParam(value = "age") boolean age, @DefaultValue("true") @FormDataParam(value = "recursif") boolean recursif, @DefaultValue("false") @FormDataParam(value = "anonymiser") boolean anonymiser) throws ExtractionException, IOException, JDOMException, InvalidFormatException, TransformeurException {

		int[] structures = construitStructures(code_structure);
		
		InputStream fBatch = new ResetableFileInputStream(new FileInputStream(new File(Manager.getConf(),"conf/batch_jeunes.txt")));
		InputStream fModele = new ResetableFileInputStream(new FileInputStream(new File(Manager.getConf(),"conf/modele_jeunes.xlsx")));
		
		WebProgress progress = new WebProgress();
		EngineAnalyseurEnLigne en = new EngineAnalyseurEnLigne(progress, Logger.get());
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

			ParamSortie psortie = new ParamSortie(outputStream);
			en.go(identifiant, motdepasse, fBatch, fModele, structures[0], age, "tout_responsables", recursif, psortie, anonymiser, false);
			return Response.ok(outputStream.toByteArray()).type(MediaType.TEXT_PLAIN_TYPE).header("Content-Disposition","attachment; filename=\"analyse_responsables.xlsx\"").build();
		} catch (EngineException e) {
			throw new WebApplicationException(e);
		} catch (LoginEngineException e) {
			throw new WebApplicationException(e);
		}
	}
}

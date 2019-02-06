package org.leplan73.outilssgdf.servlet.war;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipOutputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.glassfish.jersey.media.multipart.FormDataParam;
import org.jdom2.JDOMException;
import org.leplan73.outilssgdf.Check;
import org.leplan73.outilssgdf.ExtracteurIndividusHtml;
import org.leplan73.outilssgdf.ExtracteurXlsx;
import org.leplan73.outilssgdf.ExtractionException;
import org.leplan73.outilssgdf.calcul.Unites;
import org.leplan73.outilssgdf.formatage.GmailCsvFormatteur;
import org.leplan73.outilssgdf.intranet.ExtractionAdherents;
import org.leplan73.outilssgdf.intranet.ExtractionIntranet;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;

@Path("/v1")
@Api(value = "Server")
public class Server {
	
	@POST
    @Path("/check")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response check(@FormDataParam("fichier") InputStream fichier) throws ExtractionException, IOException {
		ExtracteurXlsx x = new ExtracteurXlsx();
		x.charge(fichier,true);
		
		List<Check> checks = new ArrayList<Check>();
		Unites unites = x.getUnites(); 
		unites.forEach((k,v) ->
		{
			x.getAdherents().check(x.getColonnes(), v, checks);
		});
		return Response.ok(checks).build();
	}
	
	@GET
    @Path("/telechargebrut")
	@Produces(MediaType.TEXT_PLAIN)
    public Response telechargebrut(@ApiParam(value = "identifiant", type = "string", format = "password") @QueryParam(value = "identifiant") String identifiant, @ApiParam(value = "mot de passe", type = "string", format = "password") @QueryParam(value = "password") String motdepasse) throws ExtractionException, IOException, JDOMException {

		// Charge le fichier
		ExtractionAdherents app = new ExtractionAdherents();
		app.init();
		if (app.login(identifiant,motdepasse) == false) return Response.status(404).build();
		String donnees = app.extract(0, true, ExtractionIntranet.TYPE_TOUT, true, null, ExtractionIntranet.SPECIALITE_SANS_IMPORTANCE, ExtractionIntranet.CATEGORIE_TOUT, ExtractionIntranet.DIPLOME_TOUT,ExtractionIntranet.QUALIFICATION_TOUT,ExtractionIntranet.FORMATION_TOUT, ExtractionIntranet.FORMAT_INDIVIDU,false);
		app.close();
		
		return Response.ok(donnees).type(MediaType.TEXT_PLAIN).header("Content-Disposition","attachment; filename=\"file.xml\"").build();
	}
	
	@POST
    @Path("/telecharge")
	@Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response telechargep(@FormParam(value = "identifiant") String identifiant, @FormParam(value = "password") String motdepasse) throws ExtractionException, IOException, JDOMException {

		// Charge le fichier
		ExtractionAdherents app = new ExtractionAdherents();
		app.init();
		if (app.login(identifiant,motdepasse) == false) return Response.status(404).build();
		String donnees = app.extract(0, true, ExtractionIntranet.TYPE_TOUT, true, null, ExtractionIntranet.SPECIALITE_SANS_IMPORTANCE, ExtractionIntranet.CATEGORIE_TOUT, ExtractionIntranet.DIPLOME_TOUT,ExtractionIntranet.QUALIFICATION_TOUT,ExtractionIntranet.FORMATION_TOUT, ExtractionIntranet.FORMAT_INDIVIDU,false);
		app.close();
		
		ExtracteurIndividusHtml x = new ExtracteurIndividusHtml();
		x.charge(new ByteArrayInputStream(donnees.getBytes(Charset.forName("UTF-8"))),true);
		GmailCsvFormatteur f = new GmailCsvFormatteur();
		
		// Convertit le fichier en archive zip
		StreamingOutput streamingOutput = outputStream -> {
	        ZipOutputStream zipOut = new ZipOutputStream(new BufferedOutputStream(outputStream));
	        f.genereEmail(x.getUnites(), x.getParents(), x.getAdherents(), x.getColonnes(), null, zipOut);
	        zipOut.close();
	        outputStream.flush();
	        outputStream.close();
		};
	        
		return Response.ok(streamingOutput).type(MediaType.TEXT_PLAIN).header("Content-Disposition","attachment; filename=\"file.zip\"").build();
	}
	
	@GET
    @Path("/telecharge")
	@Produces(MediaType.TEXT_PLAIN)
    public Response telecharge(@ApiParam(value = "identifiant", type = "string", format = "password") @QueryParam(value = "identifiant") String identifiant, @ApiParam(value = "mot de passe", type = "string", format = "password") @QueryParam(value = "password") String motdepasse) throws ExtractionException, IOException, JDOMException {

		// Charge le fichier
		ExtractionAdherents app = new ExtractionAdherents();
		app.init();
		if (app.login(identifiant,motdepasse) == false) return Response.status(404).build();
		String donnees = app.extract(0, true, ExtractionIntranet.TYPE_TOUT, true, null, ExtractionIntranet.SPECIALITE_SANS_IMPORTANCE, ExtractionIntranet.CATEGORIE_TOUT, ExtractionIntranet.DIPLOME_TOUT,ExtractionIntranet.QUALIFICATION_TOUT,ExtractionIntranet.FORMATION_TOUT, ExtractionIntranet.FORMAT_INDIVIDU,false);
		app.close();
		
		ExtracteurIndividusHtml x = new ExtracteurIndividusHtml();
		x.charge(new ByteArrayInputStream(donnees.getBytes(Charset.forName("UTF-8"))),true);
		GmailCsvFormatteur f = new GmailCsvFormatteur();
		
		// Convertit le fichier en archive zip
		StreamingOutput streamingOutput = outputStream -> {
	        ZipOutputStream zipOut = new ZipOutputStream(new BufferedOutputStream(outputStream));
	        f.genereEmail(x.getUnites(), x.getParents(), x.getAdherents(), x.getColonnes(), null, zipOut);
	        zipOut.close();
	        outputStream.flush();
	        outputStream.close();
		};
	        
		return Response.ok(streamingOutput).type(MediaType.TEXT_PLAIN).header("Content-Disposition","attachment; filename=\"file.zip\"").build();
	}
	
	@POST
    @Path("/convert")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.TEXT_PLAIN)
    public Response upload(@FormDataParam("fichier") InputStream fichier) throws ExtractionException, IOException {

		// Charge le fichier
		ExtracteurXlsx x = new ExtracteurXlsx();
		x.charge(fichier,true);
		GmailCsvFormatteur f = new GmailCsvFormatteur();
		
		// Convertit le fichier en archive zip
		StreamingOutput streamingOutput = outputStream -> {
	        ZipOutputStream zipOut = new ZipOutputStream(new BufferedOutputStream(outputStream));
	        f.genereEmail(x.getUnites(), x.getParents(), x.getAdherents(), x.getColonnes(), null, zipOut);
	        zipOut.close();
	        outputStream.flush();
	        outputStream.close();
		};
	        
		return Response.ok(streamingOutput).type(MediaType.TEXT_PLAIN).header("Content-Disposition","attachment; filename=\"file.zip\"").build();
	}
}

package org.leplan73.outilssgdf.servlet.war;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipOutputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.glassfish.jersey.media.multipart.FormDataParam;
import org.leplan73.outilssgdf.Check;
import org.leplan73.outilssgdf.ExtracteurXlsx;
import org.leplan73.outilssgdf.ExtractionException;
import org.leplan73.outilssgdf.extraction.Unite;
import org.leplan73.outilssgdf.formatage.GmailCsvFormatteur;

import io.swagger.annotations.Api;

@Path("/v1")
@Api(value = "Server")
public class Server {
	
	@POST
    @Path("/check")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response check(@FormDataParam("fichier") InputStream fichier) throws ExtractionException, IOException {
		ExtracteurXlsx x = new ExtracteurXlsx();
		x.charge(fichier);
		
		List<Check> checks = new ArrayList<Check>();
		Set<Unite> unites = x.getAdherents().unites(x.getColonnes());
		for (Unite unite : unites)
		{
			x.getAdherents().check(x.getColonnes(), unite, checks);
		}
		return Response.ok(checks).build();
	}
	
	@POST
    @Path("/convert")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.TEXT_PLAIN)
    public Response upload2(@FormDataParam("fichier") InputStream fichier) throws ExtractionException, IOException {

		// Charge le fichier
		ExtracteurXlsx x = new ExtracteurXlsx();
		x.charge(fichier);
		GmailCsvFormatteur f = new GmailCsvFormatteur();
		
		// Convertit le fichier en archive zip
		StreamingOutput streamingOutput = outputStream -> {
	        ZipOutputStream zipOut = new ZipOutputStream(new BufferedOutputStream(outputStream));
	        f.genereEmail(x.getParents(), x.getAdherents(), x.getColonnes(), null, zipOut);
	        zipOut.close();
	        outputStream.flush();
	        outputStream.close();
		};
	        
		return Response.ok(streamingOutput).type(MediaType.TEXT_PLAIN).header("Content-Disposition","attachment; filename=\"file.zip\"").build();
	}
}

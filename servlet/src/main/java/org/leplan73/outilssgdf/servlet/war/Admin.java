package org.leplan73.outilssgdf.servlet.war;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jdom2.JDOMException;
import org.leplan73.outilssgdf.ExtractionException;
import org.leplan73.outilssgdf.TransformeurException;
import org.leplan73.outilssgdf.servlet.common.Database.Requete;
import org.leplan73.outilssgdf.servlet.common.Manager;

import io.swagger.annotations.Api;

@Path("/v1")
@Api(value = "Admin")
public class Admin {
	
	@GET
    @Path("/analyseenligne_jeunes_email/lister")
    public List<Requete> lister_analyseenligne_jeunes_email(@DefaultValue("false") @QueryParam(value = "decrypt") boolean decrypt) throws ExtractionException, IOException, JDOMException, InvalidFormatException, TransformeurException {
		List<Requete> requetes = Manager.getDb().listRequetesJeunes(decrypt);
		return requetes;
	}
	
	@DELETE
    @Path("/analyseenligne_jeunes_email/supprimer")
    public Response supprimer_analyseenligne_jeunes_email(@QueryParam(value = "id") int id) throws ExtractionException, IOException, JDOMException, InvalidFormatException, TransformeurException {
		Manager.getDb().supprimerRequetesJeunes(id);
		return Response.ok().build();
	}
	
	@GET
    @Path("/analyseenligne_responsables_email/list")
    public List<Requete> lister_analyseenligne_responsables_email(@DefaultValue("false") @QueryParam(value = "decrypt") boolean decrypt) throws ExtractionException, IOException, JDOMException, InvalidFormatException, TransformeurException {
		List<Requete> requetes = Manager.getDb().listRequetesResponsables(decrypt);
		return requetes;
	}
	
	@DELETE
    @Path("/analyseenligne_responsables_email/supprimer")
    public Response supprimer_analyseenligne_responsables_email(@QueryParam(value = "id") int id) throws ExtractionException, IOException, JDOMException, InvalidFormatException, TransformeurException {
		Manager.getDb().supprimerRequetesJeunes(id);
		return Response.ok().build();
	}
}

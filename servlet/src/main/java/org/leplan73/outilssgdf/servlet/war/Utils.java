package org.leplan73.outilssgdf.servlet.war;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.multipart.FormDataParam;
import org.leplan73.outilssgdf.outils.CryptoException;
import org.leplan73.outilssgdf.outils.PasswdCrypt;

import io.swagger.annotations.Api;

@Path("/v1")
@Api(value = "Utils")
public class Utils {
	
	@POST
    @Path("/encrypt")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
    public String encrypt(@FormDataParam(value = "texte") String texte) {
		try {
			return PasswdCrypt.encrypt(texte);
		}
		catch(CryptoException e) {
			throw new WebApplicationException(e);
		}
	}
	
	@POST
    @Path("/decrypt")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
    public String decrypt(@FormDataParam(value = "texte") String texte) {
		try {
			return PasswdCrypt.decrypt(texte);
		}
		catch(CryptoException e) {
			throw new WebApplicationException(e);
		}
	}
}

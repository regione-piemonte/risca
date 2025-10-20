/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.business.be;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.web.bind.annotation.RequestBody;

import it.csi.risca.riscabesrv.dto.ElaboraDTO;
import it.csi.risca.riscabesrv.dto.ElaboraExtendedDTO;

/**
 * The interface Elabora api.
 *
 * @author CSI PIEMONTE
 */
@Path("")
@Produces(MediaType.APPLICATION_JSON)
public interface ElaboraApi {
	
    /**
     * Load elaborazioni by id_ambito, codTipoElabora, codStatoElabora
     *
     * @param idAmbito        idAmbito
     * @param idTipoElabora      codTipoElabora
     * @param idStatoElabora    codStatoElabora
     * @param securityContext SecurityContext
     * @param httpHeaders     HttpHeaders
     * @param httpRequest     HttpServletRequest
     * @return Response response
     */
	@GET
    @Path("/elabora")
    Response loadElabora(@QueryParam("fruitore") String fruitore,
    		@QueryParam("idAmbito") String idAmbito, 
    		@QueryParam("codTipoElabora") List<String> codTipoElabora, 
    		@QueryParam("codStatoElabora") List<String> codStatoElabora, 
    		@QueryParam("dataRichiestaInizio") String dataRichiestaInizio, 
    		@QueryParam("dataRichiestaFine") String dataRichiestaFine, 
    		@QueryParam("codFunzionalita") String codFunzionalita, 
    		@QueryParam(value = "offset") @Min(value = 0, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")  Integer offset,  
    		@QueryParam(value = "limit") @Min(value = 0, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")  Integer limit, 
    		@DefaultValue("") @QueryParam(value = "sort") String sort,
    	    @QueryParam("flgVisibile") @Min(value = 0, message = "flgVisibile deve essere 0 o 1") @Max(value = 1, message = "flgVisibile deve essere 0 o 1")  Integer flgVisibile, 
    		@Context SecurityContext securityContext, 
    		@Context HttpHeaders httpHeaders, 
    		@Context HttpServletRequest httpRequest);
	
	@GET
    @Path("/elabora/{idElabora}")
    Response loadElaboraById(
			@PathParam("idElabora")@Min(value = 1, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")  Long idElabora,
    		@QueryParam("download") Boolean download, 
    		@Context SecurityContext securityContext, 
    		@Context HttpHeaders httpHeaders, 
    		@Context HttpServletRequest httpRequest);

    /**
     * Load elaborazioni by id_ambito, codTipoElabora, codStatoElabora
     *
     * @param idAmbito        idAmbito
     * @param verifica    verifica
     * @param securityContext SecurityContext
     * @param httpHeaders     HttpHeaders
     * @param httpRequest     HttpServletRequest
     * @return Response response
     */
	@GET
    @Path("/_verifyElabora")
    Response verifyElabora(
    		@QueryParam("idAmbito") String idAmbito, 
    		@QueryParam("verifica") String verifica,
    		@Context SecurityContext securityContext, 
    		@Context HttpHeaders httpHeaders, 
    		@Context HttpServletRequest httpRequest);
	
	@POST
	@Path("/elabora")
	@Consumes(MediaType.APPLICATION_JSON)
	Response saveElabora(@RequestBody ElaboraDTO elabora,@QueryParam("fruitore") String fruitore,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);
	
	@PUT
	@Path("/elabora")
	@Consumes(MediaType.APPLICATION_JSON)
	Response updateElabora(@RequestBody ElaboraExtendedDTO elabora,@QueryParam("fruitore") String fruitore,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);
	
	
	@Path("/elabora/notturno")
	@GET
	Response isNotturnoTurnOn(
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);
	
	@GET
	@Path("/elabora/codiceFiscale/{codiceFiscale}")
	Response loadElaboraByCF(@PathParam("codiceFiscale") String codiceFiscale, @QueryParam("fruitore") String fruitore,
			 @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);
}

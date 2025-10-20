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

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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

import it.csi.risca.riscabesrv.business.be.exception.GenericExceptionList;
import it.csi.risca.riscabesrv.dto.IndirizzoSpedizioneDTO;


/**
 * The interface indirizzi spedizione api.
 *
 * @author CSI PIEMONTE
 */
@Path("")
@Produces(MediaType.APPLICATION_JSON)
public interface IndirizziSpedizioneApi {

	 /**
     * Save indirizzi spedizione response.
     *
     * @param securityContext SecurityContext
     * @param httpHeaders     HttpHeaders
     * @param httpRequest     HttpServletRequest
     * @return Response response
     */
	@Path("/indirizzi-spedizione")
    @POST
    Response saveIndirizziSpedizione(@RequestBody IndirizzoSpedizioneDTO indSped, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@Path("/indirizzi-spedizione")
    @PUT
    Response updateIndirizziSpedizione(@RequestBody IndirizzoSpedizioneDTO indSped, @QueryParam("fruitore") String fruitore, @QueryParam("modalitaVerifica") @Min(value = 1, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")  Long modVerifica, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) throws GenericExceptionList;
	
	@GET
	@Path("/indirizzi-spedizione/gruppi/{idGruppoSoggetto}")	
	Response loadIndirizziSpedizioneByIdGruppoSoggetto(@PathParam("idGruppoSoggetto") @Min(value = 1, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")  Long idGruppoSoggetto, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

}

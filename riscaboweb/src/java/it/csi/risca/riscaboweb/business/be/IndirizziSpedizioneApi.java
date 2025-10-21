/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscaboweb.business.be;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.web.bind.annotation.RequestBody;

import it.csi.risca.riscaboweb.business.be.exception.GenericExceptionList;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.IndirizzoSpedizioneDTO;


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
    @Consumes(MediaType.APPLICATION_JSON)
    Response saveIndirizziSpedizione(@RequestBody IndirizzoSpedizioneDTO indSped,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@Path("/indirizzi-spedizione")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    Response updateIndirizziSpedizione(@RequestBody IndirizzoSpedizioneDTO indSped, @QueryParam("modalitaVerifica") String modalitaVerifica,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) throws GenericExceptionList;
	
}

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

import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
import it.csi.risca.riscabesrv.dto.DelegatoExtendedDTO;

/**
 * The interface DelegheApi.
 *
 * @author CSI PIEMONTE
 */
@Path("/deleghe")
@Produces(MediaType.APPLICATION_JSON)
public interface DelegheApi {

	 /**
     * Load Deleghe by Codice Fiscale.
     *
     * @param fruitore          fruitore
     * @param codiceFiscale codiceFiscale
     * @param httpHeaders     HttpHeaders
     * @param httpRequest     HttpServletRequest
     * @return Response response
     */
    @GET
    @Path("/codiceFiscale/{codiceFiscale}")
    Response loadDelegheByCf(@QueryParam("fruitore") String fruitore, @PathParam("codiceFiscale") String codiceFiscale,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    
    @POST
	@Consumes(MediaType.APPLICATION_JSON)
	Response saveDelegato(@RequestBody DelegatoExtendedDTO delegato, @QueryParam("origCf") String origCf, @QueryParam("fruitore") String fruitore, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) throws GenericExceptionList, ParseException;

    
}

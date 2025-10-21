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
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * The interface Documenti Allegati api.
 *
 * @author CSI PIEMONTE
 */
@Path("")
@Produces(MediaType.APPLICATION_JSON)
public interface DocumentiAllegatiApi {

    @GET
    @Path("/classificazioni/{idRiscossione}")
    @Consumes(MediaType.APPLICATION_JSON)
    Response classificazioni(@PathParam("idRiscossione") String idRiscossione, @QueryParam("fruitore") String fruitore,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    @GET
    @Path("/allegati/{dbKeyClassificazione}")
    @Consumes(MediaType.APPLICATION_JSON)
    Response allegati(@PathParam("dbKeyClassificazione") String dbKeyClassificazione, @QueryParam("idRiscossione") String idRiscossione, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
    
    @GET
    @Path("/actaContentStream/{idClassificazione}")
    @Consumes(MediaType.APPLICATION_JSON)
    Response actaContentStream(@PathParam("idClassificazione") String idClassificazione, @QueryParam("idRiscossione") String idRiscossione,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

}

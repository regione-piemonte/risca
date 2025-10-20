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
import javax.ws.rs.core.SecurityContext;

import org.springframework.web.bind.annotation.RequestBody;

import it.csi.risca.riscabesrv.dto.DTGrandeIdroDTO;
import it.csi.risca.riscabesrv.dto.ambiente.RiscossioneDatoTecnicoDTO;

/**
 * The interface Dato tecnico api.
 *
 * @author CSI PIEMONTE
 */
@Path("/dati-tecnici")
@Produces(MediaType.APPLICATION_JSON)
public interface DatoTecnicoApi {
	

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    Response saveDatoTecnico(@RequestBody RiscossioneDatoTecnicoDTO datoTecnico,@QueryParam("fruitore") String fruitore,  @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) throws Exception;

    @PUT
    @Path("/grande-idro")
    @Consumes(MediaType.APPLICATION_JSON)
    Response updateDTGrandeIdro(@QueryParam("fruitore") String fruitore, @QueryParam("idRiscossione") Integer idRiscossione, @RequestBody DTGrandeIdroDTO DTGrandeIdroDTO, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) throws Exception;


}

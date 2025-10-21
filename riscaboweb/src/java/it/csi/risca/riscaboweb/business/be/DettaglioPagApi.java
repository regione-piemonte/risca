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
import javax.ws.rs.DELETE;
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

import org.springframework.web.bind.annotation.RequestBody;

import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.DettaglioPagDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.DettaglioPagListDTO;

/**
 * The interface  Dettaglio Pag Api .
 *
 * @author CSI PIEMONTE
 */
@Path("")
@Produces(MediaType.APPLICATION_JSON)
public interface DettaglioPagApi {
	
	
	@GET
	@Path("/dettaglio-pag")
    Response getDettaglioPagByIdRiscossioneAndIdSD(@QueryParam("fruitore") String fruitore,
    		@QueryParam("idRiscossione")  String idRiscossione, 
    		@QueryParam("idStatoDebitorio")String idStatoDebitorio, 
    		 
    		@Context HttpHeaders httpHeaders, 
    		@Context HttpServletRequest httpRequest);
	@DELETE
	@Path("/dettaglio-pag/idDettaglioPag/{idDettaglioPag}")
    Response deleteDettaglioPagByIdDettaglioPag(@PathParam("idDettaglioPag") String idDettaglioPag, @QueryParam("fruitore") String fruitore,
    		 
    		@Context HttpHeaders httpHeaders, 
    		@Context HttpServletRequest httpRequest);
	@POST
	@Path("/dettaglio-pag")
    @Consumes(MediaType.APPLICATION_JSON)
    Response saveDettaglioPag(@QueryParam("fruitore") String fruitore,
    		@RequestBody DettaglioPagDTO DettaglioPag, 
    		 
    		@Context HttpHeaders httpHeaders, 
    		@Context HttpServletRequest httpRequest);
	@POST
	@Path("/dettaglio-pag-list")
    @Consumes(MediaType.APPLICATION_JSON)
    Response saveDettaglioPagList(@QueryParam("fruitore") String fruitore,
    		@RequestBody DettaglioPagListDTO DettaglioPagList, 
    		 
    		@Context HttpHeaders httpHeaders, 
    		@Context HttpServletRequest httpRequest);
}

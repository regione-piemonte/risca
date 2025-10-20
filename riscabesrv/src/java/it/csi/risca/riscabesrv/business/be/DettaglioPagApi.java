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
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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

import it.csi.risca.riscabesrv.dto.DettaglioPagDTO;
import it.csi.risca.riscabesrv.dto.DettaglioPagListDTO;

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
    		@QueryParam("idRiscossione")  @Min(value = 1, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer") Long idRiscossione, 
    		@QueryParam("idStatoDebitorio") @Min(value = 1, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")  Long idStatoDebitorio, 
    		@Context SecurityContext securityContext, 
    		@Context HttpHeaders httpHeaders, 
    		@Context HttpServletRequest httpRequest);
	@DELETE
	@Path("/dettaglio-pag/idDettaglioPag/{idDettaglioPag}")
    Response deleteDettaglioPagByIdDettaglioPag(@QueryParam("fruitore") String fruitore,
    		@PathParam("idDettaglioPag") @Min(value = 1, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")  Long idDettaglioPag, 
    		@Context SecurityContext securityContext, 
    		@Context HttpHeaders httpHeaders, 
    		@Context HttpServletRequest httpRequest);
	@POST
	@Path("/dettaglio-pag")
    @Consumes(MediaType.APPLICATION_JSON)
    Response saveDettaglioPag(@QueryParam("fruitore") String fruitore,
    		@RequestBody DettaglioPagDTO DettaglioPag, 
    		@Context SecurityContext securityContext, 
    		@Context HttpHeaders httpHeaders, 
    		@Context HttpServletRequest httpRequest);
	@POST
	@Path("/dettaglio-pag-list")
    @Consumes(MediaType.APPLICATION_JSON)
    Response saveDettaglioPagList(@QueryParam("fruitore") String fruitore,
    		@RequestBody DettaglioPagListDTO DettaglioPagList, 
    		@Context SecurityContext securityContext, 
    		@Context HttpHeaders httpHeaders, 
    		@Context HttpServletRequest httpRequest);
	
	@PUT
	@Path("/dettaglio-pag/annullamento-soris")
	@Consumes(MediaType.APPLICATION_JSON)
	Response updateDettaglioPagAnnullamentoSoris(@RequestBody DettaglioPagDTO dettaglioPag,@QueryParam("fruitore") String fruitore,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);
	
}

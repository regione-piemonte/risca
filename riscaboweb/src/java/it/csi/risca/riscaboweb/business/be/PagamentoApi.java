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
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.web.bind.annotation.RequestBody;

import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.PagamentoExtendedDTO;

@Path("/pagamenti")
@Produces(MediaType.APPLICATION_JSON)
public interface PagamentoApi {

	@GET
    Response getPagamentoWithMaxDataOpVal(@QueryParam("fruitore") String fruitore,
    		@QueryParam("idStatoDebitorio") String idStatoDebitorio, 
    		@Context HttpHeaders httpHeaders, 
    		@Context HttpServletRequest httpRequest);
	
	@GET
	@Path("/idRiscossione/{idRiscossione}")
    Response getPagamentoByIdRiscossione(@QueryParam("fruitore") String fruitore,
    		@PathParam("idRiscossione") String idRiscossione, 
    		 
    		@Context HttpHeaders httpHeaders, 
    		@Context HttpServletRequest httpRequest);
	   
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    Response savePagamento(@RequestBody PagamentoExtendedDTO pagamentoExtendedDTO,@QueryParam("fruitore") String fruitore,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    Response updatePagamento(@RequestBody PagamentoExtendedDTO pagamentoExtendedDTO,@QueryParam("fruitore") String fruitore,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    @DELETE 
    @Path("/idPagamento/{idPagamento}")
    Response deleteByIdPagamento(@PathParam("idPagamento") String idPagamento, @QueryParam("fruitore") String fruitore,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@GET
	@Path("/idStatoDebitorio/{idStatoDebitorio}")

    Response getPagamentiByIdStatoDebitorio(@QueryParam("fruitore") String fruitore,@PathParam("idStatoDebitorio") String idStatoDebitorio,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@GET 
    @Path("/idPagamento/{idPagamento}")
    Response getPagamentoByIdPagamento(@PathParam("idPagamento") String idPagamento, @QueryParam("fruitore") String fruitore,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);


	@GET 
    @Path("/pagamenti-da-visionare")
    Response getPagamentiDaVisionare( @QueryParam("fruitore") String fruitore, @QueryParam(value = "offset") String offset,@QueryParam(value = "limit") String limit, @QueryParam(value = "sort") String sort,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	
}

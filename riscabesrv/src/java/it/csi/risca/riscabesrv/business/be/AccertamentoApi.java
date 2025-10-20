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
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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

import it.csi.risca.riscabesrv.business.be.exception.GenericExceptionList;
import it.csi.risca.riscabesrv.dto.AccertamentoExtendedDTO;

/**
 * The interface Accertamento api.
 *
 * @author CSI PIEMONTE
 */
@Path("/accertamenti")
@Produces(MediaType.APPLICATION_JSON)
public interface AccertamentoApi {
	
    @GET
    Response loadAllAccertamentiOrByIdStatoDeb(@QueryParam("fruitore") String fruitore,
    		@QueryParam("idStatoDebitorio") @Min(value = 1, message = "L'id deve essere maggiore di 0") @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")  Long idStatoDebitorio,
    		@QueryParam(value = "offset") @Min(value = 0, message = "offset deve essere maggiore di 0") @Max(value = Integer.MAX_VALUE, message = "offset deve essere minore o uguale a max value integer")  Integer offset, 
    		@QueryParam(value = "limit") @Min(value = 0, message = "limit deve essere maggiore di 0") @Max(value = Integer.MAX_VALUE, message = "limit deve essere minore o uguale a max value integer") Integer limit,
    		@DefaultValue("") @QueryParam(value = "sort") String sort, 
    		@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    Response saveAccertamenti(@RequestBody AccertamentoExtendedDTO accertamento, @QueryParam("fruitore") String fruitore, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) throws GenericExceptionList, ParseException;

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    Response updateAccertamenti(@RequestBody AccertamentoExtendedDTO accertamento, @QueryParam("fruitore") String fruitore, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) throws GenericExceptionList, ParseException;

	@DELETE
	@Path("/idAccertamento/{idAccertamento}")
	Response deleteAccertamento(
			@PathParam("idAccertamento") @Min(value = 1, message = "L'id deve essere maggiore di 0") @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer") Long idAccertamento,
			@QueryParam("fruitore") String fruitore, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest)
			throws GenericExceptionList, ParseException;

}

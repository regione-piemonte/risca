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
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
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

@Path("/annualita-sd")
@Produces(MediaType.APPLICATION_JSON)
public interface AnnualitaSdApi {
	
	@GET
    Response loadAnnualitaSd(@QueryParam("fruitore") String fruitore, 
    		@QueryParam("idStatoDebitorio") @Min(value = 1, message = "L'id deve essere maggiore di 0") @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")  Long  idStatoDebitorio, 
    		@QueryParam("speciali") Boolean speciali, 
     		@QueryParam("intere") Boolean intere,
    		@Context SecurityContext securityContext, 
    		@Context HttpHeaders httpHeaders, 
    		@Context HttpServletRequest httpRequest);
	
	@GET
	@Path("/load-grande-idro")
    Response getDatiTecniciGrandeIdro(@QueryParam("fruitore") String fruitore, 
    		@QueryParam("idStatoDebitorio") @Min(value = 1, message = "L'id deve essere maggiore di 0") @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")  Long  idStatoDebitorio,
    		@Context SecurityContext securityContext, 
    		@Context HttpHeaders httpHeaders, 
    		@Context HttpServletRequest httpRequest);
	
    @PUT
    @Path("/grande-idro")
    @Consumes(MediaType.APPLICATION_JSON)
    Response updateDatiTecniciGrandeIdro(@QueryParam("fruitore") String fruitore,  @Min(value = 1, message = "L'id deve essere maggiore di 0") @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer") @QueryParam("idAnnualitaSd") Integer idAnnualitaSd, @RequestBody DTGrandeIdroDTO DTGrandeIdroDTO, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) throws Exception;

}

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

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.web.bind.annotation.RequestBody;

@Path("/accerta-bilancio")
@Produces(MediaType.APPLICATION_JSON)
public interface PagamentoBilAccApi {

	@GET
	@Path("/lista-pagamenti")
	Response getListaIdPagamentiByAnnoDataRif(@QueryParam("anno") String anno, @QueryParam("dataRif") String dataRif,
			@QueryParam("idPagamento") List<Long> idPagamento, 
			@QueryParam("fruitore") String fruitore, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@POST
	@Path("/dettagli-pagamento-bil-acc")
	@Consumes(MediaType.APPLICATION_JSON)
	Response saveDettaglioPagamentoBilAcc(@QueryParam("fruitore") String fruitore,
			@QueryParam("idPagamento") @Min(value = 1, message = "L'id deve essere maggiore di 0") @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer") Long idPagamento,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	@DELETE
	@Path("/dettagli-pagamento-bil-acc")
	@Consumes(MediaType.APPLICATION_JSON)
	Response deleteDettaglioPagamentoBilAcc(@QueryParam("fruitore") String fruitore,
			@QueryParam("idPagamento") @Min(value = 1, message = "L'id deve essere maggiore di 0") @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer") Long idPagamento,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	@POST
	@Path("/delete-dettagli-pagamento-bil-acc")
	@Consumes(MediaType.APPLICATION_JSON)
	Response deleteDettagliPagamentoBilAcc(@QueryParam("fruitore") String fruitore,
			@RequestBody List<Long> listaIdPagamento, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

}

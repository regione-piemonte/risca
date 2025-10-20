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

import java.math.BigDecimal;
import java.util.List;

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
import it.csi.risca.riscabesrv.dto.PagamentoExtendedDTO;

@Path("/pagamenti")
@Produces(MediaType.APPLICATION_JSON)
public interface PagamentoApi {

	@GET
    Response getPagamentoWithMaxDataOpVal(@QueryParam("fruitore") String fruitore,
    		@QueryParam("idStatoDebitorio") @Min(value = 1, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")  Long idStatoDebitorio, 
    		@Context SecurityContext securityContext, 
    		@Context HttpHeaders httpHeaders, 
    		@Context HttpServletRequest httpRequest);
	
	@GET
	@Path("/idRiscossione/{idRiscossione}")
    Response getPagamentoByIdRiscossione(@QueryParam("fruitore") String fruitore,
    		@PathParam("idRiscossione") @Min(value = 1, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")  Long idRiscossione, 
    		@Context SecurityContext securityContext, 
    		@Context HttpHeaders httpHeaders, 
    		@Context HttpServletRequest httpRequest);

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	Response savePagamento(@RequestBody PagamentoExtendedDTO pagamentoExtendedDTO,@QueryParam("fruitore") String fruitore,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	Response updatePagamento(@RequestBody PagamentoExtendedDTO pagamentoExtendedDTO,@QueryParam("fruitore") String fruitore,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);
	
	@DELETE
	@Path("/idPagamento/{idPagamento}")
    Response deleteByIdPagamento(
    		@PathParam("idPagamento") @Min(value = 1, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")  Long idPagamento, @QueryParam("fruitore") String fruitore,
    		@Context SecurityContext securityContext, 
    		@Context HttpHeaders httpHeaders, 
    		@Context HttpServletRequest httpRequest);
	
	@GET
	@Path("/idStatoDebitorio/{idStatoDebitorio}")
    Response getPagamentiByIdStatoDebitorio(@QueryParam("fruitore") String fruitore,
    		@PathParam("idStatoDebitorio")@Min(value = 1, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")  Long idStatoDebitorio, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
	
	
	@GET
	@Path("/idPagamento/{idPagamento}")
    Response getPagamentoByIdPagamento(@PathParam("idPagamento") @Min(value = 1, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")  Long idPagamento, @QueryParam("fruitore") String fruitore,@Context SecurityContext securityContext, 
    		@Context HttpHeaders httpHeaders, 
    		@Context HttpServletRequest httpRequest);
	
	
	@POST
	@Path("/dettagli-pagamento")
	@Consumes(MediaType.APPLICATION_JSON)
	Response saveDettaglioPagamento(@RequestBody DettaglioPagDTO dettaglioPagDTO,@QueryParam("fruitore") String fruitore,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);
	@GET 
    @Path("/pagamenti-da-visionare")
    Response getPagamentiDaVisionare(@QueryParam("fruitore") String fruitore,
    		@QueryParam(value = "offset") @Min(value = 0, message = "offset deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "offset deve essere minore o uguale a max value integer")  Integer offset,
    		@QueryParam(value = "limit") @Min(value = 0, message = "limit deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "limit deve essere minore o uguale a max value integer")  Integer limit, 
    		@QueryParam(value = "sort") String sort, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	
	@GET
	@Path("/esiste-pagamento")
    Response getEsistenzaPagamento(@QueryParam("fruitore") String fruitore,
    		@QueryParam("dataOpVal") String dataOpVal, 
    		@QueryParam("importoVersato") BigDecimal importoVersato, @QueryParam("idFileSoris") Long idFileSoris,
    		@Context SecurityContext securityContext, 
    		@Context HttpHeaders httpHeaders, 
    		@Context HttpServletRequest httpRequest);
	
	@GET
	@Path("/esiste-pagamento-da-annullare")
    Response getEsistenzaPagamentoDaAnnullare(@QueryParam("fruitore") String fruitore,
    		@QueryParam("dataOpVal") String dataOpVal, 
    		@QueryParam("idPagamento") Long idPagamento, 
    		@Context SecurityContext securityContext, 
    		@Context HttpHeaders httpHeaders, 
    		@Context HttpServletRequest httpRequest);
	
	@PUT
	@Path("/annullamento-soris")
	@Consumes(MediaType.APPLICATION_JSON)
	Response updatePagamentoPerAnnullamentoSoris(@RequestBody PagamentoExtendedDTO pagamentoExtendedDTO,@QueryParam("fruitore") String fruitore,@NotNull @QueryParam("idFileSoris") Long idFileSoris,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);
	
	
	@POST
	@Path("/recupera-pagamenti-pagopa")
	@Consumes(MediaType.APPLICATION_JSON)
	Response recuperaPagamentoPagopa(@RequestBody List<Long> listaIdElabora, @QueryParam("fruitore") String fruitore,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);
	
	
}

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

import it.csi.risca.riscabesrv.dto.RiscossioneSearchDTO;

@Path("/report")
@Produces(MediaType.APPLICATION_JSON)
public interface ReportApi {

	/**
	 * creaReportRicercaAvanzata.
	 * 
	 * @param fruitore             fruitore
	 * @param modalitaRicerca      modalitaRicerca
	 * @param RiscossioneSearchDTO riscossioneSearch
	 * @param securityContext      SecurityContext
	 * @param httpHeaders          HttpHeaders
	 * @param httpRequest          HttpServletRequest
	 * @return Response response
	 */
	@POST
	@Path("/ricerca-avanzata")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response creaReportRicercaAvanzata(@RequestBody RiscossioneSearchDTO riscossioneSearch,
			@QueryParam("modalitaRicerca") String modalitaRicerca, @QueryParam("fruitore") String fruitore,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	/**
	 * creaReportRicercaMorosita.
	 * 
	 * @param fruitore            fruitore
	 * @param tipoRicercaMorosita tipoRicercaMorosita
	 * @param anno                anno
	 * @param flgRest             flgRest
	 * @param flgAnn              flgAnn
	 * @param lim                 lim
	 * @param securityContext     SecurityContext
	 * @param httpHeaders         HttpHeaders
	 * @param httpRequest         HttpServletRequest
	 * @return Response response
	 */
	@GET
	@Path("/ricerca-morosita")
	public Response creaReportRicercaMorosita(@QueryParam("fruitore") String fruitore,
			@QueryParam("tipoRicercaMorosita") String tipoRicercaMorosita,
			@QueryParam("anno") @Min(value = 1, message = "L'id deve essere maggiore di 0") @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer") Integer anno,
			@QueryParam("flgRest") @Min(value = 0, message = "flgRest deve essere 0 o 1") @Max(value = 1, message = "flgRest deve essere 0 o 1") Integer flgRest,
			@QueryParam("flgAnn") @Min(value = 0, message = "flgAnn deve essere 0 o 1") @Max(value = 1, message = "flgAnn deve essere 0 o 1") Integer flgAnn,
			@QueryParam("lim") String lim, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	/**
	 * creaReportRicercaRimborsi.
	 * 
	 * @param fruitore            fruitore
	 * @param tipoRicercaRimborsi tipoRicercaRimborsi
	 * @param anno                anno
	 * @param securityContext     SecurityContext
	 * @param httpHeaders         HttpHeaders
	 * @param httpRequest         HttpServletRequest
	 * @return Response response
	 */
	@GET
	@Path("/ricerca-rimborsi")
	Response creaReportRicercaRimborsi(@QueryParam("fruitore") String fruitore,
			@QueryParam("tipoRicercaRimborsi") String tipoRicercaRimborsi,
			@QueryParam("anno") @Min(value = 1, message = "L'id deve essere maggiore di 0") @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer") Integer anno,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	/**
	 * creaReportBilancio.
	 * 
	 * @param fruitore        fruitore
	 * @param tipoBilancio    tipoBilancio
	 * @param dataDa          dataDa
	 * @param dataA           dataA
	 * @param securityContext SecurityContext
	 * @param httpHeaders     HttpHeaders
	 * @param httpRequest     HttpServletRequest
	 * @return Response response
	 */
	@GET
	@Path("/bilancio")
	Response creaReportBilancio(@QueryParam("fruitore") String fruitore,
			@QueryParam("anno") @Min(value = 1, message = "L'id deve essere maggiore di 0") @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer") Integer anno,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);
	
	
}

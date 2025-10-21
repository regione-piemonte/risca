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

import java.util.List;

import javax.servlet.http.HttpServletRequest;
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

import org.springframework.web.bind.annotation.RequestBody;

import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.LocationJobDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.RiscossioneSearchDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.RuoloMorositaSearchDTO;

@Path("/report")
@Produces(MediaType.APPLICATION_JSON)
public interface ReportApi {
	
	
    /**
     * creaReportRicercaAvanzata.
     * @param fruitore fruitore
     * @param modalitaRicerca modalitaRicerca
     * @param RiscossioneSearchDTO riscossioneSearch
     * @param securityContext SecurityContext
     * @param httpHeaders     HttpHeaders
     * @param httpRequest     HttpServletRequest
     * @return Response response
     */
	@POST
	@Path("/ricerca-avanzata")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response creaReportRicercaAvanzata(@RequestBody RiscossioneSearchDTO riscossioneSearch,
			@QueryParam("modalitaRicerca") String modalitaRicerca, @QueryParam("fruitore") String fruitore, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);
	
    /**
     * pollingJob.
     * @param fruitore fruitore
     * @param LocationJobDTO LocationJobDTO
     * @param securityContext SecurityContext
     * @param httpHeaders     HttpHeaders
     * @param httpRequest     HttpServletRequest
     * @return Response response
     */
	@POST
	@Path("/polling-job")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response pollingJob(@RequestBody List<LocationJobDTO> locationJobDTO, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);
	
    /**
     * creaReportRicercaMorosita.
     * @param fruitore fruitore
     * @param tipoRicercaMorosita tipoRicercaMorosita
     * @param anno anno
     * @param flgRest flgRest
     * @param flgAnn flgAnn
     * @param lim lim
     * @param securityContext SecurityContext
     * @param httpHeaders     HttpHeaders
     * @param httpRequest     HttpServletRequest
     * @return Response response
     */
	@GET
	@Path("/ricerca-morosita")
	public Response creaReportRicercaMorosita(@QueryParam("fruitore") String fruitore, @QueryParam("tipoRicercaMorosita") String tipoRicercaMorosita,
    		@QueryParam("anno") String anno, @QueryParam("flgRest")  String flgRest , @QueryParam("flgAnn") String flgAnn,
    		@QueryParam("lim") String lim, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);
	

    /**
     * creaReportRicercaRimborsi.
     * @param fruitore fruitore
     * @param tipoRicercaRimborsi tipoRicercaRimborsi
     * @param anno anno
     * @param securityContext SecurityContext
     * @param httpHeaders     HttpHeaders
     * @param httpRequest     HttpServletRequest
     * @return Response response
     */
    @GET
    @Path("/ricerca-rimborsi")
    Response creaReportRicercaRimborsi(@QueryParam("fruitore") String fruitore, @QueryParam("tipoRicercaRimborsi") String tipoRicercaRimborsi,
    		@QueryParam("anno")   String anno,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	/**
	 * creaReportBilancio
	 * 
	 * @param fruitore
	 * @param anno
	 * @param httpHeaders
	 * @param httpRequest
	 * @return
	 */
	@GET
	@Path("/bilancio")
	Response creaReportBilancio(@QueryParam("fruitore") String fruitore, @QueryParam("anno") String anno,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
	/**
	 * creaReportFile450RuoloRicercaMorosita
	 * 
	 * @param fruitore
	 * @param RuoloMorositaSearchDTO
	 * @param httpHeaders
	 * @param httpRequest
	 * @return
	 */
	
	@POST
	@Path("/file-450/ruolo-ricerca-morosita")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response creaReportFile450RuoloRicercaMorosita(@QueryParam("fruitore") String fruitore, @QueryParam("tipoRicercaMorosita") String tipoRicercaMorosita,
    		@QueryParam("anno") String anno, @QueryParam("flgRest")  String flgRest , @QueryParam("flgAnn") String flgAnn,
    		@QueryParam("lim") String lim, @RequestBody List<Long> listIdStatiDebitoriSelezionati ,
			 @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

}

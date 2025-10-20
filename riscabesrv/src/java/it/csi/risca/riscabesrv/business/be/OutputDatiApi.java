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

import it.csi.risca.riscabesrv.dto.OutputDatiDTO;

@Path("/output-dati")
@Produces(MediaType.APPLICATION_JSON)
public interface OutputDatiApi {

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	Response salvaOutputDati(@RequestBody OutputDatiDTO outputDati, @QueryParam("fruitore") String fruitore, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@GET
	@Path("/nomi-lotto")
	@Consumes(MediaType.APPLICATION_JSON)
	Response loadNomiLotto(@QueryParam("idElabora")@Min(value = 1, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")  Long idElabora,
			@QueryParam("codTipoElabora") String codTipoElabora,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	@GET
	@Path("/posizioni-debitorie")
	@Consumes(MediaType.APPLICATION_JSON)
	Response loadPosizioniDebitorieByNomeLotto(@QueryParam("nomeLotto") String nomeLotto,
			@QueryParam("idAmbito") @Min(value = 1, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")  Long idAmbito, @QueryParam("bollettazioneSpeciale") Boolean bollettazioneSpeciale,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);
	
	@GET
	@Path("/dati-foglio")
	@Consumes(MediaType.APPLICATION_JSON)
	Response loadDatiByFoglio(@QueryParam("idElabora") Long idElabora,
			@QueryParam("idOutputFoglio") @Min(value = 1, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")  Long idOutputFoglio,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

}

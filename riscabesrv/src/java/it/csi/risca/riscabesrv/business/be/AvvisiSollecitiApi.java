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
import javax.ws.rs.DefaultValue;
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

import it.csi.risca.riscabesrv.dto.AvvisoSollecitoData;

@Path("/avvisi-solleciti")
@Produces(MediaType.APPLICATION_JSON)
public interface AvvisiSollecitiApi {

	@GET
	Response loadAvvisiSolleciti(@QueryParam("codAmbito") String codAmbito,
			@QueryParam("codAttivitaStatoDeb") String codAttivitaStatoDeb, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@POST
	@Path("/dati-avviso")
	@Consumes(MediaType.APPLICATION_JSON)
	Response saveAvvisiDatiWorking(@RequestBody AvvisoSollecitoData avvisoSollecito,
			@DefaultValue("true") @QueryParam(value = "titolareCambiato") Boolean titolareCambiato,
			@QueryParam(value = "sdCambiato") Boolean sdCambiato, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@GET
	@Path("/riordina-dati")
	@Consumes(MediaType.APPLICATION_JSON)
	Response riordinaAvvisiDatiWorking(@QueryParam(value = "idElabora")@Min(value = 1, message = "L'id deve essere maggiore di 0") @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")  Long idElabora,
			@QueryParam(value = "idOutputFoglio") String idOutputFoglio,
			@QueryParam(value = "soggettoGruppo") String soggettoGruppo, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@GET
	@Path("/copy-working")
	@Consumes(MediaType.APPLICATION_JSON)
	Response copyDatiWorking(@QueryParam(value = "idElabora")@Min(value = 1, message = "L'id deve essere maggiore di 0") @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")  Long idElabora, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@GET
	@Path("/delete-working")
	@Consumes(MediaType.APPLICATION_JSON)
	Response deleteDatiWorking(@QueryParam(value = "idElabora") @Min(value = 1, message = "L'id deve essere maggiore di 0") @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")  Long idElabora,
			@QueryParam(value = "idSpedizione") Long idSpedizione, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

}

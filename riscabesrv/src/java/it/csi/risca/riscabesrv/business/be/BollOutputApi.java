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

import it.csi.risca.riscabesrv.dto.BollOutputDatiDTO;

@Path("/_boll-output")
@Produces(MediaType.APPLICATION_JSON)
public interface BollOutputApi {

	@POST
	@Path("/save/bs")
	Response salvaDatiOutputBs(@RequestBody BollOutputDatiDTO bollOutputDati,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);
	
	@POST
	@Path("/save/bg")
	Response salvaDatiOutputBg(@RequestBody BollOutputDatiDTO bollOutputDati,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	@POST
	@Path("/save/bo")
	Response salvaDatiOutputBo(@RequestBody BollOutputDatiDTO bollOutputDati,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	@GET
	@Path("/updateIuv")
	Response updateDatiOutputIuv(@QueryParam("idElabora")  @Min(value = 1, message = "L'id deve essere maggiore di 0") @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer") Long idElabora ,
			@QueryParam("codTipoElabora") String codTipoElabora, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@GET
	@Path("/updateFlagPecMail")
	Response updateDatiOutputPecMail(@QueryParam("idElabora")  @Min(value = 1, message = "L'id deve essere maggiore di 0") @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")  Long idElabora,
			@QueryParam("progressivo")  @Min(value = 1, message = "L'id deve essere maggiore di 0") @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")  Integer progressivo, @QueryParam("idOutputFoglio") 
	        @Min(value = 1, message = "L'id deve essere maggiore di 0") @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer") Long idOutputFoglio,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	@GET
	@Path("/file/ods")
	Response loadOutputFileDataOds(@QueryParam("idElabora")  @Min(value = 1, message = "L'id deve essere maggiore di 0") @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")  Long idElabora,
			@QueryParam("idOutputFile")  @Min(value = 1, message = "L'id deve essere maggiore di 0") @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")  Long idOutputFile, @QueryParam("codTipoElabora") String codTipoElabora,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	@GET
	@Path("/file/txt")
	Response loadOutputFileDataTxt(@QueryParam("idElabora")  @Min(value = 1, message = "L'id deve essere maggiore di 0") @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")  Long idElabora,
			@QueryParam("idOutputFile")  @Min(value = 1, message = "L'id deve essere maggiore di 0") @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")  Long idOutputFile,
			@QueryParam("idOutputFoglio")  @Min(value = 1, message = "L'id deve essere maggiore di 0") @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")   Long idOutputFoglio,
			@QueryParam("codTipoElabora") String codTipoElabora, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@GET
	@Path("/file/report")
	Response loadOutputFileReport(@QueryParam("codAmbito") String codAmbito,
			@QueryParam("codTipoElabora") String codTipoElabora, @QueryParam("nomeFile") String nomeFile,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

}

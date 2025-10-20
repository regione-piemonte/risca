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

import it.csi.risca.riscabesrv.dto.FilePosteDTO;
import it.csi.risca.riscabesrv.dto.RpEstrcoDTO;
import it.csi.risca.riscabesrv.dto.RpNonPremarcatiDTO;

/**
 * The interface Poste api.
 *
 * @author CSI PIEMONTE
 */
@Path("/poste")
@Produces(MediaType.APPLICATION_JSON)
public interface PosteApi {

	@Path("/save-file")
	@POST
	Response saveFilePoste(@RequestBody FilePosteDTO filePoste, @QueryParam("fruitore") String fruitore, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@Path("/save-non-premarcato")
	@POST
	Response saveNonPremarcato(@RequestBody RpNonPremarcatiDTO rpNonPremarcato,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	@Path("/save-estrco")
	@POST
	Response saveEstrco(@RequestBody RpEstrcoDTO rpEstrco, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@Path("/delete-estrco")
	@DELETE
	Response deleteEstrco(@QueryParam("idElabora") @Min(value = 1, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")  Long idElabora, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@Path("/delete-premarcati")
	@DELETE
	Response deleteNonPremarcati(@QueryParam("idElabora") @Min(value = 1, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")  Long idElabora, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@Path("/estrco")
	@GET
	Response getEstrco(@QueryParam("idElabora")@Min(value = 1, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")   Long idElabora,
			@QueryParam("idFilePoste")@Min(value = 1, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")  Long idFilePoste,
			@QueryParam("numeroConto") String numeroConto, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@Path("/non-premarcati")
	@GET
	Response getNonPremarcati(@QueryParam("idElabora") @Min(value = 1, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")  Long idElabora,
			@QueryParam("idFilePoste") @Min(value = 1, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")  Long idFilePoste,
			@QueryParam("tipoDoc") List<String> tipoDoc, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@Path("/pagamento-non-premarcato")
	@POST
	Response inserisciPagamentoNonPrem(@RequestBody RpNonPremarcatiDTO rpNonPremarcato,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	@Path("/pagamento-estrco")
	@POST
	Response inserisciPagamentoEstrco(@RequestBody RpEstrcoDTO rpEstrco, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

}

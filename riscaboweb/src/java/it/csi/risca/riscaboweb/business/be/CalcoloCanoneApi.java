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

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.springframework.web.bind.annotation.RequestBody;

import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ambiente.RiscossioneDatoTecnicoDTO;

/**
 * @author CSI PIEMONTE
 */
@Path("")
@Produces({"application/json"})
public interface CalcoloCanoneApi {

    /**
	 * Load calcola canone
	 * 
     * @param idRiscossione   idRiscossione
     * @param dataRiferimento dataRiferimento
     * @param securityContext SecurityContext
     * @param httpHeaders     HttpHeaders
     * @param httpRequest     HttpServletRequest
     * @return Response response
     */
    @GET
    @RolesAllowed({"AMMINISTRATORE", "GESTORE_BASE", "GESTORE_DATI"})
    @Path("/calcolo-canone/idRiscossione/{idRiscossione}/dataRif/{dataRiferimento}")
    Response calcoloCanone(@PathParam("idRiscossione") String idRiscossione, @PathParam("dataRiferimento") String dataRiferimento,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    /**
	 * Load calcola canone singolo e frazionato
	 * 
	 * @param RiscossioneDatoTecnicoDTO    datoTecnico
     * @param dataFrazionamento dataFrazionamento
     * @param flgFraz		  flgFraz
     * @param dataRiferimento dataRiferimento
     * @param securityContext SecurityContext
     * @param httpHeaders     HttpHeaders
     * @param httpRequest     HttpServletRequest
     * @return Response response
    */
    @POST
    @RolesAllowed({"AMMINISTRATORE", "GESTORE_BASE", "GESTORE_DATI"})
    @Path("/_calcolo-canone/dataRif/{dataRiferimento}")
    Response calcoloCanoneSingoloEFrazionato(@RequestBody RiscossioneDatoTecnicoDTO datoTecnico, @PathParam("dataRiferimento") String dataRiferimento, @QueryParam("dataFraz") String dataFrazionamento, @QueryParam("flgFraz") String flgFraz,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

}

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

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * The interface  Tipi Elaborazione Api .
 *
 * @author CSI PIEMONTE
 */
@Path("")
@Produces(MediaType.APPLICATION_JSON)
public interface TipiElaborazioneApi {

    /**
     * Load tipi Elaborazione tramite IdAmbito And idFunzionalita And FlgVisibile.
     *
     * @param idAmbito    idAmbito
     * @param idFunzionalita    idFunzionalita
     * @param FlgVisibile FlgVisibile
     * @param securityContext SecurityContext
     * @param httpHeaders     HttpHeaders
     * @param httpRequest     HttpServletRequest
     * @return Response response
     */
    @GET
    @Path("/tipi-elaborazione")
    Response loadTipiElaborazioneByIdAmbitoAndFunzionalitaAndFlgVisibile(@QueryParam("idAmbito")String idAmbito, @QueryParam("idFunzionalita") String idFunzionalita,@QueryParam("flgVisibile") String flgVisibile, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

}

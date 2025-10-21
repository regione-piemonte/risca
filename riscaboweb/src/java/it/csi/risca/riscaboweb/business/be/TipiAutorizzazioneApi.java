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
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

/**
 * @author CSI PIEMONTE
 */
@Path("")
@Produces({"application/json"})
public interface TipiAutorizzazioneApi {

	 /**
     * @param securityContext SecurityContext
     * @param httpHeaders     HttpHeaders
     * @param httpRequest     HttpServletRequest
     * @return Response response
     */
	@Path("/tipi-autorizzazione")
    @GET
    @RolesAllowed({"AMMINISTRATORE", "GESTORE_BASE", "GESTORE_DATI", "CONSULTATORE"})
    Response getTipiAutorizzazione(@HeaderParam ("X-Request-ID") String XRequestId, @HeaderParam ("X-Forwarded-For") String XForwardedFor,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    /**

     * @param idAmbito    idAmbito
     * @param securityContext SecurityContext
     * @param httpHeaders     HttpHeaders
     * @param httpRequest     HttpServletRequest
     * @return Response response
     */
    @GET
    @RolesAllowed({"AMMINISTRATORE", "GESTORE_BASE", "GESTORE_DATI", "CONSULTATORE"})
    @Path("/ambiti/{idAmbito}/tipi-autorizzazione")
    Response getTipiAutorizzazioneByIdAmbito(@PathParam("idAmbito") String idAmbito, @QueryParam("dataIniVal") String dataIniVal, @QueryParam("dataFineVal") String dataFineVal,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    /**
     * Load tipo autorizzazione by idTipoAutorizzazione and idAmbito.
     *
     * @param idTipoAutorizzazione idTipoAutorizzazione
     * @param idAmbito    idAmbito
     * @param securityContext   SecurityContext
     * @param httpHeaders       HttpHeaders
     * @param httpRequest       HttpServletRequest
     * @return Response response
     */
    @GET
    @RolesAllowed({"AMMINISTRATORE", "GESTORE_BASE", "GESTORE_DATI", "CONSULTATORE"})
    @Path("/tipi-autorizzazione/{idTipoAutorizza}/id-ambito/{idAmbito}")
    Response getTipoAutorizzazioneByIdTipoAutorizzazioneAndIdAmbito(@PathParam("idTipoAutorizza") String idTipoAutorizzazione, @PathParam("idAmbito") String idAmbito,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    /**
     * Load tipo autorizzazione by codTipoAutorizzazione and idAmbito.
     *
     * @param codTipoAutorizzazione codTipoAutorizzazione
     * @param idAmbito    idAmbito
     * @param securityContext    SecurityContext
     * @param httpHeaders        HttpHeaders
     * @param httpRequest        HttpServletRequest
     * @return Response response
     */
    @GET
    @RolesAllowed({"AMMINISTRATORE", "GESTORE_BASE", "GESTORE_DATI", "CONSULTATORE"})
    @Path("/ambiti/{idAmbito}/tipi-autorizzazione/{codTipoAutorizza}")
    Response getTipoAutorizzazioneByCodeAndIdAmbito(@PathParam("codTipoAutorizza") String codTipoAutorizzazione, @PathParam("idAmbito") String idAmbito,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);


}

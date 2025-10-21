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
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


/**
 * The interface Tipi uso api.
 *
 * @author CSI PIEMONTE
 */
@Path("")
@Produces(MediaType.APPLICATION_JSON)
public interface TipoUsoApi {

	 /**
     * Load tipi uso response.
     *
     * @param securityContext SecurityContext
     * @param httpHeaders     HttpHeaders
     * @param httpRequest     HttpServletRequest
     * @return Response response
     */
    @GET
    @RolesAllowed({"AMMINISTRATORE", "GESTORE_BASE", "GESTORE_DATI", "CONSULTATORE"})
    @Path("/tipi-uso")
    Response loadTipiUso( @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    /**
     * Load tipi uso tramite idAmbito.
     *
     * @param idAmbito    idAmbito
     * @param securityContext SecurityContext
     * @param httpHeaders     HttpHeaders
     * @param httpRequest     HttpServletRequest
     * @return Response response
     */
//    @GET
//    @RolesAllowed({"AMMINISTRATORE", "GESTORE_BASE", "GESTORE_DATI", "CONSULTATORE"})
//    @Path("/ambiti/{idAmbito}/tipi-uso")
//    Response loadTipiUsoByIdAmbito(@PathParam("idAmbito") Long idAmbito, @QueryParam("dataIniVal") String dataIniVal, @QueryParam("dataFineVal") String dataFineVal,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    /**
     * Load tipo uso by idTipoUsoPadre.
     *
     * @param idTipoUso idTipoUso
     * @param securityContext   SecurityContext
     * @param httpHeaders       HttpHeaders
     * @param httpRequest       HttpServletRequest
     * @return Response response
     */
    @GET
    @RolesAllowed({"AMMINISTRATORE", "GESTORE_BASE", "GESTORE_DATI", "CONSULTATORE"})
    @Path("/tipi-uso/idTipoUsoPadre/{idTipoUsoPadre}")
    Response loadTipoUsoByIdTipoUsoPadre(@PathParam("idTipoUsoPadre") String idTipoUsoPadre,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    
    /**
     * Load tipo uso by idTipoUso.
     *
     * @param idTipoUso idTipoUso
     * @param securityContext   SecurityContext
     * @param httpHeaders       HttpHeaders
     * @param httpRequest       HttpServletRequest
     * @return Response response
     */
    @GET
    @RolesAllowed({"AMMINISTRATORE", "GESTORE_BASE", "GESTORE_DATI", "CONSULTATORE"})
    @Path("/tipi-uso/{idTipoUso}")
    Response loadTipoUsoByIdTipoUsoOrCodTipoUso(@PathParam("idTipoUso") String idTipoUso,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    /**
     * Load tipo uso by codTipoUso and idAmbito.
     *
     * @param codTipoUso codTipoUso
     * @param idAmbito    idAmbito
     * @param securityContext    SecurityContext
     * @param httpHeaders        HttpHeaders
     * @param httpRequest        HttpServletRequest
     * @return Response response
     */
    @GET
    @RolesAllowed({"AMMINISTRATORE", "GESTORE_BASE", "GESTORE_DATI", "CONSULTATORE"})
    @Path("/ambiti/{idAmbito}/tipi-uso/{codTipoUso}")
    Response loadTipoUsoByCodeAndIdAmbito(@PathParam("codTipoUso") String codTipoUso, @PathParam("idAmbito") String idAmbito,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    /**
     * Load tipo uso by idTipoUsoPadre and idAmbito.
     *
     * @param idTipoUsoPadre idTipoUsoPadre
     * @param idAmbito    idAmbito
     * @param securityContext    SecurityContext
     * @param httpHeaders        HttpHeaders
     * @param httpRequest        HttpServletRequest
     * @return Response response
     */
    @GET
    @RolesAllowed({"AMMINISTRATORE", "GESTORE_BASE", "GESTORE_DATI", "CONSULTATORE"})
    @Path("/ambiti/{idAmbito}/tipi-uso")
    Response loadTipoUsoByIdTipoUsoPadreAndIdAmbito(@QueryParam("idTipoUsoPadre") String idTipoUsoPadre, @PathParam("idAmbito") String idAmbito,@QueryParam("dataIniVal") String dataIniVal, @QueryParam("dataFineVal") String dataFineVal, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);


}

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

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

/**
 * @author CSI PIEMONTE
 */
@Path("")
@Produces({"application/json"})
public interface RiduzioneAumentoApi {

	 /**
	 ** Load riduzione aumento
	 * 
     * @param securityContext SecurityContext
     * @param httpHeaders     HttpHeaders
     * @param httpRequest     HttpServletRequest
     * @return Response response
     */
    @GET
    @RolesAllowed({"AMMINISTRATORE", "GESTORE_BASE", "GESTORE_DATI", "CONSULTATORE"})
    @Path("/riduzione-aumento")
    Response loadRiduzioneAumento(@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    /**
	 * Load riduzione aumento by idAmbito
	 * 
     * @param idAmbito    idAmbito
     * @param securityContext SecurityContext
     * @param httpHeaders     HttpHeaders
     * @param httpRequest     HttpServletRequest
     * @return Response response
     */
    @GET
    @RolesAllowed({"AMMINISTRATORE", "GESTORE_BASE", "GESTORE_DATI", "CONSULTATORE"})
    @Path("/riduzione-aumento/idAmbito/{idAmbito}")
    Response loadRiduzioneAumentoByIdAmbito(@PathParam("idAmbito") @Min(value = 1, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer") Long idAmbito, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    /**
     * Load riduzione aumento by idTipoUso
     *
     * @param idTipoUso idTipoUso
     * @param securityContext    SecurityContext
     * @param httpHeaders        HttpHeaders
     * @param httpRequest        HttpServletRequest
     * @return Response response
     */
    @GET
    @RolesAllowed({"AMMINISTRATORE", "GESTORE_BASE", "GESTORE_DATI", "CONSULTATORE"})
    @Path("/riduzione-aumento/idTipoUso/{idTipoUso}")
    Response loadRiduzioneAumentoByIdTipoUso(@PathParam("idTipoUso") @Min(value = 1, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer") Long idTipoUso, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    /**
     * Load riduzione aumento by idTipoUso and flgRidAum.
     *
     * @param idTipoUso idTipoUso
     * @param flgRidAum flgRidAum
     * @param securityContext   SecurityContext
     * @param httpHeaders       HttpHeaders
     * @param httpRequest       HttpServletRequest
     * @return Response response
     */
    @GET
    @RolesAllowed({"AMMINISTRATORE", "GESTORE_BASE", "GESTORE_DATI", "CONSULTATORE"})
    @Path("/riduzione-aumento/idTipoUso/{idTipoUso}/flgRidAum/{flgRidAum}")
    Response loadRiduzioneAumentoByIdTipoUsoAndFlgRidAum(@PathParam("idTipoUso") @Min(value = 1, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer") Long idTipoUso,
    		@PathParam("flgRidAum") String flgRidAum,  @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    /**
     * Load riduzione aumento by idRiduzioneAumento.
     *
     * @param idRiduzioneAumento idRiduzioneAumento
     * @param securityContext   SecurityContext
     * @param httpHeaders       HttpHeaders
     * @param httpRequest       HttpServletRequest
     * @return Response response
     */
    @GET
    @RolesAllowed({"AMMINISTRATORE", "GESTORE_BASE", "GESTORE_DATI", "CONSULTATORE"})
    @Path("/riduzione-aumento/idRiduzioneAumento/{idRiduzioneAumento}")
    Response loadRiduzioneAumentoByIdRiduzioneAumento(@PathParam("idRiduzioneAumento") @Min(value = 1, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer") Long idRiduzioneAumento, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    /**
     * Load Riduzioni by id Or Cod RiduzioneAumento.
     *
     * @param idOrCodRiduzioneAumento idOrCodRiduzioneAumento
     * @param securityContext   SecurityContext
     * @param httpHeaders       HttpHeaders
     * @param httpRequest       HttpServletRequest
     * @return Response response
     */
    @GET
    @RolesAllowed({"AMMINISTRATORE", "GESTORE_BASE", "GESTORE_DATI", "CONSULTATORE"})
    @Path("/riduzioni/{codRiduzione}")
    Response loadRiduzioniByIdOrCodRiduzioneAumento(@PathParam("codRiduzione") String idOrCodRiduzioneAumento , @QueryParam("dataIniVal") String dataIniVal, @QueryParam("dataFineVal") String dataFineVal,  @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    /**
     * Load Aumenti by id Or Cod RiduzioneAumento.
     *
     * @param idOrCodRiduzioneAumento idOrCodRiduzioneAumento
     * @param securityContext   SecurityContext
     * @param httpHeaders       HttpHeaders
     * @param httpRequest       HttpServletRequest
     * @return Response response
     */
    @GET
    @RolesAllowed({"AMMINISTRATORE", "GESTORE_BASE", "GESTORE_DATI", "CONSULTATORE"})
    @Path("/aumenti/{codAumenti}")
    Response loadAumentoByIdOrCodRiduzioneAumento(@PathParam("codAumenti") String idOrCodRiduzioneAumento , @QueryParam("dataIniVal") String dataIniVal, @QueryParam("dataFineVal") String dataFineVal,  @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
   
    
    /**
     * Load riduzione  by flgManuale.
     *
     * @param flgManuale flgManuale
     * @param securityContext   SecurityContext
     * @param httpHeaders       HttpHeaders
     * @param httpRequest       HttpServletRequest
     * @return Response response
     */
    @GET
    @RolesAllowed({"AMMINISTRATORE", "GESTORE_BASE", "GESTORE_DATI", "CONSULTATORE"})
    @Path("/riduzioni")
    Response loadRiduzioniByflgManuale(@QueryParam("flgManuale") String flgManuale , @QueryParam("dataIniVal") String dataIniVal, @QueryParam("dataFineVal") String dataFineVal,  @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
  
    /**
     * Load riduzione  by flgManuale.
     *
     * @param flgManuale flgManuale
     * @param securityContext   SecurityContext
     * @param httpHeaders       HttpHeaders
     * @param httpRequest       HttpServletRequest
     * @return Response response
     */
    @GET
    @RolesAllowed({"AMMINISTRATORE", "GESTORE_BASE", "GESTORE_DATI", "CONSULTATORE"})
    @Path("/aumenti")
    Response loadAumentiByflgManuale(@QueryParam("flgManuale") String flgManuale , @QueryParam("dataIniVal") String dataIniVal, @QueryParam("dataFineVal") String dataFineVal,  @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    /**
     * Load riduzioni by idOrCodTipoUso and flag manuale and date inizio e fine validita  
     *
     * @param idOrCodTipoUso idOrCodTipoUso
     * @param securityContext    SecurityContext
     * @param httpHeaders        HttpHeaders
     * @param httpRequest        HttpServletRequest
     * @return Response response
     */
    @GET
    @RolesAllowed({"AMMINISTRATORE", "GESTORE_BASE", "GESTORE_DATI", "CONSULTATORE"})
    @Path("/tipi-uso/{idTipoUso}/riduzioni")
    Response loadRiduzioniByIdOrCodTipoUsoFlgManuale(@PathParam("idTipoUso") String idOrCodTipoUso, @QueryParam("flgManuale") String flgManuale,   @QueryParam("dataIniVal") String dataIniVal, @QueryParam("dataFineVal") String dataFineVal,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    /**
     * Load aumenti by idOrCodTipoUso and flag manuale and date inizio e fine validita  
     *
     * @param idOrCodTipoUso idOrCodTipoUso
     * @param securityContext    SecurityContext
     * @param httpHeaders        HttpHeaders
     * @param httpRequest        HttpServletRequest
     * @return Response response
     */
    @GET
    @RolesAllowed({"AMMINISTRATORE", "GESTORE_BASE", "GESTORE_DATI", "CONSULTATORE"})
    @Path("/tipi-uso/{idTipoUso}/aumenti")
    Response loadAumentiByIdOrCodTipoUsoFlgManuale(@PathParam("idTipoUso") String idOrCodTipoUso,@QueryParam("flgManuale") String flgManuale,   @QueryParam("dataIniVal") String dataIniVal, @QueryParam("dataFineVal") String dataFineVal,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

}

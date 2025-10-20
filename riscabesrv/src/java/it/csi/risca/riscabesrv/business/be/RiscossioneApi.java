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
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.web.bind.annotation.RequestBody;

import it.csi.risca.riscabesrv.dto.RiscossioneDTO;
import it.csi.risca.riscabesrv.dto.RiscossioneSearchDTO;

/**
 * The interface Riscossione api.
 *
 * @author CSI PIEMONTE
 */
@Path("")
@Produces(MediaType.APPLICATION_JSON)
public interface RiscossioneApi {
	
    /**
     * Save riscossione response.
     *
     * @param riscossione  RiscossioneDTO
     * @param securityContext SecurityContext
     * @param httpHeaders     HttpHeaders
     * @param httpRequest     HttpServletRequest
     * @return Response AccreditamentoDTO
     */
    @POST
    @Path("/riscossioni")
    @Consumes(MediaType.APPLICATION_JSON)
    Response saveRiscossione(@RequestBody RiscossioneDTO riscossione, @QueryParam("fruitore") String fruitore, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    @PUT
    @Path("/riscossioni")
    @Consumes(MediaType.APPLICATION_JSON)
    Response updateRiscossione(@RequestBody RiscossioneDTO riscossione, @QueryParam("fruitore") String fruitore, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) throws Exception;
	
    @GET
    @Path("/riscossioni/{codRiscossione}")
    Response getRiscossione(@PathParam("codRiscossione") String codRiscossione, @QueryParam("fruitore") String fruitore, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    @GET
    @Path("/riscossioni")
    Response getRiscossioniGruppo(@QueryParam("idGruppo") String idGruppo, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    @POST
    @Path("/_search/riscossioni")
    @Consumes(MediaType.APPLICATION_JSON)
    Response searchRiscossione(@RequestBody RiscossioneSearchDTO RiscossioneSearch,
    		@QueryParam("fruitore") String fruitore,@QueryParam("modalitaRicerca") String modalitaRicerca ,
    		@QueryParam(value = "offset") @Min(value = 0, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer") Integer offset,
    		@QueryParam(value = "limit") @Min(value = 0, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer") Integer limit, 
    		@DefaultValue("codiceUtenza") @QueryParam(value = "sort") String sort,
    		@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    @GET
    @Path("/riscossioni/{idRiscossione}/dati-tecnici")
    Response loadDatoTecnico(@PathParam("idRiscossione") @Min(value = 1, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer") Long idRiscossione,@QueryParam("fruitore") String fruitore, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    @GET
    @Path("/_verify_riscossioni_stdebitori/{idOggetto}")
    Response verifyRiscossioniSTDebitori(@PathParam("idOggetto") @Min(value = 1, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer") Long idOggetto, 
    		@QueryParam("indTipoOggetto") String indTipoOggetto,  @QueryParam("idTipoOper") String idTipoOper,
    		@QueryParam("idRiscossione") @Min(value = 1, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer") Long idRiscossione ,
    		@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    @GET
    @Path("/utenze-comp/idStatoDebitorio/{idStatoDebitorio}")
    Response getCodiciUtenzaByIdStatoDebitorio(@PathParam("idStatoDebitorio")@Min(value = 1, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer") Long idStatoDebitorio, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    @PUT
    @Path("/riscossioni/stato-pratica")
    @Consumes(MediaType.APPLICATION_JSON)
    Response updateRiscossioneStatoPratica(@RequestBody RiscossioneDTO riscossione, @QueryParam("fruitore") String fruitore, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) throws Exception;


    @POST
    @Path("/_search/riscossioni/delegati")
    @Consumes(MediaType.APPLICATION_JSON)
    Response searchRiscossioneDelegati(@RequestBody RiscossioneSearchDTO RiscossioneSearch,
    		@QueryParam("fruitore") String fruitore,@QueryParam("modalitaRicerca") String modalitaRicerca ,
    		@QueryParam(value = "offset") @Min(value = 0, message = "offset  deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "offset deve essere minore o uguale a max value integer") Integer offset,
    		@QueryParam(value = "limit") @Min(value = 0, message = "limit deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "limit deve essere minore o uguale a max value integer") Integer limit,
    		@DefaultValue("codiceUtenza") @QueryParam(value = "sort") String sort,
     @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    @POST
    @Path("/_count/riscossioni/delegati")
    @Consumes(MediaType.APPLICATION_JSON)
    Response countRiscossioniDelegati(@RequestBody RiscossioneSearchDTO RiscossioneSearch, @QueryParam("fruitore") String fruitore,
    		@QueryParam("modalitaRicerca") String modalitaRicerca ,
    	 @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

}

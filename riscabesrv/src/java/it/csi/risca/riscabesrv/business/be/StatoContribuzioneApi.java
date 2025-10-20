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

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


/**
 * The interface Stato contribuzione api.
 *
 * @author CSI PIEMONTE
 */
@Path("/stato-contribuzione")
@Produces(MediaType.APPLICATION_JSON)
public interface StatoContribuzioneApi {

	
    @GET
    Response loadStatoContribuzione();
	
    @GET
    @Path("/{idStatoContribuzione}")
    Response loadStatoContribuzioneById(@PathParam("idStatoContribuzione")  @Min(value = 1, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")  Long idStatoContribuzione);

}

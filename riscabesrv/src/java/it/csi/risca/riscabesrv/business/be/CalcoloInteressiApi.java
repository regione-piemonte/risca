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

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
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
public interface CalcoloInteressiApi {


    /**
	 * calcoloInteressi
	 * 
     * @param fruitore   fruitore
     * @param importo importo
     * @param dataScadenza dataScadenza
     * @param dataVersamento dataVersamento
     * @param securityContext SecurityContext
     * @param httpHeaders     HttpHeaders
     * @param httpRequest     HttpServletRequest
     * @return Response response
     */
    @GET
    @Path("/calcolo-interessi")
    Response calcoloInteressi(@QueryParam("fruitore") String fruitore,@QueryParam("importo") BigDecimal importo, 
    		@QueryParam("dataScadenza") String dataScadenza, @QueryParam("dataVersamento") String dataVersamento,
    		@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

}

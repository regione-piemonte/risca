/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.business.be.exception;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * The type Custom read exception mapper.
 *
 * @author CSI PIEMONTE
 */
@Provider
public class CustomReadExceptionMapper implements ExceptionMapper<NotFoundException>{

    /**
     * @param arg0
     * @return Response
     */
    public Response toResponse(NotFoundException arg0) {
        Response r = Response.serverError().entity("errore di unmarshal:"+arg0.getClass()).build();
        return r;
    }

}
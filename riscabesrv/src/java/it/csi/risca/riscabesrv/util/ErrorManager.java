/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.util;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.risca.riscabesrv.business.be.exception.SystemException;
import it.csi.risca.riscabesrv.business.be.impl.dao.MessaggiDAO;
import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.dto.MessaggiDTO;

/**
 * The type Error manager.
 *
 * @author CSI PIEMONTE
 */
@Component
public class ErrorManager {

    @Autowired
    private MessaggiDAO messaggioDAO;

    /**
     * Gets error.
     *
     * @param status  the status
     * @param code    the code
     * @param title   the title
     * @param details the details
     * @param links   the links
     * @return the error
     * @throws SystemException 
     * @throws SQLException 
     */
    public ErrorDTO getError(String status, String code, String title, Map<String, String> details, List<String> links) throws SQLException, SystemException {
        MessaggiDTO messaggio = StringUtils.isNotBlank(code) ? messaggioDAO.loadMessaggiByCodMessaggio(code) : null;
        MessaggiDTO msg = null != messaggio ? messaggio : null;
        title = null != msg ? msg.getDesTestoMessaggio() : title;
        return new ErrorDTO(status, code, null != title ? title : "Errore inaspettato nella gestione della richiesta. Riprova a breve", details, links);
    }

    /**
     * Gets generic error response.
     *
     * @return the generic error response
     */
    public Response getGenericErrorResponse() {
        ErrorDTO error = null;
		try {
			error = getError("500", "E100", null, null, null);
		} catch (Exception e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR).status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
         }
        return Response.serverError().status(500).entity(error).build();
    }
}
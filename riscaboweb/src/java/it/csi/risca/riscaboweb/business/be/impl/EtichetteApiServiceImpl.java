/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscaboweb.business.be.impl;

import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.risca.riscaboweb.business.be.EtichetteApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.exception.ParameterValidationException;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.PingApiServiceHelper;
import it.csi.risca.riscaboweb.filter.ValidationFilter;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.EtichetteApiServiceHelper;

/**
 * The type TassiDiInteresseApiServiceImpl.
 *
 * @author CSI PIEMONTE
 */
@Component 
public class EtichetteApiServiceImpl extends AbstractApiServiceImpl implements EtichetteApi {

    private final String className = this.getClass().getSimpleName();

    private static final String IDENTITY = "identity";

    @Autowired
    private EtichetteApiServiceHelper etichetteApiServiceHelper;
    

    @Override
    public Response loadConfigurazioniElementiApplicativi(
    		Long idAmbito, 
    		String fruitore,
    		HttpHeaders httpHeaders, 
    		HttpServletRequest httpRequest) {
    	    	
    	try {
        	return etichetteApiServiceHelper.loadConfigurazioniElementiApplicativi(
        			idAmbito, 
        			fruitore, 
        			getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest));
        	
        } catch (Exception e) {
            // Log dell'eccezione completa
            LOGGER.error("Errore durante il caricamento del: " + e.getMessage(), e);
            // Restituzione di una risposta di errore
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Errore durante il caricamento delle configurazioni per le etichette: " + e.getMessage()).build();
        }
    }
}

/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscaboweb.business.be.helper.riscabe;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.AbstractServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.AddHeaderFilter;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.AssegnaPagamentoDTO;
import it.csi.risca.riscaboweb.filter.IrideIdAdapterFilter;
import it.csi.risca.riscaboweb.util.Constants;
import it.csi.risca.riscaboweb.util.Utils;

public class AssegnaPagamentiApiServiceHelper  extends AbstractServiceHelper {

	private final String className = this.getClass().getSimpleName();
	
	private static final String ASSEGNA_PAGAMENTI = "/assegna-pagamenti"; 
	
    public AssegnaPagamentiApiServiceHelper(String hostname, String endpointBase) {
        this.hostname = hostname;
        this.endpointBase = hostname + endpointBase;
    }

	public AssegnaPagamentoDTO AssegnaPagamentiPost(String fruitore, AssegnaPagamentoDTO assegnaPagamento,
			 HttpHeaders httpHeaders, HttpServletRequest httpRequest)  throws GenericException, ProcessingException{
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
        String targetUrl = Utils.buildTargetUrl(this.endpointBase + ASSEGNA_PAGAMENTI,Constants.FRUITORE, fruitore);
        try {
             MultivaluedMap<String, Object> map = getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);

				
             Entity<AssegnaPagamentoDTO> entity = Entity.json(assegnaPagamento);
             Response resp = getInvocationBuilder(targetUrl, map).post(entity);
             LOGGER.debug(getClassFunctionDebugString(className, methodName, " RESPONSE STATUS : " + resp.getStatus()) );	
             handleResponseErrors(resp);
             return assegnaPagamento;
        } catch (ProcessingException e) {
            LOGGER.error(getClassFunctionErrorInfo(className, methodName,e),e);
            throw e;
        } finally {
            LOGGER.debug(getClassFunctionEndInfo(className, methodName));
        }
	}

}

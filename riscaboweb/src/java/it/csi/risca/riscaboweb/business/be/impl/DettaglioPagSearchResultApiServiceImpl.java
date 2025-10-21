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

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.risca.riscaboweb.business.be.DettaglioPagSearchResultApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.exception.ParameterValidationException;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.DettaglioPagSearchResultApiServiceHelper;
import it.csi.risca.riscaboweb.filter.ValidationFilter;

/**
 * The type DettaglioPag api service.
 *
 * @author CSI PIEMONTE
 */
@Component
public class DettaglioPagSearchResultApiServiceImpl extends AbstractApiServiceImpl implements DettaglioPagSearchResultApi {
	

	 private final String className = this.getClass().getSimpleName();
    
    @Autowired
    private DettaglioPagSearchResultApiServiceHelper dettaglioPagSearchResultApiServiceHelper;
    
	@Override
	public Response getDettaglioPagSearchResultByIdPagamento(String idPagamentoS, String fruitore, 
			 HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
	        Response response = null;
	        try {
				Integer  idPagamento = ValidationFilter.validateParameter("idPagamento", idPagamentoS, 0,Integer.MAX_VALUE);
	        	response = dettaglioPagSearchResultApiServiceHelper.getDettaglioPagSearchResultByIdPagamento(fruitore, idPagamento,  httpHeaders, httpRequest);
	            
			 } catch (ParameterValidationException e) {
		        	return handleException(e, className, methodName);   
	          } catch (GenericException e) {
	        	return handleException(e, className, methodName);   
	        } catch (ProcessingException e) {
	        	return handleException(e, className, methodName);   
	        } finally {
	        	LOGGER.debug(getClassFunctionEndInfo(className, methodName));
	        }
	        return response;
	}


}

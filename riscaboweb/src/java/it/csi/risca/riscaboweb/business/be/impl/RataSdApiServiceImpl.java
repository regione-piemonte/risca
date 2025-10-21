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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.risca.riscaboweb.business.be.RataSdApi;
import it.csi.risca.riscaboweb.business.be.exception.DAOException;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.exception.ParameterValidationException;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.RataSdApiServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.RataSdDTO;
import it.csi.risca.riscaboweb.filter.ValidationFilter;

/**
 * The type RataSd api service.
 *
 * @author CSI PIEMONTE
 */
@Component
public class RataSdApiServiceImpl extends AbstractApiServiceImpl  implements RataSdApi {

	private static final String IDENTITY = "identity";
	 private final String className = this.getClass().getSimpleName();

	@Autowired
	private RataSdApiServiceHelper rataSdHelper;

	/**
	 * @param securityContext SecurityContext
	 * @param httpHeaders     HttpHeaders
	 * @param httpRequest     HttpServletRequest
	 * @return Response
	 */


	@Override
	public Response loadRataSdByStatoDebitorio(String fruitore, String idStatoDebitorioS,  HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		RataSdDTO rataSd = new RataSdDTO();
		
		try {
			Integer  idStatoDebitorio = ValidationFilter.validateParameter("idStatoDebitorio", idStatoDebitorioS, 0, Integer.MAX_VALUE);
			rataSd = rataSdHelper.loadRataSdByStatoDebitorio(fruitore, idStatoDebitorio, httpHeaders, httpRequest); 
		} catch (ParameterValidationException e) {
        	return handleException(e, className, methodName);   

        }  catch (GenericException e) {
        	return handleException(e, className, methodName);   
        } catch (ProcessingException e) {
        	return handleException(e, className, methodName);   
        } finally {
        	LOGGER.debug(getClassFunctionEndInfo(className, methodName));
        }
        return Response.ok(rataSd).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();		
	}


	@Override
	public Response saveNRataSd(String fruitore, String idStatoDebitorioS,  HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) throws DAOException {
		 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		List<Integer> listIdRataSd = new ArrayList<>();
		try {
			Integer  idStatoDebitorio = ValidationFilter.validateParameter("idStatoDebitorio", idStatoDebitorioS, 0, Integer.MAX_VALUE);
			listIdRataSd = rataSdHelper.saveNRataSd(fruitore, idStatoDebitorio, httpHeaders, httpRequest);
		} catch (ParameterValidationException e) {
        	return handleException(e, className, methodName);   

        }  catch (GenericException e) {
        	return handleException(e, className, methodName);   
        } catch (ProcessingException e) {
        	return handleException(e, className, methodName);   
        } finally {
        	LOGGER.debug(getClassFunctionEndInfo(className, methodName));
        }
        return Response.ok(listIdRataSd).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();	
	}

}

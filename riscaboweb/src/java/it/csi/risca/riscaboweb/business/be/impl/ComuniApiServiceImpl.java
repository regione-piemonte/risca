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

import it.csi.risca.riscaboweb.business.be.ComuniApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.ComuniApiServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ComuneDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ComuneExtendedDTO;

@Component
public class ComuniApiServiceImpl extends AbstractApiServiceImpl implements ComuniApi{
	
	private static final String IDENTITY = "identity";
	 private final String className = this.getClass().getSimpleName();

	@Autowired
    private ComuniApiServiceHelper comuniApiServiceHelper;
	
	@Override
	public Response loadComuni(boolean attivo,  HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {	
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));

        List<ComuneExtendedDTO> listComuni = new ArrayList<>();
        try {
        	listComuni = comuniApiServiceHelper.loadComuni(attivo, httpHeaders,httpRequest);
        } catch (GenericException e) {
        	return handleException(e, className, methodName);   
        } catch (ProcessingException e) {
        	return handleException(e, className, methodName);   
        } finally {
        	LOGGER.debug(getClassFunctionEndInfo(className, methodName));
        }
        return Response.ok(listComuni).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response loadComuniByIdOrCod(String idRegione, String idProvincia, String codIstatComune,
			 HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		List<ComuneDTO> listComuni = new ArrayList<>();
        try {
        	listComuni = comuniApiServiceHelper.loadComuniByIdOrCod(getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest), idRegione, idProvincia, codIstatComune);
        } catch (GenericException e) {
        	return handleException(e, className, methodName);   
        } catch (ProcessingException e) {
        	return handleException(e, className, methodName);   
        } finally {
        	LOGGER.debug(getClassFunctionEndInfo(className, methodName));
        }
        return Response.ok(listComuni).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response loadComuniByCodRegione(String codRegione, String codProvincia, 
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		List<ComuneDTO> listComuni = new ArrayList<>();
        try {
        	listComuni = comuniApiServiceHelper.loadComuniByCodRegione(getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest), codRegione, codProvincia);
        } catch (GenericException e) {
        	return handleException(e, className, methodName);   
        } catch (ProcessingException e) {
        	return handleException(e, className, methodName);   
        } finally {
        	LOGGER.debug(getClassFunctionEndInfo(className, methodName));
        }
        return Response.ok(listComuni).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response loadComuniByRicerca(String q, boolean attivo, 
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {	
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
        List<ComuneExtendedDTO> listComuni = new ArrayList<>();
        try {
        	listComuni = comuniApiServiceHelper.loadComuniByRicerca(getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest), q ,attivo);
        } catch (GenericException e) {
        	return handleException(e, className, methodName);   
        } catch (ProcessingException e) {
        	return handleException(e, className, methodName);   
        } finally {
        	LOGGER.debug(getClassFunctionEndInfo(className, methodName));
        }
        return Response.ok(listComuni).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response loadComuneByCodIstatComune(String codIstatComune, 
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		ComuneDTO comune;
        try {
        	comune = comuniApiServiceHelper.loadComuneByCodIstatComune(getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest), codIstatComune);
        } catch (GenericException e) {
        	return handleException(e, className, methodName);   
        } catch (ProcessingException e) {
        	return handleException(e, className, methodName);   
        } finally {
        	LOGGER.debug(getClassFunctionEndInfo(className, methodName));
        }
        return Response.ok(comune).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

}

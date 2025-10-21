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

import it.csi.risca.riscaboweb.business.be.ProvinceApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.ProvinceApiServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ProvinceExtendedDTO;

@Component
public class ProvinceApiServiceImpl extends AbstractApiServiceImpl implements ProvinceApi {
	
	private static final String IDENTITY = "identity";
	 private final String className = this.getClass().getSimpleName();

	@Autowired
	ProvinceApiServiceHelper provinceApiServiceHelper;

	@Override
	public Response getProvince(boolean attivo,  HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
        List<ProvinceExtendedDTO> listProvince = new ArrayList<>();
        try {
        	listProvince = provinceApiServiceHelper.getProvince(attivo, httpHeaders,httpRequest);
        } catch (GenericException e) {
        	return handleException(e, className, methodName);   
        } catch (ProcessingException e) {
        	return handleException(e, className, methodName);   
        } finally {
        	LOGGER.debug(getClassFunctionEndInfo(className, methodName));
        }
        return Response.ok(listProvince).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response getProvinceByCodIstatComune(String codIstatComune, 
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
        List<ProvinceExtendedDTO> listProvince = new ArrayList<>();
        try {
        	listProvince = provinceApiServiceHelper.getProvinceByCodIstatComune(getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest), codIstatComune);
        } catch (GenericException e) {
        	return handleException(e, className, methodName);   
        } catch (ProcessingException e) {
        	return handleException(e, className, methodName);   
        } finally {
        	LOGGER.debug(getClassFunctionEndInfo(className, methodName));
        }
        return Response.ok(listProvince).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response getProvinceByCodRegione(String codRegione,  HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
    List<ProvinceExtendedDTO> listProvince = new ArrayList<>();
        try {
        	listProvince = provinceApiServiceHelper.getProvinceByCodRegione(getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest), codRegione);
        } catch (GenericException e) {
        	return handleException(e, className, methodName);   
        } catch (ProcessingException e) {
        	return handleException(e, className, methodName);   
        } finally {
        	LOGGER.debug(getClassFunctionEndInfo(className, methodName));
        }
        return Response.ok(listProvince).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response loadProvinciaByIdOrCodRegioneAndIdOrCodProvincia(String codRegione, String codProvincia,
			 HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
      ProvinceExtendedDTO provincia = new ProvinceExtendedDTO();
	        try {
	        	provincia = provinceApiServiceHelper.loadProvinciaByIdOrCodRegioneAndIdOrCodProvincia(getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest), codRegione, codProvincia);
	        } catch (GenericException e) {
	        	return handleException(e, className, methodName);   
	        } catch (ProcessingException e) {
	        	return handleException(e, className, methodName);   
	        } finally {
	        	LOGGER.debug(getClassFunctionEndInfo(className, methodName));
	        }
	        return Response.ok(provincia).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

}

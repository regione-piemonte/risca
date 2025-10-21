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

import it.csi.risca.riscaboweb.business.be.TipoRicercaRimborsiApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.exception.ParameterValidationException;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.TipoRicercaRimborsiApiServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.TipoRicercaRimborsoDTO;
import it.csi.risca.riscaboweb.filter.ValidationFilter;

/**
 * The type TipoRicercaRimborsiApiServiceImpl.
 *
 * @author CSI PIEMONTE
 */
@Component
public class TipoRicercaRimborsiApiServiceImpl extends AbstractApiServiceImpl implements TipoRicercaRimborsiApi {

	private static final String IDENTITY = "identity";
	 private final String className = this.getClass().getSimpleName();

    @Autowired
    private TipoRicercaRimborsiApiServiceHelper tipoRicercaRimborsiApiServiceHelper;

	@Override
	public Response loadAllTipoRicercaRimborsi( HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
        List<TipoRicercaRimborsoDTO> tipiRicercaMorosita = new ArrayList<>();
		try {
			tipiRicercaMorosita = tipoRicercaRimborsiApiServiceHelper.loadAllTipoRicercaRimborsi(httpHeaders,httpRequest);
        } catch (GenericException e) {
        	return handleException(e, className, methodName);   
        } catch (ProcessingException e) {
        	return handleException(e, className, methodName);   
        } finally {
        	LOGGER.debug(getClassFunctionEndInfo(className, methodName));
        }
        return Response.ok(tipiRicercaMorosita).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response ricercaRimborsi(String fruitore, String tipoRicercaRimborsi, String annoS, String offsetS,
			String limitS, String sort,  HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) { 
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
			try {
				Integer  anno = null;
				if(annoS != null)
					anno = ValidationFilter.validateParameter("anno", annoS, 0, Integer.MAX_VALUE);
				
				Integer  offset = ValidationFilter.validateParameter("offset", offsetS, 0, Integer.MAX_VALUE);
				Integer  limit = ValidationFilter.validateParameter("limit", limitS, 0, Integer.MAX_VALUE);
				return tipoRicercaRimborsiApiServiceHelper.ricercaRimborsi(fruitore, tipoRicercaRimborsi, anno, offset, limit, sort,  httpHeaders, httpRequest);
			} catch (ParameterValidationException e) {
	        	return handleException(e, className, methodName);   

	        }  catch (GenericException e) {
	        	return handleException(e, className, methodName);   
	        } catch (ProcessingException e) {
	        	return handleException(e, className, methodName);   
	        } finally {
	        	LOGGER.debug(getClassFunctionEndInfo(className, methodName));
	        }
	}


}

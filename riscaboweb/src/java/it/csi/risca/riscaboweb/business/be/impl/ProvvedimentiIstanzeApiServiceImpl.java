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

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.risca.riscaboweb.business.be.ProvvedimentiIstanzeApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.exception.ParameterValidationException;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.ProvvedimentiIstanzeApiServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ProvvedimentoDTO;
import it.csi.risca.riscaboweb.filter.ValidationFilter;


/**
 * The type Provvedimenti Istanze api service.
 *
 * @author CSI PIEMONTE
 */
@Component
public class ProvvedimentiIstanzeApiServiceImpl extends AbstractApiServiceImpl implements ProvvedimentiIstanzeApi{
	
	private static final String IDENTITY = "identity";
	 private final String className = this.getClass().getSimpleName();

	@Autowired
	private ProvvedimentiIstanzeApiServiceHelper provvedimentiIstanzeApiServiceHelper;
	

	@Override
	public Response getProvvedimentiIstanze( HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) throws Exception {
		 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		 List<ProvvedimentoDTO> listProvvedimentoDTO;
		try {
			listProvvedimentoDTO = provvedimentiIstanzeApiServiceHelper.getProvvedimentiIstanze(getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest));
		} catch (GenericException e) {
			return handleException(e, className, methodName);   

		}

		 LOGGER.debug(getClassFunctionEndInfo(className, methodName));
        return Response.ok(listProvvedimentoDTO).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response getProvvedimentiIstanzeByidRiscossione(String idRiscossioneS, 
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws Exception {
		 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		 
		 List<ProvvedimentoDTO> listProvvedimentoDTO;
		try { 
			Integer  idRiscossione = ValidationFilter.validateParameter("idRiscossione", idRiscossioneS, 0, Integer.MAX_VALUE);
			listProvvedimentoDTO = provvedimentiIstanzeApiServiceHelper.getProvvedimentiIstanzeByidRiscossione(idRiscossione, getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest));
		} catch (ParameterValidationException e) {
        	return handleException(e, className, methodName);   

        } catch (GenericException e) {
			return handleException(e, className, methodName);   

		}

		LOGGER.debug(getClassFunctionEndInfo(className, methodName));
        return Response.ok(listProvvedimentoDTO).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response getProvvedimentoIstanzaByIdProvvedimenti(String idProvvedimentiIstanzeS,
			 HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws Exception {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
       ProvvedimentoDTO provvedimentoDTO;
	try {
		Integer  idProvvedimentiIstanze = ValidationFilter.validateParameter("idProvvedimentiIstanze", idProvvedimentiIstanzeS, 0, Integer.MAX_VALUE);
		provvedimentoDTO = provvedimentiIstanzeApiServiceHelper.getProvvedimentoIstanzaByIdProvvedimenti(idProvvedimentiIstanze, getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest));
	} catch (ParameterValidationException e) {
    	return handleException(e, className, methodName);   

    } catch (GenericException e) {
		return handleException(e, className, methodName);   

	}

	LOGGER.debug(getClassFunctionEndInfo(className, methodName));
        return Response.ok(provvedimentoDTO).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response saveProvvedimentiIstanze(ProvvedimentoDTO provvedimentoDTO, 
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
     
	try {
		provvedimentoDTO = provvedimentiIstanzeApiServiceHelper.saveProvvedimentiIstanze(provvedimentoDTO,  httpHeaders, httpRequest);

	} catch (Exception e) {
		return handleException(e, className, methodName);   

	}finally {
		LOGGER.debug(getClassFunctionEndInfo(className, methodName));
    }
	return Response.ok(provvedimentoDTO).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response updateProvvedimentiIstanze(ProvvedimentoDTO provvedimentoDTO, 
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws Exception {
		 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		 		     
		try {
			provvedimentoDTO = provvedimentiIstanzeApiServiceHelper.updateProvvedimentiIstanze(provvedimentoDTO, httpHeaders, httpRequest);

		} catch (Exception e) {
			return handleException(e, className, methodName);   

		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
	    }
		return Response.ok(provvedimentoDTO).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response deleteProvvedimentiIstanze(String idProvvedimentiIstanzeS, 
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		 ProvvedimentoDTO provvedimentoDTO =null;
			try {
				Integer  idProvvedimentiIstanze = ValidationFilter.validateParameter("idProvvedimentiIstanze", idProvvedimentiIstanzeS, 0, Integer.MAX_VALUE);

				 provvedimentoDTO = provvedimentiIstanzeApiServiceHelper.deleteProvvedimentiIstanze(idProvvedimentiIstanze,  httpHeaders, httpRequest);


			} catch (ParameterValidationException e) {
	        	return handleException(e, className, methodName);   

	         
			} catch (GenericException e) {
				return handleException(e, className, methodName);   

			}  catch (Exception e) {
				return handleException(e, className, methodName);   

			}finally {
				LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		    }
			return Response.ok(provvedimentoDTO).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

}

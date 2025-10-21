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
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.risca.riscaboweb.business.be.AccertamentoApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.exception.ParameterValidationException;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.AccertamentoApiServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.AccertamentoExtendedDTO;
import it.csi.risca.riscaboweb.filter.ValidationFilter;

/**
 * The type Accertamento api service Impl.
 *
 * @author CSI PIEMONTE
 */
@Component
public class AccertamentoApiServiceImpl extends AbstractApiServiceImpl implements AccertamentoApi{

    private final String className = this.getClass().getSimpleName();
    private static final String IDENTITY = "identity";

    @Autowired
    private AccertamentoApiServiceHelper accertamentoApiServiceHelper;
    
	@Override
	public Response loadAllAccertamentiOrByIdStatoDeb(String fruitore, String idStatoDebitorioS, String offsets,
			String limits, String sort, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
			LOGGER.debug(getClassFunctionBeginInfo(className, methodName));

			try {
				Integer  idStatoDebitorio = ValidationFilter.validateParameter("idStatoDebitorio", idStatoDebitorioS, 0, Integer.MAX_VALUE);
				Integer  limit = ValidationFilter.validateParameter("limits", limits, 0,Integer.MAX_VALUE);
				Integer  offset = ValidationFilter.validateParameter("offset", offsets, 0, Integer.MAX_VALUE);
				return accertamentoApiServiceHelper.loadAllAccertamentiOrByIdStatoDeb(fruitore,idStatoDebitorio, offset, limit, sort, httpHeaders, httpRequest);
				
			 } catch (ParameterValidationException e) {
		        	return handleException(e, className, methodName);   
	        } catch (GenericException e) {
	        	return handleException(e, className, methodName);   
	        } catch (ProcessingException e) {
	        	return handleException(e, className, methodName);   
	        } finally {
	        	LOGGER.debug(getClassFunctionEndInfo(className, methodName));
	        }

		
	}
       
	

	@Override
	public Response saveAccertamenti(AccertamentoExtendedDTO accertamento, String fruitore, 
			HttpHeaders httpHeaders, HttpServletRequest httpRequest)  {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));        AccertamentoExtendedDTO dto = new AccertamentoExtendedDTO();
        try {
			dto = accertamentoApiServiceHelper.saveAccertamenti(accertamento, fruitore, httpHeaders, httpRequest);
	
        } catch (GenericException e) {
        	return handleException(e, className, methodName);   
        } catch (ProcessingException e) {
        	return handleException(e, className, methodName);   
        } finally {
        	LOGGER.debug(getClassFunctionEndInfo(className, methodName));
        }

		return Response.ok(dto).status(201).header(HttpHeaders.CONTENT_ENCODING, "identity").build();
	}

	@Override
	public Response updateAccertamenti(AccertamentoExtendedDTO accertamento,String fruitore, 
			HttpHeaders httpHeaders, HttpServletRequest httpRequest)  {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName)); 
		AccertamentoExtendedDTO dto = new AccertamentoExtendedDTO();
        try {
			dto = accertamentoApiServiceHelper.updateAccertamenti(accertamento, fruitore, httpHeaders, httpRequest);
        } catch (GenericException e) {
        	return handleException(e, className, methodName);   
        } catch (ProcessingException e) {
        	return handleException(e, className, methodName);   
        } finally {
        	LOGGER.debug(getClassFunctionEndInfo(className, methodName));
        }

		return Response.ok(dto).status(201).header(HttpHeaders.CONTENT_ENCODING, "identity").build();
	}

	@Override
	public Response deleteByIdAccertamento(String idAccertamentoStr, String fruitore, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		try {
			Integer idAccertamento = ValidationFilter.validateParameter("idAccertamento", idAccertamentoStr, 0,
					Integer.MAX_VALUE);
			idAccertamentoStr = accertamentoApiServiceHelper.deleteByIdAccertamento(idAccertamento, fruitore, httpHeaders, httpRequest);
		} catch (ParameterValidationException e) {
			return handleException(e, className, methodName);

		} catch (GenericException e) {
			return handleException(e, className, methodName);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return Response.ok(idAccertamentoStr).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

}

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

import it.csi.risca.riscaboweb.business.be.LockRiscossioneApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.exception.ParameterValidationException;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.LockRiscossioneApiServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.LockRiscossioneDTO;
import it.csi.risca.riscaboweb.filter.ValidationFilter;

/**
 * The type Lock Riscossione Api Service Impl.
 *
 * @author CSI PIEMONTE
 */
@Component
public class LockRiscossioneApiServiceImpl extends AbstractApiServiceImpl implements LockRiscossioneApi{
	
	private static final String IDENTITY = "identity";
	 private final String className = this.getClass().getSimpleName();

    @Autowired
    private LockRiscossioneApiServiceHelper lockRiscossioneApiServiceHelper;
    
    
	@Override
	public Response getAllLockRiscossione(String fruitore,  HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		List<LockRiscossioneDTO> lockRiscossione = new ArrayList<>();
		 try {
			 lockRiscossione = lockRiscossioneApiServiceHelper.getAllLockRiscossione(fruitore,  httpHeaders,
						 httpRequest);
	        } catch (GenericException e) {
	        	return handleException(e, className, methodName);   
	        } catch (ProcessingException e) {
	        	return handleException(e, className, methodName);   
	        } finally {
	        	LOGGER.debug(getClassFunctionEndInfo(className, methodName));
	        }
		return Response.ok(lockRiscossione).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response getLockRiscossioneByIdRiscossione(String fruitore, String idRiscossioneS,
			 HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		LockRiscossioneDTO lockRiscossione = null;
		 try {
			Integer  idRiscossione = ValidationFilter.validateParameter("idRiscossione", idRiscossioneS, 0,Integer.MAX_VALUE);
			 lockRiscossione = lockRiscossioneApiServiceHelper.getLockRiscossioneByIdRiscossione(fruitore,idRiscossione,  httpHeaders,
					 httpRequest);				
			} catch (ParameterValidationException e) {
	        	return handleException(e, className, methodName);   

           }  catch (GenericException e) {
	        	return handleException(e, className, methodName);   
	        } catch (ProcessingException e) {
	        	return handleException(e, className, methodName);   
	        } finally {
	        	LOGGER.debug(getClassFunctionEndInfo(className, methodName));
	        }
		return Response.ok(lockRiscossione).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response saveLockRiscossione(LockRiscossioneDTO lockRiscossioneDTO, String fruitore,
			 HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		LockRiscossioneDTO lockRiscossione = new LockRiscossioneDTO();
		 try {
			 lockRiscossione = lockRiscossioneApiServiceHelper.saveLockRiscossione(lockRiscossioneDTO,fruitore,  httpHeaders,
					 httpRequest);				
	        } catch (GenericException e) {
	        	return handleException(e, className, methodName);   
	        } catch (ProcessingException e) {
	        	return handleException(e, className, methodName);   
	        } finally {
	        	LOGGER.debug(getClassFunctionEndInfo(className, methodName));
	        }
		return Response.ok(lockRiscossione).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response deleteLockRiscossione(String idRiscossioneS, String fruitore, 
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		LockRiscossioneDTO lockRiscossione = new LockRiscossioneDTO();
		 try {
			Integer  idRiscossione = ValidationFilter.validateParameter("idRiscossione", idRiscossioneS, 0,Integer.MAX_VALUE);
			 lockRiscossione = lockRiscossioneApiServiceHelper.deleteLockRiscossione(idRiscossione,fruitore,  httpHeaders,
					 httpRequest);				
			} catch (ParameterValidationException e) {
	        	return handleException(e, className, methodName);   

           }  catch (GenericException e) {
	        	return handleException(e, className, methodName);   
	        } catch (ProcessingException e) {
	        	return handleException(e, className, methodName);   
	        } finally {
	        	LOGGER.debug(getClassFunctionEndInfo(className, methodName));
	        }
		return Response.ok(lockRiscossione).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

}

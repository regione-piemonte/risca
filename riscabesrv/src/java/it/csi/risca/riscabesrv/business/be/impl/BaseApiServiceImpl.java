/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.business.be.impl;

import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import it.csi.iride2.policy.entity.Identita;
import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.identitadigitale.IdentitaDigitaleManager;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type Base api service.
 *
 * @author CSI PIEMONTE
 */
public abstract class BaseApiServiceImpl {
	protected static Logger LOGGER = Logger.getLogger(Constants.LOGGER_NAME);

	protected Response handleBusinessException(int httpStatus, BusinessException be) {
        return buildErrorRepsonse(httpStatus, be.getMessageCode(), be.getMessage(), be.getDetail());
	}
	
	protected Response buildErrorRepsonse(int httpStatus, String errcode, String message, Map<String, String> details) {
        LOGGER.debug("[" + getClass().getSimpleName() + "::" + Thread.currentThread().getStackTrace()[2].getMethodName() + "] END");

        ErrorDTO error = new ErrorDTO(""+httpStatus, errcode, message, details, null);
        return Response.serverError().entity(error).status(httpStatus).build();
	}
	
	public static <T> void setGestAttoreInsUpd(T elemento, String fruitore,HttpServletRequest httpRequest, HttpHeaders httpHeaders) throws Exception {
        if (fruitore == null) {
            try {
				Identita identita = IdentitaDigitaleManager.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders), null);
                Class<?> objectClass = elemento.getClass();
                String GestAttoreInsProperty = "GestAttoreIns";
                // Get the getter method dynamically based on the property name
                Method getGestAttoreInsMethod = objectClass.getMethod("get" + GestAttoreInsProperty);
                Object gestAttoreInsValue = getGestAttoreInsMethod.invoke(elemento) != null ? getGestAttoreInsMethod.invoke(elemento)  : httpHeaders.getHeaderString(Constants.ATTORE_RISCA);
                
                String GestAttoreUpdProperty = "GestAttoreUpd";
                // Get the getter method dynamically based on the property name
                Method getGestAttoreUpdMethod = objectClass.getMethod("get" + GestAttoreUpdProperty);
                Object gestAttoreUpdValue = getGestAttoreUpdMethod.invoke(elemento)  != null ? getGestAttoreUpdMethod.invoke(elemento) : httpHeaders.getHeaderString(Constants.ATTORE_RISCA); ;
                
                Method setGestAttoreInsMethod = objectClass.getMethod("setGestAttoreIns", String.class);
                Method setGestAttoreUpdMethod = objectClass.getMethod("setGestAttoreUpd", String.class);
                setGestAttoreInsMethod.invoke(elemento, identita != null ? identita.getCodFiscale():   (String) gestAttoreInsValue);
                setGestAttoreUpdMethod.invoke(elemento,identita != null ? identita.getCodFiscale() :  (String) gestAttoreUpdValue);
            } catch (Exception e) {
            	LOGGER.error(e);
            	throw e;
            }
        }else {
            try {
				Class<?> objectClass = elemento.getClass();
				Method setGestAttoreInsMethod = objectClass.getMethod("setGestAttoreIns", String.class);
				Method setGestAttoreUpdMethod = objectClass.getMethod("setGestAttoreUpd", String.class);
				if(fruitore.equals(Constants.BATCH_PORTING)) {
					setGestAttoreInsMethod.invoke(elemento, Constants.MIGRAZIONE);
					setGestAttoreUpdMethod.invoke(elemento, Constants.MIGRAZIONE);
				}else if(fruitore.equals(Constants.BATCH_STATO_CONT)) {
					setGestAttoreInsMethod.invoke(elemento, Constants.BATCH_STATO_CONT);
					setGestAttoreUpdMethod.invoke(elemento, Constants.BATCH_STATO_CONT);
				}else {
					setGestAttoreInsMethod.invoke(elemento, Constants.BATCH);
					setGestAttoreUpdMethod.invoke(elemento, Constants.BATCH);
				}
				

            } catch (Exception e) {
            	LOGGER.error(e);
               	throw e;
            }
        }
    }
    /**
     * Gets class function begin info.
     *
     * @param className  the class name
     * @param methodName the method name
     * @return the class function begin info
     */
    protected String getClassFunctionBeginInfo(String className, String methodName) {
        return getClassFunctionDebugString(className, methodName, "BEGIN");
    }

    /**
     * Gets class function end info.
     *
     * @param className  the class name
     * @param methodName the method name
     * @return the class function end info
     */
    protected String getClassFunctionEndInfo(String className, String methodName) {
        return getClassFunctionDebugString(className, methodName, "END");
    }

    /**
     * Gets class function error info.
     *
     * @param className  the class name
     * @param methodName the method name
     * @param error      the error
     * @return the class function error info
     */
    protected String getClassFunctionErrorInfo(String className, String methodName, Object error) {
        return getClassFunctionDebugString(className, methodName, "ERROR : " + error);
    }

    /**
     * Gets class function debug string.
     *
     * @param className  the class name
     * @param methodName the method name
     * @param info       the info
     * @return the class function debug string
     */
    protected String getClassFunctionDebugString(String className, String methodName, String info) {
        String functionIdentity = "[" + className + "::" + methodName + "] ";
        return functionIdentity + info;
    }
	
}
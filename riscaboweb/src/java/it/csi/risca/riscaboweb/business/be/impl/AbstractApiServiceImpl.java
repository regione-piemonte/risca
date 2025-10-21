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

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;

import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.exception.GenericExceptionList;
import it.csi.risca.riscaboweb.business.be.helper.AbstractServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ErrorDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ErrorObjectDTO;
import it.csi.risca.riscaboweb.dto.UserInfo;
import it.csi.risca.riscaboweb.filter.IrideIdAdapterFilter;
import it.csi.risca.riscaboweb.util.Constants;

public abstract class AbstractApiServiceImpl {
    protected static Logger LOGGER = Logger.getLogger(Constants.LOGGER_NAME + ".business");
    protected static final String HEADER_KEY_ATTORE_RISCA = "Attore-Risca";
    protected static final String HEADER_VALUE_FO = "FO";


    protected String getClassFunctionBeginInfo(String className, String methodName) {
        return getClassFunctionDebugString(className, methodName, "BEGIN");
    }

    protected String getClassFunctionEndInfo(String className, String methodName) {
        return getClassFunctionDebugString(className, methodName, "END");
    }

    protected String getClassFunctionErrorInfo(String className, String methodName, Object error) {
        return getClassFunctionDebugString(className, methodName, "ERROR : " + error);
    }

    protected String getClassFunctionDebugString(String className, String methodName, String info) {
        String functionIdentity = "[" + className + "::" + methodName + "] ";
        return functionIdentity + info;
    }

    protected UserInfo getSessionUser(HttpServletRequest httpRequest) {
        return (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
    }

    protected MultivaluedMap<String, Object> getMultivaluedMapFromHttpHeaders(HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
        if (httpHeaders == null) {
            return null;
        } else {
            MultivaluedMap<String, String> requestHeaders = httpHeaders.getRequestHeaders();
            MultivaluedMap<String, Object> map = new MultivaluedHashMap<>();
            setFOHeader(requestHeaders, httpRequest);
       	   it.csi.risca.riscaboweb.dto.UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);

            requestHeaders.forEach((name, values) -> {

                if (userInfo != null && HEADER_KEY_ATTORE_RISCA.equalsIgnoreCase(name)){
                     map.put(name, Collections.singletonList(userInfo.getCodFisc()));
                }
                		
                if (!CollectionUtils.isEmpty(values) && IrideIdAdapterFilter.AUTH_ID_MARKER.equalsIgnoreCase(name)) {
                      map.put(name, (values.size() != 1) ? Collections.singletonList(values) : Collections.singletonList(values.get(0)));
                    
                }
            });
            return map;
        }
    }

    protected MultivaluedMap<String, Object> getMultivaluedMapFromHttpHeaders(HttpHeaders httpHeaders) {
        if (httpHeaders == null) {
            return null;
        } else {
            MultivaluedMap<String, String> requestHeaders = httpHeaders.getRequestHeaders();
            MultivaluedMap<String, Object> map = new MultivaluedHashMap<>();
            /*
            for (Map.Entry<String, List<String>> entry : requestHeaders.entrySet()) {
                if (entry.getValue() != null) {
                    map.addAll(entry.getKey(), new LinkedList<Object>(entry.getValue()));
                }
            }
            */
            requestHeaders.forEach((name, values) -> {
                if (    //!"Accept".equals(name) &&
                    //!"Accept-Encoding".equals(name) &&
                    //!"Cache-Control".equals(name) &&
                    //!"Connection".equals(name) &&
                        !"Content-Type".equals(name) &&
                                !"Content-Length".equals(name)
                    //!"Cookie".equals(name) &&
                    //!"Host".equals(name) &&
                    //!"Postman-Token".equals(name) &&
                    //!"Shib-Iride-IdentitaDigitale".equals(name) &&
                    //!"User-Agent".equals(name)
                ) {
                    if (!CollectionUtils.isEmpty(values)) {
                        map.put(name, (values.size() != 1) ? Collections.singletonList(values) : Collections.singletonList(values.get(0)));
                    }
                }
            });
            return map;
        }
    }

    protected void setFOHeader(MultivaluedMap<String, String> headers, HttpServletRequest httpRequest) {
        if (!headers.containsKey(HEADER_KEY_ATTORE_RISCA)) {
            UserInfo userInfo = getSessionUser(httpRequest);
            headers.add(HEADER_KEY_ATTORE_RISCA, HEADER_VALUE_FO + (userInfo != null ? "/" + userInfo.getCodFisc() : ""));
        }
    }

    protected Response getResponseWithSharedHeaders(Object obj, AbstractServiceHelper serviceHelper) {
        return getResponseWithSharedHeaders(obj, serviceHelper, null);
    }

    protected Response getResponseWithSharedHeaders(Object obj, AbstractServiceHelper serviceHelper, Integer status) {
        String headerAttoreGestione = serviceHelper.getHeaderAttoreGestione();
        if (status!=null && status==204){
            return Response.noContent().header(HttpHeaders.CONTENT_ENCODING, "identity").header(Constants.HEADER_ATTORE_GESTIONE, headerAttoreGestione).build();
        }
        return Response.ok(obj).header(HttpHeaders.CONTENT_ENCODING, "identity").header(Constants.HEADER_ATTORE_GESTIONE, headerAttoreGestione).status(status == null ? 200 : status).build();
    }

    protected Response handleException(Exception e, String CLASSNAME, String methodName) {
        LOGGER.error(getClassFunctionErrorInfo(CLASSNAME, methodName, e), e);
        Object err;
        if (e instanceof GenericException) {
            ErrorDTO errorDTO = ((GenericException) e).getError();
            Object erroObjectDTO = ((GenericException) e).getErroObjectDTO();

            err = (errorDTO != null) ? errorDTO : erroObjectDTO;
        } else if (e instanceof GenericExceptionList) {
            List<ErrorDTO> errList = ((GenericExceptionList) e).getErrors();
            return Response.serverError().entity(errList).status(500).build();
        } else {
            err = new ErrorDTO("500", "E005", e.getMessage(), null, null);
        }
        if (err instanceof ErrorDTO) {
            return Response.serverError().entity(err).status(Integer.parseInt(((ErrorDTO) err).getStatus())).build();
        } else if (err instanceof ErrorObjectDTO) {
            return Response.serverError().entity(err).status(Integer.parseInt(((ErrorObjectDTO) err).getStatus())).build();
        } else {
            return Response.serverError().entity(err).status(500).build();
        }
    }


}
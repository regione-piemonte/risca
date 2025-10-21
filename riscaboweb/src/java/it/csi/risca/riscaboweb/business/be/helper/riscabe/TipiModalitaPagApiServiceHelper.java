/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscaboweb.business.be.helper.riscabe;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.AbstractServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.TipoModalitaPagDTO;
import it.csi.risca.riscaboweb.util.Utils;

public class TipiModalitaPagApiServiceHelper   extends AbstractServiceHelper {
	
	private final String className = this.getClass().getSimpleName();

	private static final String TIPI_MODALITA_PAGAMENTI = "/tipi-modalita-pagamenti"; 
	
    public TipiModalitaPagApiServiceHelper(String hostname, String endpointBase) {
        this.hostname = hostname;
        this.endpointBase = hostname + endpointBase;
    }
	public List<TipoModalitaPagDTO> loadAllTipiModalitaPagamenti(HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) throws GenericException {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
        String targetUrl = Utils.buildTargetUrl(this.endpointBase+TIPI_MODALITA_PAGAMENTI);
        List<TipoModalitaPagDTO>  result = new ArrayList<>();
        try {
            MultivaluedMap<String, Object> map = getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
            Response resp = getInvocationBuilder(targetUrl, map).get();
            handleResponseErrors(resp);
            if  (resp.getStatus() == 200) {
                GenericType<List<TipoModalitaPagDTO>> dtoType = new GenericType<>() {};
                result = resp.readEntity(dtoType);
            }

        } catch (ProcessingException e) {
            LOGGER.error(getClassFunctionErrorInfo(className, methodName,e) ,e);
            throw e;
        } finally {
            LOGGER.debug(getClassFunctionEndInfo(className, methodName));
        }
        return result;
	}
}

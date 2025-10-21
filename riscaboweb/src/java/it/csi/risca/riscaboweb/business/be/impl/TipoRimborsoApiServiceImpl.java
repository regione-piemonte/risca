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

import it.csi.risca.riscaboweb.business.be.TipoRimborsoApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.TipoRimborsoApiServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.TipoRimborsoDTO;
/**
 * The type Tipo Rimborso api service.
 *
 * @author CSI PIEMONTE
 */
@Component
public class TipoRimborsoApiServiceImpl extends AbstractApiServiceImpl implements TipoRimborsoApi {

	private static final String IDENTITY = "identity";
	 private final String className = this.getClass().getSimpleName();

    @Autowired
    private TipoRimborsoApiServiceHelper tipoRimborsoApiServiceHelper;

	@Override
	public Response loadTipiRimborso(
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
        List<TipoRimborsoDTO> tipiRimborso = new ArrayList<>();
		try {
			tipiRimborso = tipoRimborsoApiServiceHelper.loadTipiRimborso(httpHeaders,httpRequest);
        } catch (GenericException e) {
        	return handleException(e, className, methodName);   
        } catch (ProcessingException e) {
        	return handleException(e, className, methodName);   
        } finally {
        	LOGGER.debug(getClassFunctionEndInfo(className, methodName));
        }
        return Response.ok(tipiRimborso).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

}

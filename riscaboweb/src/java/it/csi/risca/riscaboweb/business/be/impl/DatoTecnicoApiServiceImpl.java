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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.risca.riscaboweb.business.be.DatoTecnicoApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.DatoTecnicoApiServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ambiente.RiscossioneDatoTecnicoDTO;

/**
 * The type riscossione dato tecnico api service.
 *
 * @author CSI PIEMONTE
 */
@Component
public class DatoTecnicoApiServiceImpl extends AbstractApiServiceImpl  implements DatoTecnicoApi {

	private static final String IDENTITY = "identity";
	 private final String className = this.getClass().getSimpleName();

    @Autowired
    private DatoTecnicoApiServiceHelper datoTecnicoApiServiceHelper;

	
	@Override
	public Response saveDatoTecnico(RiscossioneDatoTecnicoDTO datoTecnico, String fruitore,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {

        //datoTecnicoDAO.saveDatoTecnico(datoTecnico, codFisc);
       
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));

		RiscossioneDatoTecnicoDTO dto = new RiscossioneDatoTecnicoDTO();
        try {
			dto = datoTecnicoApiServiceHelper.saveDatoTecnico(datoTecnico,fruitore , httpHeaders, httpRequest);
			
		} catch (GenericException e) {
			return handleException(e, className, methodName);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
        

        return Response.ok(dto).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}



}

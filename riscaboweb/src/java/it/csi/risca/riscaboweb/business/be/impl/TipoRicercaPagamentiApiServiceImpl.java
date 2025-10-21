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

import it.csi.risca.riscaboweb.business.be.TipoRicercaPagamentiApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.exception.ParameterValidationException;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.TipoRicercaPagamentiApiServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.RicercaPagamentoDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.TipoRicercaPagamentoDTO;
import it.csi.risca.riscaboweb.filter.ValidationFilter;

/**
 * The type TipoRicercaPagamentiApiServiceImpl.
 *
 * @author CSI PIEMONTE
 */
@Component
public class TipoRicercaPagamentiApiServiceImpl extends AbstractApiServiceImpl implements TipoRicercaPagamentiApi {

	private final String className = this.getClass().getSimpleName();
	
	private static final String IDENTITY = "identity";
	
    @Autowired
    private TipoRicercaPagamentiApiServiceHelper tipoRicercaPagamentiApiServiceHelper;

	@Override
	public Response loadAllTipiRicercaPagamenti(HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
        List<TipoRicercaPagamentoDTO> listTipiRicercaPagamento = new ArrayList<>();
		try {
			listTipiRicercaPagamento = tipoRicercaPagamentiApiServiceHelper.loadAllTipiRicercaPagamenti(httpHeaders,httpRequest);
        } catch (GenericException e) {
            return handleException(e, className, methodName);
        } catch (ProcessingException e) {
            return handleException(e, className, methodName);
        } finally {
            LOGGER.debug(getClassFunctionEndInfo(className, methodName));
        }
        return Response.ok(listTipiRicercaPagamento).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response ricercaPagamenti(RicercaPagamentoDTO ricercaPagamentoDTO, String fruitore, String offsetS,
			String limitS, String sort, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		try {

			Integer  offset = ValidationFilter.validateParameter("offset", offsetS, 0, Integer.MAX_VALUE);
			Integer  limit = ValidationFilter.validateParameter("limit", limitS, 0, Integer.MAX_VALUE);
			return tipoRicercaPagamentiApiServiceHelper.ricercaPagamenti(ricercaPagamentoDTO, fruitore, offset,
				 limit, sort, httpHeaders, httpRequest);
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

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
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.AbstractServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.CreaUsoRegolaDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.TipoUsoRegolaExtendedDTO;
import it.csi.risca.riscaboweb.util.Constants;
import it.csi.risca.riscaboweb.util.Utils;

public class TipoUsoRegolaApiServiceHelper extends AbstractServiceHelper {

	private static final String IDENTITY = "identity";
	private final String className = this.getClass().getSimpleName();

	private static final String TIPI_USO_REGOLA = "/tipi-usi-regole";

	private static final String ANNI = "/anni/";
	private static final String LIST_REGOLA = "/lista-regole";

	public TipoUsoRegolaApiServiceHelper(String hostname, String endpointBase) {
		this.hostname = hostname;
		this.endpointBase = hostname + endpointBase;
	}

	public List<Integer> loadAllAnniFromDTInizio(Integer idAmbito, String fruitore, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) throws GenericException {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		String targetUrl = Utils.buildTargetUrl(this.endpointBase + TIPI_USO_REGOLA + ANNI + idAmbito,
				Constants.FRUITORE, fruitore);
		List<Integer> result = new ArrayList<>();
		try {
			MultivaluedMap<String, Object> map = getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
			Response resp = getInvocationBuilder(targetUrl, map).get();
			handleResponseErrors(resp);
			if (resp.getStatus() == 200) {
				GenericType<List<Integer>> dtoType = new GenericType<>() {};
				result = resp.readEntity(dtoType);
			}
		} catch (ProcessingException e) {
			LOGGER.error(getClassFunctionErrorInfo(className, methodName, e), e);
			throw e;
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return result;
	}

	public Response loadAllUsoRegolaByIdAmbitoAndAnno(Integer idAmbito, Integer anno, Integer offset, Integer limit,
			String sort, String fruitore, HttpHeaders httpHeaders, HttpServletRequest httpRequest)
			throws GenericException {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		String targetUrl = Utils.buildTargetUrl(
				this.endpointBase + TIPI_USO_REGOLA + LIST_REGOLA+ "/" + idAmbito + "/" + anno, Constants.FRUITORE, fruitore,
				"offset", offset, "limit", limit, "sort", sort);
		try {
			MultivaluedMap<String, Object> map = getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
			Response resp = getInvocationBuilder(targetUrl, map).get();
			handleResponseErrors(resp);
			return resp;
		} catch (ProcessingException e) {
			LOGGER.error(getClassFunctionErrorInfo(className, methodName, e), e);
			throw e;
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
	}

	public TipoUsoRegolaExtendedDTO updateTipoUsoRegola(TipoUsoRegolaExtendedDTO tipoUsoRegola, String fruitore,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		String targetUrl = Utils.buildTargetUrl(this.endpointBase + TIPI_USO_REGOLA, Constants.FRUITORE, fruitore);
		try {
			MultivaluedMap<String, Object> map = getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
			Entity<TipoUsoRegolaExtendedDTO> entity = Entity.json(tipoUsoRegola);
			try(Response resp = getInvocationBuilder(targetUrl, map).put(entity)){
				handleResponseErrors(resp);
				if (resp.getStatus() == 200) {
					GenericType<TipoUsoRegolaExtendedDTO> dtoType = new GenericType<>() {};
					tipoUsoRegola = resp.readEntity(dtoType);
				}
			};
			
		} catch (ProcessingException e) {
			LOGGER.error(getClassFunctionErrorInfo(className, methodName, e), e);
			throw e;
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return tipoUsoRegola;
	}

	public Response saveTipoUsoRegola(String fruitore,CreaUsoRegolaDTO usoRegola,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		String targetUrl = Utils.buildTargetUrl(this.endpointBase + TIPI_USO_REGOLA, Constants.FRUITORE, fruitore);
		List<TipoUsoRegolaExtendedDTO> listTipoUsoRegola = new ArrayList<>();
		try {
			MultivaluedMap<String, Object> map = getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
			Entity<CreaUsoRegolaDTO> entity = Entity.json(usoRegola);
			try(Response resp = getInvocationBuilder(targetUrl, map).post(entity)){
				handleResponseErrors(resp);
				if (resp.getStatus() == 200) {
					GenericType<List<TipoUsoRegolaExtendedDTO>> dtoType = new GenericType<>() {};
					listTipoUsoRegola = resp.readEntity(dtoType);
				}
			};
			return Response.ok(listTipoUsoRegola).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		} catch (ProcessingException e) {
			LOGGER.error(getClassFunctionErrorInfo(className, methodName, e), e);
			throw e;
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
	}

	public List<TipoUsoRegolaExtendedDTO> updateAllTipoUsoRegola(List<TipoUsoRegolaExtendedDTO> tipoUsoRegola,
			String fruitore, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		String targetUrl = Utils.buildTargetUrl(this.endpointBase + TIPI_USO_REGOLA + LIST_REGOLA, Constants.FRUITORE, fruitore);
		try {
			MultivaluedMap<String, Object> map = getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
			Entity<List<TipoUsoRegolaExtendedDTO>> entity = Entity.json(tipoUsoRegola);
			try(Response resp = getInvocationBuilder(targetUrl, map).put(entity)){
				handleResponseErrors(resp);
				if (resp.getStatus() == 200) {
					GenericType<List<TipoUsoRegolaExtendedDTO>> dtoType = new GenericType<>() {};
					tipoUsoRegola = resp.readEntity(dtoType);
				}
			};

		} catch (ProcessingException e) {
			LOGGER.error(getClassFunctionErrorInfo(className, methodName, e), e);
			throw e;
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return tipoUsoRegola;
	}

}

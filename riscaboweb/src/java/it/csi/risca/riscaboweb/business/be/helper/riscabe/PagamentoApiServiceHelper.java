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
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import it.csi.risca.riscaboweb.business.be.PagamentoApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.AbstractServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.AddHeaderFilter;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.PagamentoDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.PagamentoExtendedDTO;
import it.csi.risca.riscaboweb.filter.IrideIdAdapterFilter;
import it.csi.risca.riscaboweb.util.Constants;
import it.csi.risca.riscaboweb.util.Utils;

public class PagamentoApiServiceHelper extends AbstractServiceHelper {

	  private static final String PAGAMENTI = "pagamenti";
	  
	public PagamentoApiServiceHelper(String hostname, String endpointBase) {
		this.hostname = hostname;
		this.endpointBase = hostname + endpointBase;
	}

	public PagamentoDTO getPagamentoWithMaxDataOpVal(String fruitore, Integer idStatoDebitorio, 
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
		LOGGER.debug("[PagamentoApiServiceHelper::getPagamentoWithMaxDataOpVal] BEGIN");
		PagamentoDTO result = new PagamentoDTO();
        String targetUrl = Utils.buildTargetUrl(this.endpointBase+"/"+PAGAMENTI , Constants.FRUITORE,fruitore,"idStatoDebitorio", idStatoDebitorio);
		try {

	           MultivaluedMap<String, Object> map = getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
	            Response resp = getInvocationBuilder(targetUrl, map).get();
	         handleResponseErrors(resp);
			GenericType<PagamentoDTO> pagamentoType = new GenericType<PagamentoDTO>() {
			};
			result = resp.readEntity(pagamentoType);
		} catch (ProcessingException e) {
			LOGGER.error("[PagamentoApiServiceHelper::getPagamentoWithMaxDataOpVal] EXCEPTION : " + e);
			throw new ProcessingException(e);
		} finally {
			LOGGER.debug("[PagamentoApiServiceHelper::getPagamentoWithMaxDataOpVal] END");
		}
		return result;
	}

	public List<PagamentoExtendedDTO> getPagamentoByIdRiscossione(String fruitore, Integer idRiscossione, 
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
		LOGGER.debug("[PagamentoApiServiceHelper::getPagamentoByIdRiscossione] BEGIN");
		List<PagamentoExtendedDTO> result = new ArrayList<>();
        String targetUrl = Utils.buildTargetUrl(this.endpointBase + "/pagamenti/idRiscossione/" + idRiscossione, Constants.FRUITORE,fruitore );
        try {
            MultivaluedMap<String, Object> map = getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
            Response resp = getInvocationBuilder(targetUrl, map).get();
	        handleResponseErrors(resp);
			GenericType<List<PagamentoExtendedDTO>> pagamentoType = new GenericType<List<PagamentoExtendedDTO>>() {
			};
			result = resp.readEntity(pagamentoType);
		} catch (ProcessingException e) {
			LOGGER.error("[PagamentoApiServiceHelper::getPagamentoByIdRiscossione] EXCEPTION : " + e);
			throw new ProcessingException(e);
		} finally {
			LOGGER.debug("[PagamentoApiServiceHelper::getPagamentoByIdRiscossione] END");
		}
		return result;
	}

	public PagamentoExtendedDTO savePagamento(PagamentoExtendedDTO pagamentoExtendedDTO,String fruitore, 
			 HttpHeaders httpHeaders, HttpServletRequest httpRequest)
			throws GenericException {
		LOGGER.debug("[PagamentoApiServiceHelper::savePagamento] BEGIN");
		PagamentoExtendedDTO result = new PagamentoExtendedDTO();
        String targetUrl = Utils.buildTargetUrl(this.endpointBase+"/"+PAGAMENTI , Constants.FRUITORE,fruitore);

		try {
            MultivaluedMap<String, Object> map = getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
            Entity<PagamentoExtendedDTO> entity = Entity.json(pagamentoExtendedDTO);
            Response resp = getInvocationBuilder(targetUrl, map).post(entity);
	        handleResponseErrors(resp);
			GenericType<PagamentoExtendedDTO> pagamento = new GenericType<PagamentoExtendedDTO>() {
			};
			result = resp.readEntity(pagamento);
		} catch (ProcessingException e) {
			LOGGER.error("[PagamentoApiServiceHelper::savePagamento] EXCEPTION : " + e);
			throw new ProcessingException(e);
		} finally {
			LOGGER.debug("[PagamentoApiServiceHelper::savePagamento] END");
		}
		return result;
	}

	public PagamentoExtendedDTO updatePagamento(PagamentoExtendedDTO pagamentoExtendedDTO,String fruitore, 
			 HttpHeaders httpHeaders, HttpServletRequest httpRequest)
			throws GenericException {
		LOGGER.debug("[PagamentoApiServiceHelper::updatePagamento] BEGIN");
		PagamentoExtendedDTO result = new PagamentoExtendedDTO();
        String targetUrl = Utils.buildTargetUrl(this.endpointBase+"/"+PAGAMENTI , Constants.FRUITORE,fruitore);

		try {
            MultivaluedMap<String, Object> map = getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
            Entity<PagamentoExtendedDTO> entity = Entity.json(pagamentoExtendedDTO);
            Response resp = getInvocationBuilder(targetUrl, map).put(entity);
	        handleResponseErrors(resp);
	          handleResponseErrors(resp);
			GenericType<PagamentoExtendedDTO> pagamento = new GenericType<PagamentoExtendedDTO>() {
			};
			result = resp.readEntity(pagamento);
		} catch (ProcessingException e) {
			LOGGER.error("[PagamentoApiServiceHelper::updatePagamento] EXCEPTION : " + e);
			throw new ProcessingException(e);
		} finally {
			LOGGER.debug("[PagamentoApiServiceHelper::updatePagamento] END");
		}
		return result;
	}

	public String deleteByIdPagamento(Integer idPagamento, String fruitore, 
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
		LOGGER.debug("[PagamentoApiServiceHelper::deleteByIdPagamento] BEGIN");
		Long result = null;
		String targetUrl = this.endpointBase + "/pagamenti/idPagamento/"+idPagamento;
        if(fruitore != null)
        	targetUrl += "?fruitore="+fruitore;
		try {
			MultivaluedMap<String, Object> map = getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
			Client client = ClientBuilder.newClient();
			if (map.get(IrideIdAdapterFilter.AUTH_ID_MARKER) != null)
				client = ClientBuilder.newClient().register(new AddHeaderFilter(IrideIdAdapterFilter.AUTH_ID_MARKER,
						(String) map.get(IrideIdAdapterFilter.AUTH_ID_MARKER).get(0)));

			WebTarget target = client.target(this.endpointBase);
			ResteasyWebTarget rtarget = (ResteasyWebTarget) target;
			PagamentoApi pagamentoApi = rtarget.proxy(PagamentoApi.class);
			Response resp = pagamentoApi.deleteByIdPagamento(idPagamento.toString() ,fruitore,   httpHeaders,
					httpRequest);
			
	          handleResponseErrors(resp);
			GenericType<Long> idPagamentoGe = new GenericType<Long>() {
			};
			result = resp.readEntity(idPagamentoGe);
		} catch (ProcessingException e) {
			LOGGER.error("[PagamentoApiServiceHelper::deleteByIdPagamento] EXCEPTION : " + e);
			throw new ProcessingException(e);
		} finally {
			LOGGER.debug("[PagamentoApiServiceHelper::deleteByIdPagamento] END");
		}
		return idPagamento.toString();
	}
	public List<PagamentoExtendedDTO>  getPagamentiByIdStatoDebitorio(String fruitore, Integer idStatoDebitorio, 
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
		LOGGER.debug("[PagamentoApiServiceHelper::getPagamentiByIdStatoDebitorio] BEGIN");
		List<PagamentoExtendedDTO> result = new ArrayList<>();
		String baseUrl = this.endpointBase + "/pagamenti/idStatoDebitorio/" + idStatoDebitorio;
        String targetUrl = Utils.buildTargetUrl(baseUrl, Constants.FRUITORE,fruitore);
		
		try {
            MultivaluedMap<String, Object> map = getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
            Response resp = getInvocationBuilder(targetUrl, map).get();
	        handleResponseErrors(resp);
			GenericType<List<PagamentoExtendedDTO>> pagamentoType = new GenericType<List<PagamentoExtendedDTO>>() {};
			result = resp.readEntity(pagamentoType);
		} catch (ProcessingException e) {
			LOGGER.error("[PagamentoApiServiceHelper::getPagamentiByIdStatoDebitorio] EXCEPTION : " + e);
			throw new ProcessingException(e);
		} finally {
			LOGGER.debug("[PagamentoApiServiceHelper::getPagamentiByIdStatoDebitorio] END");
		}
		return result;
	}

	
	public PagamentoExtendedDTO getPagamentoByIdPagamento(Integer idPagamento, String fruitore, 
			HttpHeaders httpHeaders, HttpServletRequest httpRequest)
			throws GenericException {
		LOGGER.debug("[PagamentoApiServiceHelper::getPagamentoByIdPagamento] BEGIN");
		PagamentoExtendedDTO result = new PagamentoExtendedDTO();
	    String baseUrl = this.endpointBase + "/pagamenti/idPagamento/"+idPagamento;
	    String targetUrl = Utils.buildTargetUrl(baseUrl, Constants.FRUITORE,fruitore);
		try {
            MultivaluedMap<String, Object> map = getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
            Response resp = getInvocationBuilder(targetUrl, map).get();
	        handleResponseErrors(resp);
			GenericType<PagamentoExtendedDTO> pagamento = new GenericType<PagamentoExtendedDTO>() {
			};
			result = resp.readEntity(pagamento);
		} catch (ProcessingException e) {
			LOGGER.error("[PagamentoApiServiceHelper::getPagamentoByIdPagamento] EXCEPTION : " + e);
			throw new ProcessingException(e);
		} finally {
			LOGGER.debug("[PagamentoApiServiceHelper::getPagamentoByIdPagamento] END");
		}
		return result;
	}

	public Response getPagamentiDaVisionare(String fruitore, Integer offset,
			 Integer limit, String sort, 
			HttpHeaders httpHeaders, HttpServletRequest httpRequest)throws GenericException  {
		LOGGER.debug("[PagamentoApiServiceHelper::getPagamentiDaVisionare] BEGIN");
		Response resp = null;
		String baseUrl =this.endpointBase+"/"+PAGAMENTI +"/pagamenti-da-visionare";
	    String targetUrl = Utils.buildTargetUrl(baseUrl, Constants.FRUITORE,fruitore,"offset", offset,"limit", limit,"sort",sort);
		try {
            MultivaluedMap<String, Object> map = getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
            resp = getInvocationBuilder(targetUrl, map).get();
	        handleResponseErrors(resp);

		} catch (ProcessingException e) {
			LOGGER.error("[PagamentoApiServiceHelper::getPagamentiDaVisionare] EXCEPTION : " + e);
			throw new ProcessingException(e);
		} finally {
			LOGGER.debug("[PagamentoApiServiceHelper::getPagamentiDaVisionare] END");
		}
		return resp;
	}
}

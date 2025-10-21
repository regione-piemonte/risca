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
import java.util.stream.Collectors;

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

import org.apache.commons.lang.StringUtils;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import it.csi.risca.riscaboweb.business.be.ElaboraApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.AbstractServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.AddHeaderFilter;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ElaboraDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ElaboraExtendedDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ErrorDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.RegistroElaboraExtendedDTO;
import it.csi.risca.riscaboweb.filter.IrideIdAdapterFilter;
import it.csi.risca.riscaboweb.util.Constants;
import it.csi.risca.riscaboweb.util.Utils;

public class ElaboraApiServiceHelper extends AbstractServiceHelper {
	
	private final String className = this.getClass().getSimpleName();
	private static final String IDENTITY = "identity";

	public ElaboraApiServiceHelper(String hostname, String endpointBase) {
		this.hostname = hostname;
		this.endpointBase = hostname + endpointBase;
	}

	public Response loadElabora(String fruitore, HttpHeaders httpHeaders, HttpServletRequest httpRequest,
			String idAmbito, List<String> codTipoElabora, List<String> codStatoElabora, String dataRichiestaInizio,
			String dataRichiestaFine, String codFunzionalita, Integer offset, Integer limit, String sort,
			Integer flgVisibile) throws GenericException {
		LOGGER.debug("[ElaboraApiServiceHelper::loadElabora] BEGIN");
		LOGGER.debug("[ElaboraApiServiceHelper::loadElabora] -------------> codFunzionalita = " +codFunzionalita);
		List<ElaboraExtendedDTO> result = new ArrayList<>();
		String targetUrl = this.endpointBase + "/elabora";
		String paginazione = null;
		Response resp = null;
		boolean firstParam = true;
		if (fruitore != null) {
			targetUrl +=  "?fruitore=" + fruitore;
			firstParam = false;
		}
		if (idAmbito != null) {
			targetUrl += firstParam ? "?idAmbito=" + idAmbito : "&idAmbito=" + idAmbito;
			firstParam = false;
		}
		if (codTipoElabora != null && codTipoElabora.size() > 0) {
			for (String cod : codTipoElabora) {
				targetUrl += firstParam ? "?codTipoElabora=" + cod : "&codTipoElabora=" + cod;
				firstParam = false;
			}
		}
		if (codStatoElabora != null && codStatoElabora.size() > 0) {
			for (String cod : codStatoElabora) {
				targetUrl += firstParam ? "?codStatoElabora=" + cod : "&codStatoElabora=" + cod;
				firstParam = false;
			}
		}
		if (dataRichiestaInizio != null) {
			targetUrl += firstParam ? "?dataRichiestaInizio=" + dataRichiestaInizio
					: "&dataRichiestaInizio=" + dataRichiestaInizio;
			firstParam = false;
		}
		if (dataRichiestaFine != null) {
			targetUrl += firstParam ? "?dataRichiestaFine=" + dataRichiestaFine
					: "&dataRichiestaFine=" + dataRichiestaFine;
			firstParam = false;
		}
		if (codFunzionalita != null) {
			targetUrl += firstParam ? "?codFunzionalita=" + codFunzionalita
					: "&codFunzionalita=" + codFunzionalita;
			firstParam = false;
		}
		if (offset != null && limit != null && !sort.contains("dataProtocollo")) {
			targetUrl += firstParam ? "?offset=" + offset + "&limit=" + limit : "&offset=" + offset + "&limit=" + limit;
			firstParam = false;
		}
		if (StringUtils.isNotBlank(sort)) {
			if (sort.contains("tipoElabora")) {
				sort = sort.replace("tipoElabora", "codTipoElabora");
			}
			if (sort.contains("statoElabora")) {
				sort = sort.replace("statoElabora", "codStatoElabora");
			}
			targetUrl += firstParam ? "?sort=" + sort : "&sort=" + sort;
			firstParam = false;
		}

		targetUrl += firstParam ? "?flgVisibile=" + flgVisibile
					: "&flgVisibile=" + flgVisibile;

		
		try {
        	MultivaluedMap<String, Object> map =  getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
        	Client client = null;
        	if(map.get(IrideIdAdapterFilter.AUTH_ID_MARKER) != null) {
        		  client = ClientBuilder.newClient().register(new AddHeaderFilter(IrideIdAdapterFilter.AUTH_ID_MARKER,  (String) map.get(IrideIdAdapterFilter.AUTH_ID_MARKER).get(0)));
           }else {
                  client = ClientBuilder.newClient();
        	}
            WebTarget target = client.target(this.endpointBase);
            ResteasyWebTarget rtarget = (ResteasyWebTarget) target;
            ElaboraApi elaboraApi = rtarget.proxy(ElaboraApi.class);
            LOGGER.debug("[ElaboraApiServiceHelper::loadElabora] --> codFunzionalita = "+codFunzionalita);
			resp = elaboraApi.loadElabora(fruitore, idAmbito, codTipoElabora, codStatoElabora, dataRichiestaInizio,
					dataRichiestaFine, codFunzionalita, offset.toString(), limit.toString(), sort,
					flgVisibile.toString(), httpHeaders, httpRequest);
			if (resp.getStatus() >= 400) {
				ErrorDTO err = getError(resp);
				LOGGER.error("[ElaboraApiServiceHelper::loadElabora]  SERVER EXCEPTION : " + err);
				throw new GenericException(err);
			}
			GenericType<List<ElaboraExtendedDTO>> elaboraListType = new GenericType<List<ElaboraExtendedDTO>>() {
			};
			result = resp.readEntity(elaboraListType);
			LOGGER.debug("[ElaboraApiServiceHelper::loadRegistroElabora] BEGIN");
			List<RegistroElaboraExtendedDTO> resultRegistroElabora = new ArrayList<>();
			for (int i=0; i<result.size(); i++) {
				try {
					Long idElabora = result.get(i).getIdElabora();
					String targetUrlRegistroElabora = this.endpointBase + "/registro-elabora" + "?idElabora=" + idElabora + "&idAmbito=" + idAmbito + "&esito=" + 1;
					
					Response respRegistroElabora = getInvocationBuilder(targetUrlRegistroElabora, getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest)).get();
					if (respRegistroElabora.getStatus() >= 400) {
						ErrorDTO err = getError(respRegistroElabora);
						LOGGER.error("[ElaboraApiServiceHelper::loadRegistroElabora]  SERVER EXCEPTION : " + err);
						throw new GenericException(err);
					}
					GenericType<List<RegistroElaboraExtendedDTO>> registroElaboraListType = new GenericType<List<RegistroElaboraExtendedDTO>>() {
					};
					resultRegistroElabora = respRegistroElabora.readEntity(registroElaboraListType);
					if(resultRegistroElabora != null && resultRegistroElabora.size() > 0) {
						result.get(i).setRegistroElabora(resultRegistroElabora.get(0));
					}
				} catch (ProcessingException e) {
					LOGGER.error("[ElaboraApiServiceHelper::loadRegistroElabora] EXCEPTION : " + e);
					throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
				}
			}
			LOGGER.debug("[ElaboraApiServiceHelper::loadRegistroElabora] END");
		} catch (ProcessingException e) {
			LOGGER.error("[ElaboraApiServiceHelper::loadElabora] EXCEPTION : " + e);
			throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
		} finally {
			LOGGER.debug("[ElaboraApiServiceHelper::loadElabora] END");
		}
		paginazione = resp.getHeaderString("PaginationInfo");
		
		
        //parte per sort by data Protocollo
        if (StringUtils.isNotBlank(sort)) {
 		if (sort.contains("dataProtocollo")) {
			if (sort.substring(1).equals("dataProtocollo") && sort.charAt(0) == '-')
				result = result.stream().sorted((o1, o2) -> {
					for (int i = 0; i < Math.min(o1.getParametri().size(), o2.getParametri().size()); i++) {
						if (o2.getParametri().get(i).getChiave().equals("DT_PROTOCOLLO")
								&& o1.getParametri().get(i).getChiave().equals("DT_PROTOCOLLO")) {
							int c = o2.getParametri().get(i).getValore()
									.compareTo(o1.getParametri().get(i).getValore());
							if (c != 0) {
								return c;
							}
						}
					}
					return Integer.compare(o1.getParametri().size(), o2.getParametri().size());

				}).collect(Collectors.toList());
			else
				result = result.stream().sorted((o1, o2) -> {
					for (int i = 0; i < Math.min(o1.getParametri().size(), o2.getParametri().size()); i++) {
						if (o2.getParametri().get(i).getChiave().equals("DT_PROTOCOLLO")
								&& o1.getParametri().get(i).getChiave().equals("DT_PROTOCOLLO")) {
							int c = o1.getParametri().get(i).getValore()
									.compareTo(o2.getParametri().get(i).getValore());
							if (c != 0) {
								return c;
							}
						}
					}
					return Integer.compare(o1.getParametri().size(), o2.getParametri().size());
				}).collect(Collectors.toList());

		}
        if(offset != null && limit != null && sort.contains("dataProtocollo") ) {
        	offset = (offset - 1) * 10;
        	result = result.stream().skip(offset).limit(limit).collect(Collectors.toList());
        }
        }
        
		return Response.ok(result).header("PaginationInfo", paginazione).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();

	}

	public Response loadElaboraById(Integer idElabora, Boolean download,  HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) throws GenericException {
		LOGGER.debug("[ElaboraApiServiceHelper::loadElaboraById] BEGIN");
		ElaboraDTO result = new ElaboraDTO();
		String targetUrl = this.endpointBase + "/elabora/" + idElabora;
		Response resp = null;
		if (download != null && download == true) {
			targetUrl += "?download=" + download;
		}

		try {
        	MultivaluedMap<String, Object> map =  getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
        	Client client = null;
        	if(map.get(IrideIdAdapterFilter.AUTH_ID_MARKER) != null) {
        		  client = ClientBuilder.newClient().register(new AddHeaderFilter(IrideIdAdapterFilter.AUTH_ID_MARKER,  (String) map.get(IrideIdAdapterFilter.AUTH_ID_MARKER).get(0)));
           }else {
                  client = ClientBuilder.newClient();
        	}
            WebTarget target = client.target(this.endpointBase);
            ResteasyWebTarget rtarget = (ResteasyWebTarget) target;
            ElaboraApi elaboraApi = rtarget.proxy(ElaboraApi.class);
             resp = elaboraApi.loadElaboraById(idElabora.toString(), download,  httpHeaders, httpRequest);
			if (resp.getStatus() >= 400) {
				ErrorDTO err = getError(resp);
				LOGGER.error("[ElaboraApiServiceHelper::loadElaboraById]  SERVER EXCEPTION : " + err);
				throw new GenericException(err);
			}
			GenericType<ElaboraDTO> elaboraType = new GenericType<ElaboraDTO>() {
			};
			result = resp.readEntity(elaboraType);
		} catch (ProcessingException e) {
			LOGGER.error("[ElaboraApiServiceHelper::loadElaboraById] EXCEPTION : " + e);
			throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
		} finally {
			LOGGER.debug("[ElaboraApiServiceHelper::loadElaboraById] END");
		}
      
		return Response.ok(result).build();

	}
	
	
	public ElaboraDTO saveElabora(ElaboraDTO elabora, String fruitore,  HttpHeaders httpHeaders,
			HttpServletRequest httpRequest)
			throws GenericException {
		LOGGER.debug("[ElaboraApiServiceHelper::saveElabora] BEGIN");
		ElaboraDTO result = new ElaboraDTO();
        String targetUrl = null ;
        if(fruitore != null)
              targetUrl = this.endpointBase + "/elabora?fruitore="+fruitore;
        
        targetUrl = this.endpointBase + "/elabora";
		try {
			MultivaluedMap<String, Object> map = getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
            Entity<ElaboraDTO> entity = Entity.json(elabora);
            Response resp = getInvocationBuilder(targetUrl, map).post(entity);
			LOGGER.debug("[ElaboraApiServiceHelper::saveElabora] RESPONSE STATUS : " + resp.getStatus());
			if (resp.getStatus() >= 400) {
				ErrorDTO err = getError(resp);
				LOGGER.debug("[ElaboraApiServiceHelper::saveElabora] SERVER EXCEPTION : " + err);
				throw new GenericException(err);
			}
			GenericType<ElaboraDTO> dtoType = new GenericType<ElaboraDTO>() {
			};
			result = resp.readEntity(dtoType);
		} catch (ProcessingException e) {
			LOGGER.debug("[ElaboraApiServiceHelper::saveElabora] EXCEPTION : " + e);
			throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
		} finally {
			LOGGER.debug("[ElaboraApiServiceHelper::saveElabora] END");
		}
		return result;
	}

	public ElaboraDTO updateElabora(ElaboraExtendedDTO elabora, String  fruitore,  HttpHeaders httpHeaders,
			HttpServletRequest httpRequest)
			throws GenericException {
		LOGGER.debug("[ElaboraApiServiceHelper::updateElabora] BEGIN");
		ElaboraExtendedDTO result = new ElaboraExtendedDTO();
        String targetUrl = null ;
        if(fruitore != null)
              targetUrl = this.endpointBase + "/elabora?fruitore="+fruitore;
        
        targetUrl = this.endpointBase + "/elabora";
		try {
			MultivaluedMap<String, Object> map = getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
            Entity<ElaboraDTO> entity = Entity.json(elabora);
            Response resp = getInvocationBuilder(targetUrl, map).put(entity);
			LOGGER.debug("[ElaboraApiServiceHelper::updateElabora] RESPONSE STATUS : " + resp.getStatus());
			if (resp.getStatus() >= 400) {
				ErrorDTO err = getError(resp);
				LOGGER.debug("[ElaboraApiServiceHelper::updateElabora] SERVER EXCEPTION : " + err);
				throw new GenericException(err);
			}
			GenericType<ElaboraExtendedDTO> dtoType = new GenericType<ElaboraExtendedDTO>() {
			};
			result = resp.readEntity(dtoType);
		} catch (ProcessingException e) {
			LOGGER.debug("[ElaboraApiServiceHelper::updateElabora] EXCEPTION : " + e);
			throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
		} finally {
			LOGGER.debug("[ElaboraApiServiceHelper::updateElabora] END");
		}
		return result;
	}
	
	public List<ElaboraDTO> verifyElabora(String idAmbito, String verifica,  HttpHeaders httpHeaders,
			HttpServletRequest httpRequest)
			throws GenericException {
		LOGGER.debug("[ElaboraApiServiceHelper::verifyElabora] BEGIN");
		List<ElaboraDTO> result = new ArrayList<>();
		String targetUrl = this.endpointBase + "/_verifyElabora" + "?idAmbito=" + idAmbito  + "&verifica=" + verifica;

		try {
        	MultivaluedMap<String, Object> map =  getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
        	Client client = null;
        	if(map.get(IrideIdAdapterFilter.AUTH_ID_MARKER) != null) {
        		  client = ClientBuilder.newClient().register(new AddHeaderFilter(IrideIdAdapterFilter.AUTH_ID_MARKER,  (String) map.get(IrideIdAdapterFilter.AUTH_ID_MARKER).get(0)));
           }else {
                  client = ClientBuilder.newClient();
        	}
            WebTarget target = client.target(this.endpointBase);
            ResteasyWebTarget rtarget = (ResteasyWebTarget) target;
            ElaboraApi elaboraApi = rtarget.proxy(ElaboraApi.class);
            Response resp = elaboraApi.verifyElabora(idAmbito, verifica,  httpHeaders,
					 httpRequest);
			LOGGER.debug("[ElaboraApiServiceHelper::verifyElabora] RESPONSE STATUS : " + resp.getStatus());
			if (resp.getStatus() >= 400) {
				ErrorDTO err = getError(resp);
				LOGGER.debug("[ElaboraApiServiceHelper::verifyElabora] SERVER EXCEPTION : " + err);
				throw new GenericException(err);
			}
			GenericType<List<ElaboraDTO>> dtoType = new GenericType<List<ElaboraDTO>>() {
			};
			result = resp.readEntity(dtoType);
		} catch (ProcessingException e) {
			LOGGER.debug("[ElaboraApiServiceHelper::verifyElabora] EXCEPTION : " + e);
			throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
		} finally {
			LOGGER.debug("[ElaboraApiServiceHelper::verifyElabora] END");
		}
		return result;
	}

	public List<ElaboraDTO> loadElaboraByCF(String codiceFiscale, String fruitore, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) throws GenericException {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		String targetUrl = Utils.buildTargetUrl(this.endpointBase + "/elabora/codiceFiscale/"+codiceFiscale,
				Constants.FRUITORE, fruitore);
		List<ElaboraDTO> result = new ArrayList<>();
		try {
			MultivaluedMap<String, Object> map = getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
			Response resp = getInvocationBuilder(targetUrl, map).get();
			handleResponseErrors(resp);
			if (resp.getStatus() == 200) {
				GenericType<List<ElaboraDTO>> dtoType = new GenericType<>() {};
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
}

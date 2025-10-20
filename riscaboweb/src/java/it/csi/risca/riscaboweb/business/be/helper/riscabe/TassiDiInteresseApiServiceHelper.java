package it.csi.risca.riscaboweb.business.be.helper.riscabe;

import java.util.ArrayList;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.PathParam;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.Entity;


import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import it.csi.risca.riscaboweb.filter.IrideIdAdapterFilter;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.AbstractServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.DettaglioPagDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ErrorDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.TassiDiInteresseDTO;
import it.csi.risca.riscaboweb.business.be.TassiDiInteresseApi;
import it.csi.risca.riscaboweb.util.Constants;
import it.csi.risca.riscaboweb.util.Utils;

import it.csi.risca.riscaboweb.business.be.helper.AddHeaderFilter;

public class TassiDiInteresseApiServiceHelper extends AbstractServiceHelper {
	private static final String TASSI_DI_INTERESSE = "/tassi-interesse";
	private final String className = this.getClass().getSimpleName();
	private static final ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	private static final String IDENTITY = "identity";
	
	public TassiDiInteresseApiServiceHelper(String hostname, String endpointBase) {
		this.hostname = hostname;
		this.endpointBase = hostname + endpointBase;
	}

	public Response loadTassiDiInteresse(
			String idAmbito, 
			String fruitore,
			String tipoDiInteresse,
			Integer offset, 
			Integer limit, 
			String sort, 
			MultivaluedMap<String, Object> requestHeaders) throws GenericException {
		
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.info("className: " + className + " methodName: " + methodName);

		LOGGER.info("idAmbito: " + idAmbito);
		LOGGER.info("fruitore: " + fruitore);
		LOGGER.info("tipoInteresse: " + tipoDiInteresse);

		// Verifica che idAmbito, fruitore e tipoDiInteresse non siano nulli
		if (idAmbito == null || fruitore == null || tipoDiInteresse == null) {
			String errorMessage = "idAmbito, fruitore e tipoDiInteresse non possono essere nulli";
			LOGGER.error(errorMessage);
			throw new GenericException(errorMessage);
		}

		String targetUrl = Utils.buildTargetUrl(this.endpointBase + TASSI_DI_INTERESSE + "?idAmbito=" + idAmbito + "&fruitore=" + fruitore + "&tipoDiInteresse=" + tipoDiInteresse);
		String paginazione = null;
		Response resp = null;
		if (offset != null && limit != null) {
			targetUrl += "&offset=" + offset + "&limit=" + limit;
		}
		if (offset != null) {
			targetUrl += "&sort=" + sort;
		}

		LOGGER.info("target_url: " + targetUrl);

		LOGGER.debug("[TassiDiInteresseApiServiceHelper::loadTassiDiInteresse] BEGIN");
		List<TassiDiInteresseDTO> result = new ArrayList<>();

		try {
			resp = getInvocationBuilder(targetUrl, requestHeaders).get();
			GenericType<List<TassiDiInteresseDTO>> tassiDiInteresseListType = new GenericType<List<TassiDiInteresseDTO>>() {
			};
			LOGGER.info("Response Status: " + resp.getStatus());
			LOGGER.info("Response Headers: " + resp.getHeaders());
			String responseBody = resp.readEntity(String.class);
			LOGGER.info("Response Body: " + responseBody);

			// Verifica se il tipo di contenuto è JSON
			if (resp.getMediaType().toString().contains("application/json")) {
				result = objectMapper.readValue(responseBody, new TypeReference<List<TassiDiInteresseDTO>>() {
				});
			} else {
				LOGGER.error("Content-Type della risposta non è JSON");
				throw new GenericException("Content-Type della risposta non è JSON");
			}
		} catch (ProcessingException e) {
			LOGGER.error("[TassiDiInteresseApiServiceHelper::loadTassiDiInteresse] EXCEPTION : " + e);
			// Lancia un'eccezione personalizzata con un messaggio di errore
			throw new GenericException("Errore nella chiamata al servizio: " + e.getMessage() + ", " + e);
		} catch (Exception e) {
			LOGGER.error("[TassiDiInteresseApiServiceHelper::loadTassiDiInteresse] EXCEPTION : " + e);
			// Lancia un'eccezione personalizzata con un messaggio di errore
			throw new GenericException("Errore nella deserializzazione del JSON: " + e.getMessage() + ", " + e);
		}
		LOGGER.debug("[TassiDiInteresseApiServiceHelper::loadTassiDiInteresse] END");
		
		paginazione = resp.getHeaderString("PaginationInfo");
		return Response.ok(result).header("PaginationInfo", paginazione).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();

		
	}

	public TassiDiInteresseDTO savetassiDiInteresse(TassiDiInteresseDTO tassiDiInteresse, String fruitore,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest)
			throws GenericException {
		LOGGER.debug("[TassiDiInteresseApiServiceHelper::saveTassiDiInteresse] BEGIN");
		TassiDiInteresseDTO result = new TassiDiInteresseDTO();
		String targetUrl = this.endpointBase + "/tassi-interesse";
		if (fruitore != null)
			targetUrl += "?fruitore=" + fruitore;
		try {
			MultivaluedMap<String, Object> map = getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
			Entity<TassiDiInteresseDTO> entity = Entity.json(tassiDiInteresse);
			Response resp = getInvocationBuilder(targetUrl, map).post(entity);
			LOGGER.debug(
					"[TassiDiInteresseApiServiceHelper::saveTassiDiInteresse] RESPONSE STATUS : " + resp.getStatus());
			if (resp.getStatus() >= 400) {
				ErrorDTO err = getError(resp);
				LOGGER.debug("[TassiDiInteresseApiServiceHelper::saveTassiDiInteresse] SERVER EXCEPTION : " + err);
				throw new GenericException(err);
			}
			GenericType<TassiDiInteresseDTO> tassiDiInteresseDTO = new GenericType<TassiDiInteresseDTO>() {
			};

			result = resp.readEntity(tassiDiInteresseDTO);
		} catch (ProcessingException e) {
			LOGGER.error("[TassiDiInteresseApiServiceHelper::saveTassiDiInteresse] EXCEPTION : " + e);
			throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
		} finally {
			LOGGER.debug("[TassiDiInteresseApiServiceHelper::saveTassiDiInteresse] END");
		}
		return result;
	}

	public Response deleteTassiDiInteresse(String fruitore, Integer idAmbitoInteresse, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		Response resp = null;
		Client client = null;
		String targetUrl = this.endpointBase + "/tassi-interesse/idAmbitoInteresse" + idAmbitoInteresse;
		if (fruitore != null) {
			targetUrl += "?fruitore=" + fruitore;
		}

		try {
			MultivaluedMap<String, Object> map = getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);

			if (map.get(IrideIdAdapterFilter.AUTH_ID_MARKER) != null) {
				client = ClientBuilder.newClient().register(new AddHeaderFilter(IrideIdAdapterFilter.AUTH_ID_MARKER,
						(String) map.get(IrideIdAdapterFilter.AUTH_ID_MARKER).get(0)));
			} else {
				client = ClientBuilder.newClient();
			}

			SecurityContext securityContext = null;
			WebTarget target = client.target(this.endpointBase);
			ResteasyWebTarget rtarget = (ResteasyWebTarget) target;
			TassiDiInteresseApi TassiDiInteresseApi = rtarget.proxy(TassiDiInteresseApi.class);
			resp = TassiDiInteresseApi.deleteTassiDiInteresse(fruitore, idAmbitoInteresse.toString(), securityContext,
					httpHeaders, httpRequest);

			if (resp.getStatus() >= 400) {
				ErrorDTO err = getError(resp);
				LOGGER.error("[TassiDiInteresseApiServiceHelper::deleteTassiDiInteresse] GENERIC EXCEPTION : " + err);
				throw new GenericException(err);
			}
		} catch (GenericException e) {
			LOGGER.error("[TassiDiInteresseApiServiceHelper::deleteTassiDiInteresse] GENERIC EXCEPTION : " + e);
		}
		return resp;
	}

	public TassiDiInteresseDTO updateTassiDiInteresse(TassiDiInteresseDTO tassiDiInteresse,
			String fruitore, SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest)
			throws GenericException {

		TassiDiInteresseDTO result = new TassiDiInteresseDTO();
		String targetUrl = this.endpointBase + "/tassi-interesse/";
		if (fruitore != null) {
			targetUrl += "?fruitore=" + fruitore;
		}

		try {
			MultivaluedMap<String, Object> map = getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
			Entity<TassiDiInteresseDTO> entity = Entity.json(tassiDiInteresse);
			Response resp = getInvocationBuilder(targetUrl, map).put(entity);
			LOGGER.debug(
					"[TassiDiInteresseApiServiceHelper::updateTassiDiInteresse] RESPONSE STATUS : " + resp.getStatus());
			if (resp.getStatus() >= 400) {
				ErrorDTO err = getError(resp);
				LOGGER.debug("[TassiDiInteresseApiServiceHelper::updateTassiDiInteresse] SERVER EXCEPTION : " + err);
				throw new GenericException(err);
			}
			GenericType<TassiDiInteresseDTO> dtoType = new GenericType<TassiDiInteresseDTO>() {
			};
			result = resp.readEntity(dtoType);
		} catch (ProcessingException e) {
			LOGGER.debug("[TassiDiInteresseApiServiceHelper::updateTassiDiInteresse] EXCEPTION : " + e);
			throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
		}
		return result;
	}
}

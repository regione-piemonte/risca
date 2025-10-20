/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.business.be.helper.scriva;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.helper.AbstractServiceHelper;
import it.csi.risca.riscabesrv.business.be.helper.scriva.api.SoggettiApi;
import it.csi.risca.riscabesrv.business.be.helper.scriva.dto.SoggettoExtendedDTO;
import it.csi.risca.riscabesrv.business.be.helper.scriva.dto.XRequestAuth;
import it.csi.risca.riscabesrv.business.be.impl.dao.ConfigurazioneDAO;
import it.csi.risca.riscabesrv.util.oauth2.OauthHelper;

/**
 * The type Scriva service helper.
 *
 * @author CSI PIEMONTE
 */
public class ScrivaServiceHelper extends AbstractServiceHelper {

    private final String CLASSNAME = this.getClass().getSimpleName();
    private static final String CONF_KEY_SCRIVA_USERNAME = "";
    private static final String CONF_KEY_SCRIVA_PASSWORD = "";
    private static final String CONF_KEY_SCRIVA_CONSUMER_KEY = "RISCA_APIMAN_CONSUMERKEY";
    private static final String CONF_KEY_SCRIVA_CONSUMER_SECRET = "RISCA_APIMAN_CONSUMERSECRET";
    private static final List<String> CONF_KEYS_SCRIVA = Arrays.asList(CONF_KEY_SCRIVA_USERNAME, CONF_KEY_SCRIVA_PASSWORD, CONF_KEY_SCRIVA_CONSUMER_KEY, CONF_KEY_SCRIVA_CONSUMER_SECRET);


    private static final String COMPONENTE_APPLICATIVO = "RISCA";
    private static final String RUOLO = "RISCA_RW";
    private static final String USERNAME = "RISCA";
    private static final String PASSWORD = "a8b8ba8e9d1ba0aaf5f73ba93b79eebd";
    
    private final String tokenUrl;
    private final String apiEndpoint;
    private String consumerKey;
    private String consumerSecret;
    private Map<String, String> configurazioneList = null;

    @Autowired
    private ConfigurazioneDAO configurazioneDAO;

    /**
     * Instantiates a new Scriva service helper.
     *
     * @param endPoint   the end point
     * @param serviceUrl the service url
     * @throws JsonProcessingException the json processing exception
     */
    public ScrivaServiceHelper(String endPoint, String serviceUrl) throws JsonProcessingException {
        this.tokenUrl = endPoint + "/token";
        this.apiEndpoint = endPoint + serviceUrl;
    }

    /**
     * Sets conf keys.
     *
     * @throws JsonProcessingException the json processing exception
     */
    public void setConfKeys() throws JsonProcessingException {
        this.configurazioneList = this.configurazioneList == null || this.configurazioneList.isEmpty() ?
                configurazioneDAO.loadConfigByKeyList(CONF_KEYS_SCRIVA) :
                this.configurazioneList;
        if (this.configurazioneList != null && !this.configurazioneList.isEmpty()) {
            String username = configurazioneList.getOrDefault(CONF_KEY_SCRIVA_USERNAME, null);
            String password = configurazioneList.getOrDefault(CONF_KEY_SCRIVA_PASSWORD, null);
            this.consumerKey = configurazioneList.getOrDefault(CONF_KEY_SCRIVA_CONSUMER_KEY, null);
            this.consumerSecret = configurazioneList.getOrDefault(CONF_KEY_SCRIVA_CONSUMER_SECRET, null);
            //this.xRequestAuth = IndexFactory.getXRequestAuth(username, password, this.tenant, null, this.repository);
        }
    }

    /**
     * Gets consumer key.
     *
     * @return the consumer key
     * @throws JsonProcessingException the json processing exception
     */
    public String getConsumerKey() throws JsonProcessingException {
        if (this.configurazioneList == null || this.configurazioneList.isEmpty() || StringUtils.isBlank(this.consumerKey)) {
            setConfKeys();
        }
        return consumerKey;
    }

    /**
     * Gets consumer secret.
     *
     * @return the consumer secret
     * @throws JsonProcessingException the json processing exception
     */
    public String getConsumerSecret() throws JsonProcessingException {
        if (this.configurazioneList == null || this.configurazioneList.isEmpty() || StringUtils.isBlank(this.consumerSecret)) {
            setConfKeys();
        }
        return consumerSecret;
    }


    /**** NOTIFICHE ****/

    /**
     * Il servizio permette a sistemi fruitori esterni di inserire notifiche per gli utenti di SCRIVA appartenenti ad un determinato ente.
     *
     * @param creaNotificaFruitoreRequest the crea notifica fruitore request
     * @return the crea notifica fruitore response
     * @throws CosmoException the cosmo exception
     */
    public List<SoggettoExtendedDTO> getSoggettoFromScrivaOLD(MultivaluedMap<String, Object> requestHeaders, String codiceFiscale, String tipoSoggetto, String tipoAdempimento, String codiceFiscaleImpresa) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String inputParam = "Parametri in input codiceFiscale: \n" + codiceFiscale + "\n tipoSoggetto: " + tipoSoggetto + "\n tipoAdempimento:" + tipoAdempimento;
        LOGGER.debug(getClassFunctionDebugString(CLASSNAME, methodName, inputParam));
        LOGGER.debug(getClassFunctionBeginInfo(CLASSNAME, methodName));
//        List<SoggettoExtendedDTO> result = null;
//        String api = "/soggetti/cf/" + codiceFiscale + "/tipo-soggetto/" + tipoSoggetto + "/tipo-adempimento/" + tipoAdempimento;
//        try {
//            String url = apiEndpoint + api;
//            LOGGER.debug(getClassFunctionDebugString(CLASSNAME, methodName, "url [" + url + "]"));
//            //Entity<SoggettoExtendedDTO> entity = Entity.entity(codiceFiscale, tipoSoggetto, tipoAdempimento, MediaType.APPLICATION_JSON);
//            Response resp = getBuilder(url)
//                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
//                    .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
//                    .get();
//            if (resp.getStatus() >= 400) {
//                Esito esito = resp.readEntity(Esito.class);
//                LOGGER.debug(getClassFunctionErrorInfo(CLASSNAME, methodName, inputParam + "response : \n" + esito));
//                throw new CosmoException(esito);
//            } else {
//                result = resp.readEntity(CreaNotificaFruitoreResponse.class);
//                LOGGER.debug(getClassFunctionErrorInfo(CLASSNAME, methodName, inputParam + "response : \n" + result));
//            }
//        } catch (JsonProcessingException e) {
//            LOGGER.error(getClassFunctionErrorInfo(CLASSNAME, methodName, e));
//        } finally {
//            LOGGER.debug(getClassFunctionEndInfo(CLASSNAME, methodName));
//        }
//        return result;

        /*
        List<SoggettoExtendedDTO> result  = new ArrayList<SoggettoExtendedDTO>();
        String targetUrl = this.apiEndpoint + "/soggetti?codice_fiscale=" + codiceFiscale;
        if(!StringUtils.isBlank(tipoSoggetto))
        	targetUrl += "&tipo_soggetto=" + tipoSoggetto;
        if(!StringUtils.isBlank(tipoAdempimento))
        	targetUrl += "&cod_adempimento=" + tipoAdempimento; // codice_fiscale_impresa
        if(!StringUtils.isBlank(codiceFiscaleImpresa))
        	targetUrl += "&codice_fiscale_impresa=" + codiceFiscaleImpresa;
*/
        String targetUrl = this.apiEndpoint;
        
        try {
			Client client = ClientBuilder.newClient();
			
			/*
			// TODO: enable this to pass header token
			client.register(new ClientRequestFilter() {
				@Override
				public void filter(ClientRequestContext requestContext) throws IOException {
					requestContext.getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer " + getToken(ScrivaServiceHelper.this.tokenUrl, ScrivaServiceHelper.this.getConsumerKey(), ScrivaServiceHelper.this.getConsumerSecret()));
				}
			});
			*/			
			
			ResteasyWebTarget rtarget = (ResteasyWebTarget)client.target(targetUrl);
			
            SoggettiApi soggettiApi = rtarget.proxy(SoggettiApi.class);
            Response resp = soggettiApi.getSoggetti(
            		null, // xRequestAuth
            		null, // xRequestId
            		null, // idSoggetto
            		codiceFiscale, 
            		tipoSoggetto, 
            		tipoAdempimento, 
            		codiceFiscaleImpresa);

            if(resp.getStatus() == 200) {
                GenericType<List<SoggettoExtendedDTO>> soggetto = new GenericType<List<SoggettoExtendedDTO>>() {};
            	return (List<SoggettoExtendedDTO>)resp.readEntity(soggetto);
            }
            else if(resp.getStatus() == 404)
            	return null;
            
            LOGGER.error("[SoggettiApiServiceHelper::getSoggettoFromScriva] SERVER SCRIVA EXCEPTION : " + resp.getStatus() + " - target: " + targetUrl);
        } catch (Throwable e) {
            LOGGER.error("[SoggettiApiServiceHelper::getSoggettoFromScriva] EXCEPTION : " + e.getMessage() + " - target: " + targetUrl);
        } finally {
            LOGGER.debug("[SoggettiApiServiceHelper::getSoggettoFromScriva] END");
        }

        throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
    }

    private Invocation.Builder getBuilder(String url) throws JsonProcessingException {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String inputParam = "Parametro in input url [" + url + "]";
        LOGGER.debug(getClassFunctionDebugString(CLASSNAME, methodName, inputParam));
        LOGGER.debug(getClassFunctionBeginInfo(CLASSNAME, methodName));

        Client client = ClientBuilder.newBuilder().build();
        return client.target(url).request()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getToken(this.tokenUrl, this.getConsumerKey(), this.getConsumerSecret()));
    }

    private Invocation.Builder getBuilderProvv(String url) throws JsonProcessingException {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String inputParam = "Parametro in input url [" + url + "]";
        LOGGER.debug(getClassFunctionDebugString(CLASSNAME, methodName, inputParam));
        LOGGER.debug(getClassFunctionBeginInfo(CLASSNAME, methodName));

        Client client = ClientBuilder.newBuilder().build();
        return client.target(url).request();
    }
    
    private String getToken(String tokenUrl, String consumerKey, String consumerSecret) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String inputParam = "Parametro in input tokenUrl [" + tokenUrl + "] - consumerKey [" + consumerKey + "] - consumerSecret [" + consumerSecret + "]";
        LOGGER.debug(getClassFunctionDebugString(CLASSNAME, methodName, inputParam));
        LOGGER.debug(getClassFunctionBeginInfo(CLASSNAME, methodName));

        OauthHelper oauthHelper = new OauthHelper(
                tokenUrl,
                consumerKey,
                consumerSecret,
                10000);
        String token = oauthHelper.getToken();

        LOGGER.debug(getClassFunctionErrorInfo(CLASSNAME, methodName, inputParam + "\n token [" + token + "]"));
        return token;
    }
    
    
    
    
    public List<SoggettoExtendedDTO> getSoggettoFromScriva(MultivaluedMap<String, Object> requestHeaders, String codiceFiscale, String tipoSoggetto, String tipoAdempimento, String codiceFiscaleImpresa) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String inputParam = "Parametri in input codiceFiscale: \n" + codiceFiscale + "\n tipoSoggetto: " + tipoSoggetto + "\n tipoAdempimento:" + tipoAdempimento;
        LOGGER.debug(getClassFunctionDebugString(CLASSNAME, methodName, inputParam));
        LOGGER.debug(getClassFunctionBeginInfo(CLASSNAME, methodName));
        List<SoggettoExtendedDTO> result = null;
        String api = "/soggetti?codice_fiscale=" + codiceFiscale + "&tipo_soggetto=" + tipoSoggetto;
        try {
            String url = apiEndpoint + api;
            LOGGER.debug(getClassFunctionDebugString(CLASSNAME, methodName, "url [" + url + "]"));
            //Entity<SoggettoExtendedDTO> entity = Entity.entity(codiceFiscale, tipoSoggetto, tipoAdempimento, MediaType.APPLICATION_JSON);
            Response resp = getBuilder(url)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                    .get();
            if (resp.getStatus() >= 400) {
                String esito = resp.readEntity(String.class);
                LOGGER.debug(getClassFunctionErrorInfo(CLASSNAME, methodName, inputParam + "response : \n" + esito));
                //throw new CosmoException(esito);
            } else {
                GenericType<List<SoggettoExtendedDTO>> soggetti = new GenericType<List<SoggettoExtendedDTO>>() {
                };
                result = resp.readEntity(soggetti);
                LOGGER.debug(getClassFunctionErrorInfo(CLASSNAME, methodName, inputParam + "response : \n" + result));
            }
        } catch (JsonProcessingException e) {
            LOGGER.error(getClassFunctionErrorInfo(CLASSNAME, methodName, e),e);
        } finally {
            LOGGER.debug(getClassFunctionEndInfo(CLASSNAME, methodName));
        }
        return result;
    }
    
    public SoggettoExtendedDTO saveSoggettoInScriva(SoggettoExtendedDTO soggettoExtendedDTO) throws Exception {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.debug(getClassFunctionBeginInfo(CLASSNAME, methodName));
        SoggettoExtendedDTO result = null;
        String api = "/soggetti";
        try {
            String targetUrl = apiEndpoint + api;
            Entity<SoggettoExtendedDTO> entity = Entity.json(soggettoExtendedDTO);
            XRequestAuth xRequestAuth = new XRequestAuth();
            xRequestAuth.setComponenteApplicativa(COMPONENTE_APPLICATIVO);
            xRequestAuth.setRuolo(RUOLO);
            xRequestAuth.setUsername(USERNAME);
            xRequestAuth.setPassword(PASSWORD);
            String xRequestAuthJson = new ObjectMapper().writeValueAsString(xRequestAuth);
            String xRequestAuthHeader = new String(Base64.getEncoder().encode(xRequestAuthJson.getBytes()));
          String json =  new ObjectMapper().writer().writeValueAsString(soggettoExtendedDTO);
            Response resp = getBuilder(targetUrl)
            		.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                    .header("X-Request-Auth", xRequestAuthHeader)
                    .post(entity);
            LOGGER.debug(getClassFunctionDebugString(CLASSNAME, methodName," RESPONSE STATUS : " + resp.getStatus()));
            
            if (resp.getStatus() >= 400) {
                String esito = resp.readEntity(String.class);
            	it.csi.risca.riscabesrv.dto.ErrorObjectDTO err = new it.csi.risca.riscabesrv.dto.ErrorObjectDTO(resp.getStatus()+"", resp.getStatus()+"",esito, null, null, null);

                LOGGER.error(getClassFunctionErrorInfo(CLASSNAME, methodName, err.getTitle()));
                throw new BusinessException(Integer.valueOf(err.getStatus()),err.getCode(),err.getTitle(),err.getDetails());

            }
            GenericType<SoggettoExtendedDTO> dto = new GenericType<SoggettoExtendedDTO>()  {
            };
            result = resp.readEntity(dto);
        } catch (JsonProcessingException e) {
            LOGGER.error(getClassFunctionErrorInfo(CLASSNAME, methodName,  "response : \n" + e.getMessage()),e);
		} finally {
            LOGGER.debug(getClassFunctionEndInfo(CLASSNAME, methodName));
        }
        return result;
    }
    public SoggettoExtendedDTO updateSoggettoInScriva(SoggettoExtendedDTO soggettoExtendedDTO) throws Exception {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.debug(getClassFunctionBeginInfo(CLASSNAME, methodName));
        SoggettoExtendedDTO result = null;
        String api = "/soggetti";
        try {
            String targetUrl = apiEndpoint + api;
            Entity<SoggettoExtendedDTO> entity = Entity.json(soggettoExtendedDTO);
            XRequestAuth xRequestAuth = new XRequestAuth();
            xRequestAuth.setComponenteApplicativa(COMPONENTE_APPLICATIVO);
            xRequestAuth.setRuolo(RUOLO);
            xRequestAuth.setUsername(USERNAME);
            xRequestAuth.setPassword(PASSWORD);
            String xRequestAuthJson = new ObjectMapper().writeValueAsString(xRequestAuth);
            String xRequestAuthHeader = new String(Base64.getEncoder().encode(xRequestAuthJson.getBytes()));
            String json =  new ObjectMapper().writer().writeValueAsString(soggettoExtendedDTO);
            Response resp = getBuilder(targetUrl)
            		.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                    .header("X-Request-Auth", xRequestAuthHeader)
                    .post(entity);
            LOGGER.debug(getClassFunctionDebugString(CLASSNAME, methodName," RESPONSE STATUS : " + resp.getStatus()));
            
            if (resp.getStatus() >= 400) {
                String esito = resp.readEntity(String.class);
            	it.csi.risca.riscabesrv.dto.ErrorObjectDTO err = new it.csi.risca.riscabesrv.dto.ErrorObjectDTO(resp.getStatus()+"", resp.getStatus()+"",esito, null, null, null);

                LOGGER.error(getClassFunctionErrorInfo(CLASSNAME, methodName, err.getTitle()));
                throw new BusinessException(Integer.valueOf(err.getStatus()),err.getCode(),err.getTitle(),err.getDetails());


            }
            GenericType<SoggettoExtendedDTO> dto = new GenericType<SoggettoExtendedDTO>()  {
            };
            result = resp.readEntity(dto);
        } finally {
            LOGGER.debug(getClassFunctionEndInfo(CLASSNAME, methodName));
        }
        return result;
    }

}
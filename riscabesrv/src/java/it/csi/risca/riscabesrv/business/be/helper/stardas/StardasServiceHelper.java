/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.business.be.helper.stardas;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.ext.logging.LoggingInInterceptor;
import org.apache.cxf.ext.logging.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonProcessingException;


import it.csi.risca.riscabesrv.business.be.impl.dao.ConfigurazioneDAO;
import it.csi.risca.riscabesrv.util.Constants;
import it.csi.risca.riscabesrv.util.oauth2.OauthHelper;
import it.csi.stardas.wso2.stardasservice.StardasService;

public class StardasServiceHelper {

	private static final Logger log = Logger.getLogger(Constants.LOGGER_NAME);

	private static final String CONF_KEY_APIMAN_CONSUMER_KEY = "RISCA_APIMAN_CONSUMERKEY";
	private static final String CONF_KEY_APIMAN_CONSUMER_SECRET = "RISCA_APIMAN_CONSUMERSECRET";
	private static final List<String> CONF_KEYS_APIMAN = Arrays.asList(CONF_KEY_APIMAN_CONSUMER_KEY,
			CONF_KEY_APIMAN_CONSUMER_SECRET);

	private Map<String, String> configurazioneList = null;

	private String apiEndpoint;
	private String tokenUrl;

	private String consumerKey;
	private String consumerSecret;

	@Autowired
	private ConfigurazioneDAO configurazioneDAO;

	public StardasServiceHelper(String endPoint, String serviceUrl) {
		this.tokenUrl = endPoint + "/token";
		this.apiEndpoint = endPoint + serviceUrl;
	}

	public String getEndpoint() {
		return apiEndpoint;
	}

	public void setConfKeys() throws JsonProcessingException {
		this.configurazioneList = this.configurazioneList == null || this.configurazioneList.isEmpty()
				? configurazioneDAO.loadConfigByKeyList(CONF_KEYS_APIMAN)
				: this.configurazioneList;
		if (this.configurazioneList != null && !this.configurazioneList.isEmpty()) {
			this.consumerKey = configurazioneList.getOrDefault(CONF_KEY_APIMAN_CONSUMER_KEY, null);
			this.consumerSecret = configurazioneList.getOrDefault(CONF_KEY_APIMAN_CONSUMER_SECRET, null);
		}
	}

	/**
	 * Gets consumer key.
	 *
	 * @return the consumer key
	 * @throws JsonProcessingException the json processing exception
	 */
	public String getConsumerKey() throws JsonProcessingException {
		if (this.configurazioneList == null || this.configurazioneList.isEmpty()
				|| StringUtils.isBlank(this.consumerKey)) {
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
		if (this.configurazioneList == null || this.configurazioneList.isEmpty()
				|| StringUtils.isBlank(this.consumerSecret)) {
			setConfKeys();
		}
		return consumerSecret;
	}

	public StardasService getSoapClient() {
		log.debug("[StardasServiceHelper::getSoapClient] BEGIN");
		JaxWsProxyFactoryBean fact = new JaxWsProxyFactoryBean();
		try {
			fact.setServiceClass(StardasService.class);
			fact.setAddress(apiEndpoint);

			fact.getInInterceptors().add(new LoggingInInterceptor());
			fact.getOutInterceptors().add(new LoggingOutInterceptor());
			fact.getOutInterceptors()
					.add(new OAuth2Interceptor(getToken(tokenUrl, getConsumerKey(), getConsumerSecret())));
			
		} catch (Exception e) {
			log.error("[StardasServiceHelper::getSoapClient] Errore: ", e);
			throw new StardasException("Errore getSoapClient: " + e.getMessage());
		} finally {
			log.debug("[StardasServiceHelper::getSoapClient] END");
		}

		return (StardasService) fact.create();
	}

	private String getToken(String tokenUrl, String consumerKey, String consumerSecret) {

		log.debug("[StardasServiceHelper::getToken]");

		OauthHelper oauthHelper = new OauthHelper(tokenUrl, consumerKey, consumerSecret, 10000);
		String token = oauthHelper.getToken();

		log.debug("[StardasServiceHelper::getToken] token [" + token + "]");
		return token;
	}

}

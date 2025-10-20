/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.business.be.helper.pagopa;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.cxf.ext.logging.LoggingInInterceptor;
import org.apache.cxf.ext.logging.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.log4j.Logger;
import org.apache.wss4j.dom.WSConstants;
import org.apache.wss4j.dom.handler.WSHandlerConstants;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.epay.epaywso.enti2epaywsosrv.Enti2EPaywsoSecureProxyPortType;
import it.csi.risca.riscabesrv.business.be.impl.dao.ConfigurazioneDAO;
import it.csi.risca.riscabesrv.util.Constants;

public class PagopaServiceHelper {

	private static final Logger log = Logger.getLogger(Constants.LOGGER_NAME);

	private static final String RISCA_PAGOPA_USERNAME_KEY = "RISCA_PAGOPA.username";
	private static final String RISCA_PAGOPA_PASSWORD_KEY = "RISCA_PAGOPA.password";

	private String endpoint;
	private String username;
	private String password;

	@Autowired
	private ConfigurazioneDAO configurazioneDAO;

	public PagopaServiceHelper(String epayEndpoint) {
		this.endpoint = epayEndpoint;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public Enti2EPaywsoSecureProxyPortType getSoapClient() {
		log.debug("[PagopaServiceHelper::getSoapClient] BEGIN");
		if (username == null) {
			loadConfig();
		}
		JaxWsProxyFactoryBean fact = new JaxWsProxyFactoryBean();
		try {
			fact.setServiceClass(Enti2EPaywsoSecureProxyPortType.class);
			fact.setAddress(endpoint);

			fact.getInInterceptors().add(new LoggingInInterceptor());
			fact.getOutInterceptors().add(new LoggingOutInterceptor());

			Map<String, Object> wss4jInterceptorProps = new HashMap<String, Object>();
			wss4jInterceptorProps.put(WSHandlerConstants.ACTION,
					WSHandlerConstants.TIMESTAMP + " " + WSHandlerConstants.USERNAME_TOKEN);
			wss4jInterceptorProps.put(WSHandlerConstants.USER, username);
			wss4jInterceptorProps.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
			wss4jInterceptorProps.put(WSHandlerConstants.PW_CALLBACK_CLASS, UsernamePasswordCallback.class.getName());

			WSS4JOutInterceptor wssOut = new WSS4JOutInterceptor(wss4jInterceptorProps);
			fact.getOutInterceptors().add(wssOut);

		} catch (Exception e) {
			log.error("[PagopaServiceHelper::getSoapClient] Errore: ", e);
			throw new PagopaException("Errore getSoapClient: " + e.getMessage());
		} finally {
			log.debug("[PagopaServiceHelper::getSoapClient] END");
		}

		return (Enti2EPaywsoSecureProxyPortType) fact.create();
	}

	public String getPassword() {
		if (password == null) {
			loadConfig();
		}
		return password;
	}

	private void loadConfig() {
		List<String> keys = Arrays.asList(RISCA_PAGOPA_USERNAME_KEY, RISCA_PAGOPA_PASSWORD_KEY);
		Map<String, String> configs = configurazioneDAO.loadConfigByKeyList(keys);
		for (Map.Entry<String, String> entry : configs.entrySet()) {
			if (entry.getKey().equals(RISCA_PAGOPA_USERNAME_KEY)) {
				this.username = entry.getValue();
			}
			if (entry.getKey().equals(RISCA_PAGOPA_PASSWORD_KEY)) {
				this.password = entry.getValue();
				PasswordHolder.getInstance().setPassword(this.password);
			}
		}
	}

}

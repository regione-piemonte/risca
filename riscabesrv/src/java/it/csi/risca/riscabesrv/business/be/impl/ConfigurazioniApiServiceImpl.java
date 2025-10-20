/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.business.be.impl;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.risca.riscabesrv.business.be.ConfigurazioniApi;
import it.csi.risca.riscabesrv.business.be.impl.dao.ConfigurazioneDAO;
import it.csi.risca.riscabesrv.dto.ConfigurazioneDTO;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type Tipi riscossione api service.
 *
 * @author CSI PIEMONTE
 */
@Component
public class ConfigurazioniApiServiceImpl implements ConfigurazioniApi {

	private static final String IDENTITY = "identity";
	private static Logger LOGGER = Logger.getLogger(Constants.LOGGER_NAME);

	@Autowired
	private ConfigurazioneDAO configurazioneDAO;

	@Override
	public Response loadConfigurazioni(SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		LOGGER.debug("[ConfigurazioniApiServiceImpl::loadConfigurazioni] BEGIN");
		List<ConfigurazioneDTO> listConfig = configurazioneDAO.loadConfig();
		if (listConfig == null) {
			LOGGER.error("[ConfigurazioniApiServiceImpl::loadConfigurazioni] ERROR : ");
			LOGGER.debug("[ConfigurazioniApiServiceImpl::loadConfigurazioni] END");
			return Response.serverError().status(500).build();
		}
		LOGGER.debug("[ConfigurazioniApiServiceImpl::loadConfigurazioni] END");
		return Response.ok(listConfig).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response loadConfigurazioneByKey(String key, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		LOGGER.debug("[ConfigurazioniApiServiceImpl::loadConfigurazioneByKey] BEGIN");
		List<ConfigurazioneDTO> listConfig = configurazioneDAO.loadConfigByKey(key);
		if (listConfig == null) {
			LOGGER.error("[ConfigurazioniApiServiceImpl::loadConfigurazioneByKey] ERROR : ");
			LOGGER.debug("[ConfigurazioniApiServiceImpl::loadConfigurazioneByKey] END");
			return Response.serverError().status(500).build();
		}
		LOGGER.debug("[ConfigurazioniApiServiceImpl::loadConfigurazioneByKey] END");
		return Response.ok(listConfig).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response loadConfigurazioneByKeyList(List<String> keyList, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[ConfigurazioniApiServiceImpl::loadConfigurazioneByKeyList] BEGIN");
		Map<String, String> configs = configurazioneDAO.loadConfigByKeyList(keyList);
		if (configs == null) {
			LOGGER.error("[ConfigurazioniApiServiceImpl::loadConfigurazioneByKeyList] ERROR : ");
			LOGGER.debug("[ConfigurazioniApiServiceImpl::loadConfigurazioneByKeyList] END");
			return Response.serverError().status(500).build();
		}
		LOGGER.debug("[ConfigurazioniApiServiceImpl::loadConfigurazioneByKeyList] END");
		return Response.ok(configs).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

}

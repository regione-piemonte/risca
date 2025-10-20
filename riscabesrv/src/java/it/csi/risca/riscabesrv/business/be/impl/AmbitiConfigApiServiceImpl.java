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

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.iride2.policy.entity.Application;
import it.csi.iride2.policy.entity.Identita;
import it.csi.iride2.policy.entity.UseCase;
import it.csi.risca.riscabesrv.business.be.AmbitiConfigApi;
import it.csi.risca.riscabesrv.business.be.helper.iride.IrideServiceHelper;
import it.csi.risca.riscabesrv.business.be.impl.dao.AmbitiConfigDAO;
import it.csi.risca.riscabesrv.dto.AmbitoConfigDTO;
import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.identitadigitale.IdentitaDigitaleManager;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type Tipi riscossione api service.
 *
 * @author CSI PIEMONTE
 */
@Component
public class AmbitiConfigApiServiceImpl implements AmbitiConfigApi {
	
	private static final String IDENTITY = "identity";
	private static Logger LOGGER = Logger.getLogger(Constants.LOGGER_NAME);

	@Autowired
	private AmbitiConfigDAO ambitiConfigDAO;
	
    @Autowired
    private IrideServiceHelper serviceHelper;
	    /**
	     * @param securityContext SecurityContext
	     * @param httpHeaders     HttpHeaders
	     * @param httpRequest     HttpServletRequest
	     * @return Response
	     */
		@Override
		public Response loadAmbitiConfig(SecurityContext securityContext, HttpHeaders httpHeaders,
				HttpServletRequest httpRequest) {
	        LOGGER.debug("[AmbitiConfigApiServiceImpl::loadAmbitiConfig] BEGIN");
	        List<AmbitoConfigDTO> listAmbitiConfig;
			try {
				listAmbitiConfig = ambitiConfigDAO.loadAmbitiConfig();
			} catch (Exception e) {
				ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
	            return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
			}
	       
	        LOGGER.debug("[AmbitiConfigApiServiceImpl::loadAmbitiConfig] END");
	        return Response.ok(listAmbitiConfig).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		}

	    /**
	     * 
	     * @param idAmbito idAmbito
	     * @param securityContext SecurityContext
	     * @param httpHeaders     HttpHeaders
	     * @param httpRequest     HttpServletRequest
	     * @return Response
	     */
		@Override
		public Response loadAmbitiConfigByIdOrCodAmbito(String idAmbito, SecurityContext securityContext,
				HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
	        LOGGER.debug("[AmbitiConfigApiServiceImpl::loadAmbitiConfigByIdOrCodAmbito] BEGIN");
			Identita identita = IdentitaDigitaleManager.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders),null);
			Application cod = new Application();
			cod.setId("RISCA");
			UseCase use = new UseCase();
			use.setAppId(cod);
			use.setId("UC_SIPRA");
			Long idAmbitoSess = null;
			if (identita != null) {
				idAmbitoSess = serviceHelper.getInfoPersonaInUseCase(identita, use);
				LOGGER.debug(
						"[AmbitiConfigApiServiceImpl::loadAmbitiConfigByIdOrCodAmbito] idAmbitoSess:" + idAmbitoSess);
				if (idAmbito != null && idAmbitoSess != null) {
					LOGGER.debug("[AmbitiConfigApiServiceImpl::loadAmbitiConfigByIdOrCodAmbito] idAmbito:" + idAmbito);
					if (Long.parseLong(idAmbito) != idAmbitoSess) {
						ErrorDTO err = new ErrorDTO("401", "E100", "Ambito non consentito", null, null);
			            return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
					}
				}
			}
	        List<AmbitoConfigDTO> listAmbitoConfig;
			try {
				listAmbitoConfig = ambitiConfigDAO.loadAmbitiConfigByIdOrCodAmbito(idAmbito);
			}  catch (Exception e) {
				ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
	            return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
			}
	       
	        LOGGER.debug("[AmbitiConfigApiServiceImpl::loadAmbitiConfigByIdOrCodAmbito] END");
	        return Response.ok(listAmbitoConfig).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		}

	    /**
	     * @param securityContext SecurityContext
	     * @param httpHeaders     HttpHeaders
	     * @param httpRequest     HttpServletRequest
	     * @return Response
	     */
		@Override
		public Response loadAmbitiConfigByCodeAndKey(String codAmbito, String chiave, SecurityContext securityContext,
				HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
	        LOGGER.debug("[AmbitiConfigApiServiceImpl::loadAmbitiConfigByCodeAndKey] BEGIN");
	        List<AmbitoConfigDTO> listAmbitoConfig;
			try {
				listAmbitoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(codAmbito, chiave);
			}  catch (Exception e) {
				ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
	            return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
			}
	       
	        LOGGER.debug("[AmbitiConfigApiServiceImpl::loadAmbitiConfigByCodeAndKey] END");
	        return Response.ok(listAmbitoConfig).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		}

	
}

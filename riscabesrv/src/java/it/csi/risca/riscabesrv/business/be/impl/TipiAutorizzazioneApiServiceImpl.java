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
import it.csi.risca.riscabesrv.business.be.TipiAutorizzazioneApi;
import it.csi.risca.riscabesrv.business.be.helper.iride.IrideServiceHelper;
import it.csi.risca.riscabesrv.business.be.impl.dao.TipiAutorizzazioneDAO;
import it.csi.risca.riscabesrv.dto.ErrorDTO;
//import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.dto.TipiAutorizzazioneDTO;
import it.csi.risca.riscabesrv.dto.TipiAutorizzazioneExtendedDTO;
import it.csi.risca.riscabesrv.identitadigitale.IdentitaDigitaleManager;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type Tipi autorizzazione api service.
 *
 * @author CSI PIEMONTE
 */
@Component
public class TipiAutorizzazioneApiServiceImpl implements TipiAutorizzazioneApi {
	
		private static final String IDENTITY = "identity";
		private static Logger LOGGER = Logger.getLogger(Constants.LOGGER_NAME);

		@Autowired
	    private TipiAutorizzazioneDAO tipoAutorizzazioneDAO;


		@Autowired
		private IrideServiceHelper serviceHelper;
		
	    /**
	     * @param securityContext SecurityContext
	     * @param httpHeaders     HttpHeaders
	     * @param httpRequest     HttpServletRequest
	     * @return Response
	     */
	    @Override
	    public Response loadTipiAutorizzazione(SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
	        LOGGER.debug("[TipiAutorizzazioneApiServiceImpl::loadTipiAutorizzazione] BEGIN");
	        List<TipiAutorizzazioneExtendedDTO> listAutorizzazioni;
			try {
				listAutorizzazioni = tipoAutorizzazioneDAO.loadTipiAutorizzazione();
			} catch (Exception e) {
				return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
						.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
			}
	        LOGGER.debug("[TipiAutorizzazioneApiServiceImpl::loadTipiAutorizzazione] END");
	        return Response.ok(listAutorizzazioni).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	    }

	    /**
	     * @param idAmbito    	  idAmbito
	     * @param securityContext SecurityContext
	     * @param httpHeaders     HttpHeaders
	     * @param httpRequest     HttpServletRequest
	     * @return Response
	     */
	    @Override
	    public Response loadTipiAutorizzazioneByIdAmbito(Long idAmbito, String dataIniVal,  String dataFineVal, SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
	        LOGGER.debug("[TipiAutorizzazioneApiServiceImpl::loadTipiAutorizzazioneByIdAmbito] BEGIN");
	        LOGGER.debug("[TipiAutorizzazioneApiServiceImpl::loadTipiAutorizzazioneByIdAmbito] Parametro in input idAmbito [" + idAmbito + "]");
			Identita identita = IdentitaDigitaleManager.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders),null);

			Application cod = new Application();
			cod.setId("RISCA");
			UseCase use = new UseCase();
			use.setAppId(cod);
			use.setId("UC_SIPRA");
			Long idAmbitoSess = null;
			if (identita != null) {
				idAmbitoSess = serviceHelper.getInfoPersonaInUseCase(identita, use);
				LOGGER.debug("[TipiAutorizzazioneApiServiceImpl::loadTipiAutorizzazioneByIdAmbito] idAmbitoSess:"+ idAmbitoSess);
				if (idAmbito != null && idAmbitoSess != null) {
					LOGGER.debug(
							"[TipiAutorizzazioneApiServiceImpl::loadTipiAutorizzazioneByIdAmbito] idAmbito:" + idAmbito);
					if (!idAmbito.equals(idAmbitoSess)) {
						ErrorDTO err = new ErrorDTO("401", "E100", "Ambito non consentito", null, null);
						return Response.serverError().entity(err).status(401).build();
					}
				}
			}
	        List<TipiAutorizzazioneExtendedDTO> listAutorizzazioni;
			try {
				listAutorizzazioni = tipoAutorizzazioneDAO.loadTipiAutorizzazioneByIdAmbito(idAmbito, dataIniVal, dataFineVal);
			} catch (Exception e) {
				ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
				return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
			}
	        LOGGER.debug("[TipiAutorizzazioneApiServiceImpl::loadTipiAutorizzazioneByIdAmbito] END");
	        return Response.ok(listAutorizzazioni).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	    }

	    /**
	     * @param idTipoAutorizzazione idTipoAutorizzazione
	     * @param idAmbito			idAmbito
	     * @param securityContext   SecurityContext
	     * @param httpHeaders       HttpHeaders
	     * @param httpRequest       HttpServletRequest
	     * @return Response
	     */



		@Override
		public Response loadTipoAutorizzazioneByIdTipoAutorizzazioneAndIdAmbito(Long idTipoAutorizzazione, Long idAmbito,
				SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
	        LOGGER.debug("[TipiAutorizzazioneApiServiceImpl::loadTipoAutorizzazioneByIdTipoAutorizzazioneAndIdAmbito] BEGIN");
	        LOGGER.debug("[TipiAutorizzazioneApiServiceImpl::loadTipoAutorizzazioneByIdTipoAutorizzazioneAndIdAmbito] Parametro in input idTipoAutorizzazione [" + idTipoAutorizzazione + "] e idAmbito [" + idAmbito + "]");
			Identita identita = IdentitaDigitaleManager.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders),null);

			Application cod = new Application();
			cod.setId("RISCA");
			UseCase use = new UseCase();
			use.setAppId(cod);
			use.setId("UC_SIPRA");
			Long idAmbitoSess = null;
			if (identita != null) {
				idAmbitoSess = serviceHelper.getInfoPersonaInUseCase(identita, use);
				LOGGER.debug("[TipiAutorizzazioneApiServiceImpl::loadTipoAutorizzazioneByIdTipoAutorizzazioneAndIdAmbito] idAmbitoSess:"+ idAmbitoSess);
				if (idAmbito != null && idAmbitoSess != null) {
					LOGGER.debug(
							"[TipiAutorizzazioneApiServiceImpl::loadTipoAutorizzazioneByIdTipoAutorizzazioneAndIdAmbito] idAmbito:" + idAmbito);
					if (!idAmbito.equals(idAmbitoSess)) {
						ErrorDTO err = new ErrorDTO("401", "E100", "Ambito non consentito", null, null);
						return Response.serverError().entity(err).status(401).build();
					}
				}
			}
	        TipiAutorizzazioneDTO tipoAutorizzazione;
			try {
				tipoAutorizzazione = tipoAutorizzazioneDAO.loadTipoAutorizzazioneByIdTipiAutorizzazioneAndIdAmbito(idTipoAutorizzazione, idAmbito);
			} catch (Exception e) {
				ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
				return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
			}
            LOGGER.debug("[TipiAutorizzazioneApiServiceImpl::loadTipoAutorizzazioneByIdTipoAutorizzazioneAndIdAmbito] END");
	        return Response.ok(tipoAutorizzazione).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		}
		
		/**
	     * @param codTipoAutorizzazione codTipoAutorizzazione
	     * @param idAmbito			idAmbito
	     * @param securityContext   SecurityContext
	     * @param httpHeaders       HttpHeaders
	     * @param httpRequest       HttpServletRequest
	     * @return Response
	     */

		@Override
		public Response loadTipoAutorizzazioneByCodeAndIdAmbito(String codOrIdTipoAutorizzazione, Long idAmbito,
				SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
	        LOGGER.debug("[TipiAutorizzazioneApiServiceImpl::loadTipoAutorizzazioneByCodeAndIdAmbito] BEGIN");
	        LOGGER.debug("[TipiAutorizzazioneApiServiceImpl::loadTipoAutorizzazioneByCodeAndIdAmbito] Parametri in input codOrIdTipoAutorizzazione [" + codOrIdTipoAutorizzazione + "] e idAmbito [" + idAmbito + "]");
			Identita identita = IdentitaDigitaleManager.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders),null);

			Application cod = new Application();
			cod.setId("RISCA");
			UseCase use = new UseCase();
			use.setAppId(cod);
			use.setId("UC_SIPRA");
			Long idAmbitoSess = null;
			if (identita != null) {
				idAmbitoSess = serviceHelper.getInfoPersonaInUseCase(identita, use);
				LOGGER.debug("[TipiAutorizzazioneApiServiceImpl::loadTipoAutorizzazioneByCodeAndIdAmbito] idAmbitoSess:"+ idAmbitoSess);
				if (idAmbito != null && idAmbitoSess != null) {
					LOGGER.debug("[TipiAutorizzazioneApiServiceImpl::loadTipoAutorizzazioneByCodeAndIdAmbito] idAmbito:" + idAmbito);
					if (!idAmbito.equals(idAmbitoSess)) {
						ErrorDTO err = new ErrorDTO("401", "E100", "Ambito non consentito", null, null);
						return Response.serverError().entity(err).status(401).build();
					}
				}
			}
	        TipiAutorizzazioneDTO tipoAutorizzazione;
			try {
				tipoAutorizzazione = tipoAutorizzazioneDAO.loadTipoAutorizzazioneByCodeAndIdAmbito(codOrIdTipoAutorizzazione, idAmbito);
			} catch (Exception e) {
				ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
				return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
			}
	        LOGGER.debug("[TipiAutorizzazioneApiServiceImpl::loadTipoAutorizzazioneByCodeAndIdAmbito] END");
	        return Response.ok(tipoAutorizzazione).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		}

	
}

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
import it.csi.risca.riscabesrv.business.be.TipiTitoloApi;
import it.csi.risca.riscabesrv.business.be.helper.iride.IrideServiceHelper;
import it.csi.risca.riscabesrv.business.be.impl.dao.TipiTitoloDAO;
import it.csi.risca.riscabesrv.dto.ErrorDTO;
//import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.dto.TipiTitoloDTO;
import it.csi.risca.riscabesrv.dto.TipiTitoloExtendedDTO;
import it.csi.risca.riscabesrv.identitadigitale.IdentitaDigitaleManager;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type Tipi titolo api service.
 *
 * @author CSI PIEMONTE
 */
@Component
public class TipiTitoloApiServiceImpl implements TipiTitoloApi {

	private static final String IDENTITY = "identity";
	private static Logger LOGGER = Logger.getLogger(Constants.LOGGER_NAME);

	@Autowired
	private TipiTitoloDAO tipiTitoloDAO;
	@Autowired
	private IrideServiceHelper serviceHelper;

	/**
	 * @param securityContext SecurityContext
	 * @param httpHeaders     HttpHeaders
	 * @param httpRequest     HttpServletRequest
	 * @return Response
	 */
	@Override
	public Response loadTipiTitolo(SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		LOGGER.debug("[TipiTitoloApiServiceImpl::loadTipiTitolo] BEGIN");
		List<TipiTitoloExtendedDTO> listTitolo = null;
		try {
			listTitolo = tipiTitoloDAO.loadTipiTitolo();
			LOGGER.debug("[TipiTitoloApiServiceImpl::loadTipiTitolo] END");
		} catch (Exception e) {
			ErrorDTO err = new ErrorDTO("500", "E005",
					"Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
			return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();

		}
		return Response.ok(listTitolo).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	/**
	 * @param idAmbito        idAmbito
	 * @param securityContext SecurityContext
	 * @param httpHeaders     HttpHeaders
	 * @param httpRequest     HttpServletRequest
	 * @return Response
	 */
	@Override
	public Response loadTipiTitoloByIdAmbito(Long idAmbito, String dataIniVal, String dataFineVal,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[TipiTitoloApiServiceImpl::loadTipiTitoloByIdAmbito] BEGIN");
		LOGGER.debug(
				"[TipiTitoloApiServiceImpl::loadTipiTitoloByIdAmbito] Parametro in input idAmbito [" + idAmbito + "]");
		Identita identita = IdentitaDigitaleManager
				.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders), null);

		Application cod = new Application();
		cod.setId("RISCA");
		UseCase use = new UseCase();
		use.setAppId(cod);
		use.setId("UC_SIPRA");
		Long idAmbitoSess = null;
		if (identita != null) {
			idAmbitoSess = serviceHelper.getInfoPersonaInUseCase(identita, use);
			LOGGER.debug("[TipiTitoloApiServiceImpl::loadTipiTitoloByIdAmbito] idAmbitoSess:" + idAmbitoSess);
			if (idAmbito != null && idAmbitoSess != null) {
				LOGGER.debug("[TipiTitoloApiServiceImpl::loadTipiTitoloByIdAmbito] idAmbito:" + idAmbito);
				if (!idAmbito.equals(idAmbitoSess)) {
					ErrorDTO err = new ErrorDTO("401", "E100", "Ambito non consentito", null, null);
					return Response.serverError().entity(err).status(401).build();
				}
			}
		}
		List<TipiTitoloExtendedDTO> listTitolo;
		try {
			listTitolo = tipiTitoloDAO.loadTipiTitoloByIdAmbito(idAmbito, dataIniVal, dataFineVal);
		} catch (Exception e) {
			ErrorDTO err = new ErrorDTO("500", "E005",
					"Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
			return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();

		}
		LOGGER.debug("[TipiTitoloApiServiceImpl::loadTipiTitoloByIdAmbito] END");
		return Response.ok(listTitolo).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	/**
	 * @param idTipoTitolo    idTipoTitolo
	 * @param idAmbito        idAmbito
	 * @param securityContext SecurityContext
	 * @param httpHeaders     HttpHeaders
	 * @param httpRequest     HttpServletRequest
	 * @return Response
	 */

	@Override
	public Response loadTipoTitoloByIdTipoTitoloAndIdAmbito(Long idTipoTitolo, Long idAmbito,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[TipiTitoloApiServiceImpl::loadTipoTitoloByIdTipoTitoloAndIdAmbito] BEGIN");
		LOGGER.debug(
				"[TipiTitoloApiServiceImpl::loadTipoTitoloByIdTipoTitoloAndIdAmbito] Parametro in input idTipoTitolo ["
						+ idTipoTitolo + "] e idAmbito [" + idAmbito + "]");
		Identita identita = IdentitaDigitaleManager
				.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders), null);

		Application cod = new Application();
		cod.setId("RISCA");
		UseCase use = new UseCase();
		use.setAppId(cod);
		use.setId("UC_SIPRA");
		Long idAmbitoSess = null;
		if (identita != null) {
			idAmbitoSess = serviceHelper.getInfoPersonaInUseCase(identita, use);
			LOGGER.debug(
					"[TipiTitoloApiServiceImpl::loadTipoTitoloByIdTipoTitoloAndIdAmbito] idAmbitoSess:" + idAmbitoSess);
			if (idAmbito != null && idAmbitoSess != null) {
				LOGGER.debug(
						"[TipiTitoloApiServiceImpl::loadTipoTitoloByIdTipoTitoloAndIdAmbito] idAmbito:" + idAmbito);
				if (!idAmbito.equals(idAmbitoSess)) {
					ErrorDTO err = new ErrorDTO("401", "E100", "Ambito non consentito", null, null);
					return Response.serverError().entity(err).status(401).build();
				}
			}
		}
		TipiTitoloDTO tipoTitolo;
		try {
			tipoTitolo = tipiTitoloDAO.loadTipoTitoloByIdTipiTitoloAndIdAmbito(idTipoTitolo, idAmbito);
		} catch (Exception e) {
			ErrorDTO err = new ErrorDTO("500", "E005",
					"Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
			return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();

		}
		LOGGER.debug("[TipiTitoloApiServiceImpl::loadTipoTitolo] END");
		return Response.ok(tipoTitolo).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	/**
	 * @param codTipoTitolo   codTipoTitolo
	 * @param idAmbito        idAmbito
	 * @param securityContext SecurityContext
	 * @param httpHeaders     HttpHeaders
	 * @param httpRequest     HttpServletRequest
	 * @return Response
	 */

	@Override
	public Response loadTipoTitoloByCodeAndIdAmbito(String codOrIdTipoTitolo, Long idAmbito,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[TipiTitoloApiServiceImpl::loadTipoTitoloByCodeAndIdAmbito] BEGIN");
		LOGGER.debug("[TipiTitoloApiServiceImpl::loadTipoTitoloByCodeAndIdAmbito] Parametri in input codTipoTitolo ["
				+ codOrIdTipoTitolo + "] e idAmbito [" + idAmbito + "]");
		Identita identita = IdentitaDigitaleManager
				.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders), null);

		Application cod = new Application();
		cod.setId("RISCA");
		UseCase use = new UseCase();
		use.setAppId(cod);
		use.setId("UC_SIPRA");
		Long idAmbitoSess = null;
		if (identita != null) {
			idAmbitoSess = serviceHelper.getInfoPersonaInUseCase(identita, use);
			LOGGER.debug("[TipiTitoloApiServiceImpl::loadTipoTitoloByCodeAndIdAmbito] idAmbitoSess:" + idAmbitoSess);
			if (idAmbito != null && idAmbitoSess != null) {
				LOGGER.debug("[TipiTitoloApiServiceImpl::loadTipoTitoloByCodeAndIdAmbito] idAmbito:" + idAmbito);
				if (!idAmbito.equals(idAmbitoSess)) {
					ErrorDTO err = new ErrorDTO("401", "E100", "Ambito non consentito", null, null);
					return Response.serverError().entity(err).status(401).build();
				}
			}
		}
		TipiTitoloDTO tipoTitolo;
		try {
			tipoTitolo = tipiTitoloDAO.loadTipoTitoloByCodeAndIdAmbito(codOrIdTipoTitolo, idAmbito);
		} catch (Exception e) {
			ErrorDTO err = new ErrorDTO("500", "E005",
					"Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
			return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();

		}

		LOGGER.debug("[TipiTitoloApiServiceImpl::loadTipoTitoloByCodeAndIdAmbito] END");
		return Response.ok(tipoTitolo).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

}

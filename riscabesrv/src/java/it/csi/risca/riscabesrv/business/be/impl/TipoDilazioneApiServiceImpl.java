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
import it.csi.risca.riscabesrv.business.be.TipoDilazioneApi;
import it.csi.risca.riscabesrv.business.be.helper.iride.IrideServiceHelper;
import it.csi.risca.riscabesrv.business.be.impl.dao.TipoDilazioneDAO;
import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.dto.TipoDilazioneDTO;
import it.csi.risca.riscabesrv.identitadigitale.IdentitaDigitaleManager;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type Tipo Dilazione api service.
 *
 * @author CSI PIEMONTE
 */
@Component
public class TipoDilazioneApiServiceImpl implements TipoDilazioneApi {
	
	private static final String IDENTITY = "identity";
	private static Logger LOGGER = Logger.getLogger(Constants.LOGGER_NAME);

	@Autowired
	private TipoDilazioneDAO tipoDilazioneDAO;
	@Autowired
	private IrideServiceHelper serviceHelper;


	    /**
	     * @param idTipoDilazione   idTipoDilazione
	     * @param idAmbito			idAmbito
	     * @param securityContext   SecurityContext
	     * @param httpHeaders       HttpHeaders
	     * @param httpRequest       HttpServletRequest
	     * @return Response
	     */

		@Override
		public Response loadTipoDilazioneByIdAmbitoAndIdTipoDilazione(Long idAmbito, Long idTipoDilazione,
				SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
	        LOGGER.debug("[TipoDilazioneApiServiceImpl::loadTipiTitoloByIdAmbitoAndIdTipoDilazione] BEGIN");
	        LOGGER.debug("[TipoDilazioneApiServiceImpl::loadTipiTitoloByIdAmbitoAndIdTipoDilazione] Parametri in input idTipoDilazione [" + idTipoDilazione + "] e idAmbito [" + idAmbito + "]");
			Identita identita = IdentitaDigitaleManager.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders),null);

			Application cod = new Application();
			cod.setId("RISCA");
			UseCase use = new UseCase();
			use.setAppId(cod);
			use.setId("UC_SIPRA");
			Long idAmbitoSess = null;
			if (identita != null) {
				idAmbitoSess = serviceHelper.getInfoPersonaInUseCase(identita, use);
				LOGGER.debug("[TipoDilazioneApiServiceImpl::loadTipiTitoloByIdAmbitoAndIdTipoDilazione] idAmbitoSess:"+ idAmbitoSess);
				if (idAmbito != null && idAmbitoSess != null) {
					LOGGER.debug("[TipoDilazioneApiServiceImpl::loadTipiTitoloByIdAmbitoAndIdTipoDilazione] idAmbito:" + idAmbito);
					if (!idAmbito.equals(idAmbitoSess)) {
						ErrorDTO err = new ErrorDTO("401", "E100", "Ambito non consentito", null, null);
						return Response.serverError().entity(err).status(401).build();
					}
				}
			}
			List<TipoDilazioneDTO> tipoDilazione;
			try {
				tipoDilazione = tipoDilazioneDAO.loadTipoDilazioneByIdAmbitoAndIdTipoDilazione(idAmbito, idTipoDilazione);
			}catch (Exception e) {
				ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
				return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		
			}

	        LOGGER.debug("[TipoDilazioneApiServiceImpl::loadTipiTitoloByIdAmbitoAndIdTipoDilazione] END");
	        return Response.ok(tipoDilazione).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		}
		

	
}

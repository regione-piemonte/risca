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
import it.csi.risca.riscabesrv.business.be.TipiRiscossioneApi;
import it.csi.risca.riscabesrv.business.be.helper.iride.IrideServiceHelper;
import it.csi.risca.riscabesrv.business.be.impl.dao.TipiRiscossioneDAO;
import it.csi.risca.riscabesrv.dto.ErrorDTO;
//import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.dto.TipoRiscossioneDTO;
import it.csi.risca.riscabesrv.dto.TipoRiscossioneExtendedDTO;
import it.csi.risca.riscabesrv.identitadigitale.IdentitaDigitaleManager;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type Tipi riscossione api service.
 *
 * @author CSI PIEMONTE
 */
@Component
public class TipiRiscossioneApiServiceImpl implements TipiRiscossioneApi {
	
	private static final String IDENTITY = "identity";
	private static Logger LOGGER = Logger.getLogger(Constants.LOGGER_NAME);
	@Autowired
	private TipiRiscossioneDAO tipoRiscossioneDAO;
	@Autowired
	private IrideServiceHelper serviceHelper;
	    /**
	     * @param securityContext SecurityContext
	     * @param httpHeaders     HttpHeaders
	     * @param httpRequest     HttpServletRequest
	     * @return Response
	     */
	    @Override
	    public Response loadTipiRiscossione(SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
	        LOGGER.debug("[TipiRiscossioneApiServiceImpl::loadTipiRiscossione] BEGIN");
	        List<TipoRiscossioneExtendedDTO> listAdempimenti;
			try {
				listAdempimenti = tipoRiscossioneDAO.loadTipiRiscossione();
			} catch (Exception e) {
				return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
						.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
			}
	        LOGGER.debug("[TipiRiscossioneApiServiceImpl::loadTipiRiscossione] END");
	        return Response.ok(listAdempimenti).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	    }

	    /**
	     * @param idCompilante    idCompilante
	     * @param securityContext SecurityContext
	     * @param httpHeaders     HttpHeaders
	     * @param httpRequest     HttpServletRequest
	     * @return Response
	     */
	    @Override
	    public Response getTipiRiscossioneByIdAmbitoAndDateValidita(Long idAmbito, String dataIniVal, String dataFineVal, SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
	        LOGGER.debug("[TipiRiscossioneApiServiceImpl::getTipiRiscossioneByIdAmbitoAndDateValidita] BEGIN");
	        LOGGER.debug("[TipiRiscossioneApiServiceImpl::getTipiRiscossioneByIdAmbitoAndDateValidita] Parametro in input idAmbito [" + idAmbito + "] and dataIniVal  [" + dataIniVal + "] and dataFineVal  [" + dataFineVal + "] ");
			Identita identita = IdentitaDigitaleManager.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders),null);

			Application cod = new Application();
			cod.setId("RISCA");
			UseCase use = new UseCase();
			use.setAppId(cod);
			use.setId("UC_SIPRA");
			Long idAmbitoSess = null;
			if (identita != null) {
				idAmbitoSess = serviceHelper.getInfoPersonaInUseCase(identita, use);
				LOGGER.debug("[TipiRiscossioneApiServiceImpl::getTipiRiscossioneByIdAmbitoAndDateValidita] idAmbitoSess:"+ idAmbitoSess);
				if (idAmbito != null && idAmbitoSess != null) {
					LOGGER.debug("[TipiRiscossioneApiServiceImpl::getTipiRiscossioneByIdAmbitoAndDateValidita] idAmbito:" + idAmbito);
					if (!idAmbito.equals(idAmbitoSess)) {
						ErrorDTO err = new ErrorDTO("401", "E100", "Ambito non consentito", null, null);
						return Response.serverError().entity(err).status(401).build();
					}
				}
			}
	        List<TipoRiscossioneExtendedDTO> listRiscossioni;
			try {
				listRiscossioni = tipoRiscossioneDAO.loadTipiRiscossioneByIdAmbitoAndDateValidita(idAmbito, dataIniVal, dataFineVal );
			} catch (Exception e) {
				ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
				return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		
			}
	        LOGGER.debug("[TipiRiscossioneApiServiceImpl::loadTipiRiscossioneByIdAmbito] END");
	        return Response.ok(listRiscossioni).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	    }

	    /**
	     * @param idTipoRiscossione idTipoRiscossione
	     * @param securityContext   SecurityContext
	     * @param httpHeaders       HttpHeaders
	     * @param httpRequest       HttpServletRequest
	     * @return Response
	     */



		@Override
		public Response loadTipoRiscossioneByIdTipoRiscossioneAndIdAmbito(String idOrCodTipoRiscossione, Long idAmbito,
				SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
	        LOGGER.debug("[TipiRiscossioneApiServiceImpl::loadTipoRiscossioneByIdTipoRiscossioneAndIdAmbito] BEGIN");
	        LOGGER.debug("[TipiRiscossioneApiServiceImpl::loadTipoRiscossioneByIdTipoRiscossioneAndIdAmbito] Parametro in input idTipoRiscossione [" + idOrCodTipoRiscossione + "] e idAmbito [" + idAmbito + "]");
			Identita identita = IdentitaDigitaleManager.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders),null);

			Application cod = new Application();
			cod.setId("RISCA");
			UseCase use = new UseCase();
			use.setAppId(cod);
			use.setId("UC_SIPRA");
			Long idAmbitoSess = null;
			if (identita != null) {
				idAmbitoSess = serviceHelper.getInfoPersonaInUseCase(identita, use);
				LOGGER.debug("[TipiRiscossioneApiServiceImpl::loadTipoRiscossioneByIdTipoRiscossioneAndIdAmbito] idAmbitoSess:"+ idAmbitoSess);
				if (idAmbito != null && idAmbitoSess != null) {
					LOGGER.debug("[TipiRiscossioneApiServiceImpl::loadTipoRiscossioneByIdTipoRiscossioneAndIdAmbito] idAmbito:" + idAmbito);
					if (!idAmbito.equals(idAmbitoSess)) {
						ErrorDTO err = new ErrorDTO("401", "E100", "Ambito non consentito", null, null);
						return Response.serverError().entity(err).status(401).build();
					}
				}
			}
	        TipoRiscossioneDTO tipoRiscossione;
			try {
				tipoRiscossione = tipoRiscossioneDAO.loadTipoRiscossioneByIdTipoRiscossioneAndIdAmbito(idOrCodTipoRiscossione, idAmbito);
			} catch (Exception e) {
				ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
				return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		
			}
	        LOGGER.debug("[TipiRiscossioneApiServiceImpl::loadTipoRiscossione] END");
	        return Response.ok(tipoRiscossione).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		}

		@Override
		public Response loadTipoRiscossioneByCodeAndIdAmbito(String codTipoRiscossione, Long idAmbito,
				SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
	        LOGGER.debug("[TipiRiscossioneApiServiceImpl::loadTipoRiscossioneByCodeAndIdAmbito] BEGIN");
	        LOGGER.debug("[TipiRiscossioneApiServiceImpl::loadTipoRiscossioneByCodeAndIdAmbito] Parametri in input codTipoRiscossione [" + codTipoRiscossione + "] e idAmbito [" + idAmbito + "]");
			Identita identita = IdentitaDigitaleManager.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders),null);

			Application cod = new Application();
			cod.setId("RISCA");
			UseCase use = new UseCase();
			use.setAppId(cod);
			use.setId("UC_SIPRA");
			Long idAmbitoSess = null;
			if (identita != null) {
				idAmbitoSess = serviceHelper.getInfoPersonaInUseCase(identita, use);
				LOGGER.debug("[TipiRiscossioneApiServiceImpl::loadTipoRiscossioneByCodeAndIdAmbito] idAmbitoSess:"+ idAmbitoSess);
				if (idAmbito != null && idAmbitoSess != null) {
					LOGGER.debug("[TipiRiscossioneApiServiceImpl::loadTipoRiscossioneByCodeAndIdAmbito] idAmbito:" + idAmbito);
					if (!idAmbito.equals(idAmbitoSess)) {
						ErrorDTO err = new ErrorDTO("401", "E100", "Ambito non consentito", null, null);
						return Response.serverError().entity(err).status(401).build();
					}
				}
			}
	        TipoRiscossioneDTO tipoRiscossione;
			try {
				tipoRiscossione = tipoRiscossioneDAO.loadTipoRiscossioneByCodeAndIdAmbito(codTipoRiscossione, idAmbito);
			} catch (Exception e) {
				ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
				return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		
			}
	        LOGGER.debug("[TipiRiscossioneApiServiceImpl::loadTipoRiscossioneByCodeAndIdAmbito] END");
	        return Response.ok(tipoRiscossione).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		}

	
}

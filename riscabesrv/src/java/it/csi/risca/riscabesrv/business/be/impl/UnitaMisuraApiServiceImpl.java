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
import it.csi.risca.riscabesrv.business.be.UnitaMisuraApi;
import it.csi.risca.riscabesrv.business.be.helper.iride.IrideServiceHelper;
import it.csi.risca.riscabesrv.business.be.impl.dao.UnitaMisuraDAO;
import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.dto.UnitaMisuraDTO;
import it.csi.risca.riscabesrv.identitadigitale.IdentitaDigitaleManager;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type Unita Misura api service.
 *
 * @author CSI PIEMONTE
 */
@Component
public class UnitaMisuraApiServiceImpl implements UnitaMisuraApi {

	private static final String IDENTITY = "identity";
	private static Logger LOGGER = Logger.getLogger(Constants.LOGGER_NAME);

	@Autowired
	private UnitaMisuraDAO unitaMisuraDAO;
	@Autowired
	private IrideServiceHelper serviceHelper;

	    /**
	     * @param securityContext SecurityContext
	     * @param httpHeaders     HttpHeaders
	     * @param httpRequest     HttpServletRequest
	     * @return Response
	     */
		@Override
		public Response loadUnitaMisura(SecurityContext securityContext, HttpHeaders httpHeaders,
				HttpServletRequest httpRequest) {
	        LOGGER.debug("[UnitaMisuraApiServiceImpl::loadUnitaMisura] BEGIN");
	        List<UnitaMisuraDTO> listUnitaMisura;
			try {
				listUnitaMisura =  unitaMisuraDAO.loadUnitaMisura();
			} catch (Exception e) {
				ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
				return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		
			}
	        return Response.ok(listUnitaMisura).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		}

	    /**
	     * @param idAmbito idAmbito
	     * @param securityContext SecurityContext
	     * @param httpHeaders     HttpHeaders
	     * @param httpRequest     HttpServletRequest
	     * @return Response
	     */
		@Override
		public Response loadUnitaMisuraByIdAmbito(Long idAmbito, SecurityContext securityContext, HttpHeaders httpHeaders,
				HttpServletRequest httpRequest) {
	        LOGGER.debug("[UnitaMisuraApiServiceImpl::loadUnitaMisuraByIdAmbito] BEGIN");
	        LOGGER.debug("[UnitaMisuraApiServiceImpl::loadUnitaMisuraByIdAmbito] Parametro in input idAmbito [" + idAmbito + "]");
			Identita identita = IdentitaDigitaleManager.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders),null);

			Application cod = new Application();
			cod.setId("RISCA");
			UseCase use = new UseCase();
			use.setAppId(cod);
			use.setId("UC_SIPRA");
			Long idAmbitoSess = null;
			if (identita != null) {
				idAmbitoSess = serviceHelper.getInfoPersonaInUseCase(identita, use);
				LOGGER.debug("[UnitaMisuraApiServiceImpl::loadUnitaMisuraByIdAmbito] idAmbitoSess:"+ idAmbitoSess);
				if (idAmbito != null && idAmbitoSess != null) {
					LOGGER.debug("[UnitaMisuraApiServiceImpl::loadUnitaMisuraByIdAmbito] idAmbito:" + idAmbito);
					if (!idAmbito.equals(idAmbitoSess)) {
						ErrorDTO err = new ErrorDTO("401", "E100", "Ambito non consentito", null, null);
						return Response.serverError().entity(err).status(401).build();
					}
				}
			}
	        List<UnitaMisuraDTO> listUnitaMisura;
			try {
				listUnitaMisura = unitaMisuraDAO.loadUnitaMisuraByIdAmbito(idAmbito);
			} catch (Exception e) {
				ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
				return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		
			}
	        LOGGER.debug("[UnitaMisuraApiServiceImpl::loadUnitaMisuraByIdAmbito] END");
	        return Response.ok(listUnitaMisura).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		}
		
	    /**
	     * @param keyUnitaMisura keyUnitaMisura
	     * @param securityContext   SecurityContext
	     * @param httpHeaders       HttpHeaders
	     * @param httpRequest       HttpServletRequest
	     * @return Response
	     */
		@Override
		public Response loadUnitaMisuraByKeyUnitaMisura(String keyUnitaMisura,SecurityContext securityContext,
				HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
	        LOGGER.debug("[UnitaMisuraApiServiceImpl::loadUnitaMisuraByKeyUnitaMisura] BEGIN");
	        LOGGER.debug("[UnitaMisuraApiServiceImpl::loadUnitaMisuraByKeyUnitaMisura] Parametro in input keyUnitaMisura [" + keyUnitaMisura + "]");
	        UnitaMisuraDTO UnitaMisura;
			try {
				UnitaMisura = unitaMisuraDAO.loadUnitaMisuraByKeyUnitaMisura(keyUnitaMisura);
			} catch (Exception e) {
				ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
				return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		
			}
	        LOGGER.debug("[UnitaMisuraApiServiceImpl::loadUnitaMisuraByKeyUnitaMisura] END");
	        return Response.ok(UnitaMisura).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		}

	    /**
	     * @param idUnitaMisura idUnitaMisura
	     * @param securityContext   SecurityContext
	     * @param httpHeaders       HttpHeaders
	     * @param httpRequest       HttpServletRequest
	     * @return Response
	     */
		@Override
		public Response loadUnitaMisuraByIdUnitaMisura(String idUnitaMisura, SecurityContext securityContext, HttpHeaders httpHeaders,
				HttpServletRequest httpRequest) {
	        LOGGER.debug("[UnitaMisuraApiServiceImpl::loadUnitaMisuraByIdUnitaMisura] BEGIN");
	        LOGGER.debug("[UnitaMisuraApiServiceImpl::loadUnitaMisuraByIdUnitaMisura] Parametro in input idUnitaMisuraPadre [" + idUnitaMisura + "]");
	        UnitaMisuraDTO UnitaMisura;
			try {
				UnitaMisura = unitaMisuraDAO.loadUnitaMisuraByIdUnitaMisura(idUnitaMisura);
			}catch (Exception e) {
				ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
				return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		
			}
	        LOGGER.debug("[UnitaMisuraApiServiceImpl::loadUnitaMisuraByIdUnitaMisura] END");
	        return Response.ok(UnitaMisura).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		}

	
}

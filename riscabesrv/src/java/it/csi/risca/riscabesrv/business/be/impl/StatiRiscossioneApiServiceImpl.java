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
import it.csi.risca.riscabesrv.business.be.StatiRiscossioneApi;
import it.csi.risca.riscabesrv.business.be.helper.iride.IrideServiceHelper;
import it.csi.risca.riscabesrv.business.be.impl.dao.StatiRiscossioneDAO;
import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.dto.StatiRiscossioneExtendedDTO;
import it.csi.risca.riscabesrv.identitadigitale.IdentitaDigitaleManager;
import it.csi.risca.riscabesrv.util.Constants;
import it.csi.risca.riscabesrv.util.Utils;

@Component
public class StatiRiscossioneApiServiceImpl implements StatiRiscossioneApi {
	
	private static Logger LOGGER = Logger.getLogger(Constants.LOGGER_NAME);
	private static final String IDENTITY = "identity";

	@Autowired
	private StatiRiscossioneDAO statiRiscossioneDAO;

	@Autowired
	private IrideServiceHelper serviceHelper;
	    /**
	     * @param securityContext SecurityContext
	     * @param httpHeaders     HttpHeaders
	     * @param httpRequest     HttpServletRequest
	     * @return Response
	     */
	    @Override
	    public Response loadStatiRiscossione(SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
	        LOGGER.debug("[StatiRiscossioneApiServiceImpl::loadStatiRiscossione] BEGIN");
	        List<StatiRiscossioneExtendedDTO> listRiscossioni;
			try {
				listRiscossioni = statiRiscossioneDAO.loadStatiRiscossione();
			} catch (Exception e) {
				return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
						.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
			}
	        LOGGER.debug("[StatiRiscossioneApiServiceImpl::loadStatiRiscossione] END");
	        return Response.ok(listRiscossioni).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	    }

	    /**
	     * @param idCompilante    idCompilante
	     * @param securityContext SecurityContext
	     * @param httpHeaders     HttpHeaders
	     * @param httpRequest     HttpServletRequest
	     * @return Response
	     */
	    @Override
	    public Response loadStatiRiscossioneByIdAmbito(Long idAmbito, SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
	        LOGGER.debug("[StatiRiscossioneApiServiceImpl::loadStatiRiscossioneByIdAmbito] BEGIN");
	        LOGGER.debug("[StatiRiscossioneApiServiceImpl::loadStatiRiscossioneByIdAmbito] Parametro in input idAmbito [" + idAmbito + "]");
			Identita identita = IdentitaDigitaleManager.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders),null);

			Application cod = new Application();
			cod.setId("RISCA");
			UseCase use = new UseCase();
			use.setAppId(cod);
			use.setId("UC_SIPRA");
			Long idAmbitoSess = null;
			if (identita != null) {
				idAmbitoSess = serviceHelper.getInfoPersonaInUseCase(identita, use);
				LOGGER.debug("[StatiRiscossioneApiServiceImpl::loadStatiRiscossioneByIdAmbito] idAmbitoSess:"+ idAmbitoSess);
				if (idAmbito != null && idAmbitoSess != null) {
					LOGGER.debug("[StatiRiscossioneApiServiceImpl::loadStatiRiscossioneByIdAmbito] idAmbito:" + idAmbito);
					if (!idAmbito.equals(idAmbitoSess)) {
						ErrorDTO err = new ErrorDTO("401", "E100", "Ambito non consentito", null, null);
						return Response.serverError().entity(err).status(401).build();
					}
				}
			}
	        List<StatiRiscossioneExtendedDTO> listRiscossioni;
			try {
				listRiscossioni = statiRiscossioneDAO.loadStatiRiscossioneByIdAmbito(idAmbito);
			} catch (Exception e) {
				return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
						.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
			}
	        LOGGER.debug("[StatiRiscossioneApiServiceImpl::loadStatiRiscossioneByIdAmbito] END");
	        return Response.ok(listRiscossioni).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	    }

}

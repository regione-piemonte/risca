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

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.iride2.policy.entity.Identita;
import it.csi.risca.riscabesrv.business.be.CalcoloInteressiApi;
import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.impl.dao.CalcoloInteresseDAO;
import it.csi.risca.riscabesrv.identitadigitale.IdentitaDigitaleManager;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type Calcolo Interessi api service.
 *
 * @author CSI PIEMONTE
 */
@Component
public class CalcoloInteressiApiServiceImpl extends BaseApiServiceImpl implements CalcoloInteressiApi {
	
	private static final String IDENTITY = "identity";
    private static Logger LOGGER = Logger.getLogger(Constants.LOGGER_NAME);
    

    @Autowired
    private CalcoloInteresseDAO calcoloInteresseDAO;
	    
    @Autowired
    private IdentitaDigitaleManager identitaDigitaleManager; 
    
	@Override
	public Response calcoloInteressi(String fruitore, BigDecimal importo, String dataScadenza, String dataVersamento,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		    LOGGER.debug("[CalcoloInteressiApiServiceImpl::calcoloInteressi] BEGIN");
	        BigDecimal importoCanoneDovuto = null;
			Identita identita = null;

			Long idAmbito = null;
			try {	
			    LOGGER.debug("[CalcoloInteressiApiServiceImpl::calcoloInteressi] verificaIdentitaDigitale BEGIN");
				identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null,httpHeaders, Constants.LOAD_CAL_INTERESSI); 
			    LOGGER.debug("[CalcoloInteressiApiServiceImpl::calcoloInteressi] verificaIdentitaDigitale END");
			    LOGGER.debug("[CalcoloInteressiApiServiceImpl::calcoloInteressi] IdentitaDigitaleManager BEGIN");
				identita = IdentitaDigitaleManager.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders),fruitore);
			    LOGGER.debug("[CalcoloInteressiApiServiceImpl::calcoloInteressi] IdentitaDigitaleManager END");
			    LOGGER.debug("[CalcoloInteressiApiServiceImpl::calcoloInteressi] getAmbitoByIdentitaOrFonte BEGIN");
				idAmbito = identitaDigitaleManager.getAmbitoByIdentitaOrFonte(identita, fruitore, null);
			    LOGGER.debug("[CalcoloInteressiApiServiceImpl::calcoloInteressi] getAmbitoByIdentitaOrFonte END");
			    LOGGER.debug("[CalcoloInteressiApiServiceImpl::calcoloInteressi] calcoloInteressi BEGIN");
			    importoCanoneDovuto = calcoloInteresseDAO.calcoloInteressi(idAmbito, importo, dataScadenza, dataVersamento);
			    LOGGER.debug("[CalcoloInteressiApiServiceImpl::calcoloInteressi] calcoloInteressi END");
			}
			catch (BusinessException be) {
				return handleBusinessException(be.getHttpStatus(), be);
			} catch (Exception e) {
				return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR).status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();

			} 
	        LOGGER.debug("[CalcoloInteressiApiServiceImpl::calcoloCanone] END");
	        return Response.ok(importoCanoneDovuto).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

}

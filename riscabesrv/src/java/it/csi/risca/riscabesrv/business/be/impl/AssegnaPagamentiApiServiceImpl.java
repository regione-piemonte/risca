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

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.csi.iride2.policy.entity.Identita;
import it.csi.risca.riscabesrv.business.be.AssegnaPagamentiApi;
import it.csi.risca.riscabesrv.business.be.BusinessLogic;
import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.exception.ValidationException;
import it.csi.risca.riscabesrv.dto.AssegnaPagamentoDTO;
import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.identitadigitale.IdentitaDigitaleManager;
import it.csi.risca.riscabesrv.tracciamento.TracciamentoManager;
import it.csi.risca.riscabesrv.util.Constants;
import it.csi.risca.riscabesrv.util.Utils;

@Service
public class AssegnaPagamentiApiServiceImpl extends BaseApiServiceImpl implements AssegnaPagamentiApi {

	private static final String IDENTITY = "identity";

	@Autowired
	public BusinessLogic businessLogic;
	
	@Autowired
	private IdentitaDigitaleManager identitaDigitaleManager;

	@Override
	public Response AssegnaPagamentiPost(String fruitore, AssegnaPagamentoDTO assegnaPagamento, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[AssegnaPagamentiApiServiceImpl::AssegnaPagamentiPost] BEGIN");
		try {
			LOGGER.debug("[AssegnaPagamentiApiServiceImpl::AssegnaPagamentiPost] verificaIdentitaDigitale BEGIN");
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders,Constants.POST_PUT_DEL_DET_PAG);
			LOGGER.debug("[AssegnaPagamentiApiServiceImpl::AssegnaPagamentiPost] verificaIdentitaDigitale END");
			Identita identita = IdentitaDigitaleManager.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders), fruitore);
			Long idAmbito = identitaDigitaleManager.getAmbitoByIdentitaOrFonte(identita, fruitore, null);
			LOGGER.debug("[AssegnaPagamentiApiServiceImpl::AssegnaPagamentiPost] verifyAssegnaPagamenti BEGIN");
			businessLogic.verifyAssegnaPagamenti(assegnaPagamento);
			LOGGER.debug("[AssegnaPagamentiApiServiceImpl::AssegnaPagamentiPost] verifyAssegnaPagamenti END");
			LOGGER.debug("[AssegnaPagamentiApiServiceImpl::AssegnaPagamentiPost] assegnaPagamentiPost BEGIN");
			businessLogic.assegnaPagamentiPost(fruitore, assegnaPagamento, idAmbito, httpHeaders, httpRequest);
			LOGGER.debug("[AssegnaPagamentiApiServiceImpl::AssegnaPagamentiPost] assegnaPagamentiPost END");
			if(Utils.isNotEmpty(assegnaPagamento.getStatiDebitori())) {
				businessLogic.aggiornaStatoContribuzione(assegnaPagamento.getStatiDebitori(), idAmbito, assegnaPagamento.getPagamento().getGestAttoreIns(), null);
			}
			LOGGER.debug("[AssegnaPagamentiApiServiceImpl::AssegnaPagamentiPost] END");
			return Response.ok(assegnaPagamento).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();

		} catch (ValidationException  ve) {
			LOGGER.debug("BAD REQUEST: MESSAGE:"+ve.getMessage());
			Integer status = Response.Status.BAD_REQUEST.getStatusCode();
			ErrorDTO err = new ErrorDTO(status.toString(), Constants.E003,ve.getMessage(), null, null);
		    return Response.status(Response.Status.BAD_REQUEST).entity(err).build();
        }  catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);
		} catch (Exception e) {
			LOGGER.error("[AssegnaPagamentiApiServiceImpl::AssegnaPagamentiPost] ERROR " , e);
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();

		}
		
		
	}

	
}

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

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.risca.riscabesrv.business.be.AnnualitaSdApi;
import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.exception.SystemException;
import it.csi.risca.riscabesrv.business.be.impl.dao.AnnualitaSdDAO;
import it.csi.risca.riscabesrv.dto.AnnualitaSdDTO;
import it.csi.risca.riscabesrv.dto.DTGrandeIdroDTO;
import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.identitadigitale.IdentitaDigitaleManager;
import it.csi.risca.riscabesrv.util.Constants;

@Component
public class AnnualitaSdApiServiceImpl extends BaseApiServiceImpl implements AnnualitaSdApi {

	private final String className = this.getClass().getSimpleName();
	private static final String IDENTITY = "identity";

	@Autowired
	private AnnualitaSdDAO annualitaSdDAO;

	@Autowired
	private IdentitaDigitaleManager identitaDigitaleManager;

	@Override
	public Response loadAnnualitaSd(String fruitore, Long idStatoDebitorio, Boolean speciali, Boolean intere,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[AnnualitaSdApiServiceImpl::loadAnnualitaSd]  BEGIN");
		List<AnnualitaSdDTO> listAnnualita = null;
		try {
			LOGGER.debug("[AnnualitaSdApiServiceImpl::loadAnnualitaSd] verificaIdentitaDigitale BEGIN");
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders, Constants.LOAD_ANNUA);
			LOGGER.debug("[AnnualitaSdApiServiceImpl::loadAnnualitaSd] verificaIdentitaDigitale END");
			listAnnualita = annualitaSdDAO.loadAnnualitaSd(idStatoDebitorio, speciali, intere);
		} catch (SQLException e) {
			ErrorDTO err = new ErrorDTO("500", "E005",
					"Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
			return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		} catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);
		} catch (SystemException e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
		}

		LOGGER.debug("[AnnualitaSdApiServiceImpl::loadAnnualitaSd]  END");
		return Response.ok(listAnnualita).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response getDatiTecniciGrandeIdro(String fruitore, Long idStatoDebitorio, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[AnnualitaSdApiServiceImpl::getDatiTecniciGrandeIdro]  BEGIN");
		AnnualitaSdDTO annualitaSd = null;
		try {
			LOGGER.debug("[AnnualitaSdApiServiceImpl::getDatiTecniciGrandeIdro] verificaIdentitaDigitale BEGIN");
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders, Constants.LOAD_ANNUA);
			LOGGER.debug("[AnnualitaSdApiServiceImpl::getDatiTecniciGrandeIdro] verificaIdentitaDigitale END");
			annualitaSd = annualitaSdDAO.loadAnnualitaSdGrandeIdro(idStatoDebitorio);
		} catch (SQLException e) {
			ErrorDTO err = new ErrorDTO("500", "E005",
					"Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
			return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		} catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);
		} catch (SystemException e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
		}

		LOGGER.debug("[AnnualitaSdApiServiceImpl::getDatiTecniciGrandeIdro]  END");
		return Response.ok(annualitaSd).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response updateDatiTecniciGrandeIdro(String fruitore, Integer idAnnualitaSd, DTGrandeIdroDTO DTGrandeIdroDTO,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws Exception {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		AnnualitaSdDTO annualitaSdDTO = null;
		try {
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders, Constants.LOAD_ANNUA);

			annualitaSdDTO = annualitaSdDAO.updateDatiTecniciGrandeIdro(idAnnualitaSd, DTGrandeIdroDTO);

		} catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);
		} catch (Exception e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}

		return Response.ok(annualitaSdDTO).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

}

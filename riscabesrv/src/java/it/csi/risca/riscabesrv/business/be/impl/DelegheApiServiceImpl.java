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

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import it.csi.iride2.policy.entity.Identita;
import it.csi.risca.riscabesrv.business.be.BusinessLogic;
import it.csi.risca.riscabesrv.business.be.DelegheApi;
import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.exception.DatiInputErratiException;
import it.csi.risca.riscabesrv.business.be.exception.GenericExceptionList;
import it.csi.risca.riscabesrv.business.be.exception.SystemException;
import it.csi.risca.riscabesrv.business.be.impl.dao.DelegheDAO;
import it.csi.risca.riscabesrv.dto.CsiLogAuditDTO;
import it.csi.risca.riscabesrv.dto.DelegatoDTO;
import it.csi.risca.riscabesrv.dto.DelegatoExtendedDTO;
import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.dto.LottoDTO;
import it.csi.risca.riscabesrv.dto.TracciamentoDTO;
import it.csi.risca.riscabesrv.identitadigitale.IdentitaDigitaleManager;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The DelegheApiServiceImpl.
 *
 * @author CSI PIEMONTE
 */
@Component
public class DelegheApiServiceImpl extends BaseApiServiceImpl implements DelegheApi {

	private static final String IDENTITY = "identity";
	private final String className = this.getClass().getSimpleName();

	@Autowired
	private IdentitaDigitaleManager identitaDigitaleManager;
	
	@Autowired
	private DelegheDAO delegheDAO; 

	@Autowired
	public BusinessLogic businessLogic;
	
	@Override
	public Response loadDelegheByCf(String fruitore, String codiceFiscale, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		DelegatoDTO delegato = null;
		try {
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders, null);
            delegato = delegheDAO.loadDelegheByCf(codiceFiscale);
		} catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);
		} catch (Exception e) {
			LOGGER.error(getClassFunctionErrorInfo(className, methodName, e),e);
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return Response.ok(delegato).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response saveDelegato(DelegatoExtendedDTO delegato, String origCf, String fruitore,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest)
			throws GenericExceptionList, ParseException {
		LOGGER.debug("[DelegheApiServiceImpl::saveDelegato] BEGIN");
		LOGGER.debug("[DelegheApiServiceImpl::saveDelegato] Parametro in input delegato :\n " + delegato);
		LOGGER.debug("[DelegheApiServiceImpl::saveDelegato] Parametro in input origCf :\n " + origCf);
		DelegatoExtendedDTO delegatoResult = null;
		try {
			setGestAttoreInsUpd(delegato, fruitore, httpRequest, httpHeaders);
			LOGGER.debug("[DelegheApiServiceImpl::saveDelegato] verificaIdentitaDigitale BEGIN");
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null,httpHeaders, Constants.POST_DELEGATO);  
			LOGGER.debug("[DelegheApiServiceImpl::saveDelegato] verificaIdentitaDigitale END");

			try {
				//delegato.validate(fruitore);
				delegatoResult = businessLogic.createDelegato(delegato, origCf);

			} 
			catch (NotFoundException e) {
				LOGGER.error(e);
				return Response.serverError().entity(Response.Status.NOT_FOUND).status(Response.Status.NOT_FOUND.getStatusCode()).build();
			}
			catch (DatiInputErratiException e) {
				LOGGER.debug("BAD REQUEST: CODE:"+e.getError().getCode()+"MESSAGE:"+e.getError().getTitle());
				Integer status = Response.Status.BAD_REQUEST.getStatusCode();
				ErrorDTO err = new ErrorDTO(status.toString(), e.getError().getCode(), e.getError().getTitle(), null, null);
				return Response.serverError().entity(err).status(status).build();
			}
			catch (Exception e) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				LOGGER.error("[DelegheApiServiceImpl::saveDelegato:: EXCEPTION ]: "+e);
				return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR).status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();

			}
		} 		
		catch (SystemException e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR).status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
		} catch (Exception e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR).status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
		} 
		finally {
			LOGGER.debug("[DelegheApiServiceImpl::saveDelegato]  save ");	
		}

		return Response.ok(delegatoResult).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

}

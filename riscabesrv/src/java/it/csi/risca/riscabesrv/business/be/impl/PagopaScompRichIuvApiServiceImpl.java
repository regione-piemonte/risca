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

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import it.csi.iride2.policy.entity.Identita;
import it.csi.risca.riscabesrv.business.be.BusinessLogic;
import it.csi.risca.riscabesrv.business.be.PagopaScompRichIuvApi;
import it.csi.risca.riscabesrv.business.be.exception.DatiInputErratiException;
import it.csi.risca.riscabesrv.business.be.exception.GenericExceptionList;
import it.csi.risca.riscabesrv.business.be.exception.SystemException;
import it.csi.risca.riscabesrv.business.be.exception.ValidationException;
import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.dto.PagopaScompRichIuvDTO;
import it.csi.risca.riscabesrv.identitadigitale.IdentitaDigitaleManager;
import it.csi.risca.riscabesrv.tracciamento.TracciamentoManager;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type PagopaScompRichIuv api service.
 *
 * 
 */
@Component
public class PagopaScompRichIuvApiServiceImpl  extends BaseApiServiceImpl implements PagopaScompRichIuvApi {

	private static final String IDENTITY = "identity";
	private static Logger LOGGER = Logger.getLogger(Constants.LOGGER_NAME);

	@Autowired
	private IdentitaDigitaleManager identitaDigitaleManager; 

    @Autowired
    private TracciamentoManager tracciamentoManager;

	@Autowired
	public BusinessLogic businessLogic;	

	@Override
	public Response savePagopaScompRichIuv(PagopaScompRichIuvDTO pagopaScompRichIuv, String fruitore,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest)
			throws GenericExceptionList, ParseException {
		LOGGER.debug("[PagopaScompRichIuvApiServiceImpl::savePagopaScompRichIuv] BEGIN");
		LOGGER.debug("[PagopaScompRichIuvApiServiceImpl::savePagopaScompRichIuv] Parametro in input pagopaScompRichIuv :\n " + pagopaScompRichIuv);
		PagopaScompRichIuvDTO pagopaScompRichIuvResult = null;
		try {
			setGestAttoreInsUpd(pagopaScompRichIuv, fruitore, httpRequest, httpHeaders);
			LOGGER.debug("[PagopaScompRichIuvApiServiceImpl::savePagopaScompRichIuv] verificaIdentitaDigitale BEGIN");
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null,httpHeaders, Constants.POST_PAGOPA_SCOMP_RICH_IUV);  
			LOGGER.debug("[PagopaScompRichIuvApiServiceImpl::savePagopaScompRichIuv] verificaIdentitaDigitale END");
			businessLogic.validatorDTO(pagopaScompRichIuv, null, null);

			try {
				pagopaScompRichIuv.validate(fruitore);
				pagopaScompRichIuvResult = businessLogic.createPagopaScompRichIuv(pagopaScompRichIuv);

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
				LOGGER.error("[PagopaScompRichIuvApiServiceImpl::savePagopaScompRichIuv:: EXCEPTION ]: "+e);
				return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR).status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();

			}
		} catch (ValidationException ve) {
			LOGGER.debug("BAD REQUEST: MESSAGE:" + ve.getMessage());
			Integer status = Response.Status.BAD_REQUEST.getStatusCode();
			ErrorDTO err = new ErrorDTO(status.toString(), Constants.E003, ve.getMessage(), null, null);
			return Response.status(Response.Status.BAD_REQUEST).entity(err).build();
		} 	
		catch (SystemException e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR).status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
		} catch (Exception e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR).status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
		} 
		finally {			
		}

		return Response.ok(pagopaScompRichIuvResult).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

}

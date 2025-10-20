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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import it.csi.iride2.policy.entity.Identita;
import it.csi.risca.riscabesrv.business.be.BusinessLogic;
import it.csi.risca.riscabesrv.business.be.IuvApi;
import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.exception.DatiInputErratiException;
import it.csi.risca.riscabesrv.business.be.exception.GenericExceptionList;
import it.csi.risca.riscabesrv.business.be.exception.SystemException;
import it.csi.risca.riscabesrv.business.be.exception.ValidationException;
import it.csi.risca.riscabesrv.business.be.impl.dao.IuvDAO;
import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.dto.IuvDTO;
import it.csi.risca.riscabesrv.identitadigitale.IdentitaDigitaleManager;
import it.csi.risca.riscabesrv.tracciamento.TracciamentoManager;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type IUV api service.
 *
 * @author CSI PIEMONTE
 */
@Component
public class IuvApiServiceImpl  extends BaseApiServiceImpl implements IuvApi {

	private static final String IDENTITY = "identity";
	private static Logger LOGGER = Logger.getLogger(Constants.LOGGER_NAME);

	@Autowired
	private IuvDAO iuvDAO;

	@Autowired
	private IdentitaDigitaleManager identitaDigitaleManager; 



	@Autowired
	public BusinessLogic businessLogic;

    @Autowired
    private TracciamentoManager tracciamentoManager;

	/**
	 * @param securityContext SecurityContext
	 * @param httpHeaders     HttpHeaders
	 * @param httpRequest     HttpServletRequest
	 * @return Response
	 */


	@Override
	public Response getIuvByNap(String fruitore, String nap, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		LOGGER.debug("[IuvApiServiceImpl::getIuvByNap] BEGIN");
		IuvDTO iuv = new IuvDTO();
		try {
			LOGGER.debug("[IuvApiServiceImpl::getIuvByNap] verificaIdentitaDigitale BEGIN");
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore,null,
					httpHeaders, Constants.LOAD_IUV); 
			LOGGER.debug("[IuvApiServiceImpl::getIuvByNap] verificaIdentitaDigitale END");

			iuv = iuvDAO.getIuvByNap(nap);
		}
		catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);
		}catch (Exception e) {
			ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
			return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();

		}
		LOGGER.debug("[IuvApiServiceImpl::getIuvByNap] END");
		return Response.ok(iuv).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	@Transactional
	public Response saveIuv(IuvDTO iuv, String fruitore ,SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest)  throws GenericExceptionList, ParseException, BusinessException{
		LOGGER.debug("[IuvApiServiceImpl::saveIuv] BEGIN");
		LOGGER.debug("[IuvApiServiceImpl::saveIuv] Parametro in input iuv :\n " + iuv);
		IuvDTO iuvResult = null;
		try {
			setGestAttoreInsUpd(iuv, fruitore, httpRequest, httpHeaders);

			LOGGER.debug("[IuvApiServiceImpl::saveIuv] verificaIdentitaDigitale BEGIN");
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null,httpHeaders, Constants.POST_IUV);  
			LOGGER.debug("[IuvApiServiceImpl::saveIuv] verificaIdentitaDigitale END");
			businessLogic.validatorDTO(iuv, null, null);
			try {
				iuv.validate(fruitore);
				iuvResult = businessLogic.createIuv(iuv);

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
				LOGGER.error("[IuvApiServiceImpl::saveIuv:: EXCEPTION ]: ",e);
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

		return Response.ok(iuvResult).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

}

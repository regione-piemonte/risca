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
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import it.csi.risca.riscabesrv.business.be.BusinessLogic;
import it.csi.risca.riscabesrv.business.be.LottiApi;
import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.exception.DatiInputErratiException;
import it.csi.risca.riscabesrv.business.be.exception.GenericExceptionList;
import it.csi.risca.riscabesrv.business.be.exception.SystemException;
import it.csi.risca.riscabesrv.business.be.exception.ValidationException;
import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.dto.LottoDTO;
import it.csi.risca.riscabesrv.identitadigitale.IdentitaDigitaleManager;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type Lotti api service.
 *
 * 
 */
@Component
public class LottiApiServiceImpl  extends BaseApiServiceImpl implements LottiApi {

	private static final String IDENTITY = "identity";
	private static Logger LOGGER = Logger.getLogger(Constants.LOGGER_NAME);

	@Autowired
	private IdentitaDigitaleManager identitaDigitaleManager; 



	@Autowired
	public BusinessLogic businessLogic;

	@Override
	public Response saveLotto(LottoDTO lotto, String fruitore, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest)  throws GenericExceptionList, ParseException, BusinessException{
		LOGGER.debug("[LottiApiServiceImpl::saveLotto] BEGIN");
		LOGGER.debug("[LottiApiServiceImpl::saveLotto] Parametro in input lotto :\n " + lotto);
		LottoDTO lottoResult = null;
		try {
			setGestAttoreInsUpd(lotto, fruitore, httpRequest, httpHeaders);
			LOGGER.debug("[LottiApiServiceImpl::saveLotto] verificaIdentitaDigitale BEGIN");
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null,httpHeaders, Constants.POST_LOTTO);  
			LOGGER.debug("[LottiApiServiceImpl::saveLotto] verificaIdentitaDigitale END");
			businessLogic.validatorDTO(lotto, null, null);

			try {
				lotto.validate(fruitore);
				lottoResult = businessLogic.createLotto(lotto);

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
				LOGGER.error("[LottiApiServiceImpl::saveLotto:: EXCEPTION ]: "+e);
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

		return Response.ok(lottoResult).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response loadLottiByIdElabora(Long idElabora, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		List<LottoDTO> listLotti;
			try {
				listLotti = businessLogic.loadLottiByIdElabora(idElabora);
			}  catch (SystemException e) {
				LOGGER.debug("[LottiApiServiceImpl::loadLottiByIdElabora] SystemException - END");
				return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR).status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
			} catch (DAOException e) {
				LOGGER.debug("[LottiApiServiceImpl::loadLottiByIdElabora] ERROR:DAOException - END");
				ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
				return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
			}
	        catch (BusinessException be) {
	        	LOGGER.debug("[LottiApiServiceImpl::loadLottiByIdElabora] BusinessException - END");
				return handleBusinessException(be.getHttpStatus(), be);
			} 
	
		
		return Response.ok(listLotti).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

}

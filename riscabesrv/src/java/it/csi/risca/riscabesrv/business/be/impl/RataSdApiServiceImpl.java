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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.iride2.policy.entity.Identita;
import it.csi.risca.riscabesrv.business.be.BusinessLogic;
import it.csi.risca.riscabesrv.business.be.RataSdApi;
import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.exception.DatiInputErratiException;
import it.csi.risca.riscabesrv.business.be.exception.GenericExceptionList;
import it.csi.risca.riscabesrv.business.be.exception.SystemException;
import it.csi.risca.riscabesrv.business.be.exception.ValidationException;
import it.csi.risca.riscabesrv.business.be.impl.dao.RataSdDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.StatoDebitorioDAO;
import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.dto.RataSdDTO;
import it.csi.risca.riscabesrv.dto.StatoDebitorioExtendedDTO;
import it.csi.risca.riscabesrv.identitadigitale.IdentitaDigitaleManager;
import it.csi.risca.riscabesrv.util.Constants;
import it.csi.risca.riscabesrv.util.ErrorMessages;


@Component
public class RataSdApiServiceImpl extends BaseApiServiceImpl implements RataSdApi {

	private static final String IDENTITY = "identity";

	@Autowired
	private RataSdDAO rataSdDAO;
	
    
    
    @Autowired
    private IdentitaDigitaleManager identitaDigitaleManager; 
    

    @Autowired
	public BusinessLogic businessLogic;

	@Autowired
	private StatoDebitorioDAO statoDebitorioDAO;
    

	@Override
	public Response loadRataSdByStatoDebitorio(String fruitore, Long idStatoDebitorio, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
	    LOGGER.debug("[RataSdApiServiceImpl::loadRataSdByStatoDebitorio] BEGIN");
       try {
		    LOGGER.debug("[RataSdApiServiceImpl::loadRataSdByStatoDebitorio] verificaIdentitaDigitale BEGIN");
		     identitaDigitaleManager.verificaIdentitaDigitale(fruitore,null,
					httpHeaders, Constants.LOAD_RATA); 
		    LOGGER.debug("[RataSdApiServiceImpl::loadRataSdByStatoDebitorio] verificaIdentitaDigitale END");
		}
		catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);

		} catch (Exception e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR).status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();

		} 

		RataSdDTO rataSd = rataSdDAO.loadRataSdByStatoDebitorio(idStatoDebitorio);
		return Response.ok(rataSd).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response saveNRataSd(String fruitore,Long idStatoDebitorio, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) throws DAOException, GenericExceptionList {
	    LOGGER.debug("[RataSdApiServiceImpl::saveNRataSd] BEGIN");

        LOGGER.debug("[RataSdApiServiceImpl::saveNRataSd] Parametro in input idStatoDebitorio :\n " + idStatoDebitorio);
        try {
 		    LOGGER.debug("[RataSdApiServiceImpl::saveNRataSd] verificaIdentitaDigitale BEGIN");
 		     identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null,
 					httpHeaders, Constants.POST_PUT_DEL_RATA); 
 		    LOGGER.debug("[RataSdApiServiceImpl::saveNRataSd] verificaIdentitaDigitale END");
 		}
 		catch (BusinessException be) {
 			return handleBusinessException(be.getHttpStatus(), be);

		} catch (Exception e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR).status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();

		} 
		Identita identita = null;
        List<Integer> listIdRataSd = new ArrayList<Integer>();
		Long idAmbito = null;
		try {

			identita = IdentitaDigitaleManager.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders),fruitore);
			idAmbito = identitaDigitaleManager.getAmbitoByIdentitaOrFonte(identita, fruitore, null);
			LOGGER.debug("[RataSdApiServiceImpl::saveNRataSd] idAmbito:" + idAmbito);
	
			StatoDebitorioExtendedDTO stDebitorio =statoDebitorioDAO.loadStatoDebitorioByIdStatoDebitorio(idStatoDebitorio);
			
			listIdRataSd = rataSdDAO.saveNRataSd(stDebitorio, idAmbito);
		}
		catch (BusinessException be) {
			return handleBusinessException(400, be);
		} 
	 catch (Exception e) {
		return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR).status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();

	} 

	    LOGGER.debug("[RataSdApiServiceImpl::saveNRataSd] END");
        return Response.ok(listIdRataSd).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
        
	}

	@Override
	public Response createRataSd(RataSdDTO rataSd, String fruitore, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[RataSdApiServiceImpl::createRataSd] BEGIN");
		LOGGER.debug("[RataSdApiServiceImpl::createRataSd] Parametro in input rataSd :\n " + rataSd);
		LOGGER.debug("[RataSdApiServiceImpl::createRataSd] Parametro in input fruitore :\n " + fruitore);

		try {
			setGestAttoreInsUpd(rataSd, fruitore, httpRequest, httpHeaders);
		    LOGGER.debug("[RataSdApiServiceImpl::createRataSd] verificaIdentitaDigitale BEGIN");
		     identitaDigitaleManager.verificaIdentitaDigitale(fruitore,null,
					httpHeaders, Constants.POST_PUT_DEL_RATA); 
		    LOGGER.debug("[RataSdApiServiceImpl::createRataSd] verificaIdentitaDigitale END");
  			businessLogic.validatorDTO(rataSd, null, null);
		} catch (ValidationException ve) {
  			LOGGER.debug("BAD REQUEST: MESSAGE:" + ve.getMessage());
  			Integer status = Response.Status.BAD_REQUEST.getStatusCode();
  			ErrorDTO err = new ErrorDTO(status.toString(), Constants.E003, ve.getMessage(), null, null);
  			return Response.status(Response.Status.BAD_REQUEST).entity(err).build();
	  	} 	
		catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);
		} catch (Exception e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR).status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();

		} 
			
		Identita identita = null;
		Long idAmbito = null;
		try {
			
			identita = IdentitaDigitaleManager.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders),fruitore);
			if(identita != null) {
				idAmbito = identitaDigitaleManager.getAmbitoByIdentitaOrFonte(identita, fruitore, null);
				LOGGER.debug("[RataSdApiServiceImpl::createRataSd] idAmbito:" + idAmbito);
			}
			
			rataSd.validate();

			RataSdDTO rataSdResult = businessLogic.createRataSd(rataSd);
			ResponseBuilder rb = Response.ok(rataSdResult);
			LOGGER.debug("[RataSdApiServiceImpl : createRataSd ] END ");		      		  
			return rb.build();
		}catch (DAOException e) {		
			if(e.getMessage().equals(ErrorMessages.CODE_1_CHIAVE_DUPLICATA)) {
				LOGGER.debug("!!!CODE 1 chiave duplicata!!!!");
				LOGGER.error(e);
				Integer status = Response.Status.BAD_REQUEST.getStatusCode();
				ErrorDTO err = new ErrorDTO(status.toString(), ErrorMessages.CODE_1_CHIAVE_DUPLICATA, ErrorMessages.MESSAGE_1_CHIAVE_DUPLICATA, null, null);
				return Response.serverError().entity(err.getTitle()).build();
			}
			LOGGER.error(e);
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR).status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
		} 
		catch (SystemException e) {
			LOGGER.error(e);
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR).status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
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
		finally {

		}
	}
	
}

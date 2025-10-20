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

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.csi.iride2.policy.entity.Identita;
import it.csi.risca.riscabesrv.business.be.BusinessLogic;
import it.csi.risca.riscabesrv.business.be.RecapitiApi;
import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.exception.DatiInputErratiException;
import it.csi.risca.riscabesrv.business.be.exception.SystemException;
import it.csi.risca.riscabesrv.business.be.exception.ValidationException;
import it.csi.risca.riscabesrv.business.be.impl.dao.SoggettiDAO;
import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.dto.RecapitiExtendedDTO;
import it.csi.risca.riscabesrv.dto.SoggettiExtendedDTO;
import it.csi.risca.riscabesrv.identitadigitale.IdentitaDigitaleManager;
import it.csi.risca.riscabesrv.tracciamento.TracciamentoManager;
import it.csi.risca.riscabesrv.util.Constants;
import it.csi.risca.riscabesrv.util.ErrorMessages;

@Service
public class RecapitiApiServiceImpl extends BaseApiServiceImpl implements RecapitiApi {

	@Autowired
	public BusinessLogic businessLogic;

    
    @Autowired
    private TracciamentoManager tracciamentoManager;
	
    @Autowired
	private IdentitaDigitaleManager identitaDigitaleManager; 
    
    @Autowired
    private SoggettiDAO soggettoDAO;

	@Override
	public Response createRecapito(RecapitiExtendedDTO recapito, String fruitore, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		LOGGER.debug("[RecapitiApiServiceImpl::createRecapito] BEGIN");
		LOGGER.debug("[RecapitiApiServiceImpl::createRecapito] Parametro in input recapito :\n " + recapito);
		LOGGER.debug("[RecapitiApiServiceImpl::createRecapito] Parametro in input fruitore :\n " + fruitore);

		  try {
				setGestAttoreInsUpd(recapito, fruitore, httpRequest, httpHeaders);

	 		    LOGGER.debug("[RecapitiApiServiceImpl::createRecapito] verificaIdentitaDigitale BEGIN");
	 		     identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null,
	 					httpHeaders, Constants.POST_PUT_DEL_RATA); 
	 		    LOGGER.debug("[RecapitiApiServiceImpl::createRecapito] verificaIdentitaDigitale END");
				businessLogic.validatorDTO(recapito, null, null);
		  } catch (ValidationException ve) {
				LOGGER.debug("BAD REQUEST: MESSAGE:" + ve.getMessage());
				Integer status = Response.Status.BAD_REQUEST.getStatusCode();
				ErrorDTO err = new ErrorDTO(status.toString(), Constants.E003, ve.getMessage(), null, null);
				return Response.status(Response.Status.BAD_REQUEST).entity(err).build();
			} 	
	 		catch (BusinessException be) {
	 			return handleBusinessException(be.getHttpStatus(), be);
	 		}catch (Exception e) {
				return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR).status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();

			} 
			
		    Identita identita = null;
			Long idAmbito = null;
			RecapitiExtendedDTO recapitoResult = null;
			try {

				identita = IdentitaDigitaleManager.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders),fruitore);
				if(identita != null) {
					idAmbito = identitaDigitaleManager.getAmbitoByIdentitaOrFonte(identita, fruitore, null);
					LOGGER.debug("[RecapitiApiServiceImpl::createRecapito] idAmbito:" + idAmbito);
				}
				
			recapito.validate(fruitore);

			recapitoResult = businessLogic.createRecapito(recapito);
			ResponseBuilder rb = Response.ok(recapitoResult);
			LOGGER.debug("[RecapitiApiServiceImpl : createRecapito ] END ");		      		  
			return rb.build();
		}catch (DAOException e) {		
			if(e.getMessage().equals(ErrorMessages.CODE_1_CHIAVE_DUPLICATA)) {
				LOGGER.debug("!!!CODE 1 chiave duplicata!!!!");
				LOGGER.error(e);
				Integer status = Response.Status.BAD_REQUEST.getStatusCode();
				ErrorDTO err = new ErrorDTO(status.toString(), ErrorMessages.CODE_1_CHIAVE_DUPLICATA, ErrorMessages.MESSAGE_1_CHIAVE_DUPLICATA, null, null);
				return Response.serverError().entity(err).status(status).build();
			}
			LOGGER.error(e);
			return Response.serverError().entity(Response.Status.NO_CONTENT).status(Response.Status.NO_CONTENT.getStatusCode()).build();
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
			try {
				tracciamentoManager.saveTracciamento(fruitore, recapitoResult, identita, null, "JSON RECAPITO",
						recapitoResult.getIdRecapito() != null ? recapitoResult.getIdRecapito().toString() : null, "RISCA_R_RECAPITO",
						Constants.FLG_OPERAZIONE_INSERT, Constants.OPERAZIONE_INSERT, true, true, httpRequest);
				
			} catch (Exception e) {
				LOGGER.error("[RecapitiApiServiceImpl::createRecapito:: operazione insert LogAudit ERROR ]: "+e);
				return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR).status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();

			}	
		}
	}

	@Override
	public Response updateRecapito(RecapitiExtendedDTO recapito,Long indModManuale, String fruitore, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[RecapitiApiServiceImpl::updateRecapito] BEGIN");
		LOGGER.debug("[RecapitiApiServiceImpl::updateRecapito] Parametro in input recapito :\n " + recapito);
		LOGGER.debug("[RecapitiApiServiceImpl::updateRecapito] Parametro in input fruitore :\n " + fruitore);

		 try {
				setGestAttoreInsUpd(recapito, fruitore, httpRequest, httpHeaders);
	 		    LOGGER.debug("[RecapitiApiServiceImpl::updateRecapito] verificaIdentitaDigitale BEGIN");
	 		     identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null,
	 					httpHeaders, Constants.POST_PUT_DEL_RATA); 
	 		    LOGGER.debug("[RecapitiApiServiceImpl::updateRecapito] verificaIdentitaDigitale END");
				businessLogic.validatorDTO(recapito, null, null);
		  } catch (ValidationException ve) {
				LOGGER.debug("BAD REQUEST: MESSAGE:" + ve.getMessage());
				Integer status = Response.Status.BAD_REQUEST.getStatusCode();
				ErrorDTO err = new ErrorDTO(status.toString(), Constants.E003, ve.getMessage(), null, null);
				return Response.status(Response.Status.BAD_REQUEST).entity(err).build();
			} 
	 		catch (BusinessException be) {
	 			return handleBusinessException(be.getHttpStatus(), be);
	 		}catch (Exception e) {
				return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR).status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();

			} 
		
		
		    Identita identita = null;
					Long idAmbito = null;
					try {

						identita = IdentitaDigitaleManager.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders),fruitore);
						if(identita != null) {
							idAmbito = identitaDigitaleManager.getAmbitoByIdentitaOrFonte(identita, fruitore, null);
							LOGGER.debug("[RecapitiApiServiceImpl::updateRecapito] idAmbito:" + idAmbito);
						}
						
			recapito.validate(fruitore);
			 SoggettiExtendedDTO  soggettiExtended  = soggettoDAO.loadSoggettoById(recapito.getIdSoggetto());
			RecapitiExtendedDTO recapitoResult = businessLogic.updateRecapito(recapito,null,soggettiExtended,  indModManuale);
			ResponseBuilder rb = Response.ok(recapitoResult);
			LOGGER.debug("[RecapitiApiServiceImpl : updateRecapito ] END ");		      		  
			return rb.build();
		}catch (DAOException e ) {		
			if(e.getMessage().equals(ErrorMessages.CODE_1_CHIAVE_DUPLICATA)) {
				LOGGER.error(e);
				Integer status = Response.Status.BAD_REQUEST.getStatusCode();
				ErrorDTO err = new ErrorDTO(status.toString(), ErrorMessages.CODE_1_CHIAVE_DUPLICATA, ErrorMessages.MESSAGE_1_CHIAVE_DUPLICATA, null, null);
				return Response.serverError().entity(err).status(status).build();
			}
			LOGGER.error(e);
			return Response.serverError().entity(Response.Status.NO_CONTENT).status(Response.Status.NO_CONTENT.getStatusCode()).build();
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
		} catch (SQLException e) {
			LOGGER.error(e);
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR).status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
		}
		catch (Exception e) {
			LOGGER.error(e);
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR).status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
		}
		finally {
			try {

				tracciamentoManager.saveTracciamento(fruitore, recapito, identita, null, "JSON RECAPITO",
						recapito.getIdRecapito() != null ? recapito.getIdRecapito().toString() : null, "RISCA_R_RECAPITO",
						Constants.FLG_OPERAZIONE_UPDATE, Constants.OPERAZIONE_UPDATE, true, true, httpRequest);
				
			} catch (Exception e) {
				LOGGER.error("[RecapitiApiServiceImpl::updateRecapito:: operazione insert LogAudit ERROR ]: "+e);	
			}	
		}
	}

	@Override
	public Response readRecapitoByPk(Long idRecapito, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[RecapitiApiServiceImpl::readRecapitoByPk] BEGIN");
		LOGGER.debug("[RecapitiApiServiceImpl::readRecapitoByPk] Parametro in input idRecapito :\n " + idRecapito);

		try {

			RecapitiExtendedDTO recapito = businessLogic.readRecapitoByPk(idRecapito);
			ResponseBuilder rb = Response.ok(recapito);
			LOGGER.debug("[RecapitiApiServiceImpl : readRecapitoByPk ] END");
			return rb.build();
		}  catch (DAOException e) 
		{
			LOGGER.error(e);
			return Response.serverError().entity(Response.Status.NO_CONTENT).status(Response.Status.NO_CONTENT.getStatusCode()).build();
		} 
		catch (SystemException e) 
		{
			LOGGER.error(e);
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR).status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
		} catch (NotFoundException e) 
		{
			LOGGER.error(e);
			return Response.serverError().entity(Response.Status.NOT_FOUND).status(Response.Status.NOT_FOUND.getStatusCode()).build();
		}finally {
			//valutare se inserire tracciamento nella tabella csi_log_audit
		} 

	}

}

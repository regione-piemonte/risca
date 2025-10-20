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
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.iride2.policy.entity.Identita;
import it.csi.risca.riscabesrv.business.be.BusinessLogic;
import it.csi.risca.riscabesrv.business.be.LockRiscossioneApi;
import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.exception.SystemException;
import it.csi.risca.riscabesrv.business.be.exception.ValidationException;
import it.csi.risca.riscabesrv.business.be.impl.dao.LockRiscossioneDAO;
import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.dto.LockRiscossioneDTO;
import it.csi.risca.riscabesrv.identitadigitale.IdentitaDigitaleManager;
import it.csi.risca.riscabesrv.tracciamento.TracciamentoManager;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type  LockRiscossioneApiServiceImpl.
 *
 * @author CSI PIEMONTE
 */
@Component
public class LockRiscossioneApiServiceImpl extends BaseApiServiceImpl implements LockRiscossioneApi {

	private static final String IDENTITY = "identity";

	
    @Autowired
    private LockRiscossioneDAO  lockRiscossioneDAO;


	@Autowired
	private IdentitaDigitaleManager identitaDigitaleManager;
	
	@Autowired
	public BusinessLogic businessLogic;
	
    @Autowired
    private TracciamentoManager tracciamentoManager;
    
	@Override
	public Response getAllLockRiscossione(String fruitore, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		LOGGER.debug("[LockRiscossioneApiServiceImpl::getAllLockRiscossione] BEGIN");
		List<LockRiscossioneDTO> lockRiscossioneDTO = new ArrayList<LockRiscossioneDTO>();
		try {
			LOGGER.debug("[LockRiscossioneApiServiceImpl::getAllLockRiscossione] verificaIdentitaDigitale BEGIN");
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders, Constants.LOAD_LOC_RIS);
			LOGGER.debug("[LockRiscossioneApiServiceImpl::getAllLockRiscossione] verificaIdentitaDigitale END");

			lockRiscossioneDTO = lockRiscossioneDAO.getAllLockRiscossione();
		} catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);
		} catch (Exception e) {
			LOGGER.debug("[LockRiscossioneApiServiceImpl::getAllLockRiscossione] ERROR: Exception - END" + e);
			ErrorDTO err = new ErrorDTO("500", "E005","Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
			return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		}
		LOGGER.debug("[LockRiscossioneApiServiceImpl::getAllLockRiscossione] END");
		return Response.ok(lockRiscossioneDTO).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response getLockRiscossioneByIdRiscossione(String fruitore, Long idRiscossione,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {

		LOGGER.debug("[LockRiscossioneApiServiceImpl::getLockRiscossioneByIdRiscossione] BEGIN");
		LockRiscossioneDTO lockRiscossioneDTO = null;
		try {
			LOGGER.debug("[LockRiscossioneApiServiceImpl::getLockRiscossioneByIdRiscossione] verificaIdentitaDigitale BEGIN");
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders, Constants.LOAD_LOC_RIS);
			LOGGER.debug("[LockRiscossioneApiServiceImpl::getLockRiscossioneByIdRiscossione] verificaIdentitaDigitale END");

			lockRiscossioneDTO = lockRiscossioneDAO.getLockRiscossioneByIdRiscossione(idRiscossione);
		} catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);
		} catch (Exception e) {
			LOGGER.debug("[LockRiscossioneApiServiceImpl::getLockRiscossioneByIdRiscossione] ERROR: Exception - END" , e);
			ErrorDTO err = new ErrorDTO("500", "E005","Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
			return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		}
		LOGGER.debug("[LockRiscossioneApiServiceImpl::getLockRiscossioneByIdRiscossione] END");
		return Response.ok(lockRiscossioneDTO).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response saveLockRiscossione(LockRiscossioneDTO lockRiscossioneDTO, String fruitore,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {

		LOGGER.debug("[LockRiscossioneApiServiceImpl::saveLockRiscossione] BEGIN");
		try {
			LOGGER.debug("[LockRiscossioneApiServiceImpl::saveLockRiscossione] verificaIdentitaDigitale BEGIN");
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders, Constants.POST_PUT_DEL_lOC_RIS);
			LOGGER.debug("[LockRiscossioneApiServiceImpl::saveLockRiscossione] verificaIdentitaDigitale END");
			setGestAttoreInsUpd(lockRiscossioneDTO, null, httpRequest, httpHeaders);
			businessLogic.validatorDTO(lockRiscossioneDTO, null, null);
			lockRiscossioneDTO = lockRiscossioneDAO.saveLockRiscossione(lockRiscossioneDTO);

		} catch (ValidationException ve) {
			LOGGER.debug("BAD REQUEST: MESSAGE:" + ve.getMessage());
			Integer status = Response.Status.BAD_REQUEST.getStatusCode();
			ErrorDTO err = new ErrorDTO(status.toString(), Constants.E003, ve.getMessage(), null, null);
			return Response.status(Response.Status.BAD_REQUEST).entity(err).build();
		} 	catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);
		} catch (Exception e) {
			LOGGER.debug("[LockRiscossioneApiServiceImpl::saveLockRiscossione] ERROR: Exception - END" , e);
			ErrorDTO err = new ErrorDTO("500", "E005","Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
			return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		}
		
		try {
			LOGGER.debug("[LockRiscossioneApiServiceImpl::saveLockRiscossione]  BEGIN save tracciamento");
			Identita identita = IdentitaDigitaleManager
					.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders),fruitore);

			
			tracciamentoManager.saveTracciamento(lockRiscossioneDTO.getCfUtenteLock(), lockRiscossioneDTO, identita, lockRiscossioneDTO.getIdRiscossione(), "JSON lockRiscossione",
					lockRiscossioneDTO.getIdRiscossione() != null ? lockRiscossioneDTO.getIdRiscossione().toString() : null, "RISCA_T_LOCK_RISCOSSIONE",
					Constants.FLG_OPERAZIONE_INSERT, Constants.OPERAZIONE_INSERT, true, false, httpRequest);
			
			LOGGER.debug("[LockRiscossioneApiServiceImpl::saveLockRiscossione]  END save tracciamento");

		} catch (Exception e) {
			LOGGER.error("[LockRiscossioneApiServiceImpl::saveLockRiscossione:: operazione insert LogAudit ERROR ]: ", e);
			ErrorDTO err = new ErrorDTO("500", "E005",
					"Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
			return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();

		}

		LOGGER.debug("[LockRiscossioneApiServiceImpl::saveLockRiscossione] END");
		return Response.ok(lockRiscossioneDTO).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}
	
	@Override
	public Response deleteLockRiscossione(Long idRiscossione, String fruitore, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[LockRiscossioneApiServiceImpl::deleteLockRiscossione] BEGIN");
		LOGGER.debug("[LockRiscossioneApiServiceImpl::deleteLockRiscossione] Parametro in input idRiscossione :" + idRiscossione);
		LockRiscossioneDTO lockRiscossioneDTO = new LockRiscossioneDTO();
		try {
			LOGGER.debug("[LockRiscossioneApiServiceImpl::deleteLockRiscossione] verificaIdentitaDigitale BEGIN");
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders, Constants.POST_PUT_DEL_lOC_RIS);
			LOGGER.debug("[LockRiscossioneApiServiceImpl::deleteLockRiscossione] verificaIdentitaDigitale END");
			lockRiscossioneDTO = lockRiscossioneDAO.getLockRiscossioneByIdRiscossione(idRiscossione);
		   lockRiscossioneDAO.deleteLockRiscossione(idRiscossione);
		} catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);
		} catch (SQLException e) {
			LOGGER.error("[LockRiscossioneApiServiceImpl::deleteLockRiscossione:: operazione insert LogAudit ERROR ]: " , e);
			ErrorDTO err = new ErrorDTO("500", "E005","Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
			return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		} catch (SystemException e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR).status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();

		}
		return Response.ok(lockRiscossioneDTO).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}



}

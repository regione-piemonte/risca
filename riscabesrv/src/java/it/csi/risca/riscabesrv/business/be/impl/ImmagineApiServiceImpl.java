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

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.risca.riscabesrv.business.be.BusinessLogic;
import it.csi.risca.riscabesrv.business.be.ImmagineApi;
import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.exception.ValidationException;
import it.csi.risca.riscabesrv.business.be.impl.dao.ImmagineDAO;
import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.dto.ImmagineDTO;
import it.csi.risca.riscabesrv.dto.LockRiscossioneDTO;
import it.csi.risca.riscabesrv.identitadigitale.IdentitaDigitaleManager;
import it.csi.risca.riscabesrv.util.Constants;
import it.csi.risca.riscabesrv.util.download.DownloadManager;

/**
 * The type IUV api service.
 *
 * @author CSI PIEMONTE
 */
@Component
public class ImmagineApiServiceImpl extends BaseApiServiceImpl implements ImmagineApi {

	private static final String IDENTITY = "identity";
	// private static Logger LOGGER = Logger.getLogger(Constants.LOGGER_NAME);

	@Autowired
	private ImmagineDAO immagineDAO;
	
	@Autowired
	private DownloadManager downloadManager;
	@Autowired
	private IdentitaDigitaleManager identitaDigitaleManager;
	
	@Autowired
	public BusinessLogic businessLogic;
	
	@Override
	public Response saveImmagine(ImmagineDTO immagine, String fruitore, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {
			businessLogic.validatorDTO(immagine, null, null);
			setGestAttoreInsUpd(immagine, fruitore, httpRequest, httpHeaders);
			ImmagineDTO dto = immagineDAO.saveImmagine(immagine);
			return Response.ok(dto).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		} catch (ValidationException ve) {
			LOGGER.debug("BAD REQUEST: MESSAGE:" + ve.getMessage());
			Integer status = Response.Status.BAD_REQUEST.getStatusCode();
			ErrorDTO err = new ErrorDTO(status.toString(), Constants.E003, ve.getMessage(), null, null);
			return Response.status(Response.Status.BAD_REQUEST).entity(err).build();
		} catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);
		} catch (Exception e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();

		}
	}

	@Override
	public Response loadImmagineById(String fruitore, Integer idImmagine, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {

		LOGGER.debug("[ImmagineApiServiceImpl::loadImmagineById] BEGIN");
		ImmagineDTO immagineDTO = null;
		try {
			if(idImmagine == null) {
				return Response.serverError().entity(Response.Status.BAD_REQUEST).status(Response.Status.BAD_REQUEST.getStatusCode()).build();
			}
			LOGGER.debug("[ImmagineApiServiceImpl::loadImmagineById] verificaIdentitaDigitale BEGIN");
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders, Constants.LOAD_IMM);
			LOGGER.debug("[ImmagineApiServiceImpl::loadImmagineById] verificaIdentitaDigitale END");

			immagineDTO = immagineDAO.loadImmagineById(idImmagine);
			if(immagineDTO != null)
			   immagineDTO.setPathImmagine(downloadManager.getDownloadUrl() + immagineDTO.getPathImmagine());
			
		} catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);
		} catch (Exception e) {
			LOGGER.debug("[ImmagineApiServiceImpl::loadImmagineById] ERROR: Exception - END" , e);
			ErrorDTO err = new ErrorDTO("500", "E005","Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
			return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		}
		LOGGER.debug("[ImmagineApiServiceImpl::loadImmagineById] END");
		return Response.ok(immagineDTO).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

}

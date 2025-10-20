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

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.risca.riscabesrv.business.be.DettaglioPagSearchResultApi;
import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.impl.dao.DettaglioPagDAO;
import it.csi.risca.riscabesrv.dto.DettaglioPagExtendedDTO;
import it.csi.risca.riscabesrv.dto.DettaglioPagSearchResultDTO;
import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.identitadigitale.IdentitaDigitaleManager;
import it.csi.risca.riscabesrv.util.Constants;
import it.csi.risca.riscabesrv.util.ErrorMessages;

/**
 * The type Dettaglio Pag Api Service Impl.
 *
 * @author CSI PIEMONTE
 */
@Component
public class DettaglioPagSearchResultApiServiceImpl extends BaseApiServiceImpl implements DettaglioPagSearchResultApi {

	private static final String IDENTITY = "identity";
	private static Logger LOGGER = Logger.getLogger(Constants.LOGGER_NAME);
	
	@Autowired
	private DettaglioPagDAO dettaglioPagDAO;	
    
    @Autowired
    private IdentitaDigitaleManager identitaDigitaleManager; 
    
	@Override
	public Response getDettaglioPagSearchResultByIdPagamento(String fruitore, Long idPagamento,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[DettaglioPagSearchResultApiServiceImpl::getDettaglioPagSearchResultByIdPagamento] BEGIN");
		try {
			 LOGGER.debug("[DettaglioPagSearchResultApiServiceImpl::getDettaglioPagSearchResultByIdPagamento] verificaIdentitaDigitale BEGIN");
			 identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null,httpHeaders, Constants.LOAD_DET_PAG);  
			 LOGGER.debug("[DettaglioPagSearchResultApiServiceImpl::getDettaglioPagSearchResultByIdPagamento] verificaIdentitaDigitale END");
			
			List<DettaglioPagSearchResultDTO> listDettaglioPag = dettaglioPagDAO.getDettaglioPagByIdPagamento(idPagamento);
			LOGGER.debug("[DettaglioPagSearchResultApiServiceImpl::getDettaglioPagSearchResultByIdPagamento] END");
			return Response.ok(listDettaglioPag).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
			
		}catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);
		}catch (DAOException e) {	
		    LOGGER.error("[DettaglioPagSearchResultApiServiceImpl::getDettaglioPagSearchResultByIdPagamento] ERROR " ,e);
			if(e.getMessage().equals(ErrorMessages.CODE_1_CHIAVE_DUPLICATA)) {
				LOGGER.debug("!!!CODE 1 chiave duplicata!!!!");
				Integer status = Response.Status.BAD_REQUEST.getStatusCode();
				ErrorDTO err = new ErrorDTO(status.toString(), ErrorMessages.CODE_1_CHIAVE_DUPLICATA, ErrorMessages.MESSAGE_1_CHIAVE_DUPLICATA, null, null);
				return Response.serverError().entity(err.getTitle()).build();
			}
			ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
            return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		}catch (Exception e) {
		    LOGGER.error("[DettaglioPagSearchResultApiServiceImpl::getDettaglioPagSearchResultByIdPagamento] ERROR ", e);
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR).status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();

				
		}
	}
	


}

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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.risca.riscabesrv.business.be.BusinessLogic;
import it.csi.risca.riscabesrv.business.be.ParametroElaboraApi;
import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.exception.ValidationException;
import it.csi.risca.riscabesrv.business.be.impl.dao.ParametroElaboraDAO;
import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.dto.ParametroElaboraDTO;
import it.csi.risca.riscabesrv.identitadigitale.IdentitaDigitaleManager;
import it.csi.risca.riscabesrv.util.Constants;

@Component
public class ParametroElaboraApiServiceImpl  extends BaseApiServiceImpl implements ParametroElaboraApi {

	private static final String IDENTITY = "identity";

	@Autowired
	private ParametroElaboraDAO parametroElaboraDAO;
    
    @Autowired
    private IdentitaDigitaleManager identitaDigitaleManager; 
    
	@Autowired
	public BusinessLogic businessLogic;	

	@Override
	public Response loadParametroElaboraByElaboraRaggruppamento(String fruitore,String idElabora, String raggruppamento,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
	    LOGGER.debug("[ParametroElaboraApiServiceImpl::loadParametroElaboraByElaboraRaggruppamento]  BEGIN");
        try {
		    LOGGER.debug("[ParametroElaboraApiServiceImpl::loadParametroElaboraByElaboraRaggruppamento] verificaIdentitaDigitale BEGIN");
		     identitaDigitaleManager.verificaIdentitaDigitale(fruitore,null,
					httpHeaders, Constants.LOAD_PARAM_ELAB); 
		    LOGGER.debug("[ParametroElaboraApiServiceImpl::loadParametroElaboraByElaboraRaggruppamento] verificaIdentitaDigitale END");
		}
		catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);
		} catch (Exception e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR).status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
        }

		List<ParametroElaboraDTO> listElabora = parametroElaboraDAO
				.loadParametroElaboraByElaboraRaggruppamento(idElabora, raggruppamento);
		return Response.ok(listElabora).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response saveParametroElabora(ParametroElaboraDTO parametroElabora, String fruitore,SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
	   LOGGER.debug("[ParametroElaboraApiServiceImpl::saveParametroElabora]  BEGIN");
	   ParametroElaboraDTO regElab= null;
       try {
		    LOGGER.debug("[ParametroElaboraApiServiceImpl::saveParametroElabora] verificaIdentitaDigitale BEGIN");
		     identitaDigitaleManager.verificaIdentitaDigitale(fruitore,null,
					httpHeaders, Constants.POST_PUT_DEL_PARAM); 
			businessLogic.validatorDTO(parametroElabora, null, null);

		    LOGGER.debug("[ParametroElaboraApiServiceImpl::saveParametroElabora] verificaIdentitaDigitale END");
			 regElab = parametroElaboraDAO.saveParametroElabora(parametroElabora);
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

		return Response.ok(regElab).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();

	}

	@Override
	public Response updateParametroElabora(ParametroElaboraDTO parametroElabora, String fruitore, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
	    LOGGER.debug("[ParametroElaboraApiServiceImpl::updateParametroElabora]  BEGIN");
		   ParametroElaboraDTO regElab= null;
       try {
		    LOGGER.debug("[ParametroElaboraApiServiceImpl::updateParametroElabora] verificaIdentitaDigitale BEGIN");
		     identitaDigitaleManager.verificaIdentitaDigitale(fruitore,null,
					httpHeaders, Constants.POST_PUT_DEL_PARAM); 
		    LOGGER.debug("[ParametroElaboraApiServiceImpl::updateParametroElabora] verificaIdentitaDigitale END");
		    regElab = parametroElaboraDAO.updateParametroElabora(parametroElabora);
		}
		catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);
		} catch (Exception e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR).status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
        }
		return Response.ok(regElab).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();

	}

}

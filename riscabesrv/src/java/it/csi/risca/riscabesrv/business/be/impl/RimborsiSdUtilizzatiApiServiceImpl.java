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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import it.csi.iride2.policy.entity.Identita;
import it.csi.risca.riscabesrv.business.be.BusinessLogic;
import it.csi.risca.riscabesrv.business.be.RimborsiSdUtilizzatiApi;
import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.exception.DatiInputErratiException;
import it.csi.risca.riscabesrv.business.be.exception.GenericExceptionList;
import it.csi.risca.riscabesrv.business.be.exception.ValidationException;
import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.dto.RimborsoSdUtilizzatoDTO;
import it.csi.risca.riscabesrv.identitadigitale.IdentitaDigitaleManager;
import it.csi.risca.riscabesrv.tracciamento.TracciamentoManager;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type IUV api service.
 *
 * @author CSI PIEMONTE
 */
@Component
public class RimborsiSdUtilizzatiApiServiceImpl extends BaseApiServiceImpl implements RimborsiSdUtilizzatiApi {

	private static final String IDENTITY = "identity";

	@Autowired
	public BusinessLogic businessLogic;
	
	@Autowired
	private IdentitaDigitaleManager identitaDigitaleManager; 
	
    @Autowired
    private TracciamentoManager tracciamentoManager;
	


	@Override
	public Response saveRimborsiSdUtilizzati(RimborsoSdUtilizzatoDTO rimborsoSdUtilizzato, String fruitore,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest)
			throws GenericExceptionList, ParseException {
		LOGGER.debug("[RimborsiSdUtilizzatiApiServiceImpl::saveRimborsiSdUtilizzati] BEGIN");
		LOGGER.debug("[RimborsiSdUtilizzatiApiServiceImpl::saveRimborsiSdUtilizzati] Parametro in input rimborsoSdUtilizzato :\n " + rimborsoSdUtilizzato);
		RimborsoSdUtilizzatoDTO rimborsoSdUtilizzatoResult = null;
		try {

			setGestAttoreInsUpd(rimborsoSdUtilizzato, fruitore, httpRequest, httpHeaders);
			LOGGER.debug("[RimborsiSdUtilizzatiApiServiceImpl::saveRimborsiSdUtilizzati] verificaIdentitaDigitale BEGIN");
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null,httpHeaders, Constants.POST_RIMBORSO_SD_UTILIZZATO);  
			LOGGER.debug("[RimborsiSdUtilizzatiApiServiceImpl::saveRimborsiSdUtilizzati] verificaIdentitaDigitale END");
		
				businessLogic.validatorDTO(rimborsoSdUtilizzato, null, null);
			
			try {
				rimborsoSdUtilizzato.validate(fruitore);
				rimborsoSdUtilizzatoResult = businessLogic.createRimborsoSdUtilizzato(rimborsoSdUtilizzato);

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
				LOGGER.error("[RimborsiSdUtilizzatiApiServiceImpl::saveRimborsiSdUtilizzati:: EXCEPTION ]: ",e);
				return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR).status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();

			}
				
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
		finally {
			LOGGER.debug("[RimborsiSdUtilizzatiApiServiceImpl::saveRimborsiSdUtilizzati] BEGIN save tracciamento");	
			Identita 	identita = IdentitaDigitaleManager.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders), fruitore);

			try {
				tracciamentoManager.saveTracciamento(fruitore, rimborsoSdUtilizzatoResult, identita, null, "JSON RIMBORSO SD UTILIZZATO",
						rimborsoSdUtilizzatoResult.getIdRimborso() != null ? rimborsoSdUtilizzatoResult.getIdRimborso().toString() : null, "RISCA_R_RIMBORSO_SD_UTILIZZATO",
						Constants.FLG_OPERAZIONE_INSERT, Constants.OPERAZIONE_INSERT, true, true, httpRequest);
			} catch (Exception e) {
				return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
						.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
			}
			
			LOGGER.debug("[RimborsiSdUtilizzatiApiServiceImpl::saveRimborsiSdUtilizzati] END save tracciamento");
		}

		return Response.ok(rimborsoSdUtilizzatoResult).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

}

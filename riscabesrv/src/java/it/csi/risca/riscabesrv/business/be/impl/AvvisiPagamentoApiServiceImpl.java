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
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.csi.iride2.policy.entity.Application;
import it.csi.iride2.policy.entity.Identita;
import it.csi.iride2.policy.entity.UseCase;
import it.csi.risca.riscabesrv.business.be.AvvisiPagamentoApi;
import it.csi.risca.riscabesrv.business.be.BusinessLogic;
import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.exception.DatiInputErratiException;
import it.csi.risca.riscabesrv.business.be.exception.SystemException;
import it.csi.risca.riscabesrv.business.be.exception.ValidationException;
import it.csi.risca.riscabesrv.business.be.helper.iride.IrideServiceHelper;
import it.csi.risca.riscabesrv.dto.AvvisoPagamentoDTO;
import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.identitadigitale.IdentitaDigitaleManager;
import it.csi.risca.riscabesrv.tracciamento.TracciamentoManager;
import it.csi.risca.riscabesrv.util.Constants;
import it.csi.risca.riscabesrv.util.ErrorMessages;

@Service
public class AvvisiPagamentoApiServiceImpl extends BaseApiServiceImpl implements AvvisiPagamentoApi {

	@Autowired
	public BusinessLogic businessLogic;

	@Autowired
	private IrideServiceHelper serviceHelper;

	@Autowired
	private IdentitaDigitaleManager identitaDigitaleManager;
	
	private static final String IDENTITY = "identity";
	
    @Autowired
    private TracciamentoManager tracciamentoManager;

	@Override
	public Response createAvvisoPagamento(AvvisoPagamentoDTO avvisoPagamento, String fruitore,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[AvvisiPagamentoApiServiceImpl::createAvvisoPagamento] BEGIN");
		LOGGER.debug("[AvvisiPagamentoApiServiceImpl::createAvvisoPagamento] Parametro in input avvisoPagamento :\n "
				+ avvisoPagamento);
		LOGGER.debug(
				"[AvvisiPagamentoApiServiceImpl::createAvvisoPagamento] Parametro in input fruitore :\n " + fruitore);

		try {
			setGestAttoreInsUpd(avvisoPagamento, fruitore, httpRequest, httpHeaders);

			LOGGER.debug("[AvvisiPagamentoApiServiceImpl::createAvvisoPagamento] verificaIdentitaDigitale BEGIN");
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders, Constants.POST_AVV_PAGA);
			LOGGER.debug("[AvvisiPagamentoApiServiceImpl::createAvvisoPagamento] verificaIdentitaDigitale END");
			businessLogic.validatorDTO(avvisoPagamento, null, null);
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

		Identita identita = null;
		Long idAmbito = null;
		try {

			identita = IdentitaDigitaleManager.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders),fruitore);
			if(identita != null) {

				Application cod = new Application();
				cod.setId("RISCA");
				UseCase use = new UseCase();
				use.setAppId(cod);
				use.setId("UC_SIPRA");
				idAmbito = serviceHelper.getInfoPersonaInUseCase(identita, use);
				idAmbito = identitaDigitaleManager.getAmbitoByIdentitaOrFonte(identita, fruitore, null);
				LOGGER.debug("[AvvisiPagamentoApiServiceImpl::createAvvisoPagamento] idAmbito:" + idAmbito);
			}

			avvisoPagamento.validate();

			AvvisoPagamentoDTO avvisoPagamentoResult = businessLogic.createAvvisoPagamento(avvisoPagamento);
			avvisoPagamento.setIdSpedizione(avvisoPagamentoResult.getIdSpedizione());
			ResponseBuilder rb = Response.ok(avvisoPagamentoResult);
			LOGGER.debug("[AvvisiPagamentoApiServiceImpl : createAvvisoPagamento ] END ");		      		  
			return rb.build();
		}catch (DAOException e) {	
			
			if(e.getMessage().equals(ErrorMessages.CODE_1_CHIAVE_DUPLICATA)) {
				LOGGER.debug("!!!CODE 1 chiave duplicata!!!!");
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

	@Override
	public Response readAvvisoPagamentoByNap(String nap, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		LOGGER.debug("[AvvisiPagamentoApiServiceImpl::readAvvisoPagamentoByNap] BEGIN");
		LOGGER.debug("[AvvisiPagamentoApiServiceImpl::readAvvisoPagamentoByNap] Parametro in input nap :\n " + nap);

		try {

			AvvisoPagamentoDTO avvisoPagamento = businessLogic.readAvvisoPagamentoByNap(nap);
			ResponseBuilder rb = Response.ok(avvisoPagamento);
			LOGGER.debug("[AvvisiPagamentoApiServiceImpl : readAvvisoPagamentoByNap ] END");
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

	@Override
	public Response loadAvvisoPagamentoWorkingByIdSpedizione(Long idSpedizione, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		List<AvvisoPagamentoDTO> avvisiPagamento = businessLogic
				.loadAvvisoPagamentoWorkingByIdSpedizione(idSpedizione);
		return Response.ok(avvisiPagamento).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response copyWorkingForAvvisoPagamento(AvvisoPagamentoDTO avvisoPagamento, Long idElabora, Long idSpedizione,
			String attore, Boolean bollettazioneOrdinaria, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {

			businessLogic.validatorDTO(avvisoPagamento, null, null);
			
			businessLogic.copyWorkingForAvvisoPagamento(avvisoPagamento, idElabora, idSpedizione, attore,
					bollettazioneOrdinaria);
			
		} catch (ValidationException ve) {
			LOGGER.debug("BAD REQUEST: MESSAGE:" + ve.getMessage());
			Integer status = Response.Status.BAD_REQUEST.getStatusCode();
			ErrorDTO err = new ErrorDTO(status.toString(), Constants.E003, ve.getMessage(), null, null);
			return Response.status(Response.Status.BAD_REQUEST).entity(err).build();
		} catch (DAOException e) {
			LOGGER.error("[AvvisiPagamentoApiServiceImpl::copyWorkingForAvvisoPagamento:: EXCEPTION ]: " + e);
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
		} catch (Exception e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR).status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
        } 
		return Response.ok(avvisoPagamento).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

}

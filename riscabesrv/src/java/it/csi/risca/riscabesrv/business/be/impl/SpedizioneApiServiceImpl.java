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
import it.csi.risca.riscabesrv.business.be.SpedizioneApi;
import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.exception.ValidationException;
import it.csi.risca.riscabesrv.business.be.impl.dao.SpedizioneActaDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.SpedizioneDAO;
import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.dto.SpedizioneActaDTO;
import it.csi.risca.riscabesrv.dto.SpedizioneDTO;
import it.csi.risca.riscabesrv.util.Constants;

@Component
public class SpedizioneApiServiceImpl extends BaseApiServiceImpl implements SpedizioneApi {

	private static final String IDENTITY = "identity";

	@Autowired
	private SpedizioneDAO spedizioneDAO;

	@Autowired
	private SpedizioneActaDAO spedizioneActaDAO;

    @Autowired
    private BusinessLogic businessLogic;
    
	@Override
	public Response loadSpedizioneByIdSpedizione(String idSpedizione, boolean isWorking,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		SpedizioneDTO spedizione;
		try {
			spedizione = spedizioneDAO.loadSpedizioneByPk(idSpedizione, isWorking);
		} catch (Exception e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
		}
		return Response.ok(spedizione).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response loadSpedizioneByElaborazione(String idAmbito, String idElabora, String codTipoSpedizione,
			boolean isWorking, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		List<SpedizioneDTO> spedizione;
		try {
			spedizione = spedizioneDAO.loadSpedizioneByElaborazione(idAmbito, idElabora,
					codTipoSpedizione, isWorking);
		} catch (Exception e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
		}
		return Response.ok(spedizione).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response saveSpedizione(SpedizioneDTO spedizione, String fruitore, boolean isWorking, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			setGestAttoreInsUpd(spedizione, fruitore, httpRequest, httpHeaders);
			businessLogic.validatorDTO(spedizione, null, null);
			SpedizioneDTO regElab = spedizioneDAO.saveSpedizione(spedizione, isWorking);
			return Response.ok(regElab).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
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
	public Response updateSpedizione(SpedizioneDTO spedizione,String fruitore,  boolean isWorking, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			setGestAttoreInsUpd(spedizione, fruitore, httpRequest, httpHeaders);
			businessLogic.validatorDTO(spedizione, null, null);
			SpedizioneDTO regElab = spedizioneDAO.updateSpedizione(spedizione, isWorking);
			return Response.ok(regElab).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
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
	public Response copySpedizioneFromWorking(Long idSpedizione, String attore, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		Integer ret;
		try {
			ret = spedizioneDAO.copySpedizioneFromWorking(idSpedizione, attore);
		} catch (Exception e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
		}
		return Response.ok(ret).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response saveSpedizioneActa(SpedizioneActaDTO dto,String fruitore, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {
			setGestAttoreInsUpd(dto, fruitore, httpRequest, httpHeaders);
			businessLogic.validatorDTO(dto, null, null);
			SpedizioneActaDTO spedActa = spedizioneActaDAO.saveSpedizioneActa(dto);
			return Response.ok(spedActa).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
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

}

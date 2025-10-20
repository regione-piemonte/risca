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
import org.springframework.stereotype.Service;

import it.csi.risca.riscabesrv.business.be.BusinessLogic;
import it.csi.risca.riscabesrv.business.be.TipiModalitaPagApi;
import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.exception.SystemException;
import it.csi.risca.riscabesrv.dto.TipoModalitaPagDTO;

@Service
public class TipiModalitaPagApiServiceImpl extends BaseApiServiceImpl implements TipiModalitaPagApi {

	private static final String IDENTITY = "identity";
	
	@Autowired
	public BusinessLogic businessLogic;

	@Override
	public Response readTipoModalitaPagByCodTipoModalitaPag(String codTipoModalitaPag, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[TipiModalitaPagApiServiceImpl::readTipoModalitaPagByCodTipoModalitaPag] BEGIN");
		LOGGER.debug("[TipiModalitaPagApiServiceImpl::readTipoModalitaPagByCodTipoModalitaPag] Parametro in input codTipoModalitaPag :\n " + codTipoModalitaPag);

		try {

			TipoModalitaPagDTO tipoModalitaPag = businessLogic.readTipoModalitaPagByCodTipoModalitaPag(codTipoModalitaPag);
			ResponseBuilder rb = Response.ok(tipoModalitaPag);
			LOGGER.debug("[TipiModalitaPagApiServiceImpl : readTipoModalitaPagByCodTipoModalitaPag ] END");
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
	public Response loadAllTipiModalitaPagamenti(HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		 LOGGER.debug("[TipiModalitaPagApiServiceImpl::loadAllTipiModalitaPagamenti] BEGIN");
	        List<TipoModalitaPagDTO> tipoRicercaPagamentiList = new ArrayList<>();
			try {
				tipoRicercaPagamentiList = businessLogic.loadAllTipiModalitaPagamenti();
			}catch (Exception e) {
				buildErrorRepsonse(500, "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.",null) ;
			}
			LOGGER.debug("[TipiModalitaPagApiServiceImpl::loadAllTipiModalitaPagamenti] END");
	       return Response.ok(tipoRicercaPagamentiList).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

}

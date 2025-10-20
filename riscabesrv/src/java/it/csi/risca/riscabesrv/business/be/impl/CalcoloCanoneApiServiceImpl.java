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

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.risca.riscabesrv.business.be.CalcoloCanoneApi;
import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.impl.dao.CalcoloCanoneDAO;
import it.csi.risca.riscabesrv.dto.CalcoloCanoneDTO;
import it.csi.risca.riscabesrv.dto.CalcoloCanoneSingoloDTO;
import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.dto.ambiente.RiscossioneDatoTecnicoDTO;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type Calcolo Canone api service.
 *
 * @author CSI PIEMONTE
 */
@Component
public class CalcoloCanoneApiServiceImpl  extends BaseApiServiceImpl implements CalcoloCanoneApi {
	
	private static final String IDENTITY = "identity";
    private static Logger LOGGER = Logger.getLogger(Constants.LOGGER_NAME);

	  @Autowired
	  private CalcoloCanoneDAO calcoloCanoneDAO;

	    /**
	     * @param idRiscossione idRiscossione
	     * @param dataRiferimento dataRiferimento
	     * @param securityContext SecurityContext
	     * @param httpHeaders     HttpHeaders
	     * @param httpRequest     HttpServletRequest
	     * @return Response
	     */
		@Override
		public Response calcoloCanone(Long idRiscossione, String dataRiferimento, SecurityContext securityContext, HttpHeaders httpHeaders,
				HttpServletRequest httpRequest) {
	        LOGGER.debug("[CalcoloCanoneApiServiceImpl::calcoloCanone] BEGIN");
	        LOGGER.debug("[CalcoloCanoneApiServiceImpl::calcoloCanone] Parametro in input idRiscossione [" + idRiscossione + "]  dataRiferimento [" + dataRiferimento + "]");
	        CalcoloCanoneDTO canone;
			try {
				canone = calcoloCanoneDAO.calcoloCanone(idRiscossione, dataRiferimento);
				
			} catch (BusinessException be) {
				return handleBusinessException(404, be);
			} catch (Exception e) {
	            LOGGER.error("[CalcoloCanoneApiServiceImpl::calcoloCanone] ERROR :"+e);
				ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
	            return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
			}
	       
	        LOGGER.debug("[CalcoloCanoneApiServiceImpl::calcoloCanone] END");
	        return Response.ok(canone).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		}

		@Override
		public Response calcoloCanoneSingoloEFrazionato(RiscossioneDatoTecnicoDTO dto, String dataRiferimento, String dataFrazionamento, String flgFraz,
				SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
	        LOGGER.debug("[CalcoloCanoneApiServiceImpl::calcoloCanoneSingoloEFrazionato] BEGIN");
	        LOGGER.debug("[CalcoloCanoneApiServiceImpl::calcoloCanoneSingoloEFrazionato] Parametro in input dto: [" + dto + "], dataRiferimento: [" + dataRiferimento + "], dataFrazionamento: [" + dataFrazionamento + "],  flgFraz: [" + flgFraz + "]");
	        CalcoloCanoneSingoloDTO canone;
			try {
				canone = calcoloCanoneDAO.calcoloCanoneSingoloEFrazionato(dto, dataRiferimento, dataFrazionamento, flgFraz);
			} catch (BusinessException be) {
				return handleBusinessException(404, be);
			}catch (Exception e) {
	            LOGGER.error("[CalcoloCanoneApiServiceImpl::calcoloCanoneSingoloEFrazionato] ERROR :"+e);
				ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
	            return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
			}
	       
	        LOGGER.debug("[CalcoloCanoneApiServiceImpl::calcoloCanoneSingoloEFrazionato] END");
	        return Response.ok(canone).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		}

}

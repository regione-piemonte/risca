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

import it.csi.risca.riscabesrv.business.be.CalcoloNapApi;
import it.csi.risca.riscabesrv.business.be.impl.dao.AvvisoPagamentoDAO;
import it.csi.risca.riscabesrv.dto.CalcoloNapDTO;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type Calcolo Canone api service.
 *
 * @author CSI PIEMONTE
 */
@Component
public class CalcoloNapApiServiceImpl  extends BaseApiServiceImpl implements CalcoloNapApi {
	
	private static final String IDENTITY = "identity";
    private static Logger LOGGER = Logger.getLogger(Constants.LOGGER_NAME);

	  @Autowired
	  private AvvisoPagamentoDAO avvisoPagamentoDAO;

	    /**
	     * @param codiceDirezione codiceDirezione
	     * @param cifreAnno cifreAnno
	     * @param securityContext SecurityContext
	     * @param httpHeaders     HttpHeaders
	     * @param httpRequest     HttpServletRequest
	     * @return Response
	     */
		@Override
		public Response calcoloNap(String codiceDirezione, String cifreAnno, SecurityContext securityContext, HttpHeaders httpHeaders,
				HttpServletRequest httpRequest) {
	        LOGGER.debug("[CalcoloNapApiServiceImpl::calcoloNap] BEGIN");
	        
			Integer napProgress = avvisoPagamentoDAO.getMaxProgressivo(codiceDirezione + "-" + cifreAnno);
			Integer w_napProgress = avvisoPagamentoDAO.getMaxProgressivoWorking(codiceDirezione + "-" + cifreAnno);
			
			Integer n_progress = Integer.valueOf(napProgress.intValue() + 1);
			if(w_napProgress.intValue() > napProgress.intValue()) {
				n_progress = Integer.valueOf(w_napProgress.intValue() + 1);
			}
			
			//Padding con zero a sinistra fino a lunghezza 5
			String progressivo = String.format("%1$5s", n_progress).replace(' ', '0');
			
			CalcoloNapDTO nap = new CalcoloNapDTO();
			nap.setNap(codiceDirezione + "-" + cifreAnno + progressivo);
			nap.setProgressivo(progressivo);
	        
	        LOGGER.debug("[CalcoloNapApiServiceImpl::calcoloNap] END");
	        return Response.ok(nap).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		}

}

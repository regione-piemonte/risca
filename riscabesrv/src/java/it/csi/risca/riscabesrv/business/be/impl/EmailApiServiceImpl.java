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

import it.csi.risca.riscabesrv.business.be.EmailApi;
import it.csi.risca.riscabesrv.business.be.impl.dao.EmailServizioDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.EmailStPuntiValoriDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.EmailStSegnapostoDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.EmailStandardDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.OutputDatiDAO;
import it.csi.risca.riscabesrv.dto.BollResultEmailDTO;
import it.csi.risca.riscabesrv.dto.EmailServizioDTO;
import it.csi.risca.riscabesrv.dto.EmailStPuntiValoriDTO;
import it.csi.risca.riscabesrv.dto.EmailStSegnapostoDTO;
import it.csi.risca.riscabesrv.dto.EmailStandardDTO;
import it.csi.risca.riscabesrv.dto.OutputDatiDTO;
import it.csi.risca.riscabesrv.util.Constants;

@Component
public class EmailApiServiceImpl implements EmailApi {

	private static Logger LOGGER = Logger.getLogger(Constants.LOGGER_NAME);
	
	private static final String IDENTITY = "identity";

	@Autowired
	private EmailServizioDAO emailServizioDAO;

	@Autowired
	private EmailStandardDAO emailStandardDAO;

	@Autowired
	private OutputDatiDAO outputDatiDAO;

	@Autowired
	private EmailStPuntiValoriDAO emailStPuntiValoriDAO;

	@Autowired
	private EmailStSegnapostoDAO emailStSegnapostoDAO;

	@Override
	public Response loadEmailServizio(String codEmailServizio, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		EmailServizioDTO email = emailServizioDAO.loadEmailServizioByCodEmail(codEmailServizio);
		return Response.ok(email).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response loadEmailStandard(String codEmailStandard, Long idElabora, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[EmailApiServiceImpl::loadEmailStandard] BEGIN");

		BollResultEmailDTO resultEmailData = new BollResultEmailDTO();

		List<OutputDatiDTO> datiTitolare = outputDatiDAO.loadOutputDatiTitolare(idElabora);
		resultEmailData.setDatiTitolari(datiTitolare);

		EmailStandardDTO emailStandard = emailStandardDAO.loadEmailStandardByCodEmail(codEmailStandard);
		resultEmailData.setEmailStandard(emailStandard);

		List<EmailStPuntiValoriDTO> puntiValori = emailStPuntiValoriDAO
				.loadEmailStPuntiValoriByEmailFoglio(codEmailStandard, "Dati_Titolare");
		resultEmailData.setPuntiValori(puntiValori);

		List<EmailStSegnapostoDTO> segnapostoPdf = emailStSegnapostoDAO
				.loadEmailStSegnapostoByEmailSezione(emailStandard.getIdEmailStandard(), "PDF");
		List<EmailStSegnapostoDTO> segnapostoOggetto = emailStSegnapostoDAO
				.loadEmailStSegnapostoByEmailSezione(emailStandard.getIdEmailStandard(), "OGGETTO");
		List<EmailStSegnapostoDTO> segnapostoCorpo = emailStSegnapostoDAO
				.loadEmailStSegnapostoByEmailSezione(emailStandard.getIdEmailStandard(), "CORPO");
		resultEmailData.setSegnapostoPdf(segnapostoPdf);
		resultEmailData.setSegnapostoOggetto(segnapostoOggetto);
		resultEmailData.setSegnapostoCorpo(segnapostoCorpo);

		resultEmailData.setStatus("OK");

		LOGGER.debug("[EmailApiServiceImpl::loadEmailStandard] END");
		return Response.ok(resultEmailData).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

}

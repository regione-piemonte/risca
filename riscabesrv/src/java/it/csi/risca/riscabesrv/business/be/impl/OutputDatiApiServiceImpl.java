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

import it.csi.risca.riscabesrv.business.be.BusinessLogic;
import it.csi.risca.riscabesrv.business.be.OutputDatiApi;
import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.exception.ValidationException;
import it.csi.risca.riscabesrv.business.be.impl.dao.EntePagopaDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.OutputDatiDAO;
import it.csi.risca.riscabesrv.dto.EntePagopaDTO;
import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.dto.OutputDatiDTO;
import it.csi.risca.riscabesrv.dto.PagopaPosizioniDebitorieDTO;
import it.csi.risca.riscabesrv.util.BollUtils;
import it.csi.risca.riscabesrv.util.Constants;

@Component
public class OutputDatiApiServiceImpl extends BaseApiServiceImpl implements OutputDatiApi {

	private static final String IDENTITY = "identity";
	private static Logger LOGGER = Logger.getLogger(Constants.LOGGER_NAME);
	private static final Long ID_OUTPUT_FOGLIO_DATI_TITOLARE_BS = 56l;
	private static final Long ID_OUTPUT_FOGLIO_DATI_TITOLARE_BG = 75l;
	private static final Long ID_OUTPUT_FOGLIO_DATI_TITOLARE_BO = 40l;
	@Autowired
	private OutputDatiDAO outputDatiDAO;

	@Autowired
	private EntePagopaDAO entePagopaDAO;

	@Autowired
	public BusinessLogic businessLogic;

	@Override
	public Response salvaOutputDati(OutputDatiDTO outputDati,String fruitore, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		OutputDatiDTO ret = null;
		try {
			setGestAttoreInsUpd(outputDati, fruitore, httpRequest, httpHeaders);
			businessLogic.validatorDTO(outputDati, null, null);

			ret = outputDatiDAO.saveOutputDati(outputDati);
		} catch (ValidationException ve) {
			LOGGER.debug("BAD REQUEST: MESSAGE:" + ve.getMessage());
			Integer status = Response.Status.BAD_REQUEST.getStatusCode();
			ErrorDTO err = new ErrorDTO(status.toString(), Constants.E003, ve.getMessage(), null, null);
			return Response.status(Response.Status.BAD_REQUEST).entity(err).build();
		}  catch (DAOException e) {
			LOGGER.debug("[OutputDatiApiServiceImpl: salvaOutputDati ] Error - " + e.getMessage());
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
		} catch (Exception e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR).status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
		} 
		return Response.ok(ret).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response loadNomiLotto(Long idElabora, String codTipoElabora, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {

		Long idOutputFoglio = BollUtils.getIdFoglioTitolare(codTipoElabora);
		List<String> nomiLotto = outputDatiDAO.loadNomiLotto(idElabora, idOutputFoglio);
		return Response.ok(nomiLotto).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response loadPosizioniDebitorieByNomeLotto(String nomeLotto, Long idAmbito, Boolean bollettazioneSpeciale,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {

		EntePagopaDTO entePagopa = entePagopaDAO.loadEntePagopaPerIuvByAmbito(idAmbito);
		String causale = entePagopa.getCausale();

		// Leggere i dati delle posizioni debitorie da inviare a PAGOPA dalla tabella
		// RISCA_W_OUTPUT_DATI e dalla RISCA_T_SOGGETTO
		List<PagopaPosizioniDebitorieDTO> posizioniDebitorie = outputDatiDAO
				.loadPosizioniDebitorieByNomeLotto(nomeLotto, causale, bollettazioneSpeciale);

		return Response.ok(posizioniDebitorie).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response loadDatiByFoglio(Long idElabora, Long idOutputFoglio, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		List<OutputDatiDTO> datiFoglio = outputDatiDAO.loadOutputDatiByFoglio(idElabora, idOutputFoglio, null);
		return Response.ok(datiFoglio).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

}

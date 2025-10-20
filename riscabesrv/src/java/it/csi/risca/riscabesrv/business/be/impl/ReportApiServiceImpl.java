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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.iride2.policy.entity.Identita;
import it.csi.risca.riscabesrv.business.be.BusinessLogic;
import it.csi.risca.riscabesrv.business.be.ReportApi;
import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.exception.DatiInputErratiException;
import it.csi.risca.riscabesrv.business.be.impl.dao.AmbitiDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.impl.ambiente.ReportAmbienteDAOImpl;
import it.csi.risca.riscabesrv.dto.AmbitoDTO;
import it.csi.risca.riscabesrv.dto.ElaboraDTO;
import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.dto.ErrorObjectDTO;
import it.csi.risca.riscabesrv.dto.ReportResultDTO;
import it.csi.risca.riscabesrv.dto.RiscossioneSearchDTO;
import it.csi.risca.riscabesrv.identitadigitale.IdentitaDigitaleManager;
import it.csi.risca.riscabesrv.util.Constants;
import it.csi.risca.riscabesrv.util.ErrorMessages;

@Component
public class ReportApiServiceImpl extends BaseApiServiceImpl implements ReportApi {

	private static final String IDENTITY = "identity";
	private static Logger LOGGER = Logger.getLogger(Constants.LOGGER_NAME);
	private static final String DES_TIPO_ELABORA ="des_tipo_elabora";

	private static final String RRM="Report Ricerca Morosita'";
	private static final String RRR ="Report Ricerca Rimborsi";
	private static final String RRAU=" Report Ricerca avanzata utenze";

	@Autowired
	private AmbitiDAO ambitiDAO;
	
	@Autowired
	private IdentitaDigitaleManager identitaDigitaleManager;
	
	@Autowired
	private ReportAmbienteDAOImpl reportAmbienteDaoImpl;
	
	@Override
	public Response creaReportRicercaAvanzata(RiscossioneSearchDTO riscossioneSearch, String modalitaRicerca,
			String fruitore, SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest){
		LOGGER.debug("[ReportApiServiceImpl::creaReportRicercaAvanzata] BEGIN");
		ReportResultDTO reportResult = null;
		ElaboraDTO elabora= null;
		try {

			LOGGER.debug("[ReportApiServiceImpl::creaReportRicercaAvanzata] verificaIdentitaDigitale BEGIN");
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders, Constants.LOAD_RISC);
			LOGGER.debug("[ReportApiServiceImpl::creaReportRicercaAvanzata] verificaIdentitaDigitale END");
			Identita identita = IdentitaDigitaleManager.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders), fruitore);
			LOGGER.debug("[ReportApiServiceImpl::creaReportRicercaAvanzata] getAmbitoByIdentitaOrFonte BEGIN");
			Long idAmbito = identitaDigitaleManager.getAmbitoByIdentitaOrFonte(identita, fruitore, null);
		    String competenzaTerritoriale = identitaDigitaleManager.getCompetenzaTerritoriale(identita);
		    LOGGER.info("[ReportApiServiceImpl::creaReportRicercaAvanzata] competenzaTerritoriale: "+competenzaTerritoriale);
		    riscossioneSearch.setCompetenzaTerritoriale(competenzaTerritoriale);
			LOGGER.debug("[ReportApiServiceImpl::creaReportRicercaAvanzata] getAmbitoByIdentitaOrFonte END");
			AmbitoDTO ambito = ambitiDAO.loadAmbitoByIdAmbito(idAmbito);
			String attore = identita != null ? identita.getCodFiscale() : httpHeaders.getHeaderString(Constants.ATTORE_RISCA);
			 
			switch (ambito.getCodAmbito()) {
			  case Constants.AMBIENTE:
				  reportResult = reportAmbienteDaoImpl.creaReportRicercaAvanzata(elabora, riscossioneSearch, ambito, modalitaRicerca, attore);
			    break;
			  case Constants.OPERE_PUBBLICHE:
				//TO DO
			    break;
			  case Constants.ATTIVITA_ESTRATTIVE:
				//TO DO
			    break;
			  case Constants.TRIBUTI:
			    //TO DO
				  break;
			}

		} catch (BusinessException be) {
		    return  handleBusinessException(be, RRAU);
		} catch (Exception e) {
			return   handleUnexpectedException(e, RRAU);
		}

		LOGGER.debug("[ReportApiServiceImpl::creaReportRicercaAvanzata] END");
		return Response.ok(reportResult).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response creaReportRicercaMorosita(String fruitore, String tipoRicercaMorosita, Integer anno,
			Integer flgRest, Integer flgAnn, String lim, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		LOGGER.debug("[ReportApiServiceImpl::creaReportRicercaMorosita] BEGIN");
		ReportResultDTO reportResult = null;
		ElaboraDTO elabora =null;
		try {

			LOGGER.debug("[ReportApiServiceImpl::creaReportRicercaMorosita] verificaIdentitaDigitale BEGIN");
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders, Constants.LOAD_PAGA);
			LOGGER.debug("[ReportApiServiceImpl::creaReportRicercaMorosita] verificaIdentitaDigitale END");
			Identita identita = IdentitaDigitaleManager.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders), fruitore);
			LOGGER.debug("[ReportApiServiceImpl::creaReportRicercaMorosita] getAmbitoByIdentitaOrFonte BEGIN");
			Long idAmbito = identitaDigitaleManager.getAmbitoByIdentitaOrFonte(identita, fruitore, null);
			LOGGER.debug("[ReportApiServiceImpl::creaReportRicercaMorosita] getAmbitoByIdentitaOrFonte END");
			AmbitoDTO ambito = ambitiDAO.loadAmbitoByIdAmbito(idAmbito);
			String attore = identita != null ? identita.getCodFiscale() : httpHeaders.getHeaderString(Constants.ATTORE_RISCA);
			switch (ambito.getCodAmbito()) {
			  case Constants.AMBIENTE:
				  reportResult = reportAmbienteDaoImpl.creaReportRicercaMorosita(elabora,tipoRicercaMorosita,anno, flgRest,flgAnn, lim, ambito, attore);
			    break;
			  case Constants.OPERE_PUBBLICHE:
				//TO DO
			    break;
			  case Constants.ATTIVITA_ESTRATTIVE:
				//TO DO
			    break;
			  case Constants.TRIBUTI:
			    //TO DO
				  break;
			}
		
		} catch (BusinessException be) {
			return    handleBusinessException(be, RRM);
		} catch (Exception e) {
			return handleUnexpectedException(e, RRM);
		}

		LOGGER.debug("[ReportApiServiceImpl::creaReportRicercaMorosita] END");
		return Response.ok(reportResult).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}
	@Override
	public Response creaReportRicercaRimborsi(String fruitore, String tipoRicercaRimborsi, Integer anno,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest){
		LOGGER.debug("[ReportApiServiceImpl::creaReportRicercaRimborsi] BEGIN");
		ReportResultDTO reportResult = new ReportResultDTO();
		ElaboraDTO elabora = null;
		try {

			LOGGER.debug("[ReportApiServiceImpl::creaReportRicercaRimborsi] verificaIdentitaDigitale BEGIN");
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders, Constants.LOAD_PAGA);
			LOGGER.debug("[ReportApiServiceImpl::creaReportRicercaRimborsi] verificaIdentitaDigitale END");
			Identita identita = IdentitaDigitaleManager.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders), fruitore);
			LOGGER.debug("[ReportApiServiceImpl::creaReportRicercaRimborsi] getAmbitoByIdentitaOrFonte BEGIN");
			Long idAmbito = identitaDigitaleManager.getAmbitoByIdentitaOrFonte(identita, fruitore, null);
			LOGGER.debug("[ReportApiServiceImpl::creaReportRicercaRimborsi] getAmbitoByIdentitaOrFonte END");
			AmbitoDTO ambito = ambitiDAO.loadAmbitoByIdAmbito(idAmbito);
			String attore = identita != null ? identita.getCodFiscale() : httpHeaders.getHeaderString(Constants.ATTORE_RISCA);
		
			switch (ambito.getCodAmbito()) {
			  case Constants.AMBIENTE:
				  reportResult = reportAmbienteDaoImpl.creaReportRicercaRimborsi(elabora,tipoRicercaRimborsi, anno, ambito, attore);
			    break;
			  case Constants.OPERE_PUBBLICHE:
				//TO DO
			    break;
			  case Constants.ATTIVITA_ESTRATTIVE:
				//TO DO
			    break;
			  case Constants.TRIBUTI:
			    //TO DO
				  break;
			}
		
			
		} catch (BusinessException be) {
			return  handleBusinessException(be, RRR);
		} catch (Exception e) {
			return  handleUnexpectedException(e, RRR);
		}

		LOGGER.debug("[ReportApiServiceImpl::creaReportRicercaRimborsi] END");
		return Response.ok(reportResult).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	private Response handleBusinessException(BusinessException be, String  desTipoElabora) {
	    Map<String, String> detail = createDetailMap(desTipoElabora);
	    be.setDetail(detail);
	    return handleBusinessException(be.getHttpStatus(), be);
	}

	private Response handleUnexpectedException(Exception e, String  desTipoElabora) {
	    LOGGER.error("[" + getClass().getSimpleName() + "::" + Thread.currentThread().getStackTrace()[2].getMethodName() + "] ERROR : "+ e);
	    ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta.", null, null);
	    Map<String, String> detail = createDetailMap(desTipoElabora);
	    err.setDetail(detail);
	    return  Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
	}

	private Map<String, String> createDetailMap(String  desTipoElabora) {
	    Map<String, String> detail = new HashMap<>();
	    detail.put(DES_TIPO_ELABORA, desTipoElabora);
	    return detail;
	}


	@Override
	public Response creaReportBilancio(String fruitore, Integer anno, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[ReportApiServiceImpl::creaReportBilancio] BEGIN");
		ReportResultDTO reportResult = new ReportResultDTO();
		try {

			LOGGER.debug("[ReportApiServiceImpl::creaReportBilancio] verificaIdentitaDigitale BEGIN");
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders, Constants.LOAD_PAGA);
			LOGGER.debug("[ReportApiServiceImpl::creaReportBilancio] verificaIdentitaDigitale END");
			Identita identita = IdentitaDigitaleManager
					.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders), fruitore);
			LOGGER.debug("[ReportApiServiceImpl::creaReportBilancio] getAmbitoByIdentitaOrFonte BEGIN");
			Long idAmbito = identitaDigitaleManager.getAmbitoByIdentitaOrFonte(identita, fruitore, null);
			LOGGER.debug("[ReportApiServiceImpl::creaReportBilancio] getAmbitoByIdentitaOrFonte END");
			AmbitoDTO ambito = ambitiDAO.loadAmbitoByIdAmbito(idAmbito);
			String attore = identita != null ? identita.getCodFiscale()
					: httpHeaders.getHeaderString(Constants.ATTORE_RISCA);

			switch (ambito.getCodAmbito()) {
			case Constants.AMBIENTE:
				reportResult = reportAmbienteDaoImpl.creaReportBilancio(anno, ambito,
						attore);
				break;
			case Constants.OPERE_PUBBLICHE:
				// TO DO
				break;
			case Constants.ATTIVITA_ESTRATTIVE:
				// TO DO
				break;
			case Constants.TRIBUTI:
				// TO DO
				break;
			}

		} catch (BusinessException be) {
			return handleBusinessException(be, RRR);
		} catch (Exception e) {
			return handleUnexpectedException(e, RRR);
		}

		LOGGER.debug("[ReportApiServiceImpl::creaReportBilancio] END");
		return Response.ok(reportResult).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}
	
	}

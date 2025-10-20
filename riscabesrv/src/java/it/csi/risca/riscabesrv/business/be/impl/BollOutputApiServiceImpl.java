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

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.risca.riscabesrv.business.be.BollOutputApi;
import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.impl.dao.AnnualitaSdDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.AvvisoAnnualitaDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.AvvisoDatiAmminDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.AvvisoUsoDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.OutputColonnaDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.OutputDatiDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.OutputFileDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.OutputFoglioDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RegistroElaboraDAO;
import it.csi.risca.riscabesrv.dto.AnnualitaSdDTO;
import it.csi.risca.riscabesrv.dto.AvvisoAnnualitaDTO;
import it.csi.risca.riscabesrv.dto.AvvisoDatiAmminDTO;
import it.csi.risca.riscabesrv.dto.AvvisoDatiTitolareDTO;
import it.csi.risca.riscabesrv.dto.AvvisoUsoDTO;
import it.csi.risca.riscabesrv.dto.BollOutputDatiDTO;
import it.csi.risca.riscabesrv.dto.BollResultDTO;
import it.csi.risca.riscabesrv.dto.OutputColonnaDTO;
import it.csi.risca.riscabesrv.dto.OutputDatiDTO;
import it.csi.risca.riscabesrv.dto.OutputFileDTO;
import it.csi.risca.riscabesrv.dto.OutputFoglioDTO;
import it.csi.risca.riscabesrv.dto.RegistroElaboraDTO;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type BollOutputApiServiceImpl api service.
 *
 * @author CSI PIEMONTE
 */
@Component
public class BollOutputApiServiceImpl extends BaseApiServiceImpl implements BollOutputApi {

	private static final String IDENTITY = "identity";
	private static Logger LOGGER = Logger.getLogger(Constants.LOGGER_NAME);

	private static final String ATTORE = "riscabatchspec";
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	private static final String DATE_FORMAT_SPED = "dd/MM/yyyy";
	private static final String MODALITA_INVIO_CARTA = "CARTA";
	
	private static final String PASSO_INSERT_TIPREC2 = "INSERT_TIPREC2";
	private static final String PASSO_INSERT_TIPREC3 = "INSERT_TIPREC3";
	private static final String PASSO_INSERT_TIPREC4 = "INSERT_TIPREC4";
	private static final String PASSO_INSERT_TIPREC5 = "INSERT_TIPREC5";

	private static final String PASSO_SET_AVVISO_E_IUV = "SET_AVVISO_E_IUV";
	private static final String PASSO_INVIA_MAILING="PASSO_INVIA_MAILING";

	private static final String COD_TIPO_PASSO_ELABORA_ELAB_EMISSIONE = "ELAB_EMISSIONE";

	private static final String FASE_EMISSIONE = "Emissione";

	private static final String COD_FASE_EMISBS = "EMISBS";
	private static final String COD_FASE_EMISBO = "EMISBO";
	private static final String COD_FASE_CONFBS_CREA_FILE = "CONFBS_CREA_FILE";

	private static final String COD_TIPO_ELABORA_BS = "BS";
	private static final String COD_TIPO_ELABORA_BO = "BO";

	private static final Map<String, String[]> outputFoglio;

	static {
		HashMap<String, String[]> aMap = new HashMap<String, String[]>();
		aMap.put("BO", new String[] { "39", "40", "41", "42", "43", "44", "45", "46" });
		aMap.put("BS", new String[] { "55", "56", "57", "58", "59", "60", "61", "62" });
		aMap.put("BG", new String[] { "74", "75", "76", "77", "78", "79", "80", "81" });
		outputFoglio = Collections.unmodifiableMap(aMap);
	}

	@Autowired
	private RegistroElaboraDAO registroElaboraDAO;

	@Autowired
	private AvvisoDatiAmminDAO avvisoDatiAmminDAO;

	@Autowired
	private AvvisoAnnualitaDAO avvisoAnnualitaDAO;

	@Autowired
	private AvvisoUsoDAO avvisoUsoDAO;

	@Autowired
	private AnnualitaSdDAO annualitaSdDAO;

	@Autowired
	private OutputDatiDAO outputDatiDAO;

	@Autowired
	private OutputFileDAO outputFileDAO;

	@Autowired
	private OutputFoglioDAO outputFoglioDAO;

	@Autowired
	private OutputColonnaDAO outputColonnaDAO;

	@Override
	public Response salvaDatiOutputBo(BollOutputDatiDTO bollOutputDati, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[BollOutputApiServiceImpl::salvaDatiOutput] BEGIN");
		BollResultDTO bsResult = saveOutputDati(bollOutputDati, "BO");
		LOGGER.debug("[BollOutputApiServiceImpl::salvaDatiOutput] END");
		return Response.ok(bsResult).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response salvaDatiOutputBs(BollOutputDatiDTO bollOutputDati, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[BollOutputApiServiceImpl::salvaDatiOutput] BEGIN");
		BollResultDTO bsResult = saveOutputDati(bollOutputDati, "BS");
		LOGGER.debug("[BollOutputApiServiceImpl::salvaDatiOutput] END");
		return Response.ok(bsResult).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}
	
	@Override
	public Response salvaDatiOutputBg(BollOutputDatiDTO bollOutputDati, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[BollOutputApiServiceImpl::salvaDatiOutput] BEGIN");
		BollResultDTO bsResult = saveOutputDati(bollOutputDati, "BG");
		LOGGER.debug("[BollOutputApiServiceImpl::salvaDatiOutput] END");
		return Response.ok(bsResult).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	private BollResultDTO saveOutputDati(BollOutputDatiDTO bollOutputDati, String tipoBoll) {
		BollResultDTO bsResult = new BollResultDTO();
		String fase = FASE_EMISSIONE;

		int progrXlsDatiTitolare = bollOutputDati.getProgrXlsDatiTitolare();
		int progrXlsDatiAmmin = bollOutputDati.getProgrXlsDatiAmmin();
		int progrXlsDatiUsi = bollOutputDati.getProgrXlsDatiUsi();
		int progrXlsDatiAnnualita = bollOutputDati.getProgrXlsDatiAnnualita();
		int progrTxtCompleto = bollOutputDati.getProgrTxtCompleto();
		int progrTxtParzCarta = bollOutputDati.getProgrTxtParzCarta();

		AvvisoDatiTitolareDTO avvisoDatiTitolare = bollOutputDati.getAvvisoDatiTitolare();
		Long idElabora = bollOutputDati.getIdElabora();
		String dtProtocollo = bollOutputDati.getDtProtocollo();
		String anno = bollOutputDati.getAnno();

		// Inserimento DATI_TITOLARE ---> Record Tipo 2

		String soggettoGruppo = "" + avvisoDatiTitolare.getIdTitolare();
		soggettoGruppo = avvisoDatiTitolare.getIdGruppoSoggetto() != null
				? soggettoGruppo + "*" + avvisoDatiTitolare.getIdGruppoSoggetto()
				: soggettoGruppo;

		try {
			outputDatiDAO.saveOutputDati(getRecordDatiODSTipo2(idElabora, progrXlsDatiTitolare, dtProtocollo,
					avvisoDatiTitolare, tipoBoll, soggettoGruppo));
			outputDatiDAO.saveOutputDati(getRecordDatiTxtCompletoTipo2(idElabora, progrTxtCompleto, dtProtocollo,
					avvisoDatiTitolare, tipoBoll, soggettoGruppo));
			if (avvisoDatiTitolare.getModalitaInvio().equalsIgnoreCase(MODALITA_INVIO_CARTA)) {
				progrTxtParzCarta++;
				outputDatiDAO.saveOutputDati(getRecordDatiTxtCartaTipo2(idElabora, progrTxtParzCarta, dtProtocollo,
						avvisoDatiTitolare, tipoBoll, soggettoGruppo));
			}
		} catch (DAOException e) {
			return errorResult(bsResult, PASSO_INSERT_TIPREC2);
		}

		// Inserimento DATI_AMMIN ---> Record Tipo 3
		List<AvvisoDatiAmminDTO> avvisoDatiAmminList = avvisoDatiAmminDAO
				.loadAvvisoDatiAmminWorkingByNap(avvisoDatiTitolare.getNap());

		for (AvvisoDatiAmminDTO avvisoDatiAmmin : avvisoDatiAmminList) {
			progrXlsDatiAmmin++;
			progrTxtCompleto++;
			Date dataScadenzaEmasIso = avvisoDatiAmmin.getDataScadEmasIso();

			try {
				outputDatiDAO.saveOutputDati(getRecordDatiODSTipo3(idElabora, progrXlsDatiAmmin, dtProtocollo,
						soggettoGruppo, avvisoDatiAmmin, dataScadenzaEmasIso, anno, tipoBoll));
				outputDatiDAO.saveOutputDati(getRecordDatiTxtCompletoTipo3(idElabora, progrTxtCompleto, dtProtocollo,
						soggettoGruppo, avvisoDatiAmmin, dataScadenzaEmasIso, anno, tipoBoll));
				if (avvisoDatiTitolare.getModalitaInvio().equalsIgnoreCase(MODALITA_INVIO_CARTA)) {
					progrTxtParzCarta++;
					outputDatiDAO.saveOutputDati(getRecordDatiTxtCartaTipo3(idElabora, progrTxtParzCarta, dtProtocollo,
							soggettoGruppo, avvisoDatiAmmin, dataScadenzaEmasIso, anno, tipoBoll));
				}
			} catch (DAOException e) {
				return errorResult(bsResult, PASSO_INSERT_TIPREC3);
			}
		}

		// Inserimento ANNUALITA ---> Record Tipo 4
		List<AvvisoAnnualitaDTO> avvisoAnnualitaList = avvisoAnnualitaDAO
				.loadAvvisoAnnualitaWorkingByNap(avvisoDatiTitolare.getNap());

		for (AvvisoAnnualitaDTO avvisoAnnualita : avvisoAnnualitaList) {
			progrXlsDatiAnnualita++;
			progrTxtCompleto++;

			int flgRateoPrimaAnnualita = 0;
			int numeroMesi = 0;
			if (tipoBoll.equals("BS") || tipoBoll.equals("BG")) {
				AnnualitaSdDTO annualitaSd = annualitaSdDAO.loadAnnualitaSdByNapCodiceUtenzaAnno(
						avvisoAnnualita.getNap(), avvisoAnnualita.getCodiceUtenza(),
						avvisoAnnualita.getAnnoRichPagamento());
				flgRateoPrimaAnnualita = annualitaSd.getFlgRateoPrimaAnnualita();
				numeroMesi = annualitaSd.getNumeroMesi();
			}

			try {
				outputDatiDAO.saveOutputDati(getRecordDatiODSTipo4(idElabora, progrXlsDatiAnnualita, soggettoGruppo,
						avvisoAnnualita, flgRateoPrimaAnnualita, numeroMesi, tipoBoll));
				outputDatiDAO.saveOutputDati(getRecordDatiTxtCompletoTipo4(idElabora, progrTxtCompleto, soggettoGruppo,
						avvisoAnnualita, flgRateoPrimaAnnualita, numeroMesi, tipoBoll));
				if (avvisoDatiTitolare.getModalitaInvio().equalsIgnoreCase(MODALITA_INVIO_CARTA)) {
					progrTxtParzCarta++;
					outputDatiDAO.saveOutputDati(getRecordDatiTxtCartaTipo4(idElabora, progrTxtParzCarta,
							soggettoGruppo, avvisoAnnualita, flgRateoPrimaAnnualita, numeroMesi, tipoBoll));
				}
			} catch (DAOException e) {
				return errorResult(bsResult, PASSO_INSERT_TIPREC4);
			}
		}

		// Inserimento USI ---> Record Tipo 5
		List<AvvisoUsoDTO> avvisoUsoList = avvisoUsoDAO.loadAvvisoUsoWorkingByNap(avvisoDatiTitolare.getNap());

		for (AvvisoUsoDTO avvisoUso : avvisoUsoList) {
			progrXlsDatiUsi++;
			progrTxtCompleto++;

			try {
				outputDatiDAO.saveOutputDati(
						getRecordDatiODSTipo5(idElabora, progrXlsDatiUsi, soggettoGruppo, avvisoUso, tipoBoll));
				outputDatiDAO.saveOutputDati(getRecordDatiTxtCompletoTipo5(idElabora, progrTxtCompleto, soggettoGruppo,
						avvisoUso, tipoBoll));
				if (avvisoDatiTitolare.getModalitaInvio().equalsIgnoreCase(MODALITA_INVIO_CARTA)) {
					progrTxtParzCarta++;
					outputDatiDAO.saveOutputDati(getRecordDatiTxtCartaTipo5(idElabora, progrTxtParzCarta,
							soggettoGruppo, avvisoUso, tipoBoll));
				}
			} catch (DAOException e) {
				return errorResult(bsResult, PASSO_INSERT_TIPREC5);
			}

//			logStep(idElabora, PASSO_INSERT_TIPREC5, 0,
//					fase + " - SalvataggioDatiOutput - Inserimento tipo record da 2 a 5 - Soggetto: " + soggettoGruppo
//							+ " - NAP: " + avvisoDatiTitolare.getNap(),
//					tipoBoll.equals("BS") ? COD_FASE_EMISBS : COD_FASE_EMISBO);
		}
		
		bsResult.setProgrXlsDatiTitolare(progrXlsDatiTitolare);
		bsResult.setProgrXlsDatiAmmin(progrXlsDatiAmmin);
		bsResult.setProgrXlsDatiUsi(progrXlsDatiUsi);
		bsResult.setProgrXlsDatiAnnualita(progrXlsDatiAnnualita);
		bsResult.setProgrTxtCompleto(progrTxtCompleto);
		bsResult.setProgrTxtParzCarta(progrTxtParzCarta);

		bsResult.setStatus("OK");
		return bsResult;
	}

	@Override
	public Response updateDatiOutputIuv(Long idElabora, String codTipoElabora, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[BollOutputApiServiceImpl::updateDatiOutputIuv] BEGIN");
		BollResultDTO bsResult = new BollResultDTO();

		try {
			outputDatiDAO.updateOutputDatiIuvCodiceAvviso(idElabora, codTipoElabora);
		} catch (DAOException e) {
			bsResult = errorResult(bsResult, PASSO_SET_AVVISO_E_IUV);
			return Response.ok(bsResult).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		}

		logStep(idElabora, PASSO_SET_AVVISO_E_IUV, 0,
				"Conferma-CreazioneTXT - Step 2 -Inserimento IUV e CODICE AVVISO OK per l'elaborazione: " + idElabora,
				COD_FASE_CONFBS_CREA_FILE);

		bsResult.setStatus("OK");
		LOGGER.debug("[BollOutputApiServiceImpl::updateDatiOutputIuv] END");
		return Response.ok(bsResult).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response updateDatiOutputPecMail(Long idElabora, Integer progressivo, Long idOutputFoglio,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[BollOutputApiServiceImpl::updateDatiOutputPecMail] BEGIN");
		BollResultDTO bsResult = new BollResultDTO();

		try {
			outputDatiDAO.updateOutputDatiPecMail(idElabora, progressivo, idOutputFoglio);
		} catch (DAOException e) {
			bsResult = errorResult(bsResult, PASSO_INVIA_MAILING);
			return Response.ok(bsResult).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		}

		bsResult.setStatus("OK");
		LOGGER.debug("[BollOutputApiServiceImpl::updateDatiOutputPecMail] END");
		return Response.ok(bsResult).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response loadOutputFileDataOds(Long idElabora, Long idOutputFile, String codTipoElabora,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[BollOutputApiServiceImpl::loadOutputFileDataOds] BEGIN");

		OutputFileDTO outFile = outputFileDAO.loadOutputFile(idOutputFile);

		List<OutputFoglioDTO> outFogli = null;
		try {
			outFogli = outputFoglioDAO.loadOutputFoglioByFile(outFile.getIdOutputFile());
			for (OutputFoglioDTO foglio : outFogli) {
				List<OutputColonnaDTO> outColonne = outputColonnaDAO.loadOutputColonnaByFoglio(foglio.getIdOutputFoglio());
				foglio.setColonne(outColonne);
				List<OutputDatiDTO> dati = outputDatiDAO.loadOutputDatiByFoglio(idElabora, foglio.getIdOutputFoglio(),
						codTipoElabora);
				foglio.setDati(dati);
			}
			
			
		} catch (Exception e) {
			LOGGER.error("[BollOutputApiServiceImpl::loadOutputFileDataOds] Exception " + e.getMessage());
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR).status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
         }
		

		outFile.setFogli(outFogli);
		LOGGER.debug("[BollOutputApiServiceImpl::loadOutputFileDataOds] END");
		return Response.ok(outFile).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response loadOutputFileDataTxt(Long idElabora, Long idOutputFile, Long idOutputFoglio, String codTipoElabora,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[BollOutputApiServiceImpl::loadOutputFileDataTxt] BEGIN");

		OutputFileDTO outFile = outputFileDAO.loadOutputFile(idOutputFile);

		List<OutputFoglioDTO> outFogli = null;
		List<OutputColonnaDTO> outColonne = null;
		try {
			outFogli = outputFoglioDAO.loadOutputFoglioByFile(outFile.getIdOutputFile());
			outColonne = outputColonnaDAO.loadOutputColonnaByFoglio(idOutputFoglio);
		}  catch (Exception e) {
			LOGGER.error("[BollOutputApiServiceImpl::loadOutputFileDataTxt] Exception " + e.getMessage());
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR).status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();

		}
		List<OutputDatiDTO> dati = outputDatiDAO.loadOutputDatiByFoglio(idElabora, idOutputFoglio,
				codTipoElabora);

		outFogli.get(0).setColonne(outColonne);
		outFogli.get(0).setDati(dati);

		outFile.setFogli(outFogli);
		LOGGER.debug("[BollOutputApiServiceImpl::loadOutputFileDataTxt] END");
		return Response.ok(outFile).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}
	
	@Override
	public Response loadOutputFileReport(String codAmbito, String codTipoElabora, String nomeFile,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[BollOutputApiServiceImpl::loadOutputFileReport] BEGIN");

		OutputFileDTO outFile = null;
		try {
			outFile = outputFileDAO.loadOutputFileByAmbitoTipoElabNomeFile(codAmbito, codTipoElabora,
					nomeFile);
		} catch (Exception e) {
			LOGGER.error("[BollOutputApiServiceImpl::loadOutputFileReport] Exception " + e.getMessage());
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR).status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
		}

		LOGGER.debug("[BollOutputApiServiceImpl::loadOutputFileReport] END");
		return Response.ok(outFile).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	private OutputDatiDTO getRecordDatiTxtCartaTipo5(Long idElabora, int progressivo, String titolare,
			AvvisoUsoDTO avvisoUso, String tipoBoll) {
		OutputDatiDTO dto = new OutputDatiDTO();
		dto.setIdElabora(idElabora);
		dto.setProgressivo(progressivo);
		dto.setIdOutputFoglio(Long.parseLong(outputFoglio.get(tipoBoll)[7]));
		dto.setCodTipoPassoElabora(COD_TIPO_PASSO_ELABORA_ELAB_EMISSIONE);
		dto.setValoreColonna1(getDatiUsiString(titolare, avvisoUso));
		return dto;
	}

	private OutputDatiDTO getRecordDatiTxtCompletoTipo5(Long idElabora, int progressivo, String titolare,
			AvvisoUsoDTO avvisoUso, String tipoBoll) {
		OutputDatiDTO dto = new OutputDatiDTO();
		dto.setIdElabora(idElabora);
		dto.setProgressivo(progressivo);
		dto.setIdOutputFoglio(Long.parseLong(outputFoglio.get(tipoBoll)[6]));
		dto.setCodTipoPassoElabora(COD_TIPO_PASSO_ELABORA_ELAB_EMISSIONE);
		dto.setValoreColonna1(getDatiUsiString(titolare, avvisoUso));
		return dto;
	}

	private String getDatiUsiString(String titolare, AvvisoUsoDTO avvisoUso) {
		StringBuilder sb = new StringBuilder();
		sb.append("5").append(";");
		sb.append(titolare).append(";");
		sb.append(formatString(avvisoUso.getCodiceUtenza())).append(";");
		sb.append("" + avvisoUso.getAnnoRichPagamento()).append(";");
		sb.append(formatString(avvisoUso.getCondizioniParticolariCalc())).append(";");
		sb.append(formatString(avvisoUso.getUsoDenominazionePCalc())).append(";");
		sb.append(formatString(avvisoUso.getUnitaMisPCalc())).append(";");
		sb.append(formatQuantitaTxt(avvisoUso.getQuantitaPCalc())).append(";");
		sb.append(formatString(avvisoUso.getUnitaDiMisuraPCalc())).append(";");
		sb.append(formatBigDecimalTxt(avvisoUso.getCanoneUnitarioPCalc())).append(";");
		sb.append(formatBigDecimalTxt(avvisoUso.getCanoneUsoPCalc())).append(";");
		sb.append(formatBigDecimalTxt5(avvisoUso.getPercFaldaProf())).append(";");
		return sb.toString();
	}

	private OutputDatiDTO getRecordDatiODSTipo5(Long idElabora, int progressivo, String titolare,
			AvvisoUsoDTO avvisoUso, String tipoBoll) {
		OutputDatiDTO dto = new OutputDatiDTO();
		dto.setIdElabora(idElabora);
		dto.setProgressivo(progressivo);
		dto.setIdOutputFoglio(Long.parseLong(outputFoglio.get(tipoBoll)[4]));
		dto.setCodTipoPassoElabora(COD_TIPO_PASSO_ELABORA_ELAB_EMISSIONE);
		dto.setValoreColonna1("5");
		dto.setValoreColonna2(titolare);
		dto.setValoreColonna3(formatString(avvisoUso.getCodiceUtenza()));
		dto.setValoreColonna4("" + avvisoUso.getAnnoRichPagamento());
		dto.setValoreColonna5(formatString(avvisoUso.getCondizioniParticolariCalc()));
		dto.setValoreColonna6(formatString(avvisoUso.getUsoDenominazionePCalc()));
		dto.setValoreColonna7(formatString(avvisoUso.getUnitaMisPCalc()));
		dto.setValoreColonna8(formatQuantita(avvisoUso.getQuantitaPCalc()));
		dto.setValoreColonna9(formatString(avvisoUso.getUnitaDiMisuraPCalc()));
		dto.setValoreColonna10(formatBigDecimal(avvisoUso.getCanoneUnitarioPCalc()));
		dto.setValoreColonna11(formatBigDecimal(avvisoUso.getCanoneUsoPCalc()));
		dto.setValoreColonna12(formatBigDecimal(avvisoUso.getPercFaldaProf()));
		return dto;
	}

	private OutputDatiDTO getRecordDatiTxtCartaTipo4(Long idElabora, int progressivo, String titolare,
			AvvisoAnnualitaDTO avvisoAnnualita, int flgRateoPrimaRata, int numeroMesi, String tipoBoll) {
		OutputDatiDTO dto = new OutputDatiDTO();
		dto.setIdElabora(idElabora);
		dto.setProgressivo(progressivo);
		dto.setIdOutputFoglio(Long.parseLong(outputFoglio.get(tipoBoll)[7]));
		dto.setCodTipoPassoElabora(COD_TIPO_PASSO_ELABORA_ELAB_EMISSIONE);
		dto.setValoreColonna1(
				getDatiAnnualitaString(titolare, avvisoAnnualita, flgRateoPrimaRata, numeroMesi, tipoBoll));
		return dto;
	}

	private OutputDatiDTO getRecordDatiTxtCompletoTipo4(Long idElabora, int progressivo, String titolare,
			AvvisoAnnualitaDTO avvisoAnnualita, int flgRateoPrimaRata, int numeroMesi, String tipoBoll) {
		OutputDatiDTO dto = new OutputDatiDTO();
		dto.setIdElabora(idElabora);
		dto.setProgressivo(progressivo);
		dto.setIdOutputFoglio(Long.parseLong(outputFoglio.get(tipoBoll)[6]));
		dto.setCodTipoPassoElabora(COD_TIPO_PASSO_ELABORA_ELAB_EMISSIONE);
		dto.setValoreColonna1(
				getDatiAnnualitaString(titolare, avvisoAnnualita, flgRateoPrimaRata, numeroMesi, tipoBoll));
		return dto;
	}

	private String getDatiAnnualitaString(String titolare, AvvisoAnnualitaDTO avvisoAnnualita, int flgRateoPrimaRata,
			int numeroMesi, String tipoBoll) {
		StringBuilder sb = new StringBuilder();
		sb.append("4").append(";");
		sb.append(titolare).append(";");
		sb.append(formatString(avvisoAnnualita.getCodiceUtenza())).append(";");
		sb.append("" + avvisoAnnualita.getAnnoRichPagamento()).append(";");
		sb.append(formatBigDecimalTxt(avvisoAnnualita.getFrazTotaleCanoneAnno())).append(";");
		sb.append(formatBigDecimalTxt(avvisoAnnualita.getTotaleCanoneAnnoCalc())).append(";");
		sb.append(formatBigDecimalTxt(avvisoAnnualita.getValore20Calc())).append(";");
		if (tipoBoll.equals("BS") || tipoBoll.equals("BG")) {
			sb.append("" + flgRateoPrimaRata).append(";");
			sb.append(formatNumeroMesi(numeroMesi)).append(";");
		}
		return sb.toString();
	}

	private OutputDatiDTO getRecordDatiODSTipo4(Long idElabora, int progressivo, String titolare,
			AvvisoAnnualitaDTO avvisoAnnualita, int flgRateoPrimaRata, int numeroMesi, String tipoBoll) {
		OutputDatiDTO dto = new OutputDatiDTO();
		dto.setIdElabora(idElabora);
		dto.setProgressivo(progressivo);
		dto.setIdOutputFoglio(Long.parseLong(outputFoglio.get(tipoBoll)[3]));
		dto.setCodTipoPassoElabora(COD_TIPO_PASSO_ELABORA_ELAB_EMISSIONE);
		dto.setValoreColonna1("4");
		dto.setValoreColonna2(titolare);
		dto.setValoreColonna3(formatString(avvisoAnnualita.getCodiceUtenza()));
		dto.setValoreColonna4("" + avvisoAnnualita.getAnnoRichPagamento());
		dto.setValoreColonna5(formatBigDecimal(avvisoAnnualita.getFrazTotaleCanoneAnno()));
		dto.setValoreColonna6(formatBigDecimal(avvisoAnnualita.getTotaleCanoneAnnoCalc()));
		dto.setValoreColonna7(formatBigDecimal(avvisoAnnualita.getValore20Calc()));
		if (tipoBoll.equals("BS") || tipoBoll.equals("BG")) {
			dto.setValoreColonna8("" + flgRateoPrimaRata);
			// dto.setValoreColonna9(formatNumeroMesi(numeroMesi));
			String mesi = (numeroMesi == 0) ? "12" : String.valueOf(numeroMesi);
			// su ODS non serve formattare il numero mesi con 0 davanti
			dto.setValoreColonna9(""+mesi);
		}
		return dto;
	}

	private OutputDatiDTO getRecordDatiTxtCartaTipo3(Long idElabora, int progressivo, String dtProtocollo,
			String titolare, AvvisoDatiAmminDTO avvisoDatiAmmin, Date dataScadenzaEmasIso, String anno,
			String tipoBoll) {
		OutputDatiDTO dto = new OutputDatiDTO();
		dto.setIdElabora(idElabora);
		dto.setProgressivo(progressivo);
		dto.setIdOutputFoglio(Long.parseLong(outputFoglio.get(tipoBoll)[7]));
		dto.setCodTipoPassoElabora(COD_TIPO_PASSO_ELABORA_ELAB_EMISSIONE);
		dto.setValoreColonna1(getDatiAmminString(titolare, avvisoDatiAmmin, dataScadenzaEmasIso, anno, tipoBoll));
		return dto;
	}

	private OutputDatiDTO getRecordDatiTxtCompletoTipo3(Long idElabora, int progressivo, String dtProtocollo,
			String titolare, AvvisoDatiAmminDTO avvisoDatiAmmin, Date dataScadenzaEmasIso, String anno,
			String tipoBoll) {
		OutputDatiDTO dto = new OutputDatiDTO();
		dto.setIdElabora(idElabora);
		dto.setProgressivo(progressivo);
		dto.setIdOutputFoglio(Long.parseLong(outputFoglio.get(tipoBoll)[6]));
		dto.setCodTipoPassoElabora(COD_TIPO_PASSO_ELABORA_ELAB_EMISSIONE);
		dto.setValoreColonna1(getDatiAmminString(titolare, avvisoDatiAmmin, dataScadenzaEmasIso, anno, tipoBoll));
		return dto;
	}

	private String getDatiAmminString(String titolare, AvvisoDatiAmminDTO avvisoDatiAmmin, Date dataScadenzaEmasIso,
			String anno, String tipoBoll) {
		StringBuilder sb = new StringBuilder();
		sb.append("3").append(";");
		sb.append(titolare).append(";");
		sb.append(formatString(avvisoDatiAmmin.getCodiceUtenza())).append(";");
		sb.append(formatString(avvisoDatiAmmin.getCorpoIdrico())).append(";");
		sb.append(formatString(avvisoDatiAmmin.getComuneDiPresa())).append(";");
		sb.append(formatString(avvisoDatiAmmin.getPeriodoDiContribuzione())).append(";");
		sb.append(formatString(avvisoDatiAmmin.getScadConcCalc())).append(";");
		sb.append(formatString(avvisoDatiAmmin.getProvvedimentoCalc())).append(";");
		int annoEmasIso = getIntYearFromDate(dataScadenzaEmasIso);
		int annoParam = Integer.valueOf(anno);
		if (annoEmasIso == -1 || annoEmasIso < annoParam) {
			sb.append("").append(";");
		} else {
			sb.append(formatDate(dataScadenzaEmasIso)).append(";");
		}
		sb.append(formatBigDecimalTxt(avvisoDatiAmmin.getTotaleUtenzaCalc())).append(";");
		sb.append(formatBigDecimalTxt(avvisoDatiAmmin.getImpCompensCanone())).append(";");
		sb.append(formatBigDecimalTxt(avvisoDatiAmmin.getRecCanone())).append(";");
		sb.append(formatString(avvisoDatiAmmin.getNumPratica())).append(";");
		sb.append(formatString(avvisoDatiAmmin.getDescrUtilizzo())).append(";");
		if (tipoBoll.equals("BS") || tipoBoll.equals("BG")) {
			sb.append(formatBigDecimalTxt(avvisoDatiAmmin.getTotEnergProd())).append(";");
		}
		return sb.toString();
	}

	private OutputDatiDTO getRecordDatiODSTipo3(Long idElabora, int progressivo, String dtProtocollo, String titolare,
			AvvisoDatiAmminDTO avvisoDatiAmmin, Date dataScadenzaEmasIso, String anno, String tipoBoll) {
		OutputDatiDTO dto = new OutputDatiDTO();
		dto.setIdElabora(idElabora);
		dto.setProgressivo(progressivo);
		dto.setIdOutputFoglio(Long.parseLong(outputFoglio.get(tipoBoll)[2]));
		dto.setCodTipoPassoElabora(COD_TIPO_PASSO_ELABORA_ELAB_EMISSIONE);
		dto.setValoreColonna1("3");
		dto.setValoreColonna2(titolare);
		dto.setValoreColonna3(formatString(avvisoDatiAmmin.getCodiceUtenza()));
		dto.setValoreColonna4(formatString(avvisoDatiAmmin.getCorpoIdrico()));
		dto.setValoreColonna5(formatString(avvisoDatiAmmin.getComuneDiPresa()));
		dto.setValoreColonna6(formatString(avvisoDatiAmmin.getPeriodoDiContribuzione()));
		dto.setValoreColonna7(formatString(avvisoDatiAmmin.getScadConcCalc()));
		dto.setValoreColonna8(formatString(avvisoDatiAmmin.getProvvedimentoCalc()));
		int annoEmasIso = getIntYearFromDate(dataScadenzaEmasIso);
		int annoParam = Integer.valueOf(anno);
		if (annoEmasIso == -1 || annoEmasIso < annoParam) {
			dto.setValoreColonna9(null);
		} else {
			dto.setValoreColonna9(formatDate(dataScadenzaEmasIso));
		}
		dto.setValoreColonna10(formatBigDecimal(avvisoDatiAmmin.getTotaleUtenzaCalc()));
		dto.setValoreColonna11(formatBigDecimal(avvisoDatiAmmin.getImpCompensCanone()));
		dto.setValoreColonna12(formatBigDecimal(avvisoDatiAmmin.getRecCanone()));
		dto.setValoreColonna13(formatString(avvisoDatiAmmin.getNumPratica()));
		dto.setValoreColonna14(formatString(avvisoDatiAmmin.getDescrUtilizzo()));
		if (tipoBoll.equals("BS") || tipoBoll.equals("BG")) {
			dto.setValoreColonna15(formatBigDecimal(avvisoDatiAmmin.getTotEnergProd()));
		}

		return dto;
	}

	private OutputDatiDTO getRecordDatiTxtCartaTipo2(Long idElabora, int progressivo, String dtProtocollo,
			AvvisoDatiTitolareDTO avvisoDatiTitolare, String tipoBoll, String soggettoGruppo) {
		OutputDatiDTO dto = new OutputDatiDTO();
		dto.setIdElabora(idElabora);
		dto.setProgressivo(progressivo);
		dto.setIdOutputFoglio(Long.parseLong(outputFoglio.get(tipoBoll)[7]));
		dto.setCodTipoPassoElabora(COD_TIPO_PASSO_ELABORA_ELAB_EMISSIONE);
		dto.setValoreColonna1(getDatiTitolareString(dtProtocollo, avvisoDatiTitolare, tipoBoll, soggettoGruppo));
		return dto;
	}

	private OutputDatiDTO getRecordDatiTxtCompletoTipo2(Long idElabora, int progressivo, String dtProtocollo,
			AvvisoDatiTitolareDTO avvisoDatiTitolare, String tipoBoll, String soggettoGruppo) {
		OutputDatiDTO dto = new OutputDatiDTO();
		dto.setIdElabora(idElabora);
		dto.setProgressivo(progressivo);
		dto.setIdOutputFoglio(Long.parseLong(outputFoglio.get(tipoBoll)[6]));
		dto.setCodTipoPassoElabora(COD_TIPO_PASSO_ELABORA_ELAB_EMISSIONE);
		dto.setValoreColonna1(getDatiTitolareString(dtProtocollo, avvisoDatiTitolare, tipoBoll, soggettoGruppo));
		return dto;
	}

	private String getDatiTitolareString(String dtProtocollo, AvvisoDatiTitolareDTO avvisoDatiTitolare, String tipoBoll,
			String soggettoGruppo) {
		StringBuilder sb = new StringBuilder();
		sb.append("2").append(";");
		sb.append(soggettoGruppo).append(";");
		sb.append(formatString(avvisoDatiTitolare.getNomeTitolareIndPost())).append(";");
		sb.append(formatString(avvisoDatiTitolare.getPressoIndPost())).append(";");
		sb.append(formatString(avvisoDatiTitolare.getIndirizzoIndPost())).append(";");
		sb.append(formatString(avvisoDatiTitolare.getCapIndPost())).append(";");
		sb.append(formatString(avvisoDatiTitolare.getComuneIndPost())).append(";");
		sb.append(formatString(avvisoDatiTitolare.getProvIndPost())).append(";");
		sb.append(formatBigDecimalTxt(avvisoDatiTitolare.getImportoDaVersare())).append(";");
		sb.append(formatDate(avvisoDatiTitolare.getScadenzaPagamento())).append(";");
		sb.append(avvisoDatiTitolare.getnUtenze()).append(";");
		sb.append(avvisoDatiTitolare.getAnnualitaPagamento()).append(";");
		sb.append(formatString(avvisoDatiTitolare.getStatiPagamenti())).append(";");
		if (tipoBoll.equals("BS") || tipoBoll.equals("BG")) {
			sb.append(formatString(avvisoDatiTitolare.getDilazione())).append(";");
			sb.append(formatString(avvisoDatiTitolare.getCodiceFiscaleCalc())).append(";");
			sb.append(formatString(avvisoDatiTitolare.getnAvvisoCalc())).append(";");
			sb.append(formatString(avvisoDatiTitolare.getModalitaInvio())).append(";");
			if(avvisoDatiTitolare.getModalitaInvio().equals(MODALITA_INVIO_CARTA)) {
				sb.append("").append(";");
			} else {
				sb.append(formatString(avvisoDatiTitolare.getPecEmail())).append(";");
			}
			sb.append(formatString(avvisoDatiTitolare.getNumeroProtocolloSped())).append(";");
			sb.append(formatDate(avvisoDatiTitolare.getDataProtocolloSped())).append(";");
			sb.append(formatDate(avvisoDatiTitolare.getScadenzaPagamento2())).append(";");
			sb.append(dtProtocollo.substring(0, 4)).append(";");
		} else {
			sb.append(formatString(avvisoDatiTitolare.getCodiceFiscaleCalc())).append(";");
			sb.append(formatString(avvisoDatiTitolare.getnAvvisoCalc())).append(";");
			sb.append(formatString(avvisoDatiTitolare.getModalitaInvio())).append(";");
			if(avvisoDatiTitolare.getModalitaInvio().equals(MODALITA_INVIO_CARTA)) {
				sb.append("").append(";");
			} else {
				sb.append(formatString(avvisoDatiTitolare.getPecEmail())).append(";");
			}
			sb.append(formatString(avvisoDatiTitolare.getNumeroProtocolloSped())).append(";");
			sb.append(formatDate(avvisoDatiTitolare.getDataProtocolloSped())).append(";");
			sb.append(dtProtocollo.substring(0, 4)).append(";");
		}
		return sb.toString();
	}

	private OutputDatiDTO getRecordDatiODSTipo2(Long idElabora, int progressivo, String dtProtocollo,
			AvvisoDatiTitolareDTO avvisoDatiTitolare, String tipoBoll, String soggettoGruppo) {
		OutputDatiDTO dto = new OutputDatiDTO();
		dto.setIdElabora(idElabora);
		dto.setProgressivo(progressivo);
		dto.setIdOutputFoglio(Long.parseLong(outputFoglio.get(tipoBoll)[1]));
		dto.setCodTipoPassoElabora(COD_TIPO_PASSO_ELABORA_ELAB_EMISSIONE);
		dto.setValoreColonna1("2");
		dto.setValoreColonna2(soggettoGruppo);
		dto.setValoreColonna3(formatString(avvisoDatiTitolare.getNomeTitolareIndPost()));
		dto.setValoreColonna4(formatString(avvisoDatiTitolare.getPressoIndPost()));
		dto.setValoreColonna5(formatString(avvisoDatiTitolare.getIndirizzoIndPost()));
		dto.setValoreColonna6(formatString(avvisoDatiTitolare.getCapIndPost()));
		dto.setValoreColonna7(formatString(avvisoDatiTitolare.getComuneIndPost()));
		dto.setValoreColonna8(formatString(avvisoDatiTitolare.getProvIndPost()));
		dto.setValoreColonna9(formatBigDecimal(avvisoDatiTitolare.getImportoDaVersare()));
		dto.setValoreColonna10(formatDate(avvisoDatiTitolare.getScadenzaPagamento()));
		dto.setValoreColonna11("" + avvisoDatiTitolare.getnUtenze());
		dto.setValoreColonna12("" + avvisoDatiTitolare.getAnnualitaPagamento());
		dto.setValoreColonna13(formatString(avvisoDatiTitolare.getStatiPagamenti()));
		if (tipoBoll.equals("BS") || tipoBoll.equals("BG")) {
			dto.setValoreColonna14(formatString(avvisoDatiTitolare.getDilazione()));
			dto.setValoreColonna15(formatString(avvisoDatiTitolare.getCodiceFiscaleCalc()));
			dto.setValoreColonna16(formatString(avvisoDatiTitolare.getnAvvisoCalc()));
			dto.setValoreColonna17(formatString(avvisoDatiTitolare.getModalitaInvio()));
			dto.setValoreColonna18(formatString(avvisoDatiTitolare.getPecEmail()));
			dto.setValoreColonna19(formatString(avvisoDatiTitolare.getNumeroProtocolloSped()));
			dto.setValoreColonna20(formatDate(avvisoDatiTitolare.getDataProtocolloSped()));
			dto.setValoreColonna21(formatDate(avvisoDatiTitolare.getScadenzaPagamento2()));
			dto.setValoreColonna22(dtProtocollo.substring(0, 4));
		} else {
			dto.setValoreColonna14(formatString(avvisoDatiTitolare.getCodiceFiscaleCalc()));
			dto.setValoreColonna15(formatString(avvisoDatiTitolare.getnAvvisoCalc()));
			dto.setValoreColonna16(formatString(avvisoDatiTitolare.getModalitaInvio()));
			dto.setValoreColonna17(formatString(avvisoDatiTitolare.getPecEmail()));
			dto.setValoreColonna18(formatString(avvisoDatiTitolare.getNumeroProtocolloSped()));
			dto.setValoreColonna19(formatDate(avvisoDatiTitolare.getDataProtocolloSped()));
			dto.setValoreColonna20(dtProtocollo.substring(0, 4));
		}
		return dto;
	}

	private String formatBigDecimal(String value) {
		if (value == null || value.isEmpty())
			return "0.00";
		BigDecimal bdValue = new BigDecimal(value);
		return formatBigDecimal(bdValue);
	}

	private String formatBigDecimal(BigDecimal value) {
		if (value == null)
			value = BigDecimal.ZERO;
		Locale currentLocale = Locale.getDefault();
		DecimalFormatSymbols symbols = new DecimalFormatSymbols(currentLocale);
		symbols.setDecimalSeparator('.');
		DecimalFormat df = new DecimalFormat("#.##", symbols);
		df.setMaximumFractionDigits(2);
		df.setMinimumFractionDigits(2);
		df.setGroupingUsed(false);
		return df.format(value);
	}

	private String formatBigDecimalTxt(String value) {
		if (value == null || value.isEmpty()) {
			String s = "";
			return String.format("%1$11s", s).replace(' ', '0');
		}
		BigDecimal bdValue = new BigDecimal(value);
		return formatBigDecimalTxt(bdValue);
	}

	private String formatBigDecimalTxt(BigDecimal value) {
		String s = formatBigDecimal(value);
		s = s.replace(".", "");
		// Padding con zero a sinistra fino a lunghezza 11
		return String.format("%1$11s", s).replace(' ', '0');
	}
	
	private String formatBigDecimalTxt5(BigDecimal value) {
		String s = formatBigDecimal(value);
		s = s.replace(".", "");
		// Padding con zero a sinistra fino a lunghezza 5
		return String.format("%1$5s", s).replace(' ', '0');
	}

	private String formatQuantita(BigDecimal value) {
		if (value == null)
			value = BigDecimal.ZERO;
		Locale currentLocale = Locale.getDefault();
		DecimalFormatSymbols symbols = new DecimalFormatSymbols(currentLocale);
		symbols.setDecimalSeparator('.');
		DecimalFormat df = new DecimalFormat("#.####", symbols);
		df.setMaximumFractionDigits(4);
		df.setMinimumFractionDigits(4);
		df.setGroupingUsed(false);
		return df.format(value);
	}

	private String formatQuantitaTxt(BigDecimal value) {
		if (value == null) {
			String s = "";
			return String.format("%1$13s", s).replace(' ', '0');
		}
		String s = formatQuantita(value);
		s = s.replace(".", "");
		// Padding con zero a sinistra fino a lunghezza 13
		return String.format("%1$13s", s).replace(' ', '0');
	}

	private String formatNumeroMesi(int mesi) {
		String s = (mesi == 0) ? "12" : String.valueOf(mesi);
		// Padding con zero a sinistra fino a lunghezza 2
		return String.format("%1$2s", s).replace(' ', '0');
	}

	private String formatString(String value) {
		return value == null ? "" : value.trim();
	}

	private String formatDate(Date date) {
		if (date == null)
			return "";
		SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT_SPED);
		try {
			return df.format(date);
		} catch (Exception e) {
			return "";
		}
	}

	private String formatDate(String dateStr) {
		if (dateStr == null)
			return "";
		SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
		try {
			Date d = df.parse(dateStr);
			SimpleDateFormat df2 = new SimpleDateFormat(DATE_FORMAT_SPED);
			return df2.format(d);
		} catch (Exception e) {
			return "";
		}
	}

	private BollResultDTO errorResult(BollResultDTO bsResult, String stepError) {
		bsResult.setStatus("KO");
		bsResult.setStepError(stepError);
		return bsResult;
	}

	private void logStep(Long idElabora, String codPasso, int esito, String note, String codFase) {
		RegistroElaboraDTO registroElabora = new RegistroElaboraDTO();
		registroElabora.setIdElabora(idElabora);
		registroElabora.setCodPassoElabora(codPasso);
		registroElabora.setFlgEsitoElabora(esito);
		registroElabora.setNotaElabora(note);
		registroElabora.setGestAttoreIns(ATTORE);
		registroElabora.setGestAttoreUpd(ATTORE);
		registroElabora.setCodFaseElabora(codFase);
		try {
			registroElaboraDAO.saveRegistroElabora(registroElabora);
		} catch (Exception e) {
			LOGGER.error("[BollOutputApiServiceImpl::logStep] (Exception) ", e);
		}
		LOGGER.debug(note);
	}

	private int getIntYearFromDate(Date date) {
		if (date != null) {
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(date);
			int anno = calendar.get(Calendar.YEAR);
			return anno;
		}
		return -1;
	}

}

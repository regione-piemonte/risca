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
import java.math.RoundingMode;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.epay.epaywso.enti2epaywso.types.AggiornaPosizioniDebitorieRequest;
import it.csi.epay.epaywso.enti2epaywso.types.ComponenteImportoType;
import it.csi.epay.epaywso.enti2epaywso.types.ElencoPosizioniDaAggiornareType;
import it.csi.epay.epaywso.enti2epaywso.types.ElencoPosizioniDaAggiornareType.PosizioniDaAggiornare;
import it.csi.epay.epaywso.enti2epaywso.types.PosizioneDaAggiornareType;
import it.csi.epay.epaywso.enti2epaywso.types.PosizioneDaAggiornareType.ComponentiImporto;
import it.csi.epay.epaywso.enti2epaywso.types.TestataAggiornaPosizioniDebitorie;
import it.csi.epay.epaywso.types.ResponseType;
import it.csi.epay.epaywso.types.TipoAggiornamentoType;
import it.csi.risca.riscabesrv.business.be.RicalcoloIUVApi;
import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.helper.pagopa.PagopaException;
import it.csi.risca.riscabesrv.business.be.helper.pagopa.PagopaServiceHelper;
import it.csi.risca.riscabesrv.business.be.impl.dao.AmbitiConfigDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.AmbitiDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.ElaboraDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.EntePagopaDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.IuvDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.IuvDaInviareDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.LottoDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.PagopaListaCaricoDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.PagopaScompVarIuvDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RegistroElaboraDAO;
import it.csi.risca.riscabesrv.dto.AmbitoConfigDTO;
import it.csi.risca.riscabesrv.dto.AmbitoDTO;
import it.csi.risca.riscabesrv.dto.BollResultIUVDTO;
import it.csi.risca.riscabesrv.dto.EntePagopaDTO;
import it.csi.risca.riscabesrv.dto.IuvDaInviareDTO;
import it.csi.risca.riscabesrv.dto.LottoDTO;
import it.csi.risca.riscabesrv.dto.PagopaListaCaricoDTO;
import it.csi.risca.riscabesrv.dto.PagopaScompVarIuvDTO;
import it.csi.risca.riscabesrv.dto.RegistroElaboraDTO;
import it.csi.risca.riscabesrv.dto.RicalcoloLottoDTO;
import it.csi.risca.riscabesrv.dto.ScomposizioneRichiestaIuvDTO;
import it.csi.risca.riscabesrv.util.Constants;
import it.csi.risca.riscabesrv.util.Utils;

@Component
public class RicalcoloIUVApiServiceImpl implements RicalcoloIUVApi {

	private String attore;
	private static final String IDENTITY = "identity";
	private static Logger LOGGER = Logger.getLogger(Constants.LOGGER_NAME);
	private static final String COD_FASE_RICALCOLO_IUV = "RICALCOLO_IUV";
	private static final String FASE = "Ricalcolo IUV";
	private static final String PASSO_RICALCOLO_IUV = "RICALCOLO_IUV";
	private static final String AMBITO_CONFIG_CHIAVE_RECORD_PER_LOTTO = "LOTTO.RecordPerLotto";
	private static final String AMBITO_CONFIG_CHIAVE_IUV_INIZIO_ANNO_DATA_ELAB = "IUVinizioAnno_DataElab";

	@Autowired
	private RegistroElaboraDAO registroElaboraDAO;

	@Autowired
	private IuvDaInviareDAO iuvDaInviareDAO;

	@Autowired
	private AmbitiDAO ambitiDAO;

	@Autowired
	private AmbitiConfigDAO ambitiConfigDAO;

	@Autowired
	private EntePagopaDAO entePagopaDAO;

	@Autowired
	private PagopaScompVarIuvDAO pagopaScompVarIuvDAO;

	@Autowired
	private PagopaListaCaricoDAO pagopaListaCaricoDAO;

	@Autowired
	private IuvDAO iuvDAO;

	@Autowired
	private LottoDAO lottoDAO;

	@Autowired
	private PagopaServiceHelper pagopaServiceHelper;

	@Autowired
	private ElaboraDAO elaboraDAO;

	@Override
	public Response initRicalcolo(Long idElabora, String attore) {
		LOGGER.debug("[RicalcoloIUVApiServiceImpl::initRicalcolo] BEGIN");
		this.attore = attore;
		BollResultIUVDTO result = new BollResultIUVDTO();

		logStep(idElabora, PASSO_RICALCOLO_IUV, 0, FASE + " - Identificazione IUV da aggiornare/o annullare");

		try {
			iuvDaInviareDAO.annullaIuvPerImportoVersato(idElabora, attore);
			iuvDaInviareDAO.annullaIuvPerPosizioneDebitoriaAnnullata(idElabora, attore);
			iuvDaInviareDAO.annullaIuvPerCambioTitolare(idElabora, attore);
			iuvDaInviareDAO.annullaIuvPerEmissioneARuolo(idElabora, attore);
			iuvDaInviareDAO.modificaImportoIuvPerCanoneDovuto(idElabora, attore);
			iuvDaInviareDAO.modificaImportoIuvPerImportoVersato(idElabora, attore);
			iuvDaInviareDAO.modificaImportoIuvPerAddebitoInteressi(idElabora, attore);

			// Controllo se la data corrente coincide con la data specificata nel parametro
			// di config IUVinizioAnno_DataElab
			String codAmbito = elaboraDAO.loadElaboraById(idElabora, false).getAmbito().getCodAmbito();
			List<AmbitoConfigDTO> config = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(codAmbito,
					AMBITO_CONFIG_CHIAVE_IUV_INIZIO_ANNO_DATA_ELAB);
			String dataElab = "";
			if (config != null && config.size() > 0) {
				dataElab = config.get(0).getValore();
			}
			DateFormat dateFormat = new SimpleDateFormat(Utils.DEFAULT_DATE_FORMAT);
			Date date = new Date();
			String currentDate = dateFormat.format(date);
			if (!dataElab.isEmpty() && dataElab.equals(currentDate)) {
				// La data configurata coincide con la data corrente --> faccio l'aggiornamento
				// di inizio anno.
				iuvDaInviareDAO.aggiornaIuvDiInizioAnno(idElabora, attore);
			}

		} catch (Exception e) {
			logStep(idElabora, PASSO_RICALCOLO_IUV, 0,
					FASE + " - Identificazione IUV da aggiornare/o annullare ERRORE: " + e.getMessage());
		}

		logStep(idElabora, PASSO_RICALCOLO_IUV, 0,
				FASE + " - Identificazione IUV da aggiornare/o annullare terminata con successo");

		result.setStatus("OK");
		LOGGER.debug("[RicalcoloIUVApiServiceImpl::initRicalcolo] END");
		return Response.ok(result).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response creaLotti(Long idElabora, Long idAmbito, String attore) {
		LOGGER.debug("[RicalcoloIUVApiServiceImpl::creaLotti] BEGIN");
		this.attore = attore;
		BollResultIUVDTO result = new BollResultIUVDTO();

		List<IuvDaInviareDTO> iuvDaInviare = iuvDaInviareDAO.getIuvDaInviare(idElabora);
		int numPosizioniDebitorie = iuvDaInviare.size();

		if (numPosizioniDebitorie > 0) {
			logStep(idElabora, PASSO_RICALCOLO_IUV, 0,
					FASE + " - Estrazione numero posizioni debitorie OK - PosDebLette: " + numPosizioniDebitorie);

			String dataLotto = new SimpleDateFormat("yyyyMMdd_HHmm").format(new Date());

			int recordPerLotto = 0;
			AmbitoDTO ambito = ambitiDAO.loadAmbitoByIdAmbito(idAmbito);
			List<AmbitoConfigDTO> config;
			try {
				config = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(ambito.getCodAmbito(),
						AMBITO_CONFIG_CHIAVE_RECORD_PER_LOTTO);
				if (config != null && config.size() > 0) {
					recordPerLotto = Integer.parseInt(config.get(0).getValore());
				}
				EntePagopaDTO entePagopa = entePagopaDAO.loadEntePagopaPerIuvByAmbito(idAmbito);
				if (entePagopa != null) {
					String codiceSettore = entePagopa.getCodSettore();
					String causale = entePagopa.getCausale();
					String codiceVersamento = entePagopa.getCodVersamento();

					logStep(idElabora, PASSO_RICALCOLO_IUV, 0,
							FASE + " - Estr. info Lotto OK: CodiceSettore: " + codiceSettore + " - Causale: " + causale
									+ " - CodiceVersamento: " + codiceVersamento + " - RecordPerLotto: "
									+ recordPerLotto);

					int numeroLotti = 0;
					if (numPosizioniDebitorie % recordPerLotto == 0) {
						numeroLotti = numPosizioniDebitorie / recordPerLotto;
					} else {
						numeroLotti = numPosizioniDebitorie / recordPerLotto + 1;
					}

					List<String> nomiLotto = new ArrayList<String>();
					for (int i = 1; i <= numeroLotti; i++) {
						// Padding con zero a sinistra fino a lunghezza 2
						String progressivo = String.format("%1$2s", "" + i).replace(' ', '0');
						String nomeLotto = codiceSettore + codiceVersamento + "_" + dataLotto + "_" + idElabora + "_"
								+ progressivo;
						nomiLotto.add(nomeLotto);
					}

					logStep(idElabora, PASSO_RICALCOLO_IUV, 0,
							FASE + " - Determinazione nomi dei lotti OK - numero lotti: " + numeroLotti);

					result.setNomiLotto(nomiLotto);
					result.setRecordPerLotto(recordPerLotto);

					// SCOMPOSIZIONE CANONE/INTERESSI/SPESE NOTIFICA

					// Pulizia della tabella di working prima di inserire i nuovi dati di
					// scomposizione
					// TODO al momento SVUOTIAMO TUTTA LA TABELLA --> da rivedere nel caso di
					// attivazione di un nuovo AMBITO
					pagopaScompVarIuvDAO.deletePagopaScompVarIuvWorking();

					int retScompCanone = scomposizioneCanoneIUV(idElabora);
					int retScompInteressi = scomposizioneInteressiIUV(idElabora);
					int retScompSpese = scomposizioneSpeseIUV(idElabora);
					if (retScompCanone == 0 && retScompInteressi == 0 && retScompSpese == 0) {
						result.setStatus("OK");
					} else {
						result.setStatus("KO");
						result.setStepError(PASSO_RICALCOLO_IUV);
						result.setErrorMessage(FASE + " - Errore scomposizione IUV");
					}
				} else {
					result.setStatus("KO");
					result.setStepError(PASSO_RICALCOLO_IUV);
					result.setErrorMessage(FASE
							+ " - Estrazione info per Lotto KO: non sono state trovate tutte le informazioni per l'ambito "
							+ idAmbito);
				}
			} catch (SQLException e) {
				result.setStatus("KO");
				result.setStepError(PASSO_RICALCOLO_IUV);
				result.setErrorMessage(FASE
						+ " - Estrazione info per Lotto KO: non sono state trovate tutte le informazioni per l'ambito "
						+ idAmbito);
			} catch (DAOException e) {
				result.setStatus("KO");
				result.setStepError(PASSO_RICALCOLO_IUV);
				result.setErrorMessage(
						FASE + " - Pulizia tabella di working risca_w_pagopa_scomp_var_iuv KO: " + e.getMessage());
			}
		} else {
			// se non ci sono posizioni debitorio ritorna ok
//			result.setStatus("KO");
//			result.setStepError(PASSO_RICALCOLO_IUV);
//			result.setErrorMessage(FASE
//					+ " - Estrazione numero posizioni debitorie KO: Non sono presenti posizioni debitorie per le quali ricalcolare lo IUV");
			result.setStatus("OK");
			logStep(idElabora, PASSO_RICALCOLO_IUV, 0, FASE
					+ "- WARNING - Estrazione numero posizioni debitorie KO: Non sono presenti posizioni debitorie per le quali ricalcolare lo IUV");

		}

		LOGGER.debug("[RicalcoloIUVApiServiceImpl::creaLotti] END");
		return Response.ok(result).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	private int scomposizioneSpeseIUV(Long idElabora) {
		try {
			List<ScomposizioneRichiestaIuvDTO> scompSpese = pagopaScompVarIuvDAO
					.loadScomposizioneSpeseNotifica(idElabora);

			for (ScomposizioneRichiestaIuvDTO scomposizioneDto : scompSpese) {
				BigDecimal importoPerAcc = BigDecimal.ZERO;

				double importoVersato = scomposizioneDto.getImpVersato().doubleValue();
				double canoneDovuto = scomposizioneDto.getCanoneDovuto().doubleValue();
				double interessiTot = scomposizioneDto.getInteressiTot().doubleValue();
				double totSpeseNotifPerNap = scomposizioneDto.getTotSpeseNotifPerNap().doubleValue();

				if (importoVersato <= (canoneDovuto + interessiTot)) {
					/*
					 * IMP_VERSATO <= (CANONE_DOVUTO + interessi) quindi non c'e' nessun importo da
					 * sottrarre alle spese di notifica perche' quanto versato copre al massimo il
					 * canone + gli interessi
					 */
					importoPerAcc = scomposizioneDto.getTotSpeseNotifPerNap();
				} else {
					if (importoVersato > (canoneDovuto + interessiTot)
							&& importoVersato < (canoneDovuto + interessiTot + totSpeseNotifPerNap)) {
						/*
						 * IMP_VERSATO e' > (canone dovuto + interessi) e < (CANONE_DOVUTO + interessi +
						 * spese di notifica) , quindi bisogna sottrarre alle spese di notifica quanto
						 * avanza dal pagamento del canone + interessi
						 */
						importoPerAcc = new BigDecimal(
								totSpeseNotifPerNap - (importoVersato - (canoneDovuto + interessiTot)));
					} else {
						// if rec_scomp_var_iuv_spese.imp_versato >
						// (rec_scomp_var_iuv_spese.canone_dovuto +
						// rec_scomp_var_iuv_spese.interessi_tot +
						// rec_scomp_var_iuv_spese.tot_spese_notif_per_nap)
						if (importoVersato > (canoneDovuto + interessiTot + totSpeseNotifPerNap)) {
							/*
							 * IMP_VERSATO >= (CANONE_DOVUTO + interessi + spese di notifica) quindi le
							 * spese di notifica ancora dovute sono 0
							 */
							importoPerAcc = BigDecimal.ZERO;
						}
					}
				}

				if (importoPerAcc.compareTo(BigDecimal.ZERO) > 0) {
					// Inserire i dati nella tabella RISCA_W_PAGOPA_SCOMP_VAR_IUV
					saveScompVarIuvWorkingPerInteressiSpese(idElabora, importoPerAcc, scomposizioneDto);
				}
			}
			return 0;
		} catch (Exception e) {
			LOGGER.error("[RicalcoloIUVApiServiceImpl::scomposizioneInteressiIUV] ERRORE scomposizione interessi: "
					+ e.getMessage());
			return 1;
		}
	}

	private int scomposizioneInteressiIUV(Long idElabora) {
		try {
			List<ScomposizioneRichiestaIuvDTO> scompInteressi = pagopaScompVarIuvDAO
					.loadScomposizioneInteressi(idElabora);

			for (ScomposizioneRichiestaIuvDTO scomposizioneDto : scompInteressi) {
				BigDecimal importoPerAcc = BigDecimal.ZERO;

				double importoVersato = scomposizioneDto.getImpVersato().doubleValue();
				double canoneDovuto = scomposizioneDto.getCanoneDovuto().doubleValue();
				double interessiTot = scomposizioneDto.getInteressiTot().doubleValue();

				if (importoVersato <= canoneDovuto) {
					/*
					 * IMP_VERSATO <= CANONE_DOVUTO quindi non c'e' nessun importo da sottrarre agli
					 * interessi perche' quanto versato copre al massimo il canone
					 */
					importoPerAcc = scomposizioneDto.getInteressiTot();
				} else {
					if (importoVersato > canoneDovuto && importoVersato < (canoneDovuto + interessiTot)) {
						/*
						 * IMP_VERSATO e' > CANONE_DOVUTO e < CANONE_DOVUTO + interessi quindi bisogna
						 * sottrarre agli interessi quanto avanza dal pagamento del canone (importo ver
						 * sato - CANONE_DOVUTO)
						 */
						importoPerAcc = new BigDecimal(interessiTot - (importoVersato - canoneDovuto));
					} else {
						if (importoVersato > (canoneDovuto + interessiTot)) {
							/*
							 * IMP_VERSATO >= (CANONE_DOVUTO + interessi) quindi gli interessi ancora dovuti
							 * sono 0
							 */
							importoPerAcc = BigDecimal.ZERO;
						}
					}
				}
				if (importoPerAcc.compareTo(BigDecimal.ZERO) > 0) {
					// Inserire i dati nella tabella RISCA_W_PAGOPA_SCOMP_VAR_IUV
					saveScompVarIuvWorkingPerInteressiSpese(idElabora, importoPerAcc, scomposizioneDto);
				}
			}
			return 0;
		} catch (Exception e) {
			LOGGER.error("[RicalcoloIUVApiServiceImpl::scomposizioneInteressiIUV] ERRORE scomposizione interessi: "
					+ e.getMessage());
			return 1;
		}
	}

	private int scomposizioneCanoneIUV(Long idElabora) {
		try {
			List<ScomposizioneRichiestaIuvDTO> scompCanone = pagopaScompVarIuvDAO.loadScomposizioneCanone(idElabora);

			BigDecimal runningTotImpPerAcc = BigDecimal.ZERO;

			for (ScomposizioneRichiestaIuvDTO scomposizioneDto : scompCanone) {
				BigDecimal importoPerAcc = BigDecimal.ZERO;
				BigDecimal percTotImp = BigDecimal.ZERO;

				if (scomposizioneDto.getCountImpPerAccOrig() == 1) {
					/*
					 * per il NAP corrente c'è una sola riga (= un unico raggruppamento tipo di
					 * Accertamento di Bilancio) quindi l'Importo per NAP va a finire tutto
					 * nell'Importo per Accertamento finale
					 */
					importoPerAcc = scomposizioneDto.getImportoPerNap();
					percTotImp = new BigDecimal(100);
				} else {
					/*
					 * per il NAP corrente esiste piu' di una riga quindi liImporto per NAP va
					 * suddiviso in proporzione agli 'Importo per Accertamento originale' e poi
					 * valorizzato l'Importo per Accertamento finale.
					 * 
					 * Occorre Ripartire Importo per NAP in base alla percentuale, troncare il
					 * risultato ai centesimi (quindi ci saranno poi dei millesimi da aggiungere
					 * all'IMP_PER_ACC che viene per ultimo ordinando per IMP_PER_ACC_ORIG)
					 */
					importoPerAcc = scomposizioneDto.getImportoPerNap().multiply(scomposizioneDto.getPercTotImp())
							.divide(new BigDecimal(100)).setScale(2, RoundingMode.DOWN);// troncamento ai centesimi
					if (!scomposizioneDto.getProgrImpPerAccOrig().equals(scomposizioneDto.getCountImpPerAccOrig())) {
						// Non si tratta dell'ultimo importo per Accertamento
						runningTotImpPerAcc = runningTotImpPerAcc.add(importoPerAcc);
						percTotImp = scomposizioneDto.getPercTotImp();
					} else {
						// Si tratta dell'ultimo importo per Accertamento, cambio il valore
						// all'importo_per_acc corrente assegnandogli tutto quello che rimane se
						// all'importo_per_nap si sottrae il "running total"
						importoPerAcc = scomposizioneDto.getImportoPerNap().subtract(runningTotImpPerAcc);
						runningTotImpPerAcc = BigDecimal.ZERO;
						percTotImp = scomposizioneDto.getPercTotImp();
					}
				}

				// Inserire i dati nella tabella RISCA_W_PAGOPA_SCOMP_VAR_IUV
				saveScompVarIuvWorkingPerCanone(idElabora, importoPerAcc, percTotImp, scomposizioneDto);
			}

			return 0;
		} catch (Exception e) {
			LOGGER.error("[RicalcoloIUVApiServiceImpl::scomposizioneCanoneIUV] ERRORE scomposizione canone: "
					+ e.getMessage());
			return 1;
		}
	}

	private Long saveScompVarIuvWorkingPerCanone(Long idElabora, BigDecimal importoPerAcc, BigDecimal percTotImp,
			ScomposizioneRichiestaIuvDTO scomposizioneDto) {
		PagopaScompVarIuvDTO dto = new PagopaScompVarIuvDTO();
		dto.setNap(scomposizioneDto.getNap());
		dto.setIdBilAcc(scomposizioneDto.getIdBilAcc());
		dto.setImportoPerNap(scomposizioneDto.getImportoPerNap());
		dto.setCodBilAcc(scomposizioneDto.getCodBilAcc());
		dto.setAnno(scomposizioneDto.getAnno());
		dto.setDatiSpecRisc(scomposizioneDto.getDatiSpecRisc());
		dto.setImportoPerAccOrig(scomposizioneDto.getImportoPerAccOrig());
		dto.setImportoPerAcc(importoPerAcc);
		dto.setIdTipoBilAcc(scomposizioneDto.getIdAccertaBilancio());
		dto.setSumImpPerAccOrig(scomposizioneDto.getSumImpPerAccOrig());
		dto.setPercTotImp(percTotImp);
		dto.setNoteBackoffice(null);
		dto = pagopaScompVarIuvDAO.savePagopaScompVarIuvWorking(dto);

		if (dto.getIdPagopaScompVarIuv() != null) {
			// logStep(idElabora, PASSO_RICALCOLO_IUV, 0, FASE + " - Popola working per dati
			// scomposizione Canone OK");
		}
		return dto.getIdPagopaScompVarIuv();
	}

	private Long saveScompVarIuvWorkingPerInteressiSpese(Long idElabora, BigDecimal importoPerAcc,
			ScomposizioneRichiestaIuvDTO scomposizioneDto) {
		PagopaScompVarIuvDTO dto = new PagopaScompVarIuvDTO();
		dto.setNap(scomposizioneDto.getNap());
		dto.setIdBilAcc(scomposizioneDto.getIdBilAcc());
		dto.setImportoPerNap(null);
		dto.setCodBilAcc(scomposizioneDto.getCodBilAcc());
		dto.setAnno(scomposizioneDto.getAnno());
		dto.setDatiSpecRisc(scomposizioneDto.getDatiSpecRisc());
		dto.setImportoPerAccOrig(null);
		dto.setImportoPerAcc(importoPerAcc);
		dto.setIdTipoBilAcc(scomposizioneDto.getIdTipoBilAcc());
		dto.setSumImpPerAccOrig(null);
		dto.setPercTotImp(null);
		dto.setNoteBackoffice(null);
		dto = pagopaScompVarIuvDAO.savePagopaScompVarIuvWorking(dto);

		if (dto.getIdPagopaScompVarIuv() != null) {
			// logStep(idElabora, PASSO_RICALCOLO_IUV, 0, FASE + " - Popola working per dati
			// scomposizione Canone OK");
		}
		return dto.getIdPagopaScompVarIuv();
	}

	@Override
	public Response inviaLotto(RicalcoloLottoDTO ricalcoloLotto, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		BollResultIUVDTO result = new BollResultIUVDTO();
		result.setDataRichiestaIuv(new Date());
		this.attore = Constants.GEST_ATTORE_STATO_CONTRIBUZIONE;

		Long idElabora = ricalcoloLotto.getElaborazione().getIdElabora();
		Long idAmbito = ricalcoloLotto.getElaborazione().getAmbito().getIdAmbito();
		LottoDTO lotto = ricalcoloLotto.getLotto();
		List<IuvDaInviareDTO> listaIuvDaInviare = ricalcoloLotto.getIuvDaInviare();
		String attoreIns = attore;
		String attoreUpd = attore;

		ArrayList<String> lotti = new ArrayList<String>();
		ArrayList<String> numPosizioniDebitorie = new ArrayList<String>();

		try {
			EntePagopaDTO entePagopa = entePagopaDAO.loadEntePagopaPerIuvByAmbito(idAmbito);

			// Punto 2.8 dell'analisi
			for (IuvDaInviareDTO iuvDaInviare : listaIuvDaInviare) {
				// Leggere le componenti dell'importo
				List<PagopaScompVarIuvDTO> componentiImporto = pagopaScompVarIuvDAO.loadScompVarIuvWorkingByIdIuv(
						iuvDaInviare.getIdIuv(), ricalcoloLotto.getElaborazione().getIdElabora());
				iuvDaInviare.setComponentiImporto(componentiImporto);

				// Inserire i dati nella tabella RISCA_R_PAGOPA_LISTA_CARICO
				PagopaListaCaricoDTO listaCar = saveListaCarico(idElabora, lotto, iuvDaInviare);

				if (listaCar != null && listaCar.getIdPagopaListaCarico() != null) {
					// Per ogni riga delle componenti importo inserire i dati nella tabella
					// RISCA_R_PAGOPA_SCOMP_VAR_IUV
					for (PagopaScompVarIuvDTO comp : componentiImporto) {
						comp.setIdPagopaScompVarIuv(null);
						comp.setIdLotto(lotto.getIdLotto());
						comp.setGestAttoreIns(attoreIns);
						comp.setGestAttoreUpd(attoreUpd);
						comp = pagopaScompVarIuvDAO.savePagopaScompVarIuv(comp);
						if (comp.getIdPagopaScompVarIuv() != null) {
//							logStep(idElabora, PASSO_RICALCOLO_IUV, 0,
//									FASE + " - Inserimento Scomposizione IUV OK per l'elaborazione: " + idElabora
//											+ " - Nome Lotto: " + lotto.getNomeLotto() + " - NAP: " + comp.getNap());
						} else {
							result.setStatus("KO");
							result.setStepError(PASSO_RICALCOLO_IUV);
							result.setErrorMessage(FASE
									+ " - Inserimento Scomposizione IUV KO: non sono stati inseriti i dati nella tabella RISCA_R_PAGOPA_SCOMP_VAR_IUV per l'elaborazione: "
									+ idElabora + " - Nome Lotto: " + lotto.getNomeLotto() + " - NAP: "
									+ comp.getNap());
							return Response.ok(result).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
						}
					}
				} else {
					result.setStatus("KO");
					result.setStepError(PASSO_RICALCOLO_IUV);
					result.setErrorMessage(FASE + " - Inserimento Lista Car IUV OK per l'elaborazione: " + idElabora
							+ "	- Nome Lotto: " + lotto.getNomeLotto() + " - idIuv: " + iuvDaInviare.getIdIuv());
					return Response.ok(result).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
				}
			}

			// Richiamare il metodo AggiornaPosizioniDebitorie di PAGOPA
			AggiornaPosizioniDebitorieRequest request = makeAggiornaPosizioniDebitorieRequest(lotto.getNomeLotto(),
					entePagopa, ricalcoloLotto.getIuvDaInviare());
			ResponseType esitoPagopa;
			String esito = null;
			String esitoDesc = null;
			try {
				esitoPagopa = aggiornaPosizioniDebitorie(request);
				esito = esitoPagopa.getResult().getCodice();
				esitoDesc = esitoPagopa.getResult().getMessaggio();
			} catch (Exception e) {
				LOGGER.error("[RicalcoloIUVApiServiceImpl::inviaLotti] ERROR aggiornaPosizioniDebitorie: " + e);
			}

			if (esito != null && esito.equals("000")) {
//				logStep(idElabora, PASSO_RICALCOLO_IUV, 0,
//						FASE + " - AggiornaPosizioniDebitorie OK per l'elaborazione: " + idElabora + " - Nome Lotto: "
//								+ lotto.getNomeLotto());

				// Update nella tabella RISCA_T_LOTTO con codice esito ottenuto
				// dal servizio di PAGOPA
				Integer res = lottoDAO.updateEsitoLottoByIdLotto(lotto.getIdLotto(), esito.equals("000") ? 1 : 0, esito,
						esitoDesc);
				if (res != null && res > 0) {
//					logStep(idElabora, PASSO_RICALCOLO_IUV, 0,
//							FASE + " - Modifica Esito AggiornaPosizioniDebitorie OK: " + idElabora + " - Nome Lotto: "
//									+ lotto.getNomeLotto());

					// Invio lotto completato con successo
					result.setStatus("OK");

					// Segno le info per l'invio mail
					lotti.add(esitoDesc + " " + esito + " - " + lotto.getNomeLotto());
					numPosizioniDebitorie.add("" + listaIuvDaInviare.size());

				} else {
					result.setStatus("KO");
					result.setStepError(PASSO_RICALCOLO_IUV);
					result.setErrorMessage(FASE
							+ " - Step 3 - Modifica Esito AggiornaPosizioniDebitorie KO: non sono stati modificati i dati nella tabella RISCA_T_LOTTO per l'elaborazione : "
							+ idElabora + " - Nome Lotto: " + lotto.getNomeLotto());
					return Response.ok(result).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
				}
			} else {
				result.setStatus("KO");
				result.setStepError(PASSO_RICALCOLO_IUV);
				result.setErrorMessage(
						FASE + " - AggiornaPosizioniDebitorie KO: Invio dati a PAGOPA fallito per l'elaborazione : "
								+ idElabora + " - Nome Lotto: " + lotto.getNomeLotto() + "Codice Esito: " + esito);
				return Response.ok(result).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
			}

		} catch (Exception e) {
			LOGGER.debug("[RicalcoloIUVApiServiceImpl::inviaLotto] ERROR: " + e.getMessage());
			result.setStatus("KO");
		}

		result.setNomiLotto(lotti);
		result.setNumPosizioniDebitorie(numPosizioniDebitorie);

		LOGGER.debug("[RicalcoloIUVApiServiceImpl::inviaLotto] END");
		return Response.ok(result).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	private ResponseType aggiornaPosizioniDebitorie(AggiornaPosizioniDebitorieRequest request) {
		LOGGER.debug("[RicalcoloIUVApiServiceImpl::richiestaIuv] BEGIN");
		ResponseType response = null;
		try {
			response = pagopaServiceHelper.getSoapClient().aggiornaPosizioniDebitorie(request);

			if (response != null && response.getResult() != null) {
				LOGGER.debug("[RicalcoloIUVApiServiceImpl::aggiornaPosizioniDebitorie] Response - codice: "
						+ response.getResult().getCodice());
				LOGGER.debug("[RicalcoloIUVApiServiceImpl::aggiornaPosizioniDebitorie] Response - messaggio: "
						+ response.getResult().getMessaggio());
			} else {
				LOGGER.debug(
						"[RicalcoloIUVApiServiceImpl::aggiornaPosizioniDebitorie] ERRORE nella response da PAGOPA");
			}
			return response;
		} catch (Exception e) {
			LOGGER.error(
					"[RicalcoloIUVApiServiceImpl::aggiornaPosizioniDebitorie] Errore nella richiestaIuv (Exception) ",
					e);
			throw new PagopaException("Errore nella aggiornaPosizioniDebitorie (Exception)" + e.getMessage());
		}
	}

	private AggiornaPosizioniDebitorieRequest makeAggiornaPosizioniDebitorieRequest(String nomeLotto,
			EntePagopaDTO entePagopa, List<IuvDaInviareDTO> iuvDaInviare) {
		AggiornaPosizioniDebitorieRequest request = new AggiornaPosizioniDebitorieRequest();
		TestataAggiornaPosizioniDebitorie testata = null;
		ElencoPosizioniDaAggiornareType posDaAggiornare = null;

		try {
			if (iuvDaInviare == null || iuvDaInviare.size() < 1) {
				throw new PagopaException("List posizioniDebitorie is empty");
			}

			if (entePagopa == null) {
				throw new PagopaException("Testata is empty");
			}
			testata = new TestataAggiornaPosizioniDebitorie();
			testata.setCFEnteCreditore(entePagopa.getCfEnteCreditore());
			testata.setCodiceVersamento(entePagopa.getCodVersamento());
			testata.setIdMessaggio(nomeLotto);
			testata.setNumeroPosizioniDebitorie(iuvDaInviare.size());

			PosizioniDaAggiornare posizioni = new PosizioniDaAggiornare();

			for (IuvDaInviareDTO iuv : iuvDaInviare) {
				// if (iuv.getImportoNew().compareTo(BigDecimal.ZERO) != 0) {

				String nap = iuvDAO.getIuvById(iuv.getIdIuv()).getNap();

				PosizioneDaAggiornareType p = new PosizioneDaAggiornareType();
				p.setTipoAggiornamento(iuv.getIndTipoAggiornamento().equals("M") ? TipoAggiornamentoType.MODIFICA
						: TipoAggiornamentoType.ANNULLAMENTO);
				p.setIdPosizioneDebitoria(nap);
				p.setImportoTotale(iuv.getImportoNew());

				if (iuv.getIndTipoAggiornamento().equals("M")) {
					ComponentiImporto componentiImporto = new ComponentiImporto();
					for (PagopaScompVarIuvDTO compImporto : iuv.getComponentiImporto()) {
						if (compImporto.getImportoPerAcc().compareTo(BigDecimal.ZERO) != 0) {
							ComponenteImportoType compValue = new ComponenteImportoType();
							compValue.setAnnoAccertamento(Integer.valueOf(compImporto.getAnno()));
							compValue.setCausaleDescrittiva(iuv.getMotivazione());
							compValue.setImporto(compImporto.getImportoPerAcc());
							compValue.setNumeroAccertamento(compImporto.getCodBilAcc());
							compValue.setDatiSpecificiRiscossione(compImporto.getDatiSpecRisc());
							componentiImporto.getComponenteImporto().add(compValue);
						}
					}

					if (componentiImporto.getComponenteImporto().size() > 0) {
						p.setComponentiImporto(componentiImporto);
					}
				}

				posizioni.getPosizioneDaAggiornare().add(p);
//				} else {
//					LOGGER.info("[RicalcoloIUVApiServiceImpl::aggiornaPosizioniDebitorie] lo iuv da inviare con idIuv "
//							+ iuv.getIdIuv() + " ha importo 0.0, non la inserisco.");
//				}
			}

			posDaAggiornare = new ElencoPosizioniDaAggiornareType();
			posDaAggiornare.setPosizioniDaAggiornare(posizioni);
			request.setElencoPosizioniDaAggiornare(posDaAggiornare);
			request.setTestata(testata);

			return request;
		} catch (PagopaException e) {
			LOGGER.error("[RicalcoloIUVApiServiceImpl::aggiornaPosizioniDebitorie] ERROR" + e);
			throw e;
		} catch (Exception e) {
			LOGGER.error("[RicalcoloIUVApiServiceImpl::aggiornaPosizioniDebitorie] ERROR" + e);
			throw new PagopaException(e.getMessage());
		} finally {
			LOGGER.debug("[RicalcoloIUVApiServiceImpl::aggiornaPosizioniDebitorie] END");
		}
	}

	@Override
	public Response loadPosizioniDebitorieDaInviare(Long idElabora) {
		LOGGER.debug("[RicalcoloIUVApiServiceImpl::loadPosizioniDebitorieDaInviare] BEGIN");
		List<IuvDaInviareDTO> iuvDaInviare = iuvDaInviareDAO.getIuvDaInviare(idElabora);
		LOGGER.debug("[RicalcoloIUVApiServiceImpl::loadPosizioniDebitorieDaInviare] END");
		return Response.ok(iuvDaInviare).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	private PagopaListaCaricoDTO saveListaCarico(Long idElabora, LottoDTO lotto, IuvDaInviareDTO iuvDaInviare) {

		PagopaListaCaricoDTO listaCar = null;
		try {
			this.attore = iuvDaInviare.getGestAttoreIns();
			// Recupero il nap a partire dallo iuv
			String nap = iuvDAO.getIuvById(iuvDaInviare.getIdIuv()).getNap();

			listaCar = new PagopaListaCaricoDTO();
			listaCar.setIdLotto(lotto.getIdLotto());
			listaCar.setNap(nap);
			listaCar.setIndTipoAggiornamento(iuvDaInviare.getIndTipoAggiornamento());
			listaCar.setImportoNew(iuvDaInviare.getImportoNew());
			listaCar.setMotivazione(iuvDaInviare.getMotivazione());
			listaCar.setGestAttoreIns(attore);
			listaCar.setGestAttoreUpd(attore);

			listaCar = pagopaListaCaricoDAO.savePagopaListaCarico(listaCar);
//			if (listaCar.getIdPagopaListaCarico() != null) {
//				logStep(idElabora, PASSO_RICALCOLO_IUV, 0, FASE + " - Inserimento Lista Carico OK per l'elaborazione: "
//						+ idElabora + " - Nome Lotto: " + lotto.getNomeLotto() + " - nap: " + nap);
//			}
		} catch (Exception e) {
			logStep(idElabora, PASSO_RICALCOLO_IUV, 0, FASE + " - Inserimento Lista Carico KO per l'elaborazione: "
					+ idElabora + " - Nome Lotto: " + lotto.getNomeLotto() + " - idIuv: " + iuvDaInviare.getIdIuv());
		}
		return listaCar;
	}

	private void logStep(Long idElabora, String codPasso, int esito, String note) {
		RegistroElaboraDTO registroElabora = new RegistroElaboraDTO();
		registroElabora.setIdElabora(idElabora);
		registroElabora.setCodPassoElabora(codPasso);
		registroElabora.setFlgEsitoElabora(esito);
		registroElabora.setNotaElabora(note);
		registroElabora.setGestAttoreIns(attore);
		registroElabora.setGestAttoreUpd(attore);
		registroElabora.setCodFaseElabora(COD_FASE_RICALCOLO_IUV);
		try {
			registroElaboraDAO.saveRegistroElabora(registroElabora);
		} catch (Exception e) {
			LOGGER.error("[RicalcoloIUVApiServiceImpl::logStep] (Exception) ", e);
		}
		LOGGER.debug(note);
	}

}

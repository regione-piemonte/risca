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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.risca.riscabesrv.business.be.BollCompensazioniApi;
import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.impl.dao.AvvisoDatiAmminDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.AvvisoDatiTitolareDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.AvvisoPagamentoDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RataSdDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RegistroElaboraDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RimborsoDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RimborsoSdUtilizzatoDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.StatoDebitorioDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.TipoUsoDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.TipoUsoRegolaDAO;
import it.csi.risca.riscabesrv.dto.BollResultCompDTO;
import it.csi.risca.riscabesrv.dto.BollResultDTO;
import it.csi.risca.riscabesrv.dto.ProvvedimentoDTO;
import it.csi.risca.riscabesrv.dto.RataSdDTO;
import it.csi.risca.riscabesrv.dto.RegistroElaboraDTO;
import it.csi.risca.riscabesrv.dto.RimborsoExtendedDTO;
import it.csi.risca.riscabesrv.dto.RimborsoSdUtilizzatoDTO;
import it.csi.risca.riscabesrv.dto.RiscossioneBollDTO;
import it.csi.risca.riscabesrv.dto.RiscossioneBollData;
import it.csi.risca.riscabesrv.dto.StatoDebitorioDTO;
import it.csi.risca.riscabesrv.dto.StatoDebitorioExtendedDTO;
import it.csi.risca.riscabesrv.dto.TipoUsoDTO;
import it.csi.risca.riscabesrv.dto.TipoUsoRegolaDTO;
import it.csi.risca.riscabesrv.util.BollUtils;
import it.csi.risca.riscabesrv.util.Constants;

@Component
public class BollCompensazioniApiServiceImpl extends BaseApiServiceImpl implements BollCompensazioniApi {

	private static final String IDENTITY = "identity";
	private static Logger LOGGER = Logger.getLogger(Constants.LOGGER_NAME);

	public static final String PASSO_INS_SD_E_RIMB_UPD = "INS_SD_E_RIMB_UPD";
	public static final String PASSO_ESTRAE_CANONE_DOVUTO = "ESTRAE_CANONE_DOVUTO";
	public static final String PASSO_UPDATE_WORK_SD_UPD = "UPDATE_WORK_SD_UPD";
	public static final String PASSO_UPDATE_WORK_RIMB_UPD = "UPDATE_WORK_RIMB_UPD";
	public static final String PASSO_INSERT_WORK_RIM_UTIL = "INSERT_WORK_RIM_UTIL";
	public static final String PASSO_UPDATE_SD_IN_WORK = "UPDATE_SD_IN_WORK";
	public static final String PASSO_UPDATE_RATA_IN_WORK = "UPDATE_RATA_IN_WORK";
	public static final String PASSO_UPDATE_WORK_DT_AMMIN = "UPDATE_WORK_DT_AMMIN";
	public static final String PASSO_UPDATE_WORK_AVV_PAG = "UPDATE_WORK_AVV_PAG";
	public static final String PASSO_UPDATE_WORK_DATI_TIT = "WORK_DATI_TIT";
	public static final String PASSO_LETT_SD_ANNO_PREC = "LETT_SD_ANNO_PREC";
	public static final String PASSO_LETTURA_USI = "LETTURA_USI";
	public static final String PASSO_INSERT_SD_IN_WORK = "INSERT_SD_IN_WORK";

	public static final String COD_TIPO_USO_GRANDE_IDROELETTRICO = "GRANDE_IDROELETTRICO";
	public static final String COD_TIPO_USO_GRANDE_IDROELETTRICO_AGG = "GRANDE_IDRO_AGG";

	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final String DATE_FORMAT_SPED = "dd/MM/yyyy";

	public static final String FASE = "Emissione";

	public static final String COD_FASE_EMISBS = "EMISBS";
	public static final String COD_FASE_EMISBO = "EMISBO";
	public static final String COD_FASE_EMISBG = "EMISBG";

	public static final String TIPO_ELAB_BS = "BS";

	private String attore;

	@Autowired
	private RegistroElaboraDAO registroElaboraDAO;

	@Autowired
	private StatoDebitorioDAO statoDebitorioDAO;

	@Autowired
	private RimborsoDAO rimborsoDAO;

	@Autowired
	private RataSdDAO rataSdDAO;

	@Autowired
	private AvvisoDatiAmminDAO avvisoDatiAmminDAO;

	@Autowired
	private AvvisoPagamentoDAO avvisoPagamentoDAO;

	@Autowired
	private AvvisoDatiTitolareDAO avvisoDatiTitolareDAO;

	@Autowired
	private RimborsoSdUtilizzatoDAO rimborsoSdUtilizzatoDAO;

	@Autowired
	private TipoUsoDAO tipoUsoDAO;

	@Autowired
	private TipoUsoRegolaDAO tipoUsoRegolaDAO;

	/**
	 * @param securityContext SecurityContext
	 * @param httpHeaders     HttpHeaders
	 * @param httpRequest     HttpServletRequest
	 * @return Response
	 */
	@Override
	public Response initCompensazioni(Long idElabora, Long idAmbito, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[BollCompensazioniApiServiceImpl::initCompensazioni] BEGIN");
		BollResultDTO compResult = new BollResultDTO();

		try {
			statoDebitorioDAO.saveStatiDebitoriUpdDaCompensare(idElabora, idAmbito);
			rimborsoDAO.saveRimborsiUpdDaCompensare(idElabora, idAmbito);
			compResult.setStatus("OK");
		} catch (DAOException e) {
			LOGGER.error(
					"[BollCompensazioniApiServiceImpl::initCompensazioni] Errore inserimento SD e rimborsi da compensare");
			compResult.setStatus("KO");
			compResult.setStepError(PASSO_INS_SD_E_RIMB_UPD);
		}

		LOGGER.debug("[BollCompensazioniApiServiceImpl::initCompensazioni] END");
		return Response.ok(compResult).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	/**
	 * @param riscossioneBsData riscossioneBsData
	 * @param securityContext   SecurityContext
	 * @param httpHeaders       HttpHeaders
	 * @param httpRequest       HttpServletRequest
	 * @return Response
	 */
	@Override
	public Response elaboraCompensazioniBollSpeciale(RiscossioneBollData riscossioneBsData, int giro,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[BollCompensazioniApiServiceImpl::elaboraCompensazioniBollSpeciale] BEGIN");
		attore = "riscabatchspec";

		List<RimborsoExtendedDTO> rimborsiDaCompensare = null;
		if (giro == 1) {
			rimborsiDaCompensare = rimborsoDAO.loadRimborsiDaCompensareUpd(
					riscossioneBsData.getRiscossioneBoll().getIdRiscossione(),
					riscossioneBsData.getRiscossioneBoll().getIdSoggetto(),
					riscossioneBsData.getRiscossioneBoll().getIdGruppoSoggetto(), false);
		} else {
			rimborsiDaCompensare = rimborsoDAO.loadRimborsiDaCompensareUpd(null,
					riscossioneBsData.getRiscossioneBoll().getIdSoggetto(),
					riscossioneBsData.getRiscossioneBoll().getIdGruppoSoggetto(), false);
		}

		String codFase = riscossioneBsData.getTipoElabora().equals(TIPO_ELAB_BS) ? COD_FASE_EMISBS : COD_FASE_EMISBG;
		BollResultDTO compResult = elaboraCompensazioni(riscossioneBsData, rimborsiDaCompensare, codFase);

		LOGGER.debug("[BollCompensazioniApiServiceImpl::elaboraCompensazioniBollSpeciale] END");
		return Response.ok(compResult).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	private BollResultDTO elaboraCompensazioni(RiscossioneBollData riscossioneBsData,
			List<RimborsoExtendedDTO> rimborsiDaCompensare, String codFase) {
		BollResultDTO compResult = new BollResultDTO();
		BigDecimal canoneDovPerSdNewCorr = BigDecimal.ZERO;
		boolean errorCompensazione = false;
		for (RimborsoExtendedDTO rimborso : rimborsiDaCompensare) {

			BigDecimal compensCorrente = BigDecimal.ZERO;
			RataSdDTO rataSd = rataSdDAO
					.loadRataSdWorkingByStatoDebitorio(riscossioneBsData.getRiscossioneBoll().getIdStatoDebitorio());
			canoneDovPerSdNewCorr = rataSd.getCanoneDovuto();
			RiscossioneBollDTO risc = riscossioneBsData.getRiscossioneBoll();
			logStep(riscossioneBsData.getIdElabora(), PASSO_ESTRAE_CANONE_DOVUTO, 0,
					"Compensazioni - Estrazione canone dovuto al momento " + " - Riscossione: "
							+ risc.getIdRiscossione() + " - Soggetto: " + riscossioneBsData.getSoggettoGruppo()
							+ " - Stato debitorio: " + risc.getIdStatoDebitorio() + " - Canone dovuto: "
							+ canoneDovPerSdNewCorr,
					attore, codFase);
			if (BigDecimal.ZERO.compareTo(canoneDovPerSdNewCorr) == 0) {
				// si esce dal ciclo per la riscossione corrente perche il dovuto e' stato
				// azzerato e non c'e' piu' nulla da compensare
				continue;
			} else {
				// Calcolo la Compensazione rimanente per lo SD corrente
				BigDecimal iRimborso = rimborso.getImpRimborso() != null ? rimborso.getImpRimborso() : BigDecimal.ZERO;
				BigDecimal iRestituito = rimborso.getImpRestituito() != null ? rimborso.getImpRestituito()
						: BigDecimal.ZERO;
				BigDecimal compensRimanPerSdCorr = iRimborso.subtract(iRestituito);

				if (canoneDovPerSdNewCorr.compareTo(BigDecimal.ZERO) > 0
						&& canoneDovPerSdNewCorr.compareTo(compensRimanPerSdCorr) >= 0) {
					// Quanto dovuto e' maggiore della compensazione rimanente

					compensCorrente = compensRimanPerSdCorr;
					canoneDovPerSdNewCorr = canoneDovPerSdNewCorr.subtract(compensCorrente);

					// la Compensazione e' stata completamente utilizzata: modificare il record
					// corrispondente nella tabella RISCA_W_STATO_DEBITORIO_UPD in modo che in caso
					// di Conferma lo SD ("vecchio") venga impostato come 'REGOLARIZZATO' e con
					// nessuna attivita' da effettuare (id_attivita_stato_debitorio = NULL,
					// id_stato_contribuzione = 3 "Regolarizzato")
					try {
						statoDebitorioDAO.updateStatoDebitorioUpdRegolarizzato(risc.getIdStatoDebitorio(),
								riscossioneBsData.getIdElabora());
						logStep(riscossioneBsData.getIdElabora(), PASSO_UPDATE_WORK_SD_UPD, 0,
								"Compensazioni - Modifica Stato contribuzione " + " - Riscossione: "
										+ risc.getIdRiscossione() + " - Soggetto: "
										+ riscossioneBsData.getSoggettoGruppo() + " - Stato debitorio: "
										+ risc.getIdStatoDebitorio(),
								attore, codFase);
					} catch (DAOException e) {
						errorCompensazione = true;
						compResult.setStepError(PASSO_UPDATE_WORK_SD_UPD);
						break;
					}

					// modificare il record corrispondente nella tabella RISCA_W_RIMBORSO_UPD in
					// modo che in caso di Conferma il Rimborso venga impostato come 'COMPENSATO'
					// e la parte rimborsata/compensata corrisponda all'importo da rimborsare
					// (id_tipo_rimborso = 3 "Compensato", imp_restituito = imp_rimborso)
					try {
						rimborsoDAO.updateRimborsoUpdCompensato(rimborso.getIdRimborso(), rimborso.getImpRimborso(),
								riscossioneBsData.getIdElabora());
						logStep(riscossioneBsData.getIdElabora(), PASSO_UPDATE_WORK_RIMB_UPD, 0,
								"Compensazioni - Modifica Tipo e Importo Rimborso " + " - Riscossione: "
										+ risc.getIdRiscossione() + " - Soggetto: "
										+ riscossioneBsData.getSoggettoGruppo() + " - Stato debitorio: "
										+ risc.getIdStatoDebitorio() + " - Rimborso: " + rimborso.getIdRimborso()
										+ " - Restituito: " + rimborso.getImpRimborso(),
								attore, codFase);
					} catch (DAOException e) {
						errorCompensazione = true;
						compResult.setStepError(PASSO_UPDATE_WORK_RIMB_UPD);
						break;
					}

					// inserire un record nella tabella RISCA_W_RIMBORSI_SD_UTILIZ in modo che in
					// caso di CONFERMA il Rimborso utilizzato per SD corrisponda alla parte di
					// Rimborso che era rimasto ed e' stato utilizzato
					boolean ret = insertRimborsoUtilizzato(rimborso, compensRimanPerSdCorr, riscossioneBsData, codFase);
					if (!ret) {
						errorCompensazione = true;
						compResult.setStepError(PASSO_INSERT_WORK_RIM_UTIL);
						break;
					}

				} else if (canoneDovPerSdNewCorr.compareTo(BigDecimal.ZERO) > 0
						&& canoneDovPerSdNewCorr.compareTo(compensRimanPerSdCorr) < 0) {

					compensCorrente = canoneDovPerSdNewCorr;

					// siaggiorna RISCA_W_RIMBORSO_UPD in modo che in caso di Conferma nel Rimborso
					// la parte rimborsata/compensata venga integrata dalla compensazione corrente
					// (imp_restituito = NVL(imp_restituito,0) + v_compens_corrente)
					try {
						rimborsoDAO.updateRimborsoUpdCompensazione(rimborso.getIdRimborso(), compensCorrente,
								riscossioneBsData.getIdElabora());
						logStep(riscossioneBsData.getIdElabora(), PASSO_UPDATE_WORK_RIMB_UPD, 0,
								"Compensazioni - Modifica Tipo e Importo Rimborso " + " - Riscossione: "
										+ risc.getIdRiscossione() + " - Soggetto: "
										+ riscossioneBsData.getSoggettoGruppo() + " - Stato debitorio: "
										+ risc.getIdStatoDebitorio() + " - Rimborso: " + rimborso.getIdRimborso()
										+ " - CompensCorrente: " + compensCorrente,
								attore, codFase);
					} catch (DAOException e) {
						errorCompensazione = true;
						compResult.setStepError(PASSO_UPDATE_WORK_RIMB_UPD);
						break;
					}

					// inserire un record in RISCA_W_RIMBORSI_SD_UTILIZ in modo che in caso di
					// CONFERMA
					// il Rimborso utilizzato per SD corrisponda alla Compensazione corrente
					boolean ret = insertRimborsoUtilizzato(rimborso, compensCorrente, riscossioneBsData, codFase);
					if (!ret) {
						errorCompensazione = true;
						compResult.setStepError(PASSO_INSERT_WORK_RIM_UTIL);
						break;
					}

					// il dovuto viene azzerato
					canoneDovPerSdNewCorr = BigDecimal.ZERO;
				}

				// Aggiornare nella tabella RISCA_W_STATO_DEBITORIO la compensazione applicata
				// (campo imp_compens_canone), per lo stato debitorio corrente, con il valore di
				// imp_compens_canone + v_compens_corrente
				try {
					statoDebitorioDAO.updateStatoDebitorioWorkingCompensazione(risc.getIdStatoDebitorio(),
							compensCorrente);
					logStep(riscossioneBsData.getIdElabora(), PASSO_UPDATE_SD_IN_WORK, 0,
							"Compensazioni - Modifica Stato Debitorio " + " - Riscossione: " + risc.getIdRiscossione()
									+ " - Soggetto: " + risc.getIdSoggetto() + " - Stato debitorio: "
									+ risc.getIdStatoDebitorio() + " - Rimborso: " + rimborso.getIdRimborso()
									+ " - CompensCorrente: " + compensCorrente,
							attore, codFase);
				} catch (DAOException e) {
					errorCompensazione = true;
					compResult.setStepError(PASSO_UPDATE_SD_IN_WORK);
					break;
				}

				// Sono state eseguite compensazioni?
				if (compensCorrente.compareTo(BigDecimal.ZERO) != 0) {
					// Aggiornare la tabella di working RISCA_W_RATA_SD (al dovuto inserito
					// precedentemente sottraggo l'eventuale ulteriore Compensazione)
					try {
						rataSdDAO.updateRataSdWorkingCanoneDovutoCompensazione(risc.getIdStatoDebitorio(),
								compensCorrente);
						logStep(riscossioneBsData.getIdElabora(), PASSO_UPDATE_RATA_IN_WORK, 0,
								"Compensazioni - Modifica Rata Stato Debitorio " + " - Riscossione: "
										+ risc.getIdRiscossione() + " - Soggetto: " + risc.getIdSoggetto()
										+ " - Stato debitorio: " + risc.getIdStatoDebitorio() + " - CompensCorrente: "
										+ compensCorrente,
								attore, codFase);
					} catch (DAOException e) {
						errorCompensazione = true;
						compResult.setStepError(PASSO_UPDATE_RATA_IN_WORK);
						break;
					}

					// Aggiornare la tabella RISCA_W_AVVISO_DATI_AMMIN (al Dovuto calcolato nella
					// funzione precedente, sottraggo l'eventuale ulteriore Compensazione, alle
					// Compensazioni calcolate nella funzione precedente, sommo l'eventuale
					// ulteriore Compensazione)
					try {
						avvisoDatiAmminDAO.updateWorkingAvvisoDatiAmminCompensazione(compensCorrente,
								riscossioneBsData.getNap().getNap(),
								riscossioneBsData.getRiscossioneBoll().getCodRiscossione());
						logStep(riscossioneBsData.getIdElabora(), PASSO_UPDATE_WORK_DT_AMMIN, 0,
								"Compensazioni - Modifica Avviso Dati Amministrativi " + " - Riscossione: "
										+ risc.getIdRiscossione() + " - Soggetto: "
										+ riscossioneBsData.getSoggettoGruppo() + " - NAP: "
										+ riscossioneBsData.getNap().getNap() + " - CompensCorrente: "
										+ compensCorrente,
								attore, codFase);
					} catch (DAOException e) {
						errorCompensazione = true;
						compResult.setStepError(PASSO_UPDATE_WORK_DT_AMMIN);
						break;
					}

					// Aggiornare la tabella RISCA_W_AVVISO_PAGAMENTO (al Dovuto calcolato nella
					// funzione precedente, sottraggo l'eventuale ulteriore Compensazione)
					try {
						avvisoPagamentoDAO.updateWorkingAvvisoPagamentoCompensazione(compensCorrente,
								riscossioneBsData.getNap().getNap());
						logStep(riscossioneBsData.getIdElabora(), PASSO_UPDATE_WORK_AVV_PAG, 0,
								"Compensazioni - Modifica Avviso Pagamento " + " - Riscossione: "
										+ risc.getIdRiscossione() + " - Soggetto: "
										+ riscossioneBsData.getSoggettoGruppo() + " - NAP: "
										+ riscossioneBsData.getNap().getNap() + " - CompensCorrente: "
										+ compensCorrente,
								attore, codFase);
					} catch (DAOException e) {
						errorCompensazione = true;
						compResult.setStepError(PASSO_UPDATE_WORK_AVV_PAG);
						break;
					}

					// Aggiornare la tabella RISCA_W_AVVISO_DATI_TITOLARE (al Dovuto calcolato nella
					// funzione precedente, sottraggo l'eventuale ulteriore Compensazione)
					try {
						avvisoDatiTitolareDAO.updateWorkingDatiTitolareCompensazione(compensCorrente,
								riscossioneBsData.getNap().getNap());
						logStep(riscossioneBsData.getIdElabora(), PASSO_UPDATE_WORK_DATI_TIT, 0,
								"Compensazioni - Modifica Dati Titolare " + " - Riscossione: " + risc.getIdRiscossione()
										+ " - Soggetto: " + riscossioneBsData.getSoggettoGruppo() + " - NAP: "
										+ riscossioneBsData.getNap().getNap() + " - CompensCorrente: "
										+ compensCorrente,
								attore, codFase);
					} catch (DAOException e) {
						errorCompensazione = true;
						compResult.setStepError(PASSO_UPDATE_WORK_DATI_TIT);
						break;
					}
				}
			}
		}
		if (errorCompensazione) {
			compResult.setStatus("KO");
		} else {
			compResult.setStatus("OK");
		}
		return compResult;
	}

	/**
	 * @param riscossioneBsData riscossioneBsData
	 * @param securityContext   SecurityContext
	 * @param httpHeaders       HttpHeaders
	 * @param httpRequest       HttpServletRequest
	 * @return Response
	 */
	@Override
	public Response elaboraCompensazioniSoggettoPraticaBollOrdinaria(RiscossioneBollData riscossioneBsData,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[BollCompensazioniApiServiceImpl::elaboraCompensazioniSoggettoPraticaBollOrdinaria] BEGIN");
		BollResultCompDTO compResult = new BollResultCompDTO();
		boolean error = false;
		attore = "riscabatchord";

		Long idPraticaCorr = riscossioneBsData.getRiscossioneBoll().getIdRiscossione();

		// Leggere Stato Debitorio dell' anno precedente rispetto all'anno in input per
		// gli Stati Debitori con addebito anno precedente

		BigDecimal canoneDovutoAnnoPrec = BigDecimal.ZERO;
		int flgAddebitoAnnoSuccessivo = 0;
		// InteressiMat non si usa piu' a seguito di verifiche con CALA'
		// BigDecimal interessiMat = BigDecimal.ZERO;
		try {
			StatoDebitorioExtendedDTO sdPrecedPerRisc = statoDebitorioDAO
					.loadStatoDebitorioAnnoPrecedente(idPraticaCorr);
			if (sdPrecedPerRisc != null && sdPrecedPerRisc.getIdStatoDebitorio() != null) {
				flgAddebitoAnnoSuccessivo = sdPrecedPerRisc.getFlgAddebitoAnnoSuccessivo();
				RataSdDTO rata = rataSdDAO.loadRataSdByStatoDebitorio(sdPrecedPerRisc.getIdStatoDebitorio());
				if (rata != null) {
					canoneDovutoAnnoPrec = rata.getCanoneDovuto();
					// interessiMat = rata.getInteressiMaturati();

					logStep(riscossioneBsData.getIdElabora(), PASSO_LETT_SD_ANNO_PREC, 0,
							FASE + " - Step 5 - Lettura dati dello Stato Debitorio anno precedente", attore,
							COD_FASE_EMISBO);
				} else {
					compResult.setStatus("KO");
					compResult.setStepError(PASSO_LETT_SD_ANNO_PREC);
					error = true;
				}
			}
		} catch (DAOException e) {
			compResult.setStatus("KO");
			compResult.setStepError(PASSO_LETT_SD_ANNO_PREC);
			error = true;
		}

		if (!error) {
			// Acquisizione degli USI
			String concatDescrUsi = "";
			List<TipoUsoDTO> usiRisc = null;
			try {
				usiRisc = tipoUsoDAO.loadTipoUsoByRiscossione(idPraticaCorr);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				LOGGER.error(e);
			}
			for (TipoUsoDTO tipoUso : usiRisc) {
				concatDescrUsi += tipoUso.getDesTipouso() + ";";
				if (tipoUso.getCodTipouso().equals(COD_TIPO_USO_GRANDE_IDROELETTRICO)
						&& isConcessioneScadutaConIstanzaRinnovo(riscossioneBsData)) {
					BigDecimal canoneAggiuntivo = calcolaCanoneAggiuntivoGrandeIdro(riscossioneBsData, tipoUso);
					if (canoneAggiuntivo == null) {
						error = true;
						compResult.setStatus("KO");
						compResult.setStepError(PASSO_LETTURA_USI);
						compResult.setErrorMessage("Errore in calcolaCanoneAggiuntivoGrandeIdro");
						break;
					}
					// Il canone aggiuntivo va salvato per poi essere sommato a quello calcolato per
					// il GRANDE_IDROELETTRICO
					compResult.setCanoneAggiuntivoGrandeIdro(canoneAggiuntivo);
				}
			}
			if (!error) {
				logStep(riscossioneBsData.getIdElabora(), PASSO_LETTURA_USI, 0,
						FASE + " - Step 5 - Lettura dati degli USI", attore, COD_FASE_EMISBO);

				// Inserire i dati del nuovo SD nella tavola di working
				Long idStatoDeb = insertStatoDebitorio(riscossioneBsData, concatDescrUsi, flgAddebitoAnnoSuccessivo,
						canoneDovutoAnnoPrec);
				if (idStatoDeb == null) {
					error = true;
					compResult.setStatus("KO");
					compResult.setStepError(PASSO_INSERT_SD_IN_WORK);
				} else {
					// Salvo l'id del nuovo stato debitorio per usalro nelle successive operazioni
					riscossioneBsData.getRiscossioneBoll().setIdStatoDebitorio(idStatoDeb);
				}
			}
		}

		double sommCompensPerPrat = 0.0;
		if (!error) {
			// Verifico le compensazioni

			// Troncare il canone perche' per legge il Canone va troncato
			BigDecimal canonePratica = riscossioneBsData.getCanonePratica().setScale(0, RoundingMode.DOWN);
			BigDecimal totCanoniDovuti = canonePratica.add(canoneDovutoAnnoPrec.setScale(0, RoundingMode.DOWN));
			RiscossioneBollDTO risc = riscossioneBsData.getRiscossioneBoll();
			// Leggere i Rimborsi ancora da compensare legati alla Pratica (e al Soggetto)
			List<RimborsoExtendedDTO> sdDacompensarePerPratica = rimborsoDAO.loadRimborsiDaCompensareUpd(idPraticaCorr,
					risc.getIdSoggetto(), risc.getIdGruppoSoggetto(), true);

			double totCanoni = bdToDouble(totCanoniDovuti);

			for (RimborsoExtendedDTO sdDacompensare : sdDacompensarePerPratica) {
				double impRestituito = 0.0;
				double compensRimanente = bdToDouble(sdDacompensare.getImpRimborso())
						- bdToDouble(sdDacompensare.getImpRestituito());

				if (totCanoni > 0 && totCanoni >= compensRimanente) {
					// Il totale dei canoni (attuale e dell'anno precedente) e' >= della
					// compensazione rimanente

					// Alla sommatoria delle Compensazioni per Pratica aggiungere la Compensazione
					// da effettuare in questo giro (= quella rimanente per lo SD corrente)
					sommCompensPerPrat = sommCompensPerPrat + compensRimanente;

					// Al totale dei canoni sottrarre la compensazione corrente
					totCanoni = totCanoni - compensRimanente;

					// Poiche' la Compensazione e' stata completamente utilizzata predisporre la
					// tavola di working in modo che in caso di Conferma lo SD venga impostato come
					// 'REGOLARIZZATO' e con nessuna attivita' da effettuare
					try {
						statoDebitorioDAO.updateStatoDebitorioUpdRegolarizzato(risc.getIdStatoDebitorio(),
								riscossioneBsData.getIdElabora());
						logStep(riscossioneBsData.getIdElabora(), PASSO_UPDATE_WORK_SD_UPD, 0,
								FASE + " - Step 5 - Modifica stato contribuzione in W_STATO_DEBITORIO_UPD per SD "
										+ sdDacompensare.getIdStatoDebitorio(),
								attore, COD_FASE_EMISBO);
					} catch (DAOException e) {
						error = true;
						compResult.setStepError(PASSO_UPDATE_WORK_SD_UPD);
						break;
					}

					// predisporre la tavola di working affinche' in caso di Conferma il Rimborso
					// venga impostato come 'Compensato' e la parte rimborsata/compensata
					// corrisponda all'importo da rimborsare
					try {
						rimborsoDAO.updateRimborsoUpdCompensato(sdDacompensare.getIdRimborso(),
								sdDacompensare.getImpRimborso(), riscossioneBsData.getIdElabora());
						logStep(riscossioneBsData.getIdElabora(), PASSO_UPDATE_WORK_RIMB_UPD, 0, FASE
								+ " - Step 5 - Modifica Tipo e Importo Restituito in RISCA_W_RIMBORSO_UPD per RIMBORSO "
								+ sdDacompensare.getIdRimborso(), attore, COD_FASE_EMISBO);
					} catch (DAOException e) {
						error = true;
						compResult.setStepError(PASSO_UPDATE_WORK_RIMB_UPD);
						break;
					}

					// predisporre la tavola di working in modo che in caso di Conferma il Rimborso
					// utilizzato per SD corrisponda alla parte di Rimborso che era rimasto ed e'
					// stato utilizzato
					boolean ret = insertRimborsoUtilizzato(sdDacompensare, BigDecimal.valueOf(compensRimanente),
							riscossioneBsData, COD_FASE_EMISBO);
					if (!ret) {
						error = true;
						compResult.setStepError(PASSO_INSERT_WORK_RIM_UTIL);
						break;
					}

				} else if (totCanoni > 0 && totCanoni < compensRimanente) {
					// Il totale dei canoni e' < della compensazione rimanente

					// alla sommatoria delle Compensazioni per Pratica aggiungere la Compensazione
					// da effettuare in questo giro (che coincide con v_tot_canoni_dovuti)
					sommCompensPerPrat = sommCompensPerPrat + totCanoni;

					impRestituito = bdToDouble(sdDacompensare.getImpRestituito()) + totCanoni;

					// Predisporre la tavola di working in modo che in caso di Conferma nel Rimborso
					// la parte rimborsata/compensata venga integrata dalla Compensazione corrente
					// (che coincide con v_tot_canoni_dovuti->totCanoni)
					try {
						rimborsoDAO.updateRimborsoUpdCompensazione(sdDacompensare.getIdRimborso(),
								BigDecimal.valueOf(totCanoni), riscossioneBsData.getIdElabora());
						logStep(riscossioneBsData.getIdElabora(), PASSO_UPDATE_WORK_RIMB_UPD, 0,
								FASE + " - Step 5 - Modifica Importo Restituito in RISCA_W_RIMBORSO_UPD - Rimborso "
										+ sdDacompensare.getIdRimborso() + " - Importo Restituito " + impRestituito,
								attore, COD_FASE_EMISBO);
					} catch (DAOException e) {
						error = true;
						compResult.setStepError(PASSO_UPDATE_WORK_RIMB_UPD);
						break;
					}

					// inserire un record in RISCA_W_RIMBORSI_SD_UTILIZ in modo che in caso di
					// CONFERMA il Rimborso utilizzato per SD corrisponda alla Compensazione
					// corrente

					// Predisporre la tavola di working affinche' in caso di Conferma il Rimborso
					// utilizzato per SD corrisponda alla Compensazione corrente (che coincide con
					// v_tot_canoni_dovuti)
					boolean ret = insertRimborsoUtilizzato(sdDacompensare, BigDecimal.valueOf(totCanoni),
							riscossioneBsData, COD_FASE_EMISBO);
					if (!ret) {
						error = true;
						compResult.setStepError(PASSO_INSERT_WORK_RIM_UTIL);
						break;
					}

					// il dovuto viene azzerato
					totCanoni = 0.0;
				}
				
				if(totCanoni == 0) {
					// se totCanoni == 0 uscire dal ciclo delle compensazioni per la riscossione
					// corrente perche' il totale canoni e' stato azzerato e quindi non c'e' piu'
					// nulla da compensare
					break;
				}
				
			}
			
			// aggiornare nella tavola di working che contiene i nuovi SD, per lo SD
			// corrente, la sommatoria delle Compensazioni che gli sono state applicate
			try {
				statoDebitorioDAO.updateStatoDebitorioWorkingCompensazione(risc.getIdStatoDebitorio(),
						BigDecimal.valueOf(sommCompensPerPrat));
				logStep(riscossioneBsData.getIdElabora(), PASSO_UPDATE_SD_IN_WORK, 0,
						FASE + " - Step 5 - Modifica SD nella tabella RISCA_W_STATO_DEBITORIO - SD "
								+ risc.getIdStatoDebitorio() + " - Importo Compensazione Canone "
								+ sommCompensPerPrat,
						attore, COD_FASE_EMISBO);
			} catch (DAOException e) {
				error = true;
				compResult.setStepError(PASSO_UPDATE_SD_IN_WORK);
			}

			if (error) {
				compResult.setStatus("KO");
			} else {
				compResult.setStatus("OK");
				compResult.setIdStatoDebitorio(riscossioneBsData.getRiscossioneBoll().getIdStatoDebitorio());
				compResult.setCanoneDovutoAnnoPrec(canoneDovutoAnnoPrec);
				compResult.setSommCompensPerPrat(BigDecimal.valueOf(sommCompensPerPrat));
			}
		}

		LOGGER.debug("[BollCompensazioniApiServiceImpl::elaboraCompensazioniSoggettoPraticaBollOrdinaria] END");
		return Response.ok(compResult).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	private boolean isConcessioneScadutaConIstanzaRinnovo(RiscossioneBollData riscossioneAttiva) {
		Date dataScadConcessione = riscossioneAttiva.getRiscossioneBoll().getDataScadConcessione() != null
				? riscossioneAttiva.getRiscossioneBoll().getDataScadConcessione()
				: getMaxDate();
		boolean concessioneScaduta = dataScadConcessione.before(new Date());
		boolean istanzaDiRinnovo = hasIstanzaDiRinnovo(riscossioneAttiva);
		if (concessioneScaduta && istanzaDiRinnovo) {
			return true;
		}
		return false;
	}

	private boolean hasIstanzaDiRinnovo(RiscossioneBollData riscossioneAttiva) {
		ProvvedimentoDTO istanza = riscossioneAttiva.getIstanza();
		boolean ret = false;
		if (istanza != null) {
			String codTipoProvvedimento = istanza.getTipiProvvedimentoExtendedDTO().getCodTipoProvvedimento();
			switch (codTipoProvvedimento) {
			case BollUtils.COD_TIPO_PROVVEDIMENTO_IST_RINNOVO:
			case BollUtils.COD_TIPO_PROVVEDIMENTO_IST_SANATORIA:
			case BollUtils.COD_TIPO_PROVVEDIMENTO_AUT_PROVVISORIA:
				ret = true;
				break;
			default:
				ret = false;
				break;
			}
		}
		return ret;
	}

	private Date getMaxDate() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 9999);
		cal.set(Calendar.MONTH, Calendar.DECEMBER);
		cal.set(Calendar.DAY_OF_MONTH, 31);
		return cal.getTime();
	}

	/**
	 * @param riscossioneBsData riscossioneBsData
	 * @param securityContext   SecurityContext
	 * @param httpHeaders       HttpHeaders
	 * @param httpRequest       HttpServletRequest
	 * @return Response
	 */
	@Override
	public Response elaboraCompensazioniSoggettoBollOrdinaria(RiscossioneBollData riscossioneBsData,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[BollCompensazioniApiServiceImpl::elaboraCompensazioniSoggettoBollOrdinaria] BEGIN");
		attore = "riscabatchord";
		List<RimborsoExtendedDTO> rimborsiDaCompensare = rimborsoDAO.loadRimborsiDaCompensareUpd(null,
				riscossioneBsData.getRiscossioneBoll().getIdSoggetto(),
				riscossioneBsData.getRiscossioneBoll().getIdGruppoSoggetto(), true);

		BollResultDTO compResult = elaboraCompensazioni(riscossioneBsData, rimborsiDaCompensare, COD_FASE_EMISBO);

		LOGGER.debug("[BollCompensazioniApiServiceImpl::elaboraCompensazioniSoggettoBollOrdinaria] END");
		return Response.ok(compResult).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	private BigDecimal calcolaCanoneAggiuntivoGrandeIdro(RiscossioneBollData riscossioneBsData, TipoUsoDTO tipoUso) {
		try {
			String dataRiferimento = riscossioneBsData.getAnno() + "-01-01";
			TipoUsoRegolaDTO regola = tipoUsoRegolaDAO.loadTipoUsoRegolaByCodTipoUso(dataRiferimento,
					COD_TIPO_USO_GRANDE_IDROELETTRICO_AGG);
			if (regola != null) {
				String jsonRegola = regola.getJsonRegola();
				BigDecimal canoneUnitario = BollUtils.getCanoneUnitarioFromRegola(jsonRegola);
				BigDecimal quantita = BollUtils.estraiQuantita(riscossioneBsData.getRiscossioneBoll().getJsonDt(),
						tipoUso.getIdTipoUso());
				BigDecimal canone = canoneUnitario.multiply(quantita).setScale(2, RoundingMode.HALF_UP);
				return canone;
			} else {
				LOGGER.error(
						"[BollCompensazioniApiServiceImpl::calcolaCanoneGrandeIdroelettricoAgg] jsonRegola non trovato");
			}

		} catch (Exception e) {
			LOGGER.error("[BollCompensazioniApiServiceImpl::calcolaCanoneGrandeIdroelettricoAgg] " + e.getMessage());
		}
		return null;
	}

	private double bdToDouble(BigDecimal bd) {
		if (bd != null) {
			return bd.doubleValue();
		}
		return 0.0;
	}

	private Long insertStatoDebitorio(RiscossioneBollData riscossioneBsData, String descrUsi,
			int flgAddebitoAnnoSuccessivo, BigDecimal canoneDovutoAnnoPrec) {
		LOGGER.debug("[BollCompensazioniApiServiceImpl::insertStatoDebitorio] BEGIN");
		StatoDebitorioExtendedDTO statoDebitorio = new StatoDebitorioExtendedDTO();
		RiscossioneBollDTO riscAttiva = riscossioneBsData.getRiscossioneBoll();

		statoDebitorio.setIdAttivitaStatoDeb(null);
		statoDebitorio.setNap(riscossioneBsData.getNap().getNap());
		statoDebitorio.setIdRiscossione(riscAttiva.getIdRiscossione());
//		statoDebitorio.setIdProvvedimento(riscossioneBsData.getProvvedimento().getIdProvvedimento());
		statoDebitorio.setIdSoggetto(riscAttiva.getIdSoggetto());
		statoDebitorio.setIdGruppoSoggetto(riscAttiva.getIdGruppoSoggetto());
		Long idRecapito = riscAttiva.getIdRecapitoA() != null ? riscAttiva.getIdRecapitoA()
				: riscAttiva.getIdRecapitoP();
		statoDebitorio.setIdRecapito(idRecapito);
		statoDebitorio.setIdStatoContribuzione(null);

		SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
		try {
			statoDebitorio.setDataRichiestaProtocollo(df.parse(riscossioneBsData.getDataProtocollo()));
		} catch (ParseException e) {
			LOGGER.error(e);
			LOGGER.error("[BollCompensazioniApiServiceImpl::insertStatoDebitorio] Error parsing DataProtocollo: "
					+ riscossioneBsData.getDataProtocollo());
			return null;
		}
		statoDebitorio.setDataUltimaModifica(new Date());
		statoDebitorio.setDesUsi(descrUsi);
		statoDebitorio.setNote(null);
		if (flgAddebitoAnnoSuccessivo == 1) {
			// troncamento dei decimali
			statoDebitorio.setImpRecuperoInteressi(canoneDovutoAnnoPrec.setScale(0, RoundingMode.DOWN));
		}
		statoDebitorio.setFlgAddebitoAnnoSuccessivo(0);
		statoDebitorio.setImpRecuperoInteressi(null);
		statoDebitorio.setDescPeriodoPagamento("" + riscossioneBsData.getAnno());
		// statoDebitorio.setIdTipoDilazione(null);
		statoDebitorio.setNumRichiestaProtocollo(riscossioneBsData.getNumProtocollo());
		statoDebitorio.setFlgDilazione(0);
		statoDebitorio.setFlgInvioSpeciale(0);
		statoDebitorio.setImpSpeseNotifica(BigDecimal.ZERO);
		statoDebitorio.setFlgRestituitoMittente(0);
		statoDebitorio.setFlgAnnullato(0);
		statoDebitorio.setDescMotivoAnnullo(null);
		statoDebitorio.setImpCompensazioneCanone(null);

		if (riscossioneBsData.getProvvedimento() != null) {
			ProvvedimentoDTO provv = riscossioneBsData.getProvvedimento();
			statoDebitorio.setNumTitolo(provv.getNumTitolo());
			statoDebitorio.setNumTitolo(provv.getDataProvvedimento());
			statoDebitorio.setNumTitolo(provv.getTipoTitoloExtendedDTO().getDesTipoTitolo());
		}

		StatoDebitorioDTO ret = null;
		try {
			ret = statoDebitorioDAO.saveStatoDebitorioWorking(statoDebitorio, true);
		} catch (DAOException e) {
			LOGGER.error("[BollCompensazioniApiServiceImpl::insertStatoDebitorio] Inserimento SD fallito: "
					+ statoDebitorio);
			return null;
		}

		logStep(riscossioneBsData.getIdElabora(), PASSO_INSERT_SD_IN_WORK, 0,
				FASE + " - Step 5 - Inserimento SD nella tabella di Working: " + ret.getIdStatoDebitorio(), attore,
				COD_FASE_EMISBO);
		LOGGER.debug("[BollCompensazioniApiServiceImpl::insertStatoDebitorio] END idStatoDebitorio: "
				+ ret.getIdStatoDebitorio());
		return ret.getIdStatoDebitorio();
	}

	private boolean insertRimborsoUtilizzato(RimborsoExtendedDTO rimborso, BigDecimal compensazione,
			RiscossioneBollData riscossioneBsData, String codFase) {
		RimborsoSdUtilizzatoDTO rimborsoUtilizzato = new RimborsoSdUtilizzatoDTO();
		rimborsoUtilizzato.setIdStatoDebitorio(riscossioneBsData.getRiscossioneBoll().getIdStatoDebitorio());
		rimborsoUtilizzato.setIdRimborso(rimborso.getIdRimborso());
		rimborsoUtilizzato.setImpUtilizzato(compensazione);
		try {
			rimborsoSdUtilizzatoDAO.saveRimborsoSdUtilizzatoWorking(rimborsoUtilizzato,
					riscossioneBsData.getIdElabora());
			RiscossioneBollDTO risc = riscossioneBsData.getRiscossioneBoll();
			logStep(riscossioneBsData.getIdElabora(), PASSO_INSERT_WORK_RIM_UTIL, 0,
					FASE + " - Compensazioni - Inserimento Rimborso SD Utilizzato " + " - Riscossione: "
							+ risc.getIdRiscossione() + " - Soggetto: " + riscossioneBsData.getSoggettoGruppo()
							+ " - Stato debitorio: " + risc.getIdStatoDebitorio() + " - Rimborso: "
							+ rimborso.getIdRimborso() + " - Importo: " + compensazione,
					attore, codFase);
		} catch (DAOException e) {
			return false;
		}
		return true;
	}

	private void logStep(Long idElabora, String codPasso, int esito, String note, String attore, String codFase) {
		RegistroElaboraDTO registroElabora = new RegistroElaboraDTO();
		registroElabora.setIdElabora(idElabora);
		registroElabora.setCodPassoElabora(codPasso);
		registroElabora.setFlgEsitoElabora(esito);
		registroElabora.setNotaElabora(note);
		registroElabora.setGestAttoreIns(attore);
		registroElabora.setGestAttoreUpd(attore);
		registroElabora.setCodFaseElabora(codFase);
		try {
			registroElaboraDAO.saveRegistroElabora(registroElabora);
		} catch (Exception e) {
			LOGGER.error("[BollCompensazioniApiServiceImpl::logStep] (Exception) ", e);
		}
		LOGGER.debug(note);
	}

}

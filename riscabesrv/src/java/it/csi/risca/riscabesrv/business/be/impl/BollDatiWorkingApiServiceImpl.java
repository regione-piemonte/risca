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
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.csi.risca.riscabesrv.business.be.BollDatiWorkingApi;
import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.impl.dao.AnnualitaSdDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.AnnualitaUsoSdDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.AnnualitaUsoSdRaDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.AvvisoAnnualitaDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.AvvisoDatiAmminDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.AvvisoDatiTitolareDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.AvvisoPagamentoDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.AvvisoUsoDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.ComponentiDtDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.IuvDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RataSdDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RegistroElaboraDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiduzioneAumentoDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RimborsoDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RimborsoSdUtilizzatoDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.SpedizioneDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.StatoDebitorioDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.TipoDilazioneDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.TipoUsoDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.TipoUsoRegolaDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.UnitaMisuraDAO;
import it.csi.risca.riscabesrv.dto.AnnualitaSdDTO;
import it.csi.risca.riscabesrv.dto.AnnualitaUsoSdDTO;
import it.csi.risca.riscabesrv.dto.AnnualitaUsoSdExtendedDTO;
import it.csi.risca.riscabesrv.dto.AnnualitaUsoSdRaDTO;
import it.csi.risca.riscabesrv.dto.AvvisoAnnualitaDTO;
import it.csi.risca.riscabesrv.dto.AvvisoDatiAmminDTO;
import it.csi.risca.riscabesrv.dto.AvvisoDatiTitolareDTO;
import it.csi.risca.riscabesrv.dto.AvvisoPagamentoDTO;
import it.csi.risca.riscabesrv.dto.AvvisoUsoDTO;
import it.csi.risca.riscabesrv.dto.BollResultDTO;
import it.csi.risca.riscabesrv.dto.BollResultWorkDTO;
import it.csi.risca.riscabesrv.dto.ComponenteDtDTO;
import it.csi.risca.riscabesrv.dto.IndirizzoSpedizioneDTO;
import it.csi.risca.riscabesrv.dto.ProvvedimentoDTO;
import it.csi.risca.riscabesrv.dto.RataSdDTO;
import it.csi.risca.riscabesrv.dto.RecapitiExtendedDTO;
import it.csi.risca.riscabesrv.dto.RegistroElaboraDTO;
import it.csi.risca.riscabesrv.dto.RiduzioneAumentoDTO;
import it.csi.risca.riscabesrv.dto.RiscossioneBollDTO;
import it.csi.risca.riscabesrv.dto.RiscossioneBollData;
import it.csi.risca.riscabesrv.dto.StatoDebitorioDTO;
import it.csi.risca.riscabesrv.dto.StatoDebitorioExtendedDTO;
import it.csi.risca.riscabesrv.dto.TipoDilazioneDTO;
import it.csi.risca.riscabesrv.dto.TipoUsoExtendedDTO;
import it.csi.risca.riscabesrv.dto.TipoUsoRegolaDTO;
import it.csi.risca.riscabesrv.dto.UnitaMisuraDTO;
import it.csi.risca.riscabesrv.dto.ambiente.BollCanoneUsoDTO;
import it.csi.risca.riscabesrv.dto.ambiente.DatiTecniciAmbienteDTO;
import it.csi.risca.riscabesrv.dto.ambiente.TipoUsoDatoTecnicoDTO;
import it.csi.risca.riscabesrv.util.BollUtils;
import it.csi.risca.riscabesrv.util.Constants;

@Component
public class BollDatiWorkingApiServiceImpl extends BaseApiServiceImpl implements BollDatiWorkingApi {

	private static final String IDENTITY = "identity";
	private static Logger LOGGER = Logger.getLogger(Constants.LOGGER_NAME);

	public static final String PASSO_GENERA_NAP = "GENERA_NAP";
	public static final String PASSO_INSERT_SD_IN_WORK = "INSERT_SD_IN_WORK";
	public static final String PASSO_INSERT_RATA_IN_WORK = "INSERT_RATA_IN_WORK";
	public static final String PASSO_INSERT_SOGG_IN_WORK = "INSERT_SOGG_IN_WORK";
	public static final String PASSO_INSERT_DTAMM_IN_WORK = "INSERT_DTAMM_IN_WORK";
	public static final String PASSO_CONTA_ANNUALITA_INT = "CONTA_ANNUALITA_INT";
	public static final String PASSO_INSERT_AVVISO_ANN = "INSERT_AVVISO_ANN";
	public static final String PASSO_LEGGO_USI_ANN = "LEGGO_USI_ANN";
	public static final String PASSO_INSERT_AVVISO_USO = "INSERT_AVVISO_USO";
	public static final String PASSO_CALCOLO_CANONE_FRAZ = "CALCOLO_CANONE_FRAZ";
	public static final String PASSO_INS_ANN_SD_IN_WORK = "INS_ANN_SD_IN_WORK";
	public static final String PASSO_UPD_NOTA_SD_IN_W = "UPD_NOTA_SD_IN_W";
	public static final String PASSO_INS_USO_SD_IN_WORK = "INS_USO_SD_IN_WORK";
	public static final String PASSO_INS_USO_SD_RA_IN_WRK = "INS_USO_SD_RA_IN_WRK";
	public static final String PASSO_UPD_WORK_DT_AMMIN = "UPD_WORK_DT_AMMIN";
	public static final String PASSO_UPDATE_WORK_AVV_PAG = "UPDATE_WORK_AVV_PAG";
	public static final String PASSO_UPD_WORK_DATI_TIT = "UPD_WORK_DATI_TIT";
	public static final String PASSO_UPD_WORK_ANN_SD = "UPD_WORK_ANN_SD";

	public static final String PASSO_ESTRAE_PERC_QUO_VAR = "ESTRAE_PERC_QUO_VAR";
	public static final String PASSO_ESTRAE_PRZ_MED_POND = "ESTRAE_PRZ_MED_POND";

	public static final String PASSO_DEL_WORK_DATI_TIT = "DEL_WORK_DATI_TIT";
	public static final String PASSO_DEL_WORK_DATI_PAG = "DEL_WORK_DATI_PAG";

	public static final String PASSO_INSERT_SPED_IN_EFF = "INSERT_SPED_IN_EFF";
	public static final String PASSO_SET_ATTIV_STCON_SD = "SET_ATTIV_STCON_SD";
	public static final String PASSO_SET_TIPO_IMP_RIMB = "SET_TIPO_IMP_RIMB";
	public static final String PASSO_CONSOLIDA_DATI = "CONSOLIDA_DATI";
	public static final String PASSO_FINE_CICLO_AVVPAG = "FINE_CICLO_AVVPAG";
	public static final String PASSO_SVUOTA_WORKING = "SVUOTA_WORKING";
	public static final String PASSO_SET_IUV = "SET_IUV";

	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final String DATE_FORMAT_SPED = "dd/MM/yyyy";

	public static final String COD_TIPO_USO_GRANDE_IDROELETTRICO = "GRANDE_IDROELETTRICO";
	public static final String COD_TIPO_USO_GRANDE_IDROELETTRICO_AGG = "GRANDE_IDRO_AGG";

	public static final String SIGLA_RID_AUM_CERT_EMAS_ISO = "R5";

	public static final String FASE_EMISSIONE = "Emissione";
	public static final String FASE_CONFERMA_CONCLUSIONE = "Conferma-ConclusioneConferma";
	public static final String FASE_ANNULLA_EMISSIONE = "Annulla";

	public static final String COD_FASE_EMISBS = "EMISBS";
	public static final String COD_FASE_EMISBO = "EMISBO";
	public static final String COD_FASE_EMISBG = "EMISBG";

	public static final String TIPO_ELABORA_BS = "BS";
	public static final String TIPO_ELABORA_BO = "BO";
	public static final String TIPO_ELABORA_BG = "BG";

	public static final String COD_FASE_CONFBS_CONCLUSIONE = "CONFBS_CONCLUSIONE";
	public static final String COD_FASE_CONFBO_CONCLUSIONE = "CONFBO_CONCLUSIONE";
	public static final String COD_FASE_CONFBG_CONCLUSIONE = "CONFBG_CONCLUSIONE";

	public static final String COD_FASE_ANNULLABO = "ANNULLABO";

	@Autowired
	private AvvisoPagamentoDAO avvisoPagamentoDAO;
	@Autowired
	private StatoDebitorioDAO statoDebitorioDAO;
	@Autowired
	private RataSdDAO rataSdDAO;
	@Autowired
	private RegistroElaboraDAO registroElaboraDAO;
	@Autowired
	private AvvisoDatiTitolareDAO avvisoDatiTitolareDAO;
	@Autowired
	private AvvisoDatiAmminDAO avvisoDatiAmminDAO;
	@Autowired
	private AnnualitaSdDAO annualitaSdDAO;
	@Autowired
	private AvvisoAnnualitaDAO avvisoAnnualitaDAO;
	@Autowired
	private AnnualitaUsoSdDAO annualitaUsoSdDAO;
	@Autowired
	private AvvisoUsoDAO avvisoUsoDAO;
	@Autowired
	private TipoDilazioneDAO tipoDilazioneDAO;
	@Autowired
	private RiduzioneAumentoDAO riduzioneAumentoDAO;
	@Autowired
	private ComponentiDtDAO componentiDtDAO;
	@Autowired
	private TipoUsoRegolaDAO tipoUsoRegolaDAO;
	@Autowired
	private TipoUsoDAO tipoUsoDAO;
	@Autowired
	private AnnualitaUsoSdRaDAO annualitaUsoSdRaDAO;
	@Autowired
	private UnitaMisuraDAO unitaMisuraDAO;
	@Autowired
	private SpedizioneDAO spedizioneDAO;
	@Autowired
	private RimborsoDAO rimborsoDAO;
	@Autowired
	private RimborsoSdUtilizzatoDAO rimborsoSdUtilizzatoDAO;
	@Autowired
	private IuvDAO iuvDAO;

	private AvvisoDatiAmminDTO w_avvisoDatiAmmin;

	private String attore;

	/**
	 * @param riscossioneBsData riscossioneBsData
	 * @param securityContext   SecurityContext
	 * @param httpHeaders       HttpHeaders
	 * @param httpRequest       HttpServletRequest
	 * @return Response
	 */
	@Override
	public Response saveBsDatiWorking(RiscossioneBollData riscossioneBsData, Boolean insertAvvisoPag,
			Boolean insertTitolare, int progrPerTitolare, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		LOGGER.debug("[BollDatiWorkingApiServiceImpl::saveBsDatiWorking] BEGIN");
		BollResultDTO bsResult = new BollResultDTO();
		attore = "riscabatchspec";
		String nap = null;
		String tipoElabora = riscossioneBsData.getTipoElabora();
		String codFase = tipoElabora.equals(TIPO_ELABORA_BS) ? COD_FASE_EMISBS : COD_FASE_EMISBG;

		if (insertAvvisoPag) {
			nap = insertAvvisoPagamento(riscossioneBsData, "Step 3", codFase);
		} else {
			// Il NAP coincide con quello del giro precedente e si prende sul BsData
			nap = riscossioneBsData.getNap().getNap();
		}
		if (!insertAvvisoPag || nap != null) {
			Long idStatoDebitorio = insertStatoDebitorio(riscossioneBsData, codFase);
			if (idStatoDebitorio != null) {
				Long idRataSd = insertRataSd(riscossioneBsData, false, null, "Step 3", codFase);
				if (idRataSd != null) {
					String protSped = null;
					if (insertTitolare) {
						protSped = insertAvvisoDatiTitolare(riscossioneBsData, progrPerTitolare, "Step 3", codFase);
					}
					if (!insertTitolare || protSped != null) {
						String result = insertAvvisoDatiAmmin(riscossioneBsData, false, "Step 3", codFase);
						if (result != null) {

							List<AnnualitaSdDTO> annualitaIntere = null;
							try {
								annualitaIntere = annualitaSdDAO.loadAnnualitaSd(idStatoDebitorio, false, true);
							} catch (SQLException e) {
								LOGGER.error("[BollDatiWorkingApiServiceImpl::loadAnnualitaSd] exception: "
										+ e.getMessage());
							}
							int contaAnnualita = annualitaIntere.size();
							logStep(riscossioneBsData.getIdElabora(), PASSO_CONTA_ANNUALITA_INT, 0,
									FASE_EMISSIONE + " - Step 3 - Numero annualita intere lette: " + contaAnnualita
											+ " per lo stato debitorio " + idStatoDebitorio,
									attore, codFase);

							List<AnnualitaSdDTO> listAnnualita = null;
							try {
								listAnnualita = annualitaSdDAO.loadAnnualitaSd(idStatoDebitorio, false, false);
							} catch (SQLException e) {
								LOGGER.error("[BollDatiWorkingApiServiceImpl::loadAnnualitaSd] exception: "
										+ e.getMessage());
							}
							for (AnnualitaSdDTO annualita : listAnnualita) {
								String retAnnualita = insertAvvisoAnnualita(annualita, riscossioneBsData, tipoElabora,
										"Step 3");
								if (retAnnualita != null) {
									List<AnnualitaUsoSdExtendedDTO> listUsi = annualitaUsoSdDAO
											.loadAnnualitaUsiPrincipaliSd(annualita.getIdAnnualitaSd());
									boolean errorUsi = false;
									if (listUsi == null || listUsi.size() == 0) {
										logStep(riscossioneBsData.getIdElabora(), PASSO_LEGGO_USI_ANN, 1, FASE_EMISSIONE
												+ " - Step 3 - Non esistono usi principali legati all'annualita: NAP "
												+ annualita.getIdAnnualitaSd(), attore, codFase);
										bsResult.setStatus("KO");
										errorUsi = true;
									}
									for (AnnualitaUsoSdExtendedDTO uso : listUsi) {
										String annoCalc = uso.getDataFineCalcolata().substring(0, 4);
										if (uso.getIdAnnualitaSd() == null || uso.getUsoTAnno() == null
												|| !uso.getAnno().equals(annoCalc)) {
											// non esistono usi principali per questa annualita
											logStep(riscossioneBsData.getIdElabora(), PASSO_LEGGO_USI_ANN, 1,
													FASE_EMISSIONE
															+ " - Step 3 - Non esistono usi principali legati all'annualita: NAP "
															+ annualita.getIdAnnualitaSd(),
													attore, codFase);
											bsResult.setStatus("KO");
											errorUsi = true;
											break;
										}

										try {
											// Modifica del 24/08/2023
											// BigDecimal canoneUnitario = getCanoneUnitario(uso.getJsonRegola());
											BigDecimal canoneUnitario = uso.getCanoneUnitario();
											// TODO vale per ambito ambiente perche' esamina i dati tecnici
											BigDecimal portMediaConc = BollUtils.estraiQuantitaDaUsi(
													annualita.getJsonDtAnnualitaSd(), uso.getIdTipoUso());
											String ridAumConcat = getRiduzioniAumenti(uso.getIdAnnualitaUsoSd());

											// Estrarre i dati tecnici del Grande Idroelettrico
											Integer percQuotaVar = null;
											BigDecimal prezzoMedOraPond = null;
											if (tipoElabora.equals(TIPO_ELABORA_BG)) {
												Integer idAnnualitaSd = Math.toIntExact(annualita.getIdAnnualitaSd());
												AnnualitaSdDTO annSdGI = annualitaSdDAO
														.loadAnnualitaSdById(idAnnualitaSd);
												if (annSdGI != null) {
													String jsonDt = annSdGI.getJsonDtAnnualitaSd();
													percQuotaVar = BollUtils.estraiPercQuotaVar(jsonDt);
													logStep(riscossioneBsData.getIdElabora(), PASSO_ESTRAE_PERC_QUO_VAR,
															0,
															FASE_EMISSIONE
																	+ " - Step 3 - Estrazione Percentuale Quota Variabile OK - IdAnnualita: "
																	+ annualita.getIdAnnualitaSd()
																	+ " - Percentuale Quota Variabile: " + percQuotaVar,
															attore, codFase);
													prezzoMedOraPond = BollUtils.estraiPrezzoMedOraPond(jsonDt);
													logStep(riscossioneBsData.getIdElabora(), PASSO_ESTRAE_PRZ_MED_POND,
															0,
															FASE_EMISSIONE
																	+ " - Step 3 - Estrazione Prezzo Medio Ora Ponderato OK - IdAnnualita: "
																	+ annualita.getIdAnnualitaSd()
																	+ " - Percentuale Quota Variabile: "
																	+ prezzoMedOraPond,
															attore, codFase);
												}
											}

											String ret = insertAvvisoUsoBs(riscossioneBsData, annualita, uso,
													ridAumConcat, portMediaConc, canoneUnitario, codFase, percQuotaVar,
													prezzoMedOraPond);

											if (ret == null) {
												bsResult.setStatus("KO");
												bsResult.setStepError(PASSO_INSERT_AVVISO_USO);
												errorUsi = true;
												break;
											}
										} catch (BusinessException e) {
											LOGGER.error("[BollDatiWorkingApiServiceImpl::saveBsDatiWorking] ERROR "
													+ e.getMessageCode());
											bsResult.setStatus("KO");
											bsResult.setStepError(PASSO_INSERT_AVVISO_USO);
											bsResult.setErrorMessage(
													"Error parsing JsonDtAnnualitaSd for IdAnnualitaSd: "
															+ annualita.getIdAnnualitaSd());
											errorUsi = true;
											break;
										}
									}
									if (errorUsi) {
										break;
									} else {
										try {
											aggiornaTabelleWorking(riscossioneBsData, nap, contaAnnualita, annualita,
													codFase);
										} catch (Exception e) {
											LOGGER.error(
													"[BollDatiWorkingApiServiceImpl::InserimentoDatiWorkingApiServiceImpl] Error Updating working tables "
															+ e);
											bsResult.setStepError(PASSO_CALCOLO_CANONE_FRAZ);
											bsResult.setStatus("KO");
											break;
										}

										// Tutti gli inserimenti e aggiornamento delle
										// tabelle di Working sono OK
										bsResult.setStatus("OK");
									}
								} else {
									bsResult.setStatus("KO");
									bsResult.setStepError(PASSO_INSERT_AVVISO_ANN);
									break;
								}
							}
						} else {
							bsResult.setStatus("KO");
							bsResult.setStepError(PASSO_INSERT_DTAMM_IN_WORK);
						}
					} else {
						bsResult.setStatus("KO");
						bsResult.setStepError(PASSO_INSERT_SOGG_IN_WORK);
					}
				} else {
					bsResult.setStatus("KO");
					bsResult.setStepError(PASSO_INSERT_RATA_IN_WORK);
				}
			} else {
				bsResult.setStatus("KO");
				bsResult.setStepError(PASSO_INSERT_SD_IN_WORK);
			}
		} else {
			bsResult.setStatus("KO");
			bsResult.setStepError(PASSO_GENERA_NAP);
		}

		LOGGER.debug("[BollDatiWorkingApiServiceImpl::saveBsDatiWorking] END");
		return Response.ok(bsResult).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	/**
	 * @param RiscossioneBollData riscossioneBsData
	 * @param securityContext     SecurityContext
	 * @param httpHeaders         HttpHeaders
	 * @param httpRequest         HttpServletRequest
	 * @return Response
	 */
	@Override
	public Response saveBoDatiWorkingAvvisoPagTitolare(RiscossioneBollData riscossioneBollData, int progrPerTitolare,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[BollDatiWorkingApiServiceImpl::saveBoDatiWorkingTitolareAvvisoPag] BEGIN");
		attore = "riscabatchord";
		BollResultDTO bsResult = new BollResultDTO();

		String nap = insertAvvisoPagamento(riscossioneBollData, "Step 3", COD_FASE_EMISBO);
		if (nap != null) {
			String protSped = insertAvvisoDatiTitolare(riscossioneBollData, progrPerTitolare, "Step 3",
					COD_FASE_EMISBO);
			if (protSped != null) {
				bsResult.setStatus("OK");
			} else {
				bsResult.setStatus("KO");
				bsResult.setStepError(PASSO_INSERT_SOGG_IN_WORK);
			}
		} else {
			bsResult.setStatus("KO");
			bsResult.setStepError(PASSO_GENERA_NAP);
		}

		LOGGER.debug("[BollDatiWorkingApiServiceImpl::saveBoDatiWorkingTitolareAvvisoPag] END");
		return Response.ok(bsResult).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();

	}

	/**
	 * @param riscossioneBollData
	 * @param progrPerTitolare
	 * @param securityContext
	 * @param httpHeaders
	 * @param httpRequest
	 * @return
	 */
	@Override
	public Response saveBoDatiWorkingRataAnnualitaSdDatiAmmin(RiscossioneBollData riscossioneBollData,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[BollDatiWorkingApiServiceImpl::saveBoDatiWorkingRataAnnualitaUsiDatiAmmin] BEGIN");
		attore = "riscabatchord";
		BollResultWorkDTO bsResult = new BollResultWorkDTO();
		boolean error = false;
		String tipoElabora = TIPO_ELABORA_BO;

		// Se per la riscossione corrente l' ISTANZA piu' recente e' un'istanza di
		// Rinnovo modificare la nota_rinnovo nella tabella RISCA_W_ STATO_DEBITORIO
		if (riscossioneBollData.getIstanza() != null
				&& riscossioneBollData.getIstanza().getDataProvvedimento() != null) {
			// TODO Verificare il valore del campo notaRinnovo
			String notaRinnovo = "Istanza di Rinnovo";
			try {
				statoDebitorioDAO.updateNotaRinnovoStatoDebitorioWorking(
						riscossioneBollData.getRiscossioneBoll().getIdStatoDebitorio(), notaRinnovo);
				logStep(riscossioneBollData.getIdElabora(), PASSO_UPD_NOTA_SD_IN_W, 0,
						FASE_EMISSIONE + " - Step 5 - Modifica Nota Rinnovo in working RISCA_W_STATO_DEBITORIO", attore,
						COD_FASE_EMISBO);
			} catch (DAOException e) {
				error = true;
				bsResult.setStatus("KO");
				bsResult.setStepError(PASSO_UPD_NOTA_SD_IN_W);
			}
		}

		if (!error) {

			// Inserire i dati della RATA del nuovo SD nella tavola di working

			// NOTA: troncare i decimali solo per la parte di Canone (vecchia e nuova) e NON
			// per la somma con le compensazioni (il regolamento dice il Canone deve essere
			// troncato perdendo i decimali) se dopo la sottrazione della Compensa-zione ne
			// derivassero dei decimali, questi NON vanno troncati
			double canonePratica = riscossioneBollData.getCanonePratica().setScale(0, RoundingMode.DOWN).doubleValue();
			double canoneDovutoAnnoPrec = riscossioneBollData.getCanoneDovutoAnnoPrec().setScale(0, RoundingMode.DOWN)
					.doubleValue();
			double sommCompensPerPrat = riscossioneBollData.getSommCompensPerPrat().doubleValue();

			double canoneDovuto = canonePratica + canoneDovutoAnnoPrec - sommCompensPerPrat;
			Long idRataSd = insertRataSd(riscossioneBollData, true, BigDecimal.valueOf(canoneDovuto), "Step 5",
					COD_FASE_EMISBO);
			if (idRataSd != null) {
				AnnualitaSdDTO annualitaSd = insertAnnualitaSd(riscossioneBollData);
				if (annualitaSd != null && annualitaSd.getIdAnnualitaSd() != null) {
					String result = insertAvvisoDatiAmmin(riscossioneBollData, true, "Step 5", COD_FASE_EMISBO);
					if (result != null) {
						String retAnnualita = insertAvvisoAnnualita(null, riscossioneBollData, tipoElabora, "Step 5");
						if (retAnnualita != null) {
							// Il servizio termina con successo
							bsResult.setStatus("OK");
							bsResult.setAnnualitaSd(annualitaSd);
						} else {
							bsResult.setStatus("KO");
							bsResult.setStepError(PASSO_INSERT_AVVISO_ANN);
						}
					} else {
						bsResult.setStatus("KO");
						bsResult.setStepError(PASSO_INSERT_DTAMM_IN_WORK);
					}
				} else {
					bsResult.setStatus("KO");
					bsResult.setStepError(PASSO_INS_ANN_SD_IN_WORK);
				}
			} else {
				bsResult.setStatus("KO");
				bsResult.setStepError(PASSO_INSERT_RATA_IN_WORK);
			}
		}

		LOGGER.debug("[BollDatiWorkingApiServiceImpl::saveBoDatiWorkingRataAnnualitaUsiDatiAmmin] END");
		return Response.ok(bsResult).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response saveBoDatiWorkingUsiRidAum(RiscossioneBollData riscossioneBollData, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[BollDatiWorkingApiServiceImpl::saveBoDatiWorkingUsiRidAum] BEGIN");
		attore = "riscabatchord";
		BollResultDTO bsResult = new BollResultDTO();
		boolean errorUsi = false;
		for (BollCanoneUsoDTO riscossioneUso : riscossioneBollData.getRiscossioneUsi()) {

			if (riscossioneUso.getCanoneUso().getCodTipoUso().equals(COD_TIPO_USO_GRANDE_IDROELETTRICO_AGG)) {
				BigDecimal canoneG = calcolaCanoneGrandeIdroelettricoAgg(riscossioneUso);
				riscossioneUso.setCanonePerUso(canoneG);
				riscossioneUso.setCanonePerUsoNonFraz(canoneG);

				// Correggo TipoUsoLegge che altrimenti rimane GRANDE_IDROELETTRICO
				TipoUsoExtendedDTO tipoUso = null;
				try {
					tipoUso = tipoUsoDAO.loadTipoUsoByIdTipoUsoOrCodTipoUso(COD_TIPO_USO_GRANDE_IDROELETTRICO_AGG);
				} catch (SQLException e) {
					LOGGER.error("[BollDatiWorkingApiServiceImpl::loadTipoUsoByIdTipoUsoOrCodTipoUso] exception: "
							+ e.getMessage());
				}
				riscossioneUso.getTipoUsoDatoTecnico().setTipoUsoLegge(tipoUso.getDesTipouso());

				// Occorre aggiornare il campo json_dt_riscossione dentro la tabella
				// risca_w_annualita_sd
				try {
					updateJsonDtAnnualitaSd(riscossioneUso, tipoUso);
				} catch (Exception e) {
					bsResult.setStatus("KO");
					errorUsi = true;
					break;
				}
			}

			Long idAnnualitaUsoSd = insertAnnualitaUsoSd(riscossioneBollData.getIdElabora(), riscossioneUso);
			Long idRiscossione = riscossioneBollData.getRiscossioneBoll().getIdRiscossione();
			if (idAnnualitaUsoSd != null) {
				// Leggere gli aumenti e le riduzioni dell'uso creandosi un array rec_rid_aum
				String ridAumConcat = "";
				List<RiduzioneAumentoDTO> ridAum = null;
				try {
					ridAum = riduzioneAumentoDAO.loadRiduzioneAumentoByRiscossioneTipoUso(idRiscossione,
							riscossioneUso.getCanoneUso().getCodTipoUso());
				} catch (SQLException e) {
					LOGGER.error("[BollDatiWorkingApiServiceImpl::loadRiduzioneAumentoByRiscossioneTipoUso] exception: "
							+ e.getMessage());
				}
				for (RiduzioneAumentoDTO ra : ridAum) {
					int annoEmasIso = riscossioneBollData.getDataScadenzaEmasIso() != null
							&& riscossioneBollData.getDataScadenzaEmasIso().length() > 4
									? Integer.valueOf(riscossioneBollData.getDataScadenzaEmasIso().substring(0, 4))
									: 0;
					int annoParam = Integer.valueOf(riscossioneBollData.getAnno());
					if (annoEmasIso < annoParam && ra.getSiglaRiduzioneAumento().equals(SIGLA_RID_AUM_CERT_EMAS_ISO)) {
						// passa al record successivo
						continue;
					}
					ridAumConcat = ridAumConcat + ra.getSiglaRiduzioneAumento() + ",";

					Long idAnnualitaUsoSdRa = insertAnnualitaUsoSdRa(riscossioneBollData.getIdElabora(),
							idAnnualitaUsoSd, ra);
					if (idAnnualitaUsoSdRa == null) {
						bsResult.setStatus("KO");
						bsResult.setStepError(PASSO_INS_USO_SD_RA_IN_WRK);
						errorUsi = true;
						break;
					}
				}
				ridAumConcat = StringUtils.chop(ridAumConcat);
				if (!errorUsi) {
					// recuperare sigla_unita_misura
					UnitaMisuraDTO um = null;
					try {
						um = unitaMisuraDAO.loadUnitaMisuraByIdUnitaMisura(
								riscossioneUso.getTipoUsoDatoTecnico().getUnitaDiMisura());
					} catch (SQLException e) {
						LOGGER.error("[BollDatiWorkingApiServiceImpl::loadUnitaMisuraByIdUnitaMisura] exception: "
								+ e.getMessage());
					}
					// Inserire dati nella working RISCA_W_AVVISO_USO
					String ret = insertAvvisoUsoBo(riscossioneBollData, riscossioneUso.getAnnualitaSd(), riscossioneUso,
							ridAumConcat, um.getDesUnitaMisura());
					if (ret != null) {
						Integer result = aggiornaPeriodoContribuzioneDatiAmmin(riscossioneBollData);
						if (result == null) {
							bsResult.setStatus("KO");
							bsResult.setStepError(PASSO_UPD_WORK_DT_AMMIN);
							errorUsi = true;
							break;
						}
					} else {
						bsResult.setStatus("KO");
						bsResult.setStepError(PASSO_INSERT_AVVISO_USO);
						errorUsi = true;
						break;
					}
				}
			} else {
				bsResult.setStatus("KO");
				bsResult.setStepError(PASSO_INS_USO_SD_IN_WORK);
				errorUsi = true;
				break;
			}
		}

		if (!errorUsi) {
			if (aggiornaAvvisoPagamento(riscossioneBollData)) {
				if (aggiornaDatiTitolare(riscossioneBollData)) {
					if (updateNumMesiAnnualitaSd(riscossioneBollData)) {
						// Fine del passo 5 con successo
						bsResult.setStatus("OK");
					} else {
						bsResult.setStatus("KO");
						bsResult.setStepError(PASSO_UPD_WORK_ANN_SD);
					}
				} else {
					bsResult.setStatus("KO");
					bsResult.setStepError(PASSO_UPD_WORK_DATI_TIT);
				}
			} else {
				bsResult.setStatus("KO");
				bsResult.setStepError(PASSO_UPDATE_WORK_AVV_PAG);
			}
		}

		LOGGER.debug("[BollDatiWorkingApiServiceImpl::saveBoDatiWorkingUsiRidAum] END");
		return Response.ok(bsResult).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();

	}

	private void updateJsonDtAnnualitaSd(BollCanoneUsoDTO riscossioneUso, TipoUsoExtendedDTO tipoUso) throws Exception {
		AnnualitaSdDTO annualitaSd = riscossioneUso.getAnnualitaSd();
		DatiTecniciAmbienteDTO dt = BollUtils.getDatiTecniciFromJsonDtUsi(annualitaSd.getJsonDtAnnualitaSd());
		TipoUsoDatoTecnicoDTO tipoUsoGI = dt.getUsi().get(COD_TIPO_USO_GRANDE_IDROELETTRICO);

		ObjectMapper objectMapper = new ObjectMapper();
		TipoUsoDatoTecnicoDTO tipoUsoGI_AGG = objectMapper.readValue(objectMapper.writeValueAsString(tipoUsoGI),
				TipoUsoDatoTecnicoDTO.class);

		tipoUsoGI_AGG.setTipoUsoLegge(tipoUso.getDesTipouso());
		tipoUsoGI_AGG.setIdTipoUsoLegge(tipoUso.getIdTipoUso());

		// Aggiungo un uso per il Grande idro aggiuntivo
		dt.getUsi().put(tipoUso.getCodTipouso(), tipoUsoGI_AGG);
		String newJsonDtAnnualitaSd = dt.toJsonString();

		// Faccio update dell'annualitaSd
		annualitaSd.setJsonDtAnnualitaSd(newJsonDtAnnualitaSd);
		annualitaSdDAO.updateAnnualitaSdWorking(annualitaSd);
		riscossioneUso.setAnnualitaSd(annualitaSd);
	}

	private boolean updateNumMesiAnnualitaSd(RiscossioneBollData riscossioneBollData) {
		boolean updateOk = true;
		try {
			// AnnualitaSdDTO annualitaSd = riscossioneBollData.getRiscossioneUsi().get(0).getAnnualitaSd();
			// Devo riprendere il record annualitaSd dal DB perchè c'è stato un
			// aggiornamento del json_dt e se prendo da riscossioneBollData non e' aggiornato
			// Questo corregge il TASK 55 - BO - Json_dt dell'annualità non mette AGGIUNTIVO in caso di Grande Idroelettrico
			AnnualitaSdDTO annualitaSd = annualitaSdDAO
					.loadAnnualitaSdWorking(riscossioneBollData.getRiscossioneBoll().getIdStatoDebitorio()).get(0);

			int numMesi = riscossioneBollData.getRiscossioneUsi().get(0).getNumMesi();
			// Faccio update di numMesi
			annualitaSd.setNumeroMesi(numMesi);
			annualitaSdDAO.updateAnnualitaSdWorking(annualitaSd);
		} catch (Exception e) {
			LOGGER.error(
					"[BollDatiWorkingApiServiceImpl::updateNumMesiAnnualitaSd] Aggiornamento num_mesi su RISCA_W_ANNUALITA_SD fallito per nap: "
							+ riscossioneBollData.getNap().getNap());
			updateOk = false;
		}
		return updateOk;
	}

	@Override
	public Response updateBoDatiWorkingAvvisoPagTitolare(Long idElabora, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[BollDatiWorkingApiServiceImpl::updateBoDatiWorkingAvvisoPagTitolare] BEGIN");
		attore = "riscabatchord";
		BollResultDTO bsResult = new BollResultDTO();
		if (deleteDatiTitolareSenzaRiscDaBollettare(idElabora)) {
			if (deleteAvvisoPagamentoSenzaRiscDaBollettare(idElabora)) {
				if (aggiornaNumeroUtenzeTitolare(idElabora)) {
					// Fine del passo 7 con successo
					bsResult.setStatus("OK");
				} else {
					bsResult.setStatus("KO");
					bsResult.setStepError(PASSO_UPD_WORK_DATI_TIT);
				}
			} else {
				bsResult.setStatus("KO");
				bsResult.setStepError(PASSO_DEL_WORK_DATI_PAG);
			}
		} else {
			bsResult.setStatus("KO");
			bsResult.setStepError(PASSO_DEL_WORK_DATI_TIT);
		}

		LOGGER.debug("[BollDatiWorkingApiServiceImpl::updateBoDatiWorkingAvvisoPagTitolare] END");
		return Response.ok(bsResult).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	private boolean aggiornaDatiTitolare(RiscossioneBollData riscossioneBollData) {
		String nap = riscossioneBollData.getNap().getNap();
		boolean updateOk = true;
		try {
			double canonePratica = riscossioneBollData.getCanonePratica().setScale(0, RoundingMode.DOWN).doubleValue();
			double canoneDovutoAnnoPrec = riscossioneBollData.getCanoneDovutoAnnoPrec().setScale(0, RoundingMode.DOWN)
					.doubleValue();
			double canoneCalcolato = riscossioneBollData.getCanoneCalcolato() == null ? 0.0
					: riscossioneBollData.getCanoneCalcolato().setScale(0, RoundingMode.DOWN).doubleValue();
			double sommCompensPerPrat = riscossioneBollData.getSommCompensPerPrat().doubleValue();

			double importoPerTitolare = 0.0;
			String annoScadPag = riscossioneBollData.getDataScadenzaPagamento().substring(0, 4);
			String annoScadConcessione = riscossioneBollData.getRiscossioneBoll().getDataScadConcessione() != null
					? getYearFromDate(riscossioneBollData.getRiscossioneBoll().getDataScadConcessione())
					: null;
			if (annoScadConcessione != null && annoScadConcessione.equals(annoScadPag)) {
				importoPerTitolare = canonePratica + canoneDovutoAnnoPrec - sommCompensPerPrat;
			} else {
				importoPerTitolare = canoneCalcolato + canoneDovutoAnnoPrec - sommCompensPerPrat;
			}

			// aggiornare la tabella RISCA_W_AVVISO_DATI_TITOLARE con importo da versare
			updateAvvisoDatiTitolareImporto(riscossioneBollData.getAnno(), riscossioneBollData.getDataProtocollo(), nap,
					BigDecimal.valueOf(importoPerTitolare), true);

			logStep(riscossioneBollData.getIdElabora(), PASSO_UPD_WORK_DATI_TIT, 0, FASE_EMISSIONE
					+ " - Step 5 - Modifica Importo da Versare in working RISCA_W_ AVVISO_DATI_TITOLARE - Importo: "
					+ importoPerTitolare, attore, COD_FASE_EMISBO);
		} catch (DAOException e) {
			LOGGER.error(
					"[BollDatiWorkingApiServiceImpl::aggiornaDatiTitolare] Aggiornamento importi su AVVISO_DATI_TITOLARE fallito per nap: "
							+ riscossioneBollData.getNap().getNap());
			updateOk = false;
		}
		return updateOk;
	}

	private boolean deleteDatiTitolareSenzaRiscDaBollettare(Long idElabora) {
		boolean deleteOk = true;
		try {
			avvisoDatiTitolareDAO.deleteWorkingDatiTitolareSenzaRiscDaBollettare();

//			logStep(idElabora, PASSO_DEL_WORK_DATI_TIT, 0,
//					FASE_EMISSIONE + " - Step 7 - Cancellazione Titolari senza Riscossioni da Bollettare", attore,
//					COD_FASE_EMISBO);
		} catch (DAOException e) {
			LOGGER.error(
					"[BollDatiWorkingApiServiceImpl::deleteDatiTitolareSenzaRiscDaBollettare] Cancellazione Titolari senza Riscossioni da Bollettare fallita");
			deleteOk = false;
		}
		return deleteOk;
	}

	private boolean deleteAvvisoPagamentoSenzaRiscDaBollettare(Long idElabora) {
		boolean deleteOk = true;
		try {
			avvisoPagamentoDAO.deleteWorkingAvvisoPagamentoSenzaRiscDaBollettare();

//			logStep(idElabora, PASSO_DEL_WORK_DATI_PAG, 0,
//					FASE_EMISSIONE + " - Step 7 - Cancellazione Avvisi Pagamento per Esenzione", attore,
//					COD_FASE_EMISBO);
		} catch (DAOException e) {
			LOGGER.error(
					"[BollDatiWorkingApiServiceImpl::deleteAvvisoPagamentoSenzaRiscDaBollettare] Cancellazione Titolari senza Riscossioni da Bollettare fallita");
			deleteOk = false;
		}
		return deleteOk;
	}

	private boolean aggiornaNumeroUtenzeTitolare(Long idElabora) {
		boolean deleteOk = true;
		try {
			avvisoDatiTitolareDAO.updateWorkingDatiTitolareNumUtenze();

//			logStep(idElabora, PASSO_UPD_WORK_DATI_TIT, 0,
//					FASE_EMISSIONE + " - Step 7 - Aggiornamento Dati Titolare Campo N. Utenze", attore,
//					COD_FASE_EMISBO);
		} catch (DAOException e) {
			LOGGER.error(
					"[BollDatiWorkingApiServiceImpl::aggiornaNumeroUtenzeTitolare] Cancellazione Titolari senza Riscossioni da Bollettare fallita");
			deleteOk = false;
		}
		return deleteOk;
	}

	private boolean aggiornaAvvisoPagamento(RiscossioneBollData riscossioneBollData) {
		String nap = riscossioneBollData.getNap().getNap();
		boolean updateOk = true;
		try {
			// aggiornare la tabella RISCA_W_AVVISO_PAGAMENTO con importo totale dovuto
			double canonePratica = riscossioneBollData.getCanonePratica().setScale(0, RoundingMode.DOWN).doubleValue();
			double canoneDovutoAnnoPrec = riscossioneBollData.getCanoneDovutoAnnoPrec().setScale(0, RoundingMode.DOWN)
					.doubleValue();
			double sommCompensPerPrat = riscossioneBollData.getSommCompensPerPrat().doubleValue();
			double impTotaleDovuto = canonePratica + canoneDovutoAnnoPrec - sommCompensPerPrat;
			updateAvvisoPagamento(riscossioneBollData.getIdSpedizione(), nap, BigDecimal.valueOf(impTotaleDovuto));

			logStep(riscossioneBollData.getIdElabora(), PASSO_UPDATE_WORK_AVV_PAG, 0, FASE_EMISSIONE
					+ " - Step 5 - Modifica Importo Totale Dovuto in working RISCA_W_ AVVISO_PAGAMENTO - Importo: "
					+ impTotaleDovuto, attore, COD_FASE_EMISBO);

		} catch (DAOException e) {
			LOGGER.error(
					"[BollDatiWorkingApiServiceImpl::aggiornaAvvisoPagDatiTitolare] Aggiornamento importi su AVVISO_PAGAMENTO fallito per nap: "
							+ riscossioneBollData.getNap().getNap());
			updateOk = false;
		}
		return updateOk;
	}

	private Integer aggiornaPeriodoContribuzioneDatiAmmin(RiscossioneBollData riscossioneBollData) {
		Integer ret = null;
		try {
			ret = avvisoDatiAmminDAO.updateWorkingAvvisoDatiAmminPeriodoContrib("" + riscossioneBollData.getAnno(),
					riscossioneBollData.getNap().getNap(),
					riscossioneBollData.getRiscossioneBoll().getCodRiscossione());
			logStep(riscossioneBollData.getIdElabora(), PASSO_UPD_WORK_DT_AMMIN, 0, FASE_EMISSIONE
					+ " - Step 5 - Aggiornamento Periodo di Contribuzione in working RISCA_W_AVVISO_DATI_AMMIN - NAP "
					+ riscossioneBollData.getNap().getNap(), attore, COD_FASE_EMISBO);
		} catch (DAOException e) {
			LOGGER.error(
					"[BollDatiWorkingApiServiceImpl::aggiornaDatiAmmin] Aggiornamento Periodo di Contribuzione in working RISCA_W_AVVISO_DATI_AMMIN fallito per nap: "
							+ riscossioneBollData.getNap().getNap());
		}
		return ret;
	}

	private Long insertAnnualitaUsoSd(Long idElabora, BollCanoneUsoDTO riscossioneUso) {
		TipoUsoExtendedDTO tipoUso = null;
		try {
			tipoUso = tipoUsoDAO.loadTipoUsoByIdTipoUsoOrCodTipoUso(riscossioneUso.getCanoneUso().getCodTipoUso());
		} catch (SQLException e) {
			LOGGER.error(
					"[BollDatiWorkingApiServiceImpl::loadTipoUsoByIdTipoUsoOrCodTipoUso] Exception: " + e.getMessage());
		}
		// Inserire i dati nella tabella di working W_ANNUALITA_USO_SD
		AnnualitaUsoSdDTO annualitaUsoSdDto = new AnnualitaUsoSdDTO();
		annualitaUsoSdDto.setIdAnnualitaSd(riscossioneUso.getAnnualitaSd().getIdAnnualitaSd());
		annualitaUsoSdDto.setTipoUso(tipoUso);
		BigDecimal canoneUso = riscossioneUso.getCanonePerUso().compareTo(BigDecimal.ZERO) != 0
				? riscossioneUso.getCanonePerUso()
				: riscossioneUso.getCanonePerUsoNonFraz();
		annualitaUsoSdDto.setCanoneUso(canoneUso);
		annualitaUsoSdDto.setCanoneUnitario(riscossioneUso.getCanoneUso().getCanoneUnitario());

		try {
			annualitaUsoSdDto = annualitaUsoSdDAO.saveAnnualitaUsoSdWorking(annualitaUsoSdDto);
			logStep(idElabora, PASSO_INS_USO_SD_IN_WORK, 0,
					FASE_EMISSIONE + " - Step 5 - Inserisce Usi in working RISCA_W_ANNUALITA_USO_SD "
							+ annualitaUsoSdDto.getIdAnnualitaUsoSd() + " - USO " + tipoUso.getIdTipoUso()
							+ " - Canone " + canoneUso,
					attore, COD_FASE_EMISBO);
			return annualitaUsoSdDto.getIdAnnualitaUsoSd();
		} catch (DataAccessException | SQLException e) {
			LOGGER.error(
					"[BollDatiWorkingApiServiceImpl::insertAnnualitaUsoSd] Insert Usi in working RISCA_W_ANNUALITA_USO_SD fallito");
		}
		return null;
	}

	private Long insertAnnualitaUsoSdRa(Long idElabora, Long idAnnualitaUsoSd, RiduzioneAumentoDTO ra) {
		// Inserire i dati nella tabella di working W_ANNUALITA_USO_SD_RA
		AnnualitaUsoSdRaDTO annUsoSdRa = new AnnualitaUsoSdRaDTO();
		annUsoSdRa.setIdAnnualitaUsoSd(idAnnualitaUsoSd);
		annUsoSdRa.setIdRiduzioneAumento(ra.getIdRiduzioneAumento());
		try {
			annUsoSdRa = annualitaUsoSdRaDAO.saveAnnualitaUsoSdRaWorking(annUsoSdRa);
			logStep(idElabora, PASSO_INS_USO_SD_RA_IN_WRK, 0,
					FASE_EMISSIONE + " - Step 5 - Inserisce ridaum in working RISCA_W_ANNUALITA_USO_SD_RA - ID RA "
							+ annUsoSdRa.getIdAnnualitaUsoSdRa() + " - ID USO " + idAnnualitaUsoSd + " - RIDAUM "
							+ ra.getIdRiduzioneAumento(),
					attore, COD_FASE_EMISBO);
			return annUsoSdRa.getIdAnnualitaUsoSdRa();
		} catch (DataAccessException | SQLException e) {
			LOGGER.error(
					"[BollDatiWorkingApiServiceImpl::insertAnnualitaUsoSdRa] Insert Usi in working RISCA_W_ANNUALITA_USO_SD_RA fallito");
		}
		return null;
	}

	private BigDecimal calcolaCanoneGrandeIdroelettricoAgg(BollCanoneUsoDTO riscossioneUso) {
		String dataRiferimento = riscossioneUso.getCanoneUso().getAnnoUso() + "-01-01";
		TipoUsoRegolaDTO regola = tipoUsoRegolaDAO.loadTipoUsoRegolaByCodTipoUso(dataRiferimento,
				COD_TIPO_USO_GRANDE_IDROELETTRICO_AGG);
		if (regola != null) {
			String jsonRegola = regola.getJsonRegola();
			BigDecimal canoneUnitario = getCanoneUnitario(jsonRegola);
			BigDecimal quantita = riscossioneUso.getTipoUsoDatoTecnico().getQuantita();
			BigDecimal canone = canoneUnitario.multiply(quantita).setScale(2, RoundingMode.HALF_UP);
			riscossioneUso.getCanoneUso().setCanoneUnitario(canoneUnitario);
			riscossioneUso.getCanoneUso().setCanoneUso(canone);
			return canone;
		} else {
			LOGGER.error("[BollDatiWorkingApiServiceImpl::calcolaCanoneGrandeIdroelettricoAgg] jsonRegola non trovato");
		}
		return null;
	}

	private void aggiornaTabelleWorking(RiscossioneBollData riscossioneBsData, String nap, int contaAnnualita,
			AnnualitaSdDTO annualita, String codFase) throws Exception {
		BigDecimal sumCanoneUsoPcalc = avvisoUsoDAO.sumCanoneUsoPcalc(nap,
				riscossioneBsData.getRiscossioneBoll().getCodRiscossione(), annualita.getAnno());

		int mesi = (annualita.getNumeroMesi() == null || annualita.getNumeroMesi() == 0) ? 12
				: annualita.getNumeroMesi();

		BigDecimal frazionato = null;
		try {
			frazionato = sumCanoneUsoPcalc.divide(BigDecimal.valueOf(12), MathContext.DECIMAL64)
					.multiply(BigDecimal.valueOf(mesi), MathContext.DECIMAL64).setScale(2, RoundingMode.HALF_UP);
		} catch (Exception e) {
			logStep(riscossioneBsData.getIdElabora(), PASSO_CALCOLO_CANONE_FRAZ, 1,
					FASE_EMISSIONE + " - Step 3 - ERRORE CALCOLO del frazionato - Canone calcolato: "
							+ sumCanoneUsoPcalc + " - Canone Frazionato: " + frazionato + " - Annualita: "
							+ annualita.getIdAnnualitaSd() + " - Anno: " + annualita.getAnno(),
					attore, codFase);
			throw e;
		}

		logStep(riscossioneBsData.getIdElabora(), PASSO_CALCOLO_CANONE_FRAZ, 0,
				FASE_EMISSIONE + " - Step 3 - Canone calcolato: " + sumCanoneUsoPcalc + " - Canone Frazionato: "
						+ frazionato + " - Annualita: " + annualita.getIdAnnualitaSd() + " - Anno: "
						+ annualita.getAnno(),
				attore, codFase);

		// aggiornare la tabella RISCA_W_AVVISO_ANNUALITA con gli importi totale e
		// frazionato
		avvisoAnnualitaDAO.updateWorkingAvvisoAnnualitaTotaleCanoneAnnoCalc(sumCanoneUsoPcalc, frazionato, nap,
				riscossioneBsData.getRiscossioneBoll().getCodRiscossione(), annualita.getAnno());

		// aggiornare la tabella RISCA_W_AVVISO_PAGAMENTO con importo totale dovuto
		updateAvvisoPagamento(riscossioneBsData.getIdSpedizione(), nap, annualita.getCanoneAnnuo());

		// Se rec_riscossioni_attive(corrente).periodo_pag is NULL
		// aggiornare la tabella RISCA_W_AVVISO_DATI_AMMIN con periodo di contribuzione
		if (riscossioneBsData.getPeriodoPag() == null) {
			updateAvvisoDatiAmminPeriodoContrib(riscossioneBsData.getRiscossioneBoll().getCodRiscossione(), nap,
					annualita.getAnno());
		}

		// aggiornare la tabella RISCA_W_AVVISO_DATI_TITOLARE con importo da versare
		updateAvvisoDatiTitolareImporto(riscossioneBsData.getAnno(), riscossioneBsData.getDataProtocollo(), nap,
				annualita.getCanoneAnnuo(), false);

		// aggiornare la tabella RISCA_W_AVVISO_DATI_AMMIN con totale_utenza_calc
		updateAvvisoDatiAmminTotaleUtenzaCalc(riscossioneBsData.getRiscossioneBoll().getCodRiscossione(), nap,
				annualita.getCanoneAnnuo());

		TipoDilazioneDTO tipoDilazione = tipoDilazioneDAO.loadTipoDilazioneByDataScadenzaPagamento(
				riscossioneBsData.getDataScadenzaPagamento(), riscossioneBsData.getIdAmbito());

		int flagDilazione = 0;
		if (tipoDilazione != null) {
			double cDovuto = riscossioneBsData.getRataSd().getCanoneDovuto().doubleValue();
			double impMin = tipoDilazione.getImportoMin() != null ? tipoDilazione.getImportoMin().doubleValue() : 0.0;
			double impMax = tipoDilazione.getImportoMagg() != null ? tipoDilazione.getImportoMagg().doubleValue()
					: cDovuto;
			if (cDovuto >= impMin && cDovuto <= impMax && contaAnnualita > tipoDilazione.getNumAnnualitaMagg()) {
				flagDilazione = 1;
			}
		}

		updateAvvisoDatiTitolareDilazione(riscossioneBsData.getDataScadenzaPagamento(),
				tipoDilazione != null ? tipoDilazione.getNumMesi() : null, nap, flagDilazione);
	}

	private String getRiduzioniAumenti(Long idAnnualitaUsoSd) {
		String ridAumConcat = "";
		List<RiduzioneAumentoDTO> ridAumList = null;
		try {
			ridAumList = riduzioneAumentoDAO.loadRiduzioneAumentoByAnnualitaUsoSd(idAnnualitaUsoSd);
		} catch (SQLException e) {
			LOGGER.error("[BollDatiWorkingApiServiceImpl::getRiduzioniAumenti] exception: " + e.getMessage());
		}
		for (RiduzioneAumentoDTO riduzioneAumento : ridAumList) {
			ridAumConcat = ridAumConcat + riduzioneAumento.getSiglaRiduzioneAumento() + ",";
		}
		ridAumConcat = StringUtils.chop(ridAumConcat);
		return ridAumConcat;
	}

	private void updateAvvisoDatiTitolareImporto(Integer anno, String dataProtocollo, String nap,
			BigDecimal canoneAnnuo, boolean isBO) throws DAOException {
		// importo_da_versare = NVL(importo_da_versare, 0) +
		// NVL(rec_annualita(corrente).canone_annuo, 0)

		// BigDecimal importoDaVersare = w_avvisoDatiTitolare.getImportoDaVersare() ==
		// null ? new BigDecimal(0)
		// : w_avvisoDatiTitolare.getImportoDaVersare();
		BigDecimal cAnnuo = canoneAnnuo == null ? new BigDecimal(0) : canoneAnnuo;
		// BigDecimal impDaVersare = importoDaVersare.add(cAnnuo);
		int annualitaPagamento = 0;
		if (isBO) {
			annualitaPagamento = anno;
		} else {
			annualitaPagamento = Integer.parseInt(dataProtocollo.substring(0, 4));
		}
		avvisoDatiTitolareDAO.updateWorkingDatiTitolareImportoDaVersare(cAnnuo, annualitaPagamento, nap);
	}

	private void updateAvvisoDatiTitolareDilazione(String dataScadenzaPagamento, Integer numMesi, String nap,
			int flagDilazione) throws DAOException {
		// dilazione = DECODE(dilazione, '1', a.dilazione, NULL, v_flg_dilazione,
		// v_flg_dilazione)
		// scadenza_pagamento2 = DECODE(NVL(a.dilazione, '0'), '1',
		// TO_CHAR(ADD_MONTHS(TO_DATE(scadenza_pagamento, 'DD/MM/YYYY'),
		// NVL(rec_dilazione.num_mesi, 12)), 'DD/MM/YYYY'), scadenza_pagamento)
		int mesi = numMesi == null ? 12 : numMesi;

		String scadenzaPagamento2 = "";
		SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
		if (flagDilazione == 1) {
			Date dateScadPag;
			try {
				dateScadPag = df.parse(dataScadenzaPagamento);
				Calendar cal = new GregorianCalendar();
				cal.setTime(dateScadPag);
				cal.add(Calendar.MONTH, mesi);
				scadenzaPagamento2 = df.format(cal.getTime());
			} catch (ParseException e) {
				LOGGER.error("[BollDatiWorkingApiServiceImpl::updateAvvisoDatiTitolareDilazione] exception: "
						+ e.getMessage());
			}
		} else {
			scadenzaPagamento2 = dataScadenzaPagamento;
		}

		avvisoDatiTitolareDAO.updateWorkingDatiTitolareDilazioneScandenza("" + flagDilazione, scadenzaPagamento2, nap);
	}

	private void updateAvvisoDatiAmminPeriodoContrib(String codRiscossione, String nap, Integer annualitaAnno)
			throws DAOException {
		// periodo_di_contribuzione = DECODE(TRIM(NVL(periodo_di_contribuzione, 0)),
		// NULL, TO_CHAR(rec_annualita(corrente).anno), NVL(periodo_di_contribuzione, '
		// ') || '-' || TO_CHAR(rec_annualita(corrente).anno))
		String periodoContrib = w_avvisoDatiAmmin.getPeriodoDiContribuzione() == null ? "" + annualitaAnno
				: w_avvisoDatiAmmin.getPeriodoDiContribuzione() + "-" + annualitaAnno;
		avvisoDatiAmminDAO.updateWorkingAvvisoDatiAmminPeriodoContrib(periodoContrib, nap, codRiscossione);
	}

	private void updateAvvisoDatiAmminTotaleUtenzaCalc(String codRiscossione, String nap, BigDecimal canoneAnnuo)
			throws DAOException {
		// totale_utenza_calc = NVL(TO_NUMBER(totale_utenza_calc), 0) +
		// NVL(rec_annualita(corrente).canone_annuo, 0)

		BigDecimal cAnnuo = canoneAnnuo == null ? BigDecimal.ZERO : canoneAnnuo;
		avvisoDatiAmminDAO.updateWorkingAvvisoDatiAmminTotaleUtenza(cAnnuo, nap, codRiscossione);
	}

	private void updateAvvisoPagamento(Long idSpedizione, String nap, BigDecimal canoneAnnuo) throws DAOException {
		// imp_totale_dovuto = NVL(imp_totale_dovuto, 0) +
		// NVL(rec_annualita(corrente).canone_annuo, 0)

		BigDecimal cAnnuo = canoneAnnuo == null ? new BigDecimal(0) : canoneAnnuo;
		avvisoPagamentoDAO.updateWorkingAvvisoPagamentoImpTotaleDovuto(cAnnuo, nap, idSpedizione);
	}

	private BigDecimal getCanoneUnitario(String jsonRegola) {
		// TODO verificare se vale per tutti gli ambiti
		final JSONObject obj = new JSONObject(jsonRegola);
		String canoneUnitario = obj.get("canone_unitario").toString();
		return new BigDecimal(canoneUnitario);
	}

	private String insertAvvisoAnnualita(AnnualitaSdDTO annualita, RiscossioneBollData riscossioneBsData,
			String tipoElabora, String step) {
		LOGGER.debug("[BollDatiWorkingApiServiceImpl::insertAvvisoAnnualita] BEGIN");
		AvvisoAnnualitaDTO avvisoAnnualita = new AvvisoAnnualitaDTO();
		avvisoAnnualita.setNap(riscossioneBsData.getNap().getNap());
		avvisoAnnualita.setCodiceUtenza(riscossioneBsData.getRiscossioneBoll().getCodRiscossione());
		if (tipoElabora.equals(TIPO_ELABORA_BO)) {
			// Bollettazione ordinaria
			avvisoAnnualita.setAnnoRichPagamento(riscossioneBsData.getAnno());
			String annoScadConcessione = riscossioneBsData.getRiscossioneBoll().getDataScadConcessione() != null
					? getYearFromDate(riscossioneBsData.getRiscossioneBoll().getDataScadConcessione())
					: null;
			String annoScadPag = riscossioneBsData.getDataScadenzaPagamento().substring(0, 4);
			if (annoScadConcessione != null && annoScadConcessione.equals(annoScadPag)) {
				avvisoAnnualita
						.setFrazTotaleCanoneAnno(riscossioneBsData.getCanonePratica().setScale(0, RoundingMode.DOWN));
				// troncare canone pratica a due decimali
				avvisoAnnualita.setValore20Calc(riscossioneBsData.getCanonePratica().setScale(2, RoundingMode.DOWN));
			} else {
				avvisoAnnualita
						.setFrazTotaleCanoneAnno(riscossioneBsData.getCanoneCalcolato().setScale(0, RoundingMode.DOWN));
				avvisoAnnualita.setValore20Calc(riscossioneBsData.getCanoneCalcolato());
			}
			avvisoAnnualita.setTotaleCanoneAnnoCalc(riscossioneBsData.getCanoneCalcolato());
		} else {
			// Bollettazione speciale
			avvisoAnnualita.setAnnoRichPagamento(annualita.getAnno());
			// Canone annuo va troncato cf. pag 44 analisi passo 3.13
			avvisoAnnualita.setFrazTotaleCanoneAnno(annualita.getCanoneAnnuo().setScale(0, RoundingMode.DOWN));
			avvisoAnnualita.setTotaleCanoneAnnoCalc(null);
			avvisoAnnualita.setValore20Calc(null);
		}

		AvvisoAnnualitaDTO ret = null;
		try {
			long start = System.currentTimeMillis();
			ret = avvisoAnnualitaDAO.saveAvvisoAnnualitaWorking(avvisoAnnualita);
			long stop = System.currentTimeMillis();
			LOGGER.debug(
					"[BollDatiWorkingApiServiceImpl::insertAvvisoAnnualita] QueryExecutionTime: " + (stop - start));
		} catch (DAOException e) {
			LOGGER.error("[BollDatiWorkingApiServiceImpl::insertAvvisoAnnualita] Inserimento AvvisoAnnualita fallito: "
					+ avvisoAnnualita);
		}
		if (tipoElabora.equals(TIPO_ELABORA_BO)) {
			logStep(riscossioneBsData.getIdElabora(), PASSO_INSERT_AVVISO_ANN, 0,
					FASE_EMISSIONE + " - " + step + " - Inserimento annualita: NAP " + ret.getNap()
							+ " - Codice Riscossione: " + riscossioneBsData.getRiscossioneBoll().getCodRiscossione()
							+ " - Anno: " + avvisoAnnualita.getAnnoRichPagamento(),
					attore, COD_FASE_EMISBO);
		} else {
			String codFase = tipoElabora.equals(TIPO_ELABORA_BS) ? COD_FASE_EMISBS : COD_FASE_EMISBG;
			logStep(riscossioneBsData.getIdElabora(), PASSO_INSERT_AVVISO_ANN, 0,
					FASE_EMISSIONE + " - " + step + " - Inserimento annualita: NAP " + ret.getNap()
							+ " - Codice Riscossione: " + riscossioneBsData.getRiscossioneBoll().getCodRiscossione()
							+ " - Annualita: " + annualita.getIdAnnualitaSd() + " - Anno: " + annualita.getAnno(),
					attore, codFase);
		}

		LOGGER.debug("[BollDatiWorkingApiServiceImpl::insertAvvisoAnnualita] END");
		return ret.getNap();
	}

	private String insertAvvisoPagamento(RiscossioneBollData riscossioneBsData, String step, String codFase) {
		LOGGER.debug("[BollDatiWorkingApiServiceImpl::insertAvvisoPagamento] BEGIN");
		AvvisoPagamentoDTO avvisoPagamento = new AvvisoPagamentoDTO();
		avvisoPagamento.setNap(riscossioneBsData.getNap().getNap());
		avvisoPagamento.setIdSpedizione(riscossioneBsData.getIdSpedizione());
		avvisoPagamento.setProgNapAvvisoPagamento(Integer.valueOf(riscossioneBsData.getNap().getProgressivo()));
		avvisoPagamento.setImpTotaleDovuto(null);

		AvvisoPagamentoDTO ret = null;
		try {
			long start = System.currentTimeMillis();
			ret = avvisoPagamentoDAO.saveAvvisoPagamentoWorking(avvisoPagamento);
			long stop = System.currentTimeMillis();
			LOGGER.debug(
					"[BollDatiWorkingApiServiceImpl::insertAvvisoPagamento] QueryExecutionTime: " + (stop - start));

			logStep(riscossioneBsData.getIdElabora(), PASSO_GENERA_NAP, 0,
					FASE_EMISSIONE + " - " + step + " - Genera NAP e Inserimento in WORK Avviso Pagamento OK: NAP "
							+ ret.getNap() + " - Soggetto: " + riscossioneBsData.getSoggettoGruppo()
							+ " - Riscossione: " + riscossioneBsData.getRiscossioneBoll().getIdRiscossione(),
					attore, codFase);
		} catch (DAOException e) {
			LOGGER.error("[BollDatiWorkingApiServiceImpl::insertAvvisoPagamento] Inserimento avviso pagamento fallito: "
					+ avvisoPagamento);
		}

		LOGGER.debug("[BollDatiWorkingApiServiceImpl::insertAvvisoPagamento] END nap: " + ret.getNap());
		return ret.getNap();
	}

	private Long insertStatoDebitorio(RiscossioneBollData riscossioneBsData, String codFase) {
		LOGGER.debug("[BollDatiWorkingApiServiceImpl::insertStatoDebitorio] BEGIN");
		StatoDebitorioExtendedDTO statoDebitorio = new StatoDebitorioExtendedDTO();
		RiscossioneBollDTO riscAttiva = riscossioneBsData.getRiscossioneBoll();

		statoDebitorio.setIdStatoDebitorio(riscAttiva.getIdStatoDebitorio());
		statoDebitorio.setIdRiscossione(riscAttiva.getIdRiscossione());
//		statoDebitorio.setIdProvvedimento(riscossioneBsData.getProvvedimento().getIdProvvedimento());
		statoDebitorio.setIdSoggetto(riscAttiva.getIdSoggetto());
		statoDebitorio.setIdGruppoSoggetto(riscAttiva.getIdGruppoSoggetto());
		Long idRecapito = riscAttiva.getIdRecapitoA() != null ? riscAttiva.getIdRecapitoA()
				: riscAttiva.getIdRecapitoP();
		statoDebitorio.setIdRecapito(idRecapito);
		statoDebitorio.setIdStatoContribuzione(riscAttiva.getIdStatoContribuzione());
		statoDebitorio.setIdTipoDilazione(null);
		statoDebitorio.setNap(riscossioneBsData.getNap().getNap());
		statoDebitorio.setNumRichiestaProtocollo(riscossioneBsData.getNumProtocollo());
		SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
		try {
			statoDebitorio.setDataRichiestaProtocollo(df.parse(riscossioneBsData.getDataProtocollo()));
		} catch (ParseException e) {
			LOGGER.error("[BollDatiWorkingApiServiceImpl::insertStatoDebitorio] Error parsing DataProtocollo: "
					+ riscossioneBsData.getDataProtocollo());
			return null;
		}

		if (riscossioneBsData.getProvvedimento() != null) {
			ProvvedimentoDTO provv = riscossioneBsData.getProvvedimento();
			statoDebitorio.setNumTitolo(provv.getNumTitolo());
			statoDebitorio.setNumTitolo(provv.getDataProvvedimento());
			statoDebitorio.setNumTitolo(provv.getTipoTitoloExtendedDTO().getDesTipoTitolo());
		}

		StatoDebitorioDTO ret = null;
		try {
			ret = statoDebitorioDAO.saveStatoDebitorioWorking(statoDebitorio, false);
		} catch (DAOException e) {
			logStep(riscossioneBsData.getIdElabora(), PASSO_INSERT_SD_IN_WORK, 1,
					FASE_EMISSIONE + " - Step 3 - Inserimento SD nella tabella di Working KO: " + e.getMessage(),
					attore, codFase);
			LOGGER.error(
					"[BollDatiWorkingApiServiceImpl::insertStatoDebitorio] Inserimento SD fallito: " + statoDebitorio);
			return null;
		}

		logStep(riscossioneBsData.getIdElabora(), PASSO_INSERT_SD_IN_WORK, 0,
				FASE_EMISSIONE + " - Step 3 - Inserimento SD nella tabella di Working: " + ret.getIdStatoDebitorio(),
				attore, codFase);
		LOGGER.debug("[BollDatiWorkingApiServiceImpl::insertStatoDebitorio] END idStatoDebitorio: "
				+ ret.getIdStatoDebitorio());
		return ret.getIdStatoDebitorio();
	}

	private Long insertRataSd(RiscossioneBollData riscossioneBsData, boolean generateId, BigDecimal canoneDovuto,
			String step, String codFase) {
		LOGGER.debug("[BollDatiWorkingApiServiceImpl::insertRataSd] BEGIN");
		RataSdDTO rataSd = new RataSdDTO();
		if (!generateId) {
			rataSd.setIdRataSd(riscossioneBsData.getRataSd().getIdRataSd());
		}
		rataSd.setIdStatoDebitorio(riscossioneBsData.getRiscossioneBoll().getIdStatoDebitorio());
		rataSd.setIdRataSdPadre(null);
		if (canoneDovuto != null) {
			rataSd.setCanoneDovuto(canoneDovuto);
		} else {
			rataSd.setCanoneDovuto(riscossioneBsData.getRataSd().getCanoneDovuto());
		}
		rataSd.setInteressiMaturati(null);
		rataSd.setDataScadenzaPagamento(riscossioneBsData.getDataScadenzaPagamento());

		RataSdDTO ret = null;
		try {
			ret = rataSdDAO.saveRataSdWorking(rataSd, generateId);
		} catch (DAOException e) {
			LOGGER.error("[BollDatiWorkingApiServiceImpl::insertRataSd] Inserimento fallito: " + rataSd);
		}
		logStep(riscossioneBsData.getIdElabora(), PASSO_INSERT_RATA_IN_WORK, 0,
				FASE_EMISSIONE + " - " + step + " - Inserimento Rata Principale nella tabella di Working: "
						+ ret.getIdRataSd() + " - Canone dovuto: " + ret.getCanoneDovuto(),
				attore, codFase);
		LOGGER.debug("[BollDatiWorkingApiServiceImpl::insertRataSd] END idRataSd: " + ret.getIdRataSd());
		return ret.getIdRataSd();
	}

	private AnnualitaSdDTO insertAnnualitaSd(RiscossioneBollData riscossioneBollData) {
		LOGGER.debug("[BollDatiWorkingApiServiceImpl::insertAnnualitaSd] BEGIN");
		AnnualitaSdDTO annualitaSd = new AnnualitaSdDTO();
		AnnualitaSdDTO ret = null;
		try {
			annualitaSd.setIdStatoDebitorio(riscossioneBollData.getRiscossioneBoll().getIdStatoDebitorio());
			BigDecimal canonePratica = riscossioneBollData.getCanonePratica().setScale(0, RoundingMode.DOWN);
			annualitaSd.setCanoneAnnuo(canonePratica);
			annualitaSd.setAnno(riscossioneBollData.getAnno());
			// NB. il jsonDt sulla riscossione non è uguale al json che si mette su
			// annualitaSd
			// su JsonDtAnnualitaSd devo mettere solo la parte di dati tecnici.
			DatiTecniciAmbienteDTO datiTecnici = BollUtils
					.getDatiTecniciFromJsonDt(riscossioneBollData.getRiscossioneBoll().getJsonDt());
			annualitaSd.setJsonDtAnnualitaSd(datiTecnici.toJsonString());

			String annoRif = riscossioneBollData.getAnno() + "-01-01";
			ComponenteDtDTO compDTO = componentiDtDAO.loadComponenteDt(riscossioneBollData.getIdAmbito(), "GESTIONE",
					annoRif);
			annualitaSd.setIdComponenteDt(compDTO.getIdComponenteDt().intValue());

			ret = annualitaSdDAO.insertAnnualitaSdWorking(annualitaSd);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("[BollDatiWorkingApiServiceImpl::insertAnnualitaSd] Inserimento fallito: " + annualitaSd);
		}
		logStep(riscossioneBollData.getIdElabora(), PASSO_INS_ANN_SD_IN_WORK, 0,
				FASE_EMISSIONE + " - Step 5 - Inserisce Dati Annualita' in working RISCA_W_ANNUALITA_SD "
						+ " - Annualita' " + ret.getIdAnnualitaSd() + " - SD " + ret.getIdStatoDebitorio()
						+ " - Canone Annuo " + ret.getCanoneAnnuo() + " - Anno " + ret.getAnno(),
				attore, COD_FASE_EMISBO);
		LOGGER.debug("[BollDatiWorkingApiServiceImpl::insertAnnualitaSd] END idAnnualitaSd: " + ret.getIdAnnualitaSd());
		return ret;
	}

	private String insertAvvisoDatiTitolare(RiscossioneBollData riscossioneBsData, int progrPerTitolare, String step,
			String codFase) {
		LOGGER.debug("[BollDatiWorkingApiServiceImpl::insertAvvisoDatiTitolare] BEGIN");
		AvvisoDatiTitolareDTO avvisoDT = new AvvisoDatiTitolareDTO();
		String numeroProtocolloSped = calcolaNumeroProtocolloSped(riscossioneBsData.getNumProtocollo(),
				progrPerTitolare);
		avvisoDT.setNap(riscossioneBsData.getNap().getNap());
		avvisoDT.setIdSoggetto(riscossioneBsData.getRiscossioneBoll().getIdSoggetto());
		avvisoDT.setIdTitolare(riscossioneBsData.getRiscossioneBoll().getIdTitolare());
		avvisoDT.setIdGruppoSoggetto(riscossioneBsData.getRiscossioneBoll().getIdGruppoSoggetto());

		avvisoDT.setImportoDaVersare(null);
		avvisoDT.setScadenzaPagamento(riscossioneBsData.getDataScadenzaPagamento());
		avvisoDT.setnUtenze(null);
		avvisoDT.setAnnualitaPagamento(riscossioneBsData.getAnno());
		avvisoDT.setStatiPagamenti(riscossioneBsData.getRiscossioneBoll().getStatoContribuzione());
		avvisoDT.setCodiceFiscaleCalc(riscossioneBsData.getTitolare().getCfSoggetto());
		avvisoDT.setnAvvisoCalc(riscossioneBsData.getNap().getNap());
		avvisoDT.setScadenzaPagamento2(null);

		RecapitiExtendedDTO recapito = getRecapitoTitolare(riscossioneBsData, step, codFase);
		if (recapito != null) {
			String modalitaInvio = "";
			if (codFase.equals(COD_FASE_EMISBS) || codFase.equals(COD_FASE_EMISBG)) {
				modalitaInvio = getModalitaInvioBS(recapito.getTipoInvio().getCodTipoInvio());
				avvisoDT.setModalitaInvio(modalitaInvio);
				avvisoDT.setPecEmail(recapito.getPec());
			} else {
				modalitaInvio = getModalitaInvio(recapito.getTipoInvio().getCodTipoInvio());
				avvisoDT.setModalitaInvio(modalitaInvio);
				if (modalitaInvio.equals("PEC")) {
					avvisoDT.setPecEmail(recapito.getPec());
				} else {
					avvisoDT.setPecEmail(recapito.getEmail());
				}
			}
			if (recapito.getIndirizziSpedizione() != null && recapito.getIndirizziSpedizione().size() > 0) {
				Long igGruppoSoggetto = riscossioneBsData.getRiscossioneBoll().getIdGruppoSoggetto();
				IndirizzoSpedizioneDTO indSped = null;
				if (igGruppoSoggetto == null) {
					for (IndirizzoSpedizioneDTO ind : recapito.getIndirizziSpedizione()) {
						if (ind.getIdGruppoSoggetto() == null && ind.getIndValidoPostel().equals(1l)) {
							indSped = ind;
						}
					}
				} else {
					for (IndirizzoSpedizioneDTO ind : recapito.getIndirizziSpedizione()) {
						if (ind.getIdGruppoSoggetto() != null && ind.getIdGruppoSoggetto().equals(igGruppoSoggetto)
								&& ind.getIndValidoPostel().equals(1l)) {
							indSped = ind;
						}
					}
				}
				if (indSped != null) {
					String frazione = indSped.getFrazionePostel() != null ? indSped.getFrazionePostel() : "";
					String indirizzo = indSped.getIndirizzoPostel() != null ? indSped.getIndirizzoPostel() : "";
					String indPostel = (frazione + " " + indirizzo).trim();
					avvisoDT.setNomeTitolareIndPost(indSped.getDestinatarioPostel());
					avvisoDT.setIndirizzoIndPost(indPostel);
					avvisoDT.setPressoIndPost(indSped.getPressoPostel());
					avvisoDT.setComuneIndPost(indSped.getCittaPostel());
					avvisoDT.setCapIndPost(indSped.getCapPostel());
					avvisoDT.setProvIndPost(indSped.getProvinciaPostel());
				} else {
					// ERRORE: manca indirizzo di spedizione
					LOGGER.error(
							"[BollDatiWorkingApiServiceImpl::insertAvvisoDatiTitolare] IndirizzoSpedizione non trovato: idRecapito=  "
									+ recapito.getIdRecapito() + " idSoggetto= "
									+ riscossioneBsData.getRiscossioneBoll().getIdSoggetto() + " idGruppoSoggetto="
									+ riscossioneBsData.getRiscossioneBoll().getIdGruppoSoggetto());
					logStep(riscossioneBsData.getIdElabora(), PASSO_INSERT_SOGG_IN_WORK, 1,
							FASE_EMISSIONE + "IndirizzoSpedizione non trovato: idRecapito=  " + recapito.getIdRecapito()
									+ " idSoggetto= " + riscossioneBsData.getRiscossioneBoll().getIdSoggetto()
									+ " idGruppoSoggetto="
									+ riscossioneBsData.getRiscossioneBoll().getIdGruppoSoggetto(),
							attore, codFase);
					return null;
				}

			} else {
				// ERRORE: manca indirizzo di spedizione
				LOGGER.error(
						"[BollDatiWorkingApiServiceImpl::insertAvvisoDatiTitolare] IndirizzoSpedizione non trovato: idRecapito=  "
								+ recapito.getIdRecapito() + " idSoggetto= "
								+ riscossioneBsData.getRiscossioneBoll().getIdSoggetto() + " idGruppoSoggetto="
								+ riscossioneBsData.getRiscossioneBoll().getIdGruppoSoggetto());
				logStep(riscossioneBsData.getIdElabora(), PASSO_INSERT_SOGG_IN_WORK, 1,
						FASE_EMISSIONE + " - " + step + " - IndirizzoSpedizione non trovato: idRecapito=  "
								+ recapito.getIdRecapito() + " idSoggetto= "
								+ riscossioneBsData.getRiscossioneBoll().getIdSoggetto() + " idGruppoSoggetto="
								+ riscossioneBsData.getRiscossioneBoll().getIdGruppoSoggetto(),
						attore, codFase);
				return null;
			}
		} else {
			// IdRecapito non trovato tra i dati del soggetto ritornando NULL il batch
			// terminera' in errore --> log e registrazione errore gia' fatta su funzione
			// getRecapitoTitolare
			return null;
		}

		avvisoDT.setNumeroProtocolloSped(numeroProtocolloSped);

		SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
		try {
			avvisoDT.setDataProtocolloSped(df.parse(riscossioneBsData.getDataProtocollo()));
		} catch (ParseException e) {
			LOGGER.error("[BollDatiWorkingApiServiceImpl::insertAvvisoDatiTitolare] Error parsing DataProtocollo: "
					+ riscossioneBsData.getDataProtocollo());
			return null;
		}

		AvvisoDatiTitolareDTO ret = null;
		try {
			long start = System.currentTimeMillis();
			ret = avvisoDatiTitolareDAO.saveAvvisoDatiTitolareWorking(avvisoDT);
			long stop = System.currentTimeMillis();
			LOGGER.info(
					"[BollDatiWorkingApiServiceImpl::insertAvvisoDatiTitolare] QueryExecutionTime: " + (stop - start));
		} catch (DAOException e) {
			LOGGER.error(
					"[BollDatiWorkingApiServiceImpl::insertAvvisoDatiTitolare] inserimento avviso dati titolare fallito:"
							+ avvisoDT);
		}
		String result = null;
		if (ret != null) {
			logStep(riscossioneBsData.getIdElabora(), PASSO_INSERT_SOGG_IN_WORK, 0,
					FASE_EMISSIONE + " - " + step + " - Inserimento Dati Titolare nella tabella di Working: soggetto: "
							+ riscossioneBsData.getSoggettoGruppo() + " - Nap: " + ret.getNap() + " - Protocollo Sped: "
							+ numeroProtocolloSped,
					attore, codFase);
			result = numeroProtocolloSped;
		}
		LOGGER.debug("[BollDatiWorkingApiServiceImpl::insertAvvisoDatiTitolare] END numeroProtocolloSped: " + result);
		return result;
	}

	private RecapitiExtendedDTO getRecapitoTitolare(RiscossioneBollData riscossioneBsData, String step,
			String codFase) {
		Long idRecapito = riscossioneBsData.getRiscossioneBoll().getIdRecapitoA() != null
				? riscossioneBsData.getRiscossioneBoll().getIdRecapitoA()
				: riscossioneBsData.getRiscossioneBoll().getIdRecapitoP();
		List<RecapitiExtendedDTO> recapiti = riscossioneBsData.getTitolare().getRecapiti();
		for (RecapitiExtendedDTO recapito : recapiti) {
			if (recapito.getIdRecapito().equals(idRecapito)) {
				return recapito;
			}
		}
		
		// Se arriva qui non e' stato trovato l'idRecapito (principale o alternativo che
		// sia) tra i dati del soggetto restituiti dal servizio. Quindi si tratta di un
		// problema nei dati che va sanato. Ritornando null la bollettazione terminera'
		// con errore
		LOGGER.error("[BollDatiWorkingApiServiceImpl::getRecapitoTitolare] Recapito non trovato: idRecapito=  "
				+ idRecapito + " idSoggetto= " + riscossioneBsData.getRiscossioneBoll().getIdSoggetto()
				+ " idGruppoSoggetto=" + riscossioneBsData.getRiscossioneBoll().getIdGruppoSoggetto());
		logStep(riscossioneBsData.getIdElabora(), PASSO_INSERT_SOGG_IN_WORK, 1,
				FASE_EMISSIONE + " - " + step + " - Recapito non trovato: idRecapito=  " + idRecapito + " idSoggetto= "
						+ riscossioneBsData.getRiscossioneBoll().getIdSoggetto() + " idGruppoSoggetto="
						+ riscossioneBsData.getRiscossioneBoll().getIdGruppoSoggetto(),
				attore, codFase);
		return null;
	}

	private String calcolaNumeroProtocolloSped(String numProtocollo, int progrPerTitolare) {
		// Padding con zero a sinistra fino a lunghezza 5
		String protocollo = null;
		int index = numProtocollo.indexOf('/');
		if (index != -1) {
			String protProg = numProtocollo.substring(0, index);
			protocollo = protProg + " (Rif " + String.format("%1$5s", progrPerTitolare).replace(' ', '0') + ")"
					+ numProtocollo.substring(index);
			;
		} else {
			protocollo = numProtocollo + " (Rif " + String.format("%1$5s", progrPerTitolare).replace(' ', '0') + ")";
		}
		return protocollo;
	}

	private String insertAvvisoDatiAmmin(RiscossioneBollData riscossioneBsData, boolean isBO, String step,
			String codFase) {
		LOGGER.debug("[BollDatiWorkingApiServiceImpl::insertAvvisoDatiAmmin] BEGIN");
		String descrTipoIstanza = "";
		if (riscossioneBsData.getIstanza() != null) {
			descrTipoIstanza = " - "
					+ riscossioneBsData.getIstanza().getTipiProvvedimentoExtendedDTO().getDesTipoProvvedimento();
		} else if (riscossioneBsData.getProvvedimento() != null) {
			String codTipoProvv = riscossioneBsData.getProvvedimento().getTipiProvvedimentoExtendedDTO()
					.getCodTipoProvvedimento();
			if (codTipoProvv.equalsIgnoreCase(BollUtils.COD_TIPO_PROVVEDIMENTO_AUT_PROVVISORIA)) {
				descrTipoIstanza = " - " + riscossioneBsData.getProvvedimento().getTipiProvvedimentoExtendedDTO()
						.getDesTipoProvvedimento();
			}
		}
		AvvisoDatiAmminDTO avvisoDati = new AvvisoDatiAmminDTO();
		avvisoDati.setNap(riscossioneBsData.getNap().getNap());
		avvisoDati.setIdStatoDebitorio(riscossioneBsData.getRiscossioneBoll().getIdStatoDebitorio());
		avvisoDati.setCodiceUtenza(riscossioneBsData.getRiscossioneBoll().getCodRiscossione());
		avvisoDati.setCorpoIdrico(riscossioneBsData.getCorpoIdrico());
		avvisoDati.setComuneDiPresa(riscossioneBsData.getComune());
		if (isBO) {
			avvisoDati.setPeriodoDiContribuzione("" + riscossioneBsData.getAnno());
		} else {
			avvisoDati.setPeriodoDiContribuzione(riscossioneBsData.getPeriodoPag());
		}

		String annoScadConcessione = riscossioneBsData.getRiscossioneBoll().getDataScadConcessione() != null
				? getYearFromDate(riscossioneBsData.getRiscossioneBoll().getDataScadConcessione())
				: null;
		String annoScadPag = riscossioneBsData.getDataScadenzaPagamento().substring(0, 4);
		if (isBO) {
			avvisoDati.setTotaleUtenzaCalc(getTotaleUtenzaBo(riscossioneBsData, annoScadConcessione, annoScadPag));
		} else {
			avvisoDati.setTotaleUtenzaCalc(getTotaleUtenzaBs(riscossioneBsData, annoScadConcessione, annoScadPag));
		}

		SimpleDateFormat df_sped = new SimpleDateFormat(DATE_FORMAT_SPED);
		if (riscossioneBsData.getRiscossioneBoll().getDataScadConcessione() == null) {
			avvisoDati.setScadConcCalc("****");
		} else {

			String scadConc = df_sped.format(riscossioneBsData.getRiscossioneBoll().getDataScadConcessione());
			avvisoDati.setScadConcCalc(scadConc + descrTipoIstanza);
		}

		SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
		ProvvedimentoDTO provvedimento = riscossioneBsData.getProvvedimento();
		if (provvedimento == null) {
			avvisoDati.setProvvedimentoCalc("****");
		} else {
			String desTipoTitolo = provvedimento.getTipoTitoloExtendedDTO().getDesTipoTitolo();
			String numTitolo = provvedimento.getNumTitolo();
			String dataTitolo = provvedimento.getDataProvvedimento();
			String dataTitoloFormat = dataTitolo;

			Date dataTitoloDate = null;
			try {
				dataTitoloDate = df.parse(dataTitolo);
				dataTitoloFormat = df_sped.format(dataTitoloDate);
			} catch (ParseException e1) {
				LOGGER.error("[BollDatiWorkingApiServiceImpl::insertAvvisoDatiAmmin] Error parsing data provvedimanto: "
						+ provvedimento.getDataProvvedimento());
			}
			avvisoDati.setProvvedimentoCalc(desTipoTitolo + " " + numTitolo + " del " + dataTitoloFormat);
		}

		if (!riscossioneBsData.getDataScadenzaEmasIso().isEmpty()) {
			try {
				avvisoDati.setDataScadEmasIso(df.parse(riscossioneBsData.getDataScadenzaEmasIso()));
			} catch (ParseException e) {
				LOGGER.error(
						"[BollDatiWorkingApiServiceImpl::insertAvvisoDatiAmmin] Error parsing DataScadenzaEmasIso: "
								+ riscossioneBsData.getDataScadenzaEmasIso());
				return null;
			}
		}

		avvisoDati.setNumPratica(riscossioneBsData.getRiscossioneBoll().getNumPratica());
		// TODO questa info si valorizza per ambito = 2
		avvisoDati.setDescrUtilizzo(null);
		// serve nel caso di Grande Idroelettrico
		avvisoDati.setTotEnergProd(riscossioneBsData.getTotEnergProd());

		if (isBO) {
			avvisoDati.setImpCompensCanone("" + riscossioneBsData.getSommCompensPerPrat());
			avvisoDati.setRecCanone("" + riscossioneBsData.getCanoneDovutoAnnoPrec());
		}

		AvvisoDatiAmminDTO ret = null;
		try {
			long start = System.currentTimeMillis();
			ret = avvisoDatiAmminDAO.saveAvvisoDatiAmminWorking(avvisoDati);
			long stop = System.currentTimeMillis();
			LOGGER.debug(
					"[BollDatiWorkingApiServiceImpl::insertAvvisoDatiAmmin] QueryExecutionTime: " + (stop - start));
		} catch (DAOException e) {
			LOGGER.error("[BollDatiWorkingApiServiceImpl::insertAvvisoDatiAmmin] inserimento Avviso Dati Ammin fallito:"
					+ avvisoDati);
		}
		String result = null;
		if (ret != null) {
			logStep(riscossioneBsData.getIdElabora(), PASSO_INSERT_DTAMM_IN_WORK, 0,
					FASE_EMISSIONE + " - " + step
							+ " - Inserimento Dati Amministrativi nella tabella di Working:  Nap: " + ret.getNap()
							+ " - Codice Riscossione: " + riscossioneBsData.getRiscossioneBoll().getCodRiscossione(),
					attore, codFase);
			result = descrTipoIstanza;
			this.w_avvisoDatiAmmin = ret;
		}

		LOGGER.debug(
				"[BollDatiWorkingApiServiceImpl::insertAvvisoDatiAmmin] END descrTipoIstanza: " + descrTipoIstanza);
		return result;
	}

	private String getTotaleUtenzaBs(RiscossioneBollData riscossioneBollData, String annoScadConcessione,
			String annoScadPag) {
		if (annoScadConcessione != null && !annoScadConcessione.equals(annoScadPag)) {
			// TODO CanoneCalcolato -> campo che al momento non viene mai valorizzato
			BigDecimal canoneCalcolato = riscossioneBollData.getCanoneCalcolato();
			return canoneCalcolato != null ? "" + canoneCalcolato : null;
		} // else totaleUtenzaCalc resta NULL
		return null;
	}

	private String getTotaleUtenzaBo(RiscossioneBollData riscossioneBollData, String annoScadConcessione,
			String annoScadPag) {
		double canonePratica = riscossioneBollData.getCanonePratica().setScale(0, RoundingMode.DOWN).doubleValue();
		double canoneDovutoAnnoPrec = riscossioneBollData.getCanoneDovutoAnnoPrec().setScale(0, RoundingMode.DOWN)
				.doubleValue();
		double sommCompensPerPrat = riscossioneBollData.getSommCompensPerPrat().doubleValue();
		double canoneCalcolato = riscossioneBollData.getCanoneCalcolato().setScale(0, RoundingMode.DOWN).doubleValue();
		if (annoScadConcessione != null && annoScadConcessione.equals(annoScadPag)) {
			return String.valueOf(canonePratica + canoneDovutoAnnoPrec - sommCompensPerPrat);
		} else {
			return String.valueOf(canoneCalcolato + canoneDovutoAnnoPrec - sommCompensPerPrat);
		}
	}

	private String insertAvvisoUsoBs(RiscossioneBollData riscossioneBollData, AnnualitaSdDTO annualita,
			AnnualitaUsoSdExtendedDTO uso, String ridAumConcat, BigDecimal portMediaConc, BigDecimal canoneUnitario,
			String codFase, Integer percQuotaVar, BigDecimal prezzoMedOraPond) {
		LOGGER.debug("[BollDatiWorkingApiServiceImpl::insertAvvisoUsoBs] BEGIN");
		AvvisoUsoDTO avvisoUso = new AvvisoUsoDTO();
		avvisoUso.setNap(riscossioneBollData.getNap().getNap());
		avvisoUso.setCodiceUtenza(riscossioneBollData.getRiscossioneBoll().getCodRiscossione());
		avvisoUso.setAnnoRichPagamento(annualita.getAnno());
		avvisoUso.setUsoDenominazione(uso.getDesTipoUso());
		avvisoUso.setCondizioniParticolariCalc(ridAumConcat);
		avvisoUso.setUsoDenominazionePCalc(uso.getDesTipoUso());
		avvisoUso.setUnitaMisPCalc(uso.getCanoneUso() == null ? "0" : uso.getSiglaUnitaMisura());
		avvisoUso.setQuantitaPCalc(portMediaConc);
		avvisoUso.setUnitaDiMisuraPCalc(uso.getDesUnitaMisura());

		BigDecimal vCanoneUnitario = null;
		if (uso.getIdTipoUso().equals(3500l)) {
			vCanoneUnitario = BigDecimal.valueOf(percQuotaVar);
		} else if (uso.getIdTipoUso().equals(3600l)) {
			vCanoneUnitario = prezzoMedOraPond;
		} else {
			// Arrotondare canoneUnitario alla 4 decimale
			Math.round(canoneUnitario.doubleValue());
			vCanoneUnitario = BigDecimal.valueOf(BollUtils.round(canoneUnitario.doubleValue(), 4));
		}
		avvisoUso.setCanoneUnitarioPCalc(vCanoneUnitario);
		avvisoUso.setCanoneUsoPCalc(uso.getCanoneUso());
		// TODO vale per ambito ambiente perche' esamina i dati tecnici
		avvisoUso.setPercFaldaProf(
				BollUtils.estraiPercFaldaProfondaDaUsi(annualita.getJsonDtAnnualitaSd(), uso.getIdTipoUso()));

		AvvisoUsoDTO ret = null;
		try {
			ret = avvisoUsoDAO.saveAvvisoUsoWorking(avvisoUso);
		} catch (DAOException e) {
			LOGGER.error("[BollDatiWorkingApiServiceImpl::insertAvvisoUsoBs] inserimento Avviso Uso :" + avvisoUso);

		}
		logStep(riscossioneBollData.getIdElabora(), PASSO_INSERT_AVVISO_USO, 0,
				FASE_EMISSIONE + " - Step 3 - Inserimento usi: NAP: " + ret.getNap() + " - Annualita: "
						+ annualita.getIdAnnualitaSd() + " - Anno: " + annualita.getAnno() + " - Uso: "
						+ uso.getDesTipoUso(),
				attore, codFase);

		LOGGER.debug("[BollDatiWorkingApiServiceImpl::insertAvvisoUso] END nap: " + ret.getNap());
		return ret.getNap();
	}

	private String insertAvvisoUsoBo(RiscossioneBollData riscossioneBollData, AnnualitaSdDTO annualita,
			BollCanoneUsoDTO riscUso, String ridAumConcat, String desUnitaMisura) {
		LOGGER.debug("[BollDatiWorkingApiServiceImpl::insertAvvisoUsoBo] BEGIN");
		AvvisoUsoDTO avvisoUso = new AvvisoUsoDTO();
		avvisoUso.setNap(riscossioneBollData.getNap().getNap());
		avvisoUso.setCodiceUtenza(riscossioneBollData.getRiscossioneBoll().getCodRiscossione());
		avvisoUso.setAnnoRichPagamento(riscossioneBollData.getAnno());

		avvisoUso.setUsoDenominazione(riscUso.getTipoUsoDatoTecnico().getTipoUsoLegge());
		avvisoUso.setCondizioniParticolariCalc(ridAumConcat);
		avvisoUso.setUsoDenominazionePCalc(riscUso.getTipoUsoDatoTecnico().getTipoUsoLegge());
		avvisoUso.setUnitaMisPCalc(
				riscUso.getCanonePerUso() == null ? "-" : riscUso.getTipoUsoDatoTecnico().getUnitaDiMisura());
		avvisoUso.setQuantitaPCalc(riscUso.getTipoUsoDatoTecnico().getQuantita());
		avvisoUso.setUnitaDiMisuraPCalc(desUnitaMisura);

		// Troncare canonePerUso
		BigDecimal canonePerUso = riscUso.getCanoneUso().getCanoneUso() != null
				? riscUso.getCanoneUso().getCanoneUso().setScale(0, RoundingMode.DOWN)
				: BigDecimal.ZERO;
		BigDecimal canoneUnitarioPCalc = null;
		if (canonePerUso.compareTo(BigDecimal.ZERO) != 0) {
			// Arrotondare canone unitario a 4 decimali
			canoneUnitarioPCalc = BigDecimal
					.valueOf(BollUtils.round(riscUso.getCanoneUso().getCanoneUnitario().doubleValue(), 4));
		}
		avvisoUso.setCanoneUnitarioPCalc(canoneUnitarioPCalc);

		BigDecimal setCanoneUsoPCalc = null;
		if (riscUso.getCanonePerUsoNonFraz() != null
				&& riscUso.getCanonePerUsoNonFraz().compareTo(BigDecimal.ZERO) != 0) {
			// Arrotondare canonePerUsNonfraz alla 2 decimale
			setCanoneUsoPCalc = BigDecimal.valueOf(BollUtils.round(riscUso.getCanonePerUsoNonFraz().doubleValue(), 2));
		}
		avvisoUso.setCanoneUsoPCalc(setCanoneUsoPCalc);
		BigDecimal percFaldaProf = riscUso.getTipoUsoDatoTecnico().getPercFaldaProfonda();
		if (percFaldaProf != null && percFaldaProf.equals(BigDecimal.valueOf(-1l))) {
			percFaldaProf = BigDecimal.ZERO;
		}
		avvisoUso.setPercFaldaProf(percFaldaProf);

		AvvisoUsoDTO ret = null;
		try {
			long start = System.currentTimeMillis();
			ret = avvisoUsoDAO.saveAvvisoUsoWorking(avvisoUso);
			long stop = System.currentTimeMillis();
			LOGGER.debug("[BollDatiWorkingApiServiceImpl::insertAvvisoUsoBo] QueryExecutionTime: " + (stop - start));
		} catch (DAOException e) {
			LOGGER.error("[BollDatiWorkingApiServiceImpl::insertAvvisoUsoBo] inserimento Avviso Uso :" + avvisoUso);

		}
		logStep(riscossioneBollData.getIdElabora(), PASSO_INSERT_AVVISO_USO, 0,
				FASE_EMISSIONE + " - Step 5 - Inserimento usi: NAP: " + ret.getNap() + " - Annualita: "
						+ annualita.getIdAnnualitaSd() + " - Anno: " + annualita.getAnno() + " - Uso: "
						+ riscUso.getCanoneUso().getCodTipoUso(),
				attore, COD_FASE_EMISBO);

		LOGGER.debug("[BollDatiWorkingApiServiceImpl::insertAvvisoUsoBo] END nap: " + ret.getNap());
		return ret.getNap();
	}

	private String getYearFromDate(Date date) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		int anno = calendar.get(Calendar.YEAR);
		return "" + anno;
	}

	private String getModalitaInvio(String codTipoInvio) {
		switch (codTipoInvio) {
		case "C":
			return "CARTA";
		case "P":
			return "PEC";
		case "E":
			return "EMAIL";
		default:
			return "";
		}
	}

	private String getModalitaInvioBS(String codTipoInvio) {
		// NB per bollettazione speciale si inviano solo le PEC. Se tipo invio EMAIL si
		// trattano come le CARTA
		switch (codTipoInvio) {
		case "C":
			return "CARTA";
		case "P":
			return "PEC";
		case "E":
			return "CARTA";
		default:
			return "";
		}
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
			LOGGER.error("[BollDatiWorkingApiServiceImpl::logStep] (Exception) ", e);
		}
		LOGGER.debug(note);
	}

	@Override
	public Response deleteWorkingForNap(String nap, Long idElabora, Long idSpedizione, String codFase,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[BollDatiWorkingApiServiceImpl::deleteWorkingForNap] BEGIN");
		attore = "riscabatchspec";
		BollResultDTO bsResult = new BollResultDTO();

		try {
			// Delete degli avvisi
			avvisoUsoDAO.deleteAvvisoUsoWorkingByNap(nap);
			avvisoAnnualitaDAO.deleteAvvisoAnnualitaWorkingByNap(nap);
			avvisoDatiAmminDAO.deleteAvvisoDatiAmminWorkingByNap(nap);
			avvisoDatiTitolareDAO.deleteAvvisoDatiTitolarenWorkingByNap(nap);

			List<StatoDebitorioDTO> statiDebitoriWork = statoDebitorioDAO.loadStatoDebitorioWorkingByNap(nap);
			for (StatoDebitorioDTO statoDebitorioW : statiDebitoriWork) {

				if (codFase.equals(COD_FASE_ANNULLABO) || codFase.equals(COD_FASE_CONFBO_CONCLUSIONE)) {
					List<AnnualitaSdDTO> annualitaSdList = annualitaSdDAO
							.loadAnnualitaSdWorking(statoDebitorioW.getIdStatoDebitorio());
					for (AnnualitaSdDTO annualitaSd : annualitaSdList) {
						List<AnnualitaUsoSdDTO> listAnnualitaUsoSd = annualitaUsoSdDAO
								.loadAnnualitaUsoSdWorkingByIdAnnualitaSd(annualitaSd.getIdAnnualitaSd());
						for (AnnualitaUsoSdDTO annualitaUsoSd : listAnnualitaUsoSd) {
							annualitaUsoSdRaDAO.deleteAnnualitaUsoSdRaWorkingByIdAnnualitaUsoSd(
									annualitaUsoSd.getIdAnnualitaUsoSd());
						}
						annualitaUsoSdDAO.deleteAnnualitaUsoSdWorkingByIdAnnualitaSd(annualitaSd.getIdAnnualitaSd());
						annualitaSdDAO.deleteAnnualitaSdWorkingById(annualitaSd.getIdAnnualitaSd());
					}
				}

				rataSdDAO.deleteRataSdWorkingByIdStatoDebitorio(statoDebitorioW.getIdStatoDebitorio());
				rimborsoSdUtilizzatoDAO.deleteRimborsoSdUtilizzatoWorkingByIdStatoDebitorio(
						statoDebitorioW.getIdStatoDebitorio(), idElabora);
				statoDebitorioDAO.deleteStatoDebitorioWorkingByIdStatoDebitorio(statoDebitorioW.getIdStatoDebitorio());

			}

			avvisoPagamentoDAO.deleteAvvisoPagamentoWorkingByNap(nap);

			if (codFase.equals(COD_FASE_CONFBS_CONCLUSIONE) || codFase.equals(COD_FASE_CONFBG_CONCLUSIONE)
					|| codFase.equals(COD_FASE_CONFBO_CONCLUSIONE)) {
				iuvDAO.updateStatoIuvInseritoByNap(nap);

				logStep(idElabora, PASSO_SET_IUV, 0,
						FASE_CONFERMA_CONCLUSIONE + " - Step 2 - – Aggiornamento IUV OK - Nap: " + nap, attore,
						codFase);
			}
		} catch (DAOException | SQLException e) {
			bsResult.setStatus("KO");
			bsResult.setStepError(PASSO_SVUOTA_WORKING);
			bsResult.setErrorMessage(
					FASE_ANNULLA_EMISSIONE + " - Step 2 - Cancellazione Tabelle di WORKING KO Per NAP: " + nap);
			return Response.ok(bsResult).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		}

		bsResult.setStatus("OK");
		LOGGER.debug("[BollDatiWorkingApiServiceImpl::deleteWorkingForNap] END");
		return Response.ok(bsResult).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response deleteWorking(Long idElabora, Long idSpedizione, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[BollDatiWorkingApiServiceImpl::deleteWorking] BEGIN");
		attore = "riscabatchspec";
		BollResultDTO bsResult = new BollResultDTO();

		try {
			rimborsoDAO.deleteRimborsoUpdWorkingByIdElabora(idElabora);
			statoDebitorioDAO.deleteStatoDebitorioUpdWorkingByIdElabora(idElabora);
			spedizioneDAO.deleteSpedizioneByPk(idSpedizione, true);

		} catch (DAOException e) {
			bsResult.setStatus("KO");
			bsResult.setStepError(PASSO_SVUOTA_WORKING);
			bsResult.setErrorMessage(FASE_ANNULLA_EMISSIONE + " - Step 2 - Cancellazione Tabelle di WORKING KO ");
			return Response.ok(bsResult).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		} catch (Exception e) {
			LOGGER.error("[BollDatiWorkingApiServiceImpl::deleteWorking] (Exception) ", e);
		}

		bsResult.setStatus("OK");
		LOGGER.debug("[BollDatiWorkingApiServiceImpl::deleteWorking] END");
		return Response.ok(bsResult).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();

	}

}

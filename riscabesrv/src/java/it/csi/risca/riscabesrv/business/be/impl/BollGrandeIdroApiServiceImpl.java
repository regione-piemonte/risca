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

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import it.csi.risca.riscabesrv.business.be.BollGrandeIdroApi;
import it.csi.risca.riscabesrv.business.be.impl.dao.AnnualitaSdDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.AnnualitaUsoSdDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.ComponentiDtDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RataSdDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RegistroElaboraDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.StatoDebitorioDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.TipoUsoDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.TipoUsoRegolaDAO;
import it.csi.risca.riscabesrv.dto.AnnualitaSdDTO;
import it.csi.risca.riscabesrv.dto.AnnualitaUsoSdDTO;
import it.csi.risca.riscabesrv.dto.BollGrandeIdroData;
import it.csi.risca.riscabesrv.dto.BollResultDTO;
import it.csi.risca.riscabesrv.dto.ComponenteDtDTO;
import it.csi.risca.riscabesrv.dto.ProvvedimentoDTO;
import it.csi.risca.riscabesrv.dto.RataSdDTO;
import it.csi.risca.riscabesrv.dto.RecapitiExtendedDTO;
import it.csi.risca.riscabesrv.dto.RegistroElaboraDTO;
import it.csi.risca.riscabesrv.dto.RiscossioneDTO;
import it.csi.risca.riscabesrv.dto.StatoDebitorioDTO;
import it.csi.risca.riscabesrv.dto.StatoDebitorioExtendedDTO;
import it.csi.risca.riscabesrv.dto.TipoUsoExtendedDTO;
import it.csi.risca.riscabesrv.dto.TipoUsoRegolaDTO;
import it.csi.risca.riscabesrv.dto.ambiente.DatiTecniciAmbienteDTO;
import it.csi.risca.riscabesrv.dto.ambiente.TipoUsoDatoTecnicoDTO;
import it.csi.risca.riscabesrv.util.BollUtils;
import it.csi.risca.riscabesrv.util.Constants;

@Component
public class BollGrandeIdroApiServiceImpl extends BaseApiServiceImpl implements BollGrandeIdroApi {

	private static final String IDENTITY = "identity";
	private static Logger LOGGER = Logger.getLogger(Constants.LOGGER_NAME);

	public static final String PASSO_ESTRAE_ENERG_PROD = "ESTRAE_ENERG_PROD";
	public static final String PASSO_INS_ENERG_PROD = "INS_ENERG_PROD";

	public static final String COD_TIPO_USO_GRANDE_IDROELETTRICO = "GRANDE_IDROELETTRICO";
	public static final String COD_TIPO_USO_GRANDE_IDROELETTRICO_AGG = "GRANDE_IDRO_AGG";
	public static final String COD_TIPO_USO_MONET_ENERGIA_GRAT = "MONET_ENERGIA_GRAT";
	public static final String COD_TIPO_USO_GRANDE_IDRO_VAR = "GRANDE_IDRO_VAR";

	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final String DATE_FORMAT_SPED = "dd/MM/yyyy";

	public static final String FASE = "Richiesta-PredGrandeIdro";
	public static final String COD_FASE = "RICHIESTAPG";

	public static final String DES_USI_GI = "GRANDE IDROELETTRICO - CANONE VARIABILE, GRANDE IDROELETTRICO - MONETIZZAZIONE ENERGIA GRATUITA";

	public static final String COD_TIPO_PROVVEDIMENTO_IST_RINNOVO = "IST_RINNOVO";
	public static final String COD_TIPO_PROVVEDIMENTO_IST_SANATORIA = "IST_SANATORIA";
	public static final String COD_TIPO_PROVVEDIMENTO_AUT_PROVVISORIA = "AUT_PROVVISORIA";

	private String attore;

	@Autowired
	private RegistroElaboraDAO registroElaboraDAO;

	@Autowired
	private StatoDebitorioDAO statoDebitorioDAO;

	@Autowired
	private RataSdDAO rataSdDAO;

	@Autowired
	private TipoUsoDAO tipoUsoDAO;

	@Autowired
	private ComponentiDtDAO componentiDtDAO;

	@Autowired
	private AnnualitaSdDAO annualitaSdDAO;

	@Autowired
	private AnnualitaUsoSdDAO annualitaUsoSdDAO;

	@Autowired
	private TipoUsoRegolaDAO tipoUsoRegolaDAO;

	@Override
	public Response elaboraUtenza(BollGrandeIdroData grandeIdroData, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[BollGrandeIdroApiServiceImpl::elaboraUtenza] BEGIN");
		BollResultDTO result = new BollResultDTO();
		attore = "riscabatchpgi";
		RiscossioneDTO riscossione = grandeIdroData.getRiscossione();
		Long idElabora = grandeIdroData.getElabora().getIdElabora();
		Long idAmbito = grandeIdroData.getElabora().getAmbito().getIdAmbito();
		String anno = grandeIdroData.getAnno();

		try {
			BigDecimal totEnergProdAnno = grandeIdroData.getTotEnergProdAnno();
			BigDecimal totRicaviAnno = grandeIdroData.getTotRicaviAnno();
			boolean flgUsoGiVariabile = grandeIdroData.getFlgUsoGiVariabile().equals("1") ? true : false;
			boolean flgUsoGiMonEneGra = grandeIdroData.getFlgUsoGiMonEneGra().equals("1") ? true : false;
			String jsonDt = grandeIdroData.getRiscossione().getJsonDt();

			Integer percQuotaVar = 0;
			BigDecimal coeffEnergGrat = BigDecimal.ZERO;
			BigDecimal pnmPerEnergGrat = BigDecimal.ZERO;
			BigDecimal extraProfitti = BigDecimal.ZERO;
			BigDecimal quotaEnergGrat = BigDecimal.ZERO;

			BigDecimal prezzoMedOraPond = BigDecimal.ZERO;
			BigDecimal monetEnergGrat = BigDecimal.ZERO;
			BigDecimal totRicaviNormaliz = BigDecimal.ZERO;
			BigDecimal canoneVariabile = BigDecimal.ZERO;

			DatiTecniciAmbienteDTO datiTecnici = BollUtils.getDatiTecniciFromJsonDt(jsonDt);
			if (datiTecnici.getUsi().containsKey(COD_TIPO_USO_GRANDE_IDROELETTRICO)) {
				// 3.4 memorizzare i dati TECNICI presenti nel json_dt
				TipoUsoDatoTecnicoDTO uso = datiTecnici.getUsi().get(COD_TIPO_USO_GRANDE_IDROELETTRICO);
				percQuotaVar = uso.getPercQuotaVar();
				coeffEnergGrat = uso.getCoeffEnergGrat();
				pnmPerEnergGrat = uso.getPnmPerEnergGrat();
				extraProfitti = uso.getExtraProfitti();
				if (extraProfitti == null) {
					extraProfitti = BigDecimal.ZERO;
				}
				quotaEnergGrat = coeffEnergGrat.multiply(pnmPerEnergGrat).divide(new BigDecimal(1000), 6,
						RoundingMode.HALF_UP);
				
				// Calcoli dello step 3.5

				// Calcolo in MWh
				if (totRicaviAnno.compareTo(BigDecimal.ZERO) != 0 && totEnergProdAnno.compareTo(BigDecimal.ZERO) != 0) {
					prezzoMedOraPond = totRicaviAnno.divide(totEnergProdAnno, 2, RoundingMode.HALF_UP);
				} else {
					prezzoMedOraPond = BigDecimal.ZERO;
				}

				// Uso 3600 GRANDE IDROELETTRICO - MONETIZZAZIONE ENERGIA GRATUITA
				if (flgUsoGiMonEneGra) {
					monetEnergGrat = quotaEnergGrat.multiply(prezzoMedOraPond).setScale(2, RoundingMode.HALF_UP);
				} else {
					monetEnergGrat = BigDecimal.ZERO;
				}
				totRicaviNormaliz = totRicaviAnno.subtract(monetEnergGrat).subtract(extraProfitti);
				// se risultasse negativo, deve diventare = 0
				if (totRicaviNormaliz.compareTo(BigDecimal.ZERO) == -1) {
					totRicaviNormaliz = BigDecimal.ZERO;
				}
				// Gerica arrotondava a 2 decimali
				totRicaviNormaliz = totRicaviNormaliz.setScale(2, RoundingMode.HALF_UP);
				
				// Uso 3500 GRANDE IDROELETTRICO - CANONE VARIABILE
				if (flgUsoGiVariabile) {
					canoneVariabile = totRicaviNormaliz.multiply(new BigDecimal(percQuotaVar))
							.divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
					// se risultasse negativo, deve diventare = 0
					if (canoneVariabile.compareTo(BigDecimal.ZERO) == -1) {
						canoneVariabile = BigDecimal.ZERO;
					}
				} else {
					canoneVariabile = BigDecimal.ZERO;
				}

				logStep(idElabora, PASSO_ESTRAE_ENERG_PROD, 0, FASE
						+ " - Step 3 - Estrazione dati energia prodotta per utenza: " + riscossione.getCodRiscossione(),
						attore, COD_FASE);

				// 3.6 Inserimento DATI per l'utenza

				// Inserimento dello stato debitorio
				Long idStatoDebitorio = insertStatoDebitorio(riscossione, anno, idAmbito);
				if (idStatoDebitorio != null) {
					// Inserimento della rata
					Long idRataSd = insertRataSd(idStatoDebitorio, monetEnergGrat, canoneVariabile);
					if (idRataSd != null) {
						TipoUsoExtendedDTO tipoUsoGiVar = tipoUsoDAO
								.loadTipoUsoByIdTipoUsoOrCodTipoUso(COD_TIPO_USO_GRANDE_IDRO_VAR);
						TipoUsoExtendedDTO tipoUsoMeg = tipoUsoDAO
								.loadTipoUsoByIdTipoUsoOrCodTipoUso(COD_TIPO_USO_MONET_ENERGIA_GRAT);

						// Inserimento annualita_sd
						Long idAnnualitaSd = insertAnnualitaSd(idAmbito, idStatoDebitorio, monetEnergGrat,
								canoneVariabile, anno, riscossione, flgUsoGiVariabile, flgUsoGiMonEneGra,
								totRicaviNormaliz, percQuotaVar, coeffEnergGrat, pnmPerEnergGrat, totEnergProdAnno,
								totRicaviAnno, prezzoMedOraPond, extraProfitti, tipoUsoGiVar, tipoUsoMeg,
								quotaEnergGrat);

						if (idAnnualitaSd != null) {
							boolean ret = insertAnnualitaUsoSd(flgUsoGiVariabile, flgUsoGiMonEneGra, idAnnualitaSd,
									tipoUsoGiVar, tipoUsoMeg, monetEnergGrat, canoneVariabile, anno);
							if (ret) {
								logStep(idElabora, PASSO_INS_ENERG_PROD, 0,
										FASE + " - Step 3 - Inserimento dati energia prodotta per utenza : "
												+ riscossione.getCodRiscossione(),
										attore, COD_FASE);
								result.setStatus("OK");
							} else {
								logStep(idElabora, PASSO_INS_ENERG_PROD, 1,
										FASE + " - Step 3 - Errore Inserimento annualita usi per : "
												+ riscossione.getCodRiscossione(),
										attore, COD_FASE);
								result.setStatus("KO");
							}
						} else {
							logStep(idElabora, PASSO_INS_ENERG_PROD, 1,
									FASE + " - Step 3 - Errore Inserimento annualita per : "
											+ riscossione.getCodRiscossione(),
									attore, COD_FASE);
							result.setStatus("KO");
						}
					} else {
						logStep(idElabora, PASSO_INS_ENERG_PROD, 1,
								FASE + " - Step 3 - Errore Inserimento rata per : "
										+ riscossione.getCodRiscossione(),
								attore, COD_FASE);
						result.setStatus("KO");
					}
				} else {
					logStep(idElabora, PASSO_INS_ENERG_PROD, 1,
							FASE + " - Step 3 - Errore Inserimento stato debitorio per : "
									+ riscossione.getCodRiscossione(),
							attore, COD_FASE);
					result.setStatus("KO");
				}

			} else {
				// Errore manca uso GRANDE_IDROELETTRICO
				LOGGER.error(
						"[BollGrandeIdroApiServiceImpl::elaboraUtenza] manca uso GRANDE_IDROELETTRICO su json_dt per utenza:  "
								+ riscossione.getCodRiscossione());
				logStep(idElabora, PASSO_ESTRAE_ENERG_PROD, 1,
						FASE + " - Step 3 - manca uso GRANDE_IDROELETTRICO su json_dt per utenza: "
								+ riscossione.getCodRiscossione(),
						attore, COD_FASE);
				result.setStatus("KO");
			}

		} catch (Exception e) {
			LOGGER.error("[BollGrandeIdroApiServiceImpl::elaboraUtenza] Error  " + e);
			result.setStatus("KO");
		}

		LOGGER.debug("[BollGrandeIdroApiServiceImpl::elaboraUtenza] END");
		return Response.ok(result).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	private Long insertStatoDebitorio(RiscossioneDTO riscossione, String anno, Long idAmbito) {
		LOGGER.debug("[BollGrandeIdroApiServiceImpl::insertStatoDebitorio] BEGIN");
		StatoDebitorioExtendedDTO statoDebitorio = new StatoDebitorioExtendedDTO();

		statoDebitorio.setIdRiscossione(riscossione.getIdRiscossione());
		statoDebitorio.setIdSoggetto(riscossione.getSoggetto().getIdSoggetto());
		statoDebitorio.setIdGruppoSoggetto(riscossione.getIdGruppoSoggetto());
		statoDebitorio.setIdRecapito(getIdRecapito(riscossione));
		statoDebitorio.setDataUltimaModifica(new Date());
		statoDebitorio.setDesUsi(DES_USI_GI);
		int annoSd = Integer.valueOf(anno) + 1;
		statoDebitorio.setDescPeriodoPagamento("" + annoSd);
		statoDebitorio.setFlgInvioSpeciale(2);
		statoDebitorio.setGestAttoreIns(attore);
		statoDebitorio.setGestAttoreUpd(attore);

		// Cercare l'istanza piu' recente
		ProvvedimentoDTO provv = getProvvRecente(riscossione);
		if (provv != null) {
			SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
			statoDebitorio.setNumTitolo(provv.getNumTitolo());
			if (provv.getDataProvvedimento() != null) {
				try {
					statoDebitorio.setDataProvvedimento(df.parse(provv.getDataProvvedimento()));
				} catch (ParseException e) {
					LOGGER.error(
							"[BollGrandeIdroApiServiceImpl::insertStatoDebitorio] Errore parsing data provvedimento: "
									+ provv.getDataProvvedimento());
					return null;
				}
			}
			statoDebitorio.setTipoTitolo(provv.getTipoTitoloExtendedDTO().getDesTipoTitolo());
		}

		StatoDebitorioDTO ret = null;
		try {
			ret = statoDebitorioDAO.saveStatoDebitorio(statoDebitorio, idAmbito);
		} catch (Exception e) {
			LOGGER.error(
					"[BollGrandeIdroApiServiceImpl::insertStatoDebitorio] Inserimento SD fallito: " + statoDebitorio);
			return null;
		}

		LOGGER.debug("[BollGrandeIdroApiServiceImpl::insertStatoDebitorio] END idStatoDebitorio: "
				+ ret.getIdStatoDebitorio());
		return ret.getIdStatoDebitorio();
	}

	private Long insertRataSd(Long idStatoDebitorio, BigDecimal monetEnergGrat, BigDecimal canoneVariabile) {
		LOGGER.debug("[BollGrandeIdroApiServiceImpl::insertRataSd] BEGIN");
		RataSdDTO rataSd = new RataSdDTO();
		rataSd.setIdStatoDebitorio(idStatoDebitorio);
		// Troncare canoneVariabile a intero
		BigDecimal canoneVar = canoneVariabile.setScale(0, RoundingMode.DOWN);
		BigDecimal canoneDovuto = monetEnergGrat.add(canoneVar);
		rataSd.setCanoneDovuto(canoneDovuto);
		Calendar c = new GregorianCalendar();
		int anno = c.get(GregorianCalendar.YEAR);
		rataSd.setDataScadenzaPagamento(anno + "-07-31");
		rataSd.setGestAttoreIns(attore);
		rataSd.setGestAttoreUpd(attore);

		RataSdDTO ret = null;
		try {
			ret = rataSdDAO.saveRataSd(rataSd);
		} catch (Exception e) {
			LOGGER.error("[BollGrandeIdroApiServiceImpl::insertRataSd] Inserimento fallito: " + rataSd);
			return null;
		}
		LOGGER.debug("[BollGrandeIdroApiServiceImpl::insertRataSd] END idRataSd: " + ret.getIdRataSd());
		return ret.getIdRataSd();
	}

	private Long insertAnnualitaSd(Long idAmbito, Long idStatoDebitorio, BigDecimal monetEnergGrat,
			BigDecimal canoneVariabile, String anno, RiscossioneDTO riscossione, boolean flgUsoGiVariabile,
			boolean flgUsoGiMonEneGra, BigDecimal totRicaviNormaliz, Integer percQuotaVar, BigDecimal coeffEnergGrat,
			BigDecimal pnmPerEnergGrat, BigDecimal totEnergProdAnno, BigDecimal totRicaviAnno,
			BigDecimal prezzoMedOraPond, BigDecimal extraProfitti, TipoUsoExtendedDTO tipoUsoGiVar,
			TipoUsoExtendedDTO tipoUsoMeg, BigDecimal quotaEnergGrat) {
		LOGGER.debug("[BollGrandeIdroApiServiceImpl::insertAnnualitaSd] BEGIN");
		AnnualitaSdDTO annualitaSd = new AnnualitaSdDTO();
		AnnualitaSdDTO ret = null;
		try {
			annualitaSd.setIdStatoDebitorio(idStatoDebitorio);

			// Troncare canoneVariabile a intero
			BigDecimal canoneVar = canoneVariabile.setScale(0, RoundingMode.DOWN);
			BigDecimal canoneDovuto = monetEnergGrat.add(canoneVar);
			annualitaSd.setCanoneAnnuo(canoneDovuto);

			int annoSd = Integer.valueOf(anno) + 1;
			annualitaSd.setAnno(Integer.valueOf(annoSd));

			DatiTecniciAmbienteDTO datiTecnici = BollUtils.getDatiTecniciFromJsonDt(riscossione.getJsonDt());
			datiTecnici = aggiornaDatiTecniciPerGrandeIdro(datiTecnici, flgUsoGiVariabile, flgUsoGiMonEneGra,
					totRicaviNormaliz, percQuotaVar, coeffEnergGrat, pnmPerEnergGrat, totEnergProdAnno, totRicaviAnno,
					prezzoMedOraPond, extraProfitti, tipoUsoGiVar, tipoUsoMeg, quotaEnergGrat);
			annualitaSd.setJsonDtAnnualitaSd(datiTecnici.toJsonString());

			String annoRif = anno + "-01-01";
			ComponenteDtDTO compDTO = componentiDtDAO.loadComponenteDt(idAmbito, "GESTIONE", annoRif);
			annualitaSd.setIdComponenteDt(compDTO.getIdComponenteDt().intValue());
			annualitaSd.setGestAttoreIns(attore);
			annualitaSd.setGestAttoreUpd(attore);

			ret = annualitaSdDAO.insertAnnualitaSd(annualitaSd, idAmbito);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("[BollGrandeIdroApiServiceImpl::insertAnnualitaSd] Inserimento fallito: " + annualitaSd);
			return null;
		}
		LOGGER.debug("[BollGrandeIdroApiServiceImpl::insertAnnualitaSd] END idAnnualitaSd: " + ret.getIdAnnualitaSd());
		return ret.getIdAnnualitaSd();
	}

	private DatiTecniciAmbienteDTO aggiornaDatiTecniciPerGrandeIdro(DatiTecniciAmbienteDTO datiTecnici,
			boolean flgUsoGiVariabile, boolean flgUsoGiMonEneGra, BigDecimal totRicaviNormaliz, Integer percQuotaVar,
			BigDecimal coeffEnergGrat, BigDecimal pnmPerEnergGrat, BigDecimal totEnergProdAnno,
			BigDecimal totRicaviAnno, BigDecimal prezzoMedOraPond, BigDecimal extraProfitti,
			TipoUsoExtendedDTO tipoUsoGiVar, TipoUsoExtendedDTO tipoUsoMeg, BigDecimal quotaEnergGrat)
			throws JsonParseException, JsonMappingException, JsonProcessingException, IOException {

		// Elimino gli altri usi presenti perhe' il json_dt di questa annualita deve
		// contenere solo questi 2 nuovi usi
		datiTecnici.getUsi().clear();

		if (flgUsoGiVariabile) {
			// Uso 3500 GRANDE IDROELETTRICO - CANONE VARIABILE

			TipoUsoDatoTecnicoDTO tipoUsoGI_VAR = new TipoUsoDatoTecnicoDTO();
			tipoUsoGI_VAR.setTipoUsoLegge(tipoUsoGiVar.getDesTipouso());
			tipoUsoGI_VAR.setIdTipoUsoLegge(tipoUsoGiVar.getIdTipoUso());
			tipoUsoGI_VAR.setQuantita(totRicaviNormaliz);
			tipoUsoGI_VAR.setUnitaDiMisura(tipoUsoGiVar.getUnitaMisura().getSiglaUnitaMisura());
			tipoUsoGI_VAR.setQuantitaFaldaProfonda(BigDecimal.ZERO);
			tipoUsoGI_VAR.setPercFaldaProfonda(BigDecimal.ZERO);
			tipoUsoGI_VAR.setQuantitaFaldaSuperficiale(BigDecimal.ZERO);
			tipoUsoGI_VAR.setPercQuantitaFaldaSuperficiale(BigDecimal.ZERO);
			tipoUsoGI_VAR.setPercQuotaVar(percQuotaVar);
			tipoUsoGI_VAR.setCoeffEnergGrat(coeffEnergGrat.setScale(2, RoundingMode.HALF_UP));
			tipoUsoGI_VAR.setPnmPerEnergGrat(pnmPerEnergGrat);
			tipoUsoGI_VAR.setToTEnergProd(totEnergProdAnno.setScale(2, RoundingMode.HALF_UP));
			tipoUsoGI_VAR.setTotRicaviAnno(totRicaviAnno.setScale(2, RoundingMode.HALF_UP));
			tipoUsoGI_VAR.setPrezzoMedOraPond(prezzoMedOraPond.setScale(2, RoundingMode.HALF_UP));
			tipoUsoGI_VAR.setExtraProfitti(extraProfitti.setScale(2, RoundingMode.HALF_UP));

			// Aggiungo un uso
			datiTecnici.getUsi().put(tipoUsoGiVar.getCodTipouso(), tipoUsoGI_VAR);
		}

		if (flgUsoGiMonEneGra) {
			// Uso 3600 GRANDE IDROELETTRICO - MONETIZZAZIONE ENERGIA GRATUITA

			TipoUsoDatoTecnicoDTO tipoUsoGI_MEG = new TipoUsoDatoTecnicoDTO();
			tipoUsoGI_MEG.setTipoUsoLegge(tipoUsoMeg.getDesTipouso());
			tipoUsoGI_MEG.setIdTipoUsoLegge(tipoUsoMeg.getIdTipoUso());
			tipoUsoGI_MEG.setQuantita(quotaEnergGrat);
			tipoUsoGI_MEG.setUnitaDiMisura(tipoUsoMeg.getUnitaMisura().getSiglaUnitaMisura());
			tipoUsoGI_MEG.setQuantitaFaldaProfonda(BigDecimal.ZERO);
			tipoUsoGI_MEG.setPercFaldaProfonda(BigDecimal.ZERO);
			tipoUsoGI_MEG.setQuantitaFaldaSuperficiale(BigDecimal.ZERO);
			tipoUsoGI_MEG.setPercQuantitaFaldaSuperficiale(BigDecimal.ZERO);
			tipoUsoGI_MEG.setPercQuotaVar(percQuotaVar);
			tipoUsoGI_MEG.setCoeffEnergGrat(coeffEnergGrat.setScale(2, RoundingMode.HALF_UP));
			tipoUsoGI_MEG.setPnmPerEnergGrat(pnmPerEnergGrat);
			tipoUsoGI_MEG.setToTEnergProd(totEnergProdAnno.setScale(2, RoundingMode.HALF_UP));
			tipoUsoGI_MEG.setTotRicaviAnno(totRicaviAnno.setScale(2, RoundingMode.HALF_UP));
			tipoUsoGI_MEG.setPrezzoMedOraPond(prezzoMedOraPond.setScale(2, RoundingMode.HALF_UP));
			tipoUsoGI_MEG.setExtraProfitti(extraProfitti.setScale(2, RoundingMode.HALF_UP));

			// Aggiungo un uso
			datiTecnici.getUsi().put(tipoUsoMeg.getCodTipouso(), tipoUsoGI_MEG);
		}

		return datiTecnici;
	}

	private boolean insertAnnualitaUsoSd(boolean flgUsoGiVariabile, boolean flgUsoGiMonEneGra, Long idAnnualitaSd,
			TipoUsoExtendedDTO tipoUsoGiVar, TipoUsoExtendedDTO tipoUsoMeg, BigDecimal monetEnergGrat,
			BigDecimal canoneVariabile, String anno) {
		String dataRiferimento = anno + "-01-01";
		if (flgUsoGiVariabile) {
			TipoUsoRegolaDTO regola = tipoUsoRegolaDAO.loadTipoUsoRegolaByCodTipoUso(dataRiferimento,
					COD_TIPO_USO_GRANDE_IDRO_VAR);
			if (regola == null) {
				LOGGER.error("[BollGrandeIdroApiServiceImpl::insertAnnualitaUsoSd] TipoUsoRegola non trovata per "
						+ COD_TIPO_USO_GRANDE_IDRO_VAR);
				return false;
			}
			BigDecimal canoneUnitario = getCanoneUnitario(regola.getJsonRegola());
			AnnualitaUsoSdDTO annualitaUsoSdDto = new AnnualitaUsoSdDTO();
			annualitaUsoSdDto.setIdAnnualitaSd(idAnnualitaSd);
			annualitaUsoSdDto.setTipoUso(tipoUsoGiVar);
			annualitaUsoSdDto.setCanoneUso(canoneVariabile);
			annualitaUsoSdDto.setCanoneUnitario(canoneUnitario);
			annualitaUsoSdDto.setGestAttoreIns(attore);
			annualitaUsoSdDto.setGestAttoreUpd(attore);

			try {
				annualitaUsoSdDto = annualitaUsoSdDAO.saveAnnualitaUsoSdDTO(annualitaUsoSdDto);
			} catch (Exception e) {
				LOGGER.error(
						"[BollGrandeIdroApiServiceImpl::insertAnnualitaUsoSd] Insert Usi in working RISCA_W_ANNUALITA_USO_SD fallito");
				return false;
			}
		}

		if (flgUsoGiMonEneGra) {
			TipoUsoRegolaDTO regola = tipoUsoRegolaDAO.loadTipoUsoRegolaByCodTipoUso(dataRiferimento,
					COD_TIPO_USO_MONET_ENERGIA_GRAT);
			if (regola == null) {
				LOGGER.error("[BollGrandeIdroApiServiceImpl::insertAnnualitaUsoSd] TipoUsoRegola non trovata per "
						+ COD_TIPO_USO_MONET_ENERGIA_GRAT);
				return false;
			}
			BigDecimal canoneUnitario = getCanoneUnitario(regola.getJsonRegola());
			AnnualitaUsoSdDTO annualitaUsoSdDto = new AnnualitaUsoSdDTO();
			annualitaUsoSdDto.setIdAnnualitaSd(idAnnualitaSd);
			annualitaUsoSdDto.setTipoUso(tipoUsoMeg);
			annualitaUsoSdDto.setCanoneUso(monetEnergGrat);
			annualitaUsoSdDto.setCanoneUnitario(canoneUnitario);
			annualitaUsoSdDto.setGestAttoreIns(attore);
			annualitaUsoSdDto.setGestAttoreUpd(attore);

			try {
				annualitaUsoSdDto = annualitaUsoSdDAO.saveAnnualitaUsoSdDTO(annualitaUsoSdDto);
			} catch (Exception e) {
				LOGGER.error(
						"[BollGrandeIdroApiServiceImpl::insertAnnualitaUsoSd] Insert Usi in working RISCA_W_ANNUALITA_USO_SD fallito");
				return false;
			}
		}

		return true;
	}

	private BigDecimal getCanoneUnitario(String jsonRegola) {
		// TODO verificare se vale per tutti gli ambiti
		final JSONObject obj = new JSONObject(jsonRegola);
		String canoneUnitario = obj.get("canone_percentuale").toString();
		return new BigDecimal(canoneUnitario);
	}

	private Long getIdRecapito(RiscossioneDTO riscossione) {
		Long idRecapitoPric = null;
		Long idRecapitoAlt = null;
		for (RecapitiExtendedDTO recapito : riscossione.getRecapitiRiscossione()) {
			if (recapito.getTipoRecapito().getCodTipoRecapito().equalsIgnoreCase("P")) {
				idRecapitoPric = recapito.getIdRecapito();
			} else if (recapito.getTipoRecapito().getCodTipoRecapito().equalsIgnoreCase("A")) {
				idRecapitoAlt = recapito.getIdRecapito();
			}
		}
		if (idRecapitoAlt != null) {
			return idRecapitoAlt;
		} else {
			return idRecapitoPric;
		}
	}

	private ProvvedimentoDTO getProvvRecente(RiscossioneDTO riscossione) {
		List<ProvvedimentoDTO> provv = estraiProvvedimento(riscossione.getProvvedimento());
		if (provv != null && provv.size() > 0) {
			return provv.get(0);
		} else {
			return null;
		}
	}

	private List<ProvvedimentoDTO> estraiProvvedimento(List<ProvvedimentoDTO> provvedimentiIstanze) {
		SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
		List<ProvvedimentoDTO> filteredList = provvedimentiIstanze.stream()
				.filter(provvIst -> provvIst.getTipiProvvedimentoExtendedDTO().getFlgIstanza().equals("0"))
				.collect(Collectors.toList());

		if (filteredList != null && filteredList.size() == 1) {
			return filteredList;
		}

		List<ProvvedimentoDTO> provvMaxDate = filteredList.stream().reduce((t, u) -> {
			try {
				return df.parse(t.getDataProvvedimento()).after(df.parse(u.getDataProvvedimento())) ? t : u;
			} catch (ParseException e) {
				return null;
			}
		}).stream().collect(Collectors.toList());

		return provvMaxDate;
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

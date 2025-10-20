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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.epay.epaywso.enti2epaywso.types.ComponenteImportoType;
import it.csi.epay.epaywso.enti2epaywso.types.InserisciListaDiCaricoRequest;
import it.csi.epay.epaywso.enti2epaywso.types.ListaDiCarico;
import it.csi.epay.epaywso.enti2epaywso.types.ListaDiCarico.PosizioniDaInserire;
import it.csi.epay.epaywso.enti2epaywso.types.PosizioneDaInserireType;
import it.csi.epay.epaywso.enti2epaywso.types.PosizioneDaInserireType.ComponentiImporto;
import it.csi.epay.epaywso.enti2epaywso.types.TestataListaCarico;
import it.csi.epay.epaywso.types.PersonaFisicaType;
import it.csi.epay.epaywso.types.PersonaGiuridicaType;
import it.csi.epay.epaywso.types.ResponseType;
import it.csi.epay.epaywso.types.SoggettoType;
import it.csi.risca.riscabesrv.business.be.RichiestaIUVApi;
import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.helper.pagopa.PagopaException;
import it.csi.risca.riscabesrv.business.be.helper.pagopa.PagopaServiceHelper;
import it.csi.risca.riscabesrv.business.be.impl.dao.AmbitiConfigDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.AmbitiDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.EntePagopaDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.LottoDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.OutputDatiDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.PagopaListaCarIuvDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.PagopaScompRichIuvDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RegistroElaboraDAO;
import it.csi.risca.riscabesrv.dto.AmbitoConfigDTO;
import it.csi.risca.riscabesrv.dto.AmbitoDTO;
import it.csi.risca.riscabesrv.dto.BollResultDTO;
import it.csi.risca.riscabesrv.dto.BollResultIUVDTO;
import it.csi.risca.riscabesrv.dto.EntePagopaDTO;
import it.csi.risca.riscabesrv.dto.InvioLottoDTO;
import it.csi.risca.riscabesrv.dto.LottoDTO;
import it.csi.risca.riscabesrv.dto.OutputDatiDTO;
import it.csi.risca.riscabesrv.dto.PagopaListaCarIuvDTO;
import it.csi.risca.riscabesrv.dto.PagopaPosizioniDebitorieDTO;
import it.csi.risca.riscabesrv.dto.PagopaScompRichIuvDTO;
import it.csi.risca.riscabesrv.dto.RegistroElaboraDTO;
import it.csi.risca.riscabesrv.dto.ScomposizioneRichiestaIuvDTO;
import it.csi.risca.riscabesrv.util.BollUtils;
import it.csi.risca.riscabesrv.util.Constants;

@Component
public class RichiestaIUVApiServiceImpl implements RichiestaIUVApi {

	private static final String ATTORE_BS = "riscabatchspec";
	private static final String ATTORE_BO = "riscabatchord";
	private static final String IDENTITY = "identity";
	private static Logger LOGGER = Logger.getLogger(Constants.LOGGER_NAME);

	private static final String AMBITO_CONFIG_CHIAVE_RECORD_PER_LOTTO = "LOTTO.RecordPerLotto";
	private static final String COD_SOGGETTO_P_FISICA = "F";
	private static final String COD_SOGGETTO_P_GIURIDICA = "G";

	private static final String FASE = "Conferma - RichiestaIUV";

	public static final String COD_FASE_BO = "CONFBO_RIC_IUV";
	public static final String COD_FASE_BS = "CONFBS_RIC_IUV";
	public static final String COD_FASE_BG = "CONFBG_RIC_IUV";

	@Autowired
	private RegistroElaboraDAO registroElaboraDAO;

	@Autowired
	private OutputDatiDAO outputDatiDAO;

	@Autowired
	private AmbitiDAO ambitiDAO;

	@Autowired
	private AmbitiConfigDAO ambitiConfigDAO;

	@Autowired
	private EntePagopaDAO entePagopaDAO;

	@Autowired
	private PagopaScompRichIuvDAO pagopaScompRichIuvDAO;

	@Autowired
	private PagopaListaCarIuvDAO pagopaListaCarIuvDAO;

	@Autowired
	private LottoDAO lottoDAO;

	@Autowired
	private PagopaServiceHelper pagopaServiceHelper;

	private static final Long ID_OUTPUT_FOGLIO_DATI_TITOLARE_BS = 56l;
	private static final Long ID_OUTPUT_FOGLIO_DATI_TITOLARE_BG = 75l;
	private static final Long ID_OUTPUT_FOGLIO_DATI_TITOLARE_BO = 40l;

	private static final String COD_TIPO_ELABORA_BS = "BS";
	private static final String COD_TIPO_ELABORA_BG = "BG";
	private static final String COD_TIPO_ELABORA_BO = "BO";

	private static final String PASSO_CONTA_PD_PER_LOTTI = "CONTA_PD_PER_LOTTI";
	private static final String PASSO_INFO_PER_LOTTO = "INFO_PER_LOTTO";
	private static final String PASSO_AGGIORNA_NOME_LOTTO = "AGGIORNA_NOME_LOTTO";
	private static final String PASSO_POP_W_SCOMP_RICH_IUV = "POP_W_SCOMP_RICH_IUV";
	private static final String PASSO_CREA_LISTA_CAR_IUV = "CREA_LISTA_CAR_IUV";
	private static final String PASSO_CREA_SCOMP_RICH_IUV = "CREA_SCOMP_RICH_IUV";
	private static final String PASSO_INVIO_LISTA_CARICO = "INVIO_LISTA_CARICO";
	private static final String PASSO_SET_COD_ESITO = "SET_COD_ESITO";

	@Override
	public Response creaLotti(Long idElabora, Long idAmbito, String codTipoElabora) throws Exception {
		LOGGER.debug("[RichiestaIUVApiServiceImpl::creaLotti] BEGIN");
		BollResultDTO compResult = new BollResultDTO();
		
		String codFase = getCodFase(codTipoElabora);
		String attore = BollUtils.getAttore(codTipoElabora);
		Long idFoglio = BollUtils.getIdFoglioTitolare(codTipoElabora);
		List<OutputDatiDTO> listOutDati = outputDatiDAO.loadOutputDatiByFoglio(idElabora, idFoglio, codTipoElabora);
		int numPosizioniDebitorie = listOutDati.size();

		if (numPosizioniDebitorie > 0) {
			logStep(idElabora, codFase, PASSO_CONTA_PD_PER_LOTTI, 0, FASE
					+ " - Step 2 - Estrazione numero posizioni debitorie OK - PosDebLette: " + numPosizioniDebitorie,
					attore);

			String dataLotto = new SimpleDateFormat("yyyyMMdd_HHmm").format(new Date());

			int recordPerLotto = 0;
			AmbitoDTO ambito = ambitiDAO.loadAmbitoByIdAmbito(idAmbito);
			List<AmbitoConfigDTO> config = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(ambito.getCodAmbito(),
					AMBITO_CONFIG_CHIAVE_RECORD_PER_LOTTO);
			if (config != null && config.size() > 0) {
				recordPerLotto = Integer.parseInt(config.get(0).getValore());
			}

			EntePagopaDTO entePagopa = entePagopaDAO.loadEntePagopaPerIuvByAmbito(idAmbito);

			if (entePagopa != null) {
				String codiceSettore = entePagopa.getCodSettore();
				String causale = entePagopa.getCausale();
				String codiceVersamento = entePagopa.getCodVersamento();

				logStep(idElabora, codFase, PASSO_INFO_PER_LOTTO, 0,
						FASE + " - Step 2 - Estr. info Lotto OK: CodiceSettore: " + codiceSettore + " - Causale: "
								+ causale + " - CodiceVersamento: " + codiceVersamento + " - RecordPerLotto: "
								+ recordPerLotto,
						attore);

				Integer res = outputDatiDAO.updateOutputDatiNomiLotto(idElabora, codiceSettore, codiceVersamento,
						dataLotto, recordPerLotto, codTipoElabora);
				if (res != null && res > 0) {
					logStep(idElabora, codFase, PASSO_AGGIORNA_NOME_LOTTO, 0, FASE
							+ " - Step 2 - Aggiorna Nome Lotto OK sulla tabella RISCA_W_OUTPUT_DATI per l'elaborazione: "
							+ idElabora, attore);

					compResult.setStatus("OK");

				} else {
					compResult.setStatus("KO");
					compResult.setStepError(PASSO_AGGIORNA_NOME_LOTTO);
					compResult.setErrorMessage(FASE
							+ " - Step 2 - Aggiorna Nome Lotto KO: non sono stati aggiornati i nomi lotto sulla tabella RISCA_W_OUTPUT_DATI per l'elaborazione: "
							+ idElabora);
				}
			} else {
				compResult.setStatus("KO");
				compResult.setStepError(PASSO_INFO_PER_LOTTO);
				compResult.setErrorMessage(FASE
						+ " - Step 2 - Estrazione info per Lotto KO: non sono state trovate tutte le informazioni per l'ambito "
						+ idAmbito);
			}

		} else {
			compResult.setStatus("KO");
			compResult.setStepError(PASSO_CONTA_PD_PER_LOTTI);
			compResult.setErrorMessage(FASE
					+ " - Step 2 - Estrazione numero posizioni debitorie KO: Non sono presenti posizioni debitorie per le quali generare lo IUV");
		}

		LOGGER.debug("[RichiestaIUVApiServiceImpl::creaLotti] END");
		return Response.ok(compResult).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response initInviaLotti(Long idElabora, Long idAmbito, String codTipoElabora) {
		LOGGER.debug("[RichiestaIUVApiServiceImpl::initInviaLotti] BEGIN");
		BollResultIUVDTO compResult = new BollResultIUVDTO();
		String codFase = getCodFase(codTipoElabora);
		String attore = BollUtils.getAttore(codTipoElabora);
		try {
			List<ScomposizioneRichiestaIuvDTO> scompRichIuv = pagopaScompRichIuvDAO.loadScomposizioneRichiesteIuv(codTipoElabora);
			BigDecimal importoPerAcc = BigDecimal.ZERO;
			BigDecimal percTotImp = BigDecimal.ZERO;
			BigDecimal runningTotImpPerAcc = BigDecimal.ZERO;

			List<String> listaNap = (List<String>) scompRichIuv.stream().map(ScomposizioneRichiestaIuvDTO::getNap)
					.collect(Collectors.toList());

			if (listaNap != null && listaNap.size() > 0) {
				pagopaScompRichIuvDAO.deletePagopaScompRichIuvWorkingWhereNap(listaNap);
			}

			logStep(idElabora, codFase, PASSO_POP_W_SCOMP_RICH_IUV, 0,
					FASE + " - Step 3 - Popola working per dati scomposizione INIZIO", attore);
			for (ScomposizioneRichiestaIuvDTO scomposizioneDto : scompRichIuv) {

				if (scomposizioneDto.getCountImpPerAccOrig() == 1) {
					/*
					 * per il NAP corrente esiste una sola riga (= un unico raggruppamento tipo di
					 * Accertamento di Bilancio) quindi l'importo per NAP va a finire tutto
					 * nell'importo per Accertamento finale
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

				// Inserire i dati nella tabella RISCA_W_PAGOPA_SCOMP_RICH_IUV
				saveScompRichIuvWorking(idElabora, codFase, importoPerAcc, percTotImp, scomposizioneDto, attore);

			}
			logStep(idElabora, codFase, PASSO_POP_W_SCOMP_RICH_IUV, 0,
					FASE + " - Step 3 - Popola working per dati scomposizione FINE", attore);
		} catch (DAOException e) {
			LOGGER.error("[RichiestaIUVApiServiceImpl::initInviaLotti] ERROR: " + e.getMessage());
			compResult.setStatus("KO");
		}

		compResult.setStatus("OK");

		LOGGER.debug("[RichiestaIUVApiServiceImpl::initInviaLotti] END");
		return Response.ok(compResult).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response inviaLotto(InvioLottoDTO invioLotto, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		LOGGER.debug("[RichiestaIUVApiServiceImpl::inviaLotto] BEGIN");
		String codTipoElabora = invioLotto.getElaborazione().getTipoElabora().getCodTipoElabora();
		String codFase = getCodFase(codTipoElabora);
		String attore = BollUtils.getAttore(codTipoElabora);

		BollResultIUVDTO compResult = new BollResultIUVDTO();
		compResult.setDataRichiestaIuv(new Date());

		List<PagopaPosizioniDebitorieDTO> posizioniDebitorie = invioLotto.getPosizioniDebitorie();
		Long idElabora = invioLotto.getElaborazione().getIdElabora();
		Long idAmbito = invioLotto.getElaborazione().getAmbito().getIdAmbito();
		LottoDTO lotto = invioLotto.getLotto();

		ArrayList<String> lotti = new ArrayList<String>();
		ArrayList<String> numPosizioniDebitorie = new ArrayList<String>();

		try {
			EntePagopaDTO entePagopa = entePagopaDAO.loadEntePagopaPerIuvByAmbito(idAmbito);
			String causale = entePagopa.getCausale();

			for (PagopaPosizioniDebitorieDTO posDebitoria : posizioniDebitorie) {

				// Leggere le componenti dell'importo
				List<PagopaScompRichIuvDTO> componentiImporto = pagopaScompRichIuvDAO
						.loadScompRichIuvWorkingByNap(posDebitoria.getNap());

				posDebitoria.setComponentiImporto(componentiImporto);

				// Inserire i dati nella tabella RISCA_R_PAGOPA_LISTA_CAR_IUV
				PagopaListaCarIuvDTO listaCar = saveListaCarIuv(idElabora, codFase, causale, lotto, posDebitoria,
						attore);

				if (listaCar.getIdPagopaListaCarIuv() != null) {

					// Inserire i dati nella tabella RISCA_R_PAGOPA_SCOMP_RICH_IUV
					for (PagopaScompRichIuvDTO comp : componentiImporto) {
						comp.setIdPagopaScompRichIuv(null);
						comp.setIdLotto(lotto.getIdLotto());
						comp.setGestAttoreIns(attore);
						comp.setGestAttoreUpd(attore);
						comp = pagopaScompRichIuvDAO.savePagopaScompRichIuv(comp);
						if (comp.getIdPagopaScompRichIuv() != null) {
							logStep(idElabora, codFase, PASSO_CREA_SCOMP_RICH_IUV, 0,
									FASE + " - Step 3 - Inserimento Scomposizione IUV OK per l'elaborazione: "
											+ idElabora + " - Nome Lotto: " + lotto.getNomeLotto() + " - NAP: "
											+ posDebitoria.getNap(),
									attore);
						} else {
							compResult.setStatus("KO");
							compResult.setStepError(PASSO_CREA_SCOMP_RICH_IUV);
							compResult.setErrorMessage(FASE
									+ " - Step 3 - Inserimento Scomposizione IUV KO: non sono stati inseriti i dati nella tabella RISCA_R_PAGOPA_SCOMP_RICH_IUV per l'elaborazione: "
									+ idElabora + " - Nome Lotto: " + lotto.getNomeLotto() + " - NAP: "
									+ posDebitoria.getNap());
							return Response.ok(compResult).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
						}
					}
				} else {
					compResult.setStatus("KO");
					compResult.setStepError(PASSO_CREA_LISTA_CAR_IUV);
					compResult.setErrorMessage(
							FASE + " - Step 3 - Inserimento Lista Car IUV OK per l'elaborazione: " + idElabora
									+ " - Nome Lotto: " + lotto.getNomeLotto() + " - NAP: " + posDebitoria.getNap());
					return Response.ok(compResult).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
				}
			}

			// Richiamare il metodo InserisciListaDiCarico di PAGOPA
			InserisciListaDiCaricoRequest request = makeInserisciListaDiCaricoRequest(lotto.getNomeLotto(), entePagopa,
					posizioniDebitorie);
			ResponseType esitoPagopa;
			String esito = null;
			String esitoDesc = null;
			try {
				esitoPagopa = inserisciListaDiCarico(request);
				esito = esitoPagopa.getResult().getCodice();
				esitoDesc = esitoPagopa.getResult().getMessaggio();
				// NB. tronco la descrizione dell'esito a 200 caratteri che e' il massimo della
				// colonna sul DB
				if (esitoDesc.length() > 200) {
					esitoDesc = esitoDesc.substring(0, 200);
				}
			} catch (PagopaException e) {
				LOGGER.error("[RichiestaIUVApiServiceImpl::inviaLotti] ERROR inserisciListaDiCarico: " + e);
			}

			if (esito != null && esito.equals("000")) {
				logStep(idElabora, codFase, PASSO_INVIO_LISTA_CARICO, 0,
						FASE + " - Step 3 - Invio Lista di carico IUV OK per l'elaborazione: " + idElabora
								+ " - Nome Lotto: " + lotto.getNomeLotto(),
						attore);

				// Update nella tabella RISCA_T_LOTTO con codice esito ottenuto
				// dal servizio di PAGOPA
				Integer res = lottoDAO.updateEsitoLottoByIdLotto(lotto.getIdLotto(), esito.equals("000") ? 1 : 0, esito,
						esitoDesc);
				if (res != null && res > 0) {
					logStep(idElabora, codFase, PASSO_SET_COD_ESITO, 0,
							FASE + " - Step 3 - Modifica Esito InvioListaDiCarico OK: " + idElabora + " - Nome Lotto: "
									+ lotto.getNomeLotto(),
							attore);

					// Step 3 completato con successo
					compResult.setStatus("OK");

					// Segno le info per l'invio mail
					lotti.add(esitoDesc + " " + esito + " - " + lotto.getNomeLotto());
					numPosizioniDebitorie.add("" + posizioniDebitorie.size());

				} else {
					compResult.setStatus("KO");
					compResult.setStepError(PASSO_SET_COD_ESITO);
					compResult.setErrorMessage(FASE
							+ " - Step 3 - Modifica Esito InvioListaDiCarico KO: non sono stati modificati i dati nella tabella RISCA_T_LOTTO per l'elaborazione : "
							+ idElabora + " - Nome Lotto: " + lotto.getNomeLotto());
					return Response.ok(compResult).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
				}
			} else {
				compResult.setStatus("KO");
				compResult.setStepError(PASSO_INVIO_LISTA_CARICO);
				compResult.setErrorMessage(FASE
						+ " - Step 3 - Invio Lista di carico IUV KO: Invio dati a PAGOPA fallito per l'elaborazione : "
						+ idElabora + " - Nome Lotto: " + lotto.getNomeLotto() + "Codice Esito: " + esito);
				return Response.ok(compResult).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
			}
		} catch (Exception e) {
			LOGGER.debug("[RichiestaIUVApiServiceImpl::inviaLotto] ERROR: " + e.getMessage());
			compResult.setStatus("KO");
		}

		compResult.setNomiLotto(lotti);
		compResult.setNumPosizioniDebitorie(numPosizioniDebitorie);

		LOGGER.debug("[RichiestaIUVApiServiceImpl::inviaLotto] END");
		return Response.ok(compResult).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	private PagopaListaCarIuvDTO saveListaCarIuv(Long idElabora, String codFase, String causale, LottoDTO lotto,
			PagopaPosizioniDebitorieDTO posDebitoria, String attore) {
		PagopaListaCarIuvDTO listaCar = new PagopaListaCarIuvDTO();
		listaCar.setIdLotto(lotto.getIdLotto());
		listaCar.setNap(posDebitoria.getNap());
		listaCar.setAnno(posDebitoria.getAnno());
		// ******** TODO verificare *********************
		// to_number(nvl(posizioni_debitorie(record_corrente).valore_colonna9 IMPORTO,
		// 0), '9999999999D99', 'NLS_NUMERIC_CHARACTERS = '',.'' ') / 100
		listaCar.setImporto(posDebitoria.getImporto());
		// ***********************************
		listaCar.setDataScadenza(posDebitoria.getDataScadenza());
		listaCar.setDataInizioValidita(posDebitoria.getDataInizioValidita());
		listaCar.setDataFineValidita(posDebitoria.getDataFineValidita());
		listaCar.setCausale(causale);
		listaCar.setTipoSoggetto(posDebitoria.getTipoSoggetto());
		listaCar.setCodFiscale(posDebitoria.getCodFiscale());
		listaCar.setRagioneSociale(posDebitoria.getRagioneSociale());
		listaCar.setCognome(posDebitoria.getCognome());
		listaCar.setNome(posDebitoria.getNome());
		listaCar.setNote(posDebitoria.getNote());
		listaCar.setGestAttoreIns(attore);
		listaCar.setGestAttoreUpd(attore);

		listaCar = pagopaListaCarIuvDAO.savePagopaListaCarIuv(listaCar);
		if (listaCar.getIdPagopaListaCarIuv() != null) {
			logStep(idElabora, codFase, PASSO_CREA_LISTA_CAR_IUV, 0,
					FASE + " - Step 3 - Inserimento Lista Car IUV OK per l'elaborazione: " + idElabora
							+ " - Nome Lotto: " + lotto.getNomeLotto() + " - NAP: " + posDebitoria.getNap(),
					attore);
		}
		return listaCar;
	}

	private Long saveScompRichIuvWorking(Long idElabora, String codFase, BigDecimal importoPerAcc,
			BigDecimal percTotImp, ScomposizioneRichiestaIuvDTO scomposizioneDto, String attore) {
		PagopaScompRichIuvDTO dto = new PagopaScompRichIuvDTO();
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
		dto = pagopaScompRichIuvDAO.savePagopaScompRichIuvWorking(dto);
		return dto.getIdPagopaScompRichIuv();
	}

	private void logStep(Long idElabora, String codFase, String codPasso, int esito, String note, String attore) {
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
			LOGGER.error("[RichiestaIUVApiServiceImpl::logStep] (Exception) ", e);
		}
		LOGGER.debug(note);
	}

	private ResponseType inserisciListaDiCarico(InserisciListaDiCaricoRequest listaDiCaricoRequest)
			throws PagopaException {
		LOGGER.debug("[RichiestaIUVApiServiceImpl::richiestaIuv] BEGIN");
		ResponseType response = null;
		try {
			response = pagopaServiceHelper.getSoapClient().inserisciListaDiCarico(listaDiCaricoRequest);

			if (response != null && response.getResult() != null) {
				LOGGER.debug("[RichiestaIUVApiServiceImpl::richiestaIuv] Response - codice: "
						+ response.getResult().getCodice());
				LOGGER.debug("[RichiestaIUVApiServiceImpl::richiestaIuv] Response - messaggio: "
						+ response.getResult().getMessaggio());
			}
			return response;
		} catch (Exception e) {
			LOGGER.error("[RichiestaIUVApiServiceImpl::richiestaIuv] Errore nella richiestaIuv (Exception) ", e);
			throw new PagopaException("Errore nella richiestaIuv (Exception)" + e.getMessage());
		}
	}

	private InserisciListaDiCaricoRequest makeInserisciListaDiCaricoRequest(String nomeLotto, EntePagopaDTO entePagopa,
			List<PagopaPosizioniDebitorieDTO> posizioniDebitorie) throws Exception {

		InserisciListaDiCaricoRequest request = new InserisciListaDiCaricoRequest();
		TestataListaCarico testata = null;
		ListaDiCarico lcarico = null;
		double totalVal = 0.0;
		try {
			if (posizioniDebitorie == null || posizioniDebitorie.size() < 1) {
				throw new PagopaException("List posizioniDebitorie is empty");
			}

			if (entePagopa == null) {
				throw new PagopaException("Testata is empty");
			}
			testata = new TestataListaCarico();
			testata.setCFEnteCreditore(entePagopa.getCfEnteCreditore());
			testata.setCodiceVersamento(entePagopa.getCodVersamento());
			testata.setIdMessaggio(nomeLotto);
			testata.setNumeroPosizioniDebitorie(posizioniDebitorie.size());
			for (int i = 0; i < posizioniDebitorie.size(); i++) {
				totalVal = totalVal + posizioniDebitorie.get(i).getImporto().doubleValue();
			}

			testata.setImportoTotaleListaDiCarico(new BigDecimal(totalVal).setScale(2, RoundingMode.HALF_UP));
			LOGGER.debug("[RichiestaIUVApiServiceImpl::makeInserisciListaDiCaricoRequest] val: "
					+ testata.getImportoTotaleListaDiCarico());

			PosizioniDaInserire posizioni = new PosizioniDaInserire();

			for (PagopaPosizioniDebitorieDTO posDebitoria : posizioniDebitorie) {
				if (posDebitoria.getImporto().compareTo(BigDecimal.ZERO) != 0) {
					PosizioneDaInserireType p = new PosizioneDaInserireType();
					p.setAnnoRiferimento(Integer.valueOf(posDebitoria.getAnno()));

					ComponentiImporto componentiImporto = new ComponentiImporto();
					for (PagopaScompRichIuvDTO compImporto : posDebitoria.getComponentiImporto()) {
						if(compImporto.getImportoPerAcc().compareTo(BigDecimal.ZERO) != 0) {
							ComponenteImportoType compValue = new ComponenteImportoType();
							compValue.setAnnoAccertamento(Integer.valueOf(compImporto.getAnno()));
							compValue.setCausaleDescrittiva(posDebitoria.getCausale());
							compValue.setImporto(compImporto.getImportoPerAcc());
							compValue.setNumeroAccertamento(compImporto.getCodBilAcc());
							compValue.setDatiSpecificiRiscossione(compImporto.getDatiSpecRisc());
							componentiImporto.getComponenteImporto().add(compValue);
						}
					}

					if (componentiImporto.getComponenteImporto().size() > 0) {
						p.setComponentiImporto(componentiImporto);
					}

					p.setDataFineValidita(getXMLGCFromString(posDebitoria.getDataFineValidita()));
					p.setDataInizioValidita(getXMLGCFromString(posDebitoria.getDataInizioValidita()));
					p.setDataScadenza(getXMLGCFromString(posDebitoria.getDataScadenza()));
					p.setDescrizioneCausaleVersamento(posDebitoria.getCausale());
					p.setIdPosizioneDebitoria(posDebitoria.getNap());
					p.setImportoTotale(posDebitoria.getImporto());
					p.setNotePerIlPagatore(posDebitoria.getNote());

					SoggettoType sog = new SoggettoType();
					sog.setIdentificativoUnivocoFiscale(posDebitoria.getCodFiscale());
					if (posDebitoria.getTipoSoggetto().equals(COD_SOGGETTO_P_FISICA)) {
						PersonaFisicaType pt = new PersonaFisicaType();
						pt.setCognome(posDebitoria.getCognome());
						pt.setNome(posDebitoria.getNome());
						sog.setPersonaFisica(pt);
					} else if (posDebitoria.getTipoSoggetto().equals(COD_SOGGETTO_P_GIURIDICA)) {
						PersonaGiuridicaType pg = new PersonaGiuridicaType();
						pg.setRagioneSociale(posDebitoria.getRagioneSociale());
						sog.setPersonaGiuridica(pg);
					}
					p.setSoggettoPagatore(sog);
					posizioni.getPosizioneDaInserire().add(p);
				} else {
					LOGGER.info(
							"[RichiestaIUVApiServiceImpl::makeInserisciListaDiCaricoRequest] la posizione debitoria "
									+ posDebitoria.getNap() + " ha importo 0.0, non la inserisco.");
				}
			}

			lcarico = new ListaDiCarico();
			lcarico.setPosizioniDaInserire(posizioni);
			request.setListaDiCarico(lcarico);
			request.setTestata(testata);

			return request;
		} catch (PagopaException e) {
			LOGGER.error("[RichiestaIUVApiServiceImpl::makeInserisciListaDiCaricoRequest] ERROR" + e);
			throw e;
		} catch (Exception e) {
			LOGGER.error("[RichiestaIUVApiServiceImpl::makeInserisciListaDiCaricoRequest] ERROR" + e);
			throw new PagopaException(e.getMessage());
		} finally {
			LOGGER.debug("[RichiestaIUVApiServiceImpl::makeInserisciListaDiCaricoRequest] END");
		}
	}

	private XMLGregorianCalendar getXMLGCFromString(Date date) throws Exception {
		LOGGER.debug("[RichiestaIUVApiServiceImpl::getXMLGCFromString] date " + date);
		try {
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(date);
			return DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
		} catch (Exception e) {
			LOGGER.error("[RichiestaIUVApiServiceImpl::getXMLGCFromString] ERROR ", e);
			throw new PagopaException("Invalid date " + date);
		} finally {
		}
	}
	
	private String getCodFase(String tipoElab) {
		switch (tipoElab) {
		case COD_TIPO_ELABORA_BS:
			return COD_FASE_BS;
		case COD_TIPO_ELABORA_BG:
			return COD_FASE_BG;
		case COD_TIPO_ELABORA_BO:
			return COD_FASE_BO;
		default:
			return null;
		}
	}

}

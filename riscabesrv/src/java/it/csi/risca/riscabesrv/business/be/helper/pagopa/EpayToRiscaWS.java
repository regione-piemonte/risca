/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.business.be.helper.pagopa;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.epay.epaywso.epaywso2enti.types.EsitoAggiornaPosizioniDebitorieRequest;
import it.csi.epay.epaywso.epaywso2enti.types.EsitoInserimentoListaDiCaricoRequest;
import it.csi.epay.epaywso.epaywso2enti.types.NotificaPagamentoType;
import it.csi.epay.epaywso.epaywso2enti.types.TrasmettiNotifichePagamentoRequest;
import it.csi.epay.epaywso.types.PosizioneDebitoriaType;
import it.csi.epay.epaywso.types.ResponseType;
import it.csi.epay.epaywso.types.ResultType;
import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.impl.BusinessLogicManager;
import it.csi.risca.riscabesrv.business.be.impl.ElaborazionePagamenti;
import it.csi.risca.riscabesrv.business.be.impl.dao.AmbitiConfigDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.AmbitiDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.ElaboraDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.EmailServizioDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.EntePagopaDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.IuvDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.LottoDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.PagopaListaCarIuvDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.PagopaListaCaricoDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RegistroElaboraDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RpPagopaDAO;
import it.csi.risca.riscabesrv.dto.AmbitoConfigDTO;
import it.csi.risca.riscabesrv.dto.AmbitoDTO;
import it.csi.risca.riscabesrv.dto.ElaboraDTO;
import it.csi.risca.riscabesrv.dto.ElaborazionePagamentiResultDTO;
import it.csi.risca.riscabesrv.dto.EmailServizioDTO;
import it.csi.risca.riscabesrv.dto.EntePagopaDTO;
import it.csi.risca.riscabesrv.dto.IuvDTO;
import it.csi.risca.riscabesrv.dto.LottoDTO;
import it.csi.risca.riscabesrv.dto.PagopaListaCarIuvDTO;
import it.csi.risca.riscabesrv.dto.PagopaListaCaricoDTO;
import it.csi.risca.riscabesrv.dto.RegistroElaboraDTO;
import it.csi.risca.riscabesrv.dto.RegistroElaboraExtendedDTO;
import it.csi.risca.riscabesrv.dto.RpPagopaDTO;
import it.csi.risca.riscabesrv.dto.StatoDebitorioExtendedDTO;
import it.csi.risca.riscabesrv.dto.StatoElaborazioneDTO;
import it.csi.risca.riscabesrv.dto.StatoIuvDTO;
import it.csi.risca.riscabesrv.dto.TipoElaboraExtendedDTO;
import it.csi.risca.riscabesrv.util.BollUtils;
import it.csi.risca.riscabesrv.util.Constants;
import it.csi.risca.riscabesrv.util.mail.AttachmentData;
import it.csi.risca.riscabesrv.util.mail.MailException;
import it.csi.risca.riscabesrv.util.mail.MailInfo;
import it.csi.risca.riscabesrv.util.mail.MailManager;

@WebService(name = "EpayToRiscaWS", targetNamespace = "http://www.csi.it/epay/epaywso/epaywso2enti/types", serviceName = "EpayToRiscaWS")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, parameterStyle = SOAPBinding.ParameterStyle.BARE)
public class EpayToRiscaWS extends SpringBeanAutowiringSupport implements IEpayToRiscaWS {

	private static Logger LOGGER = Logger.getLogger(Constants.LOGGER_NAME);

	private static final String ATTORE_PP = "pagamentipagopa";
	private static final String ATTORE_SC = "BATCH_STATO_CONT-EpayToRiscaWS";
	private static final String FASE = "Conferma - AcquisizioneIUV";
	private static final String FASE_PP = "Acquisizione Pagamenti PagoPA";
	private static final String FASE_SC = "Ricalcolo IUV - EsitoAggiornaPosizioniDebitorie";
	private static final String COD_FASE_CONFBS_ACQ_IUV = "CONFBS_ACQ_IUV";
	private static final String COD_FASE_CONFBG_ACQ_IUV = "CONFBG_ACQ_IUV";
	private static final String COD_FASE_CONFBO_ACQ_IUV = "CONFBO_ACQ_IUV";
	private static final String COD_FASE_ACQ_PAGAMENTI_PAGOPA = "ACQ_PAGAMENTI_PAGOPA";
	private static final String COD_FASE_RICALCOLO_IUV = "RICALCOLO_IUV";
	private static final String TIPO_ELAB_BS = "BS";
	private static final String TIPO_ELAB_PP = "PP";

	private static final String COD_OK_PAGOPA = "000";
	private static final String COD_178_PAGOPA = "178"; //Posizione debitoria gia' pagata

	private static final String PASSO_VERIF_PRESENZA = "VERIF_PRESENZA";
	private static final String PASSO_VERIF_IN_ESECUZIONE = "VERIF_IN_ESECUZIONE";
	private static final String PASSO_LOTTO_RISCA_OK = "LOTTO_RISCA_OK";
	private static final String PASSO_SET_IUV_LISTA_CAR = "SET_IUV_LISTA_CAR";
	private static final String PASSO_SET_INSERT_IUV = "INSERT_IUV";
	private static final String PASSO_SET_RICEVUTO_LOTTO = "SET_RICEVUTO_LOTTO";
	private static final String PASSO_LOTTO_ANOMALO = "LOTTO_ANOMALO";
	private static final String PASSO_INVIO_MAIL_SERVIZIO = "INVIO_MAIL_SERVIZIO";

	private static final String PASSO_RICALCOLO_IUV = "RICALCOLO_IUV";

	private static final String PASSO_INIZIO_STEP = "INIZIO_STEP";
	private static final String PASSO_FINE_STEP = "FINE_STEP";
	private static final String PASSO_INS_ELABORA = "INS_ELABORA";
	private static final String PASSO_INS_PAGAM_IN_WORK = "INS_PAGAM_IN_WORK";
	private static final String PASSO_CODAVVISO_PRESENTE = "CODAVVISO_PRESENTE";

	private static final String STATO_ELAB_ATTESAIUV = "ATTESAIUV";
	private static final String STATO_ELAB_IUVOK = "IUVOK";
	private static final String STATO_ELAB_ATTESAPDF = "ATTESAPDF";
	private static final String STATO_ELAB_PDFPRONTI = "PDFPRONTI";
	private static final String STATO_ELAB_PDFOK = "PDFOK";
	private static final String STATO_ELAB_MAILOK = "MAILOK";
	private static final String STATO_TERMIN_KO = "TERMIN_KO";
	private static final String STATO_ELAB_PAGPGPA_ON = "PAGPGPA_ON";
	private static final String STATO_ELAB_PAGPGPA_KO = "PAGPGPA_KO";
	private static final String STATO_ELAB_PAGPGPA_OK = "PAGPGPA_OK";

	private static final String STATO_ELAB_STACONT_ON = "STACONT_ON";
	private static final String STATO_ELAB_STACONT_OK = "STACONT_OK";
	private static final String STATO_ELAB_STACONT_KO = "STACONT_KO";

	private static final String AMBITO_CONFIG_CHIAVE_MAIL_MITT = "SRVAPP.MittenteSegnalazioniServizioApplicativo";
	private static final String AMBITO_CONFIG_CHIAVE_MAIL_DEST = "SRVAPP.DestinatarioServizioApplicativo";
	private static final String AMBITO_CONFIG_CHIAVE_NOME_ALLEGATO = "SRVAPP.NomeAllegatoServizioApplicativo";
	private static final String AMBITO_CONFIG_CHIAVE_MAIL_USERNAME = "SRVAPP.UsernameServizioApplicativo";
	private static final String AMBITO_CONFIG_CHIAVE_MAIL_PASSWORD = "SRVAPP.PasswordServizioApplicativo";
	private static final String AMBITO_CONFIG_CHIAVE_INVIO_MAIL = "ACQPAG.InvioMailServizioApplicativo";

	private static final String DATE_FORMAT = "yyyy-MM-dd";
	private static final String DATE_FORMAT_MAIL = "dd/MM/yyyy";
	private static final String HOUR_FORMAT_MAIL = "HH:mm:ss";

	private static final String ERROR_E00_1_PP = "ERRORE E00.1: i dati ricevuti non sono relativi ad ambiti gestiti da RISCA.";
	private static final String ERROR_E01_4_PP = "ERRORE E01.4: caricamento prenotazione Acquisizione Pagamenti da PagoPA fallito.";

	private static final String COD_EMAIL_E_CONFBS_ACQ_IUV_I_BS = "E_CONFBS_ACQ_IUV_I";
	private static final String COD_EMAIL_E_CONFBS_ACQ_IUV_F_BS = "E_CONFBS_ACQ_IUV_F";

	private static final String COD_EMAIL_E_CONFBS_ACQ_IUV_I_BO = "E_CONFBO_ACQ_IUV_I";
	private static final String COD_EMAIL_E_CONFBS_ACQ_IUV_F_BO = "E_CONFBO_ACQ_IUV_F";

	private static final String COD_EMAIL_E_ACQ_PAGAM_PAGOPA = "E_ACQ_PAGAM_PAGOPA";

	private static final String COD_EMAIL_E_MODIFICA_IUV = "E_MODIFICA_IUV";

	private static final String COD_STATO_IUV_ANN = "ANN";

	@Autowired
	private RegistroElaboraDAO registroElaboraDAO;

	@Autowired
	private AmbitiDAO ambitiDAO;

	@Autowired
	private AmbitiConfigDAO ambitiConfigDAO;

	@Autowired
	private ElaboraDAO elaboraDAO;

	@Autowired
	private EntePagopaDAO entePagopaDAO;

	@Autowired
	private LottoDAO lottoDAO;

	@Autowired
	private PagopaListaCarIuvDAO pagopaListaCarIuvDAO;

	@Autowired
	private PagopaListaCaricoDAO pagopaListaCaricoDAO;

	@Autowired
	private IuvDAO iuvDAO;

	@Autowired
	private RpPagopaDAO rpPagopaDAO;

	@Autowired
	private EmailServizioDAO emailServizioDAO;

	@Autowired
	private MailManager mailManager;

	@Autowired
	private ElaborazionePagamenti elaborazionePagamenti;

	@Autowired
	private BusinessLogicManager businessLogicManager;

	private ElaboraDTO elabora;
	private String idAmbito;
	private Long idElabora;
	private String nomeLotto;
	private Date dataAcquisizione;
	private String attore;
	private String codFase;

	@WebResult(name = "EPaywsoServiceResponse", targetNamespace = "http://www.csi.it/epay/epaywso/types", partName = "parameters")
	@WebMethod(operationName = "EsitoInserimentoListaDiCarico", action = "http://www.csi.it/epay/epaywso/service/EsitoInserimentoListaDiCarico")
	public ResponseType EsitoInserimentoListaDiCarico(
			@WebParam(partName = "parameters", name = "EsitoInserimentoListaDiCaricoRequest", targetNamespace = "http://www.csi.it/epay/epaywso/epaywso2enti/types") EsitoInserimentoListaDiCaricoRequest parameters)
			throws Exception {
		LOGGER.debug("[EpayToRiscaWS::esitoInserimentoListaDiCarico] BEGIN");

		String codiceVersamento = parameters.getTestataEsito().getCodiceVersamento();
		String cfEnteCreditore = parameters.getTestataEsito().getCFEnteCreditore();
		String codEsitoAcquisizione = parameters.getResult().getCodice();
		String descEsitoAcquisizione = parameters.getResult().getMessaggio();
		// NB. tronco la descrizione dell'esito a 200 caratteri che e' il massimo della
		// colonna sul DB
		if (descEsitoAcquisizione.length() > 200) {
			descEsitoAcquisizione = descEsitoAcquisizione.substring(0, 200);
		}
		nomeLotto = parameters.getTestataEsito().getIdMessaggio();
		// Annoto la data e ora di acquisizione del lotto (per la mail)
		dataAcquisizione = new Date();

		EntePagopaDTO ente = entePagopaDAO.loadEntePagopaByCodVersamentoEnteCreditore(codiceVersamento, cfEnteCreditore,
				true);

		// Ottengo id_ambito
		idAmbito = "" + ente.getIdAmbito();

		// Cerco l'elaborazione in base al nome lotto
		boolean ok = verificaElaborazioneByNomeLotto(nomeLotto);
		if (ok) {
			String codTipoElabora = elabora.getTipoElabora().getCodTipoElabora();
			attore = BollUtils.getAttore(codTipoElabora);
			codFase = getCodFase(codTipoElabora);

			// Verifico che non ci sia gia' un'elaborazione in corso
			ok = verificaElaborazioneInCorso(idAmbito);
			if (ok) {
				// Verifica che il lotto ricevuto sia di RISCA
				List<LottoDTO> lotti = lottoDAO.loadLottoInviatoByName(idElabora, nomeLotto);
				if (lotti != null && lotti.size() > 0) {

					logStep(idElabora, PASSO_LOTTO_RISCA_OK, 0,
							FASE + " - Lotto Ricevuto e' di RISCA - Lotto: " + nomeLotto);

					// Ricavare il numero di lotti attesi
					List<LottoDTO> listaLottiAttesi = lottoDAO.findLottiAttesi(idElabora);
					int lottiAttesi = listaLottiAttesi.size();
					List<Long> idLottiAttesi = (List<Long>) listaLottiAttesi.stream().map(LottoDTO::getIdLotto)
							.collect(Collectors.toList());

					if (!codEsitoAcquisizione.equals("000")) {
						// Acquisizione del lotto fallita, registro codice e edescrizione ma proseguo a
						// verificare l'esito per ogni posizione debitoria
						lottoDAO.updateFlgRicevutoElaboratoByNomeLotto(idElabora, nomeLotto, 1, 0, codEsitoAcquisizione,
								descEsitoAcquisizione);
					}

					int numPosizioniDebitorieRicevute = 0;
					List<PosizioneDebitoriaType> posizioniDebitorie = parameters.getEsitoInserimento()
							.getElencoPosizioniDebitorieInserite().getPosizioneDebitoriaInserita();

					boolean lottoOk = true;
					boolean napOk = true;
					for (int i = 0; i < posizioniDebitorie.size(); i++) {

						PosizioneDebitoriaType posDeb = posizioniDebitorie.get(i);
						if (!posDeb.getCodiceEsito().equalsIgnoreCase(COD_OK_PAGOPA)) {
							lottoOk = false;
							napOk = false;
						} else {
							napOk = true;
						}

						// settare nella RISCA_R_PAGOPA_LISTA_CAR_IUV lo IUV,
						// il codice_avviso e l'esito
						ok = aggiornaEsitoListaCarIuv(posDeb);
						if (ok) {
							if (napOk) {
								// inserire un record nella tabella RISCA_T_IUV
								ok = saveIuv(codiceVersamento, posDeb);
								if (ok) {
									numPosizioniDebitorieRicevute++;
								} else {
									terminaElabConErrore("Errore in acquisizione del lotto " + nomeLotto
											+ ". Fallito inserimento record in RISCA_T_IUV");
									break;
								}
							} else {
								logStep(idElabora, PASSO_SET_INSERT_IUV, 1,
										FASE + " - ricevuto un esito negativo dello IUV per l'elaborazione: "
												+ idElabora + "NAP: " + posDeb.getIdPosizioneDebitoria()
												+ " ESITO IUV: " + posDeb.getCodiceEsito() + " "
												+ posDeb.getDescrizioneEsito());
							}
						} else {
							terminaElabConErrore("Errore in acquisizione del lotto " + nomeLotto
									+ ". Fallito aggiornamento record in RISCA_R_PAGOPA_LISTA_CAR_IUV");
							break;
						}

					}

					if (ok) {
						// Aggiornare la tabella RISCA_T_LOTTO
						int flgRicevuto = 1;
						int flgElaborato = lottoOk ? 1 : 0;
						Integer ret = lottoDAO.updateFlgRicevutoElaboratoByNomeLotto(idElabora, nomeLotto, flgRicevuto,
								flgElaborato, codEsitoAcquisizione, descEsitoAcquisizione);
						if (ret != null && ret > 0) {
							logStep(idElabora, PASSO_SET_RICEVUTO_LOTTO, 0, FASE
									+ " - Aggiorna Flag Ricevuto Lotto OK : " + idElabora + " Lotto: " + nomeLotto);

							// Ricavare il numero di lotti ricevuti
							int lottiRicevuti = lottoDAO.findLottiRicevuti(idElabora, idLottiAttesi).size();

							if (lottiAttesi == lottiRicevuti) {
								int lottiAnomali = lottoDAO.findLottiAnomali(idElabora, idLottiAttesi).size();
								if (lottiAnomali > 0) {
									logStep(idElabora, PASSO_LOTTO_ANOMALO, 0, FASE
											+ " - Esistono lotti non elaborati correttamente (errore su alcuni NAP) per l'elaborazione con idElabora: "
											+ idElabora);
								} else {
									// Tutti i lotti di una elaborazione sono stati acquisiti senza errori su tutti
									// i NAP
									aggiornaElabIuvOK();
								}
							}
							// Se non sono uguali, significa che dobbiamo attendere notifiche
							// di altri lotti. Il servizio termina con l'invio della mail di
							// riepilogo
							boolean isBO = codTipoElabora.equals(BollUtils.COD_TIPO_ELABORA_BO);
							try {
								invioMailLottoAcquisito(FASE,
										isBO ? COD_EMAIL_E_CONFBS_ACQ_IUV_I_BO : COD_EMAIL_E_CONFBS_ACQ_IUV_I_BS,
										lottiRicevuti, numPosizioniDebitorieRicevute);
								if (lottiAttesi == lottiRicevuti) {
									// Se siamo alla fine dell'acquisizione dei lotti invio anche la mai con oggetto
									// FINE
									invioMailLottoAcquisito(FASE,
											isBO ? COD_EMAIL_E_CONFBS_ACQ_IUV_F_BO : COD_EMAIL_E_CONFBS_ACQ_IUV_F_BS,
											lottiRicevuti, numPosizioniDebitorieRicevute);
								}
							} catch (MailException e) {
								LOGGER.error("[EpayToRiscaWS::" + FASE + "::InvioMail] Errore invio mail: "
										+ e.getMessage());
								logStep(idElabora, PASSO_INVIO_MAIL_SERVIZIO, 1,
										FASE + " - InvioMail - Errore:  " + e.getMessage());
							}

						} else {
							logStep(idElabora, PASSO_SET_RICEVUTO_LOTTO, 1, FASE
									+ " - Aggiorna Flag Ricevuto Lotto KO : " + idElabora + " Lotto: " + nomeLotto);
							terminaElabConErrore("Errore in acquisizione del lotto " + nomeLotto
									+ ". Fallito aggiornamento record in RISCA_T_LOTTO");
						}
					}
				} else {
					/*
					 * logStep(idElabora, PASSO_LOTTO_RISCA_OK, 1, FASE +
					 * " - Lotto Ricevuto NON e' di RISCA oppure e' un lotto gia' ricevuto - Lotto: "
					 * + nomeLotto);
					 */
					LOGGER.error(FASE + "Errore in acquisizione del lotto " + nomeLotto
							+ ". Lotto gia' acquisito id_elabora = " + elabora.getIdElabora());
					invioMailErrore("Errore in acquisizione del lotto " + nomeLotto
							+ ". Lotto gia' acquisito id_elabora = " + elabora.getIdElabora(), null,
							null);
				}
			}
		}

		/*
		 * Restituisco sempre esito ok, da analisi non sono previsti errori di ritorno a
		 * pagopa gli errori sono gestiti sul nostro db
		 */
		ResponseType response = new ResponseType();
		ResultType result = new ResultType();
		result.setCodice(COD_OK_PAGOPA);
		response.setResult(result);

		LOGGER.debug("[EpayToRiscaWS::esitoInserimentoListaDiCarico] END");
		return response;
	}

	@WebResult(name = "EPaywsoServiceResponse", targetNamespace = "http://www.csi.it/epay/epaywso/types", partName = "parameters")
	@WebMethod(operationName = "TrasmettiNotifichePagamento", action = "http://www.csi.it/epay/epaywso/service/TrasmettiNotifichePagamento")
	public ResponseType TrasmettiNotifichePagamento(
			@WebParam(partName = "parameters", name = "TrasmettiNotifichePagamentoRequest", targetNamespace = "http://www.csi.it/epay/epaywso/epaywso2enti/types") TrasmettiNotifichePagamentoRequest parameters) {
		LOGGER.debug("[EpayToRiscaWS::trasmettiNotifichePagamento] BEGIN");
		// Annoto la data e ora di acquisizione dei pagamenti (per la mail)
		dataAcquisizione = new Date();
		codFase = COD_FASE_ACQ_PAGAMENTI_PAGOPA;
		attore = ATTORE_PP;

		try {
			String codiceVersamento = parameters.getTestata().getCodiceVersamento();
			String cfEnteCreditore = parameters.getTestata().getCFEnteCreditore();

			EntePagopaDTO ente = entePagopaDAO.loadEntePagopaByCodVersamentoEnteCreditore(codiceVersamento,
					cfEnteCreditore, false);

			if (ente == null) {
				LOGGER.error("[EpayToRiscaWS::trasmettiNotifichePagamento] " + ERROR_E00_1_PP);
			} else {
				// Ottengo id_ambito
				idAmbito = "" + ente.getIdAmbito();

				// Insert su tabella Elabora
				elabora = insertElaborazionePP(ente.getIdAmbito());
				if (elabora.getIdElabora() == null) {
					LOGGER.error("[EpayToRiscaWS::trasmettiNotifichePagamento] " + ERROR_E01_4_PP);
				} else {
					idElabora = elabora.getIdElabora();
					// logStep(idElabora, PASSO_INIZIO_STEP, 0, FASE_PP + " - Step 1 - Inizio");
					logStep(idElabora, PASSO_INS_ELABORA, 0,
							COD_FASE_ACQ_PAGAMENTI_PAGOPA + " - Step 1 - Inserimento Prenotazione OK");

					logStep(idElabora, PASSO_INIZIO_STEP, 0, FASE_PP + " - Step 2 - Inizio");

					List<NotificaPagamentoType> listNotifichePagoPa = parameters.getCorpoNotifichePagamento()
							.getElencoNotifichePagamento().getNotificaPagamento();

					for (NotificaPagamentoType notificaPagamento : listNotifichePagoPa) {
						insertRpPagopa(notificaPagamento);
					}
					logStep(idElabora, PASSO_FINE_STEP, 0, FASE_PP + " - Step 2 - Fine");

					logStep(idElabora, PASSO_FINE_STEP, 0, FASE_PP + " - Step 3 - Inizio");
					List<RpPagopaDTO> listaPagopa = rpPagopaDAO.loadRpPagopaByElabora(idElabora);
					if (listaPagopa.size() == 0) {
						logStep(idElabora, PASSO_CODAVVISO_PRESENTE, 1, FASE_PP
								+ " - Step 3 - Estrazione Pagamenti da Working KO per l'elaborazione: " + idElabora);
						terminaElabConErrorePP();
					} else {
						ElaborazionePagamentiResultDTO elabResult = elaborazionePagamenti
								.elaboraPagamentiPagopa(listaPagopa, idElabora);

						aggiornaElabPagopaOK();

						// Aggiornamento stato contribuzione
						List<StatoDebitorioExtendedDTO> sdList = elabResult.getSdList();
						if (sdList != null && sdList.size() > 0) {
							int status = businessLogicManager.aggiornaStatoContribuzione(sdList,
									elabora.getAmbito().getIdAmbito(), ATTORE_PP, idElabora);
							if (status == 1) {
								List<Long> listIdStatoDeb = sdList.stream().map(dto -> dto.getIdStatoDebitorio())
										.distinct().collect(Collectors.toList());
								Long[] arrayIdStatoDeb = listIdStatoDeb.toArray(new Long[listIdStatoDeb.size()]);
								logStep(idElabora, PASSO_FINE_STEP, 1, FASE_PP
										+ " - Step 3 - Aggiornamento stato contribuzione fallito per stati debitori con id: "
										+ arrayIdStatoDeb);
							}
						}

						if (isInvioMailLottoAbilitato(idAmbito)) {
							try {
								invioMailPagamElaborati(COD_EMAIL_E_ACQ_PAGAM_PAGOPA, elabResult);
							} catch (MailException e) {
								LOGGER.error("[EpayToRiscaWS::" + FASE_PP + "::InvioMail] Errore invio mail: "
										+ e.getMessage());
								logStep(idElabora, PASSO_INVIO_MAIL_SERVIZIO, 1,
										FASE + " - InvioMail - Errore:  " + e.getMessage());
							}
						}
					}
				}
			}

		} catch (Exception e) {
			LOGGER.error("[EpayToRiscaWS::" + FASE_PP + "] Eccezione generica: " + e.getMessage());
		}

		/*
		 * Restituisco sempre esito ok, da analisi non sono previsti errori di ritorno a
		 * pagopa gli errori sono gestiti sul nostro db
		 */
		ResponseType response = new ResponseType();
		ResultType result = new ResultType();
		result.setCodice(COD_OK_PAGOPA);
		response.setResult(result);

		LOGGER.debug("[EpayToRiscaWS::trasmettiNotifichePagamento] END");
		return response;
	}

	@WebResult(name = "EPaywsoServiceResponse", targetNamespace = "http://www.csi.it/epay/epaywso/types", partName = "parameters")
	@WebMethod(operationName = "EsitoAggiornaPosizioniDebitorie", action = "http://www.csi.it/epay/epaywso/service/EsitoAggiornaPosizioniDebitorie")
	public ResponseType EsitoAggiornaPosizioniDebitorie(
			@WebParam(partName = "parameters", name = "EsitoAggiornaPosizioniDebitorieRequest", targetNamespace = "http://www.csi.it/epay/epaywso/epaywso2enti/types") EsitoAggiornaPosizioniDebitorieRequest parameters)
			throws Exception {

		LOGGER.debug("[EpayToRiscaWS::EsitoAggiornaPosizioniDebitorie] BEGIN");

		attore = ATTORE_SC;
		codFase = COD_FASE_RICALCOLO_IUV;

		String codiceVersamento = parameters.getTestataEsito().getCodiceVersamento();
		String cfEnteCreditore = parameters.getTestataEsito().getCFEnteCreditore();
		String codEsitoAcquisizione = parameters.getResult().getCodice();
		String descEsitoAcquisizione = parameters.getResult().getMessaggio();
		nomeLotto = parameters.getTestataEsito().getIdMessaggio();
		// Annoto la data e ora di acquisizione del lotto (per la mail)
		dataAcquisizione = new Date();

		EntePagopaDTO ente = entePagopaDAO.loadEntePagopaByCodVersamentoEnteCreditore(codiceVersamento, cfEnteCreditore,
				true);

		// Ottengo id_ambito
		idAmbito = "" + ente.getIdAmbito();

		// Cerco l' elaborazione in corso in base al nome lotto
		boolean ok = verificaElaborazioneSCByNomeLotto(nomeLotto);
		if (ok) {
			// Verifica che il lotto ricevuto sia di RISCA
			List<LottoDTO> lotti = lottoDAO.loadLottoInviatoByName(idElabora, nomeLotto);
			if (lotti != null && lotti.size() > 0) {

				logStep(idElabora, PASSO_LOTTO_RISCA_OK, 0,
						FASE_SC + " - Lotto Ricevuto e' di RISCA - Lotto: " + nomeLotto);

				// Ricavare il numero di lotti attesi
				List<LottoDTO> listaLottiAttesi = lottoDAO.findLottiAttesi(idElabora);
				int lottiAttesi = listaLottiAttesi.size();
				List<Long> idLottiAttesi = (List<Long>) listaLottiAttesi.stream().map(LottoDTO::getIdLotto)
						.collect(Collectors.toList());

				if (!codEsitoAcquisizione.equals("000")) {
					// Acquisizione del lotto fallita, registro codice e edescrizione ma proseguo a
					// verificare l'esito per ogni posizione debitoria
					lottoDAO.updateFlgRicevutoElaboratoByNomeLotto(idElabora, nomeLotto, 1, 0, codEsitoAcquisizione,
							descEsitoAcquisizione);
				}

				int numPosizioniDebitorieRicevute = 0;
				List<PosizioneDebitoriaType> posizioniDebitorie = parameters.getEsitoAggiornamento()
						.getElencoPosizioniDebitorieAggiornate().getPosizioneDebitoriaAggiornata();

				boolean lottoOk = true;
				boolean napOk = true;
				for (int i = 0; i < posizioniDebitorie.size(); i++) {
					PosizioneDebitoriaType posDeb = posizioniDebitorie.get(i);
					if (!posDeb.getCodiceEsito().equalsIgnoreCase(COD_OK_PAGOPA) &&
							!posDeb.getCodiceEsito().equalsIgnoreCase(COD_178_PAGOPA)) {
						lottoOk = false;
						napOk = false;
					} else {
						napOk = true;
						// Se e' un codice 178 inserisco una riga di WARNING nel registro
						if (posDeb.getCodiceEsito().equalsIgnoreCase(COD_178_PAGOPA)) {
							logStep(idElabora, PASSO_RICALCOLO_IUV, 0,
									FASE_SC + " - WARNING - Esito anomalo per il NAP: "
											+ posDeb.getIdPosizioneDebitoria() + " ESITO: " + posDeb.getCodiceEsito()
											+ " " + posDeb.getDescrizioneEsito());
						}
					}

					// settare l'esito nella RISCA_R_PAGOPA_LISTA_CARICO
					Long idLotto = lotti.get(0).getIdLotto();
					aggiornaEsitoListaCarico(posDeb, idLotto);
					if (napOk) {
						// aggiornare la tabella RISCA_T_IUV
						boolean result = updateIuv(codiceVersamento, posDeb, idLotto);
						if (result) {
							numPosizioniDebitorieRicevute++;
						} else {
							// Errore di aggiornamento dello IUV per la posizione debitoria corrente
							ok = false;
						}
					} else {
						logStep(idElabora, PASSO_RICALCOLO_IUV, 1,
								FASE_SC + " - ricevuto un esito negativo aggiornamento IUV per l'elaborazione: "
										+ idElabora + "NAP: " + posDeb.getIdPosizioneDebitoria() + " ESITO: "
										+ posDeb.getCodiceEsito() + " " + posDeb.getDescrizioneEsito());
					}
				}

				if (ok) {
					// Aggiornare la tabella RISCA_T_LOTTO
					int flgRicevuto = 1;
					int flgElaborato = lottoOk ? 1 : 0;
					Integer ret = lottoDAO.updateFlgRicevutoElaboratoByNomeLotto(idElabora, nomeLotto, flgRicevuto,
							flgElaborato, codEsitoAcquisizione, descEsitoAcquisizione);
					if (ret != null && ret > 0) {
						// logStep(idElabora, PASSO_RICALCOLO_IUV, 0,
						// FASE_SC + " - Aggiorna Flag Ricevuto Lotto OK : " + idElabora + " Lotto: " +
						// nomeLotto);

						// Ricavo il numero di lotti ricevuti
						int lottiRicevuti = lottoDAO.findLottiRicevuti(idElabora, idLottiAttesi).size();

						if (lottiAttesi == lottiRicevuti) {

							// Tutti i lotti di una elaborazione sono stati acquisiti
							// Indipendentemente dall'esito ricevuto metto in STACONT_OK l'elaborazione
							// Non faccio il controllo dei lotti anomali come invece avviene in
							// bollettazione

							// Faccio un controllo sulla presenza di registrazioni con esito 1 (KO) sul
							// registro elabora e in base a quello imposo lo stato dell'elaborazione
							// STACONT_OK se non ci sono anomalie
							// STACONT_KO se esiste almeno una riga con esito 1
							List<RegistroElaboraExtendedDTO> listaReg = registroElaboraDAO
									.loadRegistroElaboraByElaboraAndAmbito("" + idElabora, "" + idAmbito, 1, null);
							String statoElabora = STATO_ELAB_STACONT_OK;
							if (listaReg != null && listaReg.size() > 0) {
								statoElabora = STATO_ELAB_STACONT_KO;
							}
							aggiornaElabStatoCont(statoElabora);

							/*
							 * int lottiAnomali = lottoDAO.findLottiAnomali(idElabora,
							 * idLottiAttesi).size(); if (lottiAnomali > 0) { logStep(idElabora,
							 * PASSO_LOTTO_ANOMALO, 0, FASE_SC +
							 * " - Esistono lotti non elaborati correttamente (errore su alcuni NAP) per l'elaborazione con idElabora: "
							 * + idElabora); } else { // Tutti i lotti di una elaborazione sono stati
							 * acquisiti senza errori su tutti // i NAP aggiornaElabStatoContOK(); }
							 */
						}

						// Il servizio termina con l'invio della mail di
						// riepilogo

						try {
							invioMailLottoAcquisito(FASE_SC, COD_EMAIL_E_MODIFICA_IUV, lottiRicevuti,
									numPosizioniDebitorieRicevute);
						} catch (MailException e) {
							LOGGER.error(
									"[EpayToRiscaWS::" + FASE_SC + "::InvioMail] Errore invio mail: " + e.getMessage());
							logStep(idElabora, PASSO_INVIO_MAIL_SERVIZIO, 1,
									FASE_SC + " - InvioMail - Errore:  " + e.getMessage());
						}

					} else {
						logStep(idElabora, PASSO_RICALCOLO_IUV, 1,
								FASE_SC + " - Aggiorna Flag Ricevuto Lotto KO : " + idElabora + " Lotto: " + nomeLotto);
						terminaElabConErroreSC("Errore in acquisizione del lotto " + nomeLotto
								+ ". Fallito aggiornamento record in RISCA_T_LOTTO");
					}
				} else {
					logStep(idElabora, PASSO_RICALCOLO_IUV, 1,
							FASE_SC + " - Errore in acquisizione del lotto - Lotto: " + nomeLotto);
					invioMailErroreSC("Errore in acquisizione del lotto " + nomeLotto + ". Errore aggiornamento IUV. ",
							null, null);
				}
			} else {
				/*
				 * logStep(idElabora, PASSO_RICALCOLO_IUV, 1, FASE_SC +
				 * " - Lotto Ricevuto NON e' di RISCA oppure e' un lotto gia' ricevuto - Lotto: " + nomeLotto);
				 */
				LOGGER.error(FASE_SC + "Errore in acquisizione del lotto " + nomeLotto
						+ ". Lotto gia' acquisito id_elabora = " + elabora.getIdElabora());
				invioMailErroreSC("Errore in acquisizione del lotto " + nomeLotto
						+ ". Lotto gia' acquisito id_elabora = " + elabora.getIdElabora(),
						null, null);
			}
		}

		/*
		 * Restituisco sempre esito ok, da analisi non sono previsti errori di ritorno a
		 * pagopa gli errori sono gestiti sul nostro db
		 */
		ResponseType response = new ResponseType();
		ResultType result = new ResultType();
		result.setCodice(COD_OK_PAGOPA);
		response.setResult(result);

		LOGGER.debug("[EpayToRiscaWS::EsitoAggiornaPosizioniDebitorie] END");
		return response;

	}

	private void insertRpPagopa(NotificaPagamentoType notificaPagamento) {
		RpPagopaDTO pagopaDto = new RpPagopaDTO();
		try {
			pagopaDto.setIdElabora(idElabora);
			pagopaDto.setPosizDebitoria(notificaPagamento.getIdPosizioneDebitoria());
			if (notificaPagamento.getAnnoDiRiferimento() != null) {
				pagopaDto.setAnno(notificaPagamento.getAnnoDiRiferimento());
			}
			pagopaDto.setIuv(notificaPagamento.getIUV());
			pagopaDto.setImportoPagato(notificaPagamento.getImportoPagato());
			if (notificaPagamento.getDataScadenza() != null) {
				pagopaDto.setDataScadenza(notificaPagamento.getDataScadenza().toGregorianCalendar().getTime());
			}
			pagopaDto.setCausale(notificaPagamento.getDescrizioneCausaleVersamento());
			if (notificaPagamento.getDataEsitoPagamento() != null) {
				pagopaDto.setDataEsitoPagamento(
						notificaPagamento.getDataEsitoPagamento().toGregorianCalendar().getTime());
			}
			if (notificaPagamento.getSoggettoDebitore() != null) {
				String cognRsocVersante = null;
				String nomeVersante = null;
				if (notificaPagamento.getSoggettoDebitore().getPersonaFisica() != null) {
					cognRsocVersante = notificaPagamento.getSoggettoDebitore().getPersonaFisica().getCognome();
					nomeVersante = notificaPagamento.getSoggettoDebitore().getPersonaFisica().getNome();
				} else {
					if (notificaPagamento.getSoggettoDebitore().getPersonaGiuridica() != null) {
						cognRsocVersante = notificaPagamento.getSoggettoDebitore().getPersonaGiuridica()
								.getRagioneSociale();
					}
				}
				pagopaDto.setCognRsocDebitore(cognRsocVersante);
				pagopaDto.setNomeDebitore(nomeVersante);
				pagopaDto.setCfPiDebitore(notificaPagamento.getSoggettoDebitore().getIdentificativoUnivocoFiscale());
			}
			if (notificaPagamento.getDatiTransazionePSP() != null) {
				pagopaDto.setImportoTransitato(notificaPagamento.getDatiTransazionePSP().getImportoTransato());
				pagopaDto.setImportoCommissioni(notificaPagamento.getDatiTransazionePSP().getImportoCommissioni());
			}
			pagopaDto.setCodiceAvviso(notificaPagamento.getCodiceAvviso());
			pagopaDto.setNote(notificaPagamento.getNote());
			rpPagopaDAO.saveRpPagopaDTO(pagopaDto);
			logStep(idElabora, PASSO_INS_PAGAM_IN_WORK, 0,
					FASE_PP + " - Step 2 - Inserimento Pagamenti in Working OK per l'elaborazione: " + idElabora
							+ " - IUV: " + notificaPagamento.getIUV());
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("[EpayToRiscaWS::trasmettiNotifichePagamento] Error inserimento risca_w_rp_pagopa: "
					+ e.getMessage());
			logStep(idElabora, PASSO_INS_PAGAM_IN_WORK, 1,
					FASE_PP + " - Step 2 - Inserimento Pagamenti in Working KO per l'elaborazione: " + idElabora
							+ " - IUV: " + notificaPagamento.getIUV());
		}
	}

	private ElaboraDTO insertElaborazionePP(Long idAmbito) {
		ElaboraDTO elaboraDto = new ElaboraDTO();
		try {
			AmbitoDTO ambito = new AmbitoDTO();
			ambito.setIdAmbito(idAmbito);
			elaboraDto.setAmbito(ambito);
			elaboraDto.setDataRichiesta(new Date());
			elaboraDto.setGestAttoreIns(ATTORE_PP);
			elaboraDto.setGestAttoreUpd(ATTORE_PP);
			StatoElaborazioneDTO statoElab = new StatoElaborazioneDTO();
			statoElab.setCodStatoElabora(STATO_ELAB_PAGPGPA_ON);
			elaboraDto.setStatoElabora(statoElab);
			TipoElaboraExtendedDTO tipoElab = new TipoElaboraExtendedDTO();
			tipoElab.setCodTipoElabora(TIPO_ELAB_PP);
			elaboraDto.setTipoElabora(tipoElab);
			// Questa elaborazione non ha parametri ma occorre settare comunque un array
			// vuoto
			elaboraDto.setParametri(new ArrayList<>());
			elaboraDto = elaboraDAO.saveElabora(elaboraDto);
			if (elaboraDto != null && elaboraDto.getIdElabora() != null) {
				// Rileggo il dto per avere tutto il dto popolato con anche gli oggetti ambito e
				// tipoElabora
				elaboraDto = elaboraDAO.loadElaboraById(elaboraDto.getIdElabora(), false);
			}
		} catch (Exception e) {
			LOGGER.error("[EpayToRiscaWS::trasmettiNotifichePagamento] Errore inserimento Elaborazione.");
		}
		return elaboraDto;
	}

	private boolean aggiornaEsitoListaCarIuv(PosizioneDebitoriaType posDeb) throws DAOException {
		String codEsito = posDeb.getCodiceEsito();
		String descEsito = posDeb.getDescrizioneEsito();
		// NB. tronco la descrizione dell'esito a 200 caratteri che e' il massimo della
		// colonna sul DB
		if (descEsito.length() > 200) {
			descEsito = descEsito.substring(0, 200);
		}

		Integer res = pagopaListaCarIuvDAO.updateEsitoListaCarIuvByNap(codEsito, descEsito, posDeb.getCodiceAvviso(),
				posDeb.getIUV(), posDeb.getIdPosizioneDebitoria());
		if (res != null && res > 0) {
			// logStep(idElabora, PASSO_SET_IUV_LISTA_CAR, 0, FASE + " - Aggiornamento Lista
			// Car IUV ed Esito OK - Elab: "
			// + idElabora + " NAP: " + posDeb.getIdPosizioneDebitoria() + " IUV: " +
			// posDeb.getIUV());
			return true;
		} else {
			logStep(idElabora, PASSO_SET_IUV_LISTA_CAR, 1, FASE + " - Aggiornamento Lista Car IUV ed Esito KO - Elab: "
					+ idElabora + " NAP: " + posDeb.getIdPosizioneDebitoria() + " IUV: " + posDeb.getIUV());
			return false;
		}
	}

	private boolean aggiornaEsitoListaCarico(PosizioneDebitoriaType posDeb, Long idLotto) throws DAOException {
		String codEsito = posDeb.getCodiceEsito();
		String descEsito = posDeb.getDescrizioneEsito();
		// NB. tronco la descrizione dell'esito a 200 caratteri che e' il massimo della
		// colonna sul DB
		if (descEsito.length() > 200) {
			descEsito = descEsito.substring(0, 200);
		}
		Integer res = pagopaListaCaricoDAO.updateEsitoListaCaricoByNap(codEsito, descEsito,
				posDeb.getIdPosizioneDebitoria(), idLotto, attore);
		if (res != null && res > 0) {
//			logStep(idElabora, PASSO_RICALCOLO_IUV, 0, FASE_SC + " - Aggiornamento Esito PagopaListaCarico OK - Elab: "
//					+ idElabora + " NAP: " + posDeb.getIdPosizioneDebitoria());
			return true;
		} else {
			logStep(idElabora, PASSO_RICALCOLO_IUV, 1, FASE_SC + " - Aggiornamento Esito PagopaListaCarico KO - Elab: "
					+ idElabora + " NAP: " + posDeb.getIdPosizioneDebitoria());
			return false;
		}
	}

	private boolean saveIuv(String codiceVersamento, PosizioneDebitoriaType posDeb) {
		// Leggo listaCarIuv per recuperare l'importo
		List<PagopaListaCarIuvDTO> listaCarIuv = pagopaListaCarIuvDAO
				.loadPagopaListaCarIuvByNap(posDeb.getIdPosizioneDebitoria());
		if (listaCarIuv != null && listaCarIuv.size() > 0) {
			PagopaListaCarIuvDTO listaCarIuvDto = listaCarIuv.get(0);
			try {
				IuvDTO dto = new IuvDTO();
				dto.setNap(posDeb.getIdPosizioneDebitoria());
				StatoIuvDTO statoIuv = new StatoIuvDTO();
				statoIuv.setIdStatoIuv(Long.valueOf(6));
				dto.setStatoIuv(statoIuv);
				dto.setIuv(posDeb.getIUV());
				dto.setCodiceAvviso(posDeb.getCodiceAvviso());
				dto.setImporto(listaCarIuvDto.getImporto());
				dto.setCodiceVersamento(codiceVersamento);
				dto.setGestAttoreIns(attore);
				dto.setGestAttoreUpd(attore);
				dto = iuvDAO.saveIuv(dto);
				if (dto.getIdIuv() != null) {
					logStep(idElabora, PASSO_SET_INSERT_IUV, 0, FASE + " - Inserimento IUV OK per l'elaborazione: "
							+ idElabora + " NAP: " + posDeb.getIdPosizioneDebitoria() + " IUV: " + posDeb.getIUV());
					return true;
				} else {
					logStep(idElabora, PASSO_SET_INSERT_IUV, 1, FASE + " - Inserimento IUV KO per l'elaborazione: "
							+ idElabora + "NAP: " + posDeb.getIdPosizioneDebitoria() + " IUV: " + posDeb.getIUV());
					return false;
				}
			} catch (Exception e) {
				logStep(idElabora, PASSO_SET_INSERT_IUV, 1, FASE + " - Inserimento IUV KO per l'elaborazione: "
						+ idElabora + "NAP: " + posDeb.getIdPosizioneDebitoria() + " IUV: " + posDeb.getIUV());
				return false;
			}
		}
		return false;
	}

	private boolean updateIuv(String codiceVersamento, PosizioneDebitoriaType posDeb, Long idLotto) {
		boolean result = true;
		PagopaListaCaricoDTO listaCarico = pagopaListaCaricoDAO.loadPagopaListaCaricoByLottoNap(idLotto,
				posDeb.getIdPosizioneDebitoria());
		if (listaCarico != null) {
			if (listaCarico.getIndTipoAggiornamento().equalsIgnoreCase("A")) {
				iuvDAO.updateStatoIuvByNap(posDeb.getIdPosizioneDebitoria(), COD_STATO_IUV_ANN, attore);
			} else {
				iuvDAO.updateImportoIuvByNap(posDeb.getIdPosizioneDebitoria(), listaCarico.getImportoNew(), attore);
			}
		} else {
			logStep(idElabora, PASSO_RICALCOLO_IUV, 1,
					FASE_SC + " - UpdateIuv KO - idLotto: " + idLotto + " NAP: " + posDeb.getIdPosizioneDebitoria());
			result = false;
		}
		return result;
	}

	private boolean verificaElaborazioneByNomeLotto(String nomeLotto) {
		// L'id_elabora si trova nel nome lotto in quarta posizione rispetto a caratteri
		// '_' --> Esempio RISCAC002_20240902_1227_1356_01
		try {
			idElabora = Long.parseLong(nomeLotto.split("_")[3]);
		} catch (NumberFormatException e) {
			// Se il formato del nome lotto e' diverso e non riesco a parsificarlo il
			// servizio termina con errore
			LOGGER.error(
					"[EpayToRiscaWS::esitoInserimentoListaDiCarico] Errore parsificazione nome lotto: " + nomeLotto);
			return false;
		}
		LOGGER.debug(
				"[EpayToRiscaWS::esitoInserimentoListaDiCarico] Ricerca elaborazione per id_elabora = " + idElabora);
		// Cerco l'elaborazione di riferimento
		elabora = elaboraDAO.loadElaboraById(idElabora, false);
		if (elabora == null) {
			LOGGER.error("[EpayToRiscaWS::esitoInserimentoListaDiCarico] Elaborazione non trovata per id_elabora = "
					+ idElabora + "  - nomeLotto = " + nomeLotto);
			return false;
		}
		LOGGER.debug("[EpayToRiscaWS::esitoInserimentoListaDiCarico] Ricerca elaborazione OK");
		// L'elaborazione deve essere in stato ATTESA_IUV
		if (!elabora.getStatoElabora().getCodStatoElabora().equals(STATO_ELAB_ATTESAIUV)) {
			LOGGER.error(
					"[EpayToRiscaWS::esitoInserimentoListaDiCarico] Elaborazione non in stato ATTESAIUV per id_elabora = "
							+ idElabora + "  - nomeLotto = " + nomeLotto);
			return false;
		}
		logStep(idElabora, PASSO_VERIF_PRESENZA, 0, FASE + " - Verifica Presenza Elaborazione OK");
		return true;
	}

	private boolean verificaElaborazioneSCByNomeLotto(String nomeLotto) {
		// L'id_elabora si trova nel nome lotto in quarta posizione rispetto a caratteri
		// '_' --> Esempio RISCAC002_20240902_1227_1356_01

		try {
			idElabora = Long.parseLong(nomeLotto.split("_")[3]);
		} catch (NumberFormatException e) {
			// Se il formato del nome lotto e' diverso e non riesco a parsificarlo il
			// servizio termina con errore
			LOGGER.error(
					"[EpayToRiscaWS::EsitoAggiornaPosizioniDebitorie] Errore parsificazione nome lotto: " + nomeLotto);
			return false;
		}
		LOGGER.debug(
				"[EpayToRiscaWS::EsitoAggiornaPosizioniDebitorie] Ricerca elaborazione per id_elabora = " + idElabora);
		// Cerco l'elaborazione di riferimento
		elabora = elaboraDAO.loadElaboraById(idElabora, false);
		if (elabora == null) {
			LOGGER.error("[EpayToRiscaWS::EsitoAggiornaPosizioniDebitorie] Elaborazione non trovata per id_elabora = "
					+ idElabora + "  - nomeLotto = " + nomeLotto);
			return false;
		}
		LOGGER.debug("[EpayToRiscaWS::EsitoAggiornaPosizioniDebitorie] Ricerca elaborazione OK");
		// L'elaborazione deve essere in stato STACONT_ON
		if (!elabora.getStatoElabora().getCodStatoElabora().equals(STATO_ELAB_STACONT_ON)) {
			LOGGER.error(
					"[EpayToRiscaWS::EsitoAggiornaPosizioniDebitorie] Elaborazione non in stato STACONT_ON per id_elabora = "
							+ idElabora + "  - nomeLotto = " + nomeLotto);
			return false;
		}
		logStep(idElabora, PASSO_VERIF_PRESENZA, 0, FASE + " - Verifica Presenza Elaborazione OK");
		return true;
	}

	private boolean verificaElaborazioneInCorso(String idAmbito) {
		// List<String> codTipoElabora = Arrays.asList(TIPO_ELAB_BS);
		// Devo cercare una elaborazione in stato ATTESAIUV senza guardare il tipo
		// (BO/BS). Deve essere garantito che non ci siano elaborazioni BO e BS
		// contemporaneamente in corso in stato ATTESAIUV
		List<String> codStatoElabora = Arrays.asList(STATO_ELAB_IUVOK, STATO_ELAB_ATTESAPDF, STATO_ELAB_PDFPRONTI,
				STATO_ELAB_PDFOK, STATO_ELAB_MAILOK);
		List<ElaboraDTO> elaborazioni = elaboraDAO.loadElabora(idAmbito, null, codStatoElabora, null, null, null, null,
				null, null, null);
		if (elaborazioni.size() == 0) {
			logStep(idElabora, PASSO_VERIF_IN_ESECUZIONE, 0,
					FASE + " - Verifica presenza elaborazioni uguali in corso OK");
			return true;
		} else {
			String listaElaborazioni = "";
			for (ElaboraDTO elab : elaborazioni) {
				listaElaborazioni += elab.getIdElabora() + " ";
			}
			logStep(idElabora, PASSO_VERIF_IN_ESECUZIONE, 1,
					FASE + " - Verifica presenza elaborazioni uguali in corso KO - Procedura in esecuzione con ID: "
							+ listaElaborazioni);
			return false;
		}
	}

	private void logStep(Long idElabora, String codPasso, int esito, String note) {
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
			LOGGER.error("[EpayToRiscaWS::logStep] ", e);
		}
		LOGGER.debug(note);
	}

	private void terminaElabConErrore(String messaggio) {
		StatoElaborazioneDTO statoErr = new StatoElaborazioneDTO();
		statoErr.setCodStatoElabora(STATO_TERMIN_KO);
		elabora.setStatoElabora(statoErr);
		elabora.setGestAttoreUpd(attore);
		try {
			elaboraDAO.updateElabora(elabora);
			invioMailErrore(messaggio, null, null);
		} catch (MailException e) {
			LOGGER.error("[EpayToRiscaWS::" + FASE + "::InvioMail] Errore invio mail: " + e.getMessage());
			logStep(idElabora, PASSO_INVIO_MAIL_SERVIZIO, 1, FASE + " - InvioMail - Errore:  " + e.getMessage());
		} catch (Exception e) {
			LOGGER.error("[EpayToRiscaWS::terminaElabConErrore] ERROR", e);
		}
	}

	private void terminaElabConErroreSC(String messaggio) {
		StatoElaborazioneDTO statoErr = new StatoElaborazioneDTO();
		statoErr.setCodStatoElabora(STATO_ELAB_STACONT_KO);
		elabora.setStatoElabora(statoErr);
		elabora.setGestAttoreUpd(attore);
		try {
			elaboraDAO.updateElabora(elabora);
			invioMailErroreSC(messaggio, null, null);
		} catch (MailException e) {
			LOGGER.error("[EpayToRiscaWS::" + FASE + "::InvioMail] Errore invio mail: " + e.getMessage());
			logStep(idElabora, PASSO_INVIO_MAIL_SERVIZIO, 1, FASE + " - InvioMail - Errore:  " + e.getMessage());
		} catch (Exception e) {
			LOGGER.error("[EpayToRiscaWS::terminaElabConErroreSC] ERROR", e);
		}
	}

	private void terminaElabConErrorePP() {
		StatoElaborazioneDTO statoErr = new StatoElaborazioneDTO();
		statoErr.setCodStatoElabora(STATO_ELAB_PAGPGPA_KO);
		elabora.setStatoElabora(statoErr);
		elabora.setGestAttoreUpd(attore);
		try {
			elaboraDAO.updateElabora(elabora);
		} catch (Exception e) {
			LOGGER.error("[EpayToRiscaWS::terminaElabConErrorePP] ERROR", e);

		}
	}

	private void aggiornaElabIuvOK() {
		StatoElaborazioneDTO stato = new StatoElaborazioneDTO();
		stato.setCodStatoElabora(STATO_ELAB_IUVOK);
		elabora.setStatoElabora(stato);
		elabora.setGestAttoreUpd(attore);
		try {
			elaboraDAO.updateElabora(elabora);
		} catch (Exception e) {
			LOGGER.error("[EpayToRiscaWS::aggiornaElabIuvOK] ERROR", e);
		}
	}

	private void aggiornaElabStatoCont(String statoElab) {
		StatoElaborazioneDTO stato = new StatoElaborazioneDTO();
		stato.setCodStatoElabora(statoElab);
		elabora.setStatoElabora(stato);
		elabora.setGestAttoreUpd(ATTORE_SC);
		try {
			elaboraDAO.updateElabora(elabora);
		} catch (Exception e) {
			LOGGER.error("[EpayToRiscaWS::aggiornaElabStatoContOK] ERROR", e);
		}
	}

	private void aggiornaElabPagopaOK() {
		StatoElaborazioneDTO stato = new StatoElaborazioneDTO();
		stato.setCodStatoElabora(STATO_ELAB_PAGPGPA_OK);
		elabora.setStatoElabora(stato);
		elabora.setGestAttoreUpd(ATTORE_PP);
		try {
			elaboraDAO.updateElabora(elabora);
		} catch (Exception e) {
			LOGGER.error("[EpayToRiscaWS::aggiornaElabPagopaOK] ERROR", e);
		}
	}

	private String getOggettoMail(Date dataAcquisizione, String code) {
		EmailServizioDTO emailDto = emailServizioDAO.loadEmailServizioByCodEmail(code);
		String oggetto = emailDto.getOggettoEmailServizio();
		oggetto = StringUtils.replace(oggetto, "[TIPO_ELABORA]", elabora.getTipoElabora().getDesTipoElabora());
		SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT_MAIL);
		SimpleDateFormat df2 = new SimpleDateFormat(HOUR_FORMAT_MAIL);
		oggetto = StringUtils.replace(oggetto, "[ID_ELABORA]", "" + elabora.getIdElabora());
		oggetto = StringUtils.replace(oggetto, "[DATA_RICHIESTA]", df.format(dataAcquisizione));
		oggetto = StringUtils.replace(oggetto, "[ORA_RICHIESTA]", df2.format(dataAcquisizione));
		return oggetto;
	}

	private int invioMailErrore(String messaggio, Integer numLottiAcquisiti, Integer numPosizioniDebitorieRicevute)
			throws MailException {
		boolean bollettazioneSpeciale = elabora.getTipoElabora().getCodTipoElabora().equals(TIPO_ELAB_BS) ? true
				: false;
		String oggetto = getOggettoMail(dataAcquisizione,
				bollettazioneSpeciale ? COD_EMAIL_E_CONFBS_ACQ_IUV_I_BS : COD_EMAIL_E_CONFBS_ACQ_IUV_I_BO);
		String msg = FASE + " - InvioMail ";
		if (numLottiAcquisiti != null && numPosizioniDebitorieRicevute != null) {
			msg += "- Statistiche dati elaborati - Numero lotti acquisiti: " + numLottiAcquisiti
					+ " - Numero posizioni debitorie acquisite: " + numPosizioniDebitorieRicevute;
		} else {
			msg += "- Si e' verificato un errore nell'elaborazione del lotto: " + nomeLotto + ", per idElabora: "
					+ idElabora;
		}
		return invioMail(FASE, oggetto, messaggio, msg);
	}

	private int invioMailErroreSC(String messaggio, Integer numLottiAcquisiti, Integer numPosizioniDebitorieRicevute)
			throws MailException {
		String oggetto = getOggettoMail(dataAcquisizione, COD_EMAIL_E_MODIFICA_IUV);
		String msg = FASE_SC + " - InvioMail ";
		if (numLottiAcquisiti != null && numPosizioniDebitorieRicevute != null) {
			msg += "- Statistiche dati elaborati - Numero lotti acquisiti: " + numLottiAcquisiti
					+ " - Numero posizioni debitorie acquisite: " + numPosizioniDebitorieRicevute;
		} else {
			msg += "- Si e' verificato un errore nell'elaborazione del lotto: " + nomeLotto + ", per idElabora: "
					+ idElabora;
		}
		return invioMail(FASE_SC, oggetto, messaggio, msg);
	}
	
	private boolean isInvioMailLottoAbilitato(String idAmbito) throws SQLException {
		String flgInvioMail = "N";
		try {
			String codAmbito = ambitiDAO.loadAmbitoByIdAmbito(Long.valueOf(idAmbito)).getCodAmbito();
			List<AmbitoConfigDTO> config = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(codAmbito,
					AMBITO_CONFIG_CHIAVE_INVIO_MAIL);
			if (config != null && config.size() > 0) {
				flgInvioMail = config.get(0).getValore();
			}
		} catch (Exception e) {
			LOGGER.error("[EpayToRiscaWS::trasmettiNotifichePagamento::isInvioMailAbilitato] ERROR " + e.getMessage());
		}
		return flgInvioMail.equals("S");
	}

	private int invioMailLottoAcquisito(String fase, String code, Integer numLottiAcquisiti,
			Integer numPosizioniDebitorieRicevute) throws MailException {
		String oggetto = getOggettoMail(dataAcquisizione, code);

		StringBuilder testo = new StringBuilder();
		testo.append(
				"Nel file di testo in allegato si riportano, per l'elaborazione in oggetto, i passi eseguiti ed il loro esito. \n");
		testo.append("Lotto ricevuto: " + nomeLotto);
		testo.append("\n");
		testo.append("Numero posizioni debitorie acquisite: " + numPosizioniDebitorieRicevute);
		testo.append("\n");

		String msg = fase + " - InvioMail ";
		if (numLottiAcquisiti != null && numPosizioniDebitorieRicevute != null) {
			msg += "- Statistiche dati elaborati - Numero lotti acquisiti: " + numLottiAcquisiti
					+ " - Numero posizioni debitorie acquisite: " + numPosizioniDebitorieRicevute;
		} else {
			msg += "- Si e' verificato un errore nell'elaborazione del lotto: " + nomeLotto + ", per idElabora: "
					+ idElabora;
		}

		return invioMail(fase, oggetto, testo.toString(), msg);
	}

	private int invioMailPagamElaborati(String code, ElaborazionePagamentiResultDTO elabResult) throws MailException {
		String oggetto = getOggettoMail(dataAcquisizione, code);

		StringBuilder testo = new StringBuilder();
		testo.append(
				"Nel file di testo in allegato si riportano, per l'elaborazione in oggetto, i passi eseguiti ed il loro esito. \n");
		testo.append("Pagamenti letti: " + elabResult.getNumPagamLetti());
		testo.append("\n");
		testo.append("Pagamenti caricati: " + elabResult.getNumPagamCaric());
		testo.append("\n");
		testo.append("Pagamenti scartati: " + elabResult.getNumPagamScart());
		testo.append("\n");

		String logMessage = FASE_PP + " - InvioMail - Statistiche dati elaborati - Numero Pagamenti Letti: "
				+ elabResult.getNumPagamLetti() + " - Numero Pagamenti Caricati: " + elabResult.getNumPagamCaric()
				+ " - Numero Pagamenti Scartati:  " + elabResult.getNumPagamScart();

		return invioMail(FASE_PP, oggetto, testo.toString(), logMessage);
	}

	private int invioMail(String fase, String oggetto, String testo, String logMessage) throws MailException {
		LOGGER.debug("[EpayToRiscaWS::" + fase + "::InvioMail] BEGIN");
		int stepStatus = 0;

		AmbitoDTO ambito = ambitiDAO.loadAmbitoByIdAmbito(Long.parseLong(idAmbito));

		String mailDest = null;
		String mailMitt = null;
		String user = null;
		String password = null;
		String nomeAllegato = null;
		List<AmbitoConfigDTO> ambitoConfig;
		try {
			ambitoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(ambito.getCodAmbito(),
					AMBITO_CONFIG_CHIAVE_MAIL_DEST);
			if (ambitoConfig.size() > 0) {
				mailDest = ambitoConfig.get(0).getValore();
				ambitoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(ambito.getCodAmbito(),
						AMBITO_CONFIG_CHIAVE_MAIL_MITT);
				if (ambitoConfig.size() > 0) {
					mailMitt = ambitoConfig.get(0).getValore();
					LOGGER.debug("[EpayToRiscaWS::" + fase + "::InvioMail] mailDest = " + mailDest);
					LOGGER.debug("[EpayToRiscaWS::" + fase + "::InvioMail] mailMitt = " + mailMitt);

					user = ambitiConfigDAO
							.loadAmbitiConfigByCodeAndKey(ambito.getCodAmbito(), AMBITO_CONFIG_CHIAVE_MAIL_USERNAME)
							.get(0).getValore();
					password = ambitiConfigDAO
							.loadAmbitiConfigByCodeAndKey(ambito.getCodAmbito(), AMBITO_CONFIG_CHIAVE_MAIL_PASSWORD)
							.get(0).getValore();

					nomeAllegato = ambitiConfigDAO
							.loadAmbitiConfigByCodeAndKey(ambito.getCodAmbito(), AMBITO_CONFIG_CHIAVE_NOME_ALLEGATO)
							.get(0).getValore();

					logStep(idElabora, PASSO_INVIO_MAIL_SERVIZIO, 0, logMessage);

				} else {
					logStep(idElabora, PASSO_INVIO_MAIL_SERVIZIO, 1, fase
							+ " - InvioMail - La mail del mittente per le comunicazioni al Servizio Applicativo non e' stata configurata per l'ambito  "
							+ ambito.getCodAmbito());
				}
			} else {
				logStep(idElabora, PASSO_INVIO_MAIL_SERVIZIO, 1, FASE
						+ " - InvioMail - La mail del destinatario del Servizio Applicativo non e' stata configurata per l'ambito "
						+ ambito.getCodAmbito());
			}
		} catch (SQLException e) {
			logStep(idElabora, PASSO_INVIO_MAIL_SERVIZIO, 1,
					fase + " - InvioMail - Configurazione mail Servizio Applicativo non corretta per ambito: "
							+ ambito.getCodAmbito());
		}

		List<RegistroElaboraExtendedDTO> registroElaborazioni = null;
		try {
			registroElaborazioni = registroElaboraDAO.loadRegistroElaboraByElaboraAndAmbito("" + idElabora, idAmbito,
					null, codFase);
		} catch (Exception e1) {
			LOGGER.error("[EpayToRiscaWS::invioMail] ", e1);
		}
		LOGGER.debug("[EpayToRiscaWS::" + fase + "::InvioMail] invioMail - trovate " + registroElaborazioni.size()
				+ " registrazioni");

		try {
			ambitoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(ambito.getCodAmbito(),
					AMBITO_CONFIG_CHIAVE_NOME_ALLEGATO);
		} catch (SQLException e) {
			LOGGER.error("[EpayToRiscaWS::" + fase + "::InvioMail] loadAmbitiConfigByCodeAndKey - errore per ambito  "
					+ ambito.getCodAmbito() + " e chiave " + AMBITO_CONFIG_CHIAVE_NOME_ALLEGATO);
		}

		MailInfo mailInfo = prepareMail(oggetto, testo, registroElaborazioni, mailDest, mailMitt, nomeAllegato, user,
				password);
		mailManager.sendMail(mailInfo);

		return stepStatus;
	}

	private MailInfo prepareMail(String oggetto, String testo, List<RegistroElaboraExtendedDTO> registroElaborazioni,
			String mailDest, String mailMitt, String nomeAllegato, String user, String password) {
		MailInfo mailInfo = new MailInfo();
		mailInfo.setDestinatario(mailDest);
		mailInfo.setMittente(mailMitt);
		mailInfo.setHost(mailManager.getMailHost());
		mailInfo.setPort(mailManager.getMailPort());
		mailInfo.setUsername(user);
		mailInfo.setPassword(password);
		mailInfo.setOggetto(oggetto);
		mailInfo.setTesto(testo);

		StringBuffer txtElencoElab = new StringBuffer();
		for (RegistroElaboraExtendedDTO elab : registroElaborazioni) {
			txtElencoElab.append(elab.getDesPassoElabora());
			txtElencoElab.append(" - Esito: ");
			txtElencoElab.append(elab.getFlgEsitoElabora() == 0 ? "OK" : "Errore");
			txtElencoElab.append(" - ");
			txtElencoElab.append(elab.getNotaElabora());
			txtElencoElab.append("\n");
		}

		AttachmentData attachment = new AttachmentData();
		SimpleDateFormat df2 = new SimpleDateFormat(DATE_FORMAT);
		String filename = nomeAllegato.replace("{dataElaborazione}", df2.format(elabora.getDataRichiesta()));
		attachment.setFilename(filename);
		attachment.setData(txtElencoElab.toString().getBytes());
		attachment.setMimeType("text/plain");

		mailInfo.setAttachments(new AttachmentData[] { attachment });

		return mailInfo;
	}

	private String getCodFase(String tipoElab) {
		switch (tipoElab) {
		case BollUtils.COD_TIPO_ELABORA_BS:
			return COD_FASE_CONFBS_ACQ_IUV;
		case BollUtils.COD_TIPO_ELABORA_BG:
			return COD_FASE_CONFBG_ACQ_IUV;
		case BollUtils.COD_TIPO_ELABORA_BO:
			return COD_FASE_CONFBO_ACQ_IUV;
		default:
			return null;
		}
	}

}

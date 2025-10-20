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
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
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

import it.csi.risca.riscabesrv.business.be.AvvisiSollecitiApi;
import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.impl.dao.AccertamentoDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.AvvisoDatiTitolareDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.CalcoloInteresseDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.IuvDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.OutputDatiDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.PagamentoDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RegistroElaboraDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.SoggettiDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.SollDatiAmministrDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.SollDatiPagopaDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.SollDatiTitolareDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.SollDettVersDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.SpedizioneDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.StatiDebitoriAvvisiSollecitiDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.StatoDebitorioDAO;
import it.csi.risca.riscabesrv.dto.AccertamentoDTO;
import it.csi.risca.riscabesrv.dto.AvvisoDatiTitolareDTO;
import it.csi.risca.riscabesrv.dto.AvvisoSollecitoCheckSDResultDTO;
import it.csi.risca.riscabesrv.dto.AvvisoSollecitoCheckSoggettoResultDTO;
import it.csi.risca.riscabesrv.dto.AvvisoSollecitoData;
import it.csi.risca.riscabesrv.dto.AvvisoSollecitoResultDTO;
import it.csi.risca.riscabesrv.dto.IndirizzoSpedizioneDTO;
import it.csi.risca.riscabesrv.dto.IuvDTO;
import it.csi.risca.riscabesrv.dto.OutputDatiDTO;
import it.csi.risca.riscabesrv.dto.RecapitiExtendedDTO;
import it.csi.risca.riscabesrv.dto.RegistroElaboraDTO;
import it.csi.risca.riscabesrv.dto.SoggettiDTO;
import it.csi.risca.riscabesrv.dto.SollDatiAmministrDTO;
import it.csi.risca.riscabesrv.dto.SollDatiPagopaDTO;
import it.csi.risca.riscabesrv.dto.SollDatiTitolareDTO;
import it.csi.risca.riscabesrv.dto.SollDettVersDTO;
import it.csi.risca.riscabesrv.dto.StatiDebitoriAvvisiSollecitiDTO;
import it.csi.risca.riscabesrv.dto.TipiAccertamentoDTO;
import it.csi.risca.riscabesrv.util.Constants;

@Component
public class AvvisiSollecitiApiServiceImpl implements AvvisiSollecitiApi {

	private static final String IDENTITY = "identity";
	private static Logger LOGGER = Logger.getLogger(Constants.LOGGER_NAME);

	@Autowired
	private StatiDebitoriAvvisiSollecitiDAO statiDebitoriAvvisiSollecitiDAO;
	@Autowired
	private RegistroElaboraDAO registroElaboraDAO;
	@Autowired
	private OutputDatiDAO outputDatiDAO;
	@Autowired
	private PagamentoDAO pagamentoDAO;
	@Autowired
	private CalcoloInteresseDAO calcoloInteresseDAO;
	@Autowired
	private AccertamentoDAO accertamentoDAO;
	@Autowired
	private SollDatiTitolareDAO sollDatiTitolareDAO;
	@Autowired
	private SollDatiAmministrDAO sollDatiAmministrDAO;
	@Autowired
	private SollDettVersDAO sollDettVersDAO;
	@Autowired
	private SollDatiPagopaDAO sollDatiPagopaDAO;
	@Autowired
	private StatoDebitorioDAO statoDebitorioDAO;
	@Autowired
	private IuvDAO iuvDAO;
	@Autowired
	private AvvisoDatiTitolareDAO avvisoDatiTitolareDAO;
	@Autowired
	private SoggettiDAO soggettiDAO;
	@Autowired
	private SpedizioneDAO spedizioneDAO;

	private String attore;

	private static final Map<String, String[]> outputFoglio;

	static {
		HashMap<String, String[]> aMap = new HashMap<String, String[]>();
		aMap.put("AB", new String[] { "47", "32", "33", "34", "35", "68", "48", "51", "52" });
		aMap.put("SP", new String[] { "49", "24", "25", "26", "27", "67", "50", "53", "54" });
		outputFoglio = Collections.unmodifiableMap(aMap);
	}

	private static final String COD_TIPO_PASSO_ELABORA_ELAB_EMISSIONE = "ELAB_EMISSIONE";
	private static final String TIPO_ELAB_AVVISO_BONARIO = "AB";
	private static final String TIPO_ELAB_SOLLECITO = "SP";

	private static final String PASSO_INSERT_TIPREC2 = "INSERT_TIPREC2";
	private static final String PASSO_INSERT_TIPREC3 = "INSERT_TIPREC3";
	private static final String PASSO_INSERT_TIPREC4 = "INSERT_TIPREC4";
	// private static final String PASSO_INSERT_TIPREC5 = "INSERT_TIPREC5";
	private static final String PASSO_INSERT_TIPREC6 = "INSERT_TIPREC6";

	private static final String DATE_FORMAT = "yyyy-MM-dd";
	private static final String DATE_FORMAT_SPED = "dd/MM/yyyy";
	private static final String Codice_Fiscale_Eti_Calc_Dest = "Cod.Fisc./P.Iva";

	private static final String MODALITA_INVIO_CARTA = "CARTA";
	private static final String COD_TIPO_ELABORA_AB = "AB";

	@Override
	public Response loadAvvisiSolleciti(String codAmbito, String codAttivitaStatoDeb, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		List<StatiDebitoriAvvisiSollecitiDTO> avvisiSolleciti = statiDebitoriAvvisiSollecitiDAO
				.loadStatiDebitoriAvvisiSolleciti(codAmbito, codAttivitaStatoDeb);
		return Response.ok(avvisiSolleciti).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response saveAvvisiDatiWorking(AvvisoSollecitoData avvisoSollecito, Boolean titolareCambiato,
			Boolean sdCambiato, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		LOGGER.debug("[AvvisiSollecitiApiServiceImpl::saveAvvisiDatiWorking] BEGIN");
		AvvisoSollecitoResultDTO asResult = new AvvisoSollecitoResultDTO();
		attore = "riscabatchavv";
		StatiDebitoriAvvisiSollecitiDTO avvSoll = avvisoSollecito.getAvvisoSollecito();
		RecapitiExtendedDTO recapito = getRecapitoTitolare(avvisoSollecito);
		if (recapito == null) {
			LOGGER.error(
					"[AvvisiSollecitiApiServiceImpl::saveAvvisiDatiWorking] ERROR Recapito non trovato per soggetto idSoggetto: "
							+ avvSoll.getIdSoggetto());
			asResult.setStatus("KO");
			asResult.setErrorMessage("Error Recapito non trovato per soggetto idSoggetto: " + avvSoll.getIdSoggetto());
			return Response.ok(asResult).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		}
		// boolean tipoInvioCarta =
		// recapito.getTipoInvio().getCodTipoInvio().equals("C") ? true : false;
		String tipoElabora = avvisoSollecito.getElabora().getTipoElabora().getCodTipoElabora();
		boolean tipoInvioCarta = getModalitaInvio(tipoElabora, recapito.getTipoInvio().getCodTipoInvio())
				.equals(MODALITA_INVIO_CARTA) ? true : false;
		Long idElabora = avvisoSollecito.getElabora().getIdElabora();
		Long idAmbito = avvisoSollecito.getElabora().getAmbito().getIdAmbito();

		int contatoreSdPerSoggetto = avvisoSollecito.getContatoreSdPerSoggetto();
		int contaSdPerSoggetto = 0;
		double speseNotifSuddivise = 0;
		double speseNotifPerSdCorrente = 0;
		double subtotSpeseNotifSogg = avvisoSollecito.getSubtotSpeseNotifSogg();
		double speseNotifica = avvisoSollecito.getSpeseNotifica();

		// Ripartizione spese di notifica solo per Sollecito di pagamento
		// con tipo invio CARTA
		if (avvSoll.getIdAttivitaStatoDeb() == 3 && tipoInvioCarta) {
			try {
				contaSdPerSoggetto = statoDebitorioDAO.countStatoDebitorioBySoggettoTipoAttivita(
						avvSoll.getIdSoggetto(), avvSoll.getIdGruppoSoggetto(), avvSoll.getIdAttivitaStatoDeb());
			} catch (DAOException e) {
				LOGGER.error("[AvvisiSollecitiApiServiceImpl::saveAvvisiDatiWorking] ERROR " + e.getMessage());
				asResult.setStatus("KO");
				asResult.setErrorMessage("Error countStatoDebitorioBySoggettoTipoAttivita: " + e.getMessage());
				return Response.ok(asResult).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
			}

			// 3.5.3 Ripartire le spese di notifica in caso di Sollecito di pagamento fra
			// tutti gli SD del Soggetto/Gruppo
			if (contaSdPerSoggetto > 0) {
				speseNotifSuddivise = speseNotifica / contaSdPerSoggetto;
				// Arrotondamento a 2 decimali
				speseNotifSuddivise = new BigDecimal(speseNotifSuddivise).setScale(2, RoundingMode.HALF_UP)
						.doubleValue();
			}

			// 3.6.1 In base a quanti SD ha il soggetto setta le spese di notifica
			if (contaSdPerSoggetto == 1) {
				// il Soggetto ha un unico SD quindi valorizzo le Spese di Notifica con il
				// totale delle Spese di Notifica
				speseNotifPerSdCorrente = speseNotifica;
			} else {
				contatoreSdPerSoggetto = contatoreSdPerSoggetto + 1;
				if (contatoreSdPerSoggetto == contaSdPerSoggetto) {
					// sono arrivato sull'ultimo SD del Soggetto corrente
					speseNotifPerSdCorrente = subtotSpeseNotifSogg;
				} else {
					// valorizzo le Spese di Notifica, per lo SD corrente, con il semplice risultato
					// della divisione
					speseNotifPerSdCorrente = speseNotifSuddivise;
					// sottraggo dal subtotale le Spese di Notifica assegnate allo SD corrente
					subtotSpeseNotifSogg = subtotSpeseNotifSogg - speseNotifSuddivise;
				}
			}
		} else {
			// Altrimenti non ci sono spese di notifica
			speseNotifica = 0;
		}

		// Leggo i contatori aggiornati dall'oggetto ricevuto in input
		int progrOdsDatiTitolare = avvisoSollecito.getProgrOdsDatiTitolare();
		int progrOdsDatiAmmin = avvisoSollecito.getProgrOdsDatiAmmin();
		int progrOdsDettVers = avvisoSollecito.getProgrOdsDettVers();
		int progrOdsDestinatari = avvisoSollecito.getProgrOdsDestinatari();
		int progrOdsDatiPagopa = avvisoSollecito.getProgrOdsDatiPagopa();
		int progrTxtCompleto = avvisoSollecito.getProgrTxtCompleto();
		int progrTxtParzCarta = avvisoSollecito.getProgrTxtParzCarta();

		// Progressivi che mi servono per identificare il record di tipo 3 che viene
		// inserito
		int progrTxtCompletoTipo3 = -1;
		int progrTxtCartaTipo3 = -1;

		String protocolloProgr = "";
		if (titolareCambiato) {
			progrOdsDatiTitolare++;
			progrTxtCompleto++;
			protocolloProgr = calcolaNumeroProtocolloSped(avvisoSollecito.getNumProtocollo(), progrOdsDatiTitolare);
			// Inserimento dati del titolare
			try {
				outputDatiDAO.saveOutputDati(getRecordDatiODSTipo2(idElabora, progrOdsDatiTitolare, tipoElabora,
						recapito, avvisoSollecito, protocolloProgr, speseNotifica));
				outputDatiDAO.saveOutputDati(getRecordDatiTxtCompletoTipo2(idElabora, progrTxtCompleto, tipoElabora,
						recapito, avvisoSollecito, protocolloProgr, speseNotifica));
				if (tipoInvioCarta) {
					progrTxtParzCarta++;
					outputDatiDAO.saveOutputDati(getRecordDatiTxtCartaTipo2(idElabora, progrTxtParzCarta, tipoElabora,
							recapito, avvisoSollecito, protocolloProgr, speseNotifica));
				}
			} catch (DAOException e) {
				asResult.setStatus("KO");
				asResult.setStepError(PASSO_INSERT_TIPREC2);
				return Response.ok(asResult).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
			}
		} else {
			protocolloProgr = calcolaNumeroProtocolloSped(avvisoSollecito.getNumProtocollo(), progrOdsDatiTitolare);
		}

		// 3.7 Dati amministrativi

		// 1. Calcolo importo mancante, dato dalla differenza tra dovuto e versato, piu'
		// eventuali rimborsi
		double importoMancante = doubleValue(avvSoll.getCanoneDovuto()) - doubleValue(avvSoll.getSommaImportoVersato())
				+ doubleValue(avvSoll.getImportoRimborsato());

		// 2. Calcolo degli Interessi Versati Tot: corrispondono alla differenza tra
		// Importo Versato e (Canone Dovuto + Importo Rimborsato) ma non possono
		// eccedere quello degli Interessi Maturati Tot, in quel caso li forzo a
		// rec_avvisi_bonari(corrente).rata_interessi_mat
		double interessiVersatiTot = 0;
		if (importoMancante <= 0) {
			interessiVersatiTot = doubleValue(avvSoll.getImportoVersato())
					- (doubleValue(avvSoll.getCanoneDovuto()) + doubleValue(avvSoll.getImportoRimborsato()));
			if (interessiVersatiTot > doubleValue(avvSoll.getInteressiMaturati())) {
				// la parte versata in piu e' >= agli interessi maturati
				interessiVersatiTot = doubleValue(avvSoll.getInteressiMaturati());
			}
		}

		// 3. Calcolo degli Interessi Mancanti
		double interessiMancanti = 0;
		if (importoMancante <= 0) {
			// l'importo e stato versato tutto, potrebbero esserci ancora degli interessi.
			// Si prendono gli interessi maturati dalla RISCA_R_RATA_SD e non dalla
			// RISCA_R_DETTAGLIO_PAG perche' devo avere il totale per tutti i pagamenti
			interessiMancanti = doubleValue(avvSoll.getRataInteressiMaturati()) - interessiVersatiTot;
			if (interessiMancanti < 0) {
				interessiMancanti = 0;
			}
		} else {
			// l'importo NON e' ancora stato versato interamente, quindi gli interessi sono
			// ancora tutti da pagare.
			interessiMancanti = doubleValue(avvSoll.getRataInteressiMaturati());
		}

		// Leggo gli importi aggiornati dall'oggetto ricevuto in input
		double subtotImportiVers = avvisoSollecito.getSubtotImportiVers();
		double subtotInteressiVers = avvisoSollecito.getSubtotInteressiVers();
		double interessiVersDett = avvisoSollecito.getInteressiVersDett();

		double interessiTeorici = 0;

		if (sdCambiato) {
			// Se cambiato SD si azzerano questi importi
			subtotImportiVers = 0;
			subtotInteressiVers = 0;
			interessiVersDett = 0;

			// se c'e' stato un solo pagamento ne estrae la data, altrimenti a quella piu
			// recente concatena un asterisco
			String dataopVal = pagamentoDAO.getDataPagamentoMax(avvSoll.getIdStatoDebitorio());
			// Determina il motivo del sollecito
			String motivoSollecito = getMotivoSollecito(avvSoll);

			if (importoMancante <= 0) {
				importoMancante = 0;
			}
			// Determina gli interessi teorici

			if (avvSoll.getIdAttivitaStatoDeb() == 3) { // SOLLECITI
				if (importoMancante > 0) {
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
					String dataScadPag = formatter.format(avvSoll.getDataScadenzaPagamento());
					try {
						interessiTeorici = calcoloInteresseDAO
								.calcoloInteressi(idAmbito, BigDecimal.valueOf(importoMancante), dataScadPag,
										avvisoSollecito.getDataScadenzaPagamento())
								.doubleValue();
					} catch (Exception e) {
						asResult.setStatus("KO");
						asResult.setStepError(PASSO_INSERT_TIPREC3);
						return Response.ok(asResult).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
					}
				}
			}

			double totale = importoMancante + interessiMancanti;

			BigDecimal totaleTeorico = null;
			if (avvSoll.getIdAttivitaStatoDeb() == 3) { // SOLLECITI
				if (importoMancante > 0) {
					totaleTeorico = BigDecimal.valueOf(totale + interessiTeorici);
				}
			}

			// Inserimento dati amministrativi
			try {
				progrOdsDatiAmmin++;
				progrTxtCompleto++;

				progrTxtCompletoTipo3 = progrTxtCompleto;

				outputDatiDAO.saveOutputDati(getRecordDatiODSTipo3(idElabora, progrOdsDatiAmmin, tipoElabora,
						avvisoSollecito, motivoSollecito, dataopVal, importoMancante, interessiMancanti,
						interessiVersatiTot, totale, interessiTeorici, totaleTeorico, speseNotifPerSdCorrente));
				outputDatiDAO.saveOutputDati(getRecordDatiTxtCompletoTipo3(idElabora, progrTxtCompleto, tipoElabora,
						avvisoSollecito, motivoSollecito, dataopVal, importoMancante, interessiMancanti,
						interessiVersatiTot, totale, interessiTeorici, totaleTeorico, speseNotifPerSdCorrente));
				if (tipoInvioCarta) {
					progrTxtParzCarta++;
					progrTxtCartaTipo3 = progrTxtParzCarta;
					outputDatiDAO.saveOutputDati(getRecordDatiTxtCartaTipo3(idElabora, progrTxtParzCarta, tipoElabora,
							avvisoSollecito, motivoSollecito, dataopVal, importoMancante, interessiMancanti,
							interessiVersatiTot, totale, interessiTeorici, totaleTeorico, speseNotifPerSdCorrente));
				}
			} catch (DAOException e) {
				asResult.setStatus("KO");
				asResult.setStepError(PASSO_INSERT_TIPREC3);
				return Response.ok(asResult).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
			}

		}

		// 3.8 Inserimento DETTAGLIO VERSAMENTI
		double subVersOltreDovuto = 0;

		subtotImportiVers = subtotImportiVers + doubleValue(avvSoll.getDettPagImpVers());

		if (subtotImportiVers > doubleValue(avvSoll.getCanoneDovuto())) {
			// il "subtotale" dei versamenti e' > del Canone dovuto quindi considero
			// interesse tutto cio' che eccede il dovuto ma e' al di sotto del totale degli
			// Interessi Maturati

			// calcolo la differenza tra il subtot dei versamenti e il Canone Dovuto
			subVersOltreDovuto = subtotImportiVers - doubleValue(avvSoll.getCanoneDovuto());

			if (subtotInteressiVers + subVersOltreDovuto > doubleValue(avvSoll.getRataInteressiMaturati())) {
				// considero come Interesse versato di dettaglio gli Interesse maturati ancora
				// da versare
				interessiVersDett = doubleValue(avvSoll.getRataInteressiMaturati()) - subtotInteressiVers;
			} else {
				// considero come interesse versato di dettaglio tutto ci' che con il Versamento
				// corrente eccede il Dovuto
				interessiVersDett = subVersOltreDovuto;
			}
			// incremento il "subtotale" degli Interessi versati con l'interesse versato
			// corrente
			subtotInteressiVers = subtotInteressiVers + interessiVersDett;
		} else {
			// il "subtotale" dei Versamenti e' <= del Canone dovuto quindi non ci sono
			// Interessi versati
			interessiVersDett = 0;
			subtotInteressiVers = 0;
		}

		// Inserimento dati dettaglio versamenti
		try {
			progrOdsDettVers++;
			progrTxtCompleto++;

			outputDatiDAO.saveOutputDati(getRecordDatiODSTipo4(idElabora, progrOdsDettVers, tipoElabora,
					avvisoSollecito, interessiVersDett));
			outputDatiDAO.saveOutputDati(getRecordDatiTxtCompletoTipo4(idElabora, progrTxtCompleto, tipoElabora,
					avvisoSollecito, interessiVersDett));
			if (tipoInvioCarta) {
				progrTxtParzCarta++;
				outputDatiDAO.saveOutputDati(getRecordDatiTxtCartaTipo4(idElabora, progrTxtParzCarta, tipoElabora,
						avvisoSollecito, interessiVersDett));
			}
		} catch (DAOException e) {
			asResult.setStatus("KO");
			asResult.setStepError(PASSO_INSERT_TIPREC4);
			return Response.ok(asResult).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		}

		// PASSO 3.9
		AccertamentoDTO accertamento = null;
		if (sdCambiato) {
			accertamento = insertAccertamento(idElabora, avvisoSollecito, protocolloProgr);
			if (accertamento.getIdAccertamento() == null) {
				asResult.setStatus("KO");
				// TODO creare PASSO per inserimento accertamento
				// asResult.setStepError();
				return Response.ok(asResult).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
			}

			// Aggiorno id_accertamento sul tipo record 3 dati amministr
			outputDatiDAO.updateOutputDatiIdAccertamento(idElabora, progrOdsDatiAmmin,
					Long.parseLong(outputFoglio.get(tipoElabora)[2]), accertamento.getIdAccertamento(), true);
			outputDatiDAO.updateOutputDatiIdAccertamento(idElabora, (3 * 100000) + progrTxtCompletoTipo3,
					Long.parseLong(outputFoglio.get(tipoElabora)[7]), accertamento.getIdAccertamento(), false);
			outputDatiDAO.updateOutputDatiIdAccertamento(idElabora, (3 * 100000) + progrTxtCartaTipo3,
					Long.parseLong(outputFoglio.get(tipoElabora)[8]), accertamento.getIdAccertamento(), false);

			// TODO Inserimento DESTINATARI --> PER ORA NON FARE

			SollDatiTitolareDTO sollTitolare = insertSollDatiTitolare(avvisoSollecito, accertamento.getIdAccertamento(),
					recapito, protocolloProgr, tipoElabora);
			if (sollTitolare.getIdSollDatiTitolare() == null) {
				asResult.setStatus("KO");
				// TODO creare PASSO per inserimento dati titolare
				// asResult.setStepError();
				return Response.ok(asResult).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
			}
		} else {
			// Prendo l'accertamento dai dati in input
			accertamento = avvisoSollecito.getAccertamento();
		}

		// 3.10 Inserimento DATI AMMINISTRATIVI e DETTAGLIO VERSAMENTI nella
		// tabella di Working
		SollDatiAmministrDTO sollDatiAmmin = null;
		if (sdCambiato) {
			sollDatiAmmin = insertSollDatiAmministr(avvisoSollecito, accertamento.getIdAccertamento(), tipoElabora,
					importoMancante, interessiMancanti, interessiVersatiTot, interessiTeorici, speseNotifPerSdCorrente);
			if (sollDatiAmmin.getIdSollDatiAmministr() == null) {
				asResult.setStatus("KO");
				// TODO creare PASSO per inserimento dati amministrativi
				// asResult.setStepError();
				return Response.ok(asResult).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
			}
		}

		SollDettVersDTO dettVers = insertSollDettVers(avvisoSollecito, accertamento.getIdAccertamento());
		if (dettVers.getIdSollDettVers() == null) {
			asResult.setStatus("KO");
			// TODO creare PASSO per inserimento dettaglio versamento
			// asResult.setStepError();
			return Response.ok(asResult).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		}

		// 3.11 Inserimento DATI PAGOPA
		if (sdCambiato) {
			boolean cond1 = verificaCondizione1Pagopa(avvisoSollecito);
			if (cond1) {
				boolean cond2 = verificaCondizione2Pagopa(avvisoSollecito);
				if (cond2) {
					// Solo se entrambe le condizioni sono superate
					// Inserisco il record su risca_w_soll_dati_pagopa

					// Recupero il CODICE_AVVISO corrispondente al NAP passato
					IuvDTO iuv = null;
					try {
						iuv = iuvDAO.getIuvByNap(avvSoll.getNap());
						if (iuv == null || iuv.getIdIuv() == null) {
							asResult.setStatus("KO");
							// TODO creare PASSO per ricerca iuv
							// asResult.setStepError();
							return Response.ok(asResult).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
						} else {
							// valorizzo il CODICE_AVVISO nella RISCA_W_SOLL_DATI_AMMINISTR
							sollDatiAmministrDAO.updateCodiceAvvisoWorkingByIdAccertamento(iuv.getCodiceAvviso(),
									accertamento.getIdAccertamento());
						}
					} catch (Exception e) {
						asResult.setStatus("KO");
						// TODO creare PASSO per ricerca iuv
						// asResult.setStepError();
						return Response.ok(asResult).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
					}

					double impManc = sollDatiAmmin.getImportoMancante().doubleValue();
					double intManc = sollDatiAmmin.getInteressiMancanti().doubleValue();
					double speseNotif = sollDatiAmmin.getSpeseNotifica().doubleValue();
					double importoDaVersare = impManc + intManc + speseNotif;

					// estraggo dalla RISCA_T_AVVISO_DATI_TITOLARE una parte dei dati occorrenti
					AvvisoDatiTitolareDTO avvisoDatiTit = avvisoDatiTitolareDAO
							.loadAvvisoDatiTitolareByNap(avvSoll.getNap());
					try {
						// Verificare se lo IUV corrente e' gia' presente nella tabella
						// RISCA_W_SOLL_DATI_PAGOPA
						SollDatiPagopaDTO datiPagoPAPresenti = sollDatiPagopaDAO
								.loadSollDatiPagopaWorkingByIuv(iuv.getIuv());

						if (datiPagoPAPresenti == null) {
							// Inserisco una riga nella RISCA_W_SOLL_DATI_PAGOPA
							SollDatiPagopaDTO sollDatiPagopa = insertSollDatiPagopa(sollDatiAmmin, avvisoDatiTit, iuv,
									accertamento.getIdAccertamento(), importoDaVersare);

							// inserimenti nella RISCA_W_OUTPUT_DATI per il file .ods e.txt

							progrOdsDatiPagopa++;
							progrTxtCompleto++;

							outputDatiDAO.saveOutputDati(getRecordDatiODSTipo6(idElabora, progrOdsDatiPagopa,
									tipoElabora, avvisoSollecito, sollDatiAmmin, avvisoDatiTit, iuv, importoDaVersare));
							outputDatiDAO.saveOutputDati(getRecordDatiTxtCompletoTipo6(idElabora, progrTxtCompleto,
									tipoElabora, avvisoSollecito, sollDatiAmmin, avvisoDatiTit, iuv, importoDaVersare));
							if (tipoInvioCarta) {
								progrTxtParzCarta++;
								outputDatiDAO.saveOutputDati(
										getRecordDatiTxtCartaTipo6(idElabora, progrTxtParzCarta, tipoElabora,
												avvisoSollecito, sollDatiAmmin, avvisoDatiTit, iuv, importoDaVersare));
							}

						} else {
							double importoDaVersareOld = datiPagoPAPresenti.getImportoDaVersare().doubleValue();
							double importoDaVersareNew = importoDaVersareOld + importoDaVersare;

							sollDatiPagopaDAO.updateSollDatiPagopaWorkingImportoByIuv(iuv.getIuv(),
									BigDecimal.valueOf(importoDaVersareNew));
							
							//update importo sul file XLS
							outputDatiDAO.updateXlsSollImportoDaVersareByIuv(idElabora, iuv.getIuv(),
									Long.parseLong(outputFoglio.get(tipoElabora)[5]),
									BigDecimal.valueOf(importoDaVersareNew));

							//update importo su TXT completo
							String valoreColonna1 = getDatiPagopaString(tipoElabora, avvisoSollecito, sollDatiAmmin, avvisoDatiTit, iuv, importoDaVersareNew);
							
							outputDatiDAO.updateTxtSollImportoDaVersareByIuv(idElabora, iuv.getIuv(),
									Long.parseLong(outputFoglio.get(tipoElabora)[7]), valoreColonna1);
							
							if (tipoInvioCarta) {
								//update importo su TXT carta
								outputDatiDAO.updateTxtSollImportoDaVersareByIuv(idElabora, iuv.getIuv(),
										Long.parseLong(outputFoglio.get(tipoElabora)[8]), valoreColonna1);
							}

						}

						// Aggiornamento per tipo record 3, occorre aggiungere il codice avviso al fondo
						outputDatiDAO.updateOutputDatiCodiceAvviso(idElabora,
								Long.parseLong(outputFoglio.get(tipoElabora)[2]), "" + accertamento.getIdAccertamento(),
								iuv.getCodiceAvviso(), true);
						outputDatiDAO.updateOutputDatiCodiceAvviso(idElabora,
								Long.parseLong(outputFoglio.get(tipoElabora)[7]), "" + accertamento.getIdAccertamento(),
								iuv.getCodiceAvviso(), false);
						outputDatiDAO.updateOutputDatiCodiceAvviso(idElabora,
								Long.parseLong(outputFoglio.get(tipoElabora)[8]), "" + accertamento.getIdAccertamento(),
								iuv.getCodiceAvviso(), false);
					} catch (DAOException e) {
						asResult.setStatus("KO");
						asResult.setStepError(PASSO_INSERT_TIPREC6);
						return Response.ok(asResult).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
					}
				}
			}
		}

		// Riordino i progressivi
		// nella RISCA_W_OUTPUT_DATI ordina i dati per i file .txt (completo e
		// carta in base al foglio). Occorre aggiornare il progressivo, in modo che dopo
		// ogni Titolare (tipo_record = 2) ci siano tutti i suoi tipo 3, poi 4, poi 6
		// riordinaProgressiviTxt(idElabora, outputFoglio.get(tipoElabora)[7],
		// avvisoSollecito.getSoggettoGruppo());
		// riordinaProgressiviTxt(idElabora, outputFoglio.get(tipoElabora)[8],
		// avvisoSollecito.getSoggettoGruppo());

		// Salvo i contatori aggiornati per la chiamata successiva
		asResult.setProgrOdsDatiTitolare(progrOdsDatiTitolare);
		asResult.setProgrOdsDatiAmmin(progrOdsDatiAmmin);
		asResult.setProgrOdsDettVers(progrOdsDettVers);
		asResult.setProgrOdsDestinatari(progrOdsDestinatari);
		asResult.setProgrOdsDatiPagopa(progrOdsDatiPagopa);
		asResult.setProgrTxtCompleto(progrTxtCompleto);
		asResult.setProgrTxtParzCarta(progrTxtParzCarta);

		// Salvo gli importi parziali per la chiamata successiva
		asResult.setSubtotImportiVers(subtotImportiVers);
		asResult.setSubtotInteressiVers(subtotInteressiVers);
		asResult.setInteressiVersDett(interessiVersDett);
		asResult.setSubtotSpeseNotifSogg(subtotSpeseNotifSogg);
		asResult.setContatoreSdPerSoggetto(contatoreSdPerSoggetto);

		// Salvo l'accertamento per eventuale giro successivo
		asResult.setAccertamento(accertamento);

		// Status OK
		asResult.setStatus("OK");

		LOGGER.debug("[AvvisiSollecitiApiServiceImpl::saveAvvisiDatiWorking] END");
		return Response.ok(asResult).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response riordinaAvvisiDatiWorking(Long idElabora, String idOutputFoglio, String soggettoGruppo,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[AvvisiSollecitiApiServiceImpl::riordinaAvvisiDatiWorking] BEGIN");
		Integer progressivo = outputDatiDAO.loadProgressivoTitolareTxt(idElabora, Long.valueOf(idOutputFoglio),
				soggettoGruppo);
		// Se non ha trovato il progressivo per quel titolare significa che non e'
		// presente sul file (ad esempio quel soggetto ha modalita' invio PEC o MAIL e
		// quindi sul txt carta non e' presente) quindi non devo aggiornare i
		// progressivi per quell'idOutputFoglio
		if (progressivo != null) {
			List<OutputDatiDTO> recTipo3 = outputDatiDAO.loadOutputDatiByTipoRecord(idElabora,
					Long.valueOf(idOutputFoglio), soggettoGruppo, "3");
			for (OutputDatiDTO rec : recTipo3) {
				progressivo++;
				outputDatiDAO.updateOutputDatiProgressivo(rec, progressivo);
			}
			List<OutputDatiDTO> recTipo4 = outputDatiDAO.loadOutputDatiByTipoRecord(idElabora,
					Long.valueOf(idOutputFoglio), soggettoGruppo, "4");
			for (OutputDatiDTO rec : recTipo4) {
				progressivo++;
				outputDatiDAO.updateOutputDatiProgressivo(rec, progressivo);
			}
			List<OutputDatiDTO> recTipo6 = outputDatiDAO.loadOutputDatiByTipoRecord(idElabora,
					Long.valueOf(idOutputFoglio), soggettoGruppo, "6");
			for (OutputDatiDTO rec : recTipo6) {
				progressivo++;
				outputDatiDAO.updateOutputDatiProgressivo(rec, progressivo);
			}
		}
		LOGGER.debug("[AvvisiSollecitiApiServiceImpl::riordinaAvvisiDatiWorking] END");
		return Response.ok(true).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response copyDatiWorking(Long idElabora, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		LOGGER.debug("[AvvisiSollecitiApiServiceImpl::copyDatiWorking] BEGIN");
		attore = "riscabatchavv";
		try {
			// Accertamento
			accertamentoDAO.copyAccertamentoFromWorking(idElabora, attore);

			// Dati Amministrativi
			sollDatiAmministrDAO.copySollDatiAmministrFromWorking(idElabora, attore);

			// Dettaglio Versamento
			sollDettVersDAO.copySollDettVersFromWorking(idElabora, attore);

			// Dati Titolare
			sollDatiTitolareDAO.copySollDatiTitolareFromWorking(idElabora, attore);

			// Dati PAGOPA
			sollDatiPagopaDAO.copySollDatiPagopaFromWorking(idElabora, attore);

		} catch (Exception e) {
			LOGGER.error("[AvvisiSollecitiApiServiceImpl::copyDatiWorking:: EXCEPTION ]: " + e);
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
		}

		LOGGER.debug("[AvvisiSollecitiApiServiceImpl::copyDatiWorking] END");
		return Response.ok(true).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response deleteDatiWorking(Long idElabora, Long idSpedizione, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[AvvisiSollecitiApiServiceImpl::deleteDatiWorking] BEGIN");

		try {
			sollDatiPagopaDAO.deleteSollDatiPagopaWorkingByElabora(idElabora);
			sollDatiAmministrDAO.deleteSollDatiAmministrWorkingByElabora(idElabora);
			sollDettVersDAO.deleteSollDettVersWorkingByElabora(idElabora);
			sollDatiTitolareDAO.deleteSollDatiTitolareWorkingByElabora(idElabora);
			accertamentoDAO.deleteAccertamentoWorkingByIdElabora(idElabora);
			spedizioneDAO.deleteSpedizioneByPk(idSpedizione, true);
		} catch (Exception e) {
			LOGGER.error("[AvvisiSollecitiApiServiceImpl::copyDatiWorking:: EXCEPTION ]: " + e);
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
		}

		LOGGER.debug("[AvvisiSollecitiApiServiceImpl::deleteDatiWorking] END");
		return Response.ok(true).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	/*
	 * private void riordinaProgressiviTxt(Long idElabora, String idOutputFoglio,
	 * String soggettoGruppo) { Integer progressivo =
	 * outputDatiDAO.loadProgressivoTitolareTxt(idElabora,
	 * Long.valueOf(idOutputFoglio), soggettoGruppo); // Se non ha trovato il
	 * progressivo per quel titolare significa che non e' // presente sul file (ad
	 * esempio quel soggetto ha modalita' invio PEC o MAIL e // quindi sul txt carta
	 * non e' presente) quindi non devo aggiornare i // progressivi per
	 * quell'idOutputFoglio if (progressivo != null) { List<OutputDatiDTO> recTipo3
	 * = outputDatiDAO.loadOutputDatiByTipoRecord(idElabora,
	 * Long.valueOf(idOutputFoglio), soggettoGruppo, "3"); for (OutputDatiDTO rec :
	 * recTipo3) { progressivo++; outputDatiDAO.updateOutputDatiProgressivo(rec,
	 * progressivo); } List<OutputDatiDTO> recTipo4 =
	 * outputDatiDAO.loadOutputDatiByTipoRecord(idElabora,
	 * Long.valueOf(idOutputFoglio), soggettoGruppo, "4"); for (OutputDatiDTO rec :
	 * recTipo4) { progressivo++; outputDatiDAO.updateOutputDatiProgressivo(rec,
	 * progressivo); } List<OutputDatiDTO> recTipo6 =
	 * outputDatiDAO.loadOutputDatiByTipoRecord(idElabora,
	 * Long.valueOf(idOutputFoglio), soggettoGruppo, "6"); for (OutputDatiDTO rec :
	 * recTipo6) { progressivo++; outputDatiDAO.updateOutputDatiProgressivo(rec,
	 * progressivo); } } }
	 */

	private boolean verificaCondizione2Pagopa(AvvisoSollecitoData avvisoSollecito) {
		boolean ret = true;
		StatiDebitoriAvvisiSollecitiDTO avvSoll = avvisoSollecito.getAvvisoSollecito();
		List<AvvisoSollecitoCheckSoggettoResultDTO> recPMini = statiDebitoriAvvisiSollecitiDAO
				.loadSoggettoSdPraticaPerCheckCondizione2(avvSoll.getIdSoggetto(), avvSoll.getIdAttivitaStatoDeb());
		if (recPMini == null || recPMini.size() == 0) {
			ret = false;
		} else {
			for (AvvisoSollecitoCheckSoggettoResultDTO rec : recPMini) {
				if (rec.getSoggCf() == null || rec.getSdNap() == null) {
					ret = false;
					break;
				}
				// recupero il CF del soggetto dello SD
				AvvisoDatiTitolareDTO avvisoTit;
				try {
					avvisoTit = avvisoDatiTitolareDAO.loadAvvisoDatiTitolareByNap(rec.getSdNap());
					if (avvisoTit == null || avvisoTit.getNap() == null || avvisoTit.getCodiceFiscaleCalc() == null) {
						ret = false;
						break;
					}
				} catch (Exception e) {
					ret = false;
					break;
				}
				// recupero il CF del soggetto dello SD
				SoggettiDTO sogg = null;
				try {
					sogg = soggettiDAO.loadSoggettoById(rec.getSdIdSoggettoTitolare());
				} catch (Exception e) {
					ret = false;
					break;
				}
				if (sogg == null || sogg.getCfSoggetto() == null) {
					ret = false;
					break;
				}
				if (rec.getSoggCf().equalsIgnoreCase(avvisoTit.getCodiceFiscaleCalc())
						&& avvisoTit.getCodiceFiscaleCalc().equalsIgnoreCase(sogg.getCfSoggetto())) {
					// i 3 CF coincidono --> Verifico se anche gli ID soggetto coincidono
					if (rec.getIdSoggetto().equals(avvisoTit.getIdSoggetto())
							&& avvisoTit.getIdSoggetto().equals(sogg.getIdSoggetto())) {
						// i 3 ID soggetto coincidono, condizione superata per questo record,
						// continuo il ciclo for per verificare tutti i record
						ret = true;
					}
				} else {
					ret = false;
					break;
				}
			}
		}
		return ret;
	}

	private boolean verificaCondizione1Pagopa(AvvisoSollecitoData avvisoSollecito) {
		boolean ret = true;
		StatiDebitoriAvvisiSollecitiDTO avvSoll = avvisoSollecito.getAvvisoSollecito();
		AvvisoSollecitoCheckSDResultDTO resCheck1 = statiDebitoriAvvisiSollecitiDAO
				.checkCondizioneSdPerPagopa(avvSoll.getIdSoggetto(), avvSoll.getIdAttivitaStatoDeb());

		if (resCheck1.getSdNapValorizzato().equals("KO") || resCheck1.getSdNapNullo().equals("KO")
				|| resCheck1.getCodiciAvvisoAttivi().equals("KO")
				|| resCheck1.getCodiciAvvisoNonAttivi().equals("KO")) {
			// Se una delle condizioni è KO il chek fallisce e deve ritornare false
			ret = false;
		}

		return ret;
	}

	private SollDettVersDTO insertSollDettVers(AvvisoSollecitoData avvisoSollecito, Long idAccertamento) {
		StatiDebitoriAvvisiSollecitiDTO avvSoll = avvisoSollecito.getAvvisoSollecito();
		SollDettVersDTO dto = new SollDettVersDTO();
		dto.setIdAccertamento(idAccertamento);
		dto.setCodiceUtenza(avvSoll.getCodRiscossione());
		dto.setScadenzaPagamento(formatDate(avvSoll.getDataScadenzaPagamento()));
		dto.setImportoVersato(avvSoll.getDettPagImpVers());
		dto.setDataVersamento(avvSoll.getDataVersamentoDett());
		dto.setInteressiMaturati(avvSoll.getInteressiMaturati());
		dto.setGiorniRitardo(avvSoll.getFlgDilazione() == 1 ? null : avvSoll.getGiorniRitardo());
		try {
			dto = sollDettVersDAO.saveSollDettVersWorking(dto);
		} catch (DAOException e) {
			LOGGER.debug("[AvvisiSollecitiApiServiceImpl::insertSollDettVers] ERROR " + e.getMessage());
		}
		return dto;
	}

	private AccertamentoDTO insertAccertamento(Long idElabora, AvvisoSollecitoData avvisoSollecito, String protProgr) {
		StatiDebitoriAvvisiSollecitiDTO avvSoll = avvisoSollecito.getAvvisoSollecito();
		AccertamentoDTO dto = new AccertamentoDTO();
		dto.setIdElabora(idElabora);
		dto.setIdSpedizione(avvisoSollecito.getIdSpedizione());
		dto.setIdStatoDebitorio(avvSoll.getIdStatoDebitorio());
		TipiAccertamentoDTO tipoAcc = new TipiAccertamentoDTO();
		tipoAcc.setIdTipoAccertamento(avvSoll.getIdAttivitaStatoDeb() == 2 ? Long.valueOf(1) : Long.valueOf(2));
		dto.setTipoAccertamento(tipoAcc);
		dto.setNumProtocollo(protProgr);
		dto.setDataProtocollo(parseDate(avvisoSollecito.getDataProtocollo()));
		dto.setDataScadenza(parseDate(avvisoSollecito.getDataScadenzaPagamento()));
		try {
			dto = accertamentoDAO.saveAccertamentoWorking(dto);
		} catch (DAOException e) {
			LOGGER.debug("[AvvisiSollecitiApiServiceImpl::insertAccertamento] ERROR " + e.getMessage());
		}
		return dto;
	}

	private SollDatiTitolareDTO insertSollDatiTitolare(AvvisoSollecitoData avvisoSollecito, Long idAccertamento,
			RecapitiExtendedDTO recapito, String protProgr, String tipoElabora) {
		StatiDebitoriAvvisiSollecitiDTO avvSoll = avvisoSollecito.getAvvisoSollecito();
		IndirizzoSpedizioneDTO indSped = getIndirizzoSpedizione(recapito, avvSoll.getIdGruppoSoggetto());

		SollDatiTitolareDTO dto = new SollDatiTitolareDTO();
		dto.setIdAccertamento(idAccertamento);
		dto.setNomeTitolareIndPost(formatString50(indSped.getDestinatarioPostel()));
		dto.setCodiceFiscaleCalc(avvSoll.getCfSoggetto());
		dto.setCodiceFiscaleEtiCalc(Codice_Fiscale_Eti_Calc_Dest);
		dto.setProvIndPost(indSped.getProvinciaPostel());
		dto.setPressoIndPost(indSped.getPressoPostel());
		dto.setIndirizzoIndPost(indSped.getIndirizzoPostel() + " " + indSped.getCapPostel());
		dto.setComuneIndPost(indSped.getCittaPostel());
		dto.setNumProt(protProgr);
		dto.setDataProt(parseDate(avvisoSollecito.getDataProtocollo()));
		dto.setScadenzaSoll(formatDate(avvisoSollecito.getDataScadenzaPagamento()));
		dto.setIdTipoInvio(getIdTipoInvio(tipoElabora, recapito.getTipoInvio().getCodTipoInvio()));
		dto.setPecEmail(getIndirizzoMailPec(recapito));
		dto.setIdTitolare(avvSoll.getIdTitolare());
		dto.setIdSoggetto(avvSoll.getIdSoggetto());
		try {
			sollDatiTitolareDAO.saveSollDatiTitolareWorking(dto);
		} catch (DAOException e) {
			LOGGER.debug("[AvvisiSollecitiApiServiceImpl::insertSollDatiTitolare] ERROR " + e.getMessage());
		}
		return dto;
	}

	private SollDatiAmministrDTO insertSollDatiAmministr(AvvisoSollecitoData avvisoSollecito, Long idAccertamento,
			String tipoElab, double importoMancante, double interessiMancanti, double interessiVersatiTot,
			double interessiTeorici, double speseNotifPerSdCorrente) {
		StatiDebitoriAvvisiSollecitiDTO avvSoll = avvisoSollecito.getAvvisoSollecito();

		SollDatiAmministrDTO dto = new SollDatiAmministrDTO();
		dto.setIdAccertamento(idAccertamento);
		dto.setCodiceUtente(null);
		dto.setCodiceUtenza(avvSoll.getCodRiscossione());
		dto.setTipoSollecito(tipoElab.equals(TIPO_ELAB_AVVISO_BONARIO) ? "Avviso bonario" : "Sollecito di pagamento");
		dto.setTipoTitolo(avvSoll.getDesTipoTitolo());
		dto.setNumTitolo(avvSoll.getNumTitolo());
		dto.setDataTitolo(avvSoll.getDataProvvedimento());
		dto.setCorpoIdrico(avvisoSollecito.getCorpoIdricoCaptazione());
		dto.setComuneDiPresa(avvisoSollecito.getComuneDiCaptazione());
		dto.setAnnualitaPagamento(Integer.valueOf(avvSoll.getAnnualitaDiPagamento()));
		dto.setAnnoRichPagamento(avvSoll.getDescPeriodoPagamento());
		dto.setScadenzaPagamento(formatDate(avvSoll.getDataScadenzaPagamento()));
		dto.setMotivoSoll(getMotivoSollecito(avvSoll));
		dto.setCanoneDovuto(avvSoll.getCanoneDovuto());
		dto.setImportoVersato(avvSoll.getImportoVersato());
		dto.setImportoMancante(BigDecimal.valueOf(importoMancante));
		dto.setInteressiMancanti(BigDecimal.valueOf(interessiMancanti));
		dto.setSpeseNotifica(new BigDecimal(speseNotifPerSdCorrente));
		dto.setInteressiTeorici(BigDecimal.valueOf(interessiTeorici));
		dto.setInteressiVersati(BigDecimal.valueOf(interessiVersatiTot));
		dto.setDilazione(avvSoll.getFlgDilazione() == 0 ? "N" : "S");

		try {
			dto = sollDatiAmministrDAO.saveSollDatiAmministrWorking(dto);
		} catch (DAOException e) {
			LOGGER.debug("[AvvisiSollecitiApiServiceImpl::insertSollDatiAmministr] ERROR " + e.getMessage());
		}
		return dto;
	}

	private SollDatiPagopaDTO insertSollDatiPagopa(SollDatiAmministrDTO sollDatiAmmin,
			AvvisoDatiTitolareDTO avvisoDatiTit, IuvDTO iuv, Long idAccertamento, double importoDaVersare) {

		SollDatiPagopaDTO dto = new SollDatiPagopaDTO();
		dto.setIdAccertamento(idAccertamento);
		dto.setCodiceUtente(sollDatiAmmin.getCodiceUtente());
		dto.setCodiceUtenza(sollDatiAmmin.getCodiceUtenza());
		dto.setIuv(iuv.getIuv());
		dto.setCodiceAvviso(iuv.getCodiceAvviso());
		dto.setScadenzaPagamento(avvisoDatiTit.getScadenzaPagamento());
		dto.setImportoDaVersare(new BigDecimal(importoDaVersare));
		dto.setNap(avvisoDatiTit.getNap());
		dto.setNomeTitolareIndPost(avvisoDatiTit.getNomeTitolareIndPost());
		dto.setPressoIndPost(avvisoDatiTit.getPressoIndPost());
		dto.setIndirizzoIndPost(avvisoDatiTit.getIndirizzoIndPost());
		dto.setCapIndPost(avvisoDatiTit.getCapIndPost());
		dto.setComuneIndPost(avvisoDatiTit.getComuneIndPost());
		dto.setProvIndPost(avvisoDatiTit.getProvIndPost());
		dto.setPecEmail(avvisoDatiTit.getPecEmail());

		try {
			dto = sollDatiPagopaDAO.saveSollDatiPagopaWorking(dto);
		} catch (DAOException e) {
			LOGGER.debug("[AvvisiSollecitiApiServiceImpl::insertSollDatiPagopa] ERROR " + e.getMessage());
		}
		return dto;
	}

	private OutputDatiDTO getRecordDatiODSTipo6(Long idElabora, int progrTxtCarta, String tipoElab,
			AvvisoSollecitoData avvisoSollecito, SollDatiAmministrDTO sollDatiAmmin,
			AvvisoDatiTitolareDTO avvisoDatiTit, IuvDTO iuv, double importoDaVersare) {
		StatiDebitoriAvvisiSollecitiDTO avvSoll = avvisoSollecito.getAvvisoSollecito();
		OutputDatiDTO dto = new OutputDatiDTO();
		dto.setIdElabora(idElabora);
		// dto.setProgressivo(progrTxtCarta);
		dto.setProgressivo(progrTxtCarta + 6 * 100000);
		dto.setIdOutputFoglio(Long.parseLong(outputFoglio.get(tipoElab)[5]));
		dto.setCodTipoPassoElabora(COD_TIPO_PASSO_ELABORA_ELAB_EMISSIONE);
		dto.setValoreColonna1("6");
		dto.setValoreColonna2("" + avvSoll.getIdSoggetto());
		dto.setValoreColonna3(sollDatiAmmin.getCodiceUtenza());
		dto.setValoreColonna4(iuv.getIuv());
		dto.setValoreColonna5(iuv.getCodiceAvviso());
		dto.setValoreColonna6(avvisoDatiTit.getScadenzaPagamento());
		dto.setValoreColonna7(formatBigDecimal(importoDaVersare));
		dto.setValoreColonna8(avvisoDatiTit.getNap());
		dto.setValoreColonna9(avvisoDatiTit.getNomeTitolareIndPost());
		dto.setValoreColonna10(avvisoDatiTit.getPressoIndPost());
		dto.setValoreColonna11(avvisoDatiTit.getIndirizzoIndPost());
		dto.setValoreColonna12(avvisoDatiTit.getCapIndPost());
		dto.setValoreColonna13(avvisoDatiTit.getComuneIndPost());
		dto.setValoreColonna14(avvisoDatiTit.getProvIndPost());
		dto.setValoreColonna15(avvisoDatiTit.getPecEmail());
		return dto;
	}

	private OutputDatiDTO getRecordDatiTxtCompletoTipo6(Long idElabora, int progrTxtCompleto, String tipoElab,
			AvvisoSollecitoData avvisoSollecito, SollDatiAmministrDTO sollDatiAmmin,
			AvvisoDatiTitolareDTO avvisoDatiTit, IuvDTO iuv, double importoDaVersare) {
		OutputDatiDTO dto = new OutputDatiDTO();
		dto.setIdElabora(idElabora);
		// dto.setProgressivo(progrTxtCompleto);
		dto.setProgressivo(progrTxtCompleto + 6 * 100000);
		dto.setIdOutputFoglio(Long.parseLong(outputFoglio.get(tipoElab)[7]));
		dto.setCodTipoPassoElabora(COD_TIPO_PASSO_ELABORA_ELAB_EMISSIONE);
		dto.setValoreColonna1(
				getDatiPagopaString(tipoElab, avvisoSollecito, sollDatiAmmin, avvisoDatiTit, iuv, importoDaVersare));
		dto.setValoreColonna2(avvisoSollecito.getSoggettoGruppo());
		dto.setValoreColonna3("6");
		dto.setValoreColonna4(avvisoSollecito.getAvvisoSollecito().getCodRiscossione());
		dto.setValoreColonna5(formatDate(avvisoSollecito.getAvvisoSollecito().getDataScadenzaPagamento()));
		dto.setValoreColonna6(formatDate(avvisoSollecito.getAvvisoSollecito().getDataVersamentoDett()));
		return dto;
	}

	private OutputDatiDTO getRecordDatiTxtCartaTipo6(Long idElabora, int progrOdsDatiPagopa, String tipoElab,
			AvvisoSollecitoData avvisoSollecito, SollDatiAmministrDTO sollDatiAmmin,
			AvvisoDatiTitolareDTO avvisoDatiTit, IuvDTO iuv, double importoDaVersare) {
		OutputDatiDTO dto = new OutputDatiDTO();
		dto.setIdElabora(idElabora);
		dto.setProgressivo(progrOdsDatiPagopa);
		dto.setIdOutputFoglio(Long.parseLong(outputFoglio.get(tipoElab)[8]));
		dto.setCodTipoPassoElabora(COD_TIPO_PASSO_ELABORA_ELAB_EMISSIONE);
		dto.setValoreColonna1(
				getDatiPagopaString(tipoElab, avvisoSollecito, sollDatiAmmin, avvisoDatiTit, iuv, importoDaVersare));
		dto.setValoreColonna2(avvisoSollecito.getSoggettoGruppo());
		dto.setValoreColonna3("6");
		dto.setValoreColonna4(avvisoSollecito.getAvvisoSollecito().getCodRiscossione());
		dto.setValoreColonna5(formatDate(avvisoSollecito.getAvvisoSollecito().getDataScadenzaPagamento()));
		dto.setValoreColonna6(formatDate(avvisoSollecito.getAvvisoSollecito().getDataVersamentoDett()));
		return dto;
	}

	private String getDatiPagopaString(String tipoElab, AvvisoSollecitoData avvisoSollecito,
			SollDatiAmministrDTO sollDatiAmmin, AvvisoDatiTitolareDTO avvisoDatiTit, IuvDTO iuv,
			double importoDaVersare) {
		StatiDebitoriAvvisiSollecitiDTO avvSoll = avvisoSollecito.getAvvisoSollecito();
		StringBuilder sb = new StringBuilder();
		sb.append("6").append(";");
		sb.append("" + avvSoll.getIdSoggetto()).append(";");
		sb.append(sollDatiAmmin.getCodiceUtenza()).append(";");
		sb.append(formatString(iuv.getIuv())).append(";");
		sb.append(formatString(iuv.getCodiceAvviso())).append(";");
		sb.append(formatString(avvisoDatiTit.getScadenzaPagamento())).append(";");
		sb.append(formatBigDecimalTxt("" + importoDaVersare)).append(";");
		sb.append(formatString(avvisoDatiTit.getNap())).append(";");
		sb.append(formatString(avvisoDatiTit.getNomeTitolareIndPost())).append(";");
		sb.append(formatString(avvisoDatiTit.getPressoIndPost())).append(";");
		sb.append(formatString(avvisoDatiTit.getIndirizzoIndPost())).append(";");
		sb.append(formatString(avvisoDatiTit.getCapIndPost())).append(";");
		sb.append(formatString(avvisoDatiTit.getComuneIndPost())).append(";");
		sb.append(formatString(avvisoDatiTit.getProvIndPost())).append(";");
		sb.append(formatString(avvisoDatiTit.getPecEmail())).append(";");

		return sb.toString();
	}

	private OutputDatiDTO getRecordDatiTxtCartaTipo4(Long idElabora, int progrTxtParzCarta, String tipoElab,
			AvvisoSollecitoData avvisoSollecito, double interessiDettVers) {
		OutputDatiDTO dto = new OutputDatiDTO();
		dto.setIdElabora(idElabora);
		// dto.setProgressivo(progrTxtParzCarta);
		dto.setProgressivo(progrTxtParzCarta + 4 * 100000);
		dto.setIdOutputFoglio(Long.parseLong(outputFoglio.get(tipoElab)[8]));
		dto.setCodTipoPassoElabora(COD_TIPO_PASSO_ELABORA_ELAB_EMISSIONE);
		dto.setValoreColonna1(getDettVersString(tipoElab, avvisoSollecito, interessiDettVers));
		dto.setValoreColonna2(avvisoSollecito.getSoggettoGruppo());
		dto.setValoreColonna3("4");
		dto.setValoreColonna4(avvisoSollecito.getAvvisoSollecito().getCodRiscossione());
		dto.setValoreColonna5(formatDate(avvisoSollecito.getAvvisoSollecito().getDataScadenzaPagamento()));
		dto.setValoreColonna6(formatDate(avvisoSollecito.getAvvisoSollecito().getDataVersamentoDett()));
		// TODO NB id_accertamento deve essere aggiunto dopo aver inserito il record in
		// tabella risca_w_accertamento
		dto.setValoreColonna7(null);
		return dto;
	}

	private OutputDatiDTO getRecordDatiTxtCompletoTipo4(Long idElabora, int progrTxtCompleto, String tipoElab,
			AvvisoSollecitoData avvisoSollecito, double interessiDettVers) {
		OutputDatiDTO dto = new OutputDatiDTO();
		dto.setIdElabora(idElabora);
		// dto.setProgressivo(progrTxtCompleto);
		dto.setProgressivo(progrTxtCompleto + 4 * 100000);
		dto.setIdOutputFoglio(Long.parseLong(outputFoglio.get(tipoElab)[7]));
		dto.setCodTipoPassoElabora(COD_TIPO_PASSO_ELABORA_ELAB_EMISSIONE);
		dto.setValoreColonna1(getDettVersString(tipoElab, avvisoSollecito, interessiDettVers));
		dto.setValoreColonna2(avvisoSollecito.getSoggettoGruppo());
		dto.setValoreColonna3("4");
		dto.setValoreColonna4(avvisoSollecito.getAvvisoSollecito().getCodRiscossione());
		dto.setValoreColonna5(formatDate(avvisoSollecito.getAvvisoSollecito().getDataScadenzaPagamento()));
		dto.setValoreColonna6(formatDate(avvisoSollecito.getAvvisoSollecito().getDataVersamentoDett()));
		// TODO NB id_accertamento deve essere aggiunto dopo aver inserito il record in
		// tabella risca_w_accertamento
		dto.setValoreColonna7(null);
		return dto;
	}

	private String getDettVersString(String tipoElab, AvvisoSollecitoData avvisoSollecito, double interessiDettVers) {
		StatiDebitoriAvvisiSollecitiDTO avvSoll = avvisoSollecito.getAvvisoSollecito();
		StringBuilder sb = new StringBuilder();
		sb.append("4").append(";");
		sb.append(avvisoSollecito.getSoggettoGruppo()).append(";");
		sb.append(formatString(avvSoll.getCodRiscossione())).append(";");
		sb.append(formatDate(avvSoll.getDataScadenzaPagamento())).append(";");
		sb.append(formatBigDecimalTxt(avvSoll.getDettPagImpVers())).append(";");
		sb.append(formatDate(avvSoll.getDataVersamentoDett())).append(";");
		sb.append(formatBigDecimalTxt(avvSoll.getInteressiMaturati())).append(";");
		sb.append(avvSoll.getFlgDilazione() == 1 ? "" : "" + avvSoll.getGiorniRitardo()).append(";");
		sb.append(formatBigDecimalTxt(BigDecimal.valueOf(interessiDettVers))).append(";");

		return sb.toString();
	}

	private OutputDatiDTO getRecordDatiODSTipo4(Long idElabora, int progrOdsDettVers, String tipoElab,
			AvvisoSollecitoData avvisoSollecito, double interessiDettVers) {
		StatiDebitoriAvvisiSollecitiDTO avvSoll = avvisoSollecito.getAvvisoSollecito();
		OutputDatiDTO dto = new OutputDatiDTO();
		dto.setIdElabora(idElabora);
		dto.setProgressivo(progrOdsDettVers);
		dto.setIdOutputFoglio(Long.parseLong(outputFoglio.get(tipoElab)[3]));
		dto.setCodTipoPassoElabora(COD_TIPO_PASSO_ELABORA_ELAB_EMISSIONE);
		dto.setValoreColonna1("4");
		dto.setValoreColonna2(avvisoSollecito.getSoggettoGruppo());
		dto.setValoreColonna3(formatString(avvSoll.getCodRiscossione()));
		dto.setValoreColonna4(formatDate(avvSoll.getDataScadenzaPagamento()));
		dto.setValoreColonna5(formatBigDecimal(avvSoll.getDettPagImpVers()));
		dto.setValoreColonna6(formatDate(avvSoll.getDataVersamentoDett()));
		dto.setValoreColonna7(formatBigDecimal(avvSoll.getInteressiMaturati()));
		dto.setValoreColonna8(avvSoll.getFlgDilazione() == 1 ? "" : "" + avvSoll.getGiorniRitardo());
		dto.setValoreColonna9(formatBigDecimal(BigDecimal.valueOf(interessiDettVers)));
		return dto;
	}

	private OutputDatiDTO getRecordDatiTxtCartaTipo3(Long idElabora, int progrTxtParzCarta, String tipoElab,
			AvvisoSollecitoData avvisoSollecito, String motivoSollecito, String dataOpVal, double importoMancante,
			double interessiMancanti, double interessiVersatiTot, double totale, double interessiTeorici,
			BigDecimal totaleTeorico, double speseNotifPerSdCorrente) {
		OutputDatiDTO dto = new OutputDatiDTO();
		dto.setIdElabora(idElabora);
		// dto.setProgressivo(progrTxtParzCarta);
		dto.setProgressivo(progrTxtParzCarta + 3 * 100000);
		dto.setIdOutputFoglio(Long.parseLong(outputFoglio.get(tipoElab)[8]));
		dto.setCodTipoPassoElabora(COD_TIPO_PASSO_ELABORA_ELAB_EMISSIONE);
		dto.setValoreColonna1(getDatiAmminString(tipoElab, avvisoSollecito, motivoSollecito, dataOpVal, importoMancante,
				interessiMancanti, interessiVersatiTot, totale, interessiTeorici, totaleTeorico,
				speseNotifPerSdCorrente));
		dto.setValoreColonna2(avvisoSollecito.getSoggettoGruppo());
		dto.setValoreColonna3("3");
		dto.setValoreColonna4(avvisoSollecito.getAvvisoSollecito().getCodRiscossione());
		dto.setValoreColonna5(formatDate(avvisoSollecito.getAvvisoSollecito().getDataScadenzaPagamento()));
		dto.setValoreColonna6(formatDate(avvisoSollecito.getAvvisoSollecito().getDataVersamentoDett()));
		// TODO NB id_accertamento deve essere aggiunto dopo aver inserito il record in
		// tabella risca_w_accertamento
		dto.setValoreColonna7(null);
		return dto;
	}

	private OutputDatiDTO getRecordDatiTxtCompletoTipo3(Long idElabora, int progrTxtCompleto, String tipoElab,
			AvvisoSollecitoData avvisoSollecito, String motivoSollecito, String dataOpVal, double importoMancante,
			double interessiMancanti, double interessiVersatiTot, double totale, double interessiTeorici,
			BigDecimal totaleTeorico, double speseNotifPerSdCorrente) {
		OutputDatiDTO dto = new OutputDatiDTO();
		dto.setIdElabora(idElabora);
		// dto.setProgressivo(progrTxtCompleto);
		dto.setProgressivo(progrTxtCompleto + 3 * 100000);
		dto.setIdOutputFoglio(Long.parseLong(outputFoglio.get(tipoElab)[7]));
		dto.setCodTipoPassoElabora(COD_TIPO_PASSO_ELABORA_ELAB_EMISSIONE);
		dto.setValoreColonna1(getDatiAmminString(tipoElab, avvisoSollecito, motivoSollecito, dataOpVal, importoMancante,
				interessiMancanti, interessiVersatiTot, totale, interessiTeorici, totaleTeorico,
				speseNotifPerSdCorrente));
		dto.setValoreColonna2(avvisoSollecito.getSoggettoGruppo());
		dto.setValoreColonna3("3");
		dto.setValoreColonna4(avvisoSollecito.getAvvisoSollecito().getCodRiscossione());
		dto.setValoreColonna5(formatDate(avvisoSollecito.getAvvisoSollecito().getDataScadenzaPagamento()));
		dto.setValoreColonna6(formatDate(avvisoSollecito.getAvvisoSollecito().getDataVersamentoDett()));
		// TODO NB id_accertamento deve essere aggiunto dopo aver inserito il record in
		// tabella risca_w_accertamento
		dto.setValoreColonna7(null);
		return dto;
	}

	private OutputDatiDTO getRecordDatiODSTipo3(Long idElabora, int progrOdsDatiAmmin, String tipoElab,
			AvvisoSollecitoData avvisoSollecito, String motivoSollecito, String dataOpVal, double importoMancante,
			double interessiMancanti, double interessiVersatiTot, double totale, double interessiTeorici,
			BigDecimal totaleTeorico, double speseNotifPerSdCorrente) {
		StatiDebitoriAvvisiSollecitiDTO avvSoll = avvisoSollecito.getAvvisoSollecito();
		OutputDatiDTO dto = new OutputDatiDTO();
		dto.setIdElabora(idElabora);
		dto.setProgressivo(progrOdsDatiAmmin);
		dto.setIdOutputFoglio(Long.parseLong(outputFoglio.get(tipoElab)[2]));
		dto.setCodTipoPassoElabora(COD_TIPO_PASSO_ELABORA_ELAB_EMISSIONE);
		dto.setValoreColonna1("3");
		dto.setValoreColonna2(avvisoSollecito.getSoggettoGruppo());
		dto.setValoreColonna3(null);
		dto.setValoreColonna4(formatString(avvSoll.getCodRiscossione()));
		dto.setValoreColonna5(tipoElab.equals(TIPO_ELAB_AVVISO_BONARIO) ? "Avviso bonario" : "Sollecito di pagamento");
		dto.setValoreColonna6(formatString(avvSoll.getDesTipoTitolo()));
		dto.setValoreColonna7(formatString(avvSoll.getNumTitolo()));
		dto.setValoreColonna8(formatDate(avvSoll.getDataProvvedimento()));
		dto.setValoreColonna9(formatString(avvisoSollecito.getCorpoIdricoCaptazione()));
		dto.setValoreColonna10(formatString(avvisoSollecito.getComuneDiCaptazione()));
		dto.setValoreColonna11(formatString(avvSoll.getAnnualitaDiPagamento()));
		dto.setValoreColonna12(formatString(avvSoll.getDescPeriodoPagamento()));
		dto.setValoreColonna13(formatDate(avvSoll.getDataScadenzaPagamento()));
		dto.setValoreColonna14(avvSoll.getFlgDilazione() == 0 ? "N" : "S");
		dto.setValoreColonna15(motivoSollecito);
		dto.setValoreColonna16(formatBigDecimal(avvSoll.getCanoneDovuto()));
		dto.setValoreColonna17(formatBigDecimal(avvSoll.getImportoVersato()));
		dto.setValoreColonna18(dataOpVal);
		dto.setValoreColonna19(formatBigDecimal(BigDecimal.valueOf(importoMancante)));
		dto.setValoreColonna20(formatBigDecimal(BigDecimal.valueOf(interessiMancanti)));
		dto.setValoreColonna21(formatBigDecimal(avvSoll.getRataInteressiMaturati()));
		dto.setValoreColonna22(formatBigDecimal(BigDecimal.valueOf(interessiVersatiTot)));
		dto.setValoreColonna23(formatBigDecimal("" + speseNotifPerSdCorrente));
		dto.setValoreColonna24(formatBigDecimal(BigDecimal.valueOf(totale)));
		if (tipoElab.equals(TIPO_ELAB_SOLLECITO)) {
			dto.setValoreColonna25(formatBigDecimal(BigDecimal.valueOf(interessiTeorici)));
			dto.setValoreColonna26(formatBigDecimal(totaleTeorico));
		}
		// TODO NB id_accertamento deve essere aggiunto dopo aver inserito il record in
		// tabella risca_w_accertamento
		dto.setValoreColonna89(null);
		dto.setValoreColonna90("" + avvSoll.getIdStatoDebitorio());

		return dto;
	}

	private String getDatiAmminString(String tipoElab, AvvisoSollecitoData avvisoSollecito, String motivoSollecito,
			String dataOpVal, double importoMancante, double interessiMancanti, double interessiVersatiTot,
			double totale, double interessiTeorici, BigDecimal totaleTeorico, double speseNotifPerSdCorrente) {
		StatiDebitoriAvvisiSollecitiDTO avvSoll = avvisoSollecito.getAvvisoSollecito();
		StringBuilder sb = new StringBuilder();
		sb.append("3").append(";");
		sb.append(avvisoSollecito.getSoggettoGruppo()).append(";");
		sb.append(";");
		sb.append(formatString(avvSoll.getCodRiscossione())).append(";");
		sb.append(tipoElab.equals(TIPO_ELAB_AVVISO_BONARIO) ? "Avviso bonario" : "Sollecito di pagamento").append(";");
		sb.append(formatString(avvSoll.getDesTipoTitolo())).append(";");
		sb.append(formatString(avvSoll.getNumTitolo())).append(";");
		sb.append(formatDate(avvSoll.getDataProvvedimento())).append(";");
		sb.append(formatString(avvisoSollecito.getCorpoIdricoCaptazione())).append(";");
		sb.append(formatString(avvisoSollecito.getComuneDiCaptazione())).append(";");
		sb.append(formatString(avvSoll.getAnnualitaDiPagamento())).append(";");
		sb.append(formatString(avvSoll.getDescPeriodoPagamento())).append(";");
		sb.append(formatDate(avvSoll.getDataScadenzaPagamento())).append(";");
		sb.append(avvSoll.getFlgDilazione() == 0 ? "N" : "S").append(";");
		sb.append(motivoSollecito).append(";");
		sb.append(formatBigDecimalTxt(avvSoll.getCanoneDovuto())).append(";");
		sb.append(formatBigDecimalTxt(avvSoll.getImportoVersato())).append(";");
		sb.append(formatString(dataOpVal)).append(";");
		sb.append(formatBigDecimalTxt(BigDecimal.valueOf(importoMancante))).append(";");
		sb.append(formatBigDecimalTxt(BigDecimal.valueOf(interessiMancanti))).append(";");
		sb.append(formatBigDecimalTxt(avvSoll.getRataInteressiMaturati())).append(";");
		sb.append(formatBigDecimalTxt(BigDecimal.valueOf(interessiVersatiTot))).append(";");
		sb.append(formatBigDecimalTxt("" + speseNotifPerSdCorrente)).append(";");
		sb.append(formatBigDecimalTxt(BigDecimal.valueOf(totale))).append(";");
		if (tipoElab.equals(TIPO_ELAB_SOLLECITO)) {
			sb.append(formatBigDecimalTxt(BigDecimal.valueOf(interessiTeorici))).append(";");
			sb.append(formatBigDecimalTxt(totaleTeorico)).append(";");
		} else {
			sb.append(";");
			sb.append(";");
		}

		return sb.toString();
	}

	private OutputDatiDTO getRecordDatiTxtCartaTipo2(Long idElabora, int progrTxtParzCarta, String tipoElab,
			RecapitiExtendedDTO recapito, AvvisoSollecitoData avvisoSollecito, String protocolloProgr,
			double subtotSpeseNotifSogg) {
		OutputDatiDTO dto = new OutputDatiDTO();
		dto.setIdElabora(idElabora);
		// TODO non capisco perchè fare questa operazione
		// Per ora ci metto il progressivo e basta
		// dto.setProgressivo(progrTxtParzCarta + 2 * 100000);
		dto.setProgressivo(progrTxtParzCarta);
		dto.setIdOutputFoglio(Long.parseLong(outputFoglio.get(tipoElab)[8]));
		dto.setCodTipoPassoElabora(COD_TIPO_PASSO_ELABORA_ELAB_EMISSIONE);
		dto.setValoreColonna1(getDatiTitolareString(recapito, avvisoSollecito, protocolloProgr, subtotSpeseNotifSogg));
		dto.setValoreColonna2(avvisoSollecito.getSoggettoGruppo());
		dto.setValoreColonna3("2");
		dto.setValoreColonna4(avvisoSollecito.getAvvisoSollecito().getCodRiscossione());
		dto.setValoreColonna5(formatDate(avvisoSollecito.getAvvisoSollecito().getDataScadenzaPagamento()));
		dto.setValoreColonna6(formatDate(avvisoSollecito.getAvvisoSollecito().getDataVersamentoDett()));
		// TODO NB id_accertamento deve essere aggiunto dopo aver inserito il record in
		// tabella risca_w_accertamento
		dto.setValoreColonna7(null);
		return dto;
	}

	private OutputDatiDTO getRecordDatiTxtCompletoTipo2(Long idElabora, int progrTxtCompleto, String tipoElab,
			RecapitiExtendedDTO recapito, AvvisoSollecitoData avvisoSollecito, String protocolloProgr,
			double subtotSpeseNotifSogg) {
		OutputDatiDTO dto = new OutputDatiDTO();
		dto.setIdElabora(idElabora);
		// TODO non capisco perchè fare questa operazione
		// Per ora ci metto il progressivo e basta
		// dto.setProgressivo(progrTxtCompleto + 2 * 100000);
		dto.setProgressivo(progrTxtCompleto);
		dto.setIdOutputFoglio(Long.parseLong(outputFoglio.get(tipoElab)[7]));
		dto.setCodTipoPassoElabora(COD_TIPO_PASSO_ELABORA_ELAB_EMISSIONE);
		dto.setValoreColonna1(getDatiTitolareString(recapito, avvisoSollecito, protocolloProgr, subtotSpeseNotifSogg));
		dto.setValoreColonna2(avvisoSollecito.getSoggettoGruppo());
		dto.setValoreColonna3("2");
		dto.setValoreColonna4(avvisoSollecito.getAvvisoSollecito().getCodRiscossione());
		dto.setValoreColonna5(formatDate(avvisoSollecito.getAvvisoSollecito().getDataScadenzaPagamento()));
		dto.setValoreColonna6(formatDate(avvisoSollecito.getAvvisoSollecito().getDataVersamentoDett()));
		// TODO NB id_accertamento deve essere aggiunto dopo aver inserito il record in
		// tabella risca_w_accertamento
		dto.setValoreColonna7(null);
		return dto;
	}

	private String getDatiTitolareString(RecapitiExtendedDTO recapito, AvvisoSollecitoData avvisoSollecito,
			String protocolloProgr, double subtotSpeseNotifSogg) {
		StatiDebitoriAvvisiSollecitiDTO avvSoll = avvisoSollecito.getAvvisoSollecito();
		IndirizzoSpedizioneDTO indSped = getIndirizzoSpedizione(recapito, avvSoll.getIdGruppoSoggetto());

		StringBuilder sb = new StringBuilder();
		sb.append("2").append(";");
		sb.append(avvisoSollecito.getSoggettoGruppo()).append(";");
		sb.append(";");
		sb.append(formatString(indSped.getDestinatarioPostel())).append(";");
		sb.append(avvSoll.getCfSoggetto()).append(";");
		sb.append(Codice_Fiscale_Eti_Calc_Dest).append(";");
		sb.append(formatString(indSped.getPressoPostel())).append(";");
		sb.append(formatString(indSped.getIndirizzoPostel())).append(";");
		sb.append(formatString(indSped.getCapPostel())).append(";");
		sb.append(formatString(indSped.getCittaPostel())).append(";");
		sb.append(formatString(indSped.getProvinciaPostel())).append(";");
		sb.append(protocolloProgr).append(";");
		sb.append(formatDate(avvisoSollecito.getDataProtocollo())).append(";");
		sb.append(formatDate(avvisoSollecito.getDataScadenzaPagamento())).append(";");
		String modalitaInvio = avvisoSollecito.getTipoElabora().equals(TIPO_ELAB_AVVISO_BONARIO)
				? getModalitaInvioAB(recapito.getTipoInvio().getCodTipoInvio())
				: getModalitaInvioSP(recapito.getTipoInvio().getCodTipoInvio());
		sb.append(modalitaInvio).append(";");
		if(modalitaInvio.equals(MODALITA_INVIO_CARTA)) {
			sb.append("").append(";");
		} else {
			sb.append(getIndirizzoMailPec(recapito)).append(";");
		}
		sb.append(formatDate(avvisoSollecito.getDataProtocollo())).append(";");
		sb.append(subtotSpeseNotifSogg > 0 ? formatBigDecimal("" + subtotSpeseNotifSogg) : "").append(";");

		return sb.toString();
	}

	private OutputDatiDTO getRecordDatiODSTipo2(Long idElabora, int progressivo, String tipoElab,
			RecapitiExtendedDTO recapito, AvvisoSollecitoData avvisoSollecito, String protocolloProgr,
			double subtotSpeseNotifSogg) {
		StatiDebitoriAvvisiSollecitiDTO avvSoll = avvisoSollecito.getAvvisoSollecito();
		IndirizzoSpedizioneDTO indSped = getIndirizzoSpedizione(recapito, avvSoll.getIdGruppoSoggetto());

		OutputDatiDTO dto = new OutputDatiDTO();
		dto.setIdElabora(idElabora);
		dto.setProgressivo(progressivo);
		dto.setIdOutputFoglio(Long.parseLong(outputFoglio.get(tipoElab)[1]));
		dto.setCodTipoPassoElabora(COD_TIPO_PASSO_ELABORA_ELAB_EMISSIONE);
		dto.setValoreColonna1("2");
		dto.setValoreColonna2(avvisoSollecito.getSoggettoGruppo());
		dto.setValoreColonna3(null);
		dto.setValoreColonna4(formatString(indSped.getDestinatarioPostel()));
		dto.setValoreColonna5(avvSoll.getCfSoggetto());
		dto.setValoreColonna6(Codice_Fiscale_Eti_Calc_Dest);
		dto.setValoreColonna7(formatString(indSped.getPressoPostel()));
		dto.setValoreColonna8(formatString(indSped.getIndirizzoPostel()));
		dto.setValoreColonna9(formatString(indSped.getCapPostel()));
		dto.setValoreColonna10(formatString(indSped.getCittaPostel()));
		dto.setValoreColonna11(formatString(indSped.getProvinciaPostel()));
		dto.setValoreColonna12(protocolloProgr);
		dto.setValoreColonna13(formatDate(avvisoSollecito.getDataProtocollo()));
		dto.setValoreColonna14(formatDate(avvisoSollecito.getDataScadenzaPagamento()));
		String modalitaInvio = avvisoSollecito.getTipoElabora().equals(TIPO_ELAB_AVVISO_BONARIO)
				? getModalitaInvioAB(recapito.getTipoInvio().getCodTipoInvio())
				: getModalitaInvioSP(recapito.getTipoInvio().getCodTipoInvio());
		dto.setValoreColonna15(modalitaInvio);
		dto.setValoreColonna16(getIndirizzoMailPec(recapito));
		dto.setValoreColonna17(formatYearFromString(avvisoSollecito.getDataProtocollo()));
		dto.setValoreColonna18(subtotSpeseNotifSogg > 0 ? formatBigDecimal("" + subtotSpeseNotifSogg) : "");
		return dto;
	}

	private RecapitiExtendedDTO getRecapitoTitolare(AvvisoSollecitoData avvisoSollecito) {
		Long idRecapito = null;
		if (avvisoSollecito.getTipoElabora().equals(TIPO_ELAB_AVVISO_BONARIO)) {
			idRecapito = avvisoSollecito.getAvvisoSollecito().getIdRecapitoA() != null
					? avvisoSollecito.getAvvisoSollecito().getIdRecapitoA()
					: avvisoSollecito.getAvvisoSollecito().getIdRecapitoP();
		} else {
			idRecapito = avvisoSollecito.getAvvisoSollecito().getIdRecapitoP();
		}
		List<RecapitiExtendedDTO> recapiti = avvisoSollecito.getTitolare().getRecapiti();
		for (RecapitiExtendedDTO recapito : recapiti) {
			if (recapito.getIdRecapito().equals(idRecapito)) {
				return recapito;
			}
		}
		return null;
	}

	private IndirizzoSpedizioneDTO getIndirizzoSpedizione(RecapitiExtendedDTO recapito, Long idGruppoSoggetto) {
		IndirizzoSpedizioneDTO indSped = null;
		if (recapito.getIndirizziSpedizione() != null && recapito.getIndirizziSpedizione().size() > 0) {
			if (idGruppoSoggetto == null) {
				for (IndirizzoSpedizioneDTO ind : recapito.getIndirizziSpedizione()) {
					if (ind.getIdGruppoSoggetto() == null && ind.getIndValidoPostel().equals(1l)) {
						indSped = ind;
					}
				}
			} else {
				for (IndirizzoSpedizioneDTO ind : recapito.getIndirizziSpedizione()) {
					if (ind.getIdGruppoSoggetto() != null && ind.getIdGruppoSoggetto().equals(idGruppoSoggetto)
							&& ind.getIndValidoPostel().equals(1l)) {
						indSped = ind;
					}
				}
			}
		}
		return indSped;
	}

	private String calcolaNumeroProtocolloSped(String numProtocollo, int progrPerTitolare) {
		// Padding con zero a sinistra fino a lunghezza 4
		String protocollo = null;
		int index = numProtocollo.indexOf('/');
		if (index != -1) {
			String protProg = numProtocollo.substring(0, index);
			protocollo = protProg + " (Rif " + String.format("%1$4s", progrPerTitolare).replace(' ', '0') + ")"
					+ numProtocollo.substring(index);
			;
		} else {
			protocollo = numProtocollo + " (Rif " + String.format("%1$4s", progrPerTitolare).replace(' ', '0') + ")";
		}
		return protocollo;
	}

	private String getMotivoSollecito(StatiDebitoriAvvisiSollecitiDTO avvSoll) {
		String motivoSollecito = "";
		Date dataScadPag = avvSoll.getDataScadenzaPagamento();
		Date dataVersDett = avvSoll.getDataVersamentoDett() == null ? getTodayDate() : avvSoll.getDataVersamentoDett();
		if (doubleValue(avvSoll.getCanoneDovuto()) == doubleValue(avvSoll.getDettPagImpVers())
				&& avvSoll.getDataVersamentoDett() != null && dataVersDett.after(dataScadPag)) {
			motivoSollecito = "Ritardato Pagamento";
		} else if (avvSoll.getDettPagImpVers()!= null && doubleValue(avvSoll.getDettPagImpVers()) < doubleValue(avvSoll.getCanoneDovuto())) {
			motivoSollecito = "Insufficiente Pagamento";
		} else if (avvSoll.getRataPag() == null) {
			motivoSollecito = "Omesso Pagamento";
		}
		return motivoSollecito;
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
			LOGGER.error("[AvvisiSollecitiApiServiceImpl::logStep] (Exception) ", e);
		}
		LOGGER.debug(note);
	}

	private String formatBigDecimal(String value) {
		if (value == null || value.isEmpty())
			return "0.00";
		BigDecimal bdValue = new BigDecimal(value);
		return formatBigDecimal(bdValue);
	}

	private String formatBigDecimal(double value) {
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

	private String formatString(String value) {
		return value == null ? "" : value.trim();
	}

	private String formatString50(String value) {
		if (value == null) {
			return "";
		} else if (value.trim().length() > 50) {
			return value.trim().substring(0, 50);
		} else {
			return value;
		}
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

	private Date parseDate(String dateStr) {
		if (dateStr == null)
			return null;
		SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
		try {
			return df.parse(dateStr);
		} catch (Exception e) {
			return null;
		}
	}

	private String formatYearFromString(String date) {
		if (date.length() > 4) {
			return date.substring(0, 4);
		}
		return date;
	}

	private String getModalitaInvio(String tipoElabora, String codTipoInvio) {
		if (tipoElabora.equals(COD_TIPO_ELABORA_AB)) {
			return getModalitaInvioAB(codTipoInvio);
		} else {
			return getModalitaInvioSP(codTipoInvio);
		}
	}

	private String getModalitaInvioAB(String codTipoInvio) {
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

	private String getModalitaInvioSP(String codTipoInvio) {
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

	private Long getIdTipoInvio(String tipoElabora, String codTipoInvio) {
		if (tipoElabora.equals(COD_TIPO_ELABORA_AB)) {
			return getIdTipoInvioAB(codTipoInvio);
		} else {
			return getIdTipoInvioSP(codTipoInvio);
		}
	}

	private Long getIdTipoInvioAB(String codTipoInvio) {
		switch (codTipoInvio) {
		case "C":
			return Long.valueOf(3);
		case "P":
			return Long.valueOf(1);
		case "E":
			return Long.valueOf(2);
		default:
			return null;
		}
	}

	private Long getIdTipoInvioSP(String codTipoInvio) {
		switch (codTipoInvio) {
		case "C":
			return Long.valueOf(3);
		case "P":
			return Long.valueOf(1);
		case "E":
			return Long.valueOf(3);
		default:
			return null;
		}
	}

	private String getIndirizzoMailPec(RecapitiExtendedDTO recapito) {
		switch (recapito.getTipoInvio().getCodTipoInvio()) {
		case "C":
			return "";
		case "P":
			return recapito.getPec();
		case "E":
			return recapito.getEmail();
		default:
			return "";
		}
	}

	private double doubleValue(BigDecimal bd) {
		if (bd == null)
			return 0.0;
		return bd.doubleValue();

	}

	private Date getTodayDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return calendar.getTime();
	}
}

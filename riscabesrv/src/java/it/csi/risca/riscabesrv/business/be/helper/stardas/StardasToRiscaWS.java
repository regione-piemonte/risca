/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.business.be.helper.stardas;

import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.risca.riscabesrv.business.be.impl.dao.AmbitiConfigDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.AmbitiDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.ArchiviazioneActaDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.ElaboraDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.EmailServizioDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.InvioActaDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RegistroElaboraDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.SpedizioneActaDAO;
import it.csi.risca.riscabesrv.dto.AmbitoConfigDTO;
import it.csi.risca.riscabesrv.dto.AmbitoDTO;
import it.csi.risca.riscabesrv.dto.ArchiviazioneActaDTO;
import it.csi.risca.riscabesrv.dto.ElaboraDTO;
import it.csi.risca.riscabesrv.dto.EmailServizioDTO;
import it.csi.risca.riscabesrv.dto.InvioActaDTO;
import it.csi.risca.riscabesrv.dto.RegistroElaboraDTO;
import it.csi.risca.riscabesrv.dto.RegistroElaboraExtendedDTO;
import it.csi.risca.riscabesrv.dto.SpedizioneActaDTO;
import it.csi.risca.riscabesrv.dto.StatoElaborazioneDTO;
import it.csi.risca.riscabesrv.util.Constants;
import it.csi.risca.riscabesrv.util.mail.AttachmentData;
import it.csi.risca.riscabesrv.util.mail.MailException;
import it.csi.risca.riscabesrv.util.mail.MailInfo;
import it.csi.risca.riscabesrv.util.mail.MailManager;
import it.csi.stardas.services.stardascallbackservice.EsitoSmistaDocumento;
import it.csi.stardas.services.stardascallbackservice.EsitoSmistaDocumentoResponse;
import it.csi.stardas.services.stardascommontypes.ResultType;

@WebService(name = "StardasToRiscaWS", targetNamespace = "http://www.csi.it/stardas/services/StardasCallbackService", serviceName = "StardasToRiscaWS")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, parameterStyle = SOAPBinding.ParameterStyle.BARE)
public class StardasToRiscaWS extends SpringBeanAutowiringSupport implements IStardasToRiscaWS {

	private static final String ATTORE = "riscabatcharc";
	private static final String FASE = "Archiviazione-AcquisizioneEsitoActa";
	private static final String COD_FASE = "ACQ_ESITO_ARCHIVIAZIONE_ACTA";

	private static final String PASSO_CBACK_STARDAS = "CBACK_STARDAS";
	// private static final String PASSO_UPD_INVIO_ACTA = "UPD_INVIO_ACTA";
	private static final String PASSO_SPED_ACTA_ARCHIVIATA = "SPED_ACTA_ARCHIVIATA";
	private static final String PASSO_INVIO_MAIL_SERVIZIO = "INVIO_MAIL_SERVIZIO";
	private static final String PASSO_DELETE_PDF = "DELETE_PDF";

	private static final String STATO_ELAB_INVACTA_AR = "INVACTA_AR";

	private static final String COD_EMAIL_E_ACQU_ACTA = "E_ACQU_ACTA";

	public static final String AMBITO_CONFIG_CHIAVE_MAIL_MITT = "SRVAPP.MittenteSegnalazioniServizioApplicativo";
	public static final String AMBITO_CONFIG_CHIAVE_MAIL_DEST = "SRVAPP.DestinatarioServizioApplicativo";
	public static final String AMBITO_CONFIG_CHIAVE_NOME_ALLEGATO = "SRVAPP.NomeAllegatoServizioApplicativo";
	private static final String AMBITO_CONFIG_CHIAVE_MAIL_USERNAME = "SRVAPP.UsernameServizioApplicativo";
	private static final String AMBITO_CONFIG_CHIAVE_MAIL_PASSWORD = "SRVAPP.PasswordServizioApplicativo";
	
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final String DATE_FORMAT_MAIL = "dd/MM/yyyy";
	public static final String HOUR_FORMAT_MAIL = "HH:mm:ss";

	private static Logger lOGGER = Logger.getLogger(Constants.LOGGER_NAME);

	@Autowired
	private RegistroElaboraDAO registroElaboraDAO;

	@Autowired
	private ElaboraDAO elaboraDAO;

	@Autowired
	private SpedizioneActaDAO spedizioneActaDAO;

	@Autowired
	private ArchiviazioneActaDAO archiviazioneActaDAO;

	@Autowired
	private InvioActaDAO invioActaDAO;

	@Autowired
	private AmbitiDAO ambitiDAO;

	@Autowired
	private AmbitiConfigDAO ambitiConfigDAO;

	@Autowired
	private EmailServizioDAO emailServizioDAO;

	@Autowired
	private MailManager mailManager;

	@WebResult(name = "EsitoSmistaDocumentoResponse", targetNamespace = "http://www.csi.it/stardas/services/StardasCallbackService", partName = "parameters")
	@WebMethod(operationName = "EsitoSmistaDocumento", action = "http://www.csi.it/stardas/services/StardasCallbackService/EsitoSmistaDocumento")
	public EsitoSmistaDocumentoResponse EsitoSmistaDocumento(
			@WebParam(partName = "parameters", name = "EsitoSmistaDocumento", targetNamespace = "http://www.csi.it/stardas/services/StardasCallbackService") EsitoSmistaDocumento parameters)
			throws Exception {
		lOGGER.debug("[StardasToRiscaWS::EsitoSmistaDocumento] BEGIN");
		EsitoSmistaDocumentoResponse response = new EsitoSmistaDocumentoResponse();
		ResultType result = new ResultType();
		Date dataEsito = new Date();
		String idDocFruitore = parameters.getEsito().getIdDocumentoFruitore();
		String nap = null;
		Long idAccertamento = null;
		boolean isAccertamento = false;
		if (idDocFruitore.indexOf("_") != -1) {
			// E' l'esito di un accertamento
			isAccertamento = true;
			idAccertamento = Long.valueOf(idDocFruitore.split("_")[1]);
		} else {
			// E' l'esito di un avviso di pagamento
			nap = idDocFruitore;
		}

		String messageUUID = parameters.getEsito().getMessageUUID();
		String codEsito = parameters.getEsito().getEsitoTrattamento().getCodice();
		String desEsito = parameters.getEsito().getEsitoTrattamento().getMessaggio();

		if (isEsitoProvvisorio(codEsito)) {
			// Abbiamo ricevuto il codice 001 che e' un codice provvisorio usato in presenza
			// di allegati
			// Noi non gestiamo allegati per cui ignoriamo la risposta e mandiamo un esito
			// con codice di errore a Stardas
			lOGGER.error(
					"[StardasToRiscaWS::EsitoSmistaDocumento] ERROR: Ricevuto codice provvisorio 001 per il documento con id: "
							+ idDocFruitore);
			result.setCodice("199");
			result.setMessaggio("Errore applicativo: Ricevuto codice provvisorio 001 per il messageUUID: " + messageUUID
					+ " ma il documento non contiente allegati.");
			response.setEsito(result);
			return response;
		}

		if (isDocumentoGiaClassificato(codEsito)) {
			// Il codice 135 che indica un documento gia' classificato su Acta lo trasformo
			// in 000 in modo che per noi sia considerato come acquisito, altrimenti viene
			// poi preso in considerazione in caso di reinvio
			codEsito = "000";
		}

		InvioActaDTO invioActa = null;
		if (isAccertamento) {
			invioActa = invioActaDAO.loadInvioActaByIdAccertamento(idAccertamento);
		} else {
			invioActa = invioActaDAO.loadInvioActaByNap(nap);
		}
		if (invioActa != null) {
			SpedizioneActaDTO spedizioneActa = spedizioneActaDAO
					.loadSpedizioneActaById(invioActa.getIdSpedizioneActa());
			if (spedizioneActa != null) {
				ElaboraDTO elaborazione = elaboraDAO.loadElaboraById(spedizioneActa.getIdElabora(), false);
				if (elaborazione != null) {
					Long idElabora = elaborazione.getIdElabora();
					int flgArchiviata = 0;
					if (isSuccessfullStardasCode(codEsito)) {
						flgArchiviata = 1;
						logStep(idElabora, PASSO_CBACK_STARDAS, 0, FASE + " - Callback OK per il documento con id : "
								+ idDocFruitore + " relativo alla spedizione " + spedizioneActa.getIdSpedizioneActa());
					} else {
						logStep(idElabora, PASSO_CBACK_STARDAS, 1,
								FASE + " - Callback KO per il documento con id : " + idDocFruitore
										+ " relativo alla spedizione " + spedizioneActa.getIdSpedizioneActa()
										+ " - codEsito: " + codEsito);
					}
					
					// Substring descrizione esito se troppo lungo per il nostro DB
					if (desEsito!= null && desEsito.length() > 500) {
						desEsito = desEsito.substring(0, 500);
					}

					// Update della tebella risca_t_invio_acta con l'esito acquisizione
					invioActa.setCodEsitoAcquisizioneActa(codEsito);
					invioActa.setDescEsitoAcquisizioneActa(desEsito);
					invioActa.setDataEsitoActa(dataEsito);
					invioActa.setFlgArchiviataActa(flgArchiviata);
					invioActaDAO.updateInvioActa(invioActa);

					// Log aggiornamento della tabella risca_t_invio_acta
//					logStep(idElabora, PASSO_UPD_INVIO_ACTA, 0, FASE
//							+ " - Aggiornamento esito Archiviazione Acta su risca_t_invio_acta per documento con id: "
//							+ idDocFruitore + " - EsitoAcquisizioneActa: " + codEsito);

					if (isSuccessfullStardasCode(codEsito)) {
						try {
							File file = new File(invioActa.getNomeFile());
							file.delete();
							logStep(idElabora, PASSO_DELETE_PDF, 0,
									FASE + " - Cancellazione del PDF OK per il documento con id: " + idDocFruitore);
						} catch (Exception e) {
							logStep(idElabora, PASSO_DELETE_PDF, 1,
									FASE + " - Errore cancellazione del PDF per il documento con id: " + idDocFruitore);
						}
					}

					result.setCodice("000");
					result.setMessaggio("Operazione completata con esito positivo");

					List<ArchiviazioneActaDTO> docDaArchiviare = archiviazioneActaDAO
							.loadDocumentiActaNonArchiviatiBySpedActa(spedizioneActa.getIdSpedizioneActa(),
									spedizioneActa.getCodTipoSpedizione());

					if (docDaArchiviare.size() == 0) {
						// Tutti i documenti della spedizione acta in questione risultano archiviati
						// correttamente in questo caso aggiornare il flg_archiviata_acta su
						// risca_r_spedizione_acta

						spedizioneActa.setFlgArchiviataActa(1);
						spedizioneActaDAO.updateSpedizioneActa(spedizioneActa);

						logStep(idElabora, PASSO_SPED_ACTA_ARCHIVIATA, 0,
								FASE + " - Tutta la spedizione risulta archiviata - idSpedizione "
										+ spedizioneActa.getIdSpedizioneActa());

						// Aggiornare lo stato dell'elaborazione in INVACTA_AR
						aggiornaElabInvactaOK(elaborazione);

						// Estraggo i dicumenti con esito acquisizione warning o error (diverso da 000)
						List<InvioActaDTO> docWarnErr = invioActaDAO
								.loadDocumentiWarningError(spedizioneActa.getIdSpedizioneActa());

						// Invio Mail di completamento dell'archiviazione per la spedizione in questione
						invioMailAcquisizioneCompletata(spedizioneActa.getIdSpedizioneActa(), elaborazione, docWarnErr);
					}

				} else {
					// errore elaborazione non trovata
					lOGGER.error("[StardasToRiscaWS::EsitoSmistaDocumento] ERROR: Elaborazione non trovata idElabora: "
							+ spedizioneActa.getIdElabora());
					result.setCodice("299");
					result.setMessaggio("Errore di sistema: elaborazione non trovata - messageUUID: " + messageUUID);
				}
			} else {
				// errore spedizione acta non trovata
				lOGGER.error(
						"[StardasToRiscaWS::EsitoSmistaDocumento] ERROR: Spedizione Acta non trovata idSpedizioneActa: "
								+ invioActa.getIdSpedizioneActa());
				result.setCodice("299");
				result.setMessaggio("Errore di sistema: spedizione Acta non trovata - messageUUID: " + messageUUID);
			}
		} else {
			// errore, l'esito si riferisce ad un documento non presente su risca
			lOGGER.error(
					"[StardasToRiscaWS::EsitoSmistaDocumento] ERROR: Ricevuto esito relativo ad un documento non presente su RISCA: "
							+ idDocFruitore);
			result.setCodice("199");
			result.setMessaggio("Ricevuto esito relativo ad un messageUUID non presente su RISCA: " + messageUUID);
		}

		response.setEsito(result);

		lOGGER.debug("[StardasToRiscaWS::EsitoSmistaDocumento] END");
		return response;
	}

	private void logStep(Long idElabora, String codPasso, int esito, String note) {
		RegistroElaboraDTO registroElabora = new RegistroElaboraDTO();
		registroElabora.setIdElabora(idElabora);
		registroElabora.setCodPassoElabora(codPasso);
		registroElabora.setFlgEsitoElabora(esito);
		registroElabora.setNotaElabora(note);
		registroElabora.setGestAttoreIns(ATTORE);
		registroElabora.setGestAttoreUpd(ATTORE);
		registroElabora.setCodFaseElabora(COD_FASE);
		try {
			registroElaboraDAO.saveRegistroElabora(registroElabora);
		} catch (Exception e) {
			lOGGER.error("[StardasToRiscaWS::logStep] ERROR", e);

		}
		lOGGER.debug(note);
	}

	private void aggiornaElabInvactaOK(ElaboraDTO elabora) {
		StatoElaborazioneDTO stato = new StatoElaborazioneDTO();
		stato.setCodStatoElabora(STATO_ELAB_INVACTA_AR);
		elabora.setStatoElabora(stato);
		elabora.setGestAttoreUpd(ATTORE);
		try {
			elaboraDAO.updateElabora(elabora);
		} catch (Exception e) {
			lOGGER.error("[StardasToRiscaWS::aggiornaElabInvactaOK] ERROR", e);

		}
	}

	private int invioMailAcquisizioneCompletata(Long idSpedizioneActa, ElaboraDTO elabora,
			List<InvioActaDTO> docWarnErr) throws MailException, SQLException {
		lOGGER.debug("[StardasToRiscaWS::" + FASE + "::InvioMail] BEGIN");
		int stepStatus = 0;

		AmbitoDTO ambito = ambitiDAO.loadAmbitoByIdAmbito(elabora.getAmbito().getIdAmbito());

		String mailDest = null;
		String mailMitt = null;
		String user = null;
		String password = null;
		Long idElabora = elabora.getIdElabora();
		List<AmbitoConfigDTO> ambitoConfig = null;

		ambitoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(ambito.getCodAmbito(),
				AMBITO_CONFIG_CHIAVE_MAIL_DEST);

		if (ambitoConfig.size() > 0) {
			mailDest = ambitoConfig.get(0).getValore();

			ambitoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(ambito.getCodAmbito(),
					AMBITO_CONFIG_CHIAVE_MAIL_MITT);

			if (ambitoConfig.size() > 0) {
				mailMitt = ambitoConfig.get(0).getValore();
				lOGGER.debug("[StardasToRiscaWS::" + FASE + "::InvioMail] mailDest = " + mailDest);
				lOGGER.debug("[StardasToRiscaWS::" + FASE + "::InvioMail] mailMitt = " + mailMitt);
				
				user = ambitiConfigDAO
						.loadAmbitiConfigByCodeAndKey(ambito.getCodAmbito(), AMBITO_CONFIG_CHIAVE_MAIL_USERNAME)
						.get(0).getValore();
				password = ambitiConfigDAO
						.loadAmbitiConfigByCodeAndKey(ambito.getCodAmbito(), AMBITO_CONFIG_CHIAVE_MAIL_PASSWORD)
						.get(0).getValore();

				String msg = FASE + " - InvioMail - Completata archiviazione per idSpedizioneActa: " + idSpedizioneActa;
				logStep(idElabora, PASSO_INVIO_MAIL_SERVIZIO, 0, msg);

			} else {
				logStep(idElabora, PASSO_INVIO_MAIL_SERVIZIO, 1, FASE
						+ " - InvioMail - La mail del mittente per le comunicazioni al Servizio Applicativo non e' stata configurata per l'ambito  "
						+ ambito.getCodAmbito());
			}
		} else {
			logStep(idElabora, PASSO_INVIO_MAIL_SERVIZIO, 1, FASE
					+ " - InvioMail - La mail del destinatario del Servizio Applicativo non e' stata configurata per l'ambito "
					+ ambito.getCodAmbito());
		}

		List<RegistroElaboraExtendedDTO> registroElaborazioni = null;
		try {
			registroElaborazioni = registroElaboraDAO.loadRegistroElaboraByElaboraAndAmbito("" + elabora.getIdElabora(),
					"" + elabora.getAmbito().getIdAmbito(), null, COD_FASE);
		} catch (Exception e1) {
			lOGGER.error("[StardasToRiscaWS::invioMailAcquisizioneCompletata] ERROR", e1);
		}
		lOGGER.debug("[StardasToRiscaWS::" + FASE + "::InvioMail] invioMail - trovate " + registroElaborazioni.size()
				+ " registrazioni");

		try {
			ambitoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(ambito.getCodAmbito(),
					AMBITO_CONFIG_CHIAVE_NOME_ALLEGATO);
		} catch (SQLException e) {
			lOGGER.error(e);
		}
		String nomeAllegato = ambitoConfig != null ? ambitoConfig.get(0).getValore()
				: "RISCA-LogElaborazione-{dataElaborazione}.txt";

		String oggetto = getOggettoMail(elabora, idSpedizioneActa, COD_EMAIL_E_ACQU_ACTA);

		StringBuilder testo = new StringBuilder();
		testo.append("Completata archiviazione ACTA per idSpedizioneActa: " + idSpedizioneActa + "\n\n");
		testo.append(
				"Nel file di testo in allegato si riportano, per l'elaborazione in oggetto, i passi eseguiti ed il loro esito. \n");
		testo.append("\n");

		if (docWarnErr.size() > 0) {
			testo.append("Per i seguenti documenti l'acquisizione presenta un codice di warning o di errore:\n\n");
			for (InvioActaDTO doc : docWarnErr) {
				testo.append(doc.getNap() + " - codEsito: " + doc.getCodEsitoAcquisizioneActa() + " - descEsito: "
						+ doc.getDescEsitoAcquisizioneActa() + "\n");
			}
		}
		MailInfo mailInfo = prepareMail(oggetto, testo.toString(), registroElaborazioni, mailDest, mailMitt,
				nomeAllegato, elabora.getDataRichiesta(), user, password);
		mailManager.sendMail(mailInfo);

		return stepStatus;
	}

	private String getOggettoMail(ElaboraDTO elabora, Long idSpedizioneActa, String code) {
		EmailServizioDTO emailDto = emailServizioDAO.loadEmailServizioByCodEmail(code);
		String oggetto = emailDto.getOggettoEmailServizio();
		oggetto = StringUtils.replace(oggetto, "[TIPO_ELABORA]", elabora.getTipoElabora().getDesTipoElabora());
		oggetto = StringUtils.replace(oggetto, "[ID_SPEDIZIONE_ACTA]", "" + idSpedizioneActa);
		oggetto = StringUtils.replace(oggetto, "[ID_ELABORA]", "" + elabora.getIdElabora());
		return oggetto;
	}

	private MailInfo prepareMail(String oggetto, String testo, List<RegistroElaboraExtendedDTO> registroElaborazioni,
			String mailDest, String mailMitt, String nomeAllegato, Date dataRichiesta, String user, String password) {
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
		String filename = nomeAllegato.replace("{dataElaborazione}", df2.format(dataRichiesta));
		attachment.setFilename(filename);
		attachment.setData(txtElencoElab.toString().getBytes());
		attachment.setMimeType("text/plain");

		mailInfo.setAttachments(new AttachmentData[] { attachment });

		return mailInfo;
	}

	private int getIntFromCode(String code) {
		// Regex to remove leading zeros from a string
		String regex = "^0+(?!$)";
		// Replaces the matched value with given string
		code = code.replaceAll(regex, "");
		return Integer.parseInt(code);
	}

	private boolean isSuccessfullStardasCode(String code) {
		boolean ret = false;
		int codeValue = getIntFromCode(code);
		if (codeValue == 0 || (codeValue >= 2 && codeValue <= 98)) {
			ret = true;
		}
		return ret;
	}

	private boolean isEsitoProvvisorio(String code) {
		boolean ret = false;
		int codeValue = getIntFromCode(code);
		if (codeValue == 1) {
			ret = true;
		}
		return ret;
	}

	private boolean isDocumentoGiaClassificato(String code) {
		boolean ret = false;
		int codeValue = getIntFromCode(code);
		if (codeValue == 135) {
			ret = true;
		}
		return ret;
	}

}
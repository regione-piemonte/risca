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

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.risca.riscabesrv.business.be.ArchiviazioneApi;
import it.csi.risca.riscabesrv.business.be.BusinessLogic;
import it.csi.risca.riscabesrv.business.be.exception.ValidationException;
import it.csi.risca.riscabesrv.business.be.helper.pagopa.PagopaException;
import it.csi.risca.riscabesrv.business.be.helper.stardas.StardasServiceHelper;
import it.csi.risca.riscabesrv.business.be.impl.dao.AmbitiConfigDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.ArchiviazioneActaDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.InvioActaDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RegistroElaboraDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.SpedizioneActaDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.SpedizioneDAO;
import it.csi.risca.riscabesrv.dto.AmbitoConfigDTO;
import it.csi.risca.riscabesrv.dto.ArchResultDTO;
import it.csi.risca.riscabesrv.dto.ArchiviazioneActaDTO;
import it.csi.risca.riscabesrv.dto.ArchiviazioneData;
import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.dto.InvioActaDTO;
import it.csi.risca.riscabesrv.dto.RegistroElaboraDTO;
import it.csi.risca.riscabesrv.dto.SpedizioneActaDTO;
import it.csi.risca.riscabesrv.util.BollUtils;
import it.csi.risca.riscabesrv.util.Constants;
import it.csi.stardas.services.stardascommontypes.ConfigurazioneChiamanteType;
import it.csi.stardas.services.stardascommontypes.DatiSmistaDocumentoType;
import it.csi.stardas.services.stardascommontypes.DocumentoElettronicoType;
import it.csi.stardas.services.stardascommontypes.EmbeddedBinaryType;
import it.csi.stardas.services.stardascommontypes.MetadatiType;
import it.csi.stardas.services.stardascommontypes.MetadatoType;
import it.csi.stardas.services.stardascommontypes.ValoriType;
import it.csi.stardas.wso2.stardasservice.SmistaDocumentoRequestType;
import it.csi.stardas.wso2.stardasservice.SmistaDocumentoResponseType;

@Component
public class ArchiviazioneApiServiceImpl implements ArchiviazioneApi {

	private static final String IDENTITY = "identity";
	private static final String ATTORE = "riscabatcharc";
	private static final String COD_FASE_INVIO_DOCUMENTO_ACTA = "INVIO_DOCUMENTO_ACTA";

	private static final String PASSO_PDF_NOT_FOUND = "PDF_NOT_FOUND";
	private static final String PASSO_CALL_STARDAS = "CALL_STARDAS";
	private static final String PASSO_INS_INVIO_ACTA = "INS_INVIO_ACTA";

	private static final String RISCA_ACTA_codice_fiscale = "RISCA_ACTA.codice_fiscale";
	private static final String RISCA_STARDAS_cod_applicazione = "RISCA_STARDAS.cod_applicazione";
	private static final String RISCA_STARDAS_cod_fiscale_ente = "RISCA_STARDAS.cod_fiscale_ente";
	private static final String RISCA_STARDAS_cod_fruitore = "RISCA_STARDAS.cod_fruitore";
	private static final String RISCA_STARDAS_tipo_doc_bollette = "RISCA_STARDAS.tipo_doc_bollette";

	private static final String ARCHIVIAZIONE_STARDAS_limit = "ArchiviazioneStardas.limit";
	
	private static final String COD_TIPO_SOGGETTO_PF = "PF";

	public static final String DATE_FORMAT = "dd/MM/yyyy";

	@Autowired
	StardasServiceHelper stardasServiceHelper;

	private static Logger LOGGER = Logger.getLogger(Constants.LOGGER_NAME);

	@Autowired
	private InvioActaDAO invioActaDAO;

	@Autowired
	private SpedizioneActaDAO spedizioneActaDAO;
	
	@Autowired
	private SpedizioneDAO spedizioneDAO;

	@Autowired
	private ArchiviazioneActaDAO archiviazioneActaDAO;

	@Autowired
	private RegistroElaboraDAO registroElaboraDAO;
	
	@Autowired
	private AmbitiConfigDAO ambitiConfigDAO;

	@Autowired
	private BusinessLogic businessLogic;

	@Override
	public Response loadSpedizioniDaArchiviare(SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		List<SpedizioneActaDTO> spedDaArchiviare = spedizioneActaDAO.loadSpedizioniActaDaArchiviare();
		return Response.ok(spedDaArchiviare).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response updateElabSpedizioneActa(Long idSpedizioneActa, Long idElabora, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		SpedizioneActaDTO dto = spedizioneActaDAO.loadSpedizioneActaById(idSpedizioneActa);
		dto.setIdElabora(idElabora);
		dto = spedizioneActaDAO.updateSpedizioneActa(dto);
		return Response.ok(dto).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response loadDocumentiDaArchiviare(Long idSpedizione, String codTipoSpedizione,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			Long idAmbito = spedizioneDAO.loadSpedizioneByPk(idSpedizione + "", false).getIdAmbito();
			List<AmbitoConfigDTO> ambitoConfig = ambitiConfigDAO.loadAmbitiConfigByCodeAndKey(idAmbito + "",
					ARCHIVIAZIONE_STARDAS_limit);
			Long limit = null;
			if (ambitoConfig != null && ambitoConfig.size() > 0) {
				limit = Long.valueOf(ambitoConfig.get(0).getValore());
			}

			List<ArchiviazioneActaDTO> spedDaArchiviare = archiviazioneActaDAO
					.loadDocumentiActaDaArchiviareBySped(idSpedizione, codTipoSpedizione, limit);
			return Response.ok(spedDaArchiviare).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		} catch (Exception e) {
			LOGGER.error("loadDocumentiDaArchiviare:" + e.getMessage());
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
		}
	}

	@Override
	public Response archiviaDocumento(ArchiviazioneData archiviazioneData, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		ArchResultDTO result = new ArchResultDTO();

		ArchiviazioneActaDTO archActa = archiviazioneData.getArchiviazioneActa();
		Long idElabora = archiviazioneData.getIdElabora();

		String fileName;

		byte[] fileContent;
		try {
			File file = new File(archiviazioneData.getFilePath());
			fileName = file.getName();
			fileContent = FileUtils.readFileToByteArray(file);
		} catch (IOException e) {
			result.setStatus("KO");
			result.setErrorMessage(e.getMessage());
			result.setStepError(PASSO_PDF_NOT_FOUND);
			return Response.ok(result).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		}

		String codEsito;
		String desEsito;
		String uuidMessage;
		try {
			// Componiamo la request SmistaDocumento
			SmistaDocumentoRequestType smistaDocumentoRequest = makeSmistaDocumentoRequest(
					archiviazioneData.getConfigurazioniStardas(), archiviazioneData, fileName, fileContent);

			// Richiamare il servizio SmistaDocumento
			SmistaDocumentoResponseType response = smistadocumento(smistaDocumentoRequest);
			codEsito = response.getResult().getCodice();
			desEsito = response.getResult().getMessaggio();
			uuidMessage = response.getMessageUUID();
			if (codEsito.equals("000")) {
				//logStep(idElabora, PASSO_CALL_STARDAS, 0,
				//		"Archiviazione – Richiamo di STARDAS OK per il nap : " + archActa.getNap());
				result.setStatus("OK");
			} else {
				logStep(idElabora, PASSO_CALL_STARDAS, 1, "Archiviazione – Richiamo di STARDAS KO per il nap : "
						+ archActa.getNap() + " codEsito:" + codEsito);
				result.setStatus("KO");
				result.setErrorMessage(
						"Errore chiamata servizio SmistaDocumento - codEsito:" + codEsito + " - desEsito: " + desEsito);
				result.setStepError(PASSO_CALL_STARDAS);
			}
		} catch (PagopaException e) {
			result.setStatus("KO");
			result.setErrorMessage(e.getMessage());
			result.setStepError(PASSO_CALL_STARDAS);
			return Response.ok(result).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		}

		try {
			if (codEsito != null) {
				// Prima di inserire il record in RISCA_T_INVIO_ACTA cancello un eventuale record gia'
				// presente per nap o idAccertamento, se presente si tratta di un precedente invio e lo
				// cancello perche' deve esserci un solo record
				boolean isAccertamento = archActa.getCodTipoSpedizione().equals(BollUtils.COD_TIPO_SPEDIZIONE_ACCERTA);
				if (isAccertamento) {
					invioActaDAO.deleteInvioActaByAccertamento(archActa.getIdAccertamento());
				} else {
					invioActaDAO.deleteInvioActaByNap(archActa.getNap());
				}
				
				// Substring descrizione esito se troppo lungo per il nostro DB
				if (desEsito!= null && desEsito.length() > 500) {
					desEsito = desEsito.substring(0, 500);
				}

				// Inserire record in RISCA_T_INVIO_ACTA con esito della chiamata a Stardas
				InvioActaDTO invioActaDto = new InvioActaDTO();
				invioActaDto.setIdSpedizioneActa(archActa.getIdSpedizioneActa());
				invioActaDto.setNap(archActa.getNap());
				invioActaDto.setIdAccertamento(archActa.getIdAccertamento());
				invioActaDto.setNomeFile(archiviazioneData.getFilePath());
				invioActaDto.setFlgMulticlassificazione(archiviazioneData.getMulticlassificazione());
				invioActaDto.setFlgArchiviataActa(0);
				invioActaDto.setDataInvio(new Date());
				invioActaDto.setUuidMessage(uuidMessage);
				invioActaDto.setCodEsitoInvioActa(codEsito);
				invioActaDto.setDescEsitoInvioActa(desEsito);
				invioActaDto.setGestAttoreIns(ATTORE);
				invioActaDto.setGestAttoreUpd(ATTORE);

				// validare il DTO prima del insert
				businessLogic.validatorDTO(invioActaDto, null, null);

				invioActaDAO.saveInvioActa(invioActaDto);

				//logStep(idElabora, PASSO_INS_INVIO_ACTA, 0,
				//		"Archiviazione – Inserimento record in risca_t_invio_acta per il nap : " + archActa.getNap()
				//				+ " codEsito:" + codEsito);
			}
		} catch (ValidationException ve) {
			LOGGER.debug("BAD REQUEST: MESSAGE:" + ve.getMessage());
			Integer status = Response.Status.BAD_REQUEST.getStatusCode();
			ErrorDTO err = new ErrorDTO(status.toString(), Constants.E003, ve.getMessage(), null, null);
			return Response.status(Response.Status.BAD_REQUEST).entity(err).build();
		} catch (Exception e) {
			logStep(idElabora, PASSO_INS_INVIO_ACTA, 1,
					"Archiviazione – Fallito inserimento record in risca_t_invio_acta per il nap : " + archActa.getNap()
							+ " codEsito:" + codEsito);
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
		}
		return Response.ok(result).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	private SmistaDocumentoRequestType makeSmistaDocumentoRequest(Map<String, String> configurazioniStardas,
			ArchiviazioneData archiviazioneData, String fileName, byte[] fileContent) {
		ArchiviazioneActaDTO spedArch = archiviazioneData.getArchiviazioneActa();

		SmistaDocumentoRequestType smistaDocumentoRequest = new SmistaDocumentoRequestType();

		ConfigurazioneChiamanteType configChiamante = new ConfigurazioneChiamanteType();
		configChiamante.setCodiceApplicazione(configurazioniStardas.get(RISCA_STARDAS_cod_applicazione));
		configChiamante.setCodiceFiscaleEnte(configurazioniStardas.get(RISCA_STARDAS_cod_fiscale_ente));
		configChiamante.setCodiceFruitore(configurazioniStardas.get(RISCA_STARDAS_cod_fruitore));
		configChiamante.setCodiceTipoDocumento(configurazioniStardas.get(RISCA_STARDAS_tipo_doc_bollette));

		smistaDocumentoRequest.setConfigurazioneChiamante(configChiamante);

		DatiSmistaDocumentoType datiSmistaDoc = new DatiSmistaDocumentoType();
		datiSmistaDoc.setResponsabileTrattamento(configurazioniStardas.get(RISCA_ACTA_codice_fiscale));

		if (spedArch.getCodTipoSpedizione().equals(BollUtils.COD_TIPO_SPEDIZIONE_ACCERTA)) {
			datiSmistaDoc.setIdDocumentoFruitore(spedArch.getNap() + "_" + spedArch.getIdAccertamento());
		} else {
			datiSmistaDoc.setIdDocumentoFruitore(spedArch.getNap());
		}
		datiSmistaDoc.setNumAllegati(0);

		DocumentoElettronicoType docElett = new DocumentoElettronicoType();
		docElett.setNomeFile(fileName);
		docElett.setDocumentoFirmato(false);
		docElett.setMimeType("application/pdf");

		EmbeddedBinaryType embBinary = new EmbeddedBinaryType();
		embBinary.setContent(fileContent);

		docElett.setContenutoBinario(embBinary);

		datiSmistaDoc.setDocumentoElettronico(docElett);

		MetadatiType metadati = new MetadatiType();

		MetadatoType metadato = new MetadatoType();
		metadato.setNome("CODICE_FASCICOLO");
		if (archiviazioneData.getListaCodiciUtenza().size() > 1) {
			metadato.setValori(getListaValoriCodiciUtenza(archiviazioneData));
		} else {
			metadato.setValore(archiviazioneData.getListaCodiciUtenza().get(0));
		}
		metadati.getMetadato().add(metadato);

		metadato = new MetadatoType();
		metadato.setNome("OGGETTO_FASCICOLO");

		if (archiviazioneData.getListaCodiciUtenza().size() > 1) {
			metadato.setValori(getListaValoriCodiciUtenza(archiviazioneData));
		} else {
			metadato.setValore(archiviazioneData.getListaCodiciUtenza().get(0));
		}
		metadati.getMetadato().add(metadato);

		String soggetto = spedArch.getCodTipoSoggetto().equalsIgnoreCase(COD_TIPO_SOGGETTO_PF)
				? spedArch.getCognome() + " " + spedArch.getNome()
				: spedArch.getDenSoggetto();
		metadato = new MetadatoType();
		metadato.setNome("SOGGETTO_FASCICOLO");
		if (archiviazioneData.getListaCodiciUtenza().size() > 1) {
			metadato.setValori(getListaValoriSoggetto(archiviazioneData.getListaCodiciUtenza().size(), soggetto));
		} else {
			metadato.setValore(soggetto);
		}
		metadati.getMetadato().add(metadato);

		metadato = new MetadatoType();
		metadato.setNome("OGGETTO_DOCUMENTO");
		metadato.setValore(getOggettoDocumento(spedArch));
		metadati.getMetadato().add(metadato);

		metadato = new MetadatoType();
		metadato.setNome("PAROLE_CHIAVE_DOCUMENTO");
		if (spedArch.getCodTipoSpedizione().equals(BollUtils.COD_TIPO_SPEDIZIONE_ACCERTA)) {
			metadato.setValore(spedArch.getNap() + "_" + spedArch.getIdAccertamento() + "/"
					+ archiviazioneData.getListaCodiciUtenza().get(0));
		} else {
			metadato.setValore(spedArch.getNap() + "/" + archiviazioneData.getListaCodiciUtenza().get(0));
		}
		metadati.getMetadato().add(metadato);

		metadato = new MetadatoType();
		metadato.setNome("AUTORE_FISICO_DOCUMENTO");
		metadato.setValore(spedArch.getNomeDirigenteProtempore());
		metadati.getMetadato().add(metadato);

		if (spedArch.getCodTipoSoggetto().equalsIgnoreCase(COD_TIPO_SOGGETTO_PF)) {
			String destFisico = spedArch.getCognome() + " " + spedArch.getNome() + "|" + spedArch.getCfSoggetto();
			metadato = new MetadatoType();
			metadato.setNome("DESTINATARIO_FISICO_DOCUMENTO");
			metadato.setValore(destFisico);
			metadati.getMetadato().add(metadato);
		} else {
			String destGiuridico = spedArch.getDenSoggetto();
			metadato = new MetadatoType();
			metadato.setNome("DESTINATARIO_GIURIDICO_DOCUMENTO");
			metadato.setValore(destGiuridico);
			metadati.getMetadato().add(metadato);
		}

		SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
		metadato = new MetadatoType();
		metadato.setNome("DATA_CRONICA_DOCUMENTO");
		metadato.setValore(df.format(spedArch.getDataProtocollo()));
		metadati.getMetadato().add(metadato);

		String numRepAP = "";
		if (spedArch.getCodTipoSpedizione().equals(BollUtils.COD_TIPO_SPEDIZIONE_ACCERTA)) {
			numRepAP = fileName.split("\\.")[0].split("_")[0];
		} else {
			numRepAP = fileName.split("\\.")[0].split("_")[3];
		}
		metadato = new MetadatoType();
		metadato.setNome("NUMERO_REPERTORIO_DOCUMENTO");
		metadato.setValore(numRepAP);
		metadati.getMetadato().add(metadato);

		datiSmistaDoc.setMetadati(metadati);

		if (archiviazioneData.getListaCodiciUtenza().size() > 1) {
			datiSmistaDoc.setNumCopieMulticlassificazione(archiviazioneData.getListaCodiciUtenza().size() - 1);
		}

		smistaDocumentoRequest.setDatiSmistaDocumento(datiSmistaDoc);

		return smistaDocumentoRequest;
	}

	private String getOggettoDocumento(ArchiviazioneActaDTO spedArch) {
		String oggettoDoc = "";
		String dataScad = "";
		if (spedArch.getDataScadenza() != null) {
			dataScad = new SimpleDateFormat(DATE_FORMAT).format(spedArch.getDataScadenza());
		}
		switch (spedArch.getCodTipoSpedizione()) {
		case BollUtils.COD_TIPO_SPEDIZIONE_ORDINARIA:
			oggettoDoc = "AVVISO DI PAGAMENTO " + spedArch.getAnno();
			break;
		case BollUtils.COD_TIPO_SPEDIZIONE_SPECIALE:
		case BollUtils.COD_TIPO_SPEDIZIONE_GRANDEIDRO:
			oggettoDoc = "RICHIESTA DI PAGAMENTO - scadenza il " + dataScad;
			break;
		case BollUtils.COD_TIPO_SPEDIZIONE_ACCERTA:
			if (spedArch.getCodTipoAccertamento().equalsIgnoreCase(BollUtils.COD_TIPO_ACCERTAMENTO_AVB)) {
				oggettoDoc = "AVVISO BONARIO - scadenza il " + dataScad;
			} else if (spedArch.getCodTipoAccertamento().equalsIgnoreCase(BollUtils.COD_TIPO_ACCERTAMENTO_SOP)) {
				oggettoDoc = "SOLLECITO DI PAGAMENTO - scadenza il " + dataScad;
			}
			break;
		default:
			break;
		}
		return oggettoDoc;
	}

	private ValoriType getListaValoriCodiciUtenza(ArchiviazioneData archiviazioneData) {
		ValoriType valori = new ValoriType();
		for (String codiceUtenza : archiviazioneData.getListaCodiciUtenza()) {
			valori.getValore().add(codiceUtenza);
		}
		return valori;
	}

	private ValoriType getListaValoriSoggetto(int size, String soggetto) {
		ValoriType valori = new ValoriType();
		for (int i = 0; i < size; i++) {
			valori.getValore().add(soggetto);
		}
		return valori;
	}

	private SmistaDocumentoResponseType smistadocumento(SmistaDocumentoRequestType smistaDocumentoRequest)
			throws PagopaException {
		LOGGER.debug("[RichiestaIUVApiServiceImpl::richiestaIuv] BEGIN");
		SmistaDocumentoResponseType response = null;
		try {
			response = stardasServiceHelper.getSoapClient().smistaDocumento(smistaDocumentoRequest);

			if (response != null && response.getResult() != null) {
				LOGGER.debug("[ArchiviazioneApiServiceImpl::smistadocumento] Response - codice: "
						+ response.getResult().getCodice());
				LOGGER.debug("[ArchiviazioneApiServiceImpl::smistadocumento] Response - messaggio: "
						+ response.getResult().getMessaggio());
			}
			return response;
		} catch (Exception e) {
			LOGGER.error("[ArchiviazioneApiServiceImpl::smistadocumento] chiamata SmistaDocumento (Exception) ", e);
			throw new PagopaException("Errore nella chiamata SmistaDocumento (Exception)" + e.getMessage());
		}
	}

	private void logStep(Long idElabora, String codPasso, int esito, String note) {
		RegistroElaboraDTO registroElabora = new RegistroElaboraDTO();
		registroElabora.setIdElabora(idElabora);
		registroElabora.setCodPassoElabora(codPasso);
		registroElabora.setFlgEsitoElabora(esito);
		registroElabora.setNotaElabora(note);
		registroElabora.setGestAttoreIns(ATTORE);
		registroElabora.setGestAttoreUpd(ATTORE);
		registroElabora.setCodFaseElabora(COD_FASE_INVIO_DOCUMENTO_ACTA);
		try {
			registroElaboraDAO.saveRegistroElabora(registroElabora);
		} catch (Exception e) {
			LOGGER.error("[ArchiviazioneApiServiceImpl::logStep] (Exception) ", e);
		}
		LOGGER.debug(note);
	}
}

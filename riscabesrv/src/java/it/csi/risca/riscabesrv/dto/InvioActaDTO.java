/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.dto;

import java.util.Date;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * InvioActaDTO
 *
 * @author CSI PIEMONTE
 */
public class InvioActaDTO extends GestAttoreDTO {

    @Min(value = 1, message = "L' id_invio_acta deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_invio_acta supera il limite massimo consentito per Integer")
	@JsonProperty("id_invio_acta")
	private Long idInvioActa;

    @Size(max = 20, min = 0, message = "nap deve essere compreso tra 0 e 20 caratteri.")
	@JsonProperty("nap")
	private String nap;

    @Min(value = 1, message = "L' id_spedizione_acta deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_spedizione_acta supera il limite massimo consentito per Integer")
	@JsonProperty("id_spedizione_acta")
	private Long idSpedizioneActa;
    
    @Min(value = 1, message = "L' id_accertamento deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_accertamento supera il limite massimo consentito per Integer")
	@JsonProperty("id_accertamento")
	private Long idAccertamento;
    
    @Size(max = 150, min = 0, message = "nome_file deve essere compreso tra 0 e 150 caratteri.")
	@JsonProperty("nome_file")
	private String nomeFile;

    @Min(value = 0, message = "Il flg_multiclassificazione deve essere almeno 0.")
    @Max(value = 1, message = "Il flg_multiclassificazione deve essere al massimo 1.")
	@JsonProperty("flg_multiclassificazione")
	private int flgMulticlassificazione;

    @Min(value = 0, message = "Il flg_archiviata_acta deve essere almeno 0.")
    @Max(value = 1, message = "Il flg_archiviata_acta deve essere al massimo 1.")
	@JsonProperty("flg_archiviata_acta")
	private int flgArchiviataActa;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonProperty("data_invio")
	private Date dataInvio;

    @Size(max = 45, min = 0, message = "La uuid_message deve essere compreso tra 0 e 45 caratteri.")
	@JsonProperty("uuid_message")
	private String uuidMessage;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonProperty("data_esito_acta")
	private Date dataEsitoActa;

    @Size(max = 3, min = 0, message = "cod_esito_invio_acta deve essere compreso tra 0 e 3 caratteri.")
	@JsonProperty("cod_esito_invio_acta")
	private String codEsitoInvioActa;

    @Size(max = 500, min = 0, message = "desc_esito_invio_acta deve essere compreso tra 0 e 500 caratteri.")
	@JsonProperty("desc_esito_invio_acta")
	private String descEsitoInvioActa;

    @Size(max = 3, min = 0, message = "cod_esito_acquisizione_acta deve essere compreso tra 0 e 3 caratteri.")
	@JsonProperty("cod_esito_acquisizione_acta")
	private String codEsitoAcquisizioneActa;

    @Size(max = 500, min = 0, message = "desc_esito_acquisizione_acta deve essere compreso tra 0 e 500 caratteri.")
	@JsonProperty("desc_esito_acquisizione_acta")
	private String descEsitoAcquisizioneActa;


	public Long getIdInvioActa() {
		return idInvioActa;
	}

	public void setIdInvioActa(Long idInvioActa) {
		this.idInvioActa = idInvioActa;
	}

	public String getNap() {
		return nap;
	}

	public void setNap(String nap) {
		this.nap = nap;
	}

	public Long getIdSpedizioneActa() {
		return idSpedizioneActa;
	}

	public Long getIdAccertamento() {
		return idAccertamento;
	}

	public void setIdAccertamento(Long idAccertamento) {
		this.idAccertamento = idAccertamento;
	}

	public void setIdSpedizioneActa(Long idSpedizioneActa) {
		this.idSpedizioneActa = idSpedizioneActa;
	}

	public String getNomeFile() {
		return nomeFile;
	}

	public void setNomeFile(String nomeFile) {
		this.nomeFile = nomeFile;
	}

	public int getFlgMulticlassificazione() {
		return flgMulticlassificazione;
	}

	public void setFlgMulticlassificazione(int flgMulticlassificazione) {
		this.flgMulticlassificazione = flgMulticlassificazione;
	}

	public int getFlgArchiviataActa() {
		return flgArchiviataActa;
	}

	public void setFlgArchiviataActa(int flgArchiviataActa) {
		this.flgArchiviataActa = flgArchiviataActa;
	}

	public Date getDataInvio() {
		return dataInvio;
	}

	public void setDataInvio(Date dataInvio) {
		this.dataInvio = dataInvio;
	}

	public String getUuidMessage() {
		return uuidMessage;
	}

	public void setUuidMessage(String uuidMessage) {
		this.uuidMessage = uuidMessage;
	}

	public Date getDataEsitoActa() {
		return dataEsitoActa;
	}

	public void setDataEsitoActa(Date dataEsitoActa) {
		this.dataEsitoActa = dataEsitoActa;
	}

	public String getCodEsitoInvioActa() {
		return codEsitoInvioActa;
	}

	public void setCodEsitoInvioActa(String codEsitoInvioActa) {
		this.codEsitoInvioActa = codEsitoInvioActa;
	}

	public String getDescEsitoInvioActa() {
		return descEsitoInvioActa;
	}

	public void setDescEsitoInvioActa(String descEsitoInvioActa) {
		this.descEsitoInvioActa = descEsitoInvioActa;
	}

	public String getCodEsitoAcquisizioneActa() {
		return codEsitoAcquisizioneActa;
	}

	public void setCodEsitoAcquisizioneActa(String codEsitoAcquisizioneActa) {
		this.codEsitoAcquisizioneActa = codEsitoAcquisizioneActa;
	}

	public String getDescEsitoAcquisizioneActa() {
		return descEsitoAcquisizioneActa;
	}

	public void setDescEsitoAcquisizioneActa(String descEsitoAcquisizioneActa) {
		this.descEsitoAcquisizioneActa = descEsitoAcquisizioneActa;
	}

}

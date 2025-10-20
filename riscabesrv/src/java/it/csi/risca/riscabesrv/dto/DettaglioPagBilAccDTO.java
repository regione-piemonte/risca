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

import java.math.BigDecimal;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DettaglioPagDTO
 *
 * @author CSI PIEMONTE
 */

public class DettaglioPagBilAccDTO extends GestAttoreDTO {

	@Min(value = 1, message = "L' id_dettaglio_pag_bil_acc deve essere maggiore di 0")
	@Max(value = Integer.MAX_VALUE, message = "L' id_dettaglio_pag_bil_acc supera il limite massimo consentito per Integer")
	@JsonProperty("id_dettaglio_pag_bil_acc")
	private Long idDettaglioPagBilAcc;

	@Min(value = 1, message = "L' id_dettaglio_pag deve essere maggiore di 0")
	@Max(value = Integer.MAX_VALUE, message = "L' id_dettaglio_pag supera il limite massimo consentito per Integer")
	@JsonProperty("id_dettaglio_pag")
	private Long idDettaglioPag;

	@Min(value = 1, message = "L' id_bil_acc deve essere maggiore di 0")
	@Max(value = Integer.MAX_VALUE, message = "L' id_bil_acc supera il limite massimo consentito per Integer")
	@JsonProperty("id_bil_acc")
	private Long idBilAcc;

	@Digits(integer = 13, fraction = 2, message = "importo_accerta_bilancio deve avere al massimo 13 cifre intere e 2 decimali")
	@JsonProperty("importo_accerta_bilancio")
	private BigDecimal importoAccertaBilancio;

	@Min(value = 0, message = "Il flg_eccedenza deve essere almeno 0.")
	@Max(value = 1, message = "Il flg_eccedenza deve essere al massimo 1.")
	@JsonProperty("flg_eccedenza")
	private int flgEccedenza;

	@Min(value = 0, message = "Il flg_ruolo deve essere almeno 0.")
	@Max(value = 1, message = "Il flg_ruolo deve essere al massimo 1.")
	@JsonProperty("flg_ruolo")
	private int flgRuolo;

	@Min(value = 0, message = "Il flg_pubblico deve essere almeno 0.")
	@Max(value = 1, message = "Il flg_pubblico deve essere al massimo 1.")
	@JsonProperty("flg_pubblico")
	private int flgPubblico;

	public Long getIdDettaglioPagBilAcc() {
		return idDettaglioPagBilAcc;
	}

	public void setIdDettaglioPagBilAcc(Long idDettaglioPagBilAcc) {
		this.idDettaglioPagBilAcc = idDettaglioPagBilAcc;
	}

	public Long getIdDettaglioPag() {
		return idDettaglioPag;
	}

	public void setIdDettaglioPag(Long idDettaglioPag) {
		this.idDettaglioPag = idDettaglioPag;
	}

	public Long getIdBilAcc() {
		return idBilAcc;
	}

	public void setIdBilAcc(Long idBilAcc) {
		this.idBilAcc = idBilAcc;
	}

	public BigDecimal getImportoAccertaBilancio() {
		return importoAccertaBilancio;
	}

	public void setImportoAccertaBilancio(BigDecimal importoAccertaBilancio) {
		this.importoAccertaBilancio = importoAccertaBilancio;
	}

	public int getFlgEccedenza() {
		return flgEccedenza;
	}

	public void setFlgEccedenza(int flgEccedenza) {
		this.flgEccedenza = flgEccedenza;
	}

	public int getFlgRuolo() {
		return flgRuolo;
	}

	public void setFlgRuolo(int flgRuolo) {
		this.flgRuolo = flgRuolo;
	}

	public int getFlgPubblico() {
		return flgPubblico;
	}

	public void setFlgPubblico(int flgPubblico) {
		this.flgPubblico = flgPubblico;
	}

}

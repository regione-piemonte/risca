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
import java.util.Date;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RimborsoDTO extends GestAttoreDTO {

    @Min(value = 1, message = "L' id_rimborso deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_rimborso supera il limite massimo consentito per Integer")
	@JsonProperty("id_rimborso")
	private Long idRimborso;

    @Min(value = 1, message = "L' id_stato_debitorio deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_stato_debitorio supera il limite massimo consentito per Integer")
	@JsonProperty("id_stato_debitorio")
	private Long idStatoDebitorio;

    @Min(value = 1, message = "L' id_tipo_rimborso deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_tipo_rimborso supera il limite massimo consentito per Integer")
	@JsonProperty("id_tipo_rimborso")
	private Long idTipoRimborso;

    @Min(value = 1, message = "L' id_soggetto deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_soggetto supera il limite massimo consentito per Integer")
	@JsonProperty("id_soggetto")
	private Long idSoggetto;

    @Min(value = 1, message = "L' id_gruppo_soggetto deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_gruppo_soggetto supera il limite massimo consentito per Integer")
	@JsonProperty("id_gruppo_soggetto")
	private Long idGruppoSoggetto;

    @Digits(integer = 9, fraction = 2, message = "I'imp_rimborso deve avere al massimo 9 cifre intere e 2 decimali")
	@JsonProperty("imp_rimborso")
	private BigDecimal impRimborso;

    @Size(max = 300, min = 0, message = "causale deve essere compreso tra 0 e 300 caratteri.")
	@JsonProperty("causale")
	private String causale;

    @Size(max = 30, min = 0, message = "num_determina deve essere compreso tra 0 e 30 caratteri.")
	@JsonProperty("num_determina")
	private String numDetermina;

	@JsonProperty("data_determina")
	private Date dataDetermina;

    @Digits(integer = 9, fraction = 2, message = "I'imp_restituito deve avere al massimo 9 cifre intere e 2 decimali")
	@JsonProperty("imp_restituito")
	private BigDecimal impRestituito;

	public Long getIdRimborso() {
		return idRimborso;
	}

	public void setIdRimborso(Long idRimborso) {
		this.idRimborso = idRimborso;
	}

	public Long getIdStatoDebitorio() {
		return idStatoDebitorio;
	}

	public void setIdStatoDebitorio(Long idStatoDebitorio) {
		this.idStatoDebitorio = idStatoDebitorio;
	}

	public Long getIdTipoRimborso() {
		return idTipoRimborso;
	}

	public void setIdTipoRimborso(Long idTipoRimborso) {
		this.idTipoRimborso = idTipoRimborso;
	}

	public Long getIdSoggetto() {
		return idSoggetto;
	}

	public void setIdSoggetto(Long idSoggetto) {
		this.idSoggetto = idSoggetto;
	}

	public BigDecimal getImpRimborso() {
		return impRimborso;
	}

	public void setImpRimborso(BigDecimal impRimborso) {
		this.impRimborso = impRimborso;
	}

	public String getCausale() {
		return causale;
	}

	public void setCausale(String causale) {
		this.causale = causale;
	}

	public String getNumDetermina() {
		return numDetermina;
	}

	public void setNumDetermina(String numDetermina) {
		this.numDetermina = numDetermina;
	}

	public Date getDataDetermina() {
		return dataDetermina;
	}

	public void setDataDetermina(Date dataDetermina) {
		this.dataDetermina = dataDetermina;
	}

	public BigDecimal getImpRestituito() {
		return impRestituito;
	}

	public void setImpRestituito(BigDecimal impRestituito) {
		this.impRestituito = impRestituito;
	}

	

	public Long getIdGruppoSoggetto() {
		return idGruppoSoggetto;
	}

	public void setIdGruppoSoggetto(Long idGruppoSoggetto) {
		this.idGruppoSoggetto = idGruppoSoggetto;
	}

	@Override
	public String toString() {
		return "RimborsoDTO [idRimborso=" + idRimborso + ", idStatoDebitorio=" + idStatoDebitorio + ", idTipoRimborso="
				+ idTipoRimborso + ", idSoggetto=" + idSoggetto + ", idGruppoSoggetto=" + idGruppoSoggetto
				+ ", impRimborso=" + impRimborso + ", causale=" + causale + ", numDetermina=" + numDetermina
				+ ", dataDetermina=" + dataDetermina + ", impRestituito=" + impRestituito + ", getGestAttoreIns()="
				+ getGestAttoreIns() + ", getGestDataIns()=" + getGestDataIns() + ", getGestAttoreUpd()="
				+ getGestAttoreUpd() + ", getGestDataUpd()=" + getGestDataUpd() + ", getGestUid()=" + getGestUid()
				+ "]";
	}
	



}

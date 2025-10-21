/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RimborsoDTO {

	@JsonProperty("id_rimborso")
	private Long idRimborso;

	@JsonProperty("id_stato_debitorio")
	private Long idStatoDebitorio;

	@JsonProperty("id_tipo_rimborso")
	private Long idTipoRimborso;

	@JsonProperty("id_soggetto")
	private Long idSoggetto;
	
	@JsonProperty("id_gruppo_soggetto")
	private Long idGruppoSoggetto;
	
	@JsonProperty("imp_rimborso")
	private BigDecimal impRimborso;

	@JsonProperty("causale")
	private String causale;

	@JsonProperty("num_determina")
	private String numDetermina;

	@JsonProperty("data_determina")
	private Date dataDetermina;

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

	@Override
	public String toString() {
		return "RimborsoDTO [idRimborso=" + idRimborso + ", idStatoDebitorio=" + idStatoDebitorio + ", idTipoRimborso="
				+ idTipoRimborso + ", idSoggetto=" + idSoggetto + ", idGruppoSoggetto=" + idGruppoSoggetto
				+ ", impRimborso=" + impRimborso + ", causale=" + causale + ", numDetermina=" + numDetermina
				+ ", dataDetermina=" + dataDetermina + ", impRestituito=" + impRestituito + "]";
	}

}

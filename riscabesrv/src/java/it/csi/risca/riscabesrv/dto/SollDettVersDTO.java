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

import com.fasterxml.jackson.annotation.JsonProperty;

public class SollDettVersDTO {

	@JsonProperty("id_soll_dett_vers")
	private Long idSollDettVers;

	@JsonProperty("id_accertamento")
	private Long idAccertamento;

	@JsonProperty("codice_utenza")
	private String codiceUtenza;

	@JsonProperty("scadenza_pagamento")
	private String scadenzaPagamento;

	@JsonProperty("importo_versato")
	private BigDecimal importoVersato;

	@JsonProperty("data_versamento")
	private Date dataVersamento;

	@JsonProperty("interessi_maturati")
	private BigDecimal interessiMaturati;

	@JsonProperty("giorni_ritardo")
	private Integer giorniRitardo;

	public Long getIdSollDettVers() {
		return idSollDettVers;
	}

	public void setIdSollDettVers(Long idSollDettVers) {
		this.idSollDettVers = idSollDettVers;
	}

	public Long getIdAccertamento() {
		return idAccertamento;
	}

	public void setIdAccertamento(Long idAccertamento) {
		this.idAccertamento = idAccertamento;
	}

	public String getCodiceUtenza() {
		return codiceUtenza;
	}

	public void setCodiceUtenza(String codiceUtenza) {
		this.codiceUtenza = codiceUtenza;
	}

	public String getScadenzaPagamento() {
		return scadenzaPagamento;
	}

	public void setScadenzaPagamento(String scadenzaPagamento) {
		this.scadenzaPagamento = scadenzaPagamento;
	}

	public BigDecimal getImportoVersato() {
		return importoVersato;
	}

	public void setImportoVersato(BigDecimal importoVersato) {
		this.importoVersato = importoVersato;
	}

	public Date getDataVersamento() {
		return dataVersamento;
	}

	public void setDataVersamento(Date dataVersamento) {
		this.dataVersamento = dataVersamento;
	}

	public BigDecimal getInteressiMaturati() {
		return interessiMaturati;
	}

	public void setInteressiMaturati(BigDecimal interessiMaturati) {
		this.interessiMaturati = interessiMaturati;
	}

	public Integer getGiorniRitardo() {
		return giorniRitardo;
	}

	public void setGiorniRitardo(Integer giorniRitardo) {
		this.giorniRitardo = giorniRitardo;
	}

}

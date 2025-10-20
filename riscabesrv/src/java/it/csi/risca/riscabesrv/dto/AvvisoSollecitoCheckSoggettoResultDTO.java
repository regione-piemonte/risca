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

import com.fasterxml.jackson.annotation.JsonProperty;

public class AvvisoSollecitoCheckSoggettoResultDTO {

	@JsonProperty("id_soggetto")
	private Long idSoggetto;

	@JsonProperty("id_stato_debitorio")
	private Long idStatoDebitorio;

	@JsonProperty("sd_nap")
	private String sdNap;

	@JsonProperty("sd_id_soggetto_titolare")
	private Long sdIdSoggettoTitolare;

	@JsonProperty("prat_id_soggetto_titolare")
	private Long pratIdSoggettoTitolare;

	@JsonProperty("sogg_cf")
	private String soggCf;

	public Long getIdSoggetto() {
		return idSoggetto;
	}

	public void setIdSoggetto(Long idSoggetto) {
		this.idSoggetto = idSoggetto;
	}

	public Long getIdStatoDebitorio() {
		return idStatoDebitorio;
	}

	public void setIdStatoDebitorio(Long idStatoDebitorio) {
		this.idStatoDebitorio = idStatoDebitorio;
	}

	public String getSdNap() {
		return sdNap;
	}

	public void setSdNap(String sdNap) {
		this.sdNap = sdNap;
	}

	public Long getSdIdSoggettoTitolare() {
		return sdIdSoggettoTitolare;
	}

	public void setSdIdSoggettoTitolare(Long sdIdSoggettoTitolare) {
		this.sdIdSoggettoTitolare = sdIdSoggettoTitolare;
	}

	public Long getPratIdSoggettoTitolare() {
		return pratIdSoggettoTitolare;
	}

	public void setPratIdSoggettoTitolare(Long pratIdSoggettoTitolare) {
		this.pratIdSoggettoTitolare = pratIdSoggettoTitolare;
	}

	public String getSoggCf() {
		return soggCf;
	}

	public void setSoggCf(String soggCf) {
		this.soggCf = soggCf;
	}

}

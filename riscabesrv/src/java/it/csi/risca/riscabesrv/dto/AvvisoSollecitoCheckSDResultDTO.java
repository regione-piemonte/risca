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

public class AvvisoSollecitoCheckSDResultDTO {

	@JsonProperty("soggetto_titolare")
	private Long soggettoTitolare;

	@JsonProperty("sd_nap_valorizzato")
	private String sdNapValorizzato;

	@JsonProperty("sd_nap_nullo")
	private String sdNapNullo;

	@JsonProperty("codici_avviso_attivi")
	private String codiciAvvisoAttivi;

	@JsonProperty("codici_avviso_non_attivi")
	private String codiciAvvisoNonAttivi;

	public Long getSoggettoTitolare() {
		return soggettoTitolare;
	}

	public void setSoggettoTitolare(Long soggettoTitolare) {
		this.soggettoTitolare = soggettoTitolare;
	}

	public String getSdNapValorizzato() {
		return sdNapValorizzato;
	}

	public void setSdNapValorizzato(String sdNapValorizzato) {
		this.sdNapValorizzato = sdNapValorizzato;
	}

	public String getSdNapNullo() {
		return sdNapNullo;
	}

	public void setSdNapNullo(String sdNapNullo) {
		this.sdNapNullo = sdNapNullo;
	}

	public String getCodiciAvvisoAttivi() {
		return codiciAvvisoAttivi;
	}

	public void setCodiciAvvisoAttivi(String codiciAvvisoAttivi) {
		this.codiciAvvisoAttivi = codiciAvvisoAttivi;
	}

	public String getCodiciAvvisoNonAttivi() {
		return codiciAvvisoNonAttivi;
	}

	public void setCodiciAvvisoNonAttivi(String codiciAvvisoNonAttivi) {
		this.codiciAvvisoNonAttivi = codiciAvvisoNonAttivi;
	}

}

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

public class BollOutputDatiDTO {

	@JsonProperty("idElabora")
	private Long idElabora;

	@JsonProperty("dtProtocollo")
	private String dtProtocollo;

	@JsonProperty("anno")
	private String anno;

	@JsonProperty("progrXlsDatiTitolare")
	int progrXlsDatiTitolare = 0;
	@JsonProperty("progrXlsDatiAmmin")
	int progrXlsDatiAmmin = 0;
	@JsonProperty("progrXlsDatiUsi")
	int progrXlsDatiUsi = 0;
	@JsonProperty("progrXlsDatiAnnualita")
	int progrXlsDatiAnnualita = 0;
	@JsonProperty("progrTxtCompleto")
	int progrTxtCompleto = 0;
	@JsonProperty("progrTxtParzCarta")
	int progrTxtParzCarta = 0;

	AvvisoDatiTitolareDTO avvisoDatiTitolare;

	public Long getIdElabora() {
		return idElabora;
	}

	public void setIdElabora(Long idElabora) {
		this.idElabora = idElabora;
	}

	public String getDtProtocollo() {
		return dtProtocollo;
	}

	public void setDtProtocollo(String dtProtocollo) {
		this.dtProtocollo = dtProtocollo;
	}

	public String getAnno() {
		return anno;
	}

	public void setAnno(String anno) {
		this.anno = anno;
	}

	public int getProgrXlsDatiTitolare() {
		return progrXlsDatiTitolare;
	}

	public void setProgrXlsDatiTitolare(int progrXlsDatiTitolare) {
		this.progrXlsDatiTitolare = progrXlsDatiTitolare;
	}

	public int getProgrXlsDatiAmmin() {
		return progrXlsDatiAmmin;
	}

	public void setProgrXlsDatiAmmin(int progrXlsDatiAmmin) {
		this.progrXlsDatiAmmin = progrXlsDatiAmmin;
	}

	public int getProgrXlsDatiUsi() {
		return progrXlsDatiUsi;
	}

	public void setProgrXlsDatiUsi(int progrXlsDatiUsi) {
		this.progrXlsDatiUsi = progrXlsDatiUsi;
	}

	public int getProgrXlsDatiAnnualita() {
		return progrXlsDatiAnnualita;
	}

	public void setProgrXlsDatiAnnualita(int progrXlsDatiAnnualita) {
		this.progrXlsDatiAnnualita = progrXlsDatiAnnualita;
	}

	public int getProgrTxtCompleto() {
		return progrTxtCompleto;
	}

	public void setProgrTxtCompleto(int progrTxtCompleto) {
		this.progrTxtCompleto = progrTxtCompleto;
	}

	public int getProgrTxtParzCarta() {
		return progrTxtParzCarta;
	}

	public void setProgrTxtParzCarta(int progrTxtParzCarta) {
		this.progrTxtParzCarta = progrTxtParzCarta;
	}

	public AvvisoDatiTitolareDTO getAvvisoDatiTitolare() {
		return avvisoDatiTitolare;
	}

	public void setAvvisoDatiTitolare(AvvisoDatiTitolareDTO avvisoDatiTitolare) {
		this.avvisoDatiTitolare = avvisoDatiTitolare;
	}

}

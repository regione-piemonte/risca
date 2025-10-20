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

public class BollResultDTO {

	@JsonProperty("status")
	private String status;

	@JsonProperty("stepError")
	private String stepError;

	@JsonProperty("errorMessage")
	private String errorMessage;

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStepError() {
		return stepError;
	}

	public void setStepError(String stepError) {
		this.stepError = stepError;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
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

}

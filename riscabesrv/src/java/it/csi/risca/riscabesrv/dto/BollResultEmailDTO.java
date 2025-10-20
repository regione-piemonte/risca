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

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BollResultEmailDTO {

	@JsonProperty("status")
	private String status;

	@JsonProperty("stepError")
	private String stepError;

	@JsonProperty("errorMessage")
	private String errorMessage;

	@JsonProperty("datiTitolari")
	private List<OutputDatiDTO> datiTitolari;

	@JsonProperty("emailStandard")
	private EmailStandardDTO emailStandard;

	@JsonProperty("puntiValori")
	private List<EmailStPuntiValoriDTO> puntiValori;
	
	@JsonProperty("segnapostoPdf")
	private List<EmailStSegnapostoDTO> segnapostoPdf;

	@JsonProperty("segnapostoOggetto")
	private List<EmailStSegnapostoDTO> segnapostoOggetto;
	
	@JsonProperty("segnapostoCorpo")
	private List<EmailStSegnapostoDTO> segnapostoCorpo;

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

	public List<OutputDatiDTO> getDatiTitolari() {
		return datiTitolari;
	}

	public void setDatiTitolari(List<OutputDatiDTO> datiTitolari) {
		this.datiTitolari = datiTitolari;
	}

	public EmailStandardDTO getEmailStandard() {
		return emailStandard;
	}

	public void setEmailStandard(EmailStandardDTO emailStandard) {
		this.emailStandard = emailStandard;
	}

	public List<EmailStPuntiValoriDTO> getPuntiValori() {
		return puntiValori;
	}

	public void setPuntiValori(List<EmailStPuntiValoriDTO> puntiValori) {
		this.puntiValori = puntiValori;
	}

	public List<EmailStSegnapostoDTO> getSegnapostoPdf() {
		return segnapostoPdf;
	}

	public void setSegnapostoPdf(List<EmailStSegnapostoDTO> segnapostoPdf) {
		this.segnapostoPdf = segnapostoPdf;
	}

	public List<EmailStSegnapostoDTO> getSegnapostoOggetto() {
		return segnapostoOggetto;
	}

	public void setSegnapostoOggetto(List<EmailStSegnapostoDTO> segnapostoOggetto) {
		this.segnapostoOggetto = segnapostoOggetto;
	}

	public List<EmailStSegnapostoDTO> getSegnapostoCorpo() {
		return segnapostoCorpo;
	}

	public void setSegnapostoCorpo(List<EmailStSegnapostoDTO> segnapostoCorpo) {
		this.segnapostoCorpo = segnapostoCorpo;
	}

}

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
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BollResultIUVDTO {

	@JsonProperty("status")
	private String status;

	@JsonProperty("stepError")
	private String stepError;

	@JsonProperty("errorMessage")
	private String errorMessage;

	@JsonProperty("dataRichiestaIuv")
	private Date dataRichiestaIuv;

	@JsonProperty("nomiLotto")
	private List<String> nomiLotto;

	@JsonProperty("numPosizioniDebitorie")
	private List<String> numPosizioniDebitorie;

	@JsonProperty("recordPerLotto")
	private Integer recordPerLotto;

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

	public Date getDataRichiestaIuv() {
		return dataRichiestaIuv;
	}

	public void setDataRichiestaIuv(Date dataRichiestaIuv) {
		this.dataRichiestaIuv = dataRichiestaIuv;
	}

	public List<String> getNomiLotto() {
		return nomiLotto;
	}

	public void setNomiLotto(List<String> nomiLotto) {
		this.nomiLotto = nomiLotto;
	}

	public List<String> getNumPosizioniDebitorie() {
		return numPosizioniDebitorie;
	}

	public void setNumPosizioniDebitorie(List<String> numPosizioniDebitorie) {
		this.numPosizioniDebitorie = numPosizioniDebitorie;
	}

	public Integer getRecordPerLotto() {
		return recordPerLotto;
	}

	public void setRecordPerLotto(Integer recordPerLotto) {
		this.recordPerLotto = recordPerLotto;
	}

}

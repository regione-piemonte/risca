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

import com.fasterxml.jackson.annotation.JsonProperty;

public class BollResultCompDTO {

	@JsonProperty("status")
	private String status;

	@JsonProperty("stepError")
	private String stepError;

	@JsonProperty("errorMessage")
	private String errorMessage;

	@JsonProperty("idStatoDebitorio")
	private Long idStatoDebitorio;

	@JsonProperty("canoneDovutoAnnoPrec")
	private BigDecimal canoneDovutoAnnoPrec;

	@JsonProperty("sommCompensPerPrat")
	private BigDecimal sommCompensPerPrat;

	@JsonProperty("canoneAggiuntivoGrandeIdro")
	private BigDecimal canoneAggiuntivoGrandeIdro;

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

	public Long getIdStatoDebitorio() {
		return idStatoDebitorio;
	}

	public void setIdStatoDebitorio(Long idStatoDebitorio) {
		this.idStatoDebitorio = idStatoDebitorio;
	}

	public BigDecimal getCanoneDovutoAnnoPrec() {
		return canoneDovutoAnnoPrec;
	}

	public void setCanoneDovutoAnnoPrec(BigDecimal canoneDovutoAnnoPrec) {
		this.canoneDovutoAnnoPrec = canoneDovutoAnnoPrec;
	}

	public BigDecimal getSommCompensPerPrat() {
		return sommCompensPerPrat;
	}

	public void setSommCompensPerPrat(BigDecimal sommCompensPerPrat) {
		this.sommCompensPerPrat = sommCompensPerPrat;
	}

	public BigDecimal getCanoneAggiuntivoGrandeIdro() {
		return canoneAggiuntivoGrandeIdro;
	}

	public void setCanoneAggiuntivoGrandeIdro(BigDecimal canoneAggiuntivoGrandeIdro) {
		this.canoneAggiuntivoGrandeIdro = canoneAggiuntivoGrandeIdro;
	}

}

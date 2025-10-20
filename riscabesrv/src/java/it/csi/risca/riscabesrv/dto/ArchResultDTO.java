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

public class ArchResultDTO {

	@JsonProperty("status")
	private String status;

	@JsonProperty("stepError")
	private String stepError;

	@JsonProperty("errorMessage")
	private String errorMessage;

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

}

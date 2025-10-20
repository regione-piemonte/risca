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

import javax.validation.constraints.Digits;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonRangeDTO {

    @Digits(integer = 5, fraction = 4, message = "soglia_min deve avere al massimo 5 cifre intere e 4 decimali")
	@JsonProperty("soglia_min")
	private BigDecimal sogliaMin;

    @Digits(integer = 5, fraction = 4, message = "soglia_max deve avere al massimo 5 cifre intere e 4 decimali")
	@JsonProperty("soglia_max")
	private BigDecimal sogliaMax;

    @Digits(integer = 7, fraction = 2, message = "canone_minimo deve avere al massimo 7 cifre intere e 2 decimali")
	@JsonProperty("canone_minimo")
	private BigDecimal canoneMinimoRange;

	public BigDecimal getSogliaMin() {
		return sogliaMin;
	}

	public void setSogliaMin(BigDecimal sogliaMin) {
		this.sogliaMin = sogliaMin;
	}

	public BigDecimal getSogliaMax() {
		return sogliaMax;
	}

	public void setSogliaMax(BigDecimal sogliaMax) {
		this.sogliaMax = sogliaMax;
	}

	public BigDecimal getCanoneMinimoRange() {
		return canoneMinimoRange;
	}

	public void setCanoneMinimoRange(BigDecimal canoneMinimoRange) {
		this.canoneMinimoRange = canoneMinimoRange;
	}


}

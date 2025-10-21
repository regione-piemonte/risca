/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonRangeDTO {

	@JsonProperty("soglia_min")
	private BigDecimal sogliaMin;

	@JsonProperty("soglia_max")
	private BigDecimal sogliaMax;

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

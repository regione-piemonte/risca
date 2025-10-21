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

public class CalcoloCanoneDTO {
	
	@JsonProperty("calcolo_canone")
    private BigDecimal calcoloCanone;
	
	public BigDecimal getCalcoloCanone() {
		return calcoloCanone;
	}

	public void setCalcoloCanone(BigDecimal calcoloCanone) {
		this.calcoloCanone = calcoloCanone;
	}

}

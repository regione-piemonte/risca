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
/**
 * VerifyRiscossioneStatoDebDTO
 *
 * @author CSI PIEMONTE
 */

import com.fasterxml.jackson.annotation.JsonProperty;

public class VerifyRiscossioneStatoDebDTO {
	
	@JsonProperty("num_riscossioni")
    private Long  numRiscossioni;
	
	@JsonProperty("num_statdeb")
    private Long  numStatDeb;

	public Long getNumRiscossioni() {
		return numRiscossioni;
	}

	public void setNumRiscossioni(Long numRiscossioni) {
		this.numRiscossioni = numRiscossioni;
	}

	public Long getNumStatDeb() {
		return numStatDeb;
	}

	public void setNumStatDeb(Long numStatDeb) {
		this.numStatDeb = numStatDeb;
	}

}

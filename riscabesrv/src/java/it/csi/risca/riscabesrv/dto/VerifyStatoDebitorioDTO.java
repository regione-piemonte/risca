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

/**
 * VerifyRiscossioneDTO
 *
 * @author CSI PIEMONTE
 */
public class VerifyStatoDebitorioDTO {
	
	@JsonProperty("is_stato_debitorio_valid")
    private Boolean isStatoDebitorioValid;

	public Boolean getIsStatoDebitorioValid() {
		return isStatoDebitorioValid;
	}

	public void setIsStatoDebitorioValid(Boolean isStatoDebitorioValid) {
		this.isStatoDebitorioValid = isStatoDebitorioValid;
	}

}

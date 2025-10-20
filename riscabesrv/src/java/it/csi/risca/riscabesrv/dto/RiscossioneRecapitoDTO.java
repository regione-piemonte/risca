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

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * RiscossioneRecapitoDTO
 *
 * @author CSI PIEMONTE
 */
public class RiscossioneRecapitoDTO extends GestAttoreDTO {

    @Min(value = 1, message = "L' id_riscossione deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_riscossione supera il limite massimo consentito per Integer")
	@JsonProperty("id_riscossione")
    private Long idRiscossione;

    @Min(value = 1, message = "L' id_recapito deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_recapito supera il limite massimo consentito per Integer")
	@JsonProperty("id_recapito")
    private Long idRecapito;
    	
	
	
	public Long getIdRiscossione() {
		return idRiscossione;
	}

	public void setIdRiscossione(Long idRiscossione) {
		this.idRiscossione = idRiscossione;
	}

	public Long getIdRecapito() {
		return idRecapito;
	}

	public void setIdRecapito(Long idRecapito) {
		this.idRecapito = idRecapito;
	}


}

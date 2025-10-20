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

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Stato Contribuzione DTO
 *
 * @author CSI PIEMONTE
 */


public class StatoContribuzioneDTO {

    @Min(value = 1, message = "L' id_stato_contribuzione deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_stato_contribuzione supera il limite massimo consentito per Integer")
	@JsonProperty("id_stato_contribuzione")
    private Long idStatoContribuzione;
	
    @Size(max = 2, min = 0, message = "cod_stato_contribuzione deve essere compreso tra 0 e 2 caratteri. ")
    @JsonProperty("cod_stato_contribuzione")
    private String codStatoContribuzione;
    
    @Size(max = 50, min = 0, message = "des_stato_contribuzione deve essere compreso tra 0 e 50 caratteri. ")
    @JsonProperty("des_stato_contribuzione")
    private String desStatoContribuzione;

	public Long getIdStatoContribuzione() {
		return idStatoContribuzione;
	}

	public void setIdStatoContribuzione(Long idStatoContribuzione) {
		this.idStatoContribuzione = idStatoContribuzione;
	}

	public String getCodStatoContribuzione() {
		return codStatoContribuzione;
	}

	public void setCodStatoContribuzione(String codStatoContribuzione) {
		this.codStatoContribuzione = codStatoContribuzione;
	}

	public String getDesStatoContribuzione() {
		return desStatoContribuzione;
	}

	public void setDesStatoContribuzione(String desStatoContribuzione) {
		this.desStatoContribuzione = desStatoContribuzione;
	}
    
    
}

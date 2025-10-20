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
 * LockRiscossioneDTO
 *
 * @author CSI PIEMONTE
 */
public class LockRiscossioneDTO extends GestAttoreDTO {
	
    @Min(value = 1, message = "L' id_riscossione deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_riscossione supera il limite massimo consentito per Integer")
	@JsonProperty("id_riscossione")
    private Long idRiscossione;
    
    @Size(max = 100, min = 0, message = "utente_lock deve essere compreso tra 0 e 100 caratteri.")
    @JsonProperty("utente_lock")
    private String utenteLock;
    
    @Size(max = 16, min = 0, message = "cf_utente_lock deve essere compreso tra 0 e 16 caratteri.")
 	@JsonProperty("cf_utente_lock")
 	private String cfUtenteLock = null;
     

	public Long getIdRiscossione() {
		return idRiscossione;
	}

	public void setIdRiscossione(Long idRiscossione) {
		this.idRiscossione = idRiscossione;
	}

	public String getUtenteLock() {
		return utenteLock;
	}

	public void setUtenteLock(String utenteLock) {
		this.utenteLock = utenteLock;
	}

	public String getCfUtenteLock() {
		return cfUtenteLock;
	}

	public void setCfUtenteLock(String cfUtenteLock) {
		this.cfUtenteLock = cfUtenteLock;
	}
	
	
	
}

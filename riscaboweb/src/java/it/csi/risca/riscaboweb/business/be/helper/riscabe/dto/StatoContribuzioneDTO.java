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

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Stato Contribuzione DTO
 *
 * @author CSI PIEMONTE
 */


public class StatoContribuzioneDTO {

	@JsonProperty("id_stato_contribuzione")
    private Long idStatoContribuzione;
	
    @JsonProperty("cod_stato_contribuzione")
    private String codStatoContribuzione;
    
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

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
 * LocationJobDTO
 *
 * @author CSI PIEMONTE
 */
public class LocationJobDTO {


	@JsonProperty("location")
	private String location;
	
    @JsonProperty("id_risca")
    private Long idRisca;
    
	@JsonProperty("timestamp")
	private String timestamp;
    
    
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Long getIdRisca() {
		return idRisca;
	}

	public void setIdRisca(Long idRisca) {
		this.idRisca = idRisca;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
}

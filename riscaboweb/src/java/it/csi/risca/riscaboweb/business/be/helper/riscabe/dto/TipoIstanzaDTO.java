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

public class TipoIstanzaDTO {
	
	@JsonProperty("id_istanza")
	private Long idIstanza;
	
	@JsonProperty("data_istanza_da")
	private String dataIstanzaDa;

	@JsonProperty("data_istanza_a")
	private String dataIstanzaA;

	
	public Long getIdIstanza() {
		return idIstanza;
	}

	public void setIdIstanza(Long idIstanza) {
		this.idIstanza = idIstanza;
	}

	public String getDataIstanzaDa() {
		return dataIstanzaDa;
	}

	public void setDataIstanzaDa(String dataIstanzaDa) {
		this.dataIstanzaDa = dataIstanzaDa;
	}

	public String getDataIstanzaA() {
		return dataIstanzaA;
	}

	public void setDataIstanzaA(String dataIstanzaA) {
		this.dataIstanzaA = dataIstanzaA;
	}
	
	

}

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
 * DelegatoDTO
 *
 * @author CSI PIEMONTE
 */
public class DelegatoDTO extends GestAttoreDTO {
	
	@JsonProperty("id_delegato")
	private Long idDelegato;
	
	@JsonProperty("cf_delegato")
	private String cfDelegato;
	
	@JsonProperty("nominativo")
	private String nominativo;

	public Long getIdDelegato() {
		return idDelegato;
	}

	public void setIdDelegato(Long idDelegato) {
		this.idDelegato = idDelegato;
	}

	public String getCfDelegato() {
		return cfDelegato;
	}

	public void setCfDelegato(String cfDelegato) {
		this.cfDelegato = cfDelegato;
	}

	public String getNominativo() {
		return nominativo;
	}

	public void setNominativo(String nominativo) {
		this.nominativo = nominativo;
	}
	

	
}

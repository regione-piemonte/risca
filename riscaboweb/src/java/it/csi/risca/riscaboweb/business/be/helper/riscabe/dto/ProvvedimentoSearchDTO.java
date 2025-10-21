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

public class ProvvedimentoSearchDTO {

	@JsonProperty("id_provvedimento")
	private Long idProvvedimento;
	
	@JsonProperty("id_tipo_titolo")
	private Long idTipoTitolo;
	
	@JsonProperty("numero_titolo")
	private String numero_titolo;
	
	@JsonProperty("data_provvedimento_da")
	private String dataProvvedimentoDa;
	
	@JsonProperty("data_provvedimento_a")
	private String dataProvvedimentoA;

	public Long getIdProvvedimento() {
		return idProvvedimento;
	}

	public void setIdProvvedimento(Long idProvvedimento) {
		this.idProvvedimento = idProvvedimento;
	}

	public Long getIdTipoTitolo() {
		return idTipoTitolo;
	}

	public void setIdTipoTitolo(Long idTipoTitolo) {
		this.idTipoTitolo = idTipoTitolo;
	}

	public String getNumero_titolo() {
		return numero_titolo;
	}

	public void setNumero_titolo(String numero_titolo) {
		this.numero_titolo = numero_titolo;
	}

	public String getDataProvvedimentoDa() {
		return dataProvvedimentoDa;
	}

	public void setDataProvvedimentoDa(String dataProvvedimentoDa) {
		this.dataProvvedimentoDa = dataProvvedimentoDa;
	}

	public String getDataProvvedimentoA() {
		return dataProvvedimentoA;
	}

	public void setDataProvvedimentoA(String dataProvvedimentoA) {
		this.dataProvvedimentoA = dataProvvedimentoA;
	}
	

}

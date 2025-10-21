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

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CanoneUsoDTO {
	
	@JsonProperty("anno_uso")
    private String annoUso;
	
	@JsonProperty("cod_tipo_uso")
    private String codTipoUso;
	
	@JsonProperty("json_regola_mancante")
    private boolean jsonRegolaMancante;
	
	@JsonProperty("canone_uso")
    private BigDecimal canoneUso;
	
	@JsonProperty("canone_unitario")
    private BigDecimal canoneUnitario;
	
	@JsonProperty("canone_uso_frazionato")
    private BigDecimal canoneUsoFrazionato;
	
	public String getAnnoUso() {
		return annoUso;
	}

	public void setAnnoUso(String annoUso) {
		this.annoUso = annoUso;
	}

	public String getCodTipoUso() {
		return codTipoUso;
	}

	public void setCodTipoUso(String codTipoUso) {
		this.codTipoUso = codTipoUso;
	}

	public boolean isJsonRegolaMancante() {
		return jsonRegolaMancante;
	}

	public void setJsonRegolaMancante(boolean jsonRegolaMancante) {
		this.jsonRegolaMancante = jsonRegolaMancante;
	}

	public BigDecimal getCanoneUso() {
		return canoneUso;
	}

	public void setCanoneUso(BigDecimal canoneUso) {
		this.canoneUso = canoneUso;
	}

	public BigDecimal getCanoneUnitario() {
		return canoneUnitario;
	}

	public void setCanoneUnitario(BigDecimal canoneUnitario) {
		this.canoneUnitario = canoneUnitario;
	}

	public BigDecimal getCanoneUsoFrazionato() {
		return canoneUsoFrazionato;
	}

	public void setCanoneUsoFrazionato(BigDecimal canoneUsoFrazionato) {
		this.canoneUsoFrazionato = canoneUsoFrazionato;
	}

}

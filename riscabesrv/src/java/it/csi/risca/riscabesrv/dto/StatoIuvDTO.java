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

public class StatoIuvDTO {

	@Min(value = 1, message = "L' id_stato_iuv deve essere maggiore di 0")
	@Max(value = Integer.MAX_VALUE, message = "L' id_stato_iuv supera il limite massimo consentito per Integer")
	@JsonProperty("id_stato_iuv")
	private Long idStatoIuv;

    @Size(max = 30, min = 0, message = "cod_stato_iuv deve essere compreso tra 0 e 40 caratteri.")
	@JsonProperty("cod_stato_iuv")
	private String codStatoIuv;

    @Size(max = 30, min = 0, message = "des_stato_iuv deve essere compreso tra 0 e 40 caratteri.")
	@JsonProperty("des_stato_iuv")
	private String desStatoIuv;

	public Long getIdStatoIuv() {
		return idStatoIuv;
	}

	public void setIdStatoIuv(Long idStatoIuv) {
		this.idStatoIuv = idStatoIuv;
	}

	public String getCodStatoIuv() {
		return codStatoIuv;
	}

	public void setCodStatoIuv(String codStatoIuv) {
		this.codStatoIuv = codStatoIuv;
	}

	public String getDesStatoIuv() {
		return desStatoIuv;
	}

	public void setDesStatoIuv(String desStatoIuv) {
		this.desStatoIuv = desStatoIuv;
	}


}

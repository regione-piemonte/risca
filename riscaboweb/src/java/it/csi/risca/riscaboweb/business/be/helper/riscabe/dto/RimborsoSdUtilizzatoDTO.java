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

public class RimborsoSdUtilizzatoDTO {

	@JsonProperty("id_stato_debitorio")
	private Long idStatoDebitorio;

	@JsonProperty("id_rimborso")
	private Long idRimborso;

	@JsonProperty("imp_utilizzato")
	private BigDecimal impUtilizzato;

	public Long getIdStatoDebitorio() {
		return idStatoDebitorio;
	}

	public void setIdStatoDebitorio(Long idStatoDebitorio) {
		this.idStatoDebitorio = idStatoDebitorio;
	}

	public Long getIdRimborso() {
		return idRimborso;
	}

	public void setIdRimborso(Long idRimborso) {
		this.idRimborso = idRimborso;
	}

	public BigDecimal getImpUtilizzato() {
		return impUtilizzato;
	}

	public void setImpUtilizzato(BigDecimal impUtilizzato) {
		this.impUtilizzato = impUtilizzato;
	}

	@Override
	public String toString() {
		return "RimborsoSdUtilizzatoDTO [idStatoDebitorio=" + idStatoDebitorio + ", idRimborso=" + idRimborso
				+ ", impUtilizzato=" + impUtilizzato + "]";
	}

}

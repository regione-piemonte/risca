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

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * SommaCanoneTipoUsoSdDTO
 *
 * @author CSI PIEMONTE
 */

public class SommaCanoneTipoUsoSdDTO {

	@JsonProperty("id_stato_debitorio")
	private Long idStatoDebitorio;

	@JsonProperty("id_tipo_uso")
	private Long idTipoUso;

	@JsonProperty("id_accerta_bilancio")
	private Long idAccertaBilancio;

	@JsonProperty("ordina_tipo_uso")
	private Long ordinaTipoUso;

	@JsonProperty("totale_canone_uso")
	private BigDecimal totaleCanoneUso;

	public Long getIdStatoDebitorio() {
		return idStatoDebitorio;
	}

	public void setIdStatoDebitorio(Long idStatoDebitorio) {
		this.idStatoDebitorio = idStatoDebitorio;
	}

	public Long getIdTipoUso() {
		return idTipoUso;
	}

	public void setIdTipoUso(Long idTipoUso) {
		this.idTipoUso = idTipoUso;
	}

	public Long getIdAccertaBilancio() {
		return idAccertaBilancio;
	}

	public void setIdAccertaBilancio(Long idAccertaBilancio) {
		this.idAccertaBilancio = idAccertaBilancio;
	}

	public Long getOrdinaTipoUso() {
		return ordinaTipoUso;
	}

	public void setOrdinaTipoUso(Long ordinaTipoUso) {
		this.ordinaTipoUso = ordinaTipoUso;
	}

	public BigDecimal getTotaleCanoneUso() {
		return totaleCanoneUso;
	}

	public void setTotaleCanoneUso(BigDecimal totaleCanoneUso) {
		this.totaleCanoneUso = totaleCanoneUso;
	}

}

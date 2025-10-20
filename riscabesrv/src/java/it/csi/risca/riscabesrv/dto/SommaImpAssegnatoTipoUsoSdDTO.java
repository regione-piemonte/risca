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

public class SommaImpAssegnatoTipoUsoSdDTO {

	@JsonProperty("id_rata_sd")
	private Long idRataSd;

	@JsonProperty("id_accerta_bilancio")
	private Long idAccertaBilancio;

	@JsonProperty("imp_assegnato")
	private BigDecimal impAssegnato;

	public Long getIdRataSd() {
		return idRataSd;
	}

	public void setIdRataSd(Long idRataSd) {
		this.idRataSd = idRataSd;
	}

	public Long getIdAccertaBilancio() {
		return idAccertaBilancio;
	}

	public void setIdAccertaBilancio(Long idAccertaBilancio) {
		this.idAccertaBilancio = idAccertaBilancio;
	}

	public BigDecimal getImpAssegnato() {
		return impAssegnato;
	}

	public void setImpAssegnato(BigDecimal impAssegnato) {
		this.impAssegnato = impAssegnato;
	}

}

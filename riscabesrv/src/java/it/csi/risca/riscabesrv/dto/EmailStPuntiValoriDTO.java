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

public class EmailStPuntiValoriDTO {

	@JsonProperty("id_email_st_punti_valori")
	private Long idEmailStPuntiValori;

	@JsonProperty("id_email_standard")
	private Long idEmailStandard;

	@JsonProperty("id_output_colonna")
	private Long idOutputColonna;

	@JsonProperty("id_output_foglio")
	private Long idOutputFoglio;

	@JsonProperty("puntamento")
	private String puntamento;

	public Long getIdEmailStPuntiValori() {
		return idEmailStPuntiValori;
	}

	public void setIdEmailStPuntiValori(Long idEmailStPuntiValori) {
		this.idEmailStPuntiValori = idEmailStPuntiValori;
	}

	public Long getIdEmailStandard() {
		return idEmailStandard;
	}

	public void setIdEmailStandard(Long idEmailStandard) {
		this.idEmailStandard = idEmailStandard;
	}

	public Long getIdOutputColonna() {
		return idOutputColonna;
	}

	public void setIdOutputColonna(Long idOutputColonna) {
		this.idOutputColonna = idOutputColonna;
	}

	public Long getIdOutputFoglio() {
		return idOutputFoglio;
	}

	public void setIdOutputFoglio(Long idOutputFoglio) {
		this.idOutputFoglio = idOutputFoglio;
	}

	public String getPuntamento() {
		return puntamento;
	}

	public void setPuntamento(String puntamento) {
		this.puntamento = puntamento;
	}

}

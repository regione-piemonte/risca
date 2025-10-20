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

public class EmailStSegnapostoDTO {

	@JsonProperty("id_email_st_segnaposto")
	private Long idEmailStSegnaposto;

	@JsonProperty("id_email_st_punti_valori")
	private Long idEmailStPuntiValori;

	@JsonProperty("segnaposto")
	private String segnaposto;

	@JsonProperty("sezione")
	private String sezione;

	@JsonProperty("id_output_colonna")
	private Long idOutputColonna;

	public Long getIdEmailStSegnaposto() {
		return idEmailStSegnaposto;
	}

	public void setIdEmailStSegnaposto(Long idEmailStSegnaposto) {
		this.idEmailStSegnaposto = idEmailStSegnaposto;
	}

	public Long getIdEmailStPuntiValori() {
		return idEmailStPuntiValori;
	}

	public void setIdEmailStPuntiValori(Long idEmailStPuntiValori) {
		this.idEmailStPuntiValori = idEmailStPuntiValori;
	}

	public String getSegnaposto() {
		return segnaposto;
	}

	public void setSegnaposto(String segnaposto) {
		this.segnaposto = segnaposto;
	}

	public String getSezione() {
		return sezione;
	}

	public void setSezione(String sezione) {
		this.sezione = sezione;
	}

	public Long getIdOutputColonna() {
		return idOutputColonna;
	}

	public void setIdOutputColonna(Long idOutputColonna) {
		this.idOutputColonna = idOutputColonna;
	}

}

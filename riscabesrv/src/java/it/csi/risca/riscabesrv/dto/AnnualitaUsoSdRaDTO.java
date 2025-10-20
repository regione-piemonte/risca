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

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AnnualitaUsoSdRaDTO extends GestAttoreDTO {

	@JsonProperty("id_annualita_uso_sd_ra")
	private Long idAnnualitaUsoSdRa;

	@JsonProperty("id_annualita_uso_sd")
	private Long idAnnualitaUsoSd;

	@JsonProperty("id_riduzione_aumento")
	private Long idRiduzioneAumento;


	public Long getIdAnnualitaUsoSdRa() {
		return idAnnualitaUsoSdRa;
	}

	public void setIdAnnualitaUsoSdRa(Long idAnnualitaUsoSdRa) {
		this.idAnnualitaUsoSdRa = idAnnualitaUsoSdRa;
	}

	public Long getIdAnnualitaUsoSd() {
		return idAnnualitaUsoSd;
	}

	public void setIdAnnualitaUsoSd(Long idAnnualitaUsoSd) {
		this.idAnnualitaUsoSd = idAnnualitaUsoSd;
	}

	public Long getIdRiduzioneAumento() {
		return idRiduzioneAumento;
	}

	public void setIdRiduzioneAumento(Long idRiduzioneAumento) {
		this.idRiduzioneAumento = idRiduzioneAumento;
	}

}

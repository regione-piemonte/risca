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

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Uso Ridaum Sd DTO
 *
 * @author CSI PIEMONTE
 */


public class UsoRidaumSdDTO extends GestAttoreDTO {

    @Min(value = 1, message = "L' id_uso_ridaum_sd deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_uso_ridaum_sd supera il limite massimo consentito per Integer")
	@JsonProperty("id_uso_ridaum_sd")
    private Long idAnnualitaUsoSdRa;
	
    @Min(value = 1, message = "L' id_annualita_uso_ridaum deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_annualita_uso_ridaum supera il limite massimo consentito per Integer")
	@JsonProperty("id_annualita_uso_ridaum")
    private Long idAnnualitaUsoRidaum;
	
    @Min(value = 1, message = "L' id_riduzione_aumento deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_riduzione_aumento supera il limite massimo consentito per Integer")
	@JsonProperty("id_riduzione_aumento")
    private Long idRiduzioneAumento;



	public Long getIdAnnualitaUsoSdRa() {
		return idAnnualitaUsoSdRa;
	}

	public void setIdAnnualitaUsoSdRa(Long idAnnualitaUsoSdRa) {
		this.idAnnualitaUsoSdRa = idAnnualitaUsoSdRa;
	}

	public Long getIdAnnualitaUsoRidaum() {
		return idAnnualitaUsoRidaum;
	}

	public void setIdAnnualitaUsoRidaum(Long idAnnualitaUsoRidaum) {
		this.idAnnualitaUsoRidaum = idAnnualitaUsoRidaum;
	}

	public Long getIdRiduzioneAumento() {
		return idRiduzioneAumento;
	}

	public void setIdRiduzioneAumento(Long idRiduzioneAumento) {
		this.idRiduzioneAumento = idRiduzioneAumento;
	}
	
}

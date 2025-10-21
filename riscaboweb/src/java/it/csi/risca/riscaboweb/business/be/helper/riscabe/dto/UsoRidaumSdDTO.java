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

/**
 * Uso Ridaum Sd DTO
 *
 * @author CSI PIEMONTE
 */


public class UsoRidaumSdDTO {


	@JsonProperty("id_uso_ridaum_sd")
    private Long idAnnualitaUsoSdRa;
	
	@JsonProperty("id_annualita_uso_ridaum")
    private Long idAnnualitaUsoRidaum;
	
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

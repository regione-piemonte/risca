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

import com.fasterxml.jackson.annotation.JsonProperty;

public class RimborsoExtendedDTO extends RimborsoDTO {

    @Min(value = 1, message = "L' id_riscossione deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_riscossione supera il limite massimo consentito per Integer")
	@JsonProperty("id_riscossione")
	private Long idRiscossione;

	

	@JsonProperty("tipo_rimborso")
	private TipoRimborsoDTO tipoRimborso;

	@JsonProperty("soggetto_rimborso")
	private SoggettiExtendedDTO soggettoRimborso;

	
	@JsonProperty("rimborso_sd_utilizzato")
	private RimborsoSdUtilizzatoDTO rimborsoSdUtilizzato;
	
	
	public Long getIdRiscossione() {
		return idRiscossione;
	}

	public void setIdRiscossione(Long idRiscossione) {
		this.idRiscossione = idRiscossione;
	}


	public TipoRimborsoDTO getTipoRimborso() {
		return tipoRimborso;
	}

	public void setTipoRimborso(TipoRimborsoDTO tipoRimborso) {
		this.tipoRimborso = tipoRimborso;
	}

	public SoggettiExtendedDTO getSoggettoRimborso() {
		return soggettoRimborso;
	}

	public void setSoggettoRimborso(SoggettiExtendedDTO soggettoRimborso) {
		this.soggettoRimborso = soggettoRimborso;
	}

	public RimborsoSdUtilizzatoDTO getRimborsoSdUtilizzato() {
		return rimborsoSdUtilizzato;
	}

	public void setRimborsoSdUtilizzato(RimborsoSdUtilizzatoDTO rimborsoSdUtilizzato) {
		this.rimborsoSdUtilizzato = rimborsoSdUtilizzato;
	}


}

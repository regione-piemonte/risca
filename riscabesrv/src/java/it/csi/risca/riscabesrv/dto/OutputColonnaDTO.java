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

/**
 * The type OutputFile dto.
 *
 * @author CSI PIEMONTE
 */
public class OutputColonnaDTO extends GestAttoreDTO {

	@JsonProperty("id_output_colonna")
	private Long idOutputColonna;

	@JsonProperty("id_output_foglio")
	private Long idOutputFoglio;

	@JsonProperty("id_tipo_dato_colonna")
	private Long idTipoDatoColonna;

	@JsonProperty("desc_etichetta")
	private String descEtichetta;

	@JsonProperty("progressivo")
	private Integer progressivo;

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

	public Long getIdTipoDatoColonna() {
		return idTipoDatoColonna;
	}

	public void setIdTipoDatoColonna(Long idTipoDatoColonna) {
		this.idTipoDatoColonna = idTipoDatoColonna;
	}

	public String getDescEtichetta() {
		return descEtichetta;
	}

	public void setDescEtichetta(String descEtichetta) {
		this.descEtichetta = descEtichetta;
	}

	public Integer getProgressivo() {
		return progressivo;
	}

	public void setProgressivo(Integer progressivo) {
		this.progressivo = progressivo;
	}

}
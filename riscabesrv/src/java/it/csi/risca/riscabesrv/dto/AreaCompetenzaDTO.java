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

public class AreaCompetenzaDTO extends GestAttoreDTO {

	@JsonProperty("id_ambito_area_competenza")
	private Integer idAmbitoAreaCompetenza;
	
	@JsonProperty("id_ambito")
	private Integer idAmbito;
	
	@JsonProperty("id_tipo_area_competenza")
	private Integer idTipoAreaCompetenza;

	@JsonProperty("chiave_area_competenza")
    private String chiaveAreaCompetenza;



	public Integer getIdAmbitoAreaCompetenza() {
		return idAmbitoAreaCompetenza;
	}

	public void setIdAmbitoAreaCompetenza(Integer idAmbitoAreaCompetenza) {
		this.idAmbitoAreaCompetenza = idAmbitoAreaCompetenza;
	}

	public Integer getIdAmbito() {
		return idAmbito;
	}

	public void setIdAmbito(Integer idAmbito) {
		this.idAmbito = idAmbito;
	}

	public Integer getIdTipoAreaCompetenza() {
		return idTipoAreaCompetenza;
	}

	public void setIdTipoAreaCompetenza(Integer idTipoAreaCompetenza) {
		this.idTipoAreaCompetenza = idTipoAreaCompetenza;
	}

	public String getChiaveAreaCompetenza() {
		return chiaveAreaCompetenza;
	}

	public void setChiaveAreaCompetenza(String chiaveAreaCompetenza) {
		this.chiaveAreaCompetenza = chiaveAreaCompetenza;
	}
	
	
}

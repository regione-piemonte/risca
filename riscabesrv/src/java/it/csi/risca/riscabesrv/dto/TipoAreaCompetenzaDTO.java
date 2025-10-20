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

public class TipoAreaCompetenzaDTO extends GestAttoreDTO {

	@JsonProperty("id_tipo_area_competenza")
	private Integer idTipoAreaCompetenza;

	@JsonProperty("cod_tipo_area_competenza")
    private String codTipoAreaCompetenza;
	
	@JsonProperty("des_tipo_area_competenza")
    private String desTipoAreaCompetenza;

	public Integer getIdTipoAreaCompetenza() {
		return idTipoAreaCompetenza;
	}

	public void setIdTipoAreaCompetenza(Integer idTipoAreaCompetenza) {
		this.idTipoAreaCompetenza = idTipoAreaCompetenza;
	}

	public String getCodTipoAreaCompetenza() {
		return codTipoAreaCompetenza;
	}

	public void setCodTipoAreaCompetenza(String codTipoAreaCompetenza) {
		this.codTipoAreaCompetenza = codTipoAreaCompetenza;
	}

	public String getDesTipoAreaCompetenza() {
		return desTipoAreaCompetenza;
	}

	public void setDesTipoAreaCompetenza(String desTipoAreaCompetenza) {
		this.desTipoAreaCompetenza = desTipoAreaCompetenza;
	}
	
	
}

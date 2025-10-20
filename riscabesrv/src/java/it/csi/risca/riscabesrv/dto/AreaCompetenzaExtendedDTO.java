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

public class AreaCompetenzaExtendedDTO extends AreaCompetenzaDTO{

	@JsonProperty("tipo_area_competenza")
    private TipoAreaCompetenzaDTO tipoAreaCompetenza;
	
	@JsonProperty("ambito")
    private AmbitoDTO ambito;

	public TipoAreaCompetenzaDTO getTipoAreaCompetenza() {
		return tipoAreaCompetenza;
	}

	public void setTipoAreaCompetenza(TipoAreaCompetenzaDTO tipoAreaCompetenza) {
		this.tipoAreaCompetenza = tipoAreaCompetenza;
	}

	public AmbitoDTO getAmbito() {
		return ambito;
	}

	public void setAmbito(AmbitoDTO ambito) {
		this.ambito = ambito;
	}
	
	
}

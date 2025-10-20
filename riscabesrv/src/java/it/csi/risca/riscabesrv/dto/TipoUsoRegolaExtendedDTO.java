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

public class TipoUsoRegolaExtendedDTO extends TipoUsoRegolaDTO{
	
	@JsonProperty("tipo_uso")
    private TipoUsoExtendedDTO tipoUso; 
	
	@JsonProperty("json_regola_obj")
    private JsonRegolaDTO jsonRegolaDTO;

	public TipoUsoExtendedDTO getTipoUso() {
		return tipoUso;
	}

	public void setTipoUso(TipoUsoExtendedDTO tipoUso) {
		this.tipoUso = tipoUso;
	}

	public JsonRegolaDTO getJsonRegolaDTO() {
		return jsonRegolaDTO;
	}

	public void setJsonRegolaDTO(JsonRegolaDTO jsonRegolaDTO) {
		this.jsonRegolaDTO = jsonRegolaDTO;
	}


	
	
	
}

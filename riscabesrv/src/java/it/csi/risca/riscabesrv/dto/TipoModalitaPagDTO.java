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
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TipoModalitaPagDTO {

    @Min(value = 1, message = "L' id_tipo_modalita_pag deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_tipo_modalita_pag supera il limite massimo consentito per Integer")
	@JsonProperty("id_tipo_modalita_pag")
    private Long idTipoModalitaPag;
	
    @Size(max = 40, min = 0, message = "cod_modalita_pag deve essere compreso tra 0 e 40 caratteri. ")
    @JsonProperty("cod_modalita_pag")
    private String codModalitaPag;
	
    @Size(max = 40, min = 0, message = "des_modalita_pag deve essere compreso tra 0 e 40 caratteri. ")
    @JsonProperty("des_modalita_pag")
    private String desModalitaPag;

	public Long getIdTipoModalitaPag() {
		return idTipoModalitaPag;
	}

	public void setIdTipoModalitaPag(Long idTipoModalitaPag) {
		this.idTipoModalitaPag = idTipoModalitaPag;
	}

	public String getCodModalitaPag() {
		return codModalitaPag;
	}

	public void setCodModalitaPag(String codModalitaPag) {
		this.codModalitaPag = codModalitaPag;
	}

	public String getDesModalitaPag() {
		return desModalitaPag;
	}

	public void setDesModalitaPag(String desModalitaPag) {
		this.desModalitaPag = desModalitaPag;
	}
    
	


}

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

/**
 * TipiRecapitoDTO
 *
 * @author CSI PIEMONTE
 */

public class TipiRecapitoDTO {
	
    @Min(value = 1, message = "L' id_tipo_recapito deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_tipo_recapito supera il limite massimo consentito per Integer")
	@JsonProperty("id_tipo_recapito")
    private Long idTipoRecapito;
	  
    @JsonProperty("cod_tipo_recapito")
    private String codTipoRecapito;
    
    @JsonProperty("des_tipo_recapito")
    private String desTipoRecapito;
	
	public Long getIdTipoRecapito() {
		return idTipoRecapito;
	}

	public void setIdTipoRecapito(Long idTipoRecapito) {
		this.idTipoRecapito = idTipoRecapito;
	}

	public String getCodTipoRecapito() {
		return codTipoRecapito;
	}

	public void setCodTipoRecapito(String codTipoRecapito) {
		this.codTipoRecapito = codTipoRecapito;
	}

	public String getDesTipoRecapito() {
		return desTipoRecapito;
	}

	public void setDesTipoRecapito(String desTipoRecapito) {
		this.desTipoRecapito = desTipoRecapito;
	}

}

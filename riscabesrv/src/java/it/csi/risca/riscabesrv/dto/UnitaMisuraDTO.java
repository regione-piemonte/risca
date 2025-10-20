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

/**
 * UnitaMisuraDTO
 *
 * @author CSI PIEMONTE
 */

public class UnitaMisuraDTO {

    @Min(value = 1, message = "L' id_unita_misura deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_unita_misura supera il limite massimo consentito per Integer")
	@JsonProperty("id_unita_misura")
    private Long idUnitaMisura;

    @Size(max = 20, min = 0, message = "sigla_unita_misura deve essere compreso tra 0 e 20 caratteri.")
    @JsonProperty("sigla_unita_misura")
    private String siglaUnitaMisura;
    
    @Size(max = 150, min = 0, message = "des_unita_misura deve essere compreso tra 0 e 150 caratteri.")
    @JsonProperty("des_unita_misura")
    private String desUnitaMisura;

    @Min(value = 1, message = "L' ordina_unita_misura deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' ordina_unita_misura supera il limite massimo consentito per Integer")
    @JsonProperty("ordina_unita_misura")
    private Long ordinaUnitaMisura;
	
	public Long getIdUnitaMisura() {
		return idUnitaMisura;
	}

	public void setIdUnitaMisura(Long idUnitaMisura) {
		this.idUnitaMisura = idUnitaMisura;
	}

	public String getSiglaUnitaMisura() {
		return siglaUnitaMisura;
	}

	public void setSiglaUnitaMisura(String siglaUnitaMisura) {
		this.siglaUnitaMisura = siglaUnitaMisura;
	}

	public String getDesUnitaMisura() {
		return desUnitaMisura;
	}

	public void setDesUnitaMisura(String desUnitaMisura) {
		this.desUnitaMisura = desUnitaMisura;
	}

	public Long getOrdinaUnitaMisura() {
		return ordinaUnitaMisura;
	}

	public void setOrdinaUnitaMisura(Long ordinaUnitaMisura) {
		this.ordinaUnitaMisura = ordinaUnitaMisura;
	}

}

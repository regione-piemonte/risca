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
 * AccertamentoBilancioDTO
 *
 * @author CSI PIEMONTE
 */

public class AccertamentoBilancioDTO {
	
    @Min(value = 1, message = "L' id_accerta_bilancio deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_accerta_bilancio supera il limite massimo consentito per Integer")
	@JsonProperty("id_accerta_bilancio")
    private Long idAccertaBilancio;

    @Size(max = 20, min = 0, message = "cod_accerta_bilancio deve essere compreso tra 0 e 20 caratteri.")
    @JsonProperty("cod_accerta_bilancio")
    private String codAccertaBilancio;
    
    @Size(max = 150, min = 0, message = "des_accerta_bilancio deve essere compreso tra 0 e 150 caratteri.")
    @JsonProperty("des_accerta_bilancio")
    private String desAccertaBilancio;

	public Long getIdAccertaBilancio() {
		return idAccertaBilancio;
	}

	public void setIdAccertaBilancio(Long idAccertaBilancio) {
		this.idAccertaBilancio = idAccertaBilancio;
	}

	public String getCodAccertaBilancio() {
		return codAccertaBilancio;
	}

	public void setCodAccertaBilancio(String codAccertaBilancio) {
		this.codAccertaBilancio = codAccertaBilancio;
	}

	public String getDesAccertaBilancio() {
		return desAccertaBilancio;
	}

	public void setDesAccertaBilancio(String desAccertaBilancio) {
		this.desAccertaBilancio = desAccertaBilancio;
	}
	

}

/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * AccertamentoBilancioDTO
 *
 * @author CSI PIEMONTE
 */

public class AccertamentoBilancioDTO {
	
	@JsonProperty("id_accerta_bilancio")
    private Long idAccertaBilancio;

    @JsonProperty("cod_accerta_bilancio")
    private String codAccertaBilancio;
    
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

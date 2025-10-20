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

/**
 * Stato Elaborazione DTO
 *
 * @author CSI PIEMONTE
 */

import com.fasterxml.jackson.annotation.JsonProperty;

public class StatoElaborazioneDTO {
	
    @Min(value = 1, message = "L' id_stato_elabora deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_stato_elabora supera il limite massimo consentito per Integer")
	@JsonProperty("id_stato_elabora")
    private Long idStatoElabora;

    @Size(max = 40, min = 0, message = "cod_stato_elabora deve essere compreso tra 0 e 40 caratteri.  ")
    @JsonProperty("cod_stato_elabora")
    private String codStatoElabora;
    
    @Size(max = 40, min = 0, message = "des_stato_elabora deve essere compreso tra 0 e 40 caratteri.  ")
    @JsonProperty("des_stato_elabora")
    private String desStatoElabora;

	public Long getIdStatoElabora() {
		return idStatoElabora;
	}

	public void setIdStatoElabora(Long idStatoElabora) {
		this.idStatoElabora = idStatoElabora;
	}

	public String getCodStatoElabora() {
		return codStatoElabora;
	}

	public void setCodStatoElabora(String codStatoElabora) {
		this.codStatoElabora = codStatoElabora;
	}

	public String getDesStatoElabora() {
		return desStatoElabora;
	}

	public void setDesStatoElabora(String desStatoElabora) {
		this.desStatoElabora = desStatoElabora;
	}

}

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

public class ParametroElaboraDTO {
	
    @Min(value = 1, message = "L' id_parametro_elabora deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_parametro_elabora supera il limite massimo consentito per Integer")
	@JsonProperty("id_parametro_elabora")
	private Long idParametroElabora;

    @Min(value = 1, message = "L' id_elabora deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_elabora supera il limite massimo consentito per Integer")
	@JsonProperty("id_elabora")
	private Long idElabora;

    @Size(max = 10, min = 0, message = "raggruppamento deve essere compreso tra 0 e 10 caratteri.")
	@JsonProperty("raggruppamento")
	private String raggruppamento = null;

    @Size(max = 150, min = 0, message = "chiave deve essere compreso tra 0 e 150 caratteri.")
	@JsonProperty("chiave")
	private String chiave = null;

    @Size(max = 1000, min = 0, message = "valore deve essere compreso tra 0 e 1000 caratteri.")
	@JsonProperty("valore")
	private String valore = null;

	public Long getIdParametroElabora() {
		return idParametroElabora;
	}

	public void setIdParametroElabora(Long idParametroElabora) {
		this.idParametroElabora = idParametroElabora;
	}

	public Long getIdElabora() {
		return idElabora;
	}

	public void setIdElabora(Long idElabora) {
		this.idElabora = idElabora;
	}

	public String getRaggruppamento() {
		return raggruppamento;
	}

	public void setRaggruppamento(String raggruppamento) {
		this.raggruppamento = raggruppamento;
	}

	public String getChiave() {
		return chiave;
	}

	public void setChiave(String chiave) {
		this.chiave = chiave;
	}

	public String getValore() {
		return valore;
	}

	public void setValore(String valore) {
		this.valore = valore;
	}

}

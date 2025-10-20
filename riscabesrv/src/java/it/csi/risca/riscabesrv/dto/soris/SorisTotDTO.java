/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.dto.soris;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The type SorisTot dto.
 *
 * @author CSI PIEMONTE
 */
public class SorisTotDTO {

    @Min(value = 1, message = "L' id_elabora deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_elabora supera il limite massimo consentito per Integer")
	@JsonProperty("id_elabora")
	private Long idElabora;

    @Size(max = 400, min = 0, message = "campo_tot deve essere compreso tra 0 e 400 caratteri. ")
	@JsonProperty("campo_tot")
	private String campoTot;


	public String getCampoTot() {
		return campoTot;
	}

	public void setCampoTot(String campoTot) {
		this.campoTot = campoTot;
	}

	public Long getIdElabora() {
		return idElabora;
	}

	public void setIdElabora(Long idElabora) {
		this.idElabora = idElabora;
	}

	

	@Override
	public String toString() {
		return "SorisTotDTO [idElabora=" + idElabora + ", campoTot=" + campoTot+ "]";
	}

}
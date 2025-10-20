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

import java.util.Date;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The type Immagine dto.
 *
 * @author CSI PIEMONTE
 */
public class ImmagineDTO extends GestAttoreDTO {

    @Min(value = 1, message = "L' id_immagine deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_immagine supera il limite massimo consentito per Integer")
	@JsonProperty("id_immagine")
	private Long idImmagine;

    @Size(max = 100, min = 0, message = "immagine deve essere compreso tra 0 e 100 caratteri.")
	@JsonProperty("immagine")
	private String immagine;

    @Min(value = 0, message = "Il flg_validita deve essere almeno 0.")
    @Max(value = 1, message = "Il flg_validita deve essere al massimo 1.")
	@JsonProperty("flg_validita")
	private int flgValidita;

    @Size(max = 300, min = 0, message = "path_immagine deve essere compreso tra 0 e 300 caratteri.")
	@JsonProperty("path_immagine")
	private String pathImmagine;

	
	public Long getIdImmagine() {
		return idImmagine;
	}

	public void setIdImmagine(Long idImmagine) {
		this.idImmagine = idImmagine;
	}

	public String getImmagine() {
		return immagine;
	}

	public void setImmagine(String immagine) {
		this.immagine = immagine;
	}

	public int getFlgValidita() {
		return flgValidita;
	}

	public void setFlgValidita(int flgValidita) {
		this.flgValidita = flgValidita;
	}

	public String getPathImmagine() {
		return pathImmagine;
	}

	public void setPathImmagine(String pathImmagine) {
		this.pathImmagine = pathImmagine;
	}
}
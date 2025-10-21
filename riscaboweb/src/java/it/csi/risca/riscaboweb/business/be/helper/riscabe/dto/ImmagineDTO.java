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

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The type Immagine dto.
 *
 * @author CSI PIEMONTE
 */
public class ImmagineDTO {

	@JsonProperty("id_immagine")
	private Long idImmagine;

	@JsonProperty("immagine")
	private String immagine;

	@JsonProperty("flg_validita")
	private int flgValidita;

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
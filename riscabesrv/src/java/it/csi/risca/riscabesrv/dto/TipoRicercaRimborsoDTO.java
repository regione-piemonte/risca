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

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * TipoRicercaRimborsoDTO
 *
 * @author CSI PIEMONTE
 */
public class TipoRicercaRimborsoDTO {
	
	@JsonProperty("id_tipo_ricerca_rimborso")
	private Long idTipoRicercaRimborso;

	@JsonProperty("cod_tipo_ricerca_rimborso")
	private String codTipoRicercaRimborso;

	@JsonProperty("des_tipo_ricerca_rimborso")
	private String desTipoRicercaRimborso;

	public Long getIdTipoRicercaRimborso() {
		return idTipoRicercaRimborso;
	}

	public void setIdTipoRicercaRimborso(Long idTipoRicercaRimborso) {
		this.idTipoRicercaRimborso = idTipoRicercaRimborso;
	}

	public String getCodTipoRicercaRimborso() {
		return codTipoRicercaRimborso;
	}

	public void setCodTipoRicercaRimborso(String codTipoRicercaRimborso) {
		this.codTipoRicercaRimborso = codTipoRicercaRimborso;
	}

	public String getDesTipoRicercaRimborso() {
		return desTipoRicercaRimborso;
	}

	public void setDesTipoRicercaRimborso(String desTipoRicercaRimborso) {
		this.desTipoRicercaRimborso = desTipoRicercaRimborso;
	}

	
}

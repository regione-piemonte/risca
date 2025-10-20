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
 * TipoRicercaMorositaDTO
 *
 * @author CSI PIEMONTE
 */
public class TipoRicercaMorositaDTO {
	
	@JsonProperty("id_tipo_ricerca_morosita")
	private Long idTipoRicercaMorosita;

	@JsonProperty("cod_tipo_ricerca_morosita")
	private String codTipoRicercaMorosita;

	@JsonProperty("des_tipo_ricerca_morosita")
	private String desTipoRicercaMorosita;

	public Long getIdTipoRicercaMorosita() {
		return idTipoRicercaMorosita;
	}

	public void setIdTipoRicercaMorosita(Long idTipoRicercaMorosita) {
		this.idTipoRicercaMorosita = idTipoRicercaMorosita;
	}

	public String getCodTipoRicercaMorosita() {
		return codTipoRicercaMorosita;
	}

	public void setCodTipoRicercaMorosita(String codTipoRicercaMorosita) {
		this.codTipoRicercaMorosita = codTipoRicercaMorosita;
	}

	public String getDesTipoRicercaMorosita() {
		return desTipoRicercaMorosita;
	}

	public void setDesTipoRicercaMorosita(String desTipoRicercaMorosita) {
		this.desTipoRicercaMorosita = desTipoRicercaMorosita;
	}
	
	

}

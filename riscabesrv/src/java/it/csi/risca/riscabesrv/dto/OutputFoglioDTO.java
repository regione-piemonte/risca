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

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The type OutputFile dto.
 *
 * @author CSI PIEMONTE
 */
public class OutputFoglioDTO extends GestAttoreDTO {

	@JsonProperty("id_output_foglio")
	private Long idOutputFoglio;

	@JsonProperty("id_output_file")
	private Long idOutputFile;

	@JsonProperty("nome_foglio")
	private String nomeFoglio;

	@JsonProperty("numero_campi")
	private Integer numeroCampi;

	List<OutputColonnaDTO> colonne;

	List<OutputDatiDTO> dati;

	public Long getIdOutputFoglio() {
		return idOutputFoglio;
	}

	public void setIdOutputFoglio(Long idOutputFoglio) {
		this.idOutputFoglio = idOutputFoglio;
	}

	public Long getIdOutputFile() {
		return idOutputFile;
	}

	public void setIdOutputFile(Long idOutputFile) {
		this.idOutputFile = idOutputFile;
	}

	public String getNomeFoglio() {
		return nomeFoglio;
	}

	public void setNomeFoglio(String nomeFoglio) {
		this.nomeFoglio = nomeFoglio;
	}

	public Integer getNumeroCampi() {
		return numeroCampi;
	}

	public void setNumeroCampi(Integer numeroCampi) {
		this.numeroCampi = numeroCampi;
	}

	public List<OutputColonnaDTO> getColonne() {
		return colonne;
	}

	public void setColonne(List<OutputColonnaDTO> colonne) {
		this.colonne = colonne;
	}

	public List<OutputDatiDTO> getDati() {
		return dati;
	}

	public void setDati(List<OutputDatiDTO> dati) {
		this.dati = dati;
	}

}
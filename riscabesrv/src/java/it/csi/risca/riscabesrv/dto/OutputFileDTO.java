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
public class OutputFileDTO extends GestAttoreDTO {

	@JsonProperty("id_output_file")
	private Long idOutputFile;

	@JsonProperty("id_tipo_elabora")
	private Long idTipoElabora;

	@JsonProperty("id_tipo_passo_elabora")
	private Long idTipoPassoElabora;

	@JsonProperty("nome_file")
	private String nomeFile;

	@JsonProperty("tipo_file")
	private String tipoFile;

	@JsonProperty("cod_report")
	private String codReport;

	@JsonProperty("des_report")
	private String desReport;

	List<OutputFoglioDTO> fogli;

	public Long getIdOutputFile() {
		return idOutputFile;
	}

	public void setIdOutputFile(Long idOutputFile) {
		this.idOutputFile = idOutputFile;
	}

	public Long getIdTipoElabora() {
		return idTipoElabora;
	}

	public void setIdTipoElabora(Long idTipoElabora) {
		this.idTipoElabora = idTipoElabora;
	}

	public Long getIdTipoPassoElabora() {
		return idTipoPassoElabora;
	}

	public void setIdTipoPassoElabora(Long idTipoPassoElabora) {
		this.idTipoPassoElabora = idTipoPassoElabora;
	}

	public String getNomeFile() {
		return nomeFile;
	}

	public void setNomeFile(String nomeFile) {
		this.nomeFile = nomeFile;
	}

	public String getTipoFile() {
		return tipoFile;
	}

	public void setTipoFile(String tipoFile) {
		this.tipoFile = tipoFile;
	}

	public String getCodReport() {
		return codReport;
	}

	public void setCodReport(String codReport) {
		this.codReport = codReport;
	}

	public String getDesReport() {
		return desReport;
	}

	public void setDesReport(String desReport) {
		this.desReport = desReport;
	}

	public List<OutputFoglioDTO> getFogli() {
		return fogli;
	}

	public void setFogli(List<OutputFoglioDTO> fogli) {
		this.fogli = fogli;
	}

}
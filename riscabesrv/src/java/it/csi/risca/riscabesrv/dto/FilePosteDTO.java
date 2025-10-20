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

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The type FilePoste dto.
 *
 * @author CSI PIEMONTE
 */
public class FilePosteDTO extends GestAttoreDTO {

    @Min(value = 1, message = "L' id_file_poste deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_file_poste supera il limite massimo consentito per Integer")
	@JsonProperty("id_file_poste")
	private Long idFilePoste;

    @Size(max = 20, min = 0, message = "tipo_file_poste deve essere compreso tra 0 e 20 caratteri.")
	@JsonProperty("tipo_file_poste")
	private String tipoFilePoste;

    @Size(max = 100, min = 0, message = "nome deve essere compreso tra 0 e 100 caratteri.")
	@JsonProperty("nome")
	private String nome;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonProperty("data_file")
	private Date dataFile;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonProperty("data_scarico")
	private Date dataScarico;


	public Long getIdFilePoste() {
		return idFilePoste;
	}

	public void setIdFilePoste(Long idFilePoste) {
		this.idFilePoste = idFilePoste;
	}

	public String getTipoFilePoste() {
		return tipoFilePoste;
	}

	public void setTipoFilePoste(String tipoFilePoste) {
		this.tipoFilePoste = tipoFilePoste;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Date getDataFile() {
		return dataFile;
	}

	public void setDataFile(Date dataFile) {
		this.dataFile = dataFile;
	}

	public Date getDataScarico() {
		return dataScarico;
	}

	public void setDataScarico(Date dataScarico) {
		this.dataScarico = dataScarico;
	}

}
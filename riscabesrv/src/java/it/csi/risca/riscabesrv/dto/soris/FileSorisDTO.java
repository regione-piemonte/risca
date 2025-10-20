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

import java.util.Date;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.csi.risca.riscabesrv.dto.GestAttoreDTO;

/**
 * The type FileSoris dto.
 *
 * @author CSI PIEMONTE
 */
public class FileSorisDTO extends GestAttoreDTO {

    @Min(value = 1, message = "L' id_file_soris deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_file_soris supera il limite massimo consentito per Integer")
	@JsonProperty("id_file_soris")
	private Long idFileSoris;

    @Size(max = 250, min = 0, message = "nome deve essere compreso tra 0 e 250 caratteri.")
	@JsonProperty("nome_file_soris")
	private String nomeFileSoris;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonProperty("data_file")
	private Date dataFile;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonProperty("data_scarico")
	private Date dataScarico;
	

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

	public Long getIdFileSoris() {
		return idFileSoris;
	}

	public void setIdFileSoris(Long idFileSoris) {
		this.idFileSoris = idFileSoris;
	}

	public String getNomeFileSoris() {
		return nomeFileSoris;
	}

	public void setNomeFileSoris(String nomeFileSoris) {
		this.nomeFileSoris = nomeFileSoris;
	}

}
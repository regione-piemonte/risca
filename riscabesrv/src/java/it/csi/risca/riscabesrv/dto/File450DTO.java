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

public class File450DTO extends GestAttoreDTO {

	
    @Min(value = 1, message = "L' id_file_450 deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_file_450 supera il limite massimo consentito per Integer")
	@JsonProperty("id_file_450")
	private Long idFile450;

    @Size(max = 100, min = 0, message = "nome_file deve essere compreso tra 0 e 100 caratteri.")
	@JsonProperty("nome_file")
	private String nomeFile;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonProperty("data_creazione")
	private Date dataCreazione;
	
    @DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonProperty("data_conferma")
	private Date dataConferma;

   

	public Long getIdFile450() {
		return idFile450;
	}

	public void setIdFile450(Long idFile450) {
		this.idFile450 = idFile450;
	}

	public String getNomeFile() {
		return nomeFile;
	}

	public void setNomeFile(String nomeFile) {
		this.nomeFile = nomeFile;
	}

	public Date getDataCreazione() {
		return dataCreazione;
	}

	public void setDataCreazione(Date dataCreazione) {
		this.dataCreazione = dataCreazione;
	}

	public Date getDataConferma() {
		return dataConferma;
	}

	public void setDataConferma(Date dataConferma) {
		this.dataConferma = dataConferma;
	}

	@Override
	public String toString() {
		return "File450DTO [idFile450=" + idFile450 + ", nomeFile=" + nomeFile + ", dataCreazione=" + dataCreazione
				+ ", dataConferma=" + dataConferma + "]";
	}

	
	


}

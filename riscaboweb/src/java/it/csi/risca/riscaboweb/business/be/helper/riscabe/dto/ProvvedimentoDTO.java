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

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProvvedimentoDTO  {

    @Min(value = 1, message = "L' id_provvedimento deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_provvedimento supera il limite massimo consentito per Integer")
	@JsonProperty("id_provvedimento")
    private Long idProvvedimento;
	
    @Min(value = 1, message = "L' id_riscossione deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_riscossione supera il limite massimo consentito per Integer")
	@JsonProperty("id_riscossione")
    private Long idRiscossione;
	
	@JsonProperty("id_tipo_titolo")
    private TipiTitoloExtendedDTO tipoTitoloExtendedDTO;
	
	@JsonProperty("id_tipo_provvedimento")
    private TipiProvvedimentoExtendedDTO tipiProvvedimentoExtendedDTO;

    @Size(max = 20, min = 0, message = "num_titolo deve essere compreso tra 0 e 20 caratteri. ")
    @JsonProperty("num_titolo")
    private String numTitolo;
    
    @Pattern(regexp = "^(|\\d{4}-\\d{2}-\\d{2})$", message = "Formato data non valido. Utilizzare yyyy-MM-dd.")
	@JsonProperty("data_provvedimento")
    private String dataProvvedimento;
	
    @Size(max = 500, min = 0, message = "note deve essere compreso tra 0 e 500 caratteri. ")
    @JsonProperty("note")
    private String note;
	
	

	public Long getIdProvvedimento() {
		return idProvvedimento;
	}

	public void setIdProvvedimento(Long idProvvedimento) {
		this.idProvvedimento = idProvvedimento;
	}

	public Long getIdRiscossione() {
		return idRiscossione;
	}

	public void setIdRiscossione(Long idRiscossione) {
		this.idRiscossione = idRiscossione;
	}
	
	public TipiTitoloExtendedDTO getTipoTitoloExtendedDTO() {
		return tipoTitoloExtendedDTO;
	}

	public void setTipoTitoloExtendedDTO(TipiTitoloExtendedDTO tipoTitoloExtendedDTO) {
		this.tipoTitoloExtendedDTO = tipoTitoloExtendedDTO;
	}

	public TipiProvvedimentoExtendedDTO getTipiProvvedimentoExtendedDTO() {
		return tipiProvvedimentoExtendedDTO;
	}

	public void setTipiProvvedimentoExtendedDTO(TipiProvvedimentoExtendedDTO tipiProvvedimentoExtendedDTO) {
		this.tipiProvvedimentoExtendedDTO = tipiProvvedimentoExtendedDTO;
	}

	public String getNumTitolo() {
		return numTitolo;
	}

	public void setNumTitolo(String numTitolo) {
		this.numTitolo = numTitolo;
	}

	public String getDataProvvedimento() {
		return dataProvvedimento;
	}

	public void setDataProvvedimento(String dataProvvedimento) {
		this.dataProvvedimento = dataProvvedimento;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
}

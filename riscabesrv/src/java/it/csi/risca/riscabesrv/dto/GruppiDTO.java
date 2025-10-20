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

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * GruppiDTO
 *
 * @author CSI PIEMONTE
 */

public class GruppiDTO extends GestAttoreDTO {

    @Min(value = 1, message = "L' idGruppoSoggetto deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' idGruppoSoggetto supera il limite massimo consentito per Integer")
	@JsonProperty("id_gruppo_soggetto")
    private Long idGruppoSoggetto;
	
    @Size(max = 20, min = 0, message = "cod_gruppo_soggetto deve essere compreso tra 0 e 20 caratteri.")
    @JsonProperty("cod_gruppo_soggetto")
    private String codGruppoSoggetto;
    
    @Size(max = 20, min = 0, message = "cod_gruppo_fonte deve essere compreso tra 0 e 20 caratteri.")
    @JsonProperty("cod_gruppo_fonte")
    private String codGruppoFonte;
    
    @Size(max = 100, min = 0, message = "des_gruppo_soggetto deve essere compreso tra 0 e 100 caratteri. ")
    @JsonProperty("des_gruppo_soggetto")
    private String desGruppoSoggetto;
	
    @Min(value = 1, message = "L' id_fonte deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_fonte supera il limite massimo consentito per Integer")
	@JsonProperty("id_fonte")
    private Long idFonte;
	
    @Min(value = 1, message = "L' id_fonte_origine deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_fonte_origine supera il limite massimo consentito per Integer")
	@JsonProperty("id_fonte_origine")
    private Long idFonteOrigine;

    @JsonIgnore
    private String dataAggiornamento;

    @JsonIgnore
    private String dataCancellazione;

	@JsonProperty("componenti_gruppo")
    private List<GruppiSoggettoDTO> componentiGruppo;

	@JsonIgnore
	private int numeroComponentiGruppo;
	
	public Long getIdGruppoSoggetto() {
		return idGruppoSoggetto;
	}

	public void setIdGruppoSoggetto(Long idGruppoSoggetto) {
		this.idGruppoSoggetto = idGruppoSoggetto;
	}

	public String getCodGruppoSoggetto() {
		return codGruppoSoggetto;
	}

	public void setCodGruppoSoggetto(String codGruppoSoggetto) {
		this.codGruppoSoggetto = codGruppoSoggetto;
	}

	public String getCodGruppoFonte() {
		return codGruppoFonte;
	}

	public void setCodGruppoFonte(String codGruppoFonte) {
		this.codGruppoFonte = codGruppoFonte;
	}

	public String getDesGruppoSoggetto() {
		return desGruppoSoggetto;
	}

	public void setDesGruppoSoggetto(String desGruppoSoggetto) {
		this.desGruppoSoggetto = desGruppoSoggetto;
	}

	public Long getIdFonte() {
		return idFonte;
	}

	public void setIdFonte(Long idFonte) {
		this.idFonte = idFonte;
	}

	public Long getIdFonteOrigine() {
		return idFonteOrigine;
	}

	public void setIdFonteOrigine(Long idFonteOrigine) {
		this.idFonteOrigine = idFonteOrigine;
	}

	public String getDataAggiornamento() {
		return dataAggiornamento;
	}

	public void setDataAggiornamento(String dataAggiornamento) {
		this.dataAggiornamento = dataAggiornamento;
	}

	public String getDataCancellazione() {
		return dataCancellazione;
	}

	public void setDataCancellazione(String dataCancellazione) {
		this.dataCancellazione = dataCancellazione;
	}

	public List<GruppiSoggettoDTO> getComponentiGruppo() {
		return componentiGruppo;
	}

	public void setComponentiGruppo(List<GruppiSoggettoDTO> componentiGruppo) {
		this.componentiGruppo = componentiGruppo;
	}

	public int getNumeroComponentiGruppo() {
		return numeroComponentiGruppo;
	}

	public void setNumeroComponentiGruppo(int numeroComponentiGruppo) {
		this.numeroComponentiGruppo = numeroComponentiGruppo;
	}

}

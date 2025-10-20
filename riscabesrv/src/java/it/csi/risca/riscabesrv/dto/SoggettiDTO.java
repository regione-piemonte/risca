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

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * SoggettiDTO
 *
 * @author CSI PIEMONTE
 */

public class SoggettiDTO extends GestAttoreDTO {
	
    @Min(value = 1, message = "L' id_soggetto deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_soggetto supera il limite massimo consentito per Integer")
	@JsonProperty("id_soggetto")
    private Long idSoggetto;
	
    @Min(value = 1, message = "L' id_ambito deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_soggetto supera il limite massimo consentito per Integer")
	@JsonProperty("id_ambito")
    private Long idAmbito;
	
	@JsonIgnore
    private Long idTipoSoggetto;

    @Size(max = 16, min = 0, message = "cf_soggetto deve essere compreso tra 0 e 30 caratteri. ")
    @JsonProperty("cf_soggetto")
    private String cfSoggetto;
    
	@JsonIgnore	
    private Long idTipoNaturaGiuridica;
	
	
    @Min(value = 0, message = "L' id_fonte_origine deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_fonte_origine supera il limite massimo consentito per Integer")
	@JsonProperty("id_fonte_origine")
    private Long idFonteOrigine;
	
	@JsonIgnore
    private Long idFonte;
    
    @Size(max = 100, min = 0, message = "nome deve essere compreso tra 0 e 30 caratteri.  ")
    @JsonProperty("nome")
    private String nome;
    
    @Size(max = 100, min = 0, message = "cognome deve essere compreso tra 0 e 30 caratteri.  ")
    @JsonProperty("cognome")
    private String cognome;
    
    @Size(max = 250, min = 0, message = "den_soggetto deve essere compreso tra 0 e 30 caratteri.  ")
    @JsonProperty("den_soggetto")
    private String denSoggetto;
    
    @Size(max = 16, min = 0, message = "partita_iva_soggetto deve essere compreso tra 0 e 30 caratteri.  ")
    @JsonProperty("partita_iva_soggetto")
    private String partitaIvaSoggetto;
    
    @Pattern(regexp = "^(|\\d{4}-\\d{2}-\\d{2})$", message = "Formato data_nascita_soggetto non valido. Utilizzare yyyy-MM-dd.")
    @JsonProperty("data_nascita_soggetto")
    private String dataNascitaSoggetto;
    
    @JsonIgnore
    private Integer idComuneNascita;
    
    @JsonIgnore
    private Integer idStatoNascita;
    
    @Size(max = 100, min = 0, message = "citta_estera_nascita deve essere compreso tra 0 e 30 caratteri.  ")
    @JsonProperty("citta_estera_nascita")
    private String cittaEsteraNascita;
	
	@JsonIgnore
    private String dataAggiornamento;

	@JsonIgnore
    private String dataCancellazione;
	
	public Long getIdSoggetto() {
		return idSoggetto;
	}

	public void setIdSoggetto(Long idSoggetto) {
		this.idSoggetto = idSoggetto;
	}

	public Long getIdAmbito() {
		return idAmbito;
	}

	public void setIdAmbito(Long idAmbito) {
		this.idAmbito = idAmbito;
	}

	public Long getIdTipoSoggetto() {
		return idTipoSoggetto;
	}

	public void setIdTipoSoggetto(Long idTipoSoggetto) {
		this.idTipoSoggetto = idTipoSoggetto;
	}

	public String getCfSoggetto() {
		return cfSoggetto;
	}

	public void setCfSoggetto(String cfSoggetto) {
		this.cfSoggetto = cfSoggetto;
	}

	public Long getIdTipoNaturaGiuridica() {
		return idTipoNaturaGiuridica;
	}

	public void setIdTipoNaturaGiuridica(Long idTipoNaturaGiuridica) {
		this.idTipoNaturaGiuridica = idTipoNaturaGiuridica;
	}

	public Long getIdFonteOrigine() {
		return idFonteOrigine;
	}

	public void setIdFonteOrigine(Long idFonteOrigine) {
		this.idFonteOrigine = idFonteOrigine;
	}

	public Long getIdFonte() {
		return idFonte;
	}

	public void setIdFonte(Long idFonte) {
		this.idFonte = idFonte;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getDenSoggetto() {
		return denSoggetto;
	}

	public void setDenSoggetto(String denSoggetto) {
		this.denSoggetto = denSoggetto;
	}

	public String getPartitaIvaSoggetto() {
		return partitaIvaSoggetto;
	}

	public void setPartitaIvaSoggetto(String partitaIvaSoggetto) {
		this.partitaIvaSoggetto = partitaIvaSoggetto;
	}

	public String getDataNascitaSoggetto() {
		return dataNascitaSoggetto;
	}

	public void setDataNascitaSoggetto(String dataNascitaSoggetto) {
		this.dataNascitaSoggetto = dataNascitaSoggetto;
	}

	public Integer getIdComuneNascita() {
		return idComuneNascita;
	}

	public void setIdComuneNascita(Integer idComuneNascita) {
		this.idComuneNascita = idComuneNascita;
	}

	public Integer getIdStatoNascita() {
		return idStatoNascita;
	}

	public void setIdStatoNascita(Integer idStatoNascita) {
		this.idStatoNascita = idStatoNascita;
	}

	public String getCittaEsteraNascita() {
		return cittaEsteraNascita;
	}

	public void setCittaEsteraNascita(String cittaEsteraNascita) {
		this.cittaEsteraNascita = cittaEsteraNascita;
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


}

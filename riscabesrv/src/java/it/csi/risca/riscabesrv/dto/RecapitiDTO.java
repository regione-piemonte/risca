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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * RecapitiDTO
 *
 * @author CSI PIEMONTE
 */

public class RecapitiDTO extends GestAttoreDTO {
	
    @Min(value = 1, message = "L' id_recapito deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_recapito supera il limite massimo consentito per Integer")
	@JsonProperty("id_recapito")
    private Long idRecapito;
	
    @Min(value = 1, message = "L' id_soggetto deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_soggetto supera il limite massimo consentito per Integer")
	@JsonProperty("id_soggetto")
    private Long idSoggetto;
	
	@JsonIgnore
    private Long idTipoRecapito;

	@JsonIgnore
    private Long idTipoInvio;
    
	@JsonIgnore
    private Long idComuneRecapito;
	
	@JsonIgnore
    private Long idNazioneRecapito;
	
	@JsonIgnore
    private Long idFonte;
	
    @Min(value = 1, message = "L' id_fonte_origine deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_fonte_origine supera il limite massimo consentito per Integer")
	@JsonProperty("id_fonte_origine")
    private Long idFonteOrigine;
    
    @Size(max = 20, min = 0, message = "cod_recapito deve essere compreso tra 0 e 30 caratteri.")
    @JsonProperty("cod_recapito")
    private String codRecapito;
    
    @Size(max = 20, min = 0, message = "cod_recapito_fonte deve essere compreso tra 0 e 30 caratteri.")
    @JsonProperty("cod_recapito_fonte")
    private String codRecapitoFonte;
    
    @Size(max = 100, min = 0, message = "indirizzo deve essere compreso tra 0 e 30 caratteri.")
    @JsonProperty("indirizzo")
    private String indirizzo;
    
    @Size(max = 30, min = 0, message = "num_civico deve essere compreso tra 0 e 30 caratteri.")
    @JsonProperty("num_civico")
    private String numCivico;
    
    @Size(max = 300, min = 0, message = "email deve essere compreso tra 0 e 30 caratteri.")
    @JsonProperty("email")
    private String email;
   
    @Size(max = 150, min = 0, message = "pec deve essere compreso tra 0 e 30 caratteri.")
    @JsonProperty("pec")
    private String pec;

    @Size(max = 50, min = 0, message = "telefono deve essere compreso tra 0 e 30 caratteri.")
    @JsonProperty("telefono")
    private String telefono;
    
    @Size(max = 150, min = 0, message = "presso deve essere compreso tra 0 e 30 caratteri.")
    @JsonProperty("presso")
    private String presso;
    
    @Size(max = 100, min = 0, message = "citta_estera_recapito deve essere compreso tra 0 e 30 caratteri.")
    @JsonProperty("citta_estera_recapito")
    private String cittaEsteraRecapito;
    
    @Size(max = 10, min = 0, message = "cap_recapito deve essere compreso tra 0 e 30 caratteri.")
    @JsonProperty("cap_recapito")
    private String capRecapito;
    
    @Size(max = 250, min = 0, message = "des_localita deve essere compreso tra 0 e 30 caratteri.")
    @JsonProperty("des_localita")
    private String desLocalita;
	
    @Size(max = 50, min = 0, message = "cellulare deve essere compreso tra 0 e 30 caratteri.")
    @JsonProperty("cellulare")
    private String cellulare;
    
    @JsonIgnore
    private Long idTipoSede;
	
    @JsonIgnore
    private String dataAggiornamento;

    @JsonIgnore
    private String dataCancellazione;

	public Long getIdRecapito() {
		return idRecapito;
	}

	public void setIdRecapito(Long idRecapito) {
		this.idRecapito = idRecapito;
	}

	public Long getIdSoggetto() {
		return idSoggetto;
	}

	public void setIdSoggetto(Long idSoggetto) {
		this.idSoggetto = idSoggetto;
	}

	public Long getIdTipoRecapito() {
		return idTipoRecapito;
	}

	public void setIdTipoRecapito(Long idTipoRecapito) {
		this.idTipoRecapito = idTipoRecapito;
	}

	public Long getIdTipoInvio() {
		return idTipoInvio;
	}

	public void setIdTipoInvio(Long idTipoInvio) {
		this.idTipoInvio = idTipoInvio;
	}

	public Long getIdComuneRecapito() {
		return idComuneRecapito;
	}

	public void setIdComuneRecapito(Long idComuneRecapito) {
		this.idComuneRecapito = idComuneRecapito;
	}

	public Long getIdNazioneRecapito() {
		return idNazioneRecapito;
	}

	public void setIdNazioneRecapito(Long idNazioneRecapito) {
		this.idNazioneRecapito = idNazioneRecapito;
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

	public String getCodRecapito() {
		return codRecapito;
	}

	public void setCodRecapito(String codRecapito) {
		this.codRecapito = codRecapito;
	}

	public String getCodRecapitoFonte() {
		return codRecapitoFonte;
	}

	public void setCodRecapitoFonte(String codRecapitoFonte) {
		this.codRecapitoFonte = codRecapitoFonte;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public String getNumCivico() {
		return numCivico;
	}

	public void setNumCivico(String numCivico) {
		this.numCivico = numCivico;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPec() {
		return pec;
	}

	public void setPec(String pec) {
		this.pec = pec;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getPresso() {
		return presso;
	}

	public void setPresso(String presso) {
		this.presso = presso;
	}

	public String getCittaEsteraRecapito() {
		return cittaEsteraRecapito;
	}

	public void setCittaEsteraRecapito(String cittaEsteraRecapito) {
		this.cittaEsteraRecapito = cittaEsteraRecapito;
	}

	public String getCapRecapito() {
		return capRecapito;
	}

	public void setCapRecapito(String capRecapito) {
		this.capRecapito = capRecapito;
	}

	public String getDesLocalita() {
		return desLocalita;
	}

	public void setDesLocalita(String desLocalita) {
		this.desLocalita = desLocalita;
	}


	public String getCellulare() {
		return cellulare;
	}

	public void setCellulare(String cellulare) {
		this.cellulare = cellulare;
	}

	public Long getIdTipoSede() {
		return idTipoSede;
	}

	public void setIdTipoSede(Long idTipoSede) {
		this.idTipoSede = idTipoSede;
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

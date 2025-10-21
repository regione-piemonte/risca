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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * RecapitiDTO
 *
 * @author CSI PIEMONTE
 */

public class RecapitiDTO {
	
	@JsonProperty("id_recapito")
    private Long idRecapito;
	
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
	
	@JsonProperty("id_fonte_origine")
    private Long idFonteOrigine;
    
    @JsonProperty("cod_recapito")
    private String codRecapito;
    
    @JsonProperty("cod_recapito_fonte")
    private String codRecapitoFonte;
    
    @JsonProperty("indirizzo")
    private String indirizzo;
    
    @JsonProperty("num_civico")
    private String numCivico;
    
    @JsonProperty("email")
    private String email;
    
    @JsonProperty("pec")
    private String pec;
    
    @JsonProperty("telefono")
    private String telefono;
    
    @JsonProperty("presso")
    private String presso;
    
    @JsonProperty("citta_estera_recapito")
    private String cittaEsteraRecapito;
    
    @JsonProperty("cap_recapito")
    private String capRecapito;
    
    @JsonProperty("des_localita")
    private String desLocalita;
  
	
    @JsonProperty("cellulare")
    private String cellulare;
    
    @JsonIgnore
    private Long idTipoSede;
	


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

	

}

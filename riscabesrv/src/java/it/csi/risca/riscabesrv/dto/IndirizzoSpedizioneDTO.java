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
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * IndirizzoSpedizioneDTO
 *
 * @author CSI PIEMONTE
 */

public class IndirizzoSpedizioneDTO extends GestAttoreDTO {
	
    @Min(value = 1, message = "L' id_recapito deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_recapito supera il limite massimo consentito per Integer")
	@JsonProperty("id_recapito")
    private Long idRecapito;
	
    @Min(value = 1, message = "L' id_recapito_postel deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_recapito_postel supera il limite massimo consentito per Integer")
	@JsonProperty("id_recapito_postel")
    private Long idRecapitoPostel;
	
    @Min(value = 1, message = "L' id_gruppo_soggetto deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_gruppo_soggetto supera il limite massimo consentito per Integer")
	@JsonProperty("id_gruppo_soggetto")
    private Long idGruppoSoggetto;
    
    @Size(max = 200, min = 0, message = "destinatario_postel deve essere compreso tra 0 e 200 caratteri. ")
    @JsonProperty("destinatario_postel")
    private String destinatarioPostel;
    
    @Size(max = 150, min = 0, message = "presso_postel deve essere compreso tra 0 e 150 caratteri. ")
    @JsonProperty("presso_postel")
    private String pressoPostel;
    
    @Size(max = 130, min = 0, message = "indirizzo_postel deve essere compreso tra 0 e 130 caratteri. ")
    @JsonProperty("indirizzo_postel")
    private String indirizzoPostel;
    
    @Size(max = 100, min = 0, message = "citta_postel deve essere compreso tra 0 e 100 caratteri. ")
    @JsonProperty("citta_postel")
    private String cittaPostel;
    
    @Size(max = 2, min = 0, message = "provincia_postel deve essere compreso tra 0 e 2 caratteri. ")
    @JsonProperty("provincia_postel")
    private String provinciaPostel;
    
    @Size(max = 10, min = 0, message = "cap_postel deve essere compreso tra 0 e 10 caratteri. ")
    @JsonProperty("cap_postel")
    private String capPostel;
    
    @Size(max = 250, min = 0, message = "frazione_postel deve essere compreso tra 0 e 250 caratteri. ")
    @JsonProperty("frazione_postel")
    private String frazionePostel;
    
    @Size(max = 100, min = 0, message = "nazione_postel deve essere compreso tra 0 e 100 caratteri. ")
    @JsonProperty("nazione_postel")
    private String nazionePostel;
    
    @Min(value = 1, message = "L' ind_valido_postel deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' ind_valido_postel supera il limite massimo consentito per Integer")
    @JsonProperty("ind_valido_postel")
    private Long indValidoPostel;
    

	
	@JsonIgnore
    private Long idTipoSoggetto;
	
	@JsonIgnore
    private String nome;
    
	@JsonIgnore
    private String cognome;
    
	@JsonIgnore
    private String denSoggetto;
	
	public Long getIdRecapito() {
		return idRecapito;
	}

	public void setIdRecapito(Long idRecapito) {
		this.idRecapito = idRecapito;
	}

	public Long getIdRecapitoPostel() {
		return idRecapitoPostel;
	}

	public void setIdRecapitoPostel(Long idRecapitoPostel) {
		this.idRecapitoPostel = idRecapitoPostel;
	}

	public Long getIdGruppoSoggetto() {
		return idGruppoSoggetto;
	}

	public void setIdGruppoSoggetto(Long idGruppoSoggetto) {
		this.idGruppoSoggetto = idGruppoSoggetto;
	}

	public String getDestinatarioPostel() {
		return destinatarioPostel;
	}

	public void setDestinatarioPostel(String destinatarioPostel) {
		this.destinatarioPostel = destinatarioPostel;
	}

	public String getPressoPostel() {
		return pressoPostel;
	}

	public void setPressoPostel(String pressoPostel) {
		this.pressoPostel = pressoPostel;
	}

	public String getIndirizzoPostel() {
		return indirizzoPostel;
	}

	public void setIndirizzoPostel(String indirizzoPostel) {
		this.indirizzoPostel = indirizzoPostel;
	}

	public String getCittaPostel() {
		return cittaPostel;
	}

	public void setCittaPostel(String cittaPostel) {
		this.cittaPostel = cittaPostel;
	}

	public String getProvinciaPostel() {
		return provinciaPostel;
	}

	public void setProvinciaPostel(String provinciaPostel) {
		this.provinciaPostel = provinciaPostel;
	}

	public String getCapPostel() {
		return capPostel;
	}

	public void setCapPostel(String capPostel) {
		this.capPostel = capPostel;
	}

	public String getFrazionePostel() {
		return frazionePostel;
	}

	public void setFrazionePostel(String frazionePostel) {
		this.frazionePostel = frazionePostel;
	}

	public String getNazionePostel() {
		return nazionePostel;
	}

	public void setNazionePostel(String nazionePostel) {
		this.nazionePostel = nazionePostel;
	}

	public Long getIndValidoPostel() {
		return indValidoPostel;
	}

	public void setIndValidoPostel(Long indValidoPostel) {
		this.indValidoPostel = indValidoPostel;
	}

	
	public Long getIdTipoSoggetto() {
		return idTipoSoggetto;
	}

	public void setIdTipoSoggetto(Long idTipoSoggetto) {
		this.idTipoSoggetto = idTipoSoggetto;
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

}

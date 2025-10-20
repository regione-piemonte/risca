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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SollDatiTitolareDTO extends GestAttoreDTO {

	@JsonProperty("id_soll_dati_titolare")
	private Long idSollDatiTitolare;

	@JsonProperty("id_accertamento")
	private Long idAccertamento;

	@JsonProperty("id_tipo_invio")
	private Long idTipoInvio;

	@JsonProperty("nome_titolare_ind_post")
	private String nomeTitolareIndPost;

	@JsonProperty("codice_fiscale_calc")
	private String codiceFiscaleCalc;

	@JsonProperty("codice_fiscale_eti_calc")
	private String codiceFiscaleEtiCalc;

	@JsonProperty("prov_ind_post")
	private String provIndPost;

	@JsonProperty("presso_ind_post")
	private String pressoIndPost;

	@JsonProperty("indirizzo_ind_post")
	private String indirizzoIndPost;

	@JsonProperty("comune_ind_post")
	private String comuneIndPost;

	@JsonProperty("num_prot")
	private String numProt;

	@JsonProperty("data_prot")
	private Date dataProt;

	@JsonProperty("scadenza_soll")
	private String scadenzaSoll;

	@JsonProperty("pec_email")
	private String pecEmail;

	@JsonProperty("id_titolare")
	private String idTitolare;

	@JsonProperty("id_soggetto")
	private Long idSoggetto;
	
	@JsonIgnore
	private String codBelfioreComune;
	
	@JsonIgnore
	private String capComune;
	
	@JsonIgnore
	private String denSoggetto;
	
	@JsonIgnore
	private String nome;
	
	@JsonIgnore
	private String cognome;
	
	@JsonIgnore
	private String dataNascita;
	
	@JsonIgnore
	private String partitaIva;
	
	public String getDenSoggetto() {
		return denSoggetto;
	}

	public void setDenSoggetto(String denSoggetto) {
		this.denSoggetto = denSoggetto;
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

	

	public String getDataNascita() {
		return dataNascita;
	}

	public void setDataNascita(String dataNascita) {
		this.dataNascita = dataNascita;
	}

	public String getPartitaIva() {
		return partitaIva;
	}

	public void setPartitaIva(String partitaIva) {
		this.partitaIva = partitaIva;
	}

	public Long getIdTipoSoggetto() {
		return idTipoSoggetto;
	}

	public void setIdTipoSoggetto(Long idTipoSoggetto) {
		this.idTipoSoggetto = idTipoSoggetto;
	}

	@JsonIgnore
	private Long idTipoSoggetto;

	public String getCapComune() {
		return capComune;
	}

	public void setCapComune(String capComune) {
		this.capComune = capComune;
	}

	public String getCodBelfioreComune() {
		return codBelfioreComune;
	}

	public void setCodBelfioreComune(String codBelfioreComune) {
		this.codBelfioreComune = codBelfioreComune;
	}

	public Long getIdSollDatiTitolare() {
		return idSollDatiTitolare;
	}

	public void setIdSollDatiTitolare(Long idSollDatiTitolare) {
		this.idSollDatiTitolare = idSollDatiTitolare;
	}

	public Long getIdAccertamento() {
		return idAccertamento;
	}

	public void setIdAccertamento(Long idAccertamento) {
		this.idAccertamento = idAccertamento;
	}

	public Long getIdTipoInvio() {
		return idTipoInvio;
	}

	public void setIdTipoInvio(Long idTipoInvio) {
		this.idTipoInvio = idTipoInvio;
	}

	public String getNomeTitolareIndPost() {
		return nomeTitolareIndPost;
	}

	public void setNomeTitolareIndPost(String nomeTitolareIndPost) {
		this.nomeTitolareIndPost = nomeTitolareIndPost;
	}

	public String getCodiceFiscaleCalc() {
		return codiceFiscaleCalc;
	}

	public void setCodiceFiscaleCalc(String codiceFiscaleCalc) {
		this.codiceFiscaleCalc = codiceFiscaleCalc;
	}

	public String getCodiceFiscaleEtiCalc() {
		return codiceFiscaleEtiCalc;
	}

	public void setCodiceFiscaleEtiCalc(String codiceFiscaleEtiCalc) {
		this.codiceFiscaleEtiCalc = codiceFiscaleEtiCalc;
	}

	public String getProvIndPost() {
		return provIndPost;
	}

	public void setProvIndPost(String provIndPost) {
		this.provIndPost = provIndPost;
	}

	public String getPressoIndPost() {
		return pressoIndPost;
	}

	public void setPressoIndPost(String pressoIndPost) {
		this.pressoIndPost = pressoIndPost;
	}

	public String getIndirizzoIndPost() {
		return indirizzoIndPost;
	}

	public void setIndirizzoIndPost(String indirizzoIndPost) {
		this.indirizzoIndPost = indirizzoIndPost;
	}

	public String getComuneIndPost() {
		return comuneIndPost;
	}

	public void setComuneIndPost(String comuneIndPost) {
		this.comuneIndPost = comuneIndPost;
	}

	public String getNumProt() {
		return numProt;
	}

	public void setNumProt(String numProt) {
		this.numProt = numProt;
	}

	public Date getDataProt() {
		return dataProt;
	}

	public void setDataProt(Date dataProt) {
		this.dataProt = dataProt;
	}

	public String getScadenzaSoll() {
		return scadenzaSoll;
	}

	public void setScadenzaSoll(String scadenzaSoll) {
		this.scadenzaSoll = scadenzaSoll;
	}

	public String getPecEmail() {
		return pecEmail;
	}

	public void setPecEmail(String pecEmail) {
		this.pecEmail = pecEmail;
	}

	public String getIdTitolare() {
		return idTitolare;
	}

	public void setIdTitolare(String idTitolare) {
		this.idTitolare = idTitolare;
	}

	public Long getIdSoggetto() {
		return idSoggetto;
	}

	public void setIdSoggetto(Long idSoggetto) {
		this.idSoggetto = idSoggetto;
	}

}

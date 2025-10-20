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

import com.fasterxml.jackson.annotation.JsonProperty;

public class SollDestinatariDTO {

	@JsonProperty("id_soll_destinatari")
	private Long idSollDestinatari;

	@JsonProperty("id_accertamento")
	private Long idAccertamento;

	@JsonProperty("codice_utenza")
	private String codiceUtenza;

	@JsonProperty("scadenza_pagamento")
	private String scadenzaPagamento;

	@JsonProperty("codice_utente_tit")
	private String codiceUtenteTit;

	@JsonProperty("codice_utente_dest")
	private String codiceUtenteDest;

	@JsonProperty("rag_soc_cogn")
	private String ragSocCogn;

	@JsonProperty("codice_fiscale_calc")
	private String codiceFiscaleCalc;

	@JsonProperty("codice_fiscale_eti_calc")
	private String codiceFiscaleEtiCalc;

	@JsonProperty("presso_ind_post")
	private String pressoIndPost;

	@JsonProperty("indirizzo_ind_post")
	private String indirizzoIndPost;

	@JsonProperty("comune_ind_post")
	private String comuneIndPost;

	@JsonProperty("prov_ind_post")
	private String provIndPost;

	@JsonProperty("num_prot")
	private String numProt;

	@JsonProperty("data_prot")
	private Date dataProt;

	@JsonProperty("scadenza_soll")
	private String scadenzaSoll;

	@JsonProperty("rag_soc_cogn_ru")
	private String ragSocCognRu;

	@JsonProperty("nome_ru")
	private String nomeRu;

	@JsonProperty("sesso_ru")
	private String sessoRu;

	@JsonProperty("data_nascita_ru")
	private Date dataNascitaRu;

	@JsonProperty("luogo_nascita_ru")
	private String luogoNascitaRu;

	@JsonProperty("prov_nascita_ru")
	private String provNascitaRu;

	@JsonProperty("cap_ru")
	private String capRu;

	@JsonProperty("comune_ru")
	private String comuneRu;

	@JsonProperty("prov_ru")
	private String provRu;

	@JsonProperty("indirizzo_civ_ru")
	private String indirizzoCivRu;

	public Long getIdSollDestinatari() {
		return idSollDestinatari;
	}

	public void setIdSollDestinatari(Long idSollDestinatari) {
		this.idSollDestinatari = idSollDestinatari;
	}

	public Long getIdAccertamento() {
		return idAccertamento;
	}

	public void setIdAccertamento(Long idAccertamento) {
		this.idAccertamento = idAccertamento;
	}

	public String getCodiceUtenza() {
		return codiceUtenza;
	}

	public void setCodiceUtenza(String codiceUtenza) {
		this.codiceUtenza = codiceUtenza;
	}

	public String getScadenzaPagamento() {
		return scadenzaPagamento;
	}

	public void setScadenzaPagamento(String scadenzaPagamento) {
		this.scadenzaPagamento = scadenzaPagamento;
	}

	public String getCodiceUtenteTit() {
		return codiceUtenteTit;
	}

	public void setCodiceUtenteTit(String codiceUtenteTit) {
		this.codiceUtenteTit = codiceUtenteTit;
	}

	public String getCodiceUtenteDest() {
		return codiceUtenteDest;
	}

	public void setCodiceUtenteDest(String codiceUtenteDest) {
		this.codiceUtenteDest = codiceUtenteDest;
	}

	public String getRagSocCogn() {
		return ragSocCogn;
	}

	public void setRagSocCogn(String ragSocCogn) {
		this.ragSocCogn = ragSocCogn;
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

	public String getProvIndPost() {
		return provIndPost;
	}

	public void setProvIndPost(String provIndPost) {
		this.provIndPost = provIndPost;
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

	public String getRagSocCognRu() {
		return ragSocCognRu;
	}

	public void setRagSocCognRu(String ragSocCognRu) {
		this.ragSocCognRu = ragSocCognRu;
	}

	public String getNomeRu() {
		return nomeRu;
	}

	public void setNomeRu(String nomeRu) {
		this.nomeRu = nomeRu;
	}

	public String getSessoRu() {
		return sessoRu;
	}

	public void setSessoRu(String sessoRu) {
		this.sessoRu = sessoRu;
	}

	public Date getDataNascitaRu() {
		return dataNascitaRu;
	}

	public void setDataNascitaRu(Date dataNascitaRu) {
		this.dataNascitaRu = dataNascitaRu;
	}

	public String getLuogoNascitaRu() {
		return luogoNascitaRu;
	}

	public void setLuogoNascitaRu(String luogoNascitaRu) {
		this.luogoNascitaRu = luogoNascitaRu;
	}

	public String getProvNascitaRu() {
		return provNascitaRu;
	}

	public void setProvNascitaRu(String provNascitaRu) {
		this.provNascitaRu = provNascitaRu;
	}

	public String getCapRu() {
		return capRu;
	}

	public void setCapRu(String capRu) {
		this.capRu = capRu;
	}

	public String getComuneRu() {
		return comuneRu;
	}

	public void setComuneRu(String comuneRu) {
		this.comuneRu = comuneRu;
	}

	public String getProvRu() {
		return provRu;
	}

	public void setProvRu(String provRu) {
		this.provRu = provRu;
	}

	public String getIndirizzoCivRu() {
		return indirizzoCivRu;
	}

	public void setIndirizzoCivRu(String indirizzoCivRu) {
		this.indirizzoCivRu = indirizzoCivRu;
	}

}

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

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SollDatiPagopaDTO {

	@JsonProperty("id_soll_dati_pagopa")
	private Long idSollDatiPagopa;

	@JsonProperty("id_accertamento")
	private Long idAccertamento;

	@JsonProperty("codice_utente")
	private String codiceUtente;

	@JsonProperty("codice_utenza")
	private String codiceUtenza;

	@JsonProperty("iuv")
	private String iuv;

	@JsonProperty("codice_avviso")
	private String codiceAvviso;

	@JsonProperty("scadenza_pagamento")
	private String scadenzaPagamento;

	@JsonProperty("importo_da_versare")
	private BigDecimal importoDaVersare;

	@JsonProperty("nap")
	private String nap;

	@JsonProperty("nome_titolare_ind_post")
	private String nomeTitolareIndPost;

	@JsonProperty("presso_ind_post")
	private String pressoIndPost;

	@JsonProperty("indirizzo_ind_post")
	private String indirizzoIndPost;

	@JsonProperty("cap_ind_post")
	private String capIndPost;

	@JsonProperty("comune_ind_post")
	private String comuneIndPost;

	@JsonProperty("prov_ind_post")
	private String provIndPost;

	@JsonProperty("pec_email")
	private String pecEmail;

	public Long getIdSollDatiPagopa() {
		return idSollDatiPagopa;
	}

	public void setIdSollDatiPagopa(Long idSollDatiPagopa) {
		this.idSollDatiPagopa = idSollDatiPagopa;
	}

	public Long getIdAccertamento() {
		return idAccertamento;
	}

	public void setIdAccertamento(Long idAccertamento) {
		this.idAccertamento = idAccertamento;
	}

	public String getCodiceUtente() {
		return codiceUtente;
	}

	public void setCodiceUtente(String codiceUtente) {
		this.codiceUtente = codiceUtente;
	}

	public String getCodiceUtenza() {
		return codiceUtenza;
	}

	public void setCodiceUtenza(String codiceUtenza) {
		this.codiceUtenza = codiceUtenza;
	}

	public String getIuv() {
		return iuv;
	}

	public void setIuv(String iuv) {
		this.iuv = iuv;
	}

	public String getCodiceAvviso() {
		return codiceAvviso;
	}

	public void setCodiceAvviso(String codiceAvviso) {
		this.codiceAvviso = codiceAvviso;
	}

	public String getScadenzaPagamento() {
		return scadenzaPagamento;
	}

	public void setScadenzaPagamento(String scadenzaPagamento) {
		this.scadenzaPagamento = scadenzaPagamento;
	}

	public BigDecimal getImportoDaVersare() {
		return importoDaVersare;
	}

	public void setImportoDaVersare(BigDecimal importoDaVersare) {
		this.importoDaVersare = importoDaVersare;
	}

	public String getNap() {
		return nap;
	}

	public void setNap(String nap) {
		this.nap = nap;
	}

	public String getNomeTitolareIndPost() {
		return nomeTitolareIndPost;
	}

	public void setNomeTitolareIndPost(String nomeTitolareIndPost) {
		this.nomeTitolareIndPost = nomeTitolareIndPost;
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

	public String getCapIndPost() {
		return capIndPost;
	}

	public void setCapIndPost(String capIndPost) {
		this.capIndPost = capIndPost;
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

	public String getPecEmail() {
		return pecEmail;
	}

	public void setPecEmail(String pecEmail) {
		this.pecEmail = pecEmail;
	}

}

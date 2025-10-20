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
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SollDatiAmministrDTO {

	@JsonProperty("id_soll_dati_amministr")
	private Long idSollDatiAmministr;

	@JsonProperty("id_accertamento")
	private Long idAccertamento;

	@JsonProperty("codice_utente")
	private String codiceUtente;

	@JsonProperty("codice_utenza")
	private String codiceUtenza;

	@JsonProperty("tipo_sollecito")
	private String tipoSollecito;

	@JsonProperty("tipo_titolo")
	private String tipoTitolo;

	@JsonProperty("num_titolo")
	private String numTitolo;

	@JsonProperty("data_titolo")
	private Date dataTitolo;

	@JsonProperty("corpo_idrico")
	private String corpoIdrico;

	@JsonProperty("comune_di_presa")
	private String comuneDiPresa;

	@JsonProperty("annualita_pagamento")
	private Integer annualitaPagamento;

	@JsonProperty("anno_rich_pagamento")
	private String annoRichPagamento;

	@JsonProperty("scadenza_pagamento")
	private String scadenzaPagamento;

	@JsonProperty("motivo_soll")
	private String motivoSoll;

	@JsonProperty("canone_dovuto")
	private BigDecimal canoneDovuto;

	@JsonProperty("importo_versato")
	private BigDecimal importoVersato;

	@JsonProperty("importo_mancante")
	private BigDecimal importoMancante;

	@JsonProperty("interessi_mancanti")
	private BigDecimal interessiMancanti;

	@JsonProperty("spese_notifica")
	private BigDecimal speseNotifica;

	@JsonProperty("interessi_teorici")
	private BigDecimal interessiTeorici;

	@JsonProperty("interessi_versati")
	private BigDecimal interessiVersati;

	@JsonProperty("dilazione")
	private String dilazione;

	@JsonProperty("codice_avviso")
	private String codiceAvviso;

	public Long getIdSollDatiAmministr() {
		return idSollDatiAmministr;
	}

	public void setIdSollDatiAmministr(Long idSollDatiAmministr) {
		this.idSollDatiAmministr = idSollDatiAmministr;
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

	public String getTipoSollecito() {
		return tipoSollecito;
	}

	public void setTipoSollecito(String tipoSollecito) {
		this.tipoSollecito = tipoSollecito;
	}

	public String getTipoTitolo() {
		return tipoTitolo;
	}

	public void setTipoTitolo(String tipoTitolo) {
		this.tipoTitolo = tipoTitolo;
	}

	public String getNumTitolo() {
		return numTitolo;
	}

	public void setNumTitolo(String numTitolo) {
		this.numTitolo = numTitolo;
	}

	public Date getDataTitolo() {
		return dataTitolo;
	}

	public void setDataTitolo(Date dataTitolo) {
		this.dataTitolo = dataTitolo;
	}

	public String getCorpoIdrico() {
		return corpoIdrico;
	}

	public void setCorpoIdrico(String corpoIdrico) {
		this.corpoIdrico = corpoIdrico;
	}

	public String getComuneDiPresa() {
		return comuneDiPresa;
	}

	public void setComuneDiPresa(String comuneDiPresa) {
		this.comuneDiPresa = comuneDiPresa;
	}

	public Integer getAnnualitaPagamento() {
		return annualitaPagamento;
	}

	public void setAnnualitaPagamento(Integer annualitaPagamento) {
		this.annualitaPagamento = annualitaPagamento;
	}

	public String getAnnoRichPagamento() {
		return annoRichPagamento;
	}

	public void setAnnoRichPagamento(String annoRichPagamento) {
		this.annoRichPagamento = annoRichPagamento;
	}

	public String getScadenzaPagamento() {
		return scadenzaPagamento;
	}

	public void setScadenzaPagamento(String scadenzaPagamento) {
		this.scadenzaPagamento = scadenzaPagamento;
	}

	public String getMotivoSoll() {
		return motivoSoll;
	}

	public void setMotivoSoll(String motivoSoll) {
		this.motivoSoll = motivoSoll;
	}

	public BigDecimal getCanoneDovuto() {
		return canoneDovuto;
	}

	public void setCanoneDovuto(BigDecimal canoneDovuto) {
		this.canoneDovuto = canoneDovuto;
	}

	public BigDecimal getImportoVersato() {
		return importoVersato;
	}

	public void setImportoVersato(BigDecimal importoVersato) {
		this.importoVersato = importoVersato;
	}

	public BigDecimal getImportoMancante() {
		return importoMancante;
	}

	public void setImportoMancante(BigDecimal importoMancante) {
		this.importoMancante = importoMancante;
	}

	public BigDecimal getInteressiMancanti() {
		return interessiMancanti;
	}

	public void setInteressiMancanti(BigDecimal interessiMancanti) {
		this.interessiMancanti = interessiMancanti;
	}

	public BigDecimal getSpeseNotifica() {
		return speseNotifica;
	}

	public void setSpeseNotifica(BigDecimal speseNotifica) {
		this.speseNotifica = speseNotifica;
	}

	public BigDecimal getInteressiTeorici() {
		return interessiTeorici;
	}

	public void setInteressiTeorici(BigDecimal interessiTeorici) {
		this.interessiTeorici = interessiTeorici;
	}

	public BigDecimal getInteressiVersati() {
		return interessiVersati;
	}

	public void setInteressiVersati(BigDecimal interessiVersati) {
		this.interessiVersati = interessiVersati;
	}

	public String getDilazione() {
		return dilazione;
	}

	public void setDilazione(String dilazione) {
		this.dilazione = dilazione;
	}

	public String getCodiceAvviso() {
		return codiceAvviso;
	}

	public void setCodiceAvviso(String codiceAvviso) {
		this.codiceAvviso = codiceAvviso;
	}

}

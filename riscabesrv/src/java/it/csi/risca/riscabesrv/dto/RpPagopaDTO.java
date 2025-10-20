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

public class RpPagopaDTO {

	@JsonProperty("id_elabora")
	private Long idElabora;

	@JsonProperty("posiz_debitoria")
	private String posizDebitoria;

	@JsonProperty("anno")
	private int anno;

	@JsonProperty("iuv")
	private String iuv;

	@JsonProperty("importo_pagato")
	private BigDecimal importoPagato;

	@JsonProperty("data_scadenza")
	private Date dataScadenza;

	@JsonProperty("causale")
	private String causale;

	@JsonProperty("data_esito_pagamento")
	private Date dataEsitoPagamento;

	@JsonProperty("cogn_rsoc_debitore")
	private String cognRsocDebitore;

	@JsonProperty("nome_debitore")
	private String nomeDebitore;

	@JsonProperty("cf_pi_debitore")
	private String cfPiDebitore;

	@JsonProperty("cogn_rsoc_versante")
	private String cognRsocVersante;

	@JsonProperty("nome_versante")
	private String nomeVersante;

	@JsonProperty("cf_pi_versante")
	private String cfPiVersante;

	@JsonProperty("importo_transitato")
	private BigDecimal importoTransitato;

	@JsonProperty("importo_commissioni")
	private BigDecimal importoCommissioni;

	@JsonProperty("codice_avviso")
	private String codiceAvviso;

	@JsonProperty("note")
	private String note;

	public Long getIdElabora() {
		return idElabora;
	}

	public void setIdElabora(Long idElabora) {
		this.idElabora = idElabora;
	}

	public String getPosizDebitoria() {
		return posizDebitoria;
	}

	public void setPosizDebitoria(String posizDebitoria) {
		this.posizDebitoria = posizDebitoria;
	}

	public int getAnno() {
		return anno;
	}

	public void setAnno(int anno) {
		this.anno = anno;
	}

	public String getIuv() {
		return iuv;
	}

	public void setIuv(String iuv) {
		this.iuv = iuv;
	}

	public BigDecimal getImportoPagato() {
		return importoPagato;
	}

	public void setImportoPagato(BigDecimal importoPagato) {
		this.importoPagato = importoPagato;
	}

	public Date getDataScadenza() {
		return dataScadenza;
	}

	public void setDataScadenza(Date dataScadenza) {
		this.dataScadenza = dataScadenza;
	}

	public String getCausale() {
		return causale;
	}

	public void setCausale(String causale) {
		this.causale = causale;
	}

	public Date getDataEsitoPagamento() {
		return dataEsitoPagamento;
	}

	public void setDataEsitoPagamento(Date dataEsitoPagamento) {
		this.dataEsitoPagamento = dataEsitoPagamento;
	}

	public String getCognRsocDebitore() {
		return cognRsocDebitore;
	}

	public void setCognRsocDebitore(String cognRsocDebitore) {
		this.cognRsocDebitore = cognRsocDebitore;
	}

	public String getNomeDebitore() {
		return nomeDebitore;
	}

	public void setNomeDebitore(String nomeDebitore) {
		this.nomeDebitore = nomeDebitore;
	}

	public String getCfPiDebitore() {
		return cfPiDebitore;
	}

	public void setCfPiDebitore(String cfPiDebitore) {
		this.cfPiDebitore = cfPiDebitore;
	}

	public String getCognRsocVersante() {
		return cognRsocVersante;
	}

	public void setCognRsocVersante(String cognRsocVersante) {
		this.cognRsocVersante = cognRsocVersante;
	}

	public String getNomeVersante() {
		return nomeVersante;
	}

	public void setNomeVersante(String nomeVersante) {
		this.nomeVersante = nomeVersante;
	}

	public String getCfPiVersante() {
		return cfPiVersante;
	}

	public void setCfPiVersante(String cfPiVersante) {
		this.cfPiVersante = cfPiVersante;
	}

	public BigDecimal getImportoTransitato() {
		return importoTransitato;
	}

	public void setImportoTransitato(BigDecimal importoTransitato) {
		this.importoTransitato = importoTransitato;
	}

	public BigDecimal getImportoCommissioni() {
		return importoCommissioni;
	}

	public void setImportoCommissioni(BigDecimal importoCommissioni) {
		this.importoCommissioni = importoCommissioni;
	}

	public String getCodiceAvviso() {
		return codiceAvviso;
	}

	public void setCodiceAvviso(String codiceAvviso) {
		this.codiceAvviso = codiceAvviso;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Override
	public String toString() {
		return "RpPagopaDTO [idElabora=" + idElabora + ", posizDebitoria=" + posizDebitoria + ", anno=" + anno
				+ ", iuv=" + iuv + ", importoPagato=" + importoPagato + ", dataScadenza=" + dataScadenza + ", causale="
				+ causale + ", dataEsitoPagamento=" + dataEsitoPagamento + ", cognRsocDebitore=" + cognRsocDebitore
				+ ", nomeDebitore=" + nomeDebitore + ", cfPiDebitore=" + cfPiDebitore + ", cognRsocVersante="
				+ cognRsocVersante + ", nomeVersante=" + nomeVersante + ", cfPiVersante=" + cfPiVersante
				+ ", importoTransitato=" + importoTransitato + ", importoCommissioni=" + importoCommissioni
				+ ", codiceAvviso=" + codiceAvviso + ", note=" + note + "]";
	}

}

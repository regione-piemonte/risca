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

public class PagopaListaCarIuvDTO extends GestAttoreDTO {

	@JsonProperty("id_pagopa_lista_car_iuv")
	private Long idPagopaListaCarIuv;

	@JsonProperty("id_lotto")
	private Long idLotto;

	@JsonProperty("nap")
	private String nap;

	@JsonProperty("anno")
	private String anno;

	@JsonProperty("importo")
	private BigDecimal importo;

	@JsonProperty("data_scadenza")
	private Date dataScadenza;

	@JsonProperty("data_inizio_validita")
	private Date dataInizioValidita;

	@JsonProperty("data_fine_validita")
	private Date dataFineValidita;

	@JsonProperty("causale")
	private String causale;

	@JsonProperty("tipo_soggetto")
	private String tipoSoggetto;

	@JsonProperty("cod_fiscale")
	private String codFiscale;

	@JsonProperty("ragione_sociale")
	private String ragioneSociale;

	@JsonProperty("cognome")
	private String cognome;

	@JsonProperty("nome")
	private String nome;

	@JsonProperty("note")
	private String note;

	@JsonProperty("cod_esito_da_pagopa")
	private String codEsitoDaPagopa;

	@JsonProperty("desc_esito_da_pagopa")
	private String descEsitoDaPagopa;

	@JsonProperty("codice_avviso")
	private String codiceAvviso;

	@JsonProperty("iuv")
	private String iuv;


	public Long getIdPagopaListaCarIuv() {
		return idPagopaListaCarIuv;
	}

	public void setIdPagopaListaCarIuv(Long idPagopaListaCarIuv) {
		this.idPagopaListaCarIuv = idPagopaListaCarIuv;
	}

	public Long getIdLotto() {
		return idLotto;
	}

	public void setIdLotto(Long idLotto) {
		this.idLotto = idLotto;
	}

	public String getNap() {
		return nap;
	}

	public void setNap(String nap) {
		this.nap = nap;
	}

	public String getAnno() {
		return anno;
	}

	public void setAnno(String anno) {
		this.anno = anno;
	}

	public BigDecimal getImporto() {
		return importo;
	}

	public void setImporto(BigDecimal importo) {
		this.importo = importo;
	}

	public Date getDataScadenza() {
		return dataScadenza;
	}

	public void setDataScadenza(Date dataScadenza) {
		this.dataScadenza = dataScadenza;
	}

	public Date getDataInizioValidita() {
		return dataInizioValidita;
	}

	public void setDataInizioValidita(Date dataInizioValidita) {
		this.dataInizioValidita = dataInizioValidita;
	}

	public Date getDataFineValidita() {
		return dataFineValidita;
	}

	public void setDataFineValidita(Date dataFineValidita) {
		this.dataFineValidita = dataFineValidita;
	}

	public String getCausale() {
		return causale;
	}

	public void setCausale(String causale) {
		this.causale = causale;
	}

	public String getTipoSoggetto() {
		return tipoSoggetto;
	}

	public void setTipoSoggetto(String tipoSoggetto) {
		this.tipoSoggetto = tipoSoggetto;
	}

	public String getCodFiscale() {
		return codFiscale;
	}

	public void setCodFiscale(String codFiscale) {
		this.codFiscale = codFiscale;
	}

	public String getRagioneSociale() {
		return ragioneSociale;
	}

	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getCodEsitoDaPagopa() {
		return codEsitoDaPagopa;
	}

	public void setCodEsitoDaPagopa(String codEsitoDaPagopa) {
		this.codEsitoDaPagopa = codEsitoDaPagopa;
	}

	public String getDescEsitoDaPagopa() {
		return descEsitoDaPagopa;
	}

	public void setDescEsitoDaPagopa(String descEsitoDaPagopa) {
		this.descEsitoDaPagopa = descEsitoDaPagopa;
	}

	public String getCodiceAvviso() {
		return codiceAvviso;
	}

	public void setCodiceAvviso(String codiceAvviso) {
		this.codiceAvviso = codiceAvviso;
	}

	public String getIuv() {
		return iuv;
	}

	public void setIuv(String iuv) {
		this.iuv = iuv;
	}

	@Override
	public String toString() {
		return "PagopaListaCarIuvDTO [idPagopaListaCarIuv=" + idPagopaListaCarIuv + ", idLotto=" + idLotto + ", nap="
				+ nap + ", anno=" + anno + ", importo=" + importo + ", dataScadenza=" + dataScadenza
				+ ", dataInizioValidita=" + dataInizioValidita + ", dataFineValidita=" + dataFineValidita + ", causale="
				+ causale + ", tipoSoggetto=" + tipoSoggetto + ", codFiscale=" + codFiscale + ", ragioneSociale="
				+ ragioneSociale + ", cognome=" + cognome + ", nome=" + nome + ", note=" + note + ", codEsitoDaPagopa="
				+ codEsitoDaPagopa + ", descEsitoDaPagopa=" + descEsitoDaPagopa + ", codiceAvviso=" + codiceAvviso
				+ ", iuv=" + iuv + "]";
	}



}

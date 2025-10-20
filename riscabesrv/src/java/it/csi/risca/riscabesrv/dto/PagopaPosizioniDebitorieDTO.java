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
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PagopaPosizioniDebitorieDTO {

	@JsonProperty("nome_lotto")
	private String nomeLotto;

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

	List<PagopaScompRichIuvDTO> componentiImporto;

	public String getNomeLotto() {
		return nomeLotto;
	}

	public void setNomeLotto(String nomeLotto) {
		this.nomeLotto = nomeLotto;
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

	public List<PagopaScompRichIuvDTO> getComponentiImporto() {
		return componentiImporto;
	}

	public void setComponentiImporto(List<PagopaScompRichIuvDTO> componentiImporto) {
		this.componentiImporto = componentiImporto;
	}

	@Override
	public String toString() {
		return "PagopaPosizioniDebitorieDTO [nomeLotto=" + nomeLotto + ", nap=" + nap + ", anno=" + anno + ", importo="
				+ importo + ", dataScadenza=" + dataScadenza + ", dataInizioValidita=" + dataInizioValidita
				+ ", dataFineValidita=" + dataFineValidita + ", causale=" + causale + ", tipoSoggetto=" + tipoSoggetto
				+ ", codFiscale=" + codFiscale + ", ragioneSociale=" + ragioneSociale + ", cognome=" + cognome
				+ ", nome=" + nome + ", note=" + note + "]";
	}

}

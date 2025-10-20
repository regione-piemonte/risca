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

import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StatoDebitorioExtendedDTO extends StatoDebitorioDTO{

	@JsonProperty("attivita_stato_deb") 
	private AttivitaStatoDebitorioDTO attivitaStatoDeb;

	@JsonProperty("stato_contribuzione") 
	private StatoContribuzioneDTO statoContribuzione;
	
	@JsonProperty("annualita_sd")
    private List<AnnualitaSdDTO> annualitaSd;
	
	@JsonProperty("rimborsi")
	List<RimborsoExtendedDTO>  rimborsi;
	
	@JsonProperty("rate")
    private List<RataSdDTO> rate;
	
	@JsonProperty("multi_nap")
	private boolean multiNap = false;
	
	@JsonProperty("data_pagamento")
	private String dataPagamento;
	
	@JsonProperty("cod_utenza")
	private String codUtenza;
	
	@JsonProperty("data_inizio_concessione")
	private Date dataInizioConcessione;
	
	@JsonProperty("data_fine_concessione")
	private Date dataFineConcessione;
	
	@JsonProperty("stato_riscossione")
	private String statoRiscossione;
	
	@JsonProperty("calcolo_interessi")
	private BigDecimal calcoloInteressi;
	
	@JsonProperty("accertamenti")
	private  List<AccertamentoExtendedDTO> accertamenti;

    @Digits(integer = 9, fraction = 2, message = "interessi_maturati_spese_di_notifica deve avere al massimo 3 cifre intere e 2 decimali")
	@JsonProperty("interessi_maturati_spese_di_notifica")
	private BigDecimal intMaturatiSpeseNotifica;
	
    @Size(max = 60, min = 0, message = "Invalid attivita")
	@JsonProperty("attivita")
	private String attivita;

    @Digits(integer = 9, fraction = 2, message = "somma_rimborsi deve avere al massimo 9 cifre intere e 2 decimali")
	@JsonProperty("somma_rimborsi")
	private BigDecimal sommaRimborsi;

    @Digits(integer = 9, fraction = 2, message = "acc_importo_dovuto deve avere al massimo 9 cifre intere e 2 decimali")
	@JsonProperty("acc_importo_dovuto")
	private BigDecimal accImportoDovuto;
	
	
	@JsonProperty("acc_data_scadenza_pag")
	private String accDataScadenzaPag;

    @Digits(integer = 9, fraction = 2, message = "acc_importo_versato deve avere al massimo 9 cifre intere e 2 decimali")
	@JsonProperty("acc_importo_versato")
	private BigDecimal accImportoVersato;
	

    @Digits(integer = 9, fraction = 2, message = "acc_importo_di_canone_mancante deve avere al massimo 9 cifre intere e 2 decimali")
	@JsonProperty("acc_importo_di_canone_mancante")
	private BigDecimal accImportoDiCanoneMancante;

    @Digits(integer = 9, fraction = 2, message = "acc_interessi_mancanti deve avere al massimo 9 cifre intere e 2 decimali")
	@JsonProperty("acc_interessi_mancanti")
	private BigDecimal accInteressiMancanti;

    @Digits(integer = 9, fraction = 2, message = "acc_interessi_versati deve avere al massimo 9 cifre intere e 2 decimali")
	@JsonProperty("acc_interessi_versati")
	private BigDecimal accInteressiVersati;

    @Digits(integer = 9, fraction = 2, message = "acc_importo_rimborsato deve avere al massimo 9 cifre intere e 2 decimali")
	@JsonProperty("acc_importo_rimborsato")
	private BigDecimal accImportoRimborsato;
	
	
	@JsonProperty("titolare")
	private String titolare;
	
	@JsonProperty("num_pratica")
	private String numPratica;
	
	public BigDecimal getAccImportoDovuto() {
		return accImportoDovuto;
	}

	public void setAccImportoDovuto(BigDecimal accImportoDovuto) {
		this.accImportoDovuto = accImportoDovuto;
	}

	public String getAccDataScadenzaPag() {
		return accDataScadenzaPag;
	}

	public void setAccDataScadenzaPag(String accDataScadenzaPag) {
		this.accDataScadenzaPag = accDataScadenzaPag;
	}



	public BigDecimal getAccImportoVersato() {
		return accImportoVersato;
	}

	public void setAccImportoVersato(BigDecimal accImportoVersato) {
		this.accImportoVersato = accImportoVersato;
	}

	public BigDecimal getAccImportoDiCanoneMancante() {
		return accImportoDiCanoneMancante;
	}

	public void setAccImportoDiCanoneMancante(BigDecimal accImportoDiCanoneMancante) {
		this.accImportoDiCanoneMancante = accImportoDiCanoneMancante;
	}

	public BigDecimal getAccInteressiMancanti() {
		return accInteressiMancanti;
	}

	public void setAccInteressiMancanti(BigDecimal accInteressiMancanti) {
		this.accInteressiMancanti = accInteressiMancanti;
	}

	public BigDecimal getAccInteressiVersati() {
		return accInteressiVersati;
	}

	public void setAccInteressiVersati(BigDecimal accInteressiVersati) {
		this.accInteressiVersati = accInteressiVersati;
	}

	public BigDecimal getAccImportoRimborsato() {
		return accImportoRimborsato;
	}

	public void setAccImportoRimborsato(BigDecimal accImportoRimborsato) {
		this.accImportoRimborsato = accImportoRimborsato;
	}

	public AttivitaStatoDebitorioDTO getAttivitaStatoDeb() {
		return attivitaStatoDeb;
	}

	public void setAttivitaStatoDeb(AttivitaStatoDebitorioDTO attivitaStatoDeb) {
		this.attivitaStatoDeb = attivitaStatoDeb;
	}

	public StatoContribuzioneDTO getStatoContribuzione() {
		return statoContribuzione;
	}

	public void setStatoContribuzione(StatoContribuzioneDTO statoContribuzione) {
		this.statoContribuzione = statoContribuzione;
	}

	public List<AnnualitaSdDTO> getAnnualitaSd() {
		return annualitaSd;
	}

	public void setAnnualitaSd(List<AnnualitaSdDTO> annualitaSd) {
		this.annualitaSd = annualitaSd;
	}


	public List<RimborsoExtendedDTO> getRimborsi() {
		return rimborsi;
	}

	public void setRimborsi(List<RimborsoExtendedDTO>  rimborsi) {
		this.rimborsi = rimborsi;
	}

	public List<RataSdDTO> getRate() {
		return rate;
	}

	public void setRate(List<RataSdDTO> rate) {
		this.rate = rate;
	}

	public boolean isMultiNap() {
		return multiNap;
	}

	public void setMultiNap(boolean multiNap) {
		this.multiNap = multiNap;
	}

	public String getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(String dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public String getCodUtenza() {
		return codUtenza;
	}

	public void setCodUtenza(String codUtenza) {
		this.codUtenza = codUtenza;
	}

	public Date getDataInizioConcessione() {
		return dataInizioConcessione;
	}

	public void setDataInizioConcessione(Date dataInizioConcessione) {
		this.dataInizioConcessione = dataInizioConcessione;
	}

	public Date getDataFineConcessione() {
		return dataFineConcessione;
	}

	public void setDataFineConcessione(Date dataFineConcessione) {
		this.dataFineConcessione = dataFineConcessione;
	}

	public String getStatoRiscossione() {
		return statoRiscossione;
	}

	public void setStatoRiscossione(String statoRiscossione) {
		this.statoRiscossione = statoRiscossione;
	}


	public List<AccertamentoExtendedDTO> getAccertamenti() {
		return accertamenti;
	}

	public void setAccertamenti(List<AccertamentoExtendedDTO> accertamenti) {
		this.accertamenti = accertamenti;
	}

	public BigDecimal getIntMaturatiSpeseNotifica() {
		return intMaturatiSpeseNotifica;
	}

	public void setIntMaturatiSpeseNotifica(BigDecimal intMaturatiSpeseNotifica) {
		this.intMaturatiSpeseNotifica = intMaturatiSpeseNotifica;
	}

	public String getAttivita() {
		return attivita;
	}

	public void setAttivita(String attivita) {
		this.attivita = attivita;
	}

	public BigDecimal getSommaRimborsi() {
		return sommaRimborsi;
	}

	public void setSommaRimborsi(BigDecimal sommaRimborsi) {
		this.sommaRimborsi = sommaRimborsi;
	}

	public BigDecimal getCalcoloInteressi() {
		return calcoloInteressi;
	}

	public void setCalcoloInteressi(BigDecimal calcoloInteressi) {
		this.calcoloInteressi = calcoloInteressi;
	}

	public String getTitolare() {
		return titolare;
	}

	public void setTitolare(String titolare) {
		this.titolare = titolare;
	}

	public String getNumPratica() {
		return numPratica;
	}

	public void setNumPratica(String numPratica) {
		this.numPratica = numPratica;
	}


	
	
}

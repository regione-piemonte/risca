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

public class AvvisoDatiAmminDTO extends GestAttoreDTO {

	@JsonProperty("nap")
	private String nap;

	@JsonProperty("codice_utenza")
	private String codiceUtenza;

	@JsonProperty("id_stato_debitorio")
	private Long idStatoDebitorio;

	@JsonProperty("corpo_idrico")
	private String corpoIdrico;

	@JsonProperty("comune_di_presa")
	private String comuneDiPresa;

	@JsonProperty("periodo_di_contribuzione")
	private String periodoDiContribuzione;

	@JsonProperty("numero_protocollo_sped")
	private String numeroProtocolloSped;

	@JsonProperty("data_protocollo_sped")
	private Date dataProtocolloSped;

	@JsonProperty("etichetta21_calc")
	private String etichetta21Calc;

	@JsonProperty("valore21_calc")
	private String valore21Calc;

	@JsonProperty("etichetta22_calc")
	private String etichetta22Calc;

	@JsonProperty("valore22_calc")
	private String valore22Calc;

	@JsonProperty("totale_utenza_calc")
	private String totaleUtenzaCalc;

	@JsonProperty("scad_conc_eti_calc")
	private String scadConcEtiCalc;

	@JsonProperty("scad_conc_calc")
	private String scadConcCalc;

	@JsonProperty("scadenza_concessione_calc")
	private String scadenzaConcessioneCalc;

	@JsonProperty("provvedimento_calc")
	private String provvedimentoCalc;

	@JsonProperty("testo")
	private String testo;

	@JsonProperty("data_scad_emas_iso")
	private Date dataScadEmasIso;

	@JsonProperty("imp_compens_canone")
	private String impCompensCanone;

	@JsonProperty("rec_canone")
	private String recCanone;

	@JsonProperty("num_pratica")
	private String numPratica;

	@JsonProperty("descr_utilizzo")
	private String descrUtilizzo;

	@JsonProperty("tot_energ_prod")
	private BigDecimal totEnergProd;


	public String getNap() {
		return nap;
	}

	public void setNap(String nap) {
		this.nap = nap;
	}

	public String getCodiceUtenza() {
		return codiceUtenza;
	}

	public void setCodiceUtenza(String codiceUtenza) {
		this.codiceUtenza = codiceUtenza;
	}

	public Long getIdStatoDebitorio() {
		return idStatoDebitorio;
	}

	public void setIdStatoDebitorio(Long idStatoDebitorio) {
		this.idStatoDebitorio = idStatoDebitorio;
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

	public String getPeriodoDiContribuzione() {
		return periodoDiContribuzione;
	}

	public void setPeriodoDiContribuzione(String periodoDiContribuzione) {
		this.periodoDiContribuzione = periodoDiContribuzione;
	}

	public String getNumeroProtocolloSped() {
		return numeroProtocolloSped;
	}

	public void setNumeroProtocolloSped(String numeroProtocolloSped) {
		this.numeroProtocolloSped = numeroProtocolloSped;
	}

	public Date getDataProtocolloSped() {
		return dataProtocolloSped;
	}

	public void setDataProtocolloSped(Date dataProtocolloSped) {
		this.dataProtocolloSped = dataProtocolloSped;
	}

	public String getEtichetta21Calc() {
		return etichetta21Calc;
	}

	public void setEtichetta21Calc(String etichetta21Calc) {
		this.etichetta21Calc = etichetta21Calc;
	}

	public String getValore21Calc() {
		return valore21Calc;
	}

	public void setValore21Calc(String valore21Calc) {
		this.valore21Calc = valore21Calc;
	}

	public String getEtichetta22Calc() {
		return etichetta22Calc;
	}

	public void setEtichetta22Calc(String etichetta22Calc) {
		this.etichetta22Calc = etichetta22Calc;
	}

	public String getValore22Calc() {
		return valore22Calc;
	}

	public void setValore22Calc(String valore22Calc) {
		this.valore22Calc = valore22Calc;
	}

	public String getTotaleUtenzaCalc() {
		return totaleUtenzaCalc;
	}

	public void setTotaleUtenzaCalc(String totaleUtenzaCalc) {
		this.totaleUtenzaCalc = totaleUtenzaCalc;
	}

	public String getScadConcEtiCalc() {
		return scadConcEtiCalc;
	}

	public void setScadConcEtiCalc(String scadConcEtiCalc) {
		this.scadConcEtiCalc = scadConcEtiCalc;
	}

	public String getScadConcCalc() {
		return scadConcCalc;
	}

	public void setScadConcCalc(String scadConcCalc) {
		this.scadConcCalc = scadConcCalc;
	}

	public String getScadenzaConcessioneCalc() {
		return scadenzaConcessioneCalc;
	}

	public void setScadenzaConcessioneCalc(String scadenzaConcessioneCalc) {
		this.scadenzaConcessioneCalc = scadenzaConcessioneCalc;
	}

	public String getProvvedimentoCalc() {
		return provvedimentoCalc;
	}

	public void setProvvedimentoCalc(String provvedimentoCalc) {
		this.provvedimentoCalc = provvedimentoCalc;
	}

	public String getTesto() {
		return testo;
	}

	public void setTesto(String testo) {
		this.testo = testo;
	}

	public Date getDataScadEmasIso() {
		return dataScadEmasIso;
	}

	public void setDataScadEmasIso(Date dataScadEmasIso) {
		this.dataScadEmasIso = dataScadEmasIso;
	}

	public String getImpCompensCanone() {
		return impCompensCanone;
	}

	public void setImpCompensCanone(String impCompensCanone) {
		this.impCompensCanone = impCompensCanone;
	}

	public String getRecCanone() {
		return recCanone;
	}

	public void setRecCanone(String recCanone) {
		this.recCanone = recCanone;
	}

	public String getNumPratica() {
		return numPratica;
	}

	public void setNumPratica(String numPratica) {
		this.numPratica = numPratica;
	}

	public String getDescrUtilizzo() {
		return descrUtilizzo;
	}

	public void setDescrUtilizzo(String descrUtilizzo) {
		this.descrUtilizzo = descrUtilizzo;
	}

	public BigDecimal getTotEnergProd() {
		return totEnergProd;
	}

	public void setTotEnergProd(BigDecimal totEnergProd) {
		this.totEnergProd = totEnergProd;
	}

	@Override
	public String toString() {
		return "AvvisoDatiAmminDTO [nap=" + nap + ", codiceUtenza=" + codiceUtenza + ", idStatoDebitorio="
				+ idStatoDebitorio + ", corpoIdrico=" + corpoIdrico + ", comuneDiPresa=" + comuneDiPresa
				+ ", periodoDiContribuzione=" + periodoDiContribuzione + ", numeroProtocolloSped="
				+ numeroProtocolloSped + ", dataProtocolloSped=" + dataProtocolloSped + ", etichetta21Calc="
				+ etichetta21Calc + ", valore21Calc=" + valore21Calc + ", etichetta22Calc=" + etichetta22Calc
				+ ", valore22Calc=" + valore22Calc + ", totaleUtenzaCalc=" + totaleUtenzaCalc + ", scadConcEtiCalc="
				+ scadConcEtiCalc + ", scadConcCalc=" + scadConcCalc + ", scadenzaConcessioneCalc="
				+ scadenzaConcessioneCalc + ", provvedimentoCalc=" + provvedimentoCalc + ", testo=" + testo
				+ ", dataScadEmasIso=" + dataScadEmasIso + ", impCompensCanone=" + impCompensCanone + ", recCanone="
				+ recCanone + ", numPratica=" + numPratica + ", descrUtilizzo=" + descrUtilizzo + ", totEnergProd="
				+ totEnergProd + "]";
	}




}

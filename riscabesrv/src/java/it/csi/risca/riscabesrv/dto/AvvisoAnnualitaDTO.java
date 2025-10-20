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

public class AvvisoAnnualitaDTO extends GestAttoreDTO {

	@JsonProperty("nap")
	private String nap;

	@JsonProperty("codice_utenza")
	private String codiceUtenza;

	@JsonProperty("anno_rich_pagamento")
	private int annoRichPagamento;

	@JsonProperty("fraz_totale_canone_anno")
	private BigDecimal frazTotaleCanoneAnno;

	@JsonProperty("totale_canone_anno_calc")
	private BigDecimal totaleCanoneAnnoCalc;

	@JsonProperty("etichetta20_calc")
	private String etichetta20Calc;

	@JsonProperty("valore20_calc")
	private BigDecimal valore20Calc;

	@JsonProperty("vuoto")
	private String vuoto;


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

	public int getAnnoRichPagamento() {
		return annoRichPagamento;
	}

	public void setAnnoRichPagamento(int annoRichPagamento) {
		this.annoRichPagamento = annoRichPagamento;
	}

	public BigDecimal getFrazTotaleCanoneAnno() {
		return frazTotaleCanoneAnno;
	}

	public void setFrazTotaleCanoneAnno(BigDecimal frazTotaleCanoneAnno) {
		this.frazTotaleCanoneAnno = frazTotaleCanoneAnno;
	}

	public BigDecimal getTotaleCanoneAnnoCalc() {
		return totaleCanoneAnnoCalc;
	}

	public void setTotaleCanoneAnnoCalc(BigDecimal totaleCanoneAnnoCalc) {
		this.totaleCanoneAnnoCalc = totaleCanoneAnnoCalc;
	}

	public String getEtichetta20Calc() {
		return etichetta20Calc;
	}

	public void setEtichetta20Calc(String etichetta20Calc) {
		this.etichetta20Calc = etichetta20Calc;
	}

	public BigDecimal getValore20Calc() {
		return valore20Calc;
	}

	public void setValore20Calc(BigDecimal valore20Calc) {
		this.valore20Calc = valore20Calc;
	}

	public String getVuoto() {
		return vuoto;
	}

	public void setVuoto(String vuoto) {
		this.vuoto = vuoto;
	}

	@Override
	public String toString() {
		return "AvvisoAnnualitaDTO [nap=" + nap + ", codiceUtenza=" + codiceUtenza + ", annoRichPagamento="
				+ annoRichPagamento + ", frazTotaleCanoneAnno=" + frazTotaleCanoneAnno + ", totaleCanoneAnnoCalc="
				+ totaleCanoneAnnoCalc + ", etichetta20Calc=" + etichetta20Calc + ", valore20Calc=" + valore20Calc
				+ ", vuoto=" + vuoto + "]";
	}


}

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

public class AvvisoUsoDTO extends GestAttoreDTO {

	@JsonProperty("nap")
	private String nap;

	@JsonProperty("codice_utenza")
	private String codiceUtenza;

	@JsonProperty("anno_rich_pagamento")
	private int annoRichPagamento;

	@JsonProperty("uso_denominazione")
	private String usoDenominazione;

	@JsonProperty("unita_di_misura")
	private String unitaDiMisura;

	@JsonProperty("quantita")
	private BigDecimal quantita;

	@JsonProperty("canone_unitario")
	private BigDecimal canoneUnitario;

	@JsonProperty("canone_uso")
	private BigDecimal canoneUso;

	@JsonProperty("unita_mis1_calc")
	private String unitaMis1Calc;

	@JsonProperty("quantita_calc")
	private String quantitaCalc;

	@JsonProperty("condizioni_particolari_calc")
	private String condizioniParticolariCalc;

	@JsonProperty("uso_denominazione_p_calc")
	private String usoDenominazionePCalc;

	@JsonProperty("unita_mis_p_calc")
	private String unitaMisPCalc;

	@JsonProperty("quantita_p_calc")
	private BigDecimal quantitaPCalc;

	@JsonProperty("unita_di_misura_p_calc")
	private String unitaDiMisuraPCalc;

	@JsonProperty("canone_unitario_p_calc")
	private BigDecimal canoneUnitarioPCalc;

	@JsonProperty("canone_uso_p_calc")
	private BigDecimal canoneUsoPCalc;

	@JsonProperty("perc_falda_prof")
	private BigDecimal percFaldaProf;



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

	public String getUsoDenominazione() {
		return usoDenominazione;
	}

	public void setUsoDenominazione(String usoDenominazione) {
		this.usoDenominazione = usoDenominazione;
	}

	public String getUnitaDiMisura() {
		return unitaDiMisura;
	}

	public void setUnitaDiMisura(String unitaDiMisura) {
		this.unitaDiMisura = unitaDiMisura;
	}

	public BigDecimal getQuantita() {
		return quantita;
	}

	public void setQuantita(BigDecimal quantita) {
		this.quantita = quantita;
	}

	public BigDecimal getCanoneUnitario() {
		return canoneUnitario;
	}

	public void setCanoneUnitario(BigDecimal canoneUnitario) {
		this.canoneUnitario = canoneUnitario;
	}

	public BigDecimal getCanoneUso() {
		return canoneUso;
	}

	public void setCanoneUso(BigDecimal canoneUso) {
		this.canoneUso = canoneUso;
	}

	public String getUnitaMis1Calc() {
		return unitaMis1Calc;
	}

	public void setUnitaMis1Calc(String unitaMis1Calc) {
		this.unitaMis1Calc = unitaMis1Calc;
	}

	public String getQuantitaCalc() {
		return quantitaCalc;
	}

	public void setQuantitaCalc(String quantitaCalc) {
		this.quantitaCalc = quantitaCalc;
	}

	public String getCondizioniParticolariCalc() {
		return condizioniParticolariCalc;
	}

	public void setCondizioniParticolariCalc(String condizioniParticolariCalc) {
		this.condizioniParticolariCalc = condizioniParticolariCalc;
	}

	public String getUsoDenominazionePCalc() {
		return usoDenominazionePCalc;
	}

	public void setUsoDenominazionePCalc(String usoDenominazionePCalc) {
		this.usoDenominazionePCalc = usoDenominazionePCalc;
	}

	public String getUnitaMisPCalc() {
		return unitaMisPCalc;
	}

	public void setUnitaMisPCalc(String unitaMisPCalc) {
		this.unitaMisPCalc = unitaMisPCalc;
	}

	public BigDecimal getQuantitaPCalc() {
		return quantitaPCalc;
	}

	public void setQuantitaPCalc(BigDecimal quantitaPCalc) {
		this.quantitaPCalc = quantitaPCalc;
	}

	public String getUnitaDiMisuraPCalc() {
		return unitaDiMisuraPCalc;
	}

	public void setUnitaDiMisuraPCalc(String unitaDiMisuraPCalc) {
		this.unitaDiMisuraPCalc = unitaDiMisuraPCalc;
	}

	public BigDecimal getCanoneUnitarioPCalc() {
		return canoneUnitarioPCalc;
	}

	public void setCanoneUnitarioPCalc(BigDecimal canoneUnitarioPCalc) {
		this.canoneUnitarioPCalc = canoneUnitarioPCalc;
	}

	public BigDecimal getCanoneUsoPCalc() {
		return canoneUsoPCalc;
	}

	public void setCanoneUsoPCalc(BigDecimal canoneUsoPCalc) {
		this.canoneUsoPCalc = canoneUsoPCalc;
	}

	public BigDecimal getPercFaldaProf() {
		return percFaldaProf;
	}

	public void setPercFaldaProf(BigDecimal percFaldaProf) {
		this.percFaldaProf = percFaldaProf;
	}

	@Override
	public String toString() {
		return "AvvisoUsoDTO [nap=" + nap + ", codiceUtenza=" + codiceUtenza + ", annoRichPagamento="
				+ annoRichPagamento + ", usoDenominazione=" + usoDenominazione + ", unitaDiMisura=" + unitaDiMisura
				+ ", quantita=" + quantita + ", canoneUnitario=" + canoneUnitario + ", canoneUso=" + canoneUso
				+ ", unitaMis1Calc=" + unitaMis1Calc + ", quantitaCalc=" + quantitaCalc + ", condizioniParticolariCalc="
				+ condizioniParticolariCalc + ", usoDenominazionePCalc=" + usoDenominazionePCalc + ", unitaMisPCalc="
				+ unitaMisPCalc + ", quantitaPCalc=" + quantitaPCalc + ", unitaDiMisuraPCalc=" + unitaDiMisuraPCalc
				+ ", canoneUnitarioPCalc=" + canoneUnitarioPCalc + ", canoneUsoPCalc=" + canoneUsoPCalc
				+ ", percFaldaProf=" + percFaldaProf + "]";
	}



}

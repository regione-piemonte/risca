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
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RicercaPagamentiDaVisionareDTO {
	
	@JsonProperty("cod_utenza")
	private String codUtenza;
	
	@JsonProperty("nap")
	private String nap; 
	
	@JsonProperty("num_pratica") 
	private String numPratica;
	
	@JsonProperty("calcola_interessi") 
	private String calcolaInteressi;
	
	@JsonProperty("sd_da_escludere") 
	private List<Integer> sdDaEscludere;
	
	@JsonProperty("importo_da")
	private BigDecimal importoDa;  
	
	@JsonProperty("importo_a")
	private BigDecimal importoA; 
	
	@JsonProperty("titolare")
	private String titolare;
	
	@JsonProperty("flg_pratica")
	private String flgPratica;

	public String getCodUtenza() {
		return codUtenza;
	}

	public void setCodUtenza(String codUtenza) {
		this.codUtenza = codUtenza;
	}

	public String getNap() {
		return nap;
	}

	public void setNap(String nap) {
		this.nap = nap;
	}

	public String getNumPratica() {
		return numPratica;
	}

	public void setNumPratica(String numPratica) {
		this.numPratica = numPratica;
	}

	public String getCalcolaInteressi() {
		return calcolaInteressi;
	}

	public void setCalcolaInteressi(String calcolaInteressi) {
		this.calcolaInteressi = calcolaInteressi;
	}

	public List<Integer> getSdDaEscludere() {
		return sdDaEscludere;
	}

	public void setSdDaEscludere(List<Integer> sdDaEscludere) {
		this.sdDaEscludere = sdDaEscludere;
	}

	public BigDecimal getImportoDa() {
		return importoDa;
	}

	public void setImportoDa(BigDecimal importoDa) {
		this.importoDa = importoDa;
	}

	public BigDecimal getImportoA() {
		return importoA;
	}

	public void setImportoA(BigDecimal importoA) {
		this.importoA = importoA;
	}

	public String getTitolare() {
		return titolare;
	}

	public void setTitolare(String titolare) {
		this.titolare = titolare;
	}

	public String getFlgPratica() {
		return flgPratica;
	}

	public void setFlgPratica(String flgPratica) {
		this.flgPratica = flgPratica;
	}
	
	
	
}

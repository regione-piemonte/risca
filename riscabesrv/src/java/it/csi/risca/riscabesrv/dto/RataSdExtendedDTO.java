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


public class RataSdExtendedDTO extends RataSdDTO {

	

	@JsonProperty("tot_versato")
	private BigDecimal totVersato;
	
	@JsonProperty("totale_restituito")
	private BigDecimal totaleRestituito;
	
	
	@JsonProperty("imp_spese_notifica")
	private BigDecimal impSpeseNotifica;
	
	@JsonProperty("idStatoContribuzione")
	private Long idStatoContribuzione;

	@JsonProperty("flgAccertamento")
	private int flgAccertamento;
	
	@JsonProperty("idAttivitaStatoDebitorio")
	private Long idAttivitaStatoDebitorio;
	
	@JsonProperty("flgDilazione")
	private int flgDilazione;
	
	@JsonProperty("nap")
	private String nap;
	
	public String getNap() {
		return nap;
	}

	public void setNap(String nap) {
		this.nap = nap;
	}

	private int progrRata;
	
	public int getFlgDilazione() {
		return flgDilazione;
	}

	public void setFlgDilazione(int flgDilazione) {
		this.flgDilazione = flgDilazione;
	}

	public Long getIdAttivitaStatoDebitorio() {
		return idAttivitaStatoDebitorio;
	}

	public void setIdAttivitaStatoDebitorio(Long idAttivitaStatoDebitorio) {
		this.idAttivitaStatoDebitorio = idAttivitaStatoDebitorio;
	}

	public int getFlgAccertamento() {
		return flgAccertamento;
	}

	public void setFlgAccertamento(int flgAccertamento) {
		this.flgAccertamento = flgAccertamento;
	}

	public Long getIdStatoContribuzione() {
		return idStatoContribuzione;
	}

	public void setIdStatoContribuzione(Long idStatoContribuzione) {
		this.idStatoContribuzione = idStatoContribuzione;
	}

	public BigDecimal getImpSpeseNotifica() {
		return impSpeseNotifica;
	}

	public void setImpSpeseNotifica(BigDecimal impSpeseNotifica) {
		this.impSpeseNotifica = impSpeseNotifica;
	}

	public BigDecimal getTotVersato() {
		return totVersato;
	}

	public void setTotVersato(BigDecimal totVersato) {
		this.totVersato = totVersato;
	}
	
	public BigDecimal getTotaleRestituito() {
		return totaleRestituito;
	}

	public void setTotaleRestituito(BigDecimal totaleRestituito) {
		this.totaleRestituito = totaleRestituito;
	}


	@Override
	public String toString() {
		return "RataSdExtendedDTO [totVersato=" + totVersato + "totaleRestituito=" +totaleRestituito +"idStatoDebitorio="+getIdStatoDebitorio()+
				"impSpeseNotifica="+impSpeseNotifica+"idStatoContribuzione="+idStatoContribuzione+"flgAccertamento="+flgAccertamento+
				"idAttivitaStatoDebitorio="+idAttivitaStatoDebitorio+"flgDilazione="+flgDilazione+"nap="+nap+"]";
	}

	public int getProgrRata() {
		return progrRata;
	}

	public void setProgrRata(int progrRata) {
		this.progrRata = progrRata;
	}

	



	

	
	
	
}

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
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AnnualitaSdDTO extends GestAttoreDTO {

    @Min(value = 1, message = "L' id_stato_contribuzione deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_stato_contribuzione supera il limite massimo consentito per Integer")
	@JsonProperty("id_annualita_sd")
	private Long idAnnualitaSd;

    @Min(value = 1, message = "L' id_stato_debitorio deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_stato_debitorio supera il limite massimo consentito per Integer")
	@JsonProperty("id_stato_debitorio")
	private Long idStatoDebitorio;

    @Digits(integer = 4, fraction = 0, message = "anno deve avere al massimo 4 cifre intere")
	@JsonProperty("anno")
	private Integer anno;

	@JsonProperty("json_dt_annualita_sd")
	private String jsonDtAnnualitaSd;

    @Digits(integer = 9, fraction = 2, message = "canone_annuo deve avere al massimo 9 cifre intere e 2 decimali")
	@JsonProperty("canone_annuo")
	private BigDecimal canoneAnnuo;

    @Min(value = 0, message = "Il flg_rateo_prima_annualita deve essere almeno 0.")
    @Max(value = 1, message = "Il flg_rateo_prima_annualita deve essere al massimo 1.")
	@JsonProperty("flg_rateo_prima_annualita")
	private int flgRateoPrimaAnnualita;
	
    @Digits(integer = 2, fraction = 0, message = "numero_mesi deve avere al massimo 2 cifre intere")
	@JsonProperty("numero_mesi")
	private Integer numeroMesi;
	
	@JsonProperty("data_inizio")
	private Date dataInizio;

	
	@JsonProperty("annualita_uso_sd")
    private List<AnnualitaUsoSdDTO> annualitaUsoSd;

    @Min(value = 1, message = "L' id_componente_dt deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_componente_dt supera il limite massimo consentito per Integer")
	@JsonProperty("id_componente_dt")
	private Integer idComponenteDt;
	
	public Long getIdAnnualitaSd() {
		return idAnnualitaSd;
	}

	public void setIdAnnualitaSd(Long idAnnualitaSd) {
		this.idAnnualitaSd = idAnnualitaSd;
	}

	public Long getIdStatoDebitorio() {
		return idStatoDebitorio;
	}

	public void setIdStatoDebitorio(Long idStatoDebitorio) {
		this.idStatoDebitorio = idStatoDebitorio;
	}

	public Integer getAnno() {
		return anno;
	}

	public void setAnno(Integer anno) {
		this.anno = anno;
	}



	public String getJsonDtAnnualitaSd() {
		return jsonDtAnnualitaSd;
	}

	public void setJsonDtAnnualitaSd(String jsonDtAnnualitaSd) {
		this.jsonDtAnnualitaSd = jsonDtAnnualitaSd;
	}

	public BigDecimal getCanoneAnnuo() {
		return canoneAnnuo;
	}

	public void setCanoneAnnuo(BigDecimal canoneAnnuo) {
		this.canoneAnnuo = canoneAnnuo;
	}

	public int getFlgRateoPrimaAnnualita() {
		return flgRateoPrimaAnnualita;
	}

	public void setFlgRateoPrimaAnnualita(int flgRateoPrimaAnnualita) {
		this.flgRateoPrimaAnnualita = flgRateoPrimaAnnualita;
	}

	public Integer getNumeroMesi() {
		return numeroMesi;
	}

	public void setNumeroMesi(Integer numeroMesi) {
		this.numeroMesi = numeroMesi;
	}


	public List<AnnualitaUsoSdDTO> getAnnualitaUsoSd() {
		return annualitaUsoSd;
	}

	public void setAnnualitaUsoSd(List<AnnualitaUsoSdDTO> annualitaUsoSd) {
		this.annualitaUsoSd = annualitaUsoSd;
	}

	public Date getDataInizio() {
		return dataInizio;
	}

	public void setDataInizio(Date dataInizio) {
		this.dataInizio = dataInizio;
	}

	public Integer getIdComponenteDt() {
		return idComponenteDt;
	}

	public void setIdComponenteDt(Integer idComponenteDt) {
		this.idComponenteDt = idComponenteDt;
	}

	@Override
	public String toString() {
		return "AnnualitaSdDTO [idAnnualitaSd=" + idAnnualitaSd + ", idStatoDebitorio=" + idStatoDebitorio + ", anno="
				+ anno + ", jsonDtAnnualitaSd=" + jsonDtAnnualitaSd + ", canoneAnnuo=" + canoneAnnuo
				+ ", flgRateoPrimaAnnualita=" + flgRateoPrimaAnnualita + ", numeroMesi=" + numeroMesi + ", dataInizio="
				+ dataInizio + ", annualitaUsoSd=" + annualitaUsoSd + ", idComponenteDt=" + idComponenteDt + "]";
	}






}

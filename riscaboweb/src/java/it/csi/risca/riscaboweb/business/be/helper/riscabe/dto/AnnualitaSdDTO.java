/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AnnualitaSdDTO {

	@JsonProperty("id_annualita_sd")
	private Long idAnnualitaSd;

	@JsonProperty("id_stato_debitorio")
	private Long idStatoDebitorio;

	@JsonProperty("anno")
	private Integer anno;

	@JsonProperty("json_dt_annualita_sd")
	private String jsonDtAnnualitaSd;

	@JsonProperty("canone_annuo")
	private BigDecimal canoneAnnuo;

	@JsonProperty("flg_rateo_prima_annualita")
	private int flgRateoPrimaAnnualita;

	@JsonProperty("numero_mesi")
	private Integer numeroMesi;
	
	@JsonProperty("data_inizio")
	private Date dataInizio;


	@JsonProperty("annualita_uso_sd")
    private List<AnnualitaUsoSdDTO> annualitaUsoSd;
	

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

	public String getJsonDtAnnualitaSd() {
		return jsonDtAnnualitaSd;
	}

	public void setJsonDtAnnualitaSd(String jsonDtAnnualitaSd) {
		this.jsonDtAnnualitaSd = jsonDtAnnualitaSd;
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

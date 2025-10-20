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

/**
 * TipoDilazioneDTO
 *
 * @author CSI PIEMONTE
 */

public class TipoDilazioneDTO {

	@JsonProperty("id_tipo_dilazione")
	private Long idTipoDilazione;

	@JsonProperty("data_fine_val")
	private String dataFineValidita;

	@JsonProperty("data_inizio_val")
	private String dataInizioValidita;

	@JsonProperty("num_annualita_magg")
	private Integer numAnnualitaMagg;

	@JsonProperty("importo_magg")
	private BigDecimal importoMagg;

	@JsonProperty("importo_min")
	private BigDecimal importoMin;

	@JsonProperty("num_mesi")
	private Integer numMesi;

	@JsonProperty("num_rate")
	private Integer numRate;
	
	@JsonProperty("id_ambito")
	private Long idAmbito;

	public Long getIdTipoDilazione() {
		return idTipoDilazione;
	}

	public void setIdTipoDilazione(Long idTipoDilazione) {
		this.idTipoDilazione = idTipoDilazione;
	}

	public String getDataFineValidita() {
		return dataFineValidita;
	}

	public void setDataFineValidita(String dataFineValidita) {
		this.dataFineValidita = dataFineValidita;
	}

	public String getDataInizioValidita() {
		return dataInizioValidita;
	}

	public void setDataInizioValidita(String dataInizioValidita) {
		this.dataInizioValidita = dataInizioValidita;
	}

	public Integer getNumAnnualitaMagg() {
		return numAnnualitaMagg;
	}

	public void setNumAnnualitaMagg(Integer numAnnualitaMagg) {
		this.numAnnualitaMagg = numAnnualitaMagg;
	}

	public BigDecimal getImportoMagg() {
		return importoMagg;
	}

	public void setImportoMagg(BigDecimal importoMagg) {
		this.importoMagg = importoMagg;
	}

	public BigDecimal getImportoMin() {
		return importoMin;
	}

	public void setImportoMin(BigDecimal importoMin) {
		this.importoMin = importoMin;
	}

	public Integer getNumMesi() {
		return numMesi;
	}

	public void setNumMesi(Integer numMesi) {
		this.numMesi = numMesi;
	}

	public Integer getNumRate() {
		return numRate;
	}

	public void setNumRate(Integer numRate) {
		this.numRate = numRate;
	}

	public Long getIdAmbito() {
		return idAmbito;
	}

	public void setIdAmbito(Long idAmbito) {
		this.idAmbito = idAmbito;
	}
	
	@Override
	public String toString() {
		return "TipoDilazioneDTO [idTipoDilazione=" + idTipoDilazione + ", dataFineValidita=" + dataFineValidita
				+ ", dataInizioValidita=" + dataInizioValidita + ", numAnnualitaMagg=" + numAnnualitaMagg
				+ ", importoMagg=" + importoMagg + ", importoMin=" + importoMin + ", numMesi=" + numMesi + ", numRate="
				+ numRate + ",  idAmbito=" + idAmbito + "]";
	}

}

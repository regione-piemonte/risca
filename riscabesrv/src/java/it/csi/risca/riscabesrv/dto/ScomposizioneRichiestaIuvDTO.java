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

public class ScomposizioneRichiestaIuvDTO {

	@JsonProperty("nap")
	private String nap;

	@JsonProperty("id_bil_acc")
	private Long idBilAcc;

	@JsonProperty("importo_per_nap")
	private BigDecimal importoPerNap;

	@JsonProperty("cod_bil_acc")
	private String codBilAcc;

	@JsonProperty("anno")
	private String anno;

	@JsonProperty("dati_spec_risc")
	private String datiSpecRisc;

	@JsonProperty("importo_per_acc_orig")
	private BigDecimal importoPerAccOrig;

	@JsonProperty("id_accerta_bilancio")
	private Long idAccertaBilancio;

	@JsonProperty("sum_imp_per_acc_orig")
	private BigDecimal sumImpPerAccOrig;

	@JsonProperty("progr_imp_per_acc_orig")
	private Integer progrImpPerAccOrig;

	@JsonProperty("count_imp_per_acc_orig")
	private Integer countImpPerAccOrig;

	@JsonProperty("perc_tot_imp")
	private BigDecimal percTotImp;
	
	// Aggiunti per scomposizione interessi e spese notifica
	
	@JsonProperty("interessi_tot")
	private BigDecimal interessiTot;
	
	@JsonProperty("tot_spese_notif_per_nap")
	private BigDecimal totSpeseNotifPerNap;
	
	@JsonProperty("id_tipo_bil_acc")
	private Long idTipoBilAcc;
	
	@JsonProperty("canone_dovuto")
	private BigDecimal canoneDovuto;

	@JsonProperty("imp_versato")
	private BigDecimal impVersato;
	

	public String getNap() {
		return nap;
	}

	public void setNap(String nap) {
		this.nap = nap;
	}

	public Long getIdBilAcc() {
		return idBilAcc;
	}

	public void setIdBilAcc(Long idBilAcc) {
		this.idBilAcc = idBilAcc;
	}

	public BigDecimal getImportoPerNap() {
		return importoPerNap;
	}

	public void setImportoPerNap(BigDecimal importoPerNap) {
		this.importoPerNap = importoPerNap;
	}

	public String getCodBilAcc() {
		return codBilAcc;
	}

	public void setCodBilAcc(String codBilAcc) {
		this.codBilAcc = codBilAcc;
	}

	public String getAnno() {
		return anno;
	}

	public void setAnno(String anno) {
		this.anno = anno;
	}

	public String getDatiSpecRisc() {
		return datiSpecRisc;
	}

	public void setDatiSpecRisc(String datiSpecRisc) {
		this.datiSpecRisc = datiSpecRisc;
	}

	public BigDecimal getImportoPerAccOrig() {
		return importoPerAccOrig;
	}

	public void setImportoPerAccOrig(BigDecimal importoPerAccOrig) {
		this.importoPerAccOrig = importoPerAccOrig;
	}

	public Long getIdAccertaBilancio() {
		return idAccertaBilancio;
	}

	public void setIdAccertaBilancio(Long idAccertaBilancio) {
		this.idAccertaBilancio = idAccertaBilancio;
	}

	public BigDecimal getSumImpPerAccOrig() {
		return sumImpPerAccOrig;
	}

	public void setSumImpPerAccOrig(BigDecimal sumImpPerAccOrig) {
		this.sumImpPerAccOrig = sumImpPerAccOrig;
	}

	public Integer getProgrImpPerAccOrig() {
		return progrImpPerAccOrig;
	}

	public void setProgrImpPerAccOrig(Integer progrImpPerAccOrig) {
		this.progrImpPerAccOrig = progrImpPerAccOrig;
	}

	public Integer getCountImpPerAccOrig() {
		return countImpPerAccOrig;
	}

	public void setCountImpPerAccOrig(Integer countImpPerAccOrig) {
		this.countImpPerAccOrig = countImpPerAccOrig;
	}

	public BigDecimal getPercTotImp() {
		return percTotImp;
	}

	public void setPercTotImp(BigDecimal percTotImp) {
		this.percTotImp = percTotImp;
	}

	public BigDecimal getInteressiTot() {
		return interessiTot;
	}

	public void setInteressiTot(BigDecimal interessiTot) {
		this.interessiTot = interessiTot;
	}

	public BigDecimal getTotSpeseNotifPerNap() {
		return totSpeseNotifPerNap;
	}

	public void setTotSpeseNotifPerNap(BigDecimal totSpeseNotifPerNap) {
		this.totSpeseNotifPerNap = totSpeseNotifPerNap;
	}

	public Long getIdTipoBilAcc() {
		return idTipoBilAcc;
	}

	public void setIdTipoBilAcc(Long idTipoBilAcc) {
		this.idTipoBilAcc = idTipoBilAcc;
	}

	public BigDecimal getCanoneDovuto() {
		return canoneDovuto;
	}

	public void setCanoneDovuto(BigDecimal canoneDovuto) {
		this.canoneDovuto = canoneDovuto;
	}

	public BigDecimal getImpVersato() {
		return impVersato;
	}

	public void setImpVersato(BigDecimal impVersato) {
		this.impVersato = impVersato;
	}

}

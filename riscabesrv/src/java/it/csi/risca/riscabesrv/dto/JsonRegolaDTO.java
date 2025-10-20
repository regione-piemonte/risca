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

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonRegolaDTO {

	@Digits(integer = 7, fraction = 2, message = "canone_unitario deve avere al massimo 7 cifre intere e 2 decimali")
	@JsonProperty("canone_unitario")
	private BigDecimal canoneUnitario;

	@Digits(integer = 7, fraction = 2, message = "canone_percentuale deve avere al massimo 7 cifre intere e 2 decimali")
	@JsonProperty("canone_percentuale")
	private BigDecimal canonePercentuale;

	@Digits(integer = 7, fraction = 2, message = "canone_minimo deve avere al massimo 7 cifre intere e 2 decimali")
	@JsonProperty("canone_minimo")
	private BigDecimal canoneMinimo;

	@Min(value = 1, message = "L' anno_riferimento deve essere maggiore di 0")
	@Max(value = 9999, message = "L' anno_riferimento supera il limite massimo consentito 9999")
	@JsonProperty("anno_riferimento")
	private Integer annoRiferimento;

	@JsonProperty("ranges")
	private List<JsonRangeDTO> jsonRanges;

	@Digits(integer = 5, fraction = 4, message = "soglia deve avere al massimo 5 cifre intere e 4 decimali")
	@JsonProperty("soglia")
	private BigDecimal soglia;

	@Digits(integer = 7, fraction = 2, message = "canone_minimo_soglia_inf deve avere al massimo 7 cifre intere e 2 decimali")
	@JsonProperty("canone_minimo_soglia_inf")
	private BigDecimal canoneMinimoSogliaInf;

	@Digits(integer = 7, fraction = 2, message = "canone_minimo_soglia_sup deve avere al massimo 7 cifre intere e 2 decimali")
	@JsonProperty("canone_minimo_soglia_sup")
	private BigDecimal canoneMinimoSogliaSup;

	@Digits(integer = 7, fraction = 2, message = "minimo_principale deve avere al massimo 7 cifre intere e 2 decimali")
	@JsonProperty("minimo_principale")
	private BigDecimal minimoPrincipale;

	public BigDecimal getCanoneUnitario() {
		return canoneUnitario;
	}

	public void setCanoneUnitario(BigDecimal canoneUnitario) {
		this.canoneUnitario = canoneUnitario;
	}

	public BigDecimal getCanonePercentuale() {
		return canonePercentuale;
	}

	public void setCanonePercentuale(BigDecimal canonePercentuale) {
		this.canonePercentuale = canonePercentuale;
	}

	public BigDecimal getCanoneMinimo() {
		return canoneMinimo;
	}

	public void setCanoneMinimo(BigDecimal canoneMinimo) {
		this.canoneMinimo = canoneMinimo;
	}

	public Integer getAnnoRiferimento() {
		return annoRiferimento;
	}

	public void setAnnoRiferimento(Integer annoRiferimento) {
		this.annoRiferimento = annoRiferimento;
	}

	public List<JsonRangeDTO> getJsonRanges() {
		return jsonRanges;
	}

	public void setJsonRanges(List<JsonRangeDTO> jsonRanges) {
		this.jsonRanges = jsonRanges;
	}

	public BigDecimal getSoglia() {
		return soglia;
	}

	public void setSoglia(BigDecimal soglia) {
		this.soglia = soglia;
	}

	public BigDecimal getCanoneMinimoSogliaInf() {
		return canoneMinimoSogliaInf;
	}

	public void setCanoneMinimoSogliaInf(BigDecimal canoneMinimoSogliaInf) {
		this.canoneMinimoSogliaInf = canoneMinimoSogliaInf;
	}

	public BigDecimal getCanoneMinimoSogliaSup() {
		return canoneMinimoSogliaSup;
	}

	public void setCanoneMinimoSogliaSup(BigDecimal canoneMinimoSogliaSup) {
		this.canoneMinimoSogliaSup = canoneMinimoSogliaSup;
	}

	public BigDecimal getMinimoPrincipale() {
		return minimoPrincipale;
	}

	public void setMinimoPrincipale(BigDecimal minimoPrincipale) {
		this.minimoPrincipale = minimoPrincipale;
	}

}

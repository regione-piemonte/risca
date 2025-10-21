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

import com.fasterxml.jackson.annotation.JsonProperty;


public class IuvDTO {

	@JsonProperty("id_iuv")
	private Long idIuv;

	@JsonProperty("nap")
	private String nap;

	@JsonProperty("stato_iuv")
	private StatoIuvDTO statoIuv;

	@JsonProperty("iuv")
	private String iuv;

	@JsonProperty("codice_avviso")
	private String codiceAvviso;

	@JsonProperty("importo")
	private Long importo;

	@JsonProperty("codice_versamento")
	private String codiceVersamento;


	public Long getIdIuv() {
		return idIuv;
	}

	public void setIdIuv(Long idIuv) {
		this.idIuv = idIuv;
	}

	public String getNap() {
		return nap;
	}

	public void setNap(String nap) {
		this.nap = nap;
	}

	public StatoIuvDTO getStatoIuv() {
		return statoIuv;
	}

	public void setStatoIuv(StatoIuvDTO statoIuv) {
		this.statoIuv = statoIuv;
	}

	public String getIuv() {
		return iuv;
	}

	public void setIuv(String iuv) {
		this.iuv = iuv;
	}

	public String getCodiceAvviso() {
		return codiceAvviso;
	}

	public void setCodiceAvviso(String codiceAvviso) {
		this.codiceAvviso = codiceAvviso;
	}

	public Long getImporto() {
		return importo;
	}

	public void setImporto(Long importo) {
		this.importo = importo;
	}

	public String getCodiceVersamento() {
		return codiceVersamento;
	}

	public void setCodiceVersamento(String codiceVersamento) {
		this.codiceVersamento = codiceVersamento;
	}

}

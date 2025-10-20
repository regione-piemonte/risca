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

import com.fasterxml.jackson.annotation.JsonProperty;

public class EntePagopaDTO {

	@JsonProperty("id_ente_pagopa")
	private Long idEntePagopa;

	@JsonProperty("id_ambito")
	private Long idAmbito;

	@JsonProperty("cod_ente_pagopa")
	private String codEntePagopa;

	@JsonProperty("des_ente_pagopa")
	private String desEntePagopa;

	@JsonProperty("cod_settore")
	private String codSettore;

	@JsonProperty("cf_ente_creditore")
	private String cfEnteCreditore;

	@JsonProperty("cod_versamento")
	private String codVersamento;

	@JsonProperty("flg_richiesta_iuv")
	private Integer flgRichiestaIuv;

	@JsonProperty("causale")
	private String causale;

	@JsonProperty("gest_attore_upd")
	private String gestAttoreUpd = null;

	public Long getIdEntePagopa() {
		return idEntePagopa;
	}

	public void setIdEntePagopa(Long idEntePagopa) {
		this.idEntePagopa = idEntePagopa;
	}

	public Long getIdAmbito() {
		return idAmbito;
	}

	public void setIdAmbito(Long idAmbito) {
		this.idAmbito = idAmbito;
	}

	public String getCodEntePagopa() {
		return codEntePagopa;
	}

	public void setCodEntePagopa(String codEntePagopa) {
		this.codEntePagopa = codEntePagopa;
	}

	public String getDesEntePagopa() {
		return desEntePagopa;
	}

	public void setDesEntePagopa(String desEntePagopa) {
		this.desEntePagopa = desEntePagopa;
	}

	public String getCodSettore() {
		return codSettore;
	}

	public void setCodSettore(String codSettore) {
		this.codSettore = codSettore;
	}

	public String getCfEnteCreditore() {
		return cfEnteCreditore;
	}

	public void setCfEnteCreditore(String cfEnteCreditore) {
		this.cfEnteCreditore = cfEnteCreditore;
	}

	public String getCodVersamento() {
		return codVersamento;
	}

	public void setCodVersamento(String codVersamento) {
		this.codVersamento = codVersamento;
	}

	public Integer getFlgRichiestaIuv() {
		return flgRichiestaIuv;
	}

	public void setFlgRichiestaIuv(Integer flgRichiestaIuv) {
		this.flgRichiestaIuv = flgRichiestaIuv;
	}

	public String getCausale() {
		return causale;
	}

	public void setCausale(String causale) {
		this.causale = causale;
	}

	public String getGestAttoreUpd() {
		return gestAttoreUpd;
	}

	public void setGestAttoreUpd(String gestAttoreUpd) {
		this.gestAttoreUpd = gestAttoreUpd;
	}

	@Override
	public String toString() {
		return "EntePagopaDTO [idEntePagopa=" + idEntePagopa + ", idAmbito=" + idAmbito + ", codEntePagopa="
				+ codEntePagopa + ", desEntePagopa=" + desEntePagopa + ", codSettore=" + codSettore
				+ ", cfEnteCreditore=" + cfEnteCreditore + ", codVersamento=" + codVersamento + ", flgRichiestaIuv="
				+ flgRichiestaIuv + ", causale=" + causale + ", gestAttoreUpd=" + gestAttoreUpd + "]";
	}

}

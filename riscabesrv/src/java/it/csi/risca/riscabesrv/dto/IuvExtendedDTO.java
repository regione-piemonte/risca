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


public class IuvExtendedDTO extends IuvDTO {

	@JsonProperty("dovuto_iuv")
	private BigDecimal dovutoIuv;
	
	@JsonProperty("spese_iuv")
	private BigDecimal speseIuv;
	
	@JsonProperty("versato_iuv")
	private BigDecimal versatoIuv;
	
	@JsonProperty("data_scad_pag")
	private Date dataScadPag;
	
	@JsonProperty("flg_annullato")
	private int flgAnnullato;

	public int getFlgAnnullato() {
		return flgAnnullato;
	}

	public void setFlgAnnullato(int flgAnnullato) {
		this.flgAnnullato = flgAnnullato;
	}

	public BigDecimal getDovutoIuv() {
		return dovutoIuv;
	}

	public void setDovutoIuv(BigDecimal dovutoIuv) {
		this.dovutoIuv = dovutoIuv;
	}

	public BigDecimal getSpeseIuv() {
		return speseIuv;
	}

	public void setSpeseIuv(BigDecimal speseIuv) {
		this.speseIuv = speseIuv;
	}

	public BigDecimal getVersatoIuv() {
		return versatoIuv;
	}

	public void setVersatoIuv(BigDecimal versatoIuv) {
		this.versatoIuv = versatoIuv;
	}



	public Date getDataScadPag() {
		return dataScadPag;
	}

	public void setDataScadPag(Date dataScadPag) {
		this.dataScadPag = dataScadPag;
	}

	@Override
	public String toString() {
		return "IuvExtendedDTO [dovutoIuv=" + dovutoIuv + ", speseIuv=" + speseIuv + ", versatoIuv=" + versatoIuv +", dataScadPag=" + dataScadPag + "]";
	}

}

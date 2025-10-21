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

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SpedizioneDTO {

	@JsonProperty("id_spedizione")
	private Long idSpedizione;

	@JsonProperty("id_ambito")
	private Long idAmbito;

	@JsonProperty("cod_tipo_spedizione")
	private String codTipoSpedizione;

	@JsonProperty("data_spedizione")
	private Date dataSpedizione;

	@JsonProperty("data_protocollo")
	private Date dataProtocollo;

	@JsonProperty("num_protocollo")
	private String numProtocollo = null;

	@JsonProperty("data_scadenza")
	private Date dataScadenza;

	@JsonProperty("num_determina")
	private String numDetermina = null;

	@JsonProperty("data_determina")
	private Date dataDetermina;

	@JsonProperty("anno")
	private Integer anno;

	public Long getIdSpedizione() {
		return idSpedizione;
	}

	public void setIdSpedizione(Long idSpedizione) {
		this.idSpedizione = idSpedizione;
	}

	public Long getIdAmbito() {
		return idAmbito;
	}

	public void setIdAmbito(Long idAmbito) {
		this.idAmbito = idAmbito;
	}

	public String getCodTipoSpedizione() {
		return codTipoSpedizione;
	}

	public void setCodTipoSpedizione(String codTipoSpedizione) {
		this.codTipoSpedizione = codTipoSpedizione;
	}

	public Date getDataSpedizione() {
		return dataSpedizione;
	}

	public void setDataSpedizione(Date dataSpedizione) {
		this.dataSpedizione = dataSpedizione;
	}

	public Date getDataProtocollo() {
		return dataProtocollo;
	}

	public void setDataProtocollo(Date dataProtocollo) {
		this.dataProtocollo = dataProtocollo;
	}

	public String getNumProtocollo() {
		return numProtocollo;
	}

	public void setNumProtocollo(String numProtocollo) {
		this.numProtocollo = numProtocollo;
	}

	public Date getDataScadenza() {
		return dataScadenza;
	}

	public void setDataScadenza(Date dataScadenza) {
		this.dataScadenza = dataScadenza;
	}

	public String getNumDetermina() {
		return numDetermina;
	}

	public void setNumDetermina(String numDetermina) {
		this.numDetermina = numDetermina;
	}

	public Date getDataDetermina() {
		return dataDetermina;
	}

	public void setDataDetermina(Date dataDetermina) {
		this.dataDetermina = dataDetermina;
	}

	public Integer getAnno() {
		return anno;
	}

	public void setAnno(Integer anno) {
		this.anno = anno;
	}

	@Override
	public String toString() {
		return "SpedizioneDTO [idSpedizione=" + idSpedizione + ", idAmbito=" + idAmbito + ", codTipoSpedizione="
				+ codTipoSpedizione + ", dataSpedizione=" + dataSpedizione + ", dataProtocollo=" + dataProtocollo
				+ ", numProtocollo=" + numProtocollo + ", dataScadenza=" + dataScadenza + ", numDetermina="
				+ numDetermina + ", dataDetermina=" + dataDetermina + ", anno=" + anno + "]";
	}

}

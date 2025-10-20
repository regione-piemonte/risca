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

import java.util.Date;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SpedizioneDTO extends GestAttoreDTO {

    @Min(value = 1, message = "L' id_spedizione deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_spedizione supera il limite massimo consentito per Integer")
	@JsonProperty("id_spedizione")
	private Long idSpedizione;

    @Min(value = 1, message = "L' id_ambito deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_ambito supera il limite massimo consentito per Integer")
	@JsonProperty("id_ambito")
	private Long idAmbito;

    @Size(max = 100, min = 0, message = "cod_tipo_spedizione deve essere compreso tra 0 e 30 caratteri. ")
	@JsonProperty("cod_tipo_spedizione")
	private String codTipoSpedizione;


	@JsonProperty("data_spedizione")
	private Date dataSpedizione;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonProperty("data_protocollo")
	private Date dataProtocollo;

    @Size(max = 30, min = 0, message = "num_protocollo deve essere compreso tra 0 e 30 caratteri. ")
	@JsonProperty("num_protocollo")
	private String numProtocollo = null;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonProperty("data_scadenza")
	private Date dataScadenza;

    @Size(max = 25, min = 0, message = "num_determina deve essere compreso tra 0 e 25 caratteri.  ")
	@JsonProperty("num_determina")
	private String numDetermina = null;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonProperty("data_determina")
	private Date dataDetermina;

    @Digits(integer = 4, fraction = 0, message = "Il formato anno non e valido")
	@JsonProperty("anno")
	private Integer anno;
	
    @Min(value = 1, message = "L' id_elabora deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_elabora supera il limite massimo consentito per Integer")
	@JsonProperty("id_elabora")
	private Long idElabora = null;

	

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
	
	public Long getIdElabora() {
		return idElabora;
	}

	public void setIdElabora(Long idElabora) {
		this.idElabora = idElabora;
	}

	@Override
	public String toString() {
		return "SpedizioneDTO [idSpedizione=" + idSpedizione + ", idAmbito=" + idAmbito + ", codTipoSpedizione="
				+ codTipoSpedizione + ", dataSpedizione=" + dataSpedizione + ", dataProtocollo=" + dataProtocollo
				+ ", numProtocollo=" + numProtocollo + ", dataScadenza=" + dataScadenza + ", numDetermina="
				+ numDetermina + ", dataDetermina=" + dataDetermina + ", anno=" + anno + ", idElabora=" + idElabora+"]";
	}



}

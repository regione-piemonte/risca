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

import com.fasterxml.jackson.annotation.JsonProperty;

public class ComuneDTO {

	@JsonProperty("id_comune")
    private Long idComune;

    @JsonProperty("cod_istat_comune")
    private String codIstatComune;

    @JsonProperty("cod_belfiore_comune")
    private String codBelfioreComune;
    
    @JsonProperty("denom_comune")
    private String denomComune;
    
    @JsonProperty("id_provincia")
    private Long idProvincia;

	@JsonProperty("data_inizio_validita")
    private Date dataInizioValidita;
    
    @JsonProperty("data_fine_validita")
    private Date dataFineValidita;
    
    @JsonProperty("dt_id_comune")
    private Long dtIdComune;
    
    @JsonProperty("dt_id_comune_prev")
    private Long dtIdComunePrev;
    
    @JsonProperty("dt_id_comune_next")
    private Long dtIdComuneNext;

    @JsonProperty("cap_comune")
    private String capComune;
	
	public Long getIdComune() {
		return idComune;
	}

	public void setIdComune(Long idComune) {
		this.idComune = idComune;
	}

	public String getCodIstatComune() {
		return codIstatComune;
	}

	public void setCodIstatComune(String codIstatComune) {
		this.codIstatComune = codIstatComune;
	}

	public String getCodBelfioreComune() {
		return codBelfioreComune;
	}

	public void setCodBelfioreComune(String codBelfioreComune) {
		this.codBelfioreComune = codBelfioreComune;
	}

	public String getDenomComune() {
		return denomComune;
	}

	public void setDenomComune(String denomComune) {
		this.denomComune = denomComune;
	}

	public Long getIdProvincia() {
		return idProvincia;
	}

	public void setIdProvincia(Long idProvincia) {
		this.idProvincia = idProvincia;
	}
	
	public Date getDataInizioValidita() {
		return dataInizioValidita;
	}

	public void setDataInizioValidita(Date dataInizioValidita) {
		this.dataInizioValidita = dataInizioValidita;
	}

	public Date getDataFineValidita() {
		return dataFineValidita;
	}

	public void setDataFineValidita(Date dataFineValidita) {
		this.dataFineValidita = dataFineValidita;
	}
	
	public Long getDtIdComune() {
		return dtIdComune;
	}

	public void setDtIdComune(Long dtIdComune) {
		this.dtIdComune = dtIdComune;
	}

	public Long getDtIdComunePrev() {
		return dtIdComunePrev;
	}

	public void setDtIdComunePrev(Long dtIdComunePrev) {
		this.dtIdComunePrev = dtIdComunePrev;
	}

	public Long getDtIdComuneNext() {
		return dtIdComuneNext;
	}

	public void setDtIdComuneNext(Long dtIdComuneNext) {
		this.dtIdComuneNext = dtIdComuneNext;
	}

	public String getCapComune() {
		return capComune;
	}

	public void setCapComune(String capComune) {
		this.capComune = capComune;
	}

}

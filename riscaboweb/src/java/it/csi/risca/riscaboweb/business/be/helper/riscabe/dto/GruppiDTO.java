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

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * RecapitiDTO
 *
 * @author CSI PIEMONTE
 */

public class GruppiDTO {

	@JsonProperty("id_gruppo_soggetto")
    private Long idGruppoSoggetto;
	
    @JsonProperty("cod_gruppo_soggetto")
    private String codGruppoSoggetto;
    
    @JsonProperty("cod_gruppo_fonte")
    private String codGruppoFonte;
    
    @JsonProperty("des_gruppo_soggetto")
    private String desGruppoSoggetto;
	
	@JsonProperty("id_fonte")
    private Long idFonte;
	
	@JsonProperty("id_fonte_origine")
    private Long idFonteOrigine;
  
	@JsonProperty("componenti_gruppo")
    private List<GruppiSoggettoDTO> componentiGruppo;

	public Long getIdGruppoSoggetto() {
		return idGruppoSoggetto;
	}

	public void setIdGruppoSoggetto(Long idGruppoSoggetto) {
		this.idGruppoSoggetto = idGruppoSoggetto;
	}

	public String getCodGruppoSoggetto() {
		return codGruppoSoggetto;
	}

	public void setCodGruppoSoggetto(String codGruppoSoggetto) {
		this.codGruppoSoggetto = codGruppoSoggetto;
	}

	public String getCodGruppoFonte() {
		return codGruppoFonte;
	}

	public void setCodGruppoFonte(String codGruppoFonte) {
		this.codGruppoFonte = codGruppoFonte;
	}

	public String getDesGruppoSoggetto() {
		return desGruppoSoggetto;
	}

	public void setDesGruppoSoggetto(String desGruppoSoggetto) {
		this.desGruppoSoggetto = desGruppoSoggetto;
	}

	public Long getIdFonte() {
		return idFonte;
	}

	public void setIdFonte(Long idFonte) {
		this.idFonte = idFonte;
	}

	public Long getIdFonteOrigine() {
		return idFonteOrigine;
	}

	public void setIdFonteOrigine(Long idFonteOrigine) {
		this.idFonteOrigine = idFonteOrigine;
	}

	public List<GruppiSoggettoDTO> getComponentiGruppo() {
		return componentiGruppo;
	}

	public void setComponentiGruppo(List<GruppiSoggettoDTO> componentiGruppo) {
		this.componentiGruppo = componentiGruppo;
	}

}

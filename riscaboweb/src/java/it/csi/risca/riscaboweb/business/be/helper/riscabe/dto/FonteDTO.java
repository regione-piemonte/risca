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

/**
 * The type Fonte dto.
 *
 * @author CSI PIEMONTE
 */
public class FonteDTO {
	
	@JsonProperty("id_fonte")
    private Long idFonte;

    @JsonProperty("cod_fonte")
    private String codFonte;

    @JsonProperty("des_fonte")
    private String desFonte;
    
    @JsonProperty("chiave_sottoscrizione")
    private String chiaveSottoscrizione;
	
    public Long getIdFonte() {
		return idFonte;
	}

	public void setIdFonte(Long idFonte) {
		this.idFonte = idFonte;
	}

	public String getCodFonte() {
		return codFonte;
	}

	public void setCodFonte(String codFonte) {
		this.codFonte = codFonte;
	}

	public String getDesFonte() {
		return desFonte;
	}

	public void setDesFonte(String desFonte) {
		this.desFonte = desFonte;
	}

	public String getChiaveSottoscrizione() {
		return chiaveSottoscrizione;
	}

	public void setChiaveSottoscrizione(String chiaveSottoscrizione) {
		this.chiaveSottoscrizione = chiaveSottoscrizione;
	}


}
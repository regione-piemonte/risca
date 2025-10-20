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

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * GruppiDTO
 *
 * @author CSI PIEMONTE
 */

public class GruppiSoggettoDTO extends GestAttoreDTO {
	
	@Min(value = 1, message = "L' id_soggetto deve essere maggiore di 0")
	@Max(value = Integer.MAX_VALUE, message = "L' id_soggetto supera il limite massimo consentito per Integer")
	@JsonProperty("id_soggetto")
    private Long idSoggetto;
	
	@Min(value = 1, message = "L' id_gruppo_soggetto deve essere maggiore di 0")
	@Max(value = Integer.MAX_VALUE, message = "L' id_gruppo_soggetto supera il limite massimo consentito per Integer")
	@JsonProperty("id_gruppo_soggetto")
    private Long idGruppoSoggetto;
    
    @Min(value = 0, message = "Il flg_capo_gruppo deve essere almeno 0.")
    @Max(value = 1, message = "Il flg_capo_gruppo deve essere al massimo 1.")
	@JsonProperty("flg_capo_gruppo")
    private int flgCapoGruppo;

    public Long getIdSoggetto() {
		return idSoggetto;
	}

	public void setIdSoggetto(Long idSoggetto) {
		this.idSoggetto = idSoggetto;
	}

	public Long getIdGruppoSoggetto() {
		return idGruppoSoggetto;
	}

	public void setIdGruppoSoggetto(Long idGruppoSoggetto) {
		this.idGruppoSoggetto = idGruppoSoggetto;
	}

	public int getFlgCapoGruppo() {
		return flgCapoGruppo;
	}

	public void setFlgCapoGruppo(int flgCapoGruppo) {
		this.flgCapoGruppo = flgCapoGruppo;
	}



}

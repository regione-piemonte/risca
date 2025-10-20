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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * VerifyRiscossioneDTO
 *
 * @author CSI PIEMONTE
 */
public class VerifyRiscossioneDTO {
	
	@JsonProperty("id_riscossione")
    private Long idRiscossione;

	@JsonProperty("stato_riscossione")
    private StatiRiscossioneDTO statoRiscossione;

    @JsonIgnore
    private String codRiscossione;
       
    @JsonProperty("num_pratica")
    private String numPratica;

	public Long getIdRiscossione() {
		return idRiscossione;
	}

	public void setIdRiscossione(Long idRiscossione) {
		this.idRiscossione = idRiscossione;
	}

	public StatiRiscossioneDTO getStatoRiscossione() {
		return statoRiscossione;
	}

	public void setStatoRiscossione(StatiRiscossioneDTO statoRiscossione) {
		this.statoRiscossione = statoRiscossione;
	}

	public String getCodRiscossione() {
		return codRiscossione;
	}

	public void setCodRiscossione(String codRiscossione) {
		this.codRiscossione = codRiscossione;
	}

	public String getNumPratica() {
		return numPratica;
	}

	public void setNumPratica(String numPratica) {
		this.numPratica = numPratica;
	}
 

}

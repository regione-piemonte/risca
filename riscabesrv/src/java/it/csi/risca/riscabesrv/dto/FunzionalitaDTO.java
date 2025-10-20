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

/**
 * The type Funzionalita dto.
 *
 * @author CSI PIEMONTE
 */
public class FunzionalitaDTO {

	@JsonProperty("id_funzionalita")
    private Long idFunzionalita;

    @JsonProperty("cod_funzionalita")
    private String codFunzionalita;

    @JsonProperty("des_funzionalita")
    private String desFunzionalita;

	public Long getIdFunzionalita() {
		return idFunzionalita;
	}

	public void setIdFunzionalita(Long idFunzionalita) {
		this.idFunzionalita = idFunzionalita;
	}

	public String getCodFunzionalita() {
		return codFunzionalita;
	}

	public void setCodFunzionalita(String codFunzionalita) {
		this.codFunzionalita = codFunzionalita;
	}

	public String getDesFunzionalita() {
		return desFunzionalita;
	}

	public void setDesFunzionalita(String desFunzionalita) {
		this.desFunzionalita = desFunzionalita;
	}
    
    
}

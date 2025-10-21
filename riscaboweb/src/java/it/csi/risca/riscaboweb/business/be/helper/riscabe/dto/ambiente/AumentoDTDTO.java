/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ambiente;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * UsoDatoTecnicoDTO
 *
 * @author CSI PIEMONTE
 */

public class AumentoDTDTO {
	
	@JsonProperty("id_aumento")
    private Long idAumento;
	
	@JsonProperty("motivazione")
    private String motivazione;
	
	@JsonProperty("percentuale")
    private Long percentuale;
	
	public Long getIdAumento() {
		return idAumento;
	}

	public void setIdAumento(Long idAumento) {
		this.idAumento = idAumento;
	}

	public String getMotivazione() {
		return motivazione;
	}

	public void setMotivazione(String motivazione) {
		this.motivazione = motivazione;
	}

	public Long getPercentuale() {
		return percentuale;
	}

	public void setPercentuale(Long percentuale) {
		this.percentuale = percentuale;
	}

}

/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.tributi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * UsoDatoTecnicoDTO
 *
 * @author CSI PIEMONTE
 */
@JsonInclude(Include.NON_NULL)
public class TipoUsoDatoTecnicoTributiDTO {
	
	@JsonProperty("id_tipo_uso_legge")
    private Long idTipoUsoLegge;
	
	@JsonProperty("uso_di_legge")
    private String tipoUsoLegge;

	@JsonProperty("popolazione")
    private Long popolazione;

	public Long getPopolazione() {
		return popolazione;
	}

	public void setPopolazione(Long popolazione) {
		this.popolazione = popolazione;
	}
	
	public Long getIdTipoUsoLegge() {
		return idTipoUsoLegge;
	}

	public void setIdTipoUsoLegge(Long idTipoUsoLegge) {
		this.idTipoUsoLegge = idTipoUsoLegge;
	}

	public String getTipoUsoLegge() {
		return tipoUsoLegge;
	}

	public void setTipoUsoLegge(String tipoUsoLegge) {
		this.tipoUsoLegge = tipoUsoLegge;
	}
	
}

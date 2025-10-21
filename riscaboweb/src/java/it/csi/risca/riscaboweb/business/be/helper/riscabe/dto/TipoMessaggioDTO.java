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
 * TipoMessagiDTO
 *
 * @author CSI PIEMONTE
 */

public class TipoMessaggioDTO {

	@JsonProperty("id_tipo_messaggio")
    private Long idTipoMessaggio;

    @JsonProperty("cod_tipo_messaggio")
    private String codTipoMessaggio;
    
    @JsonProperty("des_tipo_messaggio")
    private String desTipoMessaggio;

	public Long getIdTipoMessaggio() {
		return idTipoMessaggio;
	}

	public void setIdTipoMessaggio(Long idTipoMessaggio) {
		this.idTipoMessaggio = idTipoMessaggio;
	}

	public String getCodTipoMessaggio() {
		return codTipoMessaggio;
	}

	public void setCodTipoMessaggio(String codTipoMessaggio) {
		this.codTipoMessaggio = codTipoMessaggio;
	}

	public String getDesTipoMessaggio() {
		return desTipoMessaggio;
	}

	public void setDesTipoMessaggio(String desTipoMessaggio) {
		this.desTipoMessaggio = desTipoMessaggio;
	}
    

}

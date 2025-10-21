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

public class MessaggiDTO {
	
	@JsonProperty("id_messaggio")
    private Long idMessaggio;

    @JsonProperty("tipo_messaggio")
    private TipoMessaggioDTO tipoMessaggio;
    
    @JsonProperty("cod_messaggio")
    private String codMessaggio;

    @JsonProperty("des_testo_messaggio")
    private String desTestoMessaggio;

	public Long getIdMessaggio() {
		return idMessaggio;
	}

	public void setIdMessaggio(Long idMessaggio) {
		this.idMessaggio = idMessaggio;
	}


	public TipoMessaggioDTO getTipoMessaggio() {
		return tipoMessaggio;
	}

	public void setTipoMessaggio(TipoMessaggioDTO tipoMessaggio) {
		this.tipoMessaggio = tipoMessaggio;
	}

	public String getCodMessaggio() {
		return codMessaggio;
	}

	public void setCodMessaggio(String codMessaggio) {
		this.codMessaggio = codMessaggio;
	}

	public String getDesTestoMessaggio() {
		return desTestoMessaggio;
	}

	public void setDesTestoMessaggio(String desTestoMessaggio) {
		this.desTestoMessaggio = desTestoMessaggio;
	}

}
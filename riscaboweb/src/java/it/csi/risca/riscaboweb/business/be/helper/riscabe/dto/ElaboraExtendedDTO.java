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

public class ElaboraExtendedDTO extends ElaboraDTO {

	@JsonProperty("registro_elabora")
	private RegistroElaboraExtendedDTO registroElabora;
	
	public RegistroElaboraExtendedDTO getRegistroElabora() {
		return registroElabora;
	}

	public void setRegistroElabora(RegistroElaboraExtendedDTO registroElabora) {
		this.registroElabora = registroElabora;
	}


}

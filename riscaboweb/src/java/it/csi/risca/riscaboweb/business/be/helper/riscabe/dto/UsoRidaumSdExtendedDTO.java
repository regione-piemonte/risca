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

public class UsoRidaumSdExtendedDTO extends UsoRidaumSdDTO{

	@JsonProperty("riduzione_aumento")
    private RiduzioneAumentoDTO riduzioneAumento;

	public RiduzioneAumentoDTO getRiduzioneAumento() {
		return riduzioneAumento;
	}

	public void setRiduzioneAumento(RiduzioneAumentoDTO riduzioneAumento) {
		this.riduzioneAumento = riduzioneAumento;
	}
	
}

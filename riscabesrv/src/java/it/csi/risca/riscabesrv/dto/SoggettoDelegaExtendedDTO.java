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

public class SoggettoDelegaExtendedDTO extends SoggettoDelegaDTO{

	@JsonProperty("soggetto")
    private SoggettiExtendedDTO soggetto;
	
	@JsonProperty("delegato")
    private DelegatoDTO delegato;

	public SoggettiExtendedDTO getSoggetto() {
		return soggetto;
	}

	public void setSoggetto(SoggettiExtendedDTO soggetto) {
		this.soggetto = soggetto;
	}

	public DelegatoDTO getDelegato() {
		return delegato;
	}

	public void setDelegato(DelegatoDTO delegato) {
		this.delegato = delegato;
	}
	
	

}

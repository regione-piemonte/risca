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

public class DetailDTO {

	private SoggettiExtendedDTO soggetto;

	public DetailDTO() {
		super();
	}

	public DetailDTO(SoggettiExtendedDTO soggetto) {
		super();
		this.soggetto = soggetto;
	}

	public SoggettiExtendedDTO getSoggetto() {
		return soggetto;
	}

	public void setSoggetto(SoggettiExtendedDTO soggetto) {
		this.soggetto = soggetto;
	}
	
}

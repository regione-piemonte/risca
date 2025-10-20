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
 * DelegatoExtendedDTO
 *
 * @author CSI PIEMONTE
 */
public class DelegatoExtendedDTO extends DelegatoDTO {
	
	@JsonProperty("soggettoDelega")
    private SoggettoDelegaDTO soggettoDelega;

	@JsonProperty("gruppoDelega")
    private GruppoDelegaDTO gruppoDelega;
	
	

	public SoggettoDelegaDTO getSoggettoDelega() {
		return soggettoDelega;
	}

	public void setSoggettoDelega(SoggettoDelegaDTO soggettoDelega) {
		this.soggettoDelega = soggettoDelega;
	}

	public GruppoDelegaDTO getGruppoDelega() {
		return gruppoDelega;
	}

	public void setGruppoDelega(GruppoDelegaDTO gruppoDelega) {
		this.gruppoDelega = gruppoDelega;
	}
	
	

	
}

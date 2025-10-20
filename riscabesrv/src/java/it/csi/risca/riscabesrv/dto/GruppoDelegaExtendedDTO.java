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
 * GruppoDelegaExtendedDTO
 *
 * @author CSI PIEMONTE
 */
public class GruppoDelegaExtendedDTO extends GruppoDelegaDTO{

	@JsonProperty("gruppo")
    private GruppiDTO gruppo;
	
	@JsonProperty("delegato")
    private DelegatoDTO delegato;


	public DelegatoDTO getDelegato() {
		return delegato;
	}

	public void setDelegato(DelegatoDTO delegato) {
		this.delegato = delegato;
	}

	public GruppiDTO getGruppo() {
		return gruppo;
	}

	public void setGruppo(GruppiDTO gruppo) {
		this.gruppo = gruppo;
	}
	
	

}

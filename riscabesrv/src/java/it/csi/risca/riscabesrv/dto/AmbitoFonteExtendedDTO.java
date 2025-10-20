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
 * The type AmbitoFonteExtended dto.
 *
 * @author CSI PIEMONTE
 */
public class AmbitoFonteExtendedDTO extends AmbitoFonteDTO{
	
	@JsonProperty("ambito")
	private AmbitoDTO ambito;

	@JsonProperty("fonte")
	private FonteDTO fonte;

	public AmbitoDTO getAmbito() {
		return ambito;
	}

	public void setAmbito(AmbitoDTO ambito) {
		this.ambito = ambito;
	}

	public FonteDTO getFonte() {
		return fonte;
	}

	public void setFonte(FonteDTO fonte) {
		this.fonte = fonte;
	}
	

}

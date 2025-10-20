/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.dto.ambiente;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RiscossioneDatoTecnicoDTO {
	
	@JsonProperty("riscossione")
	private DatoTecnicoJsonDTO riscossione;

	public DatoTecnicoJsonDTO getRiscossione() {
		return riscossione;
	}

	public void setRiscossione(DatoTecnicoJsonDTO riscossione) {
		this.riscossione = riscossione;
	}



}

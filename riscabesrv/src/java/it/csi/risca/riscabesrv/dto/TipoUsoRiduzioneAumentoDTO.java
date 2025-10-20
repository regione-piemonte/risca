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

public class TipoUsoRiduzioneAumentoDTO {
	
	@JsonProperty("id_riscossione_uso")
    private Long idRiscossioneUso;
	
	@JsonProperty("id_riduzione_aumento")
    private Long idRiduzioneAumento;
	
	
	public Long getIdRiscossioneUso() {
		return idRiscossioneUso;
	}

	public void setIdRiscossioneUso(Long idRiscossioneUso) {
		this.idRiscossioneUso = idRiscossioneUso;
	}

	public Long getIdRiduzioneAumento() {
		return idRiduzioneAumento;
	}

	public void setIdRiduzioneAumento(Long idRiduzioneAumento) {
		this.idRiduzioneAumento = idRiduzioneAumento;
	}
}

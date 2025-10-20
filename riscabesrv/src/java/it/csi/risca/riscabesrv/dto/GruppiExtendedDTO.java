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
 * GruppiDTO
 *
 * @author CSI PIEMONTE
 */

public class GruppiExtendedDTO extends GruppiDTO {

    @JsonProperty("id_soggetto")
    private Long idSoggetto;

	@JsonProperty("flg_capo_gruppo")
    private Long flgCapoGruppo;

	public Long getIdSoggetto() {
		return idSoggetto;
	}

	public void setIdSoggetto(Long idSoggetto) {
		this.idSoggetto = idSoggetto;
	}

	public Long getFlgCapoGruppo() {
		return flgCapoGruppo;
	}

	public void setFlgCapoGruppo(Long flgCapoGruppo) {
		this.flgCapoGruppo = flgCapoGruppo;
	}
	
}

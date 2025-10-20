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

public class SoggettoDelegaDTO extends GestAttoreDTO {
	
	@JsonProperty("id_soggetto_delega")
	private Long idSoggettoDelega;
	
	@JsonProperty("id_soggetto")
    private Long idSoggetto;
	
	@JsonProperty("id_delegato")
    private Long idDelegato;
	
	@JsonProperty("data_abilitazione")
    private String dataAbilitazione;
	
	@JsonProperty("data_disabilitazione")
    private String dataDisabilitazione;

	public Long getIdSoggettoDelega() {
		return idSoggettoDelega;
	}

	public void setIdSoggettoDelega(Long idSoggettoDelega) {
		this.idSoggettoDelega = idSoggettoDelega;
	}

	public Long getIdSoggetto() {
		return idSoggetto;
	}

	public void setIdSoggetto(Long idSoggetto) {
		this.idSoggetto = idSoggetto;
	}

	public Long getIdDelegato() {
		return idDelegato;
	}

	public void setIdDelegato(Long idDelegato) {
		this.idDelegato = idDelegato;
	}

	public String getDataAbilitazione() {
		return dataAbilitazione;
	}

	public void setDataAbilitazione(String dataAbilitazione) {
		this.dataAbilitazione = dataAbilitazione;
	}

	public String getDataDisabilitazione() {
		return dataDisabilitazione;
	}

	public void setDataDisabilitazione(String dataDisabilitazione) {
		this.dataDisabilitazione = dataDisabilitazione;
	}
	
	

}

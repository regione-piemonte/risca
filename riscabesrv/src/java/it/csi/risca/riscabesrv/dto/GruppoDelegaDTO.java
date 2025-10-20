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
 * GruppoDelegaDTO
 *
 * @author CSI PIEMONTE
 */
public class GruppoDelegaDTO extends GestAttoreDTO {
	
	@JsonProperty("id_gruppo_delega")
	private Long idGruppoDelega;
	
	@JsonProperty("id_gruppo")
    private Long idGruppo;
	
	@JsonProperty("id_delegato")
    private Long idDelegato;
	
	@JsonProperty("data_abilitazione")
    private String dataAbilitazione;
	
	@JsonProperty("data_disabilitazione")
    private String dataDisabilitazione;
	
	public Long getIdGruppoDelega() {
		return idGruppoDelega;
	}

	public void setIdGruppoDelega(Long idGruppoDelega) {
		this.idGruppoDelega = idGruppoDelega;
	}

	public Long getIdGruppo() {
		return idGruppo;
	}

	public void setIdGruppo(Long idGruppo) {
		this.idGruppo = idGruppo;
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

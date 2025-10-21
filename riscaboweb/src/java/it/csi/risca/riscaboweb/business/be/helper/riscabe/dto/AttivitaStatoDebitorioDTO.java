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

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Attivita Stato Debitorio
 *
 * @author CSI PIEMONTE
 */

public class AttivitaStatoDebitorioDTO {
	
	
	@JsonProperty("id_attivita_stato_deb")
    private Long idAttivitaStatoDeb;
	
    @JsonProperty("cod_attivita_stato_deb")
    private String codAttivitaStatoDeb;
    
    @JsonProperty("des_attivita_stato_deb")
    private String desAttivitaStatoDeb;
    
	@JsonProperty("id_tipo_attivita_stato_deb")
    private Long idTipoAttivitaStatoDeb;

	public Long getIdAttivitaStatoDeb() {
		return idAttivitaStatoDeb;
	}

	public void setIdAttivitaStatoDeb(Long idAttivitaStatoDeb) {
		this.idAttivitaStatoDeb = idAttivitaStatoDeb;
	}

	public String getCodAttivitaStatoDeb() {
		return codAttivitaStatoDeb;
	}

	public void setCodAttivitaStatoDeb(String codAttivitaStatoDeb) {
		this.codAttivitaStatoDeb = codAttivitaStatoDeb;
	}

	public String getDesAttivitaStatoDeb() {
		return desAttivitaStatoDeb;
	}

	public void setDesAttivitaStatoDeb(String desAttivitaStatoDeb) {
		this.desAttivitaStatoDeb = desAttivitaStatoDeb;
	}

	public Long getIdTipoAttivitaStatoDeb() {
		return idTipoAttivitaStatoDeb;
	}

	public void setIdTipoAttivitaStatoDeb(Long idTipoAttivitaStatoDeb) {
		this.idTipoAttivitaStatoDeb = idTipoAttivitaStatoDeb;
	}
			

	
}

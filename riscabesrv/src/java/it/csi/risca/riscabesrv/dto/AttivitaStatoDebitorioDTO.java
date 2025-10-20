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

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Attivita Stato Debitorio
 *
 * @author CSI PIEMONTE
 */

public class AttivitaStatoDebitorioDTO {
	
    @Min(value = 1, message = "L' id_attivita_stato_deb deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_attivita_stato_deb supera il limite massimo consentito per Integer")
	@JsonProperty("id_attivita_stato_deb")
    private Long idAttivitaStatoDeb;
	
    @Size(max = 2, min = 0, message = "cod_attivita_stato_deb deve essere compreso tra 0 e 2 caratteri.")
    @JsonProperty("cod_attivita_stato_deb")
    private String codAttivitaStatoDeb;
    
    @Size(max = 50, min = 0, message = "des_attivita_stato_deb deve essere compreso tra 0 e 50 caratteri.")
    @JsonProperty("des_attivita_stato_deb")
    private String desAttivitaStatoDeb;
    
    @Min(value = 1, message = "L' id_tipo_attivita_stato_deb deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_tipo_attivita_stato_deb supera il limite massimo consentito per Integer")
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

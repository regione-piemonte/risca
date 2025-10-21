/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ambiente;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DatiGeneraliDTO
 *
 * @author CSI PIEMONTE
 */

public class DatiGeneraliDTO {
	
	@JsonProperty("corpo_idrico_captazione")
	private String corpoIdricoCaptazione;
	
	@JsonProperty("comune")
    private String comune;
	
	@JsonProperty("nome_impianto_idrico")
	private String nomeImpiantoIdrico;
	
	@JsonProperty("portata_da_assegnare")
    private Double portataDaAssegnare;
	
	@JsonProperty("gestione_manuale")
    private String gestioneManuale;

	public String getCorpoIdricoCaptazione() {
		return corpoIdricoCaptazione;
	}

	public void setCorpoIdricoCaptazione(String corpoIdricoCaptazione) {
		this.corpoIdricoCaptazione = corpoIdricoCaptazione;
	}

	public String getComune() {
		return comune;
	}

	public void setComune(String comune) {
		this.comune = comune;
	}

	public String getNomeImpiantoIdrico() {
		return nomeImpiantoIdrico;
	}

	public void setNomeImpiantoIdrico(String nomeImpiantoIdrico) {
		this.nomeImpiantoIdrico = nomeImpiantoIdrico;
	}

	public Double getPortataDaAssegnare() {
		return portataDaAssegnare;
	}

	public void setPortataDaAssegnare(Double portataDaAssegnare) {
		this.portataDaAssegnare = portataDaAssegnare;
	}

	public String getGestioneManuale() {
		return gestioneManuale;
	}

	public void setGestioneManuale(String gestioneManuale) {
		this.gestioneManuale = gestioneManuale;
	}


}

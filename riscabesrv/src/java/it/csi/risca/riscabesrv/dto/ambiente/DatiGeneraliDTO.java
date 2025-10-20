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

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DatiGeneraliDTO
 *
 * @author CSI PIEMONTE
 */
@JsonInclude(Include.NON_NULL)
public class DatiGeneraliDTO {
	
	@JsonProperty("corpo_idrico_captazione")
	private String corpoIdricoCaptazione;
	
	@JsonProperty("comune")
    private String comune;
	
	@JsonProperty("nome_impianto_idrico")
	private String nomeImpiantoIdrico;
	
	@JsonProperty("portata_da_assegnare")
    private BigDecimal portataDaAssegnare;
	
	@JsonProperty("gestione_manuale")
    private String gestioneManuale;
	
	@JsonIgnoreProperties(ignoreUnknown = true)
	private String dataScadenzaEmasIso;
	
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

	public BigDecimal getPortataDaAssegnare() {
		return portataDaAssegnare;
	}

	public void setPortataDaAssegnare(BigDecimal portataDaAssegnare) {
		this.portataDaAssegnare = portataDaAssegnare;
	}

	public String getGestioneManuale() {
		return gestioneManuale;
	}

	public void setGestioneManuale(String gestioneManuale) {
		this.gestioneManuale = gestioneManuale;
	}

	public String getDataScadenzaEmasIso() {
		return dataScadenzaEmasIso;
	}

	public void setDataScadenzaEmasIso(String dataScadenzaEmasIso) {
		this.dataScadenzaEmasIso = dataScadenzaEmasIso;
	}


}

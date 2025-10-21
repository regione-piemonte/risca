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

public class RiscossioneSearchResultDTO {
	
	@JsonProperty("id_riscossione")
	private Long idRiscossione;
	
	@JsonProperty("codice_utenza")
	private String codiceUtenza;
	
	@JsonProperty("numero_pratica")
	private String numeroPratica;
	
	@JsonProperty("procedimento")
	private String procedimento;
	
	@JsonProperty("titolare")
	private String titolare;
	
	@JsonProperty("stato")
	private String stato;
	
	@JsonProperty("comune_opera_di_presa")
	private String comuneOperaDiPresa;

	@JsonProperty("corpo_idrico")
	private String corpoIdrico;
	
	@JsonProperty("nome_impianto")
	private String nomeImpianto;
	
	@JsonProperty("id_ambito")
	private Long idAmbito;
	
	@JsonProperty("data_scad_concessione")
	private String dataScadConcessione;
	
	public Long getIdAmbito() {
		return idAmbito;
	}
	public void setIdAmbito(Long idAmbito) {
		this.idAmbito = idAmbito;
	}
	public String getDataScadConcessione() {
		return dataScadConcessione;
	}
	public void setDataScadConcessione(String dataScadConcessione) {
		this.dataScadConcessione = dataScadConcessione;
	}
	public Long getIdRiscossione() {
		return idRiscossione;
	}
	public void setIdRiscossione(Long idRiscossione) {
		this.idRiscossione = idRiscossione;
	}
	public String getCodiceUtenza() {
		return codiceUtenza;
	}
	public void setCodiceUtenza(String codiceUtenza) {
		this.codiceUtenza = codiceUtenza;
	}
	public String getNumeroPratica() {
		return numeroPratica;
	}
	public String getProcedimento() {
		return procedimento;
	}
	public void setProcedimento(String procedimento) {
		this.procedimento = procedimento;
	}
	public void setNumeroPratica(String numeroPratica) {
		this.numeroPratica = numeroPratica;
	}
	public String getTitolare() {
		return titolare;
	}
	public void setTitolare(String titolare) {
		this.titolare = titolare;
	}
	public String getStato() {
		return stato;
	}
	public void setStato(String stato) {
		this.stato = stato;
	}
	public String getComuneOperaDiPresa() {
		return comuneOperaDiPresa;
	}
	public void setComuneOperaDiPresa(String comuneOperaDiPresa) {
		this.comuneOperaDiPresa = comuneOperaDiPresa;
	}
	public String getCorpoIdrico() {
		return corpoIdrico;
	}
	public void setCorpoIdrico(String corpoIdrico) {
		this.corpoIdrico = corpoIdrico;
	}
	public String getNomeImpianto() {
		return nomeImpianto;
	}
	public void setNomeImpianto(String nomeImpianto) {
		this.nomeImpianto = nomeImpianto;
	}
	
	
	

}

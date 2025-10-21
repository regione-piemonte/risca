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

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * UsoDatoTecnicoDTO
 *
 * @author CSI PIEMONTE
 */

public class TipoUsoDatoTecnicoDTO {
	
	@JsonProperty("id_tipo_uso_legge")
    private Long idTipoUsoLegge;
	
	@JsonProperty("uso_di_legge")
    private String tipoUsoLegge;
	
	@JsonProperty("uso_specifico")
    private String[] tipoUsoSpecifico;
	
	@JsonProperty("unita_di_misura")
    private String unitaDiMisura;
	
	@JsonProperty("qta_acqua")
    private Double quantita;
	
	@JsonProperty("qta_acqua_da")
    private Long quantitaDa;
	
	@JsonProperty("qta_acqua_a")
    private Long quantitaA;

	@JsonProperty("qta_falda_profonda")
    private Long quantitaFaldaProfonda;
	
	@JsonProperty("perc_falda_profonda")
    private Long percFaldaProfonda;
	
	@JsonProperty("qta_falda_superficie")
    private Long quantitaFaldaSuperficiale;
	
	@JsonProperty("perc_falda_superficie")
    private Long percQuantitaFaldaSuperficiale;

	@JsonProperty("riduzione")
    private List<RiduzioneDTDTO> riduzione;
	
	@JsonProperty("aumento")
    private List<AumentoDTDTO> aumento;
	
	@JsonProperty("data_scadenza_emas_iso")
	private String dataScadenzaEmasIso;
	
	@JsonProperty("data_scadenza_emas_iso_da")
	private String dataScadenzaEmasIsoDa;
	
	@JsonProperty("data_scadenza_emas_iso_a")
	private String dataScadenzaEmasIsoA;
	
	
	public Long getIdTipoUsoLegge() {
		return idTipoUsoLegge;
	}

	public void setIdTipoUsoLegge(Long idTipoUsoLegge) {
		this.idTipoUsoLegge = idTipoUsoLegge;
	}

	public String getTipoUsoLegge() {
		return tipoUsoLegge;
	}

	public void setTipoUsoLegge(String tipoUsoLegge) {
		this.tipoUsoLegge = tipoUsoLegge;
	}

	public String[] getTipoUsoSpecifico() {
		return tipoUsoSpecifico;
	}

	public void setTipoUsoSpecifico(String[] tipoUsoSpecifico) {
		this.tipoUsoSpecifico = tipoUsoSpecifico;
	}

	public String getUnitaDiMisura() {
		return unitaDiMisura;
	}

	public void setUnitaDiMisura(String unitaDiMisura) {
		this.unitaDiMisura = unitaDiMisura;
	}

	public Double getQuantita() {
		return quantita;
	}

	public void setQuantita(Double quantita) {
		this.quantita = quantita;
	}

	public Long getQuantitaFaldaProfonda() {
		return quantitaFaldaProfonda;
	}

	public void setQuantitaFaldaProfonda(Long quantitaFaldaProfonda) {
		this.quantitaFaldaProfonda = quantitaFaldaProfonda;
	}

	public Long getPercFaldaProfonda() {
		return percFaldaProfonda;
	}

	public void setPercFaldaProfonda(Long percFaldaProfonda) {
		this.percFaldaProfonda = percFaldaProfonda;
	}

	public Long getQuantitaFaldaSuperficiale() {
		return quantitaFaldaSuperficiale;
	}

	public void setQuantitaFaldaSuperficiale(Long quantitaFaldaSuperficiale) {
		this.quantitaFaldaSuperficiale = quantitaFaldaSuperficiale;
	}

	public Long getPercQuantitaFaldaSuperficiale() {
		return percQuantitaFaldaSuperficiale;
	}

	public void setPercQuantitaFaldaSuperficiale(Long percQuantitaFaldaSuperficiale) {
		this.percQuantitaFaldaSuperficiale = percQuantitaFaldaSuperficiale;
	}
	
	public List<RiduzioneDTDTO> getRiduzione() {
		return riduzione;
	}

	public void setRiduzione(List<RiduzioneDTDTO> riduzione) {
		this.riduzione = riduzione;
	}

	public List<AumentoDTDTO> getAumento() {
		return aumento;
	}

	public void setAumento(List<AumentoDTDTO> aumento) {
		this.aumento = aumento;
	}

	public String getDataScadenzaEmasIso() {
		return dataScadenzaEmasIso;
	}

	public void setDataScadenzaEmasIso(String dataScadenzaEmasIso) {
		this.dataScadenzaEmasIso = dataScadenzaEmasIso;
	}

	public Long getQuantitaDa() {
		return quantitaDa;
	}

	public void setQuantitaDa(Long quantitaDa) {
		this.quantitaDa = quantitaDa;
	}

	public Long getQuantitaA() {
		return quantitaA;
	}

	public void setQuantitaA(Long quantitaA) {
		this.quantitaA = quantitaA;
	}

	public String getDataScadenzaEmasIsoDa() {
		return dataScadenzaEmasIsoDa;
	}

	public void setDataScadenzaEmasIsoDa(String dataScadenzaEmasIsoDa) {
		this.dataScadenzaEmasIsoDa = dataScadenzaEmasIsoDa;
	}

	public String getDataScadenzaEmasIsoA() {
		return dataScadenzaEmasIsoA;
	}

	public void setDataScadenzaEmasIsoA(String dataScadenzaEmasIsoA) {
		this.dataScadenzaEmasIsoA = dataScadenzaEmasIsoA;
	}


}

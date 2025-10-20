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

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The type Ambito Interesse dto.
 *
 * @author CSI PIEMONTE
 */
public class AmbitoInteresseDTO {

    @JsonProperty("id_ambito_interesse")
    private Long idAmbitoInteresse;
    
    @JsonProperty("id_ambito")
    private Long idAmbito;
    
    @JsonProperty("tipo_interesse")
    private String tipoInteresse;

    @JsonProperty("data_inizio")
    private Date dataInizio;
    
    @JsonProperty("data_fine")
    private Date dataFine;
    
    @JsonProperty("percentuale")
    private Double percentuale;
    
    @JsonProperty("giorni_legali")
    private Integer giorniLegali;

	public Long getIdAmbitoInteresse() {
		return idAmbitoInteresse;
	}

	public void setIdAmbitoInteresse(Long idAmbitoInteresse) {
		this.idAmbitoInteresse = idAmbitoInteresse;
	}

	public Long getIdAmbito() {
		return idAmbito;
	}

	public void setIdAmbito(Long idAmbito) {
		this.idAmbito = idAmbito;
	}

	public String getTipoInteresse() {
		return tipoInteresse;
	}

	public void setTipoInteresse(String tipoInteresse) {
		this.tipoInteresse = tipoInteresse;
	}

	public Date getDataInizio() {
		return dataInizio;
	}

	public void setDataInizio(Date dataInizio) {
		this.dataInizio = dataInizio;
	}

	public Date getDataFine() {
		return dataFine;
	}

	public void setDataFine(Date dataFine) {
		this.dataFine = dataFine;
	}

	public Double getPercentuale() {
		return percentuale;
	}

	public void setPercentuale(Double percentuale) {
		this.percentuale = percentuale;
	}

	public Integer getGiorniLegali() {
		return giorniLegali;
	}

	public void setGiorniLegali(Integer giorniLegali) {
		this.giorniLegali = giorniLegali;
	}
    
    

    
}

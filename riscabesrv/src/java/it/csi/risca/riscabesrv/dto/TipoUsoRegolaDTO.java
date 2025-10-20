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

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * TipoUsoDTO
 *
 * @author CSI PIEMONTE
 */

public class TipoUsoRegolaDTO  extends GestAttoreDTO{
	
    @Min(value = 1, message = "L' id_tipo_uso_regola deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_tipo_uso_regola supera il limite massimo consentito per Integer")
	@JsonProperty("id_tipo_uso_regola")
    private Long idTipoUsoRegola;
	
    @Min(value = 1, message = "L' id_tipo_uso deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_tipo_uso supera il limite massimo consentito per Integer")
	@JsonProperty("id_tipo_uso")
    private Long idTipoUso;
	
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("data_inizio")
    private String dataInizio;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("data_fine")
    private String dataFine;

    @JsonProperty("json_regola")
    private String jsonRegola;
   
    @Min(value = 1, message = "L' id_algoritmo deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_algoritmo supera il limite massimo consentito per Integer")
    @JsonProperty("id_algoritmo")
    private Long idAlgoritmo;
    
    
    public Long getIdTipoUsoRegola() {
		return idTipoUsoRegola;
	}

	public void setIdTipoUsoRegola(Long idTipoUsoRegola) {
		this.idTipoUsoRegola = idTipoUsoRegola;
	}
	
	public Long getIdTipoUso() {
		return idTipoUso;
	}

	public void setIdTipoUso(Long idTipoUso) {
		this.idTipoUso = idTipoUso;
	}

	public String getDataInizio() {
		return dataInizio;
	}

	public void setDataInizio(String dataInizio) {
		this.dataInizio = dataInizio;
	}

	public String getDataFine() {
		return dataFine;
	}

	public void setDataFine(String dataFine) {
		this.dataFine = dataFine;
	}

	public String getJsonRegola() {
		return jsonRegola;
	}

	public void setJsonRegola(String jsonRegola) {
		this.jsonRegola = jsonRegola;
	}

	public Long getIdAlgoritmo() {
		return idAlgoritmo;
	}

	public void setIdAlgoritmo(Long idAlgoritmo) {
		this.idAlgoritmo = idAlgoritmo;
	}
	
}

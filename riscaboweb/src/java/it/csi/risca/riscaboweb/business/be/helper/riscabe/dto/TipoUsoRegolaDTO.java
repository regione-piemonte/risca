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
 * TipoUsoDTO
 *
 * @author CSI PIEMONTE
 */

public class TipoUsoRegolaDTO {
	
	@JsonProperty("id_tipo_uso_regola")
    private Long idTipoUsoRegola;
	
	@JsonProperty("id_tipo_uso")
    private Long idTipoUso;
	
    @JsonProperty("data_inizio")
    private String dataInizio;
    
    @JsonProperty("data_fine")
    private String dataFine;

    @JsonProperty("json_regola")
    private String jsonRegola;
    
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

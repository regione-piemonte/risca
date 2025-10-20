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

import java.util.Map;

import org.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

/**
 * DatiTecniciAmbienteDTO
 *
 * @author CSI PIEMONTE
 */
//@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class DatiTecniciAmbienteDTO {

	@JsonProperty("dati_generali")	 
	private DatiGeneraliDTO datiGenerali;
	
	@JsonProperty("usi")
    private Map<String, TipoUsoDatoTecnicoDTO> usi;

	public DatiGeneraliDTO getDatiGenerali() {
		return datiGenerali;
	}

	public void setDatiGenerali(DatiGeneraliDTO datiGenerali) {
		this.datiGenerali = datiGenerali;
	}

	public Map<String, TipoUsoDatoTecnicoDTO> getUsi() {
		return usi;
	}

	public void setUsi(Map<String, TipoUsoDatoTecnicoDTO> usi) {
		this.usi = usi;
	}

	
	@JsonIgnore
	public String toJsonString() {
	    ObjectMapper mapper = new ObjectMapper();
	    ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
	    try {
	        return ow.writeValueAsString(this);
	    } catch (JsonProcessingException e) {
	        return null;
	    }
	}
	/**
	 * To json obj json object.
	 *
	 * @return JSONObject json object
	 */
	@JsonIgnore
	public JSONObject toJsonObj() {
	    String sJson = this.toJsonString();
	    JSONObject obj = new JSONObject(sJson);

	    return obj;
	}
	
	
}

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

import org.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class RiscossioneDatoTecnicoDTO {
	
	@JsonProperty("riscossione")
	private DatoTecnicoJsonDTO riscossione;

	public DatoTecnicoJsonDTO getRiscossione() {
		return riscossione;
	}

	public void setRiscossione(DatoTecnicoJsonDTO riscossione) {
		this.riscossione = riscossione;
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

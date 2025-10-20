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

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The tracciamento dto.
 *
 * @author CSI PIEMONTE
 */
public class TracciamentoDTO {

    @Min(value = 1, message = "L' id_tracciamento deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_tracciamento supera il limite massimo consentito per Integer")
	@JsonProperty("id_tracciamento")
    private Long idTracciamento;
	
	@JsonProperty("data_ora")
    private Date dataOra;
	
    @Size(max = 1, min = 0, message = "flg_operazione deve essere compreso tra 0 e 1 caratteri.  ")
	@JsonProperty("flg_operazione")
    private String flgOperazione;
	
    @Min(value = 1, message = "L'ID di riscossione deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L'ID di riscossione supera il limite massimo consentito per Integer")
	@JsonProperty("id_riscossione")
    private Long idRiscossione;
    
	@JsonProperty("json_tracciamento")
	private String jsonTracciamento;
	
    @Size(max = 100, min = 0, message = "tipo_json deve essere compreso tra 0 e 100ncaratteri.  ")
	@JsonProperty("tipo_json")
	private String tipoJson;
	
	public Long getIdTracciamento() {
		return idTracciamento;
	}

	public void setIdTracciamento(Long idTracciamento) {
		this.idTracciamento = idTracciamento;
	}

	public Date getDataOra() {
		return dataOra;
	}

	public void setDataOra(Date dataOra) {
		this.dataOra = dataOra;
	}

	public String getFlgOperazione() {
		return flgOperazione;
	}

	public void setFlgOperazione(String flgOperazione) {
		this.flgOperazione = flgOperazione;
	}

	public Long getIdRiscossione() {
		return idRiscossione;
	}

	public void setIdRiscossione(Long idRiscossione) {
		this.idRiscossione = idRiscossione;
	}

	
	public String getJsonTracciamento() {
		return jsonTracciamento;
	}

	public void setJsonTracciamento(String jsonTracciamento) {
		this.jsonTracciamento = jsonTracciamento;
	}

	public String getTipoJson() {
		return tipoJson;
	}

	public void setTipoJson(String tipoJson) {
		this.tipoJson = tipoJson;
	}
	
}

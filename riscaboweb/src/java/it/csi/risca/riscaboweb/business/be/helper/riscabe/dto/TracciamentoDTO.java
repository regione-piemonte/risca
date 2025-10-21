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

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The tracciamento dto.
 *
 * @author CSI PIEMONTE
 */
public class TracciamentoDTO {

	@JsonProperty("id_tracciamento")
    private Long idTracciamento;
	
	@JsonProperty("data_ora")
    private Date dataOra;
	
	@JsonProperty("flg_operazione")
    private String flgOperazione;
	
	@JsonProperty("id_riscossione")
    private Long idRiscossione;

	@JsonProperty("json_tracciamento")
	private String jsonTracciamento;
	
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

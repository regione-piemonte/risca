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

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The Csi Log Audit dto.
 *
 * @author CSI PIEMONTE
 */
public class CsiLogAuditDTO {
	
//	@JsonProperty("data_ora")
	@JsonIgnore
    private Date dataOra;
	
    @Size(max = 100, min = 0, message = "id_app deve essere compreso tra 0 e 100 caratteri.")
	@JsonProperty("id_app")
    private String idApp;
	
    @Size(max = 40, min = 0, message = "ip_address deve essere compreso tra 0 e 40 caratteri.")
	@JsonProperty("ip_address")
    private String ipAddress;

    @Size(max = 100, min = 0, message = "utente deve essere compreso tra 0 e 100 caratteri.")
	@JsonProperty("utente")
    private String utente;
	
    @Size(max = 50, min = 0, message = "operazione deve essere compreso tra 0 e 50 caratteri.")
	@JsonProperty("operazione")
    private String operazione;
	
    @Size(max = 500, min = 0, message = "ogg_oper deve essere compreso tra 0 e 500 caratteri.")
	@JsonProperty("ogg_oper")
    private String oggOper;
	
    @Size(max = 500, min = 0, message = "key_oper deve essere compreso tra 0 e 500 caratteri.")
	@JsonProperty("key_oper")
    private String keyOper;

	public Date getDataOra() {
		return dataOra;
	}

	public void setDataOra(Date dataOra) {
		this.dataOra = dataOra;
	}

	public String getIdApp() {
		return idApp;
	}

	public void setIdApp(String idApp) {
		this.idApp = idApp;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getUtente() {
		return utente;
	}

	public void setUtente(String utente) {
		this.utente = utente;
	}

	public String getOperazione() {
		return operazione;
	}

	public void setOperazione(String operazione) {
		this.operazione = operazione;
	}

	public String getOggOper() {
		return oggOper;
	}

	public void setOggOper(String oggOper) {
		this.oggOper = oggOper;
	}

	public String getKeyOper() {
		return keyOper;
	}

	public void setKeyOper(String keyOper) {
		this.keyOper = keyOper;
	}
	
	
}

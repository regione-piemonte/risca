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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ComponenteDtDTO {

	@JsonProperty("id_componente_dt")
    private Long idComponenteDt;
	
    @JsonProperty("id_ambito")
    private Long idAmbito;
    
	@JsonIgnore
    private Long idTipoComponenteDt;

    @JsonProperty("nome_componente_dt")
    private String nomeComponenteDt;

    @JsonProperty("des_componente_dt")
    private String desComponenteDt;

	@JsonProperty("data_inizio_validita")
    private Date dataInizioValidita;
    
    @JsonProperty("data_fine_validita")
    private Date dataFineValidita; 
    
	public Long getIdComponenteDt() {
		return idComponenteDt;
	}

	public void setIdComponenteDt(Long idComponenteDt) {
		this.idComponenteDt = idComponenteDt;
	}

	public Long getIdAmbito() {
		return idAmbito;
	}

	public void setIdAmbito(Long idAmbito) {
		this.idAmbito = idAmbito;
	}

	public Long getIdTipoComponenteDt() {
		return idTipoComponenteDt;
	}

	public void setIdTipoComponenteDt(Long idTipoComponenteDt) {
		this.idTipoComponenteDt = idTipoComponenteDt;
	}

	public String getNomeComponenteDt() {
		return nomeComponenteDt;
	}

	public void setNomeComponenteDt(String nomeComponenteDt) {
		this.nomeComponenteDt = nomeComponenteDt;
	}

	public String getDesComponenteDt() {
		return desComponenteDt;
	}

	public void setDesComponenteDt(String desComponenteDt) {
		this.desComponenteDt = desComponenteDt;
	}

	public Date getDataInizioValidita() {
		return dataInizioValidita;
	}

	public void setDataInizioValidita(Date dataInizioValidita) {
		this.dataInizioValidita = dataInizioValidita;
	}

	public Date getDataFineValidita() {
		return dataFineValidita;
	}

	public void setDataFineValidita(Date dataFineValidita) {
		this.dataFineValidita = dataFineValidita;
	}

}

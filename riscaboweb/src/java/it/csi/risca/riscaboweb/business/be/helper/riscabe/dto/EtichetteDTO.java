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
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EtichetteDTO {
	
	@JsonProperty("id_etichetta")
	private Long idEtichetta;
	
	@JsonProperty("id_ambito")
	private Long idAmbito;
	
	@JsonProperty("cod_etichetta")
	private String codEtichetta;
	
    @JsonProperty("val_etichetta")
	private String valEtichetta;
    
    @JsonProperty("nota_etichetta")
	private String notaEtichetta;
    
    @JsonProperty("gest_data_ins")
	private Date gestDataIns;
    
    @JsonProperty("gest_attore_ins")
	private String gestAttoreIns;
    
    @JsonProperty("gest_data_upd")
	private Date gestDataUpd;
    
    @JsonProperty("gest_attore_upd")
	private String gestAttoreUpd;
    
    @JsonProperty("gest_uid")
	private String gestUid;
    
    

	public Long getIdEtichetta() {
		return idEtichetta;
	}

	public void setIdEtichetta(Long idEtichetta) {
		this.idEtichetta = idEtichetta;
	}

	public Long getIdAmbito() {
		return idAmbito;
	}

	public void setIdAmbito(Long idAmbito) {
		this.idAmbito = idAmbito;
	}

	public String getCodEtichetta() {
		return codEtichetta;
	}

	public void setCodEtichetta(String codEtichetta) {
		this.codEtichetta = codEtichetta;
	}

	public String getValEtichetta() {
		return valEtichetta;
	}

	public void setValEtichetta(String valEtichetta) {
		this.valEtichetta = valEtichetta;
	}

	public String getNotaEtichetta() {
		return notaEtichetta;
	}

	public void setNotaEtichetta(String notaEtichetta) {
		this.notaEtichetta = notaEtichetta;
	}

	public Date getGestDataIns() {
		return gestDataIns;
	}

	public void setGestDataIns(Date gestDataIns) {
		this.gestDataIns = gestDataIns;
	}

	public String getGestAttoreIns() {
		return gestAttoreIns;
	}

	public void setGestAttoreIns(String gestAttoreIns) {
		this.gestAttoreIns = gestAttoreIns;
	}

	public Date getGestDataUpd() {
		return gestDataUpd;
	}

	public void setGestDataUpd(Date gestDataUpd) {
		this.gestDataUpd = gestDataUpd;
	}

	public String getGestAttoreUpd() {
		return gestAttoreUpd;
	}

	public void setGestAttoreUpd(String gestAttoreUpd) {
		this.gestAttoreUpd = gestAttoreUpd;
	}

	public String getGestUid() {
		return gestUid;
	}

	public void setGestUid(String gestUid) {
		this.gestUid = gestUid;
	}
}
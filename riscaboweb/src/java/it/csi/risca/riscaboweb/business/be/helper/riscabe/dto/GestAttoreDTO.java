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

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * GestAttoreDTO
 *
 * @author CSI PIEMONTE
 */
public class GestAttoreDTO {
	
    @Size(max = 30, min = 0, message = "Il gest_attore_ins deve essere compreso tra 0 e 30 caratteri.")
	@JsonProperty("gest_attore_ins")
    private String gestAttoreIns;
	
	@JsonProperty("gest_data_ins")
    private Date gestDataIns;
	
    @Size(max = 30, min = 0, message = "Il gest_attore_upd deve essere compreso tra 0 e 30 caratteri.")
	@JsonProperty("gest_attore_upd")
    private String gestAttoreUpd;
	
	@JsonProperty("gest_data_upd")
    private Date gestDataUpd;

    @Size(max = 64, min = 0, message = "Il gest_uid deve essere compreso tra 0 e 64 caratteri.")
	@JsonProperty("gest_uid")
    private String gestUid;

	public String getGestAttoreIns() {
		return gestAttoreIns;
	}

	public void setGestAttoreIns(String gestAttoreIns) {
		this.gestAttoreIns = gestAttoreIns;
	}

	public Date getGestDataIns() {
		return gestDataIns;
	}

	public void setGestDataIns(Date gestDataIns) {
		this.gestDataIns = gestDataIns;
	}

	public String getGestAttoreUpd() {
		return gestAttoreUpd;
	}

	public void setGestAttoreUpd(String gestAttoreUpd) {
		this.gestAttoreUpd = gestAttoreUpd;
	}

	public Date getGestDataUpd() {
		return gestDataUpd;
	}

	public void setGestDataUpd(Date gestDataUpd) {
		this.gestDataUpd = gestDataUpd;
	}

	public String getGestUid() {
		return gestUid;
	}

	public void setGestUid(String gestUid) {
		this.gestUid = gestUid;
	}
	

}

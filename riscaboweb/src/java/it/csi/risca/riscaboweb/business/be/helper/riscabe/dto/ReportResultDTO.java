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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
public class ReportResultDTO {

	@JsonProperty("report_url")
	private String reportUrl;

	@JsonProperty("elabora")
	private ElaboraDTO elabora;
	
    @JsonProperty("id_risca")
    private Long idRisca;
    
    @JsonProperty("status")
    private String status;
    
    @JsonProperty("error")
    private  ErrorDTO errorDTO;
    
	@JsonProperty("timestamp")
	private String timestamp;
    
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getIdRisca() {
		return idRisca;
	}

	public void setIdRisca(Long idRisca) {
		this.idRisca = idRisca;
	}

	public String getReportUrl() {
		return reportUrl;
	}

	public void setReportUrl(String reportUrl) {
		this.reportUrl = reportUrl;
	}

	public ErrorDTO getErrorDTO() {
		return errorDTO;
	}

	public void setErrorDTO(ErrorDTO errorDTO) {
		this.errorDTO = errorDTO;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public ElaboraDTO getElabora() {
		return elabora;
	}

	public void setElabora(ElaboraDTO elabora) {
		this.elabora = elabora;
	}

	
	
}

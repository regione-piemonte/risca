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

import com.fasterxml.jackson.annotation.JsonProperty;

public class EmailStandardDTO {

	@JsonProperty("id_email_standard")
	private Long idEmailStandard;

	@JsonProperty("id_fase_elabora")
	private Long idFaseElabora;

	@JsonProperty("cod_email_standard")
	private String codEmailStandard;

	@JsonProperty("des_email_standard")
	private String desEmailStandard;

	@JsonProperty("oggetto_email")
	private String oggettoEmail;

	@JsonProperty("testo_email")
	private String testoEmail;

	@JsonProperty("allegato_email")
	private String allegatoEmail;

	public Long getIdEmailStandard() {
		return idEmailStandard;
	}

	public void setIdEmailStandard(Long idEmailStandard) {
		this.idEmailStandard = idEmailStandard;
	}

	public Long getIdFaseElabora() {
		return idFaseElabora;
	}

	public void setIdFaseElabora(Long idFaseElabora) {
		this.idFaseElabora = idFaseElabora;
	}

	public String getCodEmailStandard() {
		return codEmailStandard;
	}

	public void setCodEmailStandard(String codEmailStandard) {
		this.codEmailStandard = codEmailStandard;
	}

	public String getDesEmailStandard() {
		return desEmailStandard;
	}

	public void setDesEmailStandard(String desEmailStandard) {
		this.desEmailStandard = desEmailStandard;
	}

	public String getOggettoEmail() {
		return oggettoEmail;
	}

	public void setOggettoEmail(String oggettoEmail) {
		this.oggettoEmail = oggettoEmail;
	}

	public String getTestoEmail() {
		return testoEmail;
	}

	public void setTestoEmail(String testoEmail) {
		this.testoEmail = testoEmail;
	}

	public String getAllegatoEmail() {
		return allegatoEmail;
	}

	public void setAllegatoEmail(String allegatoEmail) {
		this.allegatoEmail = allegatoEmail;
	}

}

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

public class EmailServizioDTO {

	@JsonProperty("id_email_servizio")
	private Long idEmailServizio;

	@JsonProperty("cod_email_servizio")
	private String codEmailServizio;

	@JsonProperty("des_email_servizio")
	private String desEmailServizio;

	@JsonProperty("oggetto_email_servizio")
	private String oggettoEmailServizio;

	@JsonProperty("id_fase_elabora")
	private Long idFaseElabora;

	public Long getIdEmailServizio() {
		return idEmailServizio;
	}

	public void setIdEmailServizio(Long idEmailServizio) {
		this.idEmailServizio = idEmailServizio;
	}

	public String getCodEmailServizio() {
		return codEmailServizio;
	}

	public void setCodEmailServizio(String codEmailServizio) {
		this.codEmailServizio = codEmailServizio;
	}

	public String getDesEmailServizio() {
		return desEmailServizio;
	}

	public void setDesEmailServizio(String desEmailServizio) {
		this.desEmailServizio = desEmailServizio;
	}

	public String getOggettoEmailServizio() {
		return oggettoEmailServizio;
	}

	public void setOggettoEmailServizio(String oggettoEmailServizio) {
		this.oggettoEmailServizio = oggettoEmailServizio;
	}

	public Long getIdFaseElabora() {
		return idFaseElabora;
	}

	public void setIdFaseElabora(Long idFaseElabora) {
		this.idFaseElabora = idFaseElabora;
	}

}

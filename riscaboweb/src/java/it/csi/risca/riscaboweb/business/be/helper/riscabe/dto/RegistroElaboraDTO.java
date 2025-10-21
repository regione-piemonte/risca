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

public class RegistroElaboraDTO {

	@JsonProperty("id_registro_elabora")
	private Long idRegistroElabora;

	@JsonProperty("id_elabora")
	private Long idElabora;

	@JsonProperty("cod_passo_elabora")
	private String codPassoElabora;

	@JsonProperty("flg_esito_elabora")
	private int flgEsitoElabora;

	@JsonProperty("nota_elabora")
	private String notaElabora = null;
	
	public Long getIdRegistroElabora() {
		return idRegistroElabora;
	}

	public void setIdRegistroElabora(Long idRegistroElabora) {
		this.idRegistroElabora = idRegistroElabora;
	}

	public Long getIdElabora() {
		return idElabora;
	}

	public void setIdElabora(Long idElabora) {
		this.idElabora = idElabora;
	}

	public String getCodPassoElabora() {
		return codPassoElabora;
	}

	public void setCodPassoElabora(String codPassoElabora) {
		this.codPassoElabora = codPassoElabora;
	}

	public int getFlgEsitoElabora() {
		return flgEsitoElabora;
	}

	public void setFlgEsitoElabora(int flgEsitoElabora) {
		this.flgEsitoElabora = flgEsitoElabora;
	}

	public String getNotaElabora() {
		return notaElabora;
	}

	public void setNotaElabora(String notaElabora) {
		this.notaElabora = notaElabora;
	}

}

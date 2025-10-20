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

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BilAccResultDTO {

	@JsonProperty("id_pagamento")
	private Long idPagamento;

	@JsonProperty("status")
	private String status;

	@JsonProperty("message")
	private String message;

	@JsonProperty("listaIdPagDaRielaborare")
	private List<Long> listaIdPagDaRielaborare;

	public Long getIdPagamento() {
		return idPagamento;
	}

	public void setIdPagamento(Long idPagamento) {
		this.idPagamento = idPagamento;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<Long> getListaIdPagDaRielaborare() {
		return listaIdPagDaRielaborare;
	}

	public void setListaIdPagDaRielaborare(List<Long> listaIdPagDaRielaborare) {
		this.listaIdPagDaRielaborare = listaIdPagDaRielaborare;
	}

}

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

public class AssegnaPagamentoDTO {
	
	@JsonProperty("pagamento")
    private PagamentoExtendedDTO pagamento;	
	
	@JsonProperty("stati_debitori")
	private List<StatoDebitorioExtendedDTO> statiDebitori;
	
	@JsonProperty("dettagli_pagamento")
    private  List<DettaglioPagExtendedDTO> dettagliPagamento;	
	
	@JsonProperty("dettagli_pagamento_da_cancellare")
	private List<DettaglioPagExtendedDTO> dettagliPagamentoDaCancellare;

	public PagamentoExtendedDTO getPagamento() {
		return pagamento;
	}

	public void setPagamento(PagamentoExtendedDTO pagamento) {
		this.pagamento = pagamento;
	}

	public List<StatoDebitorioExtendedDTO> getStatiDebitori() {
		return statiDebitori;
	}

	public void setStatiDebitori(List<StatoDebitorioExtendedDTO> statiDebitori) {
		this.statiDebitori = statiDebitori;
	}

	public List<DettaglioPagExtendedDTO> getDettagliPagamento() {
		return dettagliPagamento;
	}

	public void setDettagliPagamento(List<DettaglioPagExtendedDTO> dettagliPagamento) {
		this.dettagliPagamento = dettagliPagamento;
	}

	public List<DettaglioPagExtendedDTO> getDettagliPagamentoDaCancellare() {
		return dettagliPagamentoDaCancellare;
	}

	public void setDettagliPagamentoDaCancellare(List<DettaglioPagExtendedDTO> dettagliPagamentoDaCancellare) {
		this.dettagliPagamentoDaCancellare = dettagliPagamentoDaCancellare;
	}

	
	
}
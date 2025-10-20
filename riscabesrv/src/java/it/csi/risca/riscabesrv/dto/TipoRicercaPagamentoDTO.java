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

/**
 * TipoRicercaPagamentoDTO
 *
 * @author CSI PIEMONTE
 */
public class TipoRicercaPagamentoDTO {
	
	@JsonProperty("id_tipo_ricerca_pagamento")
	private Long idTipoRicercaPagamento;

	@JsonProperty("cod_tipo_ricerca_pagamento")
	private String codTipoRicercaPagamento;

	@JsonProperty("des_tipo_ricerca_pagamento")
	private String desTipoRicercaPagamento;

	public Long getIdTipoRicercaPagamento() {
		return idTipoRicercaPagamento;
	}

	public void setIdTipoRicercaPagamento(Long idTipoRicercaPagamento) {
		this.idTipoRicercaPagamento = idTipoRicercaPagamento;
	}

	public String getCodTipoRicercaPagamento() {
		return codTipoRicercaPagamento;
	}

	public void setCodTipoRicercaPagamento(String codTipoRicercaPagamento) {
		this.codTipoRicercaPagamento = codTipoRicercaPagamento;
	}

	public String getDesTipoRicercaPagamento() {
		return desTipoRicercaPagamento;
	}

	public void setDesTipoRicercaPagamento(String desTipoRicercaPagamento) {
		this.desTipoRicercaPagamento = desTipoRicercaPagamento;
	}
	
	
}

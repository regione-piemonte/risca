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

import com.fasterxml.jackson.annotation.JsonProperty;

public class DettaglioPagExtendedDTO extends DettaglioPagDTO{

    @JsonProperty("rata")
    private RataSdDTO rataSd;
    
	@JsonProperty("pagamento")
    private PagamentoExtendedDTO pagamento;

	public PagamentoExtendedDTO getPagamento() {
		return pagamento;
	}

	public void setPagamento(PagamentoExtendedDTO pagamento) {
		this.pagamento = pagamento;
	}

	public RataSdDTO getRataSd() {
		return rataSd;
	}

	public void setRataSd(RataSdDTO rataSd) {
		this.rataSd = rataSd;
	}




	
}

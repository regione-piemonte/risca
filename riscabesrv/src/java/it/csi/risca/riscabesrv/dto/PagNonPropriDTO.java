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

import java.math.BigDecimal;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PagNonPropriDTO extends GestAttoreDTO {
	
    @Min(value = 1, message = "L' id_pag_non_propri deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_pag_non_propri supera il limite massimo consentito per Integer")
	@JsonProperty("id_pag_non_propri")
    private Long idPagNonPropri;
	
    @Min(value = 1, message = "L' id_pagamento deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_pagamento supera il limite massimo consentito per Integer")
	@JsonProperty("id_pagamento")
    private Long idPagamento;
	
    @Min(value = 1, message = "L' id_tipo_imp_non_propri deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_tipo_imp_non_propri supera il limite massimo consentito per Integer")
	@JsonProperty("id_tipo_imp_non_propri")
    private Long idTipoImpNonPropri;

    @Digits(integer = 13, fraction = 2, message = "importo_versato deve avere al massimo 13 cifre intere e 2 decimali")
    @JsonProperty("importo_versato")
    private BigDecimal importoVersato;
    
    @Size(max = 100, min = 0, message = "Il des_tipo_imp_non_propri deve essere compreso tra 0 e 100 caratteri.")
	@JsonProperty("des_tipo_imp_non_propri")
    private String desTipoImpNonPropri;


    public Long getIdPagNonPropri() {
		return idPagNonPropri;
	}

	public void setIdPagNonPropri(Long idPagNonPropri) {
		this.idPagNonPropri = idPagNonPropri;
	}

	public Long getIdPagamento() {
		return idPagamento;
	}

	public void setIdPagamento(Long idPagamento) {
		this.idPagamento = idPagamento;
	}

	public Long getIdTipoImpNonPropri() {
		return idTipoImpNonPropri;
	}

	public void setIdTipoImpNonPropri(Long idTipoImpNonPropri) {
		this.idTipoImpNonPropri = idTipoImpNonPropri;
	}

	public BigDecimal getImportoVersato() {
		return importoVersato;
	}

	public void setImportoVersato(BigDecimal importoVersato) {
		this.importoVersato = importoVersato;
	}

	public String getDesTipoImpNonPropri() {
		return desTipoImpNonPropri;
	}

	public void setDesTipoImpNonPropri(String desTipoImpNonPropri) {
		this.desTipoImpNonPropri = desTipoImpNonPropri;
	}
	
}

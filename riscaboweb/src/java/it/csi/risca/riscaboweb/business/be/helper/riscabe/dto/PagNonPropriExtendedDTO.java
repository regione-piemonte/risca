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

public class PagNonPropriExtendedDTO extends PagNonPropriDTO {
	
	@JsonProperty("tipo_imp_non_propri")
    private TipoImpNonPropriDTO tipoImpNonPropri;

	public TipoImpNonPropriDTO getTipoImpNonPropri() {
		return tipoImpNonPropri;
	}

	public void setTipoImpNonPropri(TipoImpNonPropriDTO tipoImpNonPropri) {
		this.tipoImpNonPropri = tipoImpNonPropri;
	}
	
}

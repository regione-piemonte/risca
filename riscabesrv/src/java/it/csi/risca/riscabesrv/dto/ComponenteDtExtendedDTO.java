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

public class ComponenteDtExtendedDTO extends ComponenteDtDTO {
    
	@JsonProperty("tipo_componente_dt")
    private TipoComponenteDtDTO tipoComponenteDt;

	public TipoComponenteDtDTO getTipoComponenteDt() {
		return tipoComponenteDt;
	}

	public void setTipoComponenteDt(TipoComponenteDtDTO tipoComponenteDt) {
		this.tipoComponenteDt = tipoComponenteDt;
	}

}

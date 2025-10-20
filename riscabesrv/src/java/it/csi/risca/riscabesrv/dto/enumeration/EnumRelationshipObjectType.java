/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.dto.enumeration;

public enum EnumRelationshipObjectType {

	DOCUMENT_COMPOSITION_PROPERTIES_TYPE("DocumentCompositionPropertiesType");
	
    private final String descrizione;
    
    EnumRelationshipObjectType(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getDescrizione() {
        return descrizione;
    }
}

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

public enum TipoRuolo {

    AMMINISTRATORE("AMMINISTRATORE"),

    GESTORE_BASE("GESTORE_BASE"),
	
	GESTORE_DATI("GESTORE_DATI"),
	
	CONSULTATORE("CONSULTATORE");

    private final String descrizione;

    TipoRuolo(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getDescrizione() {
        return descrizione;
    }
}

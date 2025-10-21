/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.tributi;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DatoTecnicoJsonDTO
 *
 * @author CSI PIEMONTE
 */

public class DatoTecnicoJsonDTO {
	
	@JsonProperty("gest_UID")
    private String gestUID;

    @JsonProperty("id_riscossione")
    private Long idRiscossione;
    
    @JsonProperty("data_modifica")
    private String dataModifica;

    @JsonProperty("data_inserimento")
    private String dataInserimento;
    
	@JsonProperty("dati_tecnici")
    private String datiTecnici;
	
	
	public String getGestUID() {
		return gestUID;
	}

	public void setGestUID(String gestUID) {
		this.gestUID = gestUID;
	}

	public Long getIdRiscossione() {
		return idRiscossione;
	}

	public void setIdRiscossione(Long idRiscossione) {
		this.idRiscossione = idRiscossione;
	}

	public String getDataModifica() {
		return dataModifica;
	}

	public void setDataModifica(String dataModifica) {
		this.dataModifica = dataModifica;
	}

	public String getDataInserimento() {
		return dataInserimento;
	}

	public void setDataInserimento(String dataInserimento) {
		this.dataInserimento = dataInserimento;
	}

	public String getDatiTecnici() {
		return datiTecnici;
	}

	public void setDatiTecnici(String datiTecnici) {
		this.datiTecnici = datiTecnici;
	}
	
}

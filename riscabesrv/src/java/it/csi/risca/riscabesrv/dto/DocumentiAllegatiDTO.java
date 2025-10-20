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
 * DocumentiAllegatiDTO
 *
 * @author CSI PIEMONTE
 */

public class DocumentiAllegatiDTO {
	
	@JsonProperty("id_riscossione")
    private Long idRiscossione;
	
	@JsonProperty("paginazione")
    private Long paginazione;
	
	@JsonProperty("ACTArepositoryId")
    private String actaRepositoryId;

	@JsonProperty("ACTAcodiceFiscale")
    private String actaCodiceFiscale;
	
	@JsonProperty("ACTAclientApplicationInfo")
    private String actaClientApplicationInfo;
	
	@JsonProperty("idClassifcazioneDoc")
    private String idClassifcazioneDoc;
	
	@JsonProperty("idClassificazioneAll")
    private String idClassificazioneAll;
    

	public Long getIdRiscossione() {
		return idRiscossione;
	}

	public void setIdRiscossione(Long idRiscossione) {
		this.idRiscossione = idRiscossione;
	}

	public Long getPaginazione() {
		return paginazione;
	}

	public void setPaginazione(Long paginazione) {
		this.paginazione = paginazione;
	}

	public String getActaRepositoryId() {
		return actaRepositoryId;
	}

	public void setActaRepositoryId(String actaRepositoryId) {
		this.actaRepositoryId = actaRepositoryId;
	}

	public String getActaCodiceFiscale() {
		return actaCodiceFiscale;
	}

	public void setActaCodiceFiscale(String actaCodiceFiscale) {
		this.actaCodiceFiscale = actaCodiceFiscale;
	}

	public String getActaClientApplicationInfo() {
		return actaClientApplicationInfo;
	}

	public void setActaClientApplicationInfo(String actaClientApplicationInfo) {
		this.actaClientApplicationInfo = actaClientApplicationInfo;
	}
	
	public String getIdClassifcazioneDoc() {
		return idClassifcazioneDoc;
	}

	public void setIdClassifcazioneDoc(String idClassifcazioneDoc) {
		this.idClassifcazioneDoc = idClassifcazioneDoc;
	}

	public String getIdClassificazioneAll() {
		return idClassificazioneAll;
	}

	public void setIdClassificazioneAll(String idClassificazioneAll) {
		this.idClassificazioneAll = idClassificazioneAll;
	}
	
}

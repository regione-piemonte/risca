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
 * The type Ambito Config dto.
 *
 * @author CSI PIEMONTE
 */
public class AmbitoConfigDTO extends GestAttoreDTO{

	@JsonProperty("id_ambito_config")
    private Long idAmbitoConfig;

    @JsonProperty("id_ambito")
    private Long idAmbito;

    @JsonProperty("chiave")
    private String chiave;
    
    @JsonProperty("valore")
    private String valore;
    
//    @JsonProperty("ordinamento")
//    private Long ordinamento;

    @JsonProperty("flg_attivo")
    private String flgAttivo;

    @JsonProperty("note")
    private String note;

    public Long getIdAmbitoConfig() {
		return idAmbitoConfig;
	}

	public void setIdAmbitoConfig(Long idAmbitoConfig) {
		this.idAmbitoConfig = idAmbitoConfig;
	}

	public Long getIdAmbito() {
		return idAmbito;
	}

	public void setIdAmbito(Long idAmbito) {
		this.idAmbito = idAmbito;
	}

	public String getChiave() {
		return chiave;
	}

	public void setChiave(String chiave) {
		this.chiave = chiave;
	}

	public String getValore() {
		return valore;
	}

	public void setValore(String valore) {
		this.valore = valore;
	}

//	public Long getOrdinamento() {
//		return ordinamento;
//	}
//
//	public void setOrdinamento(Long ordinamento) {
//		this.ordinamento = ordinamento;
//	}

	public String getFlgAttivo() {
		return flgAttivo;
	}

	public void setFlgAttivo(String flgAttivo) {
		this.flgAttivo = flgAttivo;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
}
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

public class ProfiloOggAppDTO {
	
	@JsonProperty("id_profilo_pa")
    private Long idProfiloPa;

    @JsonProperty("oggetto_app")
    private OggettoAppDTO oggettoApp;
    
    @JsonProperty("flg_attivo")
    private Boolean flgAttivo;

	public Long getIdProfiloPa() {
		return idProfiloPa;
	}

	public void setIdProfiloPa(Long idProfiloPa) {
		this.idProfiloPa = idProfiloPa;
	}

	public OggettoAppDTO getOggettoApp() {
		return oggettoApp;
	}

	public void setOggettoApp(OggettoAppDTO oggettoApp) {
		this.oggettoApp = oggettoApp;
	}

	public Boolean getFlgAttivo() {
		return flgAttivo;
	}

	public void setFlgAttivo(Boolean flgAttivo) {
		this.flgAttivo = flgAttivo;
	}
    

	
}

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

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;


public class ProfilazioneDTO {
	
	
    @JsonProperty("profilo_pa")
    private ProfiloPaDTO profiloPa;
	
    @JsonProperty("profilo_oggetto_app")
    private List<ProfiloOggAppDTO> profiloOggettoApp;

    
	public ProfiloPaDTO getProfiloPa() {
		return profiloPa;
	}

	public void setProfiloPa(ProfiloPaDTO profiloPa) {
		this.profiloPa = profiloPa;
	}

	public List<ProfiloOggAppDTO> getProfiloOggettoApp() {
		return profiloOggettoApp;
	}

	public void setProfiloOggettoApp(List<ProfiloOggAppDTO> profiloOggettoApp) {
		this.profiloOggettoApp = profiloOggettoApp;
	}
	
    

}

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

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.csi.risca.riscabesrv.business.be.helper.scriva.dto.ComuneExtendedDTO;
import it.csi.risca.riscabesrv.business.be.helper.scriva.dto.NazioneDTO;

/**
 * SoggettiDTO
 *
 * @author CSI PIEMONTE
 */

public class SoggettiExtendedDTO extends SoggettiDTO {
	
//	@JsonProperty("ambito")
//    private AmbitoDTO ambito;
	
	@JsonProperty("tipo_soggetto")
    private TipiSoggettoDTO tipoSoggetto;
    
	@JsonProperty("tipo_natura_giuridica")
    private TipiNaturaGiuridicaDTO tipiNaturaGiuridica;

	@JsonProperty("recapiti")
	private List<RecapitiExtendedDTO> recapiti;
	
	@JsonProperty("fonte")
    private FonteDTO Fonte;
	
	@JsonProperty("comune_nascita")
    private ComuneExtendedDTO comuneNascita;
	
	@JsonProperty("nazione_nascita")
    private NazioneDTO nazioneNascita;
	
	@JsonProperty("gruppo_soggetto")
    private List<GruppiDTO> gruppoSoggetto;

//	public AmbitoDTO getAmbito() {
//		return ambito;
//	}
//
//	public void setAmbito(AmbitoDTO ambito) {
//		this.ambito = ambito;
//	}

	public TipiSoggettoDTO getTipoSoggetto() {
		return tipoSoggetto;
	}

	public void setTipoSoggetto(TipiSoggettoDTO tipoSoggetto) {
		this.tipoSoggetto = tipoSoggetto;
	}

	public TipiNaturaGiuridicaDTO getTipiNaturaGiuridica() {
		return tipiNaturaGiuridica;
	}

	public void setTipiNaturaGiuridica(TipiNaturaGiuridicaDTO tipiNaturaGiuridica) {
		this.tipiNaturaGiuridica = tipiNaturaGiuridica;
	}


	public List<RecapitiExtendedDTO> getRecapiti() {
		return recapiti;
	}

	public void setRecapiti(List<RecapitiExtendedDTO> recapiti) {
		this.recapiti = recapiti;
	}
    
	public FonteDTO getFonte() {
		return Fonte;
	}

	public void setFonte(FonteDTO fonte) {
		Fonte = fonte;
	}

	public ComuneExtendedDTO getComuneNascita() {
		return comuneNascita;
	}

	public void setComuneNascita(ComuneExtendedDTO comuneNascita) {
		this.comuneNascita = comuneNascita;
	}

	public NazioneDTO getNazioneNascita() {
		return nazioneNascita;
	}

	public void setNazioneNascita(NazioneDTO nazioneNascita) {
		this.nazioneNascita = nazioneNascita;
	}

	public List<GruppiDTO> getGruppoSoggetto() {
		return gruppoSoggetto;
	}

	public void setGruppoSoggetto(List<GruppiDTO> gruppoSoggetto) {
		this.gruppoSoggetto = gruppoSoggetto;
	}



}

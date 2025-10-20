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
 * ProvinceExtendedDTO
 *
 * @author CSI PIEMONTE
 */
public class ProvinceExtendedDTO extends ProvinciaDTO {

	@JsonProperty("comune")
    private ComuneDTO comune;
	
	@JsonProperty("regione")
    private RegioneDTO regione;
	
//	@JsonProperty("nazione")
//    private NazioneDTO nazione;
	
	public ComuneDTO getComune() {
		return comune;
	}

	public void setComune(ComuneDTO comune) {
		this.comune = comune;
	}

	public RegioneDTO getRegione() {
		return regione;
	}

	public void setRegione(RegioneDTO regione) {
		this.regione = regione;
	}

//	public NazioneDTO getNazione() {
//		return nazione;
//	}
//
//	public void setNazione(NazioneDTO nazione) {
//		this.nazione = nazione;
//	}

	@Override
	public String toString() {
		return "ProvinceExtendedDTO [comune=" + comune + ", regione=" + regione + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((comune == null) ? 0 : comune.hashCode());
		result = prime * result + ((regione == null) ? 0 : regione.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProvinceExtendedDTO other = (ProvinceExtendedDTO) obj;
		if (comune == null) {
			if (other.comune != null)
				return false;
		} else if (!comune.equals(other.comune))
			return false;
		if (regione == null) {
			if (other.regione != null)
				return false;
		} else if (!regione.equals(other.regione))
			return false;
		return true;
	}
}

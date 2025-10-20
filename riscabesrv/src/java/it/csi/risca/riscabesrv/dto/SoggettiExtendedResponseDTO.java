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

import java.util.Objects;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * SoggettiExtendedResponseDTO
 */
@Validated




public class SoggettiExtendedResponseDTO   {
  @JsonProperty("soggetto")
  private SoggettiExtendedDTO soggetto = null;

  @JsonProperty("fonti_ambito")
  private String fontiAmbito = null;

  @JsonProperty("is_abilitato")
  private String isAbilitato = null;

  @JsonProperty("is_gestione_abilitata")
  private String isGestioneAbilitata = null;

  @JsonProperty("is_fonte_abilitata_in_lettura")
  private String isFonteAbilitataInLettura = null;

  @JsonProperty("is_fonte_abilitata_in_scrittura")
  private String isFonteAbilitataInScrittura = null;

  @JsonProperty("codice_messaggio")
  private String codiceMessaggio = null;

  @JsonProperty("lista_campi_aggiornati")
  private String listaCampiAggiornati = null;

  @JsonProperty("fonte_ricerca")
  private String fonteRicerca = null;


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SoggettiExtendedResponseDTO soggettiExtendedResponseDTO = (SoggettiExtendedResponseDTO) o;
    return Objects.equals(this.soggetto, soggettiExtendedResponseDTO.soggetto) &&
        Objects.equals(this.fontiAmbito, soggettiExtendedResponseDTO.fontiAmbito) &&
        Objects.equals(this.isAbilitato, soggettiExtendedResponseDTO.isAbilitato) &&
        Objects.equals(this.isGestioneAbilitata, soggettiExtendedResponseDTO.isGestioneAbilitata) &&
        Objects.equals(this.isFonteAbilitataInLettura, soggettiExtendedResponseDTO.isFonteAbilitataInLettura) &&
        Objects.equals(this.isFonteAbilitataInScrittura, soggettiExtendedResponseDTO.isFonteAbilitataInScrittura) &&
        Objects.equals(this.codiceMessaggio, soggettiExtendedResponseDTO.codiceMessaggio) &&
        Objects.equals(this.listaCampiAggiornati, soggettiExtendedResponseDTO.listaCampiAggiornati);
  }

  @Override
  public int hashCode() {
    return Objects.hash(soggetto, fontiAmbito, isAbilitato, isGestioneAbilitata, isFonteAbilitataInLettura, isFonteAbilitataInScrittura, codiceMessaggio, listaCampiAggiornati);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SoggettiExtendedResponseDTO {\n");
    
    sb.append("    soggetto: ").append(toIndentedString(soggetto)).append("\n");
    sb.append("    fontiAmbito: ").append(toIndentedString(fontiAmbito)).append("\n");
    sb.append("    isAbilitato: ").append(toIndentedString(isAbilitato)).append("\n");
    sb.append("    isGestioneAbilitata: ").append(toIndentedString(isGestioneAbilitata)).append("\n");
    sb.append("    isFonteAbilitataInLettura: ").append(toIndentedString(isFonteAbilitataInLettura)).append("\n");
    sb.append("    isFonteAbilitataInScrittura: ").append(toIndentedString(isFonteAbilitataInScrittura)).append("\n");
    sb.append("    codiceMessaggio: ").append(toIndentedString(codiceMessaggio)).append("\n");
    sb.append("    listaCampiAggiornati: ").append(toIndentedString(listaCampiAggiornati)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

	public SoggettiExtendedDTO getSoggetto() {
		return soggetto;
	}
	
	public void setSoggetto(SoggettiExtendedDTO soggetto) {
		this.soggetto = soggetto;
	}
	
	public String getFontiAmbito() {
		return fontiAmbito;
	}
	
	public void setFontiAmbito(String fontiAmbito) {
		this.fontiAmbito = fontiAmbito;
	}
	
	public String getIsAbilitato() {
		return isAbilitato;
	}
	
	public void setIsAbilitato(String isAbilitato) {
		this.isAbilitato = isAbilitato;
	}
	
	public String getIsGestioneAbilitata() {
		return isGestioneAbilitata;
	}
	
	public void setIsGestioneAbilitata(String isGestioneAbilitata) {
		this.isGestioneAbilitata = isGestioneAbilitata;
	}
	
	public String getIsFonteAbilitataInLettura() {
		return isFonteAbilitataInLettura;
	}
	
	public void setIsFonteAbilitataInLettura(String isFonteAbilitataInLettura) {
		this.isFonteAbilitataInLettura = isFonteAbilitataInLettura;
	}
	
	public String getIsFonteAbilitataInScrittura() {
		return isFonteAbilitataInScrittura;
	}
	
	public void setIsFonteAbilitataInScrittura(String isFonteAbilitataInScrittura) {
		this.isFonteAbilitataInScrittura = isFonteAbilitataInScrittura;
	}
	
	public String getCodiceMessaggio() {
		return codiceMessaggio;
	}
	
	public void setCodiceMessaggio(String codiceMessaggio) {
		this.codiceMessaggio = codiceMessaggio;
	}
	
	public String getListaCampiAggiornati() {
		return listaCampiAggiornati;
	}
	
	public void setListaCampiAggiornati(String listaCampiAggiornati) {
		this.listaCampiAggiornati = listaCampiAggiornati;
	}

	public String getFonteRicerca() {
		return fonteRicerca;
	}

	public void setFonteRicerca(String fonteRicerca) {
		this.fonteRicerca = fonteRicerca;
	}
	
}


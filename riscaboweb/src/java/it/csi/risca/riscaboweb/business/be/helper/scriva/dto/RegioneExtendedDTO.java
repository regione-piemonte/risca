/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscaboweb.business.be.helper.scriva.dto;

import java.util.Date;
import java.util.Objects;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * RegioneExtendedDTO
 */
@Validated




public class RegioneExtendedDTO   {
  @JsonProperty("cod_regione")
  private String codRegione = null;

  @JsonProperty("data_fine_validita")
  private Date dataFineValidita = null;

  @JsonProperty("data_inizio_validita")
  private Date dataInizioValidita = null;

  @JsonProperty("denom_regione")
  private String denomRegione = null;

  @JsonProperty("id_nazione")
  private Long idNazione = null;

  @JsonProperty("id_regione")
  private Long idRegione = null;

  @JsonProperty("nazione")
  private NazioneDTO nazione = null;

  public RegioneExtendedDTO codRegione(String codRegione) {
    this.codRegione = codRegione;
    return this;
  }

  /**
   * Get codRegione
   * @return codRegione
  **/
  @ApiModelProperty(value = "")


  public String getCodRegione() {
    return codRegione;
  }

  public void setCodRegione(String codRegione) {
    this.codRegione = codRegione;
  }

  public RegioneExtendedDTO dataFineValidita(Date dataFineValidita) {
    this.dataFineValidita = dataFineValidita;
    return this;
  }

  /**
   * Get dataFineValidita
   * @return dataFineValidita
  **/
  @ApiModelProperty(value = "")

  @Valid

  public Date getDataFineValidita() {
    return dataFineValidita;
  }

  public void setDataFineValidita(Date dataFineValidita) {
    this.dataFineValidita = dataFineValidita;
  }

  public RegioneExtendedDTO dataInizioValidita(Date dataInizioValidita) {
    this.dataInizioValidita = dataInizioValidita;
    return this;
  }

  /**
   * Get dataInizioValidita
   * @return dataInizioValidita
  **/
  @ApiModelProperty(value = "")

  @Valid

  public Date getDataInizioValidita() {
    return dataInizioValidita;
  }

  public void setDataInizioValidita(Date dataInizioValidita) {
    this.dataInizioValidita = dataInizioValidita;
  }

  public RegioneExtendedDTO denomRegione(String denomRegione) {
    this.denomRegione = denomRegione;
    return this;
  }

  /**
   * Get denomRegione
   * @return denomRegione
  **/
  @ApiModelProperty(value = "")


  public String getDenomRegione() {
    return denomRegione;
  }

  public void setDenomRegione(String denomRegione) {
    this.denomRegione = denomRegione;
  }

  public RegioneExtendedDTO idNazione(Long idNazione) {
    this.idNazione = idNazione;
    return this;
  }

  /**
   * Get idNazione
   * @return idNazione
  **/
  @ApiModelProperty(value = "")


  public Long getIdNazione() {
    return idNazione;
  }

  public void setIdNazione(Long idNazione) {
    this.idNazione = idNazione;
  }

  public RegioneExtendedDTO idRegione(Long idRegione) {
    this.idRegione = idRegione;
    return this;
  }

  /**
   * Get idRegione
   * @return idRegione
  **/
  @ApiModelProperty(value = "")


  public Long getIdRegione() {
    return idRegione;
  }

  public void setIdRegione(Long idRegione) {
    this.idRegione = idRegione;
  }

  public RegioneExtendedDTO nazione(NazioneDTO nazione) {
    this.nazione = nazione;
    return this;
  }

  /**
   * Get nazione
   * @return nazione
  **/
  @ApiModelProperty(value = "")

  @Valid

  public NazioneDTO getNazione() {
    return nazione;
  }

  public void setNazione(NazioneDTO nazione) {
    this.nazione = nazione;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RegioneExtendedDTO regioneExtendedDTO = (RegioneExtendedDTO) o;
    return Objects.equals(this.codRegione, regioneExtendedDTO.codRegione) &&
        Objects.equals(this.dataFineValidita, regioneExtendedDTO.dataFineValidita) &&
        Objects.equals(this.dataInizioValidita, regioneExtendedDTO.dataInizioValidita) &&
        Objects.equals(this.denomRegione, regioneExtendedDTO.denomRegione) &&
        Objects.equals(this.idNazione, regioneExtendedDTO.idNazione) &&
        Objects.equals(this.idRegione, regioneExtendedDTO.idRegione) &&
        Objects.equals(this.nazione, regioneExtendedDTO.nazione);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codRegione, dataFineValidita, dataInizioValidita, denomRegione, idNazione, idRegione, nazione);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RegioneExtendedDTO {\n");
    
    sb.append("    codRegione: ").append(toIndentedString(codRegione)).append("\n");
    sb.append("    dataFineValidita: ").append(toIndentedString(dataFineValidita)).append("\n");
    sb.append("    dataInizioValidita: ").append(toIndentedString(dataInizioValidita)).append("\n");
    sb.append("    denomRegione: ").append(toIndentedString(denomRegione)).append("\n");
    sb.append("    idNazione: ").append(toIndentedString(idNazione)).append("\n");
    sb.append("    idRegione: ").append(toIndentedString(idRegione)).append("\n");
    sb.append("    nazione: ").append(toIndentedString(nazione)).append("\n");
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
}


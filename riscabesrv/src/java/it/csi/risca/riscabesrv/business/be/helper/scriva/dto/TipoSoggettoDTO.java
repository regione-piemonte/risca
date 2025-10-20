/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.business.be.helper.scriva.dto;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * TipoSoggettoDTO
 */
@Validated




public class TipoSoggettoDTO   {
  @JsonProperty("cod_tipo_soggetto")
  private String codTipoSoggetto = null;

  @JsonProperty("des_tipo_soggetto")
  private String desTipoSoggetto = null;

  @JsonProperty("gestAttoreIns")
  private String gestAttoreIns = null;

  @JsonProperty("gestAttoreUpd")
  private String gestAttoreUpd = null;

  @JsonProperty("gestUID")
  private String gestUID = null;

  @JsonProperty("id_tipo_soggetto")
  private Long idTipoSoggetto = null;

  public TipoSoggettoDTO codTipoSoggetto(String codTipoSoggetto) {
    this.codTipoSoggetto = codTipoSoggetto;
    return this;
  }

  /**
   * Get codTipoSoggetto
   * @return codTipoSoggetto
  **/
  @ApiModelProperty(value = "")


  public String getCodTipoSoggetto() {
    return codTipoSoggetto;
  }

  public void setCodTipoSoggetto(String codTipoSoggetto) {
    this.codTipoSoggetto = codTipoSoggetto;
  }

  public TipoSoggettoDTO desTipoSoggetto(String desTipoSoggetto) {
    this.desTipoSoggetto = desTipoSoggetto;
    return this;
  }

  /**
   * Get desTipoSoggetto
   * @return desTipoSoggetto
  **/
  @ApiModelProperty(value = "")


  public String getDesTipoSoggetto() {
    return desTipoSoggetto;
  }

  public void setDesTipoSoggetto(String desTipoSoggetto) {
    this.desTipoSoggetto = desTipoSoggetto;
  }

  public TipoSoggettoDTO gestAttoreIns(String gestAttoreIns) {
    this.gestAttoreIns = gestAttoreIns;
    return this;
  }

  /**
   * Get gestAttoreIns
   * @return gestAttoreIns
  **/
  @ApiModelProperty(value = "")


  public String getGestAttoreIns() {
    return gestAttoreIns;
  }

  public void setGestAttoreIns(String gestAttoreIns) {
    this.gestAttoreIns = gestAttoreIns;
  }

  public TipoSoggettoDTO gestAttoreUpd(String gestAttoreUpd) {
    this.gestAttoreUpd = gestAttoreUpd;
    return this;
  }

  /**
   * Get gestAttoreUpd
   * @return gestAttoreUpd
  **/
  @ApiModelProperty(value = "")


  public String getGestAttoreUpd() {
    return gestAttoreUpd;
  }

  public void setGestAttoreUpd(String gestAttoreUpd) {
    this.gestAttoreUpd = gestAttoreUpd;
  }

  public TipoSoggettoDTO gestUID(String gestUID) {
    this.gestUID = gestUID;
    return this;
  }

  /**
   * Get gestUID
   * @return gestUID
  **/
  @ApiModelProperty(value = "")


  public String getGestUID() {
    return gestUID;
  }

  public void setGestUID(String gestUID) {
    this.gestUID = gestUID;
  }

  public TipoSoggettoDTO idTipoSoggetto(Long idTipoSoggetto) {
    this.idTipoSoggetto = idTipoSoggetto;
    return this;
  }

  /**
   * Get idTipoSoggetto
   * @return idTipoSoggetto
  **/
  @ApiModelProperty(value = "")


  public Long getIdTipoSoggetto() {
    return idTipoSoggetto;
  }

  public void setIdTipoSoggetto(Long idTipoSoggetto) {
    this.idTipoSoggetto = idTipoSoggetto;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TipoSoggettoDTO tipoSoggettoDTO = (TipoSoggettoDTO) o;
    return Objects.equals(this.codTipoSoggetto, tipoSoggettoDTO.codTipoSoggetto) &&
        Objects.equals(this.desTipoSoggetto, tipoSoggettoDTO.desTipoSoggetto) &&
        Objects.equals(this.gestAttoreIns, tipoSoggettoDTO.gestAttoreIns) &&
        Objects.equals(this.gestAttoreUpd, tipoSoggettoDTO.gestAttoreUpd) &&
        Objects.equals(this.gestUID, tipoSoggettoDTO.gestUID) &&
        Objects.equals(this.idTipoSoggetto, tipoSoggettoDTO.idTipoSoggetto);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codTipoSoggetto, desTipoSoggetto, gestAttoreIns, gestAttoreUpd, gestUID, idTipoSoggetto);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TipoSoggettoDTO {\n");
    
    sb.append("    codTipoSoggetto: ").append(toIndentedString(codTipoSoggetto)).append("\n");
    sb.append("    desTipoSoggetto: ").append(toIndentedString(desTipoSoggetto)).append("\n");
    sb.append("    gestAttoreIns: ").append(toIndentedString(gestAttoreIns)).append("\n");
    sb.append("    gestAttoreUpd: ").append(toIndentedString(gestAttoreUpd)).append("\n");
    sb.append("    gestUID: ").append(toIndentedString(gestUID)).append("\n");
    sb.append("    idTipoSoggetto: ").append(toIndentedString(idTipoSoggetto)).append("\n");
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


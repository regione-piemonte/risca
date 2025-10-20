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
 * TipoNaturaGiuridicaDTO
 */
@Validated




public class TipoNaturaGiuridicaDTO   {
  @JsonProperty("cod_tipo_natura_giuridica")
  private String codTipoNaturaGiuridica = null;

  @JsonProperty("des_tipo_natura_giuridica")
  private String desTipoNaturaGiuridica = null;

  @JsonProperty("flg_pubblico")
  private Boolean flgPubblico = null;

  @JsonProperty("id_tipo_natura_giuridica")
  private Long idTipoNaturaGiuridica = null;

  @JsonProperty("ordinamento_tipo_natura_giu")
  private Long ordinamentoTipoNaturaGiu = null;

  @JsonProperty("sigla_tipo_natura_giuridica")
  private String siglaTipoNaturaGiuridica = null;

  public TipoNaturaGiuridicaDTO codTipoNaturaGiuridica(String codTipoNaturaGiuridica) {
    this.codTipoNaturaGiuridica = codTipoNaturaGiuridica;
    return this;
  }

  /**
   * Get codTipoNaturaGiuridica
   * @return codTipoNaturaGiuridica
  **/
  @ApiModelProperty(value = "")


  public String getCodTipoNaturaGiuridica() {
    return codTipoNaturaGiuridica;
  }

  public void setCodTipoNaturaGiuridica(String codTipoNaturaGiuridica) {
    this.codTipoNaturaGiuridica = codTipoNaturaGiuridica;
  }

  public TipoNaturaGiuridicaDTO desTipoNaturaGiuridica(String desTipoNaturaGiuridica) {
    this.desTipoNaturaGiuridica = desTipoNaturaGiuridica;
    return this;
  }

  /**
   * Get desTipoNaturaGiuridica
   * @return desTipoNaturaGiuridica
  **/
  @ApiModelProperty(value = "")


  public String getDesTipoNaturaGiuridica() {
    return desTipoNaturaGiuridica;
  }

  public void setDesTipoNaturaGiuridica(String desTipoNaturaGiuridica) {
    this.desTipoNaturaGiuridica = desTipoNaturaGiuridica;
  }

  public TipoNaturaGiuridicaDTO flgPubblico(Boolean flgPubblico) {
    this.flgPubblico = flgPubblico;
    return this;
  }

  /**
   * Get flgPubblico
   * @return flgPubblico
  **/
  @ApiModelProperty(value = "")


  public Boolean isFlgPubblico() {
    return flgPubblico;
  }

  public void setFlgPubblico(Boolean flgPubblico) {
    this.flgPubblico = flgPubblico;
  }

  public TipoNaturaGiuridicaDTO idTipoNaturaGiuridica(Long idTipoNaturaGiuridica) {
    this.idTipoNaturaGiuridica = idTipoNaturaGiuridica;
    return this;
  }

  /**
   * Get idTipoNaturaGiuridica
   * @return idTipoNaturaGiuridica
  **/
  @ApiModelProperty(value = "")


  public Long getIdTipoNaturaGiuridica() {
    return idTipoNaturaGiuridica;
  }

  public void setIdTipoNaturaGiuridica(Long idTipoNaturaGiuridica) {
    this.idTipoNaturaGiuridica = idTipoNaturaGiuridica;
  }

  public TipoNaturaGiuridicaDTO ordinamentoTipoNaturaGiu(Long ordinamentoTipoNaturaGiu) {
    this.ordinamentoTipoNaturaGiu = ordinamentoTipoNaturaGiu;
    return this;
  }

  /**
   * Get ordinamentoTipoNaturaGiu
   * @return ordinamentoTipoNaturaGiu
  **/
  @ApiModelProperty(value = "")


  public Long getOrdinamentoTipoNaturaGiu() {
    return ordinamentoTipoNaturaGiu;
  }

  public void setOrdinamentoTipoNaturaGiu(Long ordinamentoTipoNaturaGiu) {
    this.ordinamentoTipoNaturaGiu = ordinamentoTipoNaturaGiu;
  }

  public TipoNaturaGiuridicaDTO siglaTipoNaturaGiuridica(String siglaTipoNaturaGiuridica) {
    this.siglaTipoNaturaGiuridica = siglaTipoNaturaGiuridica;
    return this;
  }

  /**
   * Get siglaTipoNaturaGiuridica
   * @return siglaTipoNaturaGiuridica
  **/
  @ApiModelProperty(value = "")


  public String getSiglaTipoNaturaGiuridica() {
    return siglaTipoNaturaGiuridica;
  }

  public void setSiglaTipoNaturaGiuridica(String siglaTipoNaturaGiuridica) {
    this.siglaTipoNaturaGiuridica = siglaTipoNaturaGiuridica;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TipoNaturaGiuridicaDTO tipoNaturaGiuridicaDTO = (TipoNaturaGiuridicaDTO) o;
    return Objects.equals(this.codTipoNaturaGiuridica, tipoNaturaGiuridicaDTO.codTipoNaturaGiuridica) &&
        Objects.equals(this.desTipoNaturaGiuridica, tipoNaturaGiuridicaDTO.desTipoNaturaGiuridica) &&
        Objects.equals(this.flgPubblico, tipoNaturaGiuridicaDTO.flgPubblico) &&
        Objects.equals(this.idTipoNaturaGiuridica, tipoNaturaGiuridicaDTO.idTipoNaturaGiuridica) &&
        Objects.equals(this.ordinamentoTipoNaturaGiu, tipoNaturaGiuridicaDTO.ordinamentoTipoNaturaGiu) &&
        Objects.equals(this.siglaTipoNaturaGiuridica, tipoNaturaGiuridicaDTO.siglaTipoNaturaGiuridica);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codTipoNaturaGiuridica, desTipoNaturaGiuridica, flgPubblico, idTipoNaturaGiuridica, ordinamentoTipoNaturaGiu, siglaTipoNaturaGiuridica);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TipoNaturaGiuridicaDTO {\n");
    
    sb.append("    codTipoNaturaGiuridica: ").append(toIndentedString(codTipoNaturaGiuridica)).append("\n");
    sb.append("    desTipoNaturaGiuridica: ").append(toIndentedString(desTipoNaturaGiuridica)).append("\n");
    sb.append("    flgPubblico: ").append(toIndentedString(flgPubblico)).append("\n");
    sb.append("    idTipoNaturaGiuridica: ").append(toIndentedString(idTipoNaturaGiuridica)).append("\n");
    sb.append("    ordinamentoTipoNaturaGiu: ").append(toIndentedString(ordinamentoTipoNaturaGiu)).append("\n");
    sb.append("    siglaTipoNaturaGiuridica: ").append(toIndentedString(siglaTipoNaturaGiuridica)).append("\n");
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


package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto;

import java.util.Date;
import java.util.Objects;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * ComuneExtendedDTO
 */
@Validated




public class ComuneExtendedDTO   {
  @JsonProperty("cap_comune")
  private String capComune = null;

  @JsonProperty("cod_belfiore_comune")
  private String codBelfioreComune = null;

  @JsonProperty("cod_istat_comune")
  private String codIstatComune = null;

  @JsonProperty("data_fine_validita")
  private Date dataFineValidita = null;

  @JsonProperty("data_inizio_validita")
  private Date dataInizioValidita = null;

  @JsonProperty("denom_comune")
  private String denomComune = null;

  @JsonProperty("dt_id_comune")
  private Long dtIdComune = null;

  @JsonProperty("dt_id_comune_next")
  private Long dtIdComuneNext = null;

  @JsonProperty("dt_id_comune_prev")
  private Long dtIdComunePrev = null;

  @JsonProperty("id_comune")
  private Long idComune = null;

  @JsonProperty("id_provincia")
  private Long idProvincia = null;

  @JsonProperty("provincia")
  private ProvinciaExtendedDTO provincia = null;

  public ComuneExtendedDTO capComune(String capComune) {
    this.capComune = capComune;
    return this;
  }

  /**
   * CAP del comune
   * @return capComune
  **/
  @ApiModelProperty(example = "10121", value = "CAP del comune")


  public String getCapComune() {
    return capComune;
  }

  public void setCapComune(String capComune) {
    this.capComune = capComune;
  }

  public ComuneExtendedDTO codBelfioreComune(String codBelfioreComune) {
    this.codBelfioreComune = codBelfioreComune;
    return this;
  }

  /**
   * Get codBelfioreComune
   * @return codBelfioreComune
  **/
  @ApiModelProperty(example = "L219", value = "")


  public String getCodBelfioreComune() {
    return codBelfioreComune;
  }

  public void setCodBelfioreComune(String codBelfioreComune) {
    this.codBelfioreComune = codBelfioreComune;
  }

  public ComuneExtendedDTO codIstatComune(String codIstatComune) {
    this.codIstatComune = codIstatComune;
    return this;
  }

  /**
   * Codice Istat del comune
   * @return codIstatComune
  **/
  @ApiModelProperty(example = "698", value = "Codice Istat del comune")


  public String getCodIstatComune() {
    return codIstatComune;
  }

  public void setCodIstatComune(String codIstatComune) {
    this.codIstatComune = codIstatComune;
  }

  public ComuneExtendedDTO dataFineValidita(Date dataFineValidita) {
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

  public ComuneExtendedDTO dataInizioValidita(Date dataInizioValidita) {
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

  public ComuneExtendedDTO denomComune(String denomComune) {
    this.denomComune = denomComune;
    return this;
  }

  /**
   * denominazione del comune
   * @return denomComune
  **/
  @ApiModelProperty(example = "Torino", value = "denominazione del comune")


  public String getDenomComune() {
    return denomComune;
  }

  public void setDenomComune(String denomComune) {
    this.denomComune = denomComune;
  }

  public ComuneExtendedDTO dtIdComune(Long dtIdComune) {
    this.dtIdComune = dtIdComune;
    return this;
  }

  /**
   * Get dtIdComune
   * @return dtIdComune
  **/
  @ApiModelProperty(value = "")


  public Long getDtIdComune() {
    return dtIdComune;
  }

  public void setDtIdComune(Long dtIdComune) {
    this.dtIdComune = dtIdComune;
  }

  public ComuneExtendedDTO dtIdComuneNext(Long dtIdComuneNext) {
    this.dtIdComuneNext = dtIdComuneNext;
    return this;
  }

  /**
   * Get dtIdComuneNext
   * @return dtIdComuneNext
  **/
  @ApiModelProperty(value = "")


  public Long getDtIdComuneNext() {
    return dtIdComuneNext;
  }

  public void setDtIdComuneNext(Long dtIdComuneNext) {
    this.dtIdComuneNext = dtIdComuneNext;
  }

  public ComuneExtendedDTO dtIdComunePrev(Long dtIdComunePrev) {
    this.dtIdComunePrev = dtIdComunePrev;
    return this;
  }

  /**
   * Get dtIdComunePrev
   * @return dtIdComunePrev
  **/
  @ApiModelProperty(value = "")


  public Long getDtIdComunePrev() {
    return dtIdComunePrev;
  }

  public void setDtIdComunePrev(Long dtIdComunePrev) {
    this.dtIdComunePrev = dtIdComunePrev;
  }

  public ComuneExtendedDTO idComune(Long idComune) {
    this.idComune = idComune;
    return this;
  }

  /**
   * Identificativo SCRIVA del comune
   * @return idComune
  **/
  @ApiModelProperty(value = "Identificativo SCRIVA del comune")


  public Long getIdComune() {
    return idComune;
  }

  public void setIdComune(Long idComune) {
    this.idComune = idComune;
  }

  public ComuneExtendedDTO idProvincia(Long idProvincia) {
    this.idProvincia = idProvincia;
    return this;
  }

  /**
   * Identificativo SCRIVA della provincia
   * @return idProvincia
  **/
  @ApiModelProperty(value = "Identificativo SCRIVA della provincia")


  public Long getIdProvincia() {
    return idProvincia;
  }

  public void setIdProvincia(Long idProvincia) {
    this.idProvincia = idProvincia;
  }

  public ComuneExtendedDTO provincia(ProvinciaExtendedDTO provincia) {
    this.provincia = provincia;
    return this;
  }

  /**
   * Get provincia
   * @return provincia
  **/
  @ApiModelProperty(value = "")

  @Valid

  public ProvinciaExtendedDTO getProvincia() {
    return provincia;
  }

  public void setProvincia(ProvinciaExtendedDTO provincia) {
    this.provincia = provincia;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ComuneExtendedDTO comuneExtendedDTO = (ComuneExtendedDTO) o;
    return Objects.equals(this.capComune, comuneExtendedDTO.capComune) &&
        Objects.equals(this.codBelfioreComune, comuneExtendedDTO.codBelfioreComune) &&
        Objects.equals(this.codIstatComune, comuneExtendedDTO.codIstatComune) &&
        Objects.equals(this.dataFineValidita, comuneExtendedDTO.dataFineValidita) &&
        Objects.equals(this.dataInizioValidita, comuneExtendedDTO.dataInizioValidita) &&
        Objects.equals(this.denomComune, comuneExtendedDTO.denomComune) &&
        Objects.equals(this.dtIdComune, comuneExtendedDTO.dtIdComune) &&
        Objects.equals(this.dtIdComuneNext, comuneExtendedDTO.dtIdComuneNext) &&
        Objects.equals(this.dtIdComunePrev, comuneExtendedDTO.dtIdComunePrev) &&
        Objects.equals(this.idComune, comuneExtendedDTO.idComune) &&
        Objects.equals(this.idProvincia, comuneExtendedDTO.idProvincia) &&
        Objects.equals(this.provincia, comuneExtendedDTO.provincia);
  }

  @Override
  public int hashCode() {
    return Objects.hash(capComune, codBelfioreComune, codIstatComune, dataFineValidita, dataInizioValidita, denomComune, dtIdComune, dtIdComuneNext, dtIdComunePrev, idComune, idProvincia, provincia);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ComuneExtendedDTO {\n");
    
    sb.append("    capComune: ").append(toIndentedString(capComune)).append("\n");
    sb.append("    codBelfioreComune: ").append(toIndentedString(codBelfioreComune)).append("\n");
    sb.append("    codIstatComune: ").append(toIndentedString(codIstatComune)).append("\n");
    sb.append("    dataFineValidita: ").append(toIndentedString(dataFineValidita)).append("\n");
    sb.append("    dataInizioValidita: ").append(toIndentedString(dataInizioValidita)).append("\n");
    sb.append("    denomComune: ").append(toIndentedString(denomComune)).append("\n");
    sb.append("    dtIdComune: ").append(toIndentedString(dtIdComune)).append("\n");
    sb.append("    dtIdComuneNext: ").append(toIndentedString(dtIdComuneNext)).append("\n");
    sb.append("    dtIdComunePrev: ").append(toIndentedString(dtIdComunePrev)).append("\n");
    sb.append("    idComune: ").append(toIndentedString(idComune)).append("\n");
    sb.append("    idProvincia: ").append(toIndentedString(idProvincia)).append("\n");
    sb.append("    provincia: ").append(toIndentedString(provincia)).append("\n");
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


package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto;

import java.util.Date;
import java.util.Objects;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
import it.csi.risca.riscaboweb.business.be.helper.scriva.dto.RegioneExtendedDTO;
/**
 * ProvinciaExtendedDTO
 */
@Validated




public class ProvinciaExtendedDTO   {
  @JsonProperty("cod_provincia")
  private String codProvincia = null;

  @JsonProperty("data_fine_validita")
  private Date dataFineValidita = null;

  @JsonProperty("data_inizio_validita")
  private Date dataInizioValidita = null;

  @JsonProperty("denom_provincia")
  private String denomProvincia = null;

  @JsonProperty("flg_limitrofa")
  private Boolean flgLimitrofa = null;

  @JsonProperty("id_provincia")
  private Long idProvincia = null;

  @JsonProperty("id_regione")
  private Long idRegione = null;

  @JsonProperty("ordinamento_adempi_provincia")
  private Integer ordinamentoAdempiProvincia = null;

  @JsonProperty("regione")
  private RegioneExtendedDTO regione = null;

  @JsonProperty("sigla_provincia")
  private String siglaProvincia = null;

  public ProvinciaExtendedDTO codProvincia(String codProvincia) {
    this.codProvincia = codProvincia;
    return this;
  }

  /**
   * Get codProvincia
   * @return codProvincia
  **/
  @ApiModelProperty(value = "")


  public String getCodProvincia() {
    return codProvincia;
  }

  public void setCodProvincia(String codProvincia) {
    this.codProvincia = codProvincia;
  }

  public ProvinciaExtendedDTO dataFineValidita(Date dataFineValidita) {
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

  public ProvinciaExtendedDTO dataInizioValidita(Date dataInizioValidita) {
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

  public ProvinciaExtendedDTO denomProvincia(String denomProvincia) {
    this.denomProvincia = denomProvincia;
    return this;
  }

  /**
   * Get denomProvincia
   * @return denomProvincia
  **/
  @ApiModelProperty(value = "")


  public String getDenomProvincia() {
    return denomProvincia;
  }

  public void setDenomProvincia(String denomProvincia) {
    this.denomProvincia = denomProvincia;
  }

  public ProvinciaExtendedDTO flgLimitrofa(Boolean flgLimitrofa) {
    this.flgLimitrofa = flgLimitrofa;
    return this;
  }

  /**
   * Get flgLimitrofa
   * @return flgLimitrofa
  **/
  @ApiModelProperty(value = "")


  public Boolean isFlgLimitrofa() {
    return flgLimitrofa;
  }

  public void setFlgLimitrofa(Boolean flgLimitrofa) {
    this.flgLimitrofa = flgLimitrofa;
  }

  public ProvinciaExtendedDTO idProvincia(Long idProvincia) {
    this.idProvincia = idProvincia;
    return this;
  }

  /**
   * Get idProvincia
   * @return idProvincia
  **/
  @ApiModelProperty(value = "")


  public Long getIdProvincia() {
    return idProvincia;
  }

  public void setIdProvincia(Long idProvincia) {
    this.idProvincia = idProvincia;
  }

  public ProvinciaExtendedDTO idRegione(Long idRegione) {
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

  public ProvinciaExtendedDTO ordinamentoAdempiProvincia(Integer ordinamentoAdempiProvincia) {
    this.ordinamentoAdempiProvincia = ordinamentoAdempiProvincia;
    return this;
  }

  /**
   * Get ordinamentoAdempiProvincia
   * @return ordinamentoAdempiProvincia
  **/
  @ApiModelProperty(value = "")


  public Integer getOrdinamentoAdempiProvincia() {
    return ordinamentoAdempiProvincia;
  }

  public void setOrdinamentoAdempiProvincia(Integer ordinamentoAdempiProvincia) {
    this.ordinamentoAdempiProvincia = ordinamentoAdempiProvincia;
  }

  public ProvinciaExtendedDTO regione(RegioneExtendedDTO regione) {
    this.regione = regione;
    return this;
  }

  /**
   * Get regione
   * @return regione
  **/
  @ApiModelProperty(value = "")

  @Valid

  public RegioneExtendedDTO getRegione() {
    return regione;
  }

  public void setRegione(RegioneExtendedDTO regione) {
    this.regione = regione;
  }

  public ProvinciaExtendedDTO siglaProvincia(String siglaProvincia) {
    this.siglaProvincia = siglaProvincia;
    return this;
  }

  /**
   * Get siglaProvincia
   * @return siglaProvincia
  **/
  @ApiModelProperty(value = "")


  public String getSiglaProvincia() {
    return siglaProvincia;
  }

  public void setSiglaProvincia(String siglaProvincia) {
    this.siglaProvincia = siglaProvincia;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProvinciaExtendedDTO provinciaExtendedDTO = (ProvinciaExtendedDTO) o;
    return Objects.equals(this.codProvincia, provinciaExtendedDTO.codProvincia) &&
        Objects.equals(this.dataFineValidita, provinciaExtendedDTO.dataFineValidita) &&
        Objects.equals(this.dataInizioValidita, provinciaExtendedDTO.dataInizioValidita) &&
        Objects.equals(this.denomProvincia, provinciaExtendedDTO.denomProvincia) &&
        Objects.equals(this.flgLimitrofa, provinciaExtendedDTO.flgLimitrofa) &&
        Objects.equals(this.idProvincia, provinciaExtendedDTO.idProvincia) &&
        Objects.equals(this.idRegione, provinciaExtendedDTO.idRegione) &&
        Objects.equals(this.ordinamentoAdempiProvincia, provinciaExtendedDTO.ordinamentoAdempiProvincia) &&
        Objects.equals(this.regione, provinciaExtendedDTO.regione) &&
        Objects.equals(this.siglaProvincia, provinciaExtendedDTO.siglaProvincia);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codProvincia, dataFineValidita, dataInizioValidita, denomProvincia, flgLimitrofa, idProvincia, idRegione, ordinamentoAdempiProvincia, regione, siglaProvincia);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProvinciaExtendedDTO {\n");
    
    sb.append("    codProvincia: ").append(toIndentedString(codProvincia)).append("\n");
    sb.append("    dataFineValidita: ").append(toIndentedString(dataFineValidita)).append("\n");
    sb.append("    dataInizioValidita: ").append(toIndentedString(dataInizioValidita)).append("\n");
    sb.append("    denomProvincia: ").append(toIndentedString(denomProvincia)).append("\n");
    sb.append("    flgLimitrofa: ").append(toIndentedString(flgLimitrofa)).append("\n");
    sb.append("    idProvincia: ").append(toIndentedString(idProvincia)).append("\n");
    sb.append("    idRegione: ").append(toIndentedString(idRegione)).append("\n");
    sb.append("    ordinamentoAdempiProvincia: ").append(toIndentedString(ordinamentoAdempiProvincia)).append("\n");
    sb.append("    regione: ").append(toIndentedString(regione)).append("\n");
    sb.append("    siglaProvincia: ").append(toIndentedString(siglaProvincia)).append("\n");
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


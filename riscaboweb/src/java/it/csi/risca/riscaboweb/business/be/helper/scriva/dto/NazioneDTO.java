package it.csi.risca.riscaboweb.business.be.helper.scriva.dto;

import java.util.Date;
import java.util.Objects;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * NazioneDTO
 */
@Validated




public class NazioneDTO   {
  @JsonProperty("cod_belfiore_nazione")
  private String codBelfioreNazione = null;

  @JsonProperty("cod_iso2")
  private String codIso2 = null;

  @JsonProperty("cod_istat_nazione")
  private String codIstatNazione = null;

  @JsonProperty("data_fine_validita")
  private Date dataFineValidita = null;

  @JsonProperty("data_inizio_validita")
  private Date dataInizioValidita = null;

  @JsonProperty("denom_nazione")
  private String denomNazione = null;

  @JsonProperty("dt_id_stato")
  private Long dtIdStato = null;

  @JsonProperty("dt_id_stato_next")
  private Long dtIdStatoNext = null;

  @JsonProperty("dt_id_stato_prev")
  private Long dtIdStatoPrev = null;

  @JsonProperty("id_nazione")
  private Long idNazione = null;

  @JsonProperty("id_origine")
  private Long idOrigine = null;

  public NazioneDTO codBelfioreNazione(String codBelfioreNazione) {
    this.codBelfioreNazione = codBelfioreNazione;
    return this;
  }

  /**
   * Get codBelfioreNazione
   * @return codBelfioreNazione
  **/
  @ApiModelProperty(value = "")


  public String getCodBelfioreNazione() {
    return codBelfioreNazione;
  }

  public void setCodBelfioreNazione(String codBelfioreNazione) {
    this.codBelfioreNazione = codBelfioreNazione;
  }

  public NazioneDTO codIso2(String codIso2) {
    this.codIso2 = codIso2;
    return this;
  }

  /**
   * Get codIso2
   * @return codIso2
  **/
  @ApiModelProperty(value = "")


  public String getCodIso2() {
    return codIso2;
  }

  public void setCodIso2(String codIso2) {
    this.codIso2 = codIso2;
  }

  public NazioneDTO codIstatNazione(String codIstatNazione) {
    this.codIstatNazione = codIstatNazione;
    return this;
  }

  /**
   * Get codIstatNazione
   * @return codIstatNazione
  **/
  @ApiModelProperty(value = "")


  public String getCodIstatNazione() {
    return codIstatNazione;
  }

  public void setCodIstatNazione(String codIstatNazione) {
    this.codIstatNazione = codIstatNazione;
  }

  public NazioneDTO dataFineValidita(Date dataFineValidita) {
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

  public NazioneDTO dataInizioValidita(Date dataInizioValidita) {
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

  public NazioneDTO denomNazione(String denomNazione) {
    this.denomNazione = denomNazione;
    return this;
  }

  /**
   * Get denomNazione
   * @return denomNazione
  **/
  @ApiModelProperty(value = "")


  public String getDenomNazione() {
    return denomNazione;
  }

  public void setDenomNazione(String denomNazione) {
    this.denomNazione = denomNazione;
  }

  public NazioneDTO dtIdStato(Long dtIdStato) {
    this.dtIdStato = dtIdStato;
    return this;
  }

  /**
   * Get dtIdStato
   * @return dtIdStato
  **/
  @ApiModelProperty(value = "")


  public Long getDtIdStato() {
    return dtIdStato;
  }

  public void setDtIdStato(Long dtIdStato) {
    this.dtIdStato = dtIdStato;
  }

  public NazioneDTO dtIdStatoNext(Long dtIdStatoNext) {
    this.dtIdStatoNext = dtIdStatoNext;
    return this;
  }

  /**
   * Get dtIdStatoNext
   * @return dtIdStatoNext
  **/
  @ApiModelProperty(value = "")


  public Long getDtIdStatoNext() {
    return dtIdStatoNext;
  }

  public void setDtIdStatoNext(Long dtIdStatoNext) {
    this.dtIdStatoNext = dtIdStatoNext;
  }

  public NazioneDTO dtIdStatoPrev(Long dtIdStatoPrev) {
    this.dtIdStatoPrev = dtIdStatoPrev;
    return this;
  }

  /**
   * Get dtIdStatoPrev
   * @return dtIdStatoPrev
  **/
  @ApiModelProperty(value = "")


  public Long getDtIdStatoPrev() {
    return dtIdStatoPrev;
  }

  public void setDtIdStatoPrev(Long dtIdStatoPrev) {
    this.dtIdStatoPrev = dtIdStatoPrev;
  }

  public NazioneDTO idNazione(Long idNazione) {
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

  public NazioneDTO idOrigine(Long idOrigine) {
    this.idOrigine = idOrigine;
    return this;
  }

  /**
   * Get idOrigine
   * @return idOrigine
  **/
  @ApiModelProperty(value = "")


  public Long getIdOrigine() {
    return idOrigine;
  }

  public void setIdOrigine(Long idOrigine) {
    this.idOrigine = idOrigine;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NazioneDTO nazioneDTO = (NazioneDTO) o;
    return Objects.equals(this.codBelfioreNazione, nazioneDTO.codBelfioreNazione) &&
        Objects.equals(this.codIso2, nazioneDTO.codIso2) &&
        Objects.equals(this.codIstatNazione, nazioneDTO.codIstatNazione) &&
        Objects.equals(this.dataFineValidita, nazioneDTO.dataFineValidita) &&
        Objects.equals(this.dataInizioValidita, nazioneDTO.dataInizioValidita) &&
        Objects.equals(this.denomNazione, nazioneDTO.denomNazione) &&
        Objects.equals(this.dtIdStato, nazioneDTO.dtIdStato) &&
        Objects.equals(this.dtIdStatoNext, nazioneDTO.dtIdStatoNext) &&
        Objects.equals(this.dtIdStatoPrev, nazioneDTO.dtIdStatoPrev) &&
        Objects.equals(this.idNazione, nazioneDTO.idNazione) &&
        Objects.equals(this.idOrigine, nazioneDTO.idOrigine);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codBelfioreNazione, codIso2, codIstatNazione, dataFineValidita, dataInizioValidita, denomNazione, dtIdStato, dtIdStatoNext, dtIdStatoPrev, idNazione, idOrigine);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NazioneDTO {\n");
    
    sb.append("    codBelfioreNazione: ").append(toIndentedString(codBelfioreNazione)).append("\n");
    sb.append("    codIso2: ").append(toIndentedString(codIso2)).append("\n");
    sb.append("    codIstatNazione: ").append(toIndentedString(codIstatNazione)).append("\n");
    sb.append("    dataFineValidita: ").append(toIndentedString(dataFineValidita)).append("\n");
    sb.append("    dataInizioValidita: ").append(toIndentedString(dataInizioValidita)).append("\n");
    sb.append("    denomNazione: ").append(toIndentedString(denomNazione)).append("\n");
    sb.append("    dtIdStato: ").append(toIndentedString(dtIdStato)).append("\n");
    sb.append("    dtIdStatoNext: ").append(toIndentedString(dtIdStatoNext)).append("\n");
    sb.append("    dtIdStatoPrev: ").append(toIndentedString(dtIdStatoPrev)).append("\n");
    sb.append("    idNazione: ").append(toIndentedString(idNazione)).append("\n");
    sb.append("    idOrigine: ").append(toIndentedString(idOrigine)).append("\n");
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


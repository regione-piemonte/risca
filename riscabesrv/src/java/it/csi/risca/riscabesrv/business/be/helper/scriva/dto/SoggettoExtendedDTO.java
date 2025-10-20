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

import java.util.Date;
import java.util.Objects;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * SoggettoExtendedDTO
 */
@Validated




public class SoggettoExtendedDTO   {
  @JsonProperty("cap_residenza")
  private String capResidenza = null;

  @JsonProperty("cap_sede_legale")
  private String capSedeLegale = null;

  @JsonProperty("cf_soggetto")
  private String cfSoggetto = null;

  @JsonProperty("citta_estera_nascita")
  private String cittaEsteraNascita = null;

  @JsonProperty("citta_estera_residenza")
  private String cittaEsteraResidenza = null;

  @JsonProperty("citta_estera_sede_legale")
  private String cittaEsteraSedeLegale = null;

  @JsonProperty("cognome")
  private String cognome = null;

  @JsonProperty("comune_nascita")
  private ComuneExtendedDTO comuneNascita = null;

  @JsonProperty("comune_residenza")
  private ComuneExtendedDTO comuneResidenza = null;

  @JsonProperty("comune_sede_legale")
  private ComuneExtendedDTO comuneSedeLegale = null;

  @JsonProperty("data_aggiornamento")
  private Date dataAggiornamento = null;

  @JsonProperty("data_cessazione_soggetto")
  private Date dataCessazioneSoggetto = null;

  @JsonProperty("data_nascita_soggetto")
  private Date dataNascitaSoggetto = null;

  @JsonProperty("den_anno_cciaa")
  private Integer denAnnoCciaa = null;

  @JsonProperty("den_numero_cciaa")
  private String denNumeroCciaa = null;

  @JsonProperty("den_provincia_cciaa")
  private String denProvinciaCciaa = null;

  @JsonProperty("den_soggetto")
  private String denSoggetto = null;

  @JsonProperty("des_email")
  private String desEmail = null;

  @JsonProperty("des_localita")
  private String desLocalita = null;

  @JsonProperty("des_pec")
  private String desPec = null;

  @JsonProperty("gestAttoreIns")
  private String gestAttoreIns = null;

  @JsonProperty("gestAttoreUpd")
  private String gestAttoreUpd = null;

  @JsonProperty("gestUID")
  private String gestUID = null;

  @JsonProperty("id_comune_nascita")
  private Long idComuneNascita = null;

  @JsonProperty("id_comune_residenza")
  private Long idComuneResidenza = null;

  @JsonProperty("id_comune_sede_legale")
  private Long idComuneSedeLegale = null;

  @JsonProperty("id_masterdata")
  private Long idMasterdata = null;

  @JsonProperty("id_masterdata_origine")
  private Long idMasterdataOrigine = null;

  @JsonProperty("id_nazione_nascita")
  private Long idNazioneNascita = null;

  @JsonProperty("id_nazione_residenza")
  private Long idNazioneResidenza = null;

  @JsonProperty("id_nazione_sede_legale")
  private Long idNazioneSedeLegale = null;

  @JsonProperty("id_soggetto")
  private Long idSoggetto = null;

  @JsonProperty("id_tipo_natura_giuridica")
  private Long idTipoNaturaGiuridica = null;

  @JsonProperty("id_tipo_soggetto")
  private Long idTipoSoggetto = null;

  @JsonProperty("indirizzo_soggetto")
  private String indirizzoSoggetto = null;

  @JsonProperty("nazione_nascita")
  private NazioneDTO nazioneNascita = null;

  @JsonProperty("nazione_residenza")
  private NazioneDTO nazioneResidenza = null;

  @JsonProperty("nazione_sede_legale")
  private NazioneDTO nazioneSedeLegale = null;

  @JsonProperty("nome")
  private String nome = null;

  @JsonProperty("num_cellulare")
  private String numCellulare = null;

  @JsonProperty("num_civico_indirizzo")
  private String numCivicoIndirizzo = null;

  @JsonProperty("num_telefono")
  private String numTelefono = null;

  @JsonProperty("partita_iva_soggetto")
  private String partitaIvaSoggetto = null;

  @JsonProperty("tipo_natura_giuridica")
  private TipoNaturaGiuridicaDTO tipoNaturaGiuridica = null;

  @JsonProperty("tipo_soggetto")
  private TipoSoggettoDTO tipoSoggetto = null;

  public SoggettoExtendedDTO capResidenza(String capResidenza) {
    this.capResidenza = capResidenza;
    return this;
  }

  /**
   * Get capResidenza
   * @return capResidenza
  **/
  @ApiModelProperty(value = "")


  public String getCapResidenza() {
    return capResidenza;
  }

  public void setCapResidenza(String capResidenza) {
    this.capResidenza = capResidenza;
  }

  public SoggettoExtendedDTO capSedeLegale(String capSedeLegale) {
    this.capSedeLegale = capSedeLegale;
    return this;
  }

  /**
   * Get capSedeLegale
   * @return capSedeLegale
  **/
  @ApiModelProperty(value = "")


  public String getCapSedeLegale() {
    return capSedeLegale;
  }

  public void setCapSedeLegale(String capSedeLegale) {
    this.capSedeLegale = capSedeLegale;
  }

  public SoggettoExtendedDTO cfSoggetto(String cfSoggetto) {
    this.cfSoggetto = cfSoggetto;
    return this;
  }

  /**
   * Get cfSoggetto
   * @return cfSoggetto
  **/
  @ApiModelProperty(value = "")


  public String getCfSoggetto() {
    return cfSoggetto;
  }

  public void setCfSoggetto(String cfSoggetto) {
    this.cfSoggetto = cfSoggetto;
  }

  public SoggettoExtendedDTO cittaEsteraNascita(String cittaEsteraNascita) {
    this.cittaEsteraNascita = cittaEsteraNascita;
    return this;
  }

  /**
   * Get cittaEsteraNascita
   * @return cittaEsteraNascita
  **/
  @ApiModelProperty(value = "")


  public String getCittaEsteraNascita() {
    return cittaEsteraNascita;
  }

  public void setCittaEsteraNascita(String cittaEsteraNascita) {
    this.cittaEsteraNascita = cittaEsteraNascita;
  }

  public SoggettoExtendedDTO cittaEsteraResidenza(String cittaEsteraResidenza) {
    this.cittaEsteraResidenza = cittaEsteraResidenza;
    return this;
  }

  /**
   * Get cittaEsteraResidenza
   * @return cittaEsteraResidenza
  **/
  @ApiModelProperty(value = "")


  public String getCittaEsteraResidenza() {
    return cittaEsteraResidenza;
  }

  public void setCittaEsteraResidenza(String cittaEsteraResidenza) {
    this.cittaEsteraResidenza = cittaEsteraResidenza;
  }

  public SoggettoExtendedDTO cittaEsteraSedeLegale(String cittaEsteraSedeLegale) {
    this.cittaEsteraSedeLegale = cittaEsteraSedeLegale;
    return this;
  }

  /**
   * Get cittaEsteraSedeLegale
   * @return cittaEsteraSedeLegale
  **/
  @ApiModelProperty(value = "")


  public String getCittaEsteraSedeLegale() {
    return cittaEsteraSedeLegale;
  }

  public void setCittaEsteraSedeLegale(String cittaEsteraSedeLegale) {
    this.cittaEsteraSedeLegale = cittaEsteraSedeLegale;
  }

  public SoggettoExtendedDTO cognome(String cognome) {
    this.cognome = cognome;
    return this;
  }

  /**
   * Get cognome
   * @return cognome
  **/
  @ApiModelProperty(value = "")


  public String getCognome() {
    return cognome;
  }

  public void setCognome(String cognome) {
    this.cognome = cognome;
  }

  public SoggettoExtendedDTO comuneNascita(ComuneExtendedDTO comuneNascita) {
    this.comuneNascita = comuneNascita;
    return this;
  }

  /**
   * Get comuneNascita
   * @return comuneNascita
  **/
  @ApiModelProperty(value = "")

  @Valid

  public ComuneExtendedDTO getComuneNascita() {
    return comuneNascita;
  }

  public void setComuneNascita(ComuneExtendedDTO comuneNascita) {
    this.comuneNascita = comuneNascita;
  }

  public SoggettoExtendedDTO comuneResidenza(ComuneExtendedDTO comuneResidenza) {
    this.comuneResidenza = comuneResidenza;
    return this;
  }

  /**
   * Get comuneResidenza
   * @return comuneResidenza
  **/
  @ApiModelProperty(value = "")

  @Valid

  public ComuneExtendedDTO getComuneResidenza() {
    return comuneResidenza;
  }

  public void setComuneResidenza(ComuneExtendedDTO comuneResidenza) {
    this.comuneResidenza = comuneResidenza;
  }

  public SoggettoExtendedDTO comuneSedeLegale(ComuneExtendedDTO comuneSedeLegale) {
    this.comuneSedeLegale = comuneSedeLegale;
    return this;
  }

  /**
   * Get comuneSedeLegale
   * @return comuneSedeLegale
  **/
  @ApiModelProperty(value = "")

  @Valid

  public ComuneExtendedDTO getComuneSedeLegale() {
    return comuneSedeLegale;
  }

  public void setComuneSedeLegale(ComuneExtendedDTO comuneSedeLegale) {
    this.comuneSedeLegale = comuneSedeLegale;
  }

  public SoggettoExtendedDTO dataAggiornamento(Date dataAggiornamento) {
    this.dataAggiornamento = dataAggiornamento;
    return this;
  }

  /**
   * Get dataAggiornamento
   * @return dataAggiornamento
  **/
  @ApiModelProperty(value = "")

  @Valid

  public Date getDataAggiornamento() {
    return dataAggiornamento;
  }

  public void setDataAggiornamento(Date dataAggiornamento) {
    this.dataAggiornamento = dataAggiornamento;
  }

  public SoggettoExtendedDTO dataCessazioneSoggetto(Date dataCessazioneSoggetto) {
    this.dataCessazioneSoggetto = dataCessazioneSoggetto;
    return this;
  }

  /**
   * Get dataCessazioneSoggetto
   * @return dataCessazioneSoggetto
  **/
  @ApiModelProperty(value = "")

  @Valid

  public Date getDataCessazioneSoggetto() {
    return dataCessazioneSoggetto;
  }

  public void setDataCessazioneSoggetto(Date dataCessazioneSoggetto) {
    this.dataCessazioneSoggetto = dataCessazioneSoggetto;
  }

  public SoggettoExtendedDTO dataNascitaSoggetto(Date dataNascitaSoggetto) {
    this.dataNascitaSoggetto = dataNascitaSoggetto;
    return this;
  }

  /**
   * Get dataNascitaSoggetto
   * @return dataNascitaSoggetto
  **/
  @ApiModelProperty(value = "")

  @Valid

  public Date getDataNascitaSoggetto() {
    return dataNascitaSoggetto;
  }

  public void setDataNascitaSoggetto(Date dataNascitaSoggetto) {
    this.dataNascitaSoggetto = dataNascitaSoggetto;
  }

  public SoggettoExtendedDTO denAnnoCciaa(Integer denAnnoCciaa) {
    this.denAnnoCciaa = denAnnoCciaa;
    return this;
  }

  /**
   * Get denAnnoCciaa
   * @return denAnnoCciaa
  **/
  @ApiModelProperty(value = "")


  public Integer getDenAnnoCciaa() {
    return denAnnoCciaa;
  }

  public void setDenAnnoCciaa(Integer denAnnoCciaa) {
    this.denAnnoCciaa = denAnnoCciaa;
  }

  public SoggettoExtendedDTO denNumeroCciaa(String denNumeroCciaa) {
    this.denNumeroCciaa = denNumeroCciaa;
    return this;
  }

  /**
   * Get denNumeroCciaa
   * @return denNumeroCciaa
  **/
  @ApiModelProperty(value = "")


  public String getDenNumeroCciaa() {
    return denNumeroCciaa;
  }

  public void setDenNumeroCciaa(String denNumeroCciaa) {
    this.denNumeroCciaa = denNumeroCciaa;
  }

  public SoggettoExtendedDTO denProvinciaCciaa(String denProvinciaCciaa) {
    this.denProvinciaCciaa = denProvinciaCciaa;
    return this;
  }

  /**
   * Get denProvinciaCciaa
   * @return denProvinciaCciaa
  **/
  @ApiModelProperty(value = "")


  public String getDenProvinciaCciaa() {
    return denProvinciaCciaa;
  }

  public void setDenProvinciaCciaa(String denProvinciaCciaa) {
    this.denProvinciaCciaa = denProvinciaCciaa;
  }

  public SoggettoExtendedDTO denSoggetto(String denSoggetto) {
    this.denSoggetto = denSoggetto;
    return this;
  }

  /**
   * Get denSoggetto
   * @return denSoggetto
  **/
  @ApiModelProperty(value = "")


  public String getDenSoggetto() {
    return denSoggetto;
  }

  public void setDenSoggetto(String denSoggetto) {
    this.denSoggetto = denSoggetto;
  }

  public SoggettoExtendedDTO desEmail(String desEmail) {
    this.desEmail = desEmail;
    return this;
  }

  /**
   * Get desEmail
   * @return desEmail
  **/
  @ApiModelProperty(value = "")


  public String getDesEmail() {
    return desEmail;
  }

  public void setDesEmail(String desEmail) {
    this.desEmail = desEmail;
  }

  public SoggettoExtendedDTO desLocalita(String desLocalita) {
    this.desLocalita = desLocalita;
    return this;
  }

  /**
   * Get desLocalita
   * @return desLocalita
  **/
  @ApiModelProperty(value = "")


  public String getDesLocalita() {
    return desLocalita;
  }

  public void setDesLocalita(String desLocalita) {
    this.desLocalita = desLocalita;
  }

  public SoggettoExtendedDTO desPec(String desPec) {
    this.desPec = desPec;
    return this;
  }

  /**
   * Get desPec
   * @return desPec
  **/
  @ApiModelProperty(value = "")


  public String getDesPec() {
    return desPec;
  }

  public void setDesPec(String desPec) {
    this.desPec = desPec;
  }

  public SoggettoExtendedDTO gestAttoreIns(String gestAttoreIns) {
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

  public SoggettoExtendedDTO gestAttoreUpd(String gestAttoreUpd) {
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

  public SoggettoExtendedDTO gestUID(String gestUID) {
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

  public SoggettoExtendedDTO idComuneNascita(Long idComuneNascita) {
    this.idComuneNascita = idComuneNascita;
    return this;
  }

  /**
   * Get idComuneNascita
   * @return idComuneNascita
  **/
  @ApiModelProperty(value = "")


  public Long getIdComuneNascita() {
    return idComuneNascita;
  }

  public void setIdComuneNascita(Long idComuneNascita) {
    this.idComuneNascita = idComuneNascita;
  }

  public SoggettoExtendedDTO idComuneResidenza(Long idComuneResidenza) {
    this.idComuneResidenza = idComuneResidenza;
    return this;
  }

  /**
   * Get idComuneResidenza
   * @return idComuneResidenza
  **/
  @ApiModelProperty(value = "")


  public Long getIdComuneResidenza() {
    return idComuneResidenza;
  }

  public void setIdComuneResidenza(Long idComuneResidenza) {
    this.idComuneResidenza = idComuneResidenza;
  }

  public SoggettoExtendedDTO idComuneSedeLegale(Long idComuneSedeLegale) {
    this.idComuneSedeLegale = idComuneSedeLegale;
    return this;
  }

  /**
   * Get idComuneSedeLegale
   * @return idComuneSedeLegale
  **/
  @ApiModelProperty(value = "")


  public Long getIdComuneSedeLegale() {
    return idComuneSedeLegale;
  }

  public void setIdComuneSedeLegale(Long idComuneSedeLegale) {
    this.idComuneSedeLegale = idComuneSedeLegale;
  }

  public SoggettoExtendedDTO idMasterdata(Long idMasterdata) {
    this.idMasterdata = idMasterdata;
    return this;
  }

  /**
   * Get idMasterdata
   * @return idMasterdata
  **/
  @ApiModelProperty(value = "")


  public Long getIdMasterdata() {
    return idMasterdata;
  }

  public void setIdMasterdata(Long idMasterdata) {
    this.idMasterdata = idMasterdata;
  }

  public SoggettoExtendedDTO idMasterdataOrigine(Long idMasterdataOrigine) {
    this.idMasterdataOrigine = idMasterdataOrigine;
    return this;
  }

  /**
   * Get idMasterdataOrigine
   * @return idMasterdataOrigine
  **/
  @ApiModelProperty(value = "")


  public Long getIdMasterdataOrigine() {
    return idMasterdataOrigine;
  }

  public void setIdMasterdataOrigine(Long idMasterdataOrigine) {
    this.idMasterdataOrigine = idMasterdataOrigine;
  }

  public SoggettoExtendedDTO idNazioneNascita(Long idNazioneNascita) {
    this.idNazioneNascita = idNazioneNascita;
    return this;
  }

  /**
   * Get idNazioneNascita
   * @return idNazioneNascita
  **/
  @ApiModelProperty(value = "")


  public Long getIdNazioneNascita() {
    return idNazioneNascita;
  }

  public void setIdNazioneNascita(Long idNazioneNascita) {
    this.idNazioneNascita = idNazioneNascita;
  }

  public SoggettoExtendedDTO idNazioneResidenza(Long idNazioneResidenza) {
    this.idNazioneResidenza = idNazioneResidenza;
    return this;
  }

  /**
   * Get idNazioneResidenza
   * @return idNazioneResidenza
  **/
  @ApiModelProperty(value = "")


  public Long getIdNazioneResidenza() {
    return idNazioneResidenza;
  }

  public void setIdNazioneResidenza(Long idNazioneResidenza) {
    this.idNazioneResidenza = idNazioneResidenza;
  }

  public SoggettoExtendedDTO idNazioneSedeLegale(Long idNazioneSedeLegale) {
    this.idNazioneSedeLegale = idNazioneSedeLegale;
    return this;
  }

  /**
   * Get idNazioneSedeLegale
   * @return idNazioneSedeLegale
  **/
  @ApiModelProperty(value = "")


  public Long getIdNazioneSedeLegale() {
    return idNazioneSedeLegale;
  }

  public void setIdNazioneSedeLegale(Long idNazioneSedeLegale) {
    this.idNazioneSedeLegale = idNazioneSedeLegale;
  }

  public SoggettoExtendedDTO idSoggetto(Long idSoggetto) {
    this.idSoggetto = idSoggetto;
    return this;
  }

  /**
   * Get idSoggetto
   * @return idSoggetto
  **/
  @ApiModelProperty(value = "")


  public Long getIdSoggetto() {
    return idSoggetto;
  }

  public void setIdSoggetto(Long idSoggetto) {
    this.idSoggetto = idSoggetto;
  }

  public SoggettoExtendedDTO idTipoNaturaGiuridica(Long idTipoNaturaGiuridica) {
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

  public SoggettoExtendedDTO idTipoSoggetto(Long idTipoSoggetto) {
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

  public SoggettoExtendedDTO indirizzoSoggetto(String indirizzoSoggetto) {
    this.indirizzoSoggetto = indirizzoSoggetto;
    return this;
  }

  /**
   * Get indirizzoSoggetto
   * @return indirizzoSoggetto
  **/
  @ApiModelProperty(value = "")


  public String getIndirizzoSoggetto() {
    return indirizzoSoggetto;
  }

  public void setIndirizzoSoggetto(String indirizzoSoggetto) {
    this.indirizzoSoggetto = indirizzoSoggetto;
  }

  public SoggettoExtendedDTO nazioneNascita(NazioneDTO nazioneNascita) {
    this.nazioneNascita = nazioneNascita;
    return this;
  }

  /**
   * Get nazioneNascita
   * @return nazioneNascita
  **/
  @ApiModelProperty(value = "")

  @Valid

  public NazioneDTO getNazioneNascita() {
    return nazioneNascita;
  }

  public void setNazioneNascita(NazioneDTO nazioneNascita) {
    this.nazioneNascita = nazioneNascita;
  }

  public SoggettoExtendedDTO nazioneResidenza(NazioneDTO nazioneResidenza) {
    this.nazioneResidenza = nazioneResidenza;
    return this;
  }

  /**
   * Get nazioneResidenza
   * @return nazioneResidenza
  **/
  @ApiModelProperty(value = "")

  @Valid

  public NazioneDTO getNazioneResidenza() {
    return nazioneResidenza;
  }

  public void setNazioneResidenza(NazioneDTO nazioneResidenza) {
    this.nazioneResidenza = nazioneResidenza;
  }

  public SoggettoExtendedDTO nazioneSedeLegale(NazioneDTO nazioneSedeLegale) {
    this.nazioneSedeLegale = nazioneSedeLegale;
    return this;
  }

  /**
   * Get nazioneSedeLegale
   * @return nazioneSedeLegale
  **/
  @ApiModelProperty(value = "")

  @Valid

  public NazioneDTO getNazioneSedeLegale() {
    return nazioneSedeLegale;
  }

  public void setNazioneSedeLegale(NazioneDTO nazioneSedeLegale) {
    this.nazioneSedeLegale = nazioneSedeLegale;
  }

  public SoggettoExtendedDTO nome(String nome) {
    this.nome = nome;
    return this;
  }

  /**
   * Get nome
   * @return nome
  **/
  @ApiModelProperty(value = "")


  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public SoggettoExtendedDTO numCellulare(String numCellulare) {
    this.numCellulare = numCellulare;
    return this;
  }

  /**
   * Get numCellulare
   * @return numCellulare
  **/
  @ApiModelProperty(value = "")


  public String getNumCellulare() {
    return numCellulare;
  }

  public void setNumCellulare(String numCellulare) {
    this.numCellulare = numCellulare;
  }

  public SoggettoExtendedDTO numCivicoIndirizzo(String numCivicoIndirizzo) {
    this.numCivicoIndirizzo = numCivicoIndirizzo;
    return this;
  }

  /**
   * Get numCivicoIndirizzo
   * @return numCivicoIndirizzo
  **/
  @ApiModelProperty(value = "")


  public String getNumCivicoIndirizzo() {
    return numCivicoIndirizzo;
  }

  public void setNumCivicoIndirizzo(String numCivicoIndirizzo) {
    this.numCivicoIndirizzo = numCivicoIndirizzo;
  }

  public SoggettoExtendedDTO numTelefono(String numTelefono) {
    this.numTelefono = numTelefono;
    return this;
  }

  /**
   * Get numTelefono
   * @return numTelefono
  **/
  @ApiModelProperty(value = "")


  public String getNumTelefono() {
    return numTelefono;
  }

  public void setNumTelefono(String numTelefono) {
    this.numTelefono = numTelefono;
  }

  public SoggettoExtendedDTO partitaIvaSoggetto(String partitaIvaSoggetto) {
    this.partitaIvaSoggetto = partitaIvaSoggetto;
    return this;
  }

  /**
   * Get partitaIvaSoggetto
   * @return partitaIvaSoggetto
  **/
  @ApiModelProperty(value = "")


  public String getPartitaIvaSoggetto() {
    return partitaIvaSoggetto;
  }

  public void setPartitaIvaSoggetto(String partitaIvaSoggetto) {
    this.partitaIvaSoggetto = partitaIvaSoggetto;
  }

  public SoggettoExtendedDTO tipoNaturaGiuridica(TipoNaturaGiuridicaDTO tipoNaturaGiuridica) {
    this.tipoNaturaGiuridica = tipoNaturaGiuridica;
    return this;
  }

  /**
   * Get tipoNaturaGiuridica
   * @return tipoNaturaGiuridica
  **/
  @ApiModelProperty(value = "")

  @Valid

  public TipoNaturaGiuridicaDTO getTipoNaturaGiuridica() {
    return tipoNaturaGiuridica;
  }

  public void setTipoNaturaGiuridica(TipoNaturaGiuridicaDTO tipoNaturaGiuridica) {
    this.tipoNaturaGiuridica = tipoNaturaGiuridica;
  }

  public SoggettoExtendedDTO tipoSoggetto(TipoSoggettoDTO tipoSoggetto) {
    this.tipoSoggetto = tipoSoggetto;
    return this;
  }

  /**
   * Get tipoSoggetto
   * @return tipoSoggetto
  **/
  @ApiModelProperty(value = "")

  @Valid

  public TipoSoggettoDTO getTipoSoggetto() {
    return tipoSoggetto;
  }

  public void setTipoSoggetto(TipoSoggettoDTO tipoSoggetto) {
    this.tipoSoggetto = tipoSoggetto;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SoggettoExtendedDTO soggettoExtendedDTO = (SoggettoExtendedDTO) o;
    return Objects.equals(this.capResidenza, soggettoExtendedDTO.capResidenza) &&
        Objects.equals(this.capSedeLegale, soggettoExtendedDTO.capSedeLegale) &&
        Objects.equals(this.cfSoggetto, soggettoExtendedDTO.cfSoggetto) &&
        Objects.equals(this.cittaEsteraNascita, soggettoExtendedDTO.cittaEsteraNascita) &&
        Objects.equals(this.cittaEsteraResidenza, soggettoExtendedDTO.cittaEsteraResidenza) &&
        Objects.equals(this.cittaEsteraSedeLegale, soggettoExtendedDTO.cittaEsteraSedeLegale) &&
        Objects.equals(this.cognome, soggettoExtendedDTO.cognome) &&
        Objects.equals(this.comuneNascita, soggettoExtendedDTO.comuneNascita) &&
        Objects.equals(this.comuneResidenza, soggettoExtendedDTO.comuneResidenza) &&
        Objects.equals(this.comuneSedeLegale, soggettoExtendedDTO.comuneSedeLegale) &&
        Objects.equals(this.dataAggiornamento, soggettoExtendedDTO.dataAggiornamento) &&
        Objects.equals(this.dataCessazioneSoggetto, soggettoExtendedDTO.dataCessazioneSoggetto) &&
        Objects.equals(this.dataNascitaSoggetto, soggettoExtendedDTO.dataNascitaSoggetto) &&
        Objects.equals(this.denAnnoCciaa, soggettoExtendedDTO.denAnnoCciaa) &&
        Objects.equals(this.denNumeroCciaa, soggettoExtendedDTO.denNumeroCciaa) &&
        Objects.equals(this.denProvinciaCciaa, soggettoExtendedDTO.denProvinciaCciaa) &&
        Objects.equals(this.denSoggetto, soggettoExtendedDTO.denSoggetto) &&
        Objects.equals(this.desEmail, soggettoExtendedDTO.desEmail) &&
        Objects.equals(this.desLocalita, soggettoExtendedDTO.desLocalita) &&
        Objects.equals(this.desPec, soggettoExtendedDTO.desPec) &&
        Objects.equals(this.gestAttoreIns, soggettoExtendedDTO.gestAttoreIns) &&
        Objects.equals(this.gestAttoreUpd, soggettoExtendedDTO.gestAttoreUpd) &&
        Objects.equals(this.gestUID, soggettoExtendedDTO.gestUID) &&
        Objects.equals(this.idComuneNascita, soggettoExtendedDTO.idComuneNascita) &&
        Objects.equals(this.idComuneResidenza, soggettoExtendedDTO.idComuneResidenza) &&
        Objects.equals(this.idComuneSedeLegale, soggettoExtendedDTO.idComuneSedeLegale) &&
        Objects.equals(this.idMasterdata, soggettoExtendedDTO.idMasterdata) &&
        Objects.equals(this.idMasterdataOrigine, soggettoExtendedDTO.idMasterdataOrigine) &&
        Objects.equals(this.idNazioneNascita, soggettoExtendedDTO.idNazioneNascita) &&
        Objects.equals(this.idNazioneResidenza, soggettoExtendedDTO.idNazioneResidenza) &&
        Objects.equals(this.idNazioneSedeLegale, soggettoExtendedDTO.idNazioneSedeLegale) &&
        Objects.equals(this.idSoggetto, soggettoExtendedDTO.idSoggetto) &&
        Objects.equals(this.idTipoNaturaGiuridica, soggettoExtendedDTO.idTipoNaturaGiuridica) &&
        Objects.equals(this.idTipoSoggetto, soggettoExtendedDTO.idTipoSoggetto) &&
        Objects.equals(this.indirizzoSoggetto, soggettoExtendedDTO.indirizzoSoggetto) &&
        Objects.equals(this.nazioneNascita, soggettoExtendedDTO.nazioneNascita) &&
        Objects.equals(this.nazioneResidenza, soggettoExtendedDTO.nazioneResidenza) &&
        Objects.equals(this.nazioneSedeLegale, soggettoExtendedDTO.nazioneSedeLegale) &&
        Objects.equals(this.nome, soggettoExtendedDTO.nome) &&
        Objects.equals(this.numCellulare, soggettoExtendedDTO.numCellulare) &&
        Objects.equals(this.numCivicoIndirizzo, soggettoExtendedDTO.numCivicoIndirizzo) &&
        Objects.equals(this.numTelefono, soggettoExtendedDTO.numTelefono) &&
        Objects.equals(this.partitaIvaSoggetto, soggettoExtendedDTO.partitaIvaSoggetto) &&
        Objects.equals(this.tipoNaturaGiuridica, soggettoExtendedDTO.tipoNaturaGiuridica) &&
        Objects.equals(this.tipoSoggetto, soggettoExtendedDTO.tipoSoggetto);
  }

  @Override
  public int hashCode() {
    return Objects.hash(capResidenza, capSedeLegale, cfSoggetto, cittaEsteraNascita, cittaEsteraResidenza, cittaEsteraSedeLegale, cognome, comuneNascita, comuneResidenza, comuneSedeLegale, dataAggiornamento, dataCessazioneSoggetto, dataNascitaSoggetto, denAnnoCciaa, denNumeroCciaa, denProvinciaCciaa, denSoggetto, desEmail, desLocalita, desPec, gestAttoreIns, gestAttoreUpd, gestUID, idComuneNascita, idComuneResidenza, idComuneSedeLegale, idMasterdata, idMasterdataOrigine, idNazioneNascita, idNazioneResidenza, idNazioneSedeLegale, idSoggetto, idTipoNaturaGiuridica, idTipoSoggetto, indirizzoSoggetto, nazioneNascita, nazioneResidenza, nazioneSedeLegale, nome, numCellulare, numCivicoIndirizzo, numTelefono, partitaIvaSoggetto, tipoNaturaGiuridica, tipoSoggetto);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SoggettoExtendedDTO {\n");
    
    sb.append("    capResidenza: ").append(toIndentedString(capResidenza)).append("\n");
    sb.append("    capSedeLegale: ").append(toIndentedString(capSedeLegale)).append("\n");
    sb.append("    cfSoggetto: ").append(toIndentedString(cfSoggetto)).append("\n");
    sb.append("    cittaEsteraNascita: ").append(toIndentedString(cittaEsteraNascita)).append("\n");
    sb.append("    cittaEsteraResidenza: ").append(toIndentedString(cittaEsteraResidenza)).append("\n");
    sb.append("    cittaEsteraSedeLegale: ").append(toIndentedString(cittaEsteraSedeLegale)).append("\n");
    sb.append("    cognome: ").append(toIndentedString(cognome)).append("\n");
    sb.append("    comuneNascita: ").append(toIndentedString(comuneNascita)).append("\n");
    sb.append("    comuneResidenza: ").append(toIndentedString(comuneResidenza)).append("\n");
    sb.append("    comuneSedeLegale: ").append(toIndentedString(comuneSedeLegale)).append("\n");
    sb.append("    dataAggiornamento: ").append(toIndentedString(dataAggiornamento)).append("\n");
    sb.append("    dataCessazioneSoggetto: ").append(toIndentedString(dataCessazioneSoggetto)).append("\n");
    sb.append("    dataNascitaSoggetto: ").append(toIndentedString(dataNascitaSoggetto)).append("\n");
    sb.append("    denAnnoCciaa: ").append(toIndentedString(denAnnoCciaa)).append("\n");
    sb.append("    denNumeroCciaa: ").append(toIndentedString(denNumeroCciaa)).append("\n");
    sb.append("    denProvinciaCciaa: ").append(toIndentedString(denProvinciaCciaa)).append("\n");
    sb.append("    denSoggetto: ").append(toIndentedString(denSoggetto)).append("\n");
    sb.append("    desEmail: ").append(toIndentedString(desEmail)).append("\n");
    sb.append("    desLocalita: ").append(toIndentedString(desLocalita)).append("\n");
    sb.append("    desPec: ").append(toIndentedString(desPec)).append("\n");
    sb.append("    gestAttoreIns: ").append(toIndentedString(gestAttoreIns)).append("\n");
    sb.append("    gestAttoreUpd: ").append(toIndentedString(gestAttoreUpd)).append("\n");
    sb.append("    gestUID: ").append(toIndentedString(gestUID)).append("\n");
    sb.append("    idComuneNascita: ").append(toIndentedString(idComuneNascita)).append("\n");
    sb.append("    idComuneResidenza: ").append(toIndentedString(idComuneResidenza)).append("\n");
    sb.append("    idComuneSedeLegale: ").append(toIndentedString(idComuneSedeLegale)).append("\n");
    sb.append("    idMasterdata: ").append(toIndentedString(idMasterdata)).append("\n");
    sb.append("    idMasterdataOrigine: ").append(toIndentedString(idMasterdataOrigine)).append("\n");
    sb.append("    idNazioneNascita: ").append(toIndentedString(idNazioneNascita)).append("\n");
    sb.append("    idNazioneResidenza: ").append(toIndentedString(idNazioneResidenza)).append("\n");
    sb.append("    idNazioneSedeLegale: ").append(toIndentedString(idNazioneSedeLegale)).append("\n");
    sb.append("    idSoggetto: ").append(toIndentedString(idSoggetto)).append("\n");
    sb.append("    idTipoNaturaGiuridica: ").append(toIndentedString(idTipoNaturaGiuridica)).append("\n");
    sb.append("    idTipoSoggetto: ").append(toIndentedString(idTipoSoggetto)).append("\n");
    sb.append("    indirizzoSoggetto: ").append(toIndentedString(indirizzoSoggetto)).append("\n");
    sb.append("    nazioneNascita: ").append(toIndentedString(nazioneNascita)).append("\n");
    sb.append("    nazioneResidenza: ").append(toIndentedString(nazioneResidenza)).append("\n");
    sb.append("    nazioneSedeLegale: ").append(toIndentedString(nazioneSedeLegale)).append("\n");
    sb.append("    nome: ").append(toIndentedString(nome)).append("\n");
    sb.append("    numCellulare: ").append(toIndentedString(numCellulare)).append("\n");
    sb.append("    numCivicoIndirizzo: ").append(toIndentedString(numCivicoIndirizzo)).append("\n");
    sb.append("    numTelefono: ").append(toIndentedString(numTelefono)).append("\n");
    sb.append("    partitaIvaSoggetto: ").append(toIndentedString(partitaIvaSoggetto)).append("\n");
    sb.append("    tipoNaturaGiuridica: ").append(toIndentedString(tipoNaturaGiuridica)).append("\n");
    sb.append("    tipoSoggetto: ").append(toIndentedString(tipoSoggetto)).append("\n");
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


/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscaboweb.dto;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import it.csi.iride2.policy.entity.Identita;

public class UserInfo   implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [explicit-as-modeled] 
  
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private String nome = null;
  private String cognome = null;
  private String codFisc = null;
  private String ente = null;
  private String ruolo = null;
  private String codRuolo = null;
  private Integer livAuth = null;
  private String community = null;
  private Long ambito = null;


@JsonIgnore
  private Identita identita;

/**
   * nome dell&#39;utente
   **/
  

  @JsonProperty("nome") 
 
  public String getNome() {
    return nome;
  }
  public void setNome(String nome) {
    this.nome = nome;
  }

  /**
   * cognome dell&#39;utente
   **/
  

  @JsonProperty("cognome") 
 
  public String getCognome() {
    return cognome;
  }
  public void setCognome(String cognome) {
    this.cognome = cognome;
  }

  /**
   * codice fiscale dell&#39;utente
   **/
  

  @JsonProperty("codFisc") 
 
  public String getCodFisc() {
    return codFisc;
  }
  public void setCodFisc(String codFisc) {
    this.codFisc = codFisc;
  }

  /**
   * nome dell&#39;ente
   **/
  

  @JsonProperty("ente") 
 
  public String getEnte() {
    return ente;
  }
  public void setEnte(String ente) {
    this.ente = ente;
  }

  /**
   * nome del ruolo dell&#39;utente
   **/
  

  @JsonProperty("ruolo") 
 
  public String getRuolo() {
    return ruolo;
  }
  public void setRuolo(String ruolo) {
    this.ruolo = ruolo;
  }

  /**
   * codice del ruolo dell&#39;utente
   **/
  

  @JsonProperty("cod_ruolo") 
 
  public String getCodRuolo() {
    return codRuolo;
  }
  public void setCodRuolo(String codRuolo) {
    this.codRuolo = codRuolo;
  }

  /**
   * livello di autenticazione utilizzato per accedere
   **/
  

  @JsonProperty("livAuth") 
 
  public Integer getLivAuth() {
    return livAuth;
  }
  public void setLivAuth(Integer livAuth) {
    this.livAuth = livAuth;
  }

  /**
   * community di utenti utilizzata per accedere
   **/
  

  @JsonProperty("community") 
 
  public String getCommunity() {
    return community;
  }
  public void setCommunity(String community) {
    this.community = community;
  }
  
  @JsonProperty("ambito") 
  
  public Long getAmbito() {
	return ambito;
  }
  public void setAmbito(Long ambito) {
	this.ambito = ambito;
  }

  public Identita getIdentita() {
	return identita;
}
public void setIdentita(Identita identita) {
	this.identita = identita;
}

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserInfo userInfo = (UserInfo) o;
    return Objects.equals(nome, userInfo.nome) &&
        Objects.equals(cognome, userInfo.cognome) &&
        Objects.equals(codFisc, userInfo.codFisc) &&
        Objects.equals(ente, userInfo.ente) &&
        Objects.equals(ruolo, userInfo.ruolo) &&
        Objects.equals(codRuolo, userInfo.codRuolo) &&
        Objects.equals(livAuth, userInfo.livAuth) &&
        Objects.equals(community, userInfo.community);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nome, cognome, codFisc, ente, ruolo, codRuolo, livAuth, community);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserInfo {\n");
    
    sb.append("    nome: ").append(toIndentedString(nome)).append("\n");
    sb.append("    cognome: ").append(toIndentedString(cognome)).append("\n");
    sb.append("    codFisc: ").append(toIndentedString(codFisc)).append("\n");
    sb.append("    ente: ").append(toIndentedString(ente)).append("\n");
    sb.append("    ruolo: ").append(toIndentedString(ruolo)).append("\n");
    sb.append("    codRuolo: ").append(toIndentedString(codRuolo)).append("\n");
    sb.append("    livAuth: ").append(toIndentedString(livAuth)).append("\n");
    sb.append("    community: ").append(toIndentedString(community)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}


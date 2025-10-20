/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
/**
 * InfoCreazioneCorrispondente.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actaofficialbook;

public class InfoCreazioneCorrispondente  implements java.io.Serializable {
    private java.lang.String denominazione;

    private java.lang.String nome;

    private java.lang.String cognome;

    private java.lang.Integer ordinale;

    private java.lang.String carica;

    private java.lang.String persona;

    private java.lang.String testoFoglioTrasmissione;

    private it.csi.risca.riscabesrv.util.actaofficialbook.InfoSoggettoAssociato infoSoggettoAssociato;

    private java.lang.String tipologiaCorrispondente;

    private java.lang.String codiceFiscale;

    private java.lang.String PIVA;

    private java.lang.String codiceIPAPA;

    private java.lang.String codiceIPAAOO;

    private java.lang.String codiceIPAUO;

    private it.csi.risca.riscabesrv.util.actaofficialbook.IndirizzoTelematicoType indirizzoTelematicoPA;

    private it.csi.risca.riscabesrv.util.actaofficialbook.IndirizzoTelematicoType indirizzoTelematicoAOO;

    private it.csi.risca.riscabesrv.util.actaofficialbook.IndirizzoTelematicoType indirizzoTelematicoUO;

    private java.lang.String denominazioneAmministrazione;

    private java.lang.String denominazioneUfficio;

    private it.csi.risca.riscabesrv.util.actaofficialbook.IndirizzoTelematicoType indirizzoTelematico;

    private java.lang.Boolean richiestaConferma;

    public InfoCreazioneCorrispondente() {
    }

    public InfoCreazioneCorrispondente(
           java.lang.String denominazione,
           java.lang.String nome,
           java.lang.String cognome,
           java.lang.Integer ordinale,
           java.lang.String carica,
           java.lang.String persona,
           java.lang.String testoFoglioTrasmissione,
           it.csi.risca.riscabesrv.util.actaofficialbook.InfoSoggettoAssociato infoSoggettoAssociato,
           java.lang.String tipologiaCorrispondente,
           java.lang.String codiceFiscale,
           java.lang.String PIVA,
           java.lang.String codiceIPAPA,
           java.lang.String codiceIPAAOO,
           java.lang.String codiceIPAUO,
           it.csi.risca.riscabesrv.util.actaofficialbook.IndirizzoTelematicoType indirizzoTelematicoPA,
           it.csi.risca.riscabesrv.util.actaofficialbook.IndirizzoTelematicoType indirizzoTelematicoAOO,
           it.csi.risca.riscabesrv.util.actaofficialbook.IndirizzoTelematicoType indirizzoTelematicoUO,
           java.lang.String denominazioneAmministrazione,
           java.lang.String denominazioneUfficio,
           it.csi.risca.riscabesrv.util.actaofficialbook.IndirizzoTelematicoType indirizzoTelematico,
           java.lang.Boolean richiestaConferma) {
           this.denominazione = denominazione;
           this.nome = nome;
           this.cognome = cognome;
           this.ordinale = ordinale;
           this.carica = carica;
           this.persona = persona;
           this.testoFoglioTrasmissione = testoFoglioTrasmissione;
           this.infoSoggettoAssociato = infoSoggettoAssociato;
           this.tipologiaCorrispondente = tipologiaCorrispondente;
           this.codiceFiscale = codiceFiscale;
           this.PIVA = PIVA;
           this.codiceIPAPA = codiceIPAPA;
           this.codiceIPAAOO = codiceIPAAOO;
           this.codiceIPAUO = codiceIPAUO;
           this.indirizzoTelematicoPA = indirizzoTelematicoPA;
           this.indirizzoTelematicoAOO = indirizzoTelematicoAOO;
           this.indirizzoTelematicoUO = indirizzoTelematicoUO;
           this.denominazioneAmministrazione = denominazioneAmministrazione;
           this.denominazioneUfficio = denominazioneUfficio;
           this.indirizzoTelematico = indirizzoTelematico;
           this.richiestaConferma = richiestaConferma;
    }


    /**
     * Gets the denominazione value for this InfoCreazioneCorrispondente.
     * 
     * @return denominazione
     */
    public java.lang.String getDenominazione() {
        return denominazione;
    }


    /**
     * Sets the denominazione value for this InfoCreazioneCorrispondente.
     * 
     * @param denominazione
     */
    public void setDenominazione(java.lang.String denominazione) {
        this.denominazione = denominazione;
    }


    /**
     * Gets the nome value for this InfoCreazioneCorrispondente.
     * 
     * @return nome
     */
    public java.lang.String getNome() {
        return nome;
    }


    /**
     * Sets the nome value for this InfoCreazioneCorrispondente.
     * 
     * @param nome
     */
    public void setNome(java.lang.String nome) {
        this.nome = nome;
    }


    /**
     * Gets the cognome value for this InfoCreazioneCorrispondente.
     * 
     * @return cognome
     */
    public java.lang.String getCognome() {
        return cognome;
    }


    /**
     * Sets the cognome value for this InfoCreazioneCorrispondente.
     * 
     * @param cognome
     */
    public void setCognome(java.lang.String cognome) {
        this.cognome = cognome;
    }


    /**
     * Gets the ordinale value for this InfoCreazioneCorrispondente.
     * 
     * @return ordinale
     */
    public java.lang.Integer getOrdinale() {
        return ordinale;
    }


    /**
     * Sets the ordinale value for this InfoCreazioneCorrispondente.
     * 
     * @param ordinale
     */
    public void setOrdinale(java.lang.Integer ordinale) {
        this.ordinale = ordinale;
    }


    /**
     * Gets the carica value for this InfoCreazioneCorrispondente.
     * 
     * @return carica
     */
    public java.lang.String getCarica() {
        return carica;
    }


    /**
     * Sets the carica value for this InfoCreazioneCorrispondente.
     * 
     * @param carica
     */
    public void setCarica(java.lang.String carica) {
        this.carica = carica;
    }


    /**
     * Gets the persona value for this InfoCreazioneCorrispondente.
     * 
     * @return persona
     */
    public java.lang.String getPersona() {
        return persona;
    }


    /**
     * Sets the persona value for this InfoCreazioneCorrispondente.
     * 
     * @param persona
     */
    public void setPersona(java.lang.String persona) {
        this.persona = persona;
    }


    /**
     * Gets the testoFoglioTrasmissione value for this InfoCreazioneCorrispondente.
     * 
     * @return testoFoglioTrasmissione
     */
    public java.lang.String getTestoFoglioTrasmissione() {
        return testoFoglioTrasmissione;
    }


    /**
     * Sets the testoFoglioTrasmissione value for this InfoCreazioneCorrispondente.
     * 
     * @param testoFoglioTrasmissione
     */
    public void setTestoFoglioTrasmissione(java.lang.String testoFoglioTrasmissione) {
        this.testoFoglioTrasmissione = testoFoglioTrasmissione;
    }


    /**
     * Gets the infoSoggettoAssociato value for this InfoCreazioneCorrispondente.
     * 
     * @return infoSoggettoAssociato
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.InfoSoggettoAssociato getInfoSoggettoAssociato() {
        return infoSoggettoAssociato;
    }


    /**
     * Sets the infoSoggettoAssociato value for this InfoCreazioneCorrispondente.
     * 
     * @param infoSoggettoAssociato
     */
    public void setInfoSoggettoAssociato(it.csi.risca.riscabesrv.util.actaofficialbook.InfoSoggettoAssociato infoSoggettoAssociato) {
        this.infoSoggettoAssociato = infoSoggettoAssociato;
    }


    /**
     * Gets the tipologiaCorrispondente value for this InfoCreazioneCorrispondente.
     * 
     * @return tipologiaCorrispondente
     */
    public java.lang.String getTipologiaCorrispondente() {
        return tipologiaCorrispondente;
    }


    /**
     * Sets the tipologiaCorrispondente value for this InfoCreazioneCorrispondente.
     * 
     * @param tipologiaCorrispondente
     */
    public void setTipologiaCorrispondente(java.lang.String tipologiaCorrispondente) {
        this.tipologiaCorrispondente = tipologiaCorrispondente;
    }


    /**
     * Gets the codiceFiscale value for this InfoCreazioneCorrispondente.
     * 
     * @return codiceFiscale
     */
    public java.lang.String getCodiceFiscale() {
        return codiceFiscale;
    }


    /**
     * Sets the codiceFiscale value for this InfoCreazioneCorrispondente.
     * 
     * @param codiceFiscale
     */
    public void setCodiceFiscale(java.lang.String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }


    /**
     * Gets the PIVA value for this InfoCreazioneCorrispondente.
     * 
     * @return PIVA
     */
    public java.lang.String getPIVA() {
        return PIVA;
    }


    /**
     * Sets the PIVA value for this InfoCreazioneCorrispondente.
     * 
     * @param PIVA
     */
    public void setPIVA(java.lang.String PIVA) {
        this.PIVA = PIVA;
    }


    /**
     * Gets the codiceIPAPA value for this InfoCreazioneCorrispondente.
     * 
     * @return codiceIPAPA
     */
    public java.lang.String getCodiceIPAPA() {
        return codiceIPAPA;
    }


    /**
     * Sets the codiceIPAPA value for this InfoCreazioneCorrispondente.
     * 
     * @param codiceIPAPA
     */
    public void setCodiceIPAPA(java.lang.String codiceIPAPA) {
        this.codiceIPAPA = codiceIPAPA;
    }


    /**
     * Gets the codiceIPAAOO value for this InfoCreazioneCorrispondente.
     * 
     * @return codiceIPAAOO
     */
    public java.lang.String getCodiceIPAAOO() {
        return codiceIPAAOO;
    }


    /**
     * Sets the codiceIPAAOO value for this InfoCreazioneCorrispondente.
     * 
     * @param codiceIPAAOO
     */
    public void setCodiceIPAAOO(java.lang.String codiceIPAAOO) {
        this.codiceIPAAOO = codiceIPAAOO;
    }


    /**
     * Gets the codiceIPAUO value for this InfoCreazioneCorrispondente.
     * 
     * @return codiceIPAUO
     */
    public java.lang.String getCodiceIPAUO() {
        return codiceIPAUO;
    }


    /**
     * Sets the codiceIPAUO value for this InfoCreazioneCorrispondente.
     * 
     * @param codiceIPAUO
     */
    public void setCodiceIPAUO(java.lang.String codiceIPAUO) {
        this.codiceIPAUO = codiceIPAUO;
    }


    /**
     * Gets the indirizzoTelematicoPA value for this InfoCreazioneCorrispondente.
     * 
     * @return indirizzoTelematicoPA
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.IndirizzoTelematicoType getIndirizzoTelematicoPA() {
        return indirizzoTelematicoPA;
    }


    /**
     * Sets the indirizzoTelematicoPA value for this InfoCreazioneCorrispondente.
     * 
     * @param indirizzoTelematicoPA
     */
    public void setIndirizzoTelematicoPA(it.csi.risca.riscabesrv.util.actaofficialbook.IndirizzoTelematicoType indirizzoTelematicoPA) {
        this.indirizzoTelematicoPA = indirizzoTelematicoPA;
    }


    /**
     * Gets the indirizzoTelematicoAOO value for this InfoCreazioneCorrispondente.
     * 
     * @return indirizzoTelematicoAOO
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.IndirizzoTelematicoType getIndirizzoTelematicoAOO() {
        return indirizzoTelematicoAOO;
    }


    /**
     * Sets the indirizzoTelematicoAOO value for this InfoCreazioneCorrispondente.
     * 
     * @param indirizzoTelematicoAOO
     */
    public void setIndirizzoTelematicoAOO(it.csi.risca.riscabesrv.util.actaofficialbook.IndirizzoTelematicoType indirizzoTelematicoAOO) {
        this.indirizzoTelematicoAOO = indirizzoTelematicoAOO;
    }


    /**
     * Gets the indirizzoTelematicoUO value for this InfoCreazioneCorrispondente.
     * 
     * @return indirizzoTelematicoUO
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.IndirizzoTelematicoType getIndirizzoTelematicoUO() {
        return indirizzoTelematicoUO;
    }


    /**
     * Sets the indirizzoTelematicoUO value for this InfoCreazioneCorrispondente.
     * 
     * @param indirizzoTelematicoUO
     */
    public void setIndirizzoTelematicoUO(it.csi.risca.riscabesrv.util.actaofficialbook.IndirizzoTelematicoType indirizzoTelematicoUO) {
        this.indirizzoTelematicoUO = indirizzoTelematicoUO;
    }


    /**
     * Gets the denominazioneAmministrazione value for this InfoCreazioneCorrispondente.
     * 
     * @return denominazioneAmministrazione
     */
    public java.lang.String getDenominazioneAmministrazione() {
        return denominazioneAmministrazione;
    }


    /**
     * Sets the denominazioneAmministrazione value for this InfoCreazioneCorrispondente.
     * 
     * @param denominazioneAmministrazione
     */
    public void setDenominazioneAmministrazione(java.lang.String denominazioneAmministrazione) {
        this.denominazioneAmministrazione = denominazioneAmministrazione;
    }


    /**
     * Gets the denominazioneUfficio value for this InfoCreazioneCorrispondente.
     * 
     * @return denominazioneUfficio
     */
    public java.lang.String getDenominazioneUfficio() {
        return denominazioneUfficio;
    }


    /**
     * Sets the denominazioneUfficio value for this InfoCreazioneCorrispondente.
     * 
     * @param denominazioneUfficio
     */
    public void setDenominazioneUfficio(java.lang.String denominazioneUfficio) {
        this.denominazioneUfficio = denominazioneUfficio;
    }


    /**
     * Gets the indirizzoTelematico value for this InfoCreazioneCorrispondente.
     * 
     * @return indirizzoTelematico
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.IndirizzoTelematicoType getIndirizzoTelematico() {
        return indirizzoTelematico;
    }


    /**
     * Sets the indirizzoTelematico value for this InfoCreazioneCorrispondente.
     * 
     * @param indirizzoTelematico
     */
    public void setIndirizzoTelematico(it.csi.risca.riscabesrv.util.actaofficialbook.IndirizzoTelematicoType indirizzoTelematico) {
        this.indirizzoTelematico = indirizzoTelematico;
    }


    /**
     * Gets the richiestaConferma value for this InfoCreazioneCorrispondente.
     * 
     * @return richiestaConferma
     */
    public java.lang.Boolean getRichiestaConferma() {
        return richiestaConferma;
    }


    /**
     * Sets the richiestaConferma value for this InfoCreazioneCorrispondente.
     * 
     * @param richiestaConferma
     */
    public void setRichiestaConferma(java.lang.Boolean richiestaConferma) {
        this.richiestaConferma = richiestaConferma;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof InfoCreazioneCorrispondente)) return false;
        InfoCreazioneCorrispondente other = (InfoCreazioneCorrispondente) obj;

        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.denominazione==null && other.getDenominazione()==null) || 
             (this.denominazione!=null &&
              this.denominazione.equals(other.getDenominazione()))) &&
            ((this.nome==null && other.getNome()==null) || 
             (this.nome!=null &&
              this.nome.equals(other.getNome()))) &&
            ((this.cognome==null && other.getCognome()==null) || 
             (this.cognome!=null &&
              this.cognome.equals(other.getCognome()))) &&
            ((this.ordinale==null && other.getOrdinale()==null) || 
             (this.ordinale!=null &&
              this.ordinale.equals(other.getOrdinale()))) &&
            ((this.carica==null && other.getCarica()==null) || 
             (this.carica!=null &&
              this.carica.equals(other.getCarica()))) &&
            ((this.persona==null && other.getPersona()==null) || 
             (this.persona!=null &&
              this.persona.equals(other.getPersona()))) &&
            ((this.testoFoglioTrasmissione==null && other.getTestoFoglioTrasmissione()==null) || 
             (this.testoFoglioTrasmissione!=null &&
              this.testoFoglioTrasmissione.equals(other.getTestoFoglioTrasmissione()))) &&
            ((this.infoSoggettoAssociato==null && other.getInfoSoggettoAssociato()==null) || 
             (this.infoSoggettoAssociato!=null &&
              this.infoSoggettoAssociato.equals(other.getInfoSoggettoAssociato()))) &&
            ((this.tipologiaCorrispondente==null && other.getTipologiaCorrispondente()==null) || 
             (this.tipologiaCorrispondente!=null &&
              this.tipologiaCorrispondente.equals(other.getTipologiaCorrispondente()))) &&
            ((this.codiceFiscale==null && other.getCodiceFiscale()==null) || 
             (this.codiceFiscale!=null &&
              this.codiceFiscale.equals(other.getCodiceFiscale()))) &&
            ((this.PIVA==null && other.getPIVA()==null) || 
             (this.PIVA!=null &&
              this.PIVA.equals(other.getPIVA()))) &&
            ((this.codiceIPAPA==null && other.getCodiceIPAPA()==null) || 
             (this.codiceIPAPA!=null &&
              this.codiceIPAPA.equals(other.getCodiceIPAPA()))) &&
            ((this.codiceIPAAOO==null && other.getCodiceIPAAOO()==null) || 
             (this.codiceIPAAOO!=null &&
              this.codiceIPAAOO.equals(other.getCodiceIPAAOO()))) &&
            ((this.codiceIPAUO==null && other.getCodiceIPAUO()==null) || 
             (this.codiceIPAUO!=null &&
              this.codiceIPAUO.equals(other.getCodiceIPAUO()))) &&
            ((this.indirizzoTelematicoPA==null && other.getIndirizzoTelematicoPA()==null) || 
             (this.indirizzoTelematicoPA!=null &&
              this.indirizzoTelematicoPA.equals(other.getIndirizzoTelematicoPA()))) &&
            ((this.indirizzoTelematicoAOO==null && other.getIndirizzoTelematicoAOO()==null) || 
             (this.indirizzoTelematicoAOO!=null &&
              this.indirizzoTelematicoAOO.equals(other.getIndirizzoTelematicoAOO()))) &&
            ((this.indirizzoTelematicoUO==null && other.getIndirizzoTelematicoUO()==null) || 
             (this.indirizzoTelematicoUO!=null &&
              this.indirizzoTelematicoUO.equals(other.getIndirizzoTelematicoUO()))) &&
            ((this.denominazioneAmministrazione==null && other.getDenominazioneAmministrazione()==null) || 
             (this.denominazioneAmministrazione!=null &&
              this.denominazioneAmministrazione.equals(other.getDenominazioneAmministrazione()))) &&
            ((this.denominazioneUfficio==null && other.getDenominazioneUfficio()==null) || 
             (this.denominazioneUfficio!=null &&
              this.denominazioneUfficio.equals(other.getDenominazioneUfficio()))) &&
            ((this.indirizzoTelematico==null && other.getIndirizzoTelematico()==null) || 
             (this.indirizzoTelematico!=null &&
              this.indirizzoTelematico.equals(other.getIndirizzoTelematico()))) &&
            ((this.richiestaConferma==null && other.getRichiestaConferma()==null) || 
             (this.richiestaConferma!=null &&
              this.richiestaConferma.equals(other.getRichiestaConferma())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getDenominazione() != null) {
            _hashCode += getDenominazione().hashCode();
        }
        if (getNome() != null) {
            _hashCode += getNome().hashCode();
        }
        if (getCognome() != null) {
            _hashCode += getCognome().hashCode();
        }
        if (getOrdinale() != null) {
            _hashCode += getOrdinale().hashCode();
        }
        if (getCarica() != null) {
            _hashCode += getCarica().hashCode();
        }
        if (getPersona() != null) {
            _hashCode += getPersona().hashCode();
        }
        if (getTestoFoglioTrasmissione() != null) {
            _hashCode += getTestoFoglioTrasmissione().hashCode();
        }
        if (getInfoSoggettoAssociato() != null) {
            _hashCode += getInfoSoggettoAssociato().hashCode();
        }
        if (getTipologiaCorrispondente() != null) {
            _hashCode += getTipologiaCorrispondente().hashCode();
        }
        if (getCodiceFiscale() != null) {
            _hashCode += getCodiceFiscale().hashCode();
        }
        if (getPIVA() != null) {
            _hashCode += getPIVA().hashCode();
        }
        if (getCodiceIPAPA() != null) {
            _hashCode += getCodiceIPAPA().hashCode();
        }
        if (getCodiceIPAAOO() != null) {
            _hashCode += getCodiceIPAAOO().hashCode();
        }
        if (getCodiceIPAUO() != null) {
            _hashCode += getCodiceIPAUO().hashCode();
        }
        if (getIndirizzoTelematicoPA() != null) {
            _hashCode += getIndirizzoTelematicoPA().hashCode();
        }
        if (getIndirizzoTelematicoAOO() != null) {
            _hashCode += getIndirizzoTelematicoAOO().hashCode();
        }
        if (getIndirizzoTelematicoUO() != null) {
            _hashCode += getIndirizzoTelematicoUO().hashCode();
        }
        if (getDenominazioneAmministrazione() != null) {
            _hashCode += getDenominazioneAmministrazione().hashCode();
        }
        if (getDenominazioneUfficio() != null) {
            _hashCode += getDenominazioneUfficio().hashCode();
        }
        if (getIndirizzoTelematico() != null) {
            _hashCode += getIndirizzoTelematico().hashCode();
        }
        if (getRichiestaConferma() != null) {
            _hashCode += getRichiestaConferma().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(InfoCreazioneCorrispondente.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "InfoCreazioneCorrispondente"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("denominazione");
        elemField.setXmlName(new javax.xml.namespace.QName("", "denominazione"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nome");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nome"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cognome");
        elemField.setXmlName(new javax.xml.namespace.QName("", "cognome"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ordinale");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ordinale"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("carica");
        elemField.setXmlName(new javax.xml.namespace.QName("", "carica"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("persona");
        elemField.setXmlName(new javax.xml.namespace.QName("", "persona"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("testoFoglioTrasmissione");
        elemField.setXmlName(new javax.xml.namespace.QName("", "testoFoglioTrasmissione"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("infoSoggettoAssociato");
        elemField.setXmlName(new javax.xml.namespace.QName("", "infoSoggettoAssociato"));
        elemField.setXmlType(new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "InfoSoggettoAssociato"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipologiaCorrispondente");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tipologiaCorrispondente"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codiceFiscale");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codiceFiscale"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("PIVA");
        elemField.setXmlName(new javax.xml.namespace.QName("", "PIVA"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codiceIPAPA");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codiceIPAPA"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codiceIPAAOO");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codiceIPAAOO"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codiceIPAUO");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codiceIPAUO"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("indirizzoTelematicoPA");
        elemField.setXmlName(new javax.xml.namespace.QName("", "indirizzoTelematicoPA"));
        elemField.setXmlType(new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "IndirizzoTelematicoType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("indirizzoTelematicoAOO");
        elemField.setXmlName(new javax.xml.namespace.QName("", "indirizzoTelematicoAOO"));
        elemField.setXmlType(new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "IndirizzoTelematicoType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("indirizzoTelematicoUO");
        elemField.setXmlName(new javax.xml.namespace.QName("", "indirizzoTelematicoUO"));
        elemField.setXmlType(new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "IndirizzoTelematicoType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("denominazioneAmministrazione");
        elemField.setXmlName(new javax.xml.namespace.QName("", "denominazioneAmministrazione"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("denominazioneUfficio");
        elemField.setXmlName(new javax.xml.namespace.QName("", "denominazioneUfficio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("indirizzoTelematico");
        elemField.setXmlName(new javax.xml.namespace.QName("", "indirizzoTelematico"));
        elemField.setXmlType(new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "IndirizzoTelematicoType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("richiestaConferma");
        elemField.setXmlName(new javax.xml.namespace.QName("", "richiestaConferma"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}

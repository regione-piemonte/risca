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
 * CorrispondentePropertiesType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actaofficialbook;

public class CorrispondentePropertiesType  extends it.csi.risca.riscabesrv.util.actaofficialbook.OfficialBookPropertiesType  implements java.io.Serializable {
    private java.lang.String denominazione;

    private java.lang.String nome;

    private java.lang.String cognome;

    private java.lang.String tipoMD;

    private boolean interno;

    private int ordinale;

    private java.lang.String carica;

    private java.lang.String persona;

    private java.lang.String motivoCancellazione;

    private java.util.Date dataInizioValidita;

    private java.util.Date dataFineValidita;

    private java.lang.String testoFoglioTrasmissione;

    private it.csi.risca.riscabesrv.util.actaofficialbook.DecodificaType ruolo;

    private it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idSoggetto;

    private it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idRegistrazione;

    public CorrispondentePropertiesType() {
    }

    public CorrispondentePropertiesType(
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType objectId,
           it.csi.risca.riscabesrv.util.actaofficialbook.ChangeTokenType changeToken,
           java.lang.String objectTypeId,
           java.lang.String denominazione,
           java.lang.String nome,
           java.lang.String cognome,
           java.lang.String tipoMD,
           boolean interno,
           int ordinale,
           java.lang.String carica,
           java.lang.String persona,
           java.lang.String motivoCancellazione,
           java.util.Date dataInizioValidita,
           java.util.Date dataFineValidita,
           java.lang.String testoFoglioTrasmissione,
           it.csi.risca.riscabesrv.util.actaofficialbook.DecodificaType ruolo,
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idSoggetto,
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idRegistrazione) {
        super(
            objectId,
            changeToken,
            objectTypeId);
        this.denominazione = denominazione;
        this.nome = nome;
        this.cognome = cognome;
        this.tipoMD = tipoMD;
        this.interno = interno;
        this.ordinale = ordinale;
        this.carica = carica;
        this.persona = persona;
        this.motivoCancellazione = motivoCancellazione;
        this.dataInizioValidita = dataInizioValidita;
        this.dataFineValidita = dataFineValidita;
        this.testoFoglioTrasmissione = testoFoglioTrasmissione;
        this.ruolo = ruolo;
        this.idSoggetto = idSoggetto;
        this.idRegistrazione = idRegistrazione;
    }


    /**
     * Gets the denominazione value for this CorrispondentePropertiesType.
     * 
     * @return denominazione
     */
    public java.lang.String getDenominazione() {
        return denominazione;
    }


    /**
     * Sets the denominazione value for this CorrispondentePropertiesType.
     * 
     * @param denominazione
     */
    public void setDenominazione(java.lang.String denominazione) {
        this.denominazione = denominazione;
    }


    /**
     * Gets the nome value for this CorrispondentePropertiesType.
     * 
     * @return nome
     */
    public java.lang.String getNome() {
        return nome;
    }


    /**
     * Sets the nome value for this CorrispondentePropertiesType.
     * 
     * @param nome
     */
    public void setNome(java.lang.String nome) {
        this.nome = nome;
    }


    /**
     * Gets the cognome value for this CorrispondentePropertiesType.
     * 
     * @return cognome
     */
    public java.lang.String getCognome() {
        return cognome;
    }


    /**
     * Sets the cognome value for this CorrispondentePropertiesType.
     * 
     * @param cognome
     */
    public void setCognome(java.lang.String cognome) {
        this.cognome = cognome;
    }


    /**
     * Gets the tipoMD value for this CorrispondentePropertiesType.
     * 
     * @return tipoMD
     */
    public java.lang.String getTipoMD() {
        return tipoMD;
    }


    /**
     * Sets the tipoMD value for this CorrispondentePropertiesType.
     * 
     * @param tipoMD
     */
    public void setTipoMD(java.lang.String tipoMD) {
        this.tipoMD = tipoMD;
    }


    /**
     * Gets the interno value for this CorrispondentePropertiesType.
     * 
     * @return interno
     */
    public boolean isInterno() {
        return interno;
    }


    /**
     * Sets the interno value for this CorrispondentePropertiesType.
     * 
     * @param interno
     */
    public void setInterno(boolean interno) {
        this.interno = interno;
    }


    /**
     * Gets the ordinale value for this CorrispondentePropertiesType.
     * 
     * @return ordinale
     */
    public int getOrdinale() {
        return ordinale;
    }


    /**
     * Sets the ordinale value for this CorrispondentePropertiesType.
     * 
     * @param ordinale
     */
    public void setOrdinale(int ordinale) {
        this.ordinale = ordinale;
    }


    /**
     * Gets the carica value for this CorrispondentePropertiesType.
     * 
     * @return carica
     */
    public java.lang.String getCarica() {
        return carica;
    }


    /**
     * Sets the carica value for this CorrispondentePropertiesType.
     * 
     * @param carica
     */
    public void setCarica(java.lang.String carica) {
        this.carica = carica;
    }


    /**
     * Gets the persona value for this CorrispondentePropertiesType.
     * 
     * @return persona
     */
    public java.lang.String getPersona() {
        return persona;
    }


    /**
     * Sets the persona value for this CorrispondentePropertiesType.
     * 
     * @param persona
     */
    public void setPersona(java.lang.String persona) {
        this.persona = persona;
    }


    /**
     * Gets the motivoCancellazione value for this CorrispondentePropertiesType.
     * 
     * @return motivoCancellazione
     */
    public java.lang.String getMotivoCancellazione() {
        return motivoCancellazione;
    }


    /**
     * Sets the motivoCancellazione value for this CorrispondentePropertiesType.
     * 
     * @param motivoCancellazione
     */
    public void setMotivoCancellazione(java.lang.String motivoCancellazione) {
        this.motivoCancellazione = motivoCancellazione;
    }


    /**
     * Gets the dataInizioValidita value for this CorrispondentePropertiesType.
     * 
     * @return dataInizioValidita
     */
    public java.util.Date getDataInizioValidita() {
        return dataInizioValidita;
    }


    /**
     * Sets the dataInizioValidita value for this CorrispondentePropertiesType.
     * 
     * @param dataInizioValidita
     */
    public void setDataInizioValidita(java.util.Date dataInizioValidita) {
        this.dataInizioValidita = dataInizioValidita;
    }


    /**
     * Gets the dataFineValidita value for this CorrispondentePropertiesType.
     * 
     * @return dataFineValidita
     */
    public java.util.Date getDataFineValidita() {
        return dataFineValidita;
    }


    /**
     * Sets the dataFineValidita value for this CorrispondentePropertiesType.
     * 
     * @param dataFineValidita
     */
    public void setDataFineValidita(java.util.Date dataFineValidita) {
        this.dataFineValidita = dataFineValidita;
    }


    /**
     * Gets the testoFoglioTrasmissione value for this CorrispondentePropertiesType.
     * 
     * @return testoFoglioTrasmissione
     */
    public java.lang.String getTestoFoglioTrasmissione() {
        return testoFoglioTrasmissione;
    }


    /**
     * Sets the testoFoglioTrasmissione value for this CorrispondentePropertiesType.
     * 
     * @param testoFoglioTrasmissione
     */
    public void setTestoFoglioTrasmissione(java.lang.String testoFoglioTrasmissione) {
        this.testoFoglioTrasmissione = testoFoglioTrasmissione;
    }


    /**
     * Gets the ruolo value for this CorrispondentePropertiesType.
     * 
     * @return ruolo
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.DecodificaType getRuolo() {
        return ruolo;
    }


    /**
     * Sets the ruolo value for this CorrispondentePropertiesType.
     * 
     * @param ruolo
     */
    public void setRuolo(it.csi.risca.riscabesrv.util.actaofficialbook.DecodificaType ruolo) {
        this.ruolo = ruolo;
    }


    /**
     * Gets the idSoggetto value for this CorrispondentePropertiesType.
     * 
     * @return idSoggetto
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType getIdSoggetto() {
        return idSoggetto;
    }


    /**
     * Sets the idSoggetto value for this CorrispondentePropertiesType.
     * 
     * @param idSoggetto
     */
    public void setIdSoggetto(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idSoggetto) {
        this.idSoggetto = idSoggetto;
    }


    /**
     * Gets the idRegistrazione value for this CorrispondentePropertiesType.
     * 
     * @return idRegistrazione
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType getIdRegistrazione() {
        return idRegistrazione;
    }


    /**
     * Sets the idRegistrazione value for this CorrispondentePropertiesType.
     * 
     * @param idRegistrazione
     */
    public void setIdRegistrazione(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idRegistrazione) {
        this.idRegistrazione = idRegistrazione;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof CorrispondentePropertiesType)) return false;
        CorrispondentePropertiesType other = (CorrispondentePropertiesType) obj;

        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.denominazione==null && other.getDenominazione()==null) || 
             (this.denominazione!=null &&
              this.denominazione.equals(other.getDenominazione()))) &&
            ((this.nome==null && other.getNome()==null) || 
             (this.nome!=null &&
              this.nome.equals(other.getNome()))) &&
            ((this.cognome==null && other.getCognome()==null) || 
             (this.cognome!=null &&
              this.cognome.equals(other.getCognome()))) &&
            ((this.tipoMD==null && other.getTipoMD()==null) || 
             (this.tipoMD!=null &&
              this.tipoMD.equals(other.getTipoMD()))) &&
            this.interno == other.isInterno() &&
            this.ordinale == other.getOrdinale() &&
            ((this.carica==null && other.getCarica()==null) || 
             (this.carica!=null &&
              this.carica.equals(other.getCarica()))) &&
            ((this.persona==null && other.getPersona()==null) || 
             (this.persona!=null &&
              this.persona.equals(other.getPersona()))) &&
            ((this.motivoCancellazione==null && other.getMotivoCancellazione()==null) || 
             (this.motivoCancellazione!=null &&
              this.motivoCancellazione.equals(other.getMotivoCancellazione()))) &&
            ((this.dataInizioValidita==null && other.getDataInizioValidita()==null) || 
             (this.dataInizioValidita!=null &&
              this.dataInizioValidita.equals(other.getDataInizioValidita()))) &&
            ((this.dataFineValidita==null && other.getDataFineValidita()==null) || 
             (this.dataFineValidita!=null &&
              this.dataFineValidita.equals(other.getDataFineValidita()))) &&
            ((this.testoFoglioTrasmissione==null && other.getTestoFoglioTrasmissione()==null) || 
             (this.testoFoglioTrasmissione!=null &&
              this.testoFoglioTrasmissione.equals(other.getTestoFoglioTrasmissione()))) &&
            ((this.ruolo==null && other.getRuolo()==null) || 
             (this.ruolo!=null &&
              this.ruolo.equals(other.getRuolo()))) &&
            ((this.idSoggetto==null && other.getIdSoggetto()==null) || 
             (this.idSoggetto!=null &&
              this.idSoggetto.equals(other.getIdSoggetto()))) &&
            ((this.idRegistrazione==null && other.getIdRegistrazione()==null) || 
             (this.idRegistrazione!=null &&
              this.idRegistrazione.equals(other.getIdRegistrazione())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = super.hashCode();
        if (getDenominazione() != null) {
            _hashCode += getDenominazione().hashCode();
        }
        if (getNome() != null) {
            _hashCode += getNome().hashCode();
        }
        if (getCognome() != null) {
            _hashCode += getCognome().hashCode();
        }
        if (getTipoMD() != null) {
            _hashCode += getTipoMD().hashCode();
        }
        _hashCode += (isInterno() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += getOrdinale();
        if (getCarica() != null) {
            _hashCode += getCarica().hashCode();
        }
        if (getPersona() != null) {
            _hashCode += getPersona().hashCode();
        }
        if (getMotivoCancellazione() != null) {
            _hashCode += getMotivoCancellazione().hashCode();
        }
        if (getDataInizioValidita() != null) {
            _hashCode += getDataInizioValidita().hashCode();
        }
        if (getDataFineValidita() != null) {
            _hashCode += getDataFineValidita().hashCode();
        }
        if (getTestoFoglioTrasmissione() != null) {
            _hashCode += getTestoFoglioTrasmissione().hashCode();
        }
        if (getRuolo() != null) {
            _hashCode += getRuolo().hashCode();
        }
        if (getIdSoggetto() != null) {
            _hashCode += getIdSoggetto().hashCode();
        }
        if (getIdRegistrazione() != null) {
            _hashCode += getIdRegistrazione().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CorrispondentePropertiesType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "CorrispondentePropertiesType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("denominazione");
        elemField.setXmlName(new javax.xml.namespace.QName("", "denominazione"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nome");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nome"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cognome");
        elemField.setXmlName(new javax.xml.namespace.QName("", "cognome"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoMD");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tipoMD"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("interno");
        elemField.setXmlName(new javax.xml.namespace.QName("", "interno"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ordinale");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ordinale"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("carica");
        elemField.setXmlName(new javax.xml.namespace.QName("", "carica"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("persona");
        elemField.setXmlName(new javax.xml.namespace.QName("", "persona"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("motivoCancellazione");
        elemField.setXmlName(new javax.xml.namespace.QName("", "motivoCancellazione"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataInizioValidita");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataInizioValidita"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataFineValidita");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataFineValidita"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("testoFoglioTrasmissione");
        elemField.setXmlName(new javax.xml.namespace.QName("", "testoFoglioTrasmissione"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ruolo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ruolo"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "DecodificaType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idSoggetto");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idSoggetto"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idRegistrazione");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idRegistrazione"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"));
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

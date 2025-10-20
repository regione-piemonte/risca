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
 * CorrispondenteMessaggioPropertiesType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actaofficialbook;

public class CorrispondenteMessaggioPropertiesType  extends it.csi.risca.riscabesrv.util.actaofficialbook.OfficialBookPropertiesType  implements java.io.Serializable {
    private java.lang.String tipoMD;

    private java.lang.String email;

    private java.lang.String corrispondente;

    private java.lang.String protocollo;

    private java.util.Date dataProtocollo;

    private java.lang.String denominazioneAmministrazione;

    private java.lang.String denominazioneAOO;

    private boolean primaRegistrazione;

    private boolean competenza;

    private boolean richiestaConferma;

    private boolean confermaInviata;

    private it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idMessaggio;

    private it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idSoggetto;

    public CorrispondenteMessaggioPropertiesType() {
    }

    public CorrispondenteMessaggioPropertiesType(
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType objectId,
           it.csi.risca.riscabesrv.util.actaofficialbook.ChangeTokenType changeToken,
           java.lang.String objectTypeId,
           java.lang.String tipoMD,
           java.lang.String email,
           java.lang.String corrispondente,
           java.lang.String protocollo,
           java.util.Date dataProtocollo,
           java.lang.String denominazioneAmministrazione,
           java.lang.String denominazioneAOO,
           boolean primaRegistrazione,
           boolean competenza,
           boolean richiestaConferma,
           boolean confermaInviata,
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idMessaggio,
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idSoggetto) {
        super(
            objectId,
            changeToken,
            objectTypeId);
        this.tipoMD = tipoMD;
        this.email = email;
        this.corrispondente = corrispondente;
        this.protocollo = protocollo;
        this.dataProtocollo = dataProtocollo;
        this.denominazioneAmministrazione = denominazioneAmministrazione;
        this.denominazioneAOO = denominazioneAOO;
        this.primaRegistrazione = primaRegistrazione;
        this.competenza = competenza;
        this.richiestaConferma = richiestaConferma;
        this.confermaInviata = confermaInviata;
        this.idMessaggio = idMessaggio;
        this.idSoggetto = idSoggetto;
    }


    /**
     * Gets the tipoMD value for this CorrispondenteMessaggioPropertiesType.
     * 
     * @return tipoMD
     */
    public java.lang.String getTipoMD() {
        return tipoMD;
    }


    /**
     * Sets the tipoMD value for this CorrispondenteMessaggioPropertiesType.
     * 
     * @param tipoMD
     */
    public void setTipoMD(java.lang.String tipoMD) {
        this.tipoMD = tipoMD;
    }


    /**
     * Gets the email value for this CorrispondenteMessaggioPropertiesType.
     * 
     * @return email
     */
    public java.lang.String getEmail() {
        return email;
    }


    /**
     * Sets the email value for this CorrispondenteMessaggioPropertiesType.
     * 
     * @param email
     */
    public void setEmail(java.lang.String email) {
        this.email = email;
    }


    /**
     * Gets the corrispondente value for this CorrispondenteMessaggioPropertiesType.
     * 
     * @return corrispondente
     */
    public java.lang.String getCorrispondente() {
        return corrispondente;
    }


    /**
     * Sets the corrispondente value for this CorrispondenteMessaggioPropertiesType.
     * 
     * @param corrispondente
     */
    public void setCorrispondente(java.lang.String corrispondente) {
        this.corrispondente = corrispondente;
    }


    /**
     * Gets the protocollo value for this CorrispondenteMessaggioPropertiesType.
     * 
     * @return protocollo
     */
    public java.lang.String getProtocollo() {
        return protocollo;
    }


    /**
     * Sets the protocollo value for this CorrispondenteMessaggioPropertiesType.
     * 
     * @param protocollo
     */
    public void setProtocollo(java.lang.String protocollo) {
        this.protocollo = protocollo;
    }


    /**
     * Gets the dataProtocollo value for this CorrispondenteMessaggioPropertiesType.
     * 
     * @return dataProtocollo
     */
    public java.util.Date getDataProtocollo() {
        return dataProtocollo;
    }


    /**
     * Sets the dataProtocollo value for this CorrispondenteMessaggioPropertiesType.
     * 
     * @param dataProtocollo
     */
    public void setDataProtocollo(java.util.Date dataProtocollo) {
        this.dataProtocollo = dataProtocollo;
    }


    /**
     * Gets the denominazioneAmministrazione value for this CorrispondenteMessaggioPropertiesType.
     * 
     * @return denominazioneAmministrazione
     */
    public java.lang.String getDenominazioneAmministrazione() {
        return denominazioneAmministrazione;
    }


    /**
     * Sets the denominazioneAmministrazione value for this CorrispondenteMessaggioPropertiesType.
     * 
     * @param denominazioneAmministrazione
     */
    public void setDenominazioneAmministrazione(java.lang.String denominazioneAmministrazione) {
        this.denominazioneAmministrazione = denominazioneAmministrazione;
    }


    /**
     * Gets the denominazioneAOO value for this CorrispondenteMessaggioPropertiesType.
     * 
     * @return denominazioneAOO
     */
    public java.lang.String getDenominazioneAOO() {
        return denominazioneAOO;
    }


    /**
     * Sets the denominazioneAOO value for this CorrispondenteMessaggioPropertiesType.
     * 
     * @param denominazioneAOO
     */
    public void setDenominazioneAOO(java.lang.String denominazioneAOO) {
        this.denominazioneAOO = denominazioneAOO;
    }


    /**
     * Gets the primaRegistrazione value for this CorrispondenteMessaggioPropertiesType.
     * 
     * @return primaRegistrazione
     */
    public boolean isPrimaRegistrazione() {
        return primaRegistrazione;
    }


    /**
     * Sets the primaRegistrazione value for this CorrispondenteMessaggioPropertiesType.
     * 
     * @param primaRegistrazione
     */
    public void setPrimaRegistrazione(boolean primaRegistrazione) {
        this.primaRegistrazione = primaRegistrazione;
    }


    /**
     * Gets the competenza value for this CorrispondenteMessaggioPropertiesType.
     * 
     * @return competenza
     */
    public boolean isCompetenza() {
        return competenza;
    }


    /**
     * Sets the competenza value for this CorrispondenteMessaggioPropertiesType.
     * 
     * @param competenza
     */
    public void setCompetenza(boolean competenza) {
        this.competenza = competenza;
    }


    /**
     * Gets the richiestaConferma value for this CorrispondenteMessaggioPropertiesType.
     * 
     * @return richiestaConferma
     */
    public boolean isRichiestaConferma() {
        return richiestaConferma;
    }


    /**
     * Sets the richiestaConferma value for this CorrispondenteMessaggioPropertiesType.
     * 
     * @param richiestaConferma
     */
    public void setRichiestaConferma(boolean richiestaConferma) {
        this.richiestaConferma = richiestaConferma;
    }


    /**
     * Gets the confermaInviata value for this CorrispondenteMessaggioPropertiesType.
     * 
     * @return confermaInviata
     */
    public boolean isConfermaInviata() {
        return confermaInviata;
    }


    /**
     * Sets the confermaInviata value for this CorrispondenteMessaggioPropertiesType.
     * 
     * @param confermaInviata
     */
    public void setConfermaInviata(boolean confermaInviata) {
        this.confermaInviata = confermaInviata;
    }


    /**
     * Gets the idMessaggio value for this CorrispondenteMessaggioPropertiesType.
     * 
     * @return idMessaggio
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType getIdMessaggio() {
        return idMessaggio;
    }


    /**
     * Sets the idMessaggio value for this CorrispondenteMessaggioPropertiesType.
     * 
     * @param idMessaggio
     */
    public void setIdMessaggio(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idMessaggio) {
        this.idMessaggio = idMessaggio;
    }


    /**
     * Gets the idSoggetto value for this CorrispondenteMessaggioPropertiesType.
     * 
     * @return idSoggetto
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType getIdSoggetto() {
        return idSoggetto;
    }


    /**
     * Sets the idSoggetto value for this CorrispondenteMessaggioPropertiesType.
     * 
     * @param idSoggetto
     */
    public void setIdSoggetto(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idSoggetto) {
        this.idSoggetto = idSoggetto;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof CorrispondenteMessaggioPropertiesType)) return false;
        CorrispondenteMessaggioPropertiesType other = (CorrispondenteMessaggioPropertiesType) obj;

        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.tipoMD==null && other.getTipoMD()==null) || 
             (this.tipoMD!=null &&
              this.tipoMD.equals(other.getTipoMD()))) &&
            ((this.email==null && other.getEmail()==null) || 
             (this.email!=null &&
              this.email.equals(other.getEmail()))) &&
            ((this.corrispondente==null && other.getCorrispondente()==null) || 
             (this.corrispondente!=null &&
              this.corrispondente.equals(other.getCorrispondente()))) &&
            ((this.protocollo==null && other.getProtocollo()==null) || 
             (this.protocollo!=null &&
              this.protocollo.equals(other.getProtocollo()))) &&
            ((this.dataProtocollo==null && other.getDataProtocollo()==null) || 
             (this.dataProtocollo!=null &&
              this.dataProtocollo.equals(other.getDataProtocollo()))) &&
            ((this.denominazioneAmministrazione==null && other.getDenominazioneAmministrazione()==null) || 
             (this.denominazioneAmministrazione!=null &&
              this.denominazioneAmministrazione.equals(other.getDenominazioneAmministrazione()))) &&
            ((this.denominazioneAOO==null && other.getDenominazioneAOO()==null) || 
             (this.denominazioneAOO!=null &&
              this.denominazioneAOO.equals(other.getDenominazioneAOO()))) &&
            this.primaRegistrazione == other.isPrimaRegistrazione() &&
            this.competenza == other.isCompetenza() &&
            this.richiestaConferma == other.isRichiestaConferma() &&
            this.confermaInviata == other.isConfermaInviata() &&
            ((this.idMessaggio==null && other.getIdMessaggio()==null) || 
             (this.idMessaggio!=null &&
              this.idMessaggio.equals(other.getIdMessaggio()))) &&
            ((this.idSoggetto==null && other.getIdSoggetto()==null) || 
             (this.idSoggetto!=null &&
              this.idSoggetto.equals(other.getIdSoggetto())));
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
        if (getTipoMD() != null) {
            _hashCode += getTipoMD().hashCode();
        }
        if (getEmail() != null) {
            _hashCode += getEmail().hashCode();
        }
        if (getCorrispondente() != null) {
            _hashCode += getCorrispondente().hashCode();
        }
        if (getProtocollo() != null) {
            _hashCode += getProtocollo().hashCode();
        }
        if (getDataProtocollo() != null) {
            _hashCode += getDataProtocollo().hashCode();
        }
        if (getDenominazioneAmministrazione() != null) {
            _hashCode += getDenominazioneAmministrazione().hashCode();
        }
        if (getDenominazioneAOO() != null) {
            _hashCode += getDenominazioneAOO().hashCode();
        }
        _hashCode += (isPrimaRegistrazione() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isCompetenza() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isRichiestaConferma() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isConfermaInviata() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getIdMessaggio() != null) {
            _hashCode += getIdMessaggio().hashCode();
        }
        if (getIdSoggetto() != null) {
            _hashCode += getIdSoggetto().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CorrispondenteMessaggioPropertiesType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "CorrispondenteMessaggioPropertiesType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoMD");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tipoMD"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("email");
        elemField.setXmlName(new javax.xml.namespace.QName("", "email"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("corrispondente");
        elemField.setXmlName(new javax.xml.namespace.QName("", "corrispondente"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("protocollo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "protocollo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataProtocollo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataProtocollo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("denominazioneAmministrazione");
        elemField.setXmlName(new javax.xml.namespace.QName("", "denominazioneAmministrazione"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("denominazioneAOO");
        elemField.setXmlName(new javax.xml.namespace.QName("", "denominazioneAOO"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("primaRegistrazione");
        elemField.setXmlName(new javax.xml.namespace.QName("", "primaRegistrazione"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("competenza");
        elemField.setXmlName(new javax.xml.namespace.QName("", "competenza"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("richiestaConferma");
        elemField.setXmlName(new javax.xml.namespace.QName("", "richiestaConferma"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("confermaInviata");
        elemField.setXmlName(new javax.xml.namespace.QName("", "confermaInviata"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idMessaggio");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idMessaggio"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idSoggetto");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idSoggetto"));
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

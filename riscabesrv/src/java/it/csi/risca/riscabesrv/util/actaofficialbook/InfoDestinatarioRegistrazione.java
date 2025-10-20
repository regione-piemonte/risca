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
 * InfoDestinatarioRegistrazione.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actaofficialbook;

public class InfoDestinatarioRegistrazione  implements java.io.Serializable {
    private it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType identificatore;

    private java.lang.String casella;

    private boolean confermaRicezione;

    private boolean usaCanalePec;

    public InfoDestinatarioRegistrazione() {
    }

    public InfoDestinatarioRegistrazione(
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType identificatore,
           java.lang.String casella,
           boolean confermaRicezione,
           boolean usaCanalePec) {
           this.identificatore = identificatore;
           this.casella = casella;
           this.confermaRicezione = confermaRicezione;
           this.usaCanalePec = usaCanalePec;
    }


    /**
     * Gets the identificatore value for this InfoDestinatarioRegistrazione.
     * 
     * @return identificatore
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType getIdentificatore() {
        return identificatore;
    }


    /**
     * Sets the identificatore value for this InfoDestinatarioRegistrazione.
     * 
     * @param identificatore
     */
    public void setIdentificatore(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType identificatore) {
        this.identificatore = identificatore;
    }


    /**
     * Gets the casella value for this InfoDestinatarioRegistrazione.
     * 
     * @return casella
     */
    public java.lang.String getCasella() {
        return casella;
    }


    /**
     * Sets the casella value for this InfoDestinatarioRegistrazione.
     * 
     * @param casella
     */
    public void setCasella(java.lang.String casella) {
        this.casella = casella;
    }


    /**
     * Gets the confermaRicezione value for this InfoDestinatarioRegistrazione.
     * 
     * @return confermaRicezione
     */
    public boolean isConfermaRicezione() {
        return confermaRicezione;
    }


    /**
     * Sets the confermaRicezione value for this InfoDestinatarioRegistrazione.
     * 
     * @param confermaRicezione
     */
    public void setConfermaRicezione(boolean confermaRicezione) {
        this.confermaRicezione = confermaRicezione;
    }


    /**
     * Gets the usaCanalePec value for this InfoDestinatarioRegistrazione.
     * 
     * @return usaCanalePec
     */
    public boolean isUsaCanalePec() {
        return usaCanalePec;
    }


    /**
     * Sets the usaCanalePec value for this InfoDestinatarioRegistrazione.
     * 
     * @param usaCanalePec
     */
    public void setUsaCanalePec(boolean usaCanalePec) {
        this.usaCanalePec = usaCanalePec;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof InfoDestinatarioRegistrazione)) return false;
        InfoDestinatarioRegistrazione other = (InfoDestinatarioRegistrazione) obj;

        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.identificatore==null && other.getIdentificatore()==null) || 
             (this.identificatore!=null &&
              this.identificatore.equals(other.getIdentificatore()))) &&
            ((this.casella==null && other.getCasella()==null) || 
             (this.casella!=null &&
              this.casella.equals(other.getCasella()))) &&
            this.confermaRicezione == other.isConfermaRicezione() &&
            this.usaCanalePec == other.isUsaCanalePec();
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
        if (getIdentificatore() != null) {
            _hashCode += getIdentificatore().hashCode();
        }
        if (getCasella() != null) {
            _hashCode += getCasella().hashCode();
        }
        _hashCode += (isConfermaRicezione() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isUsaCanalePec() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(InfoDestinatarioRegistrazione.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "InfoDestinatarioRegistrazione"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("identificatore");
        elemField.setXmlName(new javax.xml.namespace.QName("", "identificatore"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("casella");
        elemField.setXmlName(new javax.xml.namespace.QName("", "casella"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("confermaRicezione");
        elemField.setXmlName(new javax.xml.namespace.QName("", "confermaRicezione"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("usaCanalePec");
        elemField.setXmlName(new javax.xml.namespace.QName("", "usaCanalePec"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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

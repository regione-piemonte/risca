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
 * InfoMittenteIS.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actaofficialbook;

public class InfoMittenteIS  implements java.io.Serializable {
    private it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType identificatore;

    private java.lang.String casella;

    public InfoMittenteIS() {
    }

    public InfoMittenteIS(
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType identificatore,
           java.lang.String casella) {
           this.identificatore = identificatore;
           this.casella = casella;
    }


    /**
     * Gets the identificatore value for this InfoMittenteIS.
     * 
     * @return identificatore
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType getIdentificatore() {
        return identificatore;
    }


    /**
     * Sets the identificatore value for this InfoMittenteIS.
     * 
     * @param identificatore
     */
    public void setIdentificatore(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType identificatore) {
        this.identificatore = identificatore;
    }


    /**
     * Gets the casella value for this InfoMittenteIS.
     * 
     * @return casella
     */
    public java.lang.String getCasella() {
        return casella;
    }


    /**
     * Sets the casella value for this InfoMittenteIS.
     * 
     * @param casella
     */
    public void setCasella(java.lang.String casella) {
        this.casella = casella;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof InfoMittenteIS)) return false;
        InfoMittenteIS other = (InfoMittenteIS) obj;

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
              this.casella.equals(other.getCasella())));
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
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(InfoMittenteIS.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "InfoMittenteIS"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("identificatore");
        elemField.setXmlName(new javax.xml.namespace.QName("", "identificatore"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("casella");
        elemField.setXmlName(new javax.xml.namespace.QName("", "casella"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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

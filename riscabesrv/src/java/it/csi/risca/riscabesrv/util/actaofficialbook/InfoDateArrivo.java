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
 * InfoDateArrivo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actaofficialbook;

public class InfoDateArrivo  implements java.io.Serializable {
    private java.util.Date dataRicezione;

    private java.util.Date dataTimbroPostale;

    public InfoDateArrivo() {
    }

    public InfoDateArrivo(
           java.util.Date dataRicezione,
           java.util.Date dataTimbroPostale) {
           this.dataRicezione = dataRicezione;
           this.dataTimbroPostale = dataTimbroPostale;
    }


    /**
     * Gets the dataRicezione value for this InfoDateArrivo.
     * 
     * @return dataRicezione
     */
    public java.util.Date getDataRicezione() {
        return dataRicezione;
    }


    /**
     * Sets the dataRicezione value for this InfoDateArrivo.
     * 
     * @param dataRicezione
     */
    public void setDataRicezione(java.util.Date dataRicezione) {
        this.dataRicezione = dataRicezione;
    }


    /**
     * Gets the dataTimbroPostale value for this InfoDateArrivo.
     * 
     * @return dataTimbroPostale
     */
    public java.util.Date getDataTimbroPostale() {
        return dataTimbroPostale;
    }


    /**
     * Sets the dataTimbroPostale value for this InfoDateArrivo.
     * 
     * @param dataTimbroPostale
     */
    public void setDataTimbroPostale(java.util.Date dataTimbroPostale) {
        this.dataTimbroPostale = dataTimbroPostale;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof InfoDateArrivo)) return false;
        InfoDateArrivo other = (InfoDateArrivo) obj;

        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.dataRicezione==null && other.getDataRicezione()==null) || 
             (this.dataRicezione!=null &&
              this.dataRicezione.equals(other.getDataRicezione()))) &&
            ((this.dataTimbroPostale==null && other.getDataTimbroPostale()==null) || 
             (this.dataTimbroPostale!=null &&
              this.dataTimbroPostale.equals(other.getDataTimbroPostale())));
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
        if (getDataRicezione() != null) {
            _hashCode += getDataRicezione().hashCode();
        }
        if (getDataTimbroPostale() != null) {
            _hashCode += getDataTimbroPostale().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(InfoDateArrivo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "InfoDateArrivo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataRicezione");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataRicezione"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataTimbroPostale");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataTimbroPostale"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
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

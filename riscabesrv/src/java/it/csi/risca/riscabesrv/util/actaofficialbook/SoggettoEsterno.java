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
 * SoggettoEsterno.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actaofficialbook;

public class SoggettoEsterno  extends it.csi.risca.riscabesrv.util.actaofficialbook.InfoSoggettoAssociato  implements java.io.Serializable {
    private java.lang.String chiaveEsterna;

    private java.lang.String idPFPGUL;

    private java.lang.String codiceTipoSoggetto;

    private java.lang.String codiceFonte;

    public SoggettoEsterno() {
    }

    public SoggettoEsterno(
           java.lang.String chiaveEsterna,
           java.lang.String idPFPGUL,
           java.lang.String codiceTipoSoggetto,
           java.lang.String codiceFonte) {
        this.chiaveEsterna = chiaveEsterna;
        this.idPFPGUL = idPFPGUL;
        this.codiceTipoSoggetto = codiceTipoSoggetto;
        this.codiceFonte = codiceFonte;
    }


    /**
     * Gets the chiaveEsterna value for this SoggettoEsterno.
     * 
     * @return chiaveEsterna
     */
    public java.lang.String getChiaveEsterna() {
        return chiaveEsterna;
    }


    /**
     * Sets the chiaveEsterna value for this SoggettoEsterno.
     * 
     * @param chiaveEsterna
     */
    public void setChiaveEsterna(java.lang.String chiaveEsterna) {
        this.chiaveEsterna = chiaveEsterna;
    }


    /**
     * Gets the idPFPGUL value for this SoggettoEsterno.
     * 
     * @return idPFPGUL
     */
    public java.lang.String getIdPFPGUL() {
        return idPFPGUL;
    }


    /**
     * Sets the idPFPGUL value for this SoggettoEsterno.
     * 
     * @param idPFPGUL
     */
    public void setIdPFPGUL(java.lang.String idPFPGUL) {
        this.idPFPGUL = idPFPGUL;
    }


    /**
     * Gets the codiceTipoSoggetto value for this SoggettoEsterno.
     * 
     * @return codiceTipoSoggetto
     */
    public java.lang.String getCodiceTipoSoggetto() {
        return codiceTipoSoggetto;
    }


    /**
     * Sets the codiceTipoSoggetto value for this SoggettoEsterno.
     * 
     * @param codiceTipoSoggetto
     */
    public void setCodiceTipoSoggetto(java.lang.String codiceTipoSoggetto) {
        this.codiceTipoSoggetto = codiceTipoSoggetto;
    }


    /**
     * Gets the codiceFonte value for this SoggettoEsterno.
     * 
     * @return codiceFonte
     */
    public java.lang.String getCodiceFonte() {
        return codiceFonte;
    }


    /**
     * Sets the codiceFonte value for this SoggettoEsterno.
     * 
     * @param codiceFonte
     */
    public void setCodiceFonte(java.lang.String codiceFonte) {
        this.codiceFonte = codiceFonte;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof SoggettoEsterno)) return false;
        SoggettoEsterno other = (SoggettoEsterno) obj;

        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.chiaveEsterna==null && other.getChiaveEsterna()==null) || 
             (this.chiaveEsterna!=null &&
              this.chiaveEsterna.equals(other.getChiaveEsterna()))) &&
            ((this.idPFPGUL==null && other.getIdPFPGUL()==null) || 
             (this.idPFPGUL!=null &&
              this.idPFPGUL.equals(other.getIdPFPGUL()))) &&
            ((this.codiceTipoSoggetto==null && other.getCodiceTipoSoggetto()==null) || 
             (this.codiceTipoSoggetto!=null &&
              this.codiceTipoSoggetto.equals(other.getCodiceTipoSoggetto()))) &&
            ((this.codiceFonte==null && other.getCodiceFonte()==null) || 
             (this.codiceFonte!=null &&
              this.codiceFonte.equals(other.getCodiceFonte())));
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
        if (getChiaveEsterna() != null) {
            _hashCode += getChiaveEsterna().hashCode();
        }
        if (getIdPFPGUL() != null) {
            _hashCode += getIdPFPGUL().hashCode();
        }
        if (getCodiceTipoSoggetto() != null) {
            _hashCode += getCodiceTipoSoggetto().hashCode();
        }
        if (getCodiceFonte() != null) {
            _hashCode += getCodiceFonte().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SoggettoEsterno.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "SoggettoEsterno"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("chiaveEsterna");
        elemField.setXmlName(new javax.xml.namespace.QName("", "chiaveEsterna"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idPFPGUL");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idPFPGUL"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codiceTipoSoggetto");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codiceTipoSoggetto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codiceFonte");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codiceFonte"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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

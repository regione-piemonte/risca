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
 * RegistrazioneAPI.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actaofficialbook;

public class RegistrazioneAPI  implements java.io.Serializable {
    private java.lang.String tipoRegistrazione;

    private it.csi.risca.riscabesrv.util.actaofficialbook.InfoCreazioneRegistrazione infoCreazione;

    public RegistrazioneAPI() {
    }

    public RegistrazioneAPI(
           java.lang.String tipoRegistrazione,
           it.csi.risca.riscabesrv.util.actaofficialbook.InfoCreazioneRegistrazione infoCreazione) {
           this.tipoRegistrazione = tipoRegistrazione;
           this.infoCreazione = infoCreazione;
    }


    /**
     * Gets the tipoRegistrazione value for this RegistrazioneAPI.
     * 
     * @return tipoRegistrazione
     */
    public java.lang.String getTipoRegistrazione() {
        return tipoRegistrazione;
    }


    /**
     * Sets the tipoRegistrazione value for this RegistrazioneAPI.
     * 
     * @param tipoRegistrazione
     */
    public void setTipoRegistrazione(java.lang.String tipoRegistrazione) {
        this.tipoRegistrazione = tipoRegistrazione;
    }


    /**
     * Gets the infoCreazione value for this RegistrazioneAPI.
     * 
     * @return infoCreazione
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.InfoCreazioneRegistrazione getInfoCreazione() {
        return infoCreazione;
    }


    /**
     * Sets the infoCreazione value for this RegistrazioneAPI.
     * 
     * @param infoCreazione
     */
    public void setInfoCreazione(it.csi.risca.riscabesrv.util.actaofficialbook.InfoCreazioneRegistrazione infoCreazione) {
        this.infoCreazione = infoCreazione;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof RegistrazioneAPI)) return false;
        RegistrazioneAPI other = (RegistrazioneAPI) obj;

        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.tipoRegistrazione==null && other.getTipoRegistrazione()==null) || 
             (this.tipoRegistrazione!=null &&
              this.tipoRegistrazione.equals(other.getTipoRegistrazione()))) &&
            ((this.infoCreazione==null && other.getInfoCreazione()==null) || 
             (this.infoCreazione!=null &&
              this.infoCreazione.equals(other.getInfoCreazione())));
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
        if (getTipoRegistrazione() != null) {
            _hashCode += getTipoRegistrazione().hashCode();
        }
        if (getInfoCreazione() != null) {
            _hashCode += getInfoCreazione().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RegistrazioneAPI.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "RegistrazioneAPI"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoRegistrazione");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tipoRegistrazione"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("infoCreazione");
        elemField.setXmlName(new javax.xml.namespace.QName("", "infoCreazione"));
        elemField.setXmlType(new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "InfoCreazioneRegistrazione"));
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

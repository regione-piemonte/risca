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
 * ProtocollazioneDocumentoEsistente.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actaofficialbook;

public class ProtocollazioneDocumentoEsistente  extends it.csi.risca.riscabesrv.util.actaofficialbook.RegistrazioneRequest  implements java.io.Serializable {
    private it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType classificazioneId;

    private it.csi.risca.riscabesrv.util.actaofficialbook.RegistrazioneAPI registrazioneAPI;

    public ProtocollazioneDocumentoEsistente() {
    }

    public ProtocollazioneDocumentoEsistente(
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType registroId,
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType aooProtocollanteId,
           boolean senzaCreazioneSoggettiEsterni,
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType classificazioneId,
           it.csi.risca.riscabesrv.util.actaofficialbook.RegistrazioneAPI registrazioneAPI) {
        super(
            registroId,
            aooProtocollanteId,
            senzaCreazioneSoggettiEsterni);
        this.classificazioneId = classificazioneId;
        this.registrazioneAPI = registrazioneAPI;
    }


    /**
     * Gets the classificazioneId value for this ProtocollazioneDocumentoEsistente.
     * 
     * @return classificazioneId
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType getClassificazioneId() {
        return classificazioneId;
    }


    /**
     * Sets the classificazioneId value for this ProtocollazioneDocumentoEsistente.
     * 
     * @param classificazioneId
     */
    public void setClassificazioneId(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType classificazioneId) {
        this.classificazioneId = classificazioneId;
    }


    /**
     * Gets the registrazioneAPI value for this ProtocollazioneDocumentoEsistente.
     * 
     * @return registrazioneAPI
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.RegistrazioneAPI getRegistrazioneAPI() {
        return registrazioneAPI;
    }


    /**
     * Sets the registrazioneAPI value for this ProtocollazioneDocumentoEsistente.
     * 
     * @param registrazioneAPI
     */
    public void setRegistrazioneAPI(it.csi.risca.riscabesrv.util.actaofficialbook.RegistrazioneAPI registrazioneAPI) {
        this.registrazioneAPI = registrazioneAPI;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof ProtocollazioneDocumentoEsistente)) return false;
        ProtocollazioneDocumentoEsistente other = (ProtocollazioneDocumentoEsistente) obj;

        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.classificazioneId==null && other.getClassificazioneId()==null) || 
             (this.classificazioneId!=null &&
              this.classificazioneId.equals(other.getClassificazioneId()))) &&
            ((this.registrazioneAPI==null && other.getRegistrazioneAPI()==null) || 
             (this.registrazioneAPI!=null &&
              this.registrazioneAPI.equals(other.getRegistrazioneAPI())));
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
        if (getClassificazioneId() != null) {
            _hashCode += getClassificazioneId().hashCode();
        }
        if (getRegistrazioneAPI() != null) {
            _hashCode += getRegistrazioneAPI().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ProtocollazioneDocumentoEsistente.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "ProtocollazioneDocumentoEsistente"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("classificazioneId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "classificazioneId"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("registrazioneAPI");
        elemField.setXmlName(new javax.xml.namespace.QName("", "registrazioneAPI"));
        elemField.setXmlType(new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "RegistrazioneAPI"));
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

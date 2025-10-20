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
 * RegistrazioneRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actaofficialbook;

public abstract class RegistrazioneRequest  implements java.io.Serializable {
    private it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType registroId;

    private it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType aooProtocollanteId;

    private boolean senzaCreazioneSoggettiEsterni;

    public RegistrazioneRequest() {
    }

    public RegistrazioneRequest(
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType registroId,
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType aooProtocollanteId,
           boolean senzaCreazioneSoggettiEsterni) {
           this.registroId = registroId;
           this.aooProtocollanteId = aooProtocollanteId;
           this.senzaCreazioneSoggettiEsterni = senzaCreazioneSoggettiEsterni;
    }


    /**
     * Gets the registroId value for this RegistrazioneRequest.
     * 
     * @return registroId
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType getRegistroId() {
        return registroId;
    }


    /**
     * Sets the registroId value for this RegistrazioneRequest.
     * 
     * @param registroId
     */
    public void setRegistroId(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType registroId) {
        this.registroId = registroId;
    }


    /**
     * Gets the aooProtocollanteId value for this RegistrazioneRequest.
     * 
     * @return aooProtocollanteId
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType getAooProtocollanteId() {
        return aooProtocollanteId;
    }


    /**
     * Sets the aooProtocollanteId value for this RegistrazioneRequest.
     * 
     * @param aooProtocollanteId
     */
    public void setAooProtocollanteId(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType aooProtocollanteId) {
        this.aooProtocollanteId = aooProtocollanteId;
    }


    /**
     * Gets the senzaCreazioneSoggettiEsterni value for this RegistrazioneRequest.
     * 
     * @return senzaCreazioneSoggettiEsterni
     */
    public boolean isSenzaCreazioneSoggettiEsterni() {
        return senzaCreazioneSoggettiEsterni;
    }


    /**
     * Sets the senzaCreazioneSoggettiEsterni value for this RegistrazioneRequest.
     * 
     * @param senzaCreazioneSoggettiEsterni
     */
    public void setSenzaCreazioneSoggettiEsterni(boolean senzaCreazioneSoggettiEsterni) {
        this.senzaCreazioneSoggettiEsterni = senzaCreazioneSoggettiEsterni;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof RegistrazioneRequest)) return false;
        RegistrazioneRequest other = (RegistrazioneRequest) obj;

        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.registroId==null && other.getRegistroId()==null) || 
             (this.registroId!=null &&
              this.registroId.equals(other.getRegistroId()))) &&
            ((this.aooProtocollanteId==null && other.getAooProtocollanteId()==null) || 
             (this.aooProtocollanteId!=null &&
              this.aooProtocollanteId.equals(other.getAooProtocollanteId()))) &&
            this.senzaCreazioneSoggettiEsterni == other.isSenzaCreazioneSoggettiEsterni();
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
        if (getRegistroId() != null) {
            _hashCode += getRegistroId().hashCode();
        }
        if (getAooProtocollanteId() != null) {
            _hashCode += getAooProtocollanteId().hashCode();
        }
        _hashCode += (isSenzaCreazioneSoggettiEsterni() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RegistrazioneRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "RegistrazioneRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("registroId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "registroId"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("aooProtocollanteId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "aooProtocollanteId"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("senzaCreazioneSoggettiEsterni");
        elemField.setXmlName(new javax.xml.namespace.QName("", "senzaCreazioneSoggettiEsterni"));
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

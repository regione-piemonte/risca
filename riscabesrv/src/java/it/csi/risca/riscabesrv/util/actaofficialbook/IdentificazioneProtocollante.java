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
 * IdentificazioneProtocollante.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actaofficialbook;

public class IdentificazioneProtocollante  implements java.io.Serializable {
    private it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType strutturaId;

    private it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType nodoId;

    public IdentificazioneProtocollante() {
    }

    public IdentificazioneProtocollante(
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType strutturaId,
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType nodoId) {
           this.strutturaId = strutturaId;
           this.nodoId = nodoId;
    }


    /**
     * Gets the strutturaId value for this IdentificazioneProtocollante.
     * 
     * @return strutturaId
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType getStrutturaId() {
        return strutturaId;
    }


    /**
     * Sets the strutturaId value for this IdentificazioneProtocollante.
     * 
     * @param strutturaId
     */
    public void setStrutturaId(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType strutturaId) {
        this.strutturaId = strutturaId;
    }


    /**
     * Gets the nodoId value for this IdentificazioneProtocollante.
     * 
     * @return nodoId
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType getNodoId() {
        return nodoId;
    }


    /**
     * Sets the nodoId value for this IdentificazioneProtocollante.
     * 
     * @param nodoId
     */
    public void setNodoId(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType nodoId) {
        this.nodoId = nodoId;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof IdentificazioneProtocollante)) return false;
        IdentificazioneProtocollante other = (IdentificazioneProtocollante) obj;

        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.strutturaId==null && other.getStrutturaId()==null) || 
             (this.strutturaId!=null &&
              this.strutturaId.equals(other.getStrutturaId()))) &&
            ((this.nodoId==null && other.getNodoId()==null) || 
             (this.nodoId!=null &&
              this.nodoId.equals(other.getNodoId())));
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
        if (getStrutturaId() != null) {
            _hashCode += getStrutturaId().hashCode();
        }
        if (getNodoId() != null) {
            _hashCode += getNodoId().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(IdentificazioneProtocollante.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "IdentificazioneProtocollante"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("strutturaId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "strutturaId"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nodoId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nodoId"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"));
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

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
 * IdentificazioneProtocollanteEstesa.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actaofficialbook;

public class IdentificazioneProtocollanteEstesa  extends it.csi.risca.riscabesrv.util.actaofficialbook.IdentificazioneProtocollante  implements java.io.Serializable {
    private it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idAoo;

    public IdentificazioneProtocollanteEstesa() {
    }

    public IdentificazioneProtocollanteEstesa(
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType strutturaId,
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType nodoId,
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idAoo) {
        super(
            strutturaId,
            nodoId);
        this.idAoo = idAoo;
    }


    /**
     * Gets the idAoo value for this IdentificazioneProtocollanteEstesa.
     * 
     * @return idAoo
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType getIdAoo() {
        return idAoo;
    }


    /**
     * Sets the idAoo value for this IdentificazioneProtocollanteEstesa.
     * 
     * @param idAoo
     */
    public void setIdAoo(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idAoo) {
        this.idAoo = idAoo;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof IdentificazioneProtocollanteEstesa)) return false;
        IdentificazioneProtocollanteEstesa other = (IdentificazioneProtocollanteEstesa) obj;

        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.idAoo==null && other.getIdAoo()==null) || 
             (this.idAoo!=null &&
              this.idAoo.equals(other.getIdAoo())));
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
        if (getIdAoo() != null) {
            _hashCode += getIdAoo().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(IdentificazioneProtocollanteEstesa.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "IdentificazioneProtocollanteEstesa"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idAoo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idAoo"));
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

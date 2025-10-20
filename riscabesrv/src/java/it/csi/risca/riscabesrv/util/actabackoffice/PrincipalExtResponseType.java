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
 * PrincipalExtResponseType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actabackoffice;

public class PrincipalExtResponseType  implements java.io.Serializable {
    private it.csi.risca.riscabesrv.util.actabackoffice.PrincipalIdType principalId;

    private it.csi.risca.riscabesrv.util.actabackoffice.CollocazioneUtente utente;

    private it.csi.risca.riscabesrv.util.actabackoffice.DecodificaType[] profili;

    public PrincipalExtResponseType() {
    }

    public PrincipalExtResponseType(
           it.csi.risca.riscabesrv.util.actabackoffice.PrincipalIdType principalId,
           it.csi.risca.riscabesrv.util.actabackoffice.CollocazioneUtente utente,
           it.csi.risca.riscabesrv.util.actabackoffice.DecodificaType[] profili) {
           this.principalId = principalId;
           this.utente = utente;
           this.profili = profili;
    }


    /**
     * Gets the principalId value for this PrincipalExtResponseType.
     * 
     * @return principalId
     */
    public it.csi.risca.riscabesrv.util.actabackoffice.PrincipalIdType getPrincipalId() {
        return principalId;
    }


    /**
     * Sets the principalId value for this PrincipalExtResponseType.
     * 
     * @param principalId
     */
    public void setPrincipalId(it.csi.risca.riscabesrv.util.actabackoffice.PrincipalIdType principalId) {
        this.principalId = principalId;
    }


    /**
     * Gets the utente value for this PrincipalExtResponseType.
     * 
     * @return utente
     */
    public it.csi.risca.riscabesrv.util.actabackoffice.CollocazioneUtente getUtente() {
        return utente;
    }


    /**
     * Sets the utente value for this PrincipalExtResponseType.
     * 
     * @param utente
     */
    public void setUtente(it.csi.risca.riscabesrv.util.actabackoffice.CollocazioneUtente utente) {
        this.utente = utente;
    }


    /**
     * Gets the profili value for this PrincipalExtResponseType.
     * 
     * @return profili
     */
    public it.csi.risca.riscabesrv.util.actabackoffice.DecodificaType[] getProfili() {
        return profili;
    }


    /**
     * Sets the profili value for this PrincipalExtResponseType.
     * 
     * @param profili
     */
    public void setProfili(it.csi.risca.riscabesrv.util.actabackoffice.DecodificaType[] profili) {
        this.profili = profili;
    }

    public it.csi.risca.riscabesrv.util.actabackoffice.DecodificaType getProfili(int i) {
        return this.profili[i];
    }

    public void setProfili(int i, it.csi.risca.riscabesrv.util.actabackoffice.DecodificaType _value) {
        this.profili[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof PrincipalExtResponseType)) return false;
        PrincipalExtResponseType other = (PrincipalExtResponseType) obj;

        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.principalId==null && other.getPrincipalId()==null) || 
             (this.principalId!=null &&
              this.principalId.equals(other.getPrincipalId()))) &&
            ((this.utente==null && other.getUtente()==null) || 
             (this.utente!=null &&
              this.utente.equals(other.getUtente()))) &&
            ((this.profili==null && other.getProfili()==null) || 
             (this.profili!=null &&
              java.util.Arrays.equals(this.profili, other.getProfili())));
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
        if (getPrincipalId() != null) {
            _hashCode += getPrincipalId().hashCode();
        }
        if (getUtente() != null) {
            _hashCode += getUtente().hashCode();
        }
        if (getProfili() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getProfili());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getProfili(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(PrincipalExtResponseType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("backoffice.acaris.acta.doqui.it", "PrincipalExtResponseType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("principalId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "principalId"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "PrincipalIdType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("utente");
        elemField.setXmlName(new javax.xml.namespace.QName("", "utente"));
        elemField.setXmlType(new javax.xml.namespace.QName("backoffice.acaris.acta.doqui.it", "CollocazioneUtente"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("profili");
        elemField.setXmlName(new javax.xml.namespace.QName("", "profili"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "DecodificaType"));
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
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

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
 * MoveDocumentPropertiesType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actaobject;

public class MoveDocumentPropertiesType  extends it.csi.risca.riscabesrv.util.actaobject.PropertiesType  implements java.io.Serializable {
    private boolean offlineMoveRequest;

    private it.csi.risca.riscabesrv.util.actaobject.IdSmistamentoType idSmistamentoType;

    public MoveDocumentPropertiesType() {
    }

    public MoveDocumentPropertiesType(
           boolean offlineMoveRequest,
           it.csi.risca.riscabesrv.util.actaobject.IdSmistamentoType idSmistamentoType) {
        this.offlineMoveRequest = offlineMoveRequest;
        this.idSmistamentoType = idSmistamentoType;
    }


    /**
     * Gets the offlineMoveRequest value for this MoveDocumentPropertiesType.
     * 
     * @return offlineMoveRequest
     */
    public boolean isOfflineMoveRequest() {
        return offlineMoveRequest;
    }


    /**
     * Sets the offlineMoveRequest value for this MoveDocumentPropertiesType.
     * 
     * @param offlineMoveRequest
     */
    public void setOfflineMoveRequest(boolean offlineMoveRequest) {
        this.offlineMoveRequest = offlineMoveRequest;
    }


    /**
     * Gets the idSmistamentoType value for this MoveDocumentPropertiesType.
     * 
     * @return idSmistamentoType
     */
    public it.csi.risca.riscabesrv.util.actaobject.IdSmistamentoType getIdSmistamentoType() {
        return idSmistamentoType;
    }


    /**
     * Sets the idSmistamentoType value for this MoveDocumentPropertiesType.
     * 
     * @param idSmistamentoType
     */
    public void setIdSmistamentoType(it.csi.risca.riscabesrv.util.actaobject.IdSmistamentoType idSmistamentoType) {
        this.idSmistamentoType = idSmistamentoType;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof MoveDocumentPropertiesType)) return false;
        MoveDocumentPropertiesType other = (MoveDocumentPropertiesType) obj;

        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            this.offlineMoveRequest == other.isOfflineMoveRequest() &&
            ((this.idSmistamentoType==null && other.getIdSmistamentoType()==null) || 
             (this.idSmistamentoType!=null &&
              this.idSmistamentoType.equals(other.getIdSmistamentoType())));
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
        _hashCode += (isOfflineMoveRequest() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getIdSmistamentoType() != null) {
            _hashCode += getIdSmistamentoType().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(MoveDocumentPropertiesType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "MoveDocumentPropertiesType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("offlineMoveRequest");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "offlineMoveRequest"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idSmistamentoType");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "idSmistamentoType"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "IdSmistamentoType"));
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

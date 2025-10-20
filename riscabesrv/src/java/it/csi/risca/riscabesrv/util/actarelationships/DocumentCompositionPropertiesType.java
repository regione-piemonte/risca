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
 * DocumentCompositionPropertiesType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actarelationships;

public class DocumentCompositionPropertiesType  extends it.csi.risca.riscabesrv.util.actarelationships.RelationshipPropertiesType  implements java.io.Serializable {
    private boolean bypassControlli;

    public DocumentCompositionPropertiesType() {
    }

    public DocumentCompositionPropertiesType(
           it.csi.risca.riscabesrv.util.actarelationships.ObjectIdType objectId,
           it.csi.risca.riscabesrv.util.actarelationships.ChangeTokenType changeToken,
           java.lang.String relationType,
           it.csi.risca.riscabesrv.util.actarelationships.ObjectIdType sourceId,
           java.lang.String sourceType,
           it.csi.risca.riscabesrv.util.actarelationships.ObjectIdType targetId,
           java.lang.String targetType,
           boolean bypassControlli) {
        super(
            objectId,
            changeToken,
            relationType,
            sourceId,
            sourceType,
            targetId,
            targetType);
        this.bypassControlli = bypassControlli;
    }


    /**
     * Gets the bypassControlli value for this DocumentCompositionPropertiesType.
     * 
     * @return bypassControlli
     */
    public boolean isBypassControlli() {
        return bypassControlli;
    }


    /**
     * Sets the bypassControlli value for this DocumentCompositionPropertiesType.
     * 
     * @param bypassControlli
     */
    public void setBypassControlli(boolean bypassControlli) {
        this.bypassControlli = bypassControlli;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof DocumentCompositionPropertiesType)) return false;
        DocumentCompositionPropertiesType other = (DocumentCompositionPropertiesType) obj;

        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            this.bypassControlli == other.isBypassControlli();
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
        _hashCode += (isBypassControlli() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DocumentCompositionPropertiesType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "DocumentCompositionPropertiesType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bypassControlli");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "bypassControlli"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "boolean"));
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

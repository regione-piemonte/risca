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
 * PropertyFilterConfigurationInfoType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actabackoffice;

public class PropertyFilterConfigurationInfoType  implements java.io.Serializable {
    private java.lang.String operation;

    private boolean isAllAllowed;

    private boolean isNoneAllowed;

    private boolean isListAllowed;

    public PropertyFilterConfigurationInfoType() {
    }

    public PropertyFilterConfigurationInfoType(
           java.lang.String operation,
           boolean isAllAllowed,
           boolean isNoneAllowed,
           boolean isListAllowed) {
           this.operation = operation;
           this.isAllAllowed = isAllAllowed;
           this.isNoneAllowed = isNoneAllowed;
           this.isListAllowed = isListAllowed;
    }


    /**
     * Gets the operation value for this PropertyFilterConfigurationInfoType.
     * 
     * @return operation
     */
    public java.lang.String getOperation() {
        return operation;
    }


    /**
     * Sets the operation value for this PropertyFilterConfigurationInfoType.
     * 
     * @param operation
     */
    public void setOperation(java.lang.String operation) {
        this.operation = operation;
    }


    /**
     * Gets the isAllAllowed value for this PropertyFilterConfigurationInfoType.
     * 
     * @return isAllAllowed
     */
    public boolean isIsAllAllowed() {
        return isAllAllowed;
    }


    /**
     * Sets the isAllAllowed value for this PropertyFilterConfigurationInfoType.
     * 
     * @param isAllAllowed
     */
    public void setIsAllAllowed(boolean isAllAllowed) {
        this.isAllAllowed = isAllAllowed;
    }


    /**
     * Gets the isNoneAllowed value for this PropertyFilterConfigurationInfoType.
     * 
     * @return isNoneAllowed
     */
    public boolean isIsNoneAllowed() {
        return isNoneAllowed;
    }


    /**
     * Sets the isNoneAllowed value for this PropertyFilterConfigurationInfoType.
     * 
     * @param isNoneAllowed
     */
    public void setIsNoneAllowed(boolean isNoneAllowed) {
        this.isNoneAllowed = isNoneAllowed;
    }


    /**
     * Gets the isListAllowed value for this PropertyFilterConfigurationInfoType.
     * 
     * @return isListAllowed
     */
    public boolean isIsListAllowed() {
        return isListAllowed;
    }


    /**
     * Sets the isListAllowed value for this PropertyFilterConfigurationInfoType.
     * 
     * @param isListAllowed
     */
    public void setIsListAllowed(boolean isListAllowed) {
        this.isListAllowed = isListAllowed;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof PropertyFilterConfigurationInfoType)) return false;
        PropertyFilterConfigurationInfoType other = (PropertyFilterConfigurationInfoType) obj;

        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.operation==null && other.getOperation()==null) || 
             (this.operation!=null &&
              this.operation.equals(other.getOperation()))) &&
            this.isAllAllowed == other.isIsAllAllowed() &&
            this.isNoneAllowed == other.isIsNoneAllowed() &&
            this.isListAllowed == other.isIsListAllowed();
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
        if (getOperation() != null) {
            _hashCode += getOperation().hashCode();
        }
        _hashCode += (isIsAllAllowed() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isIsNoneAllowed() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isIsListAllowed() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(PropertyFilterConfigurationInfoType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "PropertyFilterConfigurationInfoType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("operation");
        elemField.setXmlName(new javax.xml.namespace.QName("", "operation"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("isAllAllowed");
        elemField.setXmlName(new javax.xml.namespace.QName("", "isAllAllowed"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("isNoneAllowed");
        elemField.setXmlName(new javax.xml.namespace.QName("", "isNoneAllowed"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("isListAllowed");
        elemField.setXmlName(new javax.xml.namespace.QName("", "isListAllowed"));
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

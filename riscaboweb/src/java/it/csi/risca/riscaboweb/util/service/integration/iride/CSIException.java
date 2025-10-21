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
 * CSIException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscaboweb.util.service.integration.iride;

public class CSIException  extends org.apache.axis.AxisFault  implements java.io.Serializable {
    private java.lang.String nestedExcMsg;

    private java.lang.String nestedExcClassName;

    private java.lang.String stackTraceMessage;

    public CSIException() {
    }

    public CSIException(
           java.lang.String nestedExcMsg,
           java.lang.String nestedExcClassName,
           java.lang.String stackTraceMessage) {
        this.nestedExcMsg = nestedExcMsg;
        this.nestedExcClassName = nestedExcClassName;
        this.stackTraceMessage = stackTraceMessage;
    }


    /**
     * Gets the nestedExcMsg value for this CSIException.
     * 
     * @return nestedExcMsg
     */
    public java.lang.String getNestedExcMsg() {
        return nestedExcMsg;
    }


    /**
     * Sets the nestedExcMsg value for this CSIException.
     * 
     * @param nestedExcMsg
     */
    public void setNestedExcMsg(java.lang.String nestedExcMsg) {
        this.nestedExcMsg = nestedExcMsg;
    }


    /**
     * Gets the nestedExcClassName value for this CSIException.
     * 
     * @return nestedExcClassName
     */
    public java.lang.String getNestedExcClassName() {
        return nestedExcClassName;
    }


    /**
     * Sets the nestedExcClassName value for this CSIException.
     * 
     * @param nestedExcClassName
     */
    public void setNestedExcClassName(java.lang.String nestedExcClassName) {
        this.nestedExcClassName = nestedExcClassName;
    }


    /**
     * Gets the stackTraceMessage value for this CSIException.
     * 
     * @return stackTraceMessage
     */
    public java.lang.String getStackTraceMessage() {
        return stackTraceMessage;
    }


    /**
     * Sets the stackTraceMessage value for this CSIException.
     * 
     * @param stackTraceMessage
     */
    public void setStackTraceMessage(java.lang.String stackTraceMessage) {
        this.stackTraceMessage = stackTraceMessage;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof CSIException)) return false;
        CSIException other = (CSIException) obj;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.nestedExcMsg==null && other.getNestedExcMsg()==null) || 
             (this.nestedExcMsg!=null &&
              this.nestedExcMsg.equals(other.getNestedExcMsg()))) &&
            ((this.nestedExcClassName==null && other.getNestedExcClassName()==null) || 
             (this.nestedExcClassName!=null &&
              this.nestedExcClassName.equals(other.getNestedExcClassName()))) &&
            ((this.stackTraceMessage==null && other.getStackTraceMessage()==null) || 
             (this.stackTraceMessage!=null &&
              this.stackTraceMessage.equals(other.getStackTraceMessage())));
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
        if (getNestedExcMsg() != null) {
            _hashCode += getNestedExcMsg().hashCode();
        }
        if (getNestedExcClassName() != null) {
            _hashCode += getNestedExcClassName().hashCode();
        }
        if (getStackTraceMessage() != null) {
            _hashCode += getStackTraceMessage().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CSIException.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:PolicyEnforcerBase", "CSIException"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nestedExcMsg");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nestedExcMsg"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nestedExcClassName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nestedExcClassName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("stackTraceMessage");
        elemField.setXmlName(new javax.xml.namespace.QName("", "stackTraceMessage"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
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


    /**
     * Writes the exception data to the faultDetails
     */
    public void writeDetails(javax.xml.namespace.QName qname, org.apache.axis.encoding.SerializationContext context) throws java.io.IOException {
        context.serialize(qname, null, this);
    }
}

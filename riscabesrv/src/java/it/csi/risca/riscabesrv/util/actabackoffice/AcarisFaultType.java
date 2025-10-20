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
 * AcarisFaultType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actabackoffice;

public class AcarisFaultType  extends org.apache.axis.AxisFault  implements java.io.Serializable {
    private java.lang.String exceptionType;

    private java.lang.String errorCode;

    private java.lang.String technicalInfo;

    private it.csi.risca.riscabesrv.util.actabackoffice.ObjectIdType objectId;

    private java.lang.String className;

    private java.lang.String propertyName;

    public AcarisFaultType() {
    }

    public AcarisFaultType(
           java.lang.String exceptionType,
           java.lang.String errorCode,
           java.lang.String technicalInfo,
           it.csi.risca.riscabesrv.util.actabackoffice.ObjectIdType objectId,
           java.lang.String className,
           java.lang.String propertyName) {
        this.exceptionType = exceptionType;
        this.errorCode = errorCode;
        this.technicalInfo = technicalInfo;
        this.objectId = objectId;
        this.className = className;
        this.propertyName = propertyName;
    }


    /**
     * Gets the exceptionType value for this AcarisFaultType.
     * 
     * @return exceptionType
     */
    public java.lang.String getExceptionType() {
        return exceptionType;
    }


    /**
     * Sets the exceptionType value for this AcarisFaultType.
     * 
     * @param exceptionType
     */
    public void setExceptionType(java.lang.String exceptionType) {
        this.exceptionType = exceptionType;
    }


    /**
     * Gets the errorCode value for this AcarisFaultType.
     * 
     * @return errorCode
     */
    public java.lang.String getErrorCode() {
        return errorCode;
    }


    /**
     * Sets the errorCode value for this AcarisFaultType.
     * 
     * @param errorCode
     */
    public void setErrorCode(java.lang.String errorCode) {
        this.errorCode = errorCode;
    }


    /**
     * Gets the technicalInfo value for this AcarisFaultType.
     * 
     * @return technicalInfo
     */
    public java.lang.String getTechnicalInfo() {
        return technicalInfo;
    }


    /**
     * Sets the technicalInfo value for this AcarisFaultType.
     * 
     * @param technicalInfo
     */
    public void setTechnicalInfo(java.lang.String technicalInfo) {
        this.technicalInfo = technicalInfo;
    }


    /**
     * Gets the objectId value for this AcarisFaultType.
     * 
     * @return objectId
     */
    public it.csi.risca.riscabesrv.util.actabackoffice.ObjectIdType getObjectId() {
        return objectId;
    }


    /**
     * Sets the objectId value for this AcarisFaultType.
     * 
     * @param objectId
     */
    public void setObjectId(it.csi.risca.riscabesrv.util.actabackoffice.ObjectIdType objectId) {
        this.objectId = objectId;
    }


    /**
     * Gets the className value for this AcarisFaultType.
     * 
     * @return className
     */
    public java.lang.String getClassName() {
        return className;
    }


    /**
     * Sets the className value for this AcarisFaultType.
     * 
     * @param className
     */
    public void setClassName(java.lang.String className) {
        this.className = className;
    }


    /**
     * Gets the propertyName value for this AcarisFaultType.
     * 
     * @return propertyName
     */
    public java.lang.String getPropertyName() {
        return propertyName;
    }


    /**
     * Sets the propertyName value for this AcarisFaultType.
     * 
     * @param propertyName
     */
    public void setPropertyName(java.lang.String propertyName) {
        this.propertyName = propertyName;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof AcarisFaultType)) return false;
        AcarisFaultType other = (AcarisFaultType) obj;

        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.exceptionType==null && other.getExceptionType()==null) || 
             (this.exceptionType!=null &&
              this.exceptionType.equals(other.getExceptionType()))) &&
            ((this.errorCode==null && other.getErrorCode()==null) || 
             (this.errorCode!=null &&
              this.errorCode.equals(other.getErrorCode()))) &&
            ((this.technicalInfo==null && other.getTechnicalInfo()==null) || 
             (this.technicalInfo!=null &&
              this.technicalInfo.equals(other.getTechnicalInfo()))) &&
            ((this.objectId==null && other.getObjectId()==null) || 
             (this.objectId!=null &&
              this.objectId.equals(other.getObjectId()))) &&
            ((this.className==null && other.getClassName()==null) || 
             (this.className!=null &&
              this.className.equals(other.getClassName()))) &&
            ((this.propertyName==null && other.getPropertyName()==null) || 
             (this.propertyName!=null &&
              this.propertyName.equals(other.getPropertyName())));
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
        if (getExceptionType() != null) {
            _hashCode += getExceptionType().hashCode();
        }
        if (getErrorCode() != null) {
            _hashCode += getErrorCode().hashCode();
        }
        if (getTechnicalInfo() != null) {
            _hashCode += getTechnicalInfo().hashCode();
        }
        if (getObjectId() != null) {
            _hashCode += getObjectId().hashCode();
        }
        if (getClassName() != null) {
            _hashCode += getClassName().hashCode();
        }
        if (getPropertyName() != null) {
            _hashCode += getPropertyName().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(AcarisFaultType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "acarisFaultType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("exceptionType");
        elemField.setXmlName(new javax.xml.namespace.QName("", "exceptionType"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("errorCode");
        elemField.setXmlName(new javax.xml.namespace.QName("", "errorCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("technicalInfo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "technicalInfo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("objectId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "objectId"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("className");
        elemField.setXmlName(new javax.xml.namespace.QName("", "className"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("propertyName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "propertyName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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


    /**
     * Writes the exception data to the faultDetails
     */
    public void writeDetails(javax.xml.namespace.QName qname, org.apache.axis.encoding.SerializationContext context) throws java.io.IOException {
        context.serialize(qname, null, this);
    }
}

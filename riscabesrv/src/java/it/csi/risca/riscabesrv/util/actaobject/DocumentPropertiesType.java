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
 * DocumentPropertiesType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actaobject;

public abstract class DocumentPropertiesType  extends it.csi.risca.riscabesrv.util.actaobject.ArchivePropertiesType  implements java.io.Serializable {
    private java.lang.String objectTypeId;

    private int contentStreamLength;

    private java.lang.String contentStreamMimeType;

    private java.lang.String contentStreamFilename;

    public DocumentPropertiesType() {
    }

    public DocumentPropertiesType(
           it.csi.risca.riscabesrv.util.actaobject.ObjectIdType objectId,
           it.csi.risca.riscabesrv.util.actaobject.ChangeTokenType changeToken,
           java.lang.String objectTypeId,
           int contentStreamLength,
           java.lang.String contentStreamMimeType,
           java.lang.String contentStreamFilename) {
        super(
            objectId,
            changeToken);
        this.objectTypeId = objectTypeId;
        this.contentStreamLength = contentStreamLength;
        this.contentStreamMimeType = contentStreamMimeType;
        this.contentStreamFilename = contentStreamFilename;
    }


    /**
     * Gets the objectTypeId value for this DocumentPropertiesType.
     * 
     * @return objectTypeId
     */
    public java.lang.String getObjectTypeId() {
        return objectTypeId;
    }


    /**
     * Sets the objectTypeId value for this DocumentPropertiesType.
     * 
     * @param objectTypeId
     */
    public void setObjectTypeId(java.lang.String objectTypeId) {
        this.objectTypeId = objectTypeId;
    }


    /**
     * Gets the contentStreamLength value for this DocumentPropertiesType.
     * 
     * @return contentStreamLength
     */
    public int getContentStreamLength() {
        return contentStreamLength;
    }


    /**
     * Sets the contentStreamLength value for this DocumentPropertiesType.
     * 
     * @param contentStreamLength
     */
    public void setContentStreamLength(int contentStreamLength) {
        this.contentStreamLength = contentStreamLength;
    }


    /**
     * Gets the contentStreamMimeType value for this DocumentPropertiesType.
     * 
     * @return contentStreamMimeType
     */
    public java.lang.String getContentStreamMimeType() {
        return contentStreamMimeType;
    }


    /**
     * Sets the contentStreamMimeType value for this DocumentPropertiesType.
     * 
     * @param contentStreamMimeType
     */
    public void setContentStreamMimeType(java.lang.String contentStreamMimeType) {
        this.contentStreamMimeType = contentStreamMimeType;
    }


    /**
     * Gets the contentStreamFilename value for this DocumentPropertiesType.
     * 
     * @return contentStreamFilename
     */
    public java.lang.String getContentStreamFilename() {
        return contentStreamFilename;
    }


    /**
     * Sets the contentStreamFilename value for this DocumentPropertiesType.
     * 
     * @param contentStreamFilename
     */
    public void setContentStreamFilename(java.lang.String contentStreamFilename) {
        this.contentStreamFilename = contentStreamFilename;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof DocumentPropertiesType)) return false;
        DocumentPropertiesType other = (DocumentPropertiesType) obj;

        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.objectTypeId==null && other.getObjectTypeId()==null) || 
             (this.objectTypeId!=null &&
              this.objectTypeId.equals(other.getObjectTypeId()))) &&
            this.contentStreamLength == other.getContentStreamLength() &&
            ((this.contentStreamMimeType==null && other.getContentStreamMimeType()==null) || 
             (this.contentStreamMimeType!=null &&
              this.contentStreamMimeType.equals(other.getContentStreamMimeType()))) &&
            ((this.contentStreamFilename==null && other.getContentStreamFilename()==null) || 
             (this.contentStreamFilename!=null &&
              this.contentStreamFilename.equals(other.getContentStreamFilename())));
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
        if (getObjectTypeId() != null) {
            _hashCode += getObjectTypeId().hashCode();
        }
        _hashCode += getContentStreamLength();
        if (getContentStreamMimeType() != null) {
            _hashCode += getContentStreamMimeType().hashCode();
        }
        if (getContentStreamFilename() != null) {
            _hashCode += getContentStreamFilename().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DocumentPropertiesType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "DocumentPropertiesType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("objectTypeId");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "objectTypeId"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("contentStreamLength");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "contentStreamLength"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "integer"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("contentStreamMimeType");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "contentStreamMimeType"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("contentStreamFilename");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "contentStreamFilename"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"));
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

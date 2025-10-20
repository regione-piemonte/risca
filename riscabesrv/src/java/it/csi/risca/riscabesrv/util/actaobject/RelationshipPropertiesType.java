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
 * RelationshipPropertiesType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actaobject;

public class RelationshipPropertiesType  extends it.csi.risca.riscabesrv.util.actaobject.ArchivePropertiesType  implements java.io.Serializable {
    private java.lang.String relationType;

    private it.csi.risca.riscabesrv.util.actaobject.ObjectIdType sourceId;

    private java.lang.String sourceType;

    private it.csi.risca.riscabesrv.util.actaobject.ObjectIdType targetId;

    private java.lang.String targetType;

    public RelationshipPropertiesType() {
    }

    public RelationshipPropertiesType(
           it.csi.risca.riscabesrv.util.actaobject.ObjectIdType objectId,
           it.csi.risca.riscabesrv.util.actaobject.ChangeTokenType changeToken,
           java.lang.String relationType,
           it.csi.risca.riscabesrv.util.actaobject.ObjectIdType sourceId,
           java.lang.String sourceType,
           it.csi.risca.riscabesrv.util.actaobject.ObjectIdType targetId,
           java.lang.String targetType) {
        super(
            objectId,
            changeToken);
        this.relationType = relationType;
        this.sourceId = sourceId;
        this.sourceType = sourceType;
        this.targetId = targetId;
        this.targetType = targetType;
    }


    /**
     * Gets the relationType value for this RelationshipPropertiesType.
     * 
     * @return relationType
     */
    public java.lang.String getRelationType() {
        return relationType;
    }


    /**
     * Sets the relationType value for this RelationshipPropertiesType.
     * 
     * @param relationType
     */
    public void setRelationType(java.lang.String relationType) {
        this.relationType = relationType;
    }


    /**
     * Gets the sourceId value for this RelationshipPropertiesType.
     * 
     * @return sourceId
     */
    public it.csi.risca.riscabesrv.util.actaobject.ObjectIdType getSourceId() {
        return sourceId;
    }


    /**
     * Sets the sourceId value for this RelationshipPropertiesType.
     * 
     * @param sourceId
     */
    public void setSourceId(it.csi.risca.riscabesrv.util.actaobject.ObjectIdType sourceId) {
        this.sourceId = sourceId;
    }


    /**
     * Gets the sourceType value for this RelationshipPropertiesType.
     * 
     * @return sourceType
     */
    public java.lang.String getSourceType() {
        return sourceType;
    }


    /**
     * Sets the sourceType value for this RelationshipPropertiesType.
     * 
     * @param sourceType
     */
    public void setSourceType(java.lang.String sourceType) {
        this.sourceType = sourceType;
    }


    /**
     * Gets the targetId value for this RelationshipPropertiesType.
     * 
     * @return targetId
     */
    public it.csi.risca.riscabesrv.util.actaobject.ObjectIdType getTargetId() {
        return targetId;
    }


    /**
     * Sets the targetId value for this RelationshipPropertiesType.
     * 
     * @param targetId
     */
    public void setTargetId(it.csi.risca.riscabesrv.util.actaobject.ObjectIdType targetId) {
        this.targetId = targetId;
    }


    /**
     * Gets the targetType value for this RelationshipPropertiesType.
     * 
     * @return targetType
     */
    public java.lang.String getTargetType() {
        return targetType;
    }


    /**
     * Sets the targetType value for this RelationshipPropertiesType.
     * 
     * @param targetType
     */
    public void setTargetType(java.lang.String targetType) {
        this.targetType = targetType;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof RelationshipPropertiesType)) return false;
        RelationshipPropertiesType other = (RelationshipPropertiesType) obj;
  
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.relationType==null && other.getRelationType()==null) || 
             (this.relationType!=null &&
              this.relationType.equals(other.getRelationType()))) &&
            ((this.sourceId==null && other.getSourceId()==null) || 
             (this.sourceId!=null &&
              this.sourceId.equals(other.getSourceId()))) &&
            ((this.sourceType==null && other.getSourceType()==null) || 
             (this.sourceType!=null &&
              this.sourceType.equals(other.getSourceType()))) &&
            ((this.targetId==null && other.getTargetId()==null) || 
             (this.targetId!=null &&
              this.targetId.equals(other.getTargetId()))) &&
            ((this.targetType==null && other.getTargetType()==null) || 
             (this.targetType!=null &&
              this.targetType.equals(other.getTargetType())));
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
        if (getRelationType() != null) {
            _hashCode += getRelationType().hashCode();
        }
        if (getSourceId() != null) {
            _hashCode += getSourceId().hashCode();
        }
        if (getSourceType() != null) {
            _hashCode += getSourceType().hashCode();
        }
        if (getTargetId() != null) {
            _hashCode += getTargetId().hashCode();
        }
        if (getTargetType() != null) {
            _hashCode += getTargetType().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RelationshipPropertiesType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "RelationshipPropertiesType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("relationType");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "relationType"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sourceId");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "sourceId"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sourceType");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "sourceType"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("targetId");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "targetId"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("targetType");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "targetType"));
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

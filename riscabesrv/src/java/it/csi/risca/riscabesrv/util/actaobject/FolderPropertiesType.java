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
 * FolderPropertiesType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actaobject;

public abstract class FolderPropertiesType  extends it.csi.risca.riscabesrv.util.actaobject.ArchivePropertiesType  implements java.io.Serializable {
    private java.lang.String objectTypeId;

    private it.csi.risca.riscabesrv.util.actaobject.ObjectIdType parentId;

    private java.lang.String[] allowedChildObjectTypeIds;

    public FolderPropertiesType() {
    }

    public FolderPropertiesType(
           it.csi.risca.riscabesrv.util.actaobject.ObjectIdType objectId,
           it.csi.risca.riscabesrv.util.actaobject.ChangeTokenType changeToken,
           java.lang.String objectTypeId,
           it.csi.risca.riscabesrv.util.actaobject.ObjectIdType parentId,
           java.lang.String[] allowedChildObjectTypeIds) {
        super(
            objectId,
            changeToken);
        this.objectTypeId = objectTypeId;
        this.parentId = parentId;
        this.allowedChildObjectTypeIds = allowedChildObjectTypeIds;
    }


    /**
     * Gets the objectTypeId value for this FolderPropertiesType.
     * 
     * @return objectTypeId
     */
    public java.lang.String getObjectTypeId() {
        return objectTypeId;
    }


    /**
     * Sets the objectTypeId value for this FolderPropertiesType.
     * 
     * @param objectTypeId
     */
    public void setObjectTypeId(java.lang.String objectTypeId) {
        this.objectTypeId = objectTypeId;
    }


    /**
     * Gets the parentId value for this FolderPropertiesType.
     * 
     * @return parentId
     */
    public it.csi.risca.riscabesrv.util.actaobject.ObjectIdType getParentId() {
        return parentId;
    }


    /**
     * Sets the parentId value for this FolderPropertiesType.
     * 
     * @param parentId
     */
    public void setParentId(it.csi.risca.riscabesrv.util.actaobject.ObjectIdType parentId) {
        this.parentId = parentId;
    }


    /**
     * Gets the allowedChildObjectTypeIds value for this FolderPropertiesType.
     * 
     * @return allowedChildObjectTypeIds
     */
    public java.lang.String[] getAllowedChildObjectTypeIds() {
        return allowedChildObjectTypeIds;
    }


    /**
     * Sets the allowedChildObjectTypeIds value for this FolderPropertiesType.
     * 
     * @param allowedChildObjectTypeIds
     */
    public void setAllowedChildObjectTypeIds(java.lang.String[] allowedChildObjectTypeIds) {
        this.allowedChildObjectTypeIds = allowedChildObjectTypeIds;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof FolderPropertiesType)) return false;
        FolderPropertiesType other = (FolderPropertiesType) obj;

        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.objectTypeId==null && other.getObjectTypeId()==null) || 
             (this.objectTypeId!=null &&
              this.objectTypeId.equals(other.getObjectTypeId()))) &&
            ((this.parentId==null && other.getParentId()==null) || 
             (this.parentId!=null &&
              this.parentId.equals(other.getParentId()))) &&
            ((this.allowedChildObjectTypeIds==null && other.getAllowedChildObjectTypeIds()==null) || 
             (this.allowedChildObjectTypeIds!=null &&
              java.util.Arrays.equals(this.allowedChildObjectTypeIds, other.getAllowedChildObjectTypeIds())));
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
        if (getParentId() != null) {
            _hashCode += getParentId().hashCode();
        }
        if (getAllowedChildObjectTypeIds() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getAllowedChildObjectTypeIds());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getAllowedChildObjectTypeIds(), i);
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
        new org.apache.axis.description.TypeDesc(FolderPropertiesType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "FolderPropertiesType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("objectTypeId");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "objectTypeId"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("parentId");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "parentId"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("allowedChildObjectTypeIds");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "allowedChildObjectTypeIds"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "enumObjectType"));
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("", "value"));
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

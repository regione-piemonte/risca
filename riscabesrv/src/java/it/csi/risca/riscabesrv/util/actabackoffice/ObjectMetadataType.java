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
 * ObjectMetadataType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actabackoffice;

public class ObjectMetadataType  implements java.io.Serializable {
    private it.csi.risca.riscabesrv.util.actabackoffice.QueryNameType queryName;

    private boolean selectable;

    private boolean queryable;

    private java.lang.String[] operators;

    private java.lang.String dataType;

    private boolean updatable;

    private it.csi.risca.riscabesrv.util.actabackoffice.PropertyFilterConfigurationInfoType[] propertyFilterConfigurationInfo;

    public ObjectMetadataType() {
    }

    public ObjectMetadataType(
           it.csi.risca.riscabesrv.util.actabackoffice.QueryNameType queryName,
           boolean selectable,
           boolean queryable,
           java.lang.String[] operators,
           java.lang.String dataType,
           boolean updatable,
           it.csi.risca.riscabesrv.util.actabackoffice.PropertyFilterConfigurationInfoType[] propertyFilterConfigurationInfo) {
           this.queryName = queryName;
           this.selectable = selectable;
           this.queryable = queryable;
           this.operators = operators;
           this.dataType = dataType;
           this.updatable = updatable;
           this.propertyFilterConfigurationInfo = propertyFilterConfigurationInfo;
    }


    /**
     * Gets the queryName value for this ObjectMetadataType.
     * 
     * @return queryName
     */
    public it.csi.risca.riscabesrv.util.actabackoffice.QueryNameType getQueryName() {
        return queryName;
    }


    /**
     * Sets the queryName value for this ObjectMetadataType.
     * 
     * @param queryName
     */
    public void setQueryName(it.csi.risca.riscabesrv.util.actabackoffice.QueryNameType queryName) {
        this.queryName = queryName;
    }


    /**
     * Gets the selectable value for this ObjectMetadataType.
     * 
     * @return selectable
     */
    public boolean isSelectable() {
        return selectable;
    }


    /**
     * Sets the selectable value for this ObjectMetadataType.
     * 
     * @param selectable
     */
    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }


    /**
     * Gets the queryable value for this ObjectMetadataType.
     * 
     * @return queryable
     */
    public boolean isQueryable() {
        return queryable;
    }


    /**
     * Sets the queryable value for this ObjectMetadataType.
     * 
     * @param queryable
     */
    public void setQueryable(boolean queryable) {
        this.queryable = queryable;
    }


    /**
     * Gets the operators value for this ObjectMetadataType.
     * 
     * @return operators
     */
    public java.lang.String[] getOperators() {
        return operators;
    }


    /**
     * Sets the operators value for this ObjectMetadataType.
     * 
     * @param operators
     */
    public void setOperators(java.lang.String[] operators) {
        this.operators = operators;
    }

    public java.lang.String getOperators(int i) {
        return this.operators[i];
    }

    public void setOperators(int i, java.lang.String _value) {
        this.operators[i] = _value;
    }


    /**
     * Gets the dataType value for this ObjectMetadataType.
     * 
     * @return dataType
     */
    public java.lang.String getDataType() {
        return dataType;
    }


    /**
     * Sets the dataType value for this ObjectMetadataType.
     * 
     * @param dataType
     */
    public void setDataType(java.lang.String dataType) {
        this.dataType = dataType;
    }


    /**
     * Gets the updatable value for this ObjectMetadataType.
     * 
     * @return updatable
     */
    public boolean isUpdatable() {
        return updatable;
    }


    /**
     * Sets the updatable value for this ObjectMetadataType.
     * 
     * @param updatable
     */
    public void setUpdatable(boolean updatable) {
        this.updatable = updatable;
    }


    /**
     * Gets the propertyFilterConfigurationInfo value for this ObjectMetadataType.
     * 
     * @return propertyFilterConfigurationInfo
     */
    public it.csi.risca.riscabesrv.util.actabackoffice.PropertyFilterConfigurationInfoType[] getPropertyFilterConfigurationInfo() {
        return propertyFilterConfigurationInfo;
    }


    /**
     * Sets the propertyFilterConfigurationInfo value for this ObjectMetadataType.
     * 
     * @param propertyFilterConfigurationInfo
     */
    public void setPropertyFilterConfigurationInfo(it.csi.risca.riscabesrv.util.actabackoffice.PropertyFilterConfigurationInfoType[] propertyFilterConfigurationInfo) {
        this.propertyFilterConfigurationInfo = propertyFilterConfigurationInfo;
    }

    public it.csi.risca.riscabesrv.util.actabackoffice.PropertyFilterConfigurationInfoType getPropertyFilterConfigurationInfo(int i) {
        return this.propertyFilterConfigurationInfo[i];
    }

    public void setPropertyFilterConfigurationInfo(int i, it.csi.risca.riscabesrv.util.actabackoffice.PropertyFilterConfigurationInfoType _value) {
        this.propertyFilterConfigurationInfo[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof ObjectMetadataType)) return false;
        ObjectMetadataType other = (ObjectMetadataType) obj;

        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.queryName==null && other.getQueryName()==null) || 
             (this.queryName!=null &&
              this.queryName.equals(other.getQueryName()))) &&
            this.selectable == other.isSelectable() &&
            this.queryable == other.isQueryable() &&
            ((this.operators==null && other.getOperators()==null) || 
             (this.operators!=null &&
              java.util.Arrays.equals(this.operators, other.getOperators()))) &&
            ((this.dataType==null && other.getDataType()==null) || 
             (this.dataType!=null &&
              this.dataType.equals(other.getDataType()))) &&
            this.updatable == other.isUpdatable() &&
            ((this.propertyFilterConfigurationInfo==null && other.getPropertyFilterConfigurationInfo()==null) || 
             (this.propertyFilterConfigurationInfo!=null &&
              java.util.Arrays.equals(this.propertyFilterConfigurationInfo, other.getPropertyFilterConfigurationInfo())));
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
        if (getQueryName() != null) {
            _hashCode += getQueryName().hashCode();
        }
        _hashCode += (isSelectable() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isQueryable() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getOperators() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getOperators());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getOperators(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getDataType() != null) {
            _hashCode += getDataType().hashCode();
        }
        _hashCode += (isUpdatable() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getPropertyFilterConfigurationInfo() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getPropertyFilterConfigurationInfo());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getPropertyFilterConfigurationInfo(), i);
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
        new org.apache.axis.description.TypeDesc(ObjectMetadataType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectMetadataType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("queryName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "queryName"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "QueryNameType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("selectable");
        elemField.setXmlName(new javax.xml.namespace.QName("", "selectable"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("queryable");
        elemField.setXmlName(new javax.xml.namespace.QName("", "queryable"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("operators");
        elemField.setXmlName(new javax.xml.namespace.QName("", "operators"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"));
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataType");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("updatable");
        elemField.setXmlName(new javax.xml.namespace.QName("", "updatable"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("propertyFilterConfigurationInfo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "propertyFilterConfigurationInfo"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "PropertyFilterConfigurationInfoType"));
        elemField.setMinOccurs(0);
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

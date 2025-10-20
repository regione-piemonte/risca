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
 * PropertyFilterType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actarelationships;

public class PropertyFilterType  implements java.io.Serializable {
    private java.lang.String filterType;

    private it.csi.risca.riscabesrv.util.actarelationships.QueryNameType[] propertyList;

    public PropertyFilterType() {
    }

    public PropertyFilterType(
           java.lang.String filterType,
           it.csi.risca.riscabesrv.util.actarelationships.QueryNameType[] propertyList) {
           this.filterType = filterType;
           this.propertyList = propertyList;
    }


    /**
     * Gets the filterType value for this PropertyFilterType.
     * 
     * @return filterType
     */
    public java.lang.String getFilterType() {
        return filterType;
    }


    /**
     * Sets the filterType value for this PropertyFilterType.
     * 
     * @param filterType
     */
    public void setFilterType(java.lang.String filterType) {
        this.filterType = filterType;
    }


    /**
     * Gets the propertyList value for this PropertyFilterType.
     * 
     * @return propertyList
     */
    public it.csi.risca.riscabesrv.util.actarelationships.QueryNameType[] getPropertyList() {
        return propertyList;
    }


    /**
     * Sets the propertyList value for this PropertyFilterType.
     * 
     * @param propertyList
     */
    public void setPropertyList(it.csi.risca.riscabesrv.util.actarelationships.QueryNameType[] propertyList) {
        this.propertyList = propertyList;
    }

    public it.csi.risca.riscabesrv.util.actarelationships.QueryNameType getPropertyList(int i) {
        return this.propertyList[i];
    }

    public void setPropertyList(int i, it.csi.risca.riscabesrv.util.actarelationships.QueryNameType _value) {
        this.propertyList[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof PropertyFilterType)) return false;
        PropertyFilterType other = (PropertyFilterType) obj;

        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.filterType==null && other.getFilterType()==null) || 
             (this.filterType!=null &&
              this.filterType.equals(other.getFilterType()))) &&
            ((this.propertyList==null && other.getPropertyList()==null) || 
             (this.propertyList!=null &&
              java.util.Arrays.equals(this.propertyList, other.getPropertyList())));
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
        if (getFilterType() != null) {
            _hashCode += getFilterType().hashCode();
        }
        if (getPropertyList() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getPropertyList());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getPropertyList(), i);
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
        new org.apache.axis.description.TypeDesc(PropertyFilterType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "PropertyFilterType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("filterType");
        elemField.setXmlName(new javax.xml.namespace.QName("", "filterType"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("propertyList");
        elemField.setXmlName(new javax.xml.namespace.QName("", "propertyList"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "QueryNameType"));
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

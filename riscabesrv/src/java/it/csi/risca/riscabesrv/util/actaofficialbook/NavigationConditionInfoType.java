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
 * NavigationConditionInfoType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actaofficialbook;

public class NavigationConditionInfoType  implements java.io.Serializable {
    private it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType parentNodeId;

    private java.lang.Boolean limitToChildren;

    public NavigationConditionInfoType() {
    }

    public NavigationConditionInfoType(
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType parentNodeId,
           java.lang.Boolean limitToChildren) {
           this.parentNodeId = parentNodeId;
           this.limitToChildren = limitToChildren;
    }


    /**
     * Gets the parentNodeId value for this NavigationConditionInfoType.
     * 
     * @return parentNodeId
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType getParentNodeId() {
        return parentNodeId;
    }


    /**
     * Sets the parentNodeId value for this NavigationConditionInfoType.
     * 
     * @param parentNodeId
     */
    public void setParentNodeId(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType parentNodeId) {
        this.parentNodeId = parentNodeId;
    }


    /**
     * Gets the limitToChildren value for this NavigationConditionInfoType.
     * 
     * @return limitToChildren
     */
    public java.lang.Boolean getLimitToChildren() {
        return limitToChildren;
    }


    /**
     * Sets the limitToChildren value for this NavigationConditionInfoType.
     * 
     * @param limitToChildren
     */
    public void setLimitToChildren(java.lang.Boolean limitToChildren) {
        this.limitToChildren = limitToChildren;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof NavigationConditionInfoType)) return false;
        NavigationConditionInfoType other = (NavigationConditionInfoType) obj;

        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.parentNodeId==null && other.getParentNodeId()==null) || 
             (this.parentNodeId!=null &&
              this.parentNodeId.equals(other.getParentNodeId()))) &&
            ((this.limitToChildren==null && other.getLimitToChildren()==null) || 
             (this.limitToChildren!=null &&
              this.limitToChildren.equals(other.getLimitToChildren())));
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
        if (getParentNodeId() != null) {
            _hashCode += getParentNodeId().hashCode();
        }
        if (getLimitToChildren() != null) {
            _hashCode += getLimitToChildren().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(NavigationConditionInfoType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "NavigationConditionInfoType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("parentNodeId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "parentNodeId"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("limitToChildren");
        elemField.setXmlName(new javax.xml.namespace.QName("", "limitToChildren"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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

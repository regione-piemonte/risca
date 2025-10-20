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
 * PagingResponseType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actanavigation;

public class PagingResponseType  implements java.io.Serializable {
    private it.csi.risca.riscabesrv.util.actanavigation.ObjectResponseType[] objects;

    private boolean hasMoreItems;

    public PagingResponseType() {
    }

    public PagingResponseType(
           it.csi.risca.riscabesrv.util.actanavigation.ObjectResponseType[] objects,
           boolean hasMoreItems) {
           this.objects = objects;
           this.hasMoreItems = hasMoreItems;
    }


    /**
     * Gets the objects value for this PagingResponseType.
     * 
     * @return objects
     */
    public it.csi.risca.riscabesrv.util.actanavigation.ObjectResponseType[] getObjects() {
        return objects;
    }


    /**
     * Sets the objects value for this PagingResponseType.
     * 
     * @param objects
     */
    public void setObjects(it.csi.risca.riscabesrv.util.actanavigation.ObjectResponseType[] objects) {
        this.objects = objects;
    }

    public it.csi.risca.riscabesrv.util.actanavigation.ObjectResponseType getObjects(int i) {
        return this.objects[i];
    }

    public void setObjects(int i, it.csi.risca.riscabesrv.util.actanavigation.ObjectResponseType _value) {
        this.objects[i] = _value;
    }


    /**
     * Gets the hasMoreItems value for this PagingResponseType.
     * 
     * @return hasMoreItems
     */
    public boolean isHasMoreItems() {
        return hasMoreItems;
    }


    /**
     * Sets the hasMoreItems value for this PagingResponseType.
     * 
     * @param hasMoreItems
     */
    public void setHasMoreItems(boolean hasMoreItems) {
        this.hasMoreItems = hasMoreItems;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof PagingResponseType)) return false;
        PagingResponseType other = (PagingResponseType) obj;

        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.objects==null && other.getObjects()==null) || 
             (this.objects!=null &&
              java.util.Arrays.equals(this.objects, other.getObjects()))) &&
            this.hasMoreItems == other.isHasMoreItems();
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
        if (getObjects() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getObjects());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getObjects(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        _hashCode += (isHasMoreItems() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(PagingResponseType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "PagingResponseType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("objects");
        elemField.setXmlName(new javax.xml.namespace.QName("", "objects"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectResponseType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("hasMoreItems");
        elemField.setXmlName(new javax.xml.namespace.QName("", "hasMoreItems"));
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

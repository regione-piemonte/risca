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
 * DatiInterscambioResponseType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actaofficialbook;

public class DatiInterscambioResponseType  implements java.io.Serializable {
    private it.csi.risca.riscabesrv.util.actaofficialbook.DatoInterscambio[] dati;

    private boolean hasMoreItems;

    public DatiInterscambioResponseType() {
    }

    public DatiInterscambioResponseType(
           it.csi.risca.riscabesrv.util.actaofficialbook.DatoInterscambio[] dati,
           boolean hasMoreItems) {
           this.dati = dati;
           this.hasMoreItems = hasMoreItems;
    }


    /**
     * Gets the dati value for this DatiInterscambioResponseType.
     * 
     * @return dati
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.DatoInterscambio[] getDati() {
        return dati;
    }


    /**
     * Sets the dati value for this DatiInterscambioResponseType.
     * 
     * @param dati
     */
    public void setDati(it.csi.risca.riscabesrv.util.actaofficialbook.DatoInterscambio[] dati) {
        this.dati = dati;
    }

    public it.csi.risca.riscabesrv.util.actaofficialbook.DatoInterscambio getDati(int i) {
        return this.dati[i];
    }

    public void setDati(int i, it.csi.risca.riscabesrv.util.actaofficialbook.DatoInterscambio _value) {
        this.dati[i] = _value;
    }


    /**
     * Gets the hasMoreItems value for this DatiInterscambioResponseType.
     * 
     * @return hasMoreItems
     */
    public boolean isHasMoreItems() {
        return hasMoreItems;
    }


    /**
     * Sets the hasMoreItems value for this DatiInterscambioResponseType.
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
        if (!(obj instanceof DatiInterscambioResponseType)) return false;
        DatiInterscambioResponseType other = (DatiInterscambioResponseType) obj;

        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.dati==null && other.getDati()==null) || 
             (this.dati!=null &&
              java.util.Arrays.equals(this.dati, other.getDati()))) &&
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
        if (getDati() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDati());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDati(), i);
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
        new org.apache.axis.description.TypeDesc(DatiInterscambioResponseType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "DatiInterscambioResponseType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dati");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dati"));
        elemField.setXmlType(new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "DatoInterscambio"));
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

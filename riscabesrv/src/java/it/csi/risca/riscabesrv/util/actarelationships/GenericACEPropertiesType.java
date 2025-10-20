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
 * GenericACEPropertiesType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actarelationships;

public abstract class GenericACEPropertiesType  extends it.csi.risca.riscabesrv.util.actarelationships.PolicyPropertiesType  implements java.io.Serializable {
    private it.csi.risca.riscabesrv.util.actarelationships.PrincipalIdType[] principalList;

    public GenericACEPropertiesType() {
    }

    public GenericACEPropertiesType(
           it.csi.risca.riscabesrv.util.actarelationships.ObjectIdType objectId,
           it.csi.risca.riscabesrv.util.actarelationships.ChangeTokenType changeToken,
           java.lang.String objectTypeId,
           java.lang.String policyName,
           it.csi.risca.riscabesrv.util.actarelationships.PrincipalIdType[] principalList) {
        super(
            objectId,
            changeToken,
            objectTypeId,
            policyName);
        this.principalList = principalList;
    }


    /**
     * Gets the principalList value for this GenericACEPropertiesType.
     * 
     * @return principalList
     */
    public it.csi.risca.riscabesrv.util.actarelationships.PrincipalIdType[] getPrincipalList() {
        return principalList;
    }


    /**
     * Sets the principalList value for this GenericACEPropertiesType.
     * 
     * @param principalList
     */
    public void setPrincipalList(it.csi.risca.riscabesrv.util.actarelationships.PrincipalIdType[] principalList) {
        this.principalList = principalList;
    }

    public it.csi.risca.riscabesrv.util.actarelationships.PrincipalIdType getPrincipalList(int i) {
        return this.principalList[i];
    }

    public void setPrincipalList(int i, it.csi.risca.riscabesrv.util.actarelationships.PrincipalIdType _value) {
        this.principalList[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof GenericACEPropertiesType)) return false;
        GenericACEPropertiesType other = (GenericACEPropertiesType) obj;

        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.principalList==null && other.getPrincipalList()==null) || 
             (this.principalList!=null &&
              java.util.Arrays.equals(this.principalList, other.getPrincipalList())));
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
        if (getPrincipalList() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getPrincipalList());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getPrincipalList(), i);
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
        new org.apache.axis.description.TypeDesc(GenericACEPropertiesType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "GenericACEPropertiesType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("principalList");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "principalList"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "PrincipalIdType"));
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

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
 * CommonPropertiesType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actaofficialbook;

public abstract class CommonPropertiesType  extends it.csi.risca.riscabesrv.util.actaofficialbook.PropertiesType  implements java.io.Serializable {
    private it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType objectId;

    private it.csi.risca.riscabesrv.util.actaofficialbook.ChangeTokenType changeToken;

    public CommonPropertiesType() {
    }

    public CommonPropertiesType(
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType objectId,
           it.csi.risca.riscabesrv.util.actaofficialbook.ChangeTokenType changeToken) {
        this.objectId = objectId;
        this.changeToken = changeToken;
    }


    /**
     * Gets the objectId value for this CommonPropertiesType.
     * 
     * @return objectId
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType getObjectId() {
        return objectId;
    }


    /**
     * Sets the objectId value for this CommonPropertiesType.
     * 
     * @param objectId
     */
    public void setObjectId(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType objectId) {
        this.objectId = objectId;
    }


    /**
     * Gets the changeToken value for this CommonPropertiesType.
     * 
     * @return changeToken
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.ChangeTokenType getChangeToken() {
        return changeToken;
    }


    /**
     * Sets the changeToken value for this CommonPropertiesType.
     * 
     * @param changeToken
     */
    public void setChangeToken(it.csi.risca.riscabesrv.util.actaofficialbook.ChangeTokenType changeToken) {
        this.changeToken = changeToken;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof CommonPropertiesType)) return false;
        CommonPropertiesType other = (CommonPropertiesType) obj;
 
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.objectId==null && other.getObjectId()==null) || 
             (this.objectId!=null &&
              this.objectId.equals(other.getObjectId()))) &&
            ((this.changeToken==null && other.getChangeToken()==null) || 
             (this.changeToken!=null &&
              this.changeToken.equals(other.getChangeToken())));
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
        if (getObjectId() != null) {
            _hashCode += getObjectId().hashCode();
        }
        if (getChangeToken() != null) {
            _hashCode += getChangeToken().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CommonPropertiesType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "CommonPropertiesType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("objectId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "objectId"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("changeToken");
        elemField.setXmlName(new javax.xml.namespace.QName("", "changeToken"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ChangeTokenType"));
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

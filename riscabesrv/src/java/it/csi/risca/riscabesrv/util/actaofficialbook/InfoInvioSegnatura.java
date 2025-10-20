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
 * InfoInvioSegnatura.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actaofficialbook;

public class InfoInvioSegnatura  implements java.io.Serializable {
    private boolean invioMultiplo;

    private boolean forzaturaAssenzaSigillo;

    private boolean invioNonPrioritario;

    private boolean copiaLavoro;

    public InfoInvioSegnatura() {
    }

    public InfoInvioSegnatura(
           boolean invioMultiplo,
           boolean forzaturaAssenzaSigillo,
           boolean invioNonPrioritario,
           boolean copiaLavoro) {
           this.invioMultiplo = invioMultiplo;
           this.forzaturaAssenzaSigillo = forzaturaAssenzaSigillo;
           this.invioNonPrioritario = invioNonPrioritario;
           this.copiaLavoro = copiaLavoro;
    }


    /**
     * Gets the invioMultiplo value for this InfoInvioSegnatura.
     * 
     * @return invioMultiplo
     */
    public boolean isInvioMultiplo() {
        return invioMultiplo;
    }


    /**
     * Sets the invioMultiplo value for this InfoInvioSegnatura.
     * 
     * @param invioMultiplo
     */
    public void setInvioMultiplo(boolean invioMultiplo) {
        this.invioMultiplo = invioMultiplo;
    }


    /**
     * Gets the forzaturaAssenzaSigillo value for this InfoInvioSegnatura.
     * 
     * @return forzaturaAssenzaSigillo
     */
    public boolean isForzaturaAssenzaSigillo() {
        return forzaturaAssenzaSigillo;
    }


    /**
     * Sets the forzaturaAssenzaSigillo value for this InfoInvioSegnatura.
     * 
     * @param forzaturaAssenzaSigillo
     */
    public void setForzaturaAssenzaSigillo(boolean forzaturaAssenzaSigillo) {
        this.forzaturaAssenzaSigillo = forzaturaAssenzaSigillo;
    }


    /**
     * Gets the invioNonPrioritario value for this InfoInvioSegnatura.
     * 
     * @return invioNonPrioritario
     */
    public boolean isInvioNonPrioritario() {
        return invioNonPrioritario;
    }


    /**
     * Sets the invioNonPrioritario value for this InfoInvioSegnatura.
     * 
     * @param invioNonPrioritario
     */
    public void setInvioNonPrioritario(boolean invioNonPrioritario) {
        this.invioNonPrioritario = invioNonPrioritario;
    }


    /**
     * Gets the copiaLavoro value for this InfoInvioSegnatura.
     * 
     * @return copiaLavoro
     */
    public boolean isCopiaLavoro() {
        return copiaLavoro;
    }


    /**
     * Sets the copiaLavoro value for this InfoInvioSegnatura.
     * 
     * @param copiaLavoro
     */
    public void setCopiaLavoro(boolean copiaLavoro) {
        this.copiaLavoro = copiaLavoro;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof InfoInvioSegnatura)) return false;
        InfoInvioSegnatura other = (InfoInvioSegnatura) obj;

        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.invioMultiplo == other.isInvioMultiplo() &&
            this.forzaturaAssenzaSigillo == other.isForzaturaAssenzaSigillo() &&
            this.invioNonPrioritario == other.isInvioNonPrioritario() &&
            this.copiaLavoro == other.isCopiaLavoro();
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
        _hashCode += (isInvioMultiplo() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isForzaturaAssenzaSigillo() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isInvioNonPrioritario() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isCopiaLavoro() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(InfoInvioSegnatura.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "InfoInvioSegnatura"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("invioMultiplo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "invioMultiplo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("forzaturaAssenzaSigillo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "forzaturaAssenzaSigillo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("invioNonPrioritario");
        elemField.setXmlName(new javax.xml.namespace.QName("", "invioNonPrioritario"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("copiaLavoro");
        elemField.setXmlName(new javax.xml.namespace.QName("", "copiaLavoro"));
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

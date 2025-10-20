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
 * DatiCertificatoType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actarelationships;

public class DatiCertificatoType  implements java.io.Serializable {
    private java.lang.String datiCertificatoMarcaTemporale;

    private java.lang.String datiCertificatoFirmaDigitale;

    public DatiCertificatoType() {
    }

    public DatiCertificatoType(
           java.lang.String datiCertificatoMarcaTemporale,
           java.lang.String datiCertificatoFirmaDigitale) {
           this.datiCertificatoMarcaTemporale = datiCertificatoMarcaTemporale;
           this.datiCertificatoFirmaDigitale = datiCertificatoFirmaDigitale;
    }


    /**
     * Gets the datiCertificatoMarcaTemporale value for this DatiCertificatoType.
     * 
     * @return datiCertificatoMarcaTemporale
     */
    public java.lang.String getDatiCertificatoMarcaTemporale() {
        return datiCertificatoMarcaTemporale;
    }


    /**
     * Sets the datiCertificatoMarcaTemporale value for this DatiCertificatoType.
     * 
     * @param datiCertificatoMarcaTemporale
     */
    public void setDatiCertificatoMarcaTemporale(java.lang.String datiCertificatoMarcaTemporale) {
        this.datiCertificatoMarcaTemporale = datiCertificatoMarcaTemporale;
    }


    /**
     * Gets the datiCertificatoFirmaDigitale value for this DatiCertificatoType.
     * 
     * @return datiCertificatoFirmaDigitale
     */
    public java.lang.String getDatiCertificatoFirmaDigitale() {
        return datiCertificatoFirmaDigitale;
    }


    /**
     * Sets the datiCertificatoFirmaDigitale value for this DatiCertificatoType.
     * 
     * @param datiCertificatoFirmaDigitale
     */
    public void setDatiCertificatoFirmaDigitale(java.lang.String datiCertificatoFirmaDigitale) {
        this.datiCertificatoFirmaDigitale = datiCertificatoFirmaDigitale;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof DatiCertificatoType)) return false;
        DatiCertificatoType other = (DatiCertificatoType) obj;

        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.datiCertificatoMarcaTemporale==null && other.getDatiCertificatoMarcaTemporale()==null) || 
             (this.datiCertificatoMarcaTemporale!=null &&
              this.datiCertificatoMarcaTemporale.equals(other.getDatiCertificatoMarcaTemporale()))) &&
            ((this.datiCertificatoFirmaDigitale==null && other.getDatiCertificatoFirmaDigitale()==null) || 
             (this.datiCertificatoFirmaDigitale!=null &&
              this.datiCertificatoFirmaDigitale.equals(other.getDatiCertificatoFirmaDigitale())));
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
        if (getDatiCertificatoMarcaTemporale() != null) {
            _hashCode += getDatiCertificatoMarcaTemporale().hashCode();
        }
        if (getDatiCertificatoFirmaDigitale() != null) {
            _hashCode += getDatiCertificatoFirmaDigitale().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DatiCertificatoType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "DatiCertificatoType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("datiCertificatoMarcaTemporale");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "DatiCertificatoMarcaTemporale"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "XMLType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("datiCertificatoFirmaDigitale");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "DatiCertificatoFirmaDigitale"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "XMLType"));
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

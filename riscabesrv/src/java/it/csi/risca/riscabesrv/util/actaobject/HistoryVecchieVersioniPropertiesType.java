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
 * HistoryVecchieVersioniPropertiesType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actaobject;

public class HistoryVecchieVersioniPropertiesType  extends it.csi.risca.riscabesrv.util.actaobject.RelationshipPropertiesType  implements java.io.Serializable {
    private int numeroVersione;

    private java.util.Date dataVersione;

    private java.lang.String motivazioneVersione;

    public HistoryVecchieVersioniPropertiesType() {
    }

    public HistoryVecchieVersioniPropertiesType(
           it.csi.risca.riscabesrv.util.actaobject.ObjectIdType objectId,
           it.csi.risca.riscabesrv.util.actaobject.ChangeTokenType changeToken,
           java.lang.String relationType,
           it.csi.risca.riscabesrv.util.actaobject.ObjectIdType sourceId,
           java.lang.String sourceType,
           it.csi.risca.riscabesrv.util.actaobject.ObjectIdType targetId,
           java.lang.String targetType,
           int numeroVersione,
           java.util.Date dataVersione,
           java.lang.String motivazioneVersione) {
        super(
            objectId,
            changeToken,
            relationType,
            sourceId,
            sourceType,
            targetId,
            targetType);
        this.numeroVersione = numeroVersione;
        this.dataVersione = dataVersione;
        this.motivazioneVersione = motivazioneVersione;
    }


    /**
     * Gets the numeroVersione value for this HistoryVecchieVersioniPropertiesType.
     * 
     * @return numeroVersione
     */
    public int getNumeroVersione() {
        return numeroVersione;
    }


    /**
     * Sets the numeroVersione value for this HistoryVecchieVersioniPropertiesType.
     * 
     * @param numeroVersione
     */
    public void setNumeroVersione(int numeroVersione) {
        this.numeroVersione = numeroVersione;
    }


    /**
     * Gets the dataVersione value for this HistoryVecchieVersioniPropertiesType.
     * 
     * @return dataVersione
     */
    public java.util.Date getDataVersione() {
        return dataVersione;
    }


    /**
     * Sets the dataVersione value for this HistoryVecchieVersioniPropertiesType.
     * 
     * @param dataVersione
     */
    public void setDataVersione(java.util.Date dataVersione) {
        this.dataVersione = dataVersione;
    }


    /**
     * Gets the motivazioneVersione value for this HistoryVecchieVersioniPropertiesType.
     * 
     * @return motivazioneVersione
     */
    public java.lang.String getMotivazioneVersione() {
        return motivazioneVersione;
    }


    /**
     * Sets the motivazioneVersione value for this HistoryVecchieVersioniPropertiesType.
     * 
     * @param motivazioneVersione
     */
    public void setMotivazioneVersione(java.lang.String motivazioneVersione) {
        this.motivazioneVersione = motivazioneVersione;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof HistoryVecchieVersioniPropertiesType)) return false;
        HistoryVecchieVersioniPropertiesType other = (HistoryVecchieVersioniPropertiesType) obj;

        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            this.numeroVersione == other.getNumeroVersione() &&
            ((this.dataVersione==null && other.getDataVersione()==null) || 
             (this.dataVersione!=null &&
              this.dataVersione.equals(other.getDataVersione()))) &&
            ((this.motivazioneVersione==null && other.getMotivazioneVersione()==null) || 
             (this.motivazioneVersione!=null &&
              this.motivazioneVersione.equals(other.getMotivazioneVersione())));
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
        _hashCode += getNumeroVersione();
        if (getDataVersione() != null) {
            _hashCode += getDataVersione().hashCode();
        }
        if (getMotivazioneVersione() != null) {
            _hashCode += getMotivazioneVersione().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(HistoryVecchieVersioniPropertiesType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "HistoryVecchieVersioniPropertiesType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numeroVersione");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "numeroVersione"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "integer"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataVersione");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "dataVersione"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "date"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("motivazioneVersione");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "motivazioneVersione"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"));
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

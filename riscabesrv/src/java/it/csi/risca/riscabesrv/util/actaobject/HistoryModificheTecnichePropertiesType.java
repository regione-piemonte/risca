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
 * HistoryModificheTecnichePropertiesType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actaobject;

public class HistoryModificheTecnichePropertiesType  extends it.csi.risca.riscabesrv.util.actaobject.RelationshipPropertiesType  implements java.io.Serializable {
    private java.util.Date dataTrasformazione;

    private java.lang.String motivazioneTrasformazione;

    public HistoryModificheTecnichePropertiesType() {
    }

    public HistoryModificheTecnichePropertiesType(
           it.csi.risca.riscabesrv.util.actaobject.ObjectIdType objectId,
           it.csi.risca.riscabesrv.util.actaobject.ChangeTokenType changeToken,
           java.lang.String relationType,
           it.csi.risca.riscabesrv.util.actaobject.ObjectIdType sourceId,
           java.lang.String sourceType,
           it.csi.risca.riscabesrv.util.actaobject.ObjectIdType targetId,
           java.lang.String targetType,
           java.util.Date dataTrasformazione,
           java.lang.String motivazioneTrasformazione) {
        super(
            objectId,
            changeToken,
            relationType,
            sourceId,
            sourceType,
            targetId,
            targetType);
        this.dataTrasformazione = dataTrasformazione;
        this.motivazioneTrasformazione = motivazioneTrasformazione;
    }


    /**
     * Gets the dataTrasformazione value for this HistoryModificheTecnichePropertiesType.
     * 
     * @return dataTrasformazione
     */
    public java.util.Date getDataTrasformazione() {
        return dataTrasformazione;
    }


    /**
     * Sets the dataTrasformazione value for this HistoryModificheTecnichePropertiesType.
     * 
     * @param dataTrasformazione
     */
    public void setDataTrasformazione(java.util.Date dataTrasformazione) {
        this.dataTrasformazione = dataTrasformazione;
    }


    /**
     * Gets the motivazioneTrasformazione value for this HistoryModificheTecnichePropertiesType.
     * 
     * @return motivazioneTrasformazione
     */
    public java.lang.String getMotivazioneTrasformazione() {
        return motivazioneTrasformazione;
    }


    /**
     * Sets the motivazioneTrasformazione value for this HistoryModificheTecnichePropertiesType.
     * 
     * @param motivazioneTrasformazione
     */
    public void setMotivazioneTrasformazione(java.lang.String motivazioneTrasformazione) {
        this.motivazioneTrasformazione = motivazioneTrasformazione;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof HistoryModificheTecnichePropertiesType)) return false;
        HistoryModificheTecnichePropertiesType other = (HistoryModificheTecnichePropertiesType) obj;

        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.dataTrasformazione==null && other.getDataTrasformazione()==null) || 
             (this.dataTrasformazione!=null &&
              this.dataTrasformazione.equals(other.getDataTrasformazione()))) &&
            ((this.motivazioneTrasformazione==null && other.getMotivazioneTrasformazione()==null) || 
             (this.motivazioneTrasformazione!=null &&
              this.motivazioneTrasformazione.equals(other.getMotivazioneTrasformazione())));
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
        if (getDataTrasformazione() != null) {
            _hashCode += getDataTrasformazione().hashCode();
        }
        if (getMotivazioneTrasformazione() != null) {
            _hashCode += getMotivazioneTrasformazione().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(HistoryModificheTecnichePropertiesType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "HistoryModificheTecnichePropertiesType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataTrasformazione");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "dataTrasformazione"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "date"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("motivazioneTrasformazione");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "motivazioneTrasformazione"));
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

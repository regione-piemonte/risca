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
 * GruppoAllegatiPropertiesType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actaobject;

public class GruppoAllegatiPropertiesType  extends it.csi.risca.riscabesrv.util.actaobject.FolderPropertiesType  implements java.io.Serializable {
    private int numeroAllegati;

    private java.util.Date dataInizio;

    private java.util.Date dataFine;

    private it.csi.risca.riscabesrv.util.actaobject.ObjectIdType classificazionePrincipale;

    public GruppoAllegatiPropertiesType() {
    }

    public GruppoAllegatiPropertiesType(
           it.csi.risca.riscabesrv.util.actaobject.ObjectIdType objectId,
           it.csi.risca.riscabesrv.util.actaobject.ChangeTokenType changeToken,
           java.lang.String objectTypeId,
           it.csi.risca.riscabesrv.util.actaobject.ObjectIdType parentId,
           java.lang.String[] allowedChildObjectTypeIds,
           int numeroAllegati,
           java.util.Date dataInizio,
           java.util.Date dataFine,
           it.csi.risca.riscabesrv.util.actaobject.ObjectIdType classificazionePrincipale) {
        super(
            objectId,
            changeToken,
            objectTypeId,
            parentId,
            allowedChildObjectTypeIds);
        this.numeroAllegati = numeroAllegati;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.classificazionePrincipale = classificazionePrincipale;
    }


    /**
     * Gets the numeroAllegati value for this GruppoAllegatiPropertiesType.
     * 
     * @return numeroAllegati
     */
    public int getNumeroAllegati() {
        return numeroAllegati;
    }


    /**
     * Sets the numeroAllegati value for this GruppoAllegatiPropertiesType.
     * 
     * @param numeroAllegati
     */
    public void setNumeroAllegati(int numeroAllegati) {
        this.numeroAllegati = numeroAllegati;
    }


    /**
     * Gets the dataInizio value for this GruppoAllegatiPropertiesType.
     * 
     * @return dataInizio
     */
    public java.util.Date getDataInizio() {
        return dataInizio;
    }


    /**
     * Sets the dataInizio value for this GruppoAllegatiPropertiesType.
     * 
     * @param dataInizio
     */
    public void setDataInizio(java.util.Date dataInizio) {
        this.dataInizio = dataInizio;
    }


    /**
     * Gets the dataFine value for this GruppoAllegatiPropertiesType.
     * 
     * @return dataFine
     */
    public java.util.Date getDataFine() {
        return dataFine;
    }


    /**
     * Sets the dataFine value for this GruppoAllegatiPropertiesType.
     * 
     * @param dataFine
     */
    public void setDataFine(java.util.Date dataFine) {
        this.dataFine = dataFine;
    }


    /**
     * Gets the classificazionePrincipale value for this GruppoAllegatiPropertiesType.
     * 
     * @return classificazionePrincipale
     */
    public it.csi.risca.riscabesrv.util.actaobject.ObjectIdType getClassificazionePrincipale() {
        return classificazionePrincipale;
    }


    /**
     * Sets the classificazionePrincipale value for this GruppoAllegatiPropertiesType.
     * 
     * @param classificazionePrincipale
     */
    public void setClassificazionePrincipale(it.csi.risca.riscabesrv.util.actaobject.ObjectIdType classificazionePrincipale) {
        this.classificazionePrincipale = classificazionePrincipale;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof GruppoAllegatiPropertiesType)) return false;
        GruppoAllegatiPropertiesType other = (GruppoAllegatiPropertiesType) obj;

        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            this.numeroAllegati == other.getNumeroAllegati() &&
            ((this.dataInizio==null && other.getDataInizio()==null) || 
             (this.dataInizio!=null &&
              this.dataInizio.equals(other.getDataInizio()))) &&
            ((this.dataFine==null && other.getDataFine()==null) || 
             (this.dataFine!=null &&
              this.dataFine.equals(other.getDataFine()))) &&
            ((this.classificazionePrincipale==null && other.getClassificazionePrincipale()==null) || 
             (this.classificazionePrincipale!=null &&
              this.classificazionePrincipale.equals(other.getClassificazionePrincipale())));
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
        _hashCode += getNumeroAllegati();
        if (getDataInizio() != null) {
            _hashCode += getDataInizio().hashCode();
        }
        if (getDataFine() != null) {
            _hashCode += getDataFine().hashCode();
        }
        if (getClassificazionePrincipale() != null) {
            _hashCode += getClassificazionePrincipale().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GruppoAllegatiPropertiesType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "GruppoAllegatiPropertiesType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numeroAllegati");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "numeroAllegati"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "integer"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataInizio");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "dataInizio"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "date"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataFine");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "dataFine"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "date"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("classificazionePrincipale");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "classificazionePrincipale"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"));
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

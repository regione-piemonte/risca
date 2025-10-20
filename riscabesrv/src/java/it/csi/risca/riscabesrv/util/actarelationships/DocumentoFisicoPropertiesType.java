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
 * DocumentoFisicoPropertiesType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actarelationships;

public class DocumentoFisicoPropertiesType  extends it.csi.risca.riscabesrv.util.actarelationships.FolderPropertiesType  implements java.io.Serializable {
    private java.lang.String descrizione;

    private int progressivoPerDocumento;

    private java.util.Date dataMemorizzazione;

    private java.lang.String docMimeTypes;

    public DocumentoFisicoPropertiesType() {
    }

    public DocumentoFisicoPropertiesType(
           it.csi.risca.riscabesrv.util.actarelationships.ObjectIdType objectId,
           it.csi.risca.riscabesrv.util.actarelationships.ChangeTokenType changeToken,
           java.lang.String objectTypeId,
           it.csi.risca.riscabesrv.util.actarelationships.ObjectIdType parentId,
           java.lang.String[] allowedChildObjectTypeIds,
           java.lang.String descrizione,
           int progressivoPerDocumento,
           java.util.Date dataMemorizzazione,
           java.lang.String docMimeTypes) {
        super(
            objectId,
            changeToken,
            objectTypeId,
            parentId,
            allowedChildObjectTypeIds);
        this.descrizione = descrizione;
        this.progressivoPerDocumento = progressivoPerDocumento;
        this.dataMemorizzazione = dataMemorizzazione;
        this.docMimeTypes = docMimeTypes;
    }


    /**
     * Gets the descrizione value for this DocumentoFisicoPropertiesType.
     * 
     * @return descrizione
     */
    public java.lang.String getDescrizione() {
        return descrizione;
    }


    /**
     * Sets the descrizione value for this DocumentoFisicoPropertiesType.
     * 
     * @param descrizione
     */
    public void setDescrizione(java.lang.String descrizione) {
        this.descrizione = descrizione;
    }


    /**
     * Gets the progressivoPerDocumento value for this DocumentoFisicoPropertiesType.
     * 
     * @return progressivoPerDocumento
     */
    public int getProgressivoPerDocumento() {
        return progressivoPerDocumento;
    }


    /**
     * Sets the progressivoPerDocumento value for this DocumentoFisicoPropertiesType.
     * 
     * @param progressivoPerDocumento
     */
    public void setProgressivoPerDocumento(int progressivoPerDocumento) {
        this.progressivoPerDocumento = progressivoPerDocumento;
    }


    /**
     * Gets the dataMemorizzazione value for this DocumentoFisicoPropertiesType.
     * 
     * @return dataMemorizzazione
     */
    public java.util.Date getDataMemorizzazione() {
        return dataMemorizzazione;
    }


    /**
     * Sets the dataMemorizzazione value for this DocumentoFisicoPropertiesType.
     * 
     * @param dataMemorizzazione
     */
    public void setDataMemorizzazione(java.util.Date dataMemorizzazione) {
        this.dataMemorizzazione = dataMemorizzazione;
    }


    /**
     * Gets the docMimeTypes value for this DocumentoFisicoPropertiesType.
     * 
     * @return docMimeTypes
     */
    public java.lang.String getDocMimeTypes() {
        return docMimeTypes;
    }


    /**
     * Sets the docMimeTypes value for this DocumentoFisicoPropertiesType.
     * 
     * @param docMimeTypes
     */
    public void setDocMimeTypes(java.lang.String docMimeTypes) {
        this.docMimeTypes = docMimeTypes;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof DocumentoFisicoPropertiesType)) return false;
        DocumentoFisicoPropertiesType other = (DocumentoFisicoPropertiesType) obj;

        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.descrizione==null && other.getDescrizione()==null) || 
             (this.descrizione!=null &&
              this.descrizione.equals(other.getDescrizione()))) &&
            this.progressivoPerDocumento == other.getProgressivoPerDocumento() &&
            ((this.dataMemorizzazione==null && other.getDataMemorizzazione()==null) || 
             (this.dataMemorizzazione!=null &&
              this.dataMemorizzazione.equals(other.getDataMemorizzazione()))) &&
            ((this.docMimeTypes==null && other.getDocMimeTypes()==null) || 
             (this.docMimeTypes!=null &&
              this.docMimeTypes.equals(other.getDocMimeTypes())));
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
        if (getDescrizione() != null) {
            _hashCode += getDescrizione().hashCode();
        }
        _hashCode += getProgressivoPerDocumento();
        if (getDataMemorizzazione() != null) {
            _hashCode += getDataMemorizzazione().hashCode();
        }
        if (getDocMimeTypes() != null) {
            _hashCode += getDocMimeTypes().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DocumentoFisicoPropertiesType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "DocumentoFisicoPropertiesType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descrizione");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "descrizione"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("progressivoPerDocumento");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "progressivoPerDocumento"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "integer"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataMemorizzazione");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "dataMemorizzazione"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "date"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("docMimeTypes");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "docMimeTypes"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "XMLType"));
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

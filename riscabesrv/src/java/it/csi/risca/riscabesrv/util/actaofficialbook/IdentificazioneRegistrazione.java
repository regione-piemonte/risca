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
 * IdentificazioneRegistrazione.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actaofficialbook;

public class IdentificazioneRegistrazione  implements java.io.Serializable {
    private it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType registrazioneId;

    private it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType folderId;

    private java.lang.String folderType;

    private it.csi.risca.riscabesrv.util.actaofficialbook.ChangeTokenType dataUltimoAggiornamento;

    private java.lang.String numero;

    private it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType classificazioneId;

    public IdentificazioneRegistrazione() {
    }

    public IdentificazioneRegistrazione(
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType registrazioneId,
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType folderId,
           java.lang.String folderType,
           it.csi.risca.riscabesrv.util.actaofficialbook.ChangeTokenType dataUltimoAggiornamento,
           java.lang.String numero,
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType classificazioneId) {
           this.registrazioneId = registrazioneId;
           this.folderId = folderId;
           this.folderType = folderType;
           this.dataUltimoAggiornamento = dataUltimoAggiornamento;
           this.numero = numero;
           this.classificazioneId = classificazioneId;
    }


    /**
     * Gets the registrazioneId value for this IdentificazioneRegistrazione.
     * 
     * @return registrazioneId
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType getRegistrazioneId() {
        return registrazioneId;
    }


    /**
     * Sets the registrazioneId value for this IdentificazioneRegistrazione.
     * 
     * @param registrazioneId
     */
    public void setRegistrazioneId(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType registrazioneId) {
        this.registrazioneId = registrazioneId;
    }


    /**
     * Gets the folderId value for this IdentificazioneRegistrazione.
     * 
     * @return folderId
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType getFolderId() {
        return folderId;
    }


    /**
     * Sets the folderId value for this IdentificazioneRegistrazione.
     * 
     * @param folderId
     */
    public void setFolderId(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType folderId) {
        this.folderId = folderId;
    }


    /**
     * Gets the folderType value for this IdentificazioneRegistrazione.
     * 
     * @return folderType
     */
    public java.lang.String getFolderType() {
        return folderType;
    }


    /**
     * Sets the folderType value for this IdentificazioneRegistrazione.
     * 
     * @param folderType
     */
    public void setFolderType(java.lang.String folderType) {
        this.folderType = folderType;
    }


    /**
     * Gets the dataUltimoAggiornamento value for this IdentificazioneRegistrazione.
     * 
     * @return dataUltimoAggiornamento
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.ChangeTokenType getDataUltimoAggiornamento() {
        return dataUltimoAggiornamento;
    }


    /**
     * Sets the dataUltimoAggiornamento value for this IdentificazioneRegistrazione.
     * 
     * @param dataUltimoAggiornamento
     */
    public void setDataUltimoAggiornamento(it.csi.risca.riscabesrv.util.actaofficialbook.ChangeTokenType dataUltimoAggiornamento) {
        this.dataUltimoAggiornamento = dataUltimoAggiornamento;
    }


    /**
     * Gets the numero value for this IdentificazioneRegistrazione.
     * 
     * @return numero
     */
    public java.lang.String getNumero() {
        return numero;
    }


    /**
     * Sets the numero value for this IdentificazioneRegistrazione.
     * 
     * @param numero
     */
    public void setNumero(java.lang.String numero) {
        this.numero = numero;
    }


    /**
     * Gets the classificazioneId value for this IdentificazioneRegistrazione.
     * 
     * @return classificazioneId
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType getClassificazioneId() {
        return classificazioneId;
    }


    /**
     * Sets the classificazioneId value for this IdentificazioneRegistrazione.
     * 
     * @param classificazioneId
     */
    public void setClassificazioneId(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType classificazioneId) {
        this.classificazioneId = classificazioneId;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof IdentificazioneRegistrazione)) return false;
        IdentificazioneRegistrazione other = (IdentificazioneRegistrazione) obj;

        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.registrazioneId==null && other.getRegistrazioneId()==null) || 
             (this.registrazioneId!=null &&
              this.registrazioneId.equals(other.getRegistrazioneId()))) &&
            ((this.folderId==null && other.getFolderId()==null) || 
             (this.folderId!=null &&
              this.folderId.equals(other.getFolderId()))) &&
            ((this.folderType==null && other.getFolderType()==null) || 
             (this.folderType!=null &&
              this.folderType.equals(other.getFolderType()))) &&
            ((this.dataUltimoAggiornamento==null && other.getDataUltimoAggiornamento()==null) || 
             (this.dataUltimoAggiornamento!=null &&
              this.dataUltimoAggiornamento.equals(other.getDataUltimoAggiornamento()))) &&
            ((this.numero==null && other.getNumero()==null) || 
             (this.numero!=null &&
              this.numero.equals(other.getNumero()))) &&
            ((this.classificazioneId==null && other.getClassificazioneId()==null) || 
             (this.classificazioneId!=null &&
              this.classificazioneId.equals(other.getClassificazioneId())));
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
        if (getRegistrazioneId() != null) {
            _hashCode += getRegistrazioneId().hashCode();
        }
        if (getFolderId() != null) {
            _hashCode += getFolderId().hashCode();
        }
        if (getFolderType() != null) {
            _hashCode += getFolderType().hashCode();
        }
        if (getDataUltimoAggiornamento() != null) {
            _hashCode += getDataUltimoAggiornamento().hashCode();
        }
        if (getNumero() != null) {
            _hashCode += getNumero().hashCode();
        }
        if (getClassificazioneId() != null) {
            _hashCode += getClassificazioneId().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(IdentificazioneRegistrazione.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "IdentificazioneRegistrazione"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("registrazioneId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "registrazioneId"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("folderId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "folderId"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("folderType");
        elemField.setXmlName(new javax.xml.namespace.QName("", "folderType"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataUltimoAggiornamento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataUltimoAggiornamento"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ChangeTokenType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numero");
        elemField.setXmlName(new javax.xml.namespace.QName("", "numero"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("classificazioneId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "classificazioneId"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"));
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

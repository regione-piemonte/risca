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
 * Protocollazione.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actaofficialbook;

public class Protocollazione  extends it.csi.risca.riscabesrv.util.actaofficialbook.RegistrazioneRequest  implements java.io.Serializable {
    private boolean creazioneAsincronaDocumento;

    private it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType folderId;

    private java.lang.String folderType;

    private it.csi.risca.riscabesrv.util.actaofficialbook.RegistrazioneAPI registrazioneAPI;

    public Protocollazione() {
    }

    public Protocollazione(
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType registroId,
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType aooProtocollanteId,
           boolean senzaCreazioneSoggettiEsterni,
           boolean creazioneAsincronaDocumento,
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType folderId,
           java.lang.String folderType,
           it.csi.risca.riscabesrv.util.actaofficialbook.RegistrazioneAPI registrazioneAPI) {
        super(
            registroId,
            aooProtocollanteId,
            senzaCreazioneSoggettiEsterni);
        this.creazioneAsincronaDocumento = creazioneAsincronaDocumento;
        this.folderId = folderId;
        this.folderType = folderType;
        this.registrazioneAPI = registrazioneAPI;
    }


    /**
     * Gets the creazioneAsincronaDocumento value for this Protocollazione.
     * 
     * @return creazioneAsincronaDocumento
     */
    public boolean isCreazioneAsincronaDocumento() {
        return creazioneAsincronaDocumento;
    }


    /**
     * Sets the creazioneAsincronaDocumento value for this Protocollazione.
     * 
     * @param creazioneAsincronaDocumento
     */
    public void setCreazioneAsincronaDocumento(boolean creazioneAsincronaDocumento) {
        this.creazioneAsincronaDocumento = creazioneAsincronaDocumento;
    }


    /**
     * Gets the folderId value for this Protocollazione.
     * 
     * @return folderId
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType getFolderId() {
        return folderId;
    }


    /**
     * Sets the folderId value for this Protocollazione.
     * 
     * @param folderId
     */
    public void setFolderId(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType folderId) {
        this.folderId = folderId;
    }


    /**
     * Gets the folderType value for this Protocollazione.
     * 
     * @return folderType
     */
    public java.lang.String getFolderType() {
        return folderType;
    }


    /**
     * Sets the folderType value for this Protocollazione.
     * 
     * @param folderType
     */
    public void setFolderType(java.lang.String folderType) {
        this.folderType = folderType;
    }


    /**
     * Gets the registrazioneAPI value for this Protocollazione.
     * 
     * @return registrazioneAPI
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.RegistrazioneAPI getRegistrazioneAPI() {
        return registrazioneAPI;
    }


    /**
     * Sets the registrazioneAPI value for this Protocollazione.
     * 
     * @param registrazioneAPI
     */
    public void setRegistrazioneAPI(it.csi.risca.riscabesrv.util.actaofficialbook.RegistrazioneAPI registrazioneAPI) {
        this.registrazioneAPI = registrazioneAPI;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof Protocollazione)) return false;
        Protocollazione other = (Protocollazione) obj;

        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            this.creazioneAsincronaDocumento == other.isCreazioneAsincronaDocumento() &&
            ((this.folderId==null && other.getFolderId()==null) || 
             (this.folderId!=null &&
              this.folderId.equals(other.getFolderId()))) &&
            ((this.folderType==null && other.getFolderType()==null) || 
             (this.folderType!=null &&
              this.folderType.equals(other.getFolderType()))) &&
            ((this.registrazioneAPI==null && other.getRegistrazioneAPI()==null) || 
             (this.registrazioneAPI!=null &&
              this.registrazioneAPI.equals(other.getRegistrazioneAPI())));
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
        _hashCode += (isCreazioneAsincronaDocumento() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getFolderId() != null) {
            _hashCode += getFolderId().hashCode();
        }
        if (getFolderType() != null) {
            _hashCode += getFolderType().hashCode();
        }
        if (getRegistrazioneAPI() != null) {
            _hashCode += getRegistrazioneAPI().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Protocollazione.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "Protocollazione"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("creazioneAsincronaDocumento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "creazioneAsincronaDocumento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
        elemField.setFieldName("registrazioneAPI");
        elemField.setXmlName(new javax.xml.namespace.QName("", "registrazioneAPI"));
        elemField.setXmlType(new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "RegistrazioneAPI"));
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

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
 * RiferimentoSoggettoEsistente.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actaofficialbook;

public class RiferimentoSoggettoEsistente  extends it.csi.risca.riscabesrv.util.actaofficialbook.InfoSoggettoAssociato  implements java.io.Serializable {
    private it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType soggettoId;

    private it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType nodoIdSeTipologiaUtente;

    private java.lang.String tipologia;

    private java.lang.String idPFPGUL;

    public RiferimentoSoggettoEsistente() {
    }

    public RiferimentoSoggettoEsistente(
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType soggettoId,
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType nodoIdSeTipologiaUtente,
           java.lang.String tipologia,
           java.lang.String idPFPGUL) {
        this.soggettoId = soggettoId;
        this.nodoIdSeTipologiaUtente = nodoIdSeTipologiaUtente;
        this.tipologia = tipologia;
        this.idPFPGUL = idPFPGUL;
    }


    /**
     * Gets the soggettoId value for this RiferimentoSoggettoEsistente.
     * 
     * @return soggettoId
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType getSoggettoId() {
        return soggettoId;
    }


    /**
     * Sets the soggettoId value for this RiferimentoSoggettoEsistente.
     * 
     * @param soggettoId
     */
    public void setSoggettoId(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType soggettoId) {
        this.soggettoId = soggettoId;
    }


    /**
     * Gets the nodoIdSeTipologiaUtente value for this RiferimentoSoggettoEsistente.
     * 
     * @return nodoIdSeTipologiaUtente
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType getNodoIdSeTipologiaUtente() {
        return nodoIdSeTipologiaUtente;
    }


    /**
     * Sets the nodoIdSeTipologiaUtente value for this RiferimentoSoggettoEsistente.
     * 
     * @param nodoIdSeTipologiaUtente
     */
    public void setNodoIdSeTipologiaUtente(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType nodoIdSeTipologiaUtente) {
        this.nodoIdSeTipologiaUtente = nodoIdSeTipologiaUtente;
    }


    /**
     * Gets the tipologia value for this RiferimentoSoggettoEsistente.
     * 
     * @return tipologia
     */
    public java.lang.String getTipologia() {
        return tipologia;
    }


    /**
     * Sets the tipologia value for this RiferimentoSoggettoEsistente.
     * 
     * @param tipologia
     */
    public void setTipologia(java.lang.String tipologia) {
        this.tipologia = tipologia;
    }


    /**
     * Gets the idPFPGUL value for this RiferimentoSoggettoEsistente.
     * 
     * @return idPFPGUL
     */
    public java.lang.String getIdPFPGUL() {
        return idPFPGUL;
    }


    /**
     * Sets the idPFPGUL value for this RiferimentoSoggettoEsistente.
     * 
     * @param idPFPGUL
     */
    public void setIdPFPGUL(java.lang.String idPFPGUL) {
        this.idPFPGUL = idPFPGUL;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof RiferimentoSoggettoEsistente)) return false;
        RiferimentoSoggettoEsistente other = (RiferimentoSoggettoEsistente) obj;

        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.soggettoId==null && other.getSoggettoId()==null) || 
             (this.soggettoId!=null &&
              this.soggettoId.equals(other.getSoggettoId()))) &&
            ((this.nodoIdSeTipologiaUtente==null && other.getNodoIdSeTipologiaUtente()==null) || 
             (this.nodoIdSeTipologiaUtente!=null &&
              this.nodoIdSeTipologiaUtente.equals(other.getNodoIdSeTipologiaUtente()))) &&
            ((this.tipologia==null && other.getTipologia()==null) || 
             (this.tipologia!=null &&
              this.tipologia.equals(other.getTipologia()))) &&
            ((this.idPFPGUL==null && other.getIdPFPGUL()==null) || 
             (this.idPFPGUL!=null &&
              this.idPFPGUL.equals(other.getIdPFPGUL())));
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
        if (getSoggettoId() != null) {
            _hashCode += getSoggettoId().hashCode();
        }
        if (getNodoIdSeTipologiaUtente() != null) {
            _hashCode += getNodoIdSeTipologiaUtente().hashCode();
        }
        if (getTipologia() != null) {
            _hashCode += getTipologia().hashCode();
        }
        if (getIdPFPGUL() != null) {
            _hashCode += getIdPFPGUL().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RiferimentoSoggettoEsistente.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "RiferimentoSoggettoEsistente"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("soggettoId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "soggettoId"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nodoIdSeTipologiaUtente");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nodoIdSeTipologiaUtente"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipologia");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tipologia"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idPFPGUL");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idPFPGUL"));
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

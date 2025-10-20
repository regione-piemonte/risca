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
 * IdentificazioneAnnotazioneOB.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actaofficialbook;

public class IdentificazioneAnnotazioneOB  implements java.io.Serializable {
    private it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idAnnotazioneOB;

    private it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType target;

    private java.lang.String tipologiaTarget;

    private it.csi.risca.riscabesrv.util.actaofficialbook.ChangeTokenType dataUltimoAggiornamento;

    public IdentificazioneAnnotazioneOB() {
    }

    public IdentificazioneAnnotazioneOB(
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idAnnotazioneOB,
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType target,
           java.lang.String tipologiaTarget,
           it.csi.risca.riscabesrv.util.actaofficialbook.ChangeTokenType dataUltimoAggiornamento) {
           this.idAnnotazioneOB = idAnnotazioneOB;
           this.target = target;
           this.tipologiaTarget = tipologiaTarget;
           this.dataUltimoAggiornamento = dataUltimoAggiornamento;
    }


    /**
     * Gets the idAnnotazioneOB value for this IdentificazioneAnnotazioneOB.
     * 
     * @return idAnnotazioneOB
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType getIdAnnotazioneOB() {
        return idAnnotazioneOB;
    }


    /**
     * Sets the idAnnotazioneOB value for this IdentificazioneAnnotazioneOB.
     * 
     * @param idAnnotazioneOB
     */
    public void setIdAnnotazioneOB(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idAnnotazioneOB) {
        this.idAnnotazioneOB = idAnnotazioneOB;
    }


    /**
     * Gets the target value for this IdentificazioneAnnotazioneOB.
     * 
     * @return target
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType getTarget() {
        return target;
    }


    /**
     * Sets the target value for this IdentificazioneAnnotazioneOB.
     * 
     * @param target
     */
    public void setTarget(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType target) {
        this.target = target;
    }


    /**
     * Gets the tipologiaTarget value for this IdentificazioneAnnotazioneOB.
     * 
     * @return tipologiaTarget
     */
    public java.lang.String getTipologiaTarget() {
        return tipologiaTarget;
    }


    /**
     * Sets the tipologiaTarget value for this IdentificazioneAnnotazioneOB.
     * 
     * @param tipologiaTarget
     */
    public void setTipologiaTarget(java.lang.String tipologiaTarget) {
        this.tipologiaTarget = tipologiaTarget;
    }


    /**
     * Gets the dataUltimoAggiornamento value for this IdentificazioneAnnotazioneOB.
     * 
     * @return dataUltimoAggiornamento
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.ChangeTokenType getDataUltimoAggiornamento() {
        return dataUltimoAggiornamento;
    }


    /**
     * Sets the dataUltimoAggiornamento value for this IdentificazioneAnnotazioneOB.
     * 
     * @param dataUltimoAggiornamento
     */
    public void setDataUltimoAggiornamento(it.csi.risca.riscabesrv.util.actaofficialbook.ChangeTokenType dataUltimoAggiornamento) {
        this.dataUltimoAggiornamento = dataUltimoAggiornamento;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof IdentificazioneAnnotazioneOB)) return false;
        IdentificazioneAnnotazioneOB other = (IdentificazioneAnnotazioneOB) obj;

        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.idAnnotazioneOB==null && other.getIdAnnotazioneOB()==null) || 
             (this.idAnnotazioneOB!=null &&
              this.idAnnotazioneOB.equals(other.getIdAnnotazioneOB()))) &&
            ((this.target==null && other.getTarget()==null) || 
             (this.target!=null &&
              this.target.equals(other.getTarget()))) &&
            ((this.tipologiaTarget==null && other.getTipologiaTarget()==null) || 
             (this.tipologiaTarget!=null &&
              this.tipologiaTarget.equals(other.getTipologiaTarget()))) &&
            ((this.dataUltimoAggiornamento==null && other.getDataUltimoAggiornamento()==null) || 
             (this.dataUltimoAggiornamento!=null &&
              this.dataUltimoAggiornamento.equals(other.getDataUltimoAggiornamento())));
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
        if (getIdAnnotazioneOB() != null) {
            _hashCode += getIdAnnotazioneOB().hashCode();
        }
        if (getTarget() != null) {
            _hashCode += getTarget().hashCode();
        }
        if (getTipologiaTarget() != null) {
            _hashCode += getTipologiaTarget().hashCode();
        }
        if (getDataUltimoAggiornamento() != null) {
            _hashCode += getDataUltimoAggiornamento().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(IdentificazioneAnnotazioneOB.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "IdentificazioneAnnotazioneOB"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idAnnotazioneOB");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idAnnotazioneOB"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("target");
        elemField.setXmlName(new javax.xml.namespace.QName("", "target"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipologiaTarget");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tipologiaTarget"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataUltimoAggiornamento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataUltimoAggiornamento"));
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

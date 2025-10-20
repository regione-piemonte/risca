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
 * InfoAnnullamentoRegistrazione.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actaofficialbook;

public class InfoAnnullamentoRegistrazione  implements java.io.Serializable {
    private it.csi.risca.riscabesrv.util.actaofficialbook.InfoCreazioneAnnotazioneOB annotazione;

    private java.util.Date dataAnnullamento;

    private java.lang.String utenteAnnullamento;

    public InfoAnnullamentoRegistrazione() {
    }

    public InfoAnnullamentoRegistrazione(
           it.csi.risca.riscabesrv.util.actaofficialbook.InfoCreazioneAnnotazioneOB annotazione,
           java.util.Date dataAnnullamento,
           java.lang.String utenteAnnullamento) {
           this.annotazione = annotazione;
           this.dataAnnullamento = dataAnnullamento;
           this.utenteAnnullamento = utenteAnnullamento;
    }


    /**
     * Gets the annotazione value for this InfoAnnullamentoRegistrazione.
     * 
     * @return annotazione
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.InfoCreazioneAnnotazioneOB getAnnotazione() {
        return annotazione;
    }


    /**
     * Sets the annotazione value for this InfoAnnullamentoRegistrazione.
     * 
     * @param annotazione
     */
    public void setAnnotazione(it.csi.risca.riscabesrv.util.actaofficialbook.InfoCreazioneAnnotazioneOB annotazione) {
        this.annotazione = annotazione;
    }


    /**
     * Gets the dataAnnullamento value for this InfoAnnullamentoRegistrazione.
     * 
     * @return dataAnnullamento
     */
    public java.util.Date getDataAnnullamento() {
        return dataAnnullamento;
    }


    /**
     * Sets the dataAnnullamento value for this InfoAnnullamentoRegistrazione.
     * 
     * @param dataAnnullamento
     */
    public void setDataAnnullamento(java.util.Date dataAnnullamento) {
        this.dataAnnullamento = dataAnnullamento;
    }


    /**
     * Gets the utenteAnnullamento value for this InfoAnnullamentoRegistrazione.
     * 
     * @return utenteAnnullamento
     */
    public java.lang.String getUtenteAnnullamento() {
        return utenteAnnullamento;
    }


    /**
     * Sets the utenteAnnullamento value for this InfoAnnullamentoRegistrazione.
     * 
     * @param utenteAnnullamento
     */
    public void setUtenteAnnullamento(java.lang.String utenteAnnullamento) {
        this.utenteAnnullamento = utenteAnnullamento;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof InfoAnnullamentoRegistrazione)) return false;
        InfoAnnullamentoRegistrazione other = (InfoAnnullamentoRegistrazione) obj;

        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.annotazione==null && other.getAnnotazione()==null) || 
             (this.annotazione!=null &&
              this.annotazione.equals(other.getAnnotazione()))) &&
            ((this.dataAnnullamento==null && other.getDataAnnullamento()==null) || 
             (this.dataAnnullamento!=null &&
              this.dataAnnullamento.equals(other.getDataAnnullamento()))) &&
            ((this.utenteAnnullamento==null && other.getUtenteAnnullamento()==null) || 
             (this.utenteAnnullamento!=null &&
              this.utenteAnnullamento.equals(other.getUtenteAnnullamento())));
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
        if (getAnnotazione() != null) {
            _hashCode += getAnnotazione().hashCode();
        }
        if (getDataAnnullamento() != null) {
            _hashCode += getDataAnnullamento().hashCode();
        }
        if (getUtenteAnnullamento() != null) {
            _hashCode += getUtenteAnnullamento().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(InfoAnnullamentoRegistrazione.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "InfoAnnullamentoRegistrazione"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("annotazione");
        elemField.setXmlName(new javax.xml.namespace.QName("", "annotazione"));
        elemField.setXmlType(new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "InfoCreazioneAnnotazioneOB"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataAnnullamento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataAnnullamento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("utenteAnnullamento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "utenteAnnullamento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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

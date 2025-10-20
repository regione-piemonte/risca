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
 * LogProtocolloPropertiesType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actaofficialbook;

public class LogProtocolloPropertiesType  extends it.csi.risca.riscabesrv.util.actaofficialbook.OfficialBookPropertiesType  implements java.io.Serializable {
    private java.util.Date dataModifica;

    private java.lang.String vecchioValore;

    private int idCampo;

    private it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idRegistroProtocollo;

    private it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idRegistrazione;

    private it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idUtenteCreazione;

    public LogProtocolloPropertiesType() {
    }

    public LogProtocolloPropertiesType(
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType objectId,
           it.csi.risca.riscabesrv.util.actaofficialbook.ChangeTokenType changeToken,
           java.lang.String objectTypeId,
           java.util.Date dataModifica,
           java.lang.String vecchioValore,
           int idCampo,
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idRegistroProtocollo,
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idRegistrazione,
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idUtenteCreazione) {
        super(
            objectId,
            changeToken,
            objectTypeId);
        this.dataModifica = dataModifica;
        this.vecchioValore = vecchioValore;
        this.idCampo = idCampo;
        this.idRegistroProtocollo = idRegistroProtocollo;
        this.idRegistrazione = idRegistrazione;
        this.idUtenteCreazione = idUtenteCreazione;
    }


    /**
     * Gets the dataModifica value for this LogProtocolloPropertiesType.
     * 
     * @return dataModifica
     */
    public java.util.Date getDataModifica() {
        return dataModifica;
    }


    /**
     * Sets the dataModifica value for this LogProtocolloPropertiesType.
     * 
     * @param dataModifica
     */
    public void setDataModifica(java.util.Date dataModifica) {
        this.dataModifica = dataModifica;
    }


    /**
     * Gets the vecchioValore value for this LogProtocolloPropertiesType.
     * 
     * @return vecchioValore
     */
    public java.lang.String getVecchioValore() {
        return vecchioValore;
    }


    /**
     * Sets the vecchioValore value for this LogProtocolloPropertiesType.
     * 
     * @param vecchioValore
     */
    public void setVecchioValore(java.lang.String vecchioValore) {
        this.vecchioValore = vecchioValore;
    }


    /**
     * Gets the idCampo value for this LogProtocolloPropertiesType.
     * 
     * @return idCampo
     */
    public int getIdCampo() {
        return idCampo;
    }


    /**
     * Sets the idCampo value for this LogProtocolloPropertiesType.
     * 
     * @param idCampo
     */
    public void setIdCampo(int idCampo) {
        this.idCampo = idCampo;
    }


    /**
     * Gets the idRegistroProtocollo value for this LogProtocolloPropertiesType.
     * 
     * @return idRegistroProtocollo
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType getIdRegistroProtocollo() {
        return idRegistroProtocollo;
    }


    /**
     * Sets the idRegistroProtocollo value for this LogProtocolloPropertiesType.
     * 
     * @param idRegistroProtocollo
     */
    public void setIdRegistroProtocollo(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idRegistroProtocollo) {
        this.idRegistroProtocollo = idRegistroProtocollo;
    }


    /**
     * Gets the idRegistrazione value for this LogProtocolloPropertiesType.
     * 
     * @return idRegistrazione
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType getIdRegistrazione() {
        return idRegistrazione;
    }


    /**
     * Sets the idRegistrazione value for this LogProtocolloPropertiesType.
     * 
     * @param idRegistrazione
     */
    public void setIdRegistrazione(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idRegistrazione) {
        this.idRegistrazione = idRegistrazione;
    }


    /**
     * Gets the idUtenteCreazione value for this LogProtocolloPropertiesType.
     * 
     * @return idUtenteCreazione
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType getIdUtenteCreazione() {
        return idUtenteCreazione;
    }


    /**
     * Sets the idUtenteCreazione value for this LogProtocolloPropertiesType.
     * 
     * @param idUtenteCreazione
     */
    public void setIdUtenteCreazione(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idUtenteCreazione) {
        this.idUtenteCreazione = idUtenteCreazione;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof LogProtocolloPropertiesType)) return false;
        LogProtocolloPropertiesType other = (LogProtocolloPropertiesType) obj;

        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.dataModifica==null && other.getDataModifica()==null) || 
             (this.dataModifica!=null &&
              this.dataModifica.equals(other.getDataModifica()))) &&
            ((this.vecchioValore==null && other.getVecchioValore()==null) || 
             (this.vecchioValore!=null &&
              this.vecchioValore.equals(other.getVecchioValore()))) &&
            this.idCampo == other.getIdCampo() &&
            ((this.idRegistroProtocollo==null && other.getIdRegistroProtocollo()==null) || 
             (this.idRegistroProtocollo!=null &&
              this.idRegistroProtocollo.equals(other.getIdRegistroProtocollo()))) &&
            ((this.idRegistrazione==null && other.getIdRegistrazione()==null) || 
             (this.idRegistrazione!=null &&
              this.idRegistrazione.equals(other.getIdRegistrazione()))) &&
            ((this.idUtenteCreazione==null && other.getIdUtenteCreazione()==null) || 
             (this.idUtenteCreazione!=null &&
              this.idUtenteCreazione.equals(other.getIdUtenteCreazione())));
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
        if (getDataModifica() != null) {
            _hashCode += getDataModifica().hashCode();
        }
        if (getVecchioValore() != null) {
            _hashCode += getVecchioValore().hashCode();
        }
        _hashCode += getIdCampo();
        if (getIdRegistroProtocollo() != null) {
            _hashCode += getIdRegistroProtocollo().hashCode();
        }
        if (getIdRegistrazione() != null) {
            _hashCode += getIdRegistrazione().hashCode();
        }
        if (getIdUtenteCreazione() != null) {
            _hashCode += getIdUtenteCreazione().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(LogProtocolloPropertiesType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "LogProtocolloPropertiesType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataModifica");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataModifica"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("vecchioValore");
        elemField.setXmlName(new javax.xml.namespace.QName("", "vecchioValore"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idCampo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idCampo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idRegistroProtocollo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idRegistroProtocollo"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idRegistrazione");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idRegistrazione"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idUtenteCreazione");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idUtenteCreazione"));
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

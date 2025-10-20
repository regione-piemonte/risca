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
 * AnnotazioniPropertiesType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actaobject;

public class AnnotazioniPropertiesType  extends it.csi.risca.riscabesrv.util.actaobject.CommonPropertiesType  implements java.io.Serializable {
    private java.lang.String objectTypeId;

    private java.lang.String descrizione;

    private it.csi.risca.riscabesrv.util.actaobject.IdUtenteType idUtente;

    private java.util.Date data;

    private boolean annotazioneFormale;

    public AnnotazioniPropertiesType() {
    }

    public AnnotazioniPropertiesType(
           it.csi.risca.riscabesrv.util.actaobject.ObjectIdType objectId,
           it.csi.risca.riscabesrv.util.actaobject.ChangeTokenType changeToken,
           java.lang.String objectTypeId,
           java.lang.String descrizione,
           it.csi.risca.riscabesrv.util.actaobject.IdUtenteType idUtente,
           java.util.Date data,
           boolean annotazioneFormale) {
        super(
            objectId,
            changeToken);
        this.objectTypeId = objectTypeId;
        this.descrizione = descrizione;
        this.idUtente = idUtente;
        this.data = data;
        this.annotazioneFormale = annotazioneFormale;
    }


    /**
     * Gets the objectTypeId value for this AnnotazioniPropertiesType.
     * 
     * @return objectTypeId
     */
    public java.lang.String getObjectTypeId() {
        return objectTypeId;
    }


    /**
     * Sets the objectTypeId value for this AnnotazioniPropertiesType.
     * 
     * @param objectTypeId
     */
    public void setObjectTypeId(java.lang.String objectTypeId) {
        this.objectTypeId = objectTypeId;
    }


    /**
     * Gets the descrizione value for this AnnotazioniPropertiesType.
     * 
     * @return descrizione
     */
    public java.lang.String getDescrizione() {
        return descrizione;
    }


    /**
     * Sets the descrizione value for this AnnotazioniPropertiesType.
     * 
     * @param descrizione
     */
    public void setDescrizione(java.lang.String descrizione) {
        this.descrizione = descrizione;
    }


    /**
     * Gets the idUtente value for this AnnotazioniPropertiesType.
     * 
     * @return idUtente
     */
    public it.csi.risca.riscabesrv.util.actaobject.IdUtenteType getIdUtente() {
        return idUtente;
    }


    /**
     * Sets the idUtente value for this AnnotazioniPropertiesType.
     * 
     * @param idUtente
     */
    public void setIdUtente(it.csi.risca.riscabesrv.util.actaobject.IdUtenteType idUtente) {
        this.idUtente = idUtente;
    }


    /**
     * Gets the data value for this AnnotazioniPropertiesType.
     * 
     * @return data
     */
    public java.util.Date getData() {
        return data;
    }


    /**
     * Sets the data value for this AnnotazioniPropertiesType.
     * 
     * @param data
     */
    public void setData(java.util.Date data) {
        this.data = data;
    }


    /**
     * Gets the annotazioneFormale value for this AnnotazioniPropertiesType.
     * 
     * @return annotazioneFormale
     */
    public boolean isAnnotazioneFormale() {
        return annotazioneFormale;
    }


    /**
     * Sets the annotazioneFormale value for this AnnotazioniPropertiesType.
     * 
     * @param annotazioneFormale
     */
    public void setAnnotazioneFormale(boolean annotazioneFormale) {
        this.annotazioneFormale = annotazioneFormale;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof AnnotazioniPropertiesType)) return false;
        AnnotazioniPropertiesType other = (AnnotazioniPropertiesType) obj;

        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.objectTypeId==null && other.getObjectTypeId()==null) || 
             (this.objectTypeId!=null &&
              this.objectTypeId.equals(other.getObjectTypeId()))) &&
            ((this.descrizione==null && other.getDescrizione()==null) || 
             (this.descrizione!=null &&
              this.descrizione.equals(other.getDescrizione()))) &&
            ((this.idUtente==null && other.getIdUtente()==null) || 
             (this.idUtente!=null &&
              this.idUtente.equals(other.getIdUtente()))) &&
            ((this.data==null && other.getData()==null) || 
             (this.data!=null &&
              this.data.equals(other.getData()))) &&
            this.annotazioneFormale == other.isAnnotazioneFormale();
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
        if (getObjectTypeId() != null) {
            _hashCode += getObjectTypeId().hashCode();
        }
        if (getDescrizione() != null) {
            _hashCode += getDescrizione().hashCode();
        }
        if (getIdUtente() != null) {
            _hashCode += getIdUtente().hashCode();
        }
        if (getData() != null) {
            _hashCode += getData().hashCode();
        }
        _hashCode += (isAnnotazioneFormale() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(AnnotazioniPropertiesType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "AnnotazioniPropertiesType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("objectTypeId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "objectTypeId"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descrizione");
        elemField.setXmlName(new javax.xml.namespace.QName("", "descrizione"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idUtente");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idUtente"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "IdUtenteType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("data");
        elemField.setXmlName(new javax.xml.namespace.QName("", "data"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("annotazioneFormale");
        elemField.setXmlName(new javax.xml.namespace.QName("", "annotazioneFormale"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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

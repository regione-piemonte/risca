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
 * RegistrazioneArrivo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actaofficialbook;

public class RegistrazioneArrivo  extends it.csi.risca.riscabesrv.util.actaofficialbook.RegistrazioneAPI  implements java.io.Serializable {
    private it.csi.risca.riscabesrv.util.actaofficialbook.InfoDateArrivo infoDateArrivo;

    private it.csi.risca.riscabesrv.util.actaofficialbook.InfoProtocolloMittente infoProtocolloMittente;

    private it.csi.risca.riscabesrv.util.actaofficialbook.MittenteEsterno[] mittenteEsterno;

    public RegistrazioneArrivo() {
    }

    public RegistrazioneArrivo(
           java.lang.String tipoRegistrazione,
           it.csi.risca.riscabesrv.util.actaofficialbook.InfoCreazioneRegistrazione infoCreazione,
           it.csi.risca.riscabesrv.util.actaofficialbook.InfoDateArrivo infoDateArrivo,
           it.csi.risca.riscabesrv.util.actaofficialbook.InfoProtocolloMittente infoProtocolloMittente,
           it.csi.risca.riscabesrv.util.actaofficialbook.MittenteEsterno[] mittenteEsterno) {
        super(
            tipoRegistrazione,
            infoCreazione);
        this.infoDateArrivo = infoDateArrivo;
        this.infoProtocolloMittente = infoProtocolloMittente;
        this.mittenteEsterno = mittenteEsterno;
    }


    /**
     * Gets the infoDateArrivo value for this RegistrazioneArrivo.
     * 
     * @return infoDateArrivo
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.InfoDateArrivo getInfoDateArrivo() {
        return infoDateArrivo;
    }


    /**
     * Sets the infoDateArrivo value for this RegistrazioneArrivo.
     * 
     * @param infoDateArrivo
     */
    public void setInfoDateArrivo(it.csi.risca.riscabesrv.util.actaofficialbook.InfoDateArrivo infoDateArrivo) {
        this.infoDateArrivo = infoDateArrivo;
    }


    /**
     * Gets the infoProtocolloMittente value for this RegistrazioneArrivo.
     * 
     * @return infoProtocolloMittente
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.InfoProtocolloMittente getInfoProtocolloMittente() {
        return infoProtocolloMittente;
    }


    /**
     * Sets the infoProtocolloMittente value for this RegistrazioneArrivo.
     * 
     * @param infoProtocolloMittente
     */
    public void setInfoProtocolloMittente(it.csi.risca.riscabesrv.util.actaofficialbook.InfoProtocolloMittente infoProtocolloMittente) {
        this.infoProtocolloMittente = infoProtocolloMittente;
    }


    /**
     * Gets the mittenteEsterno value for this RegistrazioneArrivo.
     * 
     * @return mittenteEsterno
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.MittenteEsterno[] getMittenteEsterno() {
        return mittenteEsterno;
    }


    /**
     * Sets the mittenteEsterno value for this RegistrazioneArrivo.
     * 
     * @param mittenteEsterno
     */
    public void setMittenteEsterno(it.csi.risca.riscabesrv.util.actaofficialbook.MittenteEsterno[] mittenteEsterno) {
        this.mittenteEsterno = mittenteEsterno;
    }

    public it.csi.risca.riscabesrv.util.actaofficialbook.MittenteEsterno getMittenteEsterno(int i) {
        return this.mittenteEsterno[i];
    }

    public void setMittenteEsterno(int i, it.csi.risca.riscabesrv.util.actaofficialbook.MittenteEsterno _value) {
        this.mittenteEsterno[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof RegistrazioneArrivo)) return false;
        RegistrazioneArrivo other = (RegistrazioneArrivo) obj;

        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.infoDateArrivo==null && other.getInfoDateArrivo()==null) || 
             (this.infoDateArrivo!=null &&
              this.infoDateArrivo.equals(other.getInfoDateArrivo()))) &&
            ((this.infoProtocolloMittente==null && other.getInfoProtocolloMittente()==null) || 
             (this.infoProtocolloMittente!=null &&
              this.infoProtocolloMittente.equals(other.getInfoProtocolloMittente()))) &&
            ((this.mittenteEsterno==null && other.getMittenteEsterno()==null) || 
             (this.mittenteEsterno!=null &&
              java.util.Arrays.equals(this.mittenteEsterno, other.getMittenteEsterno())));
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
        if (getInfoDateArrivo() != null) {
            _hashCode += getInfoDateArrivo().hashCode();
        }
        if (getInfoProtocolloMittente() != null) {
            _hashCode += getInfoProtocolloMittente().hashCode();
        }
        if (getMittenteEsterno() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getMittenteEsterno());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getMittenteEsterno(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RegistrazioneArrivo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "RegistrazioneArrivo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("infoDateArrivo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "infoDateArrivo"));
        elemField.setXmlType(new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "InfoDateArrivo"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("infoProtocolloMittente");
        elemField.setXmlName(new javax.xml.namespace.QName("", "infoProtocolloMittente"));
        elemField.setXmlType(new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "InfoProtocolloMittente"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mittenteEsterno");
        elemField.setXmlName(new javax.xml.namespace.QName("", "mittenteEsterno"));
        elemField.setXmlType(new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "MittenteEsterno"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
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

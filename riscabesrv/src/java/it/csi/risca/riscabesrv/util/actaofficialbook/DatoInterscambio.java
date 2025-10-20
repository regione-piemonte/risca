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
 * DatoInterscambio.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actaofficialbook;

public class DatoInterscambio  implements java.io.Serializable {
    private it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType objectIdMessaggio;

    private it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType objectIdMessaggioInviato;

    private java.lang.String tipoMessaggio;

    private java.lang.String statoPecInviata;

    private java.lang.String oggettoMessaggio;

    private java.lang.String mittenteMessaggio;

    private java.lang.String destinatariMessaggio;

    private java.lang.String destinatariAccettato;

    private java.lang.String destinatariAvvenutaConsegna;

    private java.lang.String destinatariErroreConsegna;

    private java.lang.String perContoDi;

    private java.util.Calendar dataInvioMessaggio;

    private java.util.Calendar dataRicezioneMessaggio;

    private it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType identificatoreRegistrazione;

    public DatoInterscambio() {
    }

    public DatoInterscambio(
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType objectIdMessaggio,
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType objectIdMessaggioInviato,
           java.lang.String tipoMessaggio,
           java.lang.String statoPecInviata,
           java.lang.String oggettoMessaggio,
           java.lang.String mittenteMessaggio,
           java.lang.String destinatariMessaggio,
           java.lang.String destinatariAccettato,
           java.lang.String destinatariAvvenutaConsegna,
           java.lang.String destinatariErroreConsegna,
           java.lang.String perContoDi,
           java.util.Calendar dataInvioMessaggio,
           java.util.Calendar dataRicezioneMessaggio,
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType identificatoreRegistrazione) {
           this.objectIdMessaggio = objectIdMessaggio;
           this.objectIdMessaggioInviato = objectIdMessaggioInviato;
           this.tipoMessaggio = tipoMessaggio;
           this.statoPecInviata = statoPecInviata;
           this.oggettoMessaggio = oggettoMessaggio;
           this.mittenteMessaggio = mittenteMessaggio;
           this.destinatariMessaggio = destinatariMessaggio;
           this.destinatariAccettato = destinatariAccettato;
           this.destinatariAvvenutaConsegna = destinatariAvvenutaConsegna;
           this.destinatariErroreConsegna = destinatariErroreConsegna;
           this.perContoDi = perContoDi;
           this.dataInvioMessaggio = dataInvioMessaggio;
           this.dataRicezioneMessaggio = dataRicezioneMessaggio;
           this.identificatoreRegistrazione = identificatoreRegistrazione;
    }


    /**
     * Gets the objectIdMessaggio value for this DatoInterscambio.
     * 
     * @return objectIdMessaggio
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType getObjectIdMessaggio() {
        return objectIdMessaggio;
    }


    /**
     * Sets the objectIdMessaggio value for this DatoInterscambio.
     * 
     * @param objectIdMessaggio
     */
    public void setObjectIdMessaggio(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType objectIdMessaggio) {
        this.objectIdMessaggio = objectIdMessaggio;
    }


    /**
     * Gets the objectIdMessaggioInviato value for this DatoInterscambio.
     * 
     * @return objectIdMessaggioInviato
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType getObjectIdMessaggioInviato() {
        return objectIdMessaggioInviato;
    }


    /**
     * Sets the objectIdMessaggioInviato value for this DatoInterscambio.
     * 
     * @param objectIdMessaggioInviato
     */
    public void setObjectIdMessaggioInviato(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType objectIdMessaggioInviato) {
        this.objectIdMessaggioInviato = objectIdMessaggioInviato;
    }


    /**
     * Gets the tipoMessaggio value for this DatoInterscambio.
     * 
     * @return tipoMessaggio
     */
    public java.lang.String getTipoMessaggio() {
        return tipoMessaggio;
    }


    /**
     * Sets the tipoMessaggio value for this DatoInterscambio.
     * 
     * @param tipoMessaggio
     */
    public void setTipoMessaggio(java.lang.String tipoMessaggio) {
        this.tipoMessaggio = tipoMessaggio;
    }


    /**
     * Gets the statoPecInviata value for this DatoInterscambio.
     * 
     * @return statoPecInviata
     */
    public java.lang.String getStatoPecInviata() {
        return statoPecInviata;
    }


    /**
     * Sets the statoPecInviata value for this DatoInterscambio.
     * 
     * @param statoPecInviata
     */
    public void setStatoPecInviata(java.lang.String statoPecInviata) {
        this.statoPecInviata = statoPecInviata;
    }


    /**
     * Gets the oggettoMessaggio value for this DatoInterscambio.
     * 
     * @return oggettoMessaggio
     */
    public java.lang.String getOggettoMessaggio() {
        return oggettoMessaggio;
    }


    /**
     * Sets the oggettoMessaggio value for this DatoInterscambio.
     * 
     * @param oggettoMessaggio
     */
    public void setOggettoMessaggio(java.lang.String oggettoMessaggio) {
        this.oggettoMessaggio = oggettoMessaggio;
    }


    /**
     * Gets the mittenteMessaggio value for this DatoInterscambio.
     * 
     * @return mittenteMessaggio
     */
    public java.lang.String getMittenteMessaggio() {
        return mittenteMessaggio;
    }


    /**
     * Sets the mittenteMessaggio value for this DatoInterscambio.
     * 
     * @param mittenteMessaggio
     */
    public void setMittenteMessaggio(java.lang.String mittenteMessaggio) {
        this.mittenteMessaggio = mittenteMessaggio;
    }


    /**
     * Gets the destinatariMessaggio value for this DatoInterscambio.
     * 
     * @return destinatariMessaggio
     */
    public java.lang.String getDestinatariMessaggio() {
        return destinatariMessaggio;
    }


    /**
     * Sets the destinatariMessaggio value for this DatoInterscambio.
     * 
     * @param destinatariMessaggio
     */
    public void setDestinatariMessaggio(java.lang.String destinatariMessaggio) {
        this.destinatariMessaggio = destinatariMessaggio;
    }


    /**
     * Gets the destinatariAccettato value for this DatoInterscambio.
     * 
     * @return destinatariAccettato
     */
    public java.lang.String getDestinatariAccettato() {
        return destinatariAccettato;
    }


    /**
     * Sets the destinatariAccettato value for this DatoInterscambio.
     * 
     * @param destinatariAccettato
     */
    public void setDestinatariAccettato(java.lang.String destinatariAccettato) {
        this.destinatariAccettato = destinatariAccettato;
    }


    /**
     * Gets the destinatariAvvenutaConsegna value for this DatoInterscambio.
     * 
     * @return destinatariAvvenutaConsegna
     */
    public java.lang.String getDestinatariAvvenutaConsegna() {
        return destinatariAvvenutaConsegna;
    }


    /**
     * Sets the destinatariAvvenutaConsegna value for this DatoInterscambio.
     * 
     * @param destinatariAvvenutaConsegna
     */
    public void setDestinatariAvvenutaConsegna(java.lang.String destinatariAvvenutaConsegna) {
        this.destinatariAvvenutaConsegna = destinatariAvvenutaConsegna;
    }


    /**
     * Gets the destinatariErroreConsegna value for this DatoInterscambio.
     * 
     * @return destinatariErroreConsegna
     */
    public java.lang.String getDestinatariErroreConsegna() {
        return destinatariErroreConsegna;
    }


    /**
     * Sets the destinatariErroreConsegna value for this DatoInterscambio.
     * 
     * @param destinatariErroreConsegna
     */
    public void setDestinatariErroreConsegna(java.lang.String destinatariErroreConsegna) {
        this.destinatariErroreConsegna = destinatariErroreConsegna;
    }


    /**
     * Gets the perContoDi value for this DatoInterscambio.
     * 
     * @return perContoDi
     */
    public java.lang.String getPerContoDi() {
        return perContoDi;
    }


    /**
     * Sets the perContoDi value for this DatoInterscambio.
     * 
     * @param perContoDi
     */
    public void setPerContoDi(java.lang.String perContoDi) {
        this.perContoDi = perContoDi;
    }


    /**
     * Gets the dataInvioMessaggio value for this DatoInterscambio.
     * 
     * @return dataInvioMessaggio
     */
    public java.util.Calendar getDataInvioMessaggio() {
        return dataInvioMessaggio;
    }


    /**
     * Sets the dataInvioMessaggio value for this DatoInterscambio.
     * 
     * @param dataInvioMessaggio
     */
    public void setDataInvioMessaggio(java.util.Calendar dataInvioMessaggio) {
        this.dataInvioMessaggio = dataInvioMessaggio;
    }


    /**
     * Gets the dataRicezioneMessaggio value for this DatoInterscambio.
     * 
     * @return dataRicezioneMessaggio
     */
    public java.util.Calendar getDataRicezioneMessaggio() {
        return dataRicezioneMessaggio;
    }


    /**
     * Sets the dataRicezioneMessaggio value for this DatoInterscambio.
     * 
     * @param dataRicezioneMessaggio
     */
    public void setDataRicezioneMessaggio(java.util.Calendar dataRicezioneMessaggio) {
        this.dataRicezioneMessaggio = dataRicezioneMessaggio;
    }


    /**
     * Gets the identificatoreRegistrazione value for this DatoInterscambio.
     * 
     * @return identificatoreRegistrazione
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType getIdentificatoreRegistrazione() {
        return identificatoreRegistrazione;
    }


    /**
     * Sets the identificatoreRegistrazione value for this DatoInterscambio.
     * 
     * @param identificatoreRegistrazione
     */
    public void setIdentificatoreRegistrazione(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType identificatoreRegistrazione) {
        this.identificatoreRegistrazione = identificatoreRegistrazione;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof DatoInterscambio)) return false;
        DatoInterscambio other = (DatoInterscambio) obj;

        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.objectIdMessaggio==null && other.getObjectIdMessaggio()==null) || 
             (this.objectIdMessaggio!=null &&
              this.objectIdMessaggio.equals(other.getObjectIdMessaggio()))) &&
            ((this.objectIdMessaggioInviato==null && other.getObjectIdMessaggioInviato()==null) || 
             (this.objectIdMessaggioInviato!=null &&
              this.objectIdMessaggioInviato.equals(other.getObjectIdMessaggioInviato()))) &&
            ((this.tipoMessaggio==null && other.getTipoMessaggio()==null) || 
             (this.tipoMessaggio!=null &&
              this.tipoMessaggio.equals(other.getTipoMessaggio()))) &&
            ((this.statoPecInviata==null && other.getStatoPecInviata()==null) || 
             (this.statoPecInviata!=null &&
              this.statoPecInviata.equals(other.getStatoPecInviata()))) &&
            ((this.oggettoMessaggio==null && other.getOggettoMessaggio()==null) || 
             (this.oggettoMessaggio!=null &&
              this.oggettoMessaggio.equals(other.getOggettoMessaggio()))) &&
            ((this.mittenteMessaggio==null && other.getMittenteMessaggio()==null) || 
             (this.mittenteMessaggio!=null &&
              this.mittenteMessaggio.equals(other.getMittenteMessaggio()))) &&
            ((this.destinatariMessaggio==null && other.getDestinatariMessaggio()==null) || 
             (this.destinatariMessaggio!=null &&
              this.destinatariMessaggio.equals(other.getDestinatariMessaggio()))) &&
            ((this.destinatariAccettato==null && other.getDestinatariAccettato()==null) || 
             (this.destinatariAccettato!=null &&
              this.destinatariAccettato.equals(other.getDestinatariAccettato()))) &&
            ((this.destinatariAvvenutaConsegna==null && other.getDestinatariAvvenutaConsegna()==null) || 
             (this.destinatariAvvenutaConsegna!=null &&
              this.destinatariAvvenutaConsegna.equals(other.getDestinatariAvvenutaConsegna()))) &&
            ((this.destinatariErroreConsegna==null && other.getDestinatariErroreConsegna()==null) || 
             (this.destinatariErroreConsegna!=null &&
              this.destinatariErroreConsegna.equals(other.getDestinatariErroreConsegna()))) &&
            ((this.perContoDi==null && other.getPerContoDi()==null) || 
             (this.perContoDi!=null &&
              this.perContoDi.equals(other.getPerContoDi()))) &&
            ((this.dataInvioMessaggio==null && other.getDataInvioMessaggio()==null) || 
             (this.dataInvioMessaggio!=null &&
              this.dataInvioMessaggio.equals(other.getDataInvioMessaggio()))) &&
            ((this.dataRicezioneMessaggio==null && other.getDataRicezioneMessaggio()==null) || 
             (this.dataRicezioneMessaggio!=null &&
              this.dataRicezioneMessaggio.equals(other.getDataRicezioneMessaggio()))) &&
            ((this.identificatoreRegistrazione==null && other.getIdentificatoreRegistrazione()==null) || 
             (this.identificatoreRegistrazione!=null &&
              this.identificatoreRegistrazione.equals(other.getIdentificatoreRegistrazione())));
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
        if (getObjectIdMessaggio() != null) {
            _hashCode += getObjectIdMessaggio().hashCode();
        }
        if (getObjectIdMessaggioInviato() != null) {
            _hashCode += getObjectIdMessaggioInviato().hashCode();
        }
        if (getTipoMessaggio() != null) {
            _hashCode += getTipoMessaggio().hashCode();
        }
        if (getStatoPecInviata() != null) {
            _hashCode += getStatoPecInviata().hashCode();
        }
        if (getOggettoMessaggio() != null) {
            _hashCode += getOggettoMessaggio().hashCode();
        }
        if (getMittenteMessaggio() != null) {
            _hashCode += getMittenteMessaggio().hashCode();
        }
        if (getDestinatariMessaggio() != null) {
            _hashCode += getDestinatariMessaggio().hashCode();
        }
        if (getDestinatariAccettato() != null) {
            _hashCode += getDestinatariAccettato().hashCode();
        }
        if (getDestinatariAvvenutaConsegna() != null) {
            _hashCode += getDestinatariAvvenutaConsegna().hashCode();
        }
        if (getDestinatariErroreConsegna() != null) {
            _hashCode += getDestinatariErroreConsegna().hashCode();
        }
        if (getPerContoDi() != null) {
            _hashCode += getPerContoDi().hashCode();
        }
        if (getDataInvioMessaggio() != null) {
            _hashCode += getDataInvioMessaggio().hashCode();
        }
        if (getDataRicezioneMessaggio() != null) {
            _hashCode += getDataRicezioneMessaggio().hashCode();
        }
        if (getIdentificatoreRegistrazione() != null) {
            _hashCode += getIdentificatoreRegistrazione().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DatoInterscambio.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "DatoInterscambio"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("objectIdMessaggio");
        elemField.setXmlName(new javax.xml.namespace.QName("", "objectIdMessaggio"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("objectIdMessaggioInviato");
        elemField.setXmlName(new javax.xml.namespace.QName("", "objectIdMessaggioInviato"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoMessaggio");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tipoMessaggio"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("statoPecInviata");
        elemField.setXmlName(new javax.xml.namespace.QName("", "statoPecInviata"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("oggettoMessaggio");
        elemField.setXmlName(new javax.xml.namespace.QName("", "oggettoMessaggio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mittenteMessaggio");
        elemField.setXmlName(new javax.xml.namespace.QName("", "mittenteMessaggio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("destinatariMessaggio");
        elemField.setXmlName(new javax.xml.namespace.QName("", "destinatariMessaggio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("destinatariAccettato");
        elemField.setXmlName(new javax.xml.namespace.QName("", "destinatariAccettato"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("destinatariAvvenutaConsegna");
        elemField.setXmlName(new javax.xml.namespace.QName("", "destinatariAvvenutaConsegna"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("destinatariErroreConsegna");
        elemField.setXmlName(new javax.xml.namespace.QName("", "destinatariErroreConsegna"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("perContoDi");
        elemField.setXmlName(new javax.xml.namespace.QName("", "perContoDi"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataInvioMessaggio");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataInvioMessaggio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataRicezioneMessaggio");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataRicezioneMessaggio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("identificatoreRegistrazione");
        elemField.setXmlName(new javax.xml.namespace.QName("", "identificatoreRegistrazione"));
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

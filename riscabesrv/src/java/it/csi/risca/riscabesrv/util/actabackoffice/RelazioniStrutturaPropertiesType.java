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
 * RelazioniStrutturaPropertiesType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actabackoffice;

public class RelazioniStrutturaPropertiesType  implements java.io.Serializable {
    private it.csi.risca.riscabesrv.util.actabackoffice.ObjectIdType idStrutturaPartenza;

    private java.lang.String ruoloStrutturaPartenza;

    private it.csi.risca.riscabesrv.util.actabackoffice.ObjectIdType idStrutturaRelazionata;

    private it.csi.risca.riscabesrv.util.actabackoffice.ObjectIdType idParentStrutturaRelazionata;

    private java.lang.String idParentStrutturaRelazionataInChiaro;

    private int level;

    public RelazioniStrutturaPropertiesType() {
    }

    public RelazioniStrutturaPropertiesType(
           it.csi.risca.riscabesrv.util.actabackoffice.ObjectIdType idStrutturaPartenza,
           java.lang.String ruoloStrutturaPartenza,
           it.csi.risca.riscabesrv.util.actabackoffice.ObjectIdType idStrutturaRelazionata,
           it.csi.risca.riscabesrv.util.actabackoffice.ObjectIdType idParentStrutturaRelazionata,
           java.lang.String idParentStrutturaRelazionataInChiaro,
           int level) {
           this.idStrutturaPartenza = idStrutturaPartenza;
           this.ruoloStrutturaPartenza = ruoloStrutturaPartenza;
           this.idStrutturaRelazionata = idStrutturaRelazionata;
           this.idParentStrutturaRelazionata = idParentStrutturaRelazionata;
           this.idParentStrutturaRelazionataInChiaro = idParentStrutturaRelazionataInChiaro;
           this.level = level;
    }


    /**
     * Gets the idStrutturaPartenza value for this RelazioniStrutturaPropertiesType.
     * 
     * @return idStrutturaPartenza
     */
    public it.csi.risca.riscabesrv.util.actabackoffice.ObjectIdType getIdStrutturaPartenza() {
        return idStrutturaPartenza;
    }


    /**
     * Sets the idStrutturaPartenza value for this RelazioniStrutturaPropertiesType.
     * 
     * @param idStrutturaPartenza
     */
    public void setIdStrutturaPartenza(it.csi.risca.riscabesrv.util.actabackoffice.ObjectIdType idStrutturaPartenza) {
        this.idStrutturaPartenza = idStrutturaPartenza;
    }


    /**
     * Gets the ruoloStrutturaPartenza value for this RelazioniStrutturaPropertiesType.
     * 
     * @return ruoloStrutturaPartenza
     */
    public java.lang.String getRuoloStrutturaPartenza() {
        return ruoloStrutturaPartenza;
    }


    /**
     * Sets the ruoloStrutturaPartenza value for this RelazioniStrutturaPropertiesType.
     * 
     * @param ruoloStrutturaPartenza
     */
    public void setRuoloStrutturaPartenza(java.lang.String ruoloStrutturaPartenza) {
        this.ruoloStrutturaPartenza = ruoloStrutturaPartenza;
    }


    /**
     * Gets the idStrutturaRelazionata value for this RelazioniStrutturaPropertiesType.
     * 
     * @return idStrutturaRelazionata
     */
    public it.csi.risca.riscabesrv.util.actabackoffice.ObjectIdType getIdStrutturaRelazionata() {
        return idStrutturaRelazionata;
    }


    /**
     * Sets the idStrutturaRelazionata value for this RelazioniStrutturaPropertiesType.
     * 
     * @param idStrutturaRelazionata
     */
    public void setIdStrutturaRelazionata(it.csi.risca.riscabesrv.util.actabackoffice.ObjectIdType idStrutturaRelazionata) {
        this.idStrutturaRelazionata = idStrutturaRelazionata;
    }


    /**
     * Gets the idParentStrutturaRelazionata value for this RelazioniStrutturaPropertiesType.
     * 
     * @return idParentStrutturaRelazionata
     */
    public it.csi.risca.riscabesrv.util.actabackoffice.ObjectIdType getIdParentStrutturaRelazionata() {
        return idParentStrutturaRelazionata;
    }


    /**
     * Sets the idParentStrutturaRelazionata value for this RelazioniStrutturaPropertiesType.
     * 
     * @param idParentStrutturaRelazionata
     */
    public void setIdParentStrutturaRelazionata(it.csi.risca.riscabesrv.util.actabackoffice.ObjectIdType idParentStrutturaRelazionata) {
        this.idParentStrutturaRelazionata = idParentStrutturaRelazionata;
    }


    /**
     * Gets the idParentStrutturaRelazionataInChiaro value for this RelazioniStrutturaPropertiesType.
     * 
     * @return idParentStrutturaRelazionataInChiaro
     */
    public java.lang.String getIdParentStrutturaRelazionataInChiaro() {
        return idParentStrutturaRelazionataInChiaro;
    }


    /**
     * Sets the idParentStrutturaRelazionataInChiaro value for this RelazioniStrutturaPropertiesType.
     * 
     * @param idParentStrutturaRelazionataInChiaro
     */
    public void setIdParentStrutturaRelazionataInChiaro(java.lang.String idParentStrutturaRelazionataInChiaro) {
        this.idParentStrutturaRelazionataInChiaro = idParentStrutturaRelazionataInChiaro;
    }


    /**
     * Gets the level value for this RelazioniStrutturaPropertiesType.
     * 
     * @return level
     */
    public int getLevel() {
        return level;
    }


    /**
     * Sets the level value for this RelazioniStrutturaPropertiesType.
     * 
     * @param level
     */
    public void setLevel(int level) {
        this.level = level;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof RelazioniStrutturaPropertiesType)) return false;
        RelazioniStrutturaPropertiesType other = (RelazioniStrutturaPropertiesType) obj;

        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.idStrutturaPartenza==null && other.getIdStrutturaPartenza()==null) || 
             (this.idStrutturaPartenza!=null &&
              this.idStrutturaPartenza.equals(other.getIdStrutturaPartenza()))) &&
            ((this.ruoloStrutturaPartenza==null && other.getRuoloStrutturaPartenza()==null) || 
             (this.ruoloStrutturaPartenza!=null &&
              this.ruoloStrutturaPartenza.equals(other.getRuoloStrutturaPartenza()))) &&
            ((this.idStrutturaRelazionata==null && other.getIdStrutturaRelazionata()==null) || 
             (this.idStrutturaRelazionata!=null &&
              this.idStrutturaRelazionata.equals(other.getIdStrutturaRelazionata()))) &&
            ((this.idParentStrutturaRelazionata==null && other.getIdParentStrutturaRelazionata()==null) || 
             (this.idParentStrutturaRelazionata!=null &&
              this.idParentStrutturaRelazionata.equals(other.getIdParentStrutturaRelazionata()))) &&
            ((this.idParentStrutturaRelazionataInChiaro==null && other.getIdParentStrutturaRelazionataInChiaro()==null) || 
             (this.idParentStrutturaRelazionataInChiaro!=null &&
              this.idParentStrutturaRelazionataInChiaro.equals(other.getIdParentStrutturaRelazionataInChiaro()))) &&
            this.level == other.getLevel();
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
        if (getIdStrutturaPartenza() != null) {
            _hashCode += getIdStrutturaPartenza().hashCode();
        }
        if (getRuoloStrutturaPartenza() != null) {
            _hashCode += getRuoloStrutturaPartenza().hashCode();
        }
        if (getIdStrutturaRelazionata() != null) {
            _hashCode += getIdStrutturaRelazionata().hashCode();
        }
        if (getIdParentStrutturaRelazionata() != null) {
            _hashCode += getIdParentStrutturaRelazionata().hashCode();
        }
        if (getIdParentStrutturaRelazionataInChiaro() != null) {
            _hashCode += getIdParentStrutturaRelazionataInChiaro().hashCode();
        }
        _hashCode += getLevel();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RelazioniStrutturaPropertiesType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("backoffice.acaris.acta.doqui.it", "RelazioniStrutturaPropertiesType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idStrutturaPartenza");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idStrutturaPartenza"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ruoloStrutturaPartenza");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ruoloStrutturaPartenza"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idStrutturaRelazionata");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idStrutturaRelazionata"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idParentStrutturaRelazionata");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idParentStrutturaRelazionata"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idParentStrutturaRelazionataInChiaro");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idParentStrutturaRelazionataInChiaro"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("level");
        elemField.setXmlName(new javax.xml.namespace.QName("", "level"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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

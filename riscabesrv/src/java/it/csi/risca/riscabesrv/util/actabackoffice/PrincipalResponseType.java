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
 * PrincipalResponseType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actabackoffice;

public class PrincipalResponseType  implements java.io.Serializable {
    private it.csi.risca.riscabesrv.util.actabackoffice.PrincipalIdType idPrincipal;

    private it.csi.risca.riscabesrv.util.actabackoffice.IdAOOType idAOO;

    private it.csi.risca.riscabesrv.util.actabackoffice.IdStrutturaType idStruttura;

    private it.csi.risca.riscabesrv.util.actabackoffice.IdNodoType idNodo;

    private it.csi.risca.riscabesrv.util.actabackoffice.ProfiloPropertiesType[] profili;

    public PrincipalResponseType() {
    }

    public PrincipalResponseType(
           it.csi.risca.riscabesrv.util.actabackoffice.PrincipalIdType idPrincipal,
           it.csi.risca.riscabesrv.util.actabackoffice.IdAOOType idAOO,
           it.csi.risca.riscabesrv.util.actabackoffice.IdStrutturaType idStruttura,
           it.csi.risca.riscabesrv.util.actabackoffice.IdNodoType idNodo,
           it.csi.risca.riscabesrv.util.actabackoffice.ProfiloPropertiesType[] profili) {
           this.idPrincipal = idPrincipal;
           this.idAOO = idAOO;
           this.idStruttura = idStruttura;
           this.idNodo = idNodo;
           this.profili = profili;
    }


    /**
     * Gets the idPrincipal value for this PrincipalResponseType.
     * 
     * @return idPrincipal
     */
    public it.csi.risca.riscabesrv.util.actabackoffice.PrincipalIdType getIdPrincipal() {
        return idPrincipal;
    }


    /**
     * Sets the idPrincipal value for this PrincipalResponseType.
     * 
     * @param idPrincipal
     */
    public void setIdPrincipal(it.csi.risca.riscabesrv.util.actabackoffice.PrincipalIdType idPrincipal) {
        this.idPrincipal = idPrincipal;
    }


    /**
     * Gets the idAOO value for this PrincipalResponseType.
     * 
     * @return idAOO
     */
    public it.csi.risca.riscabesrv.util.actabackoffice.IdAOOType getIdAOO() {
        return idAOO;
    }


    /**
     * Sets the idAOO value for this PrincipalResponseType.
     * 
     * @param idAOO
     */
    public void setIdAOO(it.csi.risca.riscabesrv.util.actabackoffice.IdAOOType idAOO) {
        this.idAOO = idAOO;
    }


    /**
     * Gets the idStruttura value for this PrincipalResponseType.
     * 
     * @return idStruttura
     */
    public it.csi.risca.riscabesrv.util.actabackoffice.IdStrutturaType getIdStruttura() {
        return idStruttura;
    }


    /**
     * Sets the idStruttura value for this PrincipalResponseType.
     * 
     * @param idStruttura
     */
    public void setIdStruttura(it.csi.risca.riscabesrv.util.actabackoffice.IdStrutturaType idStruttura) {
        this.idStruttura = idStruttura;
    }


    /**
     * Gets the idNodo value for this PrincipalResponseType.
     * 
     * @return idNodo
     */
    public it.csi.risca.riscabesrv.util.actabackoffice.IdNodoType getIdNodo() {
        return idNodo;
    }


    /**
     * Sets the idNodo value for this PrincipalResponseType.
     * 
     * @param idNodo
     */
    public void setIdNodo(it.csi.risca.riscabesrv.util.actabackoffice.IdNodoType idNodo) {
        this.idNodo = idNodo;
    }


    /**
     * Gets the profili value for this PrincipalResponseType.
     * 
     * @return profili
     */
    public it.csi.risca.riscabesrv.util.actabackoffice.ProfiloPropertiesType[] getProfili() {
        return profili;
    }


    /**
     * Sets the profili value for this PrincipalResponseType.
     * 
     * @param profili
     */
    public void setProfili(it.csi.risca.riscabesrv.util.actabackoffice.ProfiloPropertiesType[] profili) {
        this.profili = profili;
    }

    public it.csi.risca.riscabesrv.util.actabackoffice.ProfiloPropertiesType getProfili(int i) {
        return this.profili[i];
    }

    public void setProfili(int i, it.csi.risca.riscabesrv.util.actabackoffice.ProfiloPropertiesType _value) {
        this.profili[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof PrincipalResponseType)) return false;
        PrincipalResponseType other = (PrincipalResponseType) obj;

        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.idPrincipal==null && other.getIdPrincipal()==null) || 
             (this.idPrincipal!=null &&
              this.idPrincipal.equals(other.getIdPrincipal()))) &&
            ((this.idAOO==null && other.getIdAOO()==null) || 
             (this.idAOO!=null &&
              this.idAOO.equals(other.getIdAOO()))) &&
            ((this.idStruttura==null && other.getIdStruttura()==null) || 
             (this.idStruttura!=null &&
              this.idStruttura.equals(other.getIdStruttura()))) &&
            ((this.idNodo==null && other.getIdNodo()==null) || 
             (this.idNodo!=null &&
              this.idNodo.equals(other.getIdNodo()))) &&
            ((this.profili==null && other.getProfili()==null) || 
             (this.profili!=null &&
              java.util.Arrays.equals(this.profili, other.getProfili())));
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
        if (getIdPrincipal() != null) {
            _hashCode += getIdPrincipal().hashCode();
        }
        if (getIdAOO() != null) {
            _hashCode += getIdAOO().hashCode();
        }
        if (getIdStruttura() != null) {
            _hashCode += getIdStruttura().hashCode();
        }
        if (getIdNodo() != null) {
            _hashCode += getIdNodo().hashCode();
        }
        if (getProfili() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getProfili());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getProfili(), i);
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
        new org.apache.axis.description.TypeDesc(PrincipalResponseType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("backoffice.acaris.acta.doqui.it", "PrincipalResponseType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idPrincipal");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idPrincipal"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "PrincipalIdType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idAOO");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idAOO"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "IdAOOType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idStruttura");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idStruttura"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "IdStrutturaType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idNodo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idNodo"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "IdNodoType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("profili");
        elemField.setXmlName(new javax.xml.namespace.QName("", "profili"));
        elemField.setXmlType(new javax.xml.namespace.QName("backoffice.acaris.acta.doqui.it", "ProfiloPropertiesType"));
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

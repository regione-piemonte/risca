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
 * ContenutoFisicoPropertiesType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actarelationships;

public class ContenutoFisicoPropertiesType  extends it.csi.risca.riscabesrv.util.actarelationships.DocumentPropertiesType  implements java.io.Serializable {
    private int numeroVersione;

    private java.lang.String modificheTecniche;

    private boolean workingCopy;

    private java.lang.String infoFormato;

    private java.lang.String[] rapportoVerifica;

    private it.csi.risca.riscabesrv.util.actarelationships.DatiCertificatoType[] datiCertificato;

    private boolean sbustamento;

    private boolean modificabile;

    private boolean docPrimario;

    private java.lang.String mimeTypeBusta;

    public ContenutoFisicoPropertiesType() {
    }

    public ContenutoFisicoPropertiesType(
           it.csi.risca.riscabesrv.util.actarelationships.ObjectIdType objectId,
           it.csi.risca.riscabesrv.util.actarelationships.ChangeTokenType changeToken,
           java.lang.String objectTypeId,
           int contentStreamLength,
           java.lang.String contentStreamMimeType,
           java.lang.String contentStreamFilename,
           int numeroVersione,
           java.lang.String modificheTecniche,
           boolean workingCopy,
           java.lang.String infoFormato,
           java.lang.String[] rapportoVerifica,
           it.csi.risca.riscabesrv.util.actarelationships.DatiCertificatoType[] datiCertificato,
           boolean sbustamento,
           boolean modificabile,
           boolean docPrimario,
           java.lang.String mimeTypeBusta) {
        super(
            objectId,
            changeToken,
            objectTypeId,
            contentStreamLength,
            contentStreamMimeType,
            contentStreamFilename);
        this.numeroVersione = numeroVersione;
        this.modificheTecniche = modificheTecniche;
        this.workingCopy = workingCopy;
        this.infoFormato = infoFormato;
        this.rapportoVerifica = rapportoVerifica;
        this.datiCertificato = datiCertificato;
        this.sbustamento = sbustamento;
        this.modificabile = modificabile;
        this.docPrimario = docPrimario;
        this.mimeTypeBusta = mimeTypeBusta;
    }


    /**
     * Gets the numeroVersione value for this ContenutoFisicoPropertiesType.
     * 
     * @return numeroVersione
     */
    public int getNumeroVersione() {
        return numeroVersione;
    }


    /**
     * Sets the numeroVersione value for this ContenutoFisicoPropertiesType.
     * 
     * @param numeroVersione
     */
    public void setNumeroVersione(int numeroVersione) {
        this.numeroVersione = numeroVersione;
    }


    /**
     * Gets the modificheTecniche value for this ContenutoFisicoPropertiesType.
     * 
     * @return modificheTecniche
     */
    public java.lang.String getModificheTecniche() {
        return modificheTecniche;
    }


    /**
     * Sets the modificheTecniche value for this ContenutoFisicoPropertiesType.
     * 
     * @param modificheTecniche
     */
    public void setModificheTecniche(java.lang.String modificheTecniche) {
        this.modificheTecniche = modificheTecniche;
    }


    /**
     * Gets the workingCopy value for this ContenutoFisicoPropertiesType.
     * 
     * @return workingCopy
     */
    public boolean isWorkingCopy() {
        return workingCopy;
    }


    /**
     * Sets the workingCopy value for this ContenutoFisicoPropertiesType.
     * 
     * @param workingCopy
     */
    public void setWorkingCopy(boolean workingCopy) {
        this.workingCopy = workingCopy;
    }


    /**
     * Gets the infoFormato value for this ContenutoFisicoPropertiesType.
     * 
     * @return infoFormato
     */
    public java.lang.String getInfoFormato() {
        return infoFormato;
    }


    /**
     * Sets the infoFormato value for this ContenutoFisicoPropertiesType.
     * 
     * @param infoFormato
     */
    public void setInfoFormato(java.lang.String infoFormato) {
        this.infoFormato = infoFormato;
    }


    /**
     * Gets the rapportoVerifica value for this ContenutoFisicoPropertiesType.
     * 
     * @return rapportoVerifica
     */
    public java.lang.String[] getRapportoVerifica() {
        return rapportoVerifica;
    }


    /**
     * Sets the rapportoVerifica value for this ContenutoFisicoPropertiesType.
     * 
     * @param rapportoVerifica
     */
    public void setRapportoVerifica(java.lang.String[] rapportoVerifica) {
        this.rapportoVerifica = rapportoVerifica;
    }

    public java.lang.String getRapportoVerifica(int i) {
        return this.rapportoVerifica[i];
    }

    public void setRapportoVerifica(int i, java.lang.String _value) {
        this.rapportoVerifica[i] = _value;
    }


    /**
     * Gets the datiCertificato value for this ContenutoFisicoPropertiesType.
     * 
     * @return datiCertificato
     */
    public it.csi.risca.riscabesrv.util.actarelationships.DatiCertificatoType[] getDatiCertificato() {
        return datiCertificato;
    }


    /**
     * Sets the datiCertificato value for this ContenutoFisicoPropertiesType.
     * 
     * @param datiCertificato
     */
    public void setDatiCertificato(it.csi.risca.riscabesrv.util.actarelationships.DatiCertificatoType[] datiCertificato) {
        this.datiCertificato = datiCertificato;
    }

    public it.csi.risca.riscabesrv.util.actarelationships.DatiCertificatoType getDatiCertificato(int i) {
        return this.datiCertificato[i];
    }

    public void setDatiCertificato(int i, it.csi.risca.riscabesrv.util.actarelationships.DatiCertificatoType _value) {
        this.datiCertificato[i] = _value;
    }


    /**
     * Gets the sbustamento value for this ContenutoFisicoPropertiesType.
     * 
     * @return sbustamento
     */
    public boolean isSbustamento() {
        return sbustamento;
    }


    /**
     * Sets the sbustamento value for this ContenutoFisicoPropertiesType.
     * 
     * @param sbustamento
     */
    public void setSbustamento(boolean sbustamento) {
        this.sbustamento = sbustamento;
    }


    /**
     * Gets the modificabile value for this ContenutoFisicoPropertiesType.
     * 
     * @return modificabile
     */
    public boolean isModificabile() {
        return modificabile;
    }


    /**
     * Sets the modificabile value for this ContenutoFisicoPropertiesType.
     * 
     * @param modificabile
     */
    public void setModificabile(boolean modificabile) {
        this.modificabile = modificabile;
    }


    /**
     * Gets the docPrimario value for this ContenutoFisicoPropertiesType.
     * 
     * @return docPrimario
     */
    public boolean isDocPrimario() {
        return docPrimario;
    }


    /**
     * Sets the docPrimario value for this ContenutoFisicoPropertiesType.
     * 
     * @param docPrimario
     */
    public void setDocPrimario(boolean docPrimario) {
        this.docPrimario = docPrimario;
    }


    /**
     * Gets the mimeTypeBusta value for this ContenutoFisicoPropertiesType.
     * 
     * @return mimeTypeBusta
     */
    public java.lang.String getMimeTypeBusta() {
        return mimeTypeBusta;
    }


    /**
     * Sets the mimeTypeBusta value for this ContenutoFisicoPropertiesType.
     * 
     * @param mimeTypeBusta
     */
    public void setMimeTypeBusta(java.lang.String mimeTypeBusta) {
        this.mimeTypeBusta = mimeTypeBusta;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof ContenutoFisicoPropertiesType)) return false;
        ContenutoFisicoPropertiesType other = (ContenutoFisicoPropertiesType) obj;

        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            this.numeroVersione == other.getNumeroVersione() &&
            ((this.modificheTecniche==null && other.getModificheTecniche()==null) || 
             (this.modificheTecniche!=null &&
              this.modificheTecniche.equals(other.getModificheTecniche()))) &&
            this.workingCopy == other.isWorkingCopy() &&
            ((this.infoFormato==null && other.getInfoFormato()==null) || 
             (this.infoFormato!=null &&
              this.infoFormato.equals(other.getInfoFormato()))) &&
            ((this.rapportoVerifica==null && other.getRapportoVerifica()==null) || 
             (this.rapportoVerifica!=null &&
              java.util.Arrays.equals(this.rapportoVerifica, other.getRapportoVerifica()))) &&
            ((this.datiCertificato==null && other.getDatiCertificato()==null) || 
             (this.datiCertificato!=null &&
              java.util.Arrays.equals(this.datiCertificato, other.getDatiCertificato()))) &&
            this.sbustamento == other.isSbustamento() &&
            this.modificabile == other.isModificabile() &&
            this.docPrimario == other.isDocPrimario() &&
            ((this.mimeTypeBusta==null && other.getMimeTypeBusta()==null) || 
             (this.mimeTypeBusta!=null &&
              this.mimeTypeBusta.equals(other.getMimeTypeBusta())));
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
        _hashCode += getNumeroVersione();
        if (getModificheTecniche() != null) {
            _hashCode += getModificheTecniche().hashCode();
        }
        _hashCode += (isWorkingCopy() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getInfoFormato() != null) {
            _hashCode += getInfoFormato().hashCode();
        }
        if (getRapportoVerifica() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getRapportoVerifica());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getRapportoVerifica(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getDatiCertificato() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDatiCertificato());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDatiCertificato(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        _hashCode += (isSbustamento() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isModificabile() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isDocPrimario() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getMimeTypeBusta() != null) {
            _hashCode += getMimeTypeBusta().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ContenutoFisicoPropertiesType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "ContenutoFisicoPropertiesType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numeroVersione");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "numeroVersione"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "integer"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("modificheTecniche");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "modificheTecniche"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("workingCopy");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "workingCopy"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("infoFormato");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "infoFormato"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "XMLType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("rapportoVerifica");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "rapportoVerifica"));
        elemField.setXmlType(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "RapportoVerificaXMLType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("datiCertificato");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "datiCertificato"));
        elemField.setXmlType(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "DatiCertificatoType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sbustamento");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "sbustamento"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("modificabile");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "modificabile"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("docPrimario");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "docPrimario"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mimeTypeBusta");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "mimeTypeBusta"));
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

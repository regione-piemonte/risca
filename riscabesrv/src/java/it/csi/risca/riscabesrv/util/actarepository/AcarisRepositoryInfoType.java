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
 * AcarisRepositoryInfoType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actarepository;

public class AcarisRepositoryInfoType  implements java.io.Serializable {
    private it.csi.risca.riscabesrv.util.actarepository.ObjectIdType repositoryId;

    private java.lang.String repositoryName;

    private java.lang.String repositoryURI;

    private java.lang.String repositoryDescription;

    private it.csi.risca.riscabesrv.util.actarepository.ObjectIdType rootFolderId;

    private it.csi.risca.riscabesrv.util.actarepository.ObjectIdType documentRootFolderId;

    private int numeroMassimoRisultati;

    public AcarisRepositoryInfoType() {
    }

    public AcarisRepositoryInfoType(
           it.csi.risca.riscabesrv.util.actarepository.ObjectIdType repositoryId,
           java.lang.String repositoryName,
           java.lang.String repositoryURI,
           java.lang.String repositoryDescription,
           it.csi.risca.riscabesrv.util.actarepository.ObjectIdType rootFolderId,
           it.csi.risca.riscabesrv.util.actarepository.ObjectIdType documentRootFolderId,
           int numeroMassimoRisultati) {
           this.repositoryId = repositoryId;
           this.repositoryName = repositoryName;
           this.repositoryURI = repositoryURI;
           this.repositoryDescription = repositoryDescription;
           this.rootFolderId = rootFolderId;
           this.documentRootFolderId = documentRootFolderId;
           this.numeroMassimoRisultati = numeroMassimoRisultati;
    }


    /**
     * Gets the repositoryId value for this AcarisRepositoryInfoType.
     * 
     * @return repositoryId
     */
    public it.csi.risca.riscabesrv.util.actarepository.ObjectIdType getRepositoryId() {
        return repositoryId;
    }


    /**
     * Sets the repositoryId value for this AcarisRepositoryInfoType.
     * 
     * @param repositoryId
     */
    public void setRepositoryId(it.csi.risca.riscabesrv.util.actarepository.ObjectIdType repositoryId) {
        this.repositoryId = repositoryId;
    }


    /**
     * Gets the repositoryName value for this AcarisRepositoryInfoType.
     * 
     * @return repositoryName
     */
    public java.lang.String getRepositoryName() {
        return repositoryName;
    }


    /**
     * Sets the repositoryName value for this AcarisRepositoryInfoType.
     * 
     * @param repositoryName
     */
    public void setRepositoryName(java.lang.String repositoryName) {
        this.repositoryName = repositoryName;
    }


    /**
     * Gets the repositoryURI value for this AcarisRepositoryInfoType.
     * 
     * @return repositoryURI
     */
    public java.lang.String getRepositoryURI() {
        return repositoryURI;
    }


    /**
     * Sets the repositoryURI value for this AcarisRepositoryInfoType.
     * 
     * @param repositoryURI
     */
    public void setRepositoryURI(java.lang.String repositoryURI) {
        this.repositoryURI = repositoryURI;
    }


    /**
     * Gets the repositoryDescription value for this AcarisRepositoryInfoType.
     * 
     * @return repositoryDescription
     */
    public java.lang.String getRepositoryDescription() {
        return repositoryDescription;
    }


    /**
     * Sets the repositoryDescription value for this AcarisRepositoryInfoType.
     * 
     * @param repositoryDescription
     */
    public void setRepositoryDescription(java.lang.String repositoryDescription) {
        this.repositoryDescription = repositoryDescription;
    }


    /**
     * Gets the rootFolderId value for this AcarisRepositoryInfoType.
     * 
     * @return rootFolderId
     */
    public it.csi.risca.riscabesrv.util.actarepository.ObjectIdType getRootFolderId() {
        return rootFolderId;
    }


    /**
     * Sets the rootFolderId value for this AcarisRepositoryInfoType.
     * 
     * @param rootFolderId
     */
    public void setRootFolderId(it.csi.risca.riscabesrv.util.actarepository.ObjectIdType rootFolderId) {
        this.rootFolderId = rootFolderId;
    }


    /**
     * Gets the documentRootFolderId value for this AcarisRepositoryInfoType.
     * 
     * @return documentRootFolderId
     */
    public it.csi.risca.riscabesrv.util.actarepository.ObjectIdType getDocumentRootFolderId() {
        return documentRootFolderId;
    }


    /**
     * Sets the documentRootFolderId value for this AcarisRepositoryInfoType.
     * 
     * @param documentRootFolderId
     */
    public void setDocumentRootFolderId(it.csi.risca.riscabesrv.util.actarepository.ObjectIdType documentRootFolderId) {
        this.documentRootFolderId = documentRootFolderId;
    }


    /**
     * Gets the numeroMassimoRisultati value for this AcarisRepositoryInfoType.
     * 
     * @return numeroMassimoRisultati
     */
    public int getNumeroMassimoRisultati() {
        return numeroMassimoRisultati;
    }


    /**
     * Sets the numeroMassimoRisultati value for this AcarisRepositoryInfoType.
     * 
     * @param numeroMassimoRisultati
     */
    public void setNumeroMassimoRisultati(int numeroMassimoRisultati) {
        this.numeroMassimoRisultati = numeroMassimoRisultati;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof AcarisRepositoryInfoType)) return false;
        AcarisRepositoryInfoType other = (AcarisRepositoryInfoType) obj;

        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.repositoryId==null && other.getRepositoryId()==null) || 
             (this.repositoryId!=null &&
              this.repositoryId.equals(other.getRepositoryId()))) &&
            ((this.repositoryName==null && other.getRepositoryName()==null) || 
             (this.repositoryName!=null &&
              this.repositoryName.equals(other.getRepositoryName()))) &&
            ((this.repositoryURI==null && other.getRepositoryURI()==null) || 
             (this.repositoryURI!=null &&
              this.repositoryURI.equals(other.getRepositoryURI()))) &&
            ((this.repositoryDescription==null && other.getRepositoryDescription()==null) || 
             (this.repositoryDescription!=null &&
              this.repositoryDescription.equals(other.getRepositoryDescription()))) &&
            ((this.rootFolderId==null && other.getRootFolderId()==null) || 
             (this.rootFolderId!=null &&
              this.rootFolderId.equals(other.getRootFolderId()))) &&
            ((this.documentRootFolderId==null && other.getDocumentRootFolderId()==null) || 
             (this.documentRootFolderId!=null &&
              this.documentRootFolderId.equals(other.getDocumentRootFolderId()))) &&
            this.numeroMassimoRisultati == other.getNumeroMassimoRisultati();
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
        if (getRepositoryId() != null) {
            _hashCode += getRepositoryId().hashCode();
        }
        if (getRepositoryName() != null) {
            _hashCode += getRepositoryName().hashCode();
        }
        if (getRepositoryURI() != null) {
            _hashCode += getRepositoryURI().hashCode();
        }
        if (getRepositoryDescription() != null) {
            _hashCode += getRepositoryDescription().hashCode();
        }
        if (getRootFolderId() != null) {
            _hashCode += getRootFolderId().hashCode();
        }
        if (getDocumentRootFolderId() != null) {
            _hashCode += getDocumentRootFolderId().hashCode();
        }
        _hashCode += getNumeroMassimoRisultati();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(AcarisRepositoryInfoType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "acarisRepositoryInfoType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("repositoryId");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "repositoryId"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("repositoryName");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "repositoryName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("repositoryURI");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "repositoryURI"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("repositoryDescription");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "repositoryDescription"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("rootFolderId");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "rootFolderId"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("documentRootFolderId");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "documentRootFolderId"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numeroMassimoRisultati");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "numeroMassimoRisultati"));
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

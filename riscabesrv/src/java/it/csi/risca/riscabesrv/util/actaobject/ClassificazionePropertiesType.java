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
 * ClassificazionePropertiesType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actaobject;

public class ClassificazionePropertiesType  extends it.csi.risca.riscabesrv.util.actaobject.FolderPropertiesType  implements java.io.Serializable {
    private int numero;

    private java.lang.String codice;

    private java.lang.String numeroInput;

    private java.lang.String stato;

    private java.util.Date dataInizio;

    private java.util.Date dataFine;

    private it.csi.risca.riscabesrv.util.actaobject.CodiceFiscaleType utenteCreazione;

    private java.lang.String collocazioneCartacea;

    private boolean copiaCartacea;

    private boolean cartaceo;

    private java.lang.String legislatura;

    private it.csi.risca.riscabesrv.util.actaobject.IdAnnotazioniType[] idAnnotazioniList;

    private it.csi.risca.riscabesrv.util.actaobject.IdMovimentazioneType[] idMovimentazioneList;

    private int idStatoDocumento;

    private int idTipoClasse;

    public ClassificazionePropertiesType() {
    }

    public ClassificazionePropertiesType(
           it.csi.risca.riscabesrv.util.actaobject.ObjectIdType objectId,
           it.csi.risca.riscabesrv.util.actaobject.ChangeTokenType changeToken,
           java.lang.String objectTypeId,
           it.csi.risca.riscabesrv.util.actaobject.ObjectIdType parentId,
           java.lang.String[] allowedChildObjectTypeIds,
           int numero,
           java.lang.String codice,
           java.lang.String numeroInput,
           java.lang.String stato,
           java.util.Date dataInizio,
           java.util.Date dataFine,
           it.csi.risca.riscabesrv.util.actaobject.CodiceFiscaleType utenteCreazione,
           java.lang.String collocazioneCartacea,
           boolean copiaCartacea,
           boolean cartaceo,
           java.lang.String legislatura,
           it.csi.risca.riscabesrv.util.actaobject.IdAnnotazioniType[] idAnnotazioniList,
           it.csi.risca.riscabesrv.util.actaobject.IdMovimentazioneType[] idMovimentazioneList,
           int idStatoDocumento,
           int idTipoClasse) {
        super(
            objectId,
            changeToken,
            objectTypeId,
            parentId,
            allowedChildObjectTypeIds);
        this.numero = numero;
        this.codice = codice;
        this.numeroInput = numeroInput;
        this.stato = stato;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.utenteCreazione = utenteCreazione;
        this.collocazioneCartacea = collocazioneCartacea;
        this.copiaCartacea = copiaCartacea;
        this.cartaceo = cartaceo;
        this.legislatura = legislatura;
        this.idAnnotazioniList = idAnnotazioniList;
        this.idMovimentazioneList = idMovimentazioneList;
        this.idStatoDocumento = idStatoDocumento;
        this.idTipoClasse = idTipoClasse;
    }


    /**
     * Gets the numero value for this ClassificazionePropertiesType.
     * 
     * @return numero
     */
    public int getNumero() {
        return numero;
    }


    /**
     * Sets the numero value for this ClassificazionePropertiesType.
     * 
     * @param numero
     */
    public void setNumero(int numero) {
        this.numero = numero;
    }


    /**
     * Gets the codice value for this ClassificazionePropertiesType.
     * 
     * @return codice
     */
    public java.lang.String getCodice() {
        return codice;
    }


    /**
     * Sets the codice value for this ClassificazionePropertiesType.
     * 
     * @param codice
     */
    public void setCodice(java.lang.String codice) {
        this.codice = codice;
    }


    /**
     * Gets the numeroInput value for this ClassificazionePropertiesType.
     * 
     * @return numeroInput
     */
    public java.lang.String getNumeroInput() {
        return numeroInput;
    }


    /**
     * Sets the numeroInput value for this ClassificazionePropertiesType.
     * 
     * @param numeroInput
     */
    public void setNumeroInput(java.lang.String numeroInput) {
        this.numeroInput = numeroInput;
    }


    /**
     * Gets the stato value for this ClassificazionePropertiesType.
     * 
     * @return stato
     */
    public java.lang.String getStato() {
        return stato;
    }


    /**
     * Sets the stato value for this ClassificazionePropertiesType.
     * 
     * @param stato
     */
    public void setStato(java.lang.String stato) {
        this.stato = stato;
    }


    /**
     * Gets the dataInizio value for this ClassificazionePropertiesType.
     * 
     * @return dataInizio
     */
    public java.util.Date getDataInizio() {
        return dataInizio;
    }


    /**
     * Sets the dataInizio value for this ClassificazionePropertiesType.
     * 
     * @param dataInizio
     */
    public void setDataInizio(java.util.Date dataInizio) {
        this.dataInizio = dataInizio;
    }


    /**
     * Gets the dataFine value for this ClassificazionePropertiesType.
     * 
     * @return dataFine
     */
    public java.util.Date getDataFine() {
        return dataFine;
    }


    /**
     * Sets the dataFine value for this ClassificazionePropertiesType.
     * 
     * @param dataFine
     */
    public void setDataFine(java.util.Date dataFine) {
        this.dataFine = dataFine;
    }


    /**
     * Gets the utenteCreazione value for this ClassificazionePropertiesType.
     * 
     * @return utenteCreazione
     */
    public it.csi.risca.riscabesrv.util.actaobject.CodiceFiscaleType getUtenteCreazione() {
        return utenteCreazione;
    }


    /**
     * Sets the utenteCreazione value for this ClassificazionePropertiesType.
     * 
     * @param utenteCreazione
     */
    public void setUtenteCreazione(it.csi.risca.riscabesrv.util.actaobject.CodiceFiscaleType utenteCreazione) {
        this.utenteCreazione = utenteCreazione;
    }


    /**
     * Gets the collocazioneCartacea value for this ClassificazionePropertiesType.
     * 
     * @return collocazioneCartacea
     */
    public java.lang.String getCollocazioneCartacea() {
        return collocazioneCartacea;
    }


    /**
     * Sets the collocazioneCartacea value for this ClassificazionePropertiesType.
     * 
     * @param collocazioneCartacea
     */
    public void setCollocazioneCartacea(java.lang.String collocazioneCartacea) {
        this.collocazioneCartacea = collocazioneCartacea;
    }


    /**
     * Gets the copiaCartacea value for this ClassificazionePropertiesType.
     * 
     * @return copiaCartacea
     */
    public boolean isCopiaCartacea() {
        return copiaCartacea;
    }


    /**
     * Sets the copiaCartacea value for this ClassificazionePropertiesType.
     * 
     * @param copiaCartacea
     */
    public void setCopiaCartacea(boolean copiaCartacea) {
        this.copiaCartacea = copiaCartacea;
    }


    /**
     * Gets the cartaceo value for this ClassificazionePropertiesType.
     * 
     * @return cartaceo
     */
    public boolean isCartaceo() {
        return cartaceo;
    }


    /**
     * Sets the cartaceo value for this ClassificazionePropertiesType.
     * 
     * @param cartaceo
     */
    public void setCartaceo(boolean cartaceo) {
        this.cartaceo = cartaceo;
    }


    /**
     * Gets the legislatura value for this ClassificazionePropertiesType.
     * 
     * @return legislatura
     */
    public java.lang.String getLegislatura() {
        return legislatura;
    }


    /**
     * Sets the legislatura value for this ClassificazionePropertiesType.
     * 
     * @param legislatura
     */
    public void setLegislatura(java.lang.String legislatura) {
        this.legislatura = legislatura;
    }


    /**
     * Gets the idAnnotazioniList value for this ClassificazionePropertiesType.
     * 
     * @return idAnnotazioniList
     */
    public it.csi.risca.riscabesrv.util.actaobject.IdAnnotazioniType[] getIdAnnotazioniList() {
        return idAnnotazioniList;
    }


    /**
     * Sets the idAnnotazioniList value for this ClassificazionePropertiesType.
     * 
     * @param idAnnotazioniList
     */
    public void setIdAnnotazioniList(it.csi.risca.riscabesrv.util.actaobject.IdAnnotazioniType[] idAnnotazioniList) {
        this.idAnnotazioniList = idAnnotazioniList;
    }

    public it.csi.risca.riscabesrv.util.actaobject.IdAnnotazioniType getIdAnnotazioniList(int i) {
        return this.idAnnotazioniList[i];
    }

    public void setIdAnnotazioniList(int i, it.csi.risca.riscabesrv.util.actaobject.IdAnnotazioniType _value) {
        this.idAnnotazioniList[i] = _value;
    }


    /**
     * Gets the idMovimentazioneList value for this ClassificazionePropertiesType.
     * 
     * @return idMovimentazioneList
     */
    public it.csi.risca.riscabesrv.util.actaobject.IdMovimentazioneType[] getIdMovimentazioneList() {
        return idMovimentazioneList;
    }


    /**
     * Sets the idMovimentazioneList value for this ClassificazionePropertiesType.
     * 
     * @param idMovimentazioneList
     */
    public void setIdMovimentazioneList(it.csi.risca.riscabesrv.util.actaobject.IdMovimentazioneType[] idMovimentazioneList) {
        this.idMovimentazioneList = idMovimentazioneList;
    }

    public it.csi.risca.riscabesrv.util.actaobject.IdMovimentazioneType getIdMovimentazioneList(int i) {
        return this.idMovimentazioneList[i];
    }

    public void setIdMovimentazioneList(int i, it.csi.risca.riscabesrv.util.actaobject.IdMovimentazioneType _value) {
        this.idMovimentazioneList[i] = _value;
    }


    /**
     * Gets the idStatoDocumento value for this ClassificazionePropertiesType.
     * 
     * @return idStatoDocumento
     */
    public int getIdStatoDocumento() {
        return idStatoDocumento;
    }


    /**
     * Sets the idStatoDocumento value for this ClassificazionePropertiesType.
     * 
     * @param idStatoDocumento
     */
    public void setIdStatoDocumento(int idStatoDocumento) {
        this.idStatoDocumento = idStatoDocumento;
    }


    /**
     * Gets the idTipoClasse value for this ClassificazionePropertiesType.
     * 
     * @return idTipoClasse
     */
    public int getIdTipoClasse() {
        return idTipoClasse;
    }


    /**
     * Sets the idTipoClasse value for this ClassificazionePropertiesType.
     * 
     * @param idTipoClasse
     */
    public void setIdTipoClasse(int idTipoClasse) {
        this.idTipoClasse = idTipoClasse;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof ClassificazionePropertiesType)) return false;
        ClassificazionePropertiesType other = (ClassificazionePropertiesType) obj;

        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            this.numero == other.getNumero() &&
            ((this.codice==null && other.getCodice()==null) || 
             (this.codice!=null &&
              this.codice.equals(other.getCodice()))) &&
            ((this.numeroInput==null && other.getNumeroInput()==null) || 
             (this.numeroInput!=null &&
              this.numeroInput.equals(other.getNumeroInput()))) &&
            ((this.stato==null && other.getStato()==null) || 
             (this.stato!=null &&
              this.stato.equals(other.getStato()))) &&
            ((this.dataInizio==null && other.getDataInizio()==null) || 
             (this.dataInizio!=null &&
              this.dataInizio.equals(other.getDataInizio()))) &&
            ((this.dataFine==null && other.getDataFine()==null) || 
             (this.dataFine!=null &&
              this.dataFine.equals(other.getDataFine()))) &&
            ((this.utenteCreazione==null && other.getUtenteCreazione()==null) || 
             (this.utenteCreazione!=null &&
              this.utenteCreazione.equals(other.getUtenteCreazione()))) &&
            ((this.collocazioneCartacea==null && other.getCollocazioneCartacea()==null) || 
             (this.collocazioneCartacea!=null &&
              this.collocazioneCartacea.equals(other.getCollocazioneCartacea()))) &&
            this.copiaCartacea == other.isCopiaCartacea() &&
            this.cartaceo == other.isCartaceo() &&
            ((this.legislatura==null && other.getLegislatura()==null) || 
             (this.legislatura!=null &&
              this.legislatura.equals(other.getLegislatura()))) &&
            ((this.idAnnotazioniList==null && other.getIdAnnotazioniList()==null) || 
             (this.idAnnotazioniList!=null &&
              java.util.Arrays.equals(this.idAnnotazioniList, other.getIdAnnotazioniList()))) &&
            ((this.idMovimentazioneList==null && other.getIdMovimentazioneList()==null) || 
             (this.idMovimentazioneList!=null &&
              java.util.Arrays.equals(this.idMovimentazioneList, other.getIdMovimentazioneList()))) &&
            this.idStatoDocumento == other.getIdStatoDocumento() &&
            this.idTipoClasse == other.getIdTipoClasse();
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
        _hashCode += getNumero();
        if (getCodice() != null) {
            _hashCode += getCodice().hashCode();
        }
        if (getNumeroInput() != null) {
            _hashCode += getNumeroInput().hashCode();
        }
        if (getStato() != null) {
            _hashCode += getStato().hashCode();
        }
        if (getDataInizio() != null) {
            _hashCode += getDataInizio().hashCode();
        }
        if (getDataFine() != null) {
            _hashCode += getDataFine().hashCode();
        }
        if (getUtenteCreazione() != null) {
            _hashCode += getUtenteCreazione().hashCode();
        }
        if (getCollocazioneCartacea() != null) {
            _hashCode += getCollocazioneCartacea().hashCode();
        }
        _hashCode += (isCopiaCartacea() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isCartaceo() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getLegislatura() != null) {
            _hashCode += getLegislatura().hashCode();
        }
        if (getIdAnnotazioniList() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getIdAnnotazioniList());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getIdAnnotazioniList(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getIdMovimentazioneList() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getIdMovimentazioneList());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getIdMovimentazioneList(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        _hashCode += getIdStatoDocumento();
        _hashCode += getIdTipoClasse();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ClassificazionePropertiesType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "ClassificazionePropertiesType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numero");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "numero"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "integer"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codice");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "codice"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numeroInput");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "numeroInput"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("stato");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "stato"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataInizio");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "dataInizio"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "date"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataFine");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "dataFine"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "date"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("utenteCreazione");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "utenteCreazione"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "CodiceFiscaleType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("collocazioneCartacea");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "collocazioneCartacea"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("copiaCartacea");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "copiaCartacea"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cartaceo");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "cartaceo"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("legislatura");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "legislatura"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idAnnotazioniList");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "idAnnotazioniList"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "IdAnnotazioniType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idMovimentazioneList");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "idMovimentazioneList"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "IdMovimentazioneType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idStatoDocumento");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "idStatoDocumento"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "integer"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idTipoClasse");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "idTipoClasse"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "integer"));
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

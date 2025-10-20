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
 * TitolarioPropertiesType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actarelationships;

public class TitolarioPropertiesType  extends it.csi.risca.riscabesrv.util.actarelationships.FolderPropertiesType  implements java.io.Serializable {
    private java.lang.String codice;

    private java.lang.String descrizione;

    private java.lang.String stato;

    private java.lang.String idRaggruppamentoAOO;

    private java.util.Date dataCreaz;

    private java.util.Date dataInizio;

    private java.util.Date dataFine;

    private int numeroMaxLivelli;

    private int numeroLivSottofascicoliAmmessi;

    private boolean creaAutomaPrimaSerie;

    private boolean serieMultipleVoce;

    private boolean insertContenutiInVociConSottovoci;

    private boolean insertDocInFascConSottofasc;

    private boolean insertFuoriVolumi;

    private boolean scartoSoloConVolumi;

    private it.csi.risca.riscabesrv.util.actarelationships.IdProvvedimentoAutorizzatType[] idProvvedimentoAutorizzatList;

    public TitolarioPropertiesType() {
    }

    public TitolarioPropertiesType(
           it.csi.risca.riscabesrv.util.actarelationships.ObjectIdType objectId,
           it.csi.risca.riscabesrv.util.actarelationships.ChangeTokenType changeToken,
           java.lang.String objectTypeId,
           it.csi.risca.riscabesrv.util.actarelationships.ObjectIdType parentId,
           java.lang.String[] allowedChildObjectTypeIds,
           java.lang.String codice,
           java.lang.String descrizione,
           java.lang.String stato,
           java.lang.String idRaggruppamentoAOO,
           java.util.Date dataCreaz,
           java.util.Date dataInizio,
           java.util.Date dataFine,
           int numeroMaxLivelli,
           int numeroLivSottofascicoliAmmessi,
           boolean creaAutomaPrimaSerie,
           boolean serieMultipleVoce,
           boolean insertContenutiInVociConSottovoci,
           boolean insertDocInFascConSottofasc,
           boolean insertFuoriVolumi,
           boolean scartoSoloConVolumi,
           it.csi.risca.riscabesrv.util.actarelationships.IdProvvedimentoAutorizzatType[] idProvvedimentoAutorizzatList) {
        super(
            objectId,
            changeToken,
            objectTypeId,
            parentId,
            allowedChildObjectTypeIds);
        this.codice = codice;
        this.descrizione = descrizione;
        this.stato = stato;
        this.idRaggruppamentoAOO = idRaggruppamentoAOO;
        this.dataCreaz = dataCreaz;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.numeroMaxLivelli = numeroMaxLivelli;
        this.numeroLivSottofascicoliAmmessi = numeroLivSottofascicoliAmmessi;
        this.creaAutomaPrimaSerie = creaAutomaPrimaSerie;
        this.serieMultipleVoce = serieMultipleVoce;
        this.insertContenutiInVociConSottovoci = insertContenutiInVociConSottovoci;
        this.insertDocInFascConSottofasc = insertDocInFascConSottofasc;
        this.insertFuoriVolumi = insertFuoriVolumi;
        this.scartoSoloConVolumi = scartoSoloConVolumi;
        this.idProvvedimentoAutorizzatList = idProvvedimentoAutorizzatList;
    }


    /**
     * Gets the codice value for this TitolarioPropertiesType.
     * 
     * @return codice
     */
    public java.lang.String getCodice() {
        return codice;
    }


    /**
     * Sets the codice value for this TitolarioPropertiesType.
     * 
     * @param codice
     */
    public void setCodice(java.lang.String codice) {
        this.codice = codice;
    }


    /**
     * Gets the descrizione value for this TitolarioPropertiesType.
     * 
     * @return descrizione
     */
    public java.lang.String getDescrizione() {
        return descrizione;
    }


    /**
     * Sets the descrizione value for this TitolarioPropertiesType.
     * 
     * @param descrizione
     */
    public void setDescrizione(java.lang.String descrizione) {
        this.descrizione = descrizione;
    }


    /**
     * Gets the stato value for this TitolarioPropertiesType.
     * 
     * @return stato
     */
    public java.lang.String getStato() {
        return stato;
    }


    /**
     * Sets the stato value for this TitolarioPropertiesType.
     * 
     * @param stato
     */
    public void setStato(java.lang.String stato) {
        this.stato = stato;
    }


    /**
     * Gets the idRaggruppamentoAOO value for this TitolarioPropertiesType.
     * 
     * @return idRaggruppamentoAOO
     */
    public java.lang.String getIdRaggruppamentoAOO() {
        return idRaggruppamentoAOO;
    }


    /**
     * Sets the idRaggruppamentoAOO value for this TitolarioPropertiesType.
     * 
     * @param idRaggruppamentoAOO
     */
    public void setIdRaggruppamentoAOO(java.lang.String idRaggruppamentoAOO) {
        this.idRaggruppamentoAOO = idRaggruppamentoAOO;
    }


    /**
     * Gets the dataCreaz value for this TitolarioPropertiesType.
     * 
     * @return dataCreaz
     */
    public java.util.Date getDataCreaz() {
        return dataCreaz;
    }


    /**
     * Sets the dataCreaz value for this TitolarioPropertiesType.
     * 
     * @param dataCreaz
     */
    public void setDataCreaz(java.util.Date dataCreaz) {
        this.dataCreaz = dataCreaz;
    }


    /**
     * Gets the dataInizio value for this TitolarioPropertiesType.
     * 
     * @return dataInizio
     */
    public java.util.Date getDataInizio() {
        return dataInizio;
    }


    /**
     * Sets the dataInizio value for this TitolarioPropertiesType.
     * 
     * @param dataInizio
     */
    public void setDataInizio(java.util.Date dataInizio) {
        this.dataInizio = dataInizio;
    }


    /**
     * Gets the dataFine value for this TitolarioPropertiesType.
     * 
     * @return dataFine
     */
    public java.util.Date getDataFine() {
        return dataFine;
    }


    /**
     * Sets the dataFine value for this TitolarioPropertiesType.
     * 
     * @param dataFine
     */
    public void setDataFine(java.util.Date dataFine) {
        this.dataFine = dataFine;
    }


    /**
     * Gets the numeroMaxLivelli value for this TitolarioPropertiesType.
     * 
     * @return numeroMaxLivelli
     */
    public int getNumeroMaxLivelli() {
        return numeroMaxLivelli;
    }


    /**
     * Sets the numeroMaxLivelli value for this TitolarioPropertiesType.
     * 
     * @param numeroMaxLivelli
     */
    public void setNumeroMaxLivelli(int numeroMaxLivelli) {
        this.numeroMaxLivelli = numeroMaxLivelli;
    }


    /**
     * Gets the numeroLivSottofascicoliAmmessi value for this TitolarioPropertiesType.
     * 
     * @return numeroLivSottofascicoliAmmessi
     */
    public int getNumeroLivSottofascicoliAmmessi() {
        return numeroLivSottofascicoliAmmessi;
    }


    /**
     * Sets the numeroLivSottofascicoliAmmessi value for this TitolarioPropertiesType.
     * 
     * @param numeroLivSottofascicoliAmmessi
     */
    public void setNumeroLivSottofascicoliAmmessi(int numeroLivSottofascicoliAmmessi) {
        this.numeroLivSottofascicoliAmmessi = numeroLivSottofascicoliAmmessi;
    }


    /**
     * Gets the creaAutomaPrimaSerie value for this TitolarioPropertiesType.
     * 
     * @return creaAutomaPrimaSerie
     */
    public boolean isCreaAutomaPrimaSerie() {
        return creaAutomaPrimaSerie;
    }


    /**
     * Sets the creaAutomaPrimaSerie value for this TitolarioPropertiesType.
     * 
     * @param creaAutomaPrimaSerie
     */
    public void setCreaAutomaPrimaSerie(boolean creaAutomaPrimaSerie) {
        this.creaAutomaPrimaSerie = creaAutomaPrimaSerie;
    }


    /**
     * Gets the serieMultipleVoce value for this TitolarioPropertiesType.
     * 
     * @return serieMultipleVoce
     */
    public boolean isSerieMultipleVoce() {
        return serieMultipleVoce;
    }


    /**
     * Sets the serieMultipleVoce value for this TitolarioPropertiesType.
     * 
     * @param serieMultipleVoce
     */
    public void setSerieMultipleVoce(boolean serieMultipleVoce) {
        this.serieMultipleVoce = serieMultipleVoce;
    }


    /**
     * Gets the insertContenutiInVociConSottovoci value for this TitolarioPropertiesType.
     * 
     * @return insertContenutiInVociConSottovoci
     */
    public boolean isInsertContenutiInVociConSottovoci() {
        return insertContenutiInVociConSottovoci;
    }


    /**
     * Sets the insertContenutiInVociConSottovoci value for this TitolarioPropertiesType.
     * 
     * @param insertContenutiInVociConSottovoci
     */
    public void setInsertContenutiInVociConSottovoci(boolean insertContenutiInVociConSottovoci) {
        this.insertContenutiInVociConSottovoci = insertContenutiInVociConSottovoci;
    }


    /**
     * Gets the insertDocInFascConSottofasc value for this TitolarioPropertiesType.
     * 
     * @return insertDocInFascConSottofasc
     */
    public boolean isInsertDocInFascConSottofasc() {
        return insertDocInFascConSottofasc;
    }


    /**
     * Sets the insertDocInFascConSottofasc value for this TitolarioPropertiesType.
     * 
     * @param insertDocInFascConSottofasc
     */
    public void setInsertDocInFascConSottofasc(boolean insertDocInFascConSottofasc) {
        this.insertDocInFascConSottofasc = insertDocInFascConSottofasc;
    }


    /**
     * Gets the insertFuoriVolumi value for this TitolarioPropertiesType.
     * 
     * @return insertFuoriVolumi
     */
    public boolean isInsertFuoriVolumi() {
        return insertFuoriVolumi;
    }


    /**
     * Sets the insertFuoriVolumi value for this TitolarioPropertiesType.
     * 
     * @param insertFuoriVolumi
     */
    public void setInsertFuoriVolumi(boolean insertFuoriVolumi) {
        this.insertFuoriVolumi = insertFuoriVolumi;
    }


    /**
     * Gets the scartoSoloConVolumi value for this TitolarioPropertiesType.
     * 
     * @return scartoSoloConVolumi
     */
    public boolean isScartoSoloConVolumi() {
        return scartoSoloConVolumi;
    }


    /**
     * Sets the scartoSoloConVolumi value for this TitolarioPropertiesType.
     * 
     * @param scartoSoloConVolumi
     */
    public void setScartoSoloConVolumi(boolean scartoSoloConVolumi) {
        this.scartoSoloConVolumi = scartoSoloConVolumi;
    }


    /**
     * Gets the idProvvedimentoAutorizzatList value for this TitolarioPropertiesType.
     * 
     * @return idProvvedimentoAutorizzatList
     */
    public it.csi.risca.riscabesrv.util.actarelationships.IdProvvedimentoAutorizzatType[] getIdProvvedimentoAutorizzatList() {
        return idProvvedimentoAutorizzatList;
    }


    /**
     * Sets the idProvvedimentoAutorizzatList value for this TitolarioPropertiesType.
     * 
     * @param idProvvedimentoAutorizzatList
     */
    public void setIdProvvedimentoAutorizzatList(it.csi.risca.riscabesrv.util.actarelationships.IdProvvedimentoAutorizzatType[] idProvvedimentoAutorizzatList) {
        this.idProvvedimentoAutorizzatList = idProvvedimentoAutorizzatList;
    }

    public it.csi.risca.riscabesrv.util.actarelationships.IdProvvedimentoAutorizzatType getIdProvvedimentoAutorizzatList(int i) {
        return this.idProvvedimentoAutorizzatList[i];
    }

    public void setIdProvvedimentoAutorizzatList(int i, it.csi.risca.riscabesrv.util.actarelationships.IdProvvedimentoAutorizzatType _value) {
        this.idProvvedimentoAutorizzatList[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof TitolarioPropertiesType)) return false;
        TitolarioPropertiesType other = (TitolarioPropertiesType) obj;

        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.codice==null && other.getCodice()==null) || 
             (this.codice!=null &&
              this.codice.equals(other.getCodice()))) &&
            ((this.descrizione==null && other.getDescrizione()==null) || 
             (this.descrizione!=null &&
              this.descrizione.equals(other.getDescrizione()))) &&
            ((this.stato==null && other.getStato()==null) || 
             (this.stato!=null &&
              this.stato.equals(other.getStato()))) &&
            ((this.idRaggruppamentoAOO==null && other.getIdRaggruppamentoAOO()==null) || 
             (this.idRaggruppamentoAOO!=null &&
              this.idRaggruppamentoAOO.equals(other.getIdRaggruppamentoAOO()))) &&
            ((this.dataCreaz==null && other.getDataCreaz()==null) || 
             (this.dataCreaz!=null &&
              this.dataCreaz.equals(other.getDataCreaz()))) &&
            ((this.dataInizio==null && other.getDataInizio()==null) || 
             (this.dataInizio!=null &&
              this.dataInizio.equals(other.getDataInizio()))) &&
            ((this.dataFine==null && other.getDataFine()==null) || 
             (this.dataFine!=null &&
              this.dataFine.equals(other.getDataFine()))) &&
            this.numeroMaxLivelli == other.getNumeroMaxLivelli() &&
            this.numeroLivSottofascicoliAmmessi == other.getNumeroLivSottofascicoliAmmessi() &&
            this.creaAutomaPrimaSerie == other.isCreaAutomaPrimaSerie() &&
            this.serieMultipleVoce == other.isSerieMultipleVoce() &&
            this.insertContenutiInVociConSottovoci == other.isInsertContenutiInVociConSottovoci() &&
            this.insertDocInFascConSottofasc == other.isInsertDocInFascConSottofasc() &&
            this.insertFuoriVolumi == other.isInsertFuoriVolumi() &&
            this.scartoSoloConVolumi == other.isScartoSoloConVolumi() &&
            ((this.idProvvedimentoAutorizzatList==null && other.getIdProvvedimentoAutorizzatList()==null) || 
             (this.idProvvedimentoAutorizzatList!=null &&
              java.util.Arrays.equals(this.idProvvedimentoAutorizzatList, other.getIdProvvedimentoAutorizzatList())));
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
        if (getCodice() != null) {
            _hashCode += getCodice().hashCode();
        }
        if (getDescrizione() != null) {
            _hashCode += getDescrizione().hashCode();
        }
        if (getStato() != null) {
            _hashCode += getStato().hashCode();
        }
        if (getIdRaggruppamentoAOO() != null) {
            _hashCode += getIdRaggruppamentoAOO().hashCode();
        }
        if (getDataCreaz() != null) {
            _hashCode += getDataCreaz().hashCode();
        }
        if (getDataInizio() != null) {
            _hashCode += getDataInizio().hashCode();
        }
        if (getDataFine() != null) {
            _hashCode += getDataFine().hashCode();
        }
        _hashCode += getNumeroMaxLivelli();
        _hashCode += getNumeroLivSottofascicoliAmmessi();
        _hashCode += (isCreaAutomaPrimaSerie() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isSerieMultipleVoce() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isInsertContenutiInVociConSottovoci() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isInsertDocInFascConSottofasc() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isInsertFuoriVolumi() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isScartoSoloConVolumi() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getIdProvvedimentoAutorizzatList() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getIdProvvedimentoAutorizzatList());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getIdProvvedimentoAutorizzatList(), i);
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
        new org.apache.axis.description.TypeDesc(TitolarioPropertiesType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "TitolarioPropertiesType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codice");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "codice"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descrizione");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "descrizione"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("stato");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "stato"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idRaggruppamentoAOO");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "idRaggruppamentoAOO"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataCreaz");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "dataCreaz"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "date"));
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
        elemField.setFieldName("numeroMaxLivelli");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "numeroMaxLivelli"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "integer"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numeroLivSottofascicoliAmmessi");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "numeroLivSottofascicoliAmmessi"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "integer"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("creaAutomaPrimaSerie");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "creaAutomaPrimaSerie"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("serieMultipleVoce");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "serieMultipleVoce"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("insertContenutiInVociConSottovoci");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "insertContenutiInVociConSottovoci"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("insertDocInFascConSottofasc");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "insertDocInFascConSottofasc"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("insertFuoriVolumi");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "insertFuoriVolumi"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("scartoSoloConVolumi");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "scartoSoloConVolumi"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idProvvedimentoAutorizzatList");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "idProvvedimentoAutorizzatList"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "IdProvvedimentoAutorizzatType"));
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

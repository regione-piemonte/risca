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
 * DossierPropertiesType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actarelationships;

public class DossierPropertiesType  extends it.csi.risca.riscabesrv.util.actarelationships.AggregazionePropertiesType  implements java.io.Serializable {
    private boolean creazioneFascicoli;

    private boolean riclassificazioneFascicoli;

    private boolean inserimentoDocumenti;

    private boolean aggiuntaOriClassificazioneDocumenti;

    private java.lang.String stato;

    public DossierPropertiesType() {
    }

    public DossierPropertiesType(
           it.csi.risca.riscabesrv.util.actarelationships.ObjectIdType objectId,
           it.csi.risca.riscabesrv.util.actarelationships.ChangeTokenType changeToken,
           java.lang.String objectTypeId,
           it.csi.risca.riscabesrv.util.actarelationships.ObjectIdType parentId,
           java.lang.String[] allowedChildObjectTypeIds,
           java.lang.String codice,
           java.lang.String descrizione,
           java.lang.String indiceDiClassificazioneEsteso,
           java.util.Date dataCreazione,
           java.util.Date dataInizio,
           java.util.Date dataFine,
           int conservazioneCorrente,
           int conservazioneGenerale,
           java.util.Date dataPassaggioInDeposito,
           java.util.Date dataBloccoPassaggioInDeposito,
           java.util.Date dataSbloccoPassaggioInDeposito,
           java.util.Date dataScarto,
           boolean archivioCorrente,
           boolean datiPersonali,
           boolean datiSensibili,
           boolean datiRiservati,
           java.lang.String collocazioneCartaceo,
           java.lang.String paroleChiave,
           java.util.Date dataUltimaModifica,
           java.util.Date dataCancellazione,
           java.util.Date dataEsportazione,
           java.lang.String estremiProvvedimentoDiScarto,
           it.csi.risca.riscabesrv.util.actarelationships.CodiceFiscaleType utenteCreazione,
           it.csi.risca.riscabesrv.util.actarelationships.ObjectIdType idDeposito,
           it.csi.risca.riscabesrv.util.actarelationships.IdMovimentazioneType[] idMovimentazioneList,
           it.csi.risca.riscabesrv.util.actarelationships.IdAnnotazioniType[] idAnnotazioniList,
           int idTipoClasse,
           it.csi.risca.riscabesrv.util.actarelationships.IdAOOType idAOORespMat,
           it.csi.risca.riscabesrv.util.actarelationships.IdAOOType idAOORespCons,
           it.csi.risca.riscabesrv.util.actarelationships.IdStrutturaType idStrutturaRespMat,
           it.csi.risca.riscabesrv.util.actarelationships.IdStrutturaType idStrutturaRespCons,
           it.csi.risca.riscabesrv.util.actarelationships.IdNodoType idNodoRespMat,
           it.csi.risca.riscabesrv.util.actarelationships.IdNodoType idNodoRespCons,
           boolean creazioneFascicoli,
           boolean riclassificazioneFascicoli,
           boolean inserimentoDocumenti,
           boolean aggiuntaOriClassificazioneDocumenti,
           java.lang.String stato) {
        super(
            objectId,
            changeToken,
            objectTypeId,
            parentId,
            allowedChildObjectTypeIds,
            codice,
            descrizione,
            indiceDiClassificazioneEsteso,
            dataCreazione,
            dataInizio,
            dataFine,
            conservazioneCorrente,
            conservazioneGenerale,
            dataPassaggioInDeposito,
            dataBloccoPassaggioInDeposito,
            dataSbloccoPassaggioInDeposito,
            dataScarto,
            archivioCorrente,
            datiPersonali,
            datiSensibili,
            datiRiservati,
            collocazioneCartaceo,
            paroleChiave,
            dataUltimaModifica,
            dataCancellazione,
            dataEsportazione,
            estremiProvvedimentoDiScarto,
            utenteCreazione,
            idDeposito,
            idMovimentazioneList,
            idAnnotazioniList,
            idTipoClasse,
            idAOORespMat,
            idAOORespCons,
            idStrutturaRespMat,
            idStrutturaRespCons,
            idNodoRespMat,
            idNodoRespCons);
        this.creazioneFascicoli = creazioneFascicoli;
        this.riclassificazioneFascicoli = riclassificazioneFascicoli;
        this.inserimentoDocumenti = inserimentoDocumenti;
        this.aggiuntaOriClassificazioneDocumenti = aggiuntaOriClassificazioneDocumenti;
        this.stato = stato;
    }


    /**
     * Gets the creazioneFascicoli value for this DossierPropertiesType.
     * 
     * @return creazioneFascicoli
     */
    public boolean isCreazioneFascicoli() {
        return creazioneFascicoli;
    }


    /**
     * Sets the creazioneFascicoli value for this DossierPropertiesType.
     * 
     * @param creazioneFascicoli
     */
    public void setCreazioneFascicoli(boolean creazioneFascicoli) {
        this.creazioneFascicoli = creazioneFascicoli;
    }


    /**
     * Gets the riclassificazioneFascicoli value for this DossierPropertiesType.
     * 
     * @return riclassificazioneFascicoli
     */
    public boolean isRiclassificazioneFascicoli() {
        return riclassificazioneFascicoli;
    }


    /**
     * Sets the riclassificazioneFascicoli value for this DossierPropertiesType.
     * 
     * @param riclassificazioneFascicoli
     */
    public void setRiclassificazioneFascicoli(boolean riclassificazioneFascicoli) {
        this.riclassificazioneFascicoli = riclassificazioneFascicoli;
    }


    /**
     * Gets the inserimentoDocumenti value for this DossierPropertiesType.
     * 
     * @return inserimentoDocumenti
     */
    public boolean isInserimentoDocumenti() {
        return inserimentoDocumenti;
    }


    /**
     * Sets the inserimentoDocumenti value for this DossierPropertiesType.
     * 
     * @param inserimentoDocumenti
     */
    public void setInserimentoDocumenti(boolean inserimentoDocumenti) {
        this.inserimentoDocumenti = inserimentoDocumenti;
    }


    /**
     * Gets the aggiuntaOriClassificazioneDocumenti value for this DossierPropertiesType.
     * 
     * @return aggiuntaOriClassificazioneDocumenti
     */
    public boolean isAggiuntaOriClassificazioneDocumenti() {
        return aggiuntaOriClassificazioneDocumenti;
    }


    /**
     * Sets the aggiuntaOriClassificazioneDocumenti value for this DossierPropertiesType.
     * 
     * @param aggiuntaOriClassificazioneDocumenti
     */
    public void setAggiuntaOriClassificazioneDocumenti(boolean aggiuntaOriClassificazioneDocumenti) {
        this.aggiuntaOriClassificazioneDocumenti = aggiuntaOriClassificazioneDocumenti;
    }


    /**
     * Gets the stato value for this DossierPropertiesType.
     * 
     * @return stato
     */
    public java.lang.String getStato() {
        return stato;
    }


    /**
     * Sets the stato value for this DossierPropertiesType.
     * 
     * @param stato
     */
    public void setStato(java.lang.String stato) {
        this.stato = stato;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof DossierPropertiesType)) return false;
        DossierPropertiesType other = (DossierPropertiesType) obj;

        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            this.creazioneFascicoli == other.isCreazioneFascicoli() &&
            this.riclassificazioneFascicoli == other.isRiclassificazioneFascicoli() &&
            this.inserimentoDocumenti == other.isInserimentoDocumenti() &&
            this.aggiuntaOriClassificazioneDocumenti == other.isAggiuntaOriClassificazioneDocumenti() &&
            ((this.stato==null && other.getStato()==null) || 
             (this.stato!=null &&
              this.stato.equals(other.getStato())));
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
        _hashCode += (isCreazioneFascicoli() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isRiclassificazioneFascicoli() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isInserimentoDocumenti() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isAggiuntaOriClassificazioneDocumenti() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getStato() != null) {
            _hashCode += getStato().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DossierPropertiesType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "DossierPropertiesType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("creazioneFascicoli");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "creazioneFascicoli"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("riclassificazioneFascicoli");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "riclassificazioneFascicoli"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("inserimentoDocumenti");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "inserimentoDocumenti"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("aggiuntaOriClassificazioneDocumenti");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "aggiuntaOriClassificazioneDocumenti"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("stato");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "stato"));
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

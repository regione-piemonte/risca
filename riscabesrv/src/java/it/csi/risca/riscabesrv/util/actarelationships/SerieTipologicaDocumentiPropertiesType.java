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
 * SerieTipologicaDocumentiPropertiesType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actarelationships;

public class SerieTipologicaDocumentiPropertiesType  extends it.csi.risca.riscabesrv.util.actarelationships.SeriePropertiesType  implements java.io.Serializable {
    private java.lang.String modalitaCalcoloProgDoc;

    private java.lang.String parteFissa;

    private boolean registri;

    private boolean docAltraClassificazione;

    private java.lang.String stato;

    public SerieTipologicaDocumentiPropertiesType() {
    }

    public SerieTipologicaDocumentiPropertiesType(
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
           java.lang.String modalitaCalcoloProgDoc,
           java.lang.String parteFissa,
           boolean registri,
           boolean docAltraClassificazione,
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
        this.modalitaCalcoloProgDoc = modalitaCalcoloProgDoc;
        this.parteFissa = parteFissa;
        this.registri = registri;
        this.docAltraClassificazione = docAltraClassificazione;
        this.stato = stato;
    }


    /**
     * Gets the modalitaCalcoloProgDoc value for this SerieTipologicaDocumentiPropertiesType.
     * 
     * @return modalitaCalcoloProgDoc
     */
    public java.lang.String getModalitaCalcoloProgDoc() {
        return modalitaCalcoloProgDoc;
    }


    /**
     * Sets the modalitaCalcoloProgDoc value for this SerieTipologicaDocumentiPropertiesType.
     * 
     * @param modalitaCalcoloProgDoc
     */
    public void setModalitaCalcoloProgDoc(java.lang.String modalitaCalcoloProgDoc) {
        this.modalitaCalcoloProgDoc = modalitaCalcoloProgDoc;
    }


    /**
     * Gets the parteFissa value for this SerieTipologicaDocumentiPropertiesType.
     * 
     * @return parteFissa
     */
    public java.lang.String getParteFissa() {
        return parteFissa;
    }


    /**
     * Sets the parteFissa value for this SerieTipologicaDocumentiPropertiesType.
     * 
     * @param parteFissa
     */
    public void setParteFissa(java.lang.String parteFissa) {
        this.parteFissa = parteFissa;
    }


    /**
     * Gets the registri value for this SerieTipologicaDocumentiPropertiesType.
     * 
     * @return registri
     */
    public boolean isRegistri() {
        return registri;
    }


    /**
     * Sets the registri value for this SerieTipologicaDocumentiPropertiesType.
     * 
     * @param registri
     */
    public void setRegistri(boolean registri) {
        this.registri = registri;
    }


    /**
     * Gets the docAltraClassificazione value for this SerieTipologicaDocumentiPropertiesType.
     * 
     * @return docAltraClassificazione
     */
    public boolean isDocAltraClassificazione() {
        return docAltraClassificazione;
    }


    /**
     * Sets the docAltraClassificazione value for this SerieTipologicaDocumentiPropertiesType.
     * 
     * @param docAltraClassificazione
     */
    public void setDocAltraClassificazione(boolean docAltraClassificazione) {
        this.docAltraClassificazione = docAltraClassificazione;
    }


    /**
     * Gets the stato value for this SerieTipologicaDocumentiPropertiesType.
     * 
     * @return stato
     */
    public java.lang.String getStato() {
        return stato;
    }


    /**
     * Sets the stato value for this SerieTipologicaDocumentiPropertiesType.
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
        if (!(obj instanceof SerieTipologicaDocumentiPropertiesType)) return false;
        SerieTipologicaDocumentiPropertiesType other = (SerieTipologicaDocumentiPropertiesType) obj;

        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.modalitaCalcoloProgDoc==null && other.getModalitaCalcoloProgDoc()==null) || 
             (this.modalitaCalcoloProgDoc!=null &&
              this.modalitaCalcoloProgDoc.equals(other.getModalitaCalcoloProgDoc()))) &&
            ((this.parteFissa==null && other.getParteFissa()==null) || 
             (this.parteFissa!=null &&
              this.parteFissa.equals(other.getParteFissa()))) &&
            this.registri == other.isRegistri() &&
            this.docAltraClassificazione == other.isDocAltraClassificazione() &&
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
        if (getModalitaCalcoloProgDoc() != null) {
            _hashCode += getModalitaCalcoloProgDoc().hashCode();
        }
        if (getParteFissa() != null) {
            _hashCode += getParteFissa().hashCode();
        }
        _hashCode += (isRegistri() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isDocAltraClassificazione() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getStato() != null) {
            _hashCode += getStato().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SerieTipologicaDocumentiPropertiesType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "SerieTipologicaDocumentiPropertiesType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("modalitaCalcoloProgDoc");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "modalitaCalcoloProgDoc"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("parteFissa");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "parteFissa"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("registri");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "registri"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("docAltraClassificazione");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "docAltraClassificazione"));
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

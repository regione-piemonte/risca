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
 * FascicoloRealePropertiesType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actarelationships;

public abstract class FascicoloRealePropertiesType  extends it.csi.risca.riscabesrv.util.actarelationships.AggregazionePropertiesType  implements java.io.Serializable {
    private int anno;

    private java.lang.String numero;

    private java.lang.String oggetto;

    private java.lang.String soggetto;

    private boolean creatoFascStd;

    private boolean modificatoFascStd;

    private int numeroInterno;

    private it.csi.risca.riscabesrv.util.actarelationships.IdVitalRecordCodeType idVitalRecordCode;

    private it.csi.risca.riscabesrv.util.actarelationships.IdFascicoloStandardType idFascicoloStdRiferimento;

    private java.lang.String stato;

    public FascicoloRealePropertiesType() {
    }

    public FascicoloRealePropertiesType(
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
           int anno,
           java.lang.String numero,
           java.lang.String oggetto,
           java.lang.String soggetto,
           boolean creatoFascStd,
           boolean modificatoFascStd,
           int numeroInterno,
           it.csi.risca.riscabesrv.util.actarelationships.IdVitalRecordCodeType idVitalRecordCode,
           it.csi.risca.riscabesrv.util.actarelationships.IdFascicoloStandardType idFascicoloStdRiferimento,
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
        this.anno = anno;
        this.numero = numero;
        this.oggetto = oggetto;
        this.soggetto = soggetto;
        this.creatoFascStd = creatoFascStd;
        this.modificatoFascStd = modificatoFascStd;
        this.numeroInterno = numeroInterno;
        this.idVitalRecordCode = idVitalRecordCode;
        this.idFascicoloStdRiferimento = idFascicoloStdRiferimento;
        this.stato = stato;
    }


    /**
     * Gets the anno value for this FascicoloRealePropertiesType.
     * 
     * @return anno
     */
    public int getAnno() {
        return anno;
    }


    /**
     * Sets the anno value for this FascicoloRealePropertiesType.
     * 
     * @param anno
     */
    public void setAnno(int anno) {
        this.anno = anno;
    }


    /**
     * Gets the numero value for this FascicoloRealePropertiesType.
     * 
     * @return numero
     */
    public java.lang.String getNumero() {
        return numero;
    }


    /**
     * Sets the numero value for this FascicoloRealePropertiesType.
     * 
     * @param numero
     */
    public void setNumero(java.lang.String numero) {
        this.numero = numero;
    }


    /**
     * Gets the oggetto value for this FascicoloRealePropertiesType.
     * 
     * @return oggetto
     */
    public java.lang.String getOggetto() {
        return oggetto;
    }


    /**
     * Sets the oggetto value for this FascicoloRealePropertiesType.
     * 
     * @param oggetto
     */
    public void setOggetto(java.lang.String oggetto) {
        this.oggetto = oggetto;
    }


    /**
     * Gets the soggetto value for this FascicoloRealePropertiesType.
     * 
     * @return soggetto
     */
    public java.lang.String getSoggetto() {
        return soggetto;
    }


    /**
     * Sets the soggetto value for this FascicoloRealePropertiesType.
     * 
     * @param soggetto
     */
    public void setSoggetto(java.lang.String soggetto) {
        this.soggetto = soggetto;
    }


    /**
     * Gets the creatoFascStd value for this FascicoloRealePropertiesType.
     * 
     * @return creatoFascStd
     */
    public boolean isCreatoFascStd() {
        return creatoFascStd;
    }


    /**
     * Sets the creatoFascStd value for this FascicoloRealePropertiesType.
     * 
     * @param creatoFascStd
     */
    public void setCreatoFascStd(boolean creatoFascStd) {
        this.creatoFascStd = creatoFascStd;
    }


    /**
     * Gets the modificatoFascStd value for this FascicoloRealePropertiesType.
     * 
     * @return modificatoFascStd
     */
    public boolean isModificatoFascStd() {
        return modificatoFascStd;
    }


    /**
     * Sets the modificatoFascStd value for this FascicoloRealePropertiesType.
     * 
     * @param modificatoFascStd
     */
    public void setModificatoFascStd(boolean modificatoFascStd) {
        this.modificatoFascStd = modificatoFascStd;
    }


    /**
     * Gets the numeroInterno value for this FascicoloRealePropertiesType.
     * 
     * @return numeroInterno
     */
    public int getNumeroInterno() {
        return numeroInterno;
    }


    /**
     * Sets the numeroInterno value for this FascicoloRealePropertiesType.
     * 
     * @param numeroInterno
     */
    public void setNumeroInterno(int numeroInterno) {
        this.numeroInterno = numeroInterno;
    }


    /**
     * Gets the idVitalRecordCode value for this FascicoloRealePropertiesType.
     * 
     * @return idVitalRecordCode
     */
    public it.csi.risca.riscabesrv.util.actarelationships.IdVitalRecordCodeType getIdVitalRecordCode() {
        return idVitalRecordCode;
    }


    /**
     * Sets the idVitalRecordCode value for this FascicoloRealePropertiesType.
     * 
     * @param idVitalRecordCode
     */
    public void setIdVitalRecordCode(it.csi.risca.riscabesrv.util.actarelationships.IdVitalRecordCodeType idVitalRecordCode) {
        this.idVitalRecordCode = idVitalRecordCode;
    }


    /**
     * Gets the idFascicoloStdRiferimento value for this FascicoloRealePropertiesType.
     * 
     * @return idFascicoloStdRiferimento
     */
    public it.csi.risca.riscabesrv.util.actarelationships.IdFascicoloStandardType getIdFascicoloStdRiferimento() {
        return idFascicoloStdRiferimento;
    }


    /**
     * Sets the idFascicoloStdRiferimento value for this FascicoloRealePropertiesType.
     * 
     * @param idFascicoloStdRiferimento
     */
    public void setIdFascicoloStdRiferimento(it.csi.risca.riscabesrv.util.actarelationships.IdFascicoloStandardType idFascicoloStdRiferimento) {
        this.idFascicoloStdRiferimento = idFascicoloStdRiferimento;
    }


    /**
     * Gets the stato value for this FascicoloRealePropertiesType.
     * 
     * @return stato
     */
    public java.lang.String getStato() {
        return stato;
    }


    /**
     * Sets the stato value for this FascicoloRealePropertiesType.
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
        if (!(obj instanceof FascicoloRealePropertiesType)) return false;
        FascicoloRealePropertiesType other = (FascicoloRealePropertiesType) obj;

        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            this.anno == other.getAnno() &&
            ((this.numero==null && other.getNumero()==null) || 
             (this.numero!=null &&
              this.numero.equals(other.getNumero()))) &&
            ((this.oggetto==null && other.getOggetto()==null) || 
             (this.oggetto!=null &&
              this.oggetto.equals(other.getOggetto()))) &&
            ((this.soggetto==null && other.getSoggetto()==null) || 
             (this.soggetto!=null &&
              this.soggetto.equals(other.getSoggetto()))) &&
            this.creatoFascStd == other.isCreatoFascStd() &&
            this.modificatoFascStd == other.isModificatoFascStd() &&
            this.numeroInterno == other.getNumeroInterno() &&
            ((this.idVitalRecordCode==null && other.getIdVitalRecordCode()==null) || 
             (this.idVitalRecordCode!=null &&
              this.idVitalRecordCode.equals(other.getIdVitalRecordCode()))) &&
            ((this.idFascicoloStdRiferimento==null && other.getIdFascicoloStdRiferimento()==null) || 
             (this.idFascicoloStdRiferimento!=null &&
              this.idFascicoloStdRiferimento.equals(other.getIdFascicoloStdRiferimento()))) &&
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
        _hashCode += getAnno();
        if (getNumero() != null) {
            _hashCode += getNumero().hashCode();
        }
        if (getOggetto() != null) {
            _hashCode += getOggetto().hashCode();
        }
        if (getSoggetto() != null) {
            _hashCode += getSoggetto().hashCode();
        }
        _hashCode += (isCreatoFascStd() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isModificatoFascStd() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += getNumeroInterno();
        if (getIdVitalRecordCode() != null) {
            _hashCode += getIdVitalRecordCode().hashCode();
        }
        if (getIdFascicoloStdRiferimento() != null) {
            _hashCode += getIdFascicoloStdRiferimento().hashCode();
        }
        if (getStato() != null) {
            _hashCode += getStato().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(FascicoloRealePropertiesType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "FascicoloRealePropertiesType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("anno");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "anno"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "integer"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numero");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "numero"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("oggetto");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "oggetto"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("soggetto");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "soggetto"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("creatoFascStd");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "creatoFascStd"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("modificatoFascStd");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "modificatoFascStd"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numeroInterno");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "numeroInterno"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "integer"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idVitalRecordCode");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "idVitalRecordCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "IdVitalRecordCodeType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idFascicoloStdRiferimento");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "idFascicoloStdRiferimento"));
        elemField.setXmlType(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "IdFascicoloStandardType"));
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

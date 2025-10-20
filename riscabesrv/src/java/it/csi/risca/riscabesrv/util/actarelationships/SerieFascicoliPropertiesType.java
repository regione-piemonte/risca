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
 * SerieFascicoliPropertiesType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actarelationships;

public class SerieFascicoliPropertiesType  extends it.csi.risca.riscabesrv.util.actarelationships.SeriePropertiesType  implements java.io.Serializable {
    private boolean obbligoFascStand;

    private java.lang.String tipologiaNumerazione;

    private it.csi.risca.riscabesrv.util.actarelationships.IdFascicoloStandardType idFascicoloStandard;

    private java.lang.String stato;

    public SerieFascicoliPropertiesType() {
    }

    public SerieFascicoliPropertiesType(
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
           boolean obbligoFascStand,
           java.lang.String tipologiaNumerazione,
           it.csi.risca.riscabesrv.util.actarelationships.IdFascicoloStandardType idFascicoloStandard,
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
        this.obbligoFascStand = obbligoFascStand;
        this.tipologiaNumerazione = tipologiaNumerazione;
        this.idFascicoloStandard = idFascicoloStandard;
        this.stato = stato;
    }


    /**
     * Gets the obbligoFascStand value for this SerieFascicoliPropertiesType.
     * 
     * @return obbligoFascStand
     */
    public boolean isObbligoFascStand() {
        return obbligoFascStand;
    }


    /**
     * Sets the obbligoFascStand value for this SerieFascicoliPropertiesType.
     * 
     * @param obbligoFascStand
     */
    public void setObbligoFascStand(boolean obbligoFascStand) {
        this.obbligoFascStand = obbligoFascStand;
    }


    /**
     * Gets the tipologiaNumerazione value for this SerieFascicoliPropertiesType.
     * 
     * @return tipologiaNumerazione
     */
    public java.lang.String getTipologiaNumerazione() {
        return tipologiaNumerazione;
    }


    /**
     * Sets the tipologiaNumerazione value for this SerieFascicoliPropertiesType.
     * 
     * @param tipologiaNumerazione
     */
    public void setTipologiaNumerazione(java.lang.String tipologiaNumerazione) {
        this.tipologiaNumerazione = tipologiaNumerazione;
    }


    /**
     * Gets the idFascicoloStandard value for this SerieFascicoliPropertiesType.
     * 
     * @return idFascicoloStandard
     */
    public it.csi.risca.riscabesrv.util.actarelationships.IdFascicoloStandardType getIdFascicoloStandard() {
        return idFascicoloStandard;
    }


    /**
     * Sets the idFascicoloStandard value for this SerieFascicoliPropertiesType.
     * 
     * @param idFascicoloStandard
     */
    public void setIdFascicoloStandard(it.csi.risca.riscabesrv.util.actarelationships.IdFascicoloStandardType idFascicoloStandard) {
        this.idFascicoloStandard = idFascicoloStandard;
    }


    /**
     * Gets the stato value for this SerieFascicoliPropertiesType.
     * 
     * @return stato
     */
    public java.lang.String getStato() {
        return stato;
    }


    /**
     * Sets the stato value for this SerieFascicoliPropertiesType.
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
        if (!(obj instanceof SerieFascicoliPropertiesType)) return false;
        SerieFascicoliPropertiesType other = (SerieFascicoliPropertiesType) obj;

        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            this.obbligoFascStand == other.isObbligoFascStand() &&
            ((this.tipologiaNumerazione==null && other.getTipologiaNumerazione()==null) || 
             (this.tipologiaNumerazione!=null &&
              this.tipologiaNumerazione.equals(other.getTipologiaNumerazione()))) &&
            ((this.idFascicoloStandard==null && other.getIdFascicoloStandard()==null) || 
             (this.idFascicoloStandard!=null &&
              this.idFascicoloStandard.equals(other.getIdFascicoloStandard()))) &&
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
        _hashCode += (isObbligoFascStand() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getTipologiaNumerazione() != null) {
            _hashCode += getTipologiaNumerazione().hashCode();
        }
        if (getIdFascicoloStandard() != null) {
            _hashCode += getIdFascicoloStandard().hashCode();
        }
        if (getStato() != null) {
            _hashCode += getStato().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SerieFascicoliPropertiesType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "SerieFascicoliPropertiesType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("obbligoFascStand");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "obbligoFascStand"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipologiaNumerazione");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "tipologiaNumerazione"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idFascicoloStandard");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "idFascicoloStandard"));
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

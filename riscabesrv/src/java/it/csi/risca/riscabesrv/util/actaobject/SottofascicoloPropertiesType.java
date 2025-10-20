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
 * SottofascicoloPropertiesType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actaobject;

public class SottofascicoloPropertiesType  extends it.csi.risca.riscabesrv.util.actaobject.AggregazionePropertiesType  implements java.io.Serializable {
    private boolean creatoFascStd;

    private java.lang.String stato;

    public SottofascicoloPropertiesType() {
    }

    public SottofascicoloPropertiesType(
           it.csi.risca.riscabesrv.util.actaobject.ObjectIdType objectId,
           it.csi.risca.riscabesrv.util.actaobject.ChangeTokenType changeToken,
           java.lang.String objectTypeId,
           it.csi.risca.riscabesrv.util.actaobject.ObjectIdType parentId,
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
           it.csi.risca.riscabesrv.util.actaobject.CodiceFiscaleType utenteCreazione,
           it.csi.risca.riscabesrv.util.actaobject.ObjectIdType idDeposito,
           it.csi.risca.riscabesrv.util.actaobject.IdMovimentazioneType[] idMovimentazioneList,
           it.csi.risca.riscabesrv.util.actaobject.IdAnnotazioniType[] idAnnotazioniList,
           int idTipoClasse,
           it.csi.risca.riscabesrv.util.actaobject.IdAOOType idAOORespMat,
           it.csi.risca.riscabesrv.util.actaobject.IdAOOType idAOORespCons,
           it.csi.risca.riscabesrv.util.actaobject.IdStrutturaType idStrutturaRespMat,
           it.csi.risca.riscabesrv.util.actaobject.IdStrutturaType idStrutturaRespCons,
           it.csi.risca.riscabesrv.util.actaobject.IdNodoType idNodoRespMat,
           it.csi.risca.riscabesrv.util.actaobject.IdNodoType idNodoRespCons,
           boolean creatoFascStd,
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
        this.creatoFascStd = creatoFascStd;
        this.stato = stato;
    }


    /**
     * Gets the creatoFascStd value for this SottofascicoloPropertiesType.
     * 
     * @return creatoFascStd
     */
    public boolean isCreatoFascStd() {
        return creatoFascStd;
    }


    /**
     * Sets the creatoFascStd value for this SottofascicoloPropertiesType.
     * 
     * @param creatoFascStd
     */
    public void setCreatoFascStd(boolean creatoFascStd) {
        this.creatoFascStd = creatoFascStd;
    }


    /**
     * Gets the stato value for this SottofascicoloPropertiesType.
     * 
     * @return stato
     */
    public java.lang.String getStato() {
        return stato;
    }


    /**
     * Sets the stato value for this SottofascicoloPropertiesType.
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
        if (!(obj instanceof SottofascicoloPropertiesType)) return false;
        SottofascicoloPropertiesType other = (SottofascicoloPropertiesType) obj;

        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            this.creatoFascStd == other.isCreatoFascStd() &&
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
        _hashCode += (isCreatoFascStd() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getStato() != null) {
            _hashCode += getStato().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SottofascicoloPropertiesType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "SottofascicoloPropertiesType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("creatoFascStd");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "creatoFascStd"));
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

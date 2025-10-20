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
 * DocumentoSemplicePropertiesType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actaobject;

public class DocumentoSemplicePropertiesType  extends it.csi.risca.riscabesrv.util.actaobject.DocumentoPropertiesType  implements java.io.Serializable {
    private java.lang.String codBarre;

    private boolean multiplo;

    private java.lang.String tipoDocFisico;

    private java.lang.String composizione;

    public DocumentoSemplicePropertiesType() {
    }

    public DocumentoSemplicePropertiesType(
           it.csi.risca.riscabesrv.util.actaobject.ObjectIdType objectId,
           it.csi.risca.riscabesrv.util.actaobject.ChangeTokenType changeToken,
           java.lang.String objectTypeId,
           int contentStreamLength,
           java.lang.String contentStreamMimeType,
           java.lang.String contentStreamFilename,
           it.csi.risca.riscabesrv.util.actaobject.ObjectIdType idCorrente,
           boolean registrato,
           boolean modificabile,
           boolean definitivo,
           boolean origineInterna,
           boolean analogico,
           boolean rappresentazioneDigitale,
           boolean daConservare,
           boolean prontoPerConservazione,
           java.util.Date daConservareDopoIl,
           java.util.Date daConservarePrimaDel,
           boolean conservato,
           boolean datiPersonali,
           boolean datiSensibili,
           boolean datiRiservati,
           java.util.Date dataCreazione,
           java.lang.String paroleChiave,
           boolean modSMSQuarantena,
           boolean congelato,
           boolean referenziabile,
           java.lang.String identificativoConservazione,
           java.lang.String indiceEstesoOrigineEstratto,
           java.lang.String[] indiciEstesiEstrattiGenerati,
           java.lang.String[] autoreGiuridico,
           java.lang.String[] autoreFisico,
           java.lang.String[] scrittore,
           java.lang.String[] originatore,
           java.lang.String[] destinatarioGiuridico,
           java.lang.String[] destinatarioFisico,
           java.lang.String[] soggettoProduttore,
           java.lang.String oggetto,
           java.lang.String dataDocTopica,
           java.util.Date dataDocCronica,
           java.util.Date[] dataTrasmissione,
           java.util.Date dataRicevimento,
           java.lang.String numRepertorio,
           boolean forzareSeNumRepertorioEsistente,
           boolean docConAllegati,
           boolean docAllegato,
           boolean firmaElettronicaDigitale,
           boolean docAutenticato,
           boolean docAutenticatoFirmaAutenticata,
           boolean docAutenticatoCopiaAutentica,
           it.csi.risca.riscabesrv.util.actaobject.IdStatoDiEfficaciaType idStatoDiEfficacia,
           it.csi.risca.riscabesrv.util.actaobject.IdFormaDocumentariaType idFormaDocumentaria,
           boolean firmaElettronica,
           it.csi.risca.riscabesrv.util.actaobject.ObjectIdType[] idProtocollo,
           java.lang.String[] indiceClassificazione,
           java.lang.String[] indiceClassificazioneEstesa,
           java.lang.String applicativoAlimentante,
           java.util.Date dataCancellazione,
           java.util.Date dataEsportazione,
           it.csi.risca.riscabesrv.util.actaobject.IdVitalRecordCodeType idVitalRecordCode,
           it.csi.risca.riscabesrv.util.actaobject.ObjectIdType[] idAnnotazioniList,
           java.lang.String[] renditionXMLList,
           java.util.Date dataDocChiusuraRifTempUTC,
           java.util.Date dataDocChiusura,
           java.lang.String codBarre,
           boolean multiplo,
           java.lang.String tipoDocFisico,
           java.lang.String composizione) {
        super(
            objectId,
            changeToken,
            objectTypeId,
            contentStreamLength,
            contentStreamMimeType,
            contentStreamFilename,
            idCorrente,
            registrato,
            modificabile,
            definitivo,
            origineInterna,
            analogico,
            rappresentazioneDigitale,
            daConservare,
            prontoPerConservazione,
            daConservareDopoIl,
            daConservarePrimaDel,
            conservato,
            datiPersonali,
            datiSensibili,
            datiRiservati,
            dataCreazione,
            paroleChiave,
            modSMSQuarantena,
            congelato,
            referenziabile,
            identificativoConservazione,
            indiceEstesoOrigineEstratto,
            indiciEstesiEstrattiGenerati,
            autoreGiuridico,
            autoreFisico,
            scrittore,
            originatore,
            destinatarioGiuridico,
            destinatarioFisico,
            soggettoProduttore,
            oggetto,
            dataDocTopica,
            dataDocCronica,
            dataTrasmissione,
            dataRicevimento,
            numRepertorio,
            forzareSeNumRepertorioEsistente,
            docConAllegati,
            docAllegato,
            firmaElettronicaDigitale,
            docAutenticato,
            docAutenticatoFirmaAutenticata,
            docAutenticatoCopiaAutentica,
            idStatoDiEfficacia,
            idFormaDocumentaria,
            firmaElettronica,
            idProtocollo,
            indiceClassificazione,
            indiceClassificazioneEstesa,
            applicativoAlimentante,
            dataCancellazione,
            dataEsportazione,
            idVitalRecordCode,
            idAnnotazioniList,
            renditionXMLList,
            dataDocChiusuraRifTempUTC,
            dataDocChiusura);
        this.codBarre = codBarre;
        this.multiplo = multiplo;
        this.tipoDocFisico = tipoDocFisico;
        this.composizione = composizione;
    }


    /**
     * Gets the codBarre value for this DocumentoSemplicePropertiesType.
     * 
     * @return codBarre
     */
    public java.lang.String getCodBarre() {
        return codBarre;
    }


    /**
     * Sets the codBarre value for this DocumentoSemplicePropertiesType.
     * 
     * @param codBarre
     */
    public void setCodBarre(java.lang.String codBarre) {
        this.codBarre = codBarre;
    }


    /**
     * Gets the multiplo value for this DocumentoSemplicePropertiesType.
     * 
     * @return multiplo
     */
    public boolean isMultiplo() {
        return multiplo;
    }


    /**
     * Sets the multiplo value for this DocumentoSemplicePropertiesType.
     * 
     * @param multiplo
     */
    public void setMultiplo(boolean multiplo) {
        this.multiplo = multiplo;
    }


    /**
     * Gets the tipoDocFisico value for this DocumentoSemplicePropertiesType.
     * 
     * @return tipoDocFisico
     */
    public java.lang.String getTipoDocFisico() {
        return tipoDocFisico;
    }


    /**
     * Sets the tipoDocFisico value for this DocumentoSemplicePropertiesType.
     * 
     * @param tipoDocFisico
     */
    public void setTipoDocFisico(java.lang.String tipoDocFisico) {
        this.tipoDocFisico = tipoDocFisico;
    }


    /**
     * Gets the composizione value for this DocumentoSemplicePropertiesType.
     * 
     * @return composizione
     */
    public java.lang.String getComposizione() {
        return composizione;
    }


    /**
     * Sets the composizione value for this DocumentoSemplicePropertiesType.
     * 
     * @param composizione
     */
    public void setComposizione(java.lang.String composizione) {
        this.composizione = composizione;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof DocumentoSemplicePropertiesType)) return false;
        DocumentoSemplicePropertiesType other = (DocumentoSemplicePropertiesType) obj;

        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.codBarre==null && other.getCodBarre()==null) || 
             (this.codBarre!=null &&
              this.codBarre.equals(other.getCodBarre()))) &&
            this.multiplo == other.isMultiplo() &&
            ((this.tipoDocFisico==null && other.getTipoDocFisico()==null) || 
             (this.tipoDocFisico!=null &&
              this.tipoDocFisico.equals(other.getTipoDocFisico()))) &&
            ((this.composizione==null && other.getComposizione()==null) || 
             (this.composizione!=null &&
              this.composizione.equals(other.getComposizione())));
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
        if (getCodBarre() != null) {
            _hashCode += getCodBarre().hashCode();
        }
        _hashCode += (isMultiplo() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getTipoDocFisico() != null) {
            _hashCode += getTipoDocFisico().hashCode();
        }
        if (getComposizione() != null) {
            _hashCode += getComposizione().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DocumentoSemplicePropertiesType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "DocumentoSemplicePropertiesType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codBarre");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "codBarre"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("multiplo");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "multiplo"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoDocFisico");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "tipoDocFisico"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("composizione");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "composizione"));
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

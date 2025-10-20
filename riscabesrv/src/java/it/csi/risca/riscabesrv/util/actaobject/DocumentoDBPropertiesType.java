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
 * DocumentoDBPropertiesType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actaobject;

public class DocumentoDBPropertiesType  extends it.csi.risca.riscabesrv.util.actaobject.DocumentoPropertiesType  implements java.io.Serializable {
    private java.util.Date dataUltimoAggiornam;

    private boolean attivo;

    private java.lang.String ubicazione;

    private java.lang.String versioneSW;

    private java.lang.String tipoDocFisico;

    private java.lang.String composizione;

    public DocumentoDBPropertiesType() {
    }

    public DocumentoDBPropertiesType(
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
           java.util.Date dataUltimoAggiornam,
           boolean attivo,
           java.lang.String ubicazione,
           java.lang.String versioneSW,
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
        this.dataUltimoAggiornam = dataUltimoAggiornam;
        this.attivo = attivo;
        this.ubicazione = ubicazione;
        this.versioneSW = versioneSW;
        this.tipoDocFisico = tipoDocFisico;
        this.composizione = composizione;
    }


    /**
     * Gets the dataUltimoAggiornam value for this DocumentoDBPropertiesType.
     * 
     * @return dataUltimoAggiornam
     */
    public java.util.Date getDataUltimoAggiornam() {
        return dataUltimoAggiornam;
    }


    /**
     * Sets the dataUltimoAggiornam value for this DocumentoDBPropertiesType.
     * 
     * @param dataUltimoAggiornam
     */
    public void setDataUltimoAggiornam(java.util.Date dataUltimoAggiornam) {
        this.dataUltimoAggiornam = dataUltimoAggiornam;
    }


    /**
     * Gets the attivo value for this DocumentoDBPropertiesType.
     * 
     * @return attivo
     */
    public boolean isAttivo() {
        return attivo;
    }


    /**
     * Sets the attivo value for this DocumentoDBPropertiesType.
     * 
     * @param attivo
     */
    public void setAttivo(boolean attivo) {
        this.attivo = attivo;
    }


    /**
     * Gets the ubicazione value for this DocumentoDBPropertiesType.
     * 
     * @return ubicazione
     */
    public java.lang.String getUbicazione() {
        return ubicazione;
    }


    /**
     * Sets the ubicazione value for this DocumentoDBPropertiesType.
     * 
     * @param ubicazione
     */
    public void setUbicazione(java.lang.String ubicazione) {
        this.ubicazione = ubicazione;
    }


    /**
     * Gets the versioneSW value for this DocumentoDBPropertiesType.
     * 
     * @return versioneSW
     */
    public java.lang.String getVersioneSW() {
        return versioneSW;
    }


    /**
     * Sets the versioneSW value for this DocumentoDBPropertiesType.
     * 
     * @param versioneSW
     */
    public void setVersioneSW(java.lang.String versioneSW) {
        this.versioneSW = versioneSW;
    }


    /**
     * Gets the tipoDocFisico value for this DocumentoDBPropertiesType.
     * 
     * @return tipoDocFisico
     */
    public java.lang.String getTipoDocFisico() {
        return tipoDocFisico;
    }


    /**
     * Sets the tipoDocFisico value for this DocumentoDBPropertiesType.
     * 
     * @param tipoDocFisico
     */
    public void setTipoDocFisico(java.lang.String tipoDocFisico) {
        this.tipoDocFisico = tipoDocFisico;
    }


    /**
     * Gets the composizione value for this DocumentoDBPropertiesType.
     * 
     * @return composizione
     */
    public java.lang.String getComposizione() {
        return composizione;
    }


    /**
     * Sets the composizione value for this DocumentoDBPropertiesType.
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
        if (!(obj instanceof DocumentoDBPropertiesType)) return false;
        DocumentoDBPropertiesType other = (DocumentoDBPropertiesType) obj;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.dataUltimoAggiornam==null && other.getDataUltimoAggiornam()==null) || 
             (this.dataUltimoAggiornam!=null &&
              this.dataUltimoAggiornam.equals(other.getDataUltimoAggiornam()))) &&
            this.attivo == other.isAttivo() &&
            ((this.ubicazione==null && other.getUbicazione()==null) || 
             (this.ubicazione!=null &&
              this.ubicazione.equals(other.getUbicazione()))) &&
            ((this.versioneSW==null && other.getVersioneSW()==null) || 
             (this.versioneSW!=null &&
              this.versioneSW.equals(other.getVersioneSW()))) &&
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
        if (getDataUltimoAggiornam() != null) {
            _hashCode += getDataUltimoAggiornam().hashCode();
        }
        _hashCode += (isAttivo() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getUbicazione() != null) {
            _hashCode += getUbicazione().hashCode();
        }
        if (getVersioneSW() != null) {
            _hashCode += getVersioneSW().hashCode();
        }
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
        new org.apache.axis.description.TypeDesc(DocumentoDBPropertiesType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "DocumentoDBPropertiesType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataUltimoAggiornam");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "dataUltimoAggiornam"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "date"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("attivo");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "attivo"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ubicazione");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "ubicazione"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("versioneSW");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "versioneSW"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"));
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

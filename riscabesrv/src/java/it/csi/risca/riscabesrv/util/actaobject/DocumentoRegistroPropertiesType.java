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
 * DocumentoRegistroPropertiesType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actaobject;

public class DocumentoRegistroPropertiesType  extends it.csi.risca.riscabesrv.util.actaobject.DocumentoPropertiesType  implements java.io.Serializable {
    private java.lang.String codiceRegistro;

    private int annoRegistro;

    private java.lang.String numeroPrimaRegistrazione;

    private java.util.Date dataPrimaRegistrazione;

    private java.lang.String numeroUltimaRegistrazione;

    private java.util.Date dataUltimaRegistrazione;

    private java.util.Date dataApertura;

    private java.util.Date dataChiusura;

    private java.lang.String ubicazione;

    private java.lang.String versioneSW;

    private java.lang.String tipoDocFisico;

    private java.lang.String composizione;

    public DocumentoRegistroPropertiesType() {
    }

    public DocumentoRegistroPropertiesType(
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
           java.lang.String codiceRegistro,
           int annoRegistro,
           java.lang.String numeroPrimaRegistrazione,
           java.util.Date dataPrimaRegistrazione,
           java.lang.String numeroUltimaRegistrazione,
           java.util.Date dataUltimaRegistrazione,
           java.util.Date dataApertura,
           java.util.Date dataChiusura,
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
        this.codiceRegistro = codiceRegistro;
        this.annoRegistro = annoRegistro;
        this.numeroPrimaRegistrazione = numeroPrimaRegistrazione;
        this.dataPrimaRegistrazione = dataPrimaRegistrazione;
        this.numeroUltimaRegistrazione = numeroUltimaRegistrazione;
        this.dataUltimaRegistrazione = dataUltimaRegistrazione;
        this.dataApertura = dataApertura;
        this.dataChiusura = dataChiusura;
        this.ubicazione = ubicazione;
        this.versioneSW = versioneSW;
        this.tipoDocFisico = tipoDocFisico;
        this.composizione = composizione;
    }


    /**
     * Gets the codiceRegistro value for this DocumentoRegistroPropertiesType.
     * 
     * @return codiceRegistro
     */
    public java.lang.String getCodiceRegistro() {
        return codiceRegistro;
    }


    /**
     * Sets the codiceRegistro value for this DocumentoRegistroPropertiesType.
     * 
     * @param codiceRegistro
     */
    public void setCodiceRegistro(java.lang.String codiceRegistro) {
        this.codiceRegistro = codiceRegistro;
    }


    /**
     * Gets the annoRegistro value for this DocumentoRegistroPropertiesType.
     * 
     * @return annoRegistro
     */
    public int getAnnoRegistro() {
        return annoRegistro;
    }


    /**
     * Sets the annoRegistro value for this DocumentoRegistroPropertiesType.
     * 
     * @param annoRegistro
     */
    public void setAnnoRegistro(int annoRegistro) {
        this.annoRegistro = annoRegistro;
    }


    /**
     * Gets the numeroPrimaRegistrazione value for this DocumentoRegistroPropertiesType.
     * 
     * @return numeroPrimaRegistrazione
     */
    public java.lang.String getNumeroPrimaRegistrazione() {
        return numeroPrimaRegistrazione;
    }


    /**
     * Sets the numeroPrimaRegistrazione value for this DocumentoRegistroPropertiesType.
     * 
     * @param numeroPrimaRegistrazione
     */
    public void setNumeroPrimaRegistrazione(java.lang.String numeroPrimaRegistrazione) {
        this.numeroPrimaRegistrazione = numeroPrimaRegistrazione;
    }


    /**
     * Gets the dataPrimaRegistrazione value for this DocumentoRegistroPropertiesType.
     * 
     * @return dataPrimaRegistrazione
     */
    public java.util.Date getDataPrimaRegistrazione() {
        return dataPrimaRegistrazione;
    }


    /**
     * Sets the dataPrimaRegistrazione value for this DocumentoRegistroPropertiesType.
     * 
     * @param dataPrimaRegistrazione
     */
    public void setDataPrimaRegistrazione(java.util.Date dataPrimaRegistrazione) {
        this.dataPrimaRegistrazione = dataPrimaRegistrazione;
    }


    /**
     * Gets the numeroUltimaRegistrazione value for this DocumentoRegistroPropertiesType.
     * 
     * @return numeroUltimaRegistrazione
     */
    public java.lang.String getNumeroUltimaRegistrazione() {
        return numeroUltimaRegistrazione;
    }


    /**
     * Sets the numeroUltimaRegistrazione value for this DocumentoRegistroPropertiesType.
     * 
     * @param numeroUltimaRegistrazione
     */
    public void setNumeroUltimaRegistrazione(java.lang.String numeroUltimaRegistrazione) {
        this.numeroUltimaRegistrazione = numeroUltimaRegistrazione;
    }


    /**
     * Gets the dataUltimaRegistrazione value for this DocumentoRegistroPropertiesType.
     * 
     * @return dataUltimaRegistrazione
     */
    public java.util.Date getDataUltimaRegistrazione() {
        return dataUltimaRegistrazione;
    }


    /**
     * Sets the dataUltimaRegistrazione value for this DocumentoRegistroPropertiesType.
     * 
     * @param dataUltimaRegistrazione
     */
    public void setDataUltimaRegistrazione(java.util.Date dataUltimaRegistrazione) {
        this.dataUltimaRegistrazione = dataUltimaRegistrazione;
    }


    /**
     * Gets the dataApertura value for this DocumentoRegistroPropertiesType.
     * 
     * @return dataApertura
     */
    public java.util.Date getDataApertura() {
        return dataApertura;
    }


    /**
     * Sets the dataApertura value for this DocumentoRegistroPropertiesType.
     * 
     * @param dataApertura
     */
    public void setDataApertura(java.util.Date dataApertura) {
        this.dataApertura = dataApertura;
    }


    /**
     * Gets the dataChiusura value for this DocumentoRegistroPropertiesType.
     * 
     * @return dataChiusura
     */
    public java.util.Date getDataChiusura() {
        return dataChiusura;
    }


    /**
     * Sets the dataChiusura value for this DocumentoRegistroPropertiesType.
     * 
     * @param dataChiusura
     */
    public void setDataChiusura(java.util.Date dataChiusura) {
        this.dataChiusura = dataChiusura;
    }


    /**
     * Gets the ubicazione value for this DocumentoRegistroPropertiesType.
     * 
     * @return ubicazione
     */
    public java.lang.String getUbicazione() {
        return ubicazione;
    }


    /**
     * Sets the ubicazione value for this DocumentoRegistroPropertiesType.
     * 
     * @param ubicazione
     */
    public void setUbicazione(java.lang.String ubicazione) {
        this.ubicazione = ubicazione;
    }


    /**
     * Gets the versioneSW value for this DocumentoRegistroPropertiesType.
     * 
     * @return versioneSW
     */
    public java.lang.String getVersioneSW() {
        return versioneSW;
    }


    /**
     * Sets the versioneSW value for this DocumentoRegistroPropertiesType.
     * 
     * @param versioneSW
     */
    public void setVersioneSW(java.lang.String versioneSW) {
        this.versioneSW = versioneSW;
    }


    /**
     * Gets the tipoDocFisico value for this DocumentoRegistroPropertiesType.
     * 
     * @return tipoDocFisico
     */
    public java.lang.String getTipoDocFisico() {
        return tipoDocFisico;
    }


    /**
     * Sets the tipoDocFisico value for this DocumentoRegistroPropertiesType.
     * 
     * @param tipoDocFisico
     */
    public void setTipoDocFisico(java.lang.String tipoDocFisico) {
        this.tipoDocFisico = tipoDocFisico;
    }


    /**
     * Gets the composizione value for this DocumentoRegistroPropertiesType.
     * 
     * @return composizione
     */
    public java.lang.String getComposizione() {
        return composizione;
    }


    /**
     * Sets the composizione value for this DocumentoRegistroPropertiesType.
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
        if (!(obj instanceof DocumentoRegistroPropertiesType)) return false;
        DocumentoRegistroPropertiesType other = (DocumentoRegistroPropertiesType) obj;

        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.codiceRegistro==null && other.getCodiceRegistro()==null) || 
             (this.codiceRegistro!=null &&
              this.codiceRegistro.equals(other.getCodiceRegistro()))) &&
            this.annoRegistro == other.getAnnoRegistro() &&
            ((this.numeroPrimaRegistrazione==null && other.getNumeroPrimaRegistrazione()==null) || 
             (this.numeroPrimaRegistrazione!=null &&
              this.numeroPrimaRegistrazione.equals(other.getNumeroPrimaRegistrazione()))) &&
            ((this.dataPrimaRegistrazione==null && other.getDataPrimaRegistrazione()==null) || 
             (this.dataPrimaRegistrazione!=null &&
              this.dataPrimaRegistrazione.equals(other.getDataPrimaRegistrazione()))) &&
            ((this.numeroUltimaRegistrazione==null && other.getNumeroUltimaRegistrazione()==null) || 
             (this.numeroUltimaRegistrazione!=null &&
              this.numeroUltimaRegistrazione.equals(other.getNumeroUltimaRegistrazione()))) &&
            ((this.dataUltimaRegistrazione==null && other.getDataUltimaRegistrazione()==null) || 
             (this.dataUltimaRegistrazione!=null &&
              this.dataUltimaRegistrazione.equals(other.getDataUltimaRegistrazione()))) &&
            ((this.dataApertura==null && other.getDataApertura()==null) || 
             (this.dataApertura!=null &&
              this.dataApertura.equals(other.getDataApertura()))) &&
            ((this.dataChiusura==null && other.getDataChiusura()==null) || 
             (this.dataChiusura!=null &&
              this.dataChiusura.equals(other.getDataChiusura()))) &&
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
        if (getCodiceRegistro() != null) {
            _hashCode += getCodiceRegistro().hashCode();
        }
        _hashCode += getAnnoRegistro();
        if (getNumeroPrimaRegistrazione() != null) {
            _hashCode += getNumeroPrimaRegistrazione().hashCode();
        }
        if (getDataPrimaRegistrazione() != null) {
            _hashCode += getDataPrimaRegistrazione().hashCode();
        }
        if (getNumeroUltimaRegistrazione() != null) {
            _hashCode += getNumeroUltimaRegistrazione().hashCode();
        }
        if (getDataUltimaRegistrazione() != null) {
            _hashCode += getDataUltimaRegistrazione().hashCode();
        }
        if (getDataApertura() != null) {
            _hashCode += getDataApertura().hashCode();
        }
        if (getDataChiusura() != null) {
            _hashCode += getDataChiusura().hashCode();
        }
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
        new org.apache.axis.description.TypeDesc(DocumentoRegistroPropertiesType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "DocumentoRegistroPropertiesType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codiceRegistro");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "codiceRegistro"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("annoRegistro");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "annoRegistro"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "integer"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numeroPrimaRegistrazione");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "numeroPrimaRegistrazione"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataPrimaRegistrazione");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "dataPrimaRegistrazione"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "date"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numeroUltimaRegistrazione");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "numeroUltimaRegistrazione"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataUltimaRegistrazione");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "dataUltimaRegistrazione"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "date"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataApertura");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "dataApertura"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "date"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataChiusura");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "dataChiusura"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "date"));
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

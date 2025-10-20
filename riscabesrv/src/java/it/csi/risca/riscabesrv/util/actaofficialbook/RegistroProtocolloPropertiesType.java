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
 * RegistroProtocolloPropertiesType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actaofficialbook;

public class RegistroProtocolloPropertiesType  extends it.csi.risca.riscabesrv.util.actaofficialbook.OfficialBookPropertiesType  implements java.io.Serializable {
    private int anno;

    private it.csi.risca.riscabesrv.util.actaofficialbook.DecodificaType stato;

    private boolean storico;

    private long codiceUltimaRegistrazione;

    private java.util.Date dataUltimaRegistrazione;

    private java.util.Date dataCreazione;

    private java.util.Date dataApertura;

    private java.util.Date dataChiusura;

    private java.util.Date dataArchiviazione;

    private it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idAOOProtocollante;

    private it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idAOOResponsabile;

    private it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idStrutturaResponsabile;

    private it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idNodoResponsabile;

    private it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idUtenteCreazione;

    private it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType[] idAnnotazioneOB;

    private it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idClassificazioneDocStampaDefinitiva;

    private it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idAggregazioneDocStampaDefinitiva;

    public RegistroProtocolloPropertiesType() {
    }

    public RegistroProtocolloPropertiesType(
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType objectId,
           it.csi.risca.riscabesrv.util.actaofficialbook.ChangeTokenType changeToken,
           java.lang.String objectTypeId,
           int anno,
           it.csi.risca.riscabesrv.util.actaofficialbook.DecodificaType stato,
           boolean storico,
           long codiceUltimaRegistrazione,
           java.util.Date dataUltimaRegistrazione,
           java.util.Date dataCreazione,
           java.util.Date dataApertura,
           java.util.Date dataChiusura,
           java.util.Date dataArchiviazione,
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idAOOProtocollante,
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idAOOResponsabile,
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idStrutturaResponsabile,
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idNodoResponsabile,
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idUtenteCreazione,
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType[] idAnnotazioneOB,
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idClassificazioneDocStampaDefinitiva,
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idAggregazioneDocStampaDefinitiva) {
        super(
            objectId,
            changeToken,
            objectTypeId);
        this.anno = anno;
        this.stato = stato;
        this.storico = storico;
        this.codiceUltimaRegistrazione = codiceUltimaRegistrazione;
        this.dataUltimaRegistrazione = dataUltimaRegistrazione;
        this.dataCreazione = dataCreazione;
        this.dataApertura = dataApertura;
        this.dataChiusura = dataChiusura;
        this.dataArchiviazione = dataArchiviazione;
        this.idAOOProtocollante = idAOOProtocollante;
        this.idAOOResponsabile = idAOOResponsabile;
        this.idStrutturaResponsabile = idStrutturaResponsabile;
        this.idNodoResponsabile = idNodoResponsabile;
        this.idUtenteCreazione = idUtenteCreazione;
        this.idAnnotazioneOB = idAnnotazioneOB;
        this.idClassificazioneDocStampaDefinitiva = idClassificazioneDocStampaDefinitiva;
        this.idAggregazioneDocStampaDefinitiva = idAggregazioneDocStampaDefinitiva;
    }


    /**
     * Gets the anno value for this RegistroProtocolloPropertiesType.
     * 
     * @return anno
     */
    public int getAnno() {
        return anno;
    }


    /**
     * Sets the anno value for this RegistroProtocolloPropertiesType.
     * 
     * @param anno
     */
    public void setAnno(int anno) {
        this.anno = anno;
    }


    /**
     * Gets the stato value for this RegistroProtocolloPropertiesType.
     * 
     * @return stato
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.DecodificaType getStato() {
        return stato;
    }


    /**
     * Sets the stato value for this RegistroProtocolloPropertiesType.
     * 
     * @param stato
     */
    public void setStato(it.csi.risca.riscabesrv.util.actaofficialbook.DecodificaType stato) {
        this.stato = stato;
    }


    /**
     * Gets the storico value for this RegistroProtocolloPropertiesType.
     * 
     * @return storico
     */
    public boolean isStorico() {
        return storico;
    }


    /**
     * Sets the storico value for this RegistroProtocolloPropertiesType.
     * 
     * @param storico
     */
    public void setStorico(boolean storico) {
        this.storico = storico;
    }


    /**
     * Gets the codiceUltimaRegistrazione value for this RegistroProtocolloPropertiesType.
     * 
     * @return codiceUltimaRegistrazione
     */
    public long getCodiceUltimaRegistrazione() {
        return codiceUltimaRegistrazione;
    }


    /**
     * Sets the codiceUltimaRegistrazione value for this RegistroProtocolloPropertiesType.
     * 
     * @param codiceUltimaRegistrazione
     */
    public void setCodiceUltimaRegistrazione(long codiceUltimaRegistrazione) {
        this.codiceUltimaRegistrazione = codiceUltimaRegistrazione;
    }


    /**
     * Gets the dataUltimaRegistrazione value for this RegistroProtocolloPropertiesType.
     * 
     * @return dataUltimaRegistrazione
     */
    public java.util.Date getDataUltimaRegistrazione() {
        return dataUltimaRegistrazione;
    }


    /**
     * Sets the dataUltimaRegistrazione value for this RegistroProtocolloPropertiesType.
     * 
     * @param dataUltimaRegistrazione
     */
    public void setDataUltimaRegistrazione(java.util.Date dataUltimaRegistrazione) {
        this.dataUltimaRegistrazione = dataUltimaRegistrazione;
    }


    /**
     * Gets the dataCreazione value for this RegistroProtocolloPropertiesType.
     * 
     * @return dataCreazione
     */
    public java.util.Date getDataCreazione() {
        return dataCreazione;
    }


    /**
     * Sets the dataCreazione value for this RegistroProtocolloPropertiesType.
     * 
     * @param dataCreazione
     */
    public void setDataCreazione(java.util.Date dataCreazione) {
        this.dataCreazione = dataCreazione;
    }


    /**
     * Gets the dataApertura value for this RegistroProtocolloPropertiesType.
     * 
     * @return dataApertura
     */
    public java.util.Date getDataApertura() {
        return dataApertura;
    }


    /**
     * Sets the dataApertura value for this RegistroProtocolloPropertiesType.
     * 
     * @param dataApertura
     */
    public void setDataApertura(java.util.Date dataApertura) {
        this.dataApertura = dataApertura;
    }


    /**
     * Gets the dataChiusura value for this RegistroProtocolloPropertiesType.
     * 
     * @return dataChiusura
     */
    public java.util.Date getDataChiusura() {
        return dataChiusura;
    }


    /**
     * Sets the dataChiusura value for this RegistroProtocolloPropertiesType.
     * 
     * @param dataChiusura
     */
    public void setDataChiusura(java.util.Date dataChiusura) {
        this.dataChiusura = dataChiusura;
    }


    /**
     * Gets the dataArchiviazione value for this RegistroProtocolloPropertiesType.
     * 
     * @return dataArchiviazione
     */
    public java.util.Date getDataArchiviazione() {
        return dataArchiviazione;
    }


    /**
     * Sets the dataArchiviazione value for this RegistroProtocolloPropertiesType.
     * 
     * @param dataArchiviazione
     */
    public void setDataArchiviazione(java.util.Date dataArchiviazione) {
        this.dataArchiviazione = dataArchiviazione;
    }


    /**
     * Gets the idAOOProtocollante value for this RegistroProtocolloPropertiesType.
     * 
     * @return idAOOProtocollante
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType getIdAOOProtocollante() {
        return idAOOProtocollante;
    }


    /**
     * Sets the idAOOProtocollante value for this RegistroProtocolloPropertiesType.
     * 
     * @param idAOOProtocollante
     */
    public void setIdAOOProtocollante(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idAOOProtocollante) {
        this.idAOOProtocollante = idAOOProtocollante;
    }


    /**
     * Gets the idAOOResponsabile value for this RegistroProtocolloPropertiesType.
     * 
     * @return idAOOResponsabile
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType getIdAOOResponsabile() {
        return idAOOResponsabile;
    }


    /**
     * Sets the idAOOResponsabile value for this RegistroProtocolloPropertiesType.
     * 
     * @param idAOOResponsabile
     */
    public void setIdAOOResponsabile(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idAOOResponsabile) {
        this.idAOOResponsabile = idAOOResponsabile;
    }


    /**
     * Gets the idStrutturaResponsabile value for this RegistroProtocolloPropertiesType.
     * 
     * @return idStrutturaResponsabile
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType getIdStrutturaResponsabile() {
        return idStrutturaResponsabile;
    }


    /**
     * Sets the idStrutturaResponsabile value for this RegistroProtocolloPropertiesType.
     * 
     * @param idStrutturaResponsabile
     */
    public void setIdStrutturaResponsabile(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idStrutturaResponsabile) {
        this.idStrutturaResponsabile = idStrutturaResponsabile;
    }


    /**
     * Gets the idNodoResponsabile value for this RegistroProtocolloPropertiesType.
     * 
     * @return idNodoResponsabile
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType getIdNodoResponsabile() {
        return idNodoResponsabile;
    }


    /**
     * Sets the idNodoResponsabile value for this RegistroProtocolloPropertiesType.
     * 
     * @param idNodoResponsabile
     */
    public void setIdNodoResponsabile(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idNodoResponsabile) {
        this.idNodoResponsabile = idNodoResponsabile;
    }


    /**
     * Gets the idUtenteCreazione value for this RegistroProtocolloPropertiesType.
     * 
     * @return idUtenteCreazione
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType getIdUtenteCreazione() {
        return idUtenteCreazione;
    }


    /**
     * Sets the idUtenteCreazione value for this RegistroProtocolloPropertiesType.
     * 
     * @param idUtenteCreazione
     */
    public void setIdUtenteCreazione(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idUtenteCreazione) {
        this.idUtenteCreazione = idUtenteCreazione;
    }


    /**
     * Gets the idAnnotazioneOB value for this RegistroProtocolloPropertiesType.
     * 
     * @return idAnnotazioneOB
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType[] getIdAnnotazioneOB() {
        return idAnnotazioneOB;
    }


    /**
     * Sets the idAnnotazioneOB value for this RegistroProtocolloPropertiesType.
     * 
     * @param idAnnotazioneOB
     */
    public void setIdAnnotazioneOB(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType[] idAnnotazioneOB) {
        this.idAnnotazioneOB = idAnnotazioneOB;
    }

    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType getIdAnnotazioneOB(int i) {
        return this.idAnnotazioneOB[i];
    }

    public void setIdAnnotazioneOB(int i, it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType _value) {
        this.idAnnotazioneOB[i] = _value;
    }


    /**
     * Gets the idClassificazioneDocStampaDefinitiva value for this RegistroProtocolloPropertiesType.
     * 
     * @return idClassificazioneDocStampaDefinitiva
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType getIdClassificazioneDocStampaDefinitiva() {
        return idClassificazioneDocStampaDefinitiva;
    }


    /**
     * Sets the idClassificazioneDocStampaDefinitiva value for this RegistroProtocolloPropertiesType.
     * 
     * @param idClassificazioneDocStampaDefinitiva
     */
    public void setIdClassificazioneDocStampaDefinitiva(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idClassificazioneDocStampaDefinitiva) {
        this.idClassificazioneDocStampaDefinitiva = idClassificazioneDocStampaDefinitiva;
    }


    /**
     * Gets the idAggregazioneDocStampaDefinitiva value for this RegistroProtocolloPropertiesType.
     * 
     * @return idAggregazioneDocStampaDefinitiva
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType getIdAggregazioneDocStampaDefinitiva() {
        return idAggregazioneDocStampaDefinitiva;
    }


    /**
     * Sets the idAggregazioneDocStampaDefinitiva value for this RegistroProtocolloPropertiesType.
     * 
     * @param idAggregazioneDocStampaDefinitiva
     */
    public void setIdAggregazioneDocStampaDefinitiva(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idAggregazioneDocStampaDefinitiva) {
        this.idAggregazioneDocStampaDefinitiva = idAggregazioneDocStampaDefinitiva;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof RegistroProtocolloPropertiesType)) return false;
        RegistroProtocolloPropertiesType other = (RegistroProtocolloPropertiesType) obj;

        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            this.anno == other.getAnno() &&
            ((this.stato==null && other.getStato()==null) || 
             (this.stato!=null &&
              this.stato.equals(other.getStato()))) &&
            this.storico == other.isStorico() &&
            this.codiceUltimaRegistrazione == other.getCodiceUltimaRegistrazione() &&
            ((this.dataUltimaRegistrazione==null && other.getDataUltimaRegistrazione()==null) || 
             (this.dataUltimaRegistrazione!=null &&
              this.dataUltimaRegistrazione.equals(other.getDataUltimaRegistrazione()))) &&
            ((this.dataCreazione==null && other.getDataCreazione()==null) || 
             (this.dataCreazione!=null &&
              this.dataCreazione.equals(other.getDataCreazione()))) &&
            ((this.dataApertura==null && other.getDataApertura()==null) || 
             (this.dataApertura!=null &&
              this.dataApertura.equals(other.getDataApertura()))) &&
            ((this.dataChiusura==null && other.getDataChiusura()==null) || 
             (this.dataChiusura!=null &&
              this.dataChiusura.equals(other.getDataChiusura()))) &&
            ((this.dataArchiviazione==null && other.getDataArchiviazione()==null) || 
             (this.dataArchiviazione!=null &&
              this.dataArchiviazione.equals(other.getDataArchiviazione()))) &&
            ((this.idAOOProtocollante==null && other.getIdAOOProtocollante()==null) || 
             (this.idAOOProtocollante!=null &&
              this.idAOOProtocollante.equals(other.getIdAOOProtocollante()))) &&
            ((this.idAOOResponsabile==null && other.getIdAOOResponsabile()==null) || 
             (this.idAOOResponsabile!=null &&
              this.idAOOResponsabile.equals(other.getIdAOOResponsabile()))) &&
            ((this.idStrutturaResponsabile==null && other.getIdStrutturaResponsabile()==null) || 
             (this.idStrutturaResponsabile!=null &&
              this.idStrutturaResponsabile.equals(other.getIdStrutturaResponsabile()))) &&
            ((this.idNodoResponsabile==null && other.getIdNodoResponsabile()==null) || 
             (this.idNodoResponsabile!=null &&
              this.idNodoResponsabile.equals(other.getIdNodoResponsabile()))) &&
            ((this.idUtenteCreazione==null && other.getIdUtenteCreazione()==null) || 
             (this.idUtenteCreazione!=null &&
              this.idUtenteCreazione.equals(other.getIdUtenteCreazione()))) &&
            ((this.idAnnotazioneOB==null && other.getIdAnnotazioneOB()==null) || 
             (this.idAnnotazioneOB!=null &&
              java.util.Arrays.equals(this.idAnnotazioneOB, other.getIdAnnotazioneOB()))) &&
            ((this.idClassificazioneDocStampaDefinitiva==null && other.getIdClassificazioneDocStampaDefinitiva()==null) || 
             (this.idClassificazioneDocStampaDefinitiva!=null &&
              this.idClassificazioneDocStampaDefinitiva.equals(other.getIdClassificazioneDocStampaDefinitiva()))) &&
            ((this.idAggregazioneDocStampaDefinitiva==null && other.getIdAggregazioneDocStampaDefinitiva()==null) || 
             (this.idAggregazioneDocStampaDefinitiva!=null &&
              this.idAggregazioneDocStampaDefinitiva.equals(other.getIdAggregazioneDocStampaDefinitiva())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    @SuppressWarnings("deprecation")
	public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = super.hashCode();
        _hashCode += getAnno();
        if (getStato() != null) {
            _hashCode += getStato().hashCode();
        }
        _hashCode += (isStorico() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += new Long(getCodiceUltimaRegistrazione()).hashCode();
        if (getDataUltimaRegistrazione() != null) {
            _hashCode += getDataUltimaRegistrazione().hashCode();
        }
        if (getDataCreazione() != null) {
            _hashCode += getDataCreazione().hashCode();
        }
        if (getDataApertura() != null) {
            _hashCode += getDataApertura().hashCode();
        }
        if (getDataChiusura() != null) {
            _hashCode += getDataChiusura().hashCode();
        }
        if (getDataArchiviazione() != null) {
            _hashCode += getDataArchiviazione().hashCode();
        }
        if (getIdAOOProtocollante() != null) {
            _hashCode += getIdAOOProtocollante().hashCode();
        }
        if (getIdAOOResponsabile() != null) {
            _hashCode += getIdAOOResponsabile().hashCode();
        }
        if (getIdStrutturaResponsabile() != null) {
            _hashCode += getIdStrutturaResponsabile().hashCode();
        }
        if (getIdNodoResponsabile() != null) {
            _hashCode += getIdNodoResponsabile().hashCode();
        }
        if (getIdUtenteCreazione() != null) {
            _hashCode += getIdUtenteCreazione().hashCode();
        }
        if (getIdAnnotazioneOB() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getIdAnnotazioneOB());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getIdAnnotazioneOB(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getIdClassificazioneDocStampaDefinitiva() != null) {
            _hashCode += getIdClassificazioneDocStampaDefinitiva().hashCode();
        }
        if (getIdAggregazioneDocStampaDefinitiva() != null) {
            _hashCode += getIdAggregazioneDocStampaDefinitiva().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RegistroProtocolloPropertiesType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "RegistroProtocolloPropertiesType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("anno");
        elemField.setXmlName(new javax.xml.namespace.QName("", "anno"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("stato");
        elemField.setXmlName(new javax.xml.namespace.QName("", "stato"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "DecodificaType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("storico");
        elemField.setXmlName(new javax.xml.namespace.QName("", "storico"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codiceUltimaRegistrazione");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codiceUltimaRegistrazione"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataUltimaRegistrazione");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataUltimaRegistrazione"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataCreazione");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataCreazione"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataApertura");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataApertura"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataChiusura");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataChiusura"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataArchiviazione");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataArchiviazione"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idAOOProtocollante");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idAOOProtocollante"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idAOOResponsabile");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idAOOResponsabile"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idStrutturaResponsabile");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idStrutturaResponsabile"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idNodoResponsabile");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idNodoResponsabile"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idUtenteCreazione");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idUtenteCreazione"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idAnnotazioneOB");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idAnnotazioneOB"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idClassificazioneDocStampaDefinitiva");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idClassificazioneDocStampaDefinitiva"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idAggregazioneDocStampaDefinitiva");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idAggregazioneDocStampaDefinitiva"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"));
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

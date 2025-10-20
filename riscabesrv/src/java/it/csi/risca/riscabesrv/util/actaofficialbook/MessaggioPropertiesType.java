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
 * MessaggioPropertiesType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actaofficialbook;

public class MessaggioPropertiesType  extends it.csi.risca.riscabesrv.util.actaofficialbook.OfficialBookPropertiesType  implements java.io.Serializable {
    private it.csi.risca.riscabesrv.util.actaofficialbook.DecodificaType stato;

    private it.csi.risca.riscabesrv.util.actaofficialbook.DecodificaType tipo;

    private boolean ricevuto;

    private java.lang.String idMessaggioINPA;

    private java.util.Date dataRicezione;

    private java.util.Date dataAcquisizione;

    private java.util.Date dataInvio;

    private boolean inpaAggiornato;

    private boolean riservato;

    private java.lang.String note;

    private boolean presenzaAllegati;

    private java.lang.String oggetto;

    private it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idRegistrazioneProtocollo;

    private it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idUtenteCreazione;

    private it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idAOO;

    private it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType[] idCorrispondenteMessaggio;

    public MessaggioPropertiesType() {
    }

    public MessaggioPropertiesType(
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType objectId,
           it.csi.risca.riscabesrv.util.actaofficialbook.ChangeTokenType changeToken,
           java.lang.String objectTypeId,
           it.csi.risca.riscabesrv.util.actaofficialbook.DecodificaType stato,
           it.csi.risca.riscabesrv.util.actaofficialbook.DecodificaType tipo,
           boolean ricevuto,
           java.lang.String idMessaggioINPA,
           java.util.Date dataRicezione,
           java.util.Date dataAcquisizione,
           java.util.Date dataInvio,
           boolean inpaAggiornato,
           boolean riservato,
           java.lang.String note,
           boolean presenzaAllegati,
           java.lang.String oggetto,
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idRegistrazioneProtocollo,
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idUtenteCreazione,
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idAOO,
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType[] idCorrispondenteMessaggio) {
        super(
            objectId,
            changeToken,
            objectTypeId);
        this.stato = stato;
        this.tipo = tipo;
        this.ricevuto = ricevuto;
        this.idMessaggioINPA = idMessaggioINPA;
        this.dataRicezione = dataRicezione;
        this.dataAcquisizione = dataAcquisizione;
        this.dataInvio = dataInvio;
        this.inpaAggiornato = inpaAggiornato;
        this.riservato = riservato;
        this.note = note;
        this.presenzaAllegati = presenzaAllegati;
        this.oggetto = oggetto;
        this.idRegistrazioneProtocollo = idRegistrazioneProtocollo;
        this.idUtenteCreazione = idUtenteCreazione;
        this.idAOO = idAOO;
        this.idCorrispondenteMessaggio = idCorrispondenteMessaggio;
    }


    /**
     * Gets the stato value for this MessaggioPropertiesType.
     * 
     * @return stato
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.DecodificaType getStato() {
        return stato;
    }


    /**
     * Sets the stato value for this MessaggioPropertiesType.
     * 
     * @param stato
     */
    public void setStato(it.csi.risca.riscabesrv.util.actaofficialbook.DecodificaType stato) {
        this.stato = stato;
    }


    /**
     * Gets the tipo value for this MessaggioPropertiesType.
     * 
     * @return tipo
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.DecodificaType getTipo() {
        return tipo;
    }


    /**
     * Sets the tipo value for this MessaggioPropertiesType.
     * 
     * @param tipo
     */
    public void setTipo(it.csi.risca.riscabesrv.util.actaofficialbook.DecodificaType tipo) {
        this.tipo = tipo;
    }


    /**
     * Gets the ricevuto value for this MessaggioPropertiesType.
     * 
     * @return ricevuto
     */
    public boolean isRicevuto() {
        return ricevuto;
    }


    /**
     * Sets the ricevuto value for this MessaggioPropertiesType.
     * 
     * @param ricevuto
     */
    public void setRicevuto(boolean ricevuto) {
        this.ricevuto = ricevuto;
    }


    /**
     * Gets the idMessaggioINPA value for this MessaggioPropertiesType.
     * 
     * @return idMessaggioINPA
     */
    public java.lang.String getIdMessaggioINPA() {
        return idMessaggioINPA;
    }


    /**
     * Sets the idMessaggioINPA value for this MessaggioPropertiesType.
     * 
     * @param idMessaggioINPA
     */
    public void setIdMessaggioINPA(java.lang.String idMessaggioINPA) {
        this.idMessaggioINPA = idMessaggioINPA;
    }


    /**
     * Gets the dataRicezione value for this MessaggioPropertiesType.
     * 
     * @return dataRicezione
     */
    public java.util.Date getDataRicezione() {
        return dataRicezione;
    }


    /**
     * Sets the dataRicezione value for this MessaggioPropertiesType.
     * 
     * @param dataRicezione
     */
    public void setDataRicezione(java.util.Date dataRicezione) {
        this.dataRicezione = dataRicezione;
    }


    /**
     * Gets the dataAcquisizione value for this MessaggioPropertiesType.
     * 
     * @return dataAcquisizione
     */
    public java.util.Date getDataAcquisizione() {
        return dataAcquisizione;
    }


    /**
     * Sets the dataAcquisizione value for this MessaggioPropertiesType.
     * 
     * @param dataAcquisizione
     */
    public void setDataAcquisizione(java.util.Date dataAcquisizione) {
        this.dataAcquisizione = dataAcquisizione;
    }


    /**
     * Gets the dataInvio value for this MessaggioPropertiesType.
     * 
     * @return dataInvio
     */
    public java.util.Date getDataInvio() {
        return dataInvio;
    }


    /**
     * Sets the dataInvio value for this MessaggioPropertiesType.
     * 
     * @param dataInvio
     */
    public void setDataInvio(java.util.Date dataInvio) {
        this.dataInvio = dataInvio;
    }


    /**
     * Gets the inpaAggiornato value for this MessaggioPropertiesType.
     * 
     * @return inpaAggiornato
     */
    public boolean isInpaAggiornato() {
        return inpaAggiornato;
    }


    /**
     * Sets the inpaAggiornato value for this MessaggioPropertiesType.
     * 
     * @param inpaAggiornato
     */
    public void setInpaAggiornato(boolean inpaAggiornato) {
        this.inpaAggiornato = inpaAggiornato;
    }


    /**
     * Gets the riservato value for this MessaggioPropertiesType.
     * 
     * @return riservato
     */
    public boolean isRiservato() {
        return riservato;
    }


    /**
     * Sets the riservato value for this MessaggioPropertiesType.
     * 
     * @param riservato
     */
    public void setRiservato(boolean riservato) {
        this.riservato = riservato;
    }


    /**
     * Gets the note value for this MessaggioPropertiesType.
     * 
     * @return note
     */
    public java.lang.String getNote() {
        return note;
    }


    /**
     * Sets the note value for this MessaggioPropertiesType.
     * 
     * @param note
     */
    public void setNote(java.lang.String note) {
        this.note = note;
    }


    /**
     * Gets the presenzaAllegati value for this MessaggioPropertiesType.
     * 
     * @return presenzaAllegati
     */
    public boolean isPresenzaAllegati() {
        return presenzaAllegati;
    }


    /**
     * Sets the presenzaAllegati value for this MessaggioPropertiesType.
     * 
     * @param presenzaAllegati
     */
    public void setPresenzaAllegati(boolean presenzaAllegati) {
        this.presenzaAllegati = presenzaAllegati;
    }


    /**
     * Gets the oggetto value for this MessaggioPropertiesType.
     * 
     * @return oggetto
     */
    public java.lang.String getOggetto() {
        return oggetto;
    }


    /**
     * Sets the oggetto value for this MessaggioPropertiesType.
     * 
     * @param oggetto
     */
    public void setOggetto(java.lang.String oggetto) {
        this.oggetto = oggetto;
    }


    /**
     * Gets the idRegistrazioneProtocollo value for this MessaggioPropertiesType.
     * 
     * @return idRegistrazioneProtocollo
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType getIdRegistrazioneProtocollo() {
        return idRegistrazioneProtocollo;
    }


    /**
     * Sets the idRegistrazioneProtocollo value for this MessaggioPropertiesType.
     * 
     * @param idRegistrazioneProtocollo
     */
    public void setIdRegistrazioneProtocollo(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idRegistrazioneProtocollo) {
        this.idRegistrazioneProtocollo = idRegistrazioneProtocollo;
    }


    /**
     * Gets the idUtenteCreazione value for this MessaggioPropertiesType.
     * 
     * @return idUtenteCreazione
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType getIdUtenteCreazione() {
        return idUtenteCreazione;
    }


    /**
     * Sets the idUtenteCreazione value for this MessaggioPropertiesType.
     * 
     * @param idUtenteCreazione
     */
    public void setIdUtenteCreazione(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idUtenteCreazione) {
        this.idUtenteCreazione = idUtenteCreazione;
    }


    /**
     * Gets the idAOO value for this MessaggioPropertiesType.
     * 
     * @return idAOO
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType getIdAOO() {
        return idAOO;
    }


    /**
     * Sets the idAOO value for this MessaggioPropertiesType.
     * 
     * @param idAOO
     */
    public void setIdAOO(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idAOO) {
        this.idAOO = idAOO;
    }


    /**
     * Gets the idCorrispondenteMessaggio value for this MessaggioPropertiesType.
     * 
     * @return idCorrispondenteMessaggio
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType[] getIdCorrispondenteMessaggio() {
        return idCorrispondenteMessaggio;
    }


    /**
     * Sets the idCorrispondenteMessaggio value for this MessaggioPropertiesType.
     * 
     * @param idCorrispondenteMessaggio
     */
    public void setIdCorrispondenteMessaggio(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType[] idCorrispondenteMessaggio) {
        this.idCorrispondenteMessaggio = idCorrispondenteMessaggio;
    }

    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType getIdCorrispondenteMessaggio(int i) {
        return this.idCorrispondenteMessaggio[i];
    }

    public void setIdCorrispondenteMessaggio(int i, it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType _value) {
        this.idCorrispondenteMessaggio[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof MessaggioPropertiesType)) return false;
        MessaggioPropertiesType other = (MessaggioPropertiesType) obj;

        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.stato==null && other.getStato()==null) || 
             (this.stato!=null &&
              this.stato.equals(other.getStato()))) &&
            ((this.tipo==null && other.getTipo()==null) || 
             (this.tipo!=null &&
              this.tipo.equals(other.getTipo()))) &&
            this.ricevuto == other.isRicevuto() &&
            ((this.idMessaggioINPA==null && other.getIdMessaggioINPA()==null) || 
             (this.idMessaggioINPA!=null &&
              this.idMessaggioINPA.equals(other.getIdMessaggioINPA()))) &&
            ((this.dataRicezione==null && other.getDataRicezione()==null) || 
             (this.dataRicezione!=null &&
              this.dataRicezione.equals(other.getDataRicezione()))) &&
            ((this.dataAcquisizione==null && other.getDataAcquisizione()==null) || 
             (this.dataAcquisizione!=null &&
              this.dataAcquisizione.equals(other.getDataAcquisizione()))) &&
            ((this.dataInvio==null && other.getDataInvio()==null) || 
             (this.dataInvio!=null &&
              this.dataInvio.equals(other.getDataInvio()))) &&
            this.inpaAggiornato == other.isInpaAggiornato() &&
            this.riservato == other.isRiservato() &&
            ((this.note==null && other.getNote()==null) || 
             (this.note!=null &&
              this.note.equals(other.getNote()))) &&
            this.presenzaAllegati == other.isPresenzaAllegati() &&
            ((this.oggetto==null && other.getOggetto()==null) || 
             (this.oggetto!=null &&
              this.oggetto.equals(other.getOggetto()))) &&
            ((this.idRegistrazioneProtocollo==null && other.getIdRegistrazioneProtocollo()==null) || 
             (this.idRegistrazioneProtocollo!=null &&
              this.idRegistrazioneProtocollo.equals(other.getIdRegistrazioneProtocollo()))) &&
            ((this.idUtenteCreazione==null && other.getIdUtenteCreazione()==null) || 
             (this.idUtenteCreazione!=null &&
              this.idUtenteCreazione.equals(other.getIdUtenteCreazione()))) &&
            ((this.idAOO==null && other.getIdAOO()==null) || 
             (this.idAOO!=null &&
              this.idAOO.equals(other.getIdAOO()))) &&
            ((this.idCorrispondenteMessaggio==null && other.getIdCorrispondenteMessaggio()==null) || 
             (this.idCorrispondenteMessaggio!=null &&
              java.util.Arrays.equals(this.idCorrispondenteMessaggio, other.getIdCorrispondenteMessaggio())));
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
        if (getStato() != null) {
            _hashCode += getStato().hashCode();
        }
        if (getTipo() != null) {
            _hashCode += getTipo().hashCode();
        }
        _hashCode += (isRicevuto() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getIdMessaggioINPA() != null) {
            _hashCode += getIdMessaggioINPA().hashCode();
        }
        if (getDataRicezione() != null) {
            _hashCode += getDataRicezione().hashCode();
        }
        if (getDataAcquisizione() != null) {
            _hashCode += getDataAcquisizione().hashCode();
        }
        if (getDataInvio() != null) {
            _hashCode += getDataInvio().hashCode();
        }
        _hashCode += (isInpaAggiornato() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isRiservato() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getNote() != null) {
            _hashCode += getNote().hashCode();
        }
        _hashCode += (isPresenzaAllegati() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getOggetto() != null) {
            _hashCode += getOggetto().hashCode();
        }
        if (getIdRegistrazioneProtocollo() != null) {
            _hashCode += getIdRegistrazioneProtocollo().hashCode();
        }
        if (getIdUtenteCreazione() != null) {
            _hashCode += getIdUtenteCreazione().hashCode();
        }
        if (getIdAOO() != null) {
            _hashCode += getIdAOO().hashCode();
        }
        if (getIdCorrispondenteMessaggio() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getIdCorrispondenteMessaggio());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getIdCorrispondenteMessaggio(), i);
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
        new org.apache.axis.description.TypeDesc(MessaggioPropertiesType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "MessaggioPropertiesType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("stato");
        elemField.setXmlName(new javax.xml.namespace.QName("", "stato"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "DecodificaType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tipo"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "DecodificaType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ricevuto");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ricevuto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idMessaggioINPA");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idMessaggioINPA"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataRicezione");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataRicezione"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataAcquisizione");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataAcquisizione"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataInvio");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataInvio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("inpaAggiornato");
        elemField.setXmlName(new javax.xml.namespace.QName("", "inpaAggiornato"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("riservato");
        elemField.setXmlName(new javax.xml.namespace.QName("", "riservato"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("note");
        elemField.setXmlName(new javax.xml.namespace.QName("", "note"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("presenzaAllegati");
        elemField.setXmlName(new javax.xml.namespace.QName("", "presenzaAllegati"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("oggetto");
        elemField.setXmlName(new javax.xml.namespace.QName("", "oggetto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idRegistrazioneProtocollo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idRegistrazioneProtocollo"));
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
        elemField.setFieldName("idAOO");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idAOO"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idCorrispondenteMessaggio");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idCorrispondenteMessaggio"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"));
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

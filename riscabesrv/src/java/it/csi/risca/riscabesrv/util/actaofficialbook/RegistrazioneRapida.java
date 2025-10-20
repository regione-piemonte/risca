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
 * RegistrazioneRapida.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actaofficialbook;

public class RegistrazioneRapida  extends it.csi.risca.riscabesrv.util.actaofficialbook.RegistrazioneRequest  implements java.io.Serializable {
    private java.lang.String oggetto;

    private java.lang.String cognomeSoggetto;

    private java.lang.String nomeSoggetto;

    private java.lang.String denominazionePG;

    private boolean inserimentoMittenteInAnagrafica;

    private java.lang.String classificazioneEstesaPerArchiviazione;

    private java.lang.String collocazioneCartacea;

    private it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType folderId;

    private java.lang.String folderType;

    private java.lang.String tipoSoggetto;

    private it.csi.risca.riscabesrv.util.actaofficialbook.IdentificazioneProtocollante protocollante;

    private it.csi.risca.riscabesrv.util.actaofficialbook.InfoCreazioneAnnotazioneOB annotazione;

    private it.csi.risca.riscabesrv.util.actaofficialbook.MittenteEsterno mittenteEsterno;

    private it.csi.risca.riscabesrv.util.actaofficialbook.DestinatarioInterno destinatarioInterno;

    private boolean stampaRicevuta;

    private boolean forzareSePresenzaInviti;

    private boolean creazioneAsincronaDocumento;

    public RegistrazioneRapida() {
    }

    public RegistrazioneRapida(
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType registroId,
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType aooProtocollanteId,
           boolean senzaCreazioneSoggettiEsterni,
           java.lang.String oggetto,
           java.lang.String cognomeSoggetto,
           java.lang.String nomeSoggetto,
           java.lang.String denominazionePG,
           boolean inserimentoMittenteInAnagrafica,
           java.lang.String classificazioneEstesaPerArchiviazione,
           java.lang.String collocazioneCartacea,
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType folderId,
           java.lang.String folderType,
           java.lang.String tipoSoggetto,
           it.csi.risca.riscabesrv.util.actaofficialbook.IdentificazioneProtocollante protocollante,
           it.csi.risca.riscabesrv.util.actaofficialbook.InfoCreazioneAnnotazioneOB annotazione,
           it.csi.risca.riscabesrv.util.actaofficialbook.MittenteEsterno mittenteEsterno,
           it.csi.risca.riscabesrv.util.actaofficialbook.DestinatarioInterno destinatarioInterno,
           boolean stampaRicevuta,
           boolean forzareSePresenzaInviti,
           boolean creazioneAsincronaDocumento) {
        super(
            registroId,
            aooProtocollanteId,
            senzaCreazioneSoggettiEsterni);
        this.oggetto = oggetto;
        this.cognomeSoggetto = cognomeSoggetto;
        this.nomeSoggetto = nomeSoggetto;
        this.denominazionePG = denominazionePG;
        this.inserimentoMittenteInAnagrafica = inserimentoMittenteInAnagrafica;
        this.classificazioneEstesaPerArchiviazione = classificazioneEstesaPerArchiviazione;
        this.collocazioneCartacea = collocazioneCartacea;
        this.folderId = folderId;
        this.folderType = folderType;
        this.tipoSoggetto = tipoSoggetto;
        this.protocollante = protocollante;
        this.annotazione = annotazione;
        this.mittenteEsterno = mittenteEsterno;
        this.destinatarioInterno = destinatarioInterno;
        this.stampaRicevuta = stampaRicevuta;
        this.forzareSePresenzaInviti = forzareSePresenzaInviti;
        this.creazioneAsincronaDocumento = creazioneAsincronaDocumento;
    }


    /**
     * Gets the oggetto value for this RegistrazioneRapida.
     * 
     * @return oggetto
     */
    public java.lang.String getOggetto() {
        return oggetto;
    }


    /**
     * Sets the oggetto value for this RegistrazioneRapida.
     * 
     * @param oggetto
     */
    public void setOggetto(java.lang.String oggetto) {
        this.oggetto = oggetto;
    }


    /**
     * Gets the cognomeSoggetto value for this RegistrazioneRapida.
     * 
     * @return cognomeSoggetto
     */
    public java.lang.String getCognomeSoggetto() {
        return cognomeSoggetto;
    }


    /**
     * Sets the cognomeSoggetto value for this RegistrazioneRapida.
     * 
     * @param cognomeSoggetto
     */
    public void setCognomeSoggetto(java.lang.String cognomeSoggetto) {
        this.cognomeSoggetto = cognomeSoggetto;
    }


    /**
     * Gets the nomeSoggetto value for this RegistrazioneRapida.
     * 
     * @return nomeSoggetto
     */
    public java.lang.String getNomeSoggetto() {
        return nomeSoggetto;
    }


    /**
     * Sets the nomeSoggetto value for this RegistrazioneRapida.
     * 
     * @param nomeSoggetto
     */
    public void setNomeSoggetto(java.lang.String nomeSoggetto) {
        this.nomeSoggetto = nomeSoggetto;
    }


    /**
     * Gets the denominazionePG value for this RegistrazioneRapida.
     * 
     * @return denominazionePG
     */
    public java.lang.String getDenominazionePG() {
        return denominazionePG;
    }


    /**
     * Sets the denominazionePG value for this RegistrazioneRapida.
     * 
     * @param denominazionePG
     */
    public void setDenominazionePG(java.lang.String denominazionePG) {
        this.denominazionePG = denominazionePG;
    }


    /**
     * Gets the inserimentoMittenteInAnagrafica value for this RegistrazioneRapida.
     * 
     * @return inserimentoMittenteInAnagrafica
     */
    public boolean isInserimentoMittenteInAnagrafica() {
        return inserimentoMittenteInAnagrafica;
    }


    /**
     * Sets the inserimentoMittenteInAnagrafica value for this RegistrazioneRapida.
     * 
     * @param inserimentoMittenteInAnagrafica
     */
    public void setInserimentoMittenteInAnagrafica(boolean inserimentoMittenteInAnagrafica) {
        this.inserimentoMittenteInAnagrafica = inserimentoMittenteInAnagrafica;
    }


    /**
     * Gets the classificazioneEstesaPerArchiviazione value for this RegistrazioneRapida.
     * 
     * @return classificazioneEstesaPerArchiviazione
     */
    public java.lang.String getClassificazioneEstesaPerArchiviazione() {
        return classificazioneEstesaPerArchiviazione;
    }


    /**
     * Sets the classificazioneEstesaPerArchiviazione value for this RegistrazioneRapida.
     * 
     * @param classificazioneEstesaPerArchiviazione
     */
    public void setClassificazioneEstesaPerArchiviazione(java.lang.String classificazioneEstesaPerArchiviazione) {
        this.classificazioneEstesaPerArchiviazione = classificazioneEstesaPerArchiviazione;
    }


    /**
     * Gets the collocazioneCartacea value for this RegistrazioneRapida.
     * 
     * @return collocazioneCartacea
     */
    public java.lang.String getCollocazioneCartacea() {
        return collocazioneCartacea;
    }


    /**
     * Sets the collocazioneCartacea value for this RegistrazioneRapida.
     * 
     * @param collocazioneCartacea
     */
    public void setCollocazioneCartacea(java.lang.String collocazioneCartacea) {
        this.collocazioneCartacea = collocazioneCartacea;
    }


    /**
     * Gets the folderId value for this RegistrazioneRapida.
     * 
     * @return folderId
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType getFolderId() {
        return folderId;
    }


    /**
     * Sets the folderId value for this RegistrazioneRapida.
     * 
     * @param folderId
     */
    public void setFolderId(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType folderId) {
        this.folderId = folderId;
    }


    /**
     * Gets the folderType value for this RegistrazioneRapida.
     * 
     * @return folderType
     */
    public java.lang.String getFolderType() {
        return folderType;
    }


    /**
     * Sets the folderType value for this RegistrazioneRapida.
     * 
     * @param folderType
     */
    public void setFolderType(java.lang.String folderType) {
        this.folderType = folderType;
    }


    /**
     * Gets the tipoSoggetto value for this RegistrazioneRapida.
     * 
     * @return tipoSoggetto
     */
    public java.lang.String getTipoSoggetto() {
        return tipoSoggetto;
    }


    /**
     * Sets the tipoSoggetto value for this RegistrazioneRapida.
     * 
     * @param tipoSoggetto
     */
    public void setTipoSoggetto(java.lang.String tipoSoggetto) {
        this.tipoSoggetto = tipoSoggetto;
    }


    /**
     * Gets the protocollante value for this RegistrazioneRapida.
     * 
     * @return protocollante
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.IdentificazioneProtocollante getProtocollante() {
        return protocollante;
    }


    /**
     * Sets the protocollante value for this RegistrazioneRapida.
     * 
     * @param protocollante
     */
    public void setProtocollante(it.csi.risca.riscabesrv.util.actaofficialbook.IdentificazioneProtocollante protocollante) {
        this.protocollante = protocollante;
    }


    /**
     * Gets the annotazione value for this RegistrazioneRapida.
     * 
     * @return annotazione
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.InfoCreazioneAnnotazioneOB getAnnotazione() {
        return annotazione;
    }


    /**
     * Sets the annotazione value for this RegistrazioneRapida.
     * 
     * @param annotazione
     */
    public void setAnnotazione(it.csi.risca.riscabesrv.util.actaofficialbook.InfoCreazioneAnnotazioneOB annotazione) {
        this.annotazione = annotazione;
    }


    /**
     * Gets the mittenteEsterno value for this RegistrazioneRapida.
     * 
     * @return mittenteEsterno
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.MittenteEsterno getMittenteEsterno() {
        return mittenteEsterno;
    }


    /**
     * Sets the mittenteEsterno value for this RegistrazioneRapida.
     * 
     * @param mittenteEsterno
     */
    public void setMittenteEsterno(it.csi.risca.riscabesrv.util.actaofficialbook.MittenteEsterno mittenteEsterno) {
        this.mittenteEsterno = mittenteEsterno;
    }


    /**
     * Gets the destinatarioInterno value for this RegistrazioneRapida.
     * 
     * @return destinatarioInterno
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.DestinatarioInterno getDestinatarioInterno() {
        return destinatarioInterno;
    }


    /**
     * Sets the destinatarioInterno value for this RegistrazioneRapida.
     * 
     * @param destinatarioInterno
     */
    public void setDestinatarioInterno(it.csi.risca.riscabesrv.util.actaofficialbook.DestinatarioInterno destinatarioInterno) {
        this.destinatarioInterno = destinatarioInterno;
    }


    /**
     * Gets the stampaRicevuta value for this RegistrazioneRapida.
     * 
     * @return stampaRicevuta
     */
    public boolean isStampaRicevuta() {
        return stampaRicevuta;
    }


    /**
     * Sets the stampaRicevuta value for this RegistrazioneRapida.
     * 
     * @param stampaRicevuta
     */
    public void setStampaRicevuta(boolean stampaRicevuta) {
        this.stampaRicevuta = stampaRicevuta;
    }


    /**
     * Gets the forzareSePresenzaInviti value for this RegistrazioneRapida.
     * 
     * @return forzareSePresenzaInviti
     */
    public boolean isForzareSePresenzaInviti() {
        return forzareSePresenzaInviti;
    }


    /**
     * Sets the forzareSePresenzaInviti value for this RegistrazioneRapida.
     * 
     * @param forzareSePresenzaInviti
     */
    public void setForzareSePresenzaInviti(boolean forzareSePresenzaInviti) {
        this.forzareSePresenzaInviti = forzareSePresenzaInviti;
    }


    /**
     * Gets the creazioneAsincronaDocumento value for this RegistrazioneRapida.
     * 
     * @return creazioneAsincronaDocumento
     */
    public boolean isCreazioneAsincronaDocumento() {
        return creazioneAsincronaDocumento;
    }


    /**
     * Sets the creazioneAsincronaDocumento value for this RegistrazioneRapida.
     * 
     * @param creazioneAsincronaDocumento
     */
    public void setCreazioneAsincronaDocumento(boolean creazioneAsincronaDocumento) {
        this.creazioneAsincronaDocumento = creazioneAsincronaDocumento;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof RegistrazioneRapida)) return false;
        RegistrazioneRapida other = (RegistrazioneRapida) obj;

        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.oggetto==null && other.getOggetto()==null) || 
             (this.oggetto!=null &&
              this.oggetto.equals(other.getOggetto()))) &&
            ((this.cognomeSoggetto==null && other.getCognomeSoggetto()==null) || 
             (this.cognomeSoggetto!=null &&
              this.cognomeSoggetto.equals(other.getCognomeSoggetto()))) &&
            ((this.nomeSoggetto==null && other.getNomeSoggetto()==null) || 
             (this.nomeSoggetto!=null &&
              this.nomeSoggetto.equals(other.getNomeSoggetto()))) &&
            ((this.denominazionePG==null && other.getDenominazionePG()==null) || 
             (this.denominazionePG!=null &&
              this.denominazionePG.equals(other.getDenominazionePG()))) &&
            this.inserimentoMittenteInAnagrafica == other.isInserimentoMittenteInAnagrafica() &&
            ((this.classificazioneEstesaPerArchiviazione==null && other.getClassificazioneEstesaPerArchiviazione()==null) || 
             (this.classificazioneEstesaPerArchiviazione!=null &&
              this.classificazioneEstesaPerArchiviazione.equals(other.getClassificazioneEstesaPerArchiviazione()))) &&
            ((this.collocazioneCartacea==null && other.getCollocazioneCartacea()==null) || 
             (this.collocazioneCartacea!=null &&
              this.collocazioneCartacea.equals(other.getCollocazioneCartacea()))) &&
            ((this.folderId==null && other.getFolderId()==null) || 
             (this.folderId!=null &&
              this.folderId.equals(other.getFolderId()))) &&
            ((this.folderType==null && other.getFolderType()==null) || 
             (this.folderType!=null &&
              this.folderType.equals(other.getFolderType()))) &&
            ((this.tipoSoggetto==null && other.getTipoSoggetto()==null) || 
             (this.tipoSoggetto!=null &&
              this.tipoSoggetto.equals(other.getTipoSoggetto()))) &&
            ((this.protocollante==null && other.getProtocollante()==null) || 
             (this.protocollante!=null &&
              this.protocollante.equals(other.getProtocollante()))) &&
            ((this.annotazione==null && other.getAnnotazione()==null) || 
             (this.annotazione!=null &&
              this.annotazione.equals(other.getAnnotazione()))) &&
            ((this.mittenteEsterno==null && other.getMittenteEsterno()==null) || 
             (this.mittenteEsterno!=null &&
              this.mittenteEsterno.equals(other.getMittenteEsterno()))) &&
            ((this.destinatarioInterno==null && other.getDestinatarioInterno()==null) || 
             (this.destinatarioInterno!=null &&
              this.destinatarioInterno.equals(other.getDestinatarioInterno()))) &&
            this.stampaRicevuta == other.isStampaRicevuta() &&
            this.forzareSePresenzaInviti == other.isForzareSePresenzaInviti() &&
            this.creazioneAsincronaDocumento == other.isCreazioneAsincronaDocumento();
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
        if (getOggetto() != null) {
            _hashCode += getOggetto().hashCode();
        }
        if (getCognomeSoggetto() != null) {
            _hashCode += getCognomeSoggetto().hashCode();
        }
        if (getNomeSoggetto() != null) {
            _hashCode += getNomeSoggetto().hashCode();
        }
        if (getDenominazionePG() != null) {
            _hashCode += getDenominazionePG().hashCode();
        }
        _hashCode += (isInserimentoMittenteInAnagrafica() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getClassificazioneEstesaPerArchiviazione() != null) {
            _hashCode += getClassificazioneEstesaPerArchiviazione().hashCode();
        }
        if (getCollocazioneCartacea() != null) {
            _hashCode += getCollocazioneCartacea().hashCode();
        }
        if (getFolderId() != null) {
            _hashCode += getFolderId().hashCode();
        }
        if (getFolderType() != null) {
            _hashCode += getFolderType().hashCode();
        }
        if (getTipoSoggetto() != null) {
            _hashCode += getTipoSoggetto().hashCode();
        }
        if (getProtocollante() != null) {
            _hashCode += getProtocollante().hashCode();
        }
        if (getAnnotazione() != null) {
            _hashCode += getAnnotazione().hashCode();
        }
        if (getMittenteEsterno() != null) {
            _hashCode += getMittenteEsterno().hashCode();
        }
        if (getDestinatarioInterno() != null) {
            _hashCode += getDestinatarioInterno().hashCode();
        }
        _hashCode += (isStampaRicevuta() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isForzareSePresenzaInviti() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isCreazioneAsincronaDocumento() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RegistrazioneRapida.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "RegistrazioneRapida"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("oggetto");
        elemField.setXmlName(new javax.xml.namespace.QName("", "oggetto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cognomeSoggetto");
        elemField.setXmlName(new javax.xml.namespace.QName("", "cognomeSoggetto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nomeSoggetto");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nomeSoggetto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("denominazionePG");
        elemField.setXmlName(new javax.xml.namespace.QName("", "denominazionePG"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("inserimentoMittenteInAnagrafica");
        elemField.setXmlName(new javax.xml.namespace.QName("", "inserimentoMittenteInAnagrafica"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("classificazioneEstesaPerArchiviazione");
        elemField.setXmlName(new javax.xml.namespace.QName("", "classificazioneEstesaPerArchiviazione"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("collocazioneCartacea");
        elemField.setXmlName(new javax.xml.namespace.QName("", "collocazioneCartacea"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("folderId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "folderId"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("folderType");
        elemField.setXmlName(new javax.xml.namespace.QName("", "folderType"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoSoggetto");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tipoSoggetto"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("protocollante");
        elemField.setXmlName(new javax.xml.namespace.QName("", "protocollante"));
        elemField.setXmlType(new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "IdentificazioneProtocollante"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("annotazione");
        elemField.setXmlName(new javax.xml.namespace.QName("", "annotazione"));
        elemField.setXmlType(new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "InfoCreazioneAnnotazioneOB"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mittenteEsterno");
        elemField.setXmlName(new javax.xml.namespace.QName("", "mittenteEsterno"));
        elemField.setXmlType(new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "MittenteEsterno"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("destinatarioInterno");
        elemField.setXmlName(new javax.xml.namespace.QName("", "destinatarioInterno"));
        elemField.setXmlType(new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "DestinatarioInterno"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("stampaRicevuta");
        elemField.setXmlName(new javax.xml.namespace.QName("", "stampaRicevuta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("forzareSePresenzaInviti");
        elemField.setXmlName(new javax.xml.namespace.QName("", "forzareSePresenzaInviti"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("creazioneAsincronaDocumento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "creazioneAsincronaDocumento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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

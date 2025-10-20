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
 * InfoCreazioneRegistrazione.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actaofficialbook;

public class InfoCreazioneRegistrazione  implements java.io.Serializable {
    private java.lang.String numeroRegistrazionePrecedente;

    private java.lang.String annoRegistrazionePrecedente;

    private boolean registrazioneRiservata;

    private java.lang.String oggetto;

    private java.lang.String collocazioneCartacea;

    private java.lang.Long mezzoTrasmissivoId;

    private boolean forzareSeRegistrazioneSimile;

    private boolean documentoRiservato;

    private java.lang.String classificazioneProposta;

    private boolean forzareSeRegistrazionePropostaInvalida;

    private java.util.Date dataDocumento;

    private java.lang.String[] descrizioneAllegato;

    private it.csi.risca.riscabesrv.util.actaofficialbook.MittenteInterno[] mittenteInterno;

    private it.csi.risca.riscabesrv.util.actaofficialbook.DestinatarioInterno[] destinatarioInterno;

    private it.csi.risca.riscabesrv.util.actaofficialbook.DestinatarioEsterno[] destinatarioEsterno;

    private it.csi.risca.riscabesrv.util.actaofficialbook.IdentificazioneProtocollante protocollante;

    private it.csi.risca.riscabesrv.util.actaofficialbook.InfoCreazioneAnnotazioneOB annotazione;

    private boolean forzareSePresenzaInviti;

    private boolean forzareSePresenzaDaInoltrare;

    public InfoCreazioneRegistrazione() {
    }

    public InfoCreazioneRegistrazione(
           java.lang.String numeroRegistrazionePrecedente,
           java.lang.String annoRegistrazionePrecedente,
           boolean registrazioneRiservata,
           java.lang.String oggetto,
           java.lang.String collocazioneCartacea,
           java.lang.Long mezzoTrasmissivoId,
           boolean forzareSeRegistrazioneSimile,
           boolean documentoRiservato,
           java.lang.String classificazioneProposta,
           boolean forzareSeRegistrazionePropostaInvalida,
           java.util.Date dataDocumento,
           java.lang.String[] descrizioneAllegato,
           it.csi.risca.riscabesrv.util.actaofficialbook.MittenteInterno[] mittenteInterno,
           it.csi.risca.riscabesrv.util.actaofficialbook.DestinatarioInterno[] destinatarioInterno,
           it.csi.risca.riscabesrv.util.actaofficialbook.DestinatarioEsterno[] destinatarioEsterno,
           it.csi.risca.riscabesrv.util.actaofficialbook.IdentificazioneProtocollante protocollante,
           it.csi.risca.riscabesrv.util.actaofficialbook.InfoCreazioneAnnotazioneOB annotazione,
           boolean forzareSePresenzaInviti,
           boolean forzareSePresenzaDaInoltrare) {
           this.numeroRegistrazionePrecedente = numeroRegistrazionePrecedente;
           this.annoRegistrazionePrecedente = annoRegistrazionePrecedente;
           this.registrazioneRiservata = registrazioneRiservata;
           this.oggetto = oggetto;
           this.collocazioneCartacea = collocazioneCartacea;
           this.mezzoTrasmissivoId = mezzoTrasmissivoId;
           this.forzareSeRegistrazioneSimile = forzareSeRegistrazioneSimile;
           this.documentoRiservato = documentoRiservato;
           this.classificazioneProposta = classificazioneProposta;
           this.forzareSeRegistrazionePropostaInvalida = forzareSeRegistrazionePropostaInvalida;
           this.dataDocumento = dataDocumento;
           this.descrizioneAllegato = descrizioneAllegato;
           this.mittenteInterno = mittenteInterno;
           this.destinatarioInterno = destinatarioInterno;
           this.destinatarioEsterno = destinatarioEsterno;
           this.protocollante = protocollante;
           this.annotazione = annotazione;
           this.forzareSePresenzaInviti = forzareSePresenzaInviti;
           this.forzareSePresenzaDaInoltrare = forzareSePresenzaDaInoltrare;
    }


    /**
     * Gets the numeroRegistrazionePrecedente value for this InfoCreazioneRegistrazione.
     * 
     * @return numeroRegistrazionePrecedente
     */
    public java.lang.String getNumeroRegistrazionePrecedente() {
        return numeroRegistrazionePrecedente;
    }


    /**
     * Sets the numeroRegistrazionePrecedente value for this InfoCreazioneRegistrazione.
     * 
     * @param numeroRegistrazionePrecedente
     */
    public void setNumeroRegistrazionePrecedente(java.lang.String numeroRegistrazionePrecedente) {
        this.numeroRegistrazionePrecedente = numeroRegistrazionePrecedente;
    }


    /**
     * Gets the annoRegistrazionePrecedente value for this InfoCreazioneRegistrazione.
     * 
     * @return annoRegistrazionePrecedente
     */
    public java.lang.String getAnnoRegistrazionePrecedente() {
        return annoRegistrazionePrecedente;
    }


    /**
     * Sets the annoRegistrazionePrecedente value for this InfoCreazioneRegistrazione.
     * 
     * @param annoRegistrazionePrecedente
     */
    public void setAnnoRegistrazionePrecedente(java.lang.String annoRegistrazionePrecedente) {
        this.annoRegistrazionePrecedente = annoRegistrazionePrecedente;
    }


    /**
     * Gets the registrazioneRiservata value for this InfoCreazioneRegistrazione.
     * 
     * @return registrazioneRiservata
     */
    public boolean isRegistrazioneRiservata() {
        return registrazioneRiservata;
    }


    /**
     * Sets the registrazioneRiservata value for this InfoCreazioneRegistrazione.
     * 
     * @param registrazioneRiservata
     */
    public void setRegistrazioneRiservata(boolean registrazioneRiservata) {
        this.registrazioneRiservata = registrazioneRiservata;
    }


    /**
     * Gets the oggetto value for this InfoCreazioneRegistrazione.
     * 
     * @return oggetto
     */
    public java.lang.String getOggetto() {
        return oggetto;
    }


    /**
     * Sets the oggetto value for this InfoCreazioneRegistrazione.
     * 
     * @param oggetto
     */
    public void setOggetto(java.lang.String oggetto) {
        this.oggetto = oggetto;
    }


    /**
     * Gets the collocazioneCartacea value for this InfoCreazioneRegistrazione.
     * 
     * @return collocazioneCartacea
     */
    public java.lang.String getCollocazioneCartacea() {
        return collocazioneCartacea;
    }


    /**
     * Sets the collocazioneCartacea value for this InfoCreazioneRegistrazione.
     * 
     * @param collocazioneCartacea
     */
    public void setCollocazioneCartacea(java.lang.String collocazioneCartacea) {
        this.collocazioneCartacea = collocazioneCartacea;
    }


    /**
     * Gets the mezzoTrasmissivoId value for this InfoCreazioneRegistrazione.
     * 
     * @return mezzoTrasmissivoId
     */
    public java.lang.Long getMezzoTrasmissivoId() {
        return mezzoTrasmissivoId;
    }


    /**
     * Sets the mezzoTrasmissivoId value for this InfoCreazioneRegistrazione.
     * 
     * @param mezzoTrasmissivoId
     */
    public void setMezzoTrasmissivoId(java.lang.Long mezzoTrasmissivoId) {
        this.mezzoTrasmissivoId = mezzoTrasmissivoId;
    }


    /**
     * Gets the forzareSeRegistrazioneSimile value for this InfoCreazioneRegistrazione.
     * 
     * @return forzareSeRegistrazioneSimile
     */
    public boolean isForzareSeRegistrazioneSimile() {
        return forzareSeRegistrazioneSimile;
    }


    /**
     * Sets the forzareSeRegistrazioneSimile value for this InfoCreazioneRegistrazione.
     * 
     * @param forzareSeRegistrazioneSimile
     */
    public void setForzareSeRegistrazioneSimile(boolean forzareSeRegistrazioneSimile) {
        this.forzareSeRegistrazioneSimile = forzareSeRegistrazioneSimile;
    }


    /**
     * Gets the documentoRiservato value for this InfoCreazioneRegistrazione.
     * 
     * @return documentoRiservato
     */
    public boolean isDocumentoRiservato() {
        return documentoRiservato;
    }


    /**
     * Sets the documentoRiservato value for this InfoCreazioneRegistrazione.
     * 
     * @param documentoRiservato
     */
    public void setDocumentoRiservato(boolean documentoRiservato) {
        this.documentoRiservato = documentoRiservato;
    }


    /**
     * Gets the classificazioneProposta value for this InfoCreazioneRegistrazione.
     * 
     * @return classificazioneProposta
     */
    public java.lang.String getClassificazioneProposta() {
        return classificazioneProposta;
    }


    /**
     * Sets the classificazioneProposta value for this InfoCreazioneRegistrazione.
     * 
     * @param classificazioneProposta
     */
    public void setClassificazioneProposta(java.lang.String classificazioneProposta) {
        this.classificazioneProposta = classificazioneProposta;
    }


    /**
     * Gets the forzareSeRegistrazionePropostaInvalida value for this InfoCreazioneRegistrazione.
     * 
     * @return forzareSeRegistrazionePropostaInvalida
     */
    public boolean isForzareSeRegistrazionePropostaInvalida() {
        return forzareSeRegistrazionePropostaInvalida;
    }


    /**
     * Sets the forzareSeRegistrazionePropostaInvalida value for this InfoCreazioneRegistrazione.
     * 
     * @param forzareSeRegistrazionePropostaInvalida
     */
    public void setForzareSeRegistrazionePropostaInvalida(boolean forzareSeRegistrazionePropostaInvalida) {
        this.forzareSeRegistrazionePropostaInvalida = forzareSeRegistrazionePropostaInvalida;
    }


    /**
     * Gets the dataDocumento value for this InfoCreazioneRegistrazione.
     * 
     * @return dataDocumento
     */
    public java.util.Date getDataDocumento() {
        return dataDocumento;
    }


    /**
     * Sets the dataDocumento value for this InfoCreazioneRegistrazione.
     * 
     * @param dataDocumento
     */
    public void setDataDocumento(java.util.Date dataDocumento) {
        this.dataDocumento = dataDocumento;
    }


    /**
     * Gets the descrizioneAllegato value for this InfoCreazioneRegistrazione.
     * 
     * @return descrizioneAllegato
     */
    public java.lang.String[] getDescrizioneAllegato() {
        return descrizioneAllegato;
    }


    /**
     * Sets the descrizioneAllegato value for this InfoCreazioneRegistrazione.
     * 
     * @param descrizioneAllegato
     */
    public void setDescrizioneAllegato(java.lang.String[] descrizioneAllegato) {
        this.descrizioneAllegato = descrizioneAllegato;
    }

    public java.lang.String getDescrizioneAllegato(int i) {
        return this.descrizioneAllegato[i];
    }

    public void setDescrizioneAllegato(int i, java.lang.String _value) {
        this.descrizioneAllegato[i] = _value;
    }


    /**
     * Gets the mittenteInterno value for this InfoCreazioneRegistrazione.
     * 
     * @return mittenteInterno
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.MittenteInterno[] getMittenteInterno() {
        return mittenteInterno;
    }


    /**
     * Sets the mittenteInterno value for this InfoCreazioneRegistrazione.
     * 
     * @param mittenteInterno
     */
    public void setMittenteInterno(it.csi.risca.riscabesrv.util.actaofficialbook.MittenteInterno[] mittenteInterno) {
        this.mittenteInterno = mittenteInterno;
    }

    public it.csi.risca.riscabesrv.util.actaofficialbook.MittenteInterno getMittenteInterno(int i) {
        return this.mittenteInterno[i];
    }

    public void setMittenteInterno(int i, it.csi.risca.riscabesrv.util.actaofficialbook.MittenteInterno _value) {
        this.mittenteInterno[i] = _value;
    }


    /**
     * Gets the destinatarioInterno value for this InfoCreazioneRegistrazione.
     * 
     * @return destinatarioInterno
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.DestinatarioInterno[] getDestinatarioInterno() {
        return destinatarioInterno;
    }


    /**
     * Sets the destinatarioInterno value for this InfoCreazioneRegistrazione.
     * 
     * @param destinatarioInterno
     */
    public void setDestinatarioInterno(it.csi.risca.riscabesrv.util.actaofficialbook.DestinatarioInterno[] destinatarioInterno) {
        this.destinatarioInterno = destinatarioInterno;
    }

    public it.csi.risca.riscabesrv.util.actaofficialbook.DestinatarioInterno getDestinatarioInterno(int i) {
        return this.destinatarioInterno[i];
    }

    public void setDestinatarioInterno(int i, it.csi.risca.riscabesrv.util.actaofficialbook.DestinatarioInterno _value) {
        this.destinatarioInterno[i] = _value;
    }


    /**
     * Gets the destinatarioEsterno value for this InfoCreazioneRegistrazione.
     * 
     * @return destinatarioEsterno
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.DestinatarioEsterno[] getDestinatarioEsterno() {
        return destinatarioEsterno;
    }


    /**
     * Sets the destinatarioEsterno value for this InfoCreazioneRegistrazione.
     * 
     * @param destinatarioEsterno
     */
    public void setDestinatarioEsterno(it.csi.risca.riscabesrv.util.actaofficialbook.DestinatarioEsterno[] destinatarioEsterno) {
        this.destinatarioEsterno = destinatarioEsterno;
    }

    public it.csi.risca.riscabesrv.util.actaofficialbook.DestinatarioEsterno getDestinatarioEsterno(int i) {
        return this.destinatarioEsterno[i];
    }

    public void setDestinatarioEsterno(int i, it.csi.risca.riscabesrv.util.actaofficialbook.DestinatarioEsterno _value) {
        this.destinatarioEsterno[i] = _value;
    }


    /**
     * Gets the protocollante value for this InfoCreazioneRegistrazione.
     * 
     * @return protocollante
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.IdentificazioneProtocollante getProtocollante() {
        return protocollante;
    }


    /**
     * Sets the protocollante value for this InfoCreazioneRegistrazione.
     * 
     * @param protocollante
     */
    public void setProtocollante(it.csi.risca.riscabesrv.util.actaofficialbook.IdentificazioneProtocollante protocollante) {
        this.protocollante = protocollante;
    }


    /**
     * Gets the annotazione value for this InfoCreazioneRegistrazione.
     * 
     * @return annotazione
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.InfoCreazioneAnnotazioneOB getAnnotazione() {
        return annotazione;
    }


    /**
     * Sets the annotazione value for this InfoCreazioneRegistrazione.
     * 
     * @param annotazione
     */
    public void setAnnotazione(it.csi.risca.riscabesrv.util.actaofficialbook.InfoCreazioneAnnotazioneOB annotazione) {
        this.annotazione = annotazione;
    }


    /**
     * Gets the forzareSePresenzaInviti value for this InfoCreazioneRegistrazione.
     * 
     * @return forzareSePresenzaInviti
     */
    public boolean isForzareSePresenzaInviti() {
        return forzareSePresenzaInviti;
    }


    /**
     * Sets the forzareSePresenzaInviti value for this InfoCreazioneRegistrazione.
     * 
     * @param forzareSePresenzaInviti
     */
    public void setForzareSePresenzaInviti(boolean forzareSePresenzaInviti) {
        this.forzareSePresenzaInviti = forzareSePresenzaInviti;
    }


    /**
     * Gets the forzareSePresenzaDaInoltrare value for this InfoCreazioneRegistrazione.
     * 
     * @return forzareSePresenzaDaInoltrare
     */
    public boolean isForzareSePresenzaDaInoltrare() {
        return forzareSePresenzaDaInoltrare;
    }


    /**
     * Sets the forzareSePresenzaDaInoltrare value for this InfoCreazioneRegistrazione.
     * 
     * @param forzareSePresenzaDaInoltrare
     */
    public void setForzareSePresenzaDaInoltrare(boolean forzareSePresenzaDaInoltrare) {
        this.forzareSePresenzaDaInoltrare = forzareSePresenzaDaInoltrare;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof InfoCreazioneRegistrazione)) return false;
        InfoCreazioneRegistrazione other = (InfoCreazioneRegistrazione) obj;

        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.numeroRegistrazionePrecedente==null && other.getNumeroRegistrazionePrecedente()==null) || 
             (this.numeroRegistrazionePrecedente!=null &&
              this.numeroRegistrazionePrecedente.equals(other.getNumeroRegistrazionePrecedente()))) &&
            ((this.annoRegistrazionePrecedente==null && other.getAnnoRegistrazionePrecedente()==null) || 
             (this.annoRegistrazionePrecedente!=null &&
              this.annoRegistrazionePrecedente.equals(other.getAnnoRegistrazionePrecedente()))) &&
            this.registrazioneRiservata == other.isRegistrazioneRiservata() &&
            ((this.oggetto==null && other.getOggetto()==null) || 
             (this.oggetto!=null &&
              this.oggetto.equals(other.getOggetto()))) &&
            ((this.collocazioneCartacea==null && other.getCollocazioneCartacea()==null) || 
             (this.collocazioneCartacea!=null &&
              this.collocazioneCartacea.equals(other.getCollocazioneCartacea()))) &&
            ((this.mezzoTrasmissivoId==null && other.getMezzoTrasmissivoId()==null) || 
             (this.mezzoTrasmissivoId!=null &&
              this.mezzoTrasmissivoId.equals(other.getMezzoTrasmissivoId()))) &&
            this.forzareSeRegistrazioneSimile == other.isForzareSeRegistrazioneSimile() &&
            this.documentoRiservato == other.isDocumentoRiservato() &&
            ((this.classificazioneProposta==null && other.getClassificazioneProposta()==null) || 
             (this.classificazioneProposta!=null &&
              this.classificazioneProposta.equals(other.getClassificazioneProposta()))) &&
            this.forzareSeRegistrazionePropostaInvalida == other.isForzareSeRegistrazionePropostaInvalida() &&
            ((this.dataDocumento==null && other.getDataDocumento()==null) || 
             (this.dataDocumento!=null &&
              this.dataDocumento.equals(other.getDataDocumento()))) &&
            ((this.descrizioneAllegato==null && other.getDescrizioneAllegato()==null) || 
             (this.descrizioneAllegato!=null &&
              java.util.Arrays.equals(this.descrizioneAllegato, other.getDescrizioneAllegato()))) &&
            ((this.mittenteInterno==null && other.getMittenteInterno()==null) || 
             (this.mittenteInterno!=null &&
              java.util.Arrays.equals(this.mittenteInterno, other.getMittenteInterno()))) &&
            ((this.destinatarioInterno==null && other.getDestinatarioInterno()==null) || 
             (this.destinatarioInterno!=null &&
              java.util.Arrays.equals(this.destinatarioInterno, other.getDestinatarioInterno()))) &&
            ((this.destinatarioEsterno==null && other.getDestinatarioEsterno()==null) || 
             (this.destinatarioEsterno!=null &&
              java.util.Arrays.equals(this.destinatarioEsterno, other.getDestinatarioEsterno()))) &&
            ((this.protocollante==null && other.getProtocollante()==null) || 
             (this.protocollante!=null &&
              this.protocollante.equals(other.getProtocollante()))) &&
            ((this.annotazione==null && other.getAnnotazione()==null) || 
             (this.annotazione!=null &&
              this.annotazione.equals(other.getAnnotazione()))) &&
            this.forzareSePresenzaInviti == other.isForzareSePresenzaInviti() &&
            this.forzareSePresenzaDaInoltrare == other.isForzareSePresenzaDaInoltrare();
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getNumeroRegistrazionePrecedente() != null) {
            _hashCode += getNumeroRegistrazionePrecedente().hashCode();
        }
        if (getAnnoRegistrazionePrecedente() != null) {
            _hashCode += getAnnoRegistrazionePrecedente().hashCode();
        }
        _hashCode += (isRegistrazioneRiservata() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getOggetto() != null) {
            _hashCode += getOggetto().hashCode();
        }
        if (getCollocazioneCartacea() != null) {
            _hashCode += getCollocazioneCartacea().hashCode();
        }
        if (getMezzoTrasmissivoId() != null) {
            _hashCode += getMezzoTrasmissivoId().hashCode();
        }
        _hashCode += (isForzareSeRegistrazioneSimile() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isDocumentoRiservato() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getClassificazioneProposta() != null) {
            _hashCode += getClassificazioneProposta().hashCode();
        }
        _hashCode += (isForzareSeRegistrazionePropostaInvalida() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getDataDocumento() != null) {
            _hashCode += getDataDocumento().hashCode();
        }
        if (getDescrizioneAllegato() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDescrizioneAllegato());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDescrizioneAllegato(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getMittenteInterno() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getMittenteInterno());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getMittenteInterno(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getDestinatarioInterno() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDestinatarioInterno());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDestinatarioInterno(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getDestinatarioEsterno() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDestinatarioEsterno());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDestinatarioEsterno(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getProtocollante() != null) {
            _hashCode += getProtocollante().hashCode();
        }
        if (getAnnotazione() != null) {
            _hashCode += getAnnotazione().hashCode();
        }
        _hashCode += (isForzareSePresenzaInviti() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isForzareSePresenzaDaInoltrare() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(InfoCreazioneRegistrazione.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "InfoCreazioneRegistrazione"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numeroRegistrazionePrecedente");
        elemField.setXmlName(new javax.xml.namespace.QName("", "numeroRegistrazionePrecedente"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("annoRegistrazionePrecedente");
        elemField.setXmlName(new javax.xml.namespace.QName("", "annoRegistrazionePrecedente"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("registrazioneRiservata");
        elemField.setXmlName(new javax.xml.namespace.QName("", "registrazioneRiservata"));
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
        elemField.setFieldName("collocazioneCartacea");
        elemField.setXmlName(new javax.xml.namespace.QName("", "collocazioneCartacea"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mezzoTrasmissivoId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "mezzoTrasmissivoId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("forzareSeRegistrazioneSimile");
        elemField.setXmlName(new javax.xml.namespace.QName("", "forzareSeRegistrazioneSimile"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("documentoRiservato");
        elemField.setXmlName(new javax.xml.namespace.QName("", "documentoRiservato"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("classificazioneProposta");
        elemField.setXmlName(new javax.xml.namespace.QName("", "classificazioneProposta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("forzareSeRegistrazionePropostaInvalida");
        elemField.setXmlName(new javax.xml.namespace.QName("", "forzareSeRegistrazionePropostaInvalida"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataDocumento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataDocumento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descrizioneAllegato");
        elemField.setXmlName(new javax.xml.namespace.QName("", "descrizioneAllegato"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mittenteInterno");
        elemField.setXmlName(new javax.xml.namespace.QName("", "mittenteInterno"));
        elemField.setXmlType(new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "MittenteInterno"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("destinatarioInterno");
        elemField.setXmlName(new javax.xml.namespace.QName("", "destinatarioInterno"));
        elemField.setXmlType(new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "DestinatarioInterno"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("destinatarioEsterno");
        elemField.setXmlName(new javax.xml.namespace.QName("", "destinatarioEsterno"));
        elemField.setXmlType(new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "DestinatarioEsterno"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
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
        elemField.setFieldName("forzareSePresenzaInviti");
        elemField.setXmlName(new javax.xml.namespace.QName("", "forzareSePresenzaInviti"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("forzareSePresenzaDaInoltrare");
        elemField.setXmlName(new javax.xml.namespace.QName("", "forzareSePresenzaDaInoltrare"));
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

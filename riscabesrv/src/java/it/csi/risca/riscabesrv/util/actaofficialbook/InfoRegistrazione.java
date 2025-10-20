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
 * InfoRegistrazione.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actaofficialbook;

public class InfoRegistrazione  implements java.io.Serializable {
    private java.util.Date dataProtocollo;

    private java.lang.String codice;

    private boolean annullata;

    private java.lang.String numeroRegistrazionePrecedente;

    private java.lang.String annoRegistrazionePrecedente;

    private it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType registrazionePrecedenteId;

    private boolean registrazioneRiservata;

    private java.lang.Long mezzoTrasmissivoId;

    private java.lang.String oggetto;

    private java.lang.String collocazioneCartacea;

    private boolean forzareSeRegistrazioneSimile;

    private boolean documentoRiservato;

    private it.csi.risca.riscabesrv.util.actaofficialbook.IdentificazioneProtocollanteEstesa protocollante;

    private java.lang.String tipoRegistrazione;

    private it.csi.risca.riscabesrv.util.actaofficialbook.RegistrazioneArrivo infoAggiuntiveRegistrazioneInArrivo;

    private it.csi.risca.riscabesrv.util.actaofficialbook.InfoCreazioneAnnotazioneOB[] annotazione;

    private it.csi.risca.riscabesrv.util.actaofficialbook.InfoAnnullamentoRegistrazione infoAnnullamento;

    private it.csi.risca.riscabesrv.util.actaofficialbook.MittenteInterno[] mittenteInterno;

    private it.csi.risca.riscabesrv.util.actaofficialbook.DestinatarioInterno[] destinatarioInterno;

    private it.csi.risca.riscabesrv.util.actaofficialbook.DestinatarioEsterno[] destinatarioEsterno;

    private boolean forzareSePresenzaInviti;

    private boolean forzareSePresenzaDaInoltrare;

    public InfoRegistrazione() {
    }

    public InfoRegistrazione(
           java.util.Date dataProtocollo,
           java.lang.String codice,
           boolean annullata,
           java.lang.String numeroRegistrazionePrecedente,
           java.lang.String annoRegistrazionePrecedente,
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType registrazionePrecedenteId,
           boolean registrazioneRiservata,
           java.lang.Long mezzoTrasmissivoId,
           java.lang.String oggetto,
           java.lang.String collocazioneCartacea,
           boolean forzareSeRegistrazioneSimile,
           boolean documentoRiservato,
           it.csi.risca.riscabesrv.util.actaofficialbook.IdentificazioneProtocollanteEstesa protocollante,
           java.lang.String tipoRegistrazione,
           it.csi.risca.riscabesrv.util.actaofficialbook.RegistrazioneArrivo infoAggiuntiveRegistrazioneInArrivo,
           it.csi.risca.riscabesrv.util.actaofficialbook.InfoCreazioneAnnotazioneOB[] annotazione,
           it.csi.risca.riscabesrv.util.actaofficialbook.InfoAnnullamentoRegistrazione infoAnnullamento,
           it.csi.risca.riscabesrv.util.actaofficialbook.MittenteInterno[] mittenteInterno,
           it.csi.risca.riscabesrv.util.actaofficialbook.DestinatarioInterno[] destinatarioInterno,
           it.csi.risca.riscabesrv.util.actaofficialbook.DestinatarioEsterno[] destinatarioEsterno,
           boolean forzareSePresenzaInviti,
           boolean forzareSePresenzaDaInoltrare) {
           this.dataProtocollo = dataProtocollo;
           this.codice = codice;
           this.annullata = annullata;
           this.numeroRegistrazionePrecedente = numeroRegistrazionePrecedente;
           this.annoRegistrazionePrecedente = annoRegistrazionePrecedente;
           this.registrazionePrecedenteId = registrazionePrecedenteId;
           this.registrazioneRiservata = registrazioneRiservata;
           this.mezzoTrasmissivoId = mezzoTrasmissivoId;
           this.oggetto = oggetto;
           this.collocazioneCartacea = collocazioneCartacea;
           this.forzareSeRegistrazioneSimile = forzareSeRegistrazioneSimile;
           this.documentoRiservato = documentoRiservato;
           this.protocollante = protocollante;
           this.tipoRegistrazione = tipoRegistrazione;
           this.infoAggiuntiveRegistrazioneInArrivo = infoAggiuntiveRegistrazioneInArrivo;
           this.annotazione = annotazione;
           this.infoAnnullamento = infoAnnullamento;
           this.mittenteInterno = mittenteInterno;
           this.destinatarioInterno = destinatarioInterno;
           this.destinatarioEsterno = destinatarioEsterno;
           this.forzareSePresenzaInviti = forzareSePresenzaInviti;
           this.forzareSePresenzaDaInoltrare = forzareSePresenzaDaInoltrare;
    }


    /**
     * Gets the dataProtocollo value for this InfoRegistrazione.
     * 
     * @return dataProtocollo
     */
    public java.util.Date getDataProtocollo() {
        return dataProtocollo;
    }


    /**
     * Sets the dataProtocollo value for this InfoRegistrazione.
     * 
     * @param dataProtocollo
     */
    public void setDataProtocollo(java.util.Date dataProtocollo) {
        this.dataProtocollo = dataProtocollo;
    }


    /**
     * Gets the codice value for this InfoRegistrazione.
     * 
     * @return codice
     */
    public java.lang.String getCodice() {
        return codice;
    }


    /**
     * Sets the codice value for this InfoRegistrazione.
     * 
     * @param codice
     */
    public void setCodice(java.lang.String codice) {
        this.codice = codice;
    }


    /**
     * Gets the annullata value for this InfoRegistrazione.
     * 
     * @return annullata
     */
    public boolean isAnnullata() {
        return annullata;
    }


    /**
     * Sets the annullata value for this InfoRegistrazione.
     * 
     * @param annullata
     */
    public void setAnnullata(boolean annullata) {
        this.annullata = annullata;
    }


    /**
     * Gets the numeroRegistrazionePrecedente value for this InfoRegistrazione.
     * 
     * @return numeroRegistrazionePrecedente
     */
    public java.lang.String getNumeroRegistrazionePrecedente() {
        return numeroRegistrazionePrecedente;
    }


    /**
     * Sets the numeroRegistrazionePrecedente value for this InfoRegistrazione.
     * 
     * @param numeroRegistrazionePrecedente
     */
    public void setNumeroRegistrazionePrecedente(java.lang.String numeroRegistrazionePrecedente) {
        this.numeroRegistrazionePrecedente = numeroRegistrazionePrecedente;
    }


    /**
     * Gets the annoRegistrazionePrecedente value for this InfoRegistrazione.
     * 
     * @return annoRegistrazionePrecedente
     */
    public java.lang.String getAnnoRegistrazionePrecedente() {
        return annoRegistrazionePrecedente;
    }


    /**
     * Sets the annoRegistrazionePrecedente value for this InfoRegistrazione.
     * 
     * @param annoRegistrazionePrecedente
     */
    public void setAnnoRegistrazionePrecedente(java.lang.String annoRegistrazionePrecedente) {
        this.annoRegistrazionePrecedente = annoRegistrazionePrecedente;
    }


    /**
     * Gets the registrazionePrecedenteId value for this InfoRegistrazione.
     * 
     * @return registrazionePrecedenteId
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType getRegistrazionePrecedenteId() {
        return registrazionePrecedenteId;
    }


    /**
     * Sets the registrazionePrecedenteId value for this InfoRegistrazione.
     * 
     * @param registrazionePrecedenteId
     */
    public void setRegistrazionePrecedenteId(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType registrazionePrecedenteId) {
        this.registrazionePrecedenteId = registrazionePrecedenteId;
    }


    /**
     * Gets the registrazioneRiservata value for this InfoRegistrazione.
     * 
     * @return registrazioneRiservata
     */
    public boolean isRegistrazioneRiservata() {
        return registrazioneRiservata;
    }


    /**
     * Sets the registrazioneRiservata value for this InfoRegistrazione.
     * 
     * @param registrazioneRiservata
     */
    public void setRegistrazioneRiservata(boolean registrazioneRiservata) {
        this.registrazioneRiservata = registrazioneRiservata;
    }


    /**
     * Gets the mezzoTrasmissivoId value for this InfoRegistrazione.
     * 
     * @return mezzoTrasmissivoId
     */
    public java.lang.Long getMezzoTrasmissivoId() {
        return mezzoTrasmissivoId;
    }


    /**
     * Sets the mezzoTrasmissivoId value for this InfoRegistrazione.
     * 
     * @param mezzoTrasmissivoId
     */
    public void setMezzoTrasmissivoId(java.lang.Long mezzoTrasmissivoId) {
        this.mezzoTrasmissivoId = mezzoTrasmissivoId;
    }


    /**
     * Gets the oggetto value for this InfoRegistrazione.
     * 
     * @return oggetto
     */
    public java.lang.String getOggetto() {
        return oggetto;
    }


    /**
     * Sets the oggetto value for this InfoRegistrazione.
     * 
     * @param oggetto
     */
    public void setOggetto(java.lang.String oggetto) {
        this.oggetto = oggetto;
    }


    /**
     * Gets the collocazioneCartacea value for this InfoRegistrazione.
     * 
     * @return collocazioneCartacea
     */
    public java.lang.String getCollocazioneCartacea() {
        return collocazioneCartacea;
    }


    /**
     * Sets the collocazioneCartacea value for this InfoRegistrazione.
     * 
     * @param collocazioneCartacea
     */
    public void setCollocazioneCartacea(java.lang.String collocazioneCartacea) {
        this.collocazioneCartacea = collocazioneCartacea;
    }


    /**
     * Gets the forzareSeRegistrazioneSimile value for this InfoRegistrazione.
     * 
     * @return forzareSeRegistrazioneSimile
     */
    public boolean isForzareSeRegistrazioneSimile() {
        return forzareSeRegistrazioneSimile;
    }


    /**
     * Sets the forzareSeRegistrazioneSimile value for this InfoRegistrazione.
     * 
     * @param forzareSeRegistrazioneSimile
     */
    public void setForzareSeRegistrazioneSimile(boolean forzareSeRegistrazioneSimile) {
        this.forzareSeRegistrazioneSimile = forzareSeRegistrazioneSimile;
    }


    /**
     * Gets the documentoRiservato value for this InfoRegistrazione.
     * 
     * @return documentoRiservato
     */
    public boolean isDocumentoRiservato() {
        return documentoRiservato;
    }


    /**
     * Sets the documentoRiservato value for this InfoRegistrazione.
     * 
     * @param documentoRiservato
     */
    public void setDocumentoRiservato(boolean documentoRiservato) {
        this.documentoRiservato = documentoRiservato;
    }


    /**
     * Gets the protocollante value for this InfoRegistrazione.
     * 
     * @return protocollante
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.IdentificazioneProtocollanteEstesa getProtocollante() {
        return protocollante;
    }


    /**
     * Sets the protocollante value for this InfoRegistrazione.
     * 
     * @param protocollante
     */
    public void setProtocollante(it.csi.risca.riscabesrv.util.actaofficialbook.IdentificazioneProtocollanteEstesa protocollante) {
        this.protocollante = protocollante;
    }


    /**
     * Gets the tipoRegistrazione value for this InfoRegistrazione.
     * 
     * @return tipoRegistrazione
     */
    public java.lang.String getTipoRegistrazione() {
        return tipoRegistrazione;
    }


    /**
     * Sets the tipoRegistrazione value for this InfoRegistrazione.
     * 
     * @param tipoRegistrazione
     */
    public void setTipoRegistrazione(java.lang.String tipoRegistrazione) {
        this.tipoRegistrazione = tipoRegistrazione;
    }


    /**
     * Gets the infoAggiuntiveRegistrazioneInArrivo value for this InfoRegistrazione.
     * 
     * @return infoAggiuntiveRegistrazioneInArrivo
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.RegistrazioneArrivo getInfoAggiuntiveRegistrazioneInArrivo() {
        return infoAggiuntiveRegistrazioneInArrivo;
    }


    /**
     * Sets the infoAggiuntiveRegistrazioneInArrivo value for this InfoRegistrazione.
     * 
     * @param infoAggiuntiveRegistrazioneInArrivo
     */
    public void setInfoAggiuntiveRegistrazioneInArrivo(it.csi.risca.riscabesrv.util.actaofficialbook.RegistrazioneArrivo infoAggiuntiveRegistrazioneInArrivo) {
        this.infoAggiuntiveRegistrazioneInArrivo = infoAggiuntiveRegistrazioneInArrivo;
    }


    /**
     * Gets the annotazione value for this InfoRegistrazione.
     * 
     * @return annotazione
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.InfoCreazioneAnnotazioneOB[] getAnnotazione() {
        return annotazione;
    }


    /**
     * Sets the annotazione value for this InfoRegistrazione.
     * 
     * @param annotazione
     */
    public void setAnnotazione(it.csi.risca.riscabesrv.util.actaofficialbook.InfoCreazioneAnnotazioneOB[] annotazione) {
        this.annotazione = annotazione;
    }

    public it.csi.risca.riscabesrv.util.actaofficialbook.InfoCreazioneAnnotazioneOB getAnnotazione(int i) {
        return this.annotazione[i];
    }

    public void setAnnotazione(int i, it.csi.risca.riscabesrv.util.actaofficialbook.InfoCreazioneAnnotazioneOB _value) {
        this.annotazione[i] = _value;
    }


    /**
     * Gets the infoAnnullamento value for this InfoRegistrazione.
     * 
     * @return infoAnnullamento
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.InfoAnnullamentoRegistrazione getInfoAnnullamento() {
        return infoAnnullamento;
    }


    /**
     * Sets the infoAnnullamento value for this InfoRegistrazione.
     * 
     * @param infoAnnullamento
     */
    public void setInfoAnnullamento(it.csi.risca.riscabesrv.util.actaofficialbook.InfoAnnullamentoRegistrazione infoAnnullamento) {
        this.infoAnnullamento = infoAnnullamento;
    }


    /**
     * Gets the mittenteInterno value for this InfoRegistrazione.
     * 
     * @return mittenteInterno
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.MittenteInterno[] getMittenteInterno() {
        return mittenteInterno;
    }


    /**
     * Sets the mittenteInterno value for this InfoRegistrazione.
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
     * Gets the destinatarioInterno value for this InfoRegistrazione.
     * 
     * @return destinatarioInterno
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.DestinatarioInterno[] getDestinatarioInterno() {
        return destinatarioInterno;
    }


    /**
     * Sets the destinatarioInterno value for this InfoRegistrazione.
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
     * Gets the destinatarioEsterno value for this InfoRegistrazione.
     * 
     * @return destinatarioEsterno
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.DestinatarioEsterno[] getDestinatarioEsterno() {
        return destinatarioEsterno;
    }


    /**
     * Sets the destinatarioEsterno value for this InfoRegistrazione.
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
     * Gets the forzareSePresenzaInviti value for this InfoRegistrazione.
     * 
     * @return forzareSePresenzaInviti
     */
    public boolean isForzareSePresenzaInviti() {
        return forzareSePresenzaInviti;
    }


    /**
     * Sets the forzareSePresenzaInviti value for this InfoRegistrazione.
     * 
     * @param forzareSePresenzaInviti
     */
    public void setForzareSePresenzaInviti(boolean forzareSePresenzaInviti) {
        this.forzareSePresenzaInviti = forzareSePresenzaInviti;
    }


    /**
     * Gets the forzareSePresenzaDaInoltrare value for this InfoRegistrazione.
     * 
     * @return forzareSePresenzaDaInoltrare
     */
    public boolean isForzareSePresenzaDaInoltrare() {
        return forzareSePresenzaDaInoltrare;
    }


    /**
     * Sets the forzareSePresenzaDaInoltrare value for this InfoRegistrazione.
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
        if (!(obj instanceof InfoRegistrazione)) return false;
        InfoRegistrazione other = (InfoRegistrazione) obj;

        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.dataProtocollo==null && other.getDataProtocollo()==null) || 
             (this.dataProtocollo!=null &&
              this.dataProtocollo.equals(other.getDataProtocollo()))) &&
            ((this.codice==null && other.getCodice()==null) || 
             (this.codice!=null &&
              this.codice.equals(other.getCodice()))) &&
            this.annullata == other.isAnnullata() &&
            ((this.numeroRegistrazionePrecedente==null && other.getNumeroRegistrazionePrecedente()==null) || 
             (this.numeroRegistrazionePrecedente!=null &&
              this.numeroRegistrazionePrecedente.equals(other.getNumeroRegistrazionePrecedente()))) &&
            ((this.annoRegistrazionePrecedente==null && other.getAnnoRegistrazionePrecedente()==null) || 
             (this.annoRegistrazionePrecedente!=null &&
              this.annoRegistrazionePrecedente.equals(other.getAnnoRegistrazionePrecedente()))) &&
            ((this.registrazionePrecedenteId==null && other.getRegistrazionePrecedenteId()==null) || 
             (this.registrazionePrecedenteId!=null &&
              this.registrazionePrecedenteId.equals(other.getRegistrazionePrecedenteId()))) &&
            this.registrazioneRiservata == other.isRegistrazioneRiservata() &&
            ((this.mezzoTrasmissivoId==null && other.getMezzoTrasmissivoId()==null) || 
             (this.mezzoTrasmissivoId!=null &&
              this.mezzoTrasmissivoId.equals(other.getMezzoTrasmissivoId()))) &&
            ((this.oggetto==null && other.getOggetto()==null) || 
             (this.oggetto!=null &&
              this.oggetto.equals(other.getOggetto()))) &&
            ((this.collocazioneCartacea==null && other.getCollocazioneCartacea()==null) || 
             (this.collocazioneCartacea!=null &&
              this.collocazioneCartacea.equals(other.getCollocazioneCartacea()))) &&
            this.forzareSeRegistrazioneSimile == other.isForzareSeRegistrazioneSimile() &&
            this.documentoRiservato == other.isDocumentoRiservato() &&
            ((this.protocollante==null && other.getProtocollante()==null) || 
             (this.protocollante!=null &&
              this.protocollante.equals(other.getProtocollante()))) &&
            ((this.tipoRegistrazione==null && other.getTipoRegistrazione()==null) || 
             (this.tipoRegistrazione!=null &&
              this.tipoRegistrazione.equals(other.getTipoRegistrazione()))) &&
            ((this.infoAggiuntiveRegistrazioneInArrivo==null && other.getInfoAggiuntiveRegistrazioneInArrivo()==null) || 
             (this.infoAggiuntiveRegistrazioneInArrivo!=null &&
              this.infoAggiuntiveRegistrazioneInArrivo.equals(other.getInfoAggiuntiveRegistrazioneInArrivo()))) &&
            ((this.annotazione==null && other.getAnnotazione()==null) || 
             (this.annotazione!=null &&
              java.util.Arrays.equals(this.annotazione, other.getAnnotazione()))) &&
            ((this.infoAnnullamento==null && other.getInfoAnnullamento()==null) || 
             (this.infoAnnullamento!=null &&
              this.infoAnnullamento.equals(other.getInfoAnnullamento()))) &&
            ((this.mittenteInterno==null && other.getMittenteInterno()==null) || 
             (this.mittenteInterno!=null &&
              java.util.Arrays.equals(this.mittenteInterno, other.getMittenteInterno()))) &&
            ((this.destinatarioInterno==null && other.getDestinatarioInterno()==null) || 
             (this.destinatarioInterno!=null &&
              java.util.Arrays.equals(this.destinatarioInterno, other.getDestinatarioInterno()))) &&
            ((this.destinatarioEsterno==null && other.getDestinatarioEsterno()==null) || 
             (this.destinatarioEsterno!=null &&
              java.util.Arrays.equals(this.destinatarioEsterno, other.getDestinatarioEsterno()))) &&
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
        if (getDataProtocollo() != null) {
            _hashCode += getDataProtocollo().hashCode();
        }
        if (getCodice() != null) {
            _hashCode += getCodice().hashCode();
        }
        _hashCode += (isAnnullata() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getNumeroRegistrazionePrecedente() != null) {
            _hashCode += getNumeroRegistrazionePrecedente().hashCode();
        }
        if (getAnnoRegistrazionePrecedente() != null) {
            _hashCode += getAnnoRegistrazionePrecedente().hashCode();
        }
        if (getRegistrazionePrecedenteId() != null) {
            _hashCode += getRegistrazionePrecedenteId().hashCode();
        }
        _hashCode += (isRegistrazioneRiservata() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getMezzoTrasmissivoId() != null) {
            _hashCode += getMezzoTrasmissivoId().hashCode();
        }
        if (getOggetto() != null) {
            _hashCode += getOggetto().hashCode();
        }
        if (getCollocazioneCartacea() != null) {
            _hashCode += getCollocazioneCartacea().hashCode();
        }
        _hashCode += (isForzareSeRegistrazioneSimile() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isDocumentoRiservato() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getProtocollante() != null) {
            _hashCode += getProtocollante().hashCode();
        }
        if (getTipoRegistrazione() != null) {
            _hashCode += getTipoRegistrazione().hashCode();
        }
        if (getInfoAggiuntiveRegistrazioneInArrivo() != null) {
            _hashCode += getInfoAggiuntiveRegistrazioneInArrivo().hashCode();
        }
        if (getAnnotazione() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getAnnotazione());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getAnnotazione(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getInfoAnnullamento() != null) {
            _hashCode += getInfoAnnullamento().hashCode();
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
        _hashCode += (isForzareSePresenzaInviti() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isForzareSePresenzaDaInoltrare() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(InfoRegistrazione.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "InfoRegistrazione"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataProtocollo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataProtocollo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codice");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codice"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("annullata");
        elemField.setXmlName(new javax.xml.namespace.QName("", "annullata"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
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
        elemField.setFieldName("registrazionePrecedenteId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "registrazionePrecedenteId"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"));
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
        elemField.setFieldName("mezzoTrasmissivoId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "mezzoTrasmissivoId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
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
        elemField.setFieldName("protocollante");
        elemField.setXmlName(new javax.xml.namespace.QName("", "protocollante"));
        elemField.setXmlType(new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "IdentificazioneProtocollanteEstesa"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoRegistrazione");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tipoRegistrazione"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("infoAggiuntiveRegistrazioneInArrivo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "infoAggiuntiveRegistrazioneInArrivo"));
        elemField.setXmlType(new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "RegistrazioneArrivo"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("annotazione");
        elemField.setXmlName(new javax.xml.namespace.QName("", "annotazione"));
        elemField.setXmlType(new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "InfoCreazioneAnnotazioneOB"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("infoAnnullamento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "infoAnnullamento"));
        elemField.setXmlType(new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "InfoAnnullamentoRegistrazione"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
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

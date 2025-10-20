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
 * DocumentoPropertiesType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actaobject;

public abstract class DocumentoPropertiesType  extends it.csi.risca.riscabesrv.util.actaobject.DocumentPropertiesType  implements java.io.Serializable {
    private it.csi.risca.riscabesrv.util.actaobject.ObjectIdType idCorrente;

    private boolean registrato;

    private boolean modificabile;

    private boolean definitivo;

    private boolean origineInterna;

    private boolean analogico;

    private boolean rappresentazioneDigitale;

    private boolean daConservare;

    private boolean prontoPerConservazione;

    private java.util.Date daConservareDopoIl;

    private java.util.Date daConservarePrimaDel;

    private boolean conservato;

    private boolean datiPersonali;

    private boolean datiSensibili;

    private boolean datiRiservati;

    private java.util.Date dataCreazione;

    private java.lang.String paroleChiave;

    private boolean modSMSQuarantena;

    private boolean congelato;

    private boolean referenziabile;

    private java.lang.String identificativoConservazione;

    private java.lang.String indiceEstesoOrigineEstratto;

    private java.lang.String[] indiciEstesiEstrattiGenerati;

    private java.lang.String[] autoreGiuridico;

    private java.lang.String[] autoreFisico;

    private java.lang.String[] scrittore;

    private java.lang.String[] originatore;

    private java.lang.String[] destinatarioGiuridico;

    private java.lang.String[] destinatarioFisico;

    private java.lang.String[] soggettoProduttore;

    private java.lang.String oggetto;

    private java.lang.String dataDocTopica;

    private java.util.Date dataDocCronica;

    private java.util.Date[] dataTrasmissione;

    private java.util.Date dataRicevimento;

    private java.lang.String numRepertorio;

    private boolean forzareSeNumRepertorioEsistente;

    private boolean docConAllegati;

    private boolean docAllegato;

    private boolean firmaElettronicaDigitale;

    private boolean docAutenticato;

    private boolean docAutenticatoFirmaAutenticata;

    private boolean docAutenticatoCopiaAutentica;

    private it.csi.risca.riscabesrv.util.actaobject.IdStatoDiEfficaciaType idStatoDiEfficacia;

    private it.csi.risca.riscabesrv.util.actaobject.IdFormaDocumentariaType idFormaDocumentaria;

    private boolean firmaElettronica;

    private it.csi.risca.riscabesrv.util.actaobject.ObjectIdType[] idProtocollo;

    private java.lang.String[] indiceClassificazione;

    private java.lang.String[] indiceClassificazioneEstesa;

    private java.lang.String applicativoAlimentante;

    private java.util.Date dataCancellazione;

    private java.util.Date dataEsportazione;

    private it.csi.risca.riscabesrv.util.actaobject.IdVitalRecordCodeType idVitalRecordCode;

    private it.csi.risca.riscabesrv.util.actaobject.ObjectIdType[] idAnnotazioniList;

    private java.lang.String[] renditionXMLList;

    private java.util.Date dataDocChiusuraRifTempUTC;

    private java.util.Date dataDocChiusura;

    public DocumentoPropertiesType() {
    }

    public DocumentoPropertiesType(
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
           java.util.Date dataDocChiusura) {
        super(
            objectId,
            changeToken,
            objectTypeId,
            contentStreamLength,
            contentStreamMimeType,
            contentStreamFilename);
        this.idCorrente = idCorrente;
        this.registrato = registrato;
        this.modificabile = modificabile;
        this.definitivo = definitivo;
        this.origineInterna = origineInterna;
        this.analogico = analogico;
        this.rappresentazioneDigitale = rappresentazioneDigitale;
        this.daConservare = daConservare;
        this.prontoPerConservazione = prontoPerConservazione;
        this.daConservareDopoIl = daConservareDopoIl;
        this.daConservarePrimaDel = daConservarePrimaDel;
        this.conservato = conservato;
        this.datiPersonali = datiPersonali;
        this.datiSensibili = datiSensibili;
        this.datiRiservati = datiRiservati;
        this.dataCreazione = dataCreazione;
        this.paroleChiave = paroleChiave;
        this.modSMSQuarantena = modSMSQuarantena;
        this.congelato = congelato;
        this.referenziabile = referenziabile;
        this.identificativoConservazione = identificativoConservazione;
        this.indiceEstesoOrigineEstratto = indiceEstesoOrigineEstratto;
        this.indiciEstesiEstrattiGenerati = indiciEstesiEstrattiGenerati;
        this.autoreGiuridico = autoreGiuridico;
        this.autoreFisico = autoreFisico;
        this.scrittore = scrittore;
        this.originatore = originatore;
        this.destinatarioGiuridico = destinatarioGiuridico;
        this.destinatarioFisico = destinatarioFisico;
        this.soggettoProduttore = soggettoProduttore;
        this.oggetto = oggetto;
        this.dataDocTopica = dataDocTopica;
        this.dataDocCronica = dataDocCronica;
        this.dataTrasmissione = dataTrasmissione;
        this.dataRicevimento = dataRicevimento;
        this.numRepertorio = numRepertorio;
        this.forzareSeNumRepertorioEsistente = forzareSeNumRepertorioEsistente;
        this.docConAllegati = docConAllegati;
        this.docAllegato = docAllegato;
        this.firmaElettronicaDigitale = firmaElettronicaDigitale;
        this.docAutenticato = docAutenticato;
        this.docAutenticatoFirmaAutenticata = docAutenticatoFirmaAutenticata;
        this.docAutenticatoCopiaAutentica = docAutenticatoCopiaAutentica;
        this.idStatoDiEfficacia = idStatoDiEfficacia;
        this.idFormaDocumentaria = idFormaDocumentaria;
        this.firmaElettronica = firmaElettronica;
        this.idProtocollo = idProtocollo;
        this.indiceClassificazione = indiceClassificazione;
        this.indiceClassificazioneEstesa = indiceClassificazioneEstesa;
        this.applicativoAlimentante = applicativoAlimentante;
        this.dataCancellazione = dataCancellazione;
        this.dataEsportazione = dataEsportazione;
        this.idVitalRecordCode = idVitalRecordCode;
        this.idAnnotazioniList = idAnnotazioniList;
        this.renditionXMLList = renditionXMLList;
        this.dataDocChiusuraRifTempUTC = dataDocChiusuraRifTempUTC;
        this.dataDocChiusura = dataDocChiusura;
    }


    /**
     * Gets the idCorrente value for this DocumentoPropertiesType.
     * 
     * @return idCorrente
     */
    public it.csi.risca.riscabesrv.util.actaobject.ObjectIdType getIdCorrente() {
        return idCorrente;
    }


    /**
     * Sets the idCorrente value for this DocumentoPropertiesType.
     * 
     * @param idCorrente
     */
    public void setIdCorrente(it.csi.risca.riscabesrv.util.actaobject.ObjectIdType idCorrente) {
        this.idCorrente = idCorrente;
    }


    /**
     * Gets the registrato value for this DocumentoPropertiesType.
     * 
     * @return registrato
     */
    public boolean isRegistrato() {
        return registrato;
    }


    /**
     * Sets the registrato value for this DocumentoPropertiesType.
     * 
     * @param registrato
     */
    public void setRegistrato(boolean registrato) {
        this.registrato = registrato;
    }


    /**
     * Gets the modificabile value for this DocumentoPropertiesType.
     * 
     * @return modificabile
     */
    public boolean isModificabile() {
        return modificabile;
    }


    /**
     * Sets the modificabile value for this DocumentoPropertiesType.
     * 
     * @param modificabile
     */
    public void setModificabile(boolean modificabile) {
        this.modificabile = modificabile;
    }


    /**
     * Gets the definitivo value for this DocumentoPropertiesType.
     * 
     * @return definitivo
     */
    public boolean isDefinitivo() {
        return definitivo;
    }


    /**
     * Sets the definitivo value for this DocumentoPropertiesType.
     * 
     * @param definitivo
     */
    public void setDefinitivo(boolean definitivo) {
        this.definitivo = definitivo;
    }


    /**
     * Gets the origineInterna value for this DocumentoPropertiesType.
     * 
     * @return origineInterna
     */
    public boolean isOrigineInterna() {
        return origineInterna;
    }


    /**
     * Sets the origineInterna value for this DocumentoPropertiesType.
     * 
     * @param origineInterna
     */
    public void setOrigineInterna(boolean origineInterna) {
        this.origineInterna = origineInterna;
    }


    /**
     * Gets the analogico value for this DocumentoPropertiesType.
     * 
     * @return analogico
     */
    public boolean isAnalogico() {
        return analogico;
    }


    /**
     * Sets the analogico value for this DocumentoPropertiesType.
     * 
     * @param analogico
     */
    public void setAnalogico(boolean analogico) {
        this.analogico = analogico;
    }


    /**
     * Gets the rappresentazioneDigitale value for this DocumentoPropertiesType.
     * 
     * @return rappresentazioneDigitale
     */
    public boolean isRappresentazioneDigitale() {
        return rappresentazioneDigitale;
    }


    /**
     * Sets the rappresentazioneDigitale value for this DocumentoPropertiesType.
     * 
     * @param rappresentazioneDigitale
     */
    public void setRappresentazioneDigitale(boolean rappresentazioneDigitale) {
        this.rappresentazioneDigitale = rappresentazioneDigitale;
    }


    /**
     * Gets the daConservare value for this DocumentoPropertiesType.
     * 
     * @return daConservare
     */
    public boolean isDaConservare() {
        return daConservare;
    }


    /**
     * Sets the daConservare value for this DocumentoPropertiesType.
     * 
     * @param daConservare
     */
    public void setDaConservare(boolean daConservare) {
        this.daConservare = daConservare;
    }


    /**
     * Gets the prontoPerConservazione value for this DocumentoPropertiesType.
     * 
     * @return prontoPerConservazione
     */
    public boolean isProntoPerConservazione() {
        return prontoPerConservazione;
    }


    /**
     * Sets the prontoPerConservazione value for this DocumentoPropertiesType.
     * 
     * @param prontoPerConservazione
     */
    public void setProntoPerConservazione(boolean prontoPerConservazione) {
        this.prontoPerConservazione = prontoPerConservazione;
    }


    /**
     * Gets the daConservareDopoIl value for this DocumentoPropertiesType.
     * 
     * @return daConservareDopoIl
     */
    public java.util.Date getDaConservareDopoIl() {
        return daConservareDopoIl;
    }


    /**
     * Sets the daConservareDopoIl value for this DocumentoPropertiesType.
     * 
     * @param daConservareDopoIl
     */
    public void setDaConservareDopoIl(java.util.Date daConservareDopoIl) {
        this.daConservareDopoIl = daConservareDopoIl;
    }


    /**
     * Gets the daConservarePrimaDel value for this DocumentoPropertiesType.
     * 
     * @return daConservarePrimaDel
     */
    public java.util.Date getDaConservarePrimaDel() {
        return daConservarePrimaDel;
    }


    /**
     * Sets the daConservarePrimaDel value for this DocumentoPropertiesType.
     * 
     * @param daConservarePrimaDel
     */
    public void setDaConservarePrimaDel(java.util.Date daConservarePrimaDel) {
        this.daConservarePrimaDel = daConservarePrimaDel;
    }


    /**
     * Gets the conservato value for this DocumentoPropertiesType.
     * 
     * @return conservato
     */
    public boolean isConservato() {
        return conservato;
    }


    /**
     * Sets the conservato value for this DocumentoPropertiesType.
     * 
     * @param conservato
     */
    public void setConservato(boolean conservato) {
        this.conservato = conservato;
    }


    /**
     * Gets the datiPersonali value for this DocumentoPropertiesType.
     * 
     * @return datiPersonali
     */
    public boolean isDatiPersonali() {
        return datiPersonali;
    }


    /**
     * Sets the datiPersonali value for this DocumentoPropertiesType.
     * 
     * @param datiPersonali
     */
    public void setDatiPersonali(boolean datiPersonali) {
        this.datiPersonali = datiPersonali;
    }


    /**
     * Gets the datiSensibili value for this DocumentoPropertiesType.
     * 
     * @return datiSensibili
     */
    public boolean isDatiSensibili() {
        return datiSensibili;
    }


    /**
     * Sets the datiSensibili value for this DocumentoPropertiesType.
     * 
     * @param datiSensibili
     */
    public void setDatiSensibili(boolean datiSensibili) {
        this.datiSensibili = datiSensibili;
    }


    /**
     * Gets the datiRiservati value for this DocumentoPropertiesType.
     * 
     * @return datiRiservati
     */
    public boolean isDatiRiservati() {
        return datiRiservati;
    }


    /**
     * Sets the datiRiservati value for this DocumentoPropertiesType.
     * 
     * @param datiRiservati
     */
    public void setDatiRiservati(boolean datiRiservati) {
        this.datiRiservati = datiRiservati;
    }


    /**
     * Gets the dataCreazione value for this DocumentoPropertiesType.
     * 
     * @return dataCreazione
     */
    public java.util.Date getDataCreazione() {
        return dataCreazione;
    }


    /**
     * Sets the dataCreazione value for this DocumentoPropertiesType.
     * 
     * @param dataCreazione
     */
    public void setDataCreazione(java.util.Date dataCreazione) {
        this.dataCreazione = dataCreazione;
    }


    /**
     * Gets the paroleChiave value for this DocumentoPropertiesType.
     * 
     * @return paroleChiave
     */
    public java.lang.String getParoleChiave() {
        return paroleChiave;
    }


    /**
     * Sets the paroleChiave value for this DocumentoPropertiesType.
     * 
     * @param paroleChiave
     */
    public void setParoleChiave(java.lang.String paroleChiave) {
        this.paroleChiave = paroleChiave;
    }


    /**
     * Gets the modSMSQuarantena value for this DocumentoPropertiesType.
     * 
     * @return modSMSQuarantena
     */
    public boolean isModSMSQuarantena() {
        return modSMSQuarantena;
    }


    /**
     * Sets the modSMSQuarantena value for this DocumentoPropertiesType.
     * 
     * @param modSMSQuarantena
     */
    public void setModSMSQuarantena(boolean modSMSQuarantena) {
        this.modSMSQuarantena = modSMSQuarantena;
    }


    /**
     * Gets the congelato value for this DocumentoPropertiesType.
     * 
     * @return congelato
     */
    public boolean isCongelato() {
        return congelato;
    }


    /**
     * Sets the congelato value for this DocumentoPropertiesType.
     * 
     * @param congelato
     */
    public void setCongelato(boolean congelato) {
        this.congelato = congelato;
    }


    /**
     * Gets the referenziabile value for this DocumentoPropertiesType.
     * 
     * @return referenziabile
     */
    public boolean isReferenziabile() {
        return referenziabile;
    }


    /**
     * Sets the referenziabile value for this DocumentoPropertiesType.
     * 
     * @param referenziabile
     */
    public void setReferenziabile(boolean referenziabile) {
        this.referenziabile = referenziabile;
    }


    /**
     * Gets the identificativoConservazione value for this DocumentoPropertiesType.
     * 
     * @return identificativoConservazione
     */
    public java.lang.String getIdentificativoConservazione() {
        return identificativoConservazione;
    }


    /**
     * Sets the identificativoConservazione value for this DocumentoPropertiesType.
     * 
     * @param identificativoConservazione
     */
    public void setIdentificativoConservazione(java.lang.String identificativoConservazione) {
        this.identificativoConservazione = identificativoConservazione;
    }


    /**
     * Gets the indiceEstesoOrigineEstratto value for this DocumentoPropertiesType.
     * 
     * @return indiceEstesoOrigineEstratto
     */
    public java.lang.String getIndiceEstesoOrigineEstratto() {
        return indiceEstesoOrigineEstratto;
    }


    /**
     * Sets the indiceEstesoOrigineEstratto value for this DocumentoPropertiesType.
     * 
     * @param indiceEstesoOrigineEstratto
     */
    public void setIndiceEstesoOrigineEstratto(java.lang.String indiceEstesoOrigineEstratto) {
        this.indiceEstesoOrigineEstratto = indiceEstesoOrigineEstratto;
    }


    /**
     * Gets the indiciEstesiEstrattiGenerati value for this DocumentoPropertiesType.
     * 
     * @return indiciEstesiEstrattiGenerati
     */
    public java.lang.String[] getIndiciEstesiEstrattiGenerati() {
        return indiciEstesiEstrattiGenerati;
    }


    /**
     * Sets the indiciEstesiEstrattiGenerati value for this DocumentoPropertiesType.
     * 
     * @param indiciEstesiEstrattiGenerati
     */
    public void setIndiciEstesiEstrattiGenerati(java.lang.String[] indiciEstesiEstrattiGenerati) {
        this.indiciEstesiEstrattiGenerati = indiciEstesiEstrattiGenerati;
    }

    public java.lang.String getIndiciEstesiEstrattiGenerati(int i) {
        return this.indiciEstesiEstrattiGenerati[i];
    }

    public void setIndiciEstesiEstrattiGenerati(int i, java.lang.String _value) {
        this.indiciEstesiEstrattiGenerati[i] = _value;
    }


    /**
     * Gets the autoreGiuridico value for this DocumentoPropertiesType.
     * 
     * @return autoreGiuridico
     */
    public java.lang.String[] getAutoreGiuridico() {
        return autoreGiuridico;
    }


    /**
     * Sets the autoreGiuridico value for this DocumentoPropertiesType.
     * 
     * @param autoreGiuridico
     */
    public void setAutoreGiuridico(java.lang.String[] autoreGiuridico) {
        this.autoreGiuridico = autoreGiuridico;
    }

    public java.lang.String getAutoreGiuridico(int i) {
        return this.autoreGiuridico[i];
    }

    public void setAutoreGiuridico(int i, java.lang.String _value) {
        this.autoreGiuridico[i] = _value;
    }


    /**
     * Gets the autoreFisico value for this DocumentoPropertiesType.
     * 
     * @return autoreFisico
     */
    public java.lang.String[] getAutoreFisico() {
        return autoreFisico;
    }


    /**
     * Sets the autoreFisico value for this DocumentoPropertiesType.
     * 
     * @param autoreFisico
     */
    public void setAutoreFisico(java.lang.String[] autoreFisico) {
        this.autoreFisico = autoreFisico;
    }

    public java.lang.String getAutoreFisico(int i) {
        return this.autoreFisico[i];
    }

    public void setAutoreFisico(int i, java.lang.String _value) {
        this.autoreFisico[i] = _value;
    }


    /**
     * Gets the scrittore value for this DocumentoPropertiesType.
     * 
     * @return scrittore
     */
    public java.lang.String[] getScrittore() {
        return scrittore;
    }


    /**
     * Sets the scrittore value for this DocumentoPropertiesType.
     * 
     * @param scrittore
     */
    public void setScrittore(java.lang.String[] scrittore) {
        this.scrittore = scrittore;
    }

    public java.lang.String getScrittore(int i) {
        return this.scrittore[i];
    }

    public void setScrittore(int i, java.lang.String _value) {
        this.scrittore[i] = _value;
    }


    /**
     * Gets the originatore value for this DocumentoPropertiesType.
     * 
     * @return originatore
     */
    public java.lang.String[] getOriginatore() {
        return originatore;
    }


    /**
     * Sets the originatore value for this DocumentoPropertiesType.
     * 
     * @param originatore
     */
    public void setOriginatore(java.lang.String[] originatore) {
        this.originatore = originatore;
    }

    public java.lang.String getOriginatore(int i) {
        return this.originatore[i];
    }

    public void setOriginatore(int i, java.lang.String _value) {
        this.originatore[i] = _value;
    }


    /**
     * Gets the destinatarioGiuridico value for this DocumentoPropertiesType.
     * 
     * @return destinatarioGiuridico
     */
    public java.lang.String[] getDestinatarioGiuridico() {
        return destinatarioGiuridico;
    }


    /**
     * Sets the destinatarioGiuridico value for this DocumentoPropertiesType.
     * 
     * @param destinatarioGiuridico
     */
    public void setDestinatarioGiuridico(java.lang.String[] destinatarioGiuridico) {
        this.destinatarioGiuridico = destinatarioGiuridico;
    }

    public java.lang.String getDestinatarioGiuridico(int i) {
        return this.destinatarioGiuridico[i];
    }

    public void setDestinatarioGiuridico(int i, java.lang.String _value) {
        this.destinatarioGiuridico[i] = _value;
    }


    /**
     * Gets the destinatarioFisico value for this DocumentoPropertiesType.
     * 
     * @return destinatarioFisico
     */
    public java.lang.String[] getDestinatarioFisico() {
        return destinatarioFisico;
    }


    /**
     * Sets the destinatarioFisico value for this DocumentoPropertiesType.
     * 
     * @param destinatarioFisico
     */
    public void setDestinatarioFisico(java.lang.String[] destinatarioFisico) {
        this.destinatarioFisico = destinatarioFisico;
    }

    public java.lang.String getDestinatarioFisico(int i) {
        return this.destinatarioFisico[i];
    }

    public void setDestinatarioFisico(int i, java.lang.String _value) {
        this.destinatarioFisico[i] = _value;
    }


    /**
     * Gets the soggettoProduttore value for this DocumentoPropertiesType.
     * 
     * @return soggettoProduttore
     */
    public java.lang.String[] getSoggettoProduttore() {
        return soggettoProduttore;
    }


    /**
     * Sets the soggettoProduttore value for this DocumentoPropertiesType.
     * 
     * @param soggettoProduttore
     */
    public void setSoggettoProduttore(java.lang.String[] soggettoProduttore) {
        this.soggettoProduttore = soggettoProduttore;
    }

    public java.lang.String getSoggettoProduttore(int i) {
        return this.soggettoProduttore[i];
    }

    public void setSoggettoProduttore(int i, java.lang.String _value) {
        this.soggettoProduttore[i] = _value;
    }


    /**
     * Gets the oggetto value for this DocumentoPropertiesType.
     * 
     * @return oggetto
     */
    public java.lang.String getOggetto() {
        return oggetto;
    }


    /**
     * Sets the oggetto value for this DocumentoPropertiesType.
     * 
     * @param oggetto
     */
    public void setOggetto(java.lang.String oggetto) {
        this.oggetto = oggetto;
    }


    /**
     * Gets the dataDocTopica value for this DocumentoPropertiesType.
     * 
     * @return dataDocTopica
     */
    public java.lang.String getDataDocTopica() {
        return dataDocTopica;
    }


    /**
     * Sets the dataDocTopica value for this DocumentoPropertiesType.
     * 
     * @param dataDocTopica
     */
    public void setDataDocTopica(java.lang.String dataDocTopica) {
        this.dataDocTopica = dataDocTopica;
    }


    /**
     * Gets the dataDocCronica value for this DocumentoPropertiesType.
     * 
     * @return dataDocCronica
     */
    public java.util.Date getDataDocCronica() {
        return dataDocCronica;
    }


    /**
     * Sets the dataDocCronica value for this DocumentoPropertiesType.
     * 
     * @param dataDocCronica
     */
    public void setDataDocCronica(java.util.Date dataDocCronica) {
        this.dataDocCronica = dataDocCronica;
    }


    /**
     * Gets the dataTrasmissione value for this DocumentoPropertiesType.
     * 
     * @return dataTrasmissione
     */
    public java.util.Date[] getDataTrasmissione() {
        return dataTrasmissione;
    }


    /**
     * Sets the dataTrasmissione value for this DocumentoPropertiesType.
     * 
     * @param dataTrasmissione
     */
    public void setDataTrasmissione(java.util.Date[] dataTrasmissione) {
        this.dataTrasmissione = dataTrasmissione;
    }

    public java.util.Date getDataTrasmissione(int i) {
        return this.dataTrasmissione[i];
    }

    public void setDataTrasmissione(int i, java.util.Date _value) {
        this.dataTrasmissione[i] = _value;
    }


    /**
     * Gets the dataRicevimento value for this DocumentoPropertiesType.
     * 
     * @return dataRicevimento
     */
    public java.util.Date getDataRicevimento() {
        return dataRicevimento;
    }


    /**
     * Sets the dataRicevimento value for this DocumentoPropertiesType.
     * 
     * @param dataRicevimento
     */
    public void setDataRicevimento(java.util.Date dataRicevimento) {
        this.dataRicevimento = dataRicevimento;
    }


    /**
     * Gets the numRepertorio value for this DocumentoPropertiesType.
     * 
     * @return numRepertorio
     */
    public java.lang.String getNumRepertorio() {
        return numRepertorio;
    }


    /**
     * Sets the numRepertorio value for this DocumentoPropertiesType.
     * 
     * @param numRepertorio
     */
    public void setNumRepertorio(java.lang.String numRepertorio) {
        this.numRepertorio = numRepertorio;
    }


    /**
     * Gets the forzareSeNumRepertorioEsistente value for this DocumentoPropertiesType.
     * 
     * @return forzareSeNumRepertorioEsistente
     */
    public boolean isForzareSeNumRepertorioEsistente() {
        return forzareSeNumRepertorioEsistente;
    }


    /**
     * Sets the forzareSeNumRepertorioEsistente value for this DocumentoPropertiesType.
     * 
     * @param forzareSeNumRepertorioEsistente
     */
    public void setForzareSeNumRepertorioEsistente(boolean forzareSeNumRepertorioEsistente) {
        this.forzareSeNumRepertorioEsistente = forzareSeNumRepertorioEsistente;
    }


    /**
     * Gets the docConAllegati value for this DocumentoPropertiesType.
     * 
     * @return docConAllegati
     */
    public boolean isDocConAllegati() {
        return docConAllegati;
    }


    /**
     * Sets the docConAllegati value for this DocumentoPropertiesType.
     * 
     * @param docConAllegati
     */
    public void setDocConAllegati(boolean docConAllegati) {
        this.docConAllegati = docConAllegati;
    }


    /**
     * Gets the docAllegato value for this DocumentoPropertiesType.
     * 
     * @return docAllegato
     */
    public boolean isDocAllegato() {
        return docAllegato;
    }


    /**
     * Sets the docAllegato value for this DocumentoPropertiesType.
     * 
     * @param docAllegato
     */
    public void setDocAllegato(boolean docAllegato) {
        this.docAllegato = docAllegato;
    }


    /**
     * Gets the firmaElettronicaDigitale value for this DocumentoPropertiesType.
     * 
     * @return firmaElettronicaDigitale
     */
    public boolean isFirmaElettronicaDigitale() {
        return firmaElettronicaDigitale;
    }


    /**
     * Sets the firmaElettronicaDigitale value for this DocumentoPropertiesType.
     * 
     * @param firmaElettronicaDigitale
     */
    public void setFirmaElettronicaDigitale(boolean firmaElettronicaDigitale) {
        this.firmaElettronicaDigitale = firmaElettronicaDigitale;
    }


    /**
     * Gets the docAutenticato value for this DocumentoPropertiesType.
     * 
     * @return docAutenticato
     */
    public boolean isDocAutenticato() {
        return docAutenticato;
    }


    /**
     * Sets the docAutenticato value for this DocumentoPropertiesType.
     * 
     * @param docAutenticato
     */
    public void setDocAutenticato(boolean docAutenticato) {
        this.docAutenticato = docAutenticato;
    }


    /**
     * Gets the docAutenticatoFirmaAutenticata value for this DocumentoPropertiesType.
     * 
     * @return docAutenticatoFirmaAutenticata
     */
    public boolean isDocAutenticatoFirmaAutenticata() {
        return docAutenticatoFirmaAutenticata;
    }


    /**
     * Sets the docAutenticatoFirmaAutenticata value for this DocumentoPropertiesType.
     * 
     * @param docAutenticatoFirmaAutenticata
     */
    public void setDocAutenticatoFirmaAutenticata(boolean docAutenticatoFirmaAutenticata) {
        this.docAutenticatoFirmaAutenticata = docAutenticatoFirmaAutenticata;
    }


    /**
     * Gets the docAutenticatoCopiaAutentica value for this DocumentoPropertiesType.
     * 
     * @return docAutenticatoCopiaAutentica
     */
    public boolean isDocAutenticatoCopiaAutentica() {
        return docAutenticatoCopiaAutentica;
    }


    /**
     * Sets the docAutenticatoCopiaAutentica value for this DocumentoPropertiesType.
     * 
     * @param docAutenticatoCopiaAutentica
     */
    public void setDocAutenticatoCopiaAutentica(boolean docAutenticatoCopiaAutentica) {
        this.docAutenticatoCopiaAutentica = docAutenticatoCopiaAutentica;
    }


    /**
     * Gets the idStatoDiEfficacia value for this DocumentoPropertiesType.
     * 
     * @return idStatoDiEfficacia
     */
    public it.csi.risca.riscabesrv.util.actaobject.IdStatoDiEfficaciaType getIdStatoDiEfficacia() {
        return idStatoDiEfficacia;
    }


    /**
     * Sets the idStatoDiEfficacia value for this DocumentoPropertiesType.
     * 
     * @param idStatoDiEfficacia
     */
    public void setIdStatoDiEfficacia(it.csi.risca.riscabesrv.util.actaobject.IdStatoDiEfficaciaType idStatoDiEfficacia) {
        this.idStatoDiEfficacia = idStatoDiEfficacia;
    }


    /**
     * Gets the idFormaDocumentaria value for this DocumentoPropertiesType.
     * 
     * @return idFormaDocumentaria
     */
    public it.csi.risca.riscabesrv.util.actaobject.IdFormaDocumentariaType getIdFormaDocumentaria() {
        return idFormaDocumentaria;
    }


    /**
     * Sets the idFormaDocumentaria value for this DocumentoPropertiesType.
     * 
     * @param idFormaDocumentaria
     */
    public void setIdFormaDocumentaria(it.csi.risca.riscabesrv.util.actaobject.IdFormaDocumentariaType idFormaDocumentaria) {
        this.idFormaDocumentaria = idFormaDocumentaria;
    }


    /**
     * Gets the firmaElettronica value for this DocumentoPropertiesType.
     * 
     * @return firmaElettronica
     */
    public boolean isFirmaElettronica() {
        return firmaElettronica;
    }


    /**
     * Sets the firmaElettronica value for this DocumentoPropertiesType.
     * 
     * @param firmaElettronica
     */
    public void setFirmaElettronica(boolean firmaElettronica) {
        this.firmaElettronica = firmaElettronica;
    }


    /**
     * Gets the idProtocollo value for this DocumentoPropertiesType.
     * 
     * @return idProtocollo
     */
    public it.csi.risca.riscabesrv.util.actaobject.ObjectIdType[] getIdProtocollo() {
        return idProtocollo;
    }


    /**
     * Sets the idProtocollo value for this DocumentoPropertiesType.
     * 
     * @param idProtocollo
     */
    public void setIdProtocollo(it.csi.risca.riscabesrv.util.actaobject.ObjectIdType[] idProtocollo) {
        this.idProtocollo = idProtocollo;
    }

    public it.csi.risca.riscabesrv.util.actaobject.ObjectIdType getIdProtocollo(int i) {
        return this.idProtocollo[i];
    }

    public void setIdProtocollo(int i, it.csi.risca.riscabesrv.util.actaobject.ObjectIdType _value) {
        this.idProtocollo[i] = _value;
    }


    /**
     * Gets the indiceClassificazione value for this DocumentoPropertiesType.
     * 
     * @return indiceClassificazione
     */
    public java.lang.String[] getIndiceClassificazione() {
        return indiceClassificazione;
    }


    /**
     * Sets the indiceClassificazione value for this DocumentoPropertiesType.
     * 
     * @param indiceClassificazione
     */
    public void setIndiceClassificazione(java.lang.String[] indiceClassificazione) {
        this.indiceClassificazione = indiceClassificazione;
    }

    public java.lang.String getIndiceClassificazione(int i) {
        return this.indiceClassificazione[i];
    }

    public void setIndiceClassificazione(int i, java.lang.String _value) {
        this.indiceClassificazione[i] = _value;
    }


    /**
     * Gets the indiceClassificazioneEstesa value for this DocumentoPropertiesType.
     * 
     * @return indiceClassificazioneEstesa
     */
    public java.lang.String[] getIndiceClassificazioneEstesa() {
        return indiceClassificazioneEstesa;
    }


    /**
     * Sets the indiceClassificazioneEstesa value for this DocumentoPropertiesType.
     * 
     * @param indiceClassificazioneEstesa
     */
    public void setIndiceClassificazioneEstesa(java.lang.String[] indiceClassificazioneEstesa) {
        this.indiceClassificazioneEstesa = indiceClassificazioneEstesa;
    }

    public java.lang.String getIndiceClassificazioneEstesa(int i) {
        return this.indiceClassificazioneEstesa[i];
    }

    public void setIndiceClassificazioneEstesa(int i, java.lang.String _value) {
        this.indiceClassificazioneEstesa[i] = _value;
    }


    /**
     * Gets the applicativoAlimentante value for this DocumentoPropertiesType.
     * 
     * @return applicativoAlimentante
     */
    public java.lang.String getApplicativoAlimentante() {
        return applicativoAlimentante;
    }


    /**
     * Sets the applicativoAlimentante value for this DocumentoPropertiesType.
     * 
     * @param applicativoAlimentante
     */
    public void setApplicativoAlimentante(java.lang.String applicativoAlimentante) {
        this.applicativoAlimentante = applicativoAlimentante;
    }


    /**
     * Gets the dataCancellazione value for this DocumentoPropertiesType.
     * 
     * @return dataCancellazione
     */
    public java.util.Date getDataCancellazione() {
        return dataCancellazione;
    }


    /**
     * Sets the dataCancellazione value for this DocumentoPropertiesType.
     * 
     * @param dataCancellazione
     */
    public void setDataCancellazione(java.util.Date dataCancellazione) {
        this.dataCancellazione = dataCancellazione;
    }


    /**
     * Gets the dataEsportazione value for this DocumentoPropertiesType.
     * 
     * @return dataEsportazione
     */
    public java.util.Date getDataEsportazione() {
        return dataEsportazione;
    }


    /**
     * Sets the dataEsportazione value for this DocumentoPropertiesType.
     * 
     * @param dataEsportazione
     */
    public void setDataEsportazione(java.util.Date dataEsportazione) {
        this.dataEsportazione = dataEsportazione;
    }


    /**
     * Gets the idVitalRecordCode value for this DocumentoPropertiesType.
     * 
     * @return idVitalRecordCode
     */
    public it.csi.risca.riscabesrv.util.actaobject.IdVitalRecordCodeType getIdVitalRecordCode() {
        return idVitalRecordCode;
    }


    /**
     * Sets the idVitalRecordCode value for this DocumentoPropertiesType.
     * 
     * @param idVitalRecordCode
     */
    public void setIdVitalRecordCode(it.csi.risca.riscabesrv.util.actaobject.IdVitalRecordCodeType idVitalRecordCode) {
        this.idVitalRecordCode = idVitalRecordCode;
    }


    /**
     * Gets the idAnnotazioniList value for this DocumentoPropertiesType.
     * 
     * @return idAnnotazioniList
     */
    public it.csi.risca.riscabesrv.util.actaobject.ObjectIdType[] getIdAnnotazioniList() {
        return idAnnotazioniList;
    }


    /**
     * Sets the idAnnotazioniList value for this DocumentoPropertiesType.
     * 
     * @param idAnnotazioniList
     */
    public void setIdAnnotazioniList(it.csi.risca.riscabesrv.util.actaobject.ObjectIdType[] idAnnotazioniList) {
        this.idAnnotazioniList = idAnnotazioniList;
    }

    public it.csi.risca.riscabesrv.util.actaobject.ObjectIdType getIdAnnotazioniList(int i) {
        return this.idAnnotazioniList[i];
    }

    public void setIdAnnotazioniList(int i, it.csi.risca.riscabesrv.util.actaobject.ObjectIdType _value) {
        this.idAnnotazioniList[i] = _value;
    }


    /**
     * Gets the renditionXMLList value for this DocumentoPropertiesType.
     * 
     * @return renditionXMLList
     */
    public java.lang.String[] getRenditionXMLList() {
        return renditionXMLList;
    }


    /**
     * Sets the renditionXMLList value for this DocumentoPropertiesType.
     * 
     * @param renditionXMLList
     */
    public void setRenditionXMLList(java.lang.String[] renditionXMLList) {
        this.renditionXMLList = renditionXMLList;
    }

    public java.lang.String getRenditionXMLList(int i) {
        return this.renditionXMLList[i];
    }

    public void setRenditionXMLList(int i, java.lang.String _value) {
        this.renditionXMLList[i] = _value;
    }


    /**
     * Gets the dataDocChiusuraRifTempUTC value for this DocumentoPropertiesType.
     * 
     * @return dataDocChiusuraRifTempUTC
     */
    public java.util.Date getDataDocChiusuraRifTempUTC() {
        return dataDocChiusuraRifTempUTC;
    }


    /**
     * Sets the dataDocChiusuraRifTempUTC value for this DocumentoPropertiesType.
     * 
     * @param dataDocChiusuraRifTempUTC
     */
    public void setDataDocChiusuraRifTempUTC(java.util.Date dataDocChiusuraRifTempUTC) {
        this.dataDocChiusuraRifTempUTC = dataDocChiusuraRifTempUTC;
    }


    /**
     * Gets the dataDocChiusura value for this DocumentoPropertiesType.
     * 
     * @return dataDocChiusura
     */
    public java.util.Date getDataDocChiusura() {
        return dataDocChiusura;
    }


    /**
     * Sets the dataDocChiusura value for this DocumentoPropertiesType.
     * 
     * @param dataDocChiusura
     */
    public void setDataDocChiusura(java.util.Date dataDocChiusura) {
        this.dataDocChiusura = dataDocChiusura;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof DocumentoPropertiesType)) return false;
        DocumentoPropertiesType other = (DocumentoPropertiesType) obj;

        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.idCorrente==null && other.getIdCorrente()==null) || 
             (this.idCorrente!=null &&
              this.idCorrente.equals(other.getIdCorrente()))) &&
            this.registrato == other.isRegistrato() &&
            this.modificabile == other.isModificabile() &&
            this.definitivo == other.isDefinitivo() &&
            this.origineInterna == other.isOrigineInterna() &&
            this.analogico == other.isAnalogico() &&
            this.rappresentazioneDigitale == other.isRappresentazioneDigitale() &&
            this.daConservare == other.isDaConservare() &&
            this.prontoPerConservazione == other.isProntoPerConservazione() &&
            ((this.daConservareDopoIl==null && other.getDaConservareDopoIl()==null) || 
             (this.daConservareDopoIl!=null &&
              this.daConservareDopoIl.equals(other.getDaConservareDopoIl()))) &&
            ((this.daConservarePrimaDel==null && other.getDaConservarePrimaDel()==null) || 
             (this.daConservarePrimaDel!=null &&
              this.daConservarePrimaDel.equals(other.getDaConservarePrimaDel()))) &&
            this.conservato == other.isConservato() &&
            this.datiPersonali == other.isDatiPersonali() &&
            this.datiSensibili == other.isDatiSensibili() &&
            this.datiRiservati == other.isDatiRiservati() &&
            ((this.dataCreazione==null && other.getDataCreazione()==null) || 
             (this.dataCreazione!=null &&
              this.dataCreazione.equals(other.getDataCreazione()))) &&
            ((this.paroleChiave==null && other.getParoleChiave()==null) || 
             (this.paroleChiave!=null &&
              this.paroleChiave.equals(other.getParoleChiave()))) &&
            this.modSMSQuarantena == other.isModSMSQuarantena() &&
            this.congelato == other.isCongelato() &&
            this.referenziabile == other.isReferenziabile() &&
            ((this.identificativoConservazione==null && other.getIdentificativoConservazione()==null) || 
             (this.identificativoConservazione!=null &&
              this.identificativoConservazione.equals(other.getIdentificativoConservazione()))) &&
            ((this.indiceEstesoOrigineEstratto==null && other.getIndiceEstesoOrigineEstratto()==null) || 
             (this.indiceEstesoOrigineEstratto!=null &&
              this.indiceEstesoOrigineEstratto.equals(other.getIndiceEstesoOrigineEstratto()))) &&
            ((this.indiciEstesiEstrattiGenerati==null && other.getIndiciEstesiEstrattiGenerati()==null) || 
             (this.indiciEstesiEstrattiGenerati!=null &&
              java.util.Arrays.equals(this.indiciEstesiEstrattiGenerati, other.getIndiciEstesiEstrattiGenerati()))) &&
            ((this.autoreGiuridico==null && other.getAutoreGiuridico()==null) || 
             (this.autoreGiuridico!=null &&
              java.util.Arrays.equals(this.autoreGiuridico, other.getAutoreGiuridico()))) &&
            ((this.autoreFisico==null && other.getAutoreFisico()==null) || 
             (this.autoreFisico!=null &&
              java.util.Arrays.equals(this.autoreFisico, other.getAutoreFisico()))) &&
            ((this.scrittore==null && other.getScrittore()==null) || 
             (this.scrittore!=null &&
              java.util.Arrays.equals(this.scrittore, other.getScrittore()))) &&
            ((this.originatore==null && other.getOriginatore()==null) || 
             (this.originatore!=null &&
              java.util.Arrays.equals(this.originatore, other.getOriginatore()))) &&
            ((this.destinatarioGiuridico==null && other.getDestinatarioGiuridico()==null) || 
             (this.destinatarioGiuridico!=null &&
              java.util.Arrays.equals(this.destinatarioGiuridico, other.getDestinatarioGiuridico()))) &&
            ((this.destinatarioFisico==null && other.getDestinatarioFisico()==null) || 
             (this.destinatarioFisico!=null &&
              java.util.Arrays.equals(this.destinatarioFisico, other.getDestinatarioFisico()))) &&
            ((this.soggettoProduttore==null && other.getSoggettoProduttore()==null) || 
             (this.soggettoProduttore!=null &&
              java.util.Arrays.equals(this.soggettoProduttore, other.getSoggettoProduttore()))) &&
            ((this.oggetto==null && other.getOggetto()==null) || 
             (this.oggetto!=null &&
              this.oggetto.equals(other.getOggetto()))) &&
            ((this.dataDocTopica==null && other.getDataDocTopica()==null) || 
             (this.dataDocTopica!=null &&
              this.dataDocTopica.equals(other.getDataDocTopica()))) &&
            ((this.dataDocCronica==null && other.getDataDocCronica()==null) || 
             (this.dataDocCronica!=null &&
              this.dataDocCronica.equals(other.getDataDocCronica()))) &&
            ((this.dataTrasmissione==null && other.getDataTrasmissione()==null) || 
             (this.dataTrasmissione!=null &&
              java.util.Arrays.equals(this.dataTrasmissione, other.getDataTrasmissione()))) &&
            ((this.dataRicevimento==null && other.getDataRicevimento()==null) || 
             (this.dataRicevimento!=null &&
              this.dataRicevimento.equals(other.getDataRicevimento()))) &&
            ((this.numRepertorio==null && other.getNumRepertorio()==null) || 
             (this.numRepertorio!=null &&
              this.numRepertorio.equals(other.getNumRepertorio()))) &&
            this.forzareSeNumRepertorioEsistente == other.isForzareSeNumRepertorioEsistente() &&
            this.docConAllegati == other.isDocConAllegati() &&
            this.docAllegato == other.isDocAllegato() &&
            this.firmaElettronicaDigitale == other.isFirmaElettronicaDigitale() &&
            this.docAutenticato == other.isDocAutenticato() &&
            this.docAutenticatoFirmaAutenticata == other.isDocAutenticatoFirmaAutenticata() &&
            this.docAutenticatoCopiaAutentica == other.isDocAutenticatoCopiaAutentica() &&
            ((this.idStatoDiEfficacia==null && other.getIdStatoDiEfficacia()==null) || 
             (this.idStatoDiEfficacia!=null &&
              this.idStatoDiEfficacia.equals(other.getIdStatoDiEfficacia()))) &&
            ((this.idFormaDocumentaria==null && other.getIdFormaDocumentaria()==null) || 
             (this.idFormaDocumentaria!=null &&
              this.idFormaDocumentaria.equals(other.getIdFormaDocumentaria()))) &&
            this.firmaElettronica == other.isFirmaElettronica() &&
            ((this.idProtocollo==null && other.getIdProtocollo()==null) || 
             (this.idProtocollo!=null &&
              java.util.Arrays.equals(this.idProtocollo, other.getIdProtocollo()))) &&
            ((this.indiceClassificazione==null && other.getIndiceClassificazione()==null) || 
             (this.indiceClassificazione!=null &&
              java.util.Arrays.equals(this.indiceClassificazione, other.getIndiceClassificazione()))) &&
            ((this.indiceClassificazioneEstesa==null && other.getIndiceClassificazioneEstesa()==null) || 
             (this.indiceClassificazioneEstesa!=null &&
              java.util.Arrays.equals(this.indiceClassificazioneEstesa, other.getIndiceClassificazioneEstesa()))) &&
            ((this.applicativoAlimentante==null && other.getApplicativoAlimentante()==null) || 
             (this.applicativoAlimentante!=null &&
              this.applicativoAlimentante.equals(other.getApplicativoAlimentante()))) &&
            ((this.dataCancellazione==null && other.getDataCancellazione()==null) || 
             (this.dataCancellazione!=null &&
              this.dataCancellazione.equals(other.getDataCancellazione()))) &&
            ((this.dataEsportazione==null && other.getDataEsportazione()==null) || 
             (this.dataEsportazione!=null &&
              this.dataEsportazione.equals(other.getDataEsportazione()))) &&
            ((this.idVitalRecordCode==null && other.getIdVitalRecordCode()==null) || 
             (this.idVitalRecordCode!=null &&
              this.idVitalRecordCode.equals(other.getIdVitalRecordCode()))) &&
            ((this.idAnnotazioniList==null && other.getIdAnnotazioniList()==null) || 
             (this.idAnnotazioniList!=null &&
              java.util.Arrays.equals(this.idAnnotazioniList, other.getIdAnnotazioniList()))) &&
            ((this.renditionXMLList==null && other.getRenditionXMLList()==null) || 
             (this.renditionXMLList!=null &&
              java.util.Arrays.equals(this.renditionXMLList, other.getRenditionXMLList()))) &&
            ((this.dataDocChiusuraRifTempUTC==null && other.getDataDocChiusuraRifTempUTC()==null) || 
             (this.dataDocChiusuraRifTempUTC!=null &&
              this.dataDocChiusuraRifTempUTC.equals(other.getDataDocChiusuraRifTempUTC()))) &&
            ((this.dataDocChiusura==null && other.getDataDocChiusura()==null) || 
             (this.dataDocChiusura!=null &&
              this.dataDocChiusura.equals(other.getDataDocChiusura())));
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
        if (getIdCorrente() != null) {
            _hashCode += getIdCorrente().hashCode();
        }
        _hashCode += (isRegistrato() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isModificabile() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isDefinitivo() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isOrigineInterna() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isAnalogico() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isRappresentazioneDigitale() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isDaConservare() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isProntoPerConservazione() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getDaConservareDopoIl() != null) {
            _hashCode += getDaConservareDopoIl().hashCode();
        }
        if (getDaConservarePrimaDel() != null) {
            _hashCode += getDaConservarePrimaDel().hashCode();
        }
        _hashCode += (isConservato() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isDatiPersonali() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isDatiSensibili() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isDatiRiservati() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getDataCreazione() != null) {
            _hashCode += getDataCreazione().hashCode();
        }
        if (getParoleChiave() != null) {
            _hashCode += getParoleChiave().hashCode();
        }
        _hashCode += (isModSMSQuarantena() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isCongelato() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isReferenziabile() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getIdentificativoConservazione() != null) {
            _hashCode += getIdentificativoConservazione().hashCode();
        }
        if (getIndiceEstesoOrigineEstratto() != null) {
            _hashCode += getIndiceEstesoOrigineEstratto().hashCode();
        }
        if (getIndiciEstesiEstrattiGenerati() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getIndiciEstesiEstrattiGenerati());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getIndiciEstesiEstrattiGenerati(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getAutoreGiuridico() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getAutoreGiuridico());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getAutoreGiuridico(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getAutoreFisico() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getAutoreFisico());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getAutoreFisico(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getScrittore() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getScrittore());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getScrittore(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getOriginatore() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getOriginatore());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getOriginatore(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getDestinatarioGiuridico() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDestinatarioGiuridico());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDestinatarioGiuridico(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getDestinatarioFisico() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDestinatarioFisico());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDestinatarioFisico(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getSoggettoProduttore() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getSoggettoProduttore());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getSoggettoProduttore(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getOggetto() != null) {
            _hashCode += getOggetto().hashCode();
        }
        if (getDataDocTopica() != null) {
            _hashCode += getDataDocTopica().hashCode();
        }
        if (getDataDocCronica() != null) {
            _hashCode += getDataDocCronica().hashCode();
        }
        if (getDataTrasmissione() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDataTrasmissione());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDataTrasmissione(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getDataRicevimento() != null) {
            _hashCode += getDataRicevimento().hashCode();
        }
        if (getNumRepertorio() != null) {
            _hashCode += getNumRepertorio().hashCode();
        }
        _hashCode += (isForzareSeNumRepertorioEsistente() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isDocConAllegati() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isDocAllegato() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isFirmaElettronicaDigitale() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isDocAutenticato() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isDocAutenticatoFirmaAutenticata() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isDocAutenticatoCopiaAutentica() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getIdStatoDiEfficacia() != null) {
            _hashCode += getIdStatoDiEfficacia().hashCode();
        }
        if (getIdFormaDocumentaria() != null) {
            _hashCode += getIdFormaDocumentaria().hashCode();
        }
        _hashCode += (isFirmaElettronica() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getIdProtocollo() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getIdProtocollo());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getIdProtocollo(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getIndiceClassificazione() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getIndiceClassificazione());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getIndiceClassificazione(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getIndiceClassificazioneEstesa() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getIndiceClassificazioneEstesa());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getIndiceClassificazioneEstesa(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getApplicativoAlimentante() != null) {
            _hashCode += getApplicativoAlimentante().hashCode();
        }
        if (getDataCancellazione() != null) {
            _hashCode += getDataCancellazione().hashCode();
        }
        if (getDataEsportazione() != null) {
            _hashCode += getDataEsportazione().hashCode();
        }
        if (getIdVitalRecordCode() != null) {
            _hashCode += getIdVitalRecordCode().hashCode();
        }
        if (getIdAnnotazioniList() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getIdAnnotazioniList());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getIdAnnotazioniList(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getRenditionXMLList() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getRenditionXMLList());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getRenditionXMLList(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getDataDocChiusuraRifTempUTC() != null) {
            _hashCode += getDataDocChiusuraRifTempUTC().hashCode();
        }
        if (getDataDocChiusura() != null) {
            _hashCode += getDataDocChiusura().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DocumentoPropertiesType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "DocumentoPropertiesType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idCorrente");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "idCorrente"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("registrato");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "registrato"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("modificabile");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "modificabile"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("definitivo");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "definitivo"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("origineInterna");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "origineInterna"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("analogico");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "analogico"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("rappresentazioneDigitale");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "rappresentazioneDigitale"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("daConservare");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "daConservare"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("prontoPerConservazione");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "prontoPerConservazione"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("daConservareDopoIl");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "daConservareDopoIl"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "date"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("daConservarePrimaDel");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "daConservarePrimaDel"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "date"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("conservato");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "conservato"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("datiPersonali");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "datiPersonali"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("datiSensibili");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "datiSensibili"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("datiRiservati");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "datiRiservati"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataCreazione");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "dataCreazione"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "date"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("paroleChiave");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "paroleChiave"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("modSMSQuarantena");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "modSMSQuarantena"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("congelato");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "congelato"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("referenziabile");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "referenziabile"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("identificativoConservazione");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "identificativoConservazione"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("indiceEstesoOrigineEstratto");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "indiceEstesoOrigineEstratto"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "XMLType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("indiciEstesiEstrattiGenerati");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "indiciEstesiEstrattiGenerati"));
        elemField.setXmlType(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "IndiceClassificazioneEstesaXMLType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("autoreGiuridico");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "autoreGiuridico"));
        elemField.setXmlType(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "AutoreGiuridicoType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("autoreFisico");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "autoreFisico"));
        elemField.setXmlType(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "AutoreFisicoType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("scrittore");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "scrittore"));
        elemField.setXmlType(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "ScrittoreType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("originatore");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "originatore"));
        elemField.setXmlType(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "OriginatoreType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("destinatarioGiuridico");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "destinatarioGiuridico"));
        elemField.setXmlType(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "DestinatarioGiuridicoType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("destinatarioFisico");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "destinatarioFisico"));
        elemField.setXmlType(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "DestinatarioFisicoType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("soggettoProduttore");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "soggettoProduttore"));
        elemField.setXmlType(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "SoggettoProduttoreType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("oggetto");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "oggetto"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataDocTopica");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "dataDocTopica"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataDocCronica");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "dataDocCronica"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "date"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataTrasmissione");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "dataTrasmissione"));
        elemField.setXmlType(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "DataTrasmissioneType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataRicevimento");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "dataRicevimento"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "date"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numRepertorio");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "numRepertorio"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("forzareSeNumRepertorioEsistente");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "forzareSeNumRepertorioEsistente"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("docConAllegati");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "docConAllegati"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("docAllegato");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "docAllegato"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("firmaElettronicaDigitale");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "firmaElettronicaDigitale"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("docAutenticato");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "docAutenticato"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("docAutenticatoFirmaAutenticata");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "docAutenticatoFirmaAutenticata"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("docAutenticatoCopiaAutentica");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "docAutenticatoCopiaAutentica"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idStatoDiEfficacia");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "idStatoDiEfficacia"));
        elemField.setXmlType(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "IdStatoDiEfficaciaType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idFormaDocumentaria");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "idFormaDocumentaria"));
        elemField.setXmlType(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "IdFormaDocumentariaType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("firmaElettronica");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "firmaElettronica"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idProtocollo");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "idProtocollo"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("indiceClassificazione");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "indiceClassificazione"));
        elemField.setXmlType(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "IndiceClassificazioneXMLType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("indiceClassificazioneEstesa");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "indiceClassificazioneEstesa"));
        elemField.setXmlType(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "IndiceClassificazioneEstesaXMLType"));
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("applicativoAlimentante");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "applicativoAlimentante"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataCancellazione");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "dataCancellazione"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "date"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataEsportazione");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "dataEsportazione"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "date"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idVitalRecordCode");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "idVitalRecordCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "IdVitalRecordCodeType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idAnnotazioniList");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "idAnnotazioniList"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("renditionXMLList");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "renditionXMLList"));
        elemField.setXmlType(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "RenditionXMLType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataDocChiusuraRifTempUTC");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "dataDocChiusuraRifTempUTC"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "date"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataDocChiusura");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "dataDocChiusura"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "date"));
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

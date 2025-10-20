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
 * RegistrazionePropertiesType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actaofficialbook;

public class RegistrazionePropertiesType  extends it.csi.risca.riscabesrv.util.actaofficialbook.OfficialBookPropertiesType  implements java.io.Serializable {
    private java.lang.String uuid;

    private java.lang.String codice;

    private it.csi.risca.riscabesrv.util.actaofficialbook.DecodificaType stato;

    private it.csi.risca.riscabesrv.util.actaofficialbook.DecodificaType condizione;

    private boolean storico;

    private it.csi.risca.riscabesrv.util.actaofficialbook.DecodificaType tipoRegistrazione;

    private boolean riservato;

    private java.lang.String oggetto;

    private java.lang.String classificazioneProposta;

    private java.lang.String mezzoTrasmissivo;

    private java.util.Date dataDocumento;

    private java.util.Date dataRicezione;

    private java.util.Date dataTimbroPostale;

    private java.lang.String codiceProtocolloMittente;

    private int annoProtocolloMittente;

    private java.util.Date dataProtocolloMittente;

    private int numeroAllegati;

    private java.util.Date dataProtocollo;

    private java.util.Date dataInoltro;

    private java.lang.String codiceEmergenza;

    private java.util.Date dataEmergenza;

    private java.util.Date dataRecupero;

    private java.util.Date dataAnnullamento;

    private it.csi.risca.riscabesrv.util.actaofficialbook.DecodificaType motivoCreazione;

    private long idCaricamento;

    private it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idAOOProtocollante;

    private it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idAOOResponsabile;

    private it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idStrutturaProtocollante;

    private it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idStrutturaResponsabile;

    private it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idNodoProtocollante;

    private it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idNodoResponsabile;

    private it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idUtenteAnnullamento;

    private it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idUtenteCreazione;

    private it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idUtenteProtocollista;

    private it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idClassificazione;

    private it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType[] idDocumento;

    private it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idRegistro;

    private it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idRegistrazionePrecedente;

    private it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType[] idRegistrazioneSuccessiva;

    private it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType[] idCorrispondente;

    private it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType[] idAnnotazioneOB;

    private it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType[] idMessaggio;

    private it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType[] idSmistamento;

    public RegistrazionePropertiesType() {
    }

    public RegistrazionePropertiesType(
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType objectId,
           it.csi.risca.riscabesrv.util.actaofficialbook.ChangeTokenType changeToken,
           java.lang.String objectTypeId,
           java.lang.String uuid,
           java.lang.String codice,
           it.csi.risca.riscabesrv.util.actaofficialbook.DecodificaType stato,
           it.csi.risca.riscabesrv.util.actaofficialbook.DecodificaType condizione,
           boolean storico,
           it.csi.risca.riscabesrv.util.actaofficialbook.DecodificaType tipoRegistrazione,
           boolean riservato,
           java.lang.String oggetto,
           java.lang.String classificazioneProposta,
           java.lang.String mezzoTrasmissivo,
           java.util.Date dataDocumento,
           java.util.Date dataRicezione,
           java.util.Date dataTimbroPostale,
           java.lang.String codiceProtocolloMittente,
           int annoProtocolloMittente,
           java.util.Date dataProtocolloMittente,
           int numeroAllegati,
           java.util.Date dataProtocollo,
           java.util.Date dataInoltro,
           java.lang.String codiceEmergenza,
           java.util.Date dataEmergenza,
           java.util.Date dataRecupero,
           java.util.Date dataAnnullamento,
           it.csi.risca.riscabesrv.util.actaofficialbook.DecodificaType motivoCreazione,
           long idCaricamento,
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idAOOProtocollante,
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idAOOResponsabile,
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idStrutturaProtocollante,
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idStrutturaResponsabile,
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idNodoProtocollante,
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idNodoResponsabile,
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idUtenteAnnullamento,
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idUtenteCreazione,
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idUtenteProtocollista,
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idClassificazione,
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType[] idDocumento,
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idRegistro,
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idRegistrazionePrecedente,
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType[] idRegistrazioneSuccessiva,
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType[] idCorrispondente,
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType[] idAnnotazioneOB,
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType[] idMessaggio,
           it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType[] idSmistamento) {
        super(
            objectId,
            changeToken,
            objectTypeId);
        this.uuid = uuid;
        this.codice = codice;
        this.stato = stato;
        this.condizione = condizione;
        this.storico = storico;
        this.tipoRegistrazione = tipoRegistrazione;
        this.riservato = riservato;
        this.oggetto = oggetto;
        this.classificazioneProposta = classificazioneProposta;
        this.mezzoTrasmissivo = mezzoTrasmissivo;
        this.dataDocumento = dataDocumento;
        this.dataRicezione = dataRicezione;
        this.dataTimbroPostale = dataTimbroPostale;
        this.codiceProtocolloMittente = codiceProtocolloMittente;
        this.annoProtocolloMittente = annoProtocolloMittente;
        this.dataProtocolloMittente = dataProtocolloMittente;
        this.numeroAllegati = numeroAllegati;
        this.dataProtocollo = dataProtocollo;
        this.dataInoltro = dataInoltro;
        this.codiceEmergenza = codiceEmergenza;
        this.dataEmergenza = dataEmergenza;
        this.dataRecupero = dataRecupero;
        this.dataAnnullamento = dataAnnullamento;
        this.motivoCreazione = motivoCreazione;
        this.idCaricamento = idCaricamento;
        this.idAOOProtocollante = idAOOProtocollante;
        this.idAOOResponsabile = idAOOResponsabile;
        this.idStrutturaProtocollante = idStrutturaProtocollante;
        this.idStrutturaResponsabile = idStrutturaResponsabile;
        this.idNodoProtocollante = idNodoProtocollante;
        this.idNodoResponsabile = idNodoResponsabile;
        this.idUtenteAnnullamento = idUtenteAnnullamento;
        this.idUtenteCreazione = idUtenteCreazione;
        this.idUtenteProtocollista = idUtenteProtocollista;
        this.idClassificazione = idClassificazione;
        this.idDocumento = idDocumento;
        this.idRegistro = idRegistro;
        this.idRegistrazionePrecedente = idRegistrazionePrecedente;
        this.idRegistrazioneSuccessiva = idRegistrazioneSuccessiva;
        this.idCorrispondente = idCorrispondente;
        this.idAnnotazioneOB = idAnnotazioneOB;
        this.idMessaggio = idMessaggio;
        this.idSmistamento = idSmistamento;
    }


    /**
     * Gets the uuid value for this RegistrazionePropertiesType.
     * 
     * @return uuid
     */
    public java.lang.String getUuid() {
        return uuid;
    }


    /**
     * Sets the uuid value for this RegistrazionePropertiesType.
     * 
     * @param uuid
     */
    public void setUuid(java.lang.String uuid) {
        this.uuid = uuid;
    }


    /**
     * Gets the codice value for this RegistrazionePropertiesType.
     * 
     * @return codice
     */
    public java.lang.String getCodice() {
        return codice;
    }


    /**
     * Sets the codice value for this RegistrazionePropertiesType.
     * 
     * @param codice
     */
    public void setCodice(java.lang.String codice) {
        this.codice = codice;
    }


    /**
     * Gets the stato value for this RegistrazionePropertiesType.
     * 
     * @return stato
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.DecodificaType getStato() {
        return stato;
    }


    /**
     * Sets the stato value for this RegistrazionePropertiesType.
     * 
     * @param stato
     */
    public void setStato(it.csi.risca.riscabesrv.util.actaofficialbook.DecodificaType stato) {
        this.stato = stato;
    }


    /**
     * Gets the condizione value for this RegistrazionePropertiesType.
     * 
     * @return condizione
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.DecodificaType getCondizione() {
        return condizione;
    }


    /**
     * Sets the condizione value for this RegistrazionePropertiesType.
     * 
     * @param condizione
     */
    public void setCondizione(it.csi.risca.riscabesrv.util.actaofficialbook.DecodificaType condizione) {
        this.condizione = condizione;
    }


    /**
     * Gets the storico value for this RegistrazionePropertiesType.
     * 
     * @return storico
     */
    public boolean isStorico() {
        return storico;
    }


    /**
     * Sets the storico value for this RegistrazionePropertiesType.
     * 
     * @param storico
     */
    public void setStorico(boolean storico) {
        this.storico = storico;
    }


    /**
     * Gets the tipoRegistrazione value for this RegistrazionePropertiesType.
     * 
     * @return tipoRegistrazione
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.DecodificaType getTipoRegistrazione() {
        return tipoRegistrazione;
    }


    /**
     * Sets the tipoRegistrazione value for this RegistrazionePropertiesType.
     * 
     * @param tipoRegistrazione
     */
    public void setTipoRegistrazione(it.csi.risca.riscabesrv.util.actaofficialbook.DecodificaType tipoRegistrazione) {
        this.tipoRegistrazione = tipoRegistrazione;
    }


    /**
     * Gets the riservato value for this RegistrazionePropertiesType.
     * 
     * @return riservato
     */
    public boolean isRiservato() {
        return riservato;
    }


    /**
     * Sets the riservato value for this RegistrazionePropertiesType.
     * 
     * @param riservato
     */
    public void setRiservato(boolean riservato) {
        this.riservato = riservato;
    }


    /**
     * Gets the oggetto value for this RegistrazionePropertiesType.
     * 
     * @return oggetto
     */
    public java.lang.String getOggetto() {
        return oggetto;
    }


    /**
     * Sets the oggetto value for this RegistrazionePropertiesType.
     * 
     * @param oggetto
     */
    public void setOggetto(java.lang.String oggetto) {
        this.oggetto = oggetto;
    }


    /**
     * Gets the classificazioneProposta value for this RegistrazionePropertiesType.
     * 
     * @return classificazioneProposta
     */
    public java.lang.String getClassificazioneProposta() {
        return classificazioneProposta;
    }


    /**
     * Sets the classificazioneProposta value for this RegistrazionePropertiesType.
     * 
     * @param classificazioneProposta
     */
    public void setClassificazioneProposta(java.lang.String classificazioneProposta) {
        this.classificazioneProposta = classificazioneProposta;
    }


    /**
     * Gets the mezzoTrasmissivo value for this RegistrazionePropertiesType.
     * 
     * @return mezzoTrasmissivo
     */
    public java.lang.String getMezzoTrasmissivo() {
        return mezzoTrasmissivo;
    }


    /**
     * Sets the mezzoTrasmissivo value for this RegistrazionePropertiesType.
     * 
     * @param mezzoTrasmissivo
     */
    public void setMezzoTrasmissivo(java.lang.String mezzoTrasmissivo) {
        this.mezzoTrasmissivo = mezzoTrasmissivo;
    }


    /**
     * Gets the dataDocumento value for this RegistrazionePropertiesType.
     * 
     * @return dataDocumento
     */
    public java.util.Date getDataDocumento() {
        return dataDocumento;
    }


    /**
     * Sets the dataDocumento value for this RegistrazionePropertiesType.
     * 
     * @param dataDocumento
     */
    public void setDataDocumento(java.util.Date dataDocumento) {
        this.dataDocumento = dataDocumento;
    }


    /**
     * Gets the dataRicezione value for this RegistrazionePropertiesType.
     * 
     * @return dataRicezione
     */
    public java.util.Date getDataRicezione() {
        return dataRicezione;
    }


    /**
     * Sets the dataRicezione value for this RegistrazionePropertiesType.
     * 
     * @param dataRicezione
     */
    public void setDataRicezione(java.util.Date dataRicezione) {
        this.dataRicezione = dataRicezione;
    }


    /**
     * Gets the dataTimbroPostale value for this RegistrazionePropertiesType.
     * 
     * @return dataTimbroPostale
     */
    public java.util.Date getDataTimbroPostale() {
        return dataTimbroPostale;
    }


    /**
     * Sets the dataTimbroPostale value for this RegistrazionePropertiesType.
     * 
     * @param dataTimbroPostale
     */
    public void setDataTimbroPostale(java.util.Date dataTimbroPostale) {
        this.dataTimbroPostale = dataTimbroPostale;
    }


    /**
     * Gets the codiceProtocolloMittente value for this RegistrazionePropertiesType.
     * 
     * @return codiceProtocolloMittente
     */
    public java.lang.String getCodiceProtocolloMittente() {
        return codiceProtocolloMittente;
    }


    /**
     * Sets the codiceProtocolloMittente value for this RegistrazionePropertiesType.
     * 
     * @param codiceProtocolloMittente
     */
    public void setCodiceProtocolloMittente(java.lang.String codiceProtocolloMittente) {
        this.codiceProtocolloMittente = codiceProtocolloMittente;
    }


    /**
     * Gets the annoProtocolloMittente value for this RegistrazionePropertiesType.
     * 
     * @return annoProtocolloMittente
     */
    public int getAnnoProtocolloMittente() {
        return annoProtocolloMittente;
    }


    /**
     * Sets the annoProtocolloMittente value for this RegistrazionePropertiesType.
     * 
     * @param annoProtocolloMittente
     */
    public void setAnnoProtocolloMittente(int annoProtocolloMittente) {
        this.annoProtocolloMittente = annoProtocolloMittente;
    }


    /**
     * Gets the dataProtocolloMittente value for this RegistrazionePropertiesType.
     * 
     * @return dataProtocolloMittente
     */
    public java.util.Date getDataProtocolloMittente() {
        return dataProtocolloMittente;
    }


    /**
     * Sets the dataProtocolloMittente value for this RegistrazionePropertiesType.
     * 
     * @param dataProtocolloMittente
     */
    public void setDataProtocolloMittente(java.util.Date dataProtocolloMittente) {
        this.dataProtocolloMittente = dataProtocolloMittente;
    }


    /**
     * Gets the numeroAllegati value for this RegistrazionePropertiesType.
     * 
     * @return numeroAllegati
     */
    public int getNumeroAllegati() {
        return numeroAllegati;
    }


    /**
     * Sets the numeroAllegati value for this RegistrazionePropertiesType.
     * 
     * @param numeroAllegati
     */
    public void setNumeroAllegati(int numeroAllegati) {
        this.numeroAllegati = numeroAllegati;
    }


    /**
     * Gets the dataProtocollo value for this RegistrazionePropertiesType.
     * 
     * @return dataProtocollo
     */
    public java.util.Date getDataProtocollo() {
        return dataProtocollo;
    }


    /**
     * Sets the dataProtocollo value for this RegistrazionePropertiesType.
     * 
     * @param dataProtocollo
     */
    public void setDataProtocollo(java.util.Date dataProtocollo) {
        this.dataProtocollo = dataProtocollo;
    }


    /**
     * Gets the dataInoltro value for this RegistrazionePropertiesType.
     * 
     * @return dataInoltro
     */
    public java.util.Date getDataInoltro() {
        return dataInoltro;
    }


    /**
     * Sets the dataInoltro value for this RegistrazionePropertiesType.
     * 
     * @param dataInoltro
     */
    public void setDataInoltro(java.util.Date dataInoltro) {
        this.dataInoltro = dataInoltro;
    }


    /**
     * Gets the codiceEmergenza value for this RegistrazionePropertiesType.
     * 
     * @return codiceEmergenza
     */
    public java.lang.String getCodiceEmergenza() {
        return codiceEmergenza;
    }


    /**
     * Sets the codiceEmergenza value for this RegistrazionePropertiesType.
     * 
     * @param codiceEmergenza
     */
    public void setCodiceEmergenza(java.lang.String codiceEmergenza) {
        this.codiceEmergenza = codiceEmergenza;
    }


    /**
     * Gets the dataEmergenza value for this RegistrazionePropertiesType.
     * 
     * @return dataEmergenza
     */
    public java.util.Date getDataEmergenza() {
        return dataEmergenza;
    }


    /**
     * Sets the dataEmergenza value for this RegistrazionePropertiesType.
     * 
     * @param dataEmergenza
     */
    public void setDataEmergenza(java.util.Date dataEmergenza) {
        this.dataEmergenza = dataEmergenza;
    }


    /**
     * Gets the dataRecupero value for this RegistrazionePropertiesType.
     * 
     * @return dataRecupero
     */
    public java.util.Date getDataRecupero() {
        return dataRecupero;
    }


    /**
     * Sets the dataRecupero value for this RegistrazionePropertiesType.
     * 
     * @param dataRecupero
     */
    public void setDataRecupero(java.util.Date dataRecupero) {
        this.dataRecupero = dataRecupero;
    }


    /**
     * Gets the dataAnnullamento value for this RegistrazionePropertiesType.
     * 
     * @return dataAnnullamento
     */
    public java.util.Date getDataAnnullamento() {
        return dataAnnullamento;
    }


    /**
     * Sets the dataAnnullamento value for this RegistrazionePropertiesType.
     * 
     * @param dataAnnullamento
     */
    public void setDataAnnullamento(java.util.Date dataAnnullamento) {
        this.dataAnnullamento = dataAnnullamento;
    }


    /**
     * Gets the motivoCreazione value for this RegistrazionePropertiesType.
     * 
     * @return motivoCreazione
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.DecodificaType getMotivoCreazione() {
        return motivoCreazione;
    }


    /**
     * Sets the motivoCreazione value for this RegistrazionePropertiesType.
     * 
     * @param motivoCreazione
     */
    public void setMotivoCreazione(it.csi.risca.riscabesrv.util.actaofficialbook.DecodificaType motivoCreazione) {
        this.motivoCreazione = motivoCreazione;
    }


    /**
     * Gets the idCaricamento value for this RegistrazionePropertiesType.
     * 
     * @return idCaricamento
     */
    public long getIdCaricamento() {
        return idCaricamento;
    }


    /**
     * Sets the idCaricamento value for this RegistrazionePropertiesType.
     * 
     * @param idCaricamento
     */
    public void setIdCaricamento(long idCaricamento) {
        this.idCaricamento = idCaricamento;
    }


    /**
     * Gets the idAOOProtocollante value for this RegistrazionePropertiesType.
     * 
     * @return idAOOProtocollante
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType getIdAOOProtocollante() {
        return idAOOProtocollante;
    }


    /**
     * Sets the idAOOProtocollante value for this RegistrazionePropertiesType.
     * 
     * @param idAOOProtocollante
     */
    public void setIdAOOProtocollante(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idAOOProtocollante) {
        this.idAOOProtocollante = idAOOProtocollante;
    }


    /**
     * Gets the idAOOResponsabile value for this RegistrazionePropertiesType.
     * 
     * @return idAOOResponsabile
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType getIdAOOResponsabile() {
        return idAOOResponsabile;
    }


    /**
     * Sets the idAOOResponsabile value for this RegistrazionePropertiesType.
     * 
     * @param idAOOResponsabile
     */
    public void setIdAOOResponsabile(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idAOOResponsabile) {
        this.idAOOResponsabile = idAOOResponsabile;
    }


    /**
     * Gets the idStrutturaProtocollante value for this RegistrazionePropertiesType.
     * 
     * @return idStrutturaProtocollante
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType getIdStrutturaProtocollante() {
        return idStrutturaProtocollante;
    }


    /**
     * Sets the idStrutturaProtocollante value for this RegistrazionePropertiesType.
     * 
     * @param idStrutturaProtocollante
     */
    public void setIdStrutturaProtocollante(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idStrutturaProtocollante) {
        this.idStrutturaProtocollante = idStrutturaProtocollante;
    }


    /**
     * Gets the idStrutturaResponsabile value for this RegistrazionePropertiesType.
     * 
     * @return idStrutturaResponsabile
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType getIdStrutturaResponsabile() {
        return idStrutturaResponsabile;
    }


    /**
     * Sets the idStrutturaResponsabile value for this RegistrazionePropertiesType.
     * 
     * @param idStrutturaResponsabile
     */
    public void setIdStrutturaResponsabile(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idStrutturaResponsabile) {
        this.idStrutturaResponsabile = idStrutturaResponsabile;
    }


    /**
     * Gets the idNodoProtocollante value for this RegistrazionePropertiesType.
     * 
     * @return idNodoProtocollante
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType getIdNodoProtocollante() {
        return idNodoProtocollante;
    }


    /**
     * Sets the idNodoProtocollante value for this RegistrazionePropertiesType.
     * 
     * @param idNodoProtocollante
     */
    public void setIdNodoProtocollante(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idNodoProtocollante) {
        this.idNodoProtocollante = idNodoProtocollante;
    }


    /**
     * Gets the idNodoResponsabile value for this RegistrazionePropertiesType.
     * 
     * @return idNodoResponsabile
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType getIdNodoResponsabile() {
        return idNodoResponsabile;
    }


    /**
     * Sets the idNodoResponsabile value for this RegistrazionePropertiesType.
     * 
     * @param idNodoResponsabile
     */
    public void setIdNodoResponsabile(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idNodoResponsabile) {
        this.idNodoResponsabile = idNodoResponsabile;
    }


    /**
     * Gets the idUtenteAnnullamento value for this RegistrazionePropertiesType.
     * 
     * @return idUtenteAnnullamento
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType getIdUtenteAnnullamento() {
        return idUtenteAnnullamento;
    }


    /**
     * Sets the idUtenteAnnullamento value for this RegistrazionePropertiesType.
     * 
     * @param idUtenteAnnullamento
     */
    public void setIdUtenteAnnullamento(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idUtenteAnnullamento) {
        this.idUtenteAnnullamento = idUtenteAnnullamento;
    }


    /**
     * Gets the idUtenteCreazione value for this RegistrazionePropertiesType.
     * 
     * @return idUtenteCreazione
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType getIdUtenteCreazione() {
        return idUtenteCreazione;
    }


    /**
     * Sets the idUtenteCreazione value for this RegistrazionePropertiesType.
     * 
     * @param idUtenteCreazione
     */
    public void setIdUtenteCreazione(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idUtenteCreazione) {
        this.idUtenteCreazione = idUtenteCreazione;
    }


    /**
     * Gets the idUtenteProtocollista value for this RegistrazionePropertiesType.
     * 
     * @return idUtenteProtocollista
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType getIdUtenteProtocollista() {
        return idUtenteProtocollista;
    }


    /**
     * Sets the idUtenteProtocollista value for this RegistrazionePropertiesType.
     * 
     * @param idUtenteProtocollista
     */
    public void setIdUtenteProtocollista(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idUtenteProtocollista) {
        this.idUtenteProtocollista = idUtenteProtocollista;
    }


    /**
     * Gets the idClassificazione value for this RegistrazionePropertiesType.
     * 
     * @return idClassificazione
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType getIdClassificazione() {
        return idClassificazione;
    }


    /**
     * Sets the idClassificazione value for this RegistrazionePropertiesType.
     * 
     * @param idClassificazione
     */
    public void setIdClassificazione(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idClassificazione) {
        this.idClassificazione = idClassificazione;
    }


    /**
     * Gets the idDocumento value for this RegistrazionePropertiesType.
     * 
     * @return idDocumento
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType[] getIdDocumento() {
        return idDocumento;
    }


    /**
     * Sets the idDocumento value for this RegistrazionePropertiesType.
     * 
     * @param idDocumento
     */
    public void setIdDocumento(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType[] idDocumento) {
        this.idDocumento = idDocumento;
    }

    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType getIdDocumento(int i) {
        return this.idDocumento[i];
    }

    public void setIdDocumento(int i, it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType _value) {
        this.idDocumento[i] = _value;
    }


    /**
     * Gets the idRegistro value for this RegistrazionePropertiesType.
     * 
     * @return idRegistro
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType getIdRegistro() {
        return idRegistro;
    }


    /**
     * Sets the idRegistro value for this RegistrazionePropertiesType.
     * 
     * @param idRegistro
     */
    public void setIdRegistro(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idRegistro) {
        this.idRegistro = idRegistro;
    }


    /**
     * Gets the idRegistrazionePrecedente value for this RegistrazionePropertiesType.
     * 
     * @return idRegistrazionePrecedente
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType getIdRegistrazionePrecedente() {
        return idRegistrazionePrecedente;
    }


    /**
     * Sets the idRegistrazionePrecedente value for this RegistrazionePropertiesType.
     * 
     * @param idRegistrazionePrecedente
     */
    public void setIdRegistrazionePrecedente(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType idRegistrazionePrecedente) {
        this.idRegistrazionePrecedente = idRegistrazionePrecedente;
    }


    /**
     * Gets the idRegistrazioneSuccessiva value for this RegistrazionePropertiesType.
     * 
     * @return idRegistrazioneSuccessiva
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType[] getIdRegistrazioneSuccessiva() {
        return idRegistrazioneSuccessiva;
    }


    /**
     * Sets the idRegistrazioneSuccessiva value for this RegistrazionePropertiesType.
     * 
     * @param idRegistrazioneSuccessiva
     */
    public void setIdRegistrazioneSuccessiva(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType[] idRegistrazioneSuccessiva) {
        this.idRegistrazioneSuccessiva = idRegistrazioneSuccessiva;
    }

    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType getIdRegistrazioneSuccessiva(int i) {
        return this.idRegistrazioneSuccessiva[i];
    }

    public void setIdRegistrazioneSuccessiva(int i, it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType _value) {
        this.idRegistrazioneSuccessiva[i] = _value;
    }


    /**
     * Gets the idCorrispondente value for this RegistrazionePropertiesType.
     * 
     * @return idCorrispondente
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType[] getIdCorrispondente() {
        return idCorrispondente;
    }


    /**
     * Sets the idCorrispondente value for this RegistrazionePropertiesType.
     * 
     * @param idCorrispondente
     */
    public void setIdCorrispondente(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType[] idCorrispondente) {
        this.idCorrispondente = idCorrispondente;
    }

    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType getIdCorrispondente(int i) {
        return this.idCorrispondente[i];
    }

    public void setIdCorrispondente(int i, it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType _value) {
        this.idCorrispondente[i] = _value;
    }


    /**
     * Gets the idAnnotazioneOB value for this RegistrazionePropertiesType.
     * 
     * @return idAnnotazioneOB
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType[] getIdAnnotazioneOB() {
        return idAnnotazioneOB;
    }


    /**
     * Sets the idAnnotazioneOB value for this RegistrazionePropertiesType.
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
     * Gets the idMessaggio value for this RegistrazionePropertiesType.
     * 
     * @return idMessaggio
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType[] getIdMessaggio() {
        return idMessaggio;
    }


    /**
     * Sets the idMessaggio value for this RegistrazionePropertiesType.
     * 
     * @param idMessaggio
     */
    public void setIdMessaggio(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType[] idMessaggio) {
        this.idMessaggio = idMessaggio;
    }

    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType getIdMessaggio(int i) {
        return this.idMessaggio[i];
    }

    public void setIdMessaggio(int i, it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType _value) {
        this.idMessaggio[i] = _value;
    }


    /**
     * Gets the idSmistamento value for this RegistrazionePropertiesType.
     * 
     * @return idSmistamento
     */
    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType[] getIdSmistamento() {
        return idSmistamento;
    }


    /**
     * Sets the idSmistamento value for this RegistrazionePropertiesType.
     * 
     * @param idSmistamento
     */
    public void setIdSmistamento(it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType[] idSmistamento) {
        this.idSmistamento = idSmistamento;
    }

    public it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType getIdSmistamento(int i) {
        return this.idSmistamento[i];
    }

    public void setIdSmistamento(int i, it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType _value) {
        this.idSmistamento[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof RegistrazionePropertiesType)) return false;
        RegistrazionePropertiesType other = (RegistrazionePropertiesType) obj;

        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.uuid==null && other.getUuid()==null) || 
             (this.uuid!=null &&
              this.uuid.equals(other.getUuid()))) &&
            ((this.codice==null && other.getCodice()==null) || 
             (this.codice!=null &&
              this.codice.equals(other.getCodice()))) &&
            ((this.stato==null && other.getStato()==null) || 
             (this.stato!=null &&
              this.stato.equals(other.getStato()))) &&
            ((this.condizione==null && other.getCondizione()==null) || 
             (this.condizione!=null &&
              this.condizione.equals(other.getCondizione()))) &&
            this.storico == other.isStorico() &&
            ((this.tipoRegistrazione==null && other.getTipoRegistrazione()==null) || 
             (this.tipoRegistrazione!=null &&
              this.tipoRegistrazione.equals(other.getTipoRegistrazione()))) &&
            this.riservato == other.isRiservato() &&
            ((this.oggetto==null && other.getOggetto()==null) || 
             (this.oggetto!=null &&
              this.oggetto.equals(other.getOggetto()))) &&
            ((this.classificazioneProposta==null && other.getClassificazioneProposta()==null) || 
             (this.classificazioneProposta!=null &&
              this.classificazioneProposta.equals(other.getClassificazioneProposta()))) &&
            ((this.mezzoTrasmissivo==null && other.getMezzoTrasmissivo()==null) || 
             (this.mezzoTrasmissivo!=null &&
              this.mezzoTrasmissivo.equals(other.getMezzoTrasmissivo()))) &&
            ((this.dataDocumento==null && other.getDataDocumento()==null) || 
             (this.dataDocumento!=null &&
              this.dataDocumento.equals(other.getDataDocumento()))) &&
            ((this.dataRicezione==null && other.getDataRicezione()==null) || 
             (this.dataRicezione!=null &&
              this.dataRicezione.equals(other.getDataRicezione()))) &&
            ((this.dataTimbroPostale==null && other.getDataTimbroPostale()==null) || 
             (this.dataTimbroPostale!=null &&
              this.dataTimbroPostale.equals(other.getDataTimbroPostale()))) &&
            ((this.codiceProtocolloMittente==null && other.getCodiceProtocolloMittente()==null) || 
             (this.codiceProtocolloMittente!=null &&
              this.codiceProtocolloMittente.equals(other.getCodiceProtocolloMittente()))) &&
            this.annoProtocolloMittente == other.getAnnoProtocolloMittente() &&
            ((this.dataProtocolloMittente==null && other.getDataProtocolloMittente()==null) || 
             (this.dataProtocolloMittente!=null &&
              this.dataProtocolloMittente.equals(other.getDataProtocolloMittente()))) &&
            this.numeroAllegati == other.getNumeroAllegati() &&
            ((this.dataProtocollo==null && other.getDataProtocollo()==null) || 
             (this.dataProtocollo!=null &&
              this.dataProtocollo.equals(other.getDataProtocollo()))) &&
            ((this.dataInoltro==null && other.getDataInoltro()==null) || 
             (this.dataInoltro!=null &&
              this.dataInoltro.equals(other.getDataInoltro()))) &&
            ((this.codiceEmergenza==null && other.getCodiceEmergenza()==null) || 
             (this.codiceEmergenza!=null &&
              this.codiceEmergenza.equals(other.getCodiceEmergenza()))) &&
            ((this.dataEmergenza==null && other.getDataEmergenza()==null) || 
             (this.dataEmergenza!=null &&
              this.dataEmergenza.equals(other.getDataEmergenza()))) &&
            ((this.dataRecupero==null && other.getDataRecupero()==null) || 
             (this.dataRecupero!=null &&
              this.dataRecupero.equals(other.getDataRecupero()))) &&
            ((this.dataAnnullamento==null && other.getDataAnnullamento()==null) || 
             (this.dataAnnullamento!=null &&
              this.dataAnnullamento.equals(other.getDataAnnullamento()))) &&
            ((this.motivoCreazione==null && other.getMotivoCreazione()==null) || 
             (this.motivoCreazione!=null &&
              this.motivoCreazione.equals(other.getMotivoCreazione()))) &&
            this.idCaricamento == other.getIdCaricamento() &&
            ((this.idAOOProtocollante==null && other.getIdAOOProtocollante()==null) || 
             (this.idAOOProtocollante!=null &&
              this.idAOOProtocollante.equals(other.getIdAOOProtocollante()))) &&
            ((this.idAOOResponsabile==null && other.getIdAOOResponsabile()==null) || 
             (this.idAOOResponsabile!=null &&
              this.idAOOResponsabile.equals(other.getIdAOOResponsabile()))) &&
            ((this.idStrutturaProtocollante==null && other.getIdStrutturaProtocollante()==null) || 
             (this.idStrutturaProtocollante!=null &&
              this.idStrutturaProtocollante.equals(other.getIdStrutturaProtocollante()))) &&
            ((this.idStrutturaResponsabile==null && other.getIdStrutturaResponsabile()==null) || 
             (this.idStrutturaResponsabile!=null &&
              this.idStrutturaResponsabile.equals(other.getIdStrutturaResponsabile()))) &&
            ((this.idNodoProtocollante==null && other.getIdNodoProtocollante()==null) || 
             (this.idNodoProtocollante!=null &&
              this.idNodoProtocollante.equals(other.getIdNodoProtocollante()))) &&
            ((this.idNodoResponsabile==null && other.getIdNodoResponsabile()==null) || 
             (this.idNodoResponsabile!=null &&
              this.idNodoResponsabile.equals(other.getIdNodoResponsabile()))) &&
            ((this.idUtenteAnnullamento==null && other.getIdUtenteAnnullamento()==null) || 
             (this.idUtenteAnnullamento!=null &&
              this.idUtenteAnnullamento.equals(other.getIdUtenteAnnullamento()))) &&
            ((this.idUtenteCreazione==null && other.getIdUtenteCreazione()==null) || 
             (this.idUtenteCreazione!=null &&
              this.idUtenteCreazione.equals(other.getIdUtenteCreazione()))) &&
            ((this.idUtenteProtocollista==null && other.getIdUtenteProtocollista()==null) || 
             (this.idUtenteProtocollista!=null &&
              this.idUtenteProtocollista.equals(other.getIdUtenteProtocollista()))) &&
            ((this.idClassificazione==null && other.getIdClassificazione()==null) || 
             (this.idClassificazione!=null &&
              this.idClassificazione.equals(other.getIdClassificazione()))) &&
            ((this.idDocumento==null && other.getIdDocumento()==null) || 
             (this.idDocumento!=null &&
              java.util.Arrays.equals(this.idDocumento, other.getIdDocumento()))) &&
            ((this.idRegistro==null && other.getIdRegistro()==null) || 
             (this.idRegistro!=null &&
              this.idRegistro.equals(other.getIdRegistro()))) &&
            ((this.idRegistrazionePrecedente==null && other.getIdRegistrazionePrecedente()==null) || 
             (this.idRegistrazionePrecedente!=null &&
              this.idRegistrazionePrecedente.equals(other.getIdRegistrazionePrecedente()))) &&
            ((this.idRegistrazioneSuccessiva==null && other.getIdRegistrazioneSuccessiva()==null) || 
             (this.idRegistrazioneSuccessiva!=null &&
              java.util.Arrays.equals(this.idRegistrazioneSuccessiva, other.getIdRegistrazioneSuccessiva()))) &&
            ((this.idCorrispondente==null && other.getIdCorrispondente()==null) || 
             (this.idCorrispondente!=null &&
              java.util.Arrays.equals(this.idCorrispondente, other.getIdCorrispondente()))) &&
            ((this.idAnnotazioneOB==null && other.getIdAnnotazioneOB()==null) || 
             (this.idAnnotazioneOB!=null &&
              java.util.Arrays.equals(this.idAnnotazioneOB, other.getIdAnnotazioneOB()))) &&
            ((this.idMessaggio==null && other.getIdMessaggio()==null) || 
             (this.idMessaggio!=null &&
              java.util.Arrays.equals(this.idMessaggio, other.getIdMessaggio()))) &&
            ((this.idSmistamento==null && other.getIdSmistamento()==null) || 
             (this.idSmistamento!=null &&
              java.util.Arrays.equals(this.idSmistamento, other.getIdSmistamento())));
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
        if (getUuid() != null) {
            _hashCode += getUuid().hashCode();
        }
        if (getCodice() != null) {
            _hashCode += getCodice().hashCode();
        }
        if (getStato() != null) {
            _hashCode += getStato().hashCode();
        }
        if (getCondizione() != null) {
            _hashCode += getCondizione().hashCode();
        }
        _hashCode += (isStorico() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getTipoRegistrazione() != null) {
            _hashCode += getTipoRegistrazione().hashCode();
        }
        _hashCode += (isRiservato() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getOggetto() != null) {
            _hashCode += getOggetto().hashCode();
        }
        if (getClassificazioneProposta() != null) {
            _hashCode += getClassificazioneProposta().hashCode();
        }
        if (getMezzoTrasmissivo() != null) {
            _hashCode += getMezzoTrasmissivo().hashCode();
        }
        if (getDataDocumento() != null) {
            _hashCode += getDataDocumento().hashCode();
        }
        if (getDataRicezione() != null) {
            _hashCode += getDataRicezione().hashCode();
        }
        if (getDataTimbroPostale() != null) {
            _hashCode += getDataTimbroPostale().hashCode();
        }
        if (getCodiceProtocolloMittente() != null) {
            _hashCode += getCodiceProtocolloMittente().hashCode();
        }
        _hashCode += getAnnoProtocolloMittente();
        if (getDataProtocolloMittente() != null) {
            _hashCode += getDataProtocolloMittente().hashCode();
        }
        _hashCode += getNumeroAllegati();
        if (getDataProtocollo() != null) {
            _hashCode += getDataProtocollo().hashCode();
        }
        if (getDataInoltro() != null) {
            _hashCode += getDataInoltro().hashCode();
        }
        if (getCodiceEmergenza() != null) {
            _hashCode += getCodiceEmergenza().hashCode();
        }
        if (getDataEmergenza() != null) {
            _hashCode += getDataEmergenza().hashCode();
        }
        if (getDataRecupero() != null) {
            _hashCode += getDataRecupero().hashCode();
        }
        if (getDataAnnullamento() != null) {
            _hashCode += getDataAnnullamento().hashCode();
        }
        if (getMotivoCreazione() != null) {
            _hashCode += getMotivoCreazione().hashCode();
        }
        _hashCode += new Long(getIdCaricamento()).hashCode();
        if (getIdAOOProtocollante() != null) {
            _hashCode += getIdAOOProtocollante().hashCode();
        }
        if (getIdAOOResponsabile() != null) {
            _hashCode += getIdAOOResponsabile().hashCode();
        }
        if (getIdStrutturaProtocollante() != null) {
            _hashCode += getIdStrutturaProtocollante().hashCode();
        }
        if (getIdStrutturaResponsabile() != null) {
            _hashCode += getIdStrutturaResponsabile().hashCode();
        }
        if (getIdNodoProtocollante() != null) {
            _hashCode += getIdNodoProtocollante().hashCode();
        }
        if (getIdNodoResponsabile() != null) {
            _hashCode += getIdNodoResponsabile().hashCode();
        }
        if (getIdUtenteAnnullamento() != null) {
            _hashCode += getIdUtenteAnnullamento().hashCode();
        }
        if (getIdUtenteCreazione() != null) {
            _hashCode += getIdUtenteCreazione().hashCode();
        }
        if (getIdUtenteProtocollista() != null) {
            _hashCode += getIdUtenteProtocollista().hashCode();
        }
        if (getIdClassificazione() != null) {
            _hashCode += getIdClassificazione().hashCode();
        }
        if (getIdDocumento() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getIdDocumento());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getIdDocumento(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getIdRegistro() != null) {
            _hashCode += getIdRegistro().hashCode();
        }
        if (getIdRegistrazionePrecedente() != null) {
            _hashCode += getIdRegistrazionePrecedente().hashCode();
        }
        if (getIdRegistrazioneSuccessiva() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getIdRegistrazioneSuccessiva());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getIdRegistrazioneSuccessiva(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getIdCorrispondente() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getIdCorrispondente());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getIdCorrispondente(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
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
        if (getIdMessaggio() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getIdMessaggio());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getIdMessaggio(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getIdSmistamento() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getIdSmistamento());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getIdSmistamento(), i);
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
        new org.apache.axis.description.TypeDesc(RegistrazionePropertiesType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("officialbookservice.acaris.acta.doqui.it", "RegistrazionePropertiesType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("uuid");
        elemField.setXmlName(new javax.xml.namespace.QName("", "uuid"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codice");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codice"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("stato");
        elemField.setXmlName(new javax.xml.namespace.QName("", "stato"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "DecodificaType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("condizione");
        elemField.setXmlName(new javax.xml.namespace.QName("", "condizione"));
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
        elemField.setFieldName("tipoRegistrazione");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tipoRegistrazione"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "DecodificaType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("riservato");
        elemField.setXmlName(new javax.xml.namespace.QName("", "riservato"));
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
        elemField.setFieldName("classificazioneProposta");
        elemField.setXmlName(new javax.xml.namespace.QName("", "classificazioneProposta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mezzoTrasmissivo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "mezzoTrasmissivo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataDocumento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataDocumento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataRicezione");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataRicezione"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataTimbroPostale");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataTimbroPostale"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codiceProtocolloMittente");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codiceProtocolloMittente"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("annoProtocolloMittente");
        elemField.setXmlName(new javax.xml.namespace.QName("", "annoProtocolloMittente"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataProtocolloMittente");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataProtocolloMittente"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numeroAllegati");
        elemField.setXmlName(new javax.xml.namespace.QName("", "numeroAllegati"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataProtocollo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataProtocollo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataInoltro");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataInoltro"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codiceEmergenza");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codiceEmergenza"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataEmergenza");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataEmergenza"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataRecupero");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataRecupero"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataAnnullamento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataAnnullamento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("motivoCreazione");
        elemField.setXmlName(new javax.xml.namespace.QName("", "motivoCreazione"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "DecodificaType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idCaricamento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idCaricamento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
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
        elemField.setFieldName("idStrutturaProtocollante");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idStrutturaProtocollante"));
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
        elemField.setFieldName("idNodoProtocollante");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idNodoProtocollante"));
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
        elemField.setFieldName("idUtenteAnnullamento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idUtenteAnnullamento"));
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
        elemField.setFieldName("idUtenteProtocollista");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idUtenteProtocollista"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idClassificazione");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idClassificazione"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idDocumento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idDocumento"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idRegistro");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idRegistro"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idRegistrazionePrecedente");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idRegistrazionePrecedente"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idRegistrazioneSuccessiva");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idRegistrazioneSuccessiva"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idCorrispondente");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idCorrispondente"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"));
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
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
        elemField.setFieldName("idMessaggio");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idMessaggio"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idSmistamento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idSmistamento"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"));
        elemField.setMinOccurs(0);
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

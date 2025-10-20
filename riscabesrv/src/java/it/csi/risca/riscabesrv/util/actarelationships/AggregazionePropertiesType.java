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
 * AggregazionePropertiesType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actarelationships;

public abstract class AggregazionePropertiesType  extends it.csi.risca.riscabesrv.util.actarelationships.FolderPropertiesType  implements java.io.Serializable {
    private java.lang.String codice;

    private java.lang.String descrizione;

    private java.lang.String indiceDiClassificazioneEsteso;

    private java.util.Date dataCreazione;

    private java.util.Date dataInizio;

    private java.util.Date dataFine;

    private int conservazioneCorrente;

    private int conservazioneGenerale;

    private java.util.Date dataPassaggioInDeposito;

    private java.util.Date dataBloccoPassaggioInDeposito;

    private java.util.Date dataSbloccoPassaggioInDeposito;

    private java.util.Date dataScarto;

    private boolean archivioCorrente;

    private boolean datiPersonali;

    private boolean datiSensibili;

    private boolean datiRiservati;

    private java.lang.String collocazioneCartaceo;

    private java.lang.String paroleChiave;

    private java.util.Date dataUltimaModifica;

    private java.util.Date dataCancellazione;

    private java.util.Date dataEsportazione;

    private java.lang.String estremiProvvedimentoDiScarto;

    private it.csi.risca.riscabesrv.util.actarelationships.CodiceFiscaleType utenteCreazione;

    private it.csi.risca.riscabesrv.util.actarelationships.ObjectIdType idDeposito;

    private it.csi.risca.riscabesrv.util.actarelationships.IdMovimentazioneType[] idMovimentazioneList;

    private it.csi.risca.riscabesrv.util.actarelationships.IdAnnotazioniType[] idAnnotazioniList;

    private int idTipoClasse;

    private it.csi.risca.riscabesrv.util.actarelationships.IdAOOType idAOORespMat;

    private it.csi.risca.riscabesrv.util.actarelationships.IdAOOType idAOORespCons;

    private it.csi.risca.riscabesrv.util.actarelationships.IdStrutturaType idStrutturaRespMat;

    private it.csi.risca.riscabesrv.util.actarelationships.IdStrutturaType idStrutturaRespCons;

    private it.csi.risca.riscabesrv.util.actarelationships.IdNodoType idNodoRespMat;

    private it.csi.risca.riscabesrv.util.actarelationships.IdNodoType idNodoRespCons;

    public AggregazionePropertiesType() {
    }

    public AggregazionePropertiesType(
           it.csi.risca.riscabesrv.util.actarelationships.ObjectIdType objectId,
           it.csi.risca.riscabesrv.util.actarelationships.ChangeTokenType changeToken,
           java.lang.String objectTypeId,
           it.csi.risca.riscabesrv.util.actarelationships.ObjectIdType parentId,
           java.lang.String[] allowedChildObjectTypeIds,
           java.lang.String codice,
           java.lang.String descrizione,
           java.lang.String indiceDiClassificazioneEsteso,
           java.util.Date dataCreazione,
           java.util.Date dataInizio,
           java.util.Date dataFine,
           int conservazioneCorrente,
           int conservazioneGenerale,
           java.util.Date dataPassaggioInDeposito,
           java.util.Date dataBloccoPassaggioInDeposito,
           java.util.Date dataSbloccoPassaggioInDeposito,
           java.util.Date dataScarto,
           boolean archivioCorrente,
           boolean datiPersonali,
           boolean datiSensibili,
           boolean datiRiservati,
           java.lang.String collocazioneCartaceo,
           java.lang.String paroleChiave,
           java.util.Date dataUltimaModifica,
           java.util.Date dataCancellazione,
           java.util.Date dataEsportazione,
           java.lang.String estremiProvvedimentoDiScarto,
           it.csi.risca.riscabesrv.util.actarelationships.CodiceFiscaleType utenteCreazione,
           it.csi.risca.riscabesrv.util.actarelationships.ObjectIdType idDeposito,
           it.csi.risca.riscabesrv.util.actarelationships.IdMovimentazioneType[] idMovimentazioneList,
           it.csi.risca.riscabesrv.util.actarelationships.IdAnnotazioniType[] idAnnotazioniList,
           int idTipoClasse,
           it.csi.risca.riscabesrv.util.actarelationships.IdAOOType idAOORespMat,
           it.csi.risca.riscabesrv.util.actarelationships.IdAOOType idAOORespCons,
           it.csi.risca.riscabesrv.util.actarelationships.IdStrutturaType idStrutturaRespMat,
           it.csi.risca.riscabesrv.util.actarelationships.IdStrutturaType idStrutturaRespCons,
           it.csi.risca.riscabesrv.util.actarelationships.IdNodoType idNodoRespMat,
           it.csi.risca.riscabesrv.util.actarelationships.IdNodoType idNodoRespCons) {
        super(
            objectId,
            changeToken,
            objectTypeId,
            parentId,
            allowedChildObjectTypeIds);
        this.codice = codice;
        this.descrizione = descrizione;
        this.indiceDiClassificazioneEsteso = indiceDiClassificazioneEsteso;
        this.dataCreazione = dataCreazione;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.conservazioneCorrente = conservazioneCorrente;
        this.conservazioneGenerale = conservazioneGenerale;
        this.dataPassaggioInDeposito = dataPassaggioInDeposito;
        this.dataBloccoPassaggioInDeposito = dataBloccoPassaggioInDeposito;
        this.dataSbloccoPassaggioInDeposito = dataSbloccoPassaggioInDeposito;
        this.dataScarto = dataScarto;
        this.archivioCorrente = archivioCorrente;
        this.datiPersonali = datiPersonali;
        this.datiSensibili = datiSensibili;
        this.datiRiservati = datiRiservati;
        this.collocazioneCartaceo = collocazioneCartaceo;
        this.paroleChiave = paroleChiave;
        this.dataUltimaModifica = dataUltimaModifica;
        this.dataCancellazione = dataCancellazione;
        this.dataEsportazione = dataEsportazione;
        this.estremiProvvedimentoDiScarto = estremiProvvedimentoDiScarto;
        this.utenteCreazione = utenteCreazione;
        this.idDeposito = idDeposito;
        this.idMovimentazioneList = idMovimentazioneList;
        this.idAnnotazioniList = idAnnotazioniList;
        this.idTipoClasse = idTipoClasse;
        this.idAOORespMat = idAOORespMat;
        this.idAOORespCons = idAOORespCons;
        this.idStrutturaRespMat = idStrutturaRespMat;
        this.idStrutturaRespCons = idStrutturaRespCons;
        this.idNodoRespMat = idNodoRespMat;
        this.idNodoRespCons = idNodoRespCons;
    }


    /**
     * Gets the codice value for this AggregazionePropertiesType.
     * 
     * @return codice
     */
    public java.lang.String getCodice() {
        return codice;
    }


    /**
     * Sets the codice value for this AggregazionePropertiesType.
     * 
     * @param codice
     */
    public void setCodice(java.lang.String codice) {
        this.codice = codice;
    }


    /**
     * Gets the descrizione value for this AggregazionePropertiesType.
     * 
     * @return descrizione
     */
    public java.lang.String getDescrizione() {
        return descrizione;
    }


    /**
     * Sets the descrizione value for this AggregazionePropertiesType.
     * 
     * @param descrizione
     */
    public void setDescrizione(java.lang.String descrizione) {
        this.descrizione = descrizione;
    }


    /**
     * Gets the indiceDiClassificazioneEsteso value for this AggregazionePropertiesType.
     * 
     * @return indiceDiClassificazioneEsteso
     */
    public java.lang.String getIndiceDiClassificazioneEsteso() {
        return indiceDiClassificazioneEsteso;
    }


    /**
     * Sets the indiceDiClassificazioneEsteso value for this AggregazionePropertiesType.
     * 
     * @param indiceDiClassificazioneEsteso
     */
    public void setIndiceDiClassificazioneEsteso(java.lang.String indiceDiClassificazioneEsteso) {
        this.indiceDiClassificazioneEsteso = indiceDiClassificazioneEsteso;
    }


    /**
     * Gets the dataCreazione value for this AggregazionePropertiesType.
     * 
     * @return dataCreazione
     */
    public java.util.Date getDataCreazione() {
        return dataCreazione;
    }


    /**
     * Sets the dataCreazione value for this AggregazionePropertiesType.
     * 
     * @param dataCreazione
     */
    public void setDataCreazione(java.util.Date dataCreazione) {
        this.dataCreazione = dataCreazione;
    }


    /**
     * Gets the dataInizio value for this AggregazionePropertiesType.
     * 
     * @return dataInizio
     */
    public java.util.Date getDataInizio() {
        return dataInizio;
    }


    /**
     * Sets the dataInizio value for this AggregazionePropertiesType.
     * 
     * @param dataInizio
     */
    public void setDataInizio(java.util.Date dataInizio) {
        this.dataInizio = dataInizio;
    }


    /**
     * Gets the dataFine value for this AggregazionePropertiesType.
     * 
     * @return dataFine
     */
    public java.util.Date getDataFine() {
        return dataFine;
    }


    /**
     * Sets the dataFine value for this AggregazionePropertiesType.
     * 
     * @param dataFine
     */
    public void setDataFine(java.util.Date dataFine) {
        this.dataFine = dataFine;
    }


    /**
     * Gets the conservazioneCorrente value for this AggregazionePropertiesType.
     * 
     * @return conservazioneCorrente
     */
    public int getConservazioneCorrente() {
        return conservazioneCorrente;
    }


    /**
     * Sets the conservazioneCorrente value for this AggregazionePropertiesType.
     * 
     * @param conservazioneCorrente
     */
    public void setConservazioneCorrente(int conservazioneCorrente) {
        this.conservazioneCorrente = conservazioneCorrente;
    }


    /**
     * Gets the conservazioneGenerale value for this AggregazionePropertiesType.
     * 
     * @return conservazioneGenerale
     */
    public int getConservazioneGenerale() {
        return conservazioneGenerale;
    }


    /**
     * Sets the conservazioneGenerale value for this AggregazionePropertiesType.
     * 
     * @param conservazioneGenerale
     */
    public void setConservazioneGenerale(int conservazioneGenerale) {
        this.conservazioneGenerale = conservazioneGenerale;
    }


    /**
     * Gets the dataPassaggioInDeposito value for this AggregazionePropertiesType.
     * 
     * @return dataPassaggioInDeposito
     */
    public java.util.Date getDataPassaggioInDeposito() {
        return dataPassaggioInDeposito;
    }


    /**
     * Sets the dataPassaggioInDeposito value for this AggregazionePropertiesType.
     * 
     * @param dataPassaggioInDeposito
     */
    public void setDataPassaggioInDeposito(java.util.Date dataPassaggioInDeposito) {
        this.dataPassaggioInDeposito = dataPassaggioInDeposito;
    }


    /**
     * Gets the dataBloccoPassaggioInDeposito value for this AggregazionePropertiesType.
     * 
     * @return dataBloccoPassaggioInDeposito
     */
    public java.util.Date getDataBloccoPassaggioInDeposito() {
        return dataBloccoPassaggioInDeposito;
    }


    /**
     * Sets the dataBloccoPassaggioInDeposito value for this AggregazionePropertiesType.
     * 
     * @param dataBloccoPassaggioInDeposito
     */
    public void setDataBloccoPassaggioInDeposito(java.util.Date dataBloccoPassaggioInDeposito) {
        this.dataBloccoPassaggioInDeposito = dataBloccoPassaggioInDeposito;
    }


    /**
     * Gets the dataSbloccoPassaggioInDeposito value for this AggregazionePropertiesType.
     * 
     * @return dataSbloccoPassaggioInDeposito
     */
    public java.util.Date getDataSbloccoPassaggioInDeposito() {
        return dataSbloccoPassaggioInDeposito;
    }


    /**
     * Sets the dataSbloccoPassaggioInDeposito value for this AggregazionePropertiesType.
     * 
     * @param dataSbloccoPassaggioInDeposito
     */
    public void setDataSbloccoPassaggioInDeposito(java.util.Date dataSbloccoPassaggioInDeposito) {
        this.dataSbloccoPassaggioInDeposito = dataSbloccoPassaggioInDeposito;
    }


    /**
     * Gets the dataScarto value for this AggregazionePropertiesType.
     * 
     * @return dataScarto
     */
    public java.util.Date getDataScarto() {
        return dataScarto;
    }


    /**
     * Sets the dataScarto value for this AggregazionePropertiesType.
     * 
     * @param dataScarto
     */
    public void setDataScarto(java.util.Date dataScarto) {
        this.dataScarto = dataScarto;
    }


    /**
     * Gets the archivioCorrente value for this AggregazionePropertiesType.
     * 
     * @return archivioCorrente
     */
    public boolean isArchivioCorrente() {
        return archivioCorrente;
    }


    /**
     * Sets the archivioCorrente value for this AggregazionePropertiesType.
     * 
     * @param archivioCorrente
     */
    public void setArchivioCorrente(boolean archivioCorrente) {
        this.archivioCorrente = archivioCorrente;
    }


    /**
     * Gets the datiPersonali value for this AggregazionePropertiesType.
     * 
     * @return datiPersonali
     */
    public boolean isDatiPersonali() {
        return datiPersonali;
    }


    /**
     * Sets the datiPersonali value for this AggregazionePropertiesType.
     * 
     * @param datiPersonali
     */
    public void setDatiPersonali(boolean datiPersonali) {
        this.datiPersonali = datiPersonali;
    }


    /**
     * Gets the datiSensibili value for this AggregazionePropertiesType.
     * 
     * @return datiSensibili
     */
    public boolean isDatiSensibili() {
        return datiSensibili;
    }


    /**
     * Sets the datiSensibili value for this AggregazionePropertiesType.
     * 
     * @param datiSensibili
     */
    public void setDatiSensibili(boolean datiSensibili) {
        this.datiSensibili = datiSensibili;
    }


    /**
     * Gets the datiRiservati value for this AggregazionePropertiesType.
     * 
     * @return datiRiservati
     */
    public boolean isDatiRiservati() {
        return datiRiservati;
    }


    /**
     * Sets the datiRiservati value for this AggregazionePropertiesType.
     * 
     * @param datiRiservati
     */
    public void setDatiRiservati(boolean datiRiservati) {
        this.datiRiservati = datiRiservati;
    }


    /**
     * Gets the collocazioneCartaceo value for this AggregazionePropertiesType.
     * 
     * @return collocazioneCartaceo
     */
    public java.lang.String getCollocazioneCartaceo() {
        return collocazioneCartaceo;
    }


    /**
     * Sets the collocazioneCartaceo value for this AggregazionePropertiesType.
     * 
     * @param collocazioneCartaceo
     */
    public void setCollocazioneCartaceo(java.lang.String collocazioneCartaceo) {
        this.collocazioneCartaceo = collocazioneCartaceo;
    }


    /**
     * Gets the paroleChiave value for this AggregazionePropertiesType.
     * 
     * @return paroleChiave
     */
    public java.lang.String getParoleChiave() {
        return paroleChiave;
    }


    /**
     * Sets the paroleChiave value for this AggregazionePropertiesType.
     * 
     * @param paroleChiave
     */
    public void setParoleChiave(java.lang.String paroleChiave) {
        this.paroleChiave = paroleChiave;
    }


    /**
     * Gets the dataUltimaModifica value for this AggregazionePropertiesType.
     * 
     * @return dataUltimaModifica
     */
    public java.util.Date getDataUltimaModifica() {
        return dataUltimaModifica;
    }


    /**
     * Sets the dataUltimaModifica value for this AggregazionePropertiesType.
     * 
     * @param dataUltimaModifica
     */
    public void setDataUltimaModifica(java.util.Date dataUltimaModifica) {
        this.dataUltimaModifica = dataUltimaModifica;
    }


    /**
     * Gets the dataCancellazione value for this AggregazionePropertiesType.
     * 
     * @return dataCancellazione
     */
    public java.util.Date getDataCancellazione() {
        return dataCancellazione;
    }


    /**
     * Sets the dataCancellazione value for this AggregazionePropertiesType.
     * 
     * @param dataCancellazione
     */
    public void setDataCancellazione(java.util.Date dataCancellazione) {
        this.dataCancellazione = dataCancellazione;
    }


    /**
     * Gets the dataEsportazione value for this AggregazionePropertiesType.
     * 
     * @return dataEsportazione
     */
    public java.util.Date getDataEsportazione() {
        return dataEsportazione;
    }


    /**
     * Sets the dataEsportazione value for this AggregazionePropertiesType.
     * 
     * @param dataEsportazione
     */
    public void setDataEsportazione(java.util.Date dataEsportazione) {
        this.dataEsportazione = dataEsportazione;
    }


    /**
     * Gets the estremiProvvedimentoDiScarto value for this AggregazionePropertiesType.
     * 
     * @return estremiProvvedimentoDiScarto
     */
    public java.lang.String getEstremiProvvedimentoDiScarto() {
        return estremiProvvedimentoDiScarto;
    }


    /**
     * Sets the estremiProvvedimentoDiScarto value for this AggregazionePropertiesType.
     * 
     * @param estremiProvvedimentoDiScarto
     */
    public void setEstremiProvvedimentoDiScarto(java.lang.String estremiProvvedimentoDiScarto) {
        this.estremiProvvedimentoDiScarto = estremiProvvedimentoDiScarto;
    }


    /**
     * Gets the utenteCreazione value for this AggregazionePropertiesType.
     * 
     * @return utenteCreazione
     */
    public it.csi.risca.riscabesrv.util.actarelationships.CodiceFiscaleType getUtenteCreazione() {
        return utenteCreazione;
    }


    /**
     * Sets the utenteCreazione value for this AggregazionePropertiesType.
     * 
     * @param utenteCreazione
     */
    public void setUtenteCreazione(it.csi.risca.riscabesrv.util.actarelationships.CodiceFiscaleType utenteCreazione) {
        this.utenteCreazione = utenteCreazione;
    }


    /**
     * Gets the idDeposito value for this AggregazionePropertiesType.
     * 
     * @return idDeposito
     */
    public it.csi.risca.riscabesrv.util.actarelationships.ObjectIdType getIdDeposito() {
        return idDeposito;
    }


    /**
     * Sets the idDeposito value for this AggregazionePropertiesType.
     * 
     * @param idDeposito
     */
    public void setIdDeposito(it.csi.risca.riscabesrv.util.actarelationships.ObjectIdType idDeposito) {
        this.idDeposito = idDeposito;
    }


    /**
     * Gets the idMovimentazioneList value for this AggregazionePropertiesType.
     * 
     * @return idMovimentazioneList
     */
    public it.csi.risca.riscabesrv.util.actarelationships.IdMovimentazioneType[] getIdMovimentazioneList() {
        return idMovimentazioneList;
    }


    /**
     * Sets the idMovimentazioneList value for this AggregazionePropertiesType.
     * 
     * @param idMovimentazioneList
     */
    public void setIdMovimentazioneList(it.csi.risca.riscabesrv.util.actarelationships.IdMovimentazioneType[] idMovimentazioneList) {
        this.idMovimentazioneList = idMovimentazioneList;
    }

    public it.csi.risca.riscabesrv.util.actarelationships.IdMovimentazioneType getIdMovimentazioneList(int i) {
        return this.idMovimentazioneList[i];
    }

    public void setIdMovimentazioneList(int i, it.csi.risca.riscabesrv.util.actarelationships.IdMovimentazioneType _value) {
        this.idMovimentazioneList[i] = _value;
    }


    /**
     * Gets the idAnnotazioniList value for this AggregazionePropertiesType.
     * 
     * @return idAnnotazioniList
     */
    public it.csi.risca.riscabesrv.util.actarelationships.IdAnnotazioniType[] getIdAnnotazioniList() {
        return idAnnotazioniList;
    }


    /**
     * Sets the idAnnotazioniList value for this AggregazionePropertiesType.
     * 
     * @param idAnnotazioniList
     */
    public void setIdAnnotazioniList(it.csi.risca.riscabesrv.util.actarelationships.IdAnnotazioniType[] idAnnotazioniList) {
        this.idAnnotazioniList = idAnnotazioniList;
    }

    public it.csi.risca.riscabesrv.util.actarelationships.IdAnnotazioniType getIdAnnotazioniList(int i) {
        return this.idAnnotazioniList[i];
    }

    public void setIdAnnotazioniList(int i, it.csi.risca.riscabesrv.util.actarelationships.IdAnnotazioniType _value) {
        this.idAnnotazioniList[i] = _value;
    }


    /**
     * Gets the idTipoClasse value for this AggregazionePropertiesType.
     * 
     * @return idTipoClasse
     */
    public int getIdTipoClasse() {
        return idTipoClasse;
    }


    /**
     * Sets the idTipoClasse value for this AggregazionePropertiesType.
     * 
     * @param idTipoClasse
     */
    public void setIdTipoClasse(int idTipoClasse) {
        this.idTipoClasse = idTipoClasse;
    }


    /**
     * Gets the idAOORespMat value for this AggregazionePropertiesType.
     * 
     * @return idAOORespMat
     */
    public it.csi.risca.riscabesrv.util.actarelationships.IdAOOType getIdAOORespMat() {
        return idAOORespMat;
    }


    /**
     * Sets the idAOORespMat value for this AggregazionePropertiesType.
     * 
     * @param idAOORespMat
     */
    public void setIdAOORespMat(it.csi.risca.riscabesrv.util.actarelationships.IdAOOType idAOORespMat) {
        this.idAOORespMat = idAOORespMat;
    }


    /**
     * Gets the idAOORespCons value for this AggregazionePropertiesType.
     * 
     * @return idAOORespCons
     */
    public it.csi.risca.riscabesrv.util.actarelationships.IdAOOType getIdAOORespCons() {
        return idAOORespCons;
    }


    /**
     * Sets the idAOORespCons value for this AggregazionePropertiesType.
     * 
     * @param idAOORespCons
     */
    public void setIdAOORespCons(it.csi.risca.riscabesrv.util.actarelationships.IdAOOType idAOORespCons) {
        this.idAOORespCons = idAOORespCons;
    }


    /**
     * Gets the idStrutturaRespMat value for this AggregazionePropertiesType.
     * 
     * @return idStrutturaRespMat
     */
    public it.csi.risca.riscabesrv.util.actarelationships.IdStrutturaType getIdStrutturaRespMat() {
        return idStrutturaRespMat;
    }


    /**
     * Sets the idStrutturaRespMat value for this AggregazionePropertiesType.
     * 
     * @param idStrutturaRespMat
     */
    public void setIdStrutturaRespMat(it.csi.risca.riscabesrv.util.actarelationships.IdStrutturaType idStrutturaRespMat) {
        this.idStrutturaRespMat = idStrutturaRespMat;
    }


    /**
     * Gets the idStrutturaRespCons value for this AggregazionePropertiesType.
     * 
     * @return idStrutturaRespCons
     */
    public it.csi.risca.riscabesrv.util.actarelationships.IdStrutturaType getIdStrutturaRespCons() {
        return idStrutturaRespCons;
    }


    /**
     * Sets the idStrutturaRespCons value for this AggregazionePropertiesType.
     * 
     * @param idStrutturaRespCons
     */
    public void setIdStrutturaRespCons(it.csi.risca.riscabesrv.util.actarelationships.IdStrutturaType idStrutturaRespCons) {
        this.idStrutturaRespCons = idStrutturaRespCons;
    }


    /**
     * Gets the idNodoRespMat value for this AggregazionePropertiesType.
     * 
     * @return idNodoRespMat
     */
    public it.csi.risca.riscabesrv.util.actarelationships.IdNodoType getIdNodoRespMat() {
        return idNodoRespMat;
    }


    /**
     * Sets the idNodoRespMat value for this AggregazionePropertiesType.
     * 
     * @param idNodoRespMat
     */
    public void setIdNodoRespMat(it.csi.risca.riscabesrv.util.actarelationships.IdNodoType idNodoRespMat) {
        this.idNodoRespMat = idNodoRespMat;
    }


    /**
     * Gets the idNodoRespCons value for this AggregazionePropertiesType.
     * 
     * @return idNodoRespCons
     */
    public it.csi.risca.riscabesrv.util.actarelationships.IdNodoType getIdNodoRespCons() {
        return idNodoRespCons;
    }


    /**
     * Sets the idNodoRespCons value for this AggregazionePropertiesType.
     * 
     * @param idNodoRespCons
     */
    public void setIdNodoRespCons(it.csi.risca.riscabesrv.util.actarelationships.IdNodoType idNodoRespCons) {
        this.idNodoRespCons = idNodoRespCons;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof AggregazionePropertiesType)) return false;
        AggregazionePropertiesType other = (AggregazionePropertiesType) obj;

        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.codice==null && other.getCodice()==null) || 
             (this.codice!=null &&
              this.codice.equals(other.getCodice()))) &&
            ((this.descrizione==null && other.getDescrizione()==null) || 
             (this.descrizione!=null &&
              this.descrizione.equals(other.getDescrizione()))) &&
            ((this.indiceDiClassificazioneEsteso==null && other.getIndiceDiClassificazioneEsteso()==null) || 
             (this.indiceDiClassificazioneEsteso!=null &&
              this.indiceDiClassificazioneEsteso.equals(other.getIndiceDiClassificazioneEsteso()))) &&
            ((this.dataCreazione==null && other.getDataCreazione()==null) || 
             (this.dataCreazione!=null &&
              this.dataCreazione.equals(other.getDataCreazione()))) &&
            ((this.dataInizio==null && other.getDataInizio()==null) || 
             (this.dataInizio!=null &&
              this.dataInizio.equals(other.getDataInizio()))) &&
            ((this.dataFine==null && other.getDataFine()==null) || 
             (this.dataFine!=null &&
              this.dataFine.equals(other.getDataFine()))) &&
            this.conservazioneCorrente == other.getConservazioneCorrente() &&
            this.conservazioneGenerale == other.getConservazioneGenerale() &&
            ((this.dataPassaggioInDeposito==null && other.getDataPassaggioInDeposito()==null) || 
             (this.dataPassaggioInDeposito!=null &&
              this.dataPassaggioInDeposito.equals(other.getDataPassaggioInDeposito()))) &&
            ((this.dataBloccoPassaggioInDeposito==null && other.getDataBloccoPassaggioInDeposito()==null) || 
             (this.dataBloccoPassaggioInDeposito!=null &&
              this.dataBloccoPassaggioInDeposito.equals(other.getDataBloccoPassaggioInDeposito()))) &&
            ((this.dataSbloccoPassaggioInDeposito==null && other.getDataSbloccoPassaggioInDeposito()==null) || 
             (this.dataSbloccoPassaggioInDeposito!=null &&
              this.dataSbloccoPassaggioInDeposito.equals(other.getDataSbloccoPassaggioInDeposito()))) &&
            ((this.dataScarto==null && other.getDataScarto()==null) || 
             (this.dataScarto!=null &&
              this.dataScarto.equals(other.getDataScarto()))) &&
            this.archivioCorrente == other.isArchivioCorrente() &&
            this.datiPersonali == other.isDatiPersonali() &&
            this.datiSensibili == other.isDatiSensibili() &&
            this.datiRiservati == other.isDatiRiservati() &&
            ((this.collocazioneCartaceo==null && other.getCollocazioneCartaceo()==null) || 
             (this.collocazioneCartaceo!=null &&
              this.collocazioneCartaceo.equals(other.getCollocazioneCartaceo()))) &&
            ((this.paroleChiave==null && other.getParoleChiave()==null) || 
             (this.paroleChiave!=null &&
              this.paroleChiave.equals(other.getParoleChiave()))) &&
            ((this.dataUltimaModifica==null && other.getDataUltimaModifica()==null) || 
             (this.dataUltimaModifica!=null &&
              this.dataUltimaModifica.equals(other.getDataUltimaModifica()))) &&
            ((this.dataCancellazione==null && other.getDataCancellazione()==null) || 
             (this.dataCancellazione!=null &&
              this.dataCancellazione.equals(other.getDataCancellazione()))) &&
            ((this.dataEsportazione==null && other.getDataEsportazione()==null) || 
             (this.dataEsportazione!=null &&
              this.dataEsportazione.equals(other.getDataEsportazione()))) &&
            ((this.estremiProvvedimentoDiScarto==null && other.getEstremiProvvedimentoDiScarto()==null) || 
             (this.estremiProvvedimentoDiScarto!=null &&
              this.estremiProvvedimentoDiScarto.equals(other.getEstremiProvvedimentoDiScarto()))) &&
            ((this.utenteCreazione==null && other.getUtenteCreazione()==null) || 
             (this.utenteCreazione!=null &&
              this.utenteCreazione.equals(other.getUtenteCreazione()))) &&
            ((this.idDeposito==null && other.getIdDeposito()==null) || 
             (this.idDeposito!=null &&
              this.idDeposito.equals(other.getIdDeposito()))) &&
            ((this.idMovimentazioneList==null && other.getIdMovimentazioneList()==null) || 
             (this.idMovimentazioneList!=null &&
              java.util.Arrays.equals(this.idMovimentazioneList, other.getIdMovimentazioneList()))) &&
            ((this.idAnnotazioniList==null && other.getIdAnnotazioniList()==null) || 
             (this.idAnnotazioniList!=null &&
              java.util.Arrays.equals(this.idAnnotazioniList, other.getIdAnnotazioniList()))) &&
            this.idTipoClasse == other.getIdTipoClasse() &&
            ((this.idAOORespMat==null && other.getIdAOORespMat()==null) || 
             (this.idAOORespMat!=null &&
              this.idAOORespMat.equals(other.getIdAOORespMat()))) &&
            ((this.idAOORespCons==null && other.getIdAOORespCons()==null) || 
             (this.idAOORespCons!=null &&
              this.idAOORespCons.equals(other.getIdAOORespCons()))) &&
            ((this.idStrutturaRespMat==null && other.getIdStrutturaRespMat()==null) || 
             (this.idStrutturaRespMat!=null &&
              this.idStrutturaRespMat.equals(other.getIdStrutturaRespMat()))) &&
            ((this.idStrutturaRespCons==null && other.getIdStrutturaRespCons()==null) || 
             (this.idStrutturaRespCons!=null &&
              this.idStrutturaRespCons.equals(other.getIdStrutturaRespCons()))) &&
            ((this.idNodoRespMat==null && other.getIdNodoRespMat()==null) || 
             (this.idNodoRespMat!=null &&
              this.idNodoRespMat.equals(other.getIdNodoRespMat()))) &&
            ((this.idNodoRespCons==null && other.getIdNodoRespCons()==null) || 
             (this.idNodoRespCons!=null &&
              this.idNodoRespCons.equals(other.getIdNodoRespCons())));
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
        if (getCodice() != null) {
            _hashCode += getCodice().hashCode();
        }
        if (getDescrizione() != null) {
            _hashCode += getDescrizione().hashCode();
        }
        if (getIndiceDiClassificazioneEsteso() != null) {
            _hashCode += getIndiceDiClassificazioneEsteso().hashCode();
        }
        if (getDataCreazione() != null) {
            _hashCode += getDataCreazione().hashCode();
        }
        if (getDataInizio() != null) {
            _hashCode += getDataInizio().hashCode();
        }
        if (getDataFine() != null) {
            _hashCode += getDataFine().hashCode();
        }
        _hashCode += getConservazioneCorrente();
        _hashCode += getConservazioneGenerale();
        if (getDataPassaggioInDeposito() != null) {
            _hashCode += getDataPassaggioInDeposito().hashCode();
        }
        if (getDataBloccoPassaggioInDeposito() != null) {
            _hashCode += getDataBloccoPassaggioInDeposito().hashCode();
        }
        if (getDataSbloccoPassaggioInDeposito() != null) {
            _hashCode += getDataSbloccoPassaggioInDeposito().hashCode();
        }
        if (getDataScarto() != null) {
            _hashCode += getDataScarto().hashCode();
        }
        _hashCode += (isArchivioCorrente() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isDatiPersonali() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isDatiSensibili() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isDatiRiservati() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getCollocazioneCartaceo() != null) {
            _hashCode += getCollocazioneCartaceo().hashCode();
        }
        if (getParoleChiave() != null) {
            _hashCode += getParoleChiave().hashCode();
        }
        if (getDataUltimaModifica() != null) {
            _hashCode += getDataUltimaModifica().hashCode();
        }
        if (getDataCancellazione() != null) {
            _hashCode += getDataCancellazione().hashCode();
        }
        if (getDataEsportazione() != null) {
            _hashCode += getDataEsportazione().hashCode();
        }
        if (getEstremiProvvedimentoDiScarto() != null) {
            _hashCode += getEstremiProvvedimentoDiScarto().hashCode();
        }
        if (getUtenteCreazione() != null) {
            _hashCode += getUtenteCreazione().hashCode();
        }
        if (getIdDeposito() != null) {
            _hashCode += getIdDeposito().hashCode();
        }
        if (getIdMovimentazioneList() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getIdMovimentazioneList());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getIdMovimentazioneList(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
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
        _hashCode += getIdTipoClasse();
        if (getIdAOORespMat() != null) {
            _hashCode += getIdAOORespMat().hashCode();
        }
        if (getIdAOORespCons() != null) {
            _hashCode += getIdAOORespCons().hashCode();
        }
        if (getIdStrutturaRespMat() != null) {
            _hashCode += getIdStrutturaRespMat().hashCode();
        }
        if (getIdStrutturaRespCons() != null) {
            _hashCode += getIdStrutturaRespCons().hashCode();
        }
        if (getIdNodoRespMat() != null) {
            _hashCode += getIdNodoRespMat().hashCode();
        }
        if (getIdNodoRespCons() != null) {
            _hashCode += getIdNodoRespCons().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(AggregazionePropertiesType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "AggregazionePropertiesType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codice");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "codice"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descrizione");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "descrizione"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("indiceDiClassificazioneEsteso");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "indiceDiClassificazioneEsteso"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "XMLType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataCreazione");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "dataCreazione"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "date"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataInizio");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "dataInizio"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "date"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataFine");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "dataFine"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "date"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("conservazioneCorrente");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "conservazioneCorrente"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "integer"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("conservazioneGenerale");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "conservazioneGenerale"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "integer"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataPassaggioInDeposito");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "dataPassaggioInDeposito"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "date"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataBloccoPassaggioInDeposito");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "dataBloccoPassaggioInDeposito"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "date"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataSbloccoPassaggioInDeposito");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "dataSbloccoPassaggioInDeposito"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "date"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataScarto");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "dataScarto"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "date"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("archivioCorrente");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "archivioCorrente"));
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
        elemField.setFieldName("collocazioneCartaceo");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "collocazioneCartaceo"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("paroleChiave");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "paroleChiave"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataUltimaModifica");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "dataUltimaModifica"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "date"));
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
        elemField.setFieldName("estremiProvvedimentoDiScarto");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "estremiProvvedimentoDiScarto"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("utenteCreazione");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "utenteCreazione"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "CodiceFiscaleType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idDeposito");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "idDeposito"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "ObjectIdType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idMovimentazioneList");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "idMovimentazioneList"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "IdMovimentazioneType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idAnnotazioniList");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "idAnnotazioniList"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "IdAnnotazioniType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idTipoClasse");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "idTipoClasse"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "integer"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idAOORespMat");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "idAOORespMat"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "IdAOOType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idAOORespCons");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "idAOORespCons"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "IdAOOType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idStrutturaRespMat");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "idStrutturaRespMat"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "IdStrutturaType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idStrutturaRespCons");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "idStrutturaRespCons"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "IdStrutturaType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idNodoRespMat");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "idNodoRespMat"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "IdNodoType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idNodoRespCons");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "idNodoRespCons"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "IdNodoType"));
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

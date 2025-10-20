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
 * VocePropertiesType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.csi.risca.riscabesrv.util.actaobject;

public class VocePropertiesType  extends it.csi.risca.riscabesrv.util.actaobject.FolderPropertiesType  implements java.io.Serializable {
    private int codice;

    private java.lang.String descrizione;

    private java.lang.String stato;

    private java.lang.String indiceDiClassificazioneEsteso;

    private java.lang.String descrBreve;

    private java.util.Date dataCreazione;

    private java.util.Date dataInizio;

    private java.util.Date dataFine;

    private boolean presenzaFascStand;

    private boolean presenzaFascRealeAnnualeNV;

    private boolean presenzaFascRealeContinuoNV;

    private boolean presenzaFascRealeLegislaturaNV;

    private boolean presenzaFascRealeEreditatoNV;

    private boolean presenzaFascRealeLiberoNV;

    private boolean presenzaFascTemp;

    private boolean presenzaSerieDoc;

    private boolean presenzaSerieFasc;

    private boolean presenzaSerieDoss;

    private boolean insertSottoVociGestCont;

    private boolean creataGestCont;

    private java.lang.String paroleChiave;

    private java.util.Date dataUltimaModifica;

    private int conservazioneCorrente;

    private int conservazioneGenerale;

    private java.util.Date dataBloccoPassaggioInDeposito;

    private java.util.Date dataSbloccoPassaggioInDeposito;

    private java.util.Date dataCancellazione;

    private java.util.Date dataEsportazione;

    private it.csi.risca.riscabesrv.util.actaobject.IdFascicoloStandardType[] idFascicoloStandardList;

    private it.csi.risca.riscabesrv.util.actaobject.IdProvvedimentoAutorizzatType[] idProvvedimentoAutorizzatList;

    private it.csi.risca.riscabesrv.util.actaobject.IdAnnotazioniType[] idAnnotazioniList;

    private int idTipoClasse;

    public VocePropertiesType() {
    }

    public VocePropertiesType(
           it.csi.risca.riscabesrv.util.actaobject.ObjectIdType objectId,
           it.csi.risca.riscabesrv.util.actaobject.ChangeTokenType changeToken,
           java.lang.String objectTypeId,
           it.csi.risca.riscabesrv.util.actaobject.ObjectIdType parentId,
           java.lang.String[] allowedChildObjectTypeIds,
           int codice,
           java.lang.String descrizione,
           java.lang.String stato,
           java.lang.String indiceDiClassificazioneEsteso,
           java.lang.String descrBreve,
           java.util.Date dataCreazione,
           java.util.Date dataInizio,
           java.util.Date dataFine,
           boolean presenzaFascStand,
           boolean presenzaFascRealeAnnualeNV,
           boolean presenzaFascRealeContinuoNV,
           boolean presenzaFascRealeLegislaturaNV,
           boolean presenzaFascRealeEreditatoNV,
           boolean presenzaFascRealeLiberoNV,
           boolean presenzaFascTemp,
           boolean presenzaSerieDoc,
           boolean presenzaSerieFasc,
           boolean presenzaSerieDoss,
           boolean insertSottoVociGestCont,
           boolean creataGestCont,
           java.lang.String paroleChiave,
           java.util.Date dataUltimaModifica,
           int conservazioneCorrente,
           int conservazioneGenerale,
           java.util.Date dataBloccoPassaggioInDeposito,
           java.util.Date dataSbloccoPassaggioInDeposito,
           java.util.Date dataCancellazione,
           java.util.Date dataEsportazione,
           it.csi.risca.riscabesrv.util.actaobject.IdFascicoloStandardType[] idFascicoloStandardList,
           it.csi.risca.riscabesrv.util.actaobject.IdProvvedimentoAutorizzatType[] idProvvedimentoAutorizzatList,
           it.csi.risca.riscabesrv.util.actaobject.IdAnnotazioniType[] idAnnotazioniList,
           int idTipoClasse) {
        super(
            objectId,
            changeToken,
            objectTypeId,
            parentId,
            allowedChildObjectTypeIds);
        this.codice = codice;
        this.descrizione = descrizione;
        this.stato = stato;
        this.indiceDiClassificazioneEsteso = indiceDiClassificazioneEsteso;
        this.descrBreve = descrBreve;
        this.dataCreazione = dataCreazione;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.presenzaFascStand = presenzaFascStand;
        this.presenzaFascRealeAnnualeNV = presenzaFascRealeAnnualeNV;
        this.presenzaFascRealeContinuoNV = presenzaFascRealeContinuoNV;
        this.presenzaFascRealeLegislaturaNV = presenzaFascRealeLegislaturaNV;
        this.presenzaFascRealeEreditatoNV = presenzaFascRealeEreditatoNV;
        this.presenzaFascRealeLiberoNV = presenzaFascRealeLiberoNV;
        this.presenzaFascTemp = presenzaFascTemp;
        this.presenzaSerieDoc = presenzaSerieDoc;
        this.presenzaSerieFasc = presenzaSerieFasc;
        this.presenzaSerieDoss = presenzaSerieDoss;
        this.insertSottoVociGestCont = insertSottoVociGestCont;
        this.creataGestCont = creataGestCont;
        this.paroleChiave = paroleChiave;
        this.dataUltimaModifica = dataUltimaModifica;
        this.conservazioneCorrente = conservazioneCorrente;
        this.conservazioneGenerale = conservazioneGenerale;
        this.dataBloccoPassaggioInDeposito = dataBloccoPassaggioInDeposito;
        this.dataSbloccoPassaggioInDeposito = dataSbloccoPassaggioInDeposito;
        this.dataCancellazione = dataCancellazione;
        this.dataEsportazione = dataEsportazione;
        this.idFascicoloStandardList = idFascicoloStandardList;
        this.idProvvedimentoAutorizzatList = idProvvedimentoAutorizzatList;
        this.idAnnotazioniList = idAnnotazioniList;
        this.idTipoClasse = idTipoClasse;
    }


    /**
     * Gets the codice value for this VocePropertiesType.
     * 
     * @return codice
     */
    public int getCodice() {
        return codice;
    }


    /**
     * Sets the codice value for this VocePropertiesType.
     * 
     * @param codice
     */
    public void setCodice(int codice) {
        this.codice = codice;
    }


    /**
     * Gets the descrizione value for this VocePropertiesType.
     * 
     * @return descrizione
     */
    public java.lang.String getDescrizione() {
        return descrizione;
    }


    /**
     * Sets the descrizione value for this VocePropertiesType.
     * 
     * @param descrizione
     */
    public void setDescrizione(java.lang.String descrizione) {
        this.descrizione = descrizione;
    }


    /**
     * Gets the stato value for this VocePropertiesType.
     * 
     * @return stato
     */
    public java.lang.String getStato() {
        return stato;
    }


    /**
     * Sets the stato value for this VocePropertiesType.
     * 
     * @param stato
     */
    public void setStato(java.lang.String stato) {
        this.stato = stato;
    }


    /**
     * Gets the indiceDiClassificazioneEsteso value for this VocePropertiesType.
     * 
     * @return indiceDiClassificazioneEsteso
     */
    public java.lang.String getIndiceDiClassificazioneEsteso() {
        return indiceDiClassificazioneEsteso;
    }


    /**
     * Sets the indiceDiClassificazioneEsteso value for this VocePropertiesType.
     * 
     * @param indiceDiClassificazioneEsteso
     */
    public void setIndiceDiClassificazioneEsteso(java.lang.String indiceDiClassificazioneEsteso) {
        this.indiceDiClassificazioneEsteso = indiceDiClassificazioneEsteso;
    }


    /**
     * Gets the descrBreve value for this VocePropertiesType.
     * 
     * @return descrBreve
     */
    public java.lang.String getDescrBreve() {
        return descrBreve;
    }


    /**
     * Sets the descrBreve value for this VocePropertiesType.
     * 
     * @param descrBreve
     */
    public void setDescrBreve(java.lang.String descrBreve) {
        this.descrBreve = descrBreve;
    }


    /**
     * Gets the dataCreazione value for this VocePropertiesType.
     * 
     * @return dataCreazione
     */
    public java.util.Date getDataCreazione() {
        return dataCreazione;
    }


    /**
     * Sets the dataCreazione value for this VocePropertiesType.
     * 
     * @param dataCreazione
     */
    public void setDataCreazione(java.util.Date dataCreazione) {
        this.dataCreazione = dataCreazione;
    }


    /**
     * Gets the dataInizio value for this VocePropertiesType.
     * 
     * @return dataInizio
     */
    public java.util.Date getDataInizio() {
        return dataInizio;
    }


    /**
     * Sets the dataInizio value for this VocePropertiesType.
     * 
     * @param dataInizio
     */
    public void setDataInizio(java.util.Date dataInizio) {
        this.dataInizio = dataInizio;
    }


    /**
     * Gets the dataFine value for this VocePropertiesType.
     * 
     * @return dataFine
     */
    public java.util.Date getDataFine() {
        return dataFine;
    }


    /**
     * Sets the dataFine value for this VocePropertiesType.
     * 
     * @param dataFine
     */
    public void setDataFine(java.util.Date dataFine) {
        this.dataFine = dataFine;
    }


    /**
     * Gets the presenzaFascStand value for this VocePropertiesType.
     * 
     * @return presenzaFascStand
     */
    public boolean isPresenzaFascStand() {
        return presenzaFascStand;
    }


    /**
     * Sets the presenzaFascStand value for this VocePropertiesType.
     * 
     * @param presenzaFascStand
     */
    public void setPresenzaFascStand(boolean presenzaFascStand) {
        this.presenzaFascStand = presenzaFascStand;
    }


    /**
     * Gets the presenzaFascRealeAnnualeNV value for this VocePropertiesType.
     * 
     * @return presenzaFascRealeAnnualeNV
     */
    public boolean isPresenzaFascRealeAnnualeNV() {
        return presenzaFascRealeAnnualeNV;
    }


    /**
     * Sets the presenzaFascRealeAnnualeNV value for this VocePropertiesType.
     * 
     * @param presenzaFascRealeAnnualeNV
     */
    public void setPresenzaFascRealeAnnualeNV(boolean presenzaFascRealeAnnualeNV) {
        this.presenzaFascRealeAnnualeNV = presenzaFascRealeAnnualeNV;
    }


    /**
     * Gets the presenzaFascRealeContinuoNV value for this VocePropertiesType.
     * 
     * @return presenzaFascRealeContinuoNV
     */
    public boolean isPresenzaFascRealeContinuoNV() {
        return presenzaFascRealeContinuoNV;
    }


    /**
     * Sets the presenzaFascRealeContinuoNV value for this VocePropertiesType.
     * 
     * @param presenzaFascRealeContinuoNV
     */
    public void setPresenzaFascRealeContinuoNV(boolean presenzaFascRealeContinuoNV) {
        this.presenzaFascRealeContinuoNV = presenzaFascRealeContinuoNV;
    }


    /**
     * Gets the presenzaFascRealeLegislaturaNV value for this VocePropertiesType.
     * 
     * @return presenzaFascRealeLegislaturaNV
     */
    public boolean isPresenzaFascRealeLegislaturaNV() {
        return presenzaFascRealeLegislaturaNV;
    }


    /**
     * Sets the presenzaFascRealeLegislaturaNV value for this VocePropertiesType.
     * 
     * @param presenzaFascRealeLegislaturaNV
     */
    public void setPresenzaFascRealeLegislaturaNV(boolean presenzaFascRealeLegislaturaNV) {
        this.presenzaFascRealeLegislaturaNV = presenzaFascRealeLegislaturaNV;
    }


    /**
     * Gets the presenzaFascRealeEreditatoNV value for this VocePropertiesType.
     * 
     * @return presenzaFascRealeEreditatoNV
     */
    public boolean isPresenzaFascRealeEreditatoNV() {
        return presenzaFascRealeEreditatoNV;
    }


    /**
     * Sets the presenzaFascRealeEreditatoNV value for this VocePropertiesType.
     * 
     * @param presenzaFascRealeEreditatoNV
     */
    public void setPresenzaFascRealeEreditatoNV(boolean presenzaFascRealeEreditatoNV) {
        this.presenzaFascRealeEreditatoNV = presenzaFascRealeEreditatoNV;
    }


    /**
     * Gets the presenzaFascRealeLiberoNV value for this VocePropertiesType.
     * 
     * @return presenzaFascRealeLiberoNV
     */
    public boolean isPresenzaFascRealeLiberoNV() {
        return presenzaFascRealeLiberoNV;
    }


    /**
     * Sets the presenzaFascRealeLiberoNV value for this VocePropertiesType.
     * 
     * @param presenzaFascRealeLiberoNV
     */
    public void setPresenzaFascRealeLiberoNV(boolean presenzaFascRealeLiberoNV) {
        this.presenzaFascRealeLiberoNV = presenzaFascRealeLiberoNV;
    }


    /**
     * Gets the presenzaFascTemp value for this VocePropertiesType.
     * 
     * @return presenzaFascTemp
     */
    public boolean isPresenzaFascTemp() {
        return presenzaFascTemp;
    }


    /**
     * Sets the presenzaFascTemp value for this VocePropertiesType.
     * 
     * @param presenzaFascTemp
     */
    public void setPresenzaFascTemp(boolean presenzaFascTemp) {
        this.presenzaFascTemp = presenzaFascTemp;
    }


    /**
     * Gets the presenzaSerieDoc value for this VocePropertiesType.
     * 
     * @return presenzaSerieDoc
     */
    public boolean isPresenzaSerieDoc() {
        return presenzaSerieDoc;
    }


    /**
     * Sets the presenzaSerieDoc value for this VocePropertiesType.
     * 
     * @param presenzaSerieDoc
     */
    public void setPresenzaSerieDoc(boolean presenzaSerieDoc) {
        this.presenzaSerieDoc = presenzaSerieDoc;
    }


    /**
     * Gets the presenzaSerieFasc value for this VocePropertiesType.
     * 
     * @return presenzaSerieFasc
     */
    public boolean isPresenzaSerieFasc() {
        return presenzaSerieFasc;
    }


    /**
     * Sets the presenzaSerieFasc value for this VocePropertiesType.
     * 
     * @param presenzaSerieFasc
     */
    public void setPresenzaSerieFasc(boolean presenzaSerieFasc) {
        this.presenzaSerieFasc = presenzaSerieFasc;
    }


    /**
     * Gets the presenzaSerieDoss value for this VocePropertiesType.
     * 
     * @return presenzaSerieDoss
     */
    public boolean isPresenzaSerieDoss() {
        return presenzaSerieDoss;
    }


    /**
     * Sets the presenzaSerieDoss value for this VocePropertiesType.
     * 
     * @param presenzaSerieDoss
     */
    public void setPresenzaSerieDoss(boolean presenzaSerieDoss) {
        this.presenzaSerieDoss = presenzaSerieDoss;
    }


    /**
     * Gets the insertSottoVociGestCont value for this VocePropertiesType.
     * 
     * @return insertSottoVociGestCont
     */
    public boolean isInsertSottoVociGestCont() {
        return insertSottoVociGestCont;
    }


    /**
     * Sets the insertSottoVociGestCont value for this VocePropertiesType.
     * 
     * @param insertSottoVociGestCont
     */
    public void setInsertSottoVociGestCont(boolean insertSottoVociGestCont) {
        this.insertSottoVociGestCont = insertSottoVociGestCont;
    }


    /**
     * Gets the creataGestCont value for this VocePropertiesType.
     * 
     * @return creataGestCont
     */
    public boolean isCreataGestCont() {
        return creataGestCont;
    }


    /**
     * Sets the creataGestCont value for this VocePropertiesType.
     * 
     * @param creataGestCont
     */
    public void setCreataGestCont(boolean creataGestCont) {
        this.creataGestCont = creataGestCont;
    }


    /**
     * Gets the paroleChiave value for this VocePropertiesType.
     * 
     * @return paroleChiave
     */
    public java.lang.String getParoleChiave() {
        return paroleChiave;
    }


    /**
     * Sets the paroleChiave value for this VocePropertiesType.
     * 
     * @param paroleChiave
     */
    public void setParoleChiave(java.lang.String paroleChiave) {
        this.paroleChiave = paroleChiave;
    }


    /**
     * Gets the dataUltimaModifica value for this VocePropertiesType.
     * 
     * @return dataUltimaModifica
     */
    public java.util.Date getDataUltimaModifica() {
        return dataUltimaModifica;
    }


    /**
     * Sets the dataUltimaModifica value for this VocePropertiesType.
     * 
     * @param dataUltimaModifica
     */
    public void setDataUltimaModifica(java.util.Date dataUltimaModifica) {
        this.dataUltimaModifica = dataUltimaModifica;
    }


    /**
     * Gets the conservazioneCorrente value for this VocePropertiesType.
     * 
     * @return conservazioneCorrente
     */
    public int getConservazioneCorrente() {
        return conservazioneCorrente;
    }


    /**
     * Sets the conservazioneCorrente value for this VocePropertiesType.
     * 
     * @param conservazioneCorrente
     */
    public void setConservazioneCorrente(int conservazioneCorrente) {
        this.conservazioneCorrente = conservazioneCorrente;
    }


    /**
     * Gets the conservazioneGenerale value for this VocePropertiesType.
     * 
     * @return conservazioneGenerale
     */
    public int getConservazioneGenerale() {
        return conservazioneGenerale;
    }


    /**
     * Sets the conservazioneGenerale value for this VocePropertiesType.
     * 
     * @param conservazioneGenerale
     */
    public void setConservazioneGenerale(int conservazioneGenerale) {
        this.conservazioneGenerale = conservazioneGenerale;
    }


    /**
     * Gets the dataBloccoPassaggioInDeposito value for this VocePropertiesType.
     * 
     * @return dataBloccoPassaggioInDeposito
     */
    public java.util.Date getDataBloccoPassaggioInDeposito() {
        return dataBloccoPassaggioInDeposito;
    }


    /**
     * Sets the dataBloccoPassaggioInDeposito value for this VocePropertiesType.
     * 
     * @param dataBloccoPassaggioInDeposito
     */
    public void setDataBloccoPassaggioInDeposito(java.util.Date dataBloccoPassaggioInDeposito) {
        this.dataBloccoPassaggioInDeposito = dataBloccoPassaggioInDeposito;
    }


    /**
     * Gets the dataSbloccoPassaggioInDeposito value for this VocePropertiesType.
     * 
     * @return dataSbloccoPassaggioInDeposito
     */
    public java.util.Date getDataSbloccoPassaggioInDeposito() {
        return dataSbloccoPassaggioInDeposito;
    }


    /**
     * Sets the dataSbloccoPassaggioInDeposito value for this VocePropertiesType.
     * 
     * @param dataSbloccoPassaggioInDeposito
     */
    public void setDataSbloccoPassaggioInDeposito(java.util.Date dataSbloccoPassaggioInDeposito) {
        this.dataSbloccoPassaggioInDeposito = dataSbloccoPassaggioInDeposito;
    }


    /**
     * Gets the dataCancellazione value for this VocePropertiesType.
     * 
     * @return dataCancellazione
     */
    public java.util.Date getDataCancellazione() {
        return dataCancellazione;
    }


    /**
     * Sets the dataCancellazione value for this VocePropertiesType.
     * 
     * @param dataCancellazione
     */
    public void setDataCancellazione(java.util.Date dataCancellazione) {
        this.dataCancellazione = dataCancellazione;
    }


    /**
     * Gets the dataEsportazione value for this VocePropertiesType.
     * 
     * @return dataEsportazione
     */
    public java.util.Date getDataEsportazione() {
        return dataEsportazione;
    }


    /**
     * Sets the dataEsportazione value for this VocePropertiesType.
     * 
     * @param dataEsportazione
     */
    public void setDataEsportazione(java.util.Date dataEsportazione) {
        this.dataEsportazione = dataEsportazione;
    }


    /**
     * Gets the idFascicoloStandardList value for this VocePropertiesType.
     * 
     * @return idFascicoloStandardList
     */
    public it.csi.risca.riscabesrv.util.actaobject.IdFascicoloStandardType[] getIdFascicoloStandardList() {
        return idFascicoloStandardList;
    }


    /**
     * Sets the idFascicoloStandardList value for this VocePropertiesType.
     * 
     * @param idFascicoloStandardList
     */
    public void setIdFascicoloStandardList(it.csi.risca.riscabesrv.util.actaobject.IdFascicoloStandardType[] idFascicoloStandardList) {
        this.idFascicoloStandardList = idFascicoloStandardList;
    }

    public it.csi.risca.riscabesrv.util.actaobject.IdFascicoloStandardType getIdFascicoloStandardList(int i) {
        return this.idFascicoloStandardList[i];
    }

    public void setIdFascicoloStandardList(int i, it.csi.risca.riscabesrv.util.actaobject.IdFascicoloStandardType _value) {
        this.idFascicoloStandardList[i] = _value;
    }


    /**
     * Gets the idProvvedimentoAutorizzatList value for this VocePropertiesType.
     * 
     * @return idProvvedimentoAutorizzatList
     */
    public it.csi.risca.riscabesrv.util.actaobject.IdProvvedimentoAutorizzatType[] getIdProvvedimentoAutorizzatList() {
        return idProvvedimentoAutorizzatList;
    }


    /**
     * Sets the idProvvedimentoAutorizzatList value for this VocePropertiesType.
     * 
     * @param idProvvedimentoAutorizzatList
     */
    public void setIdProvvedimentoAutorizzatList(it.csi.risca.riscabesrv.util.actaobject.IdProvvedimentoAutorizzatType[] idProvvedimentoAutorizzatList) {
        this.idProvvedimentoAutorizzatList = idProvvedimentoAutorizzatList;
    }

    public it.csi.risca.riscabesrv.util.actaobject.IdProvvedimentoAutorizzatType getIdProvvedimentoAutorizzatList(int i) {
        return this.idProvvedimentoAutorizzatList[i];
    }

    public void setIdProvvedimentoAutorizzatList(int i, it.csi.risca.riscabesrv.util.actaobject.IdProvvedimentoAutorizzatType _value) {
        this.idProvvedimentoAutorizzatList[i] = _value;
    }


    /**
     * Gets the idAnnotazioniList value for this VocePropertiesType.
     * 
     * @return idAnnotazioniList
     */
    public it.csi.risca.riscabesrv.util.actaobject.IdAnnotazioniType[] getIdAnnotazioniList() {
        return idAnnotazioniList;
    }


    /**
     * Sets the idAnnotazioniList value for this VocePropertiesType.
     * 
     * @param idAnnotazioniList
     */
    public void setIdAnnotazioniList(it.csi.risca.riscabesrv.util.actaobject.IdAnnotazioniType[] idAnnotazioniList) {
        this.idAnnotazioniList = idAnnotazioniList;
    }

    public it.csi.risca.riscabesrv.util.actaobject.IdAnnotazioniType getIdAnnotazioniList(int i) {
        return this.idAnnotazioniList[i];
    }

    public void setIdAnnotazioniList(int i, it.csi.risca.riscabesrv.util.actaobject.IdAnnotazioniType _value) {
        this.idAnnotazioniList[i] = _value;
    }


    /**
     * Gets the idTipoClasse value for this VocePropertiesType.
     * 
     * @return idTipoClasse
     */
    public int getIdTipoClasse() {
        return idTipoClasse;
    }


    /**
     * Sets the idTipoClasse value for this VocePropertiesType.
     * 
     * @param idTipoClasse
     */
    public void setIdTipoClasse(int idTipoClasse) {
        this.idTipoClasse = idTipoClasse;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof VocePropertiesType)) return false;
        VocePropertiesType other = (VocePropertiesType) obj;

        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            this.codice == other.getCodice() &&
            ((this.descrizione==null && other.getDescrizione()==null) || 
             (this.descrizione!=null &&
              this.descrizione.equals(other.getDescrizione()))) &&
            ((this.stato==null && other.getStato()==null) || 
             (this.stato!=null &&
              this.stato.equals(other.getStato()))) &&
            ((this.indiceDiClassificazioneEsteso==null && other.getIndiceDiClassificazioneEsteso()==null) || 
             (this.indiceDiClassificazioneEsteso!=null &&
              this.indiceDiClassificazioneEsteso.equals(other.getIndiceDiClassificazioneEsteso()))) &&
            ((this.descrBreve==null && other.getDescrBreve()==null) || 
             (this.descrBreve!=null &&
              this.descrBreve.equals(other.getDescrBreve()))) &&
            ((this.dataCreazione==null && other.getDataCreazione()==null) || 
             (this.dataCreazione!=null &&
              this.dataCreazione.equals(other.getDataCreazione()))) &&
            ((this.dataInizio==null && other.getDataInizio()==null) || 
             (this.dataInizio!=null &&
              this.dataInizio.equals(other.getDataInizio()))) &&
            ((this.dataFine==null && other.getDataFine()==null) || 
             (this.dataFine!=null &&
              this.dataFine.equals(other.getDataFine()))) &&
            this.presenzaFascStand == other.isPresenzaFascStand() &&
            this.presenzaFascRealeAnnualeNV == other.isPresenzaFascRealeAnnualeNV() &&
            this.presenzaFascRealeContinuoNV == other.isPresenzaFascRealeContinuoNV() &&
            this.presenzaFascRealeLegislaturaNV == other.isPresenzaFascRealeLegislaturaNV() &&
            this.presenzaFascRealeEreditatoNV == other.isPresenzaFascRealeEreditatoNV() &&
            this.presenzaFascRealeLiberoNV == other.isPresenzaFascRealeLiberoNV() &&
            this.presenzaFascTemp == other.isPresenzaFascTemp() &&
            this.presenzaSerieDoc == other.isPresenzaSerieDoc() &&
            this.presenzaSerieFasc == other.isPresenzaSerieFasc() &&
            this.presenzaSerieDoss == other.isPresenzaSerieDoss() &&
            this.insertSottoVociGestCont == other.isInsertSottoVociGestCont() &&
            this.creataGestCont == other.isCreataGestCont() &&
            ((this.paroleChiave==null && other.getParoleChiave()==null) || 
             (this.paroleChiave!=null &&
              this.paroleChiave.equals(other.getParoleChiave()))) &&
            ((this.dataUltimaModifica==null && other.getDataUltimaModifica()==null) || 
             (this.dataUltimaModifica!=null &&
              this.dataUltimaModifica.equals(other.getDataUltimaModifica()))) &&
            this.conservazioneCorrente == other.getConservazioneCorrente() &&
            this.conservazioneGenerale == other.getConservazioneGenerale() &&
            ((this.dataBloccoPassaggioInDeposito==null && other.getDataBloccoPassaggioInDeposito()==null) || 
             (this.dataBloccoPassaggioInDeposito!=null &&
              this.dataBloccoPassaggioInDeposito.equals(other.getDataBloccoPassaggioInDeposito()))) &&
            ((this.dataSbloccoPassaggioInDeposito==null && other.getDataSbloccoPassaggioInDeposito()==null) || 
             (this.dataSbloccoPassaggioInDeposito!=null &&
              this.dataSbloccoPassaggioInDeposito.equals(other.getDataSbloccoPassaggioInDeposito()))) &&
            ((this.dataCancellazione==null && other.getDataCancellazione()==null) || 
             (this.dataCancellazione!=null &&
              this.dataCancellazione.equals(other.getDataCancellazione()))) &&
            ((this.dataEsportazione==null && other.getDataEsportazione()==null) || 
             (this.dataEsportazione!=null &&
              this.dataEsportazione.equals(other.getDataEsportazione()))) &&
            ((this.idFascicoloStandardList==null && other.getIdFascicoloStandardList()==null) || 
             (this.idFascicoloStandardList!=null &&
              java.util.Arrays.equals(this.idFascicoloStandardList, other.getIdFascicoloStandardList()))) &&
            ((this.idProvvedimentoAutorizzatList==null && other.getIdProvvedimentoAutorizzatList()==null) || 
             (this.idProvvedimentoAutorizzatList!=null &&
              java.util.Arrays.equals(this.idProvvedimentoAutorizzatList, other.getIdProvvedimentoAutorizzatList()))) &&
            ((this.idAnnotazioniList==null && other.getIdAnnotazioniList()==null) || 
             (this.idAnnotazioniList!=null &&
              java.util.Arrays.equals(this.idAnnotazioniList, other.getIdAnnotazioniList()))) &&
            this.idTipoClasse == other.getIdTipoClasse();
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
        _hashCode += getCodice();
        if (getDescrizione() != null) {
            _hashCode += getDescrizione().hashCode();
        }
        if (getStato() != null) {
            _hashCode += getStato().hashCode();
        }
        if (getIndiceDiClassificazioneEsteso() != null) {
            _hashCode += getIndiceDiClassificazioneEsteso().hashCode();
        }
        if (getDescrBreve() != null) {
            _hashCode += getDescrBreve().hashCode();
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
        _hashCode += (isPresenzaFascStand() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isPresenzaFascRealeAnnualeNV() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isPresenzaFascRealeContinuoNV() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isPresenzaFascRealeLegislaturaNV() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isPresenzaFascRealeEreditatoNV() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isPresenzaFascRealeLiberoNV() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isPresenzaFascTemp() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isPresenzaSerieDoc() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isPresenzaSerieFasc() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isPresenzaSerieDoss() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isInsertSottoVociGestCont() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isCreataGestCont() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getParoleChiave() != null) {
            _hashCode += getParoleChiave().hashCode();
        }
        if (getDataUltimaModifica() != null) {
            _hashCode += getDataUltimaModifica().hashCode();
        }
        _hashCode += getConservazioneCorrente();
        _hashCode += getConservazioneGenerale();
        if (getDataBloccoPassaggioInDeposito() != null) {
            _hashCode += getDataBloccoPassaggioInDeposito().hashCode();
        }
        if (getDataSbloccoPassaggioInDeposito() != null) {
            _hashCode += getDataSbloccoPassaggioInDeposito().hashCode();
        }
        if (getDataCancellazione() != null) {
            _hashCode += getDataCancellazione().hashCode();
        }
        if (getDataEsportazione() != null) {
            _hashCode += getDataEsportazione().hashCode();
        }
        if (getIdFascicoloStandardList() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getIdFascicoloStandardList());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getIdFascicoloStandardList(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getIdProvvedimentoAutorizzatList() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getIdProvvedimentoAutorizzatList());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getIdProvvedimentoAutorizzatList(), i);
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
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(VocePropertiesType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "VocePropertiesType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codice");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "codice"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "integer"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descrizione");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "descrizione"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("stato");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "stato"));
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
        elemField.setFieldName("descrBreve");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "descrBreve"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "string"));
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
        elemField.setFieldName("presenzaFascStand");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "presenzaFascStand"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("presenzaFascRealeAnnualeNV");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "presenzaFascRealeAnnualeNV"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("presenzaFascRealeContinuoNV");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "presenzaFascRealeContinuoNV"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("presenzaFascRealeLegislaturaNV");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "presenzaFascRealeLegislaturaNV"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("presenzaFascRealeEreditatoNV");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "presenzaFascRealeEreditatoNV"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("presenzaFascRealeLiberoNV");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "presenzaFascRealeLiberoNV"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("presenzaFascTemp");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "presenzaFascTemp"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("presenzaSerieDoc");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "presenzaSerieDoc"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("presenzaSerieFasc");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "presenzaSerieFasc"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("presenzaSerieDoss");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "presenzaSerieDoss"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("insertSottoVociGestCont");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "insertSottoVociGestCont"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("creataGestCont");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "creataGestCont"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "boolean"));
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
        elemField.setFieldName("idFascicoloStandardList");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "idFascicoloStandardList"));
        elemField.setXmlType(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "IdFascicoloStandardType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idProvvedimentoAutorizzatList");
        elemField.setXmlName(new javax.xml.namespace.QName("archive.acaris.acta.doqui.it", "idProvvedimentoAutorizzatList"));
        elemField.setXmlType(new javax.xml.namespace.QName("common.acaris.acta.doqui.it", "IdProvvedimentoAutorizzatType"));
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

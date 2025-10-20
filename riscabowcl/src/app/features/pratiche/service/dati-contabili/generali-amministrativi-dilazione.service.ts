import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { Moment } from 'moment';
import { PraticaVo } from 'src/app/core/commons/vo/pratica-vo';
import { StatoDebitorioVo } from '../../../../core/commons/vo/stato-debitorio-vo';
import { RiscaModalService } from '../../../../shared/services/risca/risca-modal.service';
import {
  convertMomentToNgbDateStruct,
  convertMomentToViewDate,
  convertServerDateToNgbDateStruct,
} from '../../../../shared/services/risca/risca-utilities/risca-utilities.functions';
import { RiscaUtilitiesService } from '../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import {
  AppActions,
  CodiciIstanzaAmbiente,
  FormUpdatePropagation,
  IRiscaCheckboxData,
  RiscaComponentConfig,
  RiscaFormInputCheckbox,
} from '../../../../shared/utilities';
import { ApriModalConfigs } from '../../../../shared/utilities/classes/risca-modal/risca-modal.classes';
import { IApriModalConfigsForClass } from '../../../../shared/utilities/interfaces/risca/risca-modal/risca-modal.interfaces';
import { GeneraliAmministrativiDilazioneConsts } from '../../consts/dati-contabili/generali-amministrativi-dilazione.consts';
import {
  IDCSetFormValue,
  IGenAmmDilSetFormValue,
} from '../../interfaces/dati-contabili/dati-contabili-utility.interfaces';
import { DilazioneSDModalComponent } from '../../modal/dilazione-sd-modal/dilazione-sd-modal.component';
import { IstanzaProvvedimentoVo } from './../../../../core/commons/vo/istanza-provvedimento-vo';
import { getRataPadre } from './dati-contabili/dati-contabili-utility.functions';
import { DatiContabiliUtilityService } from './dati-contabili/dati-contabili-utility.service';

@Injectable({ providedIn: 'root' })
export class GeneraliAmministrativiDilazioneService {
  /** Costante per le informazioni del componente specifico.  */
  GAD_C = GeneraliAmministrativiDilazioneConsts;
  /**
   * Costante per il campo Istanza di rinnovo.
   * Se c'è almeno un'istanza di rinnovo ma non ce ne sono con una data provvedimento, inserisce questo valore.
   */
  private ND: string = 'N.D.';

  /**
   * Costruttore.
   */
  constructor(
    private _datiContabiliUtility: DatiContabiliUtilityService,
    private _riscaModal: RiscaModalService,
    private _riscaUtilities: RiscaUtilitiesService
  ) {}

  /**
   * ###########################################
   * FUNZIONE DI SET DATI ALL'INTERNO DELLA FORM
   * ###########################################
   */

  /**
   * Funzione che effettua il set delle informazioni, per la form generali/amministrativi/dilazione.
   * Il set dei dati avverrà per referenza dell'oggetto mainForm.
   * @param configs IGenAmmDilSetFormValue contenente le informazioni per il set dati.
   */
  setMainFormValues(configs: IGenAmmDilSetFormValue) {
    // Verifico l'input
    if (!configs) {
      // Niente da configurare
      return;
    }

    // Recupero i dati per la configurazione
    const mainForm = configs.mainForm;
    const sd = configs.statoDebitorio;
    const p = configs.pratica;
    const options = configs.options;
    const modalita = configs.modalita;

    // Verifico che esistano i dati minimi
    if (!mainForm) {
      // Nessuna configurazione
      return;
    }

    // Recupero le informazioni per numeroTitolo, dataTitolo, tipoTitolo e idProvvedimento a seconda della configurazione
    let numTitoloVal: string;
    let dataTitoloVal: NgbDateStruct;
    let tipo_titolo: string;
    // Verifico la modalità
    if (modalita === AppActions.inserimento) {
      // In inserimento il dato è pre-valorizzato dalla pratica
      const prov: IstanzaProvvedimentoVo =
        this.provvedimentoPiuRecentePratica(p);
      // Estraggo e setto le informazioni
      numTitoloVal = prov?.num_titolo ?? '';
      dataTitoloVal = this._riscaUtilities.convertServerDateToNgbDateStruct(
        prov?.data_provvedimento
      );
      tipo_titolo = prov?.id_tipo_titolo?.des_tipo_titolo ?? '';
      // #
    } else if (modalita === AppActions.modifica) {
      // Recupero i dati dallo stato debitorio
      numTitoloVal = sd?.num_titolo?.toString();
      dataTitoloVal = this._riscaUtilities.convertMomentToNgbDateStruct(
        sd?.data_provvedimento
      );
      tipo_titolo = sd?.tipo_titolo;
    }

    // Creo le configurazioni per aggiornare i dati del form
    // ####### CODICE UTENZA #######
    const codiceUtenza: IDCSetFormValue = {
      mainForm,
      options,
      formControlKey: this.GAD_C.CODICE_UTENZA,
      value: `${p?.cod_riscossione_prov || ''}${p?.cod_riscossione_prog || ''}`,
    };
    // ####### RICH. PROT. N° #######
    const numRicProt: IDCSetFormValue = {
      mainForm,
      options,
      formControlKey: this.GAD_C.NUM_RICHIESTA_PROTOCOLLO,
      value: sd?.num_richiesta_protocollo,
    };
    // ####### DATA #######
    const dataRicProt: IDCSetFormValue = {
      mainForm,
      options,
      formControlKey: this.GAD_C.DATA_RICHIESTA_PROTOCOLLO,
      value: convertMomentToNgbDateStruct(
        sd?.data_richiesta_protocollo as Moment
      ),
    };
    // ####### DATA ULTIMA MODIFICA #######
    const dataUltMod: IDCSetFormValue = {
      mainForm,
      options,
      formControlKey: this.GAD_C.DATA_ULTIMA_MODIFICA,
      value: convertMomentToViewDate(sd?.data_ultima_modifica as Moment),
    };
    // ####### MOTIVAZIONE #######
    const motivazione: IDCSetFormValue = {
      mainForm,
      options,
      formControlKey: this.GAD_C.MOTIVAZIONE,
      value: sd?.desc_motivo_annullo,
    };
    // ####### PERIODO PAGAMENTO #######
    const periodoPag: IDCSetFormValue = {
      mainForm,
      options,
      formControlKey: this.GAD_C.PERIODO_PAGAMENTO,
      value: sd?.desc_periodo_pagamento,
    };
    // ####### SCADENZA PAGAMENTO #######
    const rataPadre = getRataPadre(sd?.rate);
    const scadenzaPag: IDCSetFormValue = {
      mainForm,
      options,
      formControlKey: this.GAD_C.SCADENZA_PAGAMENTO,
      value: convertMomentToNgbDateStruct(
        rataPadre?.data_scadenza_pagamento as Moment
      ),
    };
    // ####### CODICE AVVISO - IUV #######
    /**
     * Il codice avviso viene scaricato asincrono tramite API /iuv?nap={nap}.
     * IuvVo.codice_avviso
     */
    // ####### STATO - IUV #######
    /**
     * @jira RISCA-ISSUES-52
     * Come per il codice avviso viene scaricato asincrono tramite API /iuv?nap={nap}.
     * Rimuovo questa logica, è probabilmente un refuso.
     */
    // const stato: IDCSetFormValue = {
    //   mainForm,
    //   options,
    //   formControlKey: this.GAD_C.STATO,
    //   value: sd?.flg_annullato ? 'Annullato' : 'Attivo',
    // };
    // ####### CANONE ANNUALITÀ CORRENTE #######
    const canoneAnnCor: IDCSetFormValue = {
      mainForm,
      options,
      formControlKey: this.GAD_C.CANONE_ANNUALITA_CORRENTE,
      value: this._datiContabiliUtility.canoneAnnualitaTotale(sd?.annualita_sd),
    };
    // ####### ANNUALITÀ PRECEDENTE € #######
    const annualitaPrec: IDCSetFormValue = {
      mainForm,
      options,
      formControlKey: this.GAD_C.ANNUALITA_PRECEDENTE,
      value: sd?.imp_recupero_canone,
    };
    // ####### CANONE DOVUTO #######
    const canoneDovuto: IDCSetFormValue = {
      mainForm,
      options,
      formControlKey: this.GAD_C.CANONE_DOVUTO,
      value: sd?.canone_dovuto,
    };
    // ####### INTERESSI MATURATI #######
    // RISCA-754: da sd?.imp_recupero_interessi a rataPadre?.interessi_maturati. E' stato fatto un intervento con Silvia Cordero direttamente in call.
    const interessiMat: IDCSetFormValue = {
      mainForm,
      options,
      formControlKey: this.GAD_C.INTERESSI_MATURATI,
      value: rataPadre?.interessi_maturati ?? 0,
    };
    // ####### SPESE DI NOTIFICA #######
    const speseNotifica: IDCSetFormValue = {
      mainForm,
      options,
      formControlKey: this.GAD_C.SPESE_NOTIFICA,
      value: sd?.imp_spese_notifica,
    };
    // ####### IMPORTO DA COMPENSAZIONE #######
    const importoCompensazione: IDCSetFormValue = {
      mainForm,
      options,
      formControlKey: this.GAD_C.IMPORTO_COMPENSAZIONE,
      value: sd?.imp_compensazione_canone,
    };
    // ####### TIPO TITOLO #######
    const tipoTitolo: IDCSetFormValue = {
      mainForm,
      options,
      formControlKey: this.GAD_C.TIPO_TITOLO,
      value: tipo_titolo,
    };
    // ####### NUMERO TITOLO #######
    const numeroTitolo: IDCSetFormValue = {
      mainForm,
      options,
      formControlKey: this.GAD_C.NUMERO_TITOLO,
      value: numTitoloVal,
    };
    // ####### DATA TITOLO #######
    const dataTitolo: IDCSetFormValue = {
      mainForm,
      options,
      formControlKey: this.GAD_C.DATA_TITOLO,
      value: dataTitoloVal,
    };
    // ####### DATA INIZIO CONCESSIONE #######
    const inizioConcessione: IDCSetFormValue = {
      mainForm,
      options,
      formControlKey: this.GAD_C.INIZIO_CONCESSIONE,
      value: convertServerDateToNgbDateStruct(p?.data_ini_concessione),
    };
    // ####### SCADENZA CONCESSIONE #######
    const scadenzaConcessione: IDCSetFormValue = {
      mainForm,
      options,
      formControlKey: this.GAD_C.SCADENZA_CONCESSIONE,
      value: convertServerDateToNgbDateStruct(p?.data_scad_concessione),
    };
    // ####### ISTANZA DI RINNOVO #######
    const istanzaDiRinnovo: IDCSetFormValue = {
      mainForm,
      options,
      formControlKey: this.GAD_C.ISTANZA_RINNOVO,
      value: this.setIstanzaDiRinnovo(p),
    };
    // ####### NOTE #######
    const noteSD: IDCSetFormValue = {
      mainForm,
      options,
      formControlKey: this.GAD_C.NOTE_SD,
      value: sd?.note,
    };
    // ####### UTENZE DA COMPENSAZIONE #######
    /**
     * L'utenza da compensazione viene scaricata asincrona tramite API /riscossione?idStatoDebitorio={idStatoDebitorio}.
     */

    // Vado a lanciare il set di tutti i campi con le configurazioni
    this.setFCValue(codiceUtenza);
    this.setFCValue(numRicProt);
    this.setFCValue(dataRicProt);
    this.setFCValue(dataUltMod);
    this.setFCValue(motivazione);
    this.setFCValue(periodoPag);
    this.setFCValue(scadenzaPag);
    // this.setFCValue(stato);
    this.setFCValue(canoneAnnCor);
    this.setFCValue(annualitaPrec);
    this.setFCValue(canoneDovuto);
    this.setFCValue(interessiMat);
    this.setFCValue(speseNotifica);
    this.setFCValue(importoCompensazione);
    this.setFCValue(numeroTitolo);
    this.setFCValue(tipoTitolo);
    this.setFCValue(dataTitolo);
    this.setFCValue(inizioConcessione);
    this.setFCValue(scadenzaConcessione);
    this.setFCValue(istanzaDiRinnovo);
    this.setFCValue(noteSD);
  }

  /**
   * Funzione che permette il set delle informazioni relative alle checkbox della pagina.
   * @param configs IGenAmmDilSetFormValue contenente le informazioni per il set dati.
   */
  setMainFormChecboxes(configs: IGenAmmDilSetFormValue) {
    // Verifico l'input
    if (!configs) {
      // Niente da configurare
      return;
    }

    // Recupero i dati per la configurazione
    const form = configs.mainForm;
    const sd = configs.statoDebitorio;
    const o = configs.options;
    const modalita = configs.modalita;
    const formInputs = configs.formInputs;

    // Verifico che esistano i dati minimi
    if (!form || !formInputs) {
      // Nessuna configurazione
      return;
    }

    // ####### RESTITUITO AL MITTENTE #######
    const keyRMit = this.GAD_C.RESTITUITO_MITTENTE;
    const lblRMit = this.GAD_C.LABEL_RESTITUITO_MITTENTE;
    const valRMit = sd?.flg_restituito_mittente ?? false;
    const restituitoMit = this.generaIRCD(lblRMit, valRMit);
    const flgResMit = this.generaIDCSetFV(form, keyRMit, restituitoMit, o);
    const chkResMit = formInputs.restituitoMittenteConfig;

    // ####### ANNULLATO #######
    const keyAnn = this.GAD_C.ANNULLATO;
    const lblAnn = this.GAD_C.LABEL_ANNULLATO;
    const valAnn = sd?.flg_annullato ?? false;
    const annullato = this.generaIRCD(lblAnn, valAnn);
    const flgAnnullato = this.generaIDCSetFV(form, keyAnn, annullato, o);
    const chkAnnullato = formInputs.annullatoConfig;

    // ####### INVIO SPECIALE POSTEL #######
    // Variabile di appoggio
    let valISP: boolean;
    // Controllo la modalità per valorizzare ed abilitare il flag invio speciale postel
    if (modalita === AppActions.inserimento) {
      // Se siamo in inserimento, Invio speciale a postel è true e bloccato.
      valISP = false;
      // #
    } else {
      // Se siamo in modifica, Invio speciale a postel è bloccato e valorizzato con il dato dello stato debitorio.
      valISP = sd?.flg_invio_speciale ?? false;
      // #
    }
    const keyISP = this.GAD_C.INVIO_SPECIALE_POSTEL;
    const lblISP = this.GAD_C.LABEL_INVIO_SPECIALE_POSTEL;
    const invioSP = this.generaIRCD(lblISP, valISP);
    const flgInvioSpec = this.generaIDCSetFV(form, keyISP, invioSP, o);
    const chkInvioSpec = formInputs.invioSpecialePostelConfig;

    // ####### ADDEBITO ANNO SUCCESSIVO #######
    const keyAAS = this.GAD_C.ADDEBITO_ANNO_SUCCESSIVO;
    const lblAAS = this.GAD_C.LABEL_ADDEBITO_ANNO_SUCCESSIVO;
    const valAAS = sd?.flg_addebito_anno_successivo ?? false;
    const addebAnnSuc = this.generaIRCD(lblAAS, valAAS);
    const flgAddAnnoSucc = this.generaIDCSetFV(form, keyAAS, addebAnnSuc, o);
    const chkAddAnnoSucc = formInputs.addebitoAnnoSuccessivoConfig;

    // ####### DILAZIONE #######
    const keyDil = this.GAD_C.DILAZIONE;
    const lblDil = this.GAD_C.LABEL_DILAZIONE;
    const valDil = sd?.flg_dilazione ?? false;
    const dilazione = this.generaIRCD(lblDil, valDil);
    const flgDilazione = this.generaIDCSetFV(form, keyDil, dilazione, o);
    const chkDilazione = formInputs.dilazioneConfig;

    // Richiamo la funzione del servizio per impostare i valori di form e select
    this.setFCCheckBox(flgResMit, chkResMit);
    this.setFCCheckBox(flgAnnullato, chkAnnullato);
    this.setFCCheckBox(flgInvioSpec, chkInvioSpec);
    this.setFCCheckBox(flgAddAnnoSucc, chkAddAnnoSucc);
    this.setFCCheckBox(flgDilazione, chkDilazione);
  }

  /**
   * Funzione di comodo per generare oggetti di tipo: IRiscaCheckboxData.
   * @param id string con l'id dell'oggetto.
   * @param check boolean con il valore della checkbox.
   * @returns IRiscaCheckboxData con la configurazione generata.
   */
  private generaIRCD(id: string, check: boolean): IRiscaCheckboxData {
    // Genero l'oggetto
    const obj: IRiscaCheckboxData = {
      id: id,
      label: id,
      value: check,
      check: check,
    };
    // Ritorno la configurazione
    return obj;
  }

  /**
   * Funzione di comodo che genera un oggetto IDCSetFormValue.
   * @param key string che definisce la chiave di accesso al form control del form group.
   * @param value any con il valore da settare nella form.
   * @param options FormUpdatePropagation con le opzioni di propagazione.
   * @returns IDCSetFormValue con la configurazione generata.
   */
  private generaIDCSetFV(
    form: FormGroup,
    key: string,
    value: any,
    options: FormUpdatePropagation
  ): IDCSetFormValue {
    // Genero l'oggetto
    const obj: IDCSetFormValue = {
      mainForm: form,
      formControlKey: key,
      value,
      options,
    };
    // Ritorno la configurazione
    return obj;
  }

  /**
   * * Funzione che effettua il set dell'informazione per un form control di un form group, date le configurazioni dati.
   * Il set dei dati avverrà per referenza dell'oggetto mainForm.
   * @param dataConfig IDCSetFormValue contenente le informazioni per il set dati.
   * @param chekboxConfig RiscaComponentConfig<RiscaFormInputCheckbox> con le configurazioni della input.
   */
  setFCCheckBox(
    dataConfig: IDCSetFormValue,
    chekboxConfig: RiscaComponentConfig<RiscaFormInputCheckbox>
  ) {
    // Verifico l'input
    if (!dataConfig || !chekboxConfig) {
      // Niente configurazione
      return;
    }

    // Estraggo le informazioni dall'oggetto di configurazione
    const { mainForm: f, value: v, formControlKey: k, options: o } = dataConfig;

    // Aggiorno il valore del form
    this._riscaUtilities.setFormValue(f, k, v, o);
    // Aggiorno il source della configurazione dell'input della checkbox
    this._riscaUtilities.updateRFICheckboxSource(chekboxConfig, v);
  }

  /**
   * Funzione che effettua il set dell'informazione per un form control di un form group, date le configurazioni dati.
   * Il set dei dati avverrà per referenza dell'oggetto mainForm.
   * @param configs IDCSetFormValue contenente le informazioni per il set dati.
   */
  setFCValue(configs: IDCSetFormValue) {
    // Richiamo la funzione di utility
    this._datiContabiliUtility.setFormControlValue(configs);
  }

  /**
   * Funzione che ottiene l'istanza di rinnovo della pratica per lo stato debitorio
   * @param p PraticaVo associata allo stato debitorio
   */
  setIstanzaDiRinnovo(p: PraticaVo): string {
    // Se siamo in inserimento, prendo il dato dalla pratica.
    let provvedimenti = p.provvedimentoIstanza?.filter(
      (pr: IstanzaProvvedimentoVo) => {
        // Prendo solo le istanze
        const isProvv = pr?.id_tipo_provvedimento.flg_istanza == '1';
        // Prendo solo i record di tipo rinnovo
        const rinnovo =
          pr?.id_tipo_provvedimento.cod_tipo_provvedimento ==
          CodiciIstanzaAmbiente.IST_RINNOVO;
        // Prendo i provvedimenti
        return isProvv && rinnovo;
      }
    );

    // Se non ho provvedimenti, ritorno undefined
    if (provvedimenti == undefined || provvedimenti.length == 0) {
      return undefined;
    }

    // Filtro di nuovo i provvedimenti per vedere se ho almeno un provvedimento con data provvedimento
    const provv = this.provvedimentoPiuRecente(provvedimenti);

    // Se non ci sono più filtri, allora ho un'istanza di provvedimento, ma senza una data.
    if (provv.data_provvedimento == undefined) {
      // Ritorna la stringa "N.D."
      return this.ND;
      // #
    }
    // Ritorno la data provvedimento
    return provv.data_provvedimento;
    // #
  }

  /**
   * Funzione di comodo che restituisce il provvedimento più recente partendo dal dato di una pratica.
   * @param pratica PraticaVo dalla quale estrarre il provvedimento.
   * @returns IstanzaProvvedimentoVo estratto o undefined se non trovato.
   */
  private provvedimentoPiuRecentePratica(
    pratica: PraticaVo
  ): IstanzaProvvedimentoVo {
    // Verifico l'input
    if (!pratica) {
      // Niente configurazione
      return undefined;
    }

    // Se siamo in inserimento, prendo il dato dalla pratica.
    let provvedimenti = pratica.provvedimentoIstanza?.filter(
      (pr: IstanzaProvvedimentoVo) => {
        // Prendo solo i provvedimenti
        const isProvv = pr?.id_tipo_provvedimento?.flg_istanza == '0';
        // Controllo se ha la data_provvedimento
        const data =
          pr?.data_provvedimento != undefined &&
          pr?.data_provvedimento.length > 0;
        // Prendo i provvedimenti
        return isProvv && data;
      }
    );

    // Controllo se ho provvedimenti
    if (provvedimenti == undefined || provvedimenti.length == 0) {
      // Se non ne ho, ritorno che non ne ho trovati
      return undefined;
    }

    // Filtro di nuovo i provvedimenti per vedere se ho almeno un provvedimento con data provvedimento
    return this.provvedimentoPiuRecente(provvedimenti);
  }

  /**
   * Funzione di comodo che restituisce il provvedimento più recente in una lista di provvedimenti
   * @param provvedimenti IstanzaProvvedimentoVo[] dalla quale estrarre il provvedimento.
   * @returns IstanzaProvvedimentoVo estratto o undefined se non trovato.
   */
  private provvedimentoPiuRecente(
    provvedimenti: IstanzaProvvedimentoVo[]
  ): IstanzaProvvedimentoVo {
    // Controllo di esistenza
    if (!provvedimenti || provvedimenti.length == 0) {
      return undefined;
    }
    // Ordino
    const provvOrdinati = provvedimenti.sort(
      (a: IstanzaProvvedimentoVo, b: IstanzaProvvedimentoVo) => {
        // Se questa data è undefined, è sempre ultima
        if (a.data_provvedimento == undefined) {
          // L'altro oggetto è maggiore
          return -1;
        }
        // Se questa data è undefined, è sempre ultima
        if (b.data_provvedimento == undefined) {
          // L'altro oggetto è maggiore
          return 1;
        }

        // La data provvedimento qui è una stringa YYYY-MM-DD, quindi può essere ordinata come stringa
        return a.data_provvedimento > b.data_provvedimento ? -1 : 1;
      }
    );

    // Ritorno il provvedimento più recente
    return provvOrdinati[0];
  }

  /**
   * ####################################
   * GESTIONE DELLA MODALE PER L'APERTURA
   * ####################################
   */

  /**
   * Funzione che apre un modale la visualizzazione del dettaglio della dilazione dello stato debitorio.
   * @param statoDebitorio StatoDebitorioVo contenente le informazioni per la visualizzazione del dettaglio dilazione.
   */
  apriDettaglioDilazione(statoDebitorio: StatoDebitorioVo) {
    // Creo la configurazione per aprire la modale
    const configs: IApriModalConfigsForClass = {
      component: DilazioneSDModalComponent,
      options: { windowClass: 'r-mdl-dilazione-sd' },
      params: { statoDebitorio },
    };

    // Creo la classe per le configurazioni
    const modalConfigs = new ApriModalConfigs(configs);
    // Richiamo l'apertura del modale passando le configurazioni
    this._riscaModal.apriModal(modalConfigs);
  }
}

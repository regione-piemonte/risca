import { Injectable } from '@angular/core';
import { FormGroup, ValidatorFn, Validators } from '@angular/forms';
import { LoggerService } from '../../../../core/services/logger.service';
import { RiscaUtilitiesService } from '../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import { FormUpdatePropagation } from '../../../../shared/utilities';
import {
  AppActions,
  IdTipoSoggetto,
} from '../../../../shared/utilities/enums/utilities.enums';
import { DisabledDatiSoggettoPBInsClass } from '../../class/dati-anagrafici/disabled-campi/dati-soggetto/disabled-dati-soggetto.pb.ins.class';
import { DisabledDatiSoggettoPBModClass } from '../../class/dati-anagrafici/disabled-campi/dati-soggetto/disabled-dati-soggetto.pb.mod.class';
import { DisabledDatiSoggettoPFInsClass } from '../../class/dati-anagrafici/disabled-campi/dati-soggetto/disabled-dati-soggetto.pf.ins.class';
import { DisabledDatiSoggettoPFModClass } from '../../class/dati-anagrafici/disabled-campi/dati-soggetto/disabled-dati-soggetto.pf.mod.class';
import { DisabledDatiSoggettoPGInsClass } from '../../class/dati-anagrafici/disabled-campi/dati-soggetto/disabled-dati-soggetto.pg.ins.class';
import { DisabledDatiSoggettoPGModClass } from '../../class/dati-anagrafici/disabled-campi/dati-soggetto/disabled-dati-soggetto.pg.mod.class';
import { DisabledCampiDAClass } from '../../class/dati-anagrafici/disabled-campi/disabled-campi.dati-anagrafici.class';
import { ValidatorContattiInsClass } from '../../class/dati-anagrafici/require-campi/contatti/require-contatti.ins.class';
import { ValidatorContattiModClass } from '../../class/dati-anagrafici/require-campi/contatti/require-contatti.mod.class';
import { ValidatorDatiSoggettoPBInsClass } from '../../class/dati-anagrafici/require-campi/dati-soggetto/require-dati-soggetto.pb.ins.class';
import { ValidatorDatiSoggettoPBModClass } from '../../class/dati-anagrafici/require-campi/dati-soggetto/require-dati-soggetto.pb.mod.class';
import { ValidatorDatiSoggettoPFInsClass } from '../../class/dati-anagrafici/require-campi/dati-soggetto/require-dati-soggetto.pf.ins.class';
import { ValidatorDatiSoggettoPFModClass } from '../../class/dati-anagrafici/require-campi/dati-soggetto/require-dati-soggetto.pf.mod.class';
import { ValidatorDatiSoggettoPGInsClass } from '../../class/dati-anagrafici/require-campi/dati-soggetto/require-dati-soggetto.pg.ins.class';
import { ValidatorDatiSoggettoPGModClass } from '../../class/dati-anagrafici/require-campi/dati-soggetto/require-dati-soggetto.pg.mod.class';
import { ValidatorRecapitiInsClass } from '../../class/dati-anagrafici/require-campi/recapiti/require-recapiti.ins.class';
import { ValidatorRecapitiModClass } from '../../class/dati-anagrafici/require-campi/recapiti/require-recapiti.mod.class';
import { ValidatorCampiDAClass } from '../../class/dati-anagrafici/require-campi/require-campi.dati-anagrafici.class';

/**
 * Interfaccia di comodo che gestisce le informazioni per la generazione dei validatori dei campi.
 */
interface IValidatorConfigs {
  [key: string]: ValidatorCampiDAClass;
}

/*
 * Interfaccia di comodo che gestisce le informazioni per la generazione dei disabler dei campi.
 */
interface IDisabledConfigs {
  [key: string]: DisabledCampiDAClass;
}

/**
 * Interfaccia di comodo definisce tutti i validatori per i campi.
 */
interface IValidatorList {
  [key: string]: Validators[];
}

/**
 * Interfaccia di comodo che gestisce una mappa per la creazione rapida di oggetti IValidatorConfigs.
 */
interface IMapAzReqConf {
  i: ValidatorCampiDAClass;
  m: ValidatorCampiDAClass;
}

/**
 * Interfaccia di comodo che gestisce una mappa per la creazione rapida di oggetti DisabledCampiDAClass.
 */
interface IMapAzDisConf {
  i: DisabledCampiDAClass;
  m: DisabledCampiDAClass;
}

/**
 * Esistono 6 tipi di configurazione per ogni blocco dei dati anagrafici, che definiscono il campo required.
 * Le configurazioni si dividono per 3 blocci:
 * - Dati soggetto;
 * - Recapiti;
 * - Contatti;
 * Per ogni blocco esiste una definizione basata sul tipo soggetto:
 * - PF - Persona Fisica
 * - PG - Persona Giuridica Privata
 * - PB - Persona Giuridica Pubblica
 * A sua volta, ogni tipo soggetto deve gestire una differente modalità d'inserimento:
 * - Aggiunta;
 * - Modifica;
 * I casi in totale sono 18, attualmente non ci sono configurazioni lato server, per cui il mapping è manuale.
 * In caso di modifiche future, sarà necessvRIo aggiornare il servizio.
 */
@Injectable({
  providedIn: 'root',
})
export class GestioneFormsDatiAnagraficiService {
  // ##### DATI SOGGETTO
  /** ValidatorDatiSoggettoPFInsClass contenente la mappatura chiave, valore per i dati soggetto. PF e inserimento. */
  private vDSPFI: ValidatorDatiSoggettoPFInsClass;
  /** ValidatorDatiSoggettoPFModClass contenente la mappatura chiave, valore per i dati soggetto. PF e modifica. */
  private vDSPFM: ValidatorDatiSoggettoPFModClass;
  /** ValidatorDatiSoggettoPGInsClass contenente la mappatura chiave, valore per i dati soggetto. PG e inserimento. */
  private vDSPGI: ValidatorDatiSoggettoPGInsClass;
  /** ValidatorDatiSoggettoPGModClass contenente la mappatura chiave, valore per i dati soggetto. PG e modifica. */
  private vDSPGM: ValidatorDatiSoggettoPGModClass;
  /** ValidatorDatiSoggettoPBInsClass contenente la mappatura chiave, valore per i dati soggetto. PB e inserimento. */
  private vDSPBI: ValidatorDatiSoggettoPBInsClass;
  /** ValidatorDatiSoggettoPBModClass contenente la mappatura chiave, valore per i dati soggetto. PB e modifica. */
  private vDSPBM: ValidatorDatiSoggettoPBModClass;

  /** Oggetto IValidatorConfigs contenente le configurazioni per per la gestione dati soggetto di: PF. */
  private vCDSPF: IValidatorConfigs = {};
  /** Oggetto IValidatorConfigs contenente le configurazioni per per la gestione dati soggetto di: PG. */
  private vCDSPG: IValidatorConfigs = {};
  /** Oggetto IValidatorConfigs contenente le configurazioni per per la gestione dati soggetto di: PB. */
  private vCDSPB: IValidatorConfigs = {};

  /** DisabledDatiSoggettoPFInsClass contenente la mappatura chiave, valore per i dati soggetto. PF e inserimento. */
  private dDSPFI: DisabledDatiSoggettoPFInsClass;
  /** DisabledDatiSoggettoPFModClass contenente la mappatura chiave, valore per i dati soggetto. PF e modifica. */
  private dDSPFM: DisabledDatiSoggettoPFModClass;
  /** DisabledDatiSoggettoPGInsClass contenente la mappatura chiave, valore per i dati soggetto. PG e inserimento. */
  private dDSPGI: DisabledDatiSoggettoPGInsClass;
  /** DisabledDatiSoggettoPGModClass contenente la mappatura chiave, valore per i dati soggetto. PG e modifica. */
  private dDSPGM: DisabledDatiSoggettoPGModClass;
  /** DisabledDatiSoggettoPBInsClass contenente la mappatura chiave, valore per i dati soggetto. PB e inserimento. */
  private dDSPBI: DisabledDatiSoggettoPBInsClass;
  /** DisabledDatiSoggettoPBModClass contenente la mappatura chiave, valore per i dati soggetto. PB e modifica. */
  private dDSPBM: DisabledDatiSoggettoPBModClass;

  /** Oggetto IDisabledConfigs contenente le configurazioni per per la gestione dati soggetto di: PF. */
  private dCDSPF: IDisabledConfigs = {};
  /** Oggetto IDisabledConfigs contenente le configurazioni per per la gestione dati soggetto di: PG. */
  private dCDSPG: IDisabledConfigs = {};
  /** Oggetto IDisabledConfigs contenente le configurazioni per per la gestione dati soggetto di: PB. */
  private dCDSPB: IDisabledConfigs = {};

  // ##### RECAPITI
  // /** ValidatorRecapitiInsClass contenente la mappatura chiave, valore per i recapiti. Inserimento. */
  private vRI: ValidatorRecapitiInsClass;
  // /** ValidatorRecapitiModClass contenente la mappatura chiave, valore per i recapiti. Modifica. */
  private vRM: ValidatorRecapitiModClass;

  /** Oggetto IValidatorConfigs contenente le configurazioni per per la gestione recapiti. */
  private vCR: IValidatorConfigs = {};

  // ##### CONTATTI
  // /** ValidatorContattiInsClass contenente la mappatura chiave, valore per i contatti. Inserimento. */
  private vCI: ValidatorContattiInsClass;
  // /** ValidatorContattiModClass contenente la mappatura chiave, valore per i contatti. Modifica. */
  private vCM: ValidatorContattiModClass;

  /** Oggetto IValidatorConfigs contenente le configurazioni per per la gestione recapiti. */
  private vCC: IValidatorConfigs = {};

  /**
   * Costruttore
   */
  constructor(
    private _logger: LoggerService,
    private _riscaUtilities: RiscaUtilitiesService
  ) {
    // Lancio il setup delle mappe di require
    this.setupRequired();
  }

  /**
   * ###################################
   * MAPPE DI ACCESSO AI DATI ANAGRAFICI
   * ###################################
   */

  /**
   * Funzione che lancia il setup delle mappe per gli accessi ai campi delle parti dei dati anagrafici.
   */
  private setupRequired() {
    // Setup per i dati soggetto
    this.setupMapValidatorsDatiSoggetto();
    // Setup per i dati soggetto
    this.setupMapDisablerDatiSoggetto();
    // Setup per i recapiti
    this.setupMapValidatorsRecapiti();
    // Setup per i contatti
    this.setupMapValidatorsContatti();
  }

  /**
   * Funzione di comodo per il setup delle azioni/configurazioni dei require.
   * @param s IValidatorConfigs | IDisabledConfigs source da valorizzare.
   * @param c IMapAzReqConf | IMapAzDisConf mapping per la valorizzazione.
   */
  private setActionConfg(
    s: IValidatorConfigs | IDisabledConfigs,
    c: IMapAzReqConf | IMapAzDisConf
  ) {
    // Verifico l'input
    if (!s || !c) return;

    // Aggiungo al source le proprietà
    s[AppActions.inserimento] = c.i;
    s[AppActions.modifica] = c.m;
  }

  /**
   * Funzione di setup per la mappa: dati soggetto; per i validators.
   */
  setupMapValidatorsDatiSoggetto() {
    // Dati soggetto, PF e inserimento
    this.vDSPFI = new ValidatorDatiSoggettoPFInsClass();
    // Dati soggetto, PF e modifica
    this.vDSPFM = new ValidatorDatiSoggettoPFModClass();
    // Dati soggetto, PG e inserimento
    this.vDSPGI = new ValidatorDatiSoggettoPGInsClass();
    // Dati soggetto, PG e modifica
    this.vDSPGM = new ValidatorDatiSoggettoPGModClass();
    // Dati soggetto, PB e inserimento
    this.vDSPBI = new ValidatorDatiSoggettoPBInsClass();
    // Dati soggetto, PB e modifica
    this.vDSPBM = new ValidatorDatiSoggettoPBModClass();

    // Setup delle configurazioni azione/configurazione
    this.setActionConfg(this.vCDSPF, { i: this.vDSPFI, m: this.vDSPFM });
    this.setActionConfg(this.vCDSPG, { i: this.vDSPGI, m: this.vDSPGM });
    this.setActionConfg(this.vCDSPB, { i: this.vDSPBI, m: this.vDSPBM });
  }

  /**
   * Funzione di setup per la mappa: dati soggetto; per i disabled.
   */
  setupMapDisablerDatiSoggetto() {
    // Dati soggetto, PF e inserimento
    this.dDSPFI = new DisabledDatiSoggettoPFInsClass();
    // Dati soggetto, PF e modifica
    this.dDSPFM = new DisabledDatiSoggettoPFModClass();
    // Dati soggetto, PG e inserimento
    this.dDSPGI = new DisabledDatiSoggettoPGInsClass();
    // Dati soggetto, PG e modifica
    this.dDSPGM = new DisabledDatiSoggettoPGModClass();
    // Dati soggetto, PB e inserimento
    this.dDSPBI = new DisabledDatiSoggettoPBInsClass();
    // Dati soggetto, PB e modifica
    this.dDSPBM = new DisabledDatiSoggettoPBModClass();

    // Setup delle configurazioni azione/configurazione
    this.setActionConfg(this.dCDSPF, { i: this.dDSPFI, m: this.dDSPFM });
    this.setActionConfg(this.dCDSPG, { i: this.dDSPGI, m: this.dDSPGM });
    this.setActionConfg(this.dCDSPB, { i: this.dDSPBI, m: this.dDSPBM });
  }

  /**
   * Funzione di setup per la mappa: recapiti; per i validators.
   */
  setupMapValidatorsRecapiti() {
    // Recapiti e inserimento
    this.vRI = new ValidatorRecapitiInsClass();
    // Recapiti e modifica
    this.vRM = new ValidatorRecapitiModClass();

    // Setup delle configurazioni azione/configurazione
    this.setActionConfg(this.vCR, { i: this.vRI, m: this.vRM });
  }

  /**
   * Funzione di setup per la mappa: contatti; per i validators.
   */
  setupMapValidatorsContatti() {
    // Contatti e inserimento
    this.vCI = new ValidatorContattiInsClass();
    // Contatti e modifica
    this.vCM = new ValidatorContattiModClass();

    // Setup delle configurazioni azione/configurazione
    this.setActionConfg(this.vCC, { i: this.vCI, m: this.vCM });
  }

  /**
   * ##########################
   * FUNZIONI DATI DEL SOGGETTO
   * ##########################
   */

  // ############ SETTER ############

  /**
   * Funzione che gestisce i validatori dei campi in base a:
   * - Id tipo soggetto;
   * - Modalità di gestione del componente.
   * @param f FormGroup da gestire.
   * @param idts number che definisce l'id tipo soggetto.
   * @param a AppActions che definisce il tipo d'azione compiuta sui dati.
   * @param u boolean che definisce se è necessvRIo lanciare il refresh dei valori e dei controlli. Default è false.
   * @param uC FormUpdatePropagation che definisce le configurazioni per il refresh dati e controlli.
   */
  setValidatorsDatiSoggetto(
    f: FormGroup,
    idts: number,
    a: AppActions,
    u = false,
    uC: FormUpdatePropagation = { onlySelf: true, emitEvent: false }
  ) {
    // Verifico l'input
    if (!f || idts === undefined || !a) return;

    // Verifico l'id del tipo soggetto
    switch (idts) {
      case IdTipoSoggetto.PF:
        this.setValidatorsByAction(f, a, this.vCDSPF, u, uC);
        break;
      case IdTipoSoggetto.PG:
        this.setValidatorsByAction(f, a, this.vCDSPG, u, uC);
        break;
      case IdTipoSoggetto.PB:
        this.setValidatorsByAction(f, a, this.vCDSPB, u, uC);
        break;
    }
  }

  /**
   * Funzione che gestisce i validatori del campo in input, in base a:
   * - Id tipo soggetto;
   * - Modalità di gestione del componente.
   * @param f FormGroup da gestire.
   * @param field string che identifica il FormControl da settare.
   * @param idts number che definisce l'id tipo soggetto.
   * @param a AppActions che definisce il tipo d'azione compiuta sui dati.
   * @param u boolean che definisce se è necessvRIo lanciare il refresh dei valori e dei controlli. Default è false.
   * @param uC FormUpdatePropagation che definisce le configurazioni per il refresh dati e controlli.
   */
  setValidatorCampoDatiSoggetto(
    f: FormGroup,
    field: string,
    idts: number,
    a: AppActions,
    u = false,
    uC?: FormUpdatePropagation
  ) {
    // Verifico l'input
    if (!f || !field || idts === undefined || !a) return;

    // Verifico l'id del tipo soggetto
    switch (idts) {
      case IdTipoSoggetto.PF:
        this.setValidatorByAction(f, field, a, this.vCDSPF, u, uC);
        break;
      case IdTipoSoggetto.PG:
        this.setValidatorByAction(f, field, a, this.vCDSPG, u, uC);
        break;
      case IdTipoSoggetto.PB:
        this.setValidatorByAction(f, field, a, this.vCDSPB, u, uC);
        break;
    }
  }

  /**
   * Funzione che gestisce i disabled dei campi in base a:
   * - Id tipo soggetto;
   * - Modalità di gestione del componente.
   * @param f FormGroup da gestire.
   * @param idts number che definisce l'id tipo soggetto.
   * @param a AppActions che definisce il tipo d'azione compiuta sui dati.
   * @param uC FormUpdatePropagation che definisce le configurazioni per la propagazione del disabled.
   */
  setDisabledDatiSoggetto(
    f: FormGroup,
    idts: number,
    a: AppActions,
    uC: FormUpdatePropagation = { onlySelf: true, emitEvent: false }
  ) {
    // Verifico l'input
    if (!f || idts === undefined || !a) return;

    // Verifico l'id del tipo soggetto
    switch (idts) {
      case IdTipoSoggetto.PF:
        this.setDisabledByAction(f, a, this.dCDSPF, uC);
        break;
      case IdTipoSoggetto.PG:
        this.setDisabledByAction(f, a, this.dCDSPG, uC);
        break;
      case IdTipoSoggetto.PB:
        this.setDisabledByAction(f, a, this.dCDSPB, uC);
        break;
    }
  }

  // ############ GETTER ############

  /**
   * Funzione che recupera i validatori dei campi in base a:
   * - Id tipo soggetto;
   * - Modalità di gestione del componente.
   * @param idts number che definisce l'id tipo soggetto.
   * @param a AppActions che definisce il tipo d'azione compiuta sui dati.
   * @returns IValidatorList contenente una coppia: nome campo form/lista di validatori.
   */
  getValidatorsDatiSoggetto(idts: number, a: AppActions): IValidatorList {
    // Verifico l'input
    if (idts === undefined || !a) return {};

    // Verifico l'id del tipo soggetto
    switch (idts) {
      case IdTipoSoggetto.PF:
        return this.getValidatorsByAction(a, this.vCDSPF);
      case IdTipoSoggetto.PG:
        return this.getValidatorsByAction(a, this.vCDSPG);
      case IdTipoSoggetto.PB:
        return this.getValidatorsByAction(a, this.vCDSPB);
      default:
        return {};
    }
  }

  /**
   * Funzione che ritorna i validatori del campo in input, in base a:
   * - Id tipo soggetto;
   * - Modalità di gestione del componente.
   * @param field string che identifica il FormControl da recuperare.
   * @param idts number che definisce l'id tipo soggetto.
   * @param a AppActions che definisce il tipo d'azione compiuta sui dati.
   * @returns Array di ValidatorFn contenente la lista dei validatori per il campo.
   */
  getValidatorCampoDatiSoggetto(
    field: string,
    idts: number,
    a: AppActions
  ): ValidatorFn[] {
    // Verifico l'input
    if (!field || idts === undefined || !a) return [];

    // Verifico l'id del tipo soggetto
    switch (idts) {
      case IdTipoSoggetto.PF:
        return this.getValidatorByAction(field, a, this.vCDSPF);
      case IdTipoSoggetto.PG:
        return this.getValidatorByAction(field, a, this.vCDSPG);
      case IdTipoSoggetto.PB:
        return this.getValidatorByAction(field, a, this.vCDSPB);
      default:
        return [];
    }
  }

  /**
   * #################
   * FUNZIONI RECAPITI
   * #################
   */

  // ############ SETTER ############

  /**
   * Funzione che gestisce i validatori dei campi in base a:
   * - Modalità di gestione del componente.
   * @param f FormGroup da gestire.
   * @param a AppActions che definisce il tipo d'azione compiuta sui dati.
   * @param u boolean che definisce se è necessvRIo lanciare il refresh dei valori e dei controlli. Default è false.
   * @param uC FormUpdatePropagation che definisce le configurazioni per il refresh dati e controlli.
   */
  setValidatorsRecapiti(
    f: FormGroup,
    a: AppActions,
    u = false,
    uC?: FormUpdatePropagation
  ) {
    // Verifico l'input
    if (!f || !a) {
      return;
    }

    // Richiamo la funzione di gestione action
    this.setValidatorsByAction(f, a, this.vCR, u, uC);
  }

  /**
   * Funzione che gestisce i validatori del campo in input, in base a:
   * - Modalità di gestione del componente.
   * @param f FormGroup da gestire.
   * @param field string che identifica il FormControl da settare.
   * @param a AppActions che definisce il tipo d'azione compiuta sui dati.
   * @param u boolean che definisce se è necessvRIo lanciare il refresh dei valori e dei controlli. Default è false.
   * @param uC FormUpdatePropagation che definisce le configurazioni per il refresh dati e controlli.
   */
  setValidatorCampoRecapiti(
    f: FormGroup,
    field: string,
    a: AppActions,
    u = false,
    uC?: FormUpdatePropagation
  ) {
    // Verifico l'input
    if (!f || !field || !a) {
      return;
    }

    // Richiamo la funzione di gestione action
    this.setValidatorByAction(f, field, a, this.vCR, u, uC);
  }

  // ############ GETTER ############

  /**
   * Funzione che ritorna i validatori dei campi in base a:
   * - Modalità di gestione del componente.
   * @param a AppActions che definisce il tipo d'azione compiuta sui dati.
   * @returns IValidatorList contenente una coppia: nome campo form/lista di validatori.
   */
  getValidatorsRecapiti(a: AppActions): IValidatorList {
    // Verifico l'input
    if (!a) {
      return {};
    }

    // Richiamo la funzione di gestione action
    return this.getValidatorsByAction(a, this.vCR);
  }

  /**
   * Funzione che ritorna i validatori del campo in input, in base a:
   * - Modalità di gestione del componente.
   * @param field string che identifica il FormControl da recuperare.
   * @param a AppActions che definisce il tipo d'azione compiuta sui dati.
   * @returns Array di ValidatorFn contenente la lista dei validatori per il campo.
   */
  getValidatorCampoRecapiti(field: string, a: AppActions): ValidatorFn[] {
    // Verifico l'input
    if (!field || !a) {
      return;
    }

    // Richiamo la funzione di gestione action
    return this.getValidatorByAction(field, a, this.vCR);
  }

  /**
   * #################
   * FUNZIONI CONTATTI
   * #################
   */

  // ############ SETTER ############

  /**
   * Funzione che gestisce i validatori dei campi in base a:
   * - Modalità di gestione del componente.
   * @param f FormGroup da gestire.
   * @param a AppActions che definisce il tipo d'azione compiuta sui dati.
   * @param u boolean che definisce se è necessvRIo lanciare il refresh dei valori e dei controlli. Default è false.
   * @param uC FormUpdatePropagation che definisce le configurazioni per il refresh dati e controlli.
   */
  setValidatorsContatti(
    f: FormGroup,
    a: AppActions,
    u = false,
    uC?: FormUpdatePropagation
  ) {
    // Verifico l'input
    if (!f || !a) {
      return;
    }

    // Richiamo la funzione di gestione action
    this.setValidatorsByAction(f, a, this.vCC, u, uC);
  }

  /**
   * Funzione che gestisce i validatori del campo in input, in base a:
   * - Modalità di gestione del componente.
   * @param f FormGroup da gestire.
   * @param field string che identifica il FormControl da settare.
   * @param a AppActions che definisce il tipo d'azione compiuta sui dati.
   * @param u boolean che definisce se è necessvRIo lanciare il refresh dei valori e dei controlli. Default è false.
   * @param uC FormUpdatePropagation che definisce le configurazioni per il refresh dati e controlli.
   */
  setValidatorCampoContatti(
    f: FormGroup,
    field: string,
    a: AppActions,
    u = false,
    uC?: FormUpdatePropagation
  ) {
    // Verifico l'input
    if (!f || !field || !a) {
      return;
    }

    // Richiamo la funzione di gestione action
    this.setValidatorByAction(f, field, a, this.vCC, u, uC);
  }

  // ############ GETTER ############

  /**
   * Funzione che ritorna i validatori dei campi in base a:
   * - Modalità di gestione del componente.
   * @param a AppActions che definisce il tipo d'azione compiuta sui dati.
   * @returns IValidatorList contenente una coppia: nome campo form/lista di validatori.
   */
  getValidatorsContatti(a: AppActions): IValidatorList {
    // Verifico l'input
    if (!a) {
      return {};
    }

    // Richiamo la funzione di gestione action
    return this.getValidatorsByAction(a, this.vCC);
  }

  /**
   * Funzione che ritorna i validatori del campo in input, in base a:
   * - Modalità di gestione del componente.
   * @param field string che identifica il FormControl da recuperare.
   * @param a AppActions che definisce il tipo d'azione compiuta sui dati.
   * @returns Array di ValidatorFn contenente la lista dei validatori per il campo.
   */
  getValidatorCampoContatti(field: string, a: AppActions): ValidatorFn[] {
    // Verifico l'input
    if (!field || !a) {
      return;
    }

    // Richiamo la funzione di gestione action
    return this.getValidatorByAction(field, a, this.vCC);
  }

  /**
   * ############################
   * FUNZIONI COMUNI DI SETTAGGIO
   * ############################
   */

  /**
   * VALIDO PER TUTTI I CAMPI DELL'OGGETTO VALIDATORE.
   * Funzione che gestisce il set dei validatori per il form group, dato il tipo di azione e un oggetto composto dalle configurazioni dei campi.
   * L'oggetto di configurazione deve avere delle proprietà con lo stesso nome del tipo d'azione.
   * @param f FormGroup da gestire.
   * @param a AppActions azione da gestire.
   * @param s IValidatorConfigs oggetto con i source per le configurazioni.
   * @param u boolean che definisce l'attivazione del refresh dati e del refresh dei validatori.
   * @param uC FormUpdatePropagation che definisce le configurazioni per il refresh dati e controlli.
   */
  setValidatorsByAction(
    f: FormGroup,
    a: AppActions,
    s: IValidatorConfigs,
    u: boolean,
    uC?: FormUpdatePropagation
  ) {
    // Verifico l'input
    if (!f || !a || !s) {
      return;
    }

    // Recupero l'oggetto per le configurazioni
    const validator = s[a];
    // Lancio il set dei validatori
    this.setValidatorsForFields(f, validator, u, uC);
  }

  /**
   * VALIDO PER TUTTI I CAMPI DELL'OGGETTO DISABLED.
   * Funzione che gestisce il set dei disabled per il form group, dato il tipo di azione e un oggetto composto dalle configurazioni dei campi.
   * L'oggetto di configurazione deve avere delle proprietà con lo stesso nome del tipo d'azione.
   * @param f FormGroup da gestire.
   * @param a AppActions azione da gestire.
   * @param s IDisabledConfigs oggetto con i source per le configurazioni.
   * @param uC FormUpdatePropagation che definisce le configurazioni per la propagazione del disabled.
   */
  setDisabledByAction(
    f: FormGroup,
    a: AppActions,
    s: IDisabledConfigs,
    uC?: FormUpdatePropagation
  ) {
    // Verifico l'input
    if (!f || !a || !s) {
      return;
    }

    // Recupero l'oggetto per le configurazioni
    const disabled = s[a];
    // Lancio il set dei validatori
    this.setDisabledForFields(f, disabled, uC);
  }

  /**
   * VALIDO PER UN SINGOLO CAMPO.
   * Funzione che gestisce il set dei validatori per il form group, dato il tipo di azione e un oggetto composto dalle configurazioni dei campi.
   * L'oggetto di configurazione deve avere delle proprietà con lo stesso nome del tipo d'azione.
   * @param f FormGroup da gestire.
   * @param field string che identifica il FormControl da settare.
   * @param a AppActions azione da gestire.
   * @param s IValidatorConfigs oggetto con i source per le configurazioni.
   * @param u boolean che definisce l'attivazione del refresh dati e del refresh dei validatori.
   * @param uC FormUpdatePropagation che definisce le configurazioni per il refresh dati e controlli.
   */
  setValidatorByAction(
    f: FormGroup,
    field: string,
    a: AppActions,
    s: IValidatorConfigs,
    u: boolean,
    uC?: FormUpdatePropagation
  ) {
    // Verifico l'input
    if (!f || !field || !a || !s) {
      return;
    }

    // Recupero l'oggetto per le configurazioni
    const validator = s[a];
    // Lancio il set dei validatori
    this.setValidatorForField(f, field, validator, u, uC);
  }

  /**
   * VALIDO PER TUTTI I CAMPI DELL'OGGETTO VALIDATORE.
   * Funzione che gestisce il get dei validatori per tutti i campi di un form.
   * L'oggetto di configurazione deve avere delle proprietà con lo stesso nome del tipo d'azione.
   * @param a AppActions azione da gestire.
   * @param s IValidatorConfigs oggetto con i source per le configurazioni.
   * @returns IValidatorList contenente una coppia: nome campo form/lista di validatori.
   */
  getValidatorsByAction(a: AppActions, s: IValidatorConfigs): IValidatorList {
    // Verifico l'input
    if (!a || !s) {
      return {};
    }

    // Recupero l'oggetto per le configurazioni
    const validator = s[a];
    // Ritorno la lista dei validatori
    return this.getValidatorsForFields(validator);
  }

  /**
   * VALIDO PER UN SINGOLO CAMPO.
   * Funzione che gestisce il get dei validatori per di un determinato campo.
   * L'oggetto di configurazione deve avere delle proprietà con lo stesso nome del tipo d'azione.
   * @param field string che identifica il FormControl da settare.
   * @param a AppActions azione da gestire.
   * @param s IValidatorConfigs oggetto con i source per le configurazioni.
   * @returns Array di ValidatorFn contenente la lista dei validatori per il campo.
   */
  getValidatorByAction(
    field: string,
    a: AppActions,
    s: IValidatorConfigs
  ): ValidatorFn[] {
    // Verifico l'input
    if (!field || !a || !s) {
      return [];
    }

    // Recupero l'oggetto per le configurazioni
    const validator = s[a];
    // Ritorno la lista dei validatori
    return this.getValidatorForField(field, validator);
  }

  /**
   * VALIDO PER TUTTI I CAMPI DELL'OGGETTO VALIDATORE.
   * Funzione che gestisce il set dei campi required per il form group, data una configurazione.
   * @param f FormGroup da gestire.
   * @param validator ValidatorCampiDAClass con la configurazione dei campi per il form.
   * @param u boolean che definisce l'attivazione del refresh dati e del refresh dei validatori.
   * @param uC FormUpdatePropagation che definisce le configurazioni per il refresh dati e controlli.
   */
  setValidatorsForFields(
    f: FormGroup,
    validator: ValidatorCampiDAClass,
    u: boolean,
    uC?: FormUpdatePropagation
  ) {
    // Verifico l'input
    if (!f || !validator) {
      return;
    }

    // Ciclo le proprietà nell'oggetto
    for (let [field] of Object.entries(validator.config)) {
      // Lancio la funzione di setup dei validatori
      this.setValidatorForField(f, field, validator, u, uC);
    }
  }

  /**
   * VALIDO PER TUTTI I CAMPI DELL'OGGETTO DISABLED.
   * Funzione che gestisce il disable dei campi per il form group, data una configurazione.
   * @param f FormGroup da gestire.
   * @param disabler DisabledCampiDAClass con la configurazione dei campi per il form.
   * @param uC FormUpdatePropagation che definisce le configurazioni per la propagazione del disabled.
   */
  setDisabledForFields(
    f: FormGroup,
    disabler: DisabledCampiDAClass,
    uC?: FormUpdatePropagation
  ) {
    // Verifico l'input
    if (!f || !disabler) {
      return;
    }

    // Ciclo le proprietà nell'oggetto
    for (let [field] of Object.entries(disabler.config)) {
      // Lancio la funzione di setup dei disabled
      this.setDisableForField(f, field, disabler, uC);
    }
  }

  /**
   * VALIDO PER UN SINGOLO CAMPO.
   * Funzione che gestisce il set dei validatori per il form group, data una configurazione.
   * @param f FormGroup da gestire.
   * @param field string che identifica il FormControl da settare.
   * @param validator ValidatorCampiDAClass con la configurazione dei campi per il form.
   * @param u boolean che definisce l'attivazione del refresh dati e del refresh dei validatori.
   * @param uC FormUpdatePropagation che definisce le configurazioni per il refresh dati e controlli.
   */
  setValidatorForField(
    f: FormGroup,
    field: string,
    validator: ValidatorCampiDAClass,
    u: boolean,
    uC?: FormUpdatePropagation
  ) {
    // Verifico l'input
    if (!f || !field || !validator) {
      return;
    }

    // Recupero i validatori per il campo
    const validatori = this.getValidatorForField(field, validator);

    // Aggiorno i validatori
    this._riscaUtilities.setFieldValidator(f, field, validatori, u, uC);
  }

  /**
   * VALIDO PER UN SINGOLO CAMPO.
   * Funzione che gestisce il set dei disabled per il form group, data una configurazione.
   * @param f FormGroup da gestire.
   * @param field string che identifica il FormControl da settare.
   * @param disabler ValidatorCampiDAClass con la configurazione dei campi per il form.
   * @param uC FormUpdatePropagation che definisce le configurazioni per la propagazione del disabled.
   */
  setDisableForField(
    f: FormGroup,
    field: string,
    disabler: DisabledCampiDAClass,
    uC?: FormUpdatePropagation
  ) {
    // Verifico l'input
    if (!f || !field || !disabler) {
      return;
    }

    // Recupero la configurazione per il disabled
    const disabled = this.getDisabledForField(field, disabler);
    // Verifico se il campo è da disabilitare
    if (disabled) {
      // Disabilito il campo
      f.get(field).disable(uC);
    }
    // Abilito il campo
    else {
      f.get(field).enable(uC);
    }
  }

  /**
   * VALIDO PER TUTTI I CAMPI DELL'OGGETTO VALIDATORE.
   * Funzione che gestisce il get dei validatori, data la configurazione.
   * @param validator ValidatorCampiDAClass con la configurazione dei campi per il form.
   * @returns IValidatorList con i validatori per ogni proprietà della configurazione.
   */
  getValidatorsForFields(validator: ValidatorCampiDAClass): IValidatorList {
    // Verifico l'input
    if (!validator) {
      return {};
    }

    // Creo un oggetto per i validatori
    const validatori: IValidatorList = {};

    // Ciclo le proprietà nell'oggetto
    for (let [field] of Object.entries(validator.config)) {
      // Recupero i validatori per il campo
      const validatoriCampo = this.getValidatorForField(field, validator);
      // Definisco i validatori per il campo all'interno dell'oggetto
      validatori[field] = validatoriCampo;
    }

    // Ritorno l'oggetto
    return validatori;
  }

  /**
   * VALIDO PER UN SINGOLO CAMPO.
   * Funzione che ritorna tutti i validatori di un determinato campo.
   * @param field string che identifica il FormControl da settare.
   * @param validator ValidatorCampiDAClass con la configurazione dei campi per il form.
   * @returns Array di ValidatorFn definite per un campo.
   */
  getValidatorForField(
    field: string,
    validator: ValidatorCampiDAClass
  ): ValidatorFn[] {
    // Verifico l'input
    if (!field || !validator) {
      return [];
    }

    // Per evitare errori bloccanti di programmazione, gestisco le logiche in un try catch
    try {
      // Dalla configurazione dei validatori, recupero quello richiesto
      const fieldData = validator.config[field];
      // Verifico se esiste la configurazione
      if (!fieldData) {
        return;
      }
      // Creo un array di validatori
      let validatori = [];
      // Recupero l'oggetto dei campi required
      const required = fieldData.isRequired;
      // Recupero i possibili validatori del campo
      const otherValidators = fieldData.otherValidators || [];
      // Concateno i validatori
      validatori = validatori.concat(otherValidators);

      // Verifico se il campo è richiesto
      if (required) {
        // Imposto per il FormControl il validatore required
        validatori = validatori.concat([Validators.required]);
      }

      // Ritorno la lista di validatori
      return validatori;
      // #
    } catch (e) {
      // Loggo un warning
      this._logger.warning('getValidatorForField failed', { e, validator });
      // Ritorno una lista vuota
      return [];
    }
  }

  /**
   * VALIDO PER UN SINGOLO CAMPO.
   * Funzione che ritorna se il campo è da disabilitare.
   * @param field string che identifica il FormControl da settare.
   * @param disabler DisabledCampiDAClass con la configurazione dei campi per il form.
   * @returns boolean per il disable definito per un campo.
   */
  getDisabledForField(field: string, disabler: DisabledCampiDAClass): boolean {
    // Verifico l'input
    if (!field || !disabler) {
      return false;
    }

    // Per evitare errori bloccanti di programmazione, gestisco le logiche in un try catch
    try {
      // Dalla configurazione dei validatori, recupero quello richiesto
      const fieldData = disabler.config[field];
      // Verifico se esiste la configurazione
      if (fieldData === undefined) {
        return false;
      }

      // Ritorno lo stato disabled
      return fieldData.disabled || false;
      // #
    } catch (e) {
      // Loggo un warning
      this._logger.warning('getDisabledForField failed', { e, disabler });
      // Ritorno false
      return false;
    }
  }
}

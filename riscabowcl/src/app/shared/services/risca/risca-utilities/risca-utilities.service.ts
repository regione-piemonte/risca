import { HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {
  AbstractControl,
  FormControl,
  FormGroup,
  ValidatorFn,
} from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { cloneDeep, isEqual } from 'lodash';
import * as moment from 'moment';
import { Moment } from 'moment';
import { Observable, of } from 'rxjs';
import { delay, tap } from 'rxjs/operators';
import { RiscaHttpParamEncoder } from '../../../../core/classes/http-helper/http-params-encoder.class';
import { Gruppo } from '../../../../core/commons/vo/gruppo-vo';
import { IndirizzoSpedizioneVo } from '../../../../core/commons/vo/indirizzo-spedizione-vo';
import { PraticaVo } from '../../../../core/commons/vo/pratica-vo';
import { RecapitoVo } from '../../../../core/commons/vo/recapito-vo';
import { SoggettoVo } from '../../../../core/commons/vo/soggetto-vo';
import { StatoDebitorioVo } from '../../../../core/commons/vo/stato-debitorio-vo';
import { VerificaPraticheStDebitoriVo } from '../../../../core/commons/vo/verifica-pratiche-stati-deboitori-vo';
import { RiscaServicesSortTypes } from '../../../../core/services/http-helper/utilities/http-helper.enums';
import { LoggerService } from '../../../../core/services/logger.service';
import {
  DynamicObjAny,
  DynamicObjString,
  FormUpdatePropagation,
  IParseValueRules,
  IResultVerifyRStDDettaglio,
  IRiscaAnnoSelect,
  IRiscaCheckboxData,
  IRiscaServerErrorDetail,
  IRiscaTabChanges,
  IRiscaTablePagination,
  IRulesQueryParams,
  IVerifyComponeteDt,
  MappaErroriFormECodici,
  RiscaComponentConfig,
  RiscaDatepickerConfig,
  RiscaFormatoDate,
  RiscaFormInputCheckbox,
  RiscaServerError,
  RiscaServerErrorInfo,
  RiscaServerStatus,
  RiscaSortTypes,
  RiscaTablePagination,
  TipiIstanzaProvvedimento,
  TNIPFormData,
  TRiscaServerBoolean,
  VerificaPStDInfo,
} from '../../../utilities';
import { RiscaNotifyCodes } from '../../../utilities/enums/risca-notify-codes.enums';
import { RiscaSpinnerService } from '../../risca-spinner.service';
import { RiscaMessagesService } from '../risca-messages.service';
import {
  addDesSelectDataFineVal,
  arrotondaDifetto,
  arrotondaEccesso,
  clearFileName,
  controllaPartitaIVA,
  convertMomentToISOString,
  convertMomentToNgbDateStruct,
  convertNgbDateStructToDateString,
  convertNgbDateStructToMoment,
  convertNgbDateStructToServerDate,
  convertNgbDateStructToString,
  convertNgbDateStructToViewDate,
  convertServerBooleanNumberToBoolean,
  convertServerBooleanStringToBoolean,
  convertServerDateToMoment,
  convertServerDateToNgbDateStruct,
  convertServerDateToViewDate,
  convertStringDateToMoment,
  convertViewDateToServerDate,
  desSelectDataFineVal,
  ellipsisAt,
  estraiGruppo as estraiGruppoIS,
  estraiRecapito as estraiRecapitoIS,
  formatDateFromString,
  formatoImportoITA,
  generateAnni,
  generateRandomId,
  identificativoSoggetto,
  identificativoSoggettoCompleto,
  importoITAToJsFloat,
  isDataValiditaAttiva,
  isDate,
  isEmptyObject,
  isIstanza,
  isProvvedimento,
  isSBNTrue,
  isSBSTrue,
  mathTruncate,
  mergeDataWith,
  parseInnerHTML,
  quantitaDaADescrittiva,
  recapitiAlternativiSoggetto,
  recapitoAlternativoPratica,
  recapitoPrincipalePratica,
  recapitoPrincipaleSoggetto,
  replaceAll,
  riscaJSONParse,
  samePaginazioni,
  sanitizeFEProperties,
  sanitizeFEPropertiesList,
  sortDatesString,
  sortMoments,
  sortNumbers,
  sortObjects,
  sortStrings,
  statoSDFlagAnnullato,
  typeOfIstanzaOrProvvedimento,
  updatePaginazione,
} from './risca-utilities.functions';

/**
 * #########################################
 * FUNZIONI DISPONIBILI NEL CONTESTO ANGULAR
 * #########################################
 */

/**
 * Servizio di utility con funzionalità di comodo.
 */
@Injectable({
  providedIn: 'root',
})
export class RiscaUtilitiesService {
  /**
   * Costruttore.
   */
  constructor(
    private _logger: LoggerService,
    private _riscaMessages: RiscaMessagesService,
    private _spinner: RiscaSpinnerService
  ) {}

  /**
   * Funzione di comodo per il get dati in una form.
   * @param form FormGroup per la quale recuperare il valore.
   * @param key string che definisce il nome del FormControl per il get del valore.
   */
  getFormValue(form: FormGroup, key: string): any {
    return form.get(key).value;
  }

  /**
   * Funzione di comodo per il set dati in una form.
   * @param form FormGroup per la quale settare il valore.
   * @param key string che definisce il nome del FormControl per il set del valore.
   * @param value any contenente il valore da inserire nel FormControl.
   * @param options FormUpdatePropagation compatibile con la funzione "setValue", per definire delle opzioni sull'azione.
   */
  setFormValue(
    form: FormGroup,
    key: string,
    value: any,
    options?: FormUpdatePropagation
  ) {
    form.get(key).setValue(value, options);
  }

  /**
   * Funzione di comodo per il disable di un campo di una form.
   * @param form FormGroup per la quale settare il valore.
   * @param key string che definisce il nome del FormControl per il set.
   * @param options FormUpdatePropagation compatibile con la funzione "setValue", per definire delle opzioni sull'azione.
   */
  disableFormField(
    form: FormGroup,
    key: string,
    options?: FormUpdatePropagation
  ) {
    form.get(key).disable(options);
  }

  /**
   * Funzione di comodo per l'enable di un campo di una form.
   * @param form FormGroup per la quale settare il valore.
   * @param key string che definisce il nome del FormControl per il set.
   * @param options FormUpdatePropagation compatibile con la funzione "setValue", per definire delle opzioni sull'azione.
   */
  enableFormField(
    form: FormGroup,
    key: string,
    options?: FormUpdatePropagation
  ) {
    form.get(key).enable(options);
  }

  /**
   * Funzione che va ad inizializzare i valori del form, basandosi sulla sorgente dati o dal mapper definito in input.
   * Se definito un mapper, questo indicherà le proprietà da recuperare dal source e inserire nel form.
   * @param form FormGroup da inizializzare.
   * @param source any con i dati per l'inizializzazione.
   * @param mapper Array di string che definisce quali campi recuperare dal source e valorizzare nel form.
   * @returns boolean che definisce se non ci sono stati errori durante la valorizzazione del form.
   */
  initFormFields(form: FormGroup, source: any, mapper?: string[]): boolean {
    // Verifico l'input
    if (!form || !source) {
      // Blocco il flusso
      return false;
    }

    // Lavorando con dati dinamici potrebbero verificarsi errori, definisco un try catch
    try {
      // Verifico se esiste il mapper
      if (mapper) {
        // Itero le proprietà
        mapper.forEach((field: string) => {
          // Recupero il dato dal source
          const value = source[field];
          // Aggiungo il valore al form
          this.setFormValue(form, field, value);
        });
        // #
      } else {
        // Itero l'oggetto source
        for (let [field, value] of Object.entries(source)) {
          // Aggiungo il valore al form
          this.setFormValue(form, field, value);
        }
      }

      // Operazione completata
      return true;
      // #
    } catch (e) {
      // Errore, lo comunico
      return false;
    }
  }

  /**
   * Funzione che verifica se per un FormContrl esiste uno specifico validatore.
   * @param formGroup FormGroup.
   * @param formControlName string che identifica un FormControl.
   * @param validator string che identifica il validatore da verificare.
   * @returns boolean true se esiste un validatore con il nome ricercato, false se non esiste.
   */
  hasValidator(
    formGroup: FormGroup,
    formControlName: string,
    validator: string
  ): boolean {
    // Tento di recuperare i validatori
    const formControl = formGroup.get(formControlName);
    // Verifico che esistano i validatori
    if (formControl.validator) {
      // Tento di estrarre i validatori
      const validators = formControl.validator({} as AbstractControl);
      // Ritorno la verifica sui validatori e sul contenuto
      return validators && validators.hasOwnProperty(validator);
    }

    // Fallback
    return false;
  }

  /**
   * Funzione che effettua l'update dei validatori di un campo del form.
   * Se richiesto, effettua anche un refresh dei dati/controlli sul campo.
   * @param f FormGroup da gestire.
   * @param fcn string che definisce il nome del FormControl all'interno del FormGroup.
   * @param validators Array di ValidatorFn da definire per il FormControl.
   * @param u boolean che definisce se è necessario refreshare dati e controlli.
   * @param uC FormUpdatePropagation che definisce le configurazioni per il refresh dati e controlli.
   * @returns boolean con l'indicazione di avvenuto aggiornamento.
   */
  setFieldValidator(
    f: FormGroup,
    fcn: string,
    validators: ValidatorFn[],
    u = false,
    uC?: FormUpdatePropagation
  ): boolean {
    // Per gestire possibili errori, effettuo le logiche in un try catch
    try {
      // Imposto i validatori per il campo
      f.get(fcn).setValidators(validators);
      // Verifico se è richiesto il refresh
      if (u) {
        f.get(fcn).updateValueAndValidity(uC);
      }
      // Completato
      return true;
      // #
    } catch (e) {
      // Fallito
      return false;
    }
  }

  /**
   * Funzione che va ad attivare la validazione del campo per il proprio valore e per i validatori.
   * @param f FormGroup per la validazione.
   * @param fcn string nome del FormControl da validare.
   * @param c FormUpdatePropagation che definisce la tipologia d'aggiornamento d'applicare.
   */
  updateValueAndValidity(f: FormGroup, fcn: string, c?: FormUpdatePropagation) {
    // Verifico l'input
    if (!f || !fcn) return;
    // Verifico se esiste la configurazione in ingresso
    if (!c) c = { emitEvent: false };

    // Effettuo il check dei dati e validatori
    f.get(fcn).updateValueAndValidity(c);
  }

  /**
   * Funzione che va ad attivare la validazione dei campi per il proprio valore e per i validatori.
   * Tutti i ForcControl verranno verificati.
   * @param f FormGroup da validare.
   * @param c FormUpdatePropagation che definisce la tipologia d'aggiornamento d'applicare.
   */
  updateValuesAndValidities(f: FormGroup, c?: FormUpdatePropagation) {
    // Verifico l'input
    if (!f) return;

    // Recupero i nomi dei form controls
    const fcs = f.controls;
    // Ciclo le proprietà dell'oggetto
    for (let fcn of Object.keys(fcs)) {
      // Effettuo il check dei dati e validatori
      this.updateValueAndValidity(f, fcn, c);
    }
  }

  /**
   * Funzione che verifica se all'interno di un form group è stato generato uno specifico errore passato in input.
   * @param f FormGroup con l'istanza del form per la verifica.
   * @param e string che definisce la chiave per la ricerca dell'errore.
   * @returns boolean che definisce se il form è attualmente in errore e per l'errore indicato.
   */
  checkFormGroupError(f: FormGroup, e: string): boolean {
    // Verifico l'input
    if (!f || !e) {
      // Niente configurazioni
      return false;
    }

    // Verifico se il form è in errore
    if (f.invalid) {
      // Form invalido, recupero gli errori
      const errors = f.errors;
      // Verifico se esistono errori
      if (!errors) {
        // Non ci sono errori
        return false;
      }

      // Verifico se l'oggetto ha l'errore richiesto
      const error = errors[e];
      // Verifico se esiste l'errore
      if (error) {
        // C'è l'errore richiesto
        return true;
        // #
      } else {
        // Niente errore
        return false;
      }
    }

    // Non ci sono errori
    return false;
  }

  /**
   * Funzione che genera una stringa alfanumerica di 10 caratteri.
   * A seconda del valore passato in input, verrà generato un id più complesso.
   * La quantità di caratteri verranno moltiplicati sulla base di complexity.
   * Quindi per complexity:
   * - 1 = random id di 10 caratteri;
   * - 2 = random id di 20 caratteri;
   * - etc...
   * @param complexity number che definisce la complessità dell'id da generare. Default: 2.
   * @returns string che definisci un id randomico.
   */
  generateRandomId(complexity = 2): string {
    // Richiamo la funzione di generazione
    return generateRandomId(complexity);
  }

  /**
   * Funzione di comodo recupera tutti i messaggi di errore, dato un FormGroup e una mappatura: [{ erroreForm: string, erroreCodice: string }].
   * Gli errori saranno ordinati in base alla struttura della mappatura.
   * @param formGroup FormGroup dalla quale estrarre gli errori.
   * @param mappatura Array di MappaErroriFormECodici contente le chiavi per il recupero degli errori.
   * @returns Array di string contenente tutti i messaggi d'errore.
   */
  getAllErrorMessagesFromForm(
    formGroup: FormGroup,
    mappatura: MappaErroriFormECodici[]
  ): string[] {
    // Verifico che gli input siano definiti
    if (!formGroup || !mappatura) {
      // Niente configurazioni
      return [];
    }

    // Recupero tutti gli errori dalla form
    const erroriForm = this.getAllFormErrors(formGroup);
    // Ritorno la lista degli errori
    return this.estraiMessaggiErroreDaOggettoErrori(erroriForm, mappatura);
  }

  /**
   * Funzione di comodo recupera tutti i messaggi di errore, dato un FormControl e una mappatura: [{ erroreForm: string, erroreCodice: string }].
   * Gli errori saranno ordinati in base alla struttura della mappatura.
   * @param formControl FormGroup | FormControl dalla quale estrarre gli errori.
   * @param mappatura Array di MappaErroriFormECodici contente le chiavi per il recupero degli errori.
   * @returns Array di string contenente tutti i messaggi d'errore.
   */
  getErrorMessagesFromForm(
    formControl: FormGroup | FormControl,
    mappatura: MappaErroriFormECodici[]
  ): string[] {
    // Verifico che gli input siano definiti
    if (!formControl || !mappatura) {
      return [];
    }

    // Recupero tutti gli errori dalla form
    const erroriForm = formControl.errors || {};
    // Ritorno la lista degli errori
    return this.estraiMessaggiErroreDaOggettoErrori(erroriForm, mappatura);
  }

  /**
   * Funzione di comodo recupera tutti i messaggi di errore, dato un oggetto di errori chiave/valore e una mappatura: [{ erroreForm: string, erroreCodice: string }].
   * Gli errori saranno ordinati in base alla struttura della mappatura.
   * @param errori Oggetto dinamico DynamicObjString chiave/valore contenente gli errori.
   * @param mappatura Array di MappaErroriFormECodici contente le chiavi per il recupero degli errori.
   * @returns Array di string contenente tutti i messaggi d'errore.
   */
  private estraiMessaggiErroreDaOggettoErrori(
    errori: DynamicObjString = {},
    mappatura: MappaErroriFormECodici[] = []
  ): string[] {
    // Definisco l'array di messaggi vuoto
    const messaggiErrore = [];

    // Ciclo la mappatura degli errori
    for (let i = 0; i < mappatura.length; i++) {
      // Estraggo la mappatura
      const mappa = mappatura[i];

      // Verifico se esiste il messaggio definito per la form all'interno dell'oggetto di errori
      if (errori[mappa.erroreForm]) {
        // Recupero il codice di errore
        const errorceCodice = mappa.erroreCodice;
        // Definisco il contenitore per il messaggio d'errore da ritornare
        let errore: string = '';
        // Recupero il messaggio
        const msg = this._riscaMessages.getMessage(errorceCodice);

        // Se esiste una label per il messaggio d'errore, l'aggiungo
        let labelCampo = mappa.nomeCampo?.label || '';

        // Verifico se non esite una label campo, ma esiste la funzione di label dinamica
        if (labelCampo == '' && mappa.nomeCampo?.erroreDinamico != undefined) {
          // Metto in try catch per evitare errori
          try {
            // Estraggo l'oggetto di errore
            const e = errori[mappa.erroreForm];
            // Assegno alla label del campo il risultato dell'esecuzione della funzione
            errore = mappa.nomeCampo.erroreDinamico(e, msg);
            // #
          } catch (e) {}
          // #
        } else {
          // Compongo il messaggio d'errore
          errore = labelCampo ? `${labelCampo} | ${msg}` : msg;
        }

        // Aggiungo l'errore alla lista
        messaggiErrore.push(errore);
      }
    }

    // Ritorno la lista degli errori
    return messaggiErrore;
  }

  /**
   * Funzione di comodo che estrae tutti gli errori dall'oggetto FormGroup e dagli oggetti figli FormControl.
   * @param form FormGroup dalla quale estrarre gli errori.
   * @returns DynamicObjString contenente tutti gli errori con chiave (nome errore) e valore.
   */
  getAllFormErrors(form: FormGroup): DynamicObjString {
    // Definisco una lista di errori vuota
    let errors = {};

    // Estraggo gli errori dal form group
    const errFormGroup = this.getFormGroupErrors(form);
    // Estraggo gli errori dal form group
    const errFormControl = this.getFormControlsErrors(form);

    // Concateno gli errori
    errors = { ...errFormGroup, ...errFormControl };

    // Ritorno la lista di errori
    return errors;
  }

  /**
   * Funzione di comodo che estrae tutti gli errori dall'oggetto FormGroup.
   * @param form FormGroup dalla quale estrarre gli errori.
   * @returns DynamicObjString contenente tutti gli errori con chiave (nome errore) e valore.
   */
  getFormGroupErrors(form: FormGroup): DynamicObjString {
    // Ritorno la lista di errori
    return this.getFormErrors(form.errors);
  }

  /**
   * Funzione di comodo che estrae tutti gli errori dall'oggetto FormGroup, per i suoi relativi figli FormController.
   * @param form FormGroup dalla quale estrarre gli errori.
   * @returns DynamicObjString contenente tutti gli errori con chiave (nome errore) e valore.
   */
  getFormControlsErrors(form: FormGroup): DynamicObjString {
    // Definisco una lista di errori vuota
    let errors = {};

    // Recupero gli errori sul FormGroup
    const formControls = form.controls;
    // Verifico che esistano degli errori
    if (formControls) {
      // Ciclo gli errori e per ogni errore creo una entry nell'array
      for (const control in formControls) {
        // Verifico se il controller è invalid
        if (formControls[control].invalid) {
          // Estraggo la lista degli errori
          const erroriControl = this.getFormErrors(
            formControls[control].errors
          );
          // Concateno gli errori
          errors = { ...errors, ...erroriControl };
        }
      }
    }

    // Ritorno la lista di errori
    return errors;
  }

  /**
   * Funzione che itera all'interno dell'oggetto errore e genera un oggetto di oggetti errori.
   * @param errorObj DynamicObjString contenente i dati dell'errore.
   * @returns DynamicObjString contenente tutti gli errori con chiave (nome errore) e valore.
   */
  private getFormErrors(errorObj: DynamicObjString): DynamicObjString {
    // Definisco una oggetto di errori vuoto
    let errors = {};

    // Verifico che esistano degli errori
    if (errorObj) {
      // Ciclo gli errori e per ogni errore creo una entry nell'array
      for (const tipoErrore in errorObj) {
        // Verifico che all'interno dell'oggetto principale non ci sia già l'errore
        if (!errors[tipoErrore]) {
          // Aggiungo l'errore all'oggetto
          errors[tipoErrore] = errorObj[tipoErrore];
        }
      }
    }

    // Ritorno la lista di errori
    return errors;
  }

  /**
   * Funzione di comodo che effettua un convert del parametro, in base al tipo, a stringa.
   * @param param any parametro da stringhizzare.
   * @returns string il parametro stringhizzato.
   */
  convertParamToString(param: any): string {
    // Controllo che il parametro esista
    if (param === undefined || param === null) return '';

    // Verifico il tipo di parametro
    switch (typeof param) {
      case 'string':
        return param;
      case 'number':
        return String(param);
      case 'object':
        return JSON.stringify(param);
    }
  }

  /**
   * Funzione che concatena l'array di stringhe in input e produce un'unica stringa.
   * @param urlPaths Array di stringhe contenenti i path da concatenare per formare un url.
   * @returns string concatenata.
   */
  createUrl(urlPaths: string[]): string {
    // Creo la stringa di partenza
    let url = '';
    // Itero gli elementi dell'array
    urlPaths.forEach((s) => (url = url.concat(s)));

    return url;
  }

  /**
   * Funzione che crea una stringa in formato path url: <separatore><parametro>.
   * @param urlPath string da concatenare.
   * @param separatore string carattere separatore del dominio.
   * @returns string contenente una string "path like".
   */
  creaUrlPath(urlPath: string, separatore: string): string {
    return `${separatore}${urlPath}`;
  }

  /**
   * Funzione di utility che permette di verificare se la variabile in input è una funzione.
   * @param functionToCheck Input da verificare
   * @returns boolean che indica se il parametro è una funzione (true) o non lo è (false).
   */
  isFunction(functionToCheck: any) {
    return (
      functionToCheck &&
      {}.toString.call(functionToCheck) === '[object Function]'
    );
  }

  /**
   * Funzione di convert da un Moment ad un NgbDateStruct.
   * @param dateMoment Moment da convertire
   * @returns NgbDateStruct convertita.
   */
  convertMomentToNgbDateStruct(dateMoment: Moment): NgbDateStruct {
    // Ritorno il risultato della funzione
    return convertMomentToNgbDateStruct(dateMoment);
  }

  /**
   * Funzione che formatta l'input in data: GG<separatore>MM<separatore>AAAA.
   * Se la data è già una stringa, viene ritornata senza parsing.
   * @param date string | NgbDateStruct da convertire.
   * @param separatore string che definisce il separatore della data. Il default è "-".
   * @param returnIfDateIsNull qualsiasi cosa si vuole che sia ritornata se date non è valorizzato. Il default è una stringa vuota
   * @returns string data formattata.
   */
  convertNgbDateStructToString(
    date: string | NgbDateStruct,
    separatore: string = '-',
    returnIfDateIsNull: any = ''
  ): string {
    // Ritorno il risultato della funzione
    return convertNgbDateStructToString(date, separatore, returnIfDateIsNull);
  }

  /**
   * Funzione che formatta l'input in data string.
   * Se la data è già una stringa, viene ritornata senza parsing.
   * @param date string | NgbDateStruct da convertire.
   * @param formato string che definisce il formato di destinazione.
   * @returns string data formattata.
   */
  convertNgbDateStructToDateString(
    date: string | NgbDateStruct,
    formato: string
  ): string {
    // Ritorno il risultato della funzione
    return convertNgbDateStructToDateString(date, formato);
  }

  /**
   * Funzione che formatta l'input in data string.
   * @param date NgbDateStruct da convertire.
   * @returns string data formattata.
   */
  convertNgbDateStructToMoment(date: NgbDateStruct): Moment {
    // Ritorno il risultato della funzione
    return convertNgbDateStructToMoment(date);
  }

  /**
   * Funzione che formatta l'input in data con formato "view".
   * Se la data è già una stringa, viene ritornata senza parsing.
   * @param date string | NgbDateStruct da convertire.
   * @returns string data formattata.
   */
  convertNgbDateStructToViewDate(date: string | NgbDateStruct): string {
    // Ritorno il risultato della funzione
    return convertNgbDateStructToViewDate(date);
  }

  /**
   * Funzione che formatta l'input in data con formato "server".
   * Se la data è già una stringa, viene ritornata senza parsing.
   * @param date string | NgbDateStruct da convertire.
   * @returns string data formattata.
   */
  convertNgbDateStructToServerDate(date: string | NgbDateStruct): string {
    // Ritorno il risultato della funzione
    return convertNgbDateStructToServerDate(date);
  }

  /**
   * Funzione di formattazione di una data, da stringa, in una data di destazione (dato i formati di inizio e fine).
   * @param date string data da formattare.
   * @param startFormat string formato della stringa in ingresso.
   * @param targetFormat string formato della stringa in uscita.
   * @returns string data formattata.
   */
  formatDateFromString(
    date: string,
    startFormat: string,
    targetFormat: string
  ): string {
    // Richiamo e ritorno la funzione javascript
    return formatDateFromString(date, startFormat, targetFormat);
  }

  /**
   * Funzione di parsing data da un formato in input al formato server.
   * @param date string | NgbDateStruct data da convertire.
   * @returns string data convertita.
   */
  convertViewDateToServerDate(date: string | NgbDateStruct): string {
    // Richiamo e ritorno la funzione javascript
    return convertViewDateToServerDate(date);
  }

  /**
   * Funzione di parsing data da un formato server al formato view.
   * @param date string | NgbDateStruct data da convertire.
   * @returns string data convertita.
   */
  convertServerDateToViewDate(date: string | NgbDateStruct): string {
    // Richiamo e ritorno la funzione javascript
    return convertServerDateToViewDate(date);
  }

  /**
   * Funzione che effettua il convert da una data string con formato server ad una data NgbDateStruct.
   * @param date string da convertire.
   * @returns NgbDateStruct convertita.
   */
  convertServerDateToNgbDateStruct(date: string): NgbDateStruct {
    // Richiamo e ritorno la funzione javascript
    return convertServerDateToNgbDateStruct(date);
  }

  /**
   * Funzione che effettua il convert da una data string con formato server ad un Moment.
   * @param date string da convertire.
   * @returns Moment convertito.
   */
  convertServerDateToMoment(date: string): Moment {
    // Richiamo e ritorno la funzione javascript
    return convertServerDateToMoment(date);
  }

  /**
   * Funzione che effettua il convert da una data string, compatibile con l'oggetto Date di javascrpit, in un formato Moment.
   * @param date string da convertire.
   * @returns Moment convertito.
   */
  convertStringDateToMoment(date: string): Moment {
    // Richiamo e ritorno la funzione javascript
    return convertStringDateToMoment(date);
  }

  /**
   * Funzione che effettua il convert da un moment ad una iso date string.
   * @param dateMoment Moment da convertire.
   * @returns string in formato Date ISO convertito.
   */
  convertMomentToISOString(dateMoment: Moment): string {
    // Richiamo e ritorno la funzione javascript
    return convertMomentToISOString(dateMoment);
  }

  /**
   * Funzione di parsing data da un formato moment al formato view date.
   * @param dateMoment Moment da convertire.
   * @returns string data convertita.
   */
  convertMomentToViewDate(dateMoment: Moment): string {
    // Verifico l'input
    if (!dateMoment || !dateMoment.isValid()) {
      return '';
    }
    // Aggiorno il formato della data
    const viewFormat = RiscaFormatoDate.view;
    const serverFormat = RiscaFormatoDate.server;
    // Converto il moment in iso date string
    return dateMoment.format(viewFormat);
  }

  /**
   * Funzione di parsing data da un formato moment al formato server date.
   * @param dateMoment Moment da convertire.
   * @returns string data convertita.
   */
  convertMomentToServerDate(dateMoment: Moment): string {
    // Verifico l'input
    if (!dateMoment || !dateMoment.isValid()) {
      return '';
    }
    // Aggiorno il formato della data
    const serverFormat = RiscaFormatoDate.server;
    // Converto il moment in iso date string
    return dateMoment.format(serverFormat);
  }

  /**
   * Funzione che modifica per referenza l'oggetto di configurazione del calendario in input.
   * Le date, se impostate, verranno assegnate: data min -> 100 anni nel passato; data max -> 100 anni nel futuro.
   * Entrambe a partire da oggi.
   * @param calendarioConfig RiscaDatepickerConfig da configurare.
   * @param setMin boolean che definisce se inizializzare la data minima del calendario. Il default è true.
   * @param setMax boolean che definisce se inizializzare la data massima del calendario. Il default è true.
   */
  setupCalendarioMinMaxConfig(
    calendarioConfig: RiscaDatepickerConfig,
    setMin = true,
    setMax = true
  ) {
    // Verifico che l'oggetto esista
    if (!calendarioConfig) return;

    // Definisco la data minima, partendo da oggi
    const startCalendar = moment().subtract(100, 'years');
    // Definizione della data massima, partendo da oggi
    const endCalendar = moment().add(100, 'years');

    // Se richiesto, imposto la data minima
    if (setMin) {
      // Configurazione data minima per il calendario
      calendarioConfig.minDate = {
        year: startCalendar.year(),
        month: startCalendar.month() + 1,
        day: startCalendar.date(),
      };
    }

    // Se richiesto, imposto la data massima
    if (setMax) {
      // Configurazione data minima per il calendario
      calendarioConfig.maxDate = {
        year: endCalendar.year(),
        month: endCalendar.month() + 1,
        day: endCalendar.date(),
      };
    }
  }

  /**
   * Funzione che esegui un sort tra due stringhe, basato sul sort degli array in javascript.
   * @param a string a per il sort.
   * @param b string b per il sort.
   * @param sortType RiscaSortTypes che definisce il tipo di sort d'applicare. Per default è RiscaSortTypes.crescente.
   * @returns number che definisce il risultato del sort.
   */
  sortStrings(
    a: string,
    b: string,
    sortType: RiscaSortTypes = RiscaSortTypes.crescente
  ): number {
    // Richiamo e ritorno la funzione di supporto
    return sortStrings(a, b, sortType);
  }

  /**
   * Funzione che esegui un sort tra due numeri, basato sul sort degli array in javascript.
   * @param a number a per il sort.
   * @param b number b per il sort.
   * @param sortType RiscaSortTypes che definisce il tipo di sort d'applicare. Per default è RiscaSortTypes.crescente.
   * @returns number che definisce il risultato del sort.
   */
  sortNumbers(
    a: number,
    b: number,
    sortType: RiscaSortTypes = RiscaSortTypes.crescente
  ): number {
    // Richiamo e ritorno la funzione di supporto
    return sortNumbers(a, b, sortType);
  }

  /**
   * Funzione che esegui un sort tra due date (in formato stringa), basato sul sort degli array in javascript.
   * @param a string a per il sort.
   * @param b string b per il sort.
   * @param f string che definisce il formato delle stringhe.
   * @param sortType RiscaSortTypes che definisce il tipo di sort d'applicare. Per default è RiscaSortTypes.crescente.
   * @returns number che definisce il risultato del sort.
   */
  sortDatesString(
    a: string,
    b: string,
    f: string,
    sortType: RiscaSortTypes = RiscaSortTypes.crescente
  ): number {
    // Richiamo e ritorno la funzione di supporto
    return sortDatesString(a, b, f, sortType);
  }

  /**
   * Funzione che esegue un sort tra due date (in formato moment), basato sul sort degli array in javascript.
   * @param a string a per il sort.
   * @param b string b per il sort.
   * @param sortType RiscaSortTypes che definisce il tipo di sort d'applicare. Per default è RiscaSortTypes.crescente.
   * @returns number che definisce il risultato del sort.
   */
  sortMoments(
    a: Moment,
    b: Moment,
    sortType: RiscaSortTypes = RiscaSortTypes.crescente
  ): number {
    // Richiamo e ritorno la funzione di supporto
    return sortMoments(a, b, sortType);
  }

  /**
   * Funzione che esegui un sort tra due oggetti, basato sul sort degli array in javascript.
   * @param a Object a per il sort.
   * @param b Object b per il sort.
   * @param sortLogic Funzione (a: any, b: any) => number che definisce le logiche di sort.
   * @param sortType RiscaSortTypes che definisce il tipo di sort d'applicare. Per default è RiscaSortTypes.crescente.
   * @returns number che definisce il risultato del sort.
   */
  sortObjects(
    a: any,
    b: any,
    sortType: RiscaSortTypes = RiscaSortTypes.crescente,
    sortLogic: (a: any, b: any) => number
  ): number {
    // Richiamo la funzione di sort
    return sortObjects(a, b, sortLogic, sortType);
  }

  /**
   * Funzione che aggiunge o rimuove degli 0 davanti alla stringa in input.
   * La quantità di 0 è definito dal limite di lunghezza che si vuole per la stringa.
   * @param stringaNum string da manipolare.
   * @param limite number che definisce la lunghezza della stringa da ottenere.
   * @param allowEmptyString boolean che definisce se una stringa vuota può essere riempita di 0. Il default è false.
   * @returns string manipolata.
   */
  gestisciZeriInizioNumero(
    stringaNum: string,
    limite: number,
    allowEmptyString = false
  ): string {
    // Verifico se il dato esiste
    if (
      stringaNum === undefined ||
      stringaNum === null ||
      (!allowEmptyString && stringaNum === '') ||
      limite === undefined ||
      limite === 0
    ) {
      return stringaNum;
    }

    // Parso l'input in un numero
    const checkStringaNum = Number(stringaNum);
    // Verifico se è effettivamente un numero
    if (isNaN(checkStringaNum)) return stringaNum;

    // Verifico se bisogna aggiungere degli zeri prima del numero definito dall'utente
    if (stringaNum.length < limite) {
      // Conto quanti 0 servono per completare la string
      const zeriExtra = 5 - stringaNum.length;
      // Aggiungo gli 0 alla stringa
      for (let i = 0; i < zeriExtra; i++) stringaNum = `0${stringaNum}`;
      // #
    } else if (stringaNum.length > limite) {
      // Trasformo la string in un array di caratteri
      let stringaNumTmp = stringaNum.split('');
      // Fino a che ci sono 0 nella prima posizione o la stringa diventa di 5 caratteri, rimuovo gli 0
      for (let i = 0; stringaNumTmp.length > limite; i++) {
        // Verfico se alla posizione 0 c'è 0
        if (stringaNumTmp[0] === '0') {
          // Rimuovo lo 0
          stringaNumTmp.splice(0, 1);
        } else {
          // Interrompo il ciclo, ho trovato un numero non 0
          break;
        }
      }
      // Assegno il nuovo valore al progressivo
      stringaNum = stringaNumTmp.join('');
    }

    // Ritorno la stringa manipolata
    return stringaNum;
  }

  /**
   * Funzione che verifica la validità di una partita IVA.
   * @param pi string per la partita IVA da controllare.
   * @returns boolean con (true) per partita IVA valida, altrimenti (false).
   */
  controllaPartitaIVA(pi: string): boolean {
    // Richiamo la funzione di controllo della partita iva
    return controllaPartitaIVA(pi);
  }

  /**
   * Funzione che converte un oggetto in input in un oggetto HttpParams.
   * Le proprietà dell'oggetto saranno le chiavi, mentre il valore il valore.
   * @param objParam DynamicObjAny contenente i parametri da convertire.
   * @param rules IRulesQueryParams che definisce le regole di gestione per la gestione dei dati del query param.
   * @returns HttpParams risultato della conversione.
   */
  createQueryParams(
    objParam: DynamicObjAny,
    rules?: IRulesQueryParams
  ): HttpParams {
    // Verifico l'input
    if (!objParam) {
      return new HttpParams();
    }

    // Definisco la funzione di conversione di un param
    const convertParam = (value: any): any => {
      // Definisco una variabile contenitore per value
      let valueP: any;
      // Verifico la tipologia di value
      switch (typeof value) {
        case 'number':
        case 'bigint':
        case 'string':
        case 'symbol':
        case 'boolean':
          valueP = value;
          break;
        case 'object':
          valueP = JSON.stringify(value);
          break;
        default:
          valueP = '';
      }
      // Ritorno il parametro
      return valueP;
    };

    // Definisco le regole per i params
    const allowNull = rules?.allowNull || false;
    const allowUndefined = rules?.allowUndefined || false;
    const allowEmptyString = rules?.allowEmptyString || false;

    // Creo un oggetto HttpParams
    let params = new HttpParams({ encoder: new RiscaHttpParamEncoder() });

    // Ciclo le proprietà dell'oggetto
    for (let [key, value] of Object.entries(objParam)) {
      // Definisco il contenitore del parametro
      let valueP: any;

      // Verifico il value
      const isNull = value === null;
      const isUndefined = value === undefined;
      const isEmptyString = value === '';

      // Flag di check per la conversione
      const cNull = allowNull && isNull;
      const cUndefined = allowUndefined && isUndefined;
      const cEmptyString = allowEmptyString && isEmptyString;
      const normalValue = !isNull && !isUndefined && !isEmptyString;

      // Verifico le condizioni
      if (cNull || cUndefined || cEmptyString || normalValue) {
        // Converto il parametro
        valueP = convertParam(value);
        // // Effettuo l'encode per i caratteri speciali
        // valueP = encodeURIComponent(valueP);
        // Aggiungo il parametro all'oggetto
        params = params.append(key, valueP);
      }
    }

    // Ritorno l'oggetto
    return params;
  }

  /**
   * Funzione che genera un Moment che definisce la data minima per un maggiorennte.
   * Sostanzialmente, verrà fatto un now - 18 anni.
   */
  minDateMaggiorenne(
    margine?: number,
    tipoPeriodo?: moment.unitOfTime.Diff
  ): Moment {
    // Definisco oggi
    const now = moment();
    // Rimuovo 18 anni per la maggiore età
    let maggioreEta = now.subtract(18, 'years');

    // Verfico se è definito un margine
    if (margine && tipoPeriodo) {
      // Aggiungo il margine alla data
      maggioreEta.add(margine, tipoPeriodo);
    }

    // Ritorno la data calcolata
    return maggioreEta;
  }

  /**
   * Funzione che sanitizza l'oggetto dai valori undefined e li converte in null.
   * @param object any da sanitizzare.
   * @returns any sanitizzato.
   */
  sanitizeServerObject(object: any): any {
    // Verifico l'input
    if (!object) {
      // Nessuna configurazione
      return undefined;
    }

    // Ciclo tutte le proprietà
    for (let [key, value] of Object.entries(object)) {
      // Verifico il valore della proprietà
      if (value === undefined) {
        // Converto il valore in null
        object[key] = null;
      }
    }

    // Ritorno l'oggetto
    return object;
  }

  /**
   * Funzione che rimuove dagli oggetti della lista, il flag usato nei componenti risca: __selected.
   * @param lista Array di any contenente la lista degli oggetti da sanitizzare.
   */
  sanitizeRiscaSelectedFlag(lista: any[]) {
    // Verifico l'input
    if (!lista) return;

    // Ciclo gli oggetti della lista
    for (let i = 0; i < lista.length; i++) {
      // Recupero l'oggetto
      const element = lista[i];
      // Verifico se è presente la proprietà __selected
      if (element.__selected !== undefined) {
        delete element.__selected;
      }
    }
  }

  /**
   * Funzione di sanitizzazione di un oggetto dalle proprietà impiegate dal frontend.
   * Per prassi, le proprietà utilizzate solo dal frontend hanno il prefisso '__<proprietà>'.
   * La modifica avverrà per referenza.
   * @param obj any da sanitizzare.
   */
  sanitizeFEProperties(obj: any) {
    // Richiamo la funzione di utility
    sanitizeFEProperties(obj);
  }

  /**
   * Funzione di sanitizzazione di una lista di oggetti dalle proprietà impiegate dal frontend.
   * Per prassi, le proprietà utilizzate solo dal frontend hanno il prefisso '__<proprietà>'.
   * @param list any[] da sanitizzare.
   * @returns any[] con gli oggetti sanitizzati.
   */
  sanitizeFEPropertiesList(list: any[]) {
    // Richiamo la funzione di utility
    return sanitizeFEPropertiesList(list);
  }

  /**
   * Funzione che verifica se un oggetto è vuoto.
   * @param o any da verificare.
   * @returns boolean che definisce se l'oggetto è vuoto o undefined.
   */
  isEmptyObject(o: any): boolean {
    // Richiamo la funzione di utility
    return isEmptyObject(o);
  }

  /**
   * Funzione che verifica se un oggetto è vuoto controllando anche i suoi parametri.
   * Se uno di essi è un oggetto valorizzato, allora controlla che anch'esso sia vuoto.
   * @param o any da verificare.
   * @returns boolean che definisce se l'oggetto è vuoto o undefined.
   */
  isEmptyObjectDeep(o: any): boolean {
    // Verifico l'input
    if (o === null || o === undefined || o === []) {
      return true;
    }

    // Se è una stringa valorizzata, allora la ritorno
    if (typeof o == 'string') {
      return o.length === 0;
    }

    // Ottengo l'elenco delle proprietà
    const proprieta = Object.keys(o);
    // Verifico se l'oggetto ha proprietà valorizzate. Se non ne ha è un primitivo.
    if (proprieta.length === 0) {
      return o == null;
    }

    // Controllo che ogni proprietà sia vuota ricorsivamente.
    return proprieta.every((p) => this.isEmptyObjectDeep(o[p]));
  }

  /**
   * Funzione che verifica il tipo dell'input se Istanza o Provvedimento.
   * @param istanzaOProvvedimento TNIPFormData da verificare.
   * @returns TipiIstanzaProvvedimento che definisce la tipologia.
   */
  typeOfIstanzaOrProvvedimento(
    istanzaOProvvedimento: TNIPFormData
  ): TipiIstanzaProvvedimento {
    // Richiamo la funzione
    return typeOfIstanzaOrProvvedimento(istanzaOProvvedimento);
  }

  /**
   * Funzione che verifica se l'input è un'istanza.
   * @param istanzaOProvvedimento TNIPFormData da verificare.
   * @returns boolean che definisce se l'oggetto è un'istanza.
   */
  isIstanza(istanzaOProvvedimento: TNIPFormData): boolean {
    // Richiamo la funzione di check
    return isIstanza(istanzaOProvvedimento);
  }

  /**
   * Funzione che verifica se l'input è un provvedimento.
   * @param istanzaOProvvedimento TNIPFormData da verificare.
   * @returns boolean che definisce se l'oggetto è un provvedimento.
   */
  isProvvedimento(istanzaOProvvedimento: TNIPFormData): boolean {
    // Richiamo la funzione di check
    return isProvvedimento(istanzaOProvvedimento);
  }

  /**
   * Funzione di comodo che verifica se entrambi gli oggetti non sono settati.
   * Se uno è settato e l'altro non è settato, ritorno false.
   * @param a any da verificare.
   * @param b any da verificare.
   * @returns boolean che specifica se entrambi gli elementi sono unsetted [true] o se uno è unsetted e l'altro lo è [false].
   */
  sameUnsetted(a: any, b: any): boolean {
    // Verifico entrambi gli elementi unsetted
    if (!a && !b) {
      // Unsetted entrambi
      return true;
      // #
    } else {
      // Almeno uno è settato
      return false;
    }
  }

  /**
   * Funzione di supporto che verifica se l'input è strettamente uguale a null o undefined.
   * @param e any da verificare.
   * @returns boolean che definisce se l'input è strettamente null o undefined [true], atrimenti [false].
   */
  isNullOrUndefined(e: any): boolean {
    // Ritorno la verifica
    return e === undefined || e === null;
  }

  /**
   * Funzione che verifica se due oggetti sono gli stessi, verificando la stessa proprietà
   * @param a any da verificare.
   * @param b any da verificare.
   * @param p string | number che definisce la proprietà per la verifica.
   * @returns boolean che definisce se l'oggetto è lo stesso [true], o non è lo stesso [false].
   */
  sameObjectByProperty(a: any, b: any, p: string | number): boolean {
    // Verifico che esista la proprietà per il confronto
    if (this.isNullOrUndefined(p)) {
      // Controllo non si può eseguire
      throw new Error('sameObjectByProperty | Property is undefined');
      // #
    }

    // Verifico l'input
    const sameUnsetted = this.sameUnsetted(a, b);
    // Verifico se entrambi non sono settati
    if (sameUnsetted) {
      // Considero gli oggetti uguali
      return true;
    }
    // Verifico se un oggetto è settato, l'altro no
    if (!a || !b) {
      // Sono oggetti diversi
      return false;
    }

    // Recupero gli id dai comuni
    const c1 = a[p];
    const c2 = b[p];

    // Verifico per stesso id
    return c1 === c2;
  }

  /**
   * Funzione che verifica che un valore in input non sia undefined.
   * Se è undefined ritorna un valore di default.
   * @param value any con il valore da verificare.
   * @param vDefault any con il valore di default da ritornare.
   * @returns any con il risultato del check.
   */
  valueOrDefault(value: any, vDefault: any): any {
    // Verifico value
    if (value !== undefined) {
      // Ritorno il valore
      return value;
      // #
    } else {
      // Ritorno il valore di default
      return vDefault;
    }
  }

  /**
   * Funzione che effettua una comparazione profonda tra due oggetti.
   * @param obj1 any per la deep compare.
   * @param obj2 any per la deep compare.
   * @returns boolean che definisce se gli oggetti hanno stessi valori [true] o no [false].
   */
  compareDeep(obj1: any, obj2: any): any {
    // Richiamo la compare di lodash
    return isEqual(obj1, obj2);
  }

  /**
   * Funzione che effettua una comparazione profonda tra due oggetti generati da una mappa in input.
   * La funzione andrà a costruire due oggetti, prendendo le proprietà definite dalla mappatura.
   * Costruiti gli oggetti verrà effettuata una compareDeep.
   * @param obj1 any per la deep compare.
   * @param obj2 any per la deep compare.
   * @param map DynamicObjAny contenente la mappa che indica quali proprietà sono da mappare e quali no.
   * @returns boolean che definisce se gli oggetti hanno stessi valori [true] o no [false].
   */
  compareDeepFromMap(
    obj1: any,
    obj2: any,
    map: DynamicObjAny,
    c?: IParseValueRules
  ): any {
    // Verifico che esista la mappa
    if (!map) {
      // Variabili di comodo
      const m = 'compareDeepFromMap | map is undefined';
      // Lancio un errore
      throw new Error(m);
    }

    // Definisco gli oggetti da creare e comparare
    const objG1: any = {};
    const objG2: any = {};

    // Ciclo gli elementi della mappa
    for (let [key, use] of Object.entries(map)) {
      // Verifico se la proprietà è da mappare per la compare
      if (use) {
        // Aggiungo la proprietà agli oggetti con il valore dell'input corrispondente
        objG1[key] = this.valueRegulated(obj1[key]);
        objG2[key] = this.valueRegulated(obj2[key]);
      }
    }

    // Richiamo la compare di lodash
    return isEqual(obj1, obj2);
  }

  /**
   * Funzione che permette di gestire un determinato valore
   * @param o any con le informazioni da valutare.
   * @param c IParseValueRules con le regole d'applicare al value.
   * @returns any con il dato gestito dalle regole.
   */
  valueRegulated(o: any, c?: IParseValueRules): any {
    // Verifico la configurazione
    if (!c || o === undefined) {
      // Nessuna regola, ritorno il valore come è stato passato
      return o;
    }

    // 1) emptyObjToNull
    if (c.emptyObjToNull && this.isEmptyObject(o)) {
      // Oggetto vuoto, imposto null
      o = null;
    }
    // 2) sanitizeFEproperties
    if (c.sanitizeFEProperties) {
      // Sanitizzo l'oggetto
      this.sanitizeFEProperties(o);
    }

    // Ritorno l'oggetto manipolato
    return o;
  }

  /**
   * Funzione che verifica e recupera lo state dal servizio Router.
   * Se non sono forniti valori, verrà ritornato {}.
   * @param r Router service di Angular.
   * @returns T contenente i dati dentro Router per il suo state, o oggetto vuoto se non è stato trovato niente.
   */
  getRouterState(r: Router): any {
    // Verifico l'input
    if (!r) {
      // Ritorno oggetto vuoto
      return {};
    }

    // Recupero lo state
    const state = r.getCurrentNavigation()?.extras?.state || {};
    // Ritorno lo state
    return state;
  }

  /**
   * Funzione che verifica e recupera il data dal servizio ActivatedRoute.
   * Se non sono forniti valori, verrà ritornato {}.
   * @param ar ActivatedRoute service di Angular.
   * @returns DynamicObjAny contenente i dati dentro ActivatedRoute per il suo data, o oggetto vuoto se non è stato trovato niente.
   */
  getActivatedRouteData(ar: ActivatedRoute): DynamicObjAny {
    // Verifico l'input
    if (!ar) {
      // Ritorno oggetto vuoto
      return {};
    }

    // Recupero il data
    const data = ar.snapshot?.data || {};
    // Ritorno il data
    return data;
  }

  /**
   * Funzione di supporto che permette di capire se un errore generato dal server è qualcosa di gestibile sulla base dello status e del code in input.
   * @param e RiscaServerError per la verifica.
   * @param httpCode RiscaServerStatus che definisce il codice http da verificare.
   * @param riscaCode RiscaNotifyCodes che definisce il codice di verifica da controllare.
   * @returns boolean che definisce se l'errore è gestito.
   */
  isServerErrorManageable(
    e: RiscaServerError,
    httpCode: RiscaServerStatus,
    riscaCode: RiscaNotifyCodes
  ): boolean {
    // Verifico l'input
    if (!e || !httpCode || !riscaCode) {
      // Errore, lo considero non gestibile
      return false;
    }

    // Recupero le informazioni di controllo
    const { status, code } = e.error || {};
    // Verifico che esistano i dati per il controllo
    if (!status && !code) {
      // Errore, lo considero non gestibile
      return false;
    }

    // Effettuo il confronto
    const sameStatus = status === httpCode;
    const sameCode = code === riscaCode;

    // Ritorno il controllo
    return sameStatus && sameCode;
  }

  /**
   * Funzione di comodo per generare un errore custom da parte del FE con la stessa formattazione di un errore generato BE.
   * @param title string che definisce il title dell'errore.
   * @param code string che definisce il code dell'errore.
   * @param detail IRiscaServerErrorDetail che definisce il detail dell'errore.
   * @param status string che definisce il status dell'errore.
   * @param links string[ che definisce il links dell'errore.
   * @returns RiscaServerError come oggetto generato.
   */
  customResponseError(
    title?: string,
    code?: string,
    detail?: IRiscaServerErrorDetail,
    status?: string,
    links?: string[]
  ): RiscaServerError {
    // Creo un oggetto custom per la gestione delle informazioni dell'errore
    const i = new RiscaServerErrorInfo({
      title,
      code,
      detail,
      links,
      status,
    });
    // Creo un d'errore come quello del server
    return new RiscaServerError({ error: i });
  }

  /**
   * Funzione di supporto che estrae il codice d'errore da un errore generato dal server.
   * Se esiste la definizione viene ritornato il messaggio relativo, altrimenti undefined.
   * @param e RiscaServerError per il recupero delle informazioni.
   * @returns string | undefined con il messaggio d'errore se viene trovato.
   */
  getMessageFromRiscaServerError(e: RiscaServerError): string {
    // Verifico l'input
    if (!e) {
      // Niente dati
      return;
    }

    // Cerco di estrarre il codice d'errore
    const { code } = e.error || {};
    // Verifico se esiste il codice
    if (code) {
      // Recupero il messaggio dal servizio
      return this._riscaMessages.getMessage(code);
      // #
    } else {
      // Niente messaggio
      return undefined;
    }
  }

  /**
   * Funzione di supporto per la conversione dei boolean ricevuti da servere in boolean standard.
   * @param sb TRiscaServerBoolean che definisce la tipologia di boolean server.
   * @returns boolean convertito.
   */
  convertServerBooleanStringToBoolean(sb: TRiscaServerBoolean): boolean {
    // Richiamo la funzione di conversione
    return convertServerBooleanStringToBoolean(sb);
  }

  /**
   * Funzione di supporto per la conversione dei boolean ricevuti da server, in formato number, in boolean standard.
   * @param sb number che definisce la tipologia di boolean server.
   * @returns boolean convertito.
   */
  convertServerBooleanNumberToBoolean(sb: number): boolean {
    // Richiamo la funzione di conversione
    return convertServerBooleanNumberToBoolean(sb);
  }

  /**
   * Funzione di supporto per la verifica dei boolean ricevuti da server, in formato string, in boolean standard con condizione: true.
   * @param sb TRiscaServerBoolean che definisce la tipologia di boolean server.
   * @returns boolean che indica se il valore è true.
   */
  isSBSTrue(sb: TRiscaServerBoolean): boolean {
    // Richiamo la funzione di conversione
    return isSBSTrue(sb);
  }

  /**
   * Funzione di supporto per la verifica dei boolean ricevuti da server, in formato number, in boolean standard con condizione: true.
   * @param sb number che definisce la tipologia di boolean server.
   * @returns boolean che indica se il valore è true.
   */
  isSBNTrue(sb: number): boolean {
    // Richiamo la funzione di conversione
    return isSBNTrue(sb);
  }

  /**
   * Ottiene la lista di label in base ai campi passati in input per la gestione dei
   * valori modificati da fonte dei dati anagrafici
   * @param campi
   * @param map
   * @returns le label modificate
   */
  getFieldsFonteMessages(campi: string[], map: DynamicObjString): string[] {
    // Verifico l'input
    if (!campi || !map) {
      // Nessuna configurazione
      return [];
    }

    // Definisco un array di supporto
    const labelCampi = [];

    // Per ogni campo in input estraggo le informazioni per i messaggi
    campi.forEach((campo) => {
      // Recupero la label per nome del campo
      const label = map[campo];
      // Verifico che sia stata definita una label
      if (label) {
        // Aggiungo la label all'array
        labelCampi.push(label);
        // #
      } else {
        // ATTENZIONE: funzione di fallback che visualizza il campo ciclato (vuol dire che la mappa è errata)
        labelCampi.push(campo);
      }
    });

    // Ritorno le label dei campi
    return labelCampi;
  }

  /**
   * Funzione di supporto che aggiorna il source della configurazione (se esiste) e definisce come selezionato uno specifico elemento.
   * La modifica dei dati avverrà per referenza.
   * @param checkboxConfigs RiscaComponentConfig<RiscaFormInputCheckbox> da aggiornare.
   * @param selected IRiscaCheckboxData che risulta selezionato.
   */
  updateRFICheckboxSource(
    checkboxConfigs: RiscaComponentConfig<RiscaFormInputCheckbox>,
    selected: IRiscaCheckboxData
  ) {
    // Verifico l'input
    if (!checkboxConfigs?.source || !selected) {
      // Interrompo le logiche
      return;
    }

    // Effettuo le operazioni in modo sicuro per evitare che la costruzione del source sia sbagliata
    try {
      // Recupero l'id dell'oggetto selezionato
      const { id } = selected;
      // Cerco l'indice dell'oggetto nel source
      const i = checkboxConfigs.source.findIndex((cc: IRiscaCheckboxData) => {
        // Verifico gli di
        return cc.id === id;
      });

      // Verifico se è stato trovato un oggetto
      if (i !== -1) {
        // Aggiorno l'oggetto
        checkboxConfigs.source[i].check = selected.check;
        // #
      }
      // #
    } catch (e) {
      // Variabili di comodo
      const m = 'updateRFICheckboxSource';
      // Loggo un warning
      this._logger.warning(m, { choiceConfigs: checkboxConfigs, selected });
    }
  }

  /**
   * Funzione di supporto che aggiorna il source della configurazione (se esiste) e definisce come selezionato uno specifico elemento.
   * La modifica dei dati avverrà per referenza.
   * @param checkboxConfigs RiscaComponentConfig<RiscaFormInputCheckbox> da aggiornare.
   * @param selected Array di IRiscaCheckboxData che risulta selezionato.
   */
  updateRFICheckboxesSource(
    checkboxConfigs: RiscaComponentConfig<RiscaFormInputCheckbox>,
    selected: IRiscaCheckboxData[]
  ) {
    // Verifico l'input
    if (!checkboxConfigs?.source || !selected) {
      // Interrompo le logiche
      return;
    }

    // Effettuo le operazioni in modo sicuro per evitare che la costruzione del source sia sbagliata
    try {
      // Recupero gli id degli oggetti selezionati
      const ids = selected.map((s) => s.id);
      // Definisco un contenitore per gli indici posizionali degli elementi
      const indexes: number[] = [];

      // Cerco l'indice dell'oggetto nel source
      checkboxConfigs.source.forEach((cc: IRiscaCheckboxData, i: number) => {
        // Verifico gli di
        if (ids.some((id) => cc.id === id)) {
          // Oggetto è selezionato, pusho l'indice
          indexes.push(i);
          // #
        }
      });

      // Ciclo gli indici degli oggetti selezionati
      indexes.forEach((i: number) => {
        // Vado a impostare il flag di checka true per stesso id
        checkboxConfigs.source[i].check = true;
        // #
      });
      // #
    } catch (e) {
      // Variabili di comodo
      const m = 'updateRFICheckboxSource';
      // Loggo un warning
      this._logger.warning(m, { choiceConfigs: checkboxConfigs, selected });
    }
  }

  /**
   * Genera un valore numerico intero casuale compreso tra un valore minimo ed un valore massimo.
   * @param min valore minimo.
   * @param max valore massimo.
   * @returns valore random intero.
   */
  generaRandomMinAMax(min: number, max: number) {
    // Ritorno la generazione
    return Math.floor(Math.random() * (max - min + 1) + min);
  }

  /**
   * Funzione di utility che crea un effetto dealy per la risposta a delle chiamate che vengono gestite in maniera sincrona.
   * @param res any da restituire come risposta.
   * @param millisecs number che definisce i millisecondi di delay.
   * @returns Observable<any> con la risposta generata.
   */
  responseWithDelay<T>(res: any, millisecs?: number): Observable<T> {
    // Avvio lo spinner
    this._spinner.show();

    // Genero un delay per il ritorno dei dati
    const d = millisecs || this.generaRandomMinAMax(25, 100);
    // Genero un delay per lo spinner
    const s = millisecs || this.generaRandomMinAMax(10, 85);

    // Effettuo una clone del dato
    const resClone = cloneDeep(res);

    // Ritorno i dati dal servizio
    return of(resClone).pipe(
      delay(d),
      tap(() => {
        // Definisco un altro livello di delay per nascondere lo spinner
        setTimeout(() => {
          // Nascondo lo spinner
          this._spinner.hide();
          // #
        }, s);
      })
    );
  }

  /**
   * Funzione di supporto che gestisce il cognome e nome, o denominazione del soggetto.
   * @param soggetto SoggettoVo contenente i dati del soggetto.
   * @returns string con l'identificativo del soggetto.
   */
  identificativoSoggetto(soggetto: SoggettoVo): string {
    // Richiamo la funzione da costante
    return identificativoSoggetto(soggetto);
  }

  /**
   * Funzione di supporto che gestisce il cognome e nome, o denominazione del soggetto.
   * @param soggetto SoggettoVo contenente i dati del soggetto.
   * @returns string con l'identificativo del soggetto.
   */
  identificativoSoggettoCompleto(soggetto: SoggettoVo): string {
    // Richiamo la funzione da costante
    return identificativoSoggettoCompleto(soggetto);
  }

  /**
   * Formatta una stringa in modo da eliminare le lettere accentate e mettere tutto maiuscolo. Tronca a maxlength.
   * @param valore stringa iniziale
   * @returns stringa formattata
   */
  formatStringToUpperNoAccent(valore: string, maxlength?: number): string {
    // Verifico l'input
    if (!valore || valore?.length === 0) {
      return '';
    }

    // Tutto maiuscolo
    let valoreRepd = valore.toUpperCase();
    // Se contiene lettere accentate, lo modifico
    if (
      valoreRepd.indexOf('À') >= 0 ||
      valoreRepd.indexOf('Á') >= 0 ||
      valoreRepd.indexOf('È') >= 0 ||
      valoreRepd.indexOf('É') >= 0 ||
      valoreRepd.indexOf('Ì') >= 0 ||
      valoreRepd.indexOf('Ò') >= 0 ||
      valoreRepd.indexOf('Ù') >= 0
    ) {
      // Sostituisco vocali accentate usando le regex globali
      valoreRepd = valoreRepd
        .replace(/À/g, "A'")
        .replace(/Á/g, "A'")
        .replace(/È/g, "E'")
        .replace(/É/g, "E'")
        .replace(/Ì/g, "I'")
        .replace(/Ò/g, "O'")
        .replace(/Ù/g, "U'");
    }
    // Tronco se ho maxlength
    if (maxlength != undefined) {
      valoreRepd = valoreRepd.substring(0, maxlength);
    }
    // return del valore
    return valoreRepd;
  }

  /**
   * Funzione che genera gli HttpParams da passare all'API.
   * Nell'oggetto di ritorno, vengono definiti i parametri di paginazione per la chiamata e i normali parametri per l'API.
   * @param paginazione IRiscaTablePagination con la definizione dei dati per la paginazione.
   * @returns HttpParams con i parametri generati insieme alla paginazione.
   */
  createPagingParams(
    paginazione: IRiscaTablePagination = {
      currentPage: 1,
      elementsForPage: 10,
      sortBy: '',
      sortDirection: RiscaSortTypes.nessuno,
      total: 0,
    },
    params?: DynamicObjAny
  ): HttpParams {
    // Verifico l'input
    if (!paginazione) {
      // Non ritorno niente
      return null;
    }

    // Creo oggetto con parametri
    const parsedParams = this.createPagingParamsItem(paginazione);
    // Trasformo i parametri
    return this.createQueryParams({ ...params, ...parsedParams });
  }

  /**
   * Crea un oggetto con i parametri della paginazione, ma non convertiti per essere usati nelle chiamate al BE
   * @param paginazione IRiscaTablePagination con i parametri della paginazione
   * @returns un oggetto { offset: number, limit: number, sort: number }
   */
  createPagingParamsItem(
    paginazione: IRiscaTablePagination = {
      currentPage: 1,
      elementsForPage: 10,
      sortBy: '',
      sortDirection: RiscaSortTypes.nessuno,
      total: 0,
    }
  ) {
    let sorting = '';
    // Ordinamento
    const paging = new RiscaTablePagination(paginazione);
    switch (paging.sortDirection) {
      case RiscaSortTypes.crescente: {
        sorting = RiscaServicesSortTypes.crescente + paginazione.sortBy;
        break;
      }
      case RiscaSortTypes.decrescente: {
        sorting = RiscaServicesSortTypes.decrescente + paginazione.sortBy;
        break;
      }
      default: {
        sorting = RiscaServicesSortTypes.nessuno;
      }
    }
    // Creo oggetto con parametri
    const parsedParams = {
      offset: paginazione.currentPage,
      limit: paginazione.elementsForPage,
      sort: sorting,
    };

    return parsedParams;
  }

  /**
   * Funzione di supporto che gestisce la descrizione delle select, mediante logica: se esiste data fine validità, la metto nella descrizione.
   * @param o any contenente le informazioni dell'oggetto.
   * @param p string che definisce il nome della property di riferimento per la descrizione.
   * @returns string con la label da visualizzare nella select.
   */
  addDesSelectDataFineVal(o: any, p: string): string {
    // Richiamo la funzione di utility
    return addDesSelectDataFineVal(o, p);
  }

  /**
   * Funzione di supporto che aggiunge la descrizione di default per la gestione delle option della select.
   * @param d string che definisce la data da inserire nella label.
   * @param p string che definisce il prefisso della label da generare.
   * @param s string che definisce il suffisso della label da generare.
   * @returns string con la label da visualizzare nella select.
   */
  desSelectDataFineVal(d: string, p?: string, s?: string): string {
    // Richiamo la funzione di utility
    return desSelectDataFineVal(d, p, s);
  }

  /**
   * Funzione di supporto per gestire la descrizione delle opzioni con data fine validità.
   * @param o any con i dati dell'oggetto da rimappare.
   * @param p string che indica la proprietà da visualizzare per la option.
   * @returns string con la descrizione dell'oggetto.
   */
  customOptionDesDataFineVal(o: any, p: string): string {
    // Definisco la label da visuallizare
    let label = `${o[p] || ''}`;

    // Verifico se l'oggetto ha la proprietà data_fine_validita
    if (o.data_fine_validita) {
      // Converto la data server in data view
      const dataView = this.convertServerDateToViewDate(o.data_fine_validita);
      // Concateno la label con l'extra testo
      label = this.desSelectDataFineVal(dataView, label);
    }

    // Concateno le informazioni
    return label;
  }

  /**
   * Funzione di supporto per gestire gli stili delle opzioni per le options delle select con gestione data fine validità.
   * @param o any con i dati dell'oggetto da verificare/gestire lo stile'.
   * @returns DynamicObjString con gli stili d'applicare alla option.
   */
  customOptionStyleDataFineVal(o: any): DynamicObjString {
    // Definisco un contenitore per gli stili
    const style = {};
    // Verifico se l'oggetto ha la proprietà data_fine_validita
    if (o.data_fine_validita) {
      // Aggiungo agli stili per evidenziare la riga
      style['color'] = '#00B4E8';
    }
    // Concateno le informazioni
    return style;
  }

  /**
   * Funzione di supporto effettua dei controlli e tenta il parse di un oggetto.
   * @param o any da verificare e parsare.
   * @returns any come oggetto parsato.
   */
  riscaJSONParse(o: any): any {
    // Richiamo la funzione
    return riscaJSONParse(o);
  }

  /**
   * Funzione che estrae e imposta come valore di default un oggetto all'interno di un form e come definizione di array per il componente risca-select.
   * @param f FormGroup da valorizzare.
   * @param fcn FormControlName che definisce il campo del form da settare.
   * @param list Array di any dalla quale cercare il default.
   * @param element any che definisce le informazioni per la definizione dell'elemento di default.
   * @param find Funzione che definisce le logiche di match tra la lista e l'elemento passato in input.
   * @param formPropagation FormUpdatePropagation che definisce in che modo la propagazione del dato deve avvenire.
   */
  setFormValueAndSelect(
    f: FormGroup,
    fcn: string,
    list: any[],
    element: any,
    find: (a: any, b: any) => boolean,
    formPropagation?: FormUpdatePropagation
  ) {
    // Verifico l'input
    if (!f || !fcn || !list || !element || !find) {
      // Servono tutti gli elementi per poter effettuare l'automatismo
      return;
    }

    // Variabile di comodo
    const o = formPropagation;

    // Lancio una sanitizzazione degli oggetti qualora ci siano già dei default
    this.sanitizeRiscaSelectedFlag(list);

    // Vado a recuperare l'oggetto ricercato
    const elementFound = list.find((eList: any) => {
      // Ritorno la funzione di match
      return find(eList, element);
    });

    // Verifico sia stato trovato l'oggetto
    if (elementFound) {
      // Aggiorno l'oggetto referenziato per risca-select
      elementFound.__selected = true;
      // Imposto l'oggetto come valore
      this.setFormValue(f, fcn, elementFound, o);
    }
  }

  /**
   * Funzione che estrae e imposta come valore di default un oggetto all'interno di un form e come definizione di array per il componente risca-select, in modalità multi selezione.
   * @param f FormGroup da valorizzare.
   * @param fcn FormControlName che definisce il campo del form da settare.
   * @param list Array di any dalla quale cercare il default.
   * @param elements any che definisce le informazioni per la definizione per gli elementi di default.
   * @param find Funzione che definisce le logiche di match tra la lista e l'elemento passato in input.
   * @param formPropagation FormUpdatePropagation che definisce in che modo la propagazione del dato deve avvenire.
   */
  setFormValueAndMultiSelect(
    f: FormGroup,
    fcn: string,
    list: any[],
    elements: any[],
    find: (a: any, b: any) => boolean,
    formPropagation?: FormUpdatePropagation
  ) {
    // Verifico l'input
    if (!f || !fcn || !list || !elements || !find) {
      // Servono tutti gli elementi per poter effettuare l'automatismo
      return;
    }

    // Variabile di comodo
    const o = formPropagation;
    // Lancio una sanitizzazione degli oggetti qualora ci siano già dei default
    this.sanitizeRiscaSelectedFlag(list);

    // Definisco un array di supporto come contenitore degli oggetti trovati nella lista
    let elementsDefault: any[] = [];
    // Ciclo la lista degli elementi definiti come default
    elements.forEach((element: any) => {
      // Vado a recuperare l'oggetto ricercato
      const elementFound = list.find((eList: any) => {
        // Ritorno la funzione di match
        return find(eList, element);
      });

      // Verifico se ho trovato effettivamente un oggetto
      if (elementFound) {
        // Aggiungo l'elemento trovato alla lista
        elementsDefault.push(elementFound);
        // #
      }
    });

    // Verifico se sono stati trovati oggetti
    if (elementsDefault?.length > 0) {
      // Ciclo gli oggetti e aggiorno la proprietà __selected
      for (let i = 0; i < elementsDefault.length; i++) {
        // Aggiorno la proprietà dell'oggetto
        elementsDefault[i].__selected = true;
        // #
      }
      // Imposto l'oggetto come valore
      this.setFormValue(f, fcn, elementsDefault, o);
    }
  }

  /**
   * Funzione che estrae e imposta come valore di default un oggetto all'interno di un form e come definizione di array per il componente risca-select.
   * @param f FormGroup da valorizzare.
   * @param fcn FormControlName che definisce il campo del form da settare.
   * @param list Array di any dalla quale cercare il default.
   * @param element any che definisce le informazioni per la definizione dell'elemento di default.
   * @param find Funzione che definisce le logiche di match tra la lista e l'elemento passato in input.
   * @param formPropagation FormUpdatePropagation che definisce in che modo la propagazione del dato deve avvenire.
   */
  setFormAnnoAndSelect(
    f: FormGroup,
    fcn: string,
    list: IRiscaAnnoSelect[],
    year?: number,
    formPropagation?: FormUpdatePropagation
  ) {
    // Variabili di comodo
    const find = (a: IRiscaAnnoSelect, b: IRiscaAnnoSelect) => {
      // Confronto tra oggetti IRiscaAnnoSelect
      return a.anno == b.anno;
    };

    // Verifico la tipologia dell'input
    year = year ?? new Date().getFullYear();
    const anno = year;

    // Imposto il valore nel form
    this.setFormValueAndSelect(f, fcn, list, { anno }, find, formPropagation);
  }

  /**
   * Funzione di verifica formale sulle informazioni per i dati tecnici per il componente dt.
   * Se la verifica va a buon fine, viene ritornata la referenza del componente definita nella configurazione.
   * @param config IVerifyComponeteDt con la configurazione dei dati per verifica e recupero.
   * @returns any che contiene la referenza del componente definito nelle configurazioni.
   */
  verifyAndGetComponentDt(config: IVerifyComponeteDt): any {
    // Estraggo le informazioni dall'oggetto di configurazione
    const { componenteDt, componentiLocali, prefissoCDt, suffissoCDt } =
      config ?? {};

    // Verifico l'esistenza dell'oggetto
    if (!componenteDt || !componentiLocali) {
      // Definisco un messaggio d'errore
      const errore = 'No config for ComponentDt';
      // Lancio l'errore
      throw new Error(errore);
    }

    // Genero l'etichetta per il recupero del componente
    const prefix = prefissoCDt ?? '';
    const suffix = suffissoCDt ?? '';
    const dtName = componenteDt.nome_componente_dt ?? '';
    const key = `${prefix}${dtName}${suffix}`.trim();

    // Verifico che la configurazione esista tra i componenti caricabili
    if (!componentiLocali.hasOwnProperty(key)) {
      // Definisco un messaggio d'errore
      const errore = `Component [${key}] not defined`;
      // Lancio l'errore
      throw new Error(errore);
    }

    // Ritorno il componente da caricare
    return componentiLocali[key];
  }

  /**
   * Genera un array di anni a partire dalla data start-fromBefore fino all'anno start+tillAfter.
   * @param start number come data di riferimento.
   * @param fromBefore number con il numero di anni prima della data di riferimento.
   * @param tillAfter number con il numero di anni dopo della data di riferimento.
   * @returns IRiscaAnnoSelect[] con la lista di anni generati.
   */
  generateAnni(
    start: number,
    fromBefore?: number,
    tillAfter?: number
  ): IRiscaAnnoSelect[] {
    return generateAnni(start, fromBefore, tillAfter);
  }

  /**
   * Funzione di utility che gestisce le informazioni per la funzione di verify pratiche e stati debitori.
   * La funzione ritornerà il numero e la label da visualizzare.
   * @param verifica VerificaPraticheStDebitoriVo con le informazioni del verify generate.
   * @returns VerificaPStDInfo con le informazioni per la visualizzazione dei messaggi.
   */
  verifyPStDInfo(verifica: VerificaPraticheStDebitoriVo): VerificaPStDInfo {
    // Verifico l'input
    if (!verifica) {
      // Ritorno undefined
      return undefined;
    }

    // Definisco le variabili per le proprietà dell'oggetto
    let label = '';
    let quantita = 0;

    // Estraggo le informazioni dall'oggetto in input
    const { num_riscossioni, num_statdeb } = verifica;
    // Gestisco le logiche sulla base delle informazioni in input
    if (num_riscossioni > 0) {
      // Assegno la quantita
      quantita = num_riscossioni;
      // Per la label controllo se è un singolo oggetto
      label = quantita > 1 ? 'Pratiche' : 'Pratica';
      // #
    } else if (num_statdeb > 0) {
      // Assegno la quantita
      quantita = num_statdeb;
      // Per la label controllo se è un singolo oggetto
      label = quantita > 1 ? 'Stati Debitori' : 'Stato debitorio';
      // #
    } else {
      // Gestisco un default di sicurezza
      label = '[RISCA ERROR] Invalid quantity';
      quantita = -1;
    }

    // Ritorno l'oggetto con le informazioni
    return { label, quantita };
  }

  /**
   * Funzione di comodo che converte il risultato di controllo su un oggetto VerificaPraticheStDebitoriVo in un oggetto di comodo per il chiamante.
   * @param verificaPStDVo VerificaPraticheStDebitoriVo con il risultato del controllo.
   * @returns IResultVerifyRStDettaglio con le informazioni di comodo.
   */
  dettaglioFEVerifyPStD(
    verificaPStDVo: VerificaPraticheStDebitoriVo
  ): IResultVerifyRStDDettaglio {
    // Verifico le informazioni all'interno della response
    const { num_riscossioni, num_statdeb } = verificaPStDVo;
    // Verifico se il contenuto contiene informazione
    const existPS = num_riscossioni > 0;
    const existStDS = num_statdeb > 0;
    // Compongo l'oggetto di risposta finale
    const rMap: IResultVerifyRStDDettaglio = {
      dettaglio: verificaPStDVo,
      isObjCollegato: existPS || existStDS,
    };
    // Ritorno il dato mappato
    return rMap;
  }

  /**
   * Funzione di comodo che recupera le informazioni di dettaglio partendo da un oggetto: .
   * @param verifica IResultVerifyRStDDettaglio con le informazioni riguardanti il risultato della verifica.
   * @returns VerificaPStDInfo con le informazioni di dettaglio estratte.
   */
  getVerificaPStDInfo(verifica: IResultVerifyRStDDettaglio): VerificaPStDInfo {
    // Recupero le informazioni per la pratica/st debitori
    const { dettaglio } = verifica || {};
    // Definisco le informazioni da andare a sostituire nel messaggio
    return this.verifyPStDInfo(dettaglio);
  }

  /**
   * Funzione che gestisce e ritorna un messaggio d'errore sulla base dell'oggetto di verifica.
   * @param verifica IResultVerifyRStDDettaglio con l'oggetto di controllo generato dalla funzione. Contiene il risultato dei controlli.
   * @param code string che definisce il codice per il recupero del messaggio.
   * @returns string con il messaggio restituito.
   */
  msgFromVerificaPStD(
    verifica: IResultVerifyRStDDettaglio,
    code: string
  ): string {
    // Recupero le informazioni per la pratica/st debitori
    const { dettaglio } = verifica || {};

    // Definisco le informazioni da andare a sostituire nel messaggio
    const info = this.verifyPStDInfo(dettaglio);

    // Definisco le configurazioni per la generazione dell'errore
    const phs = [info.quantita, info.label];
    // Recupero il messaggio con placeholder
    return this._riscaMessages.getMessageWithPlacholderByCode(code, phs);
  }

  /**
   * Funzione di supporto che definisce le logiche di recupero per l'oggetto recapito collegato all'indirizzo di spedizione.
   * @param indirizzoSpedizione IndirizzoSpedizioneVo contenente le informazioni dell'indirizzo di spedizione.
   * @param recapiti Array di RecapitoVo con le informazioni relative ai recapiti di gestione. Verrà selezionato in base alle informazioni dell'indirizzo di spedizione.
   * @returns RecapitoVo come oggetto di riferimento per l'indirizzo di spedizione.
   */
  estraiRecapitoIS(
    indirizzoSpedizione: IndirizzoSpedizioneVo,
    recapiti: RecapitoVo[]
  ): RecapitoVo {
    // Richiamo la funzione di utility
    return estraiRecapitoIS(indirizzoSpedizione, recapiti);
  }

  /**
   * Funzione di supporto che definisce le logiche di recupero per l'oggetto recapito collegato all'indirizzo di spedizione.
   * @param indirizzoSpedizione IndirizzoSpedizioneVo contenente le informazioni dell'indirizzo di spedizione.
   * @param gruppi Array di Gruppo con le informazioni relative ai gruppi di gestione. Verrà selezionato in base alle informazioni dell'indirizzo di spedizione.
   * @returns Gruppo come oggetto di riferimento per l'indirizzo di spedizione.
   */
  estraiGruppoIS(
    indirizzoSpedizione: IndirizzoSpedizioneVo,
    gruppi: Gruppo[]
  ): Gruppo {
    // Richiamo la funzione di utility
    return estraiGruppoIS(indirizzoSpedizione, gruppi);
  }

  /**
   * Funzione di supporto che imposta un array di messaggi d'errore, estrapolandoli da un oggetto di errore lato server.
   * @param e RiscaServerError le informazioni d'errore generate dal server.
   * @returns Array di string, con la lista dei messaggi d'errore.
   */
  errorsMessageByRiscaServerError(e: RiscaServerError): string[] {
    // Verifico l'input
    if (!e || !e.errors || e.errors.length === 0) {
      // Ritorno un errore generico
      return [this._riscaMessages.getMessage(RiscaNotifyCodes.E005)];
      // #
    }

    // Estraggo la lista dei codici d'errore
    const { errors } = e;
    // Richiamo la funzione per l'estrazione dei messaggi d'errore
    return this.errorsMessageByRiscaServerErrorInfos(errors);
  }

  /**
   * Funzione di supporto che imposta un array di messaggi d'errore, estrapolandoli da un oggetto di errore lato server.
   * @param errors Array di RiscaServerErrorInfo con gli errori generati dal server.
   * @returns Array di string, con la lista dei messaggi d'errore.
   */
  errorsMessageByRiscaServerErrorInfos(
    errors: RiscaServerErrorInfo[]
  ): string[] {
    // Verifico l'input
    if (!errors || errors.length === 0) {
      // Ritorno un errore generico
      return [this._riscaMessages.getMessage(RiscaNotifyCodes.E005)];
      // #
    }

    // Estraggo la lista dei codici d'errore
    const errorCodes = errors.map((e: RiscaServerErrorInfo) => {
      // Ritorno il codice d'errore
      return e?.code;
    });

    // Recupero e ritorno i messaggi d'errore
    return errorCodes.map((ec: string) => {
      // Richiamo il servizio per il recupero del messaggio
      return this._riscaMessages.getMessage(ec);
    });
  }

  /**
   * Formatta un importo mettendo una virgola a separare i decimali ed il punto per separare le migliaia.
   * La formattazione troncherà i numeri decimali.
   * @param importo number | string da formattare.
   * @param decimal number con il numero di decimali da mandatenere.
   * @param decimaliNonSignificativi boolean che definisce se, dalla gestione dei decimali, bisogna completare i decimali con gli 0 mancanti (non significativi). Per default è false.
   * @returns string con la formattazione applicata.
   */
  formatoImportoITA(
    importo: number | string,
    decimals?: number,
    decimaliNonSignificativi: boolean = false
  ): string {
    // Richiamo la funzione di comodo
    return formatoImportoITA(importo, decimals, decimaliNonSignificativi);
    // #
  }

  /**
   * Formatta un importo italiano in un numero compatibile con javascript.
   * @param importo string da formattare.
   * @returns number con il numero correttamente convertito.
   */
  importoITAToJsFloat(importo: string): number {
    // Richiamo la funzione di comodo
    return importoITAToJsFloat(importo);
    // #
  }

  /**
   * Forza la formattazione di un importo gestendo il troncamento dei decimali e assicurandosi che sia un numero gestibile.
   * @param importo number | string da formattare.
   * @param decimal number con il numero di decimali da mandatenere.
   * @returns number con l'importo fomattato.
   */
  forzaFormattazioneImporto(
    importo: number | string,
    decimals?: number
  ): number {
    // Ritorno la concatenazione delle funzioni di formattazione
    return importoITAToJsFloat(formatoImportoITA(importo, decimals));
    // #
  }

  /**
   * Funzione che costruisce il testo da mostrare in tabella nella colonna Quantità della ricerca pratiche.
   * @param quantitaDa limite sinistro della ricerca.
   * @param quantitaA limite destro della ricerca.
   * @returns stringa formata come "Da -", "Da - a -" o "Fino a -".
   */
  quantitaDaADescrittiva(
    quantitaDa: number = null,
    quantitaA: number = null
  ): string {
    // Richiamo la funzione di comodo
    return quantitaDaADescrittiva(quantitaDa, quantitaA);
    // #
  }

  /**
   * Funzione di comodo che estrae il recapito principale di un soggetto.
   * @param soggetto SoggettoVo dalla quale estrarre il recapito principale.
   * @returns RecapitoVo con il recapito principale.
   */
  recapitoPrincipaleSoggetto(soggetto: SoggettoVo): RecapitoVo {
    // Richiamo la funzioni dal file di utility
    return recapitoPrincipaleSoggetto(soggetto);
  }

  /**
   * Funzione di comodo che estrae i reapiti alternativi di un soggetto.
   * @param soggetto SoggettoVo dalla quale estrarre i recapiti alternativi.
   * @returns RecapitoVo[] con i recapiti alternativi.
   */
  recapitiAlternativiSoggetto(soggetto: SoggettoVo): RecapitoVo[] {
    // Richiamo la funzioni dal file di utility
    return recapitiAlternativiSoggetto(soggetto);
  }

  /**
   * Funzione di comodo che estrae il recapito principale da una pratica.
   * @param pratica PraticaVo dalla quale estrarre il recapito principale.
   * @returns RecapitoVo con il recapito principale.
   */
  recapitoPrincipalePratica(pratica: PraticaVo): RecapitoVo {
    // Richiamo la funzioni dal file di utility
    return recapitoPrincipalePratica(pratica);
  }

  /**
   * Funzione di comodo che estrae il possibile recapito alternativo di una pratica.
   * @param pratica PraticaVo dalla quale estrarre i recapiti alternativi.
   * @returns RecapitoVo con i recapiti alternativi.
   */
  recapitoAlternativoPratica(pratica: PraticaVo): RecapitoVo {
    // Richiamo la funzioni dal file di utility
    return recapitoAlternativoPratica(pratica);
  }

  /**
   * Funzione che verifica il cambio di tab di un componente.
   * Se avviene un cambio verso una nuova tab e la sezione in input è attiva, allora ritorno true.
   * @param tabs IRiscaTabChanges che definisce l'oggetto per il cambio della tab.
   * @param sectionToCheck any con la sezione da verificare.
   * @returns boolean che definisce se la sta per avvenire un cambio di sezione, rispetto a quella da controllare.
   */
  movingFromTab(tabs: IRiscaTabChanges, sectionToCheck: any): boolean {
    // Verifico se il tab in input è attualmente attiva
    const isCurrentTab = tabs?.actual === sectionToCheck;
    // Verifico se il tab di destinazione è un'altra
    const isNextOther = tabs?.next !== sectionToCheck;

    // Ritorno la combinazione di controlli
    return isCurrentTab && isNextOther;
  }

  /**
   * Funzione che verifica il cambio di tab di un componente.
   * Se avviene un cambio verso una specifica tab e la sezione in input non è attiva, allora ritorno true.
   * @param tabs IRiscaTabChanges che definisce l'oggetto per il cambio della tab.
   * @param sectionToCheck any con la sezione da verificare.
   * @returns boolean che definisce se la sta per avvenire un cambio di sezione, rispetto a quella da controllare.
   */
  movingIntoTab(tabs: IRiscaTabChanges, sectionToCheck: any): boolean {
    // Verifico se il tab in input non è attualmente attivo
    const isOtherTab = tabs?.actual !== sectionToCheck;
    // Verifico se il tab di destinazione è quello da verificare
    const isNextCustom = tabs?.next === sectionToCheck;

    // Ritorno la combinazione di controlli
    return isOtherTab && isNextCustom;
  }

  /**
   * Funzione di comodo che verifica se l'input è un oggetto Date.
   * @param d Date da verificare.
   * @returns boolean che definisce se l'input è una Date.
   */
  isDate(d: Date): boolean {
    // Richiamo la funzione di controllo
    return isDate(d);
  }

  /**
   * Funzione di comodo che verifica se un oggetto possiede la proprietà per le date validità.
   * Nello specifico, verrà controllato se esiste la proprietà "data_fine_validita" ed il suo valore.
   * @param o any contenente le informazioni per le date di validità.
   * @returns boolean che definisce se l'oggetto è attualmente valido: non esiste data_fine_validita, oppure la data fine validita è successiva ad "oggi".
   */
  isDataValiditaAttiva(o: any): boolean {
    // Richiamo la funzione di controllo
    return isDataValiditaAttiva(o);
  }

  /**
   * Prende il nome del file da un path con '/' come separatori.
   * @param path string con il path del file
   */
  convertFilePathToFileName(path: string, separatore: string = '/'): string {
    // Controllo l'esistenza del path
    if (path == undefined || path == '') {
      return '';
    }
    // Provo a prendere il nome del file
    try {
      // Splitto il path
      const splitPath = path.split(separatore);
      // Prendo l'ultimo indice
      const lastindex = splitPath.length - 1;
      // Prendo l'ultimo elemento
      const filename = splitPath[lastindex];
      // Ritorno il filename
      return filename;
      // #
    } catch {
      // Se in errore, ritorno stringa vuota: non è stato possibile ritornare un nome per il file
      return '';
    }
  }

  /**
   * Funzione di supporto che gestisce l'etichetta riferita allo stato dello stato debitorio secondo il flag annullato.
   * @param sd StatoDebitorioVo con le informazioni per il check sullo stato.
   * @returns string con l'etichetta per la definizione dello stato in base al flag annullato.
   */
  statoSDFlagAnnullato(sd: StatoDebitorioVo): string {
    // Richiamo la funzione di utility
    return statoSDFlagAnnullato(sd);
  }

  /**
   * Funzione di comodo che effettua il troncamento di un numero ad una data cifra decimale. Se non definita una cifra decimale, tutti i decimali verranno rimossi.
   * @param n number da troncare.
   * @param decimal number che definisce a quale cifra decimale troncare. Per default è: 2.
   * @returns number troncato.
   */
  mathTruncate(n: number, decimal = 2): number {
    // Richiamo la funzione di utility
    return mathTruncate(n, decimal);
  }

  /**
   * Funzione che permette di aggiornare tutte le proprietà di un oggetto, con le stesse proprietà di un altro oggetto (dello stesso tipo) in input.
   * La funzione lavorerà sul riferimento dell'oggetto base
   * @param base T che definisce l'oggetto sulla quale aggiornare le informazioni.
   * @param update T | Partial<T>che definisce l'oggetto con le informazioni d'aggiornare.
   */
  mergeDataWith<T>(base: T, update: T | Partial<T>) {
    // Richiamo la funzione di utility
    mergeDataWith<T>(base, update);
  }

  /**
   * Funzione che converte determinate codifiche all'interno di una stringa in maniera tale da convertirle in tag HTML.
   * @param stringHTML string che identifica il testo che verrà convertito tramite funzione InnerHTML.
   * @returns string con la stringa formattate gestibile tramite HTML.
   */
  parseInnerHTML(stringHTML: string): string {
    // Richiamo la funzione di utility
    return parseInnerHTML(stringHTML);
  }

  /**
   * Funzione che compara i dati di due paginazioni diverse.
   * A seconda che siano uguali o differenti nei dati, ritorna un valore di check.
   * @param newPag RiscaTablePagination con la nuova paginazione.
   * @param oldPag RiscaTablePagination con la vecchia paginazione.
   * @returns true se la paginazione è cambiata
   */
  samePaginazioni(
    newPag: RiscaTablePagination,
    oldPag: RiscaTablePagination
  ): boolean {
    // Richiamo la funzione di utility
    return samePaginazioni(newPag, oldPag);
  }

  /**
   * Aggiorna la paginazione impostando i nuovi parametri generati dal servizio di ricerca.
   * @param paginazione RiscaTablePagination con l'oggetto di paginazione d'aggiornare.
   * @param newPaginazione RiscaTablePagination con il nuovo oggetto di paginazione.
   */
  updatePaginazione(
    paginazione: RiscaTablePagination,
    newPaginazione: RiscaTablePagination
  ) {
    // Richiamo la funzione di utility
    updatePaginazione(paginazione, newPaginazione);
  }

  /**
   * Funzione che effettua un troncamento con ellipsis ad una stringa.
   * @param input string sulla quale effettuare il troncamento.
   * @param truncAt number con la quantità di caratteri alla quale effettuare il troncamento.
   * @returns string con il risultato del troncamento con ellipsis.
   */
  ellipsisAt(input: string, truncAt: number): string {
    // Richiamo la funzione di utility
    return ellipsisAt(input, truncAt);
  }

  /**
   * Funzione che effettua una replaceAll di una stringa all'interno di un'altra stringa.
   * @param stringa string come base per la sostituzione.
   * @param trovaRegExp RegExp con la regular expression da trovare all'interno della stringa principale.
   * @param sostituisci string con la stringa da sostituire alla stringa trovata.
   * @returns string con le sostituzioni effettuate.
   */
  replaceAll(
    stringa: string = '',
    trovaRegExp: RegExp,
    sostituisci: string = ''
  ): string {
    // Richiamo la funzione di utility
    return replaceAll(stringa, trovaRegExp, sostituisci);
  }

  /**
   * Funzione di arrotondamento per eccesso di un numero.
   * Se non definiti, i decimali sono 0.
   * @param n number con il numero d'arrotondare.
   * @param d number con i decimali da arrotondare.
   */
  arrotondaEccesso(n: number, d: number = 0): number {
    // Richiamo la funzione di utility
    return arrotondaEccesso(n, d);
  }

  /**
   * Funzione di arrotondamento per eccesso di un numero.
   * Se non definiti, i decimali sono 0.
   * @param n number | string con il numero d'arrotondare.
   * @param d number con i decimali da arrotondare.
   */
  arrotondaDifetto(n: number | string, d: number = 0): number {
    // Richiamo la funzione di utility
    return arrotondaDifetto(n, d);
  }

  /**
   * Funzione che "pulisce" un filename da possibili path posizionali sul server.
   * @param filename string con il filename da gestire.
   * @param charSeparator string carattere custom come indicatore d'inizio del filename. Per default è "/".
   * @returns string con il filename pulito.
   */
  clearFileName(filename: string, charSeparator?: string): string {
    // Richiamo la funzione di supporto
    return clearFileName(filename, charSeparator);
  }
}

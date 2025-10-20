import { FormGroup } from '@angular/forms';
import { AccessoElementiAppKeyConsts } from '../../../core/consts/accesso-elementi-app/accesso-elementi-app.consts';
import { CommonConsts } from '../../consts/common-consts.consts';
import { RiscaAlertService } from '../../services/risca/risca-alert.service';
import { RiscaUtilitiesService } from '../../services/risca/risca-utilities/risca-utilities.service';
import { IRFCFormErrorsConfigs, MappaErroriFormECodici } from '../../utilities';
import { RiscaErrorsMap } from '../../utilities/classes/errors-maps';
import { RiscaAlertHelperClass } from '../risca-alert/risca-alert-helper.class';
import { RiscaMessagesService } from './../../services/risca/risca-messages.service';

/**
 * Classe di comodo che definisce delle funzioni comuni per il supporto delle logiche di gestione del routing Risca.
 */
export class FormHelperClass<T> extends RiscaAlertHelperClass {
  /** Oggetto CommonConsts contenente le costanti di uso comune. */
  C_C = new CommonConsts()
  /** Oggetto contenente una serie di costanti per la gestione delle mappe di errori. */
  EM = new RiscaErrorsMap();
  /** Oggetto contenente i valori costanti per le chiavi d'accesso agli elementi dell'app. */
  AEAK_C = AccessoElementiAppKeyConsts;

  /** FormGroup che definisce la struttura del main form. */
  mainForm: FormGroup;
  /** Boolean che tiene traccia dello stato di submit del mainForm. */
  mainFormSubmitted = false;
  /** Array di MappaErroriFormECodici contente la lista degli errori da gestire per il mainForm. */
  formErrors: MappaErroriFormECodici[] = [];

  /**
   * Costruttore.
   */
  constructor(
    riscaAlert: RiscaAlertService,
    riscaMessages: RiscaMessagesService,
    protected _riscaUtilities: RiscaUtilitiesService
  ) {
    // Super
    super(riscaAlert, riscaMessages);
  }

  /**
   * ################################
   * FUNZIONI PER GESTIONE DELLA FORM
   * ################################
   */

  /**
   * Funzione agganciata all'evento di Submit per il mainForm.
   */
  onFormSubmit() {
    // Verifico che la form esisti
    if (!this.mainForm) {
      return;
    }

    // Lancio la funzione di pre-validazione del form
    this.prepareMainFormForValidation();
    // Il form è stato submittato
    this.mainFormSubmitted = true;

    // Verifico che la form sia valida
    if (this.mainForm.valid) {
      // Recupero i dati della form
      const formData: T = this.getMainFormRawValue();
      // Richiamo la funzione che gestirà la success dei dati
      this.onFormSuccess(formData);
      // #
    } else {
      // Recupero le informazioni sugli errori della form
      const errors = this.getFormErrors(this.mainForm);
      // Richiamo la funzione che gestirà l'errore dei dati
      this.onFormErrors(errors);
    }
  }

  /**
   * Funzione di reset del form.
   */
  onFormReset() {
    // Reset della form
    this.mainForm.reset();
    // Pulisco eventuali errori
    this.mainForm.markAsPristine();
    this.mainForm.markAsUntouched();

    // Resetto il flag del mainForm
    this.mainFormSubmitted = false;
  }

  /**
   * Funzione pensata per l'override.
   * Le logiche definite all'interno di questa funzione, permettono di gestire il caso in cui il submit della form è andato a buon fine.
   * @param formData T con i dati generati dalla form.
   */
  protected onFormSuccess(formData: T) {
    // Logica di success
  }

  /**
   * Funzione pensata per l'override.
   * Le logiche definite all'interno di questa funzione, permettono di gestire il caso in cui il submit della form ha generato degli errori.
   * @param errors Array di string contenente tutti gli errori generati dalla form.
   */
  protected onFormErrors(errors: string[]) {
    // Logica di error
  }

  /**
   * Funzione di supporto che gestisce la visualizzazione dei messaggi d'errore per il form group passato in input.
   * @param formGroup FormGroup da verificare.
   * @param errConfigs IRFCFormErrorsConfigs contenente le configurazioni extra per la gestione della funzione.
   * @returns Array di string, contenente la lista degli errori generati dalla form.
   */
  protected getFormErrors(
    formGroup: FormGroup,
    errConfigs?: IRFCFormErrorsConfigs
  ) {
    // Variabili di comodo
    const fg = formGroup;
    const ec = errConfigs;
    // Recupero le configurazioni
    const { formErrors, serverError } = ec || {};

    // Definisco al mappatura degli errori da recuperare
    const me = formErrors || this.formErrors || [];
    // Recupero tutti i messaggi
    const mef = this._riscaUtilities.getAllErrorMessagesFromForm(fg, me);

    // Verifico e recupero un eventuale messaggio d'errore dall'oggetto errore del server
    const mse = this._riscaUtilities.getMessageFromRiscaServerError(serverError);
    // Verifico se esiste il messaggio
    if (mse) {
      // Aggiungo il messaggio alla lista dei messaggi errore
      mef.push(mse);
    }

    // Ritorno la lista di errori
    return mef;
  }

  /**
   * Funzione che permette di definire le logiche per la verifica del mainForm.
   * Questa funzione è nata per essere overridata definendo all'interno logiche di pre-validazione del form.
   * Questa funzione verrà invocata (per defualt, salvo override della funzione [onFormSubmit]) per preparare il mainForm alla validazione.
   * Nel caso in cui ci fossero informazioni esterne da definire all'interno del mainForm (per esempio i dati in tabella), si possono definire le logiche in questa funzione.
   * In caso di override della funzione [prepareMainFormForValidation], ma non della funzione [onFormSubmit], si permetterà all'applicativo di mantenere il flusso standard di gestione del submit.
   */
  protected prepareMainFormForValidation() {
    // Definire le logiche di pre-validazione effettuando un @override della funzione.
  }

  /**
   * Funzione che permette il recupero del raw value del main form del child.
   * @param c any che definisce una configurazione generica che permette di gestire logiche nell'override della funzione.
   * @returns T contente le informazioni del form in modalità raw value.
   */
  protected getMainFormRawValue(c?: any): T {
    // Verifico esista l'oggetto
    if (!this.mainForm) {
      return undefined;
    }
    // Il main form esiste, ritorno i dati del form
    return this.mainForm.getRawValue();
  }

  /**
   * Funzione che permette di avere i valori del form in un determinato momento.
   * Non verranno effettuati controlli di validità, ma tutte le informazioni saranno disponibili.
   * @param c any che definisce una configurazione generica che permette di gestire logiche nell'override della funzione.
   * @returns T contente le informazioni del form in modalità raw value.
   */
  protected getMainFormActualRawValue(c?: any): T {
    // Assegno al form tutte le informazioni esterne
    this.prepareMainFormForValidation();
    // Recupero i dati grezzi del form
    return this.getMainFormRawValue(c);
  }
}

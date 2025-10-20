import {
  Component,
  EventEmitter,
  Input,
  OnDestroy,
  OnInit,
  Output,
} from '@angular/core';
import { FormGroup } from '@angular/forms';
import { clone } from 'lodash';
import { Subscription } from 'rxjs/index';
import { LoggerService } from '../../../../core/services/logger.service';
import { NavigationHelperService } from '../../../../core/services/navigation-helper/navigation-helper.service';
import { NavigationHelperClass } from '../../../classes/navigation/navigation-helper.class';
import { CommonConsts } from '../../../consts/common-consts.consts';
import { RiscaFormSubmitHandlerService } from '../../../services/risca/risca-form-submit-handler.service';
import { RiscaUtilitiesService } from '../../../services/risca/risca-utilities/risca-utilities.service';
import {
  AppActions,
  IRFCFormErrorsConfigs,
  IRFCReqJourneyNavigation,
  IRiscaFormCheck,
  MappaErroriFormECodici,
  RiscaFormStatus,
  RiscaServerError,
} from '../../../utilities';
import { RiscaErrorsMap } from '../../../utilities/classes/errors-maps';
import { IFormChildServiziError } from '../risca-form-parent/utilities/risca-form-parent.interfaces';

@Component({
  selector: 'risca-form-child',
  templateUrl: './risca-form-child.component.html',
  styleUrls: ['./risca-form-child.component.scss'],
})
export class RiscaFormChildComponent<T>
  extends NavigationHelperClass
  implements OnInit, OnDestroy
{
  /** CommonConsts come oggetto per le costanti dell'applicazione. */
  C_C = new CommonConsts();
  /** Classe costante contenente il mapping degli errori per i form. */
  EM = new RiscaErrorsMap();

  /** Input boolean che definisce se visualizzare il pulsante di submit del form. */
  @Input() showSubmit = false;
  /** Input che definisce la chiave per il tree del padre per l'utilizzo del servizio di gestione submit della form. */
  @Input() parentFormKey: string;
  /** Input che definisce la chiave per il tree del figlio per l'utilizzo del servizio di gestione submit della form. */
  @Input() childFormKey: string;

  /** AppActions che definisce di che tipo di azioni verranno fatti sui dati. */
  @Input() modalita: AppActions;

  /** Output con evento che tiene traccia delle modifiche della form. */
  @Output('onFormSubmit') onFormSubmit$ = new EventEmitter<T>();
  /** Output con evento che tiene traccia degli errori della form. */
  @Output('onFormErrors') onFormErrors$ = new EventEmitter<string[]>();
  /** Output con evento che richiede al componente padre di effettuare una snapshot di tutti i form figli. */
  @Output('onJourneyNavigation') onJourneyNavigation$ =
    new EventEmitter<IRFCReqJourneyNavigation>();
  /** Output che emette un evento per la quale il child deve comunicare al parent dei messaggi di notifica. */
  @Output('notifyParent') onNotifyParent$ = new EventEmitter<any>();
  /** Output che emette un evento per la quale il child deve comunicare al parent un errore generato dai servizi. */
  @Output('onChildServiziError') onEmitServiziError$ =
    new EventEmitter<RiscaServerError>();
  /** Output che emette un evento per la quale il child deve comunicare al parent che è in corso un'azione che sta generando nuove informazioni. Principalmente usato per chiudere eventuali alert aperti. */
  @Output('clearData') clearData$ = new EventEmitter<any>();

  /** Subscription che viene invocato quando il componente "parent" emette l'evento di submit per tutti i suoi "children". */
  onParentSubmit: Subscription;
  /** Subscription che viene invocato quando il componente "parent" emette l'evento di reset per tutti i suoi "children". */
  onParentReset: Subscription;

  /** Form group che definisce la struttura della form principale. */
  mainForm: FormGroup;
  /** Boolean che tiene traccia dello stato di submit del mainForm. */
  mainFormSubmitted = false;
  /** Array di MappaErroriFormECodici contente la lista degli errori da gestire per il mainForm. */
  formErrors: MappaErroriFormECodici[] = [];

  /** Boolean che definisce se prima della varifica della validazione, il form deve essere checkato. */
  protected checkValueValidity = false;

  /**
   * Costruttore.
   */
  constructor(
    protected _logger: LoggerService,
    navigationHelper: NavigationHelperService,
    protected _riscaFormSubmitHandler: RiscaFormSubmitHandlerService,
    protected _riscaUtilities: RiscaUtilitiesService
  ) {
    // Richiamo il super
    super(navigationHelper);
  }

  ngOnInit() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }

  ngOnDestroy() {
    // Tento di fare l'unsubscribe dei listener
    try {
      // Verifico che esistano i listener
      if (this.onParentSubmit) {
        this.onParentSubmit.unsubscribe();
      }
      if (this.onParentReset) {
        this.onParentReset.unsubscribe();
      }
    } catch (e) {
      // Loggo l'errore
      this._logger.warning('RiscaFormChildComponent failed to unsubscribe', e);
    }
  }

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
      // Gestisco il success per il submit della form
      this.onFormSuccess(formData);
      // #
    } else {
      // Gestisco la visualizzazione degli errori del form usiForm

      this.onFormErrors(this.mainForm);
    }
  }

  /**
   * Funzione di reset del form e del componente.
   * @param args any con possibili parametri da passare alla funzione.
   */
  onFormReset(...args: any) {
    // Resetto il flag di submit
    this.mainFormSubmitted = false;
    // Reset manuale della form
    this.mainForm.reset();
  }

  /**
   * Funzione di supporto che gestisce il success del submit della form.
   * @param data any contenente le informazioni da emettere al componente padre.
   */
  protected onFormSuccess(data: any) {
    // Emetto l'evento comunicando i messaggi

    this.onFormSubmit$.emit(data);

    this.formSubmitHandler(data, RiscaFormStatus.valid);
  }

  /**
   * Funzione di supporto che gestisce la visualizzazione dei messaggi d'errore per il form group passato in input.
   * @param formGroup FormGroup da verificare.
   * @param errConfigs IRFCFormErrorsConfigs contenente le configurazioni extra per la gestione della funzione.
   */
  protected onFormErrors(
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
    const mse =
      this._riscaUtilities.getMessageFromRiscaServerError(serverError);
    // Verifico se esiste il messaggio
    if (mse) {
      // Aggiungo il messaggio alla lista dei messaggi errore
      mef.push(mse);
    }

    // Emetto l'evento comunicando i messaggi
    this.onFormErrors$.emit(mef);
    // Lancio la funzione per l'emissione dell'evento gestito tramite servizio
    this.formSubmitHandler(mef, RiscaFormStatus.invalid);
  }

  /**
   * Funzione che verifica e compone le informazioni per l'emissione dell'evento onFormChecked del servizio RiscaFormSubmitHandlerService.
   * @param data any contenente le informazioni da emettere al componente padre.
   * @param status RiscaFormStatus che definisce il risultato del check sul form.
   * @param parent string che definisce una chiave specifica per il parent. Se viene definita, prende priorità rispetto a quella settata nel componente.
   * @param child string che definisce una chiave specifica per il child. Se viene definita, prende priorità rispetto a quella settata nel componente.
   */
  protected formSubmitHandler(
    data: any,
    status: RiscaFormStatus,
    parent?: string,
    child?: string
  ) {

    // Imposto le chiavi
    const parentKey = parent || this.parentFormKey;
    const childKey = child || this.childFormKey;
    // Verifico che le chiavi esistano
    if (!parentKey || !childKey) {
      return;
    }

    // Creo l'oggetto da passare all'evento
    const formSubmitData: IRiscaFormCheck = {
      data,
      status,
      parent: parentKey,
      child: childKey,
    };
    
    // Emetto l'evento
    this._riscaFormSubmitHandler.onFormChecked$.next(formSubmitData);
  }

  /**
   * Funzione che permette di definire le logiche per la verifica del mainForm.
   * Questa funzione è nata per essere overridata definendo all'interno logiche di pre-validazione del form.
   * Questa funzione verrà invocata (per defualt, salvo override della funzione [onFormSubmit]) per preparare il mainForm alla validazione.
   * Nel caso in cui ci fossero informazioni esterne da definire all'interno del mainForm (per esempio i dati in tabella), si possono definire le logiche in questa funzione.
   * In caso di override della funzione [prepareMainFormForValidation], ma non della funzione [onFormSubmit], si permetterà all'applicativo di mantenere il flusso standard di gestione del submit.
   */
  prepareMainFormForValidation() {
    // Definire le logiche di pre-validazione effettuando un @override della funzione.
    
  }

  /**
   * Funzione che permette il recupero del raw value del main form del child.
   * @param c any che definisce una configurazione generica che permette di gestire logiche nell'override della funzione.
   * @returns T contente le informazioni del form in modalità raw value.
   */
  getMainFormRawValue(c?: any): T {
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
  getMainFormActualRawValue(c?: any): T {
    // Assegno al form tutte le informazioni esterne
    this.prepareMainFormForValidation();
    // Recupero i dati grezzi del form
    return this.getMainFormRawValue(c);
  }

  /**
   * Funzione che richiede al componente padre di effettuare una navigazione tramite processo di journey.
   * @param c IRFCReqSnapshot contenente le configurazioni per la richiesta di navigazione.
   */
  childReqJourneyNavigation(c?: IRFCReqJourneyNavigation) {
    // Verifico l'input
    if (!c) {
      // Nessuna configurazione, blocco il flusso
      return;
    }
    // Se all'interno dell'oggetto non viene definita la chiave, l'aggiungo di default
    if (!c.childFormKey) {
      // Aggiungo la chiave
      c.childFormKey = this.childFormKey;
    }

    // Emetto l'evento di richiesta snapshot
    this.onJourneyNavigation$.emit(c);
  }

  /**
   * Funzione di notificazione al parent d'informazioni.
   * La funzione è stata aggiornata per gestire anche qualunque altro tipo di dato.
   * @param notify any da notificare.
   */
  protected notifyParent(notify: any) {
    // Emetto l'evento di notifica
    this.onNotifyParent$.emit(clone(notify));
  }

  /**
   * Funzione di comodo che gestisce la segnalazione al componente padre di un errore alla chiamata dei servizi.
   * @param e RiscaServerError con i dettagli dell'errore.
   */
  protected onServiziError(e: RiscaServerError) {
    // Compongo la struttura dati per la propagazione dell'errore
    const data: IFormChildServiziError = {
      child: this.childFormKey,
      error: e,
    };
    // Invoco il servizio per la propagazione dell'errore
    this._riscaFormSubmitHandler.onChildServiziError(data);
    // Utilizzo l'event emitter diretto del componente
    this.onEmitServiziError$.emit(e);
  }

  /**
   * Funzione di setup che può essere invocata nel momento in cui c'è la necessità di rimanere in ascolto dell'evento di "parentSubmit" generato dal servizio RiscaFormSubmitHandlerService.
   * Questa funzione aggancerà l'evento alla Subscription del componente e richiamerà una funzione che permetterà la gestione delle logiche di submit del componente "child".
   * NOTA BENE: ricordarsi di richiamare la destroy della Subscription.
   * @param customParentSubmit (parent: string) => any; che definisce le logiche per la gestione dell'evento di submit del parent.
   */
  protected listenToParentSubmit(customParentSubmit?: (parent: string) => any) {
    // Definisco la funzione di default per l'evento di submit del padre
    const parentSubmitDefault = (parent: string) => {
      // Verifico se l'id del padre è lo stesso
      if (this.parentFormKey === parent) {
        // Richiamo la funzione di submit del componente
        this.onFormSubmit();
      }
    };

    // Definisco la funzione da richiamare sull'evento di submit del padre
    const parentSubmit = customParentSubmit ?? parentSubmitDefault;

    // Aggancio alla Subscription l'evento
    this.onParentSubmit =
      this._riscaFormSubmitHandler.onParentSubmit$.subscribe({
        next: (parent: string) => {
          // Richiamo al funzione di gestione
          parentSubmit(parent);
        },
      });
  }

  /**
   * Funzione di setup che può essere invocata nel momento in cui c'è la necessità di rimanere in ascolto dell'evento di "parentReset" generato dal servizio RiscaFormSubmitHandlerService.
   * Questa funzione aggancerà l'evento alla Subscription del componente e richiamerà una funzione che permetterà la gestione delle logiche di reset del componente "child".
   * NOTA BENE: ricordarsi di richiamare la destroy della Subscription.
   * @param customParentReset (parent: string) => any; che definisce le logiche per la gestione dell'evento di reset del parent.
   */
  protected listenToParentReset(customParentReset?: (parent: string) => any) {
    // Definisco la funzione di default per l'evento di reset del padre
    const parentResetDefault = (parent: string) => {
      // Verifico se l'id del padre è lo stesso
      if (this.parentFormKey === parent) {
        // Richiamo la funzione di reset del componente
        this.onFormReset();
      }
    };

    // Definisco la funzione da richiamare sull'evento di reset del padre
    const parentReset = customParentReset ?? parentResetDefault;

    // Aggancio alla Subscription l'evento
    this.onParentReset = this._riscaFormSubmitHandler.onParentReset$.subscribe({
      next: (parent: string) => {
        // Richiamo al funzione di gestione
        parentReset(parent);
      },
    });
  }

  /**
   * Funzione di supporto che lancia l'evento di clear dei dati per una nuova esecuzione sul child.
   */
  clearData() {
    // Emetto l'evento
    this.clearData$.emit();
  }

  /**
   * ###################
   * FUNZIONI DI UTILITY
   * ###################
   */

  /**
   * Funzione di controllo sulla modalità, se impostatao su: inserimento.
   */
  isInserimento(modalita: AppActions) {
    // Verifico la modalità
    return modalita === AppActions.inserimento;
  }

  /**
   * Funzione di controllo sulla modalità, se impostatao su: modifica.
   */
  isModifica(modalita: AppActions) {
    // Verifico la modalità
    return modalita === AppActions.modifica;
  }

  /**
   * ################
   * GETTER DI COMODO
   * ################
   */

  /**
   * Getter per la modalità se impostata su modifica.
   */
  get inserimento() {
    // Verifico la modalità
    return this.modalita === AppActions.inserimento;
  }

  /**
   * Getter per la modalità se impostata su modifica.
   */
  get modifica() {
    // Verifico la modalità
    return this.modalita === AppActions.modifica;
  }
}

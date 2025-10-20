import {
  Component,
  EventEmitter,
  Input,
  OnDestroy,
  OnInit,
  Output,
} from '@angular/core';
import { FormGroup } from '@angular/forms';
import { clone, uniq } from 'lodash';
import { Subscription } from 'rxjs/index';
import { LoggerService } from '../../../../core/services/logger.service';
import { NavigationHelperService } from '../../../../core/services/navigation-helper/navigation-helper.service';
import { RiscaAlertService } from '../../../services/risca/risca-alert.service';
import { RiscaFormSubmitHandlerService } from '../../../services/risca/risca-form-submit-handler.service';
import { RiscaMessagesService } from '../../../services/risca/risca-messages.service';
import { RiscaUtilitiesService } from '../../../services/risca/risca-utilities/risca-utilities.service';
import {
  IRFCFormErrorsConfigs,
  IRFCReqJourneyNavigation,
  IRiscaFormCheck,
  IRiscaFormTreeParent,
  MappaErroriFormECodici,
  RiscaAlertConfigs,
  RiscaFormStatus,
  RiscaInfoLevels,
  RiscaServerError,
} from '../../../utilities';
import { RiscaErrorsMap } from '../../../utilities/classes/errors-maps';
import {
  IFormChildServiziError,
  SetupFormSubmitHandlerListener,
} from '../risca-form-parent/utilities/risca-form-parent.interfaces';
import { RiscaUtilitiesComponent } from '../risca-utilities/risca-utilities.component';

/**
 * Questo componente è l'unione delle logiche dei componenti:
 * - RiscaFormParentComponent;
 * - RiscaFormChildComponent;
 * Essendo che tramite Mixin le logiche delle due classi NON risultano funzionanti, è stato deciso di costruire un componente unico.
 * Questo permette ai componente di essere sia "padri" che "figli" per le gestioni delle form.
 * NOTA BENE: ricordarsi di cambiare le logiche in entrambi i componenti.
 */
@Component({
  selector: 'risca-form-parent-and-child',
  templateUrl: './risca-form-parent-and-child.component.html',
  styleUrls: ['./risca-form-parent-and-child.component.scss'],
})
export class RiscaFormParentAndChildComponent<T>
  extends RiscaUtilitiesComponent
  implements OnInit, OnDestroy
{
  /**
   * ######
   * PARENT
   * ######
   */

  /** Subscription che viene invocato una volta che tutte le form registrate mediante le chiavi sono state "checkate" come "valid". */
  onFormsValid$: Subscription;
  /** Subscription che viene invocato una volta che tutte le form registrate mediante le chiavi sono state "checkate" come "invalid". */
  onFormsInvalid$: Subscription;
  /** Subscription che viene invocato quando il componente "child" emette l'evento di submit per questo componente "parent". */
  onChildSubmit$: Subscription;
  /** Subscription che viene invocato quando il componente "child" emette l'evento di reset per questo componente "parent". */
  onChildReset$: Subscription;
  /** Subscription che rimane in ascolto dell'evento: onServiziError, generato dai componenti figli. */
  onChildServiziError$: Subscription;

  /** String che definisce la chiave del padre per la gestione del submit form tramite il servizio: RiscaFormSubmitHandlerService. */
  protected parentKey: string;
  /** Array di string che definisce le chiavi dei figli per la gestione del submit form tramite il servizio: RiscaFormSubmitHandlerService. */
  protected childrenKeys: string[] = [];

  /**
   * #####
   * CHILD
   * #####
   */
  /** Classe costante contenente il mapping degli errori per i form. */
  EM = new RiscaErrorsMap();

  /** Input boolean che definisce se visualizzare il pulsante di submit del form. */
  @Input() showSubmit = false;
  /** Input che definisce la chiave per il tree del padre per l'utilizzo del servizio di gestione submit della form. */
  @Input() parentFormKey: string;
  /** Input che definisce la chiave per il tree del figlio per l'utilizzo del servizio di gestione submit della form. */
  @Input() childFormKey: string;

  /** Output con evento che tiene traccia delle modifiche della form. */
  @Output('onFormSubmit') onFormSubmit$ = new EventEmitter<T>();
  /** Output con evento che tiene traccia degli errori della form. */
  @Output('onFormErrors') onFormErrors$ = new EventEmitter<string[]>();
  /** Output con evento che richiede al componente padre di effettuare una navigazione mediante journey. */
  @Output('onJourneyNavigation') onJourneyNavigation$ = new EventEmitter<any>();
  /** Output che emette un evento per la quale il child deve comunicare al parent dei messaggi di notifica. */
  @Output('notifyParent') onNotifyParent$ = new EventEmitter<any>();
  /** Output che emette un evento per la quale il child deve comunicare al parent un errore generato dai servizi. */
  @Output('onChildServiziError') onEmitServiziError$ =
    new EventEmitter<RiscaServerError>();
  /** Output che emette un evento per la quale il child deve comunicare al parent che è in corso un'azione che sta generando nuove informazioni. Principalmente usato per chiudere eventuali alert aperti. */
  @Output('clearData') clearData$ = new EventEmitter<any>();

  /** Subscription che viene invocato quando il componente "parent" emette l'evento di submit per tutti i suoi "children". */
  onParentSubmit$: Subscription;
  /** Subscription che viene invocato quando il componente "parent" emette l'evento di reset per tutti i suoi "children". */
  onParentReset$: Subscription;

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
    riscaAlert: RiscaAlertService,
    protected _riscaFormSubmitHandler: RiscaFormSubmitHandlerService,
    riscaMessages: RiscaMessagesService,
    protected _riscaUtilities: RiscaUtilitiesService
  ) {
    // Richiamo il super
    super(navigationHelper, riscaAlert, riscaMessages);
  }

  ngOnInit() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }

  ngOnDestroy() {
    try {
      // Verifico che esistano i listener
      if (this.onFormsValid$) {
        this.onFormsValid$.unsubscribe();
      }
      if (this.onFormsInvalid$) {
        this.onFormsInvalid$.unsubscribe();
      }
      if (this.onParentSubmit$) {
        this.onParentSubmit$.unsubscribe();
      }
      if (this.onParentReset$) {
        this.onParentReset$.unsubscribe();
      }
      if (this.onChildSubmit$) {
        this.onChildSubmit$.unsubscribe();
      }
      if (this.onChildReset$) {
        this.onChildReset$.unsubscribe();
      }
      if (this.onChildServiziError$) {
        this.onChildServiziError$.unsubscribe();
      }
    } catch (e) {
      // Variabile di comodo
      const m = `RiscaFormParentAndChildComponent failed to unsubscribe`;
      // Loggo l'errore
      this._logger.warning(m, e);
    }
  }

  /**
   * #####################################################
   * FUNZIONALITA' DEL COMPONENTE RiscaFormParentComponent
   * #####################################################
   */

  /**
   * Funzione di setup per il servizio RiscaFormSubmitHandlerService.
   * @param parent string che identifica la chiave del parent per l'albero delle form.
   * @param children Array di string che identica i children del parent all'interno dell'albero delle form.
   * @param listener SetupFormSubmitHandlerListener contenente l'override delle logiche base per il formsValid e/o il formsInvalid.
   */
  setupFormsSubmitHandler(
    parent: string,
    children: string[],
    listener?: SetupFormSubmitHandlerListener
  ) {
    // Verifico l'input
    if (!parent || !children) {
      return;
    }

    // Assegno localmente le chiavi
    this.parentKey = parent;
    this.childrenKeys = children;

    // Aggiungo il nuovo albero all'interno, passando da un reset dei dati
    this._riscaFormSubmitHandler.resetFormTree(parent, children);

    // Definisco le funzioni di default da eseguire al trigger degli eventi
    const formsValidDefault = (formsValid: IRiscaFormTreeParent) => {
      // Verifico se il form è riferito a questo per questo padre
      if (!formsValid[this.parentKey]) {
        return;
      }
      // Richiamo la funzione di gestione del dato emesso
      this.formsValid(formsValid);
    };
    const formsInvalidDefault = (formsInvalid: IRiscaFormTreeParent) => {
      // Verifico se il form è riferito a questo per questo padre
      if (!formsInvalid[this.parentKey]) return;
      // Richiamo la funzione di gestione del dato emesso
      this.formsInvalid(formsInvalid);
    };

    // Definisco le funzioni da eseguire al trigger degli eventi
    const formsValidCB = listener?.onFormsValid || formsValidDefault;
    const formsInvalidCB = listener?.onFormsInvalid || formsInvalidDefault;

    // Definisco le funzioni all'emit dell'evento
    this.onFormsValid$ = this._riscaFormSubmitHandler.onFormsValid$.subscribe(
      (formsValid: IRiscaFormTreeParent) => {
        // Eseguo la funzione del listener
        formsValidCB(formsValid);
      }
    );
    this.onFormsInvalid$ =
      this._riscaFormSubmitHandler.onFormsInvalid$.subscribe(
        (formsInvalid: IRiscaFormTreeParent) => {
          // Eseguo la funzione del listener
          formsInvalidCB(formsInvalid);
        }
      );
  }

  /**
   * Funzione richiamata nel momento in cui viene emesso l'evento: onFormsValid.
   * @param formsValid IRiscaFormTreeParent con i dati validi.
   */
  formsValid(formsValid: IRiscaFormTreeParent) {
    // Gestione delle logiche
  }

  /**
   * Funzione richiamata nel momento in cui viene emesso l'evento: onFormsInvalid.
   * @param formsInvalid IRiscaFormTreeParent con i dati invalidi.
   */
  formsInvalid(formsInvalid: IRiscaFormTreeParent) {
    // Richiamo la funzione di gestione dell'alert per il reset
    this.aggiornaAlertConfigs(this.alertConfigs);

    // Definisco un array per i messaggi di errore
    let messaggi = [];
    // Genero una mappa con gli errori da visualizzare
    const mapErr = this.getRiscaFormTreedData(formsInvalid);

    // Ciclo la mappa e concateno i messaggi d'errore
    for (let messForm of mapErr.values()) {
      // Verifico che esista un valore per la chiave
      const messFormCheck = messForm ?? [];
      // Concateno gli array
      messaggi = messaggi.concat(messFormCheck);
    }

    // Rimuovo possibili messaggi duplicati
    messaggi = uniq(messaggi);

    // Richiamo la funzione di gestione dell'alert
    this.aggiornaAlertConfigs(
      this.alertConfigs,
      messaggi,
      RiscaInfoLevels.danger
    );
  }

  /**
   * Funzione che estrae le informazioni dall'oggetto in input IRiscaFormTreeParent.
   * @param riscaFormTree IRiscaFormTreeParent con i dati invalidi.
   * @param parentKey string contenente la chiave del component padre.
   * @param childrenKeys Array di string contenente le chiavi dei componenti figli.
   * @returns Map di any contenente i valori trovati per ogni chiave per childrenKeys.
   */
  protected getRiscaFormTreedData(
    riscaFormTree: IRiscaFormTreeParent,
    parentKey?: string,
    childrenKeys?: string[]
  ): Map<string, any> {
    // Verifico l'input
    if (!riscaFormTree) {
      return new Map();
    }

    // Definisco le chiavi di recupero con priorità a quelle passate per parametro
    parentKey = parentKey || this.parentKey;
    childrenKeys = childrenKeys || this.childrenKeys;
    // Verifico le chiavi
    if (!parentKey || !childrenKeys) {
      return new Map();
    }

    // Costruisco una nuova mappa
    const mapErr = new Map<string, any>();

    // Ciclo la lista delle chiavi dei figli
    for (let i = 0; i < childrenKeys.length; i++) {
      // Recupero la chiave del figlio
      const childKey = childrenKeys[i];
      // Tento di recuperare la lista di errori
      const errori = riscaFormTree[parentKey][childKey]?.data;
      // Inserisco il valore estratto nella mappa
      mapErr.set(childKey, errori);
    }

    // Ritorno la mappa con gli errori
    return mapErr;
  }

  /**
   * Funzione di intercettazione delle notifiche dai figli.
   * @param alertConfigs RiscaAlertConfigs della notifica.
   */
  protected notificationFromChild(alertConfigs: RiscaAlertConfigs) {
    // Verifico l'input
    if (!alertConfigs) {
      // Blocco le logiche
      return;
    }

    // Recupero le informazioni per l'aggiornamento dei dati
    const a = alertConfigs;
    const m = alertConfigs.messages as string[];
    const t = alertConfigs.type;
    // Aggiorno l'alert configs di base
    this.aggiornaAlertConfigs(a, m, t);
  }

  /**
   * Funzione utilizzata per la gestione della richiesta da parte di un "child" di submit.
   * La funzione è pensata per essere overridata dal componente che estende questo componente.
   */
  submitFromChild() {
    // Gestione delle logiche
  }

  /**
   * Funzione di setup che può essere invocata nel momento in cui c'è la necessità di rimanere in ascolto dell'evento di "childSubmit" generato dal servizio RiscaFormSubmitHandlerService.
   * Questa funzione aggancerà l'evento alla Subscription del componente e richiamerà una funzione che permetterà la gestione delle logiche di submit del componente "parent".
   * NOTA BENE: ricordarsi di richiamare la destroy della Subscription.
   * @param customChildSubmit (child: string) => any; che definisce le logiche per la gestione dell'evento di submit del child.
   */
  protected listenToChildSubmit(customChildSubmit?: (child: string) => any) {
    // Definisco la funzione di default per l'evento di submit del child
    const childSubmitDefault = (child: string) => {
      // Verifico se l'id del child è definito per questo parent
      const ownChild = this.childrenKeys?.some((ck: string) => ck === child);
      // Verifico il risultato
      if (ownChild) {
        // Richiamo la funzione di submit del componente
        this.submitFromChild();
      }
    };

    // Definisco la funzione da richiamare sull'evento di submit del child
    const childSubmit = customChildSubmit ?? childSubmitDefault;

    // Aggancio alla Subscription l'evento
    this.onChildSubmit$ = this._riscaFormSubmitHandler.onChildSubmit$.subscribe(
      {
        next: (child: string) => {
          // Richiamo al funzione di gestione
          childSubmit(child);
        },
      }
    );
  }

  /**
   * Funzione utilizzata per la gestione della richiesta da parte di un "child" di reset.
   * La funzione è pensata per essere overridata dal componente che estende questo componente.
   */
  resetFromChild() {
    // Gestione delle logiche
  }

  /**
   * Funzione di setup che può essere invocata nel momento in cui c'è la necessità di rimanere in ascolto dell'evento di "childReset" generato dal servizio RiscaFormSubmitHandlerService.
   * Questa funzione aggancerà l'evento alla Subscription del componente e richiamerà una funzione che permetterà la gestione delle logiche di reset del componente "parent".
   * NOTA BENE: ricordarsi di richiamare la destroy della Subscription.
   * @param customChildReset (parent: string) => any; che definisce le logiche per la gestione dell'evento di reset del child.
   */
  protected listenToChildReset(customChildReset?: (child: string) => any) {
    // Definisco la funzione di default per l'evento di reset del child
    const childResetDefault = (child: string) => {
      // Verifico se l'id del child è definito per questo parent
      const ownChild = this.childrenKeys?.some((ck: string) => ck === child);
      // Verifico il risultato
      if (ownChild) {
        // Richiamo la funzione di reset del componente
        this.resetFromChild();
      }
    };

    // Definisco la funzione da richiamare sull'evento di submit del child
    const childReset = customChildReset ?? childResetDefault;

    // Aggancio alla Subscription l'evento
    this.onChildReset$ = this._riscaFormSubmitHandler.onChildReset$.subscribe({
      next: (child: string) => {
        // Richiamo al funzione di gestione
        childReset(child);
      },
    });
  }

  /**
   * Funzione che permette di gestire le segnalazioni d'errore generati dai componenti filgi.
   * @param e RiscaServerError con l'errore generato dal servizio del filtro.
   */
  onChildServiziError(e: RiscaServerError) {
    // Gestisco l'errore richiamando il onServiziError
    this.onServiziError(e);
  }

  /**
   * Funzione di setup che può essere invocata nel momento in cui c'è la necessità di rimanere in ascolto dell'evento di "childServiziError" generato dal servizio RiscaFormSubmitHandlerService.
   * Questa funzione aggancerà l'evento alla Subscription del componente e richiamerà una funzione che permetterà la gestione delle logiche di errore generate dai servizi del componente figlio.
   * NOTA BENE: ricordarsi di richiamare la destroy della Subscription.
   * @param customChildSE (data: IFormChildServiziError) => any; che definisce le logiche per la gestione dell'evento di reset del child.
   */
  protected listenToChildSE(
    customChildSE?: (data: IFormChildServiziError) => any
  ) {
    // Definisco la funzione di default per l'evento di reset del child
    const childSEDefault = (data: IFormChildServiziError) => {
      // Verifico se l'id del child è definito per questo parent
      const ownChild = this.childrenKeys?.some((ck: string) => {
        // Confronto le chiavi dei figli
        return ck === data?.child;
      });
      // Verifico il risultato
      if (ownChild) {
        // Richiamo la funzione di reset del componente
        this.onChildServiziError(data.error);
      }
    };

    // Definisco la funzione da richiamare sull'evento di submit del child
    const childSE = customChildSE ?? childSEDefault;

    // Aggancio alla Subscription l'evento
    this.onChildServiziError$ =
      this._riscaFormSubmitHandler.onChildServiziError$.subscribe({
        next: (data: IFormChildServiziError) => {
          // Richiamo al funzione di gestione
          childSE(data);
        },
      });
  }

  /**
   * ####################################################
   * FUNZIONALITA' DEL COMPONENTE RiscaFormChildComponent
   * ####################################################
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
   */
  onFormReset() {
    // Resetto il flag di submit
    this.mainFormSubmitted = false;
    // Submit manuale della form
    this.mainForm.reset();
  }

  /**
   * Funzione di supporto che gestisce il success del submit della form.
   * @param data any contenente le informazioni da emettere al componente padre.
   */
  protected onFormSuccess(data: any) {
    // Emetto l'evento comunicando i messaggi
    this.onFormSubmit$.emit(data);
    // Lancio la funzione per l'emissione dell'evento gestito tramite servizio
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
    this.onParentSubmit$ =
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
    this.onParentReset$ = this._riscaFormSubmitHandler.onParentReset$.subscribe(
      {
        next: (parent: string) => {
          // Richiamo al funzione di gestione
          parentReset(parent);
        },
      }
    );
  }

  /**
   * Funzione di supporto che lancia l'evento di clear dei dati per una nuova esecuzione sul child.
   */
  clearData() {
    // Emetto l'evento
    this.clearData$.emit();
  }
}

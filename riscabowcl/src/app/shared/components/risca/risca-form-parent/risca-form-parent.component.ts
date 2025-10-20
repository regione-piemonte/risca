import { Component, OnDestroy, OnInit } from '@angular/core';
import { uniq } from 'lodash';
import { Subscription } from 'rxjs/index';
import { LoggerService } from '../../../../core/services/logger.service';
import { NavigationHelperService } from '../../../../core/services/navigation-helper/navigation-helper.service';
import { RiscaAlertService } from '../../../services/risca/risca-alert.service';
import { RiscaFormSubmitHandlerService } from '../../../services/risca/risca-form-submit-handler.service';
import { RiscaMessagesService } from '../../../services/risca/risca-messages.service';
import {
  IRiscaFormTreeParent,
  RiscaAlertConfigs,
  RiscaInfoLevels,
  RiscaServerError,
} from '../../../utilities';
import { RiscaUtilitiesComponent } from '../risca-utilities/risca-utilities.component';
import {
  IFormChildServiziError,
  SetupFormSubmitHandlerListener,
} from './utilities/risca-form-parent.interfaces';

@Component({
  selector: 'risca-form-parent',
  templateUrl: './risca-form-parent.component.html',
  styleUrls: ['./risca-form-parent.component.scss'],
})
export class RiscaFormParentComponent
  extends RiscaUtilitiesComponent
  implements OnInit, OnDestroy
{
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
   * Costruttore.
   */
  constructor(
    protected _logger: LoggerService,
    navigationHelper: NavigationHelperService,
    riscaAlert: RiscaAlertService,
    protected _riscaFormSubmitHandler: RiscaFormSubmitHandlerService,
    riscaMessages: RiscaMessagesService
  ) {
    // Richiamo il super
    super(navigationHelper, riscaAlert, riscaMessages);
  }

  ngOnInit() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }

  ngOnDestroy() {
    // Tento di rimuovere il parent key dal servizio
    this._riscaFormSubmitHandler.deleteFormTree(this.parentKey);

    // Tento di fare l'unsubscribe dei listener
    try {
      // Verifico che esistano i listener
      if (this.onFormsValid$) {
        this.onFormsValid$.unsubscribe();
      }
      if (this.onFormsInvalid$) {
        this.onFormsInvalid$.unsubscribe();
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
      // Loggo l'errore
      this._logger.warning('RiscaFormParentComponent failed to unsubscribe', e);
    }
  }

  /**
   * Funzione di setup per il servizio RiscaFormSubmitHandlerService.
   * @param parent string che identifica la chiave del parent per l'albero delle form.
   * @param children Array di string che identica i children del parent all'interno dell'albero delle form.
   * @param listener SetupFormSubmitHandlerListener contenente l'override delle logiche base per il formsValid e/o il formsInvalid.
   */
  setFormsSubmitHandler(
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
      if (!formsInvalid[this.parentKey]) {
        return;
      }
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
    this.resetAlertConfigs(this.alertConfigs);

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

    // Rimuovo i messaggi duplicati
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
    const a = this.alertConfigs;
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
   * @param customChildReset (child: string) => any; che definisce le logiche per la gestione dell'evento di reset del child.
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
}

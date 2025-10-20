import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { LoggerService } from '../../../core/services/logger.service';
import { IFormChildServiziError } from '../../components/risca/risca-form-parent/utilities/risca-form-parent.interfaces';
import {
  IRiscaFormCheck,
  IRiscaFormTreeParent,
  RiscaFormStatus,
} from '../../utilities';

/**
 * Servizio di utility che permette di gestire il submit asincrono di diverse form, data una configurazione di chiavi/valore.
 * Mappa funzionale logica del servizio:
 * 1) Il componente padre registra una mappa di chiavi (identificativo per un form) e valore (il default verrà impostato su "waiting");
 * 2) Il componente padre triggera il submit delle form dei figli;
 * 3) I componenti figli, una volta validate le form, utilizzeranno l'evento "onFormChecked" definendo un oggetto dati, o una lista di errori, con il relativo status "valid" o "invalid";
 * 4) Una volta che tutti i componenti figli avranno emesso l'evento "onFormChecked", il servizio analizzerà le informazioni e a seconda dello stato delle form, verrà emesso l'evento di validazione form (onFormsValid) o d'invalidazione delle form (onFormsValid) passando come parametro un array di dati.
 */
@Injectable({
  providedIn: 'root',
})
export class RiscaFormSubmitHandlerService {
  /** Map che gestisce i "flag" di validazione/invalidazione delle form. */
  private _formTreesStatus = new Map<string, Map<string, IRiscaFormCheck>>();

  /** Subject che permette di registare un valore all'interno della Map del servizio. Generalmente, questo viene utilizzato dai componenti figli con le form per definire il loro stato di validazione tramite il tipo: RiscaFormStatus. */
  onFormChecked$ = new Subject<IRiscaFormCheck>();
  /** Subject che viene invocato una volta che tutte le form registrate mediante le chiavi sono state "checkate" come "valid". */
  onFormsValid$ = new Subject<IRiscaFormTreeParent>();
  /** Subject che viene invocato una volta che tutte le form registrate mediante le chiavi sono state "checkate" come "invalid". */
  onFormsInvalid$ = new Subject<IRiscaFormTreeParent>();

  /** Subject che può essere invocato da un componente "parent" per attivare il submit di tutti i suoi form "children". */
  onParentSubmit$ = new Subject<string>();
  /** Subject che può essere invocato da un componente "parent" per attivare il reset di tutti i suoi form "children". */
  onParentReset$ = new Subject<string>();

  /** Subject che può essere invocato da un componente "child" per attivare il submit del componente "parent". */
  onChildSubmit$ = new Subject<string>();
  /** Subject che può essere invocato da un componente "child" per attivare il reset del compponente "parent". */
  onChildReset$ = new Subject<string>();

  /** Subscription che rimane in ascolto dell'evento: onChildServiziError, generato dai componenti figli. */
  onChildServiziError$ = new Subject<IFormChildServiziError>();

  /**
   * Costruttore.
   */
  constructor(private _logger: LoggerService) {
    // Lancio il setup del servizio
    this.setupService();
  }

  /**
   * Funzione di setup del servizio.
   */
  private setupService() {
    // Registro l'evento di onFormChecked
    this.onFormChecked$.subscribe((formCheck: IRiscaFormCheck) => {
      // Richiamo la funzione di gestione del form check
      this.formChecked(formCheck);
    });
  }

  /**
   * ##########################
   * FUNZIONALITA' DEL SERVIZIO
   * ##########################
   */

  /**
   * Funzione che si occupa di gestire l'evento di onFormChecked.
   * @param formCheck IRiscaFormCheck contenente le informazioni emesse da un componente.
   */
  private formChecked(formCheck: IRiscaFormCheck) {
    // Verifico l'input
    if (!this.formFormalControl(formCheck)) {
      return;
    }

    // Aggiorno il valore all'interno del formTrees
    this.setChildStatus(formCheck.parent, formCheck.child, formCheck);
    // Lancio la verifica dell'albero
    this.checkTreeValidation(formCheck.parent);
  }

  /**
   * Funzione che esegue una verifica sull'albero di un componente padre e controlla se tutti i form figli sono stati validati.
   * @param parent string che identifica la chiave di un albero di form.
   */
  private checkTreeValidation(parent: string) {
    // Verifico che l'input esista
    if (!parent) {
      return;
    }

    // Creo un array contenente le informazioni dei figli del parent per il reset
    const childrenToReset = [];
    // Creo un flag che definisce se ci sono errori nei form
    let childrenFormErrors = false;
    // Creo un flag che definisce se ci sono oggetti in attesa per i form
    let childrenFormWaiting = false;
    // Creo un oggetto che gestirà i dati delle form valid e i dati invalid
    const formsValid = {} as IRiscaFormTreeParent;
    formsValid[parent] = {};
    const formsInvalid = {} as IRiscaFormTreeParent;
    formsInvalid[parent] = {};

    // Recupero i dati dei figli
    const children = this._formTreesStatus.get(parent);
    // Ciclo la mappa dei figli
    for (let [child, childValue] of children) {
      // Aggiungo il child alla lista
      childrenToReset.push(child);

      // Verifico lo stato
      switch (childValue.status) {
        case 'valid':
          // Oggetto valid, lo aggiungo alle form invalid
          formsValid[parent][child] = childValue;
          break;
        case 'invalid':
          // Oggetto invalid, lo aggiungo alle form invalid
          formsInvalid[parent][child] = childValue;
          // Setto il flag a true
          childrenFormErrors = true;
          break;
        case 'waiting':
          // Setto il flag a true
          childrenFormWaiting = true;
          break;
      }
    }

    // Verifico se ci sono oggetti in attesa
    if (childrenFormWaiting) return; // Blocco le logiche

    // Verifico se ci sono errori
    if (childrenFormErrors) {
      // Emetto l'evento con i dati dei form errati
      this.onFormsInvalid$.next(formsInvalid);
      // #
    } else {
      // Emetto l'evento con i dati dei form corretti
      this.onFormsValid$.next(formsValid);
    }

    // Reimposto i dati dell'albero delle form
    this.resetFormTree(parent, childrenToReset);
  }

  /**
   * Funzione di comodo che permette di gestire l'emissione dell'evento di submit da parte di un componente "parent", così da comunicare l'attivazione delle logiche di submit da parte dei propri "children".
   * @param parent string che definisce la chiave con cui è stato identificato il component padre, per l'individuazioni dei propri figli.
   */
  parentSubmit(parent: string) {
    // Richiamo la next del subject
    this.onParentSubmit$.next(parent);
  }

  /**
   * Funzione di comodo che permette di gestire l'emissione dell'evento di reset da parte di un componente "parent", così da comunicare l'attivazione delle logiche di reset da parte dei propri "children".
   * @param parent string che definisce la chiave con cui è stato identificato il component padre, per l'individuazioni dei propri figli.
   */
  parentReset(parent: string) {
    // Richiamo la next del subject
    this.onParentReset$.next(parent);
  }

  /**
   * Funzione di comodo che permette di gestire l'emissione dell'evento di submit da parte di un componente "child", così da comunicare l'attivazione delle logiche di submit da parte del componete "parent".
   * @param child string che definisce la chiave con cui è stato identificato il component figlio, per l'individuazione da parte del padre.
   */
  childSubmit(child: string) {
    // Richiamo la next del subject
    this.onChildSubmit$.next(child);
  }

  /**
   * Funzione di comodo che permette di gestire l'emissione dell'evento di reset da parte di un componente "child", così da comunicare l'attivazione delle logiche di reset da parte del componete "parent".
   * @param child string che definisce la chiave con cui è stato identificato il component figlio, per l'individuazione da parte del padre.
   */
  childReset(child: string) {
    // Richiamo la next del subject
    this.onChildReset$.next(child);
  }

  /**
   * ###################
   * FUNZIONI DI UTILITY
   * ###################
   */

  /**
   * Funzione che effettua i controlli formali d'esistenza dei parametri in input e le relative composizioni.
   * @param formCheck IRiscaFormCheck da verificare.
   * @returns boolean che definisce se l'oggetto è formalmente valido.
   */
  private formFormalControl(formCheck: IRiscaFormCheck): boolean {
    // Verifico l'input
    if (!formCheck) {
      // Loggo l'errore
      this._logger.error('formChecking', 'input undefined');
      // Ritorno false
      return false;
    }
    // Verifico che il componente padre che ha emesso l'evento è registrato nella mappa
    if (!this._formTreesStatus.has(formCheck.parent)) {
      // Loggo un warning
      this._logger.warning('formChecking | parent not mapped', formCheck);
      // Ritorno false
      return false;
    }
    // Verifico che il componente figlio che ha emesso l'evento è registrato nella mappa
    if (!this._formTreesStatus.get(formCheck.parent).has(formCheck.child)) {
      // Loggo un warning
      this._logger.warning('formChecking | child not mapped', formCheck);
      // Ritorno false
      return false;
    }

    // Controlli oke
    return true;
  }

  /**
   * ################################
   * INIZIALIZZATORI, GETTER & SETTER
   * ################################
   */

  /**
   * Funzione che registra un nuovo albero di form all'interno del servizio.
   * Se esiste già un albero con la stessa chiave, verrà aggiornato.
   * @param parent string che identifica la chiave del componente padre come riferimento dei form figli.
   * @param children Array di string che definisce le chiavi dei form figli.
   */
  resetFormTree(parent: string, children: string[]) {
    // Verifico l'input
    if (!parent || !children || children.length === 0) {
      return;
    }

    // Creo una mappa per i figli
    const childrenMap = this.createChildrendMap(
      parent,
      children,
      RiscaFormStatus.inAttesa
    );
    // Aggiungo alla mappa dei tree form il nuovo albero
    this._formTreesStatus.set(parent, childrenMap);
  }

  /**
   * Funzione che rimuove un albero di form all'interno del servizio.
   * @param parent string che identifica la chiave del componente padre.
   */
  deleteFormTree(parent: string) {
    // Verifico l'input
    if (!parent || !this._formTreesStatus.has(parent)) {
      return;
    }

    // Rimuovo l'abero richiesto
    this._formTreesStatus.delete(parent);
  }

  /**
   * Funzione che resetta completamente gli alberi delle form.
   */
  clearFormTree() {
    // Rimuovo tutti i dati degli alberi
    this._formTreesStatus.clear();
  }

  /**
   * Funzione che genera una mappa per i figli di un componente.
   * @param children Array di string con le chiavi dei figli.
   * @param childValue RiscaFormStatus come valore iniziale per i figli.
   * @returns Map<string, RiscaFormStatus>.
   */
  private createChildrendMap(
    parent: string,
    children: string[],
    childValue: RiscaFormStatus
  ): Map<string, IRiscaFormCheck> {
    // Verifico l'input
    if (!children || !childValue) {
      return undefined;
    }

    // Creo una mappa per i figli
    const childrenMap = new Map<string, IRiscaFormCheck>();
    // Imposto i valori della mappa dei figli
    for (let i = 0; i < children.length; i++) {
      // Recupero la chiave del figlio
      const child = children[i];
      // Definisco il valore di default
      const childValue: IRiscaFormCheck = {
        data: undefined,
        parent,
        child,
        status: RiscaFormStatus.inAttesa,
      };
      // Aggiungo chiave e valore alla mappa
      childrenMap.set(child, childValue);
    }

    // Ritorno la map
    return childrenMap;
  }

  /**
   * Funzione che effettua un set dei dati per un albero, dato: padre, figlio e valore del figlio.
   * ATTENZIONE: non vengono effettuati controlli.
   * @param parent string identifica il padre dell'albero.
   * @param child string identifica il figlio.
   * @param childValue IRiscaFormCheck definisce le informazioni del figlio.
   */
  private setChildStatus(
    parent: string,
    child: string,
    childValue: IRiscaFormCheck
  ) {
    // Recupero il dato dei figli
    const children = this._formTreesStatus.get(parent);
    // Aggiorno il valore del figlio il dato del figlio
    children.set(child, childValue);
    // Aggiorno il valore del padre nell'albero
    this._formTreesStatus.set(parent, children);
  }

  /**
   * #####################################################
   * GESTIONE DEGLI ERRORI NEI SERVIZI NEI CHILD COMPONENT
   * #####################################################
   */

  /**
   * Funzione che permette d'emettere un errore generato dai servizi da parte di un child component.
   * @param data IFormChildServiziError con le informazioni dell'errore.
   */
  onChildServiziError(data: IFormChildServiziError) {
    // Emetto l'evento tramite subject
    this.onChildServiziError$.next(data);
  }
}

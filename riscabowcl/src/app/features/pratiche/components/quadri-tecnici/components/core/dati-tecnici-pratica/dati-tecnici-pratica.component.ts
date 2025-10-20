import { Component, Input, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs/index';
import { PraticaDTVo } from '../../../../../../../core/commons/vo/pratica-vo';
import { LoggerService } from '../../../../../../../core/services/logger.service';
import { NavigationHelperService } from '../../../../../../../core/services/navigation-helper/navigation-helper.service';
import { RiscaFormChildComponent } from '../../../../../../../shared/components/risca/risca-form-child/risca-form-child.component';
import { RiscaFormSubmitHandlerService } from '../../../../../../../shared/services/risca/risca-form-submit-handler.service';
import { RiscaStorageService } from '../../../../../../../shared/services/risca/risca-storage.service';
import { RiscaUtilitiesService } from '../../../../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import {
  AppActions,
  RiscaServerError,
} from '../../../../../../../shared/utilities';
import { PraticheService } from '../../../../../service/pratiche.service';
import { QuadriTecniciService } from '../../../services/quadri-tecnici/pratica/quadri-tecnici.service';
import { DTPraticaConfig } from '../../../utilities/configs/dt-pratica.injectiontoken';
import { DatiTecniciKeys } from './utilities/dati-tecnici-pratica.enums';

/**
 * Componente che definisce le funzionalità da estendere per tutte le varianti dei dati tecnici.
 * La classe definirà tutte le funzioni comuni per la gestione dei dati tecnici.
 */
@Component({
  selector: 'dati-tecnici-pratica',
  template: ``,
  styles: [],
})
export class DatiTecniciPraticaComponent<T>
  extends RiscaFormChildComponent<T>
  implements OnDestroy
{
  /** Input per i dati in caso di modifica */
  @Input('datiTecnici') praticaDT: PraticaDTVo;

  /** Subscription registrato per il submit della form, gestito tramite servizio. */
  onFormSubmitByService$: Subscription;
  /** Subscription registrato per il reset della form, gestito tramite servizio. */
  onFormResetByService$: Subscription;
  /** Subscription registrato per l'update dei dati tecnici. */
  onDatiTecniciUpdate$: Subscription;
  /** Subscription registrato per l'update della modalità del componente. */
  onCambioModalita$: Subscription;
  /** Subscription registrato per l'evento di pratica salvata (inserimento o modifica). */
  onServiziError$: Subscription;

  /** Boolean che definisce se la pagina dei dati tecnici deve risultare bloccata per il codice di blocco. */
  disableUserInputs: boolean = false;

  /**
   * Costruttore.
   */
  constructor(
    injConfig: DTPraticaConfig,
    logger: LoggerService,
    navigationHelper: NavigationHelperService,
    protected _pratiche: PraticheService,
    protected _quadriTecnici: QuadriTecniciService,
    riscaFormSubmitHandler: RiscaFormSubmitHandlerService,
    protected _riscaStorage: RiscaStorageService,
    riscaUtilities: RiscaUtilitiesService
  ) {
    // Richiamo il super
    super(logger, navigationHelper, riscaFormSubmitHandler, riscaUtilities);

    // Lancio il setup del componente
    this.setupInjection(injConfig);
    // Lancio il setup degli event emitter
    this.setupEvents();
  }

  ngOnDestroy() {
    // Tento di fare l'unsubscribe degli events
    try {
      // Verifico che esista l'event emitter
      if (this.onFormSubmitByService$) {
        this.onFormSubmitByService$.unsubscribe();
      }
      if (this.onFormResetByService$) {
        this.onFormResetByService$.unsubscribe();
      }
      if (this.onDatiTecniciUpdate$) {
        this.onDatiTecniciUpdate$.unsubscribe();
      }
      if (this.onCambioModalita$) {
        this.onCambioModalita$.unsubscribe();
      }
      if (this.onServiziError$) {
        this.onServiziError$.unsubscribe();
      }
    } catch (e) {
      // Loggo un warning
      this._logger.warning('DatiTecniciPraticaComponent unsubscribe', e);
    }
  }

  /**
   * Funzione di setup per le informazioni del componente.
   * @param injConfig DatiTecniciConfig contenente le informazioni passate tramite caricamento dinamico.
   */
  private setupInjection(injConfig: DTPraticaConfig) {
    // Recupero le informazioni per le input del componente
    this.parentFormKey = injConfig.parentFormKey;
    this.childFormKey = injConfig.childFormKey;
    this.showSubmit = injConfig.showSubmit;
    this.modalita = injConfig.modalita;
    this.praticaDT = injConfig.configs;
    this.disableUserInputs = injConfig.disableUserInputs ?? false;
  }

  /**
   * Funzione di setup per gli event emitter del componente.
   */
  private setupEvents() {
    // Collego il Subscribe locale al Subject del servizio
    this.onFormSubmitByService$ = this._quadriTecnici.onFormSubmit$.subscribe({
      next: (t: boolean) => {
        // Lancio l'azione del componente
        this.onFormSubmit();
      },
    });

    // Collego il Subscribe locale al Subject del servizio
    this.onFormResetByService$ = this._quadriTecnici.onFormReset$.subscribe({
      next: (t: boolean) => {
        // Lancio l'azione del componente
        this.onFormReset();
      },
    });

    // Collego la subscription locale il valore del subject del servizio
    this.onDatiTecniciUpdate$ =
      this._quadriTecnici.onDatiTecniciUpdate$.subscribe({
        next: (praticaDTVo: PraticaDTVo) => {
          // Richiamo l'aggiornamento dei dati tecnici
          this.datiTecniciUpdated(praticaDTVo);
        },
      });

    // Collego il Subscribe locale al Subject del servizio
    this.onCambioModalita$ = this._quadriTecnici.onCambioModalita$.subscribe({
      next: (modalita: AppActions) => {
        // Lancio l'azione del componente
        this.modalita = modalita;
      },
    });

    // Collego il Subscribe locale al Subject del servizio
    this.onServiziError$ = this._quadriTecnici.onPraticaSaved$.subscribe({
      next: (v?: any) => {
        // Richiamo la funzione del componente
        this.praticaSaved(v);
      },
    });
  }

  /**
   * Funzione impostata per la gestione dell'evento: onDatiTecniciUpdate.
   * La funzione è pensata per essere overridata, in maniera tale da gestire in maniera custom l'aggiornamento dei dati tecnici.
   * @param praticaDTVo PraticaDTVo con i dati tecnici aggiornati.
   */
  protected datiTecniciUpdated(praticaDTVo: PraticaDTVo) {
    // Gestione delle logiche
  }

  /**
   * Funzione impostata per la gestione dell'evento: onPraticaSaved.
   * La funzione è pensata per essere overridata, in maniera tale da gestire in maniera custom l'aggiornamento dei dati tecnici.
   * @param v any con il payload dei dati generati per l'evento.
   */
  protected praticaSaved(v?: any) {
    // Gestione delle logiche
  }

  /**
   * Funzione adibita alla gestione dei dati per i dati tecnici.
   * Essendo che i componenti vengono generati dinamicamente, non è possibile accedere direttamente al valore del main form.
   * Questo serve come workaround per poter avere in ogni momento uno stato di checkpoint dei dati della form.
   * NOTA BENE: Si consiglia di richiamare questa funzione una volta che il form è stato inizializzato e sono stati impostati tutti i valori di pre-configurazione.
   */
  protected setupDataCheckpoint() {
    // Effettuo un primo checkpoint dei dati
    this.saveDataCheckpoint();

    // Mi aggancio all'evento del mainForm per valuesChanges, in maniera tale che ad ogni modifica delle input venga creato un apposito checkpoint
    this.mainForm?.valueChanges.subscribe({
      next: (mainData: T) => {
        // Richiamo la funzione di salvataggio dei dati (non l'oggetto ritornato dalla subscribe poiché i campi disable non ritornano valori)
        this.saveDataCheckpoint();
      },
    });
  }

  /**
   * Funzione di supporto che esegue il salvataggio dei dati della form come checkpoint data.
   */
  private saveDataCheckpoint() {
    // Recupero i dati della form tramite funzione
    const datiTecnici = this.getMainFormActualRawValue();
    // Salvo i dati in sessione, in maniera che siano fruibili nell'applicazione
    this._riscaStorage.sSetItem(DatiTecniciKeys.checkpoint, datiTecnici);
  }

  /**
   * Funzione di comodo che invoca l'attivazione degli errori e la comunicazione ai componenti padri.
   * @param e RiscaServerError con l'oggetto d'errore generato.
   */
  protected onServiziError(e: RiscaServerError) {
    // Richiamo la funzione del servizio e propago l'errore
    this._quadriTecnici.onServiziError(e);
  }
}

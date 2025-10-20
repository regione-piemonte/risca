import { Component, Input, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs/index';
import { LoggerService } from '../../../../../../../core/services/logger.service';
import { NavigationHelperService } from '../../../../../../../core/services/navigation-helper/navigation-helper.service';
import { RiscaFormChildComponent } from '../../../../../../../shared/components/risca/risca-form-child/risca-form-child.component';
import { RiscaFormSubmitHandlerService } from '../../../../../../../shared/services/risca/risca-form-submit-handler.service';
import { RiscaStorageService } from '../../../../../../../shared/services/risca/risca-storage.service';
import { RiscaUtilitiesService } from '../../../../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import { PraticheService } from '../../../../../service/pratiche.service';
import { IRicercaPraticaAvanzataForm } from '../../../../ricerca-avanzata/form-ricerca-avanzata-pratiche-sd/utilities/form-ricerca-avanzata-pratiche-sd.interfaces';
import { QuadriTecniciRicercaService } from '../../../services/quadri-tecnici/ricerca/quadri-tecnici-ricerca.service';
import { DTRicercaConfig } from '../../../utilities/configs/dt-ricerca.injectiontoken';

/**
 * Componente che definisce le funzionalità da estendere per tutte le varianti dei dati tecnici.
 * La classe definirà tutte le funzioni comuni per la gestione dei dati tecnici.
 */
@Component({
  selector: 'ricerca-dati-tecnici',
  template: ``,
  styles: [],
})
export class RicercaDatiTecniciComponent<T>
  extends RiscaFormChildComponent<T>
  implements OnDestroy
{
  /** Input per i dati in caso di modifica */
  @Input('datiTecnici') configs: string;

  /** Subscription registrato per il submit della form, gestito tramite servizio. */
  onFormSubmitByService$: Subscription;
  /** Subscription registrato per il reset della form, gestito tramite servizio. */
  onFormResetByService$: Subscription;

  /** Subscription che rimane in ascolto per il cambio valore del form per la ricerca avanzata. */
  onRicercaAvanzataChange$: Subscription;

  /**
   * Costruttore.
   */
  constructor(
    injConfig: DTRicercaConfig,
    logger: LoggerService,
    navigationHelper: NavigationHelperService,
    protected _pratiche: PraticheService,
    protected _ricercaQuadriTecnici: QuadriTecniciRicercaService,
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
      if (this.onRicercaAvanzataChange$) {
        this.onRicercaAvanzataChange$.unsubscribe();
      }
    } catch (e) {
      // Loggo un warning
      this._logger.warning('RicercaDatiTecniciComponent unsubscribe', e);
    }
  }

  /**
   * Funzione di setup per le informazioni del componente.
   * @param injConfig DatiTecniciConfig contenente le informazioni passate tramite caricamento dinamico.
   */
  private setupInjection(injConfig: DTRicercaConfig) {
    // Recupero le informazioni per le input del componente
    this.parentFormKey = injConfig.parentFormKey;
    this.childFormKey = injConfig.childFormKey;
    this.showSubmit = injConfig.showSubmit;
    this.configs = injConfig.configs;
  }

  /**
   * Funzione di setup per gli event emitter del componente.
   */
  private setupEvents() {
    // Collego il Subscribe locale al Subject del servizio
    this.onFormSubmitByService$ =
      this._ricercaQuadriTecnici.onFormSubmit$.subscribe({
        next: (t: boolean) => {
          // Lancio l'azione del componente
          this.onFormSubmit();
        },
      });

    // Collego il Subscribe locale al Subject del servizio
    this.onFormResetByService$ =
      this._ricercaQuadriTecnici.onFormReset$.subscribe({
        next: (t: boolean) => {
          // Lancio l'azione del componente
          this.onFormReset();
        },
      });

    // Collego il Subscribe locale al Subject del servizio
    this.onRicercaAvanzataChange$ =
      this._ricercaQuadriTecnici.onRicercaAvanzataChange$.subscribe({
        next: (data: any) => {
          // Lancio l'azione del componente
          this.onRicercaAvanzataChange(data);
        },
      });
  }

  /**
   * Funzione sulla quale effettuare override.
   * La funzione viene invocata quando l'evento di cambio dati del form della ricerca avanzata è attivato.
   * @param data IRicercaPraticaAvanzataReq con le informazioni del form di ricerca avanzata modificate.
   */
  protected onRicercaAvanzataChange(data: IRicercaPraticaAvanzataForm) {
    // Gestione delle logiche
  }
}

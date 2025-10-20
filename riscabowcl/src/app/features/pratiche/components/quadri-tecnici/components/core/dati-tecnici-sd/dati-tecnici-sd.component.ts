import { Component, Input, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs/index';
import { AnnualitaSDVo } from '../../../../../../../core/commons/vo/annualita-sd-vo';
import { PraticaDTVo, PraticaEDatiTecnici } from '../../../../../../../core/commons/vo/pratica-vo';
import { StatoDebitorioVo } from '../../../../../../../core/commons/vo/stato-debitorio-vo';
import { LoggerService } from '../../../../../../../core/services/logger.service';
import { NavigationHelperService } from '../../../../../../../core/services/navigation-helper/navigation-helper.service';
import { RiscaFormChildComponent } from '../../../../../../../shared/components/risca/risca-form-child/risca-form-child.component';
import { RiscaFormSubmitHandlerService } from '../../../../../../../shared/services/risca/risca-form-submit-handler.service';
import { RiscaUtilitiesService } from '../../../../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import { RiscaServerError } from '../../../../../../../shared/utilities';
import { PraticheService } from '../../../../../service/pratiche.service';
import { QuadriTecniciSDService } from '../../../services/quadri-tecnici/stato-debitorio/quadri-tecnici-sd.service';
import { DTAnnualitaConfig } from '../../../utilities/configs/dt-annualita.injectiontoken';

/**
 * Componente che definisce le funzionalità da estendere per tutte le varianti dei dati tecnici.
 * La classe definirà tutte le funzioni comuni per la gestione dei dati tecnici.
 */
@Component({
  selector: 'dati-tecnici-sd',
  template: ``,
  styles: [],
})
export class DatiTecniciSDComponent<T>
  extends RiscaFormChildComponent<T>
  implements OnDestroy
{
  /** PraticaEDatiTecnici per la pre-valorizzazione del componente dei dati tecnici, partendo dal dato tecnico della pratica. */
  @Input('praticaEDatiTecnici') praticaEDatiTecnici: PraticaEDatiTecnici;
  /** Input dataInserimentoPratica che definisce la data in cui è stata inserita la pratica. */
  @Input('dataInserimentoPratica') dataInserimentoPratica: string;
  /** Input string che definisce la configurazione dati dello stato debitorio d'interesse. */
  @Input('statoDebitorio') statoDebitorio: StatoDebitorioVo;
  /** Input string che definisce la configurazione dati partendo dal dato tecnico di un'annualità. */
  @Input('annualitaDT') annualitaDT: AnnualitaSDVo;
  /** Input number che definisce l'id_componente_dt di riferimento per i dati tecnici. */
  @Input('idComponenteDt') idComponenteDt: number;
  /** Boolean che definisce se lo stato debitorio ha già un'annualità con il flag rateo prima annualità settato. */
  @Input('rateoPrimaAnnualitaDisabled') rateoPrimaAnnualitaDisabled: boolean;
  /** Boolean che definisce se l'utene non può operare sulla pagina. */
  @Input('disableUserInputs') disableUserInputsConfig: boolean = false;

  /** Subscription registrato per il submit della form, gestito tramite servizio. */
  onFormSubmitByService$: Subscription;
  /** Subscription registrato per il reset della form, gestito tramite servizio. */
  onFormResetByService$: Subscription;

  /** Boolean che definisce se la pagina dei dati tecnici deve risultare bloccata. */
  AEA_DTDisabled: boolean = false;
  
  /** PraticaDTVo che definisce la configurazione dati partendo dal dato tecnico di una pratica. */
  praticaDT: PraticaDTVo;

  /**
   * Costruttore.
   */
  constructor(
    injConfig: DTAnnualitaConfig,
    logger: LoggerService,
    navigationHelper: NavigationHelperService,
    protected _pratiche: PraticheService,
    protected _quadriTecniciSD: QuadriTecniciSDService,
    riscaFormSubmitHandler: RiscaFormSubmitHandlerService,
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
    } catch (e) {
      // Loggo un warning
      this._logger.warning('DatiTecniciSDComponent unsubscribe', e);
    }
  }

  /**
   * Funzione di setup per le informazioni del componente.
   * @param injConfig DTAnnualitaConfig contenente le informazioni passate tramite caricamento dinamico.
   */
  private setupInjection(injConfig: DTAnnualitaConfig) {
    // Recupero le informazioni per le input del componente
    this.parentFormKey = injConfig.parentFormKey;
    this.childFormKey = injConfig.childFormKey;
    this.modalita = injConfig.modalita;
    this.praticaEDatiTecnici = injConfig.praticaEDatiTecnici;
    this.praticaDT = this.praticaEDatiTecnici?.datiTecnici;
    this.dataInserimentoPratica = injConfig.dataInserimentoPratica;
    this.statoDebitorio = injConfig.statoDebitorio;
    this.annualitaDT = injConfig.annualitaDT;
    this.idComponenteDt = injConfig.idComponenteDt;
    this.AEA_DTDisabled = injConfig.disableUserInputs;
    this.rateoPrimaAnnualitaDisabled = injConfig.rateoPrimaAnnualita;
    this.disableUserInputsConfig = injConfig.disableUserInputs;
  }

  /**
   * Funzione di setup per gli event emitter del componente.
   */
  private setupEvents() {
    // Collego il Subscribe locale al Subject del servizio
    this.onFormSubmitByService$ = this._quadriTecniciSD.onFormSubmit.subscribe({
      next: (t: boolean) => {
        // Lancio l'azione del componente
        this.onFormSubmit();
      },
    });

    // Collego il Subscribe locale al Subject del servizio
    this.onFormResetByService$ = this._quadriTecniciSD.onFormReset.subscribe({
      next: (t: boolean) => {
        // Lancio l'azione del componente
        this.onFormReset();
      },
    });
  }

  /**
   * Funzione di comodo che invoca l'attivazione degli errori e la comunicazione ai componenti padri.
   * @param e RiscaServerError con l'oggetto d'errore generato.
   */
  protected onServiziErrorAnnualita(e: RiscaServerError) {
    // Richiamo la funzione del servizio e propago l'errore
    this._quadriTecniciSD.onServiziError(e);
  }

  /**
   * ###################
   * SEGNALAZIONI ERRORI
   * ###################
   */

  /**
   * Funzione di supporto che gestisce la casistica d'errore in caso in cui manchino i dati tecnici per la gestione delle pagine dei dati tecnici.
   */
  dtNotDefined() {
    // Richiamo la funzione del servizio
    this._quadriTecniciSD.dtNotDefined();
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */

  /**
   * Getter di comodo che raccoglie le logiche per il blocco dell'interfaccia utente.
   * @returns boolean con l'insieme di condizioni di blocco.
   */
  get disableUserInputs(): boolean {
    // Ritorno le condizioni di disabilitazione
    return this.AEA_DTDisabled || this.disableUserInputsConfig;
  }
}

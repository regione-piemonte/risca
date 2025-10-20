import {
  Component,
  EventEmitter,
  Input,
  OnChanges,
  OnDestroy,
  OnInit,
  Output,
  SimpleChanges,
} from '@angular/core';
import { Subscription } from 'rxjs';
import { CommonConsts } from '../../../consts/common-consts.consts';
import { RiscaAlertService } from '../../../services/risca/risca-alert.service';
import { RiscaAlertConfigs } from '../../../utilities';

@Component({
  selector: 'risca-alert-listener',
  templateUrl: './risca-alert-listener.component.html',
  styleUrls: ['./risca-alert-listener.component.scss'],
})
export class RiscaAlertListenerComponent
  implements OnInit, OnChanges, OnDestroy
{
  /** Oggetto costante contenente le informazioni comuni all'applicazione. */
  C_C = new CommonConsts();

  /** RiscaAlertConfigs con le configurazioni dell'alert. */
  @Input() alertConfigs: RiscaAlertConfigs;
  /** Boolean con la configurazione per permettere la chiusura dell'alert tramite la X. */
  @Input() allowAlertClose: boolean = false;

  /** EventEmitter che definisce l'evento di alert nascosto. */
  @Output('onAlertHidden') onAlertHidden$ = new EventEmitter<boolean>();

  /** EventEmitter che definisce l'evento di conferma dell'alert. */
  @Output('onConfirm') onConfirm$ = new EventEmitter<any>();
  /** EventEmitter che definisce l'evento di chiusura dell'alert. */
  @Output('onCancel') onCancel$ = new EventEmitter<any>();

  /** Subscription per intercettare gli alert emessi dal servizio. */
  private onAlertEmitted$: Subscription;

  constructor(private _riscaAlert: RiscaAlertService) {
    // Lancio la funzione di setup del componente
    this.setupComponente();
  }

  ngOnInit() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }

  /**
   * Hook di Angular
   * @param changes SimpleChanges
   */
  ngOnChanges(changes: SimpleChanges): void {
    // Verifico se ci sono da aggiornare i messaggi
    if (changes.configs && !changes.configs.firstChange) {
    }
  }

  /**
   * ngOnDestroy.
   */
  ngOnDestroy() {
    // Tento di fare l'unsubscribe degli events
    try {
      // Verifico che esista l'event emitter
      if (this.onAlertEmitted$) {
        this.onAlertEmitted$.unsubscribe();
      }
    } catch (e) {}
  }

  /**
   * #################
   * FUNZIONI DI SETUP
   * #################
   */

  /**
   * Funzione di setup per il componente.
   */
  private setupComponente() {
    // Lancio il setup dei listener
    this.setupListners();
  }

  /**
   * Funzione di setup per i listeners.
   */
  private setupListners() {
    // Connetto l'ascoltatore per l'emissione di un alert
    this.onAlertEmitted$ = this._riscaAlert.onAlertEmitted$.subscribe({
      next: (a: RiscaAlertConfigs) => {
        // Aggiorno localmente l'alert
        this.alertConfigs = a;
      },
    });
  }

  /**
   * ################
   * GETTER DI COMODO
   * ################
   */

  /**
   * Getter che verifica se l'oggetto alertConfigs esiste e ha messaggi.
   */
  get alertConfigsCheck() {
    // Verifico e ritorno la condizione
    return this._riscaAlert.alertConfigsCheck(this.alertConfigs);
  }
}

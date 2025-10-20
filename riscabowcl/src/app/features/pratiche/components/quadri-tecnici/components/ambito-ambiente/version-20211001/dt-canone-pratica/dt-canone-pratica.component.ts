import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import * as moment from 'moment';
import { Subscription } from 'rxjs/index';
import { CalcoloCanoneVo } from '../../../../../../../../core/commons/vo/calcolo-canone-vo';
import { AccessoElementiAppKeyConsts } from '../../../../../../../../core/consts/accesso-elementi-app/accesso-elementi-app.consts';
import { AccessoElementiAppService } from '../../../../../../../../core/services/accesso-elementi-app.service';
import { LoggerService } from '../../../../../../../../core/services/logger.service';
import { NavigationHelperService } from '../../../../../../../../core/services/navigation-helper/navigation-helper.service';
import { RiscaUtilitiesComponent } from '../../../../../../../../shared/components/risca/risca-utilities/risca-utilities.component';
import { RiscaAlertService } from '../../../../../../../../shared/services/risca/risca-alert.service';
import { RiscaMessagesService } from '../../../../../../../../shared/services/risca/risca-messages.service';
import { RiscaUtilitiesService } from '../../../../../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import {
  RiscaAlertConfigs,
  RiscaServerError,
  RiscaServerStatus,
} from '../../../../../../../../shared/utilities';
import { RiscaNotifyCodes } from '../../../../../../../../shared/utilities/enums/risca-notify-codes.enums';
import { IDatiCalcoloCanone } from '../../../../../../interfaces/quadri-tecnici/quadri-tecnici.interfaces';
import { DatiTecniciService } from '../../../../services/dati-tecnici/dati-tecnici.service';
import { QuadriTecniciService } from '../../../../services/quadri-tecnici/pratica/quadri-tecnici.service';

@Component({
  selector: 'dt-canone-pratica',
  templateUrl: './dt-canone-pratica.component.html',
  styleUrls: ['./dt-canone-pratica.component.scss'],
})
export class DTCanonePratica20211001Component
  extends RiscaUtilitiesComponent
  implements OnInit
{
  /** Oggetto contenente i valori costanti per le chiavi d'accesso agli elementi dell'app. */
  AEAK_C = AccessoElementiAppKeyConsts;

  /** Boolean che definisce se visualizzare gli alert del canone. Per default è: true. */
  @Input('mostraAlert') showAlert = true;

  /** Output che informa il padre che si è verificato un errore per regola mancante. */
  @Output() calcoloCanoneError = new EventEmitter<RiscaServerError>();
  /** Output che informa il padre che si è verificato un errore per regola mancante. */
  @Output() regolaMancanteCC = new EventEmitter<RiscaServerError>();

  /** Flag che permette di definire se il pulsante calcola canone è attivo o disattivato. */
  disabledCalcolaCanone: boolean = true;
  /** String che viene valorizzato a seguito del calcolo canone. */
  canoneCalcolato: string;
  /** String che contiene il dato per il calcolo del canone: idRiscossione. */
  idRiscossione: string;
  /** String che contiene il dato per il calcolo del canone: dataRiscossione. */
  dataRiscossione: string;

  /** RiscaAlertConfigs contenente le configurazioni per la gestione dell'alert utente. */
  alertConfigs: RiscaAlertConfigs = new RiscaAlertConfigs();

  /** Subscription registrato per l'aggiornamento delle informazioni per il calcolo canone. */
  onDatiCalcoloCanone: Subscription;
  /** Subscription registrato per il reset delle informazioni per il calcolo canone. */
  onResetEAbilitaCanone: Subscription;
  /** Subscription registrato per il reset delle informazioni per il canone calcolato. */
  onResetEDisabilitaCanone: Subscription;

  /** Boolean che definisce se la pagina dei dati tecnici deve risultare bloccata. */
  AEA_DTDisabled: boolean = false;

  /**
   * Costruttore.
   */
  constructor(
    private _accessoElementiApp: AccessoElementiAppService,
    private _logger: LoggerService,
    private _datiTecnici: DatiTecniciService,
    navigationHelper: NavigationHelperService,
    private _quadriTecnici: QuadriTecniciService,
    riscaAlert: RiscaAlertService,
    riscaMessages: RiscaMessagesService,
    private _riscaUtilities: RiscaUtilitiesService
  ) {
    // Richiamo il super
    super(navigationHelper, riscaAlert, riscaMessages);

    // Lancio il setup di blocco per configurazione profilo
    this.setupDTDisabled();
    // Lancio il setup per gli eventi
    this.setupEvents();
  }

  ngOnInit() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }

  ngOnDestroy() {
    // Tento di fare l'unsubscribe degli events
    try {
      // Verifico che esista l'event emitter
      if (this.onDatiCalcoloCanone) {
        this.onDatiCalcoloCanone.unsubscribe();
      }
      if (this.onResetEAbilitaCanone) {
        this.onResetEAbilitaCanone.unsubscribe();
      }
      if (this.onResetEDisabilitaCanone) {
        this.onResetEDisabilitaCanone.unsubscribe();
      }
    } catch (e) {
      // Loggo un warning
      this._logger.warning('DatiTecniciPraticaComponent unsubscribe', e);
    }
  }

  /**
   * Funzione che recupera la configurazione per l'abilitazione della pagina.
   */
  private setupDTDisabled() {
    // Recupero la chiave per la configurazione della form
    const dtKey = this.AEAK_C.PAGINA_DATI_TECNICI;
    // Recupero la configurazione della form dal servizio
    this.AEA_DTDisabled = this._accessoElementiApp.isAEADisabled(dtKey);
  }

  /**
   * Funzione di setup per gli event emitter del componente.
   */
  private setupEvents() {
    // Collego la subscription locale il valore del subject del servizio
    this.onDatiCalcoloCanone =
      this._quadriTecnici.onDatiCalcoloCanone$.subscribe({
        next: (datiCC: IDatiCalcoloCanone) => {
          // Verifico l'oggetto
          if (!datiCC) {
            // Oggetto undefined, blocco il flusso
            return;
          }

          // Reset dell'alert
          this.resetAlert();
          // Variabili di comodo
          const idR = datiCC.idRiscossione;
          // const dataR = datiCC.dataRiscossione;
          // Per la data è necessario definire la data di oggi
          const dataR = moment().format(this.C_C.DATE_FORMAT_SERVER);
          // Assegno localmente le informazioni
          this.setDatiCalcoloCanone(idR, dataR);
          // #
        },
      });

    // Collego la subscription locale il valore del subject del servizio
    this.onResetEAbilitaCanone =
      this._quadriTecnici.onResetEAbilitaCanone$.subscribe({
        next: (res: boolean) => {
          // Resetto il canone e abilito il calcola
          this.clearAndEnableCanone();
        },
      });

    // Collego la subscription locale il valore del subject del servizio
    this.onResetEDisabilitaCanone =
      this._quadriTecnici.onResetEDisabilitaCanone$.subscribe({
        next: (res: boolean) => {
          // Resetto il canone e disabilito il calcola
          this.clearAndDisableCanone();
        },
      });
  }

  /**
   * Funzione che imposta le informazioni per il calcolo del canone.
   * @param idRiscossione string o number che identifica una riscossione.
   * @param dataRiscossione string che identifica la data di riscossione. La data deve avere formato: AAAA-MM-GG.
   */
  setDatiCalcoloCanone(
    idRiscossione: string | number,
    dataRiscossione: string
  ) {
    // Verifico che esistano gli input
    if (
      idRiscossione === undefined ||
      idRiscossione.toString() === '' ||
      Number(idRiscossione) < 0 ||
      !dataRiscossione
    ) {
      // Blocco il flusso
      return;
    }

    // Setto i dati per il calcolo del canone
    this.idRiscossione = idRiscossione.toString();
    this.dataRiscossione = dataRiscossione;
    // Abilito il pulsante
    this.disabledCalcolaCanone = false;
  }

  /**
   * Funzione che effettua il calcolo del canone.
   */
  calcolaCanone() {
    // Verifico che i dati per il calcolo del canone esistano
    if (!this.idRiscossione || !this.dataRiscossione) {
      return;
    }

    // Reset dell'alert
    this.resetAlert();

    // Lancio la funzione del servizio per il calcolo del canone
    this._datiTecnici
      .calcoloCanonePratica(this.idRiscossione, this.dataRiscossione)
      .subscribe({
        next: (success: CalcoloCanoneVo) => {
          // Recupero il canone calcolato
          let cc = success.calcolo_canone;
          // Effettuo un troncamento del calcolo del canone
          cc = Math.trunc(cc);

          // Imposto il valore per il canone
          this.canoneCalcolato = this._riscaUtilities.formatoImportoITA(cc);
          // Disabilito il pulsante
          this.disabledCalcolaCanone = true;
          // #
        },
        error: (e: RiscaServerError) => {
          // Gestisco l'errore con le verifiche
          this.onCCError(e);
        },
      });
  }

  /**
   * Funzione che gestisce gli errori generati dal calcolo canone.
   * @param e RiscaServerError con i dettagli dell'errore.
   */
  private onCCError(e: RiscaServerError) {
    // Lancio l'aggiornamento dell'alert del canone
    this.onServiziError(e);

    // Verifico se l'errore è dovuto alla regola mancante del calcolo canone
    const isRM = this.isRegolaMancante(e);
    // Verifico l'errore
    if (isRM) {
      // Recupero la funzione da gestire
      this.onRegolaMancante(e);
      // #
    } else {
      // Gestisco le logiche di errore normale
      this.calcoloCanoneError.emit(e);
      this._quadriTecnici.calcoloCanoneError(e);
    }
  }

  /**
   * Funzione che gestisce le logiche se l'errore generato è: regola mancante.
   * @param e RiscaServerError con i dettagli dell'errore.
   */
  private onRegolaMancante(e: RiscaServerError) {
    // Disabilito il pulsante
    this.disabledCalcolaCanone = true;
    // Gestisco le logiche come se fosse una success senza aver trovato dati
    this.regolaMancanteCC.emit(e);
    this._quadriTecnici.regolaMancanteCC(e);
  }

  /**
   * Funzione di clear e disabilitazione del canone.
   */
  clearAndDisableCanone() {
    // Reset dell'alert
    this.resetAlert();
    // Resetto il campo del calcolo del canone
    this.canoneCalcolato = undefined;
    // Disabilito il pulsante
    this.disabledCalcolaCanone = true;
  }

  /**
   * Funzione di clear e abilitazione del canone.
   */
  clearAndEnableCanone() {
    // Reset dell'alert
    this.resetAlert();
    // Resetto il campo del calcolo del canone
    this.canoneCalcolato = undefined;
    // Disabilito il pulsante
    this.disabledCalcolaCanone = false;
  }

  /**
   * ##################################
   * FUNZIONI DI UTILITY DEL COMPONENTE
   * ##################################
   */

  /**
   * Funzione ad hoc creata per gestire la casistica in cui non viene trovata la regola del calcolo del canone.
   * Si verifica uno specifico codice e uno specifico status, entrambi generati dal server.
   * @param e RiscaServerError con l'errore generato.
   * @returns boolean che definisce se l'errore generato è perché non è stata trovata la regola [true] o è un altro tipo d'errore [false].
   */
  private isRegolaMancante(e: RiscaServerError): boolean {
    // Verifico l'input
    if (!e) {
      // Altro errore
      return false;
    }

    // Variabili di comodo
    const notFS = RiscaServerStatus.notFound;
    const eC = RiscaNotifyCodes.C003;
    // Ritorno il controllo
    return this._riscaUtilities.isServerErrorManageable(e, notFS, eC);
  }

  /**
   * Funzione di comodo per il reset dell'alert.
   */
  private resetAlert() {
    // Resetto l'alert
    this.alertConfigs = new RiscaAlertConfigs();
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */

  /**
   * Getter che verifica se l'oggetto alertConfigs esiste e ha messaggi.
   */
  get alertConfigsCheck() {
    // Verifico e ritorno la condizione
    return this._riscaAlert.alertConfigsCheck(this.alertConfigs);
  }
}

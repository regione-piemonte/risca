import { Component, Input, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { DilazioneVo } from '../../../../core/commons/vo/dilazione-vo';
import { RataVo } from '../../../../core/commons/vo/rata-vo';
import { StatoDebitorioVo } from '../../../../core/commons/vo/stato-debitorio-vo';
import { UserService } from '../../../../core/services/user.service';
import {
  DilazioneSDTable,
  DilazioneSDTableConfigs,
} from '../../../../shared/classes/risca-table/generali-amministrativi-dilazione/dilazione.table';
import {
  RateSDTable,
  RateSDTableConfigs,
} from '../../../../shared/classes/risca-table/generali-amministrativi-dilazione/rate.table';
import { RiscaUtilityModalComponent } from '../../../../shared/components/risca/risca-modals/risca-utility-modal/risca-utility-modal.component';
import { RiscaAlertService } from '../../../../shared/services/risca/risca-alert.service';
import { RiscaMessagesService } from '../../../../shared/services/risca/risca-messages.service';
import {
  IRiscaAlertConfigs,
  RiscaAlertConfigs,
  RiscaInfoLevels,
  RiscaServerError,
} from '../../../../shared/utilities';
import { RiscaNotifyCodes } from '../../../../shared/utilities/enums/risca-notify-codes.enums';
import { DatiContabiliService } from '../../service/dati-contabili/dati-contabili/dati-contabili.service';
import { DilazioneSDModalConsts } from './utilities/dilazione-sd-modal.consts';

@Component({
  selector: 'dilazione-sd-modal',
  templateUrl: './dilazione-sd-modal.component.html',
  styleUrls: ['./dilazione-sd-modal.component.scss'],
})
export class DilazioneSDModalComponent
  extends RiscaUtilityModalComponent
  implements OnInit
{
  /** Oggetto contenente una serie di costanti per il componente. */
  DSDM_C = DilazioneSDModalConsts;
  /** Titolo della modale. */
  title: string = this.DSDM_C.DILAZIONE_MODAL_TITLE;

  /** StatoDebitorio con le informazioni per popolare le tabelle. */
  @Input() statoDebitorio?: StatoDebitorioVo;

  /** DialzioneSDTable per la configurazione della tabella della dilazione dello stato debitorio. */
  dilazioneTable: DilazioneSDTable;
  /** RateSDTable per la configurazione della tabella delle rate dello stato debitorio */
  rateTable: RateSDTable;

  /**
   * Costruttore.
   */
  constructor(
    private _datiContabili: DatiContabiliService,
    activeModal: NgbActiveModal,
    riscaAlert: RiscaAlertService,
    private _riscaMessages: RiscaMessagesService,
    private _user: UserService
  ) {
    // Richiamo il super
    super(activeModal, riscaAlert);
  }

  ngOnInit(): void {
    // Lancio l'init del componente
    this.initComponente();
  }

  /**
   * ###############################
   * FUNZIONI DI INIT DEL COMPONENTE
   * ###############################
   */

  /**
   * Funzione di init del componente.
   */
  private initComponente() {
    // Lancio l'init delle tabelle
    this.initTables();
  }

  /**
   * Funzione di init delle tabelle del componente.
   */
  private initTables() {
    // Lancio l'inizializzazione della tabella dilazione
    this.initDilazioneTable();
    // Lancio l'inizializzazione della tabella rate
    this.initRateTable();
  }

  /**
   * Funzione di init per la tabella: dilazione.
   */
  private initDilazioneTable() {
    // Recupero l'id tipo dilazione dallo stato debitori
    const idDilazione = this.statoDebitorio?.id_tipo_dilazione;

    // Scarico le informazioni della dilazione tramite servizio
    this._datiContabili.getDilazione(this.idAmbito, idDilazione).subscribe({
      next: (dilazione: DilazioneVo) => {
        // Imposto la dilazione nella tabella
        const dilazioni = [dilazione];
        // Creo la configurazione per il costruttore della tabella
        const config: DilazioneSDTableConfigs = { dilazioni };
        // Inizializzo la tabella con le informazioni
        this.dilazioneTable = new DilazioneSDTable(config);
        // #
      },
      error: (error: RiscaServerError) => {
        // Recupero dall'errore il codice
        const code = error?.error?.code;

        // Recupero un messaggio specifico per la mancanza delle rate
        const m = this._riscaMessages.getMessage(code);
        // Definisco i parametri per l'alert
        const config: IRiscaAlertConfigs = {
          allowAlertClose: true,
          messages: [m],
          type: RiscaInfoLevels.danger,
        };

        // Aggiorno l'alert e informo l'operatore che mancano le rate
        this.alertConfigs = new RiscaAlertConfigs(config);
        // #
      },
    });
  }

  /**
   * Funzione di init per la tabella: rate.
   */
  private initRateTable() {
    // Recupero dall'oggetto in input dello stato debitorio le informzioni per la tabella
    const rateSD = this.statoDebitorio?.rate ?? [];
    const rate = rateSD.filter((r: RataVo) => r.id_rata_sd_padre != undefined);
    // Creo la configurazione per il costruttore della tabella
    const config: RateSDTableConfigs = { rate };
    // Inizializzo la tabella con le informazioni
    this.rateTable = new RateSDTable(config);

    // Verfico se esitono rate
    if (rate.length === 0) {
      // Recupero un messaggio specifico per la mancanza delle rate
      const m = this._riscaMessages.getMessage(RiscaNotifyCodes.I023);
      // Definisco i parametri per l'alert
      const config: IRiscaAlertConfigs = {
        allowAlertClose: true,
        messages: [m],
        type: RiscaInfoLevels.info,
      };

      // Aggiorno l'alert e informo l'operatore che mancano le rate
      this.alertConfigs = new RiscaAlertConfigs(config);
    }
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */

  /**
   * Getter che verifica l'esistenza della tabella: Dilazione.
   */
  get checkDilazioneTable() {
    // Esiste la tabella, verifico che ci siano dati
    return this.dilazioneTable?.source?.length > 0;
  }

  /**
   * Getter che verifica l'esistenza della tabella: Dilazione.
   */
  get checkRateTable() {
    // Esiste la tabella, verifico che ci siano dati
    return this.rateTable?.source?.length > 0;
  }

  /**
   * Getter di comodo per l'id ambito.
   */
  get idAmbito() {
    // Dal servizio recupero l'id ambito
    return this._user.idAmbito;
  }
}

import {
  Component,
  EventEmitter,
  Input,
  OnChanges,
  OnInit,
  Output,
  SimpleChanges,
} from '@angular/core';
import { clone } from 'lodash';
import { RiscaUtilitiesService } from 'src/app/shared/services/risca/risca-utilities/risca-utilities.service';
import { CommonConsts } from '../../../consts/common-consts.consts';
import { RiscaTableConsts } from '../../../consts/risca/risca-table.consts';
import {
  RiscaButtonConfig,
  RiscaButtonCss,
  RiscaTableCss,
  RiscaTableInput,
} from '../../../utilities';
import { RiscaTableDataConfig } from '../risca-table/utilities/risca-table.classes';
import {
  IRiscaTableACEvent,
  IRiscaTableAzioneCustom,
} from '../risca-table/utilities/risca-table.interfaces';

/**
 * Componente per la gestione e tracciatura degli elementi selezionati da una tabella risca.
 */
@Component({
  selector: 'risca-table-trace-selections',
  templateUrl: './risca-table-trace-selections.component.html',
  styleUrls: ['./risca-table-trace-selections.component.scss'],
})
export class RiscaTableTraceSelectionsComponent<T>
  implements OnInit, OnChanges
{
  /** Oggetto di costanti contenente le informazioni comuni all'applicazione. */
  C_C = new CommonConsts();
  /** Oggetto di costanti RiscaTableConsts per il componente. */
  RT_C = RiscaTableConsts;

  /** Input che definisce le configurazioni per gli stili della input. */
  @Input() cssConfig: RiscaTableCss;
  /** Input che definisce le configurazioni dati per la tabella. */
  @Input() dataConfig: RiscaTableInput;
  /** Input che definisce il source dati per la tabella. */
  @Input('source') inputSource: RiscaTableDataConfig<T>[];

  /** Output per la cancellazione della riga. */
  @Output('onRowDelete') onRowDelete$ = new EventEmitter<
    RiscaTableDataConfig<any>
  >();
  /** Output per un'azione custom sulla riga. */
  @Output('onRowCustomAction') onRowCustomAction$ = new EventEmitter<
    IRiscaTableACEvent<T>
  >();

  /** Array di RiscaTableDataConfig<T> che definisce il source della tabella. */
  tableSource: RiscaTableDataConfig<T>[];

  /** RiscaButtonCss per la configurazione del css per il pulsante di cancellazione di uno stato debitorio selezionato. */
  cssX: RiscaButtonCss;
  /** RiscaButtonConfig per la configurazione dati per il pulsante di cancellazione di uno stato debitorio selezionato. */
  dataX: RiscaButtonConfig;

  /**
   * ################
   * SETUP COMPONENTE
   * ################
   */

  /**
   * Costruttore.
   */
  constructor(private _riscaUtilities: RiscaUtilitiesService) {
    // Lancio il setup del componente
    this.setupComponente();
  }

  ngOnInit() {
    // Lancio il setup della tabella
    this.initTabella();
  }

  ngOnChanges(changes: SimpleChanges): void {
    // Verifico se è stato modificato il source
    if (changes.inputSource && !changes.inputSource.firstChange) {
      // Assegno il nuovo valore locale
      this.initSource();
    }
  }

  /**
   * ################################
   * FUNZIONE DI SETUP DEL COMPONENTE
   * ################################
   */

  /**
   * Funzione di setup per le informazioni del componente.
   */
  private setupComponente() {
    // Setup per il pulsante di cancellazione delle righe degli stati debitori selezionati
    this.setupBtnDeleteSD();
  }

  /**
   * Funzione per il setup delle configurazioni per la cancellazione di uno stato debitorio selezionato.
   */
  private setupBtnDeleteSD() {
    // Recupero dalle costanti le configurazioni del pulsante per il CSS
    const cssX: RiscaButtonCss = this.C_C.BTN_CLOSE_X_CSS;
    // Aggiungo alle classi di stile la specifica per il clore blu di risca
    cssX.customButton = `risca-blue-i ${cssX.customButton}`;
    // Assegno localmente le informazioni
    this.cssX = cssX;

    // Recupero dalle costanti le configurazioni del pulsante per i dati
    const dataX: RiscaButtonConfig = this.C_C.BTN_CLOSE_X_DATA;
    // Assegno localmente le informazioni
    this.dataX = dataX;
  }

  /**
   * ###########################
   * FUNZIONI DI INIT COMPONENTE
   * ###########################
   */

  /**
   * Funzione di setup per la tabella.
   */
  private initTabella() {
    // Lancio la funzione di setup del source
    this.initSource();
  }

  /**
   * Funzione di init del source. Verrà generata una copia della tabella per la gestione del componente.
   */
  private initSource() {
    // Resetto la tabella
    this.tableSource = [];

    // Verifico se l'inputSource è definito
    const inputSource = this.inputSource ? this.inputSource : [];
    // Effettuo una copia creando una nuova referenza dell'array
    this.tableSource = clone(inputSource);
  }

  /**
   * ###################################
   * FUNZIONE DI CLICK PER: ELIMINA RIGA
   * ###################################
   */

  /**
   * Funzione richiamata dal template quando viene premuto il pulsante di cancellazione.
   * @param row RiscaTableDataConfig<any> che definisce l'oggetto della riga principale che si vuole eliminare.
   */
  eliminaRiga(row: RiscaTableDataConfig<any>) {
    // Emetto l'evento di cancellazione riga
    this.onRowDelete$.emit(row);
  }

  /**
   * ###################################
   * FUNZIONE DI CLICK CUSTOM DELLA RIGA
   * ###################################
   */

  /**
   * Funzione richiamata dal template quando viene premuto sul custom DOM che gestisce un'azione custom.
   * @param customEvent IRiscaTableAzioneCustom con le informazioni dell'azione custom, contenente i dati della riga principale.
   */
  azioneCustom(customEvent: IRiscaTableAzioneCustom<T>) {
    // Converto l'oggetto per l'emissione
    const event: IRiscaTableACEvent<any> = {
      action: customEvent.action,
      row: customEvent.data,
    };
    // Emetto l'evento di azione custom
    this.onRowCustomAction$.emit(event);
  }

  /**
   * ################
   * GETTER DI COMODO
   * ################
   */

  /**
   * Getter che verifica la presenza di elementi.
   * @returns boolean con il risultato del check.
   */
  get checkSource(): boolean {
    // Verifico se esiste e ha elementi
    return this.inputSource?.length > 0;
  }
}

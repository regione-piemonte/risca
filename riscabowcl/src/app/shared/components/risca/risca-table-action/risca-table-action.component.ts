import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { RiscaTableConsts } from '../../../consts/risca/risca-table.consts';
import { RiscaTableDataConfig } from '../risca-table/utilities/risca-table.classes';
import { IRiscaTableAzioneCustom } from '../risca-table/utilities/risca-table.interfaces';
import { NgbPopoverConfig } from '@ng-bootstrap/ng-bootstrap';
import { RiscaPopoverConfig } from '../../../utilities';

/**
 * Componente adibito alla gestione delle tabelle dell'app Risca.
 */
@Component({
  selector: 'risca-table-action',
  templateUrl: './risca-table-action.component.html',
  styleUrls: ['./risca-table-action.component.scss'],
  providers: [NgbPopoverConfig],
})
export class RiscaTableActionComponent<T> implements OnInit {
  /** Oggetto di costanti RiscaTableConsts per il componente. */
  RT_C = RiscaTableConsts;

  /** Input che definisce le configurazioni dati per l'azione custom della tabella. */
  @Input('actionConfig') config: IRiscaTableAzioneCustom<T>;
  /** Input che definisce che l'action è un pulsante. Se nessuna tipologia è definita, questo verrà impostato a true di default. */
  @Input() isButton: boolean;
  /** Input che definisce che l'action è una checkbox. */
  @Input() isCheckbox: boolean;
  /** Input che definisce l'array di elementi selezionati. */
  @Input() sourcesSelected: RiscaTableDataConfig<any>[];

  /** Output per il click dell'azione custom, passando l'intera configurazione. */
  @Output() onActionClick = new EventEmitter<IRiscaTableAzioneCustom<T>>();
  /** Output per il click dell'azione custom, passando i dati source. */
  @Output() onDataEmit = new EventEmitter<RiscaTableDataConfig<any>>();
  /** Output per il chenge del checkbox. */
  @Output() onCheckboxChange = new EventEmitter<RiscaTableDataConfig<any>>();

  /**
   * ################
   * SETUP COMPONENTE
   * ################
   */

  constructor() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }

  ngOnInit() {
    // Verifico l'input
    if (!this.config) {
      throw new Error('actionConfig not defined');
    }

    // Verifico la tipologia di action
    this.initActionType();
  }

  /**
   * ###########################
   * FUNZIONI DI INIT COMPONENTE
   * ###########################
   */

  /**
   * Funzione di comodo per la verifica delle configurazioni della tipologia di action.
   */
  private initActionType() {
    // Verifico se è definito come pulsante
    if (this.isButton) {
      // Imposto a false il flag per la checkbox
      this.isCheckbox = false;
      // #
    } else if (!this.isButton && !this.isCheckbox) {
      // Imposto come default il pulsante
      this.isButton = true;
      this.isCheckbox = false;
    }
  }

  /**
   * ########################
   * FUNZIONALITA' COMPONENTE
   * ########################
   */

  /**
   * Funzione agganciata al pulsante dell'azione custom.
   */
  click() {
    // Emetto l'evento di click
    this.onActionClick.emit(this.config);
    // Emetto l'evento passando solo il dato
    this.onDataEmit.emit(this.config.data);
  }

  /**
   * Funzione agganciata al checkbox dell'azione custom.
   */
  checkboxChange(data: RiscaTableDataConfig<any>) {
    // Emetto l'evento di check
    this.onCheckboxChange.emit(data);
  }

  /**
   * ################
   * GETTER DI COMODO
   * ################
   */

  /**
   * Getter per la funzione di disabilitazione dell'azione.
   */
  get disable() {
    // Ritorno la funzione di disable
    return this.config.action?.disable;
  }

  /**
   * Getter per la funzione di disabilitazione dinamica dell'azione.
   */
  get disableDynamic() {
    // Ritorno la funzione di disable dinamica
    return this.config.action?.disableDynamic;
  }

  /**
   * Getter per il recupero della configurazione che gestisce la checkbox come selettore di tutta la riga dati.
   * @returns boolean che definisce la tipologia di configurazione come gestore dato della riga.
   */
  get isCheckboxRow(): boolean {
    // Recupero la configurazione se per checkbox
    const isC = this.isCheckbox;
    // Recupero la configurazione specifica della checkbox
    const checkboxConfigs = this.config?.action?.checkboxConfigs;
    const existCRConfig = checkboxConfigs != undefined;
    const noCRCSettings = existCRConfig && !checkboxConfigs.isRowData;

    // Ritorno la combinazione delle condizioni
    return isC && noCRCSettings;
  }

  /**
   * Getter per il recupero della configurazione che gestisce la checkbox come selettore di una proprietà della riga dati.
   * @returns boolean che definisce la tipologia di configurazione come gestore dato singolo della riga.
   */
  get isCheckboxData(): boolean {
    // Recupero la configurazione se per checkbox
    const isC = this.isCheckbox;
    // Recupero la configurazione specifica della checkbox
    const checkboxConfigs = this.config?.action?.checkboxConfigs;
    const existCRConfig = checkboxConfigs != undefined;
    const isCRCData = existCRConfig && checkboxConfigs.isRowData;

    // Ritorno la combinazione delle condizioni
    return isC && isCRCData;
  }

  /**
   * Getter che recupera la configurazione del popover, se definita.
   * @returns RiscaPopoverConfig di configurazione.
   */
  get popover(): RiscaPopoverConfig {
    // Ritorno dall'input la configurazione
    return this.config?.action?.popover;
  }
}

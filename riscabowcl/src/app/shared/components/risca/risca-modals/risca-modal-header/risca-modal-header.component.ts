import { Component, EventEmitter, Input, Output } from '@angular/core';
import { RiscaButtonCss, RiscaButtonConfig } from '../../../../utilities';
import { CommonConsts } from '../../../../consts/common-consts.consts';
@Component({
  selector: 'risca-modal-header',
  templateUrl: './risca-modal-header.component.html',
  styleUrls: ['./risca-modal-header.component.scss'],
})
export class RiscaModalHeaderComponent {
  /** Oggetto contenente i valori costanti dell'applicazione. */
  C_C = new CommonConsts();

  /** Titolo da mostrare nell'header */
  @Input() title: string;
  /** String che definisce le classi di stile d'aggiungere per la gestione del pulsante di chiusura modale. */
  @Input() customX: string;

  /** Evento al click della X: chiede al padre di chiudere la modale */
  @Output() onClickClose: EventEmitter<any> = new EventEmitter<any>();

  /** RiscaButtonCss per la configurazione del css per il pulsante di cancellazione di uno stato debitorio selezionato. */
  cssX: RiscaButtonCss;
  /** RiscaButtonConfig per la configurazione dati per il pulsante di cancellazione di uno stato debitorio selezionato. */
  dataX: RiscaButtonConfig;

  /**
   * Costruttore.
   */
  constructor() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }

  /**
   * ngOnInit.
   */
  ngOnInit() {
    // Lancio l'init del componente
    this.initComponente();
  }

  /**
   * #################
   * FUNZIONI DI SETUP
   * #################
   */

  /**
   * Funzione di setup per le informazioni del componente.
   */
  private initComponente() {
    // Setup per il pulsante di chiusura
    this.initBtnX();
  }

  /**
   * Funzione per il setup delle configurazioni per il pulsante di chiusura modale
   */
  private initBtnX() {
    // Verifico e definisco un valore per possibili classi di stili extra
    const extraCss: string = this.customX ?? '';

    // Recupero dalle costanti le configurazioni del pulsante per il CSS
    const cssX: RiscaButtonCss = this.C_C.BTN_CLOSE_X_CSS;
    // Aggiungo alle classi di stile la specifica per il clore blu di risca
    cssX.customButton = `${cssX.customButton} ${extraCss}`;
    // Assegno localmente le informazioni
    this.cssX = cssX;

    // Recupero dalle costanti le configurazioni del pulsante per i dati
    const dataX: RiscaButtonConfig = this.C_C.BTN_CLOSE_X_DATA;
    // Assegno localmente le informazioni
    this.dataX = dataX;
  }

  /**
   * #######################
   * FUNZIONI DEL COMPONENTE
   * #######################
   */

  /**
   * Emette l'evento di chiusura della modale al padre
   */
  modalDismiss() {
    this.onClickClose.emit();
  }
}

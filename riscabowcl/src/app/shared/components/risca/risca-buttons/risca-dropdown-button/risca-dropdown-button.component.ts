import {
  Component,
  EventEmitter,
  Input,
  Output,
  ViewChild,
} from '@angular/core';
import { NgbDropdown } from '@ng-bootstrap/ng-bootstrap';
import {
  RiscaCss,
  RiscaDDButtonConfig,
  RiscaDDButtonCss,
  RiscaDDItemConfig,
  SanitizerTypes,
} from '../../../../utilities';
import { NgBoostrapPlacements } from '../../../../utilities/enums/ng-bootstrap.enums';
import {
  NgbDDAutoClose,
  NgbDDContainer,
  NgbDDDisplay,
} from '../../../../utilities/types/ng-bootstrap.types';
import { RiscaButtonComponent } from '../risca-button/risca-button.component';

@Component({
  selector: 'risca-dropdown-button',
  templateUrl: './risca-dropdown-button.component.html',
  styleUrls: ['./risca-dropdown-button.component.scss'],
})
export class RiscaDropdownButtonComponent extends RiscaButtonComponent {
  /** SanitizerTypes che definisce i tipi di sanitizzazione dell'HTML possibili. */
  ST = SanitizerTypes;

  /**
   * #########################################################################################################
   * CONFIGURAZIONI PER NgbDropdown. Ref: https://ng-bootstrap.github.io/#/components/dropdown/api#NgbDropdown
   * #########################################################################################################
   */
  /** Input che gestisce la configurazione: autoClose. */
  @Input('autoClose') ngbDDAutoClose: NgbDDAutoClose = true;
  /** Input che gestisce la configurazione: container. */
  @Input('container') ngbDDContainer: NgbDDContainer = 'body';
  /** Input che gestisce la configurazione: display. */
  @Input('display') ngbDDDisplay: NgbDDDisplay = 'dynamic';
  /** Input che gestisce la configurazione: open. */
  @Input('open') ngbDDOpen: boolean = false;
  /** Input che gestisce la configurazione: placement. */
  @Input('placement') ngbDDPlacement: NgBoostrapPlacements =
    NgBoostrapPlacements.bottom;
  /** Input che gestisce la configurazione: dropdownCss. */
  @Input('dropdownCss') ngbDDCss: RiscaCss;

  /** Output che emette un evento quando viene cambiato lo stato di apertura della dropdown. */
  @Output('openChange') onOpenChanged = new EventEmitter<boolean>();

  /**
   * #########################
   * CONFIGURAZIONI COMPONENTE
   * #########################
   */
  /** Input che definisce le configurazioni per gli stili dropdown button. */
  @Input() cssConfig: RiscaDDButtonCss;
  /** Input che definisce le configurazioni per i dati dropdown button. */
  @Input() dataConfig: RiscaDDButtonConfig;

  /** Output che emette un evento quando viene premuto su un item della dropdown. */
  @Output() onItemClick = new EventEmitter<RiscaDDItemConfig>();

  /** ViewChild collegato alla struttura del dropdown. */
  @ViewChild('riscaBtnDD') riscaBtnDD: NgbDropdown;

  /**
   * Costruttore
   */
  constructor() {
    super();
  }

  /**
   * #######################
   * FUNZIONI DELLA DROPDOWN
   * #######################
   */

  /**
   * Funzione agganciata all'evento openChange della direttiva ngbDropdown.
   * @param isOpen boolean che specifica se la dropdown è stata aperta (true) o chiusa (false).
   */
  onOpenChange(isOpen: boolean) {
    // Emetto l'evento
    this.onOpenChanged.emit(isOpen);
  }

  /**
   * Funzione che apre la dropdown programmativamente.
   */
  open() {
    // Richiamo la funzione del componente
    this.riscaBtnDD.open();
  }

  /**
   * Funzione che chiude la dropdown programmativamente.
   */
  close() {
    // Richiamo la funzione del componente
    this.riscaBtnDD.close();
  }

  /**
   * Funzione che toggla la dropdown programmativamente.
   */
  toggle() {
    // Richiamo la funzione del componente
    this.riscaBtnDD.toggle();
  }

  /**
   * ##########################
   * FUNZIONALITA ITEM DROPDOWN
   * ##########################
   */

  /**
   * Funzione agganciata al click su un item della dropdown.
   * @param item RiscaDDItemConfig contenente la configurazione dati dell'oggetto dell'item.
   */
  itemClick(item: RiscaDDItemConfig) {
    // Emetto l'evento di click per l'item
    this.onItemClick.emit(item);
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */

  /**
   * Getter che verifica se la dropdown è aperta.
   * @returns boolean che definisce se la dropdown è aperta (true) o chiusa (false).
   */
  get isOpen(): boolean {
    // Richiamo la funzione del componente
    return this.riscaBtnDD?.isOpen() ?? false;
  }

  /**
   * Getter per la lista di configurazioni per gli items della dropdown.
   * @returns Array di RiscaDDItemConfig che contiene le configurazioni per gli item della dropdown.
   */
  get dropdownItems(): RiscaDDItemConfig[] {
    // Recupero dalla configurazione gli items
    return this.dataConfig?.dropdownItems ?? [];
  }
}

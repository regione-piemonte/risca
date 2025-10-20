import { TipoElaborazioneVo } from '../../../../../core/commons/vo/tipo-elaborazione-vo';
import { RiscaDropdownButtonClass } from '../../../../../shared/classes/risca-dropdown-button/risca-dropdown-button.class';
import {
  DynamicObjAny,
  IRiscaDDItemConfig,
} from '../../../../../shared/utilities';
import { NgBoostrapPlacements } from '../../../../../shared/utilities/enums/ng-bootstrap.enums';
import { AvvisoBonarioModalComponent } from '../../../modal/avviso-bonario-modal/avviso-bonario-modal.component';
import { BollettazioneOrdinariaModalComponent } from '../../../modal/bollettazione-ordinaria-modal/bollettazione-ordinaria-modal.component';
import { BollettazioneSpecialeModalComponent } from '../../../modal/bollettazione-speciale-modal/bollettazione-speciale-modal.component';
import { SollecitoPagamentoModalComponent } from '../../../modal/sollecito-pagamento-modal/sollecito-pagamento-modal.component';
import { TipiPrenotazione } from './nuova-prenotazione.enums';
import { BollettazioneGrandeIdroelettricoModalComponent } from '../../../modal/bollettazione-grande-idroelettrico/bollettazione-grande-idroelettrico-modal.component';

/**
 * Interfaccia di supporto per la definizione dei parametri della classe: NuovaPrenotazioneClass.
 */
export interface INuovaPrenotazioneClass {
  tipiPrenotazione?: TipoElaborazioneVo[];
}

/**
 * Classe di supporto per la gestione delle configurazioni per il dropdown button della pagina bollettazione.
 */
export class NuovaPrenotazioneDropDown extends RiscaDropdownButtonClass<TipoElaborazioneVo> {
  /** Costante string per la label del dropdown button. */
  protected DDB_LABEL = 'Nuova';
  /** String che definisce il codice di accesso elemento app. Può non essere valorizzato. */
  protected codeAEA: string;

  /** Array di TipoElaborazione con le informazioni per la compilazione della configurazione del dropdown button. */
  private _tipiPrenotazione?: TipoElaborazioneVo[];

  /** NgBoostrapPlacements che gestisce la configurazione: placement. */
  placement: NgBoostrapPlacements = NgBoostrapPlacements.bottom_right;
  /** DynamicObjAny che definisce la mappatura chiave [cod_tipo_elabora] e valore [Istanza componente modale] per l'apertura delle modali dell'applicazione. */
  modalMap: DynamicObjAny = {};

  /**
   * Costruttore.
   */
  constructor(c?: INuovaPrenotazioneClass) {
    // Richiamo il super
    super();

    // Lancio il setup delle configurazioni della classe
    this.setupConstructor(c);
    // Lancio il setup per le configurazioni delle modali
    this.setupModalMapper();
    // Lancio il setup delle configurazioni del pulsante
    this.setupDDButton(this._tipiPrenotazione);
  }

  /**
   * Funzione di comodo per l'assegnazione locale delle variabili dal costruttore.
   * @param c INuovaPrenotazioneClass contenente le configurazioni per la classe.
   */
  private setupConstructor(c?: INuovaPrenotazioneClass) {
    // Assegno localmente le variabili
    this._tipiPrenotazione = c?.tipiPrenotazione ?? [];
  }

  /**
   * Funzione di comodo che configura la mappatura dei dati per le nuove pronotazioni, con la specifica modale per l'inserimento dati.
   */
  private setupModalMapper() {
    // Resetto la mappatura
    this.modalMap = {};

    // Variabili di comodo
    const BS = TipiPrenotazione.bollettazione_speciale;
    const BO = TipiPrenotazione.bollettazione_ordinaria;
    const AB = TipiPrenotazione.avviso_bonario;
    const SP = TipiPrenotazione.sollecito_pagamento;
    const BGI = TipiPrenotazione.bollettazione_grande_idroelettrico;

    // Censisco le modali pronte per l'apertura
    this.modalMap[BS] = BollettazioneSpecialeModalComponent;
    this.modalMap[BO] = BollettazioneOrdinariaModalComponent;
    this.modalMap[AB] = AvvisoBonarioModalComponent;
    this.modalMap[SP] = SollecitoPagamentoModalComponent;
    this.modalMap[BGI] = BollettazioneGrandeIdroelettricoModalComponent;
  }

  /**
   * Funzione di setup per le configurazioni del pulsante di dropdown.
   * @param tipiPrenotazione Array di TipoElaborazione con i dati per popolare le configurazioni di dropdown.
   */
  setupDDButton(tipiPrenotazione: TipoElaborazioneVo[]) {
    // Richiamo la funzione di generazione delle configurazioni
    this.generaDDButtonConfigs(tipiPrenotazione);
  }

  /**
   * Funzione di supporto che genera un oggetto per la gestione degli item della dropdown.
   * @param tipoPrenotazione TipoElaborazione con i dati per popolare le configurazioni di dropdown.
   * @returns IRiscaDDItemConfig con le configurazioni per un item.
   */
  itemConfigMapper(tipoPrenotazione: TipoElaborazioneVo): IRiscaDDItemConfig {
    // Variabile di comodo
    const tp = tipoPrenotazione;
    // Definisco l'oggetto di configurazione dati per un item
    const itemConfig: IRiscaDDItemConfig = {
      id: tp.cod_tipo_elabora,
      label: tp.des_tipo_elabora,
      disabled: !this.isModalMapped(tp.cod_tipo_elabora),
      title: tp.des_tipo_elabora,
      data: tp,
    };
    // Creo e ritorno l'oggetto per l'item
    return itemConfig;
  }

  /**
   * Funzione di supporto che verifica se un determinato oggetto tipo elabora è già stato associato ad una componente modale.
   * @param codTipoElabora string che definisce il cod_tipo_elabora da verificare all'interno del mapper delle modali.
   * @returns boolean che definisce se il codice è stato mappato/associato ad una modale.
   */
  private isModalMapped(codTipoElabora: string): boolean {
    // Verifico l'input
    return this.modalMap[codTipoElabora] !== undefined;
  }
}

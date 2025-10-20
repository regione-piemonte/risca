import {
  IRiscaDDButtonConfig,
  IRiscaDDButtonCss,
  IRiscaDDItemConfig,
  RiscaButtonTypes,
  RiscaCss,
  RiscaDDButtonConfig,
  RiscaDDButtonCss,
  RiscaDDItemConfig,
} from '../../utilities';
import { NgBoostrapPlacements } from '../../utilities/enums/ng-bootstrap.enums';
import {
  NgbDDAutoClose,
  NgbDDContainer,
  NgbDDDisplay,
} from '../../utilities/types/ng-bootstrap.types';

/**
 * Classe di supporto per la gestione delle configurazioni per il dropdown button.
 * La classe fornisce delle funzioni built-in che permettono di poter definire le configurazioni per un dropdown button con facilità (ci sono delle logiche di default già impostate).
 */
export class RiscaDropdownButtonClass<T> {
  /** Costante string per la label del dropdown button. Questo è un valore di default. */
  protected DDB_LABEL = 'LABEL_DD_BTN';
  /** String che definisce il codice di accesso elemento app. Può non essere valorizzato. */
  protected codeAEA: string;
  /** RiscaButtonTypes che definisce il type (css) del bottone di dropdown. Per default è: primary. */
  protected ddbType: RiscaButtonTypes = RiscaButtonTypes.primary;

  /** RiscaDDButtonCss che definisce le configurazioni per gli stili dropdown button. */
  cssConfig: RiscaDDButtonCss;
  /** RiscaDDButtonConfig che definisce le configurazioni per i dati dropdown button. */
  dataConfig: RiscaDDButtonConfig;

  /**
   * #########################################################################################################
   * CONFIGURAZIONI PER NgbDropdown. Ref: https://ng-bootstrap.github.io/#/components/dropdown/api#NgbDropdown
   * #########################################################################################################
   */
  /** NgbDDAutoClose che gestisce la configurazione: autoClose. */
  autoClose: NgbDDAutoClose = true;
  /** NgbDDContainer che gestisce la configurazione: container. */
  container: NgbDDContainer = 'body';
  /** NgbDDDisplay che gestisce la configurazione: display. */
  display: NgbDDDisplay = 'dynamic';
  /** boolean che gestisce la configurazione: open. */
  open: boolean = false;
  /** NgBoostrapPlacements che gestisce la configurazione: placement. */
  placement: NgBoostrapPlacements = NgBoostrapPlacements.bottom;
  /** RiscaCss che gestisce la configurazione: dropdownCss. */
  dropdownCss: RiscaCss;

  /**
   * Costruttore.
   */
  constructor() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }

  /**
   * Funzione di setup per le configurazioni del pulsante di dropdown.
   * @param elements Array di T con i dati per popolare gli items della dropdown.
   */
  generaDDButtonConfigs(elements: T[]) {
    // Richiamo la configurazione data
    this.generaDDButtonDataConfigs(elements);
    // Richiamo la configurazione css
    this.generaDDButtonCssConfigs();
  }

  /**
   * Funzione di setup per le configurazioni del pulsante di dropdown, per la parte di "data".
   * @param elements Array di T con i dati per popolare gli items della dropdown.
   */
  generaDDButtonDataConfigs(elements: T[]) {
    // Verifico l'input
    elements = elements ?? [];

    // Definisco la configurazione per il pulsante
    const iDataConfig: IRiscaDDButtonConfig = {
      label: this.DDB_LABEL,
      codeAEA: this.codeAEA,
      dropdownItems: this.generaItemsDD(elements),
    };

    // Effettuo la new della classe
    this.dataConfig = new RiscaDDButtonConfig(iDataConfig);
  }

  /**
   * Funzione di setup per le configurazioni del pulsante di dropdown, per la parte di "css".
   * Le configurazioni per la parte di css prevedono le minime configurazioni per gestire correttamente il pulsante di dropdown.
   */
  generaDDButtonCssConfigs() {
    // Definisco la configurazione per il pulsante
    const iCssConfig: IRiscaDDButtonCss = {
      typeButton: this.ddbType,
    };

    // Effettuo la new della classe
    this.cssConfig = new RiscaDDButtonCss(iCssConfig);
  }

  /**
   * Funzione di supporto che genera gli oggetti per la gestione degli item della dropdown.
   * @param items Array di T con i dati per popolare le configurazioni di dropdown.
   * @returns Array di RiscaDDItemConfig con le configurazioni per gli item.
   */
  generaItemsDD(items: T[]): RiscaDDItemConfig[] {
    // Verifico l'input
    if (!items) {
      // Ritorno array vuoto
      return [];
    }

    // Rimappo le informazioni delle configurazioni per creare gli item
    return items.map((tp: T) => {
      // Richiamo la funzione per generare un item dalla configurazione
      return this.generaItemDD(tp);
    });
  }

  /**
   * Funzione di supporto che genera un oggetto per la gestione degli item della dropdown.
   * @param element T con i dati per popolare le configurazioni di dropdown.
   * @returns RiscaDDItemConfig con le configurazioni per un item.
   */
  generaItemDD(element: T): RiscaDDItemConfig {
    // Verifico l'input
    if (!element) {
      // Ritorno array vuoto
      return undefined;
    }

    // Definisco l'oggetto di configurazione dati per un item
    const itemConfig: IRiscaDDItemConfig = this.itemConfigMapper(element);
    // Creo e ritorno l'oggetto per l'item
    return new RiscaDDItemConfig(itemConfig);
  }

  /**
   * Funzione richiamata per effettuare il map dell'oggetto definito come generics estendendo la classe, in un oggetto RiscaDDItemConfig.
   * @param element T da mappare.
   * @returns IRiscaDDItemConfig risultato del mapping.
   */
  itemConfigMapper(element: T): IRiscaDDItemConfig {
    // METODO DA IMPLEMENTARE
    return undefined;
  }
}

import { Injectable } from '@angular/core';
import { IndirizzoSpedizioneVo } from '../../../../core/commons/vo/indirizzo-spedizione-vo';
import {
  IndirizzoSpedizioneConfig,
  ISComponentConfig,
} from '../../../modals/risca-indirizzi-spedizione-modal/utilities/risca-indirizzi-spedizione-modal.classes';
import { IndirizziSpedizioneModalConsts } from '../../../modals/risca-indirizzi-spedizione-modal/utilities/risca-indirizzi-spedizione-modal.consts';
import {
  IIndirizziSpedizioneModalConfig,
  IISComponentConfig,
  IISInitModalConfig,
} from '../../../modals/risca-indirizzi-spedizione-modal/utilities/risca-indirizzi-spedizione-modal.interfaces';
import { AppActions, RiscaServerErrorInfo } from '../../../utilities';
import { RiscaModalService } from '../risca-modal.service';
import { RiscaUtilitiesService } from '../risca-utilities/risca-utilities.service';

/**
 * Servizio di utility con funzionalità di comodo per la gestione degli script per il componente risca-indirizzo-spedizione-modal.
 */
@Injectable({ providedIn: 'root' })
export class RiscaIndirizziSpedizioneModalService {
  /** Oggetto contenente le costanti per il componente attuale. */
  private RISM_C = new IndirizziSpedizioneModalConsts();

  /**
   * Costruttore
   */
  constructor(
    private _riscaModal: RiscaModalService,
    private _riscaUtilities: RiscaUtilitiesService
  ) {}

  /**
   * ###################################################################
   * FUNZIONI DI STRUTTURAZIONE CONFIGURAZIONE PER L'INIT DEL COMPONENTE
   * ###################################################################
   */

  /**
   * Funzione che gestisce la generazione delle informazioni per quanto riguarda gli indirizzi di spedizione.
   * @param dataModal IIndirizziSpedizioneModalConfig con le configurazioni per la modale.
   * @returns IISInitModalConfig con le configurazioni per l'init del componente per la modale degli indirizzi di spedizione.
   */
  initModalConfigs(
    dataModal: IIndirizziSpedizioneModalConfig
  ): IISInitModalConfig {
    // Estraggo le informazioni dalla configurazione
    const indSMulti = dataModal?.indirizziSpedizione;
    const indSAction = dataModal?.modalita;

    // Lancio la funzione per gestire i multipli oggetti
    return this.initMultiIndSped(indSMulti, indSAction);
  }

  /**
   * Funzione che inizializza le informazioni per gli indirizzi di spedizione, partendo da una serie di oggetti.
   * @param indSMulti Array di IndirizzoSpedizioneVo con le informazioni degli indirizzi di spedizione.
   * @param indSAction AppActions che definisce la tipologia d'azione che gestirà i dati.
   * @returns IISInitModalConfig con le configurazioni per l'init del componente per la modale degli indirizzi di spedizione.
   */
  initMultiIndSped(
    indSMulti: IndirizzoSpedizioneVo[],
    indSAction?: AppActions
  ): IISInitModalConfig {
    // Definisco l'oggetto di configurazione per l'init
    let indSpedConfig: IISComponentConfig = {};

    // Assegno i valori ai dati di configurazione
    indSpedConfig.indirizziSpedizione = indSMulti ?? [];
    indSpedConfig.modalita = indSAction;

    // Creo la classe con le configurazioni
    const config = new ISComponentConfig(indSpedConfig);
    // Richiamo l'init degli indirizzi
    return this.initIndirizziSpedizione(config);
  }

  /**
   * Funzione che gestisce tutte le logiche per la parte del componente d'indirizzi spedizione.
   * @param config ISComponentConfig con le configurazioni dati.
   * @returns IISInitModalConfig con le configurazioni per l'init del componente per la modale degli indirizzi di spedizione.
   */
  initIndirizziSpedizione(config: ISComponentConfig): IISInitModalConfig {
    // Verifico l'input
    if (!config) {
      // Niente configurazione, blocco tutto
      return;
    }

    // Estraggo le informazioni dalla configurazione
    const { indirizziSpedizione } = config;

    // Verifico la lunghezza della lista
    if (indirizziSpedizione.length === 0) {
      // Richiamo il servizio per la gestione senza informazioni
      return this.initISNoData(config);
      // #
    } else {
      // Richiamo il servizio per la gestione con informazioni
      return this.initISWithData(config);
      // #
    }
  }

  /**
   * Funzione di comodo che gestisce la creazione delle configurazioni quando non esistono dati per un oggetto IndirizzoSpedizioneVo.
   * @param config ISComponentConfig con le configurazioni per l'init del componente della modale per gli indirizzi di spedizione.
   * @returns IISInitModalConfig con le configurazioni per l'init del componente per la modale degli indirizzi di spedizione.
   */
  initISNoData(config: ISComponentConfig): IISInitModalConfig {
    // Verifico l'input
    if (!config) {
      // Niente configurazione, blocco tutto
      return;
    }

    // Estraggo le informazioni dalla configurazione
    const { modalita } = config;
    // Variabili per la gestione della configurazione
    const parent = this.RISM_C.FORM_KEY_PARENT;
    const child = this._riscaUtilities.generateRandomId();
    const children: string[] = [child];

    // Creo un singolo oggetto di configurazione
    const indirizziConfigs = [
      new IndirizzoSpedizioneConfig({
        modalita,
        parentFormKey: parent,
        childFormKey: child,
      }),
    ];

    // Ritorno la configurazione creata
    return { indirizziConfigs, children };
  }

  /**
   * Funzione di comodo che gestisce la creazione delle configurazioni quando esistono dati per gli oggetti IndirizzoSpedizioneVo.
   * @param config ISComponentConfig con le configurazioni per l'init del componente della modale per gli indirizzi di spedizione.
   * @returns IISInitModalConfig con le configurazioni per l'init del componente per la modale degli indirizzi di spedizione.
   */
  initISWithData(config: ISComponentConfig): IISInitModalConfig {
    // Verifico l'input
    if (!config) {
      // Niente configurazione, blocco tutto
      return;
    }

    // Estraggo le informazioni dalla configurazione
    const { indirizziSpedizione, modalita } = config;
    // Ordino gli indirizzi di spedizione sulla base di logiche di FE
    const iiSOrdinatiFE = this.ordinaIndirizziSpedizioneFE(indirizziSpedizione);

    // Variabili per la gestione della configurazione
    const parent = this.RISM_C.FORM_KEY_PARENT;
    const children: string[] = [];
    const indirizziConfigs: IndirizzoSpedizioneConfig[] = [];

    // Itero gli oggetti degli indirizzi di spedizione
    for (let i = 0; i < iiSOrdinatiFE.length; i++) {
      // Recupero l'oggetto
      const indirizzoSpedizione = iiSOrdinatiFE[i];
      // Genero un id per il child
      const child = this._riscaUtilities.generateRandomId();

      // Creo l'oggetto di configurazione vero e proprio
      const indirizzoConfig = new IndirizzoSpedizioneConfig({
        modalita,
        indirizzoSpedizione,
        parentFormKey: parent,
        childFormKey: child,
      });

      // Aggiungo l'oggetto alla lista
      indirizziConfigs.push(indirizzoConfig);
      // Aggiungo l'id child alla lista di children
      children.push(child);
      // #
    }

    // Ritorno la configurazione creata
    return { indirizziConfigs, children };
  }

  /**
   * Funzione che ordina le informazioni per gli indirizzi di spedizione sulla base di logiche FE.
   * Le logiche richieste per la visualizzazione dati sono:
   * - Ordinare prima gli IS del soggetto (id_gruppo_soggetto == undefined);
   * - Ordinare poi gli IS dei gruppi (id_gruppo_soggetto != undefined);
   * @param indirizziSpedizione Array di IndirizzoSpedizioneVo con le informazioni da ordinare.
   * @return Array di IndirizzoSpedizioneVo con le informazioni ordinate secondo le logiche FE.
   */
  private ordinaIndirizziSpedizioneFE(
    indirizziSpedizione: IndirizzoSpedizioneVo[]
  ): IndirizzoSpedizioneVo[] {
    // Verifico l'input
    if (!indirizziSpedizione || indirizziSpedizione.length === 0) {
      // Non ci sono dati da ordinare
      return [];
    }
    // Verifico se c'è solo un elemento
    if (indirizziSpedizione.length === 1) {
      // Ritorno il singolo elemento
      return indirizziSpedizione;
    }

    // Variabile di comodo
    const iiS = indirizziSpedizione;

    // Ricerco gli elementi che non possiedono l'id gruppo
    const isSSNoGruppo = iiS.filter((ioS: IndirizzoSpedizioneVo) => {
      // Ritorno gli oggetti che non hanno l'id gruppo
      return ioS.id_gruppo_soggetto == undefined;
    });
    // Ricerco gli elementi che possiedono l'id gruppo
    const iiSGruppo = iiS.filter((ioS: IndirizzoSpedizioneVo) => {
      // Ritorno gli oggetti che non hanno l'id gruppo
      return ioS.id_gruppo_soggetto != undefined;
    });

    // Ritorno un array impostando le due strutture
    return [...isSSNoGruppo, ...iiSGruppo];
  }

  /**
   * Funzione di supporto che imposta un array di messaggi d'errore, estrapolandoli da un oggetto di errore lato server.
   * @param errors Array di RiscaServerErrorInfo con gli errori generati dal server.
   * @returns Array di string, con la lista dei messaggi d'errore.
   */
  messaggiErroreVerifyIS(errors: RiscaServerErrorInfo[]): string[] {
    // Richiamo la funzione di utility per l'estranzione dei dati
    return this._riscaUtilities.errorsMessageByRiscaServerErrorInfos(errors);
  }

  /**
   * ################################
   * FUNZIONI DI UTILITY DEL SERVIZIO
   * ################################
   */

  /**
   * Funzione di comodo che aggiorna un indirizzo di spedizione all'interno dell'array in input "indirizziOK".
   * @param indirizziOK Array di IndirizzoSpedizioneVo con le informazioni d'aggiornare.
   * @param indirizzoUpd IndirizzoSpedizioneVo che identifica l'oggetto con i dati aggiornati.
   * @returns boolean che informa il chiamante se l'oggetto è stato aggiornato correttamente.
   */
  aggiornaIndirizzoOK(
    indirizziOK: IndirizzoSpedizioneVo[],
    indirizzoUpd: IndirizzoSpedizioneVo
  ): boolean {
    // Verifico l'input
    if (!indirizzoUpd || !indirizziOK) {
      // Non ho i dati d'aggiornare
      return false;
    }

    // Vado a recuperare l'indice dell'oggetto nell'array di supporto
    const iIS = indirizziOK.findIndex((iOK: IndirizzoSpedizioneVo) => {
      // Ritorno il confronto tra gli id degli oggetti
      return iOK.id_recapito_postel === indirizzoUpd.id_recapito_postel;
    });

    // Verifico di aver trovato l'id
    if (iIS !== -1) {
      // Oggetto trovato, lo aggiorno
      indirizziOK[iIS] = indirizzoUpd;
      // Operazione completata
      return true;
      // #
    } else {
      // Elemento non trovato
      return false;
    }
  }
}

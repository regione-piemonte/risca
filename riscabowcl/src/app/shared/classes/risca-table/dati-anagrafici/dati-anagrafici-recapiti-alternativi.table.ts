import { RecapitialternativiConsts } from 'src/app/features/soggetti/consts/recapiti-alternativi/recapiti-alternativi.consts';
import { RiscaTableDataConfig } from 'src/app/shared/components/risca/risca-table/utilities/risca-table.classes';
import { CommonConsts } from 'src/app/shared/consts/common-consts.consts';
import { RecapitoVo } from '../../../../core/commons/vo/recapito-vo';
import {
  iconTableISDisable,
  iconTableISEnable,
  indirizzoSpedizioneFromRecapito,
} from '../../../../features/pratiche/service/dati-anagrafici/indirizzi-spedizione-utility/indirizzi-spedizione-utility.functions';
import { indirizzoRecapito } from '../../../services/risca/risca-utilities/risca-utilities.functions';
import {
  RiscaSortTypes,
  RiscaTableBodyTabMethodData,
  RiscaTableCss,
  RiscaTableSourceMap,
  RiscaTableSourceMapBody,
  RiscaTableSourceMapHeader,
} from '../../../utilities';
import { IRiscaTableClass, RiscaTableClass } from '../risca-table';

/**
 * Interfaccia personalizzata che definisce la struttura dei dati recapito alternativo impiegato come configurazione della tabella risca-table.
 */
export interface IDARecapitoAlternativoTable {
  presso: string;
  indirizzo: string;
  telefono: string;
  email: string;
  pec: string;
  tipoInvio: string;
}

/**
 * Interfaccia di supporto che definisce le configurazioni per la tabella tramite costruttore.
 */
export interface IDARecapitiAlternativiTableConfigs {
  recapiti?: RecapitoVo[];
  isGestioneAbilitata?: boolean;
  disableUserInputs?: boolean;
  idGruppo?: number;
  hideActions?: boolean;
  activeSelection?: boolean;
  isStatoDebitorio?: boolean;
}

/**
 * Classe usata per la generazione delle risca-table che contiene i risultati di ricerca per i recapiti.
 */
export class DARecapitiAlternativiTable
  extends RiscaTableClass<IDARecapitoAlternativoTable>
  implements IRiscaTableClass<IDARecapitoAlternativoTable>
{
  /** Oggetto CommonConsts contenente le costanti di uso comune. */
  C_C = new CommonConsts();
  /** Oggetto RecapitialternativiConsts contenente le costanti del componente. */
  RA_C = RecapitialternativiConsts;

  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Associa alla riscossione. */
  private STYLE_ASSOCIA_RISCOSSIONE = {
    width: '120px',
    'min-width': '120px',
    'text-align': 'center',
  };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Presso. */
  private STYLE_PRESSO = { 'max-width': '250px' };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Indirizzo. */
  private STYLE_INDIRIZZO = { 'max-width': '250px' };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Telefono. */
  private STYLE_TELEFONO = { 'max-width': '250px', 'min-width': '125px' };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Email. */
  private STYLE_EMAIL = { 'min-width': '125px', 'max-width': '250px' };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: PEC. */
  private STYLE_PEC = { 'min-width': '125px', 'max-width': '250px' };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Tipo invio. */
  private STYLE_TIPO_INVIO = {};
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Azioni. */
  private STYLE_AZIONI = { width: '120px' };

  /** Boolean che definisce la configurazione di disabilitazione degli elementi in app per ambito. */
  private _isGestioneAbilitata: boolean;
  /** Boolean che definisce la configurazione di disabilitazione degli elementi in app per profilo. */
  private _disableUserInputs: boolean;
  /** Boolean che definisce il flag per la gestione delle label per la pagina dello stato debitorio. */
  private _isStatoDebitorio: boolean;
  /** Boolean che definisce la configurazione di stampa delle sole action della tabella. */
  private hideActions: boolean = false;
  /** Boolean che forza l'abilitazione del radiobutton per la selezione del recapito alternativo. */
  private activeSelection: boolean = false;

  /** Id del gruppo.*/
  idGruppo: number;

  /**
   * Costruttore.
   */
  constructor(configs: IDARecapitiAlternativiTableConfigs) {
    // Richiamo il super
    super();

    // Estraggo le propriatà dalla configurazione
    const {
      recapiti,
      isGestioneAbilitata,
      disableUserInputs,
      idGruppo,
      hideActions,
      activeSelection,
      isStatoDebitorio,
    } = configs;
    // Assegno le variabili
    this._isGestioneAbilitata = isGestioneAbilitata ?? true;
    this._disableUserInputs = disableUserInputs ?? false;
    this._isStatoDebitorio = isStatoDebitorio ?? false;
    this.hideActions = hideActions ?? false;
    this.activeSelection = activeSelection ?? false;

    // Memorizzo il gruppo
    this.idGruppo = idGruppo;
    // Definisco il searcher e il converter
    this.setTableFunctions();
    // Definisco la struttura dati source
    this.setElements(recapiti);
    // Lancio il setup delle configurazioni
    this.setupClasse();
  }

  /**
   * Funzione di setup per la classe.
   */
  setTableFunctions() {
    // Definisco le logiche per il converter
    this.converter = (recapito: RecapitoVo) => {
      // Verifico l'oggetto esista
      if (!recapito) {
        return undefined;
      }

      // Definisco le proprietà che verranno tornate come parsing
      const presso = recapito.presso;
      const indirizzo = indirizzoRecapito(recapito);
      const telefono = recapito.telefono;
      const email = recapito.email;
      const pec = recapito.pec;
      const tipoInvio = recapito.tipo_invio?.des_tipo_invio;

      // Effettuo il parsing  delle informazioni in input
      return {
        presso,
        indirizzo,
        telefono,
        email,
        pec,
        tipoInvio,
      } as IDARecapitoAlternativoTable;
    };
  }

  /**
   * Funzione di setup per la classe.
   */
  private setupClasse() {
    // Lancio il setup del cssConfig
    this.setupCssConfig();
    // Lancio il setup del cssConfig
    this.setupDataConfig();
  }

  /**
   * Funzione di setup per la configurazione css.
   */
  setupCssConfig() {
    // Creo il setup css
    this.cssConfig = new RiscaTableCss();
  }

  /**
   * Funzione di setup per la configurazione dati.
   */
  setupDataConfig() {
    // Definisco la label di associazione
    let labelAssocia: string = 'Associa alla riscossione';
    // Verifico se la pagina è quella dello stato debitorio
    if (this._isStatoDebitorio) {
      // Cambio la label
      labelAssocia = 'Associa allo stato debitorio';
    }

    // Creo la configurazione RiscaTableSourceMap
    const sourceMap: RiscaTableSourceMap[] = [];
    // Inserisco all'interno della sourceMap le configurazioni per le colonne
    sourceMap.push(
      {
        header: new RiscaTableSourceMapHeader({
          label: labelAssocia,
          sortable: false,
          cssCell: this.STYLE_ASSOCIA_RISCOSSIONE,
        }),
        body: new RiscaTableSourceMapBody({
          useTabMethod: true,
          tabMethodData: {
            actions: [
              {
                action: 'radio',
                disable: (v: any) => {
                  // Ritorno la configurazione
                  return !this.activeSelection && this._disableUserInputs;
                },
              },
            ],
          },
          cssCell: this.STYLE_ASSOCIA_RISCOSSIONE,
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Presso',
          sortable: true,
          cssCell: this.STYLE_PRESSO,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: { property: 'presso', type: 'string' },
          cssCell: this.STYLE_PRESSO,
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Indirizzo',
          sortable: true,
          cssCell: this.STYLE_INDIRIZZO,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: { property: 'indirizzo', type: 'string' },
          cssCell: this.STYLE_INDIRIZZO,
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Telefono',
          sortable: true,
          cssCell: this.STYLE_TELEFONO,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: { property: 'telefono', type: 'string' },
          cssCell: this.STYLE_TELEFONO,
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Email',
          sortable: true,
          cssCell: this.STYLE_EMAIL,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: { property: 'email', type: 'string' },
          cssCell: this.STYLE_EMAIL,
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'PEC',
          sortable: true,
          cssCell: this.STYLE_PEC,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: { property: 'pec', type: 'string' },
          cssCell: this.STYLE_PEC,
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Tipo invio',
          sortable: true,
          sortType: RiscaSortTypes.crescente,
          cssCell: this.STYLE_TIPO_INVIO,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: { property: 'tipoInvio', type: 'string' },
          cssCell: this.STYLE_TIPO_INVIO,
        }),
      }
    );

    // Se non devo nascondere le actions, le inserisco
    if (!this.hideActions) {
      sourceMap.push({
        header: new RiscaTableSourceMapHeader({
          label: 'Azioni',
          sortable: false,
          cssCell: this.STYLE_AZIONI,
        }),
        body: new RiscaTableSourceMapBody({
          useTabMethod: true,
          tabMethodData: {
            actions: [
              this.actionModify(),
              this.actionDelete(),
              this.actionIndirizzoSpedizione(),
            ],
          },
        }),
      });
    }

    // Assegno alla variabile locale la configurazione
    this.dataConfig = { sourceMap };
  }

  /**
   * Crea l'action della tabella per la modifica dell'indirizzo spedizione
   * @returns RiscaTableBodyTabMethodData
   */
  private actionModify(): RiscaTableBodyTabMethodData {
    return {
      action: 'modify',
      disable: (v: any) => {
        // Ritorno la configurazione
        return this._disableUserInputs;
      },
    };
  }

  /**
   * Crea l'action della tabella per la cancellazione dell'indirizzo
   * @returns RiscaTableBodyTabMethodData
   */
  private actionDelete(): RiscaTableBodyTabMethodData {
    return {
      action: 'delete',
      disable: (v: any) => {
        // Ritorno la configurazione
        return !this._isGestioneAbilitata || this._disableUserInputs;
      },
    };
  }

  /**
   * Crea l'action della tabella per l'indirizzo spedizione
   * @returns RiscaTableBodyTabMethodData
   */
  private actionIndirizzoSpedizione(): RiscaTableBodyTabMethodData {
    // Definisco una funzione di comodo che verrà usata per la gestione della visualizzazione, che per la disabilitazione
    const iconUsage = (v: RiscaTableDataConfig<RecapitoVo>) => {
      // Recupero dall'input il flag selected
      const { original } = v;
      // controllo se l'icona dell'indirizzo di spedizione è disabilitata
      return this.isActionModificaISDisabled(original);
    };

    return {
      action: this.RA_C.INDIRIZZO_SPEDIZIONE,
      custom: true,
      title: this.RA_C.TITLE_INDIRIZZO_SPEDIZIONE,
      class: this.C_C.RISCA_TABLE_ACTION,
      style: { width: '25px' },
      visible: (v: RiscaTableDataConfig<RecapitoVo>) => {
        // Richiamo e ritorno la funzione di appoggio
        return !iconUsage(v);
      },
      disable: (v: RiscaTableDataConfig<RecapitoVo>) => {
        // Verifico la configurazione di utilizzo
        const case1 = iconUsage(v);
        // Verifico le casistiche di disabilitazione
        return case1;
      },
      chooseIconEnabled: (v: RiscaTableDataConfig<RecapitoVo>, ...p) => {
        // Recupero l'id gruppo
        const idG = this.idGruppo;
        // Recupero dall'input il flag selected
        const { original } = v;
        // Ritorno l'icona corretta da visualizzare
        return this.getIconIndirizzoSpedizioneEnabled(original, idG);
      },
      chooseIconDisabled: (v: RiscaTableDataConfig<RecapitoVo>, ...p) => {
        // Recupero l'id gruppo
        const idG = this.idGruppo;
        // Recupero dall'input il flag selected
        const { original } = v;
        // Ritorno l'icona corretta da visualizzare
        return this.getIconIndirizzoSpedizioneDisabled(original, idG);
      },
    };
  }

  /**
   * Verifica se il pulsante della spedizione è abilitato o meno.
   * Se non esiste l'indirizzo di spedizione, allora l'icona è disabilitata.
   * @param recapito RecapitoVo con i dati del recapito.
   * @returns true se disabilitata.
   */
  isActionModificaISDisabled(recapito?: RecapitoVo): boolean {
    // Recupero un possibile indirizzo di spedizione
    const is = indirizzoSpedizioneFromRecapito(recapito, this.idGruppo);
    // Verifico e ritorno se esiste l'indirizzo di spedizione
    return !is;
  }

  /**
   * Determina quale pulsante mostrare in base ai dati dell'oggetto passato se l'icona è abilitata.
   * @param recapito RecapitoVo che definisce i dati per la selezione dell'icona.
   * @param idGruppo number che definisce l'id gruppo dell'indirizzo di spedizione da recuperare.
   * @returns la classe dell'icona corretta.
   */
  private getIconIndirizzoSpedizioneEnabled(
    recapito: RecapitoVo,
    idGruppo: number
  ) {
    // Richiamo la funzione di supporto per la definizione dell'icona
    return iconTableISEnable(recapito, idGruppo);
  }

  /**
   * Determina quale pulsante mostrare in base ai dati dell'oggetto passato se l'icona è disabilitata.
   * @param recapito RecapitoVo che definisce i dati per la selezione dell'icona.
   * @param idGruppo number che definisce l'id gruppo dell'indirizzo di spedizione da recuperare.
   * @returns la classe dell'icona corretta.
   */
  private getIconIndirizzoSpedizioneDisabled(
    recapito: RecapitoVo,
    idGruppo: number
  ) {
    // Richiamo la funzione di supporto per la definizione dell'icona
    return iconTableISDisable(recapito, idGruppo, true);
  }
}

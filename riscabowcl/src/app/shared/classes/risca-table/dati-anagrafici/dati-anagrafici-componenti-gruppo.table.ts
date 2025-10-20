import { SoggettoVo } from '../../../../core/commons/vo/soggetto-vo';
import { RiscaTableDataConfig } from '../../../components/risca/risca-table/utilities/risca-table.classes';
import {
  identificativoSoggetto,
  indirizzoRecapitoSoggetto,
  recapitoPrincipaleSoggetto,
  identificativoSoggettoCompleto,
} from '../../../services/risca/risca-utilities/risca-utilities.functions';
import {
  RiscaTableCss,
  RiscaTableSourceMap,
  RiscaTableSourceMapBody,
  RiscaTableSourceMapHeader,
} from '../../../utilities';
import { IRiscaTableClass, RiscaTableClass } from './../risca-table';

/**
 * Interfaccia personalizzata che definisce la struttura dei dati componente gruppo impiegato come configurazione della tabella risca-table.
 */
export interface IDAComponenteGruppoTable {
  tipoSoggetto: string;
  soggetto: string;
  codiceFiscale: string;
  partitaIva: string;
  indirizzo: string;
  tipoInvio: string;
  email: string;
  pec: string;
}

/**
 * Interfaccia di supporto che definisce le configurazioni per la tabella tramite costruttore.
 */
export interface IDAComponentiGruppoTableConfigs {
  componenti?: SoggettoVo[];
  AEA_pratica_DADisabled?: boolean;
}

/**
 * Classe usata per la generazione delle risca-table che contiene i dati per i componenti gruppo dei dati anagrafici.
 */
export class DAComponentiGruppoTable
  extends RiscaTableClass<IDAComponenteGruppoTable>
  implements IRiscaTableClass<IDAComponenteGruppoTable>
{
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Referente gruppo. */
  private STYLE_REFERENTE_GRUPPO = { 'text-align': 'center' };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Tipo soggetto. */
  private STYLE_TIPO_SOGGETTO = {};
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Soggetto. */
  private STYLE_SOGGETTO = { 'max-width': '250px' };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Codice fiscale. */
  private STYLE_CODICE_FISCALE = {};
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Partita IVA. */
  private STYLE_PARTITA_IVA = {};
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Indirizzo. */
  private STYLE_INDIRIZZO = { 'max-width': '250px' };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Tipo invio. */
  private STYLE_TIPO_INVIO = {};
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: E-mail. */
  private STYLE_E_MAIL = { 'max-width': '150px' };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: PEC. */
  private STYLE_PEC = { 'max-width': '150px' };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Azioni. */
  // private STYLE_AZIONI = { width: '85px' };

  /** Boolean che definisce la configurazione di disabilitazione degli elementi in app per profilo. */
  private _AEA_pratica_DADisabled: boolean;

  /**
   * Costruttore.
   */
  constructor(configs: IDAComponentiGruppoTableConfigs) {
    // Richiamo il super
    super();

    // Estraggo le propriatà dalla configurazione
    const { componenti, AEA_pratica_DADisabled } = configs;
    // Assegno le variabili
    this._AEA_pratica_DADisabled = AEA_pratica_DADisabled || false;

    // Definisco il searcher e il converter
    this.setTableFunctions();
    // Definisco la struttura dati source
    this.setElements(componenti);
    // Lancio il setup delle configurazioni
    this.setupClasse();
  }

  /**
   * Funzione di setup per la classe.
   */
  setTableFunctions() {
    // Definisco le logiche per il converter
    this.converter = (s: SoggettoVo) => {
      // Verifico l'oggetto esista
      if (!s) {
        return undefined;
      }

      // Recupero il recapito principale
      const rP = recapitoPrincipaleSoggetto(s);

      // Definisco le proprietà che verranno tornate come parsing
      const tipoSoggetto = s.tipo_soggetto?.des_tipo_soggetto || '';
      const soggetto = identificativoSoggetto(s);
      const codiceFiscale = s.cf_soggetto || '';
      const partitaIva = s.partita_iva_soggetto || '';
      const indirizzo = indirizzoRecapitoSoggetto(s, rP);
      const tipoInvio = rP?.tipo_invio?.des_tipo_invio || '';
      const email = rP?.email || '';
      const pec = rP?.pec || '';

      // Effettuo il parsing  delle informazioni in input
      return {
        tipoSoggetto,
        soggetto,
        codiceFiscale,
        partitaIva,
        indirizzo,
        tipoInvio,
        email,
        pec,
      } as IDAComponenteGruppoTable;
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
    // Creo la configurazione RiscaTableSourceMap
    const sourceMap: RiscaTableSourceMap[] = [];
    // Inserisco all'interno della sourceMap le configurazioni per le colonne
    sourceMap.push(
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Referente gruppo',
          sortable: false,
          cssCell: this.STYLE_REFERENTE_GRUPPO,
        }),
        body: new RiscaTableSourceMapBody({
          useTabMethod: true,
          tabMethodData: {
            actions: [
              {
                action: 'radio',
                disable: (v: RiscaTableDataConfig<SoggettoVo>) => true,
              },
            ],
          },
          cssCell: this.STYLE_REFERENTE_GRUPPO,
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Tipo soggetto',
          sortable: true,
          cssCell: this.STYLE_TIPO_SOGGETTO,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'tipoSoggetto',
            type: 'string',
          },
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Soggetto',
          sortable: true,
          cssCell: this.STYLE_SOGGETTO,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'soggetto',
            type: 'string',
          },
          cssCell: this.STYLE_SOGGETTO,
          titleCustom: (s: SoggettoVo) => {
            // Ritorno un title "esteso"
            return identificativoSoggettoCompleto(s);
          },
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Codice fiscale',
          sortable: true,
          cssCell: this.STYLE_CODICE_FISCALE,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'codiceFiscale',
            type: 'string',
          },
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Partita IVA',
          sortable: true,
          cssCell: this.STYLE_PARTITA_IVA,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'partitaIva',
            type: 'string',
          },
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
          sourceData: {
            property: 'indirizzo',
            type: 'string',
          },
          cssCell: this.STYLE_INDIRIZZO,
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Tipo invio',
          sortable: true,
          cssCell: this.STYLE_TIPO_INVIO,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'tipoInvio',
            type: 'string',
          },
          cssCell: this.STYLE_TIPO_INVIO,
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'E-mail',
          sortable: true,
          cssCell: this.STYLE_E_MAIL,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'email',
            type: 'string',
          },
          cssCell: this.STYLE_E_MAIL,
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
          sourceData: {
            property: 'pec',
            type: 'string',
          },
          cssCell: this.STYLE_PEC,
        }),
      }
      // {
      //   header: new RiscaTableSourceMapHeader({
      //     label: 'Azioni',
      //     sortable: false,
      //     cssCell: this.STYLE_AZIONI,
      //   }),
      //   body: new RiscaTableSourceMapBody({
      //     useTabMethod: true,
      //     tabMethodData: {
      //       actions: [
      //         {
      //           action: 'detail',
      //           disable: (v: RiscaTableDataConfig<SoggettoVo>) => {
      //             // Verifico la configurazione
      //             const disabled = this._AEA_pratica_DADisabled;
      //             // Verifico il disable dalla configurazione
      //             if (disabled) {
      //               // La configurazione comanda
      //               return true;
      //             }

      //             // Recupero dall'input il flag selected
      //             const { selected } = v;
      //             // Se è selezionato, disabilito la cancellazione
      //             return selected;
      //           },
      //         },
      //       ],
      //     },
      //     cssCell: this.STYLE_AZIONI,
      //   }),
      // }
    );

    // Assegno alla variabile locale la configurazione
    this.dataConfig = { sourceMap };
  }
}

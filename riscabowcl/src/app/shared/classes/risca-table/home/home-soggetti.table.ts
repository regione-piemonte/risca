import { SoggettoVo } from '../../../../core/commons/vo/soggetto-vo';
import { AccessoElementiAppKeyConsts } from '../../../../core/consts/accesso-elementi-app/accesso-elementi-app.consts';
import { AccessoElementiAppService } from '../../../../core/services/accesso-elementi-app.service';
import { HomeConsts } from '../../../../features/home/consts/home/home.consts';
import { CommonConsts } from '../../../consts/common-consts.consts';
import {
  identificativoSoggetto,
  identificativoSoggettoCompleto,
  indirizzoRecapitoSoggetto,
  recapitoPrincipaleSoggetto,
} from '../../../services/risca/risca-utilities/risca-utilities.functions';
import {
  RiscaSortTypes,
  RiscaTableBodyTabMethods,
  RiscaTableCss,
  RiscaTablePagination,
  RiscaTableSourceMap,
  RiscaTableSourceMapBody,
  RiscaTableSourceMapHeader,
} from '../../../utilities';
import { IRiscaTableClass, RiscaTableClass } from '../risca-table';

/**
 * Interfaccia personalizzata che definisce la struttura dei dati componente gruppo impiegato come configurazione della tabella risca-table.
 */
export interface IHomeSoggettiTable {
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
 * Classe usata per la generazione delle risca-table che contiene i risultati di ricerca per i soggetti nella home page.
 */
export class HomeSoggettiTable
  extends RiscaTableClass<IHomeSoggettiTable>
  implements IRiscaTableClass<IHomeSoggettiTable>
{
  /** Oggetto di costanti comuni all'applicazione. */
  private C_C = new CommonConsts()
  /** Oggetto contenente i valori costanti per il componente. */
  private H_C = HomeConsts;
  /** Oggetto contenente i valori costanti per le chiavi d'accesso agli elementi dell'app. */
  private AEAK_C = AccessoElementiAppKeyConsts;

  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Azioni. */
  private STYLE_AZIONI = {
    width: '110px',
    'min-width': '110px',
    'max-width': '110px',
  };

  /** Boolean che definisce la configurazione d'attivazione/disattivazione per un nuovo soggetto (gestendo il pulsante della tabella) */
  private newSoggettoDisabled: boolean = false;

  /**
   * Costruttore.
   */
  constructor(
    soggetti: SoggettoVo[],
    accessoElementiApp: AccessoElementiAppService
  ) {
    // Richiamo il super
    super();

    // Lancio il setup per la configurazione di accesso alle funzionalità
    this.setupAccessoElementiTable(accessoElementiApp);
    // Definisco il searcher e il converter
    this.setTableFunctions();
    // Definisco la struttura dati source
    this.setElements(soggetti);
    // Lancio il setup delle configurazioni
    this.setupClasse();
  }

  /**
   * Funzione che definisce le logiche di accesso agli elementi della tabella secondo la configurazione "AccessoElementiApp".
   * @param accessoElementiApp AccessoElementiAppService con il servizio che contiene le configurazioni dell'applicazione.
   */
  setupAccessoElementiTable(accessoElementiApp: AccessoElementiAppService) {
    // Recupero la chiave per gli elementi
    const nsKey = this.AEAK_C.NUOVA_PRATICA_SOGG;

    // Recupero la configurazione per la chiave
    const nsDisabled = accessoElementiApp.isAEADisabled(nsKey);

    // Assegno la variabile localmente
    this.newSoggettoDisabled = nsDisabled;
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
      const tipoSoggetto = s.tipo_soggetto?.des_tipo_soggetto;
      const codiceFiscale = s.cf_soggetto;
      const soggetto = identificativoSoggetto(s);
      const partitaIva = s.partita_iva_soggetto;
      const indirizzo = indirizzoRecapitoSoggetto(s, rP);
      const tipoInvio = rP?.tipo_invio?.des_tipo_invio;
      const email = rP?.email;
      const pec = rP?.pec;

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
      } as IHomeSoggettiTable;
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
    this.cssConfig = new RiscaTableCss({
      showContainer: true,
      titleContainer: 'Soggetti',
    });
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
          label: 'Tipo soggetto',
          sortable: true,
          sortType: RiscaSortTypes.nessuno,
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
          sortType: RiscaSortTypes.crescente,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'soggetto',
            type: 'string',
          },
          cssCell: { 'max-width': '250px' },
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
          sortType: RiscaSortTypes.nessuno,
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
          sortType: RiscaSortTypes.nessuno,
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
          sortType: RiscaSortTypes.nessuno,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'indirizzo',
            type: 'string',
          },
          cssCell: { 'max-width': '250px' },
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Tipo invio',
          sortable: true,
          sortType: RiscaSortTypes.nessuno,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'tipoInvio',
            type: 'string',
          },
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'E-mail',
          sortable: true,
          sortType: RiscaSortTypes.nessuno,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'email',
            type: 'string',
          },
          cssCell: { 'max-width': '250px' },
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'PEC',
          sortable: true,
          sortType: RiscaSortTypes.nessuno,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'pec',
            type: 'string',
          },
          cssCell: { 'max-width': '250px' },
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Azioni',
          sortable: false,
          cssCell: this.STYLE_AZIONI,
        }),
        body: new RiscaTableSourceMapBody({
          useTabMethod: true,
          tabMethodData: {
            actions: [
              {
                action: RiscaTableBodyTabMethods.detail,
                title: 'Dettaglio',
              },
              {
                action: this.H_C.TAB_SOG_PRATICHE_COLLEGATE,
                custom: true,
                title: 'Pratiche collegate',
                class: this.C_C.RISCA_TABLE_ACTION,
                iconEnabled: 'rta-enable-pratiche-collegate',
                iconDisabled: 'rta-disable-pratiche-collegate',
                disable: (v: any) => false,
              },
              {
                action: this.H_C.TAB_SOG_NUOVA_PRATICA,
                custom: true,
                title: 'Nuova pratica',
                class: this.C_C.RISCA_TABLE_ACTION,
                iconEnabled: 'rta-enable-nuova-pratica',
                iconDisabled: 'rta-disable-nuova-pratica',
                disable: (v: any) => {
                  // Ritorno lo stato di disabilitazione della configurazione
                  return this.newSoggettoDisabled;
                },
              },
            ],
          },
          cssCell: this.STYLE_AZIONI,
        }),
      }
    );
    // Definisco la paginazione
    const pagination: RiscaTablePagination = this.getDefaultPagination();

    // Assegno alla variabile locale la configurazione
    this.dataConfig = { sourceMap, pagination };
  }

  /**
   * Genera la paginazione di default
   * @returns RiscaTablePagination come paginazione di default
   */
  getDefaultPagination(): RiscaTablePagination {
    return {
      total: this.source?.length || 0,
      label: 'risultati di ricerca',
      elementsForPage: 10,
      showTotal: true,
      currentPage: 1,
      sortBy: 'soggetto',
      sortDirection: RiscaSortTypes.crescente,
      maxPages: 10,
    };
  }
}

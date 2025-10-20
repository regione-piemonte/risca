import { AccessoElementiAppKeyConsts } from '../../../../core/consts/accesso-elementi-app/accesso-elementi-app.consts';
import { AccessoElementiAppService } from '../../../../core/services/accesso-elementi-app.service';
import { HomeConsts } from '../../../../features/home/consts/home/home.consts';
import { CommonConsts } from '../../../consts/common-consts.consts';
import {
  GruppoEComponenti,
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
export interface IHomeGruppiTable {
  nomeGruppo: string;
  soggettoReferente: string;
  codiceFiscaleReferente: string;
  numeroComponenti: number;
}

/**
 * Classe usata per la generazione delle risca-table che contiene i risultati di ricerca per i soggetti nella home page.
 */
export class HomeGruppiTable
  extends RiscaTableClass<IHomeGruppiTable>
  implements IRiscaTableClass<IHomeGruppiTable>
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
  private newGruppoDisabled: boolean = false;

  /**
   * Costruttore.
   */
  constructor(
    gruppiEReferenti: GruppoEComponenti[],
    accessoElementiApp: AccessoElementiAppService
  ) {
    // Richiamo il super
    super();

    // Lancio il setup per la configurazione di accesso alle funzionalità
    this.setupAccessoElementiTable(accessoElementiApp);
    // Definisco il searcher e il converter
    this.setTableFunctions();
    // Definisco la struttura dati source
    this.setElements(gruppiEReferenti);
    // Lancio il setup delle configurazioni
    this.setupClasse();
  }

  /**
   * Funzione che definisce le logiche di accesso agli elementi della tabella secondo la configurazione "AccessoElementiApp".
   * @param accessoElementiApp AccessoElementiAppService con il servizio che contiene le configurazioni dell'applicazione.
   */
  setupAccessoElementiTable(accessoElementiApp: AccessoElementiAppService) {
    // Recupero la chiave per gli elementi
    const ngKey = this.AEAK_C.NUOVA_PRATICA_GRUPPO;

    // Recupero la configurazione per la chiave
    const ngDisabled = accessoElementiApp.isAEADisabled(ngKey);

    // Assegno la variabile localmente
    this.newGruppoDisabled = ngDisabled;
  }

  /**
   * Funzione di setup per la classe.
   */
  setTableFunctions() {
    // Definisco le logiche per il converter
    this.converter = (v: GruppoEComponenti) => {
      // Verifico l'oggetto esista
      if (!v || !v.gruppo || !v.componenti) {
        // Non si può generare un oggetto
        return undefined;
      }

      // Recupero i dati dall'oggetto
      const { gruppo, componenti } = v;
      // Recupero il referente
      const capogruppo = gruppo.componenti_gruppo?.find(
        (c) => c.flg_capo_gruppo
      );
      const referente = componenti.find(
        (c) => c.id_soggetto === capogruppo?.id_soggetto
      );

      // Veriabili di comodo
      let datiReferente = '';
      // Verifico la dichiarazione identificativa
      if (referente?.cognome || referente?.nome) {
        // Definisco nome e cognome come dati
        datiReferente = `${referente.cognome || ''} ${referente.nome || ''}`;
        // #
      } else if (referente?.partita_iva_soggetto) {
        // Definisco la partita iva del soggetto
        datiReferente = `${referente.partita_iva_soggetto || ''}`;
      }

      // Definisco le proprietà che verranno tornate come parsing
      const nomeGruppo = gruppo.des_gruppo_soggetto || '';
      const soggettoReferente = datiReferente;
      const codiceFiscaleReferente = referente?.cf_soggetto || '';
      const numeroComponenti = gruppo.componenti_gruppo?.length || 0;

      // Dichiaro l'oggetto di ritorno per il parsing
      const o: IHomeGruppiTable = {
        nomeGruppo,
        soggettoReferente,
        codiceFiscaleReferente,
        numeroComponenti,
      };

      // Ritorno l'oggetto
      return o;
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
      titleContainer: 'Gruppi',
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
          label: 'Nome gruppo',
          sortable: true,
          sortType: RiscaSortTypes.crescente,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'nomeGruppo',
            type: 'string',
          },
          cssCell: { 'max-width': '250px' },
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Soggetto referente',
          sortable: true,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'soggettoReferente',
            type: 'string',
          },
          cssCell: { 'max-width': '250px' },
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Codice fiscale referente',
          sortable: true,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'codiceFiscaleReferente',
            type: 'string',
          },
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Numero componenti',
          sortable: true,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'numeroComponenti',
            type: 'string',
          },
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
                action: this.H_C.TAB_GRP_PRATICHE_COLLEGATE,
                custom: true,
                title: 'Pratiche collegate',
                class: this.C_C.RISCA_TABLE_ACTION,
                iconEnabled: 'rta-enable-pratiche-collegate',
                iconDisabled: 'rta-disable-pratiche-collegate',
                disable: (v: any) => false,
              },
              {
                action: this.H_C.TAB_GRP_NUOVA_PRATICA,
                custom: true,
                title: 'Nuova pratica',
                class: this.C_C.RISCA_TABLE_ACTION,
                iconEnabled: 'rta-enable-nuova-pratica',
                iconDisabled: 'rta-disable-nuova-pratica',
                disable: (v: any) => {
                  // Ritorno lo stato di disabilitazione della configurazione
                  return this.newGruppoDisabled;
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
      sortBy: 'nomeGruppo',
      sortDirection: RiscaSortTypes.crescente,
      maxPages: 10,
    };
  }
}

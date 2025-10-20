import { SoggettoVo } from 'src/app/core/commons/vo/soggetto-vo';
import {
  IdTipoRecapito,
  IRiscaTableCss,
  IRiscaTableGraphic,
  RiscaSortTypes,
  RiscaTableBodyTabMethods,
  RiscaTableCss,
  RiscaTableSourceMap,
  RiscaTableSourceMapBody,
  RiscaTableSourceMapHeader,
} from 'src/app/shared/utilities';
import { identificativoSoggetto, identificativoSoggettoCompleto } from '../../../services/risca/risca-utilities/risca-utilities.functions';
import { IRiscaTableClass, RiscaTableClass } from '../risca-table';

/**
 * Interfaccia personalizzata che definisce la struttura dei dati componente gruppo impiegato come configurazione della tabella risca-table.
 */
export interface IPraticheCollegateSoggettiTable {
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
export class PraticheCollegateSoggettiTable
  extends RiscaTableClass<IPraticheCollegateSoggettiTable>
  implements IRiscaTableClass<IPraticheCollegateSoggettiTable>
{
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Azioni. */
  private STYLE_AZIONI = {
    width: '110px',
    'min-width': '110px',
    'max-width': '110px',
  };

  /**
   * Costruttore.
   */
  constructor(soggetti: SoggettoVo[]) {
    // Richiamo il super
    super();

    // Definisco il searcher e il converter
    this.setTableFunctions();
    // Definisco la struttura dati source
    this.setElements(soggetti);
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
      if (!s) {return undefined;}

      // Recupero il recapito principale
      const recapitoPrincipale = s.recapiti?.find(
        (r) => r.tipo_recapito?.id_tipo_recapito === IdTipoRecapito.principale
      );

      // Definisco le proprietà che verranno tornate come parsing
      const tipoSoggetto = s.tipo_soggetto?.des_tipo_soggetto;
      const soggetto = identificativoSoggetto(s);
      const codiceFiscale = s.cf_soggetto;
      const partitaIva = s.partita_iva_soggetto;
      const indirizzo = recapitoPrincipale?.indirizzo;
      const tipoInvio = recapitoPrincipale?.tipo_invio?.des_tipo_invio;
      const email = recapitoPrincipale?.email;
      const pec = recapitoPrincipale?.pec;

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
      } as IPraticheCollegateSoggettiTable;
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
    // Definisco le configurazioni
    const graphicCss: IRiscaTableGraphic = {
      showContainer: true,
      titleContainer: 'Soggetto',
    };
    const tableCss: IRiscaTableCss = {
      thead: 'risca-thead-modal-soggetti',
      tbody: 'risca-tbody-modal-soggetti',
    };

    // Creo il setup css
    this.cssConfig = new RiscaTableCss(graphicCss, tableCss);
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
            ],
          },
          cssCell: this.STYLE_AZIONI,
        }),
      }
    );

    // Assegno alla variabile locale la configurazione
    this.dataConfig = { sourceMap };
  }
}

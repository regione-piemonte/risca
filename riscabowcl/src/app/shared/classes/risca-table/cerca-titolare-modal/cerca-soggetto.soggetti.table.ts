import { SoggettoVo } from '../../../../core/commons/vo/soggetto-vo';
import { identificativoSoggetto, identificativoSoggettoCompleto } from '../../../services/risca/risca-utilities/risca-utilities.functions';
import {
  IRiscaTableGraphic,
  RiscaTableCss,
  RiscaTableSourceMap,
  RiscaTableSourceMapBody,
  RiscaTableSourceMapHeader,
} from '../../../utilities';
import { IRiscaTableClass, RiscaTableClass } from '../risca-table';

/**
 * Interfaccia personalizzata che definisce la struttura dei dati soggetti impiegato come configurazione della tabella risca-table.
 */
export interface IFCSDatiSoggettoTable {
  codiceFiscale: string;
  denominazione: string;
  descTipoSoggetto: string;
  fonte: string;
  fonteRicerca: string;
}

/**
 * Interfaccia di supporto che definisce le configurazioni per la tabella tramite costruttore.
 */
export interface IFCSDatiSoggettoTableConfigs {
  soggetti?: SoggettoVo[];
  fonteRicerca?: string;
  mostraFonteRicerca?: boolean;
}

/**
 * Classe usata per la generazione delle risca-table che contiene i risultati di ricerca per i soggetti.
 */
export class FCSDatiSoggettoTable
  extends RiscaTableClass<IFCSDatiSoggettoTable>
  implements IRiscaTableClass<IFCSDatiSoggettoTable>
{
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella. */
  private STYLE_ASSOCIA_PRATICA = { width: '175px', 'text-align': 'center' };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella. */
  private STYLE_COD_FISC = { width: '170px' };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella. */
  private STYLE_DENOMINAZIONE = { 'min-width': '280px' };

  /** String che contiene l'indicazione per la fonte di ricerca del soggetto. */
  private fonteRicerca: string;
  /** Boolean che definisce la configurazione di gestione per la visualizzazione della colonna: fonteRicerca. */
  private mostraFonteRicerca: boolean;

  /**
   * Costruttore.
   */
  constructor(configs: IFCSDatiSoggettoTableConfigs) {
    // Richiamo il super
    super();

    // Estraggo le propriatà dalla configurazione
    const { soggetti, fonteRicerca, mostraFonteRicerca } = configs;

    // Assegno locamente le altre informazioni per la tabella
    this.fonteRicerca = fonteRicerca ?? '';
    this.mostraFonteRicerca = mostraFonteRicerca ?? true;

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
      if (!s) {
        return undefined;
      }

      // Definisco le proprietà che verranno tornate come parsing
      const codiceFiscale = s.cf_soggetto ?? '';
      const denominazione = identificativoSoggetto(s) ?? '';
      const descTipoSoggetto = s.tipo_soggetto?.des_tipo_soggetto ?? '';
      const fonte = s.fonte?.des_fonte ?? '';
      const fonteRicerca = this.fonteRicerca;

      // Definisco l'oggetto da passare alla configurazione
      const soggetto: IFCSDatiSoggettoTable = {
        codiceFiscale,
        denominazione,
        descTipoSoggetto,
        fonte,
        fonteRicerca,
      };

      // Effettuo il parsing  delle informazioni in input
      return soggetto;
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
      titleContainer: 'Soggetti',
    };
    // Definisco le configurazioni per gli stili
    const table = {
      'min-width': '875px',
    };

    // Creo il setup css
    this.cssConfig = new RiscaTableCss(graphicCss, { table });
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
          label: 'Associa alla pratica',
          sortable: false,
          cssCell: this.STYLE_ASSOCIA_PRATICA,
        }),
        body: new RiscaTableSourceMapBody({
          useCommonIcon: true,
          commonIconData: { fontAwesome: 'fa-check fa-lg risca-green' },
          cssCell: { ...this.STYLE_ASSOCIA_PRATICA, 'max-width': '250px' },
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Codice fiscale',
          sortable: false,
          cssCell: this.STYLE_COD_FISC,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: { property: 'codiceFiscale', type: 'string' },
          cssCell: { ...this.STYLE_COD_FISC, 'max-width': '250px' },
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Denominazione/Cognome Nome',
          sortable: false,
          cssCell: this.STYLE_DENOMINAZIONE,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: { property: 'denominazione', type: 'string' },
          cssCell: { 'max-width': '250px' },
          titleCustom: (s: SoggettoVo) => {
            // Ritorno un title "esteso"
            return identificativoSoggettoCompleto(s);
          },
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Tipo soggetto',
          sortable: false,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: { property: 'descTipoSoggetto', type: 'string' },
          cssCell: { 'max-width': '250px' },
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Fonte',
          sortable: false,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: { property: 'fonte', type: 'string' },
          cssCell: { 'max-width': '250px' },
        }),
      }
    );

    // Verifico la configurazione per la colonna "fonte ricerca"
    if (this.mostraFonteRicerca) {
      // Aggiungo la colonna per la fonte ricerca
      sourceMap.push({
        header: new RiscaTableSourceMapHeader({
          label: 'Fonte ricerca',
          sortable: false,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: { property: 'fonteRicerca', type: 'string' },
          cssCell: { 'max-width': '250px' },
        }),
      });
    }

    // Assegno alla variabile locale la configurazione
    this.dataConfig = { sourceMap };
  }
}

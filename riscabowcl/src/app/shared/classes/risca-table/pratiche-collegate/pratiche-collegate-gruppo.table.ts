import {
  GruppoEComponenti,
  RiscaTableCss,
  RiscaTableSourceMap,
  RiscaTableSourceMapBody,
  RiscaTableSourceMapHeader,
} from '../../../utilities';
import { RiscaTableBodyTabMethods } from '../../../utilities/enums/utilities.enums';
import { IRiscaTableClass, RiscaTableClass } from '../risca-table';

/**
 * Interfaccia personalizzata che definisce la struttura dei dati componente gruppo impiegato come configurazione della tabella risca-table.
 */
export interface IPraticheCollegateGruppoTable {
  nomeGruppo: string;
  soggettoReferente: string;
  codiceFiscaleReferente: string;
  numeroComponenti: number;
}

/**
 * Classe usata per la generazione delle risca-table che contiene i risultati di ricerca per i soggetti nella home page.
 */
export class PraticheCollegateGruppoTable
  extends RiscaTableClass<IPraticheCollegateGruppoTable>
  implements IRiscaTableClass<IPraticheCollegateGruppoTable>
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
  constructor(gruppiEReferenti: GruppoEComponenti[]) {
    // Richiamo il super
    super();

    // Definisco il searcher e il converter
    this.setTableFunctions();
    // Definisco la struttura dati source
    this.setElements(gruppiEReferenti);
    // Lancio il setup delle configurazioni
    this.setupClasse();
  }

  /**
   * Funzione di setup per la classe.
   */
  setTableFunctions() {
    // Definisco le logiche per il converter
    this.converter = (gec: GruppoEComponenti) => {
      // Verifico l'oggetto esista
      if (!gec || !gec.gruppo || !gec.componenti) {
        // Non si può generare un oggetto
        return undefined;
      }

      // Recupero i dati dall'oggetto
      const { gruppo, componenti } = gec;
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
      const o: IPraticheCollegateGruppoTable = {
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
      titleContainer: 'Gruppo',
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
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'codiceFiscaleReferente',
            type: 'string',
          },
          cssCell: { 'max-width': '250px' },
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Numero componenti',
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

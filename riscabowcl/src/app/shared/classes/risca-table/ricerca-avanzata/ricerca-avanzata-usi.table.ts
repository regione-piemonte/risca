import {
  IRicercaPraticaAvanzataReduced,
  IRicercaPraticaUsiReq, RiscaSortTypes, RiscaTableCss,
  RiscaTableSorting,
  RiscaTableSourceMap,
  RiscaTableSourceMapBody,
  RiscaTableSourceMapHeader
} from '../../../utilities';
import { IRiscaTableClass, RiscaTableClass } from '../risca-table';

/**
 * Interfaccia che rappresenta la struttura dati utilizzata all'interno della tabella dati generali amministrativi, per istanze e provvedimenti.
 */
interface IRiscaRicercaAvanzataPraticheUsiTable {
  usoDiLegge?: string;
  usoSpecifico?: string;
  unitaDiMisura?: string;
  quantitaDaA?: string;
  dataScadenzaEMASoISODaA?: string;
}

/**
 * Classe usata per la generazione delle risca-table che contiene i dati degli usi di legge inseriti dall'utente.
 */
export class RiscaRicercaAvanzataPraticheUsiTable
  extends RiscaTableClass<IRiscaRicercaAvanzataPraticheUsiTable>
  implements IRiscaTableClass<IRiscaRicercaAvanzataPraticheUsiTable>
{
  /**
   * Costruttore.
   */
   constructor(pratiche: IRicercaPraticaAvanzataReduced[]) {
    // Richiamo il super
    super();

    // Definisco il searcher e il converter
    this.setTableFunctions();
    // Definisco la struttura dati source
    this.setElements(pratiche);
    // Lancio il setup delle configurazioni
    this.setupClasse();
  }

  /**
   * Funzione di setup per la classe.
   */
  setTableFunctions() {
    // Definisco le logiche per il converter
    this.converter = (e: IRicercaPraticaUsiReq) => {
      let eParsed: IRiscaRicercaAvanzataPraticheUsiTable = {};

      // Definisco i dati per la tabella
      eParsed.usoDiLegge = e.usoDiLegge?.des_tipo_uso;
      eParsed.usoSpecifico = e.usoSpecifico?.des_tipo_uso;
      eParsed.unitaDiMisura = e.unitaDiMisura;
      eParsed.quantitaDaA = e?.quantitaDa != null ? 'da ' + e.quantitaDa : '';
      eParsed.quantitaDaA += e?.quantitaA != null ? '  a  ' + e.quantitaA : '';
      eParsed.dataScadenzaEMASoISODaA = e?.dataScadenzaEMASoISODa != null ? 'da ' + e.dataScadenzaEMASoISODa?.day + '.' + e.dataScadenzaEMASoISODa?.month + '.' + e.dataScadenzaEMASoISODa?.year + '      ': '';
      eParsed.dataScadenzaEMASoISODaA += e?.dataScadenzaEMASoISOA != null ? 'a ' + e.dataScadenzaEMASoISOA?.day + '.' + e.dataScadenzaEMASoISOA?.month + '.' + e.dataScadenzaEMASoISOA?.year : '';

      // Ritorno l'oggetto convertito
      return eParsed;
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
    // Definisco gli oggetti per le colonne della tabella
    const usoDiLegge: RiscaTableSourceMap = {
      header: new RiscaTableSourceMapHeader({
        label: 'Uso di legge',
        sortable: true,
        sortType: RiscaSortTypes.crescente,
        cssCell: { width: '350px' },
      }),
      body: new RiscaTableSourceMapBody({
        useSource: true,
        sourceData: { property: 'usoDiLegge', type: 'string' },
      }),
    };
    const usoSpecifico: RiscaTableSourceMap = {
      header: new RiscaTableSourceMapHeader({
        label: 'Uso specifico',
        sortable: false,
        cssCell: { width: '350px' },
      }),
      body: new RiscaTableSourceMapBody({
        useSource: true,
        sourceData: { property: 'usoSpecifico', type: 'string' },
      }),
    };
    const unitaDiMisura: RiscaTableSourceMap = {
      header: new RiscaTableSourceMapHeader({
        label: 'Unità di misura',
        sortable: false,
        cssCell: { width: '150px' },
      }),
      body: new RiscaTableSourceMapBody({
        useSource: true,
        sourceData: { property: 'unitaDiMisura', type: 'string' },
      }),
    };
    const quantitaDaA: RiscaTableSourceMap = {
      header: new RiscaTableSourceMapHeader({
        label: 'Quantità',
        sortable: false,
        cssCell: { width: '150px' },
      }),
      body: new RiscaTableSourceMapBody({
        useSource: true,
        sourceData: { property: 'quantitaDaA', type: 'string' },
      }),
    };
    const dataScadenzaEMASoISODaA: RiscaTableSourceMap = {
      header: new RiscaTableSourceMapHeader({
        label: 'Data scadenza EMAS o ISO',
        sortable: false,
        cssCell: { width: '300px' },
      }),
      body: new RiscaTableSourceMapBody({
        useSource: true,
        sourceData: { property: 'dataScadenzaEMASoISODaA', type: 'string' },
      }),
    };
    const azioni: RiscaTableSourceMap = {
      header: new RiscaTableSourceMapHeader({
        label: 'Azioni',
        cssCell: { width: '80px' },
      }),
      body: new RiscaTableSourceMapBody({
        useTabMethod: true,
        tabMethodData: { actions: [{ action: 'delete' }] },
      }),
    };

    // Creo la configurazione RiscaTableSourceMap per le colonne
    const sourceMap: RiscaTableSourceMap[] = [
      usoDiLegge,
      usoSpecifico,
      unitaDiMisura,
      quantitaDaA,
      dataScadenzaEMASoISODaA,
      azioni
    ];
    // Definisco le regole di sorting
    const sorting: RiscaTableSorting = { resetSortOnAdd: true };

    // Assegno alla variabile locale la configurazione
    this.dataConfig = { sourceMap, sorting };
  }
}

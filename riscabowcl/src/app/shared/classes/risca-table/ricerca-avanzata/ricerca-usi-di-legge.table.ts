import { IUsoLeggeSpecificoVo } from '../../../../core/commons/vo/uso-legge-specifico-vo';
import { UsoDiLeggeInfoSearch } from '../../../../features/pratiche/components/quadri-tecnici/services/quadri-tecnici/ricerca/quadri-tecnici-ricerca.service';
import { UsoLeggePSDAmbienteInfo } from '../../../../features/pratiche/components/quadri-tecnici/utilities/interfaces/dt-ambiente-pratica.interfaces';
import { RiscaTableDataConfig } from '../../../components/risca/risca-table/utilities/risca-table.classes';
import {
  convertNgbDateStructToViewDate,
  quantitaDaADescrittiva,
} from '../../../services/risca/risca-utilities/risca-utilities.functions';
import {
  RiscaSortTypes,
  RiscaTableCss,
  RiscaTableSorting,
  RiscaTableSourceMap,
  RiscaTableSourceMapBody,
  RiscaTableSourceMapHeader,
} from '../../../utilities';
import { IRiscaTableClass, RiscaTableClass } from '../risca-table';

/**
 * Interfaccia personalizzata che definisce la struttura dei dati uso di legge impiegato come configurazione della tabella risca-table.
 */
export interface IRicercaUsoDiLeggeInfoTable {
  usoDiLeggeDes?: string;
  listaUsoDiLeggeSpecificoDes?: string;
  unitaDiMisuraDes?: string;
  quantita?: string;
  dataScadenzaEmasIsoDa?: string;
  dataScadenzaEmasIsoA?: string;
}

/**
 * Classe usata per la generazione delle risca-table che contiene i dati degli usi di legge inseriti dall'utente.
 */
export class RicercaUsiDiLeggeTable
  extends RiscaTableClass<IRicercaUsoDiLeggeInfoTable>
  implements IRiscaTableClass<IRicercaUsoDiLeggeInfoTable>
{
  /**
   * Costruttore.
   */
  constructor(usiDiLeggeInfo?: UsoLeggePSDAmbienteInfo[]) {
    // Richiamo il super
    super();

    // Definisco il searcher e il converter
    this.setTableFunctions();
    // Definisco la struttura dati source
    this.setElements(usiDiLeggeInfo);
    // Lancio il setup delle configurazioni
    this.setupClasse();
  }

  /**
   * Funzione di setup per la classe.
   */
  setTableFunctions() {
    // Definisco le logiche per il searcher
    this.searcher = (
      element: RiscaTableDataConfig<UsoLeggePSDAmbienteInfo>,
      eSource: RiscaTableDataConfig<UsoLeggePSDAmbienteInfo>
    ) => {
      // Recupero gli id di confronto
      const idInput = element.original?.usoDiLegge?.id_tipo_uso;
      const idSource = eSource.original?.usoDiLegge?.id_tipo_uso;
      // Verifico se esistono e son lo stesso id
      return (
        idInput !== undefined && idSource !== undefined && idInput === idSource
      );
    };

    // Definisco le logiche per il converter
    this.converter = (usoDiLeggeInfo: UsoDiLeggeInfoSearch) => {
      // Verifico l'oggetto esista
      if (!usoDiLeggeInfo) {
        return undefined;
      }

      // Definisco una costante per separare le informazioni negli array
      const s = ';<br>';
      // Definisco delle funzioni di mapping di comodo
      const mapUsiLeggeSpec = (uls: IUsoLeggeSpecificoVo): string => {
        return uls.des_tipo_uso;
      };

      // Recupero le informazioni
      const usoLegge = usoDiLeggeInfo.usoDiLegge?.des_tipo_uso;
      const quantita = quantitaDaADescrittiva(
        usoDiLeggeInfo.quantitaDa,
        usoDiLeggeInfo.quantitaA
      );
      const usiSpec = usoDiLeggeInfo.usiDiLeggeSpecifici;
      const unitMisura = usoDiLeggeInfo.unitaDiMisura?.des_unita_misura;

      // Definisco le proprietà che verranno tornate come parsing
      const usoDiLeggeDes = usoLegge;
      const unitaDiMisuraDes = unitMisura;
      // Recupero le possibili date da visualizzare
      const dataSEIDa = usoDiLeggeInfo?.dataScadenzaEmasIsoDa;
      const dataSEIA = usoDiLeggeInfo?.dataScadenzaEmasIsoA;
      const dataScadenzaEmasIsoDa = convertNgbDateStructToViewDate(dataSEIDa);
      const dataScadenzaEmasIsoA = convertNgbDateStructToViewDate(dataSEIA);
      // Definisco la descrizione per gli usi di legge
      const usiDes =
        usiSpec?.length > 0 ? usiSpec.map(mapUsiLeggeSpec).join(s) + ';' : '';
      const listaUsoDiLeggeSpecificoDes = usiDes;

      // Effettuo il parsing  delle informazioni in input
      return {
        usoDiLeggeDes,
        listaUsoDiLeggeSpecificoDes,
        unitaDiMisuraDes,
        quantita,
        dataScadenzaEmasIsoDa,
        dataScadenzaEmasIsoA,
      } as IRicercaUsoDiLeggeInfoTable;
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
          label: 'Uso di legge',
          sortable: true,
          sortType: RiscaSortTypes.crescente,
          cssCell: { 'min-width': '110px' },
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: { property: 'usoDiLeggeDes', type: 'string' },
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Uso specifico',
          sortable: true,
          cssCell: { 'min-width': '160px' },
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'listaUsoDiLeggeSpecificoDes',
            type: 'string',
          },
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Unità di misura',
          sortable: true,
          cssCell: { 'min-width': '100px' },
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: { property: 'unitaDiMisuraDes', type: 'string' },
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Quantità',
          sortable: true,
          cssCell: { 'min-width': '125px' },
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'quantita',
            type: 'string',
          },
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Data scadenza EMAS/ISO da',
          sortable: true,
          cssCell: { 'min-width': '170px' },
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'dataScadenzaEmasIsoDa',
            type: 'date',
          },
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Data scadenza EMAS/ISO a',
          sortable: true,
          cssCell: { 'min-width': '170px' },
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'dataScadenzaEmasIsoA',
            type: 'date',
          },
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Azioni',
          sortable: false,
          cssCell: { 'min-width': '80px' },
        }),
        body: new RiscaTableSourceMapBody({
          useTabMethod: true,
          tabMethodData: { actions: [{ action: 'delete' }] },
        }),
      }
    );

    // Definisco le regole di sorting
    const sorting: RiscaTableSorting = { resetSortOnAdd: true };

    // Assegno alla variabile locale la configurazione
    this.dataConfig = { sourceMap, sorting };
  }
}

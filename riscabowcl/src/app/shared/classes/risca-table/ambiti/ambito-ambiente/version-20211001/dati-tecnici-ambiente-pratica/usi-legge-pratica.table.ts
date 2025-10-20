import { RiduzioneAumentoVo } from '../../../../../../../core/commons/vo/riduzione-aumento-vo';
import { IUsoLeggeSpecificoVo } from '../../../../../../../core/commons/vo/uso-legge-specifico-vo';
import { UsoLeggePSDAmbienteInfo } from '../../../../../../../features/pratiche/components/quadri-tecnici/utilities/interfaces/dt-ambiente-pratica.interfaces';
import { RiscaTableDataConfig } from '../../../../../../components/risca/risca-table/utilities/risca-table.classes';
import { CommonConsts } from '../../../../../../consts/common-consts.consts';
import { RiscaIconsCss } from '../../../../../../enums/icons.enums';
import {
  convertNgbDateStructToDateString,
  formatoImportoITA,
  importoITAToJsFloat,
} from '../../../../../../services/risca/risca-utilities/risca-utilities.functions';
import {
  RiscaFormatoDate,
  RiscaPopoverConfig,
  RiscaSortTypes,
  RiscaTableBodyTabMethods,
  RiscaTableCss,
  RiscaTableSorting,
  RiscaTableSourceMap,
  RiscaTableSourceMapBody,
  RiscaTableSourceMapHeader,
} from '../../../../../../utilities';
import { NgBoostrapPlacements } from '../../../../../../utilities/enums/ng-bootstrap.enums';
import { IRiscaTableClass, RiscaTableClass } from '../../../../risca-table';

/**
 * Interfaccia personalizzata che definisce la struttura dei dati uso di legge impiegato come configurazione della tabella risca-table.
 */
export interface IUsoLeggePraticaInfoTable {
  usoDiLeggeDes?: string;
  listUsoSpecDes?: string;
  unitaDiMisuraDes?: string;
  quantita?: number;
  listPercRidSiglaDes?: string;
  listPercAumSiglaDes?: string;
  qntFladaProfConPerc?: string;
  qntFaldaSupConPerc?: string;
  dataScadenzaEmasIso?: string;
}

/**
 * Interfaccia di supporto che definisce le configurazioni per la tabella tramite costruttore.
 */
export interface IUsiLeggePraticaTableConfigs {
  usiDiLeggeInfo?: UsoLeggePSDAmbienteInfo[];
  disableUserInputs?: boolean;
}

/**
 * Interfaccia di comodo che gestisce le informazioni di ritorno per la falda superficiale e la falda profonda.
 */
interface IFaldaSuperficialeEProdfonda {
  faldaSuperficiale: string;
  faldaProfonda: string;
}

/**
 * Classe usata per la generazione delle risca-table che contiene i dati degli usi di legge inseriti dall'utente.
 */
export class UsiLeggePraticaTable
  extends RiscaTableClass<IUsoLeggePraticaInfoTable>
  implements IRiscaTableClass<IUsoLeggePraticaInfoTable>
{
  /** Oggetto con le costanti comuni. */
  private C_C = new CommonConsts();
  /** Boolean che definisce la configurazione di disabilitazione degli elementi in app per profilo. */
  private _disableUserInputs: boolean;

  /**
   * Costruttore.
   */
  constructor(configs: IUsiLeggePraticaTableConfigs) {
    // Richiamo il super
    super();

    // Estraggo le propriatà dalla configurazione
    const { usiDiLeggeInfo, disableUserInputs } = configs;
    // Assegno le variabili
    this._disableUserInputs = disableUserInputs || false;

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
      element: RiscaTableDataConfig<any>,
      eSource: RiscaTableDataConfig<any>
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
    this.converter = (usoDiLeggeInfo: UsoLeggePSDAmbienteInfo) => {
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
      const mapPercRidAum = (ra: RiduzioneAumentoVo): string => {
        // Recupero le informazioni
        const sigla = ra.sigla_riduzione_aumento;
        const desc = ra.des_riduzione_aumento;
        // Verifico e ritorno le info concatenate
        return (sigla ? sigla + ' - ' : '') + desc;
      };

      // Recupero le informazioni
      const usoLegge = usoDiLeggeInfo.usoDiLegge?.des_tipo_uso;
      const quantita = usoDiLeggeInfo.quantita;
      const usiSpec = usoDiLeggeInfo.usiDiLeggeSpecifici;
      const unitMisura = usoDiLeggeInfo.unitaDiMisura?.des_unita_misura;
      const percRiduz = usoDiLeggeInfo.percRiduzioni;
      const percAumen = usoDiLeggeInfo.percAumenti;

      const qntFalProfNum = usoDiLeggeInfo.quantitaFaldaProfonda;
      const qntFalProf = this.numIta(qntFalProfNum, 4, true);

      const qntFalSupNum = usoDiLeggeInfo.quantitaFaldaSuperficiale;
      const qntFalSup = this.numIta(qntFalSupNum, 4, true);

      // Richiamo la funzione di gestione per le percentuali delle falde
      let percentuali: IFaldaSuperficialeEProdfonda;
      percentuali = this.percentualeFaldaProfondaESuperficiale(usoDiLeggeInfo);
      // Estraggo dai dati prodotti le informazioni
      const percFalProf: string = percentuali.faldaProfonda;
      const percFalSup: string = percentuali.faldaSuperficiale;

      const dataEI = convertNgbDateStructToDateString(
        usoDiLeggeInfo.dataScadenzaEmasIso,
        RiscaFormatoDate.view
      );

      // Definisco le proprietà che verranno tornate come parsing
      const usoDiLeggeDes = usoLegge;
      const unitaDiMisuraDes = unitMisura;
      const dataScadenzaEmasIso = dataEI;
      let listUsoSpecDes = '';
      if (usiSpec?.length > 0) {
        listUsoSpecDes = usiSpec.map(mapUsiLeggeSpec).join(s) + ';';
      }
      let listPercRidSiglaDes = '';
      if (percRiduz?.length > 0) {
        listPercRidSiglaDes = percRiduz.map(mapPercRidAum).join(s) + ';';
      }
      let listPercAumSiglaDes = '';
      if (percAumen?.length > 0) {
        listPercAumSiglaDes = percAumen.map(mapPercRidAum).join(s) + ';';
      }
      let qntFladaProfConPerc = '';
      if (qntFalProf) {
        qntFladaProfConPerc = `${qntFalProf} ${percFalProf}`;
      }
      let qntFaldaSupConPerc = '';
      if (qntFalSup) {
        qntFaldaSupConPerc = `${qntFalSup} ${percFalSup}`;
      }

      // Effettuo il parsing  delle informazioni in input
      const row: IUsoLeggePraticaInfoTable = {
        usoDiLeggeDes,
        listUsoSpecDes,
        unitaDiMisuraDes,
        quantita,
        listPercRidSiglaDes,
        listPercAumSiglaDes,
        qntFladaProfConPerc,
        qntFaldaSupConPerc,
        dataScadenzaEmasIso,
      };
      // Ritorno l'oggetto
      return row;
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
          label: 'COLSPAN_DETTAGLIO_GRANDE_IDROELETTRICO',
          colspan: true,
        }),
        body: new RiscaTableSourceMapBody({
          useTabMethod: true,
          tabMethodData: {
            actions: [
              {
                action: 'COLSPAN_DETTAGLIO_GRANDE_IDROELETTRICO',
                custom: true,
                title: 'Dettaglio aggiuntivi uso',
                class: this.C_C.RISCA_TABLE_ACTION,
                visible: (
                  v?: RiscaTableDataConfig<UsoLeggePSDAmbienteInfo>
                ) => {
                  // Verifico se esistono i dati extras dell'uso
                  return v.original?.extras != undefined;
                  // #
                },
                chooseIconEnabled: (v?: RiscaTableDataConfig<any>) => {
                  // Ritorno l'icona informativa
                  return RiscaIconsCss.info;
                  // #
                },
                popover: new RiscaPopoverConfig({
                  ngbPopover: (e: UsoLeggePSDAmbienteInfo) => {
                    // Definisco la struttura HTML da iniettare nel DOM
                    const innertHTML = e?.extras?.popoverPratica();
                    // Ritorno la struttura per il popover
                    return innertHTML;
                  },
                  popoverTitle: (e: UsoLeggePSDAmbienteInfo) => {
                    // Definisco la struttura HTML da iniettare nel DOM
                    const innertHTML = `Dati aggiuntivi`;
                    // Ritorno la struttura per il popover
                    return innertHTML;
                  },
                  placement: NgBoostrapPlacements.right,
                }),
              },
            ],
          },
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
            property: 'listUsoSpecDes',
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
            type: 'number',
            outputFormat: (v: number): string => {
              // Ritorno la formattazione numerica per italia
              return formatoImportoITA(v, 4);
            },
          },
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: '% di riduzione motivazione',
          sortable: true,
          cssCell: { 'min-width': '170px' },
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'listPercRidSiglaDes',
            type: 'string',
          },
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: '% di aumento motivazione',
          sortable: true,
          cssCell: { 'min-width': '160px' },
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'listPercAumSiglaDes',
            type: 'string',
          },
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Quantità falda profonda (%)',
          sortable: true,
          cssCell: { 'min-width': '170px' },
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'qntFladaProfConPerc',
            type: 'string',
          },
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Quantità falda superficiale (%)',
          sortable: true,
          cssCell: { 'min-width': '180px' },
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'qntFaldaSupConPerc',
            type: 'string',
          },
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Data scadenza EMAS/ISO',
          sortable: true,
          cssCell: { 'min-width': '170px' },
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'dataScadenzaEmasIso',
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
          tabMethodData: {
            actions: [
              {
                action: RiscaTableBodyTabMethods.modify,
                disable: (v: any) => {
                  // Recupero la configurazione
                  const disabled = this._disableUserInputs ?? false;
                  // Ritorno lo stato di disabled
                  return disabled;
                },
              },
              {
                action: RiscaTableBodyTabMethods.delete,
                disable: (v: any) => {
                  // Recupero la configurazione
                  const disabled = this._disableUserInputs ?? false;
                  // Ritorno lo stato di disabled
                  return disabled;
                },
              },
            ],
          },
        }),
      }
    );

    // Definisco le regole di sorting
    const sorting: RiscaTableSorting = { resetSortOnAdd: true };

    // Assegno alla variabile locale la configurazione
    this.dataConfig = { sourceMap, sorting };
  }

  /**
   * Funzione di supporto che invoca la formattazione di un numero in formato italiano.
   * @param n number da formattare.
   * @param decimals number con il numero di decimali da visualizzare dopo la virgola. Per default è: 2.
   * @param decimaliNonSignificativi boolean che definisce se, dalla gestione dei decimali, bisogna completare i decimali con gli 0 mancanti (non significativi). Per default è false.
   * @returns string formattata.
   */
  private numIta(
    n: number,
    decimals: number = 2,
    decimaliNonSignificativi: boolean = false
  ): string {
    // Ritorno la formattazione numerica per italia
    return formatoImportoITA(n, decimals, decimaliNonSignificativi);
  }

  /**
   * Formatta un importo italiano in un numero compatibile con javascript.
   * @param importo string da formattare.
   * @returns number con il numero correttamente convertito.
   */
  importoITAToJsFloat(importo: string): number {
    // Richiamo la funzione di comodo
    return importoITAToJsFloat(importo);
    // #
  }

  /**
   * Funzione che gestisce la formattazione e la gestione dei dati per le percentuali per la falda profonda e la falda superficiale.
   * @param usoDiLeggeInfo UsoLeggePSDAmbienteInfo con i dati dell'uso di legge da visualizzare.
   * @returns IFaldaSuperficialeEProdfonda con le informazioni generate dalla funzione.
   */
  private percentualeFaldaProfondaESuperficiale(
    usoDiLeggeInfo: UsoLeggePSDAmbienteInfo
  ): IFaldaSuperficialeEProdfonda {
    // Definisco un contenitore vuoto con le informazioni dati
    let datiFalde: IFaldaSuperficialeEProdfonda;
    datiFalde = { faldaProfonda: '', faldaSuperficiale: '' };

    // Verifico l'input della funzione
    if (!usoDiLeggeInfo) {
      // Ritorno l'oggetto vuoto
      return datiFalde;
    }

    /**
     * ### NOTA DELLO SVILUPPATORE ###
     * Il calcolo inizialmente prevedeva due formattazioni separate per le percentuali.
     * Essendo che però le percentuali possono essere a 4 decimali, ma con un troncamento a 2 decimali, capita che il troncamento vada a perdere un
     * valore d'arrotondamento che renderebbe la somma delle due percentuali: 100%.
     * Per evitare questo, si prende come base la falda profonda, si effettua un arrotondamento, e poi si sottrae da 100 il valore arrotondato.
     * Il risultato della sottazione sarà usato come valore per la falda superficiale.
     */

    /*
      ### VECCHIO CODICE ###
      let percFalProf: any = usoDiLeggeInfo.percFaldaProfonda;
      percFalProf = percFalProf ? `(${this.numIta(percFalProf)}%)` : '';
      let percFalSup: any = usoDiLeggeInfo.percFaldaSuperficiale;
      percFalSup = percFalSup ? `(${this.numIta(percFalSup)}%)` : '';
    */

    // Recupero la percentuale falda profonda dall'uso di legge
    let percFaldaProfonda: number;
    percFaldaProfonda = usoDiLeggeInfo.percFaldaProfonda;
    // Trasformo, troncando i decimali, la percentuale falda profonda
    let percFalProfIT: string;
    percFalProfIT = this.numIta(percFaldaProfonda, 2);

    // Verifico se è stato generato un valore per la falda profonda
    if (percFalProfIT == undefined || percFalProfIT == '') {
      // Non è stato generato nessun valore, ritorno le label vuote
      return datiFalde;
    }

    // Per il calcolo, trasformo la percentuale stringa in un numero a virgola mobile
    let percFalProfNum: number;
    percFalProfNum = this.importoITAToJsFloat(percFalProfIT);
    // Per la falda superficiale effettuo un calcolo partendo dal 100% e sottraendo la falda pronda
    let percFalSupfNum: number;
    percFalSupfNum = 100 - percFalProfNum;
    // Trasformo, troncando i decimali, la percentuale falda superficiale
    let percFalSupfIT: string;
    percFalSupfIT = this.numIta(percFalSupfNum, 2);

    // Definisco le variabili da utilizzare per
    let faldaProfonda: any = percFalProfIT ? `(${percFalProfIT}%)` : '';
    let faldaSuperficiale: any = percFalSupfIT ? `(${percFalSupfIT}%)` : '';

    // Ritorno le label generate
    return { faldaProfonda, faldaSuperficiale };
  }
}

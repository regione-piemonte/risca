import { DocumentoAllegatoVo } from 'src/app/core/commons/vo/documento-allegato-vo';
import { DocumentiAllegatiConsts } from 'src/app/features/pratiche/consts/documenti-allegati/documenti-allegati.consts';
import { RiscaTableDataConfig } from 'src/app/shared/components/risca/risca-table/utilities/risca-table.classes';
import { CommonConsts } from 'src/app/shared/consts/common-consts.consts';
import {
  RiscaIconsCss,
  RiscaIconsCssDisabled,
} from 'src/app/shared/enums/icons.enums';
import { convertServerDateToViewDate } from '../../../services/risca/risca-utilities/risca-utilities.functions';
import {
  IRiscaTableCss,
  RiscaSortTypes,
  RiscaTableBodyTabMethodData,
  RiscaTableCss,
  RiscaTablePagination,
  RiscaTableSourceMap,
  RiscaTableSourceMapBody,
  RiscaTableSourceMapHeader,
} from '../../../utilities';
import { IRiscaTableClass, RiscaTableClass } from './../risca-table';

/**
 * Funzione che ritorna un nuovo oggetto con la configurazione per la paginazione di default.
 */
export const DocumentiAllegatiTablePagination: () => RiscaTablePagination =
  () => {
    return {
      total: 0,
      label: 'Risultati di ricerca',
      elementsForPage: 20,
      showTotal: false,
      currentPage: 1,
      sortBy: '',
      sortDirection: RiscaSortTypes.nessuno,
      maxPages: 10,
    };
  };

/**
 * Interfaccia personalizzata che definisce la struttura dei dati componente gruppo impiegato come configurazione della tabella risca-table.
 */
export interface IDocumentiAllegatiTable {
  idClassificazione: string;
  dbKeyClassificazione: string;
  entrataUscita?: string;
  protocolloRegionale?: string;
  protocolloMittente?: string;
  descrizione?: string;
  visibilita: string;
  dataInserimento?: string;
  rappresentazioneDigitale: boolean;
  docConAllegati: boolean;
  valido: boolean;
}

/**
 * Interfaccia di supporto che definisce le configurazioni per la tabella tramite costruttore.
 */
export interface DocumentiAllegatiTableConfigs {
  documenti?: DocumentoAllegatoVo[];
  docInvalidMsg: string;
}

/**
 * Classe usata per la generazione delle risca-table che contiene i dati per i componenti gruppo dei dati anagrafici.
 */
export class DocumentiAllegatiTable
  extends RiscaTableClass<IDocumentiAllegatiTable>
  implements IRiscaTableClass<IDocumentiAllegatiTable>
{
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Referente gruppo. */
  private STYLE_ENTRATA_USCITA = {};
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Tipo soggetto. */
  private STYLE_PROTOCOLLO_REGIONALE = {};
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Soggetto. */
  private STYLE_PROTOCOLLO_MITTENTE = {};
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Codice fiscale. */
  private STYLE_DESCRIZIONE = {
    'max-width': '250px',
    'text-align': 'center',
  };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Partita IVA. */
  private STYLE_VISIBILITA = {
    'text-align': 'center',
  };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Indirizzo. */
  private STYLE_DATA_INSERIMENTO = {
    'text-align': 'center',
  };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Tipo invio. */
  private STYLE_DOCUMENTO = {
    'text-align': 'center',
  };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Allegati. */
  private STYLE_ALLEGATI = {
    'text-align': 'center',
  };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Allegati (icone). */
  private STYLE_ALLEGATI_BODY = {
    width: '20px',
    height: '24px',
    'vertical-align': 'middle',
  };

  /** Oggetto con le costanti comuni. */
  private C_C = new CommonConsts();
  /** Oggetto con le costanti per la sezione dei documenti allegati. */
  private DA_C = DocumentiAllegatiConsts;
  /** String che contiene il messaggio di default per i documenti non validi. */
  private docInvalidMsg: string;

  /**
   * Costruttore.
   */
  constructor(configs: DocumentiAllegatiTableConfigs) {
    // Richiamo il super
    super();

    // Estraggo le propriatà dalla configurazione
    const { documenti, docInvalidMsg } = configs;

    // Definisco il messaggio d'errore di default
    this.docInvalidMsg = docInvalidMsg;

    // Definisco il searcher e il converter
    this.setTableFunctions();
    // Definisco la struttura dati source
    this.setElements(documenti);
    // Lancio il setup delle configurazioni
    this.setupClasse();
  }

  /**
   * Override inutilizzato
   */
  setTableFunctions() {
    // Definisco le logiche per il converter
    this.converter = (s: DocumentoAllegatoVo) => {
      // Verifico l'oggetto esista
      if (!s) {
        return undefined;
      }

      // Definisco le proprietà che verranno tornate come parsing
      const idClassificazione = s.id_classificazione;
      const dbKeyClassificazione = s.db_key_classificazione;
      const entrataUscita = s.entrata_uscita;
      const protocolloRegionale = s.protocollo_regionale;
      const protocolloMittente = s.protocollo_mittente;
      const descrizione = s.descrizione;
      const visibilita = s.visibilita;
      const dataInserimento = convertServerDateToViewDate(s.data_inserimento);
      const rappresentazioneDigitale = s.rappresentazione_digitale;
      const docConAllegati = s.doc_con_allegati;
      const valido = s.valido;

      // Effettuo il parsing  delle informazioni in input
      return {
        idClassificazione,
        dbKeyClassificazione,
        entrataUscita,
        protocolloRegionale,
        protocolloMittente,
        descrizione,
        visibilita,
        dataInserimento,
        rappresentazioneDigitale,
        docConAllegati,
        valido,
      } as IDocumentiAllegatiTable;
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
    // Setter di classe per il css delle righe
    const trDynamicClass = (
      row: RiscaTableDataConfig<IDocumentiAllegatiTable>
    ) => {
      if (row.original.valido) {
        return '';
      } else {
        return this.TR_DISABLE_CLASS;
      }
    };
    // Creo l'oggetto cssTable con le configurazioni delle righe
    const cssTable: IRiscaTableCss = { trDynamicClass };
    // Creo il setup css
    this.cssConfig = new RiscaTableCss(undefined, cssTable);
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
          label: 'Entrata/Uscita',
          sortable: false,
          cssCell: this.STYLE_ENTRATA_USCITA,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'entrataUscita',
            type: 'string',
          },
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Protocollo regionale',
          sortable: false,
          cssCell: this.STYLE_PROTOCOLLO_REGIONALE,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'protocolloRegionale',
            type: 'string',
          },
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Protocollo mittente',
          sortable: false,
          cssCell: this.STYLE_PROTOCOLLO_MITTENTE,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'protocolloMittente',
            type: 'string',
          },
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Descrizione',
          sortable: false,
          cssCell: this.STYLE_DESCRIZIONE,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'descrizione',
            type: 'string',
          },
          cssCell: this.STYLE_DESCRIZIONE,
          titleCustom: (documento: DocumentoAllegatoVo) => {
            // Verifico se il documento è valido
            if (documento?.valido) {
              // Ritorno la descrizione del documento
              return documento.descrizione;
              // #
            } else {
              // Documento non valido, devo ritornare il messaggio d'errore
              return this.docInvalidMsg;
            }
          },
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Visibilità',
          sortable: false,
          cssCell: this.STYLE_VISIBILITA,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'visibilita',
            type: 'string',
          },
          cssCell: this.STYLE_VISIBILITA,
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Data inserimento',
          sortable: false,
          cssCell: this.STYLE_DATA_INSERIMENTO,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'dataInserimento',
            type: 'string',
          },
          cssCell: this.STYLE_DATA_INSERIMENTO,
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Documento',
          sortable: false,
          cssCell: this.STYLE_DOCUMENTO,
        }),
        body: new RiscaTableSourceMapBody({
          useTabMethod: true,
          tabMethodData: { actions: [this.actionDocumento()] },
          cssCell: this.STYLE_DOCUMENTO,
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Allegati',
          sortable: false,
          cssCell: this.STYLE_ALLEGATI,
        }),
        body: new RiscaTableSourceMapBody({
          useTabMethod: true,
          tabMethodData: { actions: [this.actionAllegati()] },
          cssCell: this.STYLE_ALLEGATI,
        }),
      }
    );

    // Definisco la paginazione
    const pagination: RiscaTablePagination = this.getDefaultPagination();
    // Assegno alla variabile locale la configurazione
    this.dataConfig = { sourceMap, pagination };
  }

  /**
   * Genera l'azione e l'icona del pulsante documento.
   * @returns RiscaTableBodyTabMethodData con le configurazioni del pulsante.
   */
  private actionDocumento(): RiscaTableBodyTabMethodData {
    return {
      action: this.DA_C.ACTION_DOCUMENTO,
      custom: true,
      title: 'Documento',
      class: this.C_C.RISCA_TABLE_ACTION,
      iconEnabled: RiscaIconsCss.download_file,
      iconDisabled: RiscaIconsCssDisabled.download_file,
      disable: (v: RiscaTableDataConfig<DocumentoAllegatoVo>) => {
        // Il default ritorna false
        return !v?.original?.rappresentazione_digitale;
      },
      visible: (v: RiscaTableDataConfig<DocumentoAllegatoVo>) => {
        // Va mostrata solo se è valido
        return v.original.valido;
      },
    };
  }

  /**
   * Genera l'azione e l'icona del pulsante allegati.
   * @returns RiscaTableBodyTabMethodData con le configurazioni del pulsante.
   */
  private actionAllegati(): RiscaTableBodyTabMethodData {
    return {
      action: this.DA_C.ACTION_ALLEGATI,
      custom: true,
      title: 'Allegati',
      class: this.C_C.RISCA_TABLE_ACTION,
      style: this.STYLE_ALLEGATI_BODY,
      iconEnabled: RiscaIconsCss.download_allegato,
      iconDisabled: RiscaIconsCssDisabled.download_allegato,
      disable: (v: RiscaTableDataConfig<DocumentoAllegatoVo>) => {
        // Il default ritorna false
        return !v?.original?.doc_con_allegati;
      },
      visible: (v: RiscaTableDataConfig<DocumentoAllegatoVo>) => {
        // Va mostrata solo se è valido
        return v.original.valido;
      },
    };
  }

  /**
   * Genera la paginazione di default
   * @returns RiscaTablePagination come paginazione di default
   */
  getDefaultPagination(): RiscaTablePagination {
    // Richiamo la funzione exportata per la gestione della paginazione
    return DocumentiAllegatiTablePagination();
  }
}

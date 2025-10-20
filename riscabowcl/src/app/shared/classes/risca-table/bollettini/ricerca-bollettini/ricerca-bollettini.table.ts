import { RRBollettiniPagamenti } from 'src/app/shared/consts/risca/risca-ricerca-bollettini.consts';
import {
  RiscaIconsCss,
  RiscaIconsCssDisabled,
} from 'src/app/shared/enums/icons.enums';
import {
  RiscaSortTypes,
  RiscaTableCss,
  RiscaTableInput,
  RiscaTablePagination,
  RiscaTableSorting,
  RiscaTableSourceMap,
  RiscaTableSourceMapBody,
  RiscaTableSourceMapHeader,
} from 'src/app/shared/utilities';
import { getParametroElaborazioneBollettini } from '../../../../../features/pagamenti/consts/bollettini/bollettini.functions';
import { TipoParametroElaborazione } from '../../../../../features/pagamenti/enums/pagamenti/pagamenti.enums';
import { RiscaTableDataConfig } from '../../../../components/risca/risca-table/utilities/risca-table.classes';
import { CommonConsts } from '../../../../consts/common-consts.consts';
import {
  convertMomentToViewDate,
  convertServerDateToViewDate,
} from '../../../../services/risca/risca-utilities/risca-utilities.functions';
import { IRiscaTableClass, RiscaTableClass } from '../../risca-table';
import {
  ElaborazioneVo,
  IElaborazioneVo,
} from './../../../../../core/commons/vo/elaborazione-vo';
import { CodiciStatiElaborazione } from './../../../../../features/pagamenti/enums/gestione-pagamenti/stato-elaborazione.enums';

interface IElaborazione {
  tipoElabora: string;
  dataProtocollo: string;
  numeroProtocollo: string;
  statoElabora: string;
  dataRichiesta: string;
  fase: string;
}

/**
 * Classe usata per la generazione delle risca-table che contiene i dati degli usi di legge inseriti dall'utente.
 */
export class RiscaRicercaBollettiniTable
  extends RiscaTableClass<IElaborazione>
  implements IRiscaTableClass<IElaborazione>
{
  /** Oggetto contenente i valori costanti comuni all'applicazione. */
  C_C = new CommonConsts()
  /** Oggetto contenente i valori costanti per il componente. */
  RRB_C = RRBollettiniPagamenti;

  /** Costante di comodo per attivare l'ordinamento dei campi. */
  SORT_ACTIVE = true;

  /**
   * Costruttore.
   */
  constructor(pratiche?: IElaborazioneVo[]) {
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
    this.converter = (e: ElaborazioneVo) => {
      // Ritorno l'oggetto convertito
      if (!e) {
        // Nessona configurazione
        return undefined;
      }

      // Recupero la lista dei parametri
      const p = e.parametri;
      // Recupero dati dai parametri: data protocollo
      const DATA_P = TipoParametroElaborazione.DT_PROTOCOLLO;
      const dpValue = getParametroElaborazioneBollettini(p, DATA_P)?.valore;
      const dataProtocollo = convertServerDateToViewDate(dpValue);
      // Recupero dati dai parametri: numero protocollo
      const NUM_P = TipoParametroElaborazione.NUM_PROTOCOLLO;
      const npValue = getParametroElaborazioneBollettini(p, NUM_P)?.valore;

      // Altre conversioni
      const dataRichiesta = convertMomentToViewDate(e.data_richiesta);

      const result: IElaborazione = {
        tipoElabora: e.tipo_elabora?.des_tipo_elabora ?? '',
        dataProtocollo: dataProtocollo ?? '',
        numeroProtocollo: npValue ?? '',
        statoElabora: e.stato_elabora?.des_stato_elabora ?? '',
        dataRichiesta: dataRichiesta ?? '',
        fase: e.registro_elabora?.cod_passo_elabora,
      };

      // Ritorno l'oggetto convertito
      return result;
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
    const tipoElabora: RiscaTableSourceMap = {
      header: new RiscaTableSourceMapHeader({
        label: 'Tipo',
        sortable: this.SORT_ACTIVE,
        cssCell: { width: '280px', 'min-width': '280px' },
      }),
      body: new RiscaTableSourceMapBody({
        useSource: true,
        sourceData: { property: 'tipoElabora', type: 'string' },
      }),
    };
    const dataRichiesta: RiscaTableSourceMap = {
      header: new RiscaTableSourceMapHeader({
        label: 'Data Protocollo',
        sortable: this.SORT_ACTIVE,
        cssCell: { width: '175px' },
      }),
      body: new RiscaTableSourceMapBody({
        useSource: true,
        sourceData: { property: 'dataProtocollo', type: 'date' },
      }),
    };
    const numeroProtocollo: RiscaTableSourceMap = {
      header: new RiscaTableSourceMapHeader({
        label: 'Numero Protocollo',
      }),
      body: new RiscaTableSourceMapBody({
        useSource: true,
        sourceData: { property: 'numeroProtocollo', type: 'string' },
      }),
    };
    const statoElabora: RiscaTableSourceMap = {
      header: new RiscaTableSourceMapHeader({
        label: 'Stato',
        sortable: this.SORT_ACTIVE,
        cssCell: { width: '230px', 'min-width': '230px' },
      }),
      body: new RiscaTableSourceMapBody({
        useSource: true,
        sourceData: { property: 'statoElabora', type: 'string' },
      }),
    };
    const stato: RiscaTableSourceMap = {
      header: new RiscaTableSourceMapHeader({
        label: 'Data Elaborazione',
        sortable: this.SORT_ACTIVE,
        sortType: RiscaSortTypes.decrescente,
        cssCell: { width: '195px', 'min-width': '160px' },
      }),
      body: new RiscaTableSourceMapBody({
        useSource: true,
        sourceData: { property: 'dataRichiesta', type: 'string' },
      }),
    };
    const fase: RiscaTableSourceMap = {
      header: new RiscaTableSourceMapHeader({
        label: 'Fase',
        cssCell: { width: '300px' },
      }),
      body: new RiscaTableSourceMapBody({
        useSource: true,
        sourceData: { property: 'fase', type: 'string' },
      }),
    };

    const azioni: RiscaTableSourceMap = {
      header: new RiscaTableSourceMapHeader({
        label: 'Azioni',
        cssCell: { width: '130px', 'min-width': '130px' },
      }),
      body: new RiscaTableSourceMapBody({
        useTabMethod: true,
        tabMethodData: {
          actions: [
            {
              action: this.RRB_C.RIC_BOLL_CONFERMA,
              custom: true,
              title: 'Conferma',
              class: this.C_C.RISCA_TABLE_ACTION,
              style: { width: '25px' },
              iconEnabled: RiscaIconsCss.confirm_elements,
              iconDisabled: RiscaIconsCssDisabled.confirm_elements,
              disable: (v: RiscaTableDataConfig<ElaborazioneVo>) => {
                // Recupero dall'input il flag selected
                const { original } = v;
                const disabilitato = this.disabledConferma(original);
                // Se è selezionato, disabilito la cancellazione
                return disabilitato;
              },
            },
            {
              action: this.RRB_C.RIC_BOLL_ANNULLA,
              custom: true,
              title: 'Annulla',
              class: this.C_C.RISCA_TABLE_ACTION,
              style: { width: '15px' },
              iconEnabled: RiscaIconsCss.reload,
              iconDisabled: RiscaIconsCssDisabled.reload,
              disable: (v: RiscaTableDataConfig<ElaborazioneVo>) => {
                // Recupero dall'input il flag selected
                const { original } = v;
                const disabilitato = this.disabledAnnulla(original);
                // Se è selezionato, disabilito la cancellazione
                return disabilitato;
              },
            },
            {
              action: this.RRB_C.RIC_BOLL_SCARICA_FILE,
              custom: true,
              title: this.C_C.LABEL_SCARICA_FILE,
              class: this.C_C.RISCA_TABLE_ACTION,
              style: { width: '18px' },
              iconEnabled: RiscaIconsCss.download_file,
              iconDisabled: RiscaIconsCssDisabled.download_file,
              disable: (v: RiscaTableDataConfig<ElaborazioneVo>) => {
                // Recupero dall'input il flag selected
                const { original } = v;
                const disabilitato = this.disabledScaricaFile(original);
                // Se è selezionato, disabilito la cancellazione
                return disabilitato;
              },
            },
          ],
        },
      }),
    };

    // Creo la configurazione RiscaTableSourceMap per le colonne
    const sourceMap: RiscaTableSourceMap[] = [
      tipoElabora,
      dataRichiesta,
      numeroProtocollo,
      statoElabora,
      stato,
      fase,
      azioni,
    ];
    // Definisco le regole di sorting
    const sorting: RiscaTableSorting = { resetSortOnAdd: true };
    // Definisco la paginazione, ma se presente uso quella
    const pagination: RiscaTablePagination = this.getDefaultPagination();

    // Assegno alla variabile locale la configurazione
    this.dataConfig = new RiscaTableInput({ sourceMap, sorting, pagination });
  }

  /**
   * Genera la paginazione di default
   * @returns RiscaTablePagination come paginazione di default
   */
  getDefaultPagination(): RiscaTablePagination {
    return {
      total: this.source?.length || 0,
      label: 'Risultati di ricerca',
      elementsForPage: 10,
      showTotal: true,
      currentPage: 1,
      sortBy: 'dataRichiesta',
      sortDirection: RiscaSortTypes.decrescente,
      maxPages: 10,
    };
  }

  /**
   * #################################
   * FUNZIONI DI ABILITAZIONE PULSANTI
   * #################################
   */

  /**
   * Determina se il pulsante di conferma per una riga é disabilitato
   * @param elaborazione ElaborazioneVo da esaminare
   * @returns True se è disabilitata. False altrimenti.
   */
  private disabledConferma(elaborazione: ElaborazioneVo) {
    // Controllo di esistenza
    if (!elaborazione || !elaborazione.stato_elabora) {
      return true;
    }

    // Il controllo avviene tramite stato elaborazione
    switch (elaborazione.stato_elabora.cod_stato_elabora) {
      case CodiciStatiElaborazione.EMESSA: {
        return false;
      }
      default: {
        return true;
      }
    }
  }

  /**
   * Determina se il pulsante di annulla per una riga é disabilitato
   * @param elaborazione ElaborazioneVo da esaminare
   * @returns True se è disabilitata. False altrimenti.
   */
  private disabledAnnulla(elaborazione: ElaborazioneVo) {
    // Controllo di esistenza
    if (!elaborazione || !elaborazione.stato_elabora) {
      return true;
    }

    // Il controllo avviene tramite stato elaborazione
    switch (elaborazione.stato_elabora.cod_stato_elabora) {
      case CodiciStatiElaborazione.EMESSA: {
        return false;
      }
      default: {
        return true;
      }
    }
  }

  /**
   * Determina se il pulsante di scarica file per una riga é disabilitato
   * @param elaborazione ElaborazioneVo da esaminare
   * @returns True se è disabilitata. False altrimenti.
   */
  private disabledScaricaFile(elaborazione: ElaborazioneVo) {
    // Controllo di esistenza
    if (!elaborazione || !elaborazione.stato_elabora) {
      return true;
    }

    // Il controllo avviene tramite stato elaborazione
    switch (elaborazione.stato_elabora.cod_stato_elabora) {
      case CodiciStatiElaborazione.EMESSA: {
        return false;
      }
      default: {
        return true;
      }
    }
  }
}

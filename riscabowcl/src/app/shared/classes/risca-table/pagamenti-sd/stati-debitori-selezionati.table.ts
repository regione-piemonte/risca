import { ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { StatoDebitorioVo } from '../../../../core/commons/vo/stato-debitorio-vo';
import {
  annualitaRiferimento,
  getRataPadre,
} from '../../../../features/pratiche/service/dati-contabili/dati-contabili/dati-contabili-utility.functions';
import { generaObjErrore } from '../../../miscellaneous/forms-validators/forms-validators';
import { RiscaFormBuilderService } from '../../../services/risca/risca-form-builder/risca-form-builder.service';
import {
  convertMomentToViewDate,
  formatoImportoITA,
} from '../../../services/risca/risca-utilities/risca-utilities.functions';
import {
  IRiscaTableBodyInputField,
  IRiscaTableNumberConfig,
  IRTInputValidatorParams,
  MappaErroriFormECodici,
  RiscaFormBuilderSize,
  RiscaFormInputCss,
  RiscaFormInputNumber,
  RiscaTableCss,
  RiscaTableSourceMap,
  RiscaTableSourceMapBody,
  RiscaTableSourceMapHeader,
  TipoInputTable,
  TRTInputValidator,
} from '../../../utilities';
import { RiscaErrorKeys } from '../../../utilities/classes/errors-keys';
import { RiscaErrorsMap } from '../../../utilities/classes/errors-maps';
import { IRiscaTableClass, RiscaTableClass } from '../risca-table';

/**
 * Interfaccia personalizzata che definisce la struttura dei dati impiegato come configurazione della tabella risca-table.
 */
export interface ISDSelezionatiTable {
  codiceUtenza: string;
  annualita: string;
  canoneDovuto: number;
  importoMancanteConInteressi: number;
  importoVersato: string;
  scadenza: string;
}

/**
 * Interfaccia di supporto che definisce le configurazioni per la tabella tramite costruttore.
 */
export interface SDSelezionatiTableConfigs {
  statiDebitori?: StatoDebitorioVo[];
  importoVersatoPagamento: number;
}

/**
 * Classe usata per la generazione delle risca-table per la visualizzazione delle informazioni.
 */
export class StatiDebitoriSelezionatiTable
  extends RiscaTableClass<ISDSelezionatiTable>
  implements IRiscaTableClass<ISDSelezionatiTable>
{
  /** number con l'importo versato del pagamento, utilizzato per i controlli sull'inserimento. */
  private _importoVersato: number;
  // STILI GRAFICI
  private STYLE_CODICE_UTENZA: any = {};
  private STYLE_ANNUALITA: any = {};
  private STYLE_CANONE_DOVUTO_H: any = { width: '200px' };
  private STYLE_CANONE_DOVUTO_B: any = { 'text-align': 'right' };
  private STYLE_IMPORTO_MANCANTE_CON_INTERESSI_H: any = { width: '300px' };
  private STYLE_IMPORTO_MANCANTE_CON_INTERESSI_B: any = {
    'text-align': 'right',
  };
  private STYLE_IMPORTO_VERSATO: any = {
    'text-align': 'center',
    width: '279px',
  };
  private STYLE_SCADENZA: any = { 'text-align': 'center' };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Azioni. */
  private STYLE_AZIONI = { width: '120px' };

  /**
   * Costruttore.
   */
  constructor(
    configs: SDSelezionatiTableConfigs,
    private _riscaFormBuilder: RiscaFormBuilderService
  ) {
    // Richiamo il super
    super();

    // Estraggo le propriatà dalla configurazione
    const { statiDebitori, importoVersatoPagamento } = configs;
    // Assegno localmente le informazioni
    this._importoVersato = importoVersatoPagamento;

    // Definisco il searcher e il converter
    this.setTableFunctions();
    // Definisco la struttura dati source
    this.setElements(statiDebitori);
    // Lancio il setup delle configurazioni
    this.setupClasse();
  }

  /**
   * Funzione di setup per la classe.
   */
  setTableFunctions() {
    // Definisco le logiche per il converter
    this.converter = (sd: StatoDebitorioVo) => {
      // Verifico l'oggetto esista
      if (!sd) {
        return undefined;
      }

      // Recupero le informazioni per la tabella
      const codiceUtenza = sd.cod_utenza;
      const annualita = annualitaRiferimento(sd.annualita_sd);
      const canoneDovuto = sd.canone_dovuto;
      const importoMancanteConInteressi = sd.importoMancanteConInteressi;
      const importoVersato = 'importo_versato';
      const dataScadPag = getRataPadre(sd.rate)?.data_scadenza_pagamento;
      const scadenza = convertMomentToViewDate(dataScadPag);

      // Creo l'oggetto convertito
      const obj: ISDSelezionatiTable = {
        codiceUtenza,
        annualita,
        canoneDovuto,
        importoMancanteConInteressi,
        importoVersato,
        scadenza,
      };

      // Ritorno la configurazione
      return obj;
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
    // Definisco le configurazioni per gli stili
    const table = { 'min-width': '1225px' };
    // Creo il setup css
    this.cssConfig = new RiscaTableCss(undefined, { table });
  }

  /**
   * Funzione di setup per la configurazione dati.
   */
  setupDataConfig() {
    // Creo la configurazione RiscaTableSourceMap
    const sourceMap: RiscaTableSourceMap[] = [];

    // Definizione degli oggetti per la configurazione della tabella
    // ### CODICE UTENZA
    const codiceUtenza = new RiscaTableSourceMap(
      new RiscaTableSourceMapHeader({
        label: 'Codice utenza',
        sortable: false,
        cssCell: this.STYLE_CODICE_UTENZA,
      }),
      new RiscaTableSourceMapBody({
        useSource: true,
        sourceData: {
          property: 'codiceUtenza',
          type: 'string',
        },
      })
    );

    // ### ANNUALITA
    const annualita = new RiscaTableSourceMap(
      new RiscaTableSourceMapHeader({
        label: 'Anno',
        sortable: false,
        cssCell: this.STYLE_ANNUALITA,
      }),
      new RiscaTableSourceMapBody({
        useSource: true,
        sourceData: {
          property: 'annualita',
          type: 'string',
        },
      })
    );

    // ### CANONE DOVUTO
    const canoneDovuto = new RiscaTableSourceMap(
      new RiscaTableSourceMapHeader({
        label: 'Canone dovuto',
        sortable: false,
        cssCell: this.STYLE_CANONE_DOVUTO_H,
      }),
      new RiscaTableSourceMapBody({
        useSource: true,
        sourceData: {
          property: 'canoneDovuto',
          type: 'number',
          outputFormat: (v: number): string => {
            // Ritorno la formattazione numerica per italia
            return formatoImportoITA(v);
          },
        },
        cssCell: this.STYLE_CANONE_DOVUTO_B,
      })
    );

    // ### IMPORTO MANCANTE ECCEDENTE
    const importoMancanteConInteressi = new RiscaTableSourceMap(
      new RiscaTableSourceMapHeader({
        label: 'Importo mancante con interessi',
        sortable: false,
        cssCell: this.STYLE_IMPORTO_MANCANTE_CON_INTERESSI_H,
      }),
      new RiscaTableSourceMapBody({
        useSource: true,
        sourceData: {
          property: 'importoMancanteConInteressi',
          type: 'number',
          outputFormat: (v: number): string => {
            // Ritorno la formattazione numerica per italia
            return formatoImportoITA(v);
          },
        },
        cssCell: this.STYLE_IMPORTO_MANCANTE_CON_INTERESSI_B,
      })
    );

    // ### IMPORTO VERSATO
    const importoVersato = this.importoVersatoConfig();

    // ### SCADENZA
    const scadenza = new RiscaTableSourceMap(
      new RiscaTableSourceMapHeader({
        label: 'Scadenza',
        sortable: false,
        cssCell: this.STYLE_SCADENZA,
      }),
      new RiscaTableSourceMapBody({
        useSource: true,
        sourceData: {
          property: 'scadenza',
          type: 'string',
        },
      })
    );

    // # AZIONI
    const azioni: RiscaTableSourceMap = {
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
              action: 'delete',
            },
          ],
        },
      }),
    };

    // Inserisco all'interno della sourceMap le configurazioni per le colonne
    sourceMap.push(
      codiceUtenza,
      annualita,
      canoneDovuto,
      importoMancanteConInteressi,
      importoVersato,
      scadenza,
      azioni
    );

    // Assegno alla variabile locale la configurazione
    this.dataConfig = { sourceMap };
  }

  /**
   * Funzione che genera la configurazione per la gestione del campo "Importo versato".
   * Essendo il campo una input con configurazioni complesse, la funzione impacchetterà le logiche per la costruzione dell'oggetto.
   * @returns RiscaTableSourceMap con la configurazione completa del campo.
   */
  private importoVersatoConfig(): RiscaTableSourceMap {
    // Recupero le costanti per la gestione degli errori di una input
    const EM = new RiscaErrorsMap();
    // Definizione delle informazioni statiche della input
    const property = 'importoVersato';
    const originalProperty = 'importo_versato';

    // Genero la configurazione principale per la gestione delle input
    const numberConfigs = this._riscaFormBuilder.genInputNumber({
      // label: originalProperty,
      size: RiscaFormBuilderSize.standard,
      showErrorFC: true,
      useDecimal: true,
      decimals: 2,
      decimaliNonSignificativi: true,
      min: 0,
    });
    // Definizione delle configurazioni specifiche dell'input
    const cssConfig: RiscaFormInputCss = numberConfigs.css as RiscaFormInputCss;
    const dataConfig: RiscaFormInputNumber = numberConfigs.data;
    const disabled: boolean = false;
    const errMapFormGroup: MappaErroriFormECodici[] = [];
    const errMapFormControl: MappaErroriFormECodici[] = [
      ...EM.MAP_GENERIC_ERROR,
      ...EM.MAP_IMP_VERSATO_MAGG_CANONE_DOVUTO,
      ...EM.MAP_IMP_VERSATO_MAGG_IMP_PAGAMENTO,
      ...EM.MAP_FORM_CONTROL_REQUIRED,
    ];
    const customValidators: TRTInputValidator<number>[] = [
      this.validatorMinZero,
      // this.validatorMaxImportoMancanteInteressi, // RISCA-ISSUES-39: rimosso controllo per gli importi
      this.validatorMaxImportoVersato,
    ];
    const angularValidators: ValidatorFn[] = [Validators.required];

    // Definisco la configurazione per la gestione della input
    const numberItFormatConfig: IRiscaTableNumberConfig = {
      cssConfig,
      dataConfig,
      disabled,
      errMapFormControl,
      errMapFormGroup,
      customValidators,
      angularValidators,
    };
    // Definisco l'oggetto finale di configurazione per la input
    const inputData: IRiscaTableBodyInputField = {
      property,
      originalProperty,
      type: TipoInputTable.numberItFormat,
      numberItFormatConfig,
    };

    // Definisco la configurazione del campo
    const importoVersato = new RiscaTableSourceMap(
      new RiscaTableSourceMapHeader({
        label: '*Importo versato',
        sortable: false,
        cssCell: this.STYLE_IMPORTO_VERSATO,
      }),
      new RiscaTableSourceMapBody({
        useInput: true,
        inputData,
      })
    );

    // Ritorno la configurazione
    return importoVersato;
  }

  /**
   * Funzione che genera un validatore per la input dell'importo versato.
   * Il validatore gestirà il valore minimo della input.
   * @returns TRTInputValidator<number> con la funzione di validazione.
   */
  private validatorMinZero: TRTInputValidator<number> = (
    params: IRTInputValidatorParams<number>
  ): ValidationErrors | null => {
    // Estraggo dall'input i parametri
    const { value, row, inputConfig } = params ?? {};
    // Definisco la costante contenente le chiavi d'errore per i forms validators
    const ERR_KEY = RiscaErrorKeys;

    // Verifico che il valore sia definito
    if (value != undefined) {
      // Input definita, verifico se è minore di 0
      // [VF] Issue 88 - si richiede di poter inserire il valore 0 nell'importo versato sugli stati debitori selezionati
      if (value < 0) {
        // Recupero la chiave d'errore
        const key = ERR_KEY.ERRORE_FORMATO_GENERICO;
        // Genero e ritorno l'errore
        return generaObjErrore(key);
      }
    }

    // Nessun errore
    return null;
  };

  /**
   * Funzione che genera un validatore per la input dell'importo versato.
   * Il validatore gestirà il valore massimo della input.
   * @returns TRTInputValidator<number> con la funzione di validazione.
   */
  private validatorMaxImportoMancanteInteressi: TRTInputValidator<number> = (
    params: IRTInputValidatorParams<number>
  ): ValidationErrors | null => {
    // Estraggo dall'input i parametri
    const { value, row, inputConfig } = params ?? {};
    // Definisco la costante contenente le chiavi d'errore per i forms validators
    const ERR_KEY = RiscaErrorKeys;

    // Recupero dall'oggetto della riga l'oggetto originale
    const sd: StatoDebitorioVo = row?.original;
    // Calcolo il totale e lo formatto
    let totImpMancInteressi: number = sd.importoMancanteConInteressi;

    // Verifico che il valore sia definito
    if (value != undefined) {
      // Input definita, verifico se il valore è maggiore del o dell'importo versato
      if (value > totImpMancInteressi) {
        // Recupero la chiave d'errore
        const key = ERR_KEY.IMP_VERSATO_MAGG_IMP_MANCANTE_CON_INTERESSI;
        // Genero e ritorno l'errore
        return generaObjErrore(key);
      }
    }

    // Nessun errore
    return null;
  };

  /**
   * Funzione che genera un validatore per la input dell'importo versato.
   * Il validatore gestirà il valore massimo della input.
   * @returns TRTInputValidator<number> con la funzione di validazione.
   */
  private validatorMaxImportoVersato: TRTInputValidator<number> = (
    params: IRTInputValidatorParams<number>
  ): ValidationErrors | null => {
    // Estraggo dall'input i parametri
    const { value, row, inputConfig } = params ?? {};
    // Definisco la costante contenente le chiavi d'errore per i forms validators
    const ERR_KEY = RiscaErrorKeys;
    // Dall'instanza della classe recupero il valore per l'importo versato
    const importoVersato: number = this._importoVersato;

    // Verifico che il valore sia definito
    if (value != undefined) {
      // Input definita, verifico se il valore è maggiore del canone dovuto o dell'importo versato
      if (value > importoVersato) {
        // Recupero la chiave d'errore
        const key = ERR_KEY.IMPORTO_VERSATO_MAGG_IMPORTO_PAGAMENTO;
        // Genero e ritorno l'errore
        return generaObjErrore(key);
      }
    }

    // Nessun errore
    return null;
  };
}

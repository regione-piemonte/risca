import { SoggettoConsts } from 'src/app/features/soggetti/consts/soggetto/soggetto.consts';
import { RiscaTableDataConfig } from 'src/app/shared/components/risca/risca-table/utilities/risca-table.classes';
import { CommonConsts } from 'src/app/shared/consts/common-consts.consts';
import { indirizzoRecapito } from 'src/app/shared/services/risca/risca-utilities/risca-utilities.functions';
import { RecapitoVo } from '../../../../core/commons/vo/recapito-vo';
import {
  iconTableISDisable,
  iconTableISEnable,
  indirizzoSpedizioneFromRecapito,
} from '../../../../features/pratiche/service/dati-anagrafici/indirizzi-spedizione-utility/indirizzi-spedizione-utility.functions';
import {
  RiscaSortTypes,
  RiscaTableCss,
  RiscaTableSourceMap,
  RiscaTableSourceMapBody,
  RiscaTableSourceMapHeader,
} from '../../../utilities';
import { IRiscaTableClass, RiscaTableClass } from '../risca-table';
import { RiscaTableBodyTabMethodData } from './../../../utilities/interfaces/utilities.interfaces';

/**
 * Interfaccia personalizzata che definisce la struttura dei dati recapito alternativo impiegato come configurazione della tabella risca-table.
 */
export interface ISRecapitoAlternativoTable {
  presso: string;
  indirizzo: string;
  telefono: string;
  email: string;
  pec: string;
  tipoInvio: string;
}

/**
 * Interfaccia di supporto che definisce le configurazioni per la tabella tramite costruttore.
 */
export interface ISoggettoRecapitiAlternativiTableConfigs {
  isGestioneAbilitata: boolean;
  recapiti?: RecapitoVo[];
  AEA_ins_mod_DADisabled?: boolean;
}

/**
 * Classe usata per la generazione delle risca-table che contiene i dati inseriti per i recapiti alternativi.
 */
export class SoggettoRecapitiAlternativiTable
  extends RiscaTableClass<ISRecapitoAlternativoTable>
  implements IRiscaTableClass<ISRecapitoAlternativoTable>
{
  /** Oggetto CommonConsts contenente le costanti di uso comune. */
  C_C = new CommonConsts()
  /** Oggetto con le costanti per la sezione dei dati del soggetto. */
  S_C = SoggettoConsts;
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Presso. */
  private STYLE_PRESSO = { 'max-width': '200px' };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Indirizzo. */
  private STYLE_INDIRIZZO = { 'max-width': '200px' };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Telefono. */
  private STYLE_TELEFONO = { 'max-width': '200px' };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Email. */
  private STYLE_EMAIL = { 'min-width': '125px', 'max-width': '250px' };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: PEC. */
  private STYLE_PEC = { 'min-width': '125px', 'max-width': '250px' };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Tipo invio. */
  private STYLE_TIPO_INVIO = {};
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Azioni. */
  private STYLE_AZIONI = { width: '120px' };

  /** Boolean che definisce la configurazione di disabilitazione degli elementi in app per ambito. */
  private _isGestioneAbilitata: boolean;
  /** Boolean che definisce la configurazione di disabilitazione degli elementi in app per profilo. */
  private _AEA_ins_mod_DADisabled: boolean;

  /**
   * Costruttore.
   */
  constructor(configs: ISoggettoRecapitiAlternativiTableConfigs) {
    // Richiamo il super
    super();

    // Estraggo le propriatà dalla configurazione
    const {
      isGestioneAbilitata,
      recapiti,
      AEA_ins_mod_DADisabled,
    } = configs;
  
    // Assegno le variabili
    this._isGestioneAbilitata = isGestioneAbilitata;
    this._AEA_ins_mod_DADisabled = AEA_ins_mod_DADisabled ?? false;

    // Definisco il searcher e il converter
    this.setTableFunctions();
    // Definisco la struttura dati source
    this.setElements(recapiti);
    // Lancio il setup delle configurazioni
    this.setupClasse();
  }

  /**
   * Funzione di setup per la classe.
   */
  setTableFunctions() {
    // Definisco le logiche per il converter
    this.converter = (recapito: RecapitoVo) => {
      // Verifico l'oggetto esista
      if (!recapito) {
        return undefined;
      }

      // Definisco le proprietà che verranno tornate come parsing
      const presso = recapito.presso;
      const indirizzo = indirizzoRecapito(recapito);
      const telefono = recapito.telefono;
      const email = recapito.email;
      const pec = recapito.pec;
      const tipoInvio = recapito.tipo_invio?.des_tipo_invio;

      // Effettuo il parsing  delle informazioni in input
      return {
        presso,
        indirizzo,
        telefono,
        email,
        pec,
        tipoInvio,
      } as ISRecapitoAlternativoTable;
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
          label: 'Presso',
          sortable: true,
          cssCell: this.STYLE_PRESSO,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: { property: 'presso', type: 'string' },
          cssCell: this.STYLE_PRESSO,
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Indirizzo',
          sortable: true,
          cssCell: this.STYLE_INDIRIZZO,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: { property: 'indirizzo', type: 'string' },
          cssCell: this.STYLE_INDIRIZZO,
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Telefono',
          sortable: true,
          cssCell: this.STYLE_TELEFONO,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: { property: 'telefono', type: 'string' },
          cssCell: this.STYLE_TELEFONO,
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Email',
          sortable: true,
          cssCell: this.STYLE_EMAIL,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: { property: 'email', type: 'string' },
          cssCell: this.STYLE_EMAIL,
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'PEC',
          sortable: true,
          cssCell: this.STYLE_PEC,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: { property: 'pec', type: 'string' },
          cssCell: this.STYLE_PEC,
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Tipo invio',
          sortable: true,
          sortType: RiscaSortTypes.crescente,
          cssCell: this.STYLE_TIPO_INVIO,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: { property: 'tipoInvio', type: 'string' },
          cssCell: this.STYLE_TIPO_INVIO,
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
              this.actionModify(),
              this.actionDelete(),
              this.actionIndirizzoSpedizione(),
            ],
          },
        }),
      }
    );

    // Assegno alla variabile locale la configurazione
    this.dataConfig = { sourceMap };
  }

  /**
   * Crea l'action della tabella per la modifica dell'indirizzo spedizione
   * @returns RiscaTableBodyTabMethodData
   */
  private actionModify(): RiscaTableBodyTabMethodData {
    return {
      action: 'modify',
      disable: (v: any) => {
        // Recupero dalla classe lo stato di configurazione per la profilazione
        const AEA_DADisabled = this._AEA_ins_mod_DADisabled;
        // Ritorno l'abilitazione
        if (AEA_DADisabled) {
          return true;
        }

        // Il default ritorna false
        return false;
      },
    };
  }

  /**
   * Crea l'action della tabella per la cancellazione dell'indirizzo
   * @returns RiscaTableBodyTabMethodData
   */
  private actionDelete(): RiscaTableBodyTabMethodData {
    return {
      action: 'delete',
      disable: (v: any) => {
        // Recupero dal servizio l'abilitazione per sezione/tipo abilitazione
        const abilitaS = this._isGestioneAbilitata;
        // Recupero dalla classe lo stato di configurazione per la profilazione
        const AEA_DADisabled = this._AEA_ins_mod_DADisabled;
        // Ritorno l'abilitazione
        if (AEA_DADisabled || !abilitaS) {
          return true;
        }

        // Il default ritorna false
        return false;
      },
    };
  }

  /**
   * Crea l'action della tabella per l'indirizzo spedizione
   * @returns RiscaTableBodyTabMethodData
   */
  private actionIndirizzoSpedizione(): RiscaTableBodyTabMethodData {
    return {
      action: this.S_C.INDIRIZZO_SPEDIZIONE,
      custom: true,
      title: this.S_C.TITLE_INDIRIZZO_SPEDIZIONE,
      class: this.C_C.RISCA_TABLE_ACTION,
      style: { width: '25px' },
      disable: (v: RiscaTableDataConfig<RecapitoVo>) => {
        // Recupero dall'input il flag selected
        const { original } = v;
        // Controllo se l'icona dell'indirizzo di spedizione è disabilitata
        const case1 = this.checkIconaIndSpedizioneDisabled(original);
        // Ritorno il valore del check per disabilitare
        return case1;
      },
      chooseIconEnabled: (v: RiscaTableDataConfig<RecapitoVo>, ...p) => {
        // Recupero dall'input il flag selected
        const { original } = v;
        // Ritorno l'icona corretta da visualizzare
        return this.getIconIndirizzoSpedizioneEnabled(original);
      },
      chooseIconDisabled: (v: RiscaTableDataConfig<RecapitoVo>, ...p) => {
        // Recupero dall'input il flag selected
        const { original } = v;
        // Ritorno l'icona corretta da visualizzare
        return this.getIconIndirizzoSpedizioneDisabled(original);
      },
    };
  }

  /**
   * Verifica se il pulsante della spedizione è abilitato o meno.
   * Se non esiste l'indirizzo di spedizione, allora l'icona è disabilitata.
   * @param recapito RecapitoVo con i dati del recapito.
   * @returns true se disabilitata.
   */
  checkIconaIndSpedizioneDisabled(recapito?: RecapitoVo): boolean {
    // Richiamo la funzione di check per verificare l'oggetto recapito e il suo indirizzo di spedizione
    const is = indirizzoSpedizioneFromRecapito(recapito);
    // L'azione è disabilitata se non ho l'indirizzo di spedizione
    return !is;
  }

  /**
   * Determina quale pulsante mostrare in base ai dati dell'oggetto passato se l'icona è abilitata
   * @param soggetto ElaborazioneVo da esaminare
   * @returns la classe dell'icona corretta.
   */
  private getIconIndirizzoSpedizioneEnabled(recapito?: RecapitoVo) {
    // Richiamo la funzione di supporto per la definizione dell'icona
    return iconTableISEnable(recapito);
  }

  /**
   * Determina quale pulsante mostrare in base ai dati dell'oggetto passato se l'icona è disabilitata
   * @param recapito ElaborazioneVo da esaminare
   * @returns la classe dell'icona corretta.
   */
  private getIconIndirizzoSpedizioneDisabled(recapito?: RecapitoVo) {
    // Richiamo la funzione di supporto per la definizione dell'icona
    return iconTableISDisable(recapito, undefined, true);
  }
}

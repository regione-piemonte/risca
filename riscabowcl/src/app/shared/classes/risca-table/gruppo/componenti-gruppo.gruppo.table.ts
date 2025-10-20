import { GruppoConsts } from 'src/app/features/soggetti/consts/gruppo/gruppo.consts';
import { RiscaTableDataConfig } from 'src/app/shared/components/risca/risca-table/utilities/risca-table.classes';
import { CommonConsts } from 'src/app/shared/consts/common-consts.consts';
import { SoggettoVo } from '../../../../core/commons/vo/soggetto-vo';
import {
  iconTableISDisable,
  iconTableISEnable,
  indirizzoSpedizioneFromRecapito,
} from '../../../../features/pratiche/service/dati-anagrafici/indirizzi-spedizione-utility/indirizzi-spedizione-utility.functions';
import { IRiscaTableDataConfig } from '../../../components/risca/risca-table/utilities/risca-table.interfaces';
import {
  identificativoSoggetto,
  generateRandomId,
  indirizzoRecapitoSoggetto,
  recapitoPrincipaleSoggetto,
  identificativoSoggettoCompleto,
} from '../../../services/risca/risca-utilities/risca-utilities.functions';
import {
  RiscaTableBodyTabMethodData,
  RiscaTableCss,
  RiscaTableSourceMap,
  RiscaTableSourceMapBody,
  RiscaTableSourceMapHeader,
} from '../../../utilities';
import { IRiscaTableClass, RiscaTableClass } from '../risca-table';
import { RecapitoVo } from './../../../../core/commons/vo/recapito-vo';
import { recapitiAlternativiSoggetto } from '../../../services/risca/risca-utilities/risca-utilities.functions';

/**
 * Interfaccia personalizzata che definisce la struttura dei dati componente gruppo impiegato come configurazione della tabella risca-table.
 */
export interface IGComponenteGruppoTable {
  tipoSoggetto: string;
  soggetto: string;
  codiceFiscale: string;
  partitaIva: string;
  indirizzo: string;
  tipoInvio: string;
  email: string;
  pec: string;
  tipoRecapito: string;
}

/**
 * Interfaccia di supporto che definisce le configurazioni per la tabella tramite costruttore.
 */
export interface IGComponentiGruppoTableConfigs {
  componenti?: SoggettoVo[];
  AEA_GRDisabled?: boolean;
  isGestioneAbilitata?: boolean;
  idCapogruppo?: number;
  idGruppo?: number;
}

/**
 * Classe usata per la generazione delle risca-table che contiene i dati per i componenti gruppo per la pagina della gestione gruppo.
 */
export class GComponentiGruppoTable
  extends RiscaTableClass<IGComponenteGruppoTable>
  implements IRiscaTableClass<IGComponenteGruppoTable>
{
  /** Oggetto contenente i valori costanti comuni all'applicazione. */
  private C_C = new CommonConsts()
  /** Oggetto GruppoConsts contenente le costanti utilizzate dal componente. */
  private G_C = GruppoConsts;

  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Referente gruppo. */
  private STYLE_REFERENTE_GRUPPO = {
    'text-align': 'center',
    'max-width': '105px',
    'min-width': '105px',
  };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Tipo soggetto. */
  private STYLE_TIPO_SOGGETTO = {
    width: '160px',
    'min-width': '160px',
    'max-width': '160px',
  };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Soggetto. */
  private STYLE_SOGGETTO = { 'max-width': '250px' };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Codice fiscale. */
  private STYLE_CODICE_FISCALE = {
    width: '190px',
    'min-width': '190px',
    'max-width': '190px',
  };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Tipo Recapito. */
  private STYLE_TIPO_RECAPITO = {
    width: '125px',
    'min-width': '125px',
    'max-width': '125px',
  };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Partita IVA. */
  private STYLE_PARTITA_IVA = {
    width: '180px',
    'min-width': '180px',
    'max-width': '180px',
  };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Indirizzo. */
  private STYLE_INDIRIZZO = { 'max-width': '200px' };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Tipo invio. */
  private STYLE_TIPO_INVIO = {
    width: '110px',
    'min-width': '110px',
    'max-width': '110px',
  };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: E-mail. */
  private STYLE_E_MAIL = { 'min-width': '105px', 'max-width': '105px' };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: PEC. */
  private STYLE_PEC = { 'min-width': '100px', 'max-width': '100px' };
  /** Oggetto costante, compatibile con NgStyle, per la customizzazione della colonna della tabella: Azioni. */
  private STYLE_AZIONI = { width: '120px', 'min-width': '120px' };

  /** Boolean che definisce la configurazione di disabilitazione degli elementi in app per profilo. */
  private _AEA_GRDisabled: boolean;
  /** Boolean che definisce la configurazione di disabilitazione degli elementi in app per l'ambito. */
  private _isGestioneAbilitata: boolean;
  /** Id del capogruppo. Serve per sapere quale dei membri del gruppo è il capogruppo e mostrare i suoi recapiti */
  idCapogruppo: number;
  /** Id del gruppo.*/
  idGruppo: number;

  /**
   * Costruttore.
   */
  constructor(configs: IGComponentiGruppoTableConfigs) {
    // Richiamo il super
    super();
    // Estraggo le propriatà dalla configurazione
    const {
      componenti,
      AEA_GRDisabled,
      isGestioneAbilitata,
      idCapogruppo,
      idGruppo,
    } = configs;

    // Assegno le variabili
    this._AEA_GRDisabled = AEA_GRDisabled ?? false;
    // Assegno il flag per la gestione tramite ambito dei dati anagrafici
    this._isGestioneAbilitata = isGestioneAbilitata ?? true;
    // Assegno l'id del capogruppo
    this.idCapogruppo = idCapogruppo;
    // Assegno l'id del gruppo
    this.idGruppo = idGruppo;

    // Definisco il searcher e il converter
    this.setTableFunctions();
    // Definisco la struttura dati source
    this.setElements(componenti);
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

      // Recupero il recapito principale
      const rP = recapitoPrincipaleSoggetto(s);

      // Definisco le proprietà che verranno tornate come parsing
      const tipoSoggetto = s.tipo_soggetto?.des_tipo_soggetto || '';
      const soggetto = identificativoSoggetto(s);
      const codiceFiscale = s.cf_soggetto || '';
      const partitaIva = s.partita_iva_soggetto || '';
      const indirizzo = indirizzoRecapitoSoggetto(s, rP);
      const tipoInvio = rP?.tipo_invio?.des_tipo_invio || '';
      const email = rP?.email || '';
      const pec = rP?.pec || '';
      const tipoRecapito = this.G_C.RECAPITO_PRINCIPALE;

      // Definisco l'oggetto da ritornare per il parser
      const componente: IGComponenteGruppoTable = {
        tipoSoggetto,
        soggetto,
        codiceFiscale,
        partitaIva,
        indirizzo,
        tipoInvio,
        email,
        pec,
        tipoRecapito,
      };

      // Effettuo il parsing  delle informazioni in input
      return componente;
    };

    // Definisco le logiche per il subRowsGenerator, per le sotto righe (che identificheranno le informazioni dei recapiti alternativi)
    this.subRowsGenerator = (
      s: SoggettoVo
    ): RiscaTableDataConfig<RecapitoVo>[] => {
      // Verifico l'oggetto esista e se è il capogruppo
      if (!s || s.id_soggetto != this.idCapogruppo) {
        // Ritorno undefined
        return undefined;
      }

      // Recupero i recapiti alternativi del soggetto
      const riAS = recapitiAlternativiSoggetto(s);

      // Mappo le informazioni dei recapiti alternativi verso un oggetto di configurazione della tabella
      const sottoRighe = riAS.map((ra: RecapitoVo) => {
        // Definisco le proprietà dell'oggetto da ritornare
        const c: IRiscaTableDataConfig = {
          id: generateRandomId(3),
          original: ra,
          dataTable: this.convertRAToIGComponentiGruppoTable(s, ra),
        };
        // Ritorno l'oggetto mappato
        return new RiscaTableDataConfig<RecapitoVo>(c);
      });

      // Ritorno l'array di configurazioni per le sottorighe
      return sottoRighe;
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
      titleContainer: 'Componenti gruppo',
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
          label: 'Referente gruppo',
          sortable: false,
          cssCell: this.STYLE_REFERENTE_GRUPPO,
        }),
        body: new RiscaTableSourceMapBody({
          useTabMethod: true,
          tabMethodData: {
            actions: [
              {
                action: 'radio',
                disable: (v: any) => {
                  // Ritorno la configurazione per il profilo
                  return this._AEA_GRDisabled;
                },
              },
            ],
          },
          cssCell: this.STYLE_REFERENTE_GRUPPO,
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Tipo soggetto',
          sortable: true,
          cssCell: this.STYLE_TIPO_SOGGETTO,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'tipoSoggetto',
            type: 'string',
          },
          cssCell: this.STYLE_TIPO_SOGGETTO,
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Soggetto',
          sortable: true,
          cssCell: this.STYLE_SOGGETTO,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'soggetto',
            type: 'string',
          },
          cssCell: this.STYLE_SOGGETTO,
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
          cssCell: this.STYLE_CODICE_FISCALE,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'codiceFiscale',
            type: 'string',
          },
          cssCell: this.STYLE_CODICE_FISCALE,
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Partita IVA',
          sortable: true,
          cssCell: this.STYLE_PARTITA_IVA,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'partitaIva',
            type: 'string',
          },
          cssCell: this.STYLE_PARTITA_IVA,
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Tipo Recapito',
          sortable: true,
          cssCell: this.STYLE_TIPO_RECAPITO,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'tipoRecapito',
            type: 'string',
          },
          cssCell: this.STYLE_TIPO_RECAPITO,
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
          sourceData: {
            property: 'indirizzo',
            type: 'string',
          },
          cssCell: this.STYLE_INDIRIZZO,
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Tipo invio',
          sortable: true,
          cssCell: this.STYLE_TIPO_INVIO,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'tipoInvio',
            type: 'string',
          },
          cssCell: this.STYLE_TIPO_INVIO,
        }),
      },
      {
        header: new RiscaTableSourceMapHeader({
          label: 'E-mail',
          sortable: true,
          cssCell: this.STYLE_E_MAIL,
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
          cssCell: this.STYLE_PEC,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'pec',
            type: 'string',
          },
          cssCell: this.STYLE_PEC,
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
              this.actionDetail(),
              this.actionDelete(),
              this.actionIndirizzoSpedizione(),
            ],
          },
          cssCell: this.STYLE_AZIONI,
        }),
      }
    );

    // Creo la configurazione RiscaTableSourceMap per le sottorighe della tabella
    const sourceMapSub: RiscaTableSourceMap[] = [];

    // Creo l'oggetto per colonna vuota
    const emptyBody = {
      body: new RiscaTableSourceMapBody({}),
    };

    // Inserisco all'interno della sourceMap le configurazioni per le colonne.
    sourceMapSub.push(
      // Devo inserire delle colonne vuote per la spaziatura da sinistra
      emptyBody,
      emptyBody,
      emptyBody,
      emptyBody,
      emptyBody,
      {
        header: new RiscaTableSourceMapHeader({
          label: 'Tipo Recapito',
          sortable: true,
          cssCell: this.STYLE_TIPO_RECAPITO,
        }),
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'tipoRecapito',
            type: 'string',
          },
          cssCell: this.STYLE_TIPO_RECAPITO,
        }),
      },
      {
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'indirizzo',
            type: 'string',
          },
          cssCell: this.STYLE_INDIRIZZO,
        }),
      },
      {
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'tipoInvio',
            type: 'string',
          },
          cssCell: this.STYLE_INDIRIZZO,
        }),
      },
      {
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'email',
            type: 'string',
          },
          cssCell: this.STYLE_E_MAIL,
        }),
      },
      {
        body: new RiscaTableSourceMapBody({
          useSource: true,
          sourceData: {
            property: 'pec',
            type: 'string',
          },
          cssCell: this.STYLE_PEC,
        }),
      },
      {
        body: new RiscaTableSourceMapBody({
          useTabMethod: true,
          tabMethodData: {
            actions: [this.actionIndirizzoSpedizioneSub()],
          },
          cssCell: this.STYLE_AZIONI,
        }),
      }
    );

    // Assegno alla variabile locale la configurazione
    this.dataConfig = { sourceMap, sourceMapSub };
  }

  /**
   * Crea l'action della tabella per il dettaglio dell'indirizzo spedizione
   * @returns RiscaTableBodyTabMethodData
   */
  private actionDetail(): RiscaTableBodyTabMethodData {
    return {
      action: 'detail',
      disable: (v: RiscaTableDataConfig<SoggettoVo>) => {
        // Ritorno la configurazione per il profilo
        return this._AEA_GRDisabled || !this._isGestioneAbilitata;
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
      disable: (v: RiscaTableDataConfig<SoggettoVo>) => {
        // Ritorno la configurazione per il profilo
        const disabled = this._AEA_GRDisabled || !this._isGestioneAbilitata;
        // Verifico la configurazione
        if (disabled) {
          // Per configurazione è disabilitato
          return true;
        }

        // Recupero dall'input il flag selected
        const { selected } = v;
        // Se è selezionato, disabilito la cancellazione
        return selected;
      },
    };
  }

  /**
   * Crea l'action della tabella per l'indirizzo spedizione
   * @returns RiscaTableBodyTabMethodData
   */
  private actionIndirizzoSpedizione(): RiscaTableBodyTabMethodData {
    // Definisco una funzione di comodo che verrà usata per la gestione della disabilitazione
    const isIconDisabled = (v: RiscaTableDataConfig<SoggettoVo>) => {
      // Recupero dall'input il flag selected
      const { original: soggetto } = v;
      // Recupero il recapito principale del soggetto
      const r = recapitoPrincipaleSoggetto(soggetto);
      // controllo se l'icona dell'indirizzo di spedizione è disabilitata
      return this.isActionModificaISDisabled(r);
    };

    return {
      action: this.G_C.INDIRIZZO_SPEDIZIONE_PRI,
      custom: true,
      title: this.G_C.TITLE_INDIRIZZO_SPEDIZIONE,
      class: this.C_C.RISCA_TABLE_ACTION,
      style: { width: '25px' },
      disable: (v: RiscaTableDataConfig<SoggettoVo>) => {
        // Definisco le condizioni di disabilitazione dell'icona
        const case1 = isIconDisabled(v);
        // Richiamo e ritorno la funzione di appoggio
        return case1;
      },
      chooseIconEnabled: (v: RiscaTableDataConfig<SoggettoVo>, ...p) => {
        // Recupero l'id gruppo
        const idG = this.idGruppo;
        // Recupero dall'input il flag selected
        const { original } = v;
        // Recupero il recapito
        const r = recapitoPrincipaleSoggetto(original);
        // Ritorno l'icona corretta da visualizzare
        return this.getIconIndirizzoSpedizioneEnabled(r, idG);
      },
      chooseIconDisabled: (v: RiscaTableDataConfig<SoggettoVo>, ...p) => {
        // Recupero l'id gruppo
        const idG = this.idGruppo;
        // Recupero dall'input il flag selected
        const { original } = v;
        // Recupero il recapito
        const r = recapitoPrincipaleSoggetto(original);
        // Ritorno l'icona corretta da visualizzare
        return this.getIconIndirizzoSpedizioneDisabled(r, idG);
      },
    };
  }

  /**
   * Crea l'action della tabella per l'indirizzo spedizione dei recapiti alternativi
   * @returns RiscaTableBodyTabMethodData
   */
  private actionIndirizzoSpedizioneSub(): RiscaTableBodyTabMethodData {
    // Definisco una funzione di comodo che verrà usata per la gestione della visualizzazione, che per la disabilitazione
    const iconUsage = (v: RiscaTableDataConfig<RecapitoVo>) => {
      // Recupero dall'input il flag selected
      const { original } = v;
      // controllo se l'icona dell'indirizzo di spedizione è disabilitata
      return this.isActionModificaISDisabled(original);
    };

    return {
      action: this.G_C.INDIRIZZO_SPEDIZIONE_ALT,
      custom: true,
      title: this.G_C.TITLE_INDIRIZZO_SPEDIZIONE,
      class: this.C_C.RISCA_TABLE_ACTION,
      style: { width: '25px', 'margin-left': '52px' },
      disable: (v: RiscaTableDataConfig<RecapitoVo>) => {
        // Definisco le condizioni di disabilitazione dell'icona
        const case1 = iconUsage(v);
        // Richiamo e ritorno la funzione di appoggio
        return case1;
      },
      chooseIconEnabled: (v: RiscaTableDataConfig<RecapitoVo>, ...p) => {
        // Recupero l'id gruppo
        const idG = this.idGruppo;
        // Recupero dall'input il flag selected
        const { original } = v;
        // Ritorno l'icona corretta da visualizzare
        return this.getIconIndirizzoSpedizioneEnabled(original, idG);
      },
      chooseIconDisabled: (v: RiscaTableDataConfig<RecapitoVo>, ...p) => {
        // Recupero l'id gruppo
        const idG = this.idGruppo;
        // Recupero dall'input il flag selected
        const { original } = v;
        // Ritorno l'icona corretta da visualizzare
        return this.getIconIndirizzoSpedizioneDisabled(original, idG);
      },
    };
  }

  /**
   * Verifica se il pulsante della spedizione è abilitato o meno.
   * Se non esiste l'indirizzo di spedizione, allora l'icona è disabilitata.
   * @param recapito RecapitoVo con i dati del recapito.
   * @returns true se disabilitata.
   */
  isActionModificaISDisabled(recapito?: RecapitoVo): boolean {
    // Se _idGruppo non è definito, non si abilita l'icona
    if (this.idGruppo == undefined) {
      // Ritorno forzato a true
      return true;
    }

    // Recupero un possibile indirizzo di spedizione
    const is = indirizzoSpedizioneFromRecapito(recapito, this.idGruppo);
    // Verifico e ritorno se esiste l'indirizzo di spedizione
    return !is;
  }

  /**
   * Determina quale pulsante mostrare in base ai dati dell'oggetto passato se l'icona è abilitata.
   * @param recapito RecapitoVo che definisce i dati per la selezione dell'icona.
   * @param idGruppo number che definisce l'id gruppo dell'indirizzo di spedizione da recuperare.
   * @returns la classe dell'icona corretta.
   */
  private getIconIndirizzoSpedizioneEnabled(
    recapito: RecapitoVo,
    idGruppo: number
  ) {
    // Richiamo la funzione di supporto per la definizione dell'icona
    return iconTableISEnable(recapito, idGruppo);
  }

  /**
   * Determina quale pulsante mostrare in base ai dati dell'oggetto passato se l'icona è disabilitata.
   * @param recapito RecapitoVo che definisce i dati per la selezione dell'icona.
   * @param idGruppo number che definisce l'id gruppo dell'indirizzo di spedizione da recuperare.
   * @returns la classe dell'icona corretta.
   */
  private getIconIndirizzoSpedizioneDisabled(
    recapito: RecapitoVo,
    idGruppo: number
  ) {
    // Richiamo la funzione di supporto per la definizione dell'icona
    return iconTableISDisable(recapito, idGruppo, true);
  }

  /**
   * Funzione di comodo per la gestione della conversione dati per le sotto righe.
   * La conversione prevedere la mappatura dati da un oggetto RecapitoVo (recapito alternativo), ad un oggetto IGComponentiGruppoTable.
   * @param s SoggettoVo con le informazioni del soggetto per la conversione.
   * @param ra RecapitoVo con le informazioni da convertire.
   * @returns IGComponentiGruppoTable come oggetto di struttura per la tabella.
   */
  private convertRAToIGComponentiGruppoTable(
    s: SoggettoVo,
    ra: RecapitoVo
  ): IGComponenteGruppoTable {
    // Verifico l'oggetto esista e se è il capogruppo
    if (!ra) {
      // Ritorno undefined
      return undefined;
    }

    // Definisco le proprietà che verranno tornate come parsing
    const indirizzo = indirizzoRecapitoSoggetto(s, ra);
    const tipoInvio = ra.tipo_invio?.des_tipo_invio || '';
    const email = ra.email || '';
    const pec = ra.pec || '';
    const tipoRecapito = this.G_C.RECAPITO_ALTERNATIVO;

    // inserisco l'oggetto con le informazioni del recapito alternativo
    const componente: IGComponenteGruppoTable = {
      tipoSoggetto: undefined,
      soggetto: undefined,
      codiceFiscale: undefined,
      partitaIva: undefined,
      indirizzo,
      tipoInvio,
      email,
      pec,
      tipoRecapito,
    };

    // Ritorno la configurazione dati
    return componente;
  }
}

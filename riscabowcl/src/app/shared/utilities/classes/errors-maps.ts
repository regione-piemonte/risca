import { MappaErroriFormECodici } from '..';
import { IUsoLeggeVo } from '../../../core/commons/vo/uso-legge-vo';
import { RiscaNotifyCodes } from '../enums/risca-notify-codes.enums';
import { RiscaErrorKeys } from './errors-keys';

/**
 * Classe di mapping per gli errori delle form.
 */
export class RiscaErrorsMap {
  /** Definisco localmente una variabile contenente RiscaNotifyCodes. */
  private codes = RiscaNotifyCodes;
  // Definisco la costante contenente le chiavi d'errore per i forms validators
  private ERR_KEY = RiscaErrorKeys;

  /** Definisco le costanti per gli errori di Angular */
  private ANGULAR_REQUIRED = 'required';
  private ANGULAR_PATTERN = 'pattern';
  private ANGULAR_MIN = 'min';
  private ANGULAR_MAX = 'max';
  private ANGULAR_EMAIL = 'email';
  private ANGULAR_MAXLENGTH = 'maxlength';

  /** Mappatura errori: campo obbligatorio sul form. */
  MAP_FORM_GROUP_REQUIRED: MappaErroriFormECodici[] = [
    {
      erroreForm: this.ANGULAR_REQUIRED,
      erroreCodice: this.codes.E001,
    },
  ];
  /** Mappatura errori: campo obbligatorio. */
  MAP_FORM_CONTROL_REQUIRED: MappaErroriFormECodici[] = [
    {
      erroreForm: this.ANGULAR_REQUIRED,
      erroreCodice: this.codes.F008,
    },
  ];
  /** Mappatura errori: codice fiscale. */
  MAP_PATTERN_COD_FISC: MappaErroriFormECodici[] = [
    {
      erroreForm: this.ANGULAR_PATTERN,
      erroreCodice: this.codes.F004,
    },
  ];
  /** Mappatura errori: partita iva. */
  MAP_PARTITA_IVA: MappaErroriFormECodici[] = [
    {
      erroreForm: this.ANGULAR_PATTERN,
      erroreCodice: this.codes.F005,
    },
  ];
  MAP_PARTITA_IVA_SOGGETTO: MappaErroriFormECodici[] = [
    {
      erroreForm: this.ANGULAR_REQUIRED,
      erroreCodice: this.codes.F008,
    },
    {
      erroreForm: this.ERR_KEY.PARTITA_IVA_PB,
      erroreCodice: this.codes.F005,
    },
  ];
  /** Mappatura errori: valore minimo. */
  MAP_MIN: MappaErroriFormECodici[] = [
    {
      erroreForm: this.ANGULAR_MIN,
      erroreCodice: this.codes.E003,
    },
  ];
  /** Mappatura errori: valore massimo. */
  MAP_MAX: MappaErroriFormECodici[] = [
    {
      erroreForm: this.ANGULAR_MAX,
      erroreCodice: this.codes.E003,
    },
  ];
  /** Mappatura errori: solo caratteri numerici. */
  MAP_PATTERN_NUMBER: MappaErroriFormECodici[] = [
    {
      erroreForm: this.ANGULAR_PATTERN,
      erroreCodice: this.codes.F001,
    },
  ];
  /** Mappatura errori: validazione email. */
  MAP_PATTERN_EMAIL: MappaErroriFormECodici[] = [
    {
      erroreForm: this.ANGULAR_EMAIL,
      erroreCodice: this.codes.F007,
    },
  ];
  /** Mappatura errori: validazione pec. */
  MAP_PATTERN_PEC: MappaErroriFormECodici[] = [
    {
      erroreForm: this.ANGULAR_EMAIL,
      erroreCodice: this.codes.F009,
    },
  ];
  /** Mappatura errori: lunghezza del campo errata. */
  MAP_MAX_LENGTH: MappaErroriFormECodici[] = [
    {
      erroreForm: this.ANGULAR_MAXLENGTH,
      erroreCodice: this.codes.E003,
    },
  ];
  /** Mappatura errori: errore generico. */
  MAP_GENERIC_ERROR: MappaErroriFormECodici[] = [
    {
      erroreForm: this.ERR_KEY.ERRORE_FORMATO_GENERICO,
      erroreCodice: this.codes.E003,
    },
  ];
  /** Mappatura errori: campi obbligatori. */
  MAP_FORM_GROUP_ONE_FIELD_REQUIRED: MappaErroriFormECodici[] = [
    {
      erroreForm: this.ERR_KEY.CAMPI_OBBLIGATORI,
      erroreCodice: this.codes.E001,
    },
  ];

  /** Mappatura errori: data inizio successiva/uguale a data fine. */
  MAP_DATA_INIZIO_DATA_FINE_INVALID: MappaErroriFormECodici[] = [
    {
      erroreForm: this.ERR_KEY.DATE_START_END_INVALID,
      erroreCodice: this.codes.F002,
    },
  ];
  /** Mappatura errori: tipo pratica e progressivo utenza. */
  MAP_PRATICA_PROGRESSIVO_UTENZA: MappaErroriFormECodici[] = [
    {
      erroreForm: this.ERR_KEY.PRATICA_ORDINARIA_PROGRESSIVO,
      erroreCodice: this.codes.E019,
    },
    {
      erroreForm: this.ERR_KEY.PRATICA_PREFERENZIALE_PROGRESSIVO,
      erroreCodice: this.codes.E020,
    },
    {
      erroreForm: this.ERR_KEY.PRATICA_ATTINGIMENTO_O_PLUS_PROGRESSIVO,
      erroreCodice: this.codes.E021,
    },
  ];
  /** Mappatura errori: calendario inizio concessione maggiore uguale a quello di fine. */
  MAP_CALENDARIO_CONCESSIONE: MappaErroriFormECodici[] = [
    {
      erroreForm: this.ERR_KEY.DATA_CONCESSIONE,
      erroreCodice: this.codes.E017,
    },
    {
      erroreForm: this.ERR_KEY.DATA_SCADENZA_CONCESSIONE_REQUIRED,
      erroreCodice: this.codes.F008,
      nomeCampo: { label: 'Concessione - Scadenza' },
    },
  ];
  /** Mappatura errori: calendario inizio sospensione maggiore uguale a quello di fine. */
  MAP_CALENDARIO_SOSPENSIONE: MappaErroriFormECodici[] = [
    {
      erroreForm: this.ERR_KEY.DATA_SOSPENSIONE,
      erroreCodice: this.codes.E018,
    },
  ];
  /** Mappatura errori: calendario inizio sospensione maggiore uguale a quello di fine. */
  MAP_CALENDARIO_SOSPENSIONE_REQ: MappaErroriFormECodici[] = [
    {
      erroreForm: this.ERR_KEY.PROCEDIMENTO_SCADENZA_SOSPENSIONE,
      erroreCodice: this.codes.F008,
      nomeCampo: { label: 'Sospensione - Scadenza' },
    },
  ];
  /** Mappatura errori: il numero non è formattato correttamente. */
  MAP_FORMATO_NUMERO_6_INT_4_DEC: MappaErroriFormECodici[] = [
    {
      erroreForm: this.ERR_KEY.NUMBER_COMP_NOT_NUMBER,
      erroreCodice: this.codes.F001,
    },
    {
      erroreForm: this.ERR_KEY.NUMBER_COMP_INVALID,
      erroreCodice: this.codes.E026,
    },
  ];
  /** Mappatura errori: il numero non è formattato correttamente. */
  MAP_FORMATO_NUMERO_7_INT_2_DEC: MappaErroriFormECodici[] = [
    {
      erroreForm: this.ERR_KEY.NUMBER_COMP_NOT_NUMBER,
      erroreCodice: this.codes.F001,
    },
    {
      erroreForm: this.ERR_KEY.NUMBER_COMP_INVALID,
      erroreCodice: this.codes.E102,
    },
  ];
  /** Mappatura errori: il numero non rispetta il minimo o non è un numero. */
  MAP_ALLOW_NULL_NUMBER_MIN: MappaErroriFormECodici[] = [
    {
      erroreForm: this.ERR_KEY.NUMBER_COMP_NOT_NUMBER,
      erroreCodice: this.codes.F001,
    },
    {
      erroreForm: this.ERR_KEY.NUMBER_MIN_INVALID,
      erroreCodice: this.codes.E003,
    },
  ];
  /** Mappatura errori: il numero non rispetta il massimo o non è un numero. */
  MAP_ALLOW_NULL_NUMBER_MAX: MappaErroriFormECodici[] = [
    {
      erroreForm: this.ERR_KEY.NUMBER_COMP_NOT_NUMBER,
      erroreCodice: this.codes.F001,
    },
    {
      erroreForm: this.ERR_KEY.NUMBER_MAX_INVALID,
      erroreCodice: this.codes.E003,
    },
  ];
  /** Mappatura errori: il numero minimo è maggiore del massimo. */
  MAP_MIN_MAX_CHECK: MappaErroriFormECodici[] = [
    {
      erroreForm: this.ERR_KEY.NUMBER_MIN_MAX_INVALID,
      erroreCodice: this.codes.E003,
    },
  ];
  /** Mappatura errori: deve essere definito almeno un elemento. */
  MAP_ALMENO_UN_ELEMENTO: MappaErroriFormECodici[] = [
    {
      erroreForm: this.ERR_KEY.CONTAIN_AT_LEAST,
      erroreCodice: this.codes.E025,
    },
  ];
  /** Mappatura errori: deve essere definito almeno un elemento. */
  MAP_ALMENO_UN_PROVVEDIMENTO: MappaErroriFormECodici[] = [
    {
      erroreForm: this.ERR_KEY.CONTAIN_AT_LEAST,
      erroreCodice: this.codes.E044,
    },
  ];

  /** Mappatura errori: portata d'assegnare numerica e composta. */
  MAP_PORTATA_ASSEGNARE: MappaErroriFormECodici[] = [
    ...this.MAP_PATTERN_NUMBER,
    ...this.MAP_FORMATO_NUMERO_6_INT_4_DEC,
  ];
  /** Mappatura errori: dati tecnici ambiente quantità uso di legge. */
  MAP_DTA_QUANTITA: MappaErroriFormECodici[] = [
    ...this.MAP_ALLOW_NULL_NUMBER_MIN,
    ...this.MAP_PATTERN_NUMBER,
    ...this.MAP_FORMATO_NUMERO_6_INT_4_DEC,
    {
      erroreForm: this.ERR_KEY.QUANTITA_RIS_IDRICHE_PER_UNITA,
      erroreCodice: this.codes.E038,
    },
    {
      erroreForm: this.ERR_KEY.QUANT_FALD_PROF_INVALID,
      erroreCodice: this.codes.E041,
    },
    {
      erroreForm: this.ERR_KEY.QUANT_FALD_PROF_INV_PER_UNITA,
      erroreCodice: this.codes.E027,
    },
  ];
  /** Mappatura errori: dati tecnici ambiente quantità falda profonda uso di legge. */
  MAP_DTA_QUANTITA_FALDA_PROFONDA: MappaErroriFormECodici[] = [
    ...this.MAP_ALLOW_NULL_NUMBER_MIN,
    ...this.MAP_PATTERN_NUMBER,
    ...this.MAP_FORMATO_NUMERO_6_INT_4_DEC,
  ];
  /** Mappatura errori: dati tecnici ambiente quantità falda profonda uso di legge per form. */
  MAP_DTA_FORM_QUANTITA_FALDA_PROFONDA: MappaErroriFormECodici[] = [
    {
      erroreForm: this.ERR_KEY.QUANT_FALD_PROF_AUM_INVALID,
      erroreCodice: this.codes.E100,
    },
    {
      erroreForm: this.ERR_KEY.QUANT_FALD_PROF_INVALID,
      erroreCodice: this.codes.E041,
    },
    {
      erroreForm: this.ERR_KEY.QUANT_FALD_PROF_INV_PER_UNITA,
      erroreCodice: this.codes.E027,
    },
  ];
  /** Mappatura errori: required e codice fiscale. */
  MAP_REQ_PATTERN_COD_FISC: MappaErroriFormECodici[] = [
    ...this.MAP_FORM_CONTROL_REQUIRED,
    ...this.MAP_PATTERN_COD_FISC,
  ];
  /** Mappatura errori: codice fiscale per tipo soggetto. */
  MAP_COD_FISC_TIPO_SOGG: MappaErroriFormECodici[] = [
    {
      erroreForm: this.ERR_KEY.COD_FISC_PF,
      erroreCodice: this.codes.F004,
    },
    {
      erroreForm: this.ERR_KEY.PARTITA_IVA_PB,
      erroreCodice: this.codes.F005,
    },
    {
      erroreForm: this.ERR_KEY.COD_FISC_PG,
      erroreCodice: this.codes.F004,
    },
    {
      erroreForm: this.ERR_KEY.PARTITA_IVA_PG,
      erroreCodice: this.codes.F005,
    },
  ];
  /** Mappatura errori: codice fiscale per tipo soggetto. */
  MAP_STATO_RISCOSSIONE: MappaErroriFormECodici[] = [
    {
      erroreForm: this.ERR_KEY.STATO_RISCOSSIONE_SOSPESA_DATA_SOSPENSIONE,
      erroreCodice: this.codes.E043,
    },
    {
      erroreForm: this.ERR_KEY.STATO_RISCOSSIONE_RINUNCIATA_ISTANZA,
      erroreCodice: this.codes.E043,
    },
    {
      erroreForm: this.ERR_KEY.STATO_RISCOSSIONE_RINUNCIATA_TIPO,
      erroreCodice: this.codes.E043,
    },
    {
      erroreForm: this.ERR_KEY.STATO_RISCOSSIONE_SCADUTA_DATA_CONCESSIONE,
      erroreCodice: this.codes.E043,
    },
    {
      erroreForm: this.ERR_KEY.STATO_RISCOSSIONE_SCADUTA_ISTANZA,
      erroreCodice: this.codes.E043,
    },
    {
      erroreForm: this.ERR_KEY.STATO_RISCOSSIONE_ATTIVA,
      erroreCodice: this.codes.E043,
    },
  ];
  /** Mappatura errori: quantita componenti gruppo. */
  MAP_QUANTITA_COMPONENTI_GRUPPO: MappaErroriFormECodici[] = [
    {
      erroreForm: this.ERR_KEY.QUANTITA_COMPONENTI_GRUPPO,
      erroreCodice: this.codes.E024,
    },
  ];
  /** Mappatura errori: presenza capogruppo. */
  MAP_PRESENZA_CAPOGRUPPO: MappaErroriFormECodici[] = [
    {
      erroreForm: this.ERR_KEY.PRESENZA_CAPOGRUPPO,
      erroreCodice: this.codes.E035,
    },
  ];
  /** Mappatura errori: gestione errori per i componenti gruppo. */
  MAP_CONTROLLI_COMPONENTI_GRUPPO: MappaErroriFormECodici[] = [
    ...this.MAP_QUANTITA_COMPONENTI_GRUPPO,
    ...this.MAP_PRESENZA_CAPOGRUPPO,
  ];
  /** Mappatura errori: data scadenza emas iso per usi di legge. */
  MAP_DTA_DATA_SCADENZA_EMAS_ISO: MappaErroriFormECodici[] = [
    {
      erroreForm: this.ERR_KEY.DATA_SCADENZA_EMAS_ISO_R5,
      erroreCodice: this.codes.E039,
    },
  ];
  /** Mappatura errori: contatti soggetto email. */
  MAP_REQUIRE_EMAIL: MappaErroriFormECodici[] = [
    ...this.MAP_FORM_CONTROL_REQUIRED,
    ...this.MAP_PATTERN_EMAIL,
  ];
  /** Mappatura errori: contatti soggetto pec. */
  MAP_REQUIRE_PEC: MappaErroriFormECodici[] = [
    ...this.MAP_FORM_CONTROL_REQUIRED,
    ...this.MAP_PATTERN_PEC,
  ];
  /** Mappatura errori: bollettazione ordinaria => controllo sull'anno. */
  MAP_BOLLETTAZIONE_ORDINARIA_ANNO: MappaErroriFormECodici[] = [
    {
      erroreForm: this.ERR_KEY.ANNO_PRIMA_ANNO_SCAD_PAG,
      erroreCodice: this.codes.E047,
    },
  ];
  /** Mappatura errori: bollettazione => controllo per data protocollo e data scadenza pagamento. */
  MAP_BOLLETTAZIONE_DATA_PROT_DATA_SCAD_PAG: MappaErroriFormECodici[] = [
    {
      erroreForm: this.ERR_KEY.DATA_PROT_DOPO_DATA_SCAD_PAG,
      erroreCodice: this.codes.E046,
    },
  ];
  /** Mappatura errori: bollettazione ordinaria => controllo incrociato per data scadenza protocollo con anno e data pagamento. */
  MAP_BOLLETTAZIONE_O_DATA_SCAD_PAG: MappaErroriFormECodici[] = [
    ...this.MAP_BOLLETTAZIONE_ORDINARIA_ANNO,
    ...this.MAP_BOLLETTAZIONE_DATA_PROT_DATA_SCAD_PAG,
  ];
  /** Mappatura errori: indirizzo spedizione => se messaggio è valorizzato, allora descrizione è in errore */
  MAP_INDIRIZZO_SPEDIZIONE: MappaErroriFormECodici[] = [
    {
      erroreForm: this.ERR_KEY.DESC_BY_MESS_INVALID,
      erroreCodice: this.codes.E001,
    },
  ];
  /** Mappatura errori: validatori per la lunghezza del cap. */
  MAP_LUNGHEZZA_CAP: MappaErroriFormECodici[] = [
    {
      erroreForm: this.ERR_KEY.LUNGHEZZA_CAMPO_MINORE,
      erroreCodice: this.codes.E066,
    },
    {
      erroreForm: this.ERR_KEY.LUNGHEZZA_CAMPO_MAGGIORE,
      erroreCodice: this.codes.E060,
    },
    {
      erroreForm: this.ANGULAR_MAXLENGTH,
      erroreCodice: this.codes.E061,
    },
  ];
  /** Mappatura errori: validatori per il cap di un oggetto recapito. */
  MAP_CAP_RECAPITO: MappaErroriFormECodici[] = [
    ...this.MAP_LUNGHEZZA_CAP,
    ...this.MAP_FORM_CONTROL_REQUIRED,
  ];

  /** Mappatura errori: comune valido nel form control. */
  MAP_COMUNE_VALIDO_FC: MappaErroriFormECodici[] = [
    ...this.MAP_FORM_CONTROL_REQUIRED,
    {
      erroreForm: this.ERR_KEY.COMUNE_DATA_FINE_VALIDITA,
      erroreCodice: this.codes.F010,
    },
  ];
  /** Mappatura errori: comune valido nel form group. */
  MAP_COMUNE_VALIDO_FG: MappaErroriFormECodici[] = [
    ...this.MAP_FORM_CONTROL_REQUIRED,
    {
      erroreForm: this.ERR_KEY.COMUNE_DATA_FINE_VALIDITA,
      erroreCodice: this.codes.E085,
    },
  ];

  /**
   * ############################################################
   * CONFIGURAZIONI PER LA GESTIONE DEL FORM INDIRIZZO SPEDIZIONE
   * ############################################################
   */
  MAP_IS_ML_DESTINATARIO: MappaErroriFormECodici[] = [
    {
      erroreForm: this.ANGULAR_MAXLENGTH,
      erroreCodice: this.codes.E056,
    },
  ];
  MAP_IS_ML_PRESSO: MappaErroriFormECodici[] = [
    {
      erroreForm: this.ANGULAR_MAXLENGTH,
      erroreCodice: this.codes.E057,
    },
  ];
  MAP_IS_ML_FRAZIONE: MappaErroriFormECodici[] = [
    {
      erroreForm: this.ANGULAR_MAXLENGTH,
      erroreCodice: this.codes.E058,
    },
  ];
  MAP_IS_ML_INDIRIZZO: MappaErroriFormECodici[] = [
    {
      erroreForm: this.ANGULAR_MAXLENGTH,
      erroreCodice: this.codes.E059,
    },
  ];
  MAP_IS_LENGTH_CAP: MappaErroriFormECodici[] = [...this.MAP_LUNGHEZZA_CAP];
  MAP_IS_ML_COMUNE_CITTA: MappaErroriFormECodici[] = [
    {
      erroreForm: this.ANGULAR_MAXLENGTH,
      erroreCodice: this.codes.E062,
    },
  ];
  MAP_IS_LENGTH_PROVINCIA_ITA: MappaErroriFormECodici[] = [
    {
      erroreForm: this.ERR_KEY.LUNGHEZZA_CAMPO_MINORE,
      erroreCodice: this.codes.E067,
    },
    {
      erroreForm: this.ERR_KEY.LUNGHEZZA_CAMPO_MAGGIORE,
      erroreCodice: this.codes.E063,
    },
  ];
  MAP_IS_ML_NAZIONE: MappaErroriFormECodici[] = [
    {
      erroreForm: this.ANGULAR_MAXLENGTH,
      erroreCodice: this.codes.E064,
    },
  ];
  MAP_IS_DESTINATARIO: MappaErroriFormECodici[] = [
    ...this.MAP_FORM_GROUP_REQUIRED,
    ...this.MAP_IS_ML_DESTINATARIO,
  ];
  MAP_IS_PRESSO: MappaErroriFormECodici[] = [...this.MAP_IS_ML_PRESSO];
  MAP_IS_FRAZIONE: MappaErroriFormECodici[] = [...this.MAP_IS_ML_FRAZIONE];
  MAP_IS_INDIRIZZO: MappaErroriFormECodici[] = [
    ...this.MAP_FORM_GROUP_REQUIRED,
    ...this.MAP_IS_ML_INDIRIZZO,
  ];
  MAP_IS_CAP: MappaErroriFormECodici[] = [
    ...this.MAP_FORM_GROUP_REQUIRED,
    ...this.MAP_IS_LENGTH_CAP,
  ];
  MAP_IS_COMUNE_CITTA: MappaErroriFormECodici[] = [
    ...this.MAP_FORM_GROUP_REQUIRED,
    ...this.MAP_IS_ML_COMUNE_CITTA,
  ];
  MAP_IS_PROVINCIA: MappaErroriFormECodici[] = [
    ...this.MAP_FORM_GROUP_REQUIRED,
  ];
  MAP_IS_NAZIONE: MappaErroriFormECodici[] = [
    ...this.MAP_FORM_GROUP_REQUIRED,
    ...this.MAP_IS_ML_NAZIONE,
  ];

  /**
   * #########################################################################
   * CONFIGURAZIONI PER LA GESTIONE DEL FORM GENERALI AMMINISTRATIVI DILAZIONE
   * #########################################################################
   */
  MAP_STATO_DEBITORIO_E_ANNUALITA: MappaErroriFormECodici[] = [
    {
      erroreForm: this.ERR_KEY.STATO_DEBITORIO_E_ANNUALITA,
      erroreCodice: this.codes.E048,
    },
  ];
  MAP_ANNUALITA_E_USI: MappaErroriFormECodici[] = [
    {
      erroreForm: this.ERR_KEY.ANNUALITA_E_USI,
      erroreCodice: this.codes.E049,
    },
  ];
  MAP_USI_VALIDI_SD: MappaErroriFormECodici[] = [
    {
      erroreForm: this.ERR_KEY.USI_INVALIDI_CANONE,
      erroreCodice: this.codes.E098,
      nomeCampo: {
        erroreDinamico: (
          e: { [key: string]: IUsoLeggeVo },
          msg: string
        ): string => {
          // Definisco la label per il messaggio
          let descUsiErr: string[] = [];
          // Ciclo le informazioni dell'oggetto
          for (let usoLegge of Object.values(e)) {
            // Recupero la descrizione dall'uso di legge e lo aggiungo all'array
            descUsiErr.push(`[${usoLegge.des_tipo_uso}]`);
          }
          // Ritorno la stringa
          return `${descUsiErr.join(' ')} ${msg}`;
        },
      },
    },
  ];
  MAP_PERIODO_PAGAMENTO: MappaErroriFormECodici[] = [
    {
      erroreForm: this.ERR_KEY.PERIODO_PAGAMENTO,
      erroreCodice: this.codes.E053,
    },
  ];
  MAP_DATA_SCADENZA_PAGAMENTO: MappaErroriFormECodici[] = [
    {
      erroreForm: this.ERR_KEY.DATA_SCADENZA_PAGAMENTO,
      erroreCodice: this.codes.E054,
    },
  ];
  FLAG_ANNULLATO_E_MOTIVAZIONE: MappaErroriFormECodici[] = [
    {
      erroreForm: this.ERR_KEY.FLAG_ANNULLATO_E_MOTIVAZIONE,
      erroreCodice: this.codes.E050,
    },
  ];
  MAP_VERIFICA_ANNUALITA_DOPPIA: MappaErroriFormECodici[] = [
    {
      erroreForm: this.ERR_KEY.VERIFICA_ANNUALITA_DOPPIA,
      erroreCodice: this.codes.E069,
    },
  ];
  MAP_REGOLE_PER_MODIFICA: MappaErroriFormECodici[] = [
    {
      erroreForm: this.ERR_KEY.REGOLE_PER_MODIFICA,
      erroreCodice: this.codes.E051,
    },
  ];
  MAP_RIMBORSI_DA_COMPENSARE_O_COMPENSATI: MappaErroriFormECodici[] = [
    {
      erroreForm: this.ERR_KEY.RIMBORSI_DA_COMPENSARE_O_COMPENSATI,
      erroreCodice: this.codes.A018,
    },
  ];
  MAP_IMPORTO_COMPENSAZIONE_CANONE: MappaErroriFormECodici[] = [
    {
      erroreForm: this.ERR_KEY.IMPORTO_COMPENSAZIONE_CANONE,
      erroreCodice: this.codes.E051,
    },
  ];
  MAP_DATA_INIZIO_REQUIRED: MappaErroriFormECodici[] = [
    {
      erroreForm: this.ERR_KEY.DATA_INIZIO_REQUIRED,
      erroreCodice: this.codes.E001,
    },
  ];
  MAP_STATO_DEBITORIO: MappaErroriFormECodici[] = [
    ...this.MAP_FORM_GROUP_REQUIRED,
    ...this.MAP_STATO_DEBITORIO_E_ANNUALITA,
    ...this.MAP_ANNUALITA_E_USI,
    ...this.MAP_USI_VALIDI_SD,
    ...this.MAP_PERIODO_PAGAMENTO,
    ...this.MAP_DATA_SCADENZA_PAGAMENTO,
    ...this.FLAG_ANNULLATO_E_MOTIVAZIONE,
    ...this.MAP_VERIFICA_ANNUALITA_DOPPIA,
    ...this.MAP_REGOLE_PER_MODIFICA,
    ...this.MAP_RIMBORSI_DA_COMPENSARE_O_COMPENSATI,
    ...this.MAP_IMPORTO_COMPENSAZIONE_CANONE,
    ...this.MAP_DATA_INIZIO_REQUIRED,
  ];
  MAP_IMPORTO_DA_ASSEGNARE: MappaErroriFormECodici[] = [
    {
      erroreForm: this.ERR_KEY.IMPORTO_DA_ASSEGNARE_SUPERIORE_A_IMPORTO_VERSATO,
      erroreCodice: this.codes.E082,
    },
  ];

  /** Mappatura errori: data inizio successiva/uguale a data fine ricerca avanzata. */
  MAP_DATA_INI_FIN_SCADENZA_CONCESSIONE: MappaErroriFormECodici[] = [
    {
      erroreForm: this.ERR_KEY.DATA_INI_FIN_SCADENZA_CONCESSIONE,
      erroreCodice: this.codes.F002,
    },
  ];
  MAP_DATA_INI_FIN_DATA_RINUNCIA_REVOCA: MappaErroriFormECodici[] = [
    {
      erroreForm: this.ERR_KEY.DATA_INI_FIN_DATA_RINUNCIA_REVOCA,
      erroreCodice: this.codes.F002,
    },
  ];
  MAP_DATA_INI_FIN_DATA_ISTANZA: MappaErroriFormECodici[] = [
    {
      erroreForm: this.ERR_KEY.DATA_INI_FIN_DATA_ISTANZA,
      erroreCodice: this.codes.F002,
    },
  ];
  MAP_DATA_INI_FIN_DATA_TITOLO: MappaErroriFormECodici[] = [
    {
      erroreForm: this.ERR_KEY.DATA_INI_FIN_DATA_TITOLO,
      erroreCodice: this.codes.F002,
    },
  ];

  /** Mappatura error per canone uso e canone unitario uso. */
  MAP_FORM_GROUP_CANONE_CANONE_USO: MappaErroriFormECodici[] = [
    {
      erroreForm: this.ERR_KEY.CANONE_E_CANONE_UNITARIO_REQUIRED,
      erroreCodice: this.codes.E098,
    },
  ];

  /** Mappatura error per importo versato e canone dovuto. */
  MAP_IMP_VERSATO_MAGG_CANONE_DOVUTO: MappaErroriFormECodici[] = [
    {
      erroreForm: this.ERR_KEY.IMP_VERSATO_MAGG_IMP_MANCANTE_CON_INTERESSI,
      erroreCodice: this.codes.E082,
    },
  ];
  /** Mappatura error per canone uso e canone unitario uso. */
  MAP_IMP_VERSATO_MAGG_IMP_PAGAMENTO: MappaErroriFormECodici[] = [
    {
      erroreForm: this.ERR_KEY.IMPORTO_VERSATO_MAGG_IMPORTO_PAGAMENTO,
      erroreCodice: this.codes.E096,
    },
  ];
  /** Mappatura errori: campo obbligatorio. */
  MAP_SOGGETTO_PAGAMENTO: MappaErroriFormECodici[] = [
    {
      erroreForm: this.ANGULAR_REQUIRED,
      erroreCodice: this.codes.E097,
    },
    ...this.MAP_MAX_LENGTH,
  ];

  /** Mappatura errori: data inizio fine calendario per esporta dati invalidi. */
  MAP_VALIDAZIONE_DATE_START_ESPORTA_DATI: MappaErroriFormECodici[] = [
    {
      erroreForm: this.ERR_KEY.DATE_START_AFTER_END_EXPORT_DATA,
      erroreCodice: this.codes.E094,
    },
  ];
  /** Mappatura errori: data inizio fine calendario per esporta dati invalidi. */
  MAP_VALIDAZIONE_DATE_END_ESPORTA_DATI: MappaErroriFormECodici[] = [
    {
      erroreForm: this.ERR_KEY.DATE_END_EXPORT_DATA_SAME_YEAR,
      erroreCodice: this.codes.E095,
    },
  ];

  /** Mappatura errori: valore non proprio maggiore di 0. */
  MAP_IMPORTO_NON_PROPRIO_0: MappaErroriFormECodici[] = [
    {
      erroreForm: this.ERR_KEY.IMPORTO_NON_PROPRIO_ZERO,
      erroreCodice: this.codes.E003,
    },
  ];

  /** Mappatura errori: ricerca pagamenti con almeno un campo valorizzato. */
  MAP_RICERCA_PAGAMENTI_FILTRI_VALORIZZATI: MappaErroriFormECodici[] = [
    {
      erroreForm: this.ERR_KEY.ALMENO_UN_CAMPO_RICERCA_PAGAMENTI_VALORIZZATO,
      erroreCodice: this.codes.I029,
    },
  ];

  /** Mappatura errori: controlli su range importo ricerca. */
  MAP_RIC_PAG_RANGE_IMPORTI: MappaErroriFormECodici[] = [
    ...this.MAP_RICERCA_PAGAMENTI_FILTRI_VALORIZZATI,
    ...this.MAP_MIN_MAX_CHECK,
  ];

  /** Mappatura errori: controlli su range importo ricerca. */
  MAP_RIC_PAG_RANGE_DATA_OP: MappaErroriFormECodici[] = [
    ...this.MAP_RICERCA_PAGAMENTI_FILTRI_VALORIZZATI,
    ...this.MAP_DATA_INIZIO_DATA_FINE_INVALID,
  ];

  /** Mappatura errori: controlli annualità per nuova regola uso. */
  MAP_ANNUALITA_NUOVA_REGOLA: MappaErroriFormECodici[] = [
    ...this.MAP_FORM_CONTROL_REQUIRED,
    {
      erroreForm: this.ERR_KEY.NUMBER_NOT_IN_RANGE,
      erroreCodice: this.codes.E106,
    },
  ];

  /** Mappatura errori: controlli percentuale per nuova regola uso. */
  MAP_PERCENTUALE_NUOVA_REGOLA: MappaErroriFormECodici[] = [
    {
      erroreForm: this.ERR_KEY.NUMBER_COMP_INVALID,
      erroreCodice: this.codes.E107,
    },
  ];

  /** Mappatura errori. */
  MAP_REGOLA_CANONE_MINIMO_UNITARIO_PERCENTUALE: MappaErroriFormECodici[] = [
    ...this.MAP_FORM_CONTROL_REQUIRED,
    {
      erroreForm: this.ERR_KEY.NUMBER_COMP_INVALID,
      erroreCodice: this.codes.E109,
    },
  ];

  /** Mappatura errori. */
  MAP_REGOLA_SOGLIA: MappaErroriFormECodici[] = [
    ...this.MAP_FORM_CONTROL_REQUIRED,
    {
      erroreForm: this.ERR_KEY.NUMBER_COMP_INVALID,
      erroreCodice: this.codes.E108,
    },
  ];

  /** Mappatura errori. */
  MAP_REGOLA_SOGLIA_MINIMO_INFERIORE: MappaErroriFormECodici[] = [
    ...this.MAP_FORM_CONTROL_REQUIRED,
    {
      erroreForm: this.ERR_KEY.NUMBER_COMP_INVALID,
      erroreCodice: this.codes.E109,
    },
  ];

  /** Mappatura errori. */
  MAP_REGOLA_SOGLIA_MINIMO_SUPERIORE: MappaErroriFormECodici[] = [
    ...this.MAP_FORM_CONTROL_REQUIRED,
    {
      erroreForm: this.ERR_KEY.NUMBER_COMP_INVALID,
      erroreCodice: this.codes.E109,
    },
  ];

  /** Mappatura errori. */
  MAP_REGOLA_RANGE_MINIMO_PRINCIPALE: MappaErroriFormECodici[] = [
    {
      erroreForm: this.ERR_KEY.NUMBER_COMP_INVALID,
      erroreCodice: this.codes.E109,
    },
  ];

  /** Mappatura errori. */
  MAP_REGOLA_RANGE_VALORI: MappaErroriFormECodici[] = [
    ...this.MAP_GENERIC_ERROR,
    {
      erroreForm: this.ERR_KEY.NUMBER_COMP_INVALID,
      erroreCodice: this.codes.E108,
    },
  ];

  /** Mappatura errori. */
  MAP_RANGE_VALORE1_VALORE2: MappaErroriFormECodici[] = [
    {
      erroreForm: this.ERR_KEY.CAMPI_OBBLIGATORI,
      erroreCodice: this.codes.E001,
    },
    {
      erroreForm: this.ERR_KEY.RANGE_VALORE1_GREAT_VALORE2,
      erroreCodice: this.codes.E114,
    },
  ];

  /** Mappatura errori. */
  MAP_PERCENTUALE_TASSI_DI_INTERESSE: MappaErroriFormECodici[] = [
    ...this.MAP_FORM_CONTROL_REQUIRED,
    {
      erroreForm: this.ERR_KEY.NUMBER_COMP_NOT_NUMBER,
      erroreCodice: this.codes.E003
    },
    {
      erroreForm: this.ERR_KEY.NUMBER_COMP_INVALID,
      erroreCodice: this.codes.E003
    }
  ];
  MAP_GIORNI_INTERESSI_LEGALI: MappaErroriFormECodici[] = [
    ...this.MAP_FORM_CONTROL_REQUIRED,
    {
      erroreForm: this.ANGULAR_MIN,
      erroreCodice: this.codes.E111
    }
  ];

  MAP_GIORNI_INTERESSI_DI_MORA: MappaErroriFormECodici[] = [
    ...this.MAP_FORM_CONTROL_REQUIRED,
    {
      erroreForm: this.ANGULAR_MIN,
      erroreCodice: this.codes.E111
    }
  ];


  constructor() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }
}

import { DynamicObjString } from 'src/app/shared/utilities';
import { CercaTitolareModalChiusure } from '../../enums/cerca-titolare-modal/cerca-titolare-modal.enums';
import { RiscaContattiConsts } from './../../../../shared/consts/risca/risca-contatti.consts';
import { RiscaDatiSoggettoConsts } from './../../../../shared/consts/risca/risca-dati-soggetto.consts';
import { RiscaRecapitoConsts } from './../../../../shared/consts/risca/risca-recapito.consts';

/**
 * Classe contenente una serie di costanti per il componente dati-anagrafici.
 */
export class DatiAnagraficiConsts {
  /** Importo le costanti dei componenti per la gestione dei messaggi di aggiornamento alla fonte */
  RC_C = RiscaContattiConsts;
  RDS_C = RiscaDatiSoggettoConsts;
  RR_C = RiscaRecapitoConsts;

  /** Costanti che definiscono i nomi dei campi del main form del componente. */
  SOGGETTO_PRATICA = 'soggettoPratica';
  GRUPPO_PRATICA = 'gruppoPratica';
  RECAPITI_PRATICA = 'recapitiPratica';

  /** Costante string che definisce il tipo di chiusura della modale di ricerca titolare, quando è stato scelto un titolare da inserire nella pratica. */
  CLOSE_TITOLARE_SCELTO = CercaTitolareModalChiusure.titolareScelto;
  /** Costante string che definisce il tipo di chiusura della modale di ricerca titolare, quando è stata richiesta un'aggiunta di un nuovo gruppo. */
  CLOSE_AGGIUNGI_GRUPPO = CercaTitolareModalChiusure.aggiungiGruppo;
  /** Costante string che definisce il tipo di chiusura della modale di ricerca titolare, quando è stata richiesta un'aggiunta di un nuovo soggetto. */
  CLOSE_AGGIUNGI_SOGGETTO = CercaTitolareModalChiusure.aggiungiSoggetto;

  /** Costante string che definisce la label per l'accordion = nuovo recaptio alternativo. */
  ACC_NUO_REC_ALT = 'Nuovo recapito alternativo';
  /** Definisce il nome della action per l'indirizzo di spedizione da usare nel bottone della tabella dei recapiti */
  INDIRIZZO_SPEDIZIONE = 'indirizzospedizione';

  /**
   * Testo nel bottone di ricerca del titolare.
   * Se modalita === inserimento ALLORA nome pulsante = CERCA TITOLARE
   * Se modalita === modifica ALLORA nome pulsante = CAMBIA TITOLARE
   */
  TESTO_BOTTONE_CERCA_TITOLARE = 'CERCA TITOLARE';
  TESTO_BOTTONE_CAMBIA_TITOLARE = 'CAMBIA TITOLARE';
  TESTO_TESTATA_SENZA_GRUPPO = 'Dati Anagrafici del Soggetto Titolare';
  TESTO_TESTATA_CON_GRUPPO_PLACEHOLDER = '{des_gruppo_soggetto}';
  TESTO_TESTATA_CON_GRUPPO = `Nome Raggruppamento/Consorzio di Soggetti: <strong>${this.TESTO_TESTATA_CON_GRUPPO_PLACEHOLDER}</strong>`;
  TESTO_TESTATA_CON_GRUPPO_DETTAGLIO = `Dati anagrafici del referente del gruppo`;

  /** Chiavi per la gestione dei form principali. */
  FORM_KEY_PARENT_DA = 'FORM_KEY_PARENT_DA';
  FORM_KEY_CHILD_DA_DATI_SOGGETTO = 'FORM_KEY_CHILD_DA_DATI_SOGGETTO';
  FORM_KEY_CHILD_DA_RECAPITO = 'FORM_KEY_CHILD_DA_RECAPITO';
  FORM_KEY_CHILD_DA_CONTATTI = 'FORM_KEY_CHILD_DA_CONTATTI';

  /** Chiavi per la gestione dei form per i recapiti alternativi. */
  FORM_KEY_PARENT_DA_RECAPITI_ALTERNATIVI =
    'FORM_KEY_PARENT_DA_RECAPITI_ALTERNATIVI';
  FORM_KEY_CHILD_DA_RECAPITO_ALTERNATIVO =
    'FORM_KEY_CHILD_DA_RECAPITO_ALTERNATIVO';
  FORM_KEY_CHILD_DA_CONTATTI_ALTERNATIVO =
    'FORM_KEY_CHILD_DA_CONTATTI_ALTERNATIVO';

  /**
   * Contenuto dell'alert di info sui campi modificati del soggetto
   */
  TITOLO_INFO = '<strong>Aggiornamento dati</strong>';
  SOTTOTITOLO_INFO =
    'I dati seguenti sono stati aggiornati dalla Fonte (Scriva):';

  MAP_CAMPI_FONTE: DynamicObjString = {};

  TIPO_SOGGETTO = 'tipo_soggetto';
  CF_SOGGETTO = 'cf_soggetto';
  TIPO_NATURA_GIURIDICA = 'tipo_natura_giuridica';
  RAGIONE_SOCIALE = 'den_soggetto';
  DEN_SOGGETTO = 'den_soggetto';
  PARTITA_IVA_SOGGETTO = 'partita_iva_soggetto';
  COGNOME = 'cognome';
  NOME = 'nome';
  DATA_NASCITA_SOGGETTO = 'data_nascita_soggetto';
  NAZIONE_NASCITA = 'nazione_nascita';
  COMUNE_NASCITA = 'comune_nascita';
  CITTA_ESTERA_NASCITA = 'citta_estera_nascita';

  TIPO_SEDE = 'tipo_sede';
  PRESSO = 'presso';
  NAZIONE_RECAPITO = 'nazione_recapito';
  COMUNE_RECAPITO = 'comune_recapito';
  CITTA_ESTERA_RECAPITO = 'citta_estera_recapito';
  LOCALITA = 'des_localita';
  INDIRIZZO = 'indirizzo';
  NUMERO_CIVICO = 'num_civico';
  CAP = 'cap_recapito';

  TIPO_INVIO = 'tipo_invio';
  PEC = 'pec';
  EMAIL = 'email';
  TELEFONO = 'telefono';
  CELLULARE = 'cellulare';

  constructor() {
    this.componiMapCampiFonte();
  }

  /**
   * Funzione che genera la mappa per la gestione dei campi aggiornati dalla fonte.
   */
  private componiMapCampiFonte() {
    this.MAP_CAMPI_FONTE[this.TIPO_SOGGETTO] = this.RDS_C.LABEL_TIPO_SOGGETTO;
    this.MAP_CAMPI_FONTE[this.CF_SOGGETTO] = this.RDS_C.LABEL_CODICE_FISCALE;
    this.MAP_CAMPI_FONTE[this.TIPO_NATURA_GIURIDICA] =
      this.RDS_C.LABEL_NATURA_GIURIDICA;
    this.MAP_CAMPI_FONTE[this.RAGIONE_SOCIALE] =
      this.RDS_C.LABEL_RAGIONE_SOCIALE;
    this.MAP_CAMPI_FONTE[this.DEN_SOGGETTO] = this.RDS_C.LABEL_RAGIONE_SOCIALE;
    this.MAP_CAMPI_FONTE[this.PARTITA_IVA_SOGGETTO] =
      this.RDS_C.LABEL_PARTITA_IVA;
    this.MAP_CAMPI_FONTE[this.COGNOME] = this.RDS_C.LABEL_COGNOME;
    this.MAP_CAMPI_FONTE[this.NOME] = this.RDS_C.LABEL_NOME;
    this.MAP_CAMPI_FONTE[this.DATA_NASCITA_SOGGETTO] =
      this.RDS_C.LABEL_DATA_NASCITA;
    this.MAP_CAMPI_FONTE[this.NAZIONE_NASCITA] = this.RDS_C.LABEL_STATO_NASCITA;
    this.MAP_CAMPI_FONTE[this.COMUNE_NASCITA] = this.RDS_C.LABEL_COMUNE_NASCITA;
    this.MAP_CAMPI_FONTE[this.CITTA_ESTERA_NASCITA] =
      this.RDS_C.LABEL_CITTA_ESTERA_NASCITA;
    this.MAP_CAMPI_FONTE[this.TIPO_SEDE] = this.RR_C.LABEL_TIPO_SEDE;
    this.MAP_CAMPI_FONTE[this.PRESSO] = this.RR_C.LABEL_PRESSO;
    this.MAP_CAMPI_FONTE[this.NAZIONE_RECAPITO] = this.RR_C.LABEL_STATO;
    this.MAP_CAMPI_FONTE[this.COMUNE_RECAPITO] = this.RR_C.LABEL_COMUNE;
    this.MAP_CAMPI_FONTE[this.CITTA_ESTERA_RECAPITO] =
      this.RR_C.LABEL_CITTA_ESTERA_RECAPITO;
    this.MAP_CAMPI_FONTE[this.LOCALITA] = this.RR_C.LABEL_LOCALITA;
    this.MAP_CAMPI_FONTE[this.INDIRIZZO] = this.RR_C.LABEL_INDIRIZZO;
    this.MAP_CAMPI_FONTE[this.NUMERO_CIVICO] = this.RR_C.LABEL_NUMERO_CIVICO;
    this.MAP_CAMPI_FONTE[this.CAP] = this.RR_C.LABEL_CAP;
    this.MAP_CAMPI_FONTE[this.TIPO_INVIO] = this.RC_C.LABEL_TIPO_INVIO;
    this.MAP_CAMPI_FONTE[this.PEC] = this.RC_C.LABEL_PEC;
    this.MAP_CAMPI_FONTE[this.EMAIL] = this.RC_C.LABEL_EMAIL;
    this.MAP_CAMPI_FONTE[this.TELEFONO] = this.RC_C.LABEL_TELEFONO;
    this.MAP_CAMPI_FONTE[this.CELLULARE] = this.RC_C.LABEL_CELLULARE;
  }
}

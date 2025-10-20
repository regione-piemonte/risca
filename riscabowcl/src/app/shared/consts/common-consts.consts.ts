import { RiscaButtonConfig, RiscaButtonCss } from '../utilities';
import {
  AbilitaDAGruppi,
  AbilitaDASezioni,
  AbilitaDASoggetti,
  AppActions,
  RCOrientamento,
  RiscaButtonTypes,
  RiscaCssHandlerTypes,
  RiscaFormatoDate,
  UserRoles,
} from '../utilities/enums/utilities.enums';

/**
 * Oggetto di costanti contenente una serie di costanti comuni.
 */
export class CommonConsts {
  /** Costante per label delle select. */
  SELEZIONA = 'Seleziona';

  // Id nazione per ITALIA
  COD_NAZ_ITA = '100';

  // Codici iride per ruolo utente
  AMMINISTRATORE = UserRoles.amministratore;
  GESTORE_BASE = UserRoles.gestoreBase;
  GESTORE_DATI = UserRoles.gestoreDati;
  CONSULTATORE = UserRoles.consultatore;

  // Formattazione delle date per view e server
  DATE_FORMAT_VIEW = RiscaFormatoDate.view;
  DATE_FORMAT_VIEW_EXTENDED = RiscaFormatoDate.viewExtended;
  DATE_FORMAT_SERVER = RiscaFormatoDate.server;

  // Placeholder per quando non viene definito un user
  USER_NOT_DEFINED = 'USER_NOT_DEFINED';
  // Messaggio quando non vengono trovate informazioni dalle ricerche
  MSG_NESSUN_ELEMENTO_TROVATO_FILTRI =
    'Nessun elementro trovato per i filtri di ricerca applicati.';

  // Per i template HTML è necessario usare i dati in oggetti, gli enum non vengono considerati
  MODIFICA = AppActions.modifica;
  INSERIMENTO = AppActions.inserimento;
  HORIZONTAL = RCOrientamento.orizzontale;
  VERTICAL = RCOrientamento.verticale;

  // Classe di stile utilizzata per la gestione delle icone nel componente risca-table
  RISCA_TABLE_ACTION = 'risca-table-action';
  // Label comune per il pulsante "scarica file"
  LABEL_SCARICA_FILE = 'Scarica file';

  // AbilitaDASezioni per i soggetti.
  ABILITA_DA_SEZIONE_SOGGETTI = AbilitaDASezioni.soggetti;
  // AbilitaDASezioni per i gruppi.
  ABILITA_DA_SEZIONE_GRUPPI = AbilitaDASezioni.gruppi;

  // AbilitaDASoggetti per isGestioneAbilitata
  ABILITA_DA_SOGGETTI_IGA = AbilitaDASoggetti.isGestioneAbilitata;
  // AbilitaDASoggetti per isFonteAbilitataInLettura
  ABILITA_DA_SOGGETTI_IFAIL = AbilitaDASoggetti.isFonteAbilitataInLettura;
  // AbilitaDASoggetti per isFonteAbilitataInScrittura
  ABILITA_DA_SOGGETTI_IFAIS = AbilitaDASoggetti.isFonteAbilitataInScrittura;

  // AbilitaDAGruppi per isAbilitato
  ABILITA_DA_GRUPPI_IA = AbilitaDAGruppi.isAbilitato;

  // Tipologia per la gestione del css per class e style
  CSS_TYPE_CLASS = RiscaCssHandlerTypes.class;
  CSS_TYPE_STYLE = RiscaCssHandlerTypes.style;

  // String che definisce un messaggio d'errore da sviluppatore in caso di modalità non passata mediante routing.
  MODALITA_NOT_DEFINED = '[modalita] not defined.';
  MODALITA_NOT_DEFINED_MSG = `Parametrizzare il route applicativo correttamente:
  this._router.navigateByUrl('/any-path'; {
    state: {
      modalita: <modalita>,
    },
  });
  `;

  // String che definisce il nome dello spinner principale dell'applicazione
  SPINNER = 'risca-spinner';
  SPINNER_MANUALE = 'spinner_manuale';

  // DELAY PROGRAMMATO PER LA GESTIONE DELLE RESPONSE ISTANTANEE
  DELAY_MIN = 800;
  DELAY_MAX = 2000;

  // Chiavi impiegate dal servizio RiscaEventEmitter
  // NON CANCELLARE = 4D414E4E41474749412041204A415641534352495054204D444E4E205054544E
  REE_KEY_REST_FORM_CERCA_SOG = 'REE_KEY_REST_FORM_CERCA_SOG';
  REE_KEY_SOGGETTO_SUBMITTED = 'REE_KEY_SOGGETTO_SUBMITTED';

  CAP_MAX_LENGTH_ITA = 5;
  CAP_MAX_LENGTH_EST = 10;

  /** Oggetto costante, che definisce la grafica da utilizzare per le label a sinistra: "da" e "a". */
  LEFT_LABEL_CSS: any = { width: '20px' };
  /** Oggetto costante, che definisce la grafica da utilizzare per le componenti che includono label a sinistra e datepicker. */
  DATEPICKER_CSS: any = { width: '168px' };

  BTN_INDIETRO: RiscaButtonConfig = { label: 'INDIETRO' };
  CSS_INDIETRO: RiscaButtonCss = {
    typeButton: RiscaButtonTypes.link,
    customButton: {
      'padding-left': '0px',
      'padding-right': '0px',
      border: '0px',
    },
  };
  BTN_ANNULLA: RiscaButtonConfig = { label: 'ANNULLA' };
  CSS_ANNULLA: RiscaButtonCss = {
    typeButton: RiscaButtonTypes.link,
    customButton: {
      'padding-left': '0px',
      'padding-right': '0px',
      border: '0px',
    },
  };
  BTN_CERCA: RiscaButtonConfig = { label: 'CERCA' };
  CSS_CERCA: RiscaButtonCss = { typeButton: RiscaButtonTypes.primary };
  BTN_SALVA: RiscaButtonConfig = { label: 'SALVA' };
  CSS_SALVA: RiscaButtonCss = { typeButton: RiscaButtonTypes.primary };
  BTN_SALVA_MODIFICHE: RiscaButtonConfig = { label: 'SALVA MODIFICHE' };
  CSS_SALVA_MODIFICHE: RiscaButtonCss = {
    typeButton: RiscaButtonTypes.primary,
  };
  BTN_CONFERMA: RiscaButtonConfig = { label: 'CONFERMA' };
  CSS_CONFERMA: RiscaButtonCss = { typeButton: RiscaButtonTypes.primary };
  BTN_CREA: RiscaButtonConfig = { label: 'CREA' };
  CSS_CREA: RiscaButtonCss = { typeButton: RiscaButtonTypes.primary };
  BTN_CREA_REPORT: RiscaButtonConfig = { label: 'Crea report' };
  CSS_CREA_REPORT: RiscaButtonCss = { typeButton: RiscaButtonTypes.default };
  BTN_AGGIUNGI: RiscaButtonConfig = { label: 'Aggiungi' };
  CSS_AGGIUNGI: RiscaButtonCss = { typeButton: RiscaButtonTypes.default };

  BTN_ALERT_LABEL_SI = 'Sì';
  BTN_ALERT_LABEL_NO = 'No';

  BTN_CLOSE_X_CSS = {
    customButton: 'risca-btn-x close',
    typeButton: RiscaButtonTypes.none,
  };
  BTN_CLOSE_X_DATA = { label: '×' };

  SEPARATORE_MIGLIAIA = '.';

  TYPEAHEAD_COMUNE_SEARCH_ON_LENGHT: number = 2;
}

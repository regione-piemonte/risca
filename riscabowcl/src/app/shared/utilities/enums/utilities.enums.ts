/**
 * Enum che definisce gli stati di caricamento per l'applicazione, al momento del bootstrap.
 */
export enum RiscaLoadStatus {
  caricamento = 'IN_CARICAMENTO',
  caricato = 'CARICATO',
  fallito = 'FALLITO',
}

/**
 * Enum per gli ambiti
 */
export enum RiscaAmbiti {
  ambiente = 1, // AKA risorse idriche
  // Provvisori.
  operePubbliche = 2,
  attivitaEstrattive = 3,
  tributi = 4,
}

/**
 * Enum per i raggruppamenti per gli oggetti ParametroElaborazione.
 */
export enum RiscaCodiciRaggruppamenti {
  ambiente = 'R1',
}

/**
 * Enum che definisce i possibili ruoli utente.
 */
export enum UserRoles {
  amministratore = 'AMMINISTRATORE',
  gestoreBase = 'GESTORE_BASE',
  gestoreDati = 'GESTORE_DATI',
  consultatore = 'CONSULTATORE',
}

/**
 * Enum che definisce le rotte dell'applicazione come supporto e punto di riferimento per il cambio delle rotte.
 */
export enum AppRoutes {
  root = '/',
  home = '/home',
  pratiche = '/pratiche',
  gestionePratiche = '/pratiche/pratica',
  praticheCollegate = '/pratiche/pratiche-collegate',
  pagamenti = '/pagamenti',
  gestionePagamenti = '/pagamenti/gestione-pagamenti',
  dettaglioPagamenti = '/pagamenti/dettaglio-pagamenti',
  verifiche = '/verifiche',
  gestioneVerifiche = '/verifiche/gestione-verifiche',
  dettaglioRimborsi = '/verifiche/dettaglio-rimborsi',
  dettaglioMorosita = '/verifiche/dettaglio-morosita',
  spedizioni = '/spedizioni',
  bollettini = '/spedizioni/bollettini',
  configurazioni = '/configurazioni',
  gestioneConfigurazioni = '/configurazioni/configurazione',
  report = '/report',
  esportaDati = '/report/esporta-dati',
  soggetti = '/soggetti',
  soggetto = '/soggetti/soggetto',
  gruppo = '/soggetti/gruppo',
}

/**
 * Enum che definisce degli id per la gestione del flusso logico per la gestione dei "callers" applicativi.
 */
export enum AppCallers {
  pratica = 'pratica',
  home = 'home',
  praticheCollegate = 'praticheCollegate',
  ricercaSemplicePratiche = 'ricercaSemplicePratiche',
  ricercaAvanzata = 'ricercaAvanzata',
  gruppo = 'gruppo',
  soggetto = 'soggetto',
  bollettini = 'bollettini',
  pagamentiDaVisionare = 'pagamentiDaVisionare',
  ricerchePagamenti = 'ricerchePagamenti',
  ricerche = 'ricerche',
  ricercaMorosita = 'ricercaMorosita',
  ricercaRimborsi = 'ricercaRimborsi',
  canoni = 'canoni',
  tassiDiInteresse = 'tassiDiInteresse',
  parametriDellaDilazione = 'parametriDellaDilazione',
  altriParametri = 'altriParametri',
}

/**
 * Enum che definisce specifiche proprietà per la gestione specifiche
 */
export enum AppRoutesSpecsState {
  resetState = 'RESET_STATE',
}

/**
 * Enum personalizzato per la gestione dei filtri d'ordinamento per tabelle.
 */
export enum RiscaSortTypes {
  crescente = 'asc',
  decrescente = 'desc',
  nessuno = 'none',
}

/**
 * Enum per lo stato Risca Form Status Validation.
 */
export enum RiscaFormStatus {
  valid = 'valid',
  invalid = 'invalid',
  waiting = 'waiting',
}

/**
 * Enum per i codici tipo soggetto.
 */
export enum CodiceTipoSoggetto {
  PF = 'PF', // Persona Fisica
  PG = 'PG', // Persona Giuridica Privata
  PB = 'PB', // Persona Giuridica Pubblica
}

/**
 * Enum per gli id tipo soggetto.
 */
export enum IdTipoSoggetto {
  PF = 1, // Persona Fisica
  PG = 2, // Persona Giuridica Privata
  PB = 3, // Persona Giuridica Pubblica
}

/**
 * Enum per gli id del tipo natura giuridica.
 */
export enum IdNaturaGiuridica {
  impresaIndividuale = 7,
}

/**
 * Enum per la gestione dell'informazione riguardante il flag pubblico.
 */
export enum FlagPubblico {
  SI = '1',
  NO = '0',
}

/**
 * Enum per i codici tipo sede.
 */
export enum CodiceTipoSede {
  residenza = 'RE',
  sedeLegale = 'SL',
  sedeIstituzionale = 'SI',
}

/**
 * Enum che definisce i valori per i tipi d'istanza di una pratica, per la gestione del menu.
 */
export enum RiscaAzioniPratica {
  ricercaSemplice = 'ricerca-semplice',
  ricercaAvanzata = 'ricerca-avanzata',
  inserisciPratica = 'inserisci-pratica',
}

/**
 * Enum che definisce i valori per le istanze di ricerca pratica/stato debitorio, per la gestione del menu.
 */
export enum RiscaIstanzeRicercaAvanzata {
  pratiche = 'pratiche',
  statiDebitori = 'statiDebitori',
}

/**
 * Enum che definisce i valori per le ricerche pagamenti, per la gestione del menu.
 */
export enum RiscaRicerchePagamento {
  pagamenti = 'pagamenti',
  morosita = 'morosita',
  rimborsi = 'rimborsi',
}

/**
 * Enum che definisce i valori per le istanze di una pratica, per la gestione del menu.
 */
export enum RiscaIstanzePratica {
  generaliAmministrativi = 'generali-amministrativi',
  datiAnagrafici = 'dati-anagrafici',
  datiTecnici = 'dati-tecnici',
  datiContabili = 'dati-contabili',
  documentiAllegati = 'documenti-allegati',
}

/**
 * Enum che definisce i valori per le parti dei dati contabili, per la gestione del menu.
 */
export enum RiscaDatiContabili {
  statiDebitori = 'stati-debitori',
  versamenti = 'versamenti',
  rimborsi = 'rimborsi',
  accertamenti = 'accertamenti',
  statoDebitorio = 'stato-debitorio',
  inserimentoStatoDebitorio = 'inserimento-stato-sebitorio',
  nap = 'nap',
  calcolaInteressi = 'calcola-interessi',
  stampa = 'stampa',
}

/**
 * Enum che definisce i valori per le parti dello stato debitorio, per la gestione del menu.
 */
export enum RiscaStatoDebitorio {
  generaliAmministrativiDilazione = 'generali-amministrativi-dilazione',
  datiAnagrafici = 'dati-anagrafici',
  annualita = 'annualita',
}

/**
 * Enum che definisce i valori per le parti della sezione NAP, per la gestione del menu.
 */
export enum RiscaNumeroAvvisoPagamento {
  statiDebitoriNAP = 'stati-debitori-nap',
  dettaglioStatoDebitorioNAP = 'dettaglio-stato-debitorio-nap',
}

/**
 * Enum che definisce delle azioni CRUD di utilizzo generico nell'applicazione.
 */
export enum EnumCRUD {
  create = 'creazione',
  read = 'lettura',
  update = 'aggiornamento',
  delete = 'cancellazione',
}

/**
 * Enum che definisce il tipo d'azione che l'applicazione gestisce.
 */
export enum AppActions {
  inserimento = 'insert',
  modifica = 'update',
  cancellazione = 'delete',
}

/**
 * Enum che definisce i possibili orientamenti per il componente risca-checkbox/risca-radio.
 */
export enum RCOrientamento {
  verticale = 'vertical',
  orizzontale = 'horizontal',
}

/**
 * Enum che definisce l'etichetta per le tipologie di istanze e provvedimenti.
 */
export enum TipiIstanzaProvvedimento {
  istanza = 'Istanza',
  provvedimento = 'Provvedimento',
}

/**
 * Enum che definisce il formato delle date usate dall'applicazione.
 */
export enum RiscaFormatoDate {
  view = 'DD/MM/YYYY',
  viewExtended = 'DD/MM/YYYY HH:mm:ss',
  server = 'YYYY-MM-DD',
}

/**
 * Enum di supporto che definisce gli degli ambiti dell'applicazione.
 */
export enum IdAmbiti {
  risorseIdriche = 1,
}

/**
 * Enum di supporto che definisce i valori per la ricerca per campo libero.
 * I flag sono utilizzati per la ricerca di soggetti e di gruppi.
 */
export enum FlagRicercaCL {
  gruppi = 'G',
  soggetti = 'S',
  soggettiEGruppi = 'T',
}

/**
 * Enum per la gestione delle "sezioni" per la gestione dei dati anagrafici.
 */
export enum AbilitaDASezioni {
  soggetti = 'soggetti',
  gruppi = 'gruppi',
}

/**
 * Enum per la gestione delle abilitazioni dei dati anagrafici sui soggetti.
 */
export enum AbilitaDASoggetti {
  isGestioneAbilitata = 'isGestioneAbilitata',
  isFonteAbilitataInLettura = 'isFonteAbilitataInLettura',
  isFonteAbilitataInScrittura = 'isFonteAbilitataInScrittura',
}

/**
 * Enum per la gestione delle abilitazioni dei dati anagrafici sui gruppi.
 */
export enum AbilitaDAGruppi {
  isAbilitato = 'isAbilitato',
}

/**
 * Enum per la gestione delle abilitazioni dei dati ACTA.
 */
export enum AbilitaACTA {
  visDocumentiPratica = 'visDocumentiPratica',
}

/**
 * Enum che definisce degli id per la gestione del flusso logico per la gestione delle pratiche collegate.
 */
export enum PraticheCollegateCaller {
  home = 'home',
}

/*
 * Enum per gli stati riscossione per l'ambito Ambiente (risorse idriche)
 */
export enum CodStatiRiscossione {
  ATTIVA = 'ATTIVA',
  RINUNCIATA = 'RINUNCIATA',
  REVOCATA = 'REVOCATA',
  ANNULLATA = 'ANNULLATA ',
  RIGETTATA = 'RIGETTATA ',
  SOSPESA = 'SOSPESA',
  SCADUTA = 'SCADUTA',
}

/**
 * Sono i cod_tipo_provvedimento di TipoProvvedimentoVo per le istanze della pratica.
 * L'ambito è: AMBIENTE
 */
export enum CodiciIstanzaAmbiente {
  IST_RICONOSCIMENTO = 'IST_RICONOSCIMENTO',
  IST_RINNOVO = 'IST_RINNOVO',
  IST_RINNOVO_PR_88_96 = 'IST_RINNOVO_PR_88_96',
  IST_SANATORIA = 'IST_SANATORIA',
  IST_RINUNCIA_PARZ = 'IST_RINUNCIA_PARZ',
  IST_RINUNCIA_TOT = 'IST_RINUNCIA_TOT',
  IST_SUBINGRESSO = 'IST_SUBINGRESSO',
  RINNOVO_1 = '1_RINNOVO',
  RINNOVO_2 = '2_RINNOVO',
  RINNOVO_3 = '3_RINNOVO',
  RINNOVO_4 = '4_RINNOVO',
  RINNOVO_5 = '5_RINNOVO',
  AUT_PROVVISORIA = 'AUT_PROVVISORIA',
}

/**
 * Sono i cod_tipo_provvedimento di TipoProvvedimentoVo per i provvedimenti della pratica.
 * L'ambito è: AMBIENTE
 */
export enum CodiciProvvedimentoAmbiente {
  RIN_TOTALE = 'RIN_TOTALE',
}

/**
 * Enum che definisce gli id delle nazioni dell'applicazione.
 */
export enum IdNazioni {
  italia = 1,
}

/**
 * Enum che definisce i codici istat delle nazioni dell'applicazione.
 */
export enum CodIstatNazioni {
  italia = '100',
}

/**
 * Enum che definisce le denominazioni delle nazioni dell'applicazione.
 */
export enum DenominazioneNazioni {
  italia = 'Italia',
}

/**
 * Enum che definisce gli id dei tipi invio dell'applicazione.
 */
export enum IdTipoInvio {
  PEC = 1,
  email = 2,
  cartaceo = 3,
}

/**
 * Enum che definisce il livello di gestione degli elementi risca.
 */
export enum RiscaInfoLevels {
  success = 'SUCCESS',
  danger = 'DANGER',
  info = 'INFO',
  warning = 'WARNING',
  debug = 'DEBUG',
  error = 'ERROR',
  log = 'LOG',
  none = '',
}

/**
 * Enum che definisce le azioni pre-configurate per il componente: risca-table.
 */
export enum RiscaTableBodyTabMethods {
  check = 'check',
  delete = 'delete',
  detail = 'detail',
  modify = 'modify',
  radio = 'radio',
  close = 'close',
}

/**
 * Enum che definisce la mappatura degli di del tipo recapito.
 */
export enum IdTipoRecapito {
  principale = 1,
  alternativo = 2,
}

/**
 * Enum che definisce le tipologie di cloning di un oggetto/array.
 */
export enum RiscaCloningType {
  simple = 'SIMPLE_CLONING',
  deep = 'DEEP_CLONING',
  lodash = 'LODASH_CLONING',
  lodashDeep = 'LODASH_DEEP_CLONING',
}

/**
 * Enum che definisce le azioni possibili per l'interfaccia IRiscaTableSMHActions.
 */
export enum RiscaTableSMHActions {
  checkbox = 'checkbox',
}

/**
 * Tipo personalizzato per la gestione dello stato delle form.
 */
export enum RiscaFormStatus {
  inAttesa = 'waiting',
  valido = 'valid',
  invalido = 'invalid',
}

/**
 * Enum personalizzato per la gestione del tipo di css da usare.
 */
export enum RiscaCssHandlerTypes {
  class = 'class',
  style = 'style',
}

/**
 * Enum personalizzato per la gestione delle componenti gestite dal servizio risca-form-builder.
 */
export enum RiscaFormBuilderSize {
  small = 'small',
  standard = 'standard',
  x2 = 'x2',
  x3 = 'x3',
  full = 'full',
}

/**
 * Enum personalizzato per la gestione degli status generati dal server e definiti all'interno degli oggetti d'errore intercettati dalle subscribe per le http requests.
 */
export enum RiscaServerStatus {
  unknown = 0,
  notFound = '404',
}

/**
 * Enum personalizzato con la definizione delle API per la gestione di alcune specifiche casistiche.
 */
export enum RiscaServerUtilsAPI {
  sessionExpiredAPI_partial = '/profile/SAML2/Redirect/',
}

/**
 * Enum personalizzato che mappa il tipo di boolean gestito dai valori server.
 */
export enum ServerStringAsBoolean {
  true = 'S',
  false = 'N',
}

/**
 * Enum personalizzato che mappa il tipo di boolean gestito dai valori server.
 */
export enum ServerNumberAsBoolean {
  true = 1,
  false = 0,
}

/**
 * Enum personalizzato che mappa il tipo di richiedente per una specifica azione.
 */
export enum AppClaimants {
  insertGruppo_cercaTitolare = 'inserimento-gruppo-da-cerca-titolare',
  dettaglioGruppo_pratica = 'dettaglio-gruppo-da-pratica',
  insertSoggetto = 'inserimento-soggetto-se-non-trovato',
  pagamentoSD = 'pagamento-sd',
  ricercaPagamenti = 'ricerca-pagamenti',
  ricercaMorosita = 'ricerca-morosita',
  ricercaRimborsi = 'ricerca-rimborsi',
  ricercaAvanzataPratiche = 'ricerca-avanzata-pratiche',
  ricercaAvanzataStatiDebitori = 'ricerca-avanzata-stati-debitori',
}

/**
 * Enum che definisce i flag di gestione per la ricerca di riduzioni/aumenti dei dati tecnici ambiente.
 */
export enum DTAmbienteFlagManuale {
  tutti = undefined,
  liberi = 1,
  automatico = 0,
}

/**
 * Enum di supporto che definisce l'id per il recupero di istanze e provvedimenti.
 */
export enum IdIstanzaProvvedimento {
  provvedimento = 0,
  istanza = 1,
}

/**
 * Enum di supporto che definisce il codice per i tipi autorizzazione.
 */
export enum CodTipiAutorizzazione {
  CONCESSIONE = 'CONCESSIONE',
  ATTINGIMENTO = 'ATTINGIMENTO',
  ATTINGIMENTO_PLURI = 'ATTINGIMENTO_PLURI',
  AUTORIZZAZIONE_PROVV = 'AUTORIZZAZIONE_PROVV',
}

/**
 * Enum che definisce l'id per le unità di misura degli usi per dati tecnici.
 */
export enum IdUnitaDiMisura {
  euroLitriAlSecondo = 1,
}

/**
 * Enum che definisce le tipologie di button di risca.
 */
export enum RiscaButtonTypes {
  default = 'btn-default',
  link = 'btn-link',
  primary = 'btn-primary',
  none = '',
}

/**
 * Enum che definisce la posizione per il componente risca-label.
 */
export enum RiscaLabelPosition {
  top = 'top',
  left = 'left',
  right = 'right',
  bottom = 'bottom',
}

/**
 * Enum che definisce i codici per la gestione dei componenti dinamici per i dati tecnici.
 */
export enum CodiciComponentiDt {
  pratica = 'GESTIONE',
  ricerca = 'RICERCA',
  sdGeneraliTecnici = 'SD_GENERALI_TECNICI',
  sdAnnualita = 'SD_ANNUALITA',
}

/**
 * Enum che definisce il tipo di sanitizer da impiegare per la pipe: 'sanitizer'.
 */
export enum SanitizerTypes {
  html = 'html',
  style = 'style',
  script = 'script',
  url = 'url',
  resourceUrl = 'resourceUrl',
}

/**
 * Enum di comodo che definisce le sigle delle riduzioni per la parte dei dati tecnici.
 */
export enum CodiciRiduzioniAumenti {
  E1 = 'E1',
  UP2 = 'UP2',
  UP3 = 'UP3',
  R5 = 'R5',
  R1 = 'R1',
  R2 = 'R2',
  R6 = 'R6',
  R3 = 'R3',
  R4 = 'R4',
  M1 = 'M1',
  UP1 = 'UP1',
  RM1 = 'RM1',
  NP = 'NP',
}

/**
 * Enum di comodo che definisce la mappatura parlante tra tipi ind oggetti e il loro codice da passare in query param.
 */
export enum VerifyIndTipiOggetti {
  soggetto = 'S',
  gruppoReferenteNotUpd = 'G',
  gruppoReferenteUpd = 'R',
  recapitoPrincipale = 'P',
  recapitoAlternativo = 'A',
}

/**
 * Enum di comodo che definisce la mappatura parlante tra tipi ind operazioni e il loro codice da passare in query param.
 */
export enum VerifyIndTipiOperazioni {
  update = 'U',
  delete = 'D',
}

/**
 * Enum di comodo che definisce le modalità di gestione per la modale degli indirizzi di spedizione.
 * A seconda del tipo di gestione, verranno impostati e gestiti flussi differenti.
 */
export enum RiscaGestioneISModal {
  correzione = 'correzione_indirizzi_spedizione',
  aggiornamentoSingolo = 'aggiornamento_indirizzo_spedizione',
}

/**
 * Enum che mappa i tipi di dati richiesti per il servizio HttpClient di Angular.
 */
export enum ResponseTypes {
  json = 'json',
  arraybuffer = 'arraybuffer',
  blob = 'blob',
  text = 'text',
}

/**
 * Enum che mappa i tipi di dati ritornati dal servizio HttpClient di Angular.
 */
export enum BlobTypes {
  pdf = 'application/pdf',
  png = 'image/png',
  jpg = 'image/jpeg',
  tiff = 'image/tiff',
  tif = 'image/tiff',
  bmp = 'image/bmp',
  p7m = 'application/pkcs7-mime',
}

/*
 * Enum che mappa i codici tipo messeggio per RISCA.
 */
export enum RiscaCodTipiMessaggioUtente {
  USER_ACTION = 'A',
  CALC_ERROR = 'C',
  ERROR = 'E',
  FORMAL_CHECK = 'F',
  INFO = 'I',
  SUCCESS = 'P',
  USER_ENABLING = 'U',
}

/**
 * Enum con i codici dei tipi riscossione nello stato debitorio
 */
export enum CodiciTipiRiscossione {
  RIMBORSATO = 'RIMBORSATO',
  DA_COMPENSARE = 'DA_COMPENSARE',
  COMPENSATO = 'COMPENSATO',
}

/**
 * Enum che mappa i ccodici degli stati contribuzione di uno stato debitorio per RISCA.
 */
export enum RiscaCodStatiContribuzione {
  REGOLARE = 'RE',
  INSUFFICIENTE = 'IN',
  REGOLARIZZATO = 'RO',
  ECCEDENTE = 'EC',
  INESIGIBILE = 'IG',
}

/**
 * Enum che definisce i tipi di frazionamento per il calcolo canone.
 */
export enum TipiFrazionamentoCanone {
  inizio = 'inizio',
  fine = 'fine',
}

/**
 * Enum che definisce i tipi di label gestiti per il flag annullato.
 */
export enum StatiSDFlagAnnullato {
  annullato = 'ANNULLATO',
  attivo = 'ATTIVO',
  sconosciuto = 'SCONOSCIUTO', // Fallback di sicurezza
}

/**
 * Enum che definisce la tipologia di gestione del calcolo canone per i dati tecnici dell'annualità.
 * Se il canone è inserito dall'utente sarà "manuale".
 * Se invece il canone viene generato tramite API sarà "automatico".
 */
export enum TipoCalcoloCanoneADT {
  manuale = 'MANUALE',
  automatico = 'AUTOMATICO',
  nessuno = 'NESSUNO',
  inserimento = 'INSERIMENTO',
}

/**
 * Enum che definisce i tipi di input utilizzabili all'intenro della tabella risca.
 */
export enum TipoInputTable {
  text = 'textConfig',
  number = 'numberConfig',
  numberItFormat = 'numberItFormatConfig',
  email = 'emailConfig',
}

/**
 * Enum che definisce quando attivare la validazione del campo per la input della tabella risca.
 */
export enum ValidaInputTableOn {
  blur = 'blur',
  change = 'change',
}

/**
 * Enum che definisce le posizioni per la gestione delle appendici delle input risca.
 */
export enum RiscaAppendixPosition {
  left = 'sinistra',
  right = 'destra',
}

/**
 * Enum che riporta nel progetto i valori riferiti alla tipologia FormHooks.
 * @tutorial https://angular.io/api/forms/AbstractControl#updateOn
 */
export enum RiscaFormHooks {
  change = 'change',
  blur = 'blur',
  submit = 'submit',
}

/**
 * Enum che definisce i fruitori gestiti dall'applicazione.
 */
export enum RiscaFruitori {
  RISCA = 'RISCA',
}

/**
 * Interfaccia che definisce la mappatura delle chiavi d'errore.
 */
interface IRiscaErrorKeys {
  /** Costante che indica la chiave per il recupero dell'errore form per campo invalido generico. */
  ERRORE_FORMATO_GENERICO: string;
  /** Costante che indica la chiave per il recupero dell'errore form sul calendario concessione. */
  DATA_CONCESSIONE: string;
  /** Costante che indica la chiave per il recupero dell'errore form sul calendario sospensione. */
  DATA_SOSPENSIONE: string;
  /** Costante che indica la chiave per il recupero dell'errore form per motivazione non compilata, se pratica è prenotata. */
  MOTIVAZIONE: string;
  /** Costante che indica la chiave per il recupero dell'errore sul form: pratica ordinaria progressivo. */
  PRATICA_ORDINARIA_PROGRESSIVO: string;
  /** Costante che indica la chiave per il recupero dell'errore sul form: pratica preferenziale progressivo. */
  PRATICA_PREFERENZIALE_PROGRESSIVO: string;
  /** Costante che indica la chiave per il recupero dell'errore sul form: pratica attingimento o plus progressivo. */
  PRATICA_ATTINGIMENTO_O_PLUS_PROGRESSIVO: string;
  /** Costante che indica la chiave per il recupero dell'errore sul form: procedimento concessione o autorizzazione provvisora, e scadenza sospensione non definita. */
  PROCEDIMENTO_SCADENZA_SOSPENSIONE: string;
  /** Costante che indica la chiave per il recupero dell'errore sul form: data scadenza obbligatoria quando procedimento = 'Concessione' . */
  DATA_SCADENZA_CONCESSIONE_REQUIRED: string;
  /** Costante che indica la chiave per il recupero dell'errore sul form: composizione del numero non numero. */
  NUMBER_COMP_NOT_NUMBER: string;
  /** Costante che indica la chiave per il recupero dell'errore sul form: composizione del numero non valida. */
  NUMBER_COMP_INVALID: string;
  /** Costante che indica la chiave per il recupero dell'errore sul form: composizione del numero non valida, poiché minore del min. */
  NUMBER_MIN_INVALID: string;
  /** Costante che indica la chiave per il recupero dell'errore sul form: composizione del numero non valida, poiché maggiore del max. */
  NUMBER_MAX_INVALID: string;
  /** Costante che indica la chiave per il recupero dell'errore sul form: composizione del numero non valida, poiché il min è maggiore del max. */
  NUMBER_MIN_MAX_INVALID: string;
  /** Costante che indica la chiave per il recupero dell'errore sul form: quantità di elementi minimi non raggiunta. */
  CONTAIN_AT_LEAST: string;
  /** Costante che indica la chiave per il recupero dell'errore sul form: quantità falda profonda maggiore della quantità. */
  QUANT_FALD_PROF_INVALID: string;
  /** Costante che indica la chiave per il recupero dell'errore sul form: quantità falda profonda obbligatoria per aumento definito. */
  QUANT_FALD_PROF_AUM_INVALID: string;
  /** Costante che indica la chiave per il recupero dell'errore sul form: quantità falda profonda maggiore di zero se l'unità di misura è '€ l/sec'. */
  QUANT_FALD_PROF_INV_PER_UNITA: string;
  /** Costante che indica la chiave per il recupero dell'errore sul form: quantità falda profonda maggiore di zero se l'unità di misura è '€ l/sec'. */
  QUANTITA_RIS_IDRICHE_PER_UNITA: string;
  /** Costante che indica la chiave per l'errore di validazione del codice fiscale per tipo soggetto: persona fisica. */
  COD_FISC_PF: string;
  /** Costante che indica la chiave per l'errore di validazione della partita iva per tipo soggetto: persona giuridica pubblica. */
  PARTITA_IVA_PB: string;
  /** Costante che indica la chiave per l'errore di validazione del codice fiscale per tipo soggetto: persona giuridica privata. */
  COD_FISC_PG: string;
  /** Costante che indica la chiave per l'errore di validazione del codice fiscale per tipo soggetto: persona giuridica privata. */
  PARTITA_IVA_PG: string;
  /** Costante che indica la chiave per l'errore di validazione sul codice fiscale. */
  COD_FISC: string;
  /** Costante che indica la chiave per l'errore di validazione per un comune non valido. */
  COMUNE_DATA_FINE_VALIDITA: string;
  /** Costante che indica la chiave per l'errore di validazione sullo stato di una riscossione sospesa per data sospensione non valida */
  STATO_RISCOSSIONE_SOSPESA_DATA_SOSPENSIONE: string;
  /** Costante che indica la chiave per l'errore di validazione sullo stato di una riscossione rinunciata per istanza mancante o non valida*/
  STATO_RISCOSSIONE_RINUNCIATA_ISTANZA: string;
  /** Costante che indica la chiave per l'errore di validazione sullo stato di una riscossione rinunciata per istanza non valida (perchè assente o provvedimento) per tipo provvedimento != rinuncia totale*/
  STATO_RISCOSSIONE_RINUNCIATA_TIPO: string;
  /** Costante che indica la chiave per l'errore di validazione sullo stato di una riscossione scaduta per data concessione assente o futura (ancora deve scadere)*/
  STATO_RISCOSSIONE_SCADUTA_DATA_CONCESSIONE: string;
  /** Costante che indica la chiave per l'errore di validazione sullo stato di una riscossione scaduta per istanza non valida*/
  STATO_RISCOSSIONE_SCADUTA_ISTANZA: string;
  /** Costante che indica la chiave per l'errore di validazione sullo stato di una riscossione attiva*/
  STATO_RISCOSSIONE_ATTIVA: string;
  /** Costante che indica la chiave per l'errore di validazione sulla quantità di componenti. */
  QUANTITA_COMPONENTI_GRUPPO: string;
  /** Costante che indica la chiave per l'errore di validazione sulla quantità di provvedimento. */
  QUANTITA_PROVVEDIMENTI: string;
  /** Costante che indica la chiave per l'errore di validazione per la mancanza di un capogruppo. */
  PRESENZA_CAPOGRUPPO: string;
  /** Costante che indica la chiave per l'errore di validazione per una data scadenza emas iso. */
  DATA_SCADENZA_EMAS_ISO_R5: string;
  /** Costante che indica la chiave per l'errore di validazione per un anno bollettazione ordinaria prima dell'anno di scadenza pagamento. */
  ANNO_PRIMA_ANNO_SCAD_PAG: string;
  /** Costante che indica la chiave per l'errore di validazione data protocollo dopo la data scandenza pagamento. */
  DATA_PROT_DOPO_DATA_SCAD_PAG: string;
  /** Costante che indica la chiave per l'errore di validazione quando una data d'inizio è temporalmente sbagliata rispetto a quella di fine. */
  DATE_START_END_INVALID: string;
  /** Costante che indica la chiave per l'errore di validazione della descrizione dell'indirizzo di spedizione in presenza di un messaggio. */
  DESC_BY_MESS_INVALID: string;
  /** Costante che indica la chiave per l'errore di validazione quando il campo ha una lunghezza errata, rispetto a quello richiesto. */
  LUNGHEZZA_CAMPO_ESATTA: string;
  /** Costante che indica la chiave per l'errore di validazione quando il campo ha una lunghezza errata e risulta minore rispetto a quello richiesto. */
  LUNGHEZZA_CAMPO_MINORE: string;
  /** Costante che indica la chiave per l'errore di validazione quando il campo ha una lunghezza errata e risulta maggiore rispetto a quello richiesto. */
  LUNGHEZZA_CAMPO_MAGGIORE: string;
  /** Costante che indica la chiave per l'errore di validazione quando il campo non è valorizzato ed è richiesto. */
  DATA_INIZIO_REQUIRED: string;

  /** Costante che indica la chiave per l'errore di validazione quando lo stato debitorio può essere salvato solo se almeno un’annualità è associata. Quindi: COUNT StatoDebitorio.annualita_sd > 0. */
  STATO_DEBITORIO_E_ANNUALITA: string;
  /** Costante che indica la chiave per l'errore di validazione quando tutte le annualità dello stato debitorio devono avere almeno un uso, con i relativi dati, associato. Quindi: COUNT StatoDebitorio.annualita_uso_sd > 0. */
  ANNUALITA_E_USI: string;
  /** Costante che indica la chiave per l'errore di validazione quando il periodo pagamento deve essere definito all’interno dell’uso. Quindi: StatoDebitorio.desc_periodo_pagamento != NULL. */
  PERIODO_PAGAMENTO: string;
  /** Costante che indica la chiave per l'errore di validazione quando la data di scadenza del pagamento deve essere valorizzata. */
  DATA_SCADENZA_PAGAMENTO: string;
  /** Costante che indica la chiave per l'errore di validazione quando il flag annullato è attivo, la motivazione deve essere valorizzata. Quindi: StatoDebitorio.flg_annullato = 1 (true) && StatoDebitorio.desc_motivo_annullo != (NULL OR ‘’) */
  FLAG_ANNULLATO_E_MOTIVAZIONE: string;
  /** Costante che indica la chiave per l'errore di validazione quando il flag invio speciale è attivo, non ci devono essere annualità con un stesso anno. Quindi: StatoDebitorio.flg_invio_speciale == 1 (true) && UNIQUE StatoDebitorio.annualita_sd.anno. */
  VERIFICA_ANNUALITA_DOPPIA: string;
  /** Costante che indica la chiave per l'errore di validazione quando la proprietà dell’oggetto stato debitorio: imp_compensazione_canone; è valorizzata, la modifica dello stato debitorio non è permessa. Quindi: StatoDebitorio.imp_compensazione_canone == (NULL OR 0). */
  REGOLE_PER_MODIFICA: string;
  /** Costante che indica la chiave per l'errore di validazione quando all’interno dello stato debitorio, esistono rimborsi “da compensare” o “compensati” (id_tipo_rimborso = 2 o id_tipo_rimborso = 3), non è possibile modificare lo stato debitorio. Quindi: StatoDebitorio.Rimborso<list>.tipo_rimborso.id_tipo_rimborso != (2 OR 3). */
  RIMBORSI_DA_COMPENSARE_O_COMPENSATI: string;
  /** Costante che indica la chiave per l'errore di validazione quando si modifica lo stato debitorio, l'importo compensazione canone non deve essere valorizzato. */
  IMPORTO_COMPENSAZIONE_CANONE: string;
  /** Costante che indica la chiave per l'errore di validazione quando si assegna un importo da assegnare superiore all'importo versato nella pagina dei versamenti */
  IMPORTO_DA_ASSEGNARE_SUPERIORE_A_IMPORTO_VERSATO: string;
  /** Costante d'errore per data inizio/fine invalida per la ricerca avanzata e i campi: SCADENZA_CONCESSIONE. */
  DATA_INI_FIN_SCADENZA_CONCESSIONE: string;
  /** Costante d'errore per data inizio/fine invalida per la ricerca avanzata e i campi: DATA_RINUNCIA_REVOCA. */
  DATA_INI_FIN_DATA_RINUNCIA_REVOCA: string;
  /** Costante d'errore per data inizio/fine invalida per la ricerca avanzata e i campi: DATA_ISTANZA. */
  DATA_INI_FIN_DATA_ISTANZA: string;
  /** Costante d'errore per data inizio/fine invalida per la ricerca avanzata e i campi: DATA_TITOLO. */
  DATA_INI_FIN_DATA_TITOLO: string;
  /** Costante d'errore per gestione del canone uso e del canone unitario uso. */
  CANONE_E_CANONE_UNITARIO_REQUIRED: string;
  /** Costante d'errore per gestione dell'errore dovuto ad usi di legge senza dati per i canoni. */
  USI_INVALIDI_CANONE: string;
  /** Costante d'errore per la gestione dell'errore dovuto all'importo versato maggiore del canone dovuto. */
  IMP_VERSATO_MAGG_IMP_MANCANTE_CON_INTERESSI: string;
  /** Costante d'errore per la gestione dell'errore dovuto all'importo versato maggiore dell'importo pagamento. */
  IMPORTO_VERSATO_MAGG_IMPORTO_PAGAMENTO: string;

  /** Costante che indica la chiave per l'errore di validazione quando una data d'inizio è temporalmente sbagliata rispetto a quella di fine per la sezione di export dati. */
  DATE_START_AFTER_END_EXPORT_DATA: string;
  /** Costante che indica la chiave per l'errore di validazione quando la data fine per form esporta dati NON E' nello stesso anno della data inizio. */
  DATE_END_EXPORT_DATA_SAME_YEAR: string;

  /** Costante che indica la chiave per l'errore di importo non proprio 0 o minore di 0. */
  IMPORTO_NON_PROPRIO_ZERO: string;

  /** Costante che indica la chiave per l'errore per nessun campo valorizzato per ricerca pagamenti. */
  ALMENO_UN_CAMPO_RICERCA_PAGAMENTI_VALORIZZATO: string;

  /** Costante che indica la chiave per l'errore quando un numero non è all'interno di un range di valori. */
  NUMBER_NOT_IN_RANGE: string;

  /** Costante che indica la chiave per l'errore quando i campi risultano obbligatori. */
  CAMPI_OBBLIGATORI: string;

  /** Costante che indica la chiave per l'errore quando il "Valore1" dei range canoni è maggiore del "Valore2". */
  RANGE_VALORE1_GREAT_VALORE2: string;
}

/**
 * Classe di mapping delle chiavi per gli erorri del form.
 */
export const RiscaErrorKeys: IRiscaErrorKeys = {
  /** Costante che indica la chiave per il recupero dell'errore form per campo invalido generico. */
  ERRORE_FORMATO_GENERICO: 'erroreFormatoGenerico',
  /** Costante che indica la chiave per il recupero dell'errore form sul calendario concessione. */
  DATA_CONCESSIONE: 'dataInizioConcessioneInvalid',
  /** Costante che indica la chiave per il recupero dell'errore form sul calendario sospensione. */
  DATA_SOSPENSIONE: 'dataInizioSospensioneInvalid',
  /** Costante che indica la chiave per il recupero dell'errore form per motivazione non compilata, se pratica è prenotata. */
  MOTIVAZIONE: 'motivazioneRequired',
  /** Costante che indica la chiave per il recupero dell'errore sul form: pratica ordinaria progressivo. */
  PRATICA_ORDINARIA_PROGRESSIVO: 'praticaOrdinariaProgressivoInvalid',
  /** Costante che indica la chiave per il recupero dell'errore sul form: pratica preferenziale progressivo. */
  PRATICA_PREFERENZIALE_PROGRESSIVO: 'praticaPreferenzialeProgressivoInvalid',
  /** Costante che indica la chiave per il recupero dell'errore sul form: pratica attingimento o plus progressivo. */
  PRATICA_ATTINGIMENTO_O_PLUS_PROGRESSIVO:
    'praticaAttingimentoOPlusProgressivoInvalid',
  /** Costante che indica la chiave per il recupero dell'errore sul form: procedimento concessione o autorizzazione provvisora, e scadenza sospensione non definita. */
  PROCEDIMENTO_SCADENZA_SOSPENSIONE: 'scadenzaSospensioneRequired',
  /** Costante che indica la chiave per il recupero dell'errore sul form: data scadenza concessione richiesta . */
  DATA_SCADENZA_CONCESSIONE_REQUIRED: 'dataScadenzaConcessioneRequired',
  /** Costante che indica la chiave per il recupero dell'errore sul form: composizione del numero non numero. */
  NUMBER_COMP_NOT_NUMBER: 'numberCompositionNotNumber',
  /** Costante che indica la chiave per il recupero dell'errore sul form: composizione del numero non valida. */
  NUMBER_COMP_INVALID: 'numberCompositionInvalid',
  /** Costante che indica la chiave per il recupero dell'errore sul form: composizione del numero non valida, poiché minore del min. */
  NUMBER_MIN_INVALID: 'numberMinInvalid',
  /** Costante che indica la chiave per il recupero dell'errore sul form: composizione del numero non valida, poiché maggiore del max. */
  NUMBER_MAX_INVALID: 'numberMaxInvalid',
  /** Costante che indica la chiave per il recupero dell'errore sul form: composizione del numero non valida, poiché il min è maggiore del max. */
  NUMBER_MIN_MAX_INVALID: 'numberMinMaxInvalid',
  /** Costante che indica la chiave per il recupero dell'errore sul form: quantità di elementi minimi non raggiunta. */
  CONTAIN_AT_LEAST: 'arrayNotContainsAtLeast',
  /** Costante che indica la chiave per il recupero dell'errore sul form: quantità falda profonda maggiore della quantità. */
  QUANT_FALD_PROF_INVALID: 'valoreQuantitaFaldaInvalid',
  /** Costante che indica la chiave per il recupero dell'errore sul form: quantità falda profonda obbligatoria per aumento definito. */
  QUANT_FALD_PROF_AUM_INVALID: 'valoreQuantitaFaldaPerAumentoInvalid',
  /** Costante che indica la chiave per il recupero dell'errore sul form: quantità falda profonda maggiore della quantità. */
  QUANT_FALD_PROF_INV_PER_UNITA: 'valoreQuantitaFaldaPerUnitaDiMisuraInvalid',
  /** Costante che indica la chiave per il recupero dell'errore sul form: quantità falda profonda maggiore della quantità. */
  QUANTITA_RIS_IDRICHE_PER_UNITA:
    'valoreQuantitaFaldaPerUnitaDiMisuraRisorseIdricheInvalid',
  /** Costante che indica la chiave per l'errore di validazione del codice fiscale per tipo soggetto: persona fisica. */
  COD_FISC_PF: 'PFCodFiscInvalid',
  /** Costante che indica la chiave per l'errore di validazione della partita iva per tipo soggetto: persona giuridica pubblica. */
  PARTITA_IVA_PB: 'PBCodFiscInvalid',
  /** Costante che indica la chiave per l'errore di validazione del codice fiscale per tipo soggetto: persona giuridica privata. */
  COD_FISC_PG: 'PGCodFiscInvalid',
  /** Costante che indica la chiave per l'errore di validazione della partita iva per tipo soggetto: persona giuridica privata. */
  PARTITA_IVA_PG: 'PGPartitaIvaInvalid',
  /** Costante che indica la chiave per l'errore di validazione sul codice fiscale. */
  COD_FISC: 'codiceFiscaleInvalid',
  /** Costante che indica la chiave per l'errore di validazione per comune non valido. */
  COMUNE_DATA_FINE_VALIDITA: 'comuneDataFineValiditaInvalid',
  /** Costante che indica la chiave per l'errore di validazione sullo stato di una riscossione sospesa per data sospensione non valida */
  STATO_RISCOSSIONE_SOSPESA_DATA_SOSPENSIONE:
    'statoRiscossioneSospensioneDateInvalid',
  /** Costante che indica la chiave per l'errore di validazione sullo stato di una riscossione rinunciata per istanza mancante o non valida*/
  STATO_RISCOSSIONE_RINUNCIATA_ISTANZA:
    'statoRiscossioneRinunciataIstanzaInvalid',
  /** Costante che indica la chiave per l'errore di validazione sullo stato di una riscossione rinunciata per istanza non valida (perchè assente o provvedimento) per tipo provvedimento != rinuncia totale*/
  STATO_RISCOSSIONE_RINUNCIATA_TIPO: 'statoRiscossioneRinunciataTipoInvalid',
  /** Costante che indica la chiave per l'errore di validazione sullo stato di una riscossione scaduta per data concessione assente o futura (ancora deve scadere)*/
  STATO_RISCOSSIONE_SCADUTA_DATA_CONCESSIONE:
    'statoRiscossioneScadutaDataConcessioneInvalid',
  /** Costante che indica la chiave per l'errore di validazione sullo stato di una riscossione scaduta per istanza non valida*/
  STATO_RISCOSSIONE_SCADUTA_ISTANZA: 'statoRiscossioneScadutaIstanzaInvalid',
  /** Costante che indica la chiave per l'errore di validazione sullo stato di una riscossione attiva*/
  STATO_RISCOSSIONE_ATTIVA: 'statoRiscossioneAttivaInvalid',
  /** Costante che indica la chiave per l'errore di validazione sulla quantità di componenti. */
  QUANTITA_COMPONENTI_GRUPPO: 'quantitaComponentiInvalid',
  /** Costante che indica la chiave per l'errore di validazione sulla quantità di componenti. */
  QUANTITA_PROVVEDIMENTI: 'quantitaProvvedimentiInvalid',
  /** Costante che indica la chiave per l'errore di validazione per la mancanza di un capogruppo. */
  PRESENZA_CAPOGRUPPO: 'presenzaCapogruppoInvalid',
  /** Costante che indica la chiave per l'errore di validazione per una data scadenza emas iso invalida per Riduzione R5. */
  DATA_SCADENZA_EMAS_ISO_R5: 'dataScadenzaEmasIsoR5',
  /** Costante che indica la chiave per l'errore di validazione per un anno bollettazione ordinaria prima dell'anno di scadenza pagamento. */
  ANNO_PRIMA_ANNO_SCAD_PAG: 'annoBOPrecedenteAnnoScadenzaPagamentoInvalid',
  /** Costante che indica la chiave per l'errore di validazione data protocollo dopo la data scandenza pagamento. */
  DATA_PROT_DOPO_DATA_SCAD_PAG:
    'dataProtocolloDopoDataScadenzaPagamenteInvalid',
  /** Costante che indica la chiave per l'errore di validazione quando una data d'inizio è temporalmente sbagliata rispetto a quella di fine. */
  DATE_START_END_INVALID: 'dataInizioEDataFineInvalid',
  /** Costante che indica la chiave per l'errore di validazione della descrizione dell'indirizzo di spedizione in presenza di un messaggio. */
  DESC_BY_MESS_INVALID: 'descrizioneSeMessaggioInvalid',
  /** Costante che indica la chiave per l'errore di validazione quando il campo ha una lunghezza errata, rispetto a quello richiesto. */
  LUNGHEZZA_CAMPO_ESATTA: 'lunghezzaCampoEsattaInvalid',
  /** Costante che indica la chiave per l'errore di validazione quando il campo ha una lunghezza errata e risulta minore rispetto a quello richiesto. */
  LUNGHEZZA_CAMPO_MINORE: 'lunghezzaCampoMinoreInvalid',
  /** Costante che indica la chiave per l'errore di validazione quando il campo ha una lunghezza errata e risulta maggiore rispetto a quello richiesto. */
  LUNGHEZZA_CAMPO_MAGGIORE: 'lunghezzaCampoMaggioreInvalid',
  /** Costante che indica la chiave per il recupero dell'errore sul form: usi delle annualità in stati debitori . */
  DATA_INIZIO_REQUIRED: 'dataInizioRequired',

  /** Costante che indica la chiave per l'errore di validazione quando lo stato debitorio può essere salvato solo se almeno un’annualità è associata. Quindi: COUNT StatoDebitorio.annualita_sd > 0. */
  STATO_DEBITORIO_E_ANNUALITA: 'statoDebitorioEAnnualita',
  /** Costante che indica la chiave per l'errore di validazione quando tutte le annualità dello stato debitorio devono avere almeno un uso, con i relativi dati, associato. Quindi: COUNT StatoDebitorio.annualita_uso_sd > 0. */
  ANNUALITA_E_USI: 'annualitaEUsi',
  /** Costante che indica la chiave per l'errore di validazione quando il periodo pagamento deve essere definito all’interno dell’uso. Quindi: StatoDebitorio.desc_periodo_pagamento != NULL. */
  PERIODO_PAGAMENTO: 'periodoPagamento',
  /** Costante che indica la chiave per l'errore di validazione quando la data di scadenza del pagamento deve essere valorizzata. */
  DATA_SCADENZA_PAGAMENTO: 'dataScadenzaPagamento',
  /** Costante che indica la chiave per l'errore di validazione quando il flag annullato è attivo, la motivazione deve essere valorizzata. Quindi: StatoDebitorio.flg_annullato = 1 (true) && StatoDebitorio.desc_motivo_annullo != (NULL OR ‘’) */
  FLAG_ANNULLATO_E_MOTIVAZIONE: 'flagAnnullatoEMotivazione',
  /** Costante che indica la chiave per l'errore di validazione quando il flag invio speciale è attivo, non ci devono essere annualità con un stesso anno. Quindi: StatoDebitorio.flg_invio_speciale == 1 (true) && UNIQUE StatoDebitorio.annualita_sd.anno. */
  VERIFICA_ANNUALITA_DOPPIA: 'verificaAnnualitaDoppia',
  /** Costante che indica la chiave per l'errore di validazione quando la proprietà dell’oggetto stato debitorio: imp_compensazione_canone; è valorizzata, la modifica dello stato debitorio non è permessa. Quindi: StatoDebitorio.imp_compensazione_canone == (NULL OR 0). */
  REGOLE_PER_MODIFICA: 'regolePerModifica',
  /** Costante che indica la chiave per l'errore di validazione quando all’interno dello stato debitorio, esistono rimborsi “da compensare” o “compensati” (id_tipo_rimborso = 2 o id_tipo_rimborso = 3), non è possibile modificare lo stato debitorio. Quindi: StatoDebitorio.Rimborso<list>.tipo_rimborso.id_tipo_rimborso != (2 OR 3). */
  RIMBORSI_DA_COMPENSARE_O_COMPENSATI: 'rimborsiDaCompensareOCompensati',
  /** Costante che indica la chiave per l'errore di validazione quando si modifica lo stato debitorio, l'importo compensazione canone non deve essere valorizzato. */
  IMPORTO_COMPENSAZIONE_CANONE: 'importoCompensazioneCanone',
  /** Costante che indica la chiave per l'errore di validazione quando si assegna un importo da assegnare superiore all'importo versato nella pagina dei versamenti */
  IMPORTO_DA_ASSEGNARE_SUPERIORE_A_IMPORTO_VERSATO:
    'importoDaAssegnareSuperioreAImportoDovuto',

  /** Costante d'errore per data inizio/fine invalida per la ricerca avanzata e i campi: SCADENZA_CONCESSIONE. */
  DATA_INI_FIN_SCADENZA_CONCESSIONE: 'dataInizioFineScadenzaConcessione',
  /** Costante d'errore per data inizio/fine invalida per la ricerca avanzata e i campi: DATA_RINUNCIA_REVOCA. */
  DATA_INI_FIN_DATA_RINUNCIA_REVOCA: 'dataInizioFineDataRinunciaRevoca',
  /** Costante d'errore per data inizio/fine invalida per la ricerca avanzata e i campi: DATA_ISTANZA. */
  DATA_INI_FIN_DATA_ISTANZA: 'dataInizioFineDataIstanza',
  /** Costante d'errore per data inizio/fine invalida per la ricerca avanzata e i campi: DATA_TITOLO. */
  DATA_INI_FIN_DATA_TITOLO: 'dataInizioFineDataTitolo',

  /** Costante d'errore per gestione del canone uso e del canone unitario uso. */
  CANONE_E_CANONE_UNITARIO_REQUIRED: 'canoneCanoneUnitarioRequired',
  /** Costante d'errore per gestione dell'errore dovuto ad usi di legge senza dati per i canoni. */
  USI_INVALIDI_CANONE: 'usiInvalidiCanone',

  /** Costante d'errore per la gestione dell'errore dovuto all'importo versato maggiore del canone dovuto. */
  IMP_VERSATO_MAGG_IMP_MANCANTE_CON_INTERESSI: 'importoVersatoMaggioreCanoneDovuto',
  /** Costante d'errore per la gestione dell'errore dovuto all'importo versato maggiore dell'importo pagamento. */
  IMPORTO_VERSATO_MAGG_IMPORTO_PAGAMENTO:
    'importoVersatoMaggioreImportoPagamento',

  /** Costante che indica la chiave per l'errore di validazione quando una data d'inizio è temporalmente sbagliata rispetto a quella di fine per la sezione di export dati. */
  DATE_START_AFTER_END_EXPORT_DATA: 'dateStartAfterEndExportData',
  /** Costante che indica la chiave per l'errore di validazione quando la data fine per form esporta dati NON E' nello stesso anno della data inizio. */
  DATE_END_EXPORT_DATA_SAME_YEAR: 'dataFineEsportaDatiStessoAnnoDataInizio',

  /** Costante che indica la chiave per l'errore di importo non proprio 0 o minore di 0. */
  IMPORTO_NON_PROPRIO_ZERO: 'importoNonProprioMinoreDi',

  /** Costante che indica la chiave per l'errore per nessun campo valorizzato per ricerca pagamenti. */
  ALMENO_UN_CAMPO_RICERCA_PAGAMENTI_VALORIZZATO:
    'almenoUnCampoRicercaPagamentiValorizzato',

  /** Costante che indica la chiave per l'errore quando un numero non è all'interno di un range di valori. */
  NUMBER_NOT_IN_RANGE: 'numberNotInrange',

  /** Costante che indica la chiave per l'errore quando i campi risultano obbligatori. */
  CAMPI_OBBLIGATORI: 'campiOblligatoriError',
  
  /** Costante che indica la chiave per l'errore quando il "Valore1" dei range canoni è maggiore del "Valore2". */
  RANGE_VALORE1_GREAT_VALORE2: 'rangeValore1MaggValore2',
};

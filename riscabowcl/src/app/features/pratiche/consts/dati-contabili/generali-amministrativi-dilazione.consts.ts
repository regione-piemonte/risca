/**
 * Interfaccia che definisce le proprietà dell'oggetto di costanti per il componente: generali amministrativi dilazione.
 */
interface IGeneraliAmministrativiDilazioneConsts {
  /**
   * ##########
   * CAMPI FORM
   * ##########
   */

  /** ######### DATI GENERALI SINTESI ######### */
  /** Costante che indica il campo form. */
  CODICE_UTENZA: string;
  /** Costante che indica il campo form. */
  DATA_ULTIMA_MODIFICA: string;
  /** Costante che indica il campo form. */
  NUM_RICHIESTA_PROTOCOLLO: string;
  /** Costante che indica il campo form. */
  DATA_RICHIESTA_PROTOCOLLO: string;
  /** Costante che indica il campo form. */
  PERIODO_PAGAMENTO: string;
  /** Costante che indica il campo form. */
  SCADENZA_PAGAMENTO: string;
  /** Costante che indica il campo form. */
  CODICE_AVVISO: string;
  /** Costante che indica il campo form. */
  STATO: string;
  /** Costante che indica il campo form. */
  RESTITUITO_MITTENTE: string;
  /** Costante che indica il campo form. */
  INVIO_SPECIALE_POSTEL: string;
  /** Costante che indica il campo form. */
  ANNULLATO: string;
  /** Costante che indica il campo form. */
  MOTIVAZIONE: string;

  /** ######### RECUPERO CANONE ######### */
  /** Costante che indica il campo form. */
  CANONE_ANNUALITA_CORRENTE: string;
  /** Costante che indica il campo form. */
  ANNUALITA_PRECEDENTE: string;
  /** Costante che indica il campo form. */
  CANONE_DOVUTO: string;
  /** Costante che indica il campo form. */
  ADDEBITO_ANNO_SUCCESSIVO: string;
  /** Costante che indica il campo form. */
  INTERESSI_MATURATI: string;
  /** Costante che indica il campo form. */
  SPESE_NOTIFICA: string;
  /** Costante che indica il campo form. */
  IMPORTO_COMPENSAZIONE: string;
  /** Costante che indica il campo form. */
  DILAZIONE: string;

  /** ######### DATI AMMINISTRATIVI E TECNICI ######### */
  /** Costante che indica il campo form. */
  TIPO_TITOLO: string;
  /** Costante che indica il campo form. */
  NUMERO_TITOLO: string;
  /** Costante che indica il campo form. */
  DATA_TITOLO: string;
  /** Costante che indica il campo form. */
  INIZIO_CONCESSIONE: string;
  /** Costante che indica il campo form. */
  SCADENZA_CONCESSIONE: string;
  /** Costante che indica il campo form. */
  ISTANZA_RINNOVO: string;
  /** Costante che indica il campo form. */
  NOTE_SD: string;
  /** Costante che indica il pulsante del form. */
  UTENZE_COMPENSAZIONE: string;

  /**
   * ###############
   * LABEL ACCORDION
   * ###############
   */

  ACCORDION_DATI_GENERALI_SINTESI: string;
  ACCORDION_AMMINISTRATIVI_TECNICI: string;

  /**
   * ################
   * LABEL CAMPI FORM
   * ################
   */

  /** ######### DATI GENERALI SINTESI ######### */
  /** Costante che indica la label il campo form. */
  LABEL_CODICE_UTENZA: string;
  /** Costante che indica la label il campo form. */
  LABEL_DATA_ULTIMA_MODIFICA: string;
  /** Costante che indica la label il campo form. */
  LABEL_NUM_RICHIESTA_PROTOCOLLO: string;
  /** Costante che indica la label il campo form. */
  LABEL_DATA_RICHIESTA_PROTOCOLLO: string;
  /** Costante che indica la label il campo form. */
  LABEL_PERIODO_PAGAMENTO: string;
  /** Costante che indica la label il campo form. */
  LABEL_SCADENZA_PAGAMENTO: string;
  /** Costante che indica la label il campo form. */
  LABEL_CODICE_AVVISO: string;
  /** Costante che indica la label il campo form. */
  LABEL_STATO: string;
  /** Costante che indica la label il campo form. */
  LABEL_RESTITUITO_MITTENTE: string;
  /** Costante che indica la label il campo form. */
  LABEL_INVIO_SPECIALE_POSTEL: string;
  /** Costante che indica la label il campo form. */
  LABEL_ANNULLATO: string;
  /** Costante che indica la label il campo form. */
  LABEL_MOTIVAZIONE: string;

  /** ######### RECUPERO CANONE ######### */
  /** Costante che indica la label il campo form. */
  LABEL_CANONE_ANNUALITA_CORRENTE: string;
  /** Costante che indica la label il campo form. */
  LABEL_ANNUALITA_PRECEDENTE: string;
  /** Costante che indica la label il campo form. */
  LABEL_CANONE_DOVUTO: string;
  /** Costante che indica la label il campo form. */
  LABEL_ADDEBITO_ANNO_SUCCESSIVO: string;
  /** Costante che indica la label il campo form. */
  LABEL_INTERESSI_MATURATI: string;
  /** Costante che indica la label il campo form. */
  LABEL_SPESE_NOTIFICA: string;
  /** Costante che indica la label il campo form. */
  LABEL_IMPORTO_COMPENSAZIONE: string;
  /** Costante che indica la label il campo form. */
  LABEL_DILAZIONE: string;
  /** Costante che indica la label il campo form. */
  LABEL_DETTAGLIO_DILAZIONE: string;

  /** ######### DATI AMMINISTRATIVI E TECNICI ######### */
  /** Costante che indica la label il campo form. */
  LABEL_TIPO_TITOLO: string;
  /** Costante che indica la label il campo form. */
  LABEL_NUMERO_TITOLO: string;
  /** Costante che indica la label il campo form. */
  LABEL_DATA_TITOLO: string;
  /** Costante che indica la label il campo form. */
  LABEL_INIZIO_CONCESSIONE: string;
  /** Costante che indica la label il campo form. */
  LABEL_SCADENZA_CONCESSIONE: string;
  /** Costante che indica la label il campo form. */
  LABEL_ISTANZA_RINNOVO: string;
  /** Costante che indica la label il campo form. */
  LABEL_NOTE_SD: string;
  /** Costante che indica la label il campo form. */
  LABEL_UTENZE_COMPENSAZIONE: string;

  /**
   * ############################
   * PROPRIETA' SELECT CAMPI FORM
   * ############################
   */
  /** Costante che indica il nome della prorpietà da visualizzare per il campo del form. */
  PROPERTY_TIPO_TITOLO: string;
  PROPERTY_TIPO_ISTANZA: string;

  /**
   * #############################
   * CHIAVI PER COMPONENTE TECNICO
   * #############################
   */
  FORM_KEY_PARENT: string;
  FORM_KEY_CHILD_DT: string;
}

/**
 * Oggetto contenente una serie di costanti per il componente: generali amministrativi dilazione.
 */
export const GeneraliAmministrativiDilazioneConsts: IGeneraliAmministrativiDilazioneConsts =
  {
    /** ######### DATI GENERALI SINTESI ######### */
    /** Costante che indica il campo form. */
    CODICE_UTENZA: 'codiceUtenza',
    /** Costante che indica il campo form. */
    DATA_ULTIMA_MODIFICA: 'dataUltimaModifica',
    /** Costante che indica il campo form. */
    NUM_RICHIESTA_PROTOCOLLO: 'numRichiestaProtocollo',
    /** Costante che indica il campo form. */
    DATA_RICHIESTA_PROTOCOLLO: 'dataRichiestaProtocollo',
    /** Costante che indica il campo form. */
    RESTITUITO_MITTENTE: 'restituitoMittente',
    /** Costante che indica il campo form. */
    PERIODO_PAGAMENTO: 'periodoPagamento',
    /** Costante che indica il campo form. */
    SCADENZA_PAGAMENTO: 'scadenzaPagamento',
    /** Costante che indica il campo form. */
    CODICE_AVVISO: 'codiceAvviso',
    /** Costante che indica il campo form. */
    STATO: 'stato',
    /** Costante che indica il campo form. */
    INVIO_SPECIALE_POSTEL: 'invioSpecialePostel',
    /** Costante che indica il campo form. */
    ANNULLATO: 'annullato',
    /** Costante che indica il campo form. */
    MOTIVAZIONE: 'motivazione',

    /** ######### RECUPERO CANONE ######### */
    /** Costante che indica il campo form. */
    CANONE_ANNUALITA_CORRENTE: 'canoneAnnualitaCorrente',
    /** Costante che indica il campo form. */
    ANNUALITA_PRECEDENTE: 'annualitaPrecedente',
    /** Costante che indica il campo form. */
    CANONE_DOVUTO: 'canoneDovuto',
    /** Costante che indica il campo form. */
    ADDEBITO_ANNO_SUCCESSIVO: 'addebitoAnnoSuccessivo',
    /** Costante che indica il campo form. */
    INTERESSI_MATURATI: 'interessiMaturati',
    /** Costante che indica il campo form. */
    SPESE_NOTIFICA: 'speseNotifica',
    /** Costante che indica il campo form. */
    IMPORTO_COMPENSAZIONE: 'importoCompensazione',
    /** Costante che indica il campo form. */
    DILAZIONE: 'dilazione',

    /** ######### DATI AMMINISTRATIVI E TECNICI ######### */
    /** Costante che indica il campo form. */
    TIPO_TITOLO: 'tipoTitolo',
    /** Costante che indica il campo form. */
    NUMERO_TITOLO: 'numeroTitolo',
    /** Costante che indica il campo form. */
    DATA_TITOLO: 'dataTitolo',
    /** Costante che indica il campo form. */
    INIZIO_CONCESSIONE: 'inizioConcessione',
    /** Costante che indica il campo form. */
    SCADENZA_CONCESSIONE: 'scadenzaConcessione',
    /** Costante che indica il campo form. */
    ISTANZA_RINNOVO: 'istanzaRinnovo',
    /** Costante che indica il campo form. */
    NOTE_SD: 'noteSD',
    /** Costante che indica il campo form. */
    UTENZE_COMPENSAZIONE: 'utenzeCompensazione',

    /**
     * ###############
     * LABEL ACCORDION
     * ###############
     */

    ACCORDION_DATI_GENERALI_SINTESI: 'Dati generali e di sintesi',
    ACCORDION_AMMINISTRATIVI_TECNICI: 'Dati amministrativi e tecnici',

    /**
     * ################
     * LABEL CAMPI FORM
     * ################
     */

    /** ######### DATI GENERALI SINTESI ######### */
    /** Costante che indica la label il campo form. */
    LABEL_CODICE_UTENZA: 'Codice utenza',
    /** Costante che indica la label il campo form. */
    LABEL_DATA_ULTIMA_MODIFICA: 'Data ultima modifica',
    /** Costante che indica la label il campo form. */
    LABEL_NUM_RICHIESTA_PROTOCOLLO: 'Rich. prot. N°',
    /** Costante che indica la label il campo form. */
    LABEL_DATA_RICHIESTA_PROTOCOLLO: 'Data',
    /** Costante che indica la label il campo form. */
    LABEL_PERIODO_PAGAMENTO: 'Periodo di pagamento',
    /** Costante che indica la label il campo form. */
    LABEL_SCADENZA_PAGAMENTO: 'Scadenza pagamento',
    /** Costante che indica la label il campo form. */
    LABEL_CODICE_AVVISO: 'Codice avviso',
    /** Costante che indica la label il campo form. */
    LABEL_STATO: 'Stato',
    /** Costante che indica la label il campo form. */
    LABEL_RESTITUITO_MITTENTE: 'Restituito al mittente',
    /** Costante che indica la label il campo form. */
    LABEL_INVIO_SPECIALE_POSTEL: 'Invio richiesta speciale',
    /** Costante che indica la label il campo form. */
    LABEL_ANNULLATO: 'Annullato',
    /** Costante che indica la label il campo form. */
    LABEL_MOTIVAZIONE: 'Motivazione',

    /** ######### RECUPERO CANONE ######### */
    /** Costante che indica la label il campo form. */
    LABEL_CANONE_ANNUALITA_CORRENTE: 'Canone annualità corrente',
    /** Costante che indica la label il campo form. */
    LABEL_ANNUALITA_PRECEDENTE: 'Annualità precedente €',
    /** Costante che indica la label il campo form. */
    LABEL_CANONE_DOVUTO: 'Canone dovuto',
    /** Costante che indica la label il campo form. */
    LABEL_ADDEBITO_ANNO_SUCCESSIVO: 'Addebito anno successivo',
    /** Costante che indica la label il campo form. */
    LABEL_INTERESSI_MATURATI: 'Interessi maturati',
    /** Costante che indica la label il campo form. */
    LABEL_SPESE_NOTIFICA: 'Spese di notifica',
    /** Costante che indica la label il campo form. */
    LABEL_IMPORTO_COMPENSAZIONE: 'Importo da compensazione',
    /** Costante che indica la label il campo form. */
    LABEL_DILAZIONE: 'Dilazione',
    /** Costante che indica il pulsante del form. */
    LABEL_DETTAGLIO_DILAZIONE: 'Dettaglio dilazione',

    /** ######### DATI AMMINISTRATIVI E TECNICI ######### */
    /** Costante che indica la label il campo form. */
    LABEL_TIPO_TITOLO: 'Tipo titolo',
    /** Costante che indica la label il campo form. */
    LABEL_NUMERO_TITOLO: 'Numero titolo',
    /** Costante che indica la label il campo form. */
    LABEL_DATA_TITOLO: 'Data titolo',
    /** Costante che indica la label il campo form. */
    LABEL_INIZIO_CONCESSIONE: 'Inizio concessione',
    /** Costante che indica la label il campo form. */
    LABEL_SCADENZA_CONCESSIONE: 'Scadenza concessione',
    /** Costante che indica la label il campo form. */
    LABEL_ISTANZA_RINNOVO: 'Istanza di rinnovo',
    /** Costante che indica la label il campo form. */
    LABEL_NOTE_SD: 'Note (max 500 caratteri)',
    /** Costante che indica la label il campo form. */
    LABEL_UTENZE_COMPENSAZIONE: 'Utenze da compensazione',

    /**
     * ############################
     * PROPRIETA' SELECT CAMPI FORM
     * ############################
     */
    /** Costante che inidica il nome della proprietà per la select: tipoTitolo. */
    PROPERTY_TIPO_TITOLO: 'des_tipo_titolo',
    /** Costante che inidica il nome della proprietà per la select: tipologiaIstanza. */
    PROPERTY_TIPO_ISTANZA: 'des_tipo_provvedimento',

    /**
     * #############################
     * CHIAVI PER COMPONENTE TECNICO
     * #############################
     */
    FORM_KEY_PARENT: '',
    FORM_KEY_CHILD_DT: '',
  };
